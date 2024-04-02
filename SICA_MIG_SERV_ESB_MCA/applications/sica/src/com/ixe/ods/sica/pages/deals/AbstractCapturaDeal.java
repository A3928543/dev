/*
 * $Id: AbstractCapturaDeal.java,v 1.30.2.2.6.6.14.1.14.1.14.2.10.2.6.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2013 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.bup.model.ClaveReferenciaPersona;
import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Keys;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaValidationException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Actividad;
import com.ixe.ods.sica.model.Cliente;
import com.ixe.ods.sica.model.CodigoPostalListaBlanca;
import com.ixe.ods.sica.model.Contrato;
import com.ixe.ods.sica.model.CuentaAltamira;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.LimiteEfectivo;
import com.ixe.ods.sica.model.LimitesRestriccionOperacion;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PersonaListaBlanca;
import com.ixe.ods.sica.model.PlantillaCuentaIxe;
import com.ixe.ods.sica.sdo.LineaCambioServiceData;
import com.ixe.ods.sica.services.ContratoDireccionesService;
import com.ixe.ods.sica.services.PersonaService;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.legosoft.hibernate.type.SiNoType;

/**
 * Superclase de las p&aacute;ginas de Captura de Deals normales e interbancarios, provee la
 * funcionalidad com&uacute;n tal como el procesamiento del pago anticipado y la cancelaci&oacute;n
 * de Deals.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.30.2.2.6.6.14.1.14.1.14.2.10.2.6.1 $ $Date: 2020/12/01 04:53:01 $
 */
public abstract class AbstractCapturaDeal extends AbstractComprobanteDeal {

	/**
     * Regresa el detalle que tiene el idDealPosicion especificado.
     *
     * @param idDealPosicion el idDealPosicion a buscar.
     * @return DealDetalle.
     */
    protected DealDetalle findDetalle(int idDealPosicion) {
        List dets = getDeal().getDetalles();
        for (Iterator it = dets.iterator(); it.hasNext();) {
            DealDetalle d = (DealDetalle) it.next();
            if (d.getIdDealPosicion() == idDealPosicion) {
                return d;
            }
        }
        return null;
    }

    /**
     * Recorre los detalles no cancelados del deal. Si encuentra un mnem&oacute;nico que termina en
     * 'REF', calcula la clave de referencia del cliente y la asigna al registro del deal.
     *
     * @param deal El deal a revisar.
     */
    protected void asignarClaveDeReferencia(Deal deal) {
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle dealDetalle = (DealDetalle) it.next();

            if (!dealDetalle.isCancelado() && dealDetalle.getMnemonico() != null &&
                    dealDetalle.getMnemonico().endsWith("REF")) {
                String idClaveReferencia = "";
                Integer idPersona = deal.getCliente().getIdPersona();
                List clavesReferencia = getBupServiceData().findClavesReferenciaPersona(
                        idPersona.intValue());
                List clavesMostradas = new ArrayList();
                if (!clavesReferencia.isEmpty()) {
                    int primerIdPersona = ((ClaveReferenciaPersona) clavesReferencia
                            .get(0)).getIdPersona();
                    for (Iterator it2 = clavesReferencia.iterator(); it2.hasNext();) {
                        ClaveReferenciaPersona crp = (ClaveReferenciaPersona)
                                it2.next();
                        if (crp.getIdPersona() == primerIdPersona) {
                            clavesMostradas.add(crp);
                        }
                    }
                    idClaveReferencia = ((ClaveReferenciaPersona) clavesMostradas.
                            get(0)).getClaveReferencia();
                }
                deal.setClaveReferencia(idClaveReferencia);
                getSicaServiceData().update(deal);
                break;
            }
        }
    }
    
    /**
     * La asignacion del CR (Sucursal de origen) al deal considera los siguientes criterios:
     * 1._ Si el deal tiene detalles (recibimos) de cargo a cuenta con alguno de los mnemonicos siguientes ('RUSDCARGIXEB','RMXNCARGIXEB')
     *     Se asigna el cr de la primer chequera que se encuentre.
     * 2._ Si el deal a reportar NO incluye 'Cargo a Cuenta' como forma de liquidacion,  se le asocia el CR registrado en SC_CLIENTE.
     * 3._ Para cualquier otro caso, se asociara a la operacion un CR generico,  el cual sera configurable dentro de los parametros de SICA.
     * @param deal
     */
    protected void asignarCrADeal(Deal deal)
    {
    	String mnemonico = null, noCuentaContratoSica = null; // status = null;
    	String status = null, LIQ = "TT", PROCESADO = "CO", ALTA = "AL";
    	DealDetalle dealDetalle = null;
    	List cuentaAltamiraList = null;
        CuentaAltamira cuentaAltamira = null;
        Iterator it = null;
        PlantillaCuentaIxe plantilla = null;
        Integer cr = null, idPersona = null;
        
        _logger.debug("--> AbstractCapturaDeal.asignarCrADeal(): inicia el proceso de asiganacion de cr al deal ");
        
        _logger.debug("--> AbstractCapturaDeal.asignarCrADeal(): " +
        		      "1._ Si el deal tiene detalles (recibimos) de cargo a cuenta con alguno de los mnemonicos \n" +
        		      "    siguientes ('RUSDCARGIXEB','RMXNCARGIXEB')\n " +
        		      "    Se asigna el cr de la primer chequera que se encuentre. ");
        /* 1._ Si el deal tiene detalles (recibimos) de cargo a cuenta con alguno de los mnemonicos siguientes ('RUSDCARGIXEB','RMXNCARGIXEB')
               Se asigna el cr de la primer chequera que se encuentre. */
    	for (it = deal.getDetalles().iterator(); it.hasNext();)
    	{
    		dealDetalle = (DealDetalle) it.next();
    		status = dealDetalle.getStatusDetalleDeal();
    		if(PROCESADO.equals(status) || ALTA.equals(status) || (LIQ.equals(status) && dealDetalle.getReversado() == 0))
    		{
    			mnemonico = dealDetalle.getMnemonico();
        		if(dealDetalle.isRecibimos() && ("RUSDCARGIXEB".equals(mnemonico) || "RMXNCARGIXEB".equals(mnemonico)))
        		{
        			_logger.debug("--> AbstractCapturaDeal.asignarCrADeal(): se encontro mnemonico " + mnemonico + 
        					      ", detalle: " + dealDetalle.getFolioDetalle());
        			try
        			{
        				plantilla = ((PlantillaCuentaIxe)dealDetalle.getPlantilla());
            			cuentaAltamiraList = getSicaServiceData().findCuentaAltamiraByNoCuenta(plantilla.getNoCuentaIxe());
            			_logger.debug("--> AbstractCapturaDeal.asignarCrADeal(): plantilla.getNoCuentaIxe(): " + plantilla.getNoCuentaIxe());
            			_logger.debug("--> AbstractCapturaDeal.asignarCrADeal(): cuentaAltamiraList: " + cuentaAltamiraList);
            			if(cuentaAltamiraList != null && !cuentaAltamiraList.isEmpty())
                		{
                			cuentaAltamira = (CuentaAltamira)cuentaAltamiraList.get(0);
                			if(cuentaAltamira.getCr() != null)
                			{
                				cr = new Integer(cuentaAltamira.getCr());
                    			_logger.debug("--> AbstractCapturaDeal.asignarCrADeal(), 1._ cr: " + cr.intValue() + ", deal: " + deal.getIdDeal());
                			}
                			
                			break;
                		}
        			}
        			catch(Exception e)
        			{
        				e.printStackTrace();
        			}
        		}
    		}
    	}
    	
    	/* 2._ Si el deal a reportar NO incluye 'Cargo a Cuenta' como forma de liquidacion,  
    	       se le asocia el CR registrado en SC_CLIENTE.*/
    	if(cr == null)
    	{
    		_logger.debug("2._ Si el deal a reportar NO incluye 'Cargo a Cuenta' como \n" +
    				      "forma de liquidacion, se le asocia el CR registrado en SC_CLIENTE.");
    		noCuentaContratoSica = deal.getContratoSica().getNoCuenta();
    		List contratos=getSicaServiceData().findContratosByCuenta(noCuentaContratoSica);
    		_logger.debug("contratos: " + contratos);
        	Contrato contratoCorto=null;
        	for (Iterator i = contratos.iterator(); i.hasNext();) 
        	{
        		contratoCorto = (Contrato) i.next();
        		if(contratoCorto != null)
            	{
        			idPersona = deal.getCliente().getIdPersona();
        			_logger.debug("idPersona: " + idPersona + ", contratoCorto.getIdCliente(): " + contratoCorto.getIdCliente());
        			List clientes = getSicaServiceData().findClienteByIdPersonaAndIdCliente(idPersona, contratoCorto.getIdCliente());
        			_logger.debug("clientes: " + clientes);
        			if(clientes != null && !clientes.isEmpty())
        	    	{
        				if(((Cliente)clientes.get(0)).getSucursalOrigen() != null)
        				{
        					cr = new Integer(((Cliente)clientes.get(0)).getSucursalOrigen());
            				_logger.debug("2._ cr: " + cr.intValue() + ", deal: " + deal.getIdDeal());
        				}
        				
        				break;
        	    	}
            	}
        	}
        	
    	}
    	
    	/* 3._ Para cualquier otro caso, se asociara a la operacion un CR generico,  
	           el cual sera configurable dentro de los parametros de SICA.*/
    	if(cr == null) 
    	{
    		_logger.debug("3._ Para cualquier otro caso, se asociara a la operacion un CR generico, \n" + 
	                      "    el cual sera configurable dentro de los parametros de SICA.");
    		ParametroSica crGenerico = getSicaServiceData().findParametro(ParametroSica.CR_GENERICO);
    		if(crGenerico != null && StringUtils.isNotEmpty(crGenerico.getValor()))
    			cr = new Integer(crGenerico.getValor());
    		
    		_logger.debug("3._ cr generico: " + cr + ", deal: " + deal.getIdDeal());
    	}
    	
    	if(cr != null)
    	{
    		deal.setCr(cr);
    		getSicaServiceData().update(deal);
    	}
    }
    
    /**
     * Regresa los l&iacute;mites de restricci&oacute;n de operaci&oacute;n del contrato sica
     * que se encuentra operando.
     *
     * @param deal El deal a operar.
     * @return LimitesRestriccionOperacion.
     */
    protected LimitesRestriccionOperacion getLimitesActualizados(Deal deal) {
    	String noCuenta = deal.getContratoSica() != null ? deal.getContratoSica().getNoCuenta() :
    		null;
    	if (noCuenta == null) {
    		return null;
    	}
    	else if (getLimitesRestriccionOperacion() == null ||
    			!getLimitesRestriccionOperacion().getNoCuenta().equals(noCuenta)) {
    		//ComplementoDatos complDatos = getSicaServiceData().
    				//findComplementoDatosByNoCuenta(noCuenta);
    		LimitesRestriccionOperacion limRestOper = new LimitesRestriccionOperacion();
    		if (deal.getCliente().getPais() == null) {
				throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
    					"falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
    					"Contrataci\u00f3n.\n" + "No se puede identificar la nacionalidad del" +
    					" cliente.");
			}
    		
   		limRestOper.setCliente(getSicaServiceData().isClienteOrUsuario(deal.
    				getCliente().getIdPersona()) ? SiNoType.TRUE : SiNoType.FALSE);

   		//limRestOper.setCliente(SiNoType.TRUE);
    				
    		limRestOper.setNacionalidad(deal.getCliente().getPais().getIdPais());
    		limRestOper.setNoCuenta(noCuenta);
    		
    		PersonaListaBlanca plb = getSicaServiceData().findPersonaListaBlanca(noCuenta);
    			
    		ContratoDireccionesService cds = (com.ixe.ods.sica.services.ContratoDireccionesService)
    				getApplicationContext().getBean("contratoDireccionesService");
    		if (deal.getCliente().getTipoPersona().equals("PM")) {
    					
    			if(limRestOper.getCliente().equals(SiNoType.FALSE)){

        			limRestOper.setListaLimitesNormales(getSicaServiceData().
        						findLimitesRestriccionOperacion(deal.getCliente().getTipoPersona(),
        								new Boolean(limRestOper.getCliente().equals(SiNoType.TRUE)),
        								null,
        								new Boolean(limRestOper.getNacionalidad().equals(String.
        										valueOf(Num.I_48)))));
        		    deal.setTipoZona(null);
    			} 
    			//Es Cliente
    			else{ 

    			Direccion dirFiscalPersona = cds.getDireccionFiscalParaPersona(deal.getCliente().
    					getIdPersona().intValue());
    			if (dirFiscalPersona == null) {
    				throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
    						"falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
    						"Contrataci\u00f3n.\n" + "No se puede identificar la direcci\u00f3n " +
    						"fiscal del cliente.");
    			}
    			String codigoPostalCliente = dirFiscalPersona.getCodigoPostal();
    			
    			
    			if (codigoPostalCliente == null || codigoPostalCliente.equals("")) {
    				throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
    						"falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
    						"Contrataci\u00f3n.\n" + "El cliente no cuenta con un c\u00f3digo postal" +
    				" asociado.");
    			}
    			String codigoPostalSucursal = "";
    			if (!(deal.getCanalMesa().getCanal().getSucursal() != null && deal.getCanalMesa().
    					getCanal().getSucursal().getAddress() != null && (deal.getCanalMesa().
    							getCanal().getSucursal().getAddress().getCodigoPostal() != null ||
    							!deal.getCanalMesa().getCanal().getSucursal().getAddress().
    							getCodigoPostal().equals("")))) {
    				codigoPostalSucursal = deal.getCanalMesa().getCanal().getCodigoPostal();
    			}
    			else {
    				codigoPostalSucursal = deal.getCanalMesa().getCanal().getSucursal().getAddress().
    				getCodigoPostal();
    			}
    			if (codigoPostalSucursal == null || codigoPostalSucursal.equals("")) {
    				throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
    						"falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
    						"Contrataci\u00f3n.\n" + "La sucursal no cuenta con un c\u00f3digo postal" +
    				" asociado.");
    			}
    			CodigoPostalListaBlanca codPostLBCliente = getSicaServiceData().
    			findCodigoPostalListaBlanca(codigoPostalCliente, true);
    			CodigoPostalListaBlanca codPostLBSucursal = getSicaServiceData().
    			findCodigoPostalListaBlanca(codigoPostalSucursal, false);
    			boolean zonaTuristica = false;
    			boolean zonaFronteriza = false;
    			if (codPostLBCliente != null && codPostLBSucursal != null &&
    					!codPostLBCliente.equals("") && !codPostLBSucursal.equals("")) {
    				zonaTuristica = codPostLBCliente.getZonaTuristica().booleanValue() &&
    				codPostLBSucursal.getZonaTuristica().booleanValue();
    				zonaFronteriza = codPostLBCliente.getZonaFronteriza().booleanValue() &&
    				codPostLBSucursal.getZonaFronteriza().booleanValue();
    			}
    			// Se obtiene la lista para las zonas que aplique
    			String zona = Constantes.ZONA_NORMAL;
    			if (!zonaTuristica && ! zonaFronteriza) {
    				// Zona Normal
    				limRestOper.setListaLimitesNormales(getSicaServiceData().
    						findLimitesRestriccionOperacion(deal.getCliente().getTipoPersona(),
    								new Boolean(limRestOper.getCliente().equals(SiNoType.TRUE)),
    								Constantes.ZONA_NORMAL,
    								new Boolean(limRestOper.getNacionalidad().equals(String.
    										valueOf(Num.I_48)))));
    			} else {
    				List listaZonaTuristica = null;
    				List listaZonaFronteriza = null;
    				if (zonaTuristica) {
    					// Zona Turistica
    					listaZonaTuristica =
    						getSicaServiceData().
    						findLimitesRestriccionOperacion(
    								deal.getCliente().getTipoPersona(),
    								new Boolean(limRestOper.getCliente().equals(SiNoType.TRUE)),
    								Constantes.ZONA_TURISTICA,
    								new Boolean(limRestOper.getNacionalidad().equals(String.
    										valueOf(Num.I_48))));
    				}
    				if (zonaFronteriza) {
    					// Zona Fronteriza
    					listaZonaFronteriza =
    						getSicaServiceData().
    						findLimitesRestriccionOperacion(
    								deal.getCliente().getTipoPersona(),
    								new Boolean(limRestOper.getCliente().equals(SiNoType.TRUE)),
    								Constantes.ZONA_FRONTERIZA,
    								new Boolean(limRestOper.getNacionalidad().equals(String.
    										valueOf(Num.I_48))));
    				}
    				if (listaZonaTuristica != null && listaZonaFronteriza != null) {
    					// Se toma el limite mayor.
    					for (Iterator i = listaZonaTuristica.iterator(); i.hasNext(); ) {
    						LimiteEfectivo limiteTur = (LimiteEfectivo) i.next();
    						for (Iterator j = listaZonaFronteriza.iterator(); j.hasNext(); ) {
    							LimiteEfectivo limiteFro = (LimiteEfectivo) j.next();
    							if (limiteTur.getDivisa().equals(limiteFro.getDivisa())) {
    								if (limiteTur.getLimiteMensual().doubleValue() >
    								limiteFro.getLimiteMensual().doubleValue()) {
    									// Zona turistica es mayor
    									limRestOper.setListaLimitesNormales(listaZonaTuristica);
    									zona = Constantes.ZONA_TURISTICA;
    								}
    								else if (limiteTur.getLimiteMensual().doubleValue() <
    										limiteFro.getLimiteMensual().doubleValue()) {
    									// zona fronteriza es mayor
    									limRestOper.setListaLimitesNormales(listaZonaFronteriza);
    									zona = Constantes.ZONA_FRONTERIZA;
    								}
    							}
    						}
    					}
    					if (limRestOper.getListaLimitesNormales() == null) {
    						// Si todas las reglas son iguales, se toma zona turistica
    						limRestOper.setListaLimitesNormales(listaZonaTuristica);
    						zona = Constantes.ZONA_TURISTICA;
    					}
    				}
    				else if (listaZonaTuristica != null) {
    					// Se toma el limite de zona turistica
    					limRestOper.setListaLimitesNormales(listaZonaTuristica);
    					zona = Constantes.ZONA_TURISTICA;
    				}
    				else if (listaZonaFronteriza != null) {
    					// Se toma el limite se zona fronteriza
    					limRestOper.setListaLimitesNormales(listaZonaFronteriza);
    					zona = Constantes.ZONA_FRONTERIZA;
    				}
    			}
    			
    			deal.setTipoZona(zona);

    		 }//Es Cliente
    		}
    		else {
    			//INICIA PF
    			limRestOper.setListaLimitesNormales(getSicaServiceData().
    						findLimitesRestriccionOperacion(deal.getCliente().getTipoPersona(),
    								new Boolean(limRestOper.getCliente().equals(SiNoType.TRUE)),
    								null,
    								new Boolean(limRestOper.getNacionalidad().equals(String.
    										valueOf(Num.I_48)))));
    		    deal.setTipoZona(null);
    			//TERMINA
    		}
    		
    		
			if (plb != null) {
				limRestOper.setLimiteDiarioIxeCpra(plb.getLimiteDiario().doubleValue());
				limRestOper.setLimiteMensualIxeCpra(plb.getLimiteMensual().doubleValue());
				limRestOper.setTipoExcepcion(plb.getTipoExcepcion());
			} 
			else {
				limRestOper.setLimiteDiarioIxeCpra(0);
				limRestOper.setLimiteMensualIxeCpra(0);
				limRestOper.setTipoExcepcion(PersonaListaBlanca.SIN_EXCEPCION);
			}
			deal.setTipoExcepcion(limRestOper.getTipoExcepcion());
			deal.setEsCliente(limRestOper.getCliente());
			setLimitesRestriccionOperacion(limRestOper);
			
		}
		return getLimitesRestriccionOperacion();
	}
    
    /**
     * Verifica si se excede del l&iacute;mite normal de operaci&oacute;n, de ser as&iacute; se
     * enviara una notificaci&oacute;n a la pantalla de solicitud de modificaci&oacute;n.
     *
     * @param deal El deal a verificar
     */
    protected void verificaLimiteNormalOper(Deal deal) {
    	StringBuffer limsDivsProds = new StringBuffer("");
    	LimitesRestriccionOperacion limRestOper = getLimitesActualizados(deal);
    	List productosEnRestriccion = getSicaServiceData().getProductosEnRestriccion();
    	for (Iterator it1 = limRestOper.getListaLimitesNormales().iterator(); it1.hasNext();) {
    		LimiteEfectivo limEfecti = (LimiteEfectivo) it1.next();
    		String divisaEnRestriccion = limEfecti.getDivisa();
    		double acumDiarioProdsRestCpra = getSicaServiceData().
    				findAcumuladoUsdCompraVentaDiarioMensual(deal.getContratoSica().getNoCuenta(),
    						deal.getFechaLiquidacion(), true, divisaEnRestriccion,
    						productosEnRestriccion, true);
    		double acumMensualProdsRestCpra = getSicaServiceData().
    				findAcumuladoUsdCompraVentaDiarioMensual(deal.getContratoSica().getNoCuenta(),
    						deal.getFechaLiquidacion(), false, divisaEnRestriccion,
    						productosEnRestriccion, true);
    		double acumMensualProdsRestVta = getSicaServiceData().
					findAcumuladoUsdCompraVentaDiarioMensual(deal.getContratoSica().getNoCuenta(),
							deal.getFechaLiquidacion(), false, divisaEnRestriccion,
							productosEnRestriccion, false);
    		double acumDiarioProdsRestVta = getSicaServiceData().
					findAcumuladoUsdCompraVentaDiarioMensual(deal.getContratoSica().getNoCuenta(),
							deal.getFechaLiquidacion(), true, divisaEnRestriccion,
							productosEnRestriccion, false);
    		if ((limRestOper.getLimiteMaximoMensual(divisaEnRestriccion, true) != null) &&
    				(acumMensualProdsRestCpra > limRestOper.getLimiteMaximoMensual(
    						divisaEnRestriccion, true).doubleValue())) {
    			auditar(new Integer(deal.getIdDeal()), LogAuditoria.EXCEDIO_LIMITE_OPERACION, null,
    					"WARN", "F");
    			throw new SicaException("El cliente ya no puede operar " + productosEnRestriccion.
    					toString().substring(1, productosEnRestriccion.toString().length()-1) +
    					" durante el resto del mes en la compra para la " + "divisa " +
    					divisaEnRestriccion + ".");
    		}
    		else if ((limRestOper.getLimiteMaximoDiario(divisaEnRestriccion, true) != null) &&
    				(acumDiarioProdsRestCpra > limRestOper.getLimiteMaximoDiario(
    						divisaEnRestriccion, true).doubleValue())) {
    			auditar(new Integer(deal.getIdDeal()), LogAuditoria.EXCEDIO_LIMITE_OPERACION, null,
    					"WARN", "F");
    			throw new SicaException("El cliente ya no puede operar " + productosEnRestriccion.
    					toString().substring(1, productosEnRestriccion.toString().length()-1) +
    					" durante el resto del d\u00eda en la compra para la " + "divisa " +
    					divisaEnRestriccion + ".");
    		}
    		else if ((limRestOper.getLimiteMaximoMensual(divisaEnRestriccion, false) != null) &&
    				(acumMensualProdsRestVta > limRestOper.getLimiteMaximoMensual(
    						divisaEnRestriccion, false).doubleValue())) {
    			auditar(new Integer(deal.getIdDeal()), LogAuditoria.EXCEDIO_LIMITE_OPERACION, null,
    					"WARN", "F");
    			throw new SicaException("El cliente ya no puede operar " + productosEnRestriccion.
    					toString().substring(1, productosEnRestriccion.toString().length()-1) +
    					" durante el resto del mes en la venta para la " + "divisa " +
    					divisaEnRestriccion + ".");
    		}
    		else if ((limRestOper.getLimiteMaximoDiario(divisaEnRestriccion, false) != null) &&
    				(acumDiarioProdsRestVta > limRestOper.getLimiteMaximoDiario(divisaEnRestriccion,
    						false).doubleValue())) {
    			auditar(new Integer(deal.getIdDeal()), LogAuditoria.EXCEDIO_LIMITE_OPERACION, null,
    					"WARN", "F");
    			throw new SicaException("El cliente ya no puede operar " + productosEnRestriccion.
    					toString().substring(1, productosEnRestriccion.toString().length()-1) +
    					" durante el resto del d\u00eda en la venta para la " + "divisa " +
    					divisaEnRestriccion + ".");
    		}
    		if (limRestOper.getTipoExcepcion().equals(PersonaListaBlanca.EXCEPCION_IXE)) {
    			for (Iterator it2 = productosEnRestriccion.iterator(); it2.hasNext();) {
    				String productoEnRestriccion = (String) it2.next();
    				for (Iterator it3 = deal.getDetalles().iterator(); it3.hasNext();) {
    					DealDetalle det = (DealDetalle) it3.next();
    					if (det.getClaveFormaLiquidacion() != null && !det.getDivisa().
    							getIdDivisa().equals(Constantes.MXN) && !deal.isInterbancario() &&
    							det.getClaveFormaLiquidacion().equals(productoEnRestriccion) &&
    							deal.getContratoSica() != null && det.isRecibimos() &&
    							det.getDivisa().getIdDivisa().equals(divisaEnRestriccion) &&
    							!det.getStatusDetalleDeal().equals(DealDetalle.
    							STATUS_DET_CANCELADO) && det.getReversado() == 0) { 
    						if (((acumMensualProdsRestCpra > limEfecti.getLimiteMensual().
    								doubleValue()) && (acumMensualProdsRestCpra <
    										limRestOper.getLimiteMensualIxeCpra())) ||
    										((acumDiarioProdsRestCpra > limEfecti.getLimiteDiario().
    												doubleValue()) && acumDiarioProdsRestCpra <
    												limRestOper.getLimiteDiarioIxeCpra())) {
    							if (limsDivsProds.indexOf(divisaEnRestriccion + ":R-") < 0) {
    								limsDivsProds.append(divisaEnRestriccion).append(":").
    								append("R-").append(((productosEnRestriccion.toString().
    								replaceAll("\\[", "")).replaceAll("\\]", "")).
    								replaceAll(" ", "")).append(";").append(limEfecti.
    								getLimiteDiario()).append(",").append(acumDiarioProdsRestCpra).
    								append(",").append(limRestOper.getLimiteDiarioIxeCpra()).
    								append(",").append(limEfecti.getLimiteMensual()).append(",").
    								append(acumMensualProdsRestCpra).append(",").append(limRestOper.
    								getLimiteMensualIxeCpra()).append("|");
    							}
    						}
    					}
    				}
    			}
    		}
    	}
    	if (limsDivsProds.length() > 0) {
    		limsDivsProds = new StringBuffer(limsDivsProds.substring(0, limsDivsProds.
					length()-1));
    		debug("*** Solicitando notif Deal: " + deal.getIdDeal());
    		getSicaServiceData().crearNotificacion(Actividad.PROC_NOTIF_LIM_OPER,
					Actividad.ACT_DN_NOTIF_LIM, deal, limsDivsProds.toString());
    		auditar(new Integer(deal.getIdDeal()), LogAuditoria.NOTIFICACION_LIMITE_OPERACION,
    				limsDivsProds.toString(), "WARN", "F");
			debug("*** Termina Sol. notif Deal: " + deal.getIdDeal());
    	}
    }

    /**
     * Regresa la lista de FormaPagoLiq que pueden utilizarse para hacer la modificaci&oacute;n de
     * un detalle del deal. Esta lista es pasada al applet.
     *
     * @param det El detalle de deal.
     * @return List.
     */
    protected List getFormasPagoLiqParaModifDeal(DealDetalle det) {
        String ticket = getTicket();
        List fpls = Divisa.PESO.equals(det.getDivisa().getIdDivisa()) ?
                getFormasPagoLiqService().getProductosNoPizarron(ticket, det.isRecibimos(),
                        det.getDivisa().getIdDivisa(), null) :
                getFormasPagoLiqService().getProductosPizarron(ticket, det.isRecibimos(),
                        det.getDivisa().getIdDivisa(), det.getClaveFormaLiquidacion());
        if (!det.isInterbancario()) {
            for (Iterator it = fpls.iterator(); it.hasNext();) {
                FormaPagoLiq fpl = (FormaPagoLiq) it.next();
                if (!fpl.getDesplegableSica().booleanValue() ||
                        !Constantes.MNEMONICO_ACTIVO.equals(fpl.getStatus()) ||
                        ("SPEI".equals(fpl.getClaveTipoLiquidacion().trim()) &&
                                (!(fpl.getMnemonico().indexOf("MXNSPEIIXEB") >= 0) &&
                                        !(fpl.getMnemonico().indexOf("BCTC") >= 0)))) {
                    it.remove();
                }
            }
        }
        Collections.sort(fpls, new Comparator() {
            public int compare(Object fp1, Object fp2) {
                return ((FormaPagoLiq) fp1).getDescripcion().toLowerCase().
                        compareTo(((FormaPagoLiq) fp2).getDescripcion().toLowerCase());
            }
        });
        return fpls;
    }

    /**
     * Obtiene los Tipos de Liquidaci&oacute;n.
     *
     * @param det El detalle del deal.
     * @return List.
     */
    protected List getTiposPagoLiqParaModifDeal(DealDetalle det) {
        List clavesTiposLiq = new ArrayList();
        List formasLiquidacion = getFormasPagoLiqParaModifDeal(det);
        for (Iterator it = formasLiquidacion.iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (!clavesTiposLiq.contains(fpl.getNombreTipoLiquidacion().trim())) {
                clavesTiposLiq.add(fpl.getNombreTipoLiquidacion().trim());
            }
        }
        return clavesTiposLiq;
    }

    /**
     * Revisa que
     * <li>El status del detalle no sea cancelado ni liquidado</li>
     * <li>El deal no tenga una solicitud de modificaci&oacute;n pendiente</li>
     * <li>El deal no tenga una solicitud de cancelaci&oacute;n pendiente</li>
     * <li>El detalle no tenga una negaci&oacute;n de autorizaci&oacute;n de
     * modificaci&oacute;n</li>
     * <li>El detalle no tenga una solicitud de autorizaci&oacute;n de horario o monto
     * pendientes</li>
     *
     * @param det El detalle de deal a modificar (Split o cambio de forma de liquidaci&oacute;n.
     */
    protected void validarModificaciones(DealDetalle det) {
        if (det.isStatusIn(new String[]{DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ,
                DealDetalle.STATUS_DET_CANCELADO})) {
            throw new SicaException("no puede estar liquidado ni cancelado.");
        }
        if (det.getDeal().tieneSolModifPendiente()) {
            throw new SicaException("el deal tiene una solicitud de modificaci\u00f3n" +
                    " pendiente.");
        }
        if (det.getDeal().tieneSolCancPendiente()) {
            throw new SicaException("el deal tiene una solicitud de cancelaci\u00f3n total " +
                    "pendiente.");
        }
        if (det.isEvento(det.isInterbancario() ?
                DealDetalle.EV_IND_INT_MODIFICACION : DealDetalle.EV_IND_MODIFICACION,
                new String[]{Deal.EV_NEGACION})) {
            throw new SicaException("no se puede pedir una solicitud de cancelaci\u00f3n para " +
                    "un detalle que el \u00e1rea de tesorer\u00eda neg\u00f3 la " +
                    "cancelaci\u00f3n con anterioridad.");
        }
        if (!getDeal().isInterbancario()) {
            if (det.tieneAutPendientesHorario()) {
                throw new SicaException("tiene una autorizaci\u00f3n por horario pendiente.");
            }
            if (det.tieneAutPendientesMonto()) {
                throw new SicaException("tiene una autorizaci\u00f3n por monto pendiente.");
            }
        }
    }

    /**
     * Genera el mapa de informaci&oacute;n que es pasado al applet por medio de JavaScript, para
     * poder realizar una modificaci&oacute;n al detalle seleccionado.
     * <p/>
     * <li>El detalle no debe estar cancelado ni liquidado.</li>
     * <li>No debe haber otra modificaci&oacute;n pendiente en el encabezado de deal.</li>
     * <li>No debe haber solicitud de cancelaci&oacute;n pendiente en el encabezado de deal.</li>
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void solicitarModificacionDetalle(IRequestCycle cycle) {
        try {
            DealDetalle det = findDetalle(((Integer) cycle.getServiceParameters()[0]).intValue());
            if (det.getMnemonico() == null) {
                throw new SicaException("a\u00fan no tiene mnem\u00f3nico.");
            }
            validarModificaciones(det);
            Map modifMap = new HashMap();
            modifMap.put("inter", Boolean.valueOf(getDeal().isInterbancario()));
            modifMap.put(Keys.DET, det);
            modifMap.put(Keys.TIPOS_PAGO_LIQ, getTiposPagoLiqParaModifDeal(det));
            modifMap.put(Keys.FORMAS_PAGO_LIQ, getFormasPagoLiqParaModifDeal(det));
            modifMap.put("etiqueta", "Nueva forma de " + (det.isRecibimos() ? "Cobro:" : "Pago:"));
            setModifMap(modifMap);
        }
        catch (SicaException e) {
            getDelegate().record("El detalle no puede ser modificado porque " + e.getMessage(),
                    null);
            debug(e.getMessage(), e);
        }
    }

    /**
     * Contiene la l&oacute;gica para cambiar la forma de liquidaci&oacute;n del detalle de un deal.
     *
     * @param idDealDetalle El n&uacute;mero de detalle del deal que desea modificarse.
     * @param mnemonico     El mnem&oacute;nico a asignar.
     */
    public void limpiarFormaLiquidacion(int idDealDetalle, String mnemonico) {
        DealDetalle det = findDetalle(idDealDetalle);
        try {
            if (isHorarioParaModificarDetalle()) {
                getWorkFlowServiceData().solicitarModificacionDetalle(getTicket(), det, mnemonico,
                        ((Visit) getVisit()).getUser());
                if (getDeal().tieneSolModifPendiente()) {
                    setLevel(1);
                    getDelegate().record("Se solicit\u00f3 autorizaci\u00f3n al \u00e1rea de " +
                            "tesorer\u00eda, por favor espere la respuesta para continuar la " +
                            "modificaci\u00f3n.", null);
                }
            }
            else {
                throw new SicaException("S\u00f3lo se pueden realizar modificaciones a la Forma " +
                        "de Liquidaci\u00f3n del deal en el horario normal, vespertino o " +
                        "restringido.");
            }
        }
        catch (SicaException e) {
            warn(e.getMessage(), e);
            setDeal(getSicaServiceData().findDeal(getDeal().getIdDeal()));
            throw e;
        }
    }
    
    /**
     * Contiene la l&oacute;gica para modificar el monto a un detalle.
     *
     * @param idDealDetalle 	El n&uacute;mero de detalle del deal que desea modificarse.
     * @param nuevoMonto     	El monto a asignar.
     * @param montoActual       El monto que tiene actualmente el detalle del deal.
     */
    public void modificarMontoDetalle(int idDealDetalle, double nuevoMonto, double montoActual) {
    	DealDetalle det = findDetalle(idDealDetalle);
        try {
        	if (montoActual == nuevoMonto) {
        		throw new SicaException("El monto solicitado no puede ser el mismo que el monto " +
        				"actual.");
        	}
        	if (nuevoMonto == 0.0) {
        		throw new SicaException("El monto tiene que ser mayor a cero.");
        	}
        	ParametroSica paramModMonto = getSicaServiceData()
        		.findParametro(ParametroSica.PORCENTAJE_MOD_MONTO);
        	double porcentaje = new Double(paramModMonto.getValor().trim()).doubleValue();
        	porcentaje = porcentaje / 100;
        	double porMonto = det.getMonto() * porcentaje;
        	if (det.getMonto() - porMonto > nuevoMonto || nuevoMonto > det.getMonto() + porMonto) {
        		throw new SicaException("La modificaci\u00f3n del monto excede el l\u00edmite " +
        				"permitido.");
        	}
            if (isHorarioParaModificarDetalle()) {
                getWorkFlowServiceData().solicitarModificacionDetallePorMonto(getTicket(), det, 
                		nuevoMonto, ((Visit) getVisit()).getUser());
                if (getDeal().tieneSolModifPendiente()) {
                	setLevel(1);
                    getDelegate().record("Se solicit\u00f3 autorizaci\u00f3n a la mesa de " + 
                    		"cambios, por favor espere la respuesta para continuar la " +
                            "modificaci\u00f3n.", null);
                }
            }
            else {
                throw new SicaException("S\u00f3lo se pueden realizar modificaciones por monto " +
                        "en el horario normal, vespertino o restringido.");
            }
        }
        catch (SicaException e) {
            warn(e);
            setDeal(getSicaServiceData().findDeal(getDeal().getIdDeal()));
            throw e;
        }
    }
    
    /**
     * Contiene la l&oacute;gica para modificar el producto a un detalle.
     * 
     * @param idDealDetalle          El n&uacute;mero de detalle del deal que desea modificarse.
     * @param nvaFormaLiquidacion    El nuevo producto a asignar.
     * @param nvoTipoCambioMesa      El nuevo tipo de cambio de la mesa.
     * @param nvoTipoCambioCliente   El nuevo tipo de cambio que se le dar&aacute; al cliente.
     */
    public void modificarProductoDetalle(int idDealDetalle, String nvaFormaLiquidacion, 
    		double nvoTipoCambioMesa, double nvoTipoCambioCliente) {
    	DealDetalle det = findDetalle(idDealDetalle);
    	try {
    		if (isHorarioParaModificarDetalle()) {
    			getWorkFlowServiceData().solicitarModificacionDetallePorProducto(getTicket(), det,
                		nvaFormaLiquidacion, nvoTipoCambioMesa, nvoTipoCambioCliente, 
                		((Visit) getVisit()).getUser());
                if (getDeal().tieneSolModifPendiente()) {
                	setLevel(1);
                    getDelegate().record("Se solicit\u00f3 autorizaci\u00f3n al mesa de cambios, " +
                            "por favor espere la respuesta para continuar la " +
                            "modificaci\u00f3n.", null);
                }
    		}
    		else {
                throw new SicaException("S\u00f3lo se pueden realizar modificaciones por producto" +
                		" en el horario normal, vespertino o restringido.");
            }
    	}
    	catch (SicaException e) {
    		warn(e);
    		setDeal(getSicaServiceData().findDeal(getDeal().getIdDeal()));
            throw e;
    	}
    }

    /**
     * Prepara el script para llamar al applet y preguntar el monto y mnem&oacute;nico para el nuevo
     * detalle resultado de la divisi&oacute;n.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void partirDetalle(IRequestCycle cycle) {
        try {
            DealDetalle det = findDetalle(((Integer) cycle.getServiceParameters()[0]).intValue());
            
            if(det.getMnemonico() == null){
            	throw new SicaException("Favor de completar la forma de cobro");
            }
            validarModificaciones(det);
            Map splitMap = new HashMap();
            splitMap.put("inter", Boolean.valueOf(getDeal().isInterbancario()));            
            splitMap.put(Keys.DET, det);
            splitMap.put(Keys.TIPOS_PAGO_LIQ, getTiposPagoLiqParaModifDeal(det));
            splitMap.put(Keys.FORMAS_PAGO_LIQ, getFormasPagoLiqParaModifDeal(det));
            setSplitMap(splitMap);
        }
        catch (SicaException e) {
            getDelegate().record("El detalle no puede ser modificado: " + e.getMessage(),
                    null);
            debug(e);
        }
    }

    /**
     * Llama al servicio hacerSplitDetalle() de WorkFlowServiceData para realizar el split.
     *
     * @param idDealPosicion El n&uacute;mero de detalle de deal.
     * @param monto          El monto a asignar al segundo detalle del split.
     * @param mnemonico      El mnem&oacute;nico a asignar al segundo detalle del split.
     */
    protected void hacerSplit(int idDealPosicion, double monto, String mnemonico) {
        DealDetalle det = findDetalle(idDealPosicion);
        try {
            Visit visit = (Visit) getVisit();
            getWorkFlowServiceData().hacerSplitDetalle(getTicket(), det, mnemonico, monto,
                    visit.getUser());
            if (getDeal().tieneSolModifPendiente()) {
                setLevel(1);
                getDelegate().record("Se solicit\u00f3 autorizaci\u00f3n al \u00e1rea de " +
                        "tesorer\u00eda, por favor espere la respuesta para continuar la " +
                        "modificaci\u00f3n.", null);
            }
            if (getDeal().isInterbancario()) {
                setDeal(getSicaServiceData().findDeal(getDeal().getIdDeal()));
            }
        }
        catch (SicaValidationException e) {
            debug(e);
            getDelegate().record(e.getMessage(), null);
            // Refrescamos el deal.
            setDeal(getSicaServiceData().findDeal(getDeal().getIdDeal()));
        }
        catch (SicaException e) {
            warn(e.getMessage(), e);
            // No funciona refrescar el deal, por lo tanto:
            throw new ApplicationRuntimeException(e.getMessage());
        }
    }

    /**
     * Activa la p&aacute;gina CapturaObservacionesDeal para que el usuario pueda editar las
     * observaciones.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see com.ixe.ods.sica.pages.deals.CapturaObservacionesDeal
     */
    public void capturarObservaciones(IRequestCycle cycle) {
        CapturaObservacionesDeal nextPage = (CapturaObservacionesDeal) cycle.
                getPage("CapturaObservacionesDeal");
        nextPage.setDeal(getDeal());
        nextPage.setNombrePaginaAnterior(getPageName());
        nextPage.activate(cycle);
    }


    /**
     * Activa la p&aacute;gina CapturaMetodoPagoDeal para que el usuario pueda editar el
     * m&eacute;todo de Pago.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see com.ixe.ods.sica.pages.deals.CapturaMetodoPagoDeal 
     */
    public void capturarMetodoPago(IRequestCycle cycle) {
    	CapturaMetodoPagoDeal nextPage = (CapturaMetodoPagoDeal) cycle.
                getPage("CapturaMetodoPagoDeal");
        nextPage.setDeal(getDeal());
        nextPage.setNombrePaginaAnterior(getPageName());
        nextPage.activate(cycle);
    }


    
    
    
    /**
     * Marca el deal con evento de solicitud de cancelaci&oacute;n. Si el deal ya hab&iacute;a sido
     * procesado, llama a <code>terminarActividadesParaDeal()</code> para terminar ese bpm y arranca
     * el proceso de solicitud de cancelaci&oacute;n en el workflow para el deal.
     *
     * @param cycle El IRequestCycle.
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#terminarActividadesParaDeal(
     *com.ixe.ods.sica.model.Deal, String, com.ixe.ods.seguridad.model.IUsuario).
     */
    public void solicitarCancelacion(IRequestCycle cycle) {
        try {
            Deal deal = getDeal();
            Date fechaHoy = DateUtils.inicioDia();
            if (!fechaHoy.equals(DateUtils.inicioDia(deal.getFechaCaptura()))) {
                throw new SicaException("s\u00f3lo se pueden cancelar deals con fecha de captura " +
                        "correspondiente a hoy");
            }
            if (deal.getSwap() != null) {
                throw new SicaException("est\u00e1 ligado a un Swap. Utilice la pantalla de " +
                        "consulta de swaps.");
            }
            if (deal.tieneAutPendientesHorario()) {
                throw new SicaException("tiene una solicitud de autorizaci\u00f3n por horario" +
                        " pendiente, llame a la mesa para que no lo autoricen.");

            }
            if (deal.tieneAutPendientesMonto()) {
                throw new SicaException("tiene una solicitud de autorizaci\u00f3n por monto " +
                        "pendiente, llame a la mesa para que no lo autoricen.");

            }
            getWorkFlowServiceData().wfSolicitarCancelacionDeal(getTicket(), getDeal().getIdDeal(),
                    ((Visit) getVisit()).getUser());
            setDeal(getSicaServiceData().findDeal(getDeal().getIdDeal()));
        }
        catch (SicaException e) {
            debug(e);
            getDelegate().record("El deal no puede ser cancelado porque " + e.getMessage(), null);
        }
    }

    /**
     * Define si el horario permite modificar el detalle.
     *
     * @return boolean
     */
    private boolean isHorarioParaModificarDetalle() {
        Estado estadoActual = getEstadoActual();
        return Estado.ESTADO_OPERACION_NORMAL != estadoActual.getIdEstado() ||
                Estado.ESTADO_OPERACION_RESTRINGIDA != estadoActual.getIdEstado() ||
                Estado.ESTADO_OPERACION_VESPERTINA != estadoActual.getIdEstado();
    }

    /**
     * Obtiene la l&iacute;nea de cambios asignada al cliente.
     *
     * @return LineaCambio.
     */
    public LineaCambio getLinea() {
        if (getDeal().getCliente() != null) {
            LineaCambioServiceData lcsd = (LineaCambioServiceData)
                    getApplicationContext().getBean("lineasCambioServiceData");
            return lcsd.findLineaCambioParaCliente(getDeal().getCliente().getIdPersona());
        }
        return null;
    }    
	
    /**
     * Obtiene la lista de productos como una cadena separadas por ,
     * 
     * @param productosEnRestriccion La lista a usar.
     * 
     * @return Una cadena con los elementos de la lista.
     */
    private String getProductosAsString(List productosEnRestriccion){
    	
    	List aux = new ArrayList();	
    	String respuesta = "";
    	for(int i = 0; i < productosEnRestriccion.size(); i++){
    		
    		if(!aux.contains(productosEnRestriccion.get(i))){
    		    aux.add(productosEnRestriccion.get(i));
    		respuesta += productosEnRestriccion.get(i) +",";
    		}
    	}
    	
    	if(respuesta.length() > 2){
    	respuesta = respuesta.substring(0, respuesta.length() -1);
    	}
    	
    	return respuesta;
    	
    }
    
    private String getDivisasAsString(List divisas_compra){
    
    List aux = new ArrayList();	
	String respuesta = "";
	for(int i = 0; i < divisas_compra.size(); i++){
		if(!aux.contains(((Object[])divisas_compra.get(i))[0].toString())){
		    aux.add(((Object[])divisas_compra.get(i))[0].toString());
			respuesta += ((Object[])divisas_compra.get(i))[0].toString() +",";
		}
	}
	
	if(respuesta.length() > 2){
	respuesta = respuesta.substring(0, respuesta.length() -1);
	}
	
	return respuesta;
    }

    /**
     * Nivel de los mensajes al usuario: -1 Error, 0 Warning, 1 Exito.
     *
     * @param level El valor a asignar.
     */
    public abstract void setLevel(int level);

    /**
     * Regresa el valor de deal.
     *
     * @return Deal.
     */
    public abstract Deal getDeal();

    /**
     * Fija el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public abstract void setDeal(Deal deal);

    /**
     * Regresa el valor de modifMap.
     *
     * @return Map.
     */
    public abstract Map getModifMap();

    /**
     * Fija el valor de splitMap.
     *
     * @param splitMap El valor a asignar.
     */
    public abstract void setSplitMap(Map splitMap);

    /**
     * Fija el valor de modifMap.
     *
     * @param modifMap El valor a asignar.
     */
    public abstract void setModifMap(Map modifMap);

    /**
     * Regresa el valor de modoInsercion.
     *
     * @return boolean.
     */
    public abstract boolean isModoInsercion();

    /**
     * Fija el valor de modoInsercion.
     *
     * @param modoInsercion El valor a asignar.
     */
    public abstract void setModoInsercion(boolean modoInsercion);
    
    /**
     * Regresa los l&iacute;mites de restricci&oacute;n para el contrato sica actual.
     *
     * @return LimitesRestriccionOperacion.
     */
    public abstract LimitesRestriccionOperacion getLimitesRestriccionOperacion();
    
    /**
     * Fija el valor de los l&iacute;mites de restricci&oacute;n de operaci&oacute;n.
     *
     * @param limites
     */
    public abstract void setLimitesRestriccionOperacion(LimitesRestriccionOperacion limites);

    /**
     * Constante que identifica el modo de submit de pagoAnticipado.
     */
    public static final String MODO_PAGO_ANTICIPADO = "pagoAnticipado";

    /**
     * Constante para el modo de submit de mensajeria.
     */
    public static final String SUBMIT_MODE_MENSAJERIA = "mensajeria";

    /**
     * Constante para indicar la opci&oacute;n de crear nuevo deal.
     */
    public static final int OPCION_NORMAL = 0;

    /**
     * Constante para indicar la opci&oacute; de inserci&oacute;n de detalle.
     */
    public static final int OPCION_INSERTAR_DETALLE = 1;

    /**
     * Constante para indicar la revisi&oacute;n de un deal.
     */
    public static final int OPCION_EDITAR = 2;

    /**
     * Constante para indicar la carga de nuevo el deal desplegado.
     */
    public static final int OPCION_REFRESCAR_DEAL = 3;

    /**
     * Constante para solicitar la divisi&oacute;n de un detalle en dos.
     */
    public static final int OPCION_SPLIT_DETALLE = 4;

    /**
     * Constante para solicitar la limpieza de mnem&oacute;nico y plantilla.
     */
    public static final int OPCION_MODIFICAR_DETALLE = 5;

    /**
     * La constante para el par&aacute;metro 3 (Mnem&oacute;nico).
     */
    protected static final int PARAM_SPLIT_MNEMONICO = 3;
    
    /**
     * Gets the persona service.
     *
     * @return the persona service
     */
    public PersonaService getPersonaService() {
    	return (PersonaService) getApplicationContext().getBean("personaService");
    }
}

/*
 * $Id: CapturaDeal.java,v 1.48.2.6.4.1.2.2.2.1.4.1.4.2.18.1.14.1.8.1.2.10.6.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2013 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.springframework.remoting.RemoteAccessException;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaExceptionHelper;
import com.ixe.ods.sica.SicaValidationException;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.components.EncabezadoDeal;
import com.ixe.ods.sica.model.Actividad;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.CanalMesa;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.LimitesRestriccionOperacion;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.model.PersonaListaBlanca;
import com.ixe.ods.sica.pages.ConsultaClientes;
import com.ixe.ods.sica.pages.Mensaje;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.ContratoDireccionesService;
import com.ixe.ods.sica.services.LineaCreditoConstantes;
import com.ixe.ods.sica.services.LineaCreditoMensajes;
import com.ixe.ods.sica.services.PersonaService;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;
import com.legosoft.hibernate.type.SiNoType;

// TODO: Auto-generated Javadoc
/**
 * P&aacute;gina que permite al usuario insertar y editar un deal y sus detalles.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.48.2.6.4.1.2.2.2.1.4.1.4.2.18.1.14.1.8.1.2.10.6.1 $ $Date: 2020/12/01 04:53:01 $
 */
public abstract class CapturaDeal extends AbstractCapturaDeal implements IExternalPage, DataSourceProvider {

    /**
     * S&oacute;lo se ejecuta desde la captura r&aacute;pida de deal.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        if (isHorarioVespertino()) {
            throw new PageRedirectException("ErrorEstado");
        }
        else {
            try {
                Deal deal = new Deal();
                deal.setUsuario(((Visit) getVisit()).getUser());
                deal.setTipoDeal(Deal.TIPO_SIMPLE);
                Canal canal = getSicaServiceData().findCanal(((Visit) getVisit()).getIdCanal());
                CanalMesa cm = new CanalMesa(canal, canal.getMesaCambio());
                deal.setCanalMesa(cm);
                deal.setGrupoTrabajo(((Visit) getVisit()).getGrupoTrabajo());
                setDeal(deal);
                setModoInsercion(true);
                setMensajearApplet(true);
                setEstadoLineaCredito(null);
            }
            catch (SicaException e) {
                debug(e);
                getDelegate().record(e.getMessage(), null);
            }
        }
    }

    /**
     * M&eacut;etodo que coordina todas las posibles opciones de operaci&oacute;n, como crear un
     * deal, insertar detalles, dividir en dos un detalle y editar el deal.
     *
     * @param params Los par&aacute;metros del Request Cycle.
     * @param cycle  El ciclo de la p&aacute;gina.
     */
    public void activateExternalPage(Object[] params, IRequestCycle cycle) {
        super.activate(cycle);
        try {
            Visit visit = (Visit) getVisit();
            int opc = ((Integer) params[0]).intValue();
            _logger.debug(">>>>>> OPCION -> " + opc);
            for (int i = 0; i < params.length ; i++) {
            	_logger.debug("params[" + i + "] = " + params[i]);
            }
            if (opc == OPCION_NORMAL) {
                // Si es de ixe directo, el cliente esta asignado a un ejecutivo y este se encuentra
                // en la Jerarquia del SICA, no debe poder operarlo:
                if (visit.isIxeDirecto()) {
                    try {
                        validarIxeDirecto(params);
                    }
                    catch (SicaException e) {
                        Mensaje nextPage = (Mensaje) getRequestCycle().getPage("Mensaje");
                        nextPage.setMensaje(e.getMessage());
                        cycle.activate(nextPage);
                        return;
                    }
                }
                if (isHorarioVespertino()) {
                    throw new SicaException("No es posible capturar un deal en Horario Vespertino");
                }
                else {
                    crearDeal(params, cycle);
                    Integer idPersona         = new Integer(params[4].toString());
                    validarLineaCreditoActiva(idPersona);
                    
                }
            }
            else if (opc == OPCION_INSERTAR_DETALLE) {
                if (isHorarioVespertino()) {
                    throw new SicaException("No es posible insertar un nuevo detalle para el " +
                            "deal en Horario Vespertino.");
                }else {
                	if ("V".equals(params[PARAM_INSDET_OPER]) &&
                		Divisa.DOLAR.equals(params[PARAM_INSDET_ID_DIVISA]) && 
                		Constantes.DOCUMENTO.equals(params[PARAM_INSDET_CLAVE_FORMA_LIQ])) {
                		throw new SicaException("El producto DOCEXT USD no se encuentra disponible.");
                	}
                    try {
                    	validarChequeViajero(params);  
                    	if(!((String) params[PARAM_INSDET_FECHA_VALOR]).equals(Constantes.CASH)){
                    		if(getDeal().getCliente() == null ){
                        		throw new SicaException(LineaCreditoMensajes.ERROR_DEAL_CAPTURA_RAPIDA_);
                        	}
                    		
                    		validarLineaCreditoActiva(getDeal().getCliente().getIdPersona());
                    		
                    	}
                    	getSicaServiceData().insertarDivisa(visit.getTicket(), getDeal(),
                    			"C".equals(params[PARAM_INSDET_OPER]),
                    			(String) params[PARAM_INSDET_FECHA_VALOR],
                    			(String) params[PARAM_INSDET_CLAVE_FORMA_LIQ],
                    			(String) params[PARAM_INSDET_ID_DIVISA],
                    			((Double) params[PARAM_INSDET_TCM]).doubleValue(),
                    			((Double) params[PARAM_INSDET_MONTO]).doubleValue(),
                    			((Double) params[PARAM_INSDET_TCC]).doubleValue(),
                    			(Double) params[PARAM_INSDET_PRE_REF_MID_SPOT],
                    			(Double) params[PARAM_INSDET_PRE_REF_SPOT],
                    			(Double) params[PARAM_INSDET_FACT_DIV],
                    			((Integer) params[PARAM_INSDET_ID_SPREAD]).intValue(),
                    			((Integer) params[PARAM_INSDET_ID_PRECIO_REF]),
                    			((Integer) params[PARAM_INSDET_ID_FACT_DIV]),
                    			null, true,
                    			null, getLimitesActualizados(getDeal()), null);
                    }
                    catch (SicaException se) {
                    	if (se.getMessage().substring(0, 9).equals("LimEfect:")) {
                    		getSicaServiceData().auditar(visit.getTicket(), getRequestCycle().
                    				getRequestContext().getRequest().getRemoteAddr(),
                    				new Integer(getDeal().getIdDeal()), ((Visit) getVisit()).
                    				getUser(), LogAuditoria.EXCEDIO_LIMITE_OPERACION, null,
                    				"ERROR", "F");
                    		warn(se.getMessage(), se);
                            getDelegate().record(se.getMessage().substring(10, se.getMessage().
                            		length()), null);
                    	}
                    	else {
                    		getDelegate().record(se.getMessage(), null);
                    	}
                    }
                }
            }
            else if (opc == OPCION_INSERTAR_DETALLE_NETEO) {
                if (isHorarioVespertino()) {
                    throw new SicaException(
                            "No es posible insertar un nuevo detalle para el " +
                                    "deal en Horario Vespertino.");
                }
                else {
                	// Si es captura rapida, no se acepta venta de cheque de viajero
                    if (getDeal().getContratoSica() == null) {
                    	for (Iterator it = getSicaServiceData().getProductosEnRestriccion().
                        		iterator();it.hasNext();) {
                        	String productoEnRestriccion = (String) it.next();
                        	if (productoEnRestriccion.
                        			equals(params[PARAM_INSDET_CLAVE_FORMA_LIQ_CPRA]) ||
                        			productoEnRestriccion.
                        			equals(params[PARAM_INSDET_CLAVE_FORMA_LIQ_VTA])) {
                        		throw new SicaException("No es posible capturar " +
                        				productoEnRestriccion + " desde la captura r\u00e1pida" +
                        				" hasta que el deal cuente con un contrato asociado.");
                        	}
                        }
                        if ("V".equals(params[PARAM_INSDET_OPER_VTA]) && Constantes.TRAVELER_CHECKS.
                                equals(params[PARAM_INSDET_CLAVE_FORMA_LIQ_VTA])) {
                            throw new SicaException("No es posible capturar Venta de Cheques de " +
                                    "Viajero desde la captura r\u00e1pida.");
                        }
                    }
                	validaChviaj(getDeal(), (String) params[PARAM_INSDET_CLAVE_FORMA_LIQ_VTA],
                        ((Double) params[PARAM_INSDET_MONTO_VTA]).doubleValue(),
                        (String) params[PARAM_INSDET_FECHA_VALOR_VTA],
                        (String) params[PARAM_INSDET_ID_DIVISA_VTA],
                        ((Double) params[PARAM_INSDET_FACT_DIV_VTA]));
                    validaFormaLiquidacion(getDeal(),
                        (String) params[PARAM_INSDET_CLAVE_FORMA_LIQ_VTA],
                        (String) params[PARAM_INSDET_ID_DIVISA_VTA]);
                    try {
                    	validaLimitesRestriccionOperacionNeteos(getDeal(),
                    			(String) params[PARAM_INSDET_CLAVE_FORMA_LIQ_CPRA],
                    			((Double) params[PARAM_INSDET_MONTO_CPRA]).doubleValue(),
                    			(String) params[PARAM_INSDET_FECHA_VALOR_CPRA],
                    			(String) params[PARAM_INSDET_ID_DIVISA_CPRA],
                    			((Double) params[PARAM_INSDET_FACT_DIV_CPRA]),
                    			"C".equals(params[PARAM_INSDET_OPER_CPRA]));
                    	validaLimitesRestriccionOperacionNeteos(getDeal(),
                    			(String) params[PARAM_INSDET_CLAVE_FORMA_LIQ_VTA],
                    			((Double) params[PARAM_INSDET_MONTO_VTA]).doubleValue(),
                    			(String) params[PARAM_INSDET_FECHA_VALOR_VTA],
                    			(String) params[PARAM_INSDET_ID_DIVISA_VTA],
                    			((Double) params[PARAM_INSDET_FACT_DIV_VTA]),
                    			"C".equals(params[PARAM_INSDET_OPER_VTA]));
                    	getSicaServiceData().insertarDivisa(visit.getTicket(), getDeal(), "C".
                    			equals(params[PARAM_INSDET_OPER_CPRA]),
                    			(String) params[PARAM_INSDET_FECHA_VALOR_CPRA],
                    			(String) params[PARAM_INSDET_CLAVE_FORMA_LIQ_CPRA],
                    			(String) params[PARAM_INSDET_ID_DIVISA_CPRA],
                    			((Double) params[PARAM_INSDET_TCM_CPRA]).doubleValue(),
                    			((Double) params[PARAM_INSDET_MONTO_CPRA]).doubleValue(),
                    			((Double) params[PARAM_INSDET_TCC_CPRA]).doubleValue(),
                    			((Double) params[PARAM_INSDET_PRE_REF_MID_SPOT_CPRA]),
                    			((Double) params[PARAM_INSDET_PRE_REF_SPOT_CPRA]),
                    			(Double) params[PARAM_INSDET_FACT_DIV_CPRA],
                    			((Integer) params[PARAM_INSDET_ID_SPREAD_CPRA]).intValue(),
                    			((Integer) params[PARAM_INSDET_ID_PRECIO_REF_CPRA]),
                    			((Integer) params[PARAM_INSDET_ID_FACT_DIV_CPRA]),
                    			null,
                    			true, null, null, null);
                    	getSicaServiceData().insertarDivisa(visit.getTicket(), getDeal(), "C".
                    			equals(params[PARAM_INSDET_OPER_VTA]),
                    			(String) params[PARAM_INSDET_FECHA_VALOR_VTA],
                    			(String) params[PARAM_INSDET_CLAVE_FORMA_LIQ_VTA],
                    			(String) params[PARAM_INSDET_ID_DIVISA_VTA],
                    			((Double) params[PARAM_INSDET_TCM_VTA]).doubleValue(),
                    			((Double) params[PARAM_INSDET_MONTO_VTA]).doubleValue(),
                    			((Double) params[PARAM_INSDET_TCC_VTA]).doubleValue(),
                    			((Double) params[PARAM_INSDET_PRE_REF_MID_SPOT_VTA]),
                    			((Double) params[PARAM_INSDET_PRE_REF_SPOT_VTA]),
                    			(Double) params[PARAM_INSDET_FACT_DIV_VTA],
                    			((Integer) params[PARAM_INSDET_ID_SPREAD_VTA]).intValue(),
                    			((Integer) params[PARAM_INSDET_ID_PRECIO_REF_VTA]),
                    			((Integer) params[PARAM_INSDET_ID_FACT_DIV_VTA]),
                    			null,
                    			true, null, null, null);
                    }
                    catch (SicaException se) {
                    	if (se.getMessage().substring(0, 9).equals("LimEfect:")) {
                    		getSicaServiceData().auditar(visit.getTicket(), getRequestCycle().
                    				getRequestContext().getRequest().getRemoteAddr(),
                    				new Integer(getDeal().getIdDeal()), ((Visit) getVisit()).
                    				getUser(), LogAuditoria.EXCEDIO_LIMITE_OPERACION, null,
                    				"ERROR", "F");
                    		warn(se.getMessage(), se);
                        	getDelegate().record(se.getMessage().substring(10, se.getMessage().
                            		length()), null);
                    	}
                    	else {
                    		getDelegate().record(se.getMessage(), null);
                    	}
                    }
                    getDeal().setContieneNeteo(new Boolean(true));
                }
            }
            else if (opc == OPCION_EDITAR) {
            	
                Integer idDeal = (Integer) params[1];
                auditar(idDeal, LogAuditoria.EDICION_DEAL, null, "INFO", "E");
                ConsultaDeal prevPage = (ConsultaDeal) cycle.getPage("ConsultaDeal");
                prevPage.setCriteriosMinimizados(false);
                prevPage.limpiar();
                setModoInsercion(false);
                setDeal(getSicaServiceData().findDeal(idDeal.intValue()));
                setArea(Constantes.SICA_CONS_DEAL_T.equals(params[2]));
                if(getDeal()!=null && getDeal().getCliente()!= null){
                	validarLineaCreditoActiva(getDeal().getCliente().getIdPersona());
                }
                	
            }
            else if (opc == OPCION_EDITAR_DEALS_PENDIENTE_DIA) {
                setDeal(getSicaServiceData().findDeal(((Integer) params[1]).intValue()));
                setPaginaRegreso((String) params[Num.I_3]);
            }
            else if (opc == OPCION_REFRESCAR_DEAL) {
            	int idDeal = ((Integer) params[1]).intValue();
                setDeal(getSicaServiceData().findDeal(idDeal));
            }
            else if (opc == OPCION_SPLIT_DETALLE) {
                if (isHorarioVespertino()) {
                    throw new SicaException("No es posible insertar un nuevo detalle para el " +
                            "deal en Horario Vespertino.");
                }
                else {
                    int idDealPosicion = ((Integer) cycle.getServiceParameters()[1]).intValue();
                    DealDetalle det = findDetalle(idDealPosicion);
                    if (isHorarioVespertino() && !det.isPesos()) {
                        throw new SicaException("No es posible hacer Split para un detalle de " +
                                "deal en Horario Vespertino.");
                    }
                    else {
                        double monto = ((Double) cycle.getServiceParameters()[2]).doubleValue();
                        String mnemonico = cycle.getServiceParameters()[PARAM_SPLIT_MNEMONICO].
                                toString().trim();
                        hacerSplit(idDealPosicion, monto, mnemonico);
                    }
                }
            }
            else if (opc == OPCION_MODIFICAR_DETALLE) {
                limpiarFormaLiquidacion(((Integer) cycle.getServiceParameters()[1]).intValue(),
                        cycle.getServiceParameters()[2].toString().trim());
            }
        }
        catch (SicaException e) {
        	e.printStackTrace();
            getDelegate().record(e.getMessage(), null);
            debug(e);
        }
    }

    
   

	/**
	 * Si es captura rapida, no se acepta venta de cheque de viajero:.
	 *
	 * @param params the params
	 */
	private void validarChequeViajero(Object[] params) {
		if (getDeal().getContratoSica() == null) {
		    for (Iterator it = getSicaServiceData().getProductosEnRestriccion().
		    		iterator();it.hasNext();) {
		    	String productoEnRestriccion = (String) it.next();
		    	if (productoEnRestriccion.
		    			equals(params[PARAM_INSDET_CLAVE_FORMA_LIQ])) {
		    		throw new SicaException("No es posible capturar " +
		    				productoEnRestriccion + " desde la captura r\u00e1pida" +
		    				" hasta que el deal cuente con un contrato asociado.");
		    	}
		    }
		    if ("V".equals(params[PARAM_INSDET_OPER]) && Constantes.TRAVELER_CHECKS.
		            equals(params[PARAM_INSDET_CLAVE_FORMA_LIQ])) {
		        throw new SicaException("No es posible capturar Venta de Cheques de " +
		                "Viajero desde la captura r\u00e1pida.");
		    }
		}
	}
     
    
  

    /**
     * Se encarga de validar si existe una Linea de Credito Activa para el Cliente.
     *
     * @param idPersona the id persona
     */
	private void validarLineaCreditoActiva(Integer idPersona) {
    	
    	setEstadoLineaCredito("");
		LineaCambio lineaCambio   = getLineaCreditoService().findLineaCreditoByIdPersona(idPersona);
		if(lineaCambio == null || !LineaCreditoConstantes.STATUS_ACTIVADA.equals(lineaCambio.getStatusLinea())){
			throw new SicaException(LineaCreditoMensajes.LINEA_CREDITO_NO_ACTIVA);
		}
		if(lineaCambio != null){
			String estadoLineaCredito = lineaCambio.getStatusLinea();
			setEstadoLineaCredito(estadoLineaCredito);			
		}
	}
    

	/**
     * M&eacute;todo que hace la validaci&oacute;n que no haya compra y venta de efectivo
     * de la misma divisa en el deal.
     * @param deal deal.
     * @param claveFormaLiquidacionVta clave de la forma de liquidaci&oacute;n.
     * @param idDivisa el id de la divisa.
     */
    private void validaFormaLiquidacion(Deal deal, String claveFormaLiquidacionVta,
            String idDivisa) {
        if (claveFormaLiquidacionVta.equals(Constantes.EFECTIVO)) {
            for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                DealDetalle det = (DealDetalle) it.next();
                if (!det.isCancelado() && det.getClaveFormaLiquidacion() != null &&
                        det.isRecibimos() && det.getClaveFormaLiquidacion().
                        equals(claveFormaLiquidacionVta) && det.getDivisa().
                        getIdDivisa().equals(idDivisa)) {
                    throw new SicaException(
                        "No se puede comprar y vender efectivo en la misma divisa.");
                }
            }
        }
    }
    
    /**
     * M&eacute;todo que hace la validaci&oacute;n del monto de la venta de
     * cheques de viajero.
     *
     * @param deal deal.
     * @param claveFormaLiquidacion the clave forma liquidacion
     * @param monto monto que se desea operar.
     * @param fechaValorVta fecha valor de la venta.
     * @param idDivisa el id de la divisa.
     * @param factorDivisa El Factor actual de la divisa.
     */
    private void validaChviaj(Deal deal, String claveFormaLiquidacion, double monto,
            String fechaValorVta, String idDivisa, Double factorDivisa) {
        Date fechaLiquidacion = deal.getFechaLiquidacion();
        PizarronServiceData psd = getPizarronServiceData();
        Date[] fechasValor = new Date[] { psd.getFechaOperacion(), psd.getFechaTOM(),
            psd.getFechaSPOT(), psd.getFecha72HR(), psd.getFechaVFUT() };
        if (fechaLiquidacion == null) {
            String tpVal = deal.getTipoValor();
            if (tpVal == null) {
                tpVal = fechaValorVta;
            }
            fechaLiquidacion = tpVal.equals(Constantes.VFUT) ? fechasValor[Num.I_4] :
                    tpVal.equals(Constantes.HR72) ? fechasValor[Num.I_3] :
                            tpVal.equals(Constantes.SPOT) ? fechasValor[2] :
                                    tpVal.equals(Constantes.TOM) ?
                                            fechasValor[1] : fechasValor[0];
        }
        DealDetalle det = new DealDetalle();
        det.setMonto(monto);
        det.setDivisa(getSicaServiceData().findDivisa(idDivisa));
        det.setFactorDivisa(factorDivisa);
        double montoUSD = det.getMontoUSD();
        if (!deal.isInterbancario() &&
                Constantes.TRAVELER_CHECKS.equals(claveFormaLiquidacion) &&
                deal.getContratoSica() != null) {
            double montoChviaj = getSicaServiceData().findMontoChequesViajeroUsdFechaLiquidacion(
                    deal.getContratoSica().getNoCuenta(), fechaLiquidacion);
            if (montoUSD + montoChviaj > 25000) {
                if ("0.00".equals(Constantes.MONEY_FORMAT.format(25000 - montoChviaj))) {
                    throw new SicaException("El cliente ya no puede operar Cheques de " +
                            "Viajero durante el resto del d\u00eda.");
                }
                else {
                    throw new SicaException("El cliente solo puede operar " +
                            Constantes.MONEY_FORMAT.format(25000 - montoChviaj) +
                            " USD en Cheques de Viajero durante el resto del d\u00eda.");
                }
            }
        }
    }
    
    /**
     * Hace la validaci&oacute;n de la compra/venta de efectivo al operar por mec&aacute;nica de
     * neteos.
     *
     * @param deal El deal.
     * @param claveFormaLiquidacion La clave forma de liquidaci&oacute;n.
     * @param monto El monto que se desea operar.
     * @param fechaValor La fecha valor.
     * @param idDivisa La divisa que se esta operando.
     * @param factorDivisa El Factor Divisa actual de la divisa.
     * @param recibimos Si es compra o venta.
     */
    private void validaLimitesRestriccionOperacionNeteos(Deal deal, String claveFormaLiquidacion,
    		double monto, String fechaValor, String idDivisa, Double factorDivisa,
    		boolean recibimos) {
    	Date fechaLiquidacion = deal.getFechaLiquidacion();
    	PizarronServiceData psd = getPizarronServiceData();
    	Date[] fechasValor = new Date[] { psd.getFechaOperacion(), psd.getFechaTOM(),
                psd.getFechaSPOT(), psd.getFecha72HR(), psd.getFechaVFUT() };
            if (fechaLiquidacion == null) {
                String tpVal = deal.getTipoValor();
                if (tpVal == null) {
                    tpVal = fechaValor;
                }
                fechaLiquidacion = tpVal.equals(Constantes.VFUT) ? fechasValor[Num.I_4] :
                        tpVal.equals(Constantes.HR72) ? fechasValor[Num.I_3] :
                                tpVal.equals(Constantes.SPOT) ? fechasValor[2] :
                                        tpVal.equals(Constantes.TOM) ?
                                                fechasValor[1] : fechasValor[0];
            }
            DealDetalle det = new DealDetalle();
            det.setMonto(monto);
            det.setDivisa(getSicaServiceData().findDivisa(idDivisa));
            det.setFactorDivisa(factorDivisa);
            det.setRecibimos(recibimos);
            LimitesRestriccionOperacion limRestOper = getLimitesActualizados(deal);
            if (limRestOper != null && !limRestOper.getTipoExcepcion().equals(PersonaListaBlanca.
            		EXCEPCION_SHCP) && getTicket() != null && getRequestCycle().
            		getRequestContext().getRequest().getRemoteAddr() != null &&
            		((Visit) getVisit()).getUser() != null) {
            	getSicaServiceData().validaLimitesRestriccionOperacion(deal, det,
            			claveFormaLiquidacion, getLimitesActualizados(deal), null, null,
            			fechaLiquidacion, true);
            }
    }
    
    /**
     * Levanta una excepci&oacute;n Si el contrato no tiene asignado un ejecutivo o si el promotor
     * asignado se encuentra en la jerarqu&iacute;a de usuarios del sica y el promotor asignado no
     * pertenece al &aacute;rea de Ixe Directo.
     *
     * @param params Los par&aacute;metros del servicio de la petici&oacute;n.
     */
    private void validarIxeDirecto(Object[] params) {
        String noCta = (String) params[1];
        SicaServiceData ssd = getSicaServiceData();
        EmpleadoIxe promotor = ssd.findPromotorByContratoSica(noCta);
        List promotoresIxeDirecto = ssd.findEjecutivosIxeDirecto();
        for (Iterator it = promotoresIxeDirecto.iterator(); it.hasNext();) {
            Persona empleadoIxe = (Persona) it.next();
            if (promotor.getIdPersona().equals(empleadoIxe.getIdPersona())) {
                return;
            }
        }
        List promotoresJerarquia = getSicaServiceData().findAllPromotoresSICA(
                ((SupportEngine) getEngine()).getApplicationName());
        for (Iterator it = promotoresJerarquia.iterator(); it.hasNext();) {
            EmpleadoIxe empleadoIxe = (EmpleadoIxe) it.next();
            if (empleadoIxe.getIdPersona().equals(promotor.getIdPersona())) {
                throw new SicaException("Por favor contacte al promotor " + promotor.
                        getNombreCompleto() + " para que sea \u00e9l quien atienda al cliente " +
                        params[Num.I_3] + " con Contrato SICA " + noCta + ".");
            }
        }
        // Si llega a este punto, el contrato est&aacute; asignado a alguien que no opera el sica,
        // entonces el ejecutivo de ixe directo puede operar el contrato.
    }

	/**
     * Regresa el arreglo con los estados de operaci&oacute;n normal y operaci&oacute;n restringida.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[]{Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
                Estado.ESTADO_OPERACION_VESPERTINA};
    }

    /**
     * Define si el sistema se encuentra en Horario Vespertino.
     *
     * @return boolean
     */
    protected boolean isHorarioVespertino() {
        return getEstadoActual().getIdEstado() == Estado.ESTADO_OPERACION_VESPERTINA;
    }

    /**
     * Crea la instancia de deal. Asigna la mesa y canal, y revisa si ser&aacute; necesaria la
     * autorizaci&oacute;n por falta de documentaci&oacute;n.
     *
     * @param params El arreglo de par&aacute;metros del servicio HTTP.
     * @param cycle  El ciclo de la p&aacute;gina.
     */
    private void crearDeal(Object[] params, IRequestCycle cycle) {
        Deal deal = new Deal();
        deal.setUsuario(((Visit) getVisit()).getUser());
        deal.setGrupoTrabajo(((Visit) getVisit()).getGrupoTrabajo());
        try {
            deal.setTipoDeal(params[2].toString());
            Canal canal = getSicaServiceData().findCanal(((Visit) getVisit()).getIdCanal());
            CanalMesa cm = new CanalMesa(canal, canal.getMesaCambio());
            deal.setCanalMesa(cm);
            // Limpiamos la lista de Clientes:
            ((ConsultaClientes) cycle.getPage("ConsultaClientes")).limpiar();
            ContratoDireccionesService cds = (ContratoDireccionesService)
                    getApplicationContext().getBean("contratoDireccionesService");
            cds.asignarContrato((String) params[1], deal,
                    getPizarronServiceData().isValorFuturoHabilitado());
        }
        catch (SicaException e) {
            debug(e);
            Mensaje nextPage = (Mensaje) getRequestCycle().getPage("Mensaje");
            nextPage.setMensaje(e.getMessage());
            nextPage.setTipo(Mensaje.TIPO_ADVERTENCIA);
            getRequestCycle().activate(nextPage);
        }
        catch (Exception e) {
            debug(e);
        }
        setDeal(deal);
//        if (((Visit) getVisit()).isGuardia()) {
//            throw new SicaException("El usuario de guardia no est\u00e1 facultado "
//                    + "para capturar deals.");
//        }
        setModoInsercion(true);
    }

    /**
     * Determina si es posible capturar una operaci&oacute;n.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void definirContrato(IRequestCycle cycle) {
        if (isCapturableContratoPorFecha()) {
        	SeleccionContrato seleccionContratoPage = (SeleccionContrato) cycle.getPage("SeleccionContrato");
        	if(SiNoType.TRUE.equals(getDeal().getEspecial()) && getDeal().getPromotor() != null && getDeal().getContratoSica() == null) {
        		seleccionContratoPage.setPromotorIdPersona(getDeal().getPromotor().getIdPersona());
        		debug("Se ha seteado el id promotor a SeleccionContrato ...");
        	} else {
        		seleccionContratoPage.setPromotorIdPersona(null);
        	}
            seleccionContratoPage.activate(cycle);
        }
        else {
            getDelegate().record("El tiempo l\u00edmite para la Captura del Contrato expir\u00f3.",
                    null);
        }
    }
    
    /**
     * Listener para aceptar un deal de tipo de cambio especial.
     *
     * @param cycle the cycle
     */
    public void aceptarTcEspecial(IRequestCycle cycle) {
    	Visit visit = (Visit)getVisit();
    	if(visit == null || visit.getUser() == null) {
    		getDelegate().record("Error al obtener el usuario firmado", null);
    	}
    	Integer idPersonaVisit = visit.getUser().getIdPersona();
    	if(getDeal() == null || getDeal().getPromotor() == null) {
    		getDelegate().record("Error al obtener el promotor del deal", null);
    	}
    	Integer idPersonaPromotorAsignado = getDeal().getPromotor().getIdPersona();
    	
    	if(idPersonaVisit != null && idPersonaVisit.equals(idPersonaPromotorAsignado)) {
	    	getDeal().setEstatusEspecial(Deal.STATUS_ESPECIAL_TC_TOMADO);
	    	getSicaServiceData().storeDeal(getDeal());
	    	setMensajearApplet(true);
	    	debug("Aceptado deal de Tc Especial: " + getDeal().getEstatusEspecial());
    	} else {
    		getDelegate().record("Para aceptar el Deal de TC Especial el promotor asignado debe ser el mismo que el promotor actual", null);
    	}
    }

    /**
     * Regresa un mapa con una llave 'deal' utilizado para el script TipoDeal.
     *
     * @return Map.
     */
    public Map getTipoDealMap() {
        Map mapa = new HashMap();
        mapa.put("deal", getDeal());
        return mapa;
    }

    /**
     * Marca el detalle seleccionado con status de cancelado. Como s&oacute;lo se pueden cancelar
     * detalles de deal en pesos, no es necesario afectar la posici&oacute;n.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void delete(IRequestCycle cycle) {
        int idDealDetalle = ((Integer) cycle.getServiceParameters()[0]).intValue();
        for (Iterator it = getDeal().getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (det.getIdDealPosicion() == idDealDetalle) {
                if (!Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
                    if (isHorarioVespertino()) {
                        getDelegate().record("No es posible eliminar el detalle para una divisa " +
                                "en Horario Vespertino.", null);
                        break;
                    }
                    getWorkFlowServiceData().wfSolicitarCancelacionDetalleDeal(getTicket(), det,
                            ((Visit) getVisit()).getUser());
                }
                else {
                    det.setStatusDetalleDeal(DealDetalle.STATUS_DET_CANCELADO);
                    getSicaServiceData().update(det);
                }
                break;
            }
        }
    }

    /**
     * Llama a la p&aacute;gina <code>CapturarFormaLiquidacion</code> y la activa en un popup para
     * capturar un nuevo detalle de liquidaci&oacute;n en pesos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see CapturarFormaLiquidacion
     */
    public void insertarFormaLiquidacion(IRequestCycle cycle) {
        CapturarFormaLiquidacion nextPage = (CapturarFormaLiquidacion) cycle.
                getPage("CapturarFormaLiquidacion");

        nextPage.setRecibimos(((Boolean) cycle.getServiceParameters()[0]).booleanValue());
        nextPage.setCantidad(Math.abs(getDeal().getBalance()));
        nextPage.setMontoOriginal(Math.abs(getDeal().getBalance()));
        cycle.activate(nextPage);
    }

    /**
     * Activa la p&aacute;gina <code>Mensaje</code> y presenta un mensaje al usuario de que el deal
     * fue actualizado exitosamente.
     */
    private void mostrarConfirmacion() {
        Mensaje nextPage = (Mensaje) getRequestCycle().getPage("Mensaje");
        
        nextPage.setMensaje("El deal con n&uacute;mero " + getDeal().getIdDeal() +
                " fue actualizado con &eacute;xito.");
        nextPage.setTipo(Mensaje.TIPO_AVISO);
        getRequestCycle().activate(nextPage);
    }

    /**
     * Regresa false, o si la fecha de captura ocurri&oacute; en un d&iacute; anterior, si el deal
     * no se encuentra en proceso de captura, y true si el evento de sobreprecio es normal.
     *
     * @return boolean.
     */
    public boolean isSolAutSobrePrecioVisible() {
        Deal deal = getDeal();

        if (deal.getFechaCaptura().getTime() < DateUtils.inicioDia().getTime()) {
            return false;
        }
        if (isHorarioVespertino()) {
            return false;
        }
        if (deal.tieneAutPendientesHorario() || deal.tieneAutPendientesMonto() ||
                deal.tieneSolCancPendiente() || deal.tieneSolCancPendientesDetalles() ||
                deal.tieneSolModifPendiente() || deal.tieneSolModifPendientesDetalles()) {
            return false;
        }
        if (!Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(deal.getStatusDeal())) {
            return false;
        }
        if (deal.isDealBalanceo() || deal.isDealCorreccion()) {
            return false;
        }
        boolean hayDetallesCapturados = false;
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle detalle = (DealDetalle) it.next();
            if (!DealDetalle.STATUS_DET_CANCELADO.equals(detalle.getStatusDetalleDeal())) {
                hayDetallesCapturados = true;
                break;
            }
        }
        return hayDetallesCapturados &&
                !Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_SOBREPRECIO));
    }

    /**
     * Marca como solicitud de autorizaci&oacute;n el evento de sobreprecio.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void solAutSobreprecio(IRequestCycle cycle) {
        try {
            int idDeal = getDeal().getIdDeal();
            getWorkFlowServiceData().solicitarAutorizacionSobreprecio(idDeal);
            setDeal(getSicaServiceData().findDeal(idDeal));
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * Submit de la forma. De acuerdo a la propiedad <code>submitMode</code> se conoce cu&aacute;l
     * fue la acci&oacute;n que dispar&oacute; el submit. De acuerdo a este valor se toma la
     * acci&oacute;n correspondiente.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see #solicitarCancelacion(IRequestCycle).
     */
    public void save(IRequestCycle cycle) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
        	getBean("delegate");
        SicaServiceData sd = getSicaServiceData();
        try {
            Deal deal = getDeal();
            if (EncabezadoDeal.SUBMIT_MODE_MENSAJERIA.equals(getSubmitMode())) {
                if (deal.isMensajeria()) {
                    capturarDatosEnvio(cycle, true);
                }
                else {
                    if (deal.getIdDeal() > 0) {
                        getWorkFlowServiceData().actualizarDatosDeal(getTicket(), deal);
                    }
                }
            }
            else if (EncabezadoDeal.SUBMIT_MODE_FACTURA.equals(getSubmitMode())) {
                if (getDeal().isConFactura()) {
                    capturarDatosFacturacion(cycle, true);
                }
                else {
                    getDeal().setCambioRfc(Deal.EV_NORMAL);
                    getDeal().setCambioEmail(Deal.EV_NORMAL);
                    getDeal().setEmailFactura(null);
                    getDeal().setEmailFacturaOtro(null);
                    getDeal().setDirFacturaMensajeria(null);
                    getDeal().setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
                    if (deal.getIdDeal() > 0) {
                        getWorkFlowServiceData().actualizarDatosDeal(getTicket(), deal);
                        getWorkFlowServiceData().terminarActividadesParaDeal(getDeal(),
                                Actividad.RES_SOL_MODIF, ((Visit) getVisit()).getUser());
                    }
                }
            }
            else if (EncabezadoDeal.SUBMIT_MODE_PAGO_ANTICIPADO.equals(getSubmitMode())) {
                getWorkFlowServiceData().marcarPagAntTomaEnFirme(getTicket(), getDeal(),
                        getDeal().getUsuario(), true);
            }
            else if (EncabezadoDeal.SUBMIT_MODE_TOMA_EN_FIRME.equals(getSubmitMode())) {
                getWorkFlowServiceData().marcarPagAntTomaEnFirme(getTicket(), getDeal(),
                        getDeal().getUsuario(), false);
            }
            else if (EncabezadoDeal.SUBMIT_MODE_ENVIAR_CLIENTE.equals(getSubmitMode())) {
                if (getDeal().getIdDeal() > 0) {
                    getDeal().setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
                    getWorkFlowServiceData().actualizarDatosDeal(getTicket(), deal);
                    getWorkFlowServiceData().terminarActividadesParaDeal(getDeal(),
                            Actividad.RES_SOL_MODIF, ((Visit) getVisit()).getUser());
                }
                if (getDeal().isEnviarAlCliente()) {
                    capturarDatosComprobante(cycle, true);
                }
            }
            else if (EncabezadoDeal.SUBMIT_MODE_CANCELAR.equals(getSubmitMode())) {
                solicitarCancelacion(cycle);
                setLevel(1);
                delegate.record("La solicitud de cancelaci\u00f3n ha sido enviada. " +
                		"El estado del deal cambiar\u00E1 " +
                		"una vez autorizada dicha solicitud.", null);
            }
            else if (EncabezadoDeal.SUBMIT_MODE_PROCESAR.equals(getSubmitMode())) {
            	if ((getDeal().isPagoAnticipado() || getDeal().isTomaEnFirme()) &&
                        getLinea() == null) {
                    throw new SicaException("No se ha solicitado una l\u00ednea de cr\u00e9dito.");
                }
                try {
                	asignarCrADeal(deal);
                    asignarClaveDeReferencia(deal);
                    
                    if(StringUtils.isEmpty(deal.getMetodoPago()) && deal.isConFactura()){
                    	deal.setMetodoPago(CapturaMetodoPagoDeal.MP_NA);
                    	sd.update(deal);
                    }
                    
                    getSicaWorkFlowService().wfIniciarProcesoDeal(getTicket(), deal.getIdDeal());
                    this.getPersonaService().isValidaInformacionGeneralPersona(deal);//Envia correo
                    mostrarConfirmacion();
                }
                catch (RemoteAccessException e) {
                    warn("Error al procesar el deal " + getDeal().getIdDeal(), e);
                    throw SicaExceptionHelper.transformar(e);
                }
            }
            else if (EncabezadoDeal.SUBMIT_MODE_SALVAR.equals(getSubmitMode())) {
                if (!deal.isConFactura()) {
                    deal.setRfcFactura(null);
                    deal.setNombreFactura(null);
                    deal.setDirFactura(null);
                }
                sd.storeDeal(deal);
                mostrarConfirmacion();
            }
        }
        catch (SicaException e) {
            debug(e);
            getDelegate().record(e.getMessage(), null);
        }
    }
    
    /**
     * Regresa true si hay un detalle de SPEI en la parte de recibimos.
     *
     * @return boolean.
     */
    public boolean isSpeiRecibimosPresente() {
        List dets = getDeal().getRecibimos();
        for (Iterator it = dets.iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (det.getMnemonico() != null &&
                    det.getMnemonico().indexOf(Constantes.SPEI) >= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Muestra la p&aacute;gina <code>CapturaDatosFacturacion</code> y le pasa el deal que
     * est&aacute; siendo capturado.
     *
     * @param cycle      El ciclo de la p&aacute;gina.
     * @param primeraVez True si viene del checkbox de factura.
     * @see com.ixe.ods.sica.pages.deals.CapturaDatosFacturacion
     */
    private void capturarDatosFacturacion(IRequestCycle cycle, boolean primeraVez) {
        CapturaDatosFacturacion nextPage = (CapturaDatosFacturacion) cycle.
                getPage("CapturaDatosFacturacion");
        nextPage.setModoPrimeraVez(primeraVez);
        nextPage.setDeal(getDeal());
        nextPage.activate(cycle);
    }

    /**
     * Muestra la p&aacute;gina <code>CapturaDatosComprobante</code> y le pasa el deal que
     * est&aacute; siendo capturado.
     *
     * @param cycle      El ciclo de la p&aacute;gina.
     * @param primeraVez True si viene del checkbox de comprobante.
     * @see com.ixe.ods.sica.pages.deals.CapturaDatosComprobante
     */
    private void capturarDatosComprobante(IRequestCycle cycle, boolean primeraVez) {
        CapturaDatosComprobante nextPage = (CapturaDatosComprobante) cycle.
                getPage("CapturaDatosComprobante");
        nextPage.setModoPrimeraVez(primeraVez);
        nextPage.setDeal(getDeal());
        nextPage.activate(cycle);
    }

    /**
     * Muestra la p&aacute;gina <code>CapturaDatosFacturacion</code> y le pasa el deal que
     * est&aacute; siendo capturado.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see #capturarDatosFacturacion(org.apache.tapestry.IRequestCycle, boolean).
     */
    public void capturarDatosFacturacion(IRequestCycle cycle) {
        capturarDatosFacturacion(cycle, false);
    }

    /**
     * Muestra la p&aacute;gina <code>CapturaDatosFComprobante</code> y le pasa el deal que
     * est&aacute; siendo capturado.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see #capturarDatosFacturacion(org.apache.tapestry.IRequestCycle, boolean).
     */
    public void capturarDatosComprobante(IRequestCycle cycle) {
        capturarDatosComprobante(cycle, false);
    }

    /**
     * Muestra la p&aacute;gina <code>CapturaDatosEnvio</code> y le pasa el deal que est&aacute;
     * siendo capturado.
     *
     * @param cycle      El ciclo de la p&aacute;gina.
     * @param primeraVez True si viene del checkbox de mensajer&iacute;a.
     * @see com.ixe.ods.sica.pages.deals.CapturaDatosEnvio
     */
    private void capturarDatosEnvio(IRequestCycle cycle, boolean primeraVez) {
        CapturaDatosEnvio nextPage = (CapturaDatosEnvio) cycle.getPage("CapturaDatosEnvio");
        nextPage.setDeal(getDeal());
        nextPage.setModoPrimeraVez(primeraVez);
        nextPage.setPaginaAnterior(getPageName());
        nextPage.activate(cycle);
    }

    /**
     * Muestra la p&aacute;gina <code>CapturaDatosEnvio</code> y le pasa el deal que est&aacute;
     * siendo capturado.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see #capturarDatosEnvio(org.apache.tapestry.IRequestCycle, boolean).
     */
    public void capturarDatosEnvio(IRequestCycle cycle) {
        capturarDatosEnvio(cycle, false);
    }

    /**
     * Permite saber si a&uacute;n se puede capturar un contrato de acuerdo a
     * si la fecha y hora actuales son menores a la Fecha Limite Captura Contrato.
     *
     * @return boolean.
     */
    public boolean isCapturableContratoPorFecha() {
        return getDeal().getFechaLimiteCapturaContrato() == null ||
                new Date().compareTo(getDeal().getFechaLimiteCapturaContrato()) < 0;
    }

    /**
     * Regresa true si la fecha de captura del deal es igual a la fecha actual del sistema y si su
     * status es 'AL'.
     *
     * @return boolean.
     */
    public boolean isDivisasCapturables() {
        return DateUtils.inicioDia(getDeal().getFechaCaptura()).equals(DateUtils.inicioDia()) &&
                Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(getDeal().getStatusDeal());
    }

    /**
     * Regresa true si es v&aacute;lida la captura de detalles de divisas en el deal.
     *
     * @return boolean.
     */
    public boolean isValidaCapturaDivisas() {
        boolean isValidoUsuarioEstado = false;

        if (((Visit) getVisit()).isGuardia()) {
            if (Estado.ESTADO_OPERACION_NORMAL == getEstadoActual().getIdEstado()) {
                isValidoUsuarioEstado = true;
            }
        }
        else {
            if (Estado.ESTADO_OPERACION_NORMAL == getEstadoActual().getIdEstado() ||
                    Estado.ESTADO_OPERACION_RESTRINGIDA == getEstadoActual().getIdEstado()) {
                isValidoUsuarioEstado = true;
            }
        }
        return isValidoUsuarioEstado && isDivisasCapturables();
    }

    /**
     * M&eacut;etodo que obtiene el DataSource para reporte del Deal.
     *
     * @param id La llave primaria del registro.
     * @return JRDataSource.
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider
     */
    public JRDataSource getDataSource(String id) {
        try {
            JRDataSource comprobanteDealDataSource =
                    getComprobanteDealDataSource(getDeal().getIdDeal(),
                            new Integer(id));
            return comprobanteDealDataSource;
        }
        catch (SicaException e) {
            //debug(e);
            throw new ApplicationRuntimeException(e);
        }
    }

    /**
     * Nivel de los mensajes al usuario: -1 Error, 0 Warning, 1 Exito.
     *
     * @param level El valor a asignar.
     */
    public abstract void setLevel(int level);

    /**
     * Regresa el valor de area.
     *
     * @return boolean.
     */
    public abstract boolean getArea();

    /**
     * Fija el valor de area.
     *
     * @param area El valor a asignar.
     */
    public abstract void setArea(boolean area);

    /**
     * Establece el valor de mensajearApplet.
     *
     * @param mensajearApplet El valor a asignar.
     */
    public abstract void setMensajearApplet(boolean mensajearApplet);
    
    /**
     * Regresa el valor de paginaRegreso.
     *
     * @return String.
     */
    public abstract String getPaginaRegreso();

    /**
     * Fija el valor de paginaRegreso.
     *
     * @param paginaRegreso El valor a asignar.
     */
    public abstract void setPaginaRegreso(String paginaRegreso);

    /**
     * Regresa el valor de submitMode.
     *
     * @return String.
     */
    public abstract String getSubmitMode();

    /**
     * Establece el valor del Estado de la Linea de Credito asociada al Cliente.
     *
     * @param estadoLineaCredito Estado de la Linea de credito
     */
	public abstract  void setEstadoLineaCredito(String estadoLineaCredito);
	
	/**
	 * Obtiene el Estado de la Linea de credito asociada al Cliente.
	 *
	 * @return String Estado de la Linea de Credito
	 */
	public abstract String getEstadoLineaCredito();
    
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

    /** Constante para redirijir a la pantalla de ConsultaPendientesDia. */
    public static final int OPCION_EDITAR_DEALS_PENDIENTE_DIA = 6;

    /**
     * Constante para indicar la opci&oacute;n de inserci&oacute;n de detalle por la Mec&aacute;nica
     * de Neteos.
     */
    public static final int OPCION_INSERTAR_DETALLE_NETEO = 7;

    /**
     * Constante para el par&acute;metro de operacion al insertar un detalle de deal.
     */
    private static final int PARAM_INSDET_OPER = 1;

    /**
     * Constante para el par&acute;metro de fecha valor al insertar un detalle de deal.
     */
    private static final int PARAM_INSDET_FECHA_VALOR = 2;

    /**
     * Constante para el par&acute;metro de clave forma liq al insertar un detalle de deal.
     */
    private static final int PARAM_INSDET_CLAVE_FORMA_LIQ = 3;

    /**
     * Constante para el par&acute;metro de id divisa al insertar un detalle de deal.
     */
    private static final int PARAM_INSDET_ID_DIVISA = 4;

    /**
     * Constante para el par&acute;metro de tipo de cambio de mesa al insertar un detalle de deal.
     */
    private static final int PARAM_INSDET_TCM = 5;

    /**
     * Constante para el par&acute;metro de monto al insertar un detalle de deal.
     */
    private static final int PARAM_INSDET_MONTO = 6;

    /**
     * Constante para el par&acute;metro de tipo de cambio cliente al insertar un detalle de deal.
     */
    private static final int PARAM_INSDET_TCC = 7;

    /**
     * Constante para el par&acute;metro de precio referencia Mid Spot al insertar un detalle de deal.
     */
    private static final int PARAM_INSDET_PRE_REF_MID_SPOT = 8;
    
    /**
     * Constante para el par&acute;metro de precio referencia Spot al insertar un detalle de deal.
     */
    private static final int PARAM_INSDET_PRE_REF_SPOT = 9;

    /**
     * Constante para el par&acute;metro de Factor Divisa al insertar un detalle de deal.
     */
    private static final int PARAM_INSDET_FACT_DIV = 10;

    /**
     * Constante para el par&acute;metro de idSpread al insertar un detalle de deal.
     */
    private static final int PARAM_INSDET_ID_SPREAD = 11;
    
    /**
     * Constante para el par&acute;metro de idPrecioRef al insertar un detalle de deal.
     * 
     * @deprecated Se debe utilizar el valor directo del Precio Referencia.
     */
    private static final int PARAM_INSDET_ID_PRECIO_REF = 12;

    /**
     * Constante para el par&acute;metro de idFactorDivisa al insertar un detalle de deal.
     * @deprecated Se debe utilizar el valor directo del Factor Divisa.
     */
    private static final int PARAM_INSDET_ID_FACT_DIV = 13;

    /**
	 * Constante para el par&acute;metro de operacion al insertar un detalle de
	 * deal (COMPRA) sobre neteo.
	 */
	private static final int PARAM_INSDET_OPER_CPRA = 1;

	/**
	 * Constante para el par&acute;metro de fecha valor al insertar un detalle
	 * de deal (COMPRA) sobre neteo.
	 */
	private static final int PARAM_INSDET_FECHA_VALOR_CPRA = 2;

	/**
	 * Constante para el par&acute;metro de clave forma liq al insertar un
	 * detalle de deal (COMPRA) sobre neteo.
	 */
	private static final int PARAM_INSDET_CLAVE_FORMA_LIQ_CPRA = 3;

	/**
	 * Constante para el par&acute;metro de id divisa al insertar un detalle de
	 * deal (COMPRA) sobre neteo.
	 */
	private static final int PARAM_INSDET_ID_DIVISA_CPRA = 4;

	/**
	 * Constante para el par&acute;metro de tipo de cambio de mesa al insertar
	 * un detalle de deal (COMPRA) sobre neteo.
	 */
	private static final int PARAM_INSDET_TCM_CPRA = 5;

	/**
	 * Constante para el par&acute;metro de monto al insertar un detalle de
	 * deal (COMPRA) sobre neteo.
	 */
	private static final int PARAM_INSDET_MONTO_CPRA = 6;

	/**
	 * Constante para el par&acute;metro de tipo de cambio cliente al insertar
	 * un detalle de deal (COMPRA) sobre neteo.
	 */
	private static final int PARAM_INSDET_TCC_CPRA = 7;
	
	/**
	 * Constante para el par&acute;metro de precio referencia Mid Spot 
	 * al insertar un detalle de deal (COMPRA) sobre neteo.
	 */
	private static final int PARAM_INSDET_PRE_REF_MID_SPOT_CPRA = 8;
	
	/**
	 * Constante para el par&acute;metro de precio referencia Mid Spot
	 * al insertar un detalle de deal (COMPRA) sobre neteo.
	 * 
	 */
	private static final int PARAM_INSDET_PRE_REF_SPOT_CPRA = 9;
	
	/**
	 * Constante para el par&acute;metro de Factor Divisa al insertar un
	 * detalle de deal (COMPRA) sobre neteo.
	 */
	private static final int PARAM_INSDET_FACT_DIV_CPRA = 10;

	/**
	 * Constante para el par&acute;metro de idSpread al insertar un detalle de
	 * deal (COMPRA) sobre neteo.
	 */
	private static final int PARAM_INSDET_ID_SPREAD_CPRA = 11;
	
	/**
	 * Constante para el par&acute;metro de idPrecioRef al insertar un detalle
	 * de deal (COMPRA) sobre neteo.
	 * 
	 * @deprecated Se utiliza la posici&oacute;n del valor directo del precio 
	 */
	private static final int PARAM_INSDET_ID_PRECIO_REF_CPRA = 12;
	
	/**
	 * Constante para el par&acute;metro de idFactorDivisa al insertar un
	 * detalle de deal (COMPRA) sobre neteo.
	 * 
	 * @deprecated Se debe utilizar el valor directo del Factor Divisa para la Compra.
	 */
	private static final int PARAM_INSDET_ID_FACT_DIV_CPRA = 13;

	/**
	 * Constante para el par&acute;metro de operacion al insertar un detalle de
	 * deal (VENTA) sobre neteo.
	 */
	private static final int PARAM_INSDET_OPER_VTA = 14;

	/**
	 * Constante para el par&acute;metro de fecha valor al insertar un detalle
	 * de deal (VENTA) sobre neteo.
	 */
	private static final int PARAM_INSDET_FECHA_VALOR_VTA = 15;

	/**
	 * Constante para el par&acute;metro de clave forma liq al insertar un
	 * detalle de deal (VENTA) sobre neteo.
	 */
	private static final int PARAM_INSDET_CLAVE_FORMA_LIQ_VTA = 16;

	/**
	 * Constante para el par&acute;metro de id divisa al insertar un detalle de
	 * deal (VENTA) sobre neteo.
	 */
	private static final int PARAM_INSDET_ID_DIVISA_VTA = 17;

	/**
	 * Constante para el par&acute;metro de tipo de cambio de mesa al insertar
	 * un detalle de deal (VENTA) sobre neteo.
	 */
	private static final int PARAM_INSDET_TCM_VTA = 18;

	/**
	 * Constante para el par&acute;metro de monto al insertar un detalle de
	 * deal (VENTA) sobre neteo.
	 */
	private static final int PARAM_INSDET_MONTO_VTA = 19;

	/**
	 * Constante para el par&acute;metro de tipo de cambio cliente al insertar
	 * un detalle de deal (VENTA) sobre neteo.
	 */
	private static final int PARAM_INSDET_TCC_VTA = 20;

	/**
	 * Constante para el par&acute;metro de precio referencia Mis Spot 
	 * al insertar un detalle de deal (VENTA) sobre neteo.
	 */
	private static final int PARAM_INSDET_PRE_REF_MID_SPOT_VTA = 21;
	
	/**
	 * Constante para el par&acute;metro de precio referencia Spot 
	 * al insertar un detalle de deal (VENTA) sobre neteo.
	 */
	private static final int PARAM_INSDET_PRE_REF_SPOT_VTA = 22;
	
	/**
	 * Constante para el par&acute;metro de Factor Divisa al insertar un
	 * detalle de deal (VENTA) sobre neteo.
	 */
	private static final int PARAM_INSDET_FACT_DIV_VTA = 23;

	/**
	 * Constante para el par&acute;metro de idSpread al insertar un detalle de
	 * deal (VENTA) sobre neteo.
	 */
	private static final int PARAM_INSDET_ID_SPREAD_VTA = 24;
	
	/**
	 * Constante para el par&acute;metro de idPrecioRef al insertar un detalle
	 * de deal (VENTA) sobre neteo.
	 * 
	 * @deprecated Se utiliza el valor directo del precio referencia.
	 */
	private static final int PARAM_INSDET_ID_PRECIO_REF_VTA = 25;
	
	/**
	 * Constante para el par&acute;metro de idFactorDivisa al insertar un
	 * detalle de deal (VENTA) sobre neteo.
	 * 
	 * @deprecated Se debe utlizar le valor directo del Factor Divisa para la Venta.
	 */
	private static final int PARAM_INSDET_ID_FACT_DIV_VTA = 26;
}
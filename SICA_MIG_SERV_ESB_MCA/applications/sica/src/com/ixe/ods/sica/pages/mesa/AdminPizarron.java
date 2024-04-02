/*
 * $Id: AdminPizarron.java,v 1.20.26.4.10.1.42.2 2020/12/03 21:59:08 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.mesa;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.springframework.remoting.RemoteAccessException;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.MontoMaximoSucursal;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.Spread;
import com.ixe.ods.sica.model.TcMinMaxTeller;
import com.ixe.ods.sica.model.TipoPizarron;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.dto.DealsPendientesDto;
import com.ixe.ods.sica.Visit;

/**
 * Cat&aacute;logo que permite modificar el precio de referencia, los factores
 * por divisa y los spreads por producto, canal, mesa y divisa.
 *
 * @author Jean C. Favila, Javier C&oacute;rdova
 * @version  $Revision: 1.20.26.4.10.1.42.2 $ $Date: 2020/12/03 21:59:08 $
 */
public abstract class AdminPizarron extends SicaPage {

    /**
     * Fija el precio de referencia en el &uacute;ltimo que est&aacute; en la base de datos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        if (Estado.ESTADO_INICIO_DIA == getEstadoActual().getIdEstado()) {
            setDealsPendientes(getSicaServiceData().findDealsTeller());
            if (!getDealsPendientes().isEmpty()) {
                setTotalesOperaciones(
                        obtenerTotalesOperacionesDealsPendientes(getDealsPendientes()));
                setDivisasDealsPendientes(obtenerDivisasDealsPendientes(getTotalesOperaciones()));
            }
            else {
                setTotalesOperaciones(new ArrayList());
                setDivisasDealsPendientes(null);
            }
        }
        else {
            setDealsPendientes(new ArrayList());
            setTotalesOperaciones(new ArrayList());
            setDivisasDealsPendientes(new String[]{});
        }
        limpiarTodo();
        setFactorAutomatico("S".equals(getSicaServiceData().
                findParametro(ParametroSica.FACTOR_AUTOMATICO).getValor()));
        setConfirmar(false);
        ParametroSica p = getSicaServiceData().findParametro(ParametroSica.SLACK_PRECIO_REFERENCIA);
        setSlack(Double.valueOf(p.getValor()));
        setPrecioReferencia(getSicaServiceData().findPrecioReferenciaActual());
		asignarFactoresDivisa(ordenarFactoresDivisa(
				getSicaServiceData().findFactoresDivisaActuales()));
		ArrayList al = new ArrayList();
		for (Iterator it = getFactoresDivisa().iterator(); it.hasNext();) {
			FactorDivisaActual fd = (FactorDivisaActual) it.next();
			FactorDivisaActual fdNuevo = new FactorDivisaActual();
			fdNuevo.getFacDiv().setFactor(fd.getFacDiv().getFactor());
			fdNuevo.getFacDiv().setFactorCompra(fd.getFacDiv().getFactorCompra());
			fdNuevo.getFacDiv().setFactorTmp(fd.getFacDiv().getFactorTmp());
			fdNuevo.getFacDiv().setFactorCompraTmp(fd.getFacDiv().getFactorCompraTmp());
            fdNuevo.getFacDiv().setCarry(fd.getFacDiv().getCarry());
            fdNuevo.getFacDiv().setSpreadReferencia(fd.getFacDiv().getSpreadReferencia());
            fdNuevo.getFacDiv().setSpreadCompra(fd.getFacDiv().getSpreadCompra());
			fdNuevo.getFacDiv().setSlack(fd.getFacDiv().getSlack());
			al.add(fdNuevo);
		}
		setFactoresDivisaTemporal(al);
		setMesaCambioSeleccionadaMontosMaximos(getMesaCambioSeleccionadaMontosMaximos());
		setOrigenVariacion(getSicaServiceData().findParametro(ParametroSica.FDD_ORIGEN_VARIACION).getValor());
    }

    /**
     * Regresa un arreglo de estados del sistema v&aacute;lidos para esta p&aacute;gina:
     * ESTADO_INICIO_DIA, ESTADO_OPERACION_NORMAL, ESTADO_OPERACION_RESTRINGIDA,
     * ESTADO_OPERACION_VESPERTINA.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[]{
            Estado.ESTADO_INICIO_DIA, Estado.ESTADO_OPERACION_NORMAL,
            Estado.ESTADO_OPERACION_RESTRINGIDA, Estado.ESTADO_OPERACION_VESPERTINA };
    }

    /**
     * Ordena los factores de divisa posicionando al principio el caso DOLAR / DOLAR.
     *
     * @param factoresDivisa La lista de factores de divisa.
     * @return List.
     */
    private List ordenarFactoresDivisa(List factoresDivisa) {
        Collections.sort(factoresDivisa, new Comparator() {
            public int compare(Object o1, Object o2) {
                FactorDivisaActual f1 = (FactorDivisaActual) o1;
                FactorDivisaActual f2 = (FactorDivisaActual) o2;
                if (Divisa.DOLAR.equals(f1.getFacDiv().getFromDivisa().getIdDivisa()) &&
                        Divisa.DOLAR.equals(f1.getFacDiv().getToDivisa().getIdDivisa())) {
                    return -1;
                }
                else if (Divisa.DOLAR.equals(f2.getFacDiv().getFromDivisa().getIdDivisa()) &&
                        Divisa.DOLAR.equals(f2.getFacDiv().getToDivisa().getIdDivisa())) {
                    return 1;
                }
                else if (Divisa.DOLAR.equals(f1.getFacDiv().getToDivisa().getIdDivisa()) &&
                        Divisa.DOLAR.equals(f2.getFacDiv().getToDivisa().getIdDivisa())) {
                    return f1.getFacDiv().getFromDivisa().getOrden() < f2.getFacDiv().
                            getFromDivisa().getOrden() ? -1 : 1;
                }
                else if (Divisa.DOLAR.equals(f1.getFacDiv().getToDivisa().getIdDivisa())) {
                    return -1;
                }
                else if (Divisa.DOLAR.equals(f2.getFacDiv().getToDivisa().getIdDivisa())) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
        });
        return factoresDivisa;
    }

    /**
     * Regresa el ParametroSica con clave ParametroSica.CONF_PRECIO_REF.
     *
     * @return ParametroSica.
     */
    public ParametroSica getParConfPrecioReferencia() {
        return getSicaServiceData().findParametro(ParametroSica.CONF_PRECIO_REF);
    }

    /**
     * Regresa el ParametroSica con clave ParametroSica.CONF_SPREADS.
     *
     * @return ParametroSica.
     */
    public ParametroSica getParConfSpread() {
        return getSicaServiceData().findParametro(ParametroSica.CONF_SPREADS);
    }

    /**
     * Regresa el ParametroSica con clave ParametroSica.CONF_FACTORES_DIV.
     *
     * @return ParametroSica.
     */
    public ParametroSica getParConfFactorDivisa() {
        return getSicaServiceData().findParametro(ParametroSica.CONF_FACTORES_DIV);
    }

    /**
     * Llena la lista de montos m&aacute;ximos para los deals.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cargarMontosMaximos(IRequestCycle cycle) {
    	List l = getSicaServiceData().findMaximoDealByIdMesaCambio(
                getMesaCambioSeleccionadaMontosMaximos().getIdMesaCambio());
    	setRegistros(l);
     }

    /**
     * Actualiza los cambios en la base de datos con respecto a los montos m&aacute;ximos para
     * deals.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void actualizarMontosMaximos(IRequestCycle cycle) {
    	getSicaServiceData().update(getRegistros());
	}

    /**
     * Realiza un broadcast del pizarr&oacute;n para todas las mesas (portafolios) y canales.
     *
     * @see #notificarCambioPizarra(com.ixe.ods.sica.model.TipoPizarron).
     * @throws SicaException Si algo sale mal.
     */
    private void notificarCambioPizarra() throws SicaException {
        getPizarronServiceData().refrescarPizarrones(getTicket(), 
        		getRequestCycle().getRequestContext().getRequest().getRemoteAddr(), ((Visit) getVisit()).getUser());
    }

    /**
     * Publica los cambios en la pizarra por JMS en el t&oacute;pico correspondiente.
     *
     * @param tipoPizarron El tipo de pizarron a limpiar.
     * @throws SicaException Si algo sale mal.
     */
    private void notificarCambioPizarra(TipoPizarron tipoPizarron) throws SicaException {
    	getPizarronServiceData().refrescarPizarron(getTicket(), tipoPizarron, 
    			getRequestCycle().getRequestContext().getRequest().getRemoteAddr(), ((Visit) getVisit()).getUser());
    }

    /**
     * Actualiza los cambios en la base de datos. Llama a <code>notificarCambioPizarra()</code>.
     *
     * @see #notificarCambioPizarra().
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void actualizarPrecioReferencia(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        if (delegate.getHasErrors()) {
            return;
        }
        try {
            if (getModo() == REFRESCAR) {
                setPrecioReferencia(getSicaServiceData().findPrecioReferenciaActual());
                return;
            }
            if (getPrecioReferencia().isActualizacionAutomatica()
                    || getPrecioReferencia().isMidSpot()) {
                ParametroSica p = getSicaServiceData().
                        findParametro(ParametroSica.SLACK_PRECIO_REFERENCIA);
                p.setValor(getCurrencyFormat().format(getSlack().doubleValue()));
                getSicaServiceData().update(p);
            }
            else if(getPrecioReferencia().isActualizacionManual()) {
            	
            	//se obtiene el precio actual
            	double prBase = getSicaServiceData().findPrecioReferenciaActual()
            										.getPreRef().getPrecioSpot().doubleValue();
            	//se obtiene el precio a validar
            	double pr =    	getPrecioReferencia().getPreRef().getPrecioSpot().doubleValue();
            	
            	// se obtiene el porcentaje de variacion permitido
            	double porcVarPr = getPorcVarPr();
            	
            	// se valida el precio de referencia. 
            	if(!isPrecioReferenciaValido(pr, prBase, porcVarPr)) return;
            	
            }
            //validar aqui precio de referencia siempre que sea manual
            
            getSicaServiceData().storePrecioReferencia(getPrecioReferencia()); 
            notificarCambioPizarra();
            if (getModo() == CONFIRMAR) {
                ParametroSica par = getParConfPrecioReferencia();
                par.setValor("0");
                getSicaServiceData().update(par);
                checarVariablesInicioDia();
            }
        }
        catch (SicaException e) {
            warn(e);
            delegate.record(e.getMessage(), null);
        }
        catch (Exception e) {
            warn(e);
            delegate.record("No fue posible actualizar, por favor intente de nuevo.", null);
        }
        finally {
            setPrecioReferencia(getSicaServiceData().findPrecioReferenciaActual());
        }
    }
    
    
    
    /**
	 * Valida la desviacion del precio de referencia especificado
	 * y de ser necesario envía mensajes de error correspondientes. 
	 * 
	 * @param pr
	 *            Precio de referencia a validar
	 * @param prBase
	 *            El precio de referencia contra el que se valida el porcentaje de
	 *            desviacion
	 * 
	 * @return true si pr excede el porcentaje maximo de desviacion contra el
	 *         prBase, false en cualquier otro caso.
	 */
	private boolean isPrecioReferenciaValido(double pr, double prBase,
			double porcVarPr) {
		
		debug("isPrecioReferenciaValido, pr = " + pr + ", prBase=" + prBase
				+ ", porcVarPr=" + porcVarPr );
		
		double desviacionMaxima = Redondeador.redondear6Dec(prBase * porcVarPr
				/ 100.0);
		
		double desviacionActual = Redondeador.redondear6Dec(Math.abs(prBase
				- pr));

		debug("isPrecioReferenciaValido - porcVarPr = " + porcVarPr
				+ ", desviacionMaxima=" + desviacionMaxima
				+ ", desviacionActual=" + desviacionActual);
		
		if (desviacionActual > desviacionMaxima) {
			
			double prLimInferior = prBase - desviacionMaxima;
			double prLimSuperior = prBase + desviacionMaxima;

			String mensaje = MessageFormat.format("El Precio de referencia debe estar entre {0} y {1}",
					new Object[] {
							getCurrencyFormatSix().format(prLimInferior),
							getCurrencyFormatSix().format(prLimSuperior) });

			getDelegate().record(mensaje, null);
			return false;
		}

		return true;
	}
	
	
	/**
	 * Obtiene el porcentaje permitido para modificar el 
	 * Precio de Referencia en modo manual
	 * 
	 * @return <code>double</code> con el valor 
	 * 			del Precio de Referencia.
	 */
	private double getPorcVarPr() {
		
		try {
			String parametro = null;
			
			parametro = getSicaServiceData().findParametro(
					ParametroSica.PORC_VAR_PR).getValor();
			
			if(parametro != null) {
				//tratar de convertir
				double porcVarPr = Double.parseDouble(parametro);
				
				return porcVarPr;
			}
			else {
				throw new SicaException(
						"No se pudo recuperar PORC_VAR_PR");
			}
	
		}
		catch(NumberFormatException nfe) {
			throw new SicaException("PORC_VAR_PR no tiene un dato valido");
		}
				
	}
	
	
    /**
     * Actualiza el par&oacute;metro que define el origen de las variaciones para la generaci&oacute;n 
     * del pizarr&oacute;n seteando el valor correspondiente del radio seleccionado
     * 
     * @param cycle
     */
    public void actualizarOrigenVariacion(IRequestCycle cycle) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
        getBean("delegate");
    	if (delegate.getHasErrors()) {
            return;
        }
    	try {
	    	ParametroSica p = getSicaServiceData().findParametro(ParametroSica.FDD_ORIGEN_VARIACION);
			p.setValor(getOrigenVariacion());
			getSicaServiceData().update(p);
    	} catch(Exception e) {
    		warn(e);
            delegate.record("No fue posible actualizar, por favor intente de nuevo.", null);
    	}
    }

    /**
     * Actualiza los cambios en la base de datos. Llama a <code>notificarCambioPizarra()</code>.
     *
     * @see #notificarCambioPizarra().
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void actualizarPrecioReferenciaValores(IRequestCycle cycle) {
        if (getPrecioReferencia().isActualizacionManual() || (getPrecioReferencia().isMidSpot())) {
        	ParametroSica p = getSicaServiceData().findParametro("SLACK");
        	p.setValor(getSlack() + "");
            getSicaServiceData().update(p);
        }
        setPrecioReferencia(getSicaServiceData().findPrecioReferenciaActual());
    }

    /**
     * Llena la lista de spreads con los que cumplen con los criterios de consulta.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void listarSpreads(IRequestCycle cycle) {
    	setSpreads(getSicaServiceData().findSpreadsActualesByTipoPizarronDivisa(getTipoPizarron(),
                getDivisaSeleccionada().getIdDivisa()));
        ArrayList al = new ArrayList();
        for (Iterator it = getSpreads().iterator(); it.hasNext();) {
            Spread s = (Spread) it.next();
            Spread sNuevo = new Spread();
            sNuevo.getCpaVta().setCompraCash(s.getCpaVta().getCompraCash());
            sNuevo.getCpaVta().setVentaCash(s.getCpaVta().getVentaCash());
            sNuevo.getCpaVta().setCompraTom(s.getCpaVta().getCompraTom());
            sNuevo.getCpaVta().setVentaTom(s.getCpaVta().getVentaTom());
            sNuevo.getCpaVta().setCompraSpot(s.getCpaVta().getCompraSpot());
            sNuevo.getCpaVta().setVentaSpot(s.getCpaVta().getVentaSpot());
            sNuevo.getCpaVta().setCompra72Hr(s.getCpaVta().getCompra72Hr());
            sNuevo.getCpaVta().setVenta72Hr(s.getCpaVta().getVenta72Hr());
            sNuevo.getCpaVta().setCompraVFut(s.getCpaVta().getCompraVFut());
            sNuevo.getCpaVta().setVentaVFut(s.getCpaVta().getVentaVFut());
            al.add(sNuevo);
        }
        setSpreadsTemporal(al);
        fechaMasReciente();
    }

    /**
     * Llena la lista de spreads con los que cumplen con los criterios de consulta y cuyo Canal
     * tiene relacion con BUP Sucursal.
     *
     * @param claveFormaLiquidacion El ID del Canal que tiene relacion con BUP Sucursal.
     */
    public void listarSpreadsBUPSucursal(String claveFormaLiquidacion) {
    	setSpreadsBUPSucursal(getSicaServiceData().
                findSpreadsActualesByMesaSucursalDivisaFormaLiquidacion(
                    getCanalSeleccionado().getMesaCambio().getIdMesaCambio(),
                    getDivisaSeleccionada().getIdDivisa(), claveFormaLiquidacion));
    }

    /**
     * Regresa la fecha m&aacute;s reciente de la lista de spreads.
     *
     * @return Date.
     */
    private Date fechaMasReciente() {
        if (getSpreads().size() == 0) {
            return new Date();
        }
        Date mayor = ((Spread) getSpreads().get(0)).getUltimaModificacion();
        for (Iterator it = getSpreads().iterator(); it.hasNext();) {
            Spread s = (Spread) it.next();
            if (s.getUltimaModificacion().after(mayor)) {
                mayor = s.getUltimaModificacion();
            }
        }
        return mayor;
    }

    /**
     * Actualiza los Spreads para los Precios de Referencia.
     *
     * @param spread El valor del spread.
     */
    private void actualizarSpreadPrecioReferencia(double spread) {
        PrecioReferenciaActual pr = getSicaServiceData().findPrecioReferenciaActual();
        if (pr.isActualizacionAutomatica() || pr.isMidSpot()) {
            double midSpot = (pr.getPreRef().getPrecioCompra().doubleValue()
                    + pr.getPreRef().getPrecioVenta().doubleValue()) / 2.0;
            PrecioReferenciaActual nvoPr = new PrecioReferenciaActual(pr);
            pr.getPreRef().setPrecioCompra(new Double(midSpot - (spread / 2.0)));
            pr.getPreRef().setPrecioVenta(new Double(midSpot + (spread / 2.0)));
            getSicaServiceData().storePrecioReferencia(nvoPr);
            setPrecioReferencia(pr);
        }
    }

    /**
     * Actualiza los cambios en la base de datos. Llama a
     * <code>notificarCambioPizarra()</code>.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void actualizarFactoresDivisa(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        if (delegate.getHasErrors()) {
            return;
        }
        try {
            Iterator iterator = getFactoresDivisa().iterator();
            boolean actualizar = false;
            double spread = 0.0;
            for (Iterator it = getFactoresDivisa().iterator(); it.hasNext();) {
                FactorDivisaActual fd = (FactorDivisaActual) it.next();
                fd.getFacDiv().aplicarFactorTmp();
            }
            for (Iterator iteratorTemporal = getFactoresDivisaTemporal().iterator();
                 iteratorTemporal.hasNext();) {
                FactorDivisaActual factorDivisaTemporal = (FactorDivisaActual) iteratorTemporal.next();
                FactorDivisaActual factorDivisa = (FactorDivisaActual) iterator.next();
                if (Divisa.DOLAR.equals(factorDivisa.getFacDiv().getFromDivisa().getIdDivisa())
                        && Divisa.DOLAR.equals(
                        factorDivisa.getFacDiv().getToDivisa().getIdDivisa())) {
                    spread = factorDivisa.getFacDiv().getSpreadCompra();
                }
                if (!(factorDivisa.getFacDiv().getFactorTmp()
                        == factorDivisaTemporal.getFacDiv().getFactorTmp()
                        && factorDivisa.getFacDiv().getFactorCompraTmp()
                        == factorDivisaTemporal.getFacDiv().getFactorCompraTmp()
                        && factorDivisa.getFacDiv().getSpreadReferencia()
                        == factorDivisaTemporal.getFacDiv().getSpreadReferencia()
                        && factorDivisa.getFacDiv().getSpreadCompra()
                        == factorDivisaTemporal.getFacDiv().getSpreadCompra()
                        && factorDivisa.getFacDiv().getCarry()
                        == factorDivisaTemporal.getFacDiv().getCarry()
                        && factorDivisa.getFacDiv().getSlack().equals(
                        factorDivisaTemporal.getFacDiv().getSlack()))) {
                    actualizar = true;
                    factorDivisa.setModificado(true);
                    factorDivisaTemporal.getFacDiv().setFactor(
                            factorDivisa.getFacDiv().getFactor());
                    factorDivisaTemporal.getFacDiv().setFactorCompra(
                            factorDivisa.getFacDiv().getFactorCompra());
                    factorDivisaTemporal.getFacDiv().setFactorTmp(
                            factorDivisa.getFacDiv().getFactorTmp());
                    factorDivisaTemporal.getFacDiv().setFactorCompraTmp(
                            factorDivisa.getFacDiv().getFactorCompraTmp());
                    factorDivisaTemporal.getFacDiv().setSpreadReferencia(
                            factorDivisa.getFacDiv().getSpreadReferencia());
                    factorDivisaTemporal.getFacDiv().setSpreadCompra(
                            factorDivisa.getFacDiv().getSpreadCompra());
                    factorDivisaTemporal.getFacDiv().setCarry(factorDivisa.getFacDiv().getCarry());
                    factorDivisaTemporal.getFacDiv().setSlack(factorDivisa.getFacDiv().getSlack());
                }
            }
            getSicaServiceData().storeFactoresDivisa(getFactoresDivisa());
            asignarFactoresDivisa(ordenarFactoresDivisa(
                    getSicaServiceData().findFactoresDivisaActuales()));
            if (actualizar) {
                actualizarSpreadPrecioReferencia(spread);
                notificarCambioPizarra();
            }
            if (getModo() == CONFIRMAR) {
                ParametroSica par = getParConfFactorDivisa();
                par.setValor("0");
                getSicaServiceData().update(par);
                checarVariablesInicioDia();
            }
        }
        catch (SicaException e) {
            warn(e);
            delegate.record(e.getMessage(), null);
        }
        catch (Exception e) {
            warn(e);
            delegate.record("No fue posible actualizar, por favor intente de nuevo.", null);
        }
    }

    /**
     * Actualiza los cambios en la base de datos. Llama a
     * <code>notificarCambioPizarra()</code> y genera los
     * tipos de cambio del Teller para la apertura de
     * operaciones.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void actualizarSpreads(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        if (delegate.getHasErrors()) {
            return;
        }
        try {
            Iterator iterator = getSpreads().iterator();
            double diferencia;
            for (Iterator iteratorTemporal = getSpreadsTemporal().iterator();
                 iteratorTemporal.hasNext();) {
                Spread spreadTemporal = (Spread) iteratorTemporal.next();
                Spread spread = (Spread) iterator.next();
                if (!(spread.getCpaVta().getCompraCash()
                        == spreadTemporal.getCpaVta().getCompraCash()
                        && spread.getCpaVta().getVentaCash()
                        == spreadTemporal.getCpaVta().getVentaCash()
                        && spread.getCpaVta().getCompraTom()
                        == spreadTemporal.getCpaVta().getCompraTom()
                        && spread.getCpaVta().getVentaTom()
                        == spreadTemporal.getCpaVta().getVentaTom()
                        && spread.getCpaVta().getCompraSpot()
                        == spreadTemporal.getCpaVta().getCompraSpot()
                        && spread.getCpaVta().getVentaSpot()
                        == spreadTemporal.getCpaVta().getVentaSpot()
                        && spread.getCpaVta().getCompra72Hr().doubleValue()
                        == spreadTemporal.getCpaVta().getCompra72Hr().doubleValue()
                        && spread.getCpaVta().getVenta72Hr().doubleValue()
                        == spreadTemporal.getCpaVta().getVenta72Hr().doubleValue()
                        && spread.getCpaVta().getCompraVFut().doubleValue()
                        == spreadTemporal.getCpaVta().getCompraVFut().doubleValue()
                        && spread.getCpaVta().getVentaVFut().doubleValue()
                        == spreadTemporal.getCpaVta().getVentaVFut().doubleValue())) {
                    spread.setModificado(true);
                    spreadTemporal.getCpaVta().setCompraCash(spread.getCpaVta().getCompraCash());
                    spreadTemporal.getCpaVta().setVentaCash(spread.getCpaVta().getVentaCash());
                    spreadTemporal.getCpaVta().setCompraTom(spread.getCpaVta().getCompraTom());
                    spreadTemporal.getCpaVta().setVentaTom(spread.getCpaVta().getVentaTom());
                    spreadTemporal.getCpaVta().setCompraSpot(spread.getCpaVta().getCompraSpot());
                    spreadTemporal.getCpaVta().setVentaSpot(spread.getCpaVta().getVentaSpot());
                    spreadTemporal.getCpaVta().setCompra72Hr(spread.getCpaVta().getCompra72Hr());
                    spreadTemporal.getCpaVta().setVenta72Hr(spread.getCpaVta().getVenta72Hr());
                    spreadTemporal.getCpaVta().setCompraVFut(spread.getCpaVta().getCompraVFut());
                    spreadTemporal.getCpaVta().setVentaVFut(spread.getCpaVta().getVentaVFut());
                }
            }
            getSicaServiceData().storeSpreads(getSpreads());
            notificarCambioPizarra(getTipoPizarron());
            if (getModo() == CONFIRMAR) {
                ParametroSica par = getParConfSpread();
                par.setValor("0");
                getSicaServiceData().update(par);
                checarVariablesInicioDia();
            	generarTcMinMaxTellerApertura();
            }
            if (getModo() == 0) {
            	delegate.record("El Spread para el pizarr\u00f3n "
            			+ getTipoPizarron().getDescripcion()
            			+ " fue actualizado con \u00eaxito.", null);
            	setLevel(1);
            }
        }
        catch (SicaException e) {
        	warn(e);
        	delegate.record(e.getMessage(), null);
        }
        catch (Exception e) {
            warn(e);
            delegate.record("No fue posible actualizar, por favor intente de nuevo.", null);
        }
    }

    /**
     * Checa Parametros de inicio de dia.
     *
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    private void checarVariablesInicioDia() throws SicaException {
        if ("0".equals(getSicaServiceData().findParametro(ParametroSica.CONF_SPREADS).
                getValor()) && "0".equals(getSicaServiceData().
                findParametro(ParametroSica.CONF_PRECIO_REF).getValor())) {
            setConfirmar(true);
        }
        if ("0".equals(getSicaServiceData().findParametro(ParametroSica.CONF_PRECIO_REF).
                getValor()) && "0".equals(getSicaServiceData().
                findParametro(ParametroSica.CONF_FACTORES_DIV).getValor())) {
            setConfirmar(true);
        }
        if ("0".equals(getSicaServiceData().findParametro(ParametroSica.CONF_SPREADS).
                getValor()) && "0".equals(getSicaServiceData().
                findParametro(ParametroSica.CONF_FACTORES_DIV).getValor())) {
            setConfirmar(true);
        }
        if ("0".equals(getSicaServiceData().findParametro(ParametroSica.CONF_SPREADS).
                getValor()) && "0".equals(getSicaServiceData().
                findParametro(ParametroSica.CONF_PRECIO_REF).getValor())
                && "0".equals(getSicaServiceData().
                findParametro(ParametroSica.CONF_FACTORES_DIV).getValor())) {
            try {
                ParametroSica paramSica = getSicaServiceData().
                        findParametro(ParametroSica.FECHA_SISTEMA);
                SimpleDateFormat DATE_FORMAT_V = new SimpleDateFormat("dd/MM/yyyy");
                paramSica.setValor(DATE_FORMAT_V.format(new Date()));
                getSicaServiceData().update(paramSica);
                List montosMaximos = getSicaServiceData().findAll(MontoMaximoSucursal.class);
                for (Iterator it = montosMaximos.iterator(); it.hasNext();) {
                    MontoMaximoSucursal mms = (MontoMaximoSucursal) it.next();
                    mms.setMontoOperado(new BigDecimal("0.0"));
                    getSicaServiceData().update(mms);
                }
                getSicaWorkFlowService().wfIniciarProcesoDealsPendientes(getTicket());
                refrescarEstadoActual();
            }
            catch (RemoteAccessException e) {
                warn(e.getMessage(), e);
                getDelegate().record(SicaException.transformar(e).getMessage(), null);
            }
        }
    }

    /**
     * Asigna los valores para los Factores de la Divisa.
     *
     * @param factoresDivisa La lista de factores de la divisa.
     */
    private void asignarFactoresDivisa(List factoresDivisa) {
        for (Iterator it = factoresDivisa.iterator(); it.hasNext();) {
            FactorDivisaActual fd = (FactorDivisaActual) it.next();
            fd.getFacDiv().inicializarFactorTmp();
        }
        setFactoresDivisa(factoresDivisa);
    }

    /**
     * Establece si la operacion del parametro es manual o atomatica
     *
     * @param cycle El ciclo de la p&aacute;gina
     */
    public void establecerAutomaticoManual(IRequestCycle cycle) {
        setFactorAutomatico(!isFactorAutomatico());
        ParametroSica p = getSicaServiceData().findParametro(ParametroSica.FACTOR_AUTOMATICO);
        p.setValor(isFactorAutomatico() ? "S" : "N");
        getSicaServiceData().update(p);
    }

    /**
     * Establece si el factor de la divisa es o no visible.
     *
     * @param fd El factor de la divisa.
     * @return boolean.
     */
    public boolean isFactorCompraVisible(FactorDivisaActual fd) {
        return !fd.getFacDiv().getFromDivisa().getIdDivisa().equals(fd.getFacDiv().
                getToDivisa().getIdDivisa()) && fd.getFacDiv().getFromDivisa().isFrecuente()
                && fd.getFacDiv().getToDivisa().isFrecuente();
    }

    /**
     * Obtiene el arreglo de Strings de Id's de las divisas operadas por el Teller.
     *
     * @param deals La lista de deals.
     * @return String[]
     */
    public String[] obtenerDivisasDealsPendientes(List deals) {
    	String [] divisas = new String [deals.size()];
    	int i = 0;
    	for (Iterator it = deals.iterator(); it.hasNext(); ) {
    		DealsPendientesDto dto = (DealsPendientesDto) it.next();
    		divisas[i] = dto.getIdDivisa();
    		i++;
    	}
    	return divisas;
    }

    /**
     * Obtiene los totales operados de compras y ventas de los deals capturados por el Teller.
     *
     * @param deals La lista de Deals.
     * @return List
     */
    public List obtenerTotalesOperacionesDealsPendientes(List deals) {
    	List op = new ArrayList();
    	List divisas = new ArrayList();
    	for (Iterator it = deals.iterator(); it.hasNext() ;) {
    		Deal deal = (Deal) it.next();
            for (Iterator detIt = deal.getDetalles().iterator(); detIt.hasNext();) {
                DealDetalle det = (DealDetalle) detIt.next();
                if (!divisas.contains(det.getDivisa().getIdDivisa()) && !det.isCancelado() &&
                        !Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
                    divisas.add(det.getDivisa().getIdDivisa());
                    op.add(new DealsPendientesDto(det.getDivisa().getIdDivisa(),
                            det.getDivisa().getDescripcion(),
                            deal.isCompra() ? det.getMonto() : 0.0,
                            deal.isCompra() ? 0.0 : det.getMonto()));
                }
                else {
                    for (Iterator opIt = op.iterator(); opIt.hasNext();) {
                        DealsPendientesDto dpd = (DealsPendientesDto) opIt.next();
                        if (det.getDivisa().getIdDivisa().equals(dpd.getIdDivisa()) &&
                                !Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
                            dpd.sumaMonto(deal.isCompra(), det.getMonto());
                        }
                    }
                }
    		}
    	}
    	return op;
    }

    /**
     * Genera los tipos de cambio para apertura de operaciones para el Teller.
     *
     */
    public void generarTcMinMaxTellerApertura() {
    	try {
    		String producto = Constantes.EFECTIVO;
    		String idDivisa = Divisa.DOLAR;
    		List tiposPizarron = getSicaServiceData().findAll(TipoPizarron.class);
    		List canalesSucursales = getSicaServiceData().findCanalSucursales();
    		List tcSucursales = new ArrayList();
    		debug(":::CANALES " + canalesSucursales.size());
    		for (Iterator it = tiposPizarron.iterator(); it.hasNext();) {
                TipoPizarron pizarron = (TipoPizarron) it.next();
                for (Iterator itt = canalesSucursales.iterator(); itt.hasNext();) {
                    Canal canal = (Canal) itt.next();
                    if (canal.getTipoPizarron().getIdTipoPizarron().intValue() ==
                            pizarron.getIdTipoPizarron().intValue()) {
                        Map map = getPizarronServiceData().obtenerTipoDeCambioPorDivisa(canal,
                                idDivisa, producto);
                        map.put("idCanal", canal.getIdCanal());
                        tcSucursales.add(map);
                    }
                }
            }
            for (Iterator it = tcSucursales.iterator(); it.hasNext();) {
                Map tc = (HashMap) it.next();
                TcMinMaxTeller tcApertura = new TcMinMaxTeller(
                        ((Double) tc.get("minimoCompra")).doubleValue(),
    					((Double) tc.get("maximoCompra")).doubleValue(),
    					((Double) tc.get("minimoVenta")).doubleValue(),
    					((Double) tc.get("maximoVenta")).doubleValue(),
    					(String) tc.get("idCanal"),
    					false);
    			getSicaServiceData().store(tcApertura);
    		}
    	}
    	catch (Exception e) {
    		warn(e.getMessage(), e);
            getDelegate().record("No fue posible guardar los Tipos de Cambio " +
            		"para las sucursales favor de notificar al area de Sistemas.", null);
		}
    }

    /**
     * Regresa los Spreads que tienen relacion con BUP Sucursal.
     *
     * @return List La Lista de Spreads esperada.
     */
    public List getSpreadsBUPSucursal() {
    	return spreadsBUPSucursal;
    }

    /**
     * Activa los Spreads que tienen relacion con BUP Sucursal.
     *
     * @param spreadsBUPSucursal El valor a asignar.
     */
    public void setSpreadsBUPSucursal(List spreadsBUPSucursal) {
    	this.spreadsBUPSucursal = spreadsBUPSucursal;
    }

    /**
     * Obtiene el valor de ParametroAutomatico
     *
     * @return String
     */
    public abstract boolean isFactorAutomatico();

    /**
     * Asina el valor para parametroAutomatico
     *
     * @param factorAutomatico "S" para autom&aacute;tico "N" para manual
     */
    public abstract void setFactorAutomatico(boolean factorAutomatico);

    /**
     * Regresa el valor de slack.
     *
     * @return Double.
     */
    public abstract Double getSlack();

    /**
     * Fija lel valor del Stack
     *
     * @param slack El valor del stack
     */
    public abstract void setSlack(Double slack);

    /**
     * Regresa el valor de canalSeleccionado.
     *
     * @return Canal.
     */
    public abstract Canal getCanalSeleccionado();

    /**
     * Regresa el valor de divisaSeleccionada.
     *
     * @return Divisa.
     */
    public abstract Divisa getDivisaSeleccionada();

    /**
     * Regresa el valor de factoresDivisa.
     *
     * @return List.
     */
    public abstract List getFactoresDivisa();

    /**
     * Fija los valores factoresDivisa.
     *
     * @param factoresDivisa La lista de factores divisa a asignar.
     */
    public abstract void setFactoresDivisa(List factoresDivisa);

    /**
     * Obtiene la Lista Temporal de Factores Divisa.
     *
     * @return List.
     */
    public abstract List getFactoresDivisaTemporal();

    /**
     * Fija la Lista Temporal de Factores Divisa.
     *
     * @param factoresDivisaTemporal El valor a asignar.
     */
    public abstract void setFactoresDivisaTemporal(List factoresDivisaTemporal);

    /**
     * Regresa el valor de modoSubmit.
     *
     * @return int.
     */
    public abstract int getModoSubmit();

    /**
     * Regresa el valor de precioReferencia.
     *
     * @return PrecioReferencia.
     */
    public abstract PrecioReferenciaActual getPrecioReferencia();

    /**
     * Fija el valor de precioReferencia.
     *
     * @param precioReferencia El precio de referencia a asignar.
     */
    public abstract void setPrecioReferencia(PrecioReferenciaActual precioReferencia);

    /**
     * Regresa el valor de spreads.
     *
     * @return List.
     */
    public abstract List getSpreads();

    /**
     * Fija el valor de spreads.
     *
     * @param spreads La lista de spreads a asignar.
     */
    public abstract void setSpreads(List spreads);

    /**
     * Regresa el valor de spreadsTemporal.
     *
     * @return List.
     */
    public abstract List getSpreadsTemporal();

    /**
     * Fija el valor de spreadsTemporal.
     *
     * @param spreads La lista de spreadsTemporal a asignar.
     */
    public abstract void setSpreadsTemporal(List spreads);

	/**
	 * @param lista de registros de SC_MAXIMO_DEAL
	 */
	public abstract void setRegistros(List lista);

	/**
	 * @return List.
	 */
	public abstract List getRegistros();

	/**
     * Regresa la mesa de cambios seleccionada para Montos M&aacute;ximos.
     *
	 * @return MesaCambio.
	 */
	public abstract MesaCambio getMesaCambioSeleccionadaMontosMaximos();

    /**
     * Fija el valor de mesaCambioSeleccionadaMontosMaximos.
     *
     * @param mc El valor a asignar.
     */
    public abstract void setMesaCambioSeleccionadaMontosMaximos(MesaCambio mc);

    /**
     * Regresa el valor de modo.
     *
     * @return int.
     */
    public abstract int getModo();

    /**
     * Constante para formatear las fechas de la forma dd/mm/yy.
     */
    public static final java.text.SimpleDateFormat FORMATEADOR_FECHA =
            new java.text.SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "mx"));

    /**
     * Constante para saber si un canal es de tipo SUC (Sucursales).
     */
    private static final String SUC = "SUC";

    /**
     * Lista que contiene los Spreads que tienen relacion con BUP Sucursal.
     */
    private List spreadsBUPSucursal;

    /**
     * Regresa el valor de confirmar
     *
     * @return boolean
     */
    public abstract boolean getConfirmar();

    /**
     * Fija el valor de Confirmar.
     *
     * @param confirmar El valor a asignar.
     */
    public abstract void setConfirmar(boolean confirmar);

    /**
     * Regresa el valor de TipoPizarron.
     *
     * @return TipoPizarron.
     */
    public abstract TipoPizarron getTipoPizarron();

    /**
     * Asigna el valor para TipoPizarron.
     *
     * @param tipoPizarron El valor para tipoPizarron
     */
    public abstract void setTipoPizarron(TipoPizarron tipoPizarron);

    /**
     * Obtiene el arreglo de divisas operadas por el Teller.
     *
     * @return String[]
     */
    public abstract String[] getDivisasDealsPendientes();

    /**
     * Asigna el valor para el arreglo de divisas operadas
     * en los deals Pendientes.
     *
     * @param divisasDealsPendientes El arrelgo de divisas.
     */
    public abstract void setDivisasDealsPendientes(String[] divisasDealsPendientes);

    /**
     * Obtiene la lista de deals capturados que se encuentran pendientes.
     *
     * @return List
     */
    public abstract List getDealsPendientes();

    /**
     * Asigna el valor para la lista de deals operados pendientes.
     *
     * @param dealsPendientes El valor para las lista de deals.
     */
    public abstract void setDealsPendientes(List dealsPendientes);

    /**
     * Obtiene la lista de DTO's con los totale de los deals pendientes.
     *
     * @return List
     */
    public abstract List getTotalesOperaciones();

    /**
     * Asigna el valor para los totales de deals pendientes.
     *
     * @param totalesOperaciones Los totales para deals pendientes.
     */
    public abstract void setTotalesOperaciones(List totalesOperaciones);

    /**
     * Regresa el valor de level.
     *
     * @return int.
     */
    public abstract int getLevel();

    /**
     * Fija el valor de level.
     *
     * @param level El valor a asignar.
     */
    public abstract void setLevel(int level);
    
    /**
     * Obtiene el origen de la variaci&oacute;n
     * 
     * @return
     */
    public abstract String getOrigenVariacion();
    
    /**
     * Establece el origen de la variaci&oacute;n
     * 
     * @param origenVariacion
     */
    public abstract void setOrigenVariacion(String origenVariacion);

    /**
     * Constante para confirmaci&oacute;n de valores.
     */
    public static final int CONFIRMAR = 2;

    /**
     * Constante para refrescar los valores.
     */
    public static final int REFRESCAR = 3;
}
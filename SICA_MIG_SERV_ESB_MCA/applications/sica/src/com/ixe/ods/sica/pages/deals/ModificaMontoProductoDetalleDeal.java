/*
 * $Id: ModificaMontoProductoDetalleDeal.java,v 1.3.2.4.6.1.58.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Actividad;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.RenglonPizarron;
import com.ixe.ods.sica.pizarron.Consts;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Clase que modifica el monto o el producto de un deal respectivamente.
 *
 * @author Abraham L&oacute;pez A.
 * @version $Revision: 1.3.2.4.6.1.58.1 $ $Date: 2020/12/01 04:53:01 $
 */
public abstract class ModificaMontoProductoDetalleDeal extends AbstractCapturaDeal
	implements IExternalPage{

	/**
     * S&oacute;lo se ejecuta cuando se quiere modificar el producto o el monto de un detalle
     * de deal.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void activateExternalPage(Object[] arg0, IRequestCycle cycle) {

		Visit visit = (Visit) getVisit();
		try {
			if (arg0[0] != null) {
				int idDealPosicion = ((Integer) arg0[0]).intValue();
                Integer modo = (Integer) arg0[1];
                setModo(modo.intValue());
                DealDetalle dealDetalle = getSicaServiceData().findDealDetalleConDivisaFactorDivisa(
                        idDealPosicion);
                setDealDetalle(dealDetalle);
                setDeal(dealDetalle.getDeal());
                setMontoActual(dealDetalle.getMonto());
                setNuevoMonto(dealDetalle.getMonto());
                setTipoCambioMesa(Redondeador.redondear6Dec(dealDetalle.getTipoCambioMesa()));
                setTipoCambioCliente(Redondeador.redondear6Dec(dealDetalle.getTipoCambio()));
                setCanal(visit.getIdCanal());
                setFormaPagoLiq(getDealDetalle().getClaveFormaLiquidacion());
                if (getModo() == OPCION_MODIFICAR_PRODUCTO) {
                    modelLit = new ArrayList();
                    List clavesFormasLiquidacion = getPizarronServiceData().getRenglonesPizarron(
                            visit.getTicket(), (getSicaServiceData().findCanal(
                                    getCanal()).getTipoPizarron()).getIdTipoPizarron(), false);
                    for (Iterator it = clavesFormasLiquidacion.iterator(); it.hasNext();) {
                        RenglonPizarron rpTmp = (RenglonPizarron) it.next();
                        if (rpTmp.getIdDivisa().
                                equals(getDealDetalle().getDivisa().getIdDivisa())) {
                            pares = new HashMap();
                            pares.put(LABEL, rpTmp.getClaveFormaLiquidacion());
                            pares.put(VALUE, rpTmp.getClaveFormaLiquidacion());
                            modelLit.add(pares);
                            if (getDealDetalle().getClaveFormaLiquidacion().equals(rpTmp.
                                    getClaveFormaLiquidacion())) {
                                setProductOrig(pares);
                            }
                        }
                    }
                    setModelList(modelLit);
                    setModoSubmit("");
                }
                if (isHorarioVespertino()) {
                    throw new SicaException("No es posible modificar el monto o producto de " +
                            "un deal en Horario Vespertino");
                }
                validarModificaciones(dealDetalle);
            }
        }
        catch (SicaException e) {
            warn(e.getMessage(), e);
            getDelegate().record("El detalle no puede ser modificado porque: " + e.getMessage(),
                    null);
            setExisteModificacionPendiente(true);
        }
    }

    /**
     * M&eacut;etodo que verifica si se quiere modificar el monto o el producto de un detalle
     * de deal.
     *
     * @param cycle  El ciclo de la p&aacute;gina.
     */
    public void modificar(IRequestCycle cycle) {
        if (getDelegate().getHasErrors()) {
            return;
        }
        try {
            if (getModo() == OPCION_MODIFICAR_MONTO) {
                validaMinMult(getDealDetalle());
                getSicaServiceData().validaLimitesRestriccionOperacion(getDeal(), getDealDetalle(),
                		getDealDetalle().getClaveFormaLiquidacion(),
                		getLimitesActualizados(getDeal()), Constantes.MODIF_MTO,
                		new Double(getNuevoMonto() - getDealDetalle().getMontoUSD()),
                		getDeal().getFechaLiquidacion(), false);
                validaMontoTravelerChecks(getDeal(), getDealDetalle(),
                        getDealDetalle().getClaveFormaLiquidacion(),
                        getNuevoMonto() - getDealDetalle().getMontoUSD());
                modificarMontoDetalle(getDealDetalle().getIdDealPosicion(), getNuevoMonto(),
                        getMontoActual());
                setMensaje("" + getDeal().getIdDeal());
                setLevel(1);
                setNuevoMonto(getNuevoMonto());
                setMuestraCampoNuevoMonto(true);
            }
            else if (getModo() == OPCION_MODIFICAR_PRODUCTO) {
                if (getModoSubmit().equals(MODIF_PRODUCT)) {
                    setTipoCambioMesa(getTipoCambioCliente());
                    setModoSubmit("");
                    return;
                }
                else {
                    if (getDealDetalle().getClaveFormaLiquidacion().trim().equals(((String)
                            getProductOrig().get(VALUE)).trim())) {
                        throw new SicaException("El tipo de producto no ha sido modificado.");
                    }
                    double fd = 1.0;
                    validaMinMult(getDealDetalle(), (String) getProductOrig().get(VALUE));
                    getSicaServiceData().validaLimitesRestriccionOperacion(getDeal(),
                    		getDealDetalle(), (String) getProductOrig().get(VALUE),
                    		getLimitesActualizados(getDeal()), Constantes.MODIF_PROD, null,
                    		getDeal().getFechaLiquidacion(), false);
                    validaMontoTravelerChecks(getDeal(), getDealDetalle(),
                            (String) getProductOrig().get(VALUE), getDealDetalle().getMontoUSD());
                    validarTCLimDesv(getDealDetalle().getMonto() * fd, getTipoCambioCliente(),
                            getTipoCambioMesa());
                    modificarProductoDetalle(getDealDetalle().getIdDealPosicion(), (String)
                            getProductOrig().get(VALUE), getTipoCambioMesa(),
                            getTipoCambioCliente());
                    setMensaje("" + getDeal().getIdDeal());
                    setLevel(1);
                    setMuestraCampoNuevoProducto(true);
					setMuestraCampoNuevoTcc(true);
					setNuevoProducto((String) getProductOrig().get(VALUE));
					setTipoCambioMesa(getTipoCambioMesa());
					setTipoCambioCliente(getTipoCambioCliente());
                }
            }
            setExisteModificacionPendiente(true);
        }
        catch (SicaException e) {
        	if (e.getMessage().substring(0, 9).equals("LimEfect:")) {
        		getSicaServiceData().auditar(((Visit) getVisit()).getTicket(),
        				getRequestCycle().getRequestContext().getRequest().getRemoteAddr(),
        				new Integer(getDeal().getIdDeal()), ((Visit) getVisit()).
        				getUser(), LogAuditoria.EXCEDIO_LIMITE_OPERACION, null, "ERROR", "F");
        		warn(e.getMessage(), e);
                getDelegate().record("El detalle no puede ser modificado porque: " +
                		e.getMessage().substring(10, e.getMessage().
                		length()), null);
                debug(e.getMessage(), e);
        	}
        	else {
        		getDelegate().record("El detalle no puede ser modificado porque: " + e.getMessage(),
                        null);
                debug(e.getMessage(), e);
        	}
        }
    }

    /**
     * M&eacut;etodo que se encarga de validar si el cliente a&uacute;n puede operar TRAVELER_CHECKS
     * en la modificaci&oacute;n del monto de un detalle de deal.
     *
     * @param deal El deal a verificar.
     * @param det  El detalle del deal a verificar.
     * @param claveFormaLiquidacion    El producto del detalle del deal.
     * @param nuevoMonto               El nuevo monto que se quiere modificar.
     */
    private void validaMontoTravelerChecks(Deal deal, DealDetalle det,
                                           String claveFormaLiquidacion, double nuevoMonto) {
        if (!deal.isInterbancario() &&
                Constantes.TRAVELER_CHECKS.equals(claveFormaLiquidacion) &&
                deal.getContratoSica() != null && !det.isRecibimos()) {
            double montoChviaj = getSicaServiceData().
                    findMontoChequesViajeroUsdFechaLiquidacion(deal.getContratoSica().
                            getNoCuenta(), deal.getFechaLiquidacion());

            if (nuevoMonto + montoChviaj > 25000) {
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
     * Se encarga de validar si el tipo de cambio al cliente capturado est&aacute; dentro del
     * l&iacute;mite de desviaci&oacute;n (+,-).
     *
     * @param mto El monto.
     * @param tcc El tipo de cambio al cliente.
     * @param tcm El tipo de cambio de la mesa.
     */
    private void validarTCLimDesv(double mto, double tcc, double tcm) {
    	ParametroSica psDesvMtoLim = (ParametroSica) getSicaServiceData().find(ParametroSica.class,
                ParametroSica.DESV_MONTO_LIM);
    	double _desvMontoLim = Double.parseDouble(psDesvMtoLim.getValor());
    	ParametroSica psDesvPorcLim = (ParametroSica) getSicaServiceData().find(ParametroSica.class,
                ParametroSica.DESV_PORC_LIM);
    	double _desvPorcLim = Double.parseDouble(psDesvPorcLim.getValor());
    	ParametroSica psDesvPorcMax = (ParametroSica) getSicaServiceData().find(ParametroSica.class,
                ParametroSica.DESV_PORC_MAX);
    	double _desvPorcMax = Double.parseDouble(psDesvPorcMax.getValor());
    	ParametroSica psDesvFact1 = (ParametroSica) getSicaServiceData().find(ParametroSica.class,
                ParametroSica.DESV_FACT_1);
    	double _desvFact1 = Double.parseDouble(psDesvFact1.getValor());
    	ParametroSica psDesvFact2 = (ParametroSica) getSicaServiceData().find(ParametroSica.class,
                ParametroSica.DESV_FACT_2);
    	double _desvFact2 = Double.parseDouble(psDesvFact2.getValor());
        debug("Monto: " + mto + " tcc " + tcm);
        double tcCliente = Redondeador.redondear6Dec(tcc);
        debug("tcCliente: " + tcCliente);
        double tcMesa = Redondeador.redondear6Dec(tcm);
        debug("tcMesa: " + tcMesa);
        double limiteDesviacion;
        if (mto >= _desvMontoLim) {
            limiteDesviacion = _desvPorcLim;
        }
        else {
            limiteDesviacion = _desvPorcMax * Math.exp(_desvFact1 * log(mto)) * _desvFact2 /
                    Consts.NUMD_100;
        }
        debug("limiteDesviacion: " + limiteDesviacion);
        limiteDesviacion = Redondeador.redondear6Dec(limiteDesviacion);
        debug("2. limiteDesviacion: " + limiteDesviacion);
        debug("a. " + Redondeador.redondear6Dec(tcMesa * (1.0 - limiteDesviacion)));
        debug("b. " + Redondeador.redondear6Dec(tcMesa * (1.0 + limiteDesviacion)));
        if (tcCliente < Redondeador.redondear6Dec(tcMesa * (1.0 - limiteDesviacion))) {
            throw new SicaException("El tipo cambio es menor al l\u00edmite de desviaci\u00f3n " +
                    "permitido.");
        }
        if (tcCliente > Redondeador.redondear6Dec(tcMesa * (1.0 + limiteDesviacion))) {
            throw new SicaException("El tipo cambio es mayor al l\u00edmite de desviaci\u00f3n " +
                    "permitido.");
        }
    }

    /**
     * Se encarga de validar el m&iacute;nimo y el m&uacute;ltiplo que se debe de capturar en el
     * monto al modificar el detalle de un deal.
     *
     * @param det       El DealDetalle a verificar.
     * @return boolean
     */
    private boolean validaMinMult(DealDetalle det) {
        StringBuffer sb = new StringBuffer();
        DecimalFormat df = new DecimalFormat("0.00");
        List formasPagLiq = getFormasPagoLiqService().getFormasTiposLiq(getTicket());
        for (Iterator iter = formasPagLiq.iterator(); iter
                .hasNext();) {
            FormaPagoLiq formaPagLiq = (FormaPagoLiq) iter.next();
			String mnem = formaPagLiq.getMnemonico();
            if (det.getClaveFormaLiquidacion().equals(formaPagLiq.getClaveFormaLiquidacion()) &&
            		det.getDivisa().getIdDivisa().equals(formaPagLiq.getIdDivisa()) &&
                    ((det.isRecibimos() && mnem.startsWith("R")) ||
                    		(!det.isRecibimos() && mnem.startsWith("E")))) {
                double min = formaPagLiq.getMontoMinimo().doubleValue();
                double mult = formaPagLiq.getMultiplo().doubleValue();
                double cosciente;
                try {
                    cosciente = df.parse(df.format(getNuevoMonto() / mult)).doubleValue();
                }
                catch (ParseException e) {
                    throw new SicaException("Ocurri\u00f3 un error. Por favor intente de nuevo " +
                            "la operaci\u00f3n, si persiste el problema comun\u00edquese al " +
                            "\u00e1rea de sistemas.");
                }
                if (getNuevoMonto() >= min && (cosciente - (long) cosciente == 0)) {
                    return true;
                }
                StringBuffer sb2 = new StringBuffer("(M\u00ednimo: ").append(min).
                        append(", M\u00faltiplo de ").append(mult).append(")\n");
                if (sb.toString().indexOf(sb2.toString()) < 0) {
                    sb.append(sb2.toString());
                }
            }
        }
        if (sb.toString().trim().length() > 0) {
            throw new SicaException("El monto no es v\u00e1lido. Debe seguir las siguientes " +
                    "reglas:\n" + sb.toString());
        }
        else {
            throw new SicaException("Error de datos.\nNo se tienen configuradas correctamente " +
                    "las formas de liquidaci\u00f3n para la Divisa " +
                    det.getDivisa().getIdDivisa() +
                    " \nen la " + (det.isRecibimos() ? "COMPRA " : "VENTA") + " de " +
                    det.getClaveFormaLiquidacion() + ".\nFavor de llamar a Sistemas");
        }
    }

    /**
     * Se encarga de validar el m&iacute;nimo y el m&uacute;ltiplo que se debe de validar al momento
     * de modificar el producto del detalle de un deal.
	 *
	 * @param det       		El DealDetalle a verificar.
	 * @param nuevoProducto		El producto por el que se desea cambiar.
	 * @return boolean
	 */
	private boolean validaMinMult(DealDetalle det, String nuevoProducto) {
		StringBuffer sb = new StringBuffer();
        DecimalFormat df = new DecimalFormat("0.00");
		List formasPagLiq = getFormasPagoLiqService().getFormasTiposLiq(getTicket());
		for (Iterator iter = formasPagLiq.iterator(); iter
				.hasNext();) {
			FormaPagoLiq formaPagLiq = (FormaPagoLiq) iter.next();
			String mnem = formaPagLiq.getMnemonico();
            if (nuevoProducto.equals(formaPagLiq.getClaveFormaLiquidacion()) &&
            		det.getDivisa().getIdDivisa().equals(formaPagLiq.getIdDivisa()) &&
                    ((det.isRecibimos() && mnem.startsWith("R")) ||
                    		(!det.isRecibimos() && mnem.startsWith("E")))) {
                double min = formaPagLiq.getMontoMinimo().doubleValue();
                double mult = formaPagLiq.getMultiplo().doubleValue();
                double cosciente;
                try {
                    cosciente = df.parse(df.format(det.getMonto() / mult)).doubleValue();
                }
                catch (ParseException e) {
                    throw new SicaException("Ocurri\u00f3 un error. Por favor intente de nuevo " +
                            "la operaci\u00f3n, si persiste el problema comun\u00edquese al " +
                            "\u00e1rea de sistemas.");
                }
                if (det.getMonto() >= min && (cosciente - (long) cosciente == 0)) {
                    return true;
                }
                StringBuffer sb2 = new StringBuffer("(M\u00ednimo: ").append(min).
                        append(", M\u00faltiplo de ").append(mult).append(")\n");
                if (sb.toString().indexOf(sb2.toString()) < 0) {
                    sb.append(sb2.toString());
                }
            }
        }
        if (sb.toString().trim().length() > 0) {
            throw new SicaException("El monto no es v\u00e1lido. Debe seguir las siguientes " +
                    "reglas:\n" + sb.toString());
        }
        else {
            throw new SicaException("Error de datos.\nNo se tienen configuradas correctamente " +
                    "las formas de liquidaci\u00f3n para la Divisa " +
                    det.getDivisa().getIdDivisa() +
                    " \nen la " + (det.isRecibimos() ? "COMPRA " : "VENTA") + " de " +
                    nuevoProducto + ".\nFavor de llamar a Sistemas.");
        }
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
            throw new SicaException("El deal no puede estar liquidado ni cancelado.");
        }
        if (det.getDeal().tieneSolModifPendiente()) {
        	Actividad act = null;
        	if (getModo() == OPCION_MODIFICAR_MONTO) {
        		act = getSicaServiceData().findActividadByIdDealPosicion(
            			det.getIdDealPosicion(), Actividad.ACT_DN_MOD_MONTO);
        	}
            else if (getModo() == OPCION_MODIFICAR_PRODUCTO) {
                act = getSicaServiceData().findActividadByIdDealPosicion(
                        det.getIdDealPosicion(), Actividad.ACT_DN_MOD_PROD);
            }
            if (act != null && Actividad.ACT_DN_MOD_MONTO.equals(act.getNombreActividad().trim())) {
                setNuevoMonto(new Double(act.getResultado().trim()).doubleValue());
                setMuestraCampoNuevoMonto(true);
                throw new SicaException("El deal tiene una solicitud pendiente por " +
                        "modificaci\u00f3n de monto.");
            }
            else if (act != null && Actividad.ACT_DN_MOD_PROD.equals(act.getNombreActividad().
                    trim())) {
                String[] valoresActividad = act.getResultado().split("\\|");
                setMuestraCampoNuevoProducto(true);
                setMuestraCampoNuevoTcc(true);
                setNuevoProducto(valoresActividad[0]);
				setTipoCambioMesa(Double.parseDouble(valoresActividad[1]));
				setTipoCambioCliente(Double.parseDouble(valoresActividad[2]));
        		throw new SicaException("El deal tiene una solicitud" +
                " pendiente por modificaci\u00f3n de producto.");
        	}
        	else {
        		setMuestraCampoNuevoMonto(false);
        		setNuevoMonto(0.0);
        		throw new SicaException("El deal tiene una solicitud de modificaci\u00f3n" +
                " pendiente.");
        	}
        }
        if (det.getDeal().tieneSolSobreprecioPendiente()) {
        	throw new SicaException("El deal tiene una solicitud de sobreprecio pendiente.");
        }
        if (det.getDeal().tieneSolCancPendiente()) {
            throw new SicaException("El deal tiene una solicitud de cancelaci\u00f3n " +
                    "total pendiente.");
        }
        if (det.isEvento(det.isInterbancario() ?
                DealDetalle.EV_IND_INT_MODIFICACION : DealDetalle.EV_IND_MODIFICACION,
                new String[]{Deal.EV_NEGACION})) {
            throw new SicaException("No se puede pedir una solicitud de cancelaci\u00f3n o " +
                    "modificaci\u00f3n por monto o producto, para un detalle que el \u00e1rea de " +
                    "tesorer\u00eda neg\u00f3 con anterioridad.");
        }
        if (!getDeal().isInterbancario()) {
            if (det.tieneAutPendientesHorario()) {
                throw new SicaException("El deal tiene una autorizaci\u00f3n por horario " +
                        "pendiente.");
            }
            if (det.tieneAutPendientesMonto()) {
                throw new SicaException("El deal tiene una autorizaci\u00f3n por monto pendiente.");
            }
        }
	}

    /**
     * Define si el estado de operaciones actual es Horario Vespertino.
     *
     * @return boolean
     */
    protected boolean isHorarioVespertino() {
    	return getSicaServiceData().findEstadoSistemaActual().getIdEstado() ==
    		Estado.ESTADO_OPERACION_VESPERTINA;
    }

    /**
     * Regresa el logaritmo base 10 de x.
     *
     * @param x El valor a calcular.
     * @return double.
     */
    private double log(double x) {
        return Math.log(x) / Math.log(Consts.NUMD_10);
    }

    /**
     * Modelo para la lista de productos por divisa.
     *
     * @return StringPropertySelectionModel
     */
    public IPropertySelectionModel getProductsModel() {
    	return new RecordSelectionModel(getModelList(), VALUE, LABEL);
    }

    /**
     * Fija el valor del detalle del deal.
     *
     * @param detalle El valor a asignar.
     */
	public abstract void setDealDetalle(DealDetalle detalle);

	/**
	 * Regresa el valor del detalle del deal.
	 *
	 * @return DealDetalle
	 */
	public abstract DealDetalle getDealDetalle();

	/**
	 * Fija el valor de true o false dependiendo de si el detalle de deal tiene una
	 * modificaci&oacute;n pendiente.
	 *
	 * @param val El valor a asignar.
	 */
    public abstract void setExisteModificacionPendiente(boolean val);

    /**
     * Regresa el valor del modo a operar dependiendo de si se va a hacer una modificaci&oacute;n
     * por monto o por producto respectivamente.
     *
     * @return int
     */
    public abstract int getModo();

    /**
     * Fija el valor del modo a operar para modificaci&oacute;n de monto o de producto.
     *
     * @param modo El valor a asignar.
     */
    public abstract void setModo(int modo);

    /**
     * Regresa el valor del nuevo monto a modificarse para el detalle de deal.
     *
     * @return double
     */
    public abstract double getNuevoMonto();

    /**
     * Regresa el valor del monto actual del detalle de deal a modificarse.
     *
     * @return double
     */
    public abstract double getMontoActual();

    /**
     * Fija el valor del mensaje.
     *
     * @param mensaje El valor a asignar.
     */
    public abstract void setMensaje(String mensaje);

    /**
     * Fija el valor del nuevo monto a capturar en la modificaci&oacute;n del detalle de un deal.
     *
     * @param nuevoMonto El valor a asignar.
     */
    public abstract void setNuevoMonto(double nuevoMonto);

    /**
     * Fija el valor del monto actual del detalle de deal a modificar.
     *
     * @param montoActual El valor a asignar.
     */
    public abstract void setMontoActual(double montoActual);

    /**
     * Regresa el tipo de cambio ofrecido al cliente.
     *
     * @return double
     */
    public abstract double getTipoCambioCliente();

    /**
     * Fija el valor del tipo de cambio ofrecido al cliente.
     *
     * @param tipoCambioCliente El valor a asignar.
     */
    public abstract void setTipoCambioCliente(double tipoCambioCliente);

    /**
     * Fija el valor del tipo de cambio de la mesa.
     *
     * @param tipoCambioMesa El valor a asignar.
     */
    public abstract void setTipoCambioMesa(double tipoCambioMesa);

    /**
     * Obtiene el tipo de cambio de la mesa original.
     *
     * @return double
     */
    public abstract double getTipoCambioMesa();


    /**
     * Regresa el valor de las claves forma de liquidaci&oacute;n.
     *
     * @return String[]
     */
    public abstract String[] getClavesFormasLiquidacion();

    /**
     * Fija el valor de las claves forma de liquidaci&oacute;n.
     *
     * @param clavesFormasLiquidacion El valor a asignar.
     */
    public abstract void setClavesFormasLiquidacion(String[] clavesFormasLiquidacion);

    /**
     * Regresa el valor de la forma de pago liquidaci&oacute;n cuando se modifica el producto
     * del detalle de un deal.
     *
     * @return String
     */
    public abstract String getFormaPagoLiq();

    /**
     * Fija el valor de la forma de pago liquidaci&oacute;n.
     *
     * @param formaPagoLiq El valor a asignar.
     */
    public abstract void setFormaPagoLiq(String formaPagoLiq);

    /**
     * Fija el valor del canal.
     *
     * @param canal El valor a asignar.
     */
    public abstract void setCanal(String canal);

    /**
     * Indica si se debe de mostrar o no el cambio de nuevo monto.
     *
     * @param muestraCampoNuevoMonto El valor a asignar.
     */
    public abstract void setMuestraCampoNuevoMonto(boolean muestraCampoNuevoMonto);

    /**
     * Indica si se debe de mostrar o no el cambio de nuevo producto.
     *
     * @param muestraCampoNuevoProducto El valor a asignar.
     */
    public abstract void setMuestraCampoNuevoProducto(boolean muestraCampoNuevoProducto);

    /**
     * Indica si se debe de mostrar o no el cambio de nuevo tcc.
     *
     * @param muestraCampoNuevoTcc El valor a asignar.
     */
    public abstract void setMuestraCampoNuevoTcc(boolean muestraCampoNuevoTcc);

    /**
     * Establece el valor del nuevo producto seleccionado para modificar el detalle de un deal.
     *
     * @param nuevoProducto El valor a asignar.
     */
    public abstract void setNuevoProducto(String nuevoProducto);

    /**
     * Regresa el valor del canal.
     *
     * @return String
     */
    public abstract String getCanal();

    /**
     * Asigna el valor del producto que originalmente trae el detalle de deal.
     *
     * @param productOrig El valor a asignar.
     */
    public abstract void setProductOrig(Map productOrig);

    /**
     * Regresa el valor de la productOrig.
     *
     * @return Map
     */
    public abstract Map getProductOrig();

    /**
     * Asigna la lista del modelo.
     *
     * @param modelLit El valor a asignar.
     */
    public abstract void setModelList(List modelLit);

    /**
     * Obtiene el valor de 'modifProduct' en caso de que se haya seleccionado un producto diferente
     * para el detalle de un deal.
     *
     * @return String.
     */
    public abstract String getModoSubmit();

    /**
     * Establece el valor de '' para que sea posible seleccionar un producto diferente.
     *
     * @param modoSubmit El valor a asignar.
     */
    public abstract void setModoSubmit(String modoSubmit);

    /**
     * Regresa la lista del modelo.
     *
     * @return List.
     */
    public abstract List getModelList();

    /**
     * Constante actualizar
     */
    public static final String ACTUALIZAR = "actualizar";

    /**
     * Constante utilizada para saber si lo que se quiere modificar es el monto del detalle de un
     * deal.
     */
    private static final int OPCION_MODIFICAR_MONTO = 0;

    /**
     * Constante utilizada para saber si lo que se quiere modificar es el producto del detalle de un
     * deal.
     */
    private static final int OPCION_MODIFICAR_PRODUCTO = 1;

    /**
     * Constante para saber si el promotor ha seleccionado un tipo de producto diferente al que ya
     * ten&iacute;a el detalle del deal.
     */
    private static final String MODIF_PRODUCT = "modifProduct";

    /**
     * Constante label
     */
    private static final String LABEL = "label";

    /**
     * Constante  value
     */
    private static final String VALUE = "value";

    /**
     * Lista que guarda todas las clave forma liquidaci&oacute;n dependiendo de la divisa.
     */
    private static List modelLit;

    /**
     * Mapa que guarda los valores de las clave forma liquidaci&oacute;n dependiendo de la divisa.
     */
    private static Map pares;
}

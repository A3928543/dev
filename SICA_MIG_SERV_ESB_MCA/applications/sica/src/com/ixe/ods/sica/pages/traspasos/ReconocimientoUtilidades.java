/*
 * $Id: ReconocimientoUtilidades.java,v 1.18 2009/08/03 22:08:48 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.traspasos;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.Posicion;
import com.ixe.ods.sica.model.RecoUtilidad;
import com.ixe.ods.sica.pages.ValidacionDealsSicaPage;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Clase para el Reconocimiento de Utilidades de los Portafolios (Mesas).
 *
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.18 $ $Date: 2009/08/03 22:08:48 $
 */
public abstract class ReconocimientoUtilidades extends ValidacionDealsSicaPage {

    /**
     * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        restablecerValoresIniciales();
        setModoSubmit(0);
        setConfirmar(true);
        setOperacionExitosa(false);
        setIdMesaOperacion(((ParametroSica) getSicaServiceData().
                find(ParametroSica.class, ParametroSica.MESA_OPERACION)).getValor());
        setSelectedPortafolio(getSicaServiceData().
                findMesaCambio(Integer.parseInt(getIdMesaOperacion())));
        setSelectedAlPortafolio(getSelectedPortafolio());
    }

    /**
     * Restablece los valores iniciales de los campos.
     */
    private void restablecerValoresIniciales() {
        setSelectedDivisa(null);
        setMonto(0.0);
        setTipoCambio(0.0);
        setTcAmaneceMesa(0.0);
        setObservaciones(null);
        setMontoNeteo(0.0);
        setReconocimientos(getSicaServiceData().findReconocimientos());
    }

    /**
     * Permite confirmar la operaci&oacute;n de Utilidad o P&eacute;rdida antes de salvarla en
     * Base de Datos.
     */
    private void confirmarMontoUtilidades() {
        setOperacionExitosa(false);
        try {
            //Obteniendo la Posicion con los valores "Del Portafolio" y la Divisa Operacion
            // (Seleccionada por el Usuario).
            Posicion posicion = getSicaServiceData().
                    findRegistroPosicionByIdMesaCambioAndIdDivisa(getSelectedPortafolio().
                            getIdMesaCambio(), getSelectedDivisa().getIdDivisa());
            //Calculando el Monto de la Perdida o Utilidad, y calculando el Monto por Neteo.
            setMonto(getSicaServiceData().calculaMontoReconocimientoUtilidades(posicion,
                    getTcAmaneceMesa()));
            if (!isDivisaMXN()) {
                setMontoNeteo(getSicaServiceData().calculaMontoUtilidadesNeteo(posicion));
            }
            else {
                setMontoNeteo(0.0);
            }
            if (Math.abs(getMonto() + getMontoNeteo()) < 1.0) {
                getDelegate().record("No se puede llevar a cabo el Reconocimiento de Utilidades. " +
                        "Revise el Tipo de Cambio o la Posici\u00f3n, ya que el Monto de la " +
                        "Utilidad es 0.0.", null);
                return;
            }
            int aux = identificaOperacion();
            setOperacionExitosa(true);
            getDelegate().record(construyeMensajeFinal(aux, true), null);
        }
        catch (Exception e) {
        	if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
            getDelegate().record("Hubo un error al intentar hacer el Reconocimiento de Utilidades.",
                    null);
        }
    }

    /**
     * Crea un registro Reconocimiento de Utilidades con los datos proporcionados.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void crearReconocimientoUtilidad(IRequestCycle cycle) {
    	if (getSelectedPortafolio().getIdMesaCambio() == Constantes.ID_MESA_TRADER_USD) {
    		checarDealsPendientes();
        }
        else {
            setConfirmar(true);
        }
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        if (getModoSubmit() == 1 || getModoSubmit() == 2) {
        	if (!isDivisaMXN()) {
        		RecoUtilidad ru = getSicaServiceData().
                        findReconocimientoByMesaAndDivisa(getSelectedPortafolio().getIdMesaCambio(),
                                getSelectedDivisa().getIdDivisa());
		        if (ru != null) {
		            delegate.record("La Utilidad del d\u00eda para el Portafolio " +
                            getSelectedPortafolio().getNombre() + " y la Divisa " +
                            getSelectedDivisa().getIdDivisa() + " ya fue Reconocida. S\u00f3lo se" +
                            " puede hacer reconocimiento por Mesa Divisa, cuando la divisa de " +
                            "referencia de la mesa no es Pesos Mexicanos, una vez al d\u00eda.",
                            null);
		            return;
		        }
        	}
        }
        if (getModoSubmit() == 1) {
        	confirmarMontoUtilidades();
        }
        else if (getModoSubmit() == 2) {
        	setModoSubmit(0);
            setOperacionExitosa(false);
            setMonto(0.0);
            setMontoNeteo(0.0);
            if (delegate != null && delegate.getHasErrors()) {
                return;
            }
            if (!isDivisaMXN()) {
                if (getTipoCambio() <= 0.0) {
                    delegate.record("El Tipo Cambio debe ser mayor a 0.0", null);
                    return;
                }
            }
            if (getTcAmaneceMesa() <= 0.0) {
                delegate.record("El Tipo Cambio Amanece Mesa debe ser mayor a 0.0", null);
                return;
            }
            if (getObservaciones().length() > 60) {
                delegate.record("Las Observaciones no deben exceder 60 caracteres.", null);
                return;
            }
            Visit visit = (Visit) getVisit();
            try {
                //IdCanal "Del Portafolio"
                ParametroSica idCanalDel = (ParametroSica) getSicaServiceData().
                        find(ParametroSica.class, "CANAL_MESA_" +
                                getSelectedPortafolio().getNombre().trim());
                ParametroSica idCanalAl;
                Posicion posicion = getSicaServiceData().
                        findRegistroPosicionByIdMesaCambioAndIdDivisa(getSelectedPortafolio().
                                getIdMesaCambio(), getSelectedDivisa().getIdDivisa());
                if (isDivisaMXN()) {
                    setTipoCambio(0.0);
                    setMonto(getSicaServiceData().storeReconocimientoUtilidades(visit.getUser().
                            getIdUsuario(), idCanalDel.getValor(), null, getSelectedPortafolio(),
                            getSelectedAlPortafolio(), getSelectedDivisa(), Constantes.CASH,
                            getTipoCambio(), getObservaciones(), isDivisaMXN(), getTcAmaneceMesa(),
                            1.0));
                    setMontoNeteo(0.0);
                }
                else {
                    //IdCanal "Al Portafolio"
                    idCanalAl = (ParametroSica) getSicaServiceData().find(ParametroSica.class,
                            "CANAL_MESA_" + getSelectedAlPortafolio().getNombre().trim());
                    setMonto(getSicaServiceData().storeReconocimientoUtilidades(visit.getUser().
                            getIdUsuario(), idCanalDel.getValor(), idCanalAl.getValor(),
                            getSelectedPortafolio(), getSelectedAlPortafolio(), getSelectedDivisa(),
                            Constantes.CASH, getTipoCambio(), getObservaciones(), isDivisaMXN(),
                            getTcAmaneceMesa(), getTipoCambio()));
                    setMontoNeteo(getSicaServiceData().calculaMontoUtilidadesNeteo(posicion));
                }
                if (Math.abs(getMonto() + getMontoNeteo()) < 1.0) {
                    delegate.record("No se puede llevar a cabo el Reconocimiento de Utilidades. " +
                            "Revise el Tipo de Cambio o la Posici\u00f3n, ya que el Monto de la " +
                            "Utilidad es 0.0.", null);
                    return;
                }
                int aux = identificaOperacion();
                setOperacionExitosa(true);
                delegate.record(construyeMensajeFinal(aux, false), null);
                restablecerValoresIniciales();
            }
            catch (Exception e) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug(e);
                }
                delegate.record("Hubo un error al intentar hacer el Reconocimiento de Utilidades.",
                        null);
            }
        }
    }

    /**
     * Permite borrar un registro de SC_RECO_UTILIDAD.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void borrarReconocimiento(IRequestCycle cycle) {
        RecoUtilidad ru = (RecoUtilidad) getSicaServiceData().find(RecoUtilidad.class,
                new Integer(cycle.getServiceParameters()[0].toString()));
        if (ru != null) {
            getSicaServiceData().delete(ru);
        }
        restablecerValoresIniciales();
    }

    /**
     * Permite identificar el tipo de operaci&oacute;n que se realiz&oacute;.
     *
     * @return int.
     */
    private int identificaOperacion() {
        if ((getMonto() > 0) && isDivisaMXN()) {
            return 1;
        }
        else if (((getMonto() + getMontoNeteo()) > 0) && !isDivisaMXN()) {
            return 2;
        }
        else if ((getMonto() < 0) && isDivisaMXN()) {
            return 3;
        }
        else if (((getMonto() + getMontoNeteo()) < 0) && !isDivisaMXN()) {
            return 4;
        }
        return 0;
    }


    /**
     * Permite construir un mensaje que le especifique al usuario la operaci&oacute;n que
     * efectu&oacute;
     *
     * @param aux El tipo de operaci&oacute;n
     * @param confirmar Si el usuario esta confirmando su operaci&oacute;n o ya la acept&oacute;
     * @return mensaje El mensaje formado.
     */
    private String construyeMensajeFinal(int aux, boolean confirmar) {
        String mensaje = confirmar ? "El Reconocimiento de Utilidades quedar\u00e1: " :
                "El Reconocimiento de Utilidades se realiz\u00f3 con \u00e9xito. ";
        switch (aux) {
            case 1 :
                mensaje += "Se compraron $" + getMoneyFormat().format(getMonto()) +
                        " pesos como utilidad a la Mesa.";
                break;
            case 2 :
                mensaje += "Se compraron $" + (confirmar ? getMoneyFormat().format(getMonto() +
                        getMontoNeteo()) : getMoneyFormat().format(getMonto())) + " " +
                        getSelectedPortafolio().getDivisaReferencia().getIdDivisa() +
                        " como utilidad a la Mesa. De los cuales por ajuste del TC son: " +
                        (!confirmar ? getMoneyFormat().format(getMonto() -
                                getMontoNeteo()) : getMoneyFormat().format(getMonto())) +
                        " y por neteo de operaciones: " + getMoneyFormat().format(getMontoNeteo()) +
                        ".";
                break;
            case 3 :
                mensaje += "Se vendieron $" + getMoneyFormat().format(getMonto()) +
                        " pesos como p\u00e9rdida a la Mesa.";
                break;
            case 4 :
                mensaje += "Se vendieron $" + (confirmar ? getMoneyFormat().format(getMonto() +
                        getMontoNeteo()) : getMoneyFormat().format(getMonto())) + " " +
                        getSelectedPortafolio().getDivisaReferencia().getIdDivisa() +
                        " como p\u00e9rdida a la Mesa. De los cuales por ajuste del TC son: " +
                        (!confirmar ? getMoneyFormat().format(getMonto() - getMontoNeteo()) :
                                getMoneyFormat().format(getMonto())) +
                        " y por neteo de operaciones: " +
                        getMoneyFormat().format(getMontoNeteo()) + ".";
                break;
        }
        return mensaje;
    }

    /**
     * Llena el combo "Portafolio".
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getPortafolioModel() {
        return new RecordSelectionModel(getSicaServiceData().findAll(MesaCambio.class),
                "idMesaCambio", "nombre");
    }

    /**
     * Llena el combo de Divisas.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getDivisasModel() {
    	Visit visit = (Visit) getVisit();
        Canal canal = getSicaServiceData().findCanal(visit.getIdCanal());
        return new RecordSelectionModel(getSicaServiceData().findDivisasByTipoPizarron(
                		canal.getTipoPizarron().getIdTipoPizarron().intValue()),
                "idDivisa", "idDivisa");
    }

    /**
     * Indica si la Moneda de Referencia del Portafolio seleccionado es MXN.
     *
     * @return boolean.
     */
    public boolean isDivisaMXN() {
        return Divisa.PESO.equals(getSelectedPortafolio().getDivisaReferencia().getIdDivisa());
    }

    /**
     * Permite saber si hay deals pendientes por autorizaci&oacute;n de alg&uacute;n tipo.
     *
     */
    public void checarDealsPendientes() {
        //Se obtienen los deals a checar
        List deals = new ArrayList();
        List dealsSwaps = new ArrayList();
        try {
            deals = getSicaServiceData().findDealsBlockerVespertino();
            dealsSwaps = getSicaServiceData().findSwapCierreDelDia();
        }
        catch (SicaException e1) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e1);
            }
		}
		try {
            //Checa Deals Detenidos por Banxico
            setDealsPendientesDocumentacion(validarDealsBlocker(deals,
                    DEALS_PENDIENTES_POR_DOCUMENTACION, true, false));
            //Checa Deals Detenidos por Banxico
            setDealsPendientesBanxico(validarDealsBlocker(deals, DEALS_PENDIENTES_POR_BANXICO,
                    true, false));
            //Checa Deals Detenidos por Toma
            setDealsPendientesLinTomaEnFirme(validarDealsBlocker(deals,
                    DEALS_PENDIENTES_POR_TOMA_EN_FIRME, true, false));
            //Checa Deals Detenidos por No Balanceado
            setDealsPendientesNoBalanceado(validarDealsBlocker(deals, DEALS_PENDIENTES_POR_BALANCE,
                    true, false));
            //Checa Deals Detenidos por Modificacion
            setDealsPendientesModCan(validarDealsBlocker(deals, DEALS_PENDIENTES_POR_MODIFICACION,
                    false, false));
            //Checa Deals Detenidos por Detalles Pendientes
            setDealsDetallesPendientes(validarDealsBlocker(deals,
                    DEALS_PENDIENTES_POR_ALGUN_DETALLE, true, false));
            //Checa Deals Detenidos por Completar
            setDealsPendientesCompletar(validarDealsBlocker(deals, DEALS_PENDIENTES_POR_COMPLETAR,
                    true, false));
            //Checa Deals Detenidos por Swap
            setDealsSwap(validarDealsBlocker(dealsSwaps, DEALS_PENDIENTES_POR_SWAP, true, false));
            //Checa Deals Detenidos por Contrato Sica
            setDealsPendientesContratoSica(validarDealsBlocker(deals, DEALS_PENDIENTES_POR_CONTRATO,
                    false, false));
		}
        catch (Exception e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
    }

    /**
     * Permite saber si ya se oprimi&oacute; el bot&oacute;n Aceptar.
     *
     * @return int Aceptar: 1.
     */
    public abstract int getModoSubmit();

    /**
     * Fija el Modo Submit de la P&aacute;gina en Aceptar: 1.
     *
     * @param modoSubmit Aceptar: 1.
     */
    public abstract void setModoSubmit(int modoSubmit);

    /**
     * Obtiene la <code>MesaCambio</code> seleccionada del Combo "Del Portafolio".
     *
     * @return MesaCambio.
     */
    public abstract MesaCambio getSelectedPortafolio();

    /**
     * Fija la <code>MesaCambio</code> seleccionada del Combo "Del Portafolio".
     *
     * @param selectedPortafolio El portafolios seleccionado.
     */
    public abstract void setSelectedPortafolio(MesaCambio selectedPortafolio);

    /**
     * Obtiene la <code>MesaCambio</code> seleccionada del Combo "Al Portafolio".
     *
     * @return MesaCambio.
     */
    public abstract MesaCambio getSelectedAlPortafolio();

    /**
     * Fija la <code>MesaCambio</code> seleccionada del Combo "Al Portafolio".
     *
     * @param selectedAlPortafolio El portafolio destino.
     */
    public abstract void setSelectedAlPortafolio(MesaCambio selectedAlPortafolio);

    /**
     * Obtiene la <code>Divisa</code> seleccionada del Combo de Divisa Operaci&oacute;n.
     *
     * @return Divisa.
     */
    public abstract Divisa getSelectedDivisa();

    /**
     * Fija la <code>Divisa</code> seleccionada del Combo de Divisa Operaci&oacute;n.
     *
     * @param divisa El valor a asignar.
     */
    public abstract void setSelectedDivisa(Divisa divisa);

    /**
     * Obtiene el monto calculado del Reconocimiento de Utilidades.
     *
     * @return double El Monto.
     */
    public abstract double getMonto();

    /**
     * Fija el monto calculado del Reconocimiento de Utilidades.
     *
     * @param monto El valor a asignar.
     */
    public abstract void setMonto(double monto);

    /**
     * Obtiene el <code>tipoCambio</code> tecleado del Reconocimiento de Utilidad.
     *
     * @return double El tipoCambio.
     */
    public abstract double getTipoCambio();

    /**
     * Fija el <code>tipoCambio</code> tecleado del Reconocimiento de Utilidad.
     *
     * @param tipoCambio El valor a asignar.
     */
    public abstract void setTipoCambio(double tipoCambio);

    /**
     * Obtiene el valor del campo <code>Observaciones</code> del Reconocimiento de Utilidad.
     *
     * @return String Las Observaciones.
     */
    public abstract String getObservaciones();

    /**
     * Fija el valor del campo <code>Observaciones</code> del Reconocimiento de Utilidad.
     *
     * @param observaciones El valor a asignar.
     */
    public abstract void setObservaciones(String observaciones);

    /**
     * Bandera que indica si el Reconocimiento de Utilidad fue exitoso.
     *
     * @param operacionExitosa True o False.
     */
    public abstract void setOperacionExitosa(boolean operacionExitosa);

    /**
     * Obtiene el <code>tcAmaneceMesa</code> tecleado al que Amanece la Mesa.
     *
     * @return double El tcAmaneceMesa.
     */
    public abstract double getTcAmaneceMesa();

    /**
     * Fija el <code>tcAmaneceMesa</code> tecleado al que Amanece la Mesa.
     *
     * @param tcAmaneceMesa El valor a asignar.
     */
    public abstract void setTcAmaneceMesa(double tcAmaneceMesa);

    /**
     * Obtiene el <code>idMesaOperacion</code>.
     *
     * @return String El identificador de la Mesa de Operaci&oacute;n.
     */
    public abstract String getIdMesaOperacion();

    /**
     * Fija el <code>idMesaOperacion</code>.
     *
     * @param idMesaOperacion El identificador de la Mesa de Operaci&oacute;n.
     */
    public abstract void setIdMesaOperacion(String idMesaOperacion);

    /**
     * Regresa el valor de reconocimientos.
     *
     * @return List.
     */
    public abstract List getReconocimientos();

    /**
     * Fija la lista de registros de Reconocimientos del D&iacute;a.
     *
     * @param reconocimientos La lista de Reconocimientos encontrados.
     */
    public abstract void setReconocimientos(List reconocimientos);

    /**
     * Obtiene el monto por el neteo de las operaciones de Reconocimiento de Utilidades.
     *
     * @return double El Monto Neteo.
     */
    public abstract double getMontoNeteo();

    /**
     * Fija el monto por el neteo de las operaciones de Reconocimiento de Utilidades.
     *
     * @param montoNeteo El valor a asignar.
     */
    public abstract void setMontoNeteo(double montoNeteo);

    /**
     * Regresa el valor de dealsPendientesNoBalanceado.
     *
     * @return List.
     */
    public abstract List getDealsPendientesNoBalanceado();

    /**
     * Fija el valor de dealsPendientesNoBalanceado
     *
     * @param dealsPendientesNoBalanceado El valor a asignar.
     */
    public abstract void setDealsPendientesNoBalanceado(List dealsPendientesNoBalanceado);

    /**
     * Regresa el valor de dealsPendientesBanxico.
     *
     * @return List.
     */
    public abstract List getDealsPendientesBanxico();

    /**
     * Fija el valor de dealsPendientesBanxico.
     *
     * @param dealsPendientesBanxico El valor a asignar.
     */
    public abstract void setDealsPendientesBanxico(List dealsPendientesBanxico);

    /**
     * Regresa el valor de dealsPendientesDocumentacion
     *
     * @return List.
     */
    public abstract List getDealsPendientesDocumentacion();

    /**
     * Fija el valor de dealsPendientesDocumentacion
     *
     * @param dealsPendientesDocumentacion El valor a asignar.
     */
    public abstract void setDealsPendientesDocumentacion(List dealsPendientesDocumentacion);

    /**
     * Regresa el valor de dealsSwap
     *
     * @return List.
     */
    public abstract List getDealsSwap();

    /**
     * Fija el valor de dealsSwap
     *
     * @param dealsSwap El valor a asignar.
     */
    public abstract void setDealsSwap(List dealsSwap);

    /**
     * Regresa el valor de dealsPendientesContratoSica
     *
     * @return List.
     */
    public abstract List getDealsPendientesContratoSica();

    /**
     * Fija el valor de dealsPendientesContratoSica
     *
     * @param dealsPendientesContratoSica El valor a asignar.
     */
    public abstract void setDealsPendientesContratoSica(List dealsPendientesContratoSica);

    /**
     * Regresa el valor de dealsPendientesCompletar
     *
     * @return List.
     */
    public abstract List getDealsPendientesCompletar();

    /**
     * Fija el valor de dealsPendientesCompletar
     *
     * @param dealsPendientesCompletar El valor a asignar.
     */
    public abstract void setDealsPendientesCompletar(List dealsPendientesCompletar);

    /**
     * Regresa el valor de dealsDetallePendientes
     *
     * @return List.
     */
    public abstract List getDealsDetallesPendientes();

    /**
     * Fija el valor de dealsDetallePendientes
     *
     * @param dealsDetallesPendientes El valor a asignar.
     */
    public abstract void setDealsDetallesPendientes(List dealsDetallesPendientes);

    /**
     * Regresa el valor de dealsPendientesModCan
     *
     * @return List.
     */
    public abstract List getDealsPendientesModCan();

    /**
     * Fija el valor de dealsPendientesModCan
     *
     * @param dealsPendientesModCan El valor a asignar.
     */
    public abstract void setDealsPendientesModCan(List dealsPendientesModCan);

    /**
     * Regresa el valor de dealsPendientesNoBalanceado.
     *
     * @return List.
     */
    public abstract List getDealsPendientesLinTomaEnFirme();

    /**
     * Fija el valor de dealsPendientesNoBalanceado
     *
     * @param dealsPendientesLinTomaEnFirme El valor a asignar.
     */
    public abstract void setDealsPendientesLinTomaEnFirme(List dealsPendientesLinTomaEnFirme);

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
}

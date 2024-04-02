/*
 * $Id: SeleccionBroker.java,v 1.15 2008/07/29 04:34:06 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.interbancario;

import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Broker;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.LineaOperacion;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.pages.Mensaje;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.SicaServiceData;

/**
 * P&aacute;gina que muestra los brokers existentes para que el usuario seleccione uno y prosiga
 * con la captura de un deal interbancario.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.15 $ $Date: 2008/07/29 04:34:06 $
 */
public abstract class SeleccionBroker extends SicaPage {

    /**
     * Inicializa la propiedad <i>brokers</i> con todos los registros de la clase Broker.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        inicializar(false);
        if (cycle.getServiceParameters() != null && cycle.getServiceParameters().length > 0) {
            String facultad = (String) cycle.getServiceParameters()[0];
            setModoCobertura(FacultySystem.SICA_DEAL_INTER_COB.equals(facultad));
            setModoNeteo(FacultySystem.SICA_CAPT_DEAL_NETEO.equals(facultad));
        }
        if (isModoCobertura()) {
            MesaCambio mesaCambio = getSicaServiceData().findMesaCambio(
                    ((Visit) getVisit()).getIdMesaCambio());
            if (!Divisa.PESO.equals(mesaCambio.getDivisaReferencia().getIdDivisa())) {
                Mensaje nextPage = (Mensaje) cycle.getPage("Mensaje");
                nextPage.setTipo(-1);
                nextPage.setMensaje("La captura de coberturas s\u00f3lo est\u00e1 disponible "
                        + "cuando la divisa de referencia de la mesa es Pesos Mexicanos");
                nextPage.activate(cycle);
            }
        }
    }

    /**
     * Asigna el valor de modoBrokerFaltante. Si &eacute;ste es true, inicializa la lista de brokers
     * con contrapartes que son Instituciones Financieras; en caso contrario, inicializa la lista de
     * brokers con todas las contrapartes existentes.
     *
     * @param modoBrokerFaltante true: S&oacute;lo instituciones financieras).
     */
    public void inicializar(boolean modoBrokerFaltante) {
        setModoBrokerFaltante(modoBrokerFaltante);
        if (modoBrokerFaltante) {
            setBrokers(getSicaServiceData().findAllBrokersInstFin());
        }
        else {
            setBrokers(getSicaServiceData().findAllBrokers());
        }
        Iterator it=this.getBrokers().iterator();
        while(it.hasNext()){
        	Broker brk=(Broker)it.next();
        	if (brk.getStatus().equals(Broker.STATUS_INACTIVO)){
        		it.remove();
        	}
        }
    }

    /**
     * Define si el horario del sistema es Horario Vespertino.
     *
     * @return boolean
     */
    protected boolean isHorarioVespertino() {
        return getEstadoActual().getIdEstado() == Estado.ESTADO_OPERACION_VESPERTINA;
    }

    /**
     * Activa la p&aacute;gina <i>CapturaDatosDealInterbancario</i> llamando a
     * <code>crearDeal()</code> pasando como par&aacute;metro el broker seleccionado por el usuario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see CapturaDatosDealInterbancario#crearDeal(com.ixe.ods.sica.model.Broker).
     */
    public void seleccionarBroker(IRequestCycle cycle) {
        if (isHorarioVespertino()) {
            getDelegate().record("No es posible capturar Deals durante el horario de " +
                    "operaci\u00f3n Vespertino.", null);
        }
        else {
            int idPersona = ((Integer) cycle.getServiceParameters()[0]).intValue();
            Broker broker = null;
            for (Iterator it = getBrokers().iterator(); it.hasNext() && broker == null;) {
                Broker b = (Broker) it.next();
                if (b.getId().getPersonaMoral().getIdPersona().intValue() == idPersona) {
                    broker = b;
                }
            }
            try {
                // Reviso que no este suspendida la linea de operacion, si es inst. fin.
                if (broker != null &&
                        Broker.INSTITUCION_FINANCIERA.equals(broker.getTipoBroker())) {
                    SicaServiceData sd = getSicaServiceData();
                    LineaOperacion lo = sd.findLineaOperacionByBroker(idPersona);
                    if (lo != null &&
                            LineaOperacion.STATUS_SUSPENDIDA.equals(lo.getStatusLinea())) {
                        throw new SicaException("La l\u00ednea de operaci\u00f3n de la " +
                                "contraparte " + broker.getId().getPersonaMoral().
                                getNombreCompleto() + " est\u00e1 suspendida.");
                    }
                }
                if (isModoBrokerFaltante()) {
                    CapturaDealInterbancario nextPage = (CapturaDealInterbancario) cycle.
                            getPage("CapturaDealInterbancario");
                    nextPage.setModoInsercion(true);
                    nextPage.definirContraparte(broker);
                    nextPage.activate(cycle);
                }
                else {
                    CapturaDatosDealInterbancario nextPage = (CapturaDatosDealInterbancario) cycle.
                            getPage("CapturaDatosDealInterbancario");
                    nextPage.setModoCobertura(isModoCobertura());
                    nextPage.setModoNeteo(isModoNeteo());
                    nextPage.crearDeal(broker);
                    nextPage.activate(cycle);
                }
            }
            catch (SicaException e) {
                getDelegate().record(e.getMessage(), null);
                if (_logger.isDebugEnabled()) {
                    _logger.debug(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Activa la p&aacute;gina <i>CapturaDatosDealInterbancario</i> llamando a
     * <code>crearDeal()</code> pasando como par&aacute;metro null como broker.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see CapturaDatosDealInterbancario#crearDeal(com.ixe.ods.sica.model.Broker).
     */
    public void crearSinBroker(IRequestCycle cycle) {
        try {
            CapturaDatosDealInterbancario nextPage = (CapturaDatosDealInterbancario) cycle.
                    getPage("CapturaDatosDealInterbancario");
            nextPage.crearDeal(null);
            nextPage.activate(cycle);
        }
        catch (SicaException e) {
            IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
            delegate.record(e.getMessage(), null);
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
    }

    /**
     * No hace nada.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
    }

    /**
     * Regresa el valor de brokers.
     *
     * @return List.
     */
    public abstract List getBrokers();

    /**
     * Fija el valor de brokers.
     *
     * @param brokers El valor a asignar.
     */
    public abstract void setBrokers(List brokers);

    /**
     * Regresa el valor de modoBrokerFaltante.
     *
     * @return boolean.
     */
    public abstract boolean isModoBrokerFaltante();

    /**
     * Fija el valor de modoBrokerFaltante.
     *
     * @param modoBrokerFaltante El valor a asignar.
     */
    public abstract void setModoBrokerFaltante(boolean modoBrokerFaltante);

    /**
     * Regresa el valor de modoCobertura.
     *
     * @return boolean.
     */
    public abstract boolean isModoCobertura();

    /**
     * Establece el valor de modoCobertura.
     *
     * @param modoCobertura El valor a asignar.
     */
    public abstract void setModoCobertura(boolean modoCobertura);

    /**
     * Regresa el valor de modoNeteo.
     *
     * @return boolean.
     */
    public abstract boolean isModoNeteo();

    /**
     * Establece el valor de modoNeteo.
     *
     * @param modoNeteo El valor a asignar.
     */
    public abstract void setModoNeteo(boolean modoNeteo);
}

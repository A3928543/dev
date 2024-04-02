/*
 * $Id: AbstractDealsCancelados.java,v 1.4 2008/02/22 18:25:10 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages;

import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.sdo.DealServiceData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IRequestCycle;

/**
 * P&aacute;gina que permite al usuario consultar los deals de captura r&aacute;pida que fueron
 * cancelados por no tener asignado un contrato sica a tiempo. La consulta se puede realizar por
 * rango de fechas, promotor y canal.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.4 $ $Date: 2008/02/22 18:25:10 $
 */
public abstract class AbstractDealsCancelados extends SicaPage {

    /**
     * Inicializa las listas para los combo box de Canales y Promotores.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        try {
            List promotores;
            SicaServiceData sd = getSicaServiceData();
            setDeals(new ArrayList());
            if (FacultySystem.SICA_CDCCR_DP.equals(cycle.getServiceParameters()[0])) {
                List canales = new ArrayList();
                String idCanal = ((Visit) getVisit()).getIdCanal();
                canales.add(sd.findCanal(idCanal));
                setCanales(canales);
                promotores = getPromotoresJerarquia();
            }
            else {
                List canales = sd.findAllCanalesByPromocionMayoreo();
                Canal canal = new Canal();
                canal.setIdCanal("");
                canal.setNombre(Constantes.TODOS);
                canales.add(0, canal);
                setCanales(canales);
                promotores = sd.findAllPromotoresSICA(((SupportEngine) getEngine()).
                        getApplicationName());
            }
            EmpleadoIxe prom = new EmpleadoIxe();
            prom.setIdPersona(new Integer(0));
            prom.setNombre(Constantes.TODOS);
            promotores.add(0, prom);
            setPromotores(promotores);
        }
        catch (SicaException e) {
            debug(e);
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * Llama al servicicio findDealsCanceladosCapturaRapida() para encontrar los deals que cumplen
     * con los criterios especificados por el usuario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see com.ixe.ods.sica.sdo.DealServiceData#findDealsCapturaRapida(java.util.Date,
            java.util.Date, String, Integer, boolean).
     */
    public void buscar(IRequestCycle cycle) {
        try {
            if (getFechaInicio() == null) {
                throw new SicaException("Seleccione la fecha de inicio por favor.");
            }
            if (getFechaFin() == null) {
                throw new SicaException("Seleccione la fecha de fin por favor.");
            }
            Integer idPromotor = getPromotorSeleccionado().getIdPersona().intValue() == 0
                    ? null : getPromotorSeleccionado().getIdPersona();
            String idCanal = getCanalSeleccionado().getIdCanal().length() == 0
                    ? null : getCanalSeleccionado().getIdCanal();
            DealServiceData dsd = (DealServiceData) getApplicationContext().
                    getBean("dealServiceData");
            setDeals(dsd.findDealsCapturaRapida(getFechaInicio(), getFechaFin(), idCanal,
                    idPromotor, true));
        }
        catch (SicaException e) {
            setDeals(new ArrayList());
            getDelegate().record(e.getMessage(), null);
            debug(e);
        }
    }

    /**
     * Regresa el valor de fechaInicio.
     *
     * @return Date.
     */
    public abstract Date getFechaInicio();

    /**
     * Regresa el valor de fechaFin.
     *
     * @return Date.
     */
    public abstract Date getFechaFin();

    /**
     * Regresa el valor de canalSeleccionado.
     *
     * @return Canal.
     */
    public abstract Canal getCanalSeleccionado();

    /**
     * Establece el valor de canales.
     *
     * @param canales El valor a asignar.
     */
    public abstract void setCanales(List canales);

    /**
     * Regresa el valor de promotorSeleccionado.
     *
     * @return EmpleadoIxe.
     */
    public abstract EmpleadoIxe getPromotorSeleccionado();

    /**
     * Establece el valor de promotores.
     *
     * @param promotores El valor a asignar.
     */
    public abstract void setPromotores(List promotores);

    /**
     * Establece el valor de deals.
     *
     * @param deals El valor a asignar.
     */
    public abstract void setDeals(List deals);
}

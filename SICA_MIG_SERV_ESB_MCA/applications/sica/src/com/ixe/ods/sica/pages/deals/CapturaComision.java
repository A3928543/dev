/*
 * $Id: CapturaComision.java,v 1.13.40.2 2011/05/02 17:09:48 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pizarron.Consts;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.treasury.dom.common.FormaPagoLiq;

/**
 * P&aacute;gina que permite al promotor capturar la comisi&oacute;n que se cobrar&aacute; en un
 * detalle del deal.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.13.40.2 $ $Date: 2011/05/02 17:09:48 $
 */
public abstract class CapturaComision extends SicaPage {

    /**
     * Cada vez que se tiene acceso a la pagina carga sus parametros iniciales.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        if (getDealDetalle().getMnemonico() == null) {
            getDelegate().record("No es posible capturar Comisiones sin antes haber definido la " +
                    "Forma de Liquidaci\u00f3n (Mnem\u00f3nico) y su respectiva Plantilla.", null);
            return;
        }
        if (getDealDetalle().getComisionCobradaUsd() == 0) {
            getDealService().calcularComisionDealDetalle(getTicket(), getDealDetalle(), true);
        }
    }

    /**
     * Define si el estado de operacione actual es Horario Vespertino.
     *
     * @return boolean
     */
    protected boolean isHorarioVespertino() {
        return getEstadoActual().getIdEstado() == Estado.ESTADO_OPERACION_VESPERTINA;
    }

    /**
     * Calcula la comisi&oacute;n en pesos a partir de la comisi&oacute;n capturada en USD y
     * actualiza el registro del Detalle del Deal en la base de datos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see SicaServiceData#calcularComisionMxn(com.ixe.ods.sica.model.DealDetalle,
     *      com.ixe.ods.sica.model.PrecioReferenciaActual).
     */
    public void submit(IRequestCycle cycle) {
        refrescarEstadoActual();
        IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
        if (delegate.getHasErrors()) {
            return;
        }
        if (isHorarioVespertino()) {
            delegate.record("No es posible capturar Comisi\u00f3n para un detalle de deal en " +
                    "Horario Vespertino.", null);
        }
        else {
            SicaServiceData sd = getSicaServiceData();
            DealDetalle det = getDealDetalle();
            sd.calcularComisionMxn(det, det.getPrecioReferenciaSpot().doubleValue());
            sd.update(det);
            if (det.getMnemonico() != null) {
                FormaPagoLiq fpl = getFormaPagoLiq(det.getMnemonico());
                double comisionUsd = fpl.getComision() != null ?
                        fpl.getComision().doubleValue() : 0.0;
                ContratoSica cs = sd.findContrato(det.getDeal().getContratoSica().getNoCuenta());
                if (det.getComisionCobradaUsd() > 0 && comisionUsd > 0) {
                    cs.setPorcentajeComision(Consts.NUM_100 * det.getComisionCobradaUsd() /
                            comisionUsd);
                }
                sd.update(cs);
            }
            CapturaDeal nextPage = (CapturaDeal) cycle.getPage("CapturaDeal");
            nextPage.setDeal(getDealDetalle().getDeal());
            setDealDetalle(null);
            cycle.activate(nextPage);
        }
    }

    /**
     * Regresa el valor de dealDetalle.
     *
     * @return DealDetalle.
     */
    public abstract DealDetalle getDealDetalle();

    /**
     * Fija el valor de dealDetalle.
     *
     * @param det El valor a asignar.
     */
    public abstract void setDealDetalle(DealDetalle det);
}

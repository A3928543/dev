/*
 * $Id: CapturaObservacionesDeal.java,v 1.4 2008/06/05 01:15:13 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * P&aacute;gina que permite al usuario capturar las observaciones del deal.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.4 $ $Date: 2008/06/05 01:15:13 $
 */
public abstract class CapturaObservacionesDeal extends SicaPage {

    /**
     * Valida que la longitud de las instrucciones tecleadas no exceda 512 caracteres. Actualiza el
     * registro del deal en la base de datos.
     *
     * @param cycle El IRequestCycle.
     */
    public void actualizar(IRequestCycle cycle) {
        try {
            Deal deal = getDeal();
            if (deal.getObservaciones().length() > 512) {
                throw new SicaException("La longitud de las observaciones no debe exceder 512 " +
                        "caracteres.");
            }
            if (deal.getIdDeal() > 0) {
                getWorkFlowServiceData().actualizarDatosDeal(getTicket(), getDeal());
            }
            regresar(cycle);
        }
        catch (SicaException e) {
            debug(e);
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * Define si el area de texto para ingresar elcontenido de 
     * las observacionesdebe estar habilitada.
     *
     * @return boolean
     */
    public boolean isAreaHabilitada() {
        Estado estadoActual = getEstadoActual();
        return Estado.ESTADO_OPERACION_NORMAL == estadoActual.getIdEstado() ||
                Estado.ESTADO_OPERACION_RESTRINGIDA == estadoActual.getIdEstado() ||
                Estado.ESTADO_OPERACION_VESPERTINA == estadoActual.getIdEstado();
    }

    /**
     * Activa la p&aacute;gina que redireccion&oacute; a esta (CapturaDeal o
     * CapturaDealInterbancario).
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void regresar(IRequestCycle cycle) {
        cycle.activate(getNombrePaginaAnterior());
    }

    /**
     * Regresa el valor de deal.
     *
     * @return Deal.
     */
    public abstract Deal getDeal();

    /**
     * Establece el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public abstract void setDeal(Deal deal);

    /**
     * Regresa el valor de nombrePaginaAnterior.
     *
     * @return String.
     */
    public abstract String getNombrePaginaAnterior();

    /**
     * Establece el valor de nombrePaginaAnterior.
     *
     * @param nombrePaginaAnterior El valor a asignar.
     */
    public abstract void setNombrePaginaAnterior(String nombrePaginaAnterior);
}

/*
 * $Id: AbstractCapturaLiq.java,v 1.17 2009/08/03 21:59:53 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.deals.formliq;

import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * Superclase de las p&aacute;ginas que permiten capturar los datos de una plantilla para el detalle de un deal.
 * Implementa o declara la funcionalidad com&uacute;n.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.17 $ $Date: 2009/08/03 21:59:53 $
 */
public abstract class AbstractCapturaLiq extends SicaPage implements ICapturaLiquidacion {

    /**
     * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        // El super.activate() falla en algunos casos, solo se revisa el estado actual:
        try {
            refrescarEstadoActual();
            revisarEstadoActual();
            cycle.activate(this);
        }
        catch (SicaException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
    }

    /**
     * No hace nada. Las subclases deben crear una instancia de IPlantilla o sus subclases.
     */
    public void crearPlantilla() {

    }

    /**
     * Regresa true si <code>modo</code> es igual a ICapturaLiquidacion.MODO_CAPTURA.
     *
     * @return boolean.
     */
    public boolean isModoCaptura() {
        return ICapturaLiquidacion.MODO_CAPTURA == getModo();
    }

    /**
     * No hace nada, las subclases deben redirigir a la pantalla de CapturaDeal.
     */
    public void cancelarOperacion() {

    }
    
    /**
     * Asigna al Detalle de Deal el evento de Autorizaci&oacute;n por Documentaci&oacute;n. 
     * 
     * @param det El detalle del deal
     */
    protected void marcarSolicitudAutorizacionPorPlantilla(DealDetalle det) {
        if (!det.getDeal().isInterbancario()) {
            det.setEvento(Deal.EV_SOLICITUD, DealDetalle.EV_IND_DOCUMENTACION);
            getSicaServiceData().update(det);
        }
    }

    /**
     * Regresa el valor de dealDetalle.
     *
     * @return DealDetalle.
     */
    public abstract DealDetalle getDealDetalle();

    /**
     * Establece el valor de dealDetalle.
     *
     * @param dealDetalle El valor a asignar.
     */
    public abstract void setDealDetalle(DealDetalle dealDetalle);

    /**
     * Regresa el valor de modo.
     *
     * @return int.
     */
    public abstract int getModo();

    /**
     * Establece el valor de modo.
     *
     * @param modo El valor a asignar.
     */
    public abstract void setModo(int modo);
    
    /**
     * Indica si el detalle ya tiene plantilla o no.
     * 
     * @return boolean.
     */
    public abstract boolean getTienePlantilla();
    
    /**
     * Fija si el detalle tiene plantilla o no.
     * 
     * @param tienePlantilla True o False.
     */
    public abstract void setTienePlantilla(boolean tienePlantilla);
    
}
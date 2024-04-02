/*
 * $Id: DealDetailTable.java,v 1.13 2008/06/05 01:15:16 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.deals.CapturaComision;
import com.ixe.ods.sica.pages.deals.SeleccionFormaLiquidacion;
import com.ixe.ods.sica.services.DealService;
import com.ixe.ods.sica.utils.DateUtils;

/**
 * Componente para mostrar los datos de los detalles de recibimos o entregamos de un deal. Permite
 * tambi&eacute;n realizar algunas operaciones sobre ellos como capturar o limpiar la forma de
 * liquidacion, etc.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.13 $ $Date: 2008/06/05 01:15:16 $
 */
public abstract class DealDetailTable extends BaseComponent {

    /**
     * Regresa false si se cumple alguna de las siguientes condiciones:
     * <ul>
     *   <li>La fecha de captura del deal no es igual a la fecha actual del sistema.</li>
     *   <li>Hay un detalle en pesos no cancelado en la contraparte.</li>
     * </ul>
     *
     * @return boolean.
     */
    public boolean puedeRecibirPesos() {
        Date inicioDia = DateUtils.inicioDia();
        if (getDeal().getFechaLiquidacion() != null &&
                getDeal().getFechaLiquidacion().before(inicioDia)) {
            return false;
        }
        List dets = isRecibimos() ?
                getDeal().getEntregamosEnPesos() : getDeal().getRecibimosEnPesos();
        for (Iterator it = dets.iterator(); it.hasNext(); ) {
            DealDetalle det = (DealDetalle) it.next();
            if (! det.isCancelado()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Regresa true si el detalle puedeRecibirPesos y es v&aacute;lido el Balanceo de entrega o
     * recepci&oacute;n del detalle del deal.
     *
     * @return boolean.
     */
    public boolean isBotonLiqEnabled() {
        DealService dealService = ((SicaPage) getPage()).getDealService();
        return getDeal().getIdDeal() > 0 && puedeRecibirPesos() &&
                ((isRecibimos() && dealService.isValidoBalanceoRec(getDeal())) ||
                        (! isRecibimos() && dealService.isValidoBalanceoEnt(getDeal())));
    }

    /**
     * Regresa el detalle que tiene el idDealPosicion especificado.
     *
     * @param idDealPosicion El id del detalle a buscar.
     * @return DealDetalle.
     */
    private DealDetalle getDealDetalle(int idDealPosicion) {
        Collection dets = (Collection) getBinding("detalles").getObject();
        for (Iterator it = dets.iterator(); it.hasNext(); ) {
            DealDetalle det = (DealDetalle) it.next();
            if (det.getIdDealPosicion() == idDealPosicion) {
                return det;
            }
        }
        return null;
    }

    /**
     * Activa la p&aacute;gina <code>SeleccionFormaLiquidacion</code> para
     * asignar mnem&oacute;nico y datos adicionales al detalle.
     *
     * @param cycle El IRequestCycle.
     * @see SeleccionFormaLiquidacion#inicializar(com.ixe.ods.sica.model.DealDetalle).
     */
    public void capturarFormaLiquidacion(IRequestCycle cycle) {
        try {
            DealDetalle det = getDealDetalle(((Integer) cycle.
                    getServiceParameters()[0]).intValue());
            SeleccionFormaLiquidacion nextPage = (SeleccionFormaLiquidacion) cycle.
                    getPage("SeleccionFormaLiquidacion");
            if (det.getPlantilla() != null) {
                nextPage.setTienePlantilla(true);
                ((SicaPage) getPage()).getSicaServiceData().
                        inicializarPlantilla(det.getPlantilla());
            }
            else {
                nextPage.setTienePlantilla(false);
            }
            nextPage.inicializar(det);
        }
        catch (SicaException e) {
            throw new ApplicationRuntimeException(e);
        }
    }

    /**
     * Define si el estado de operacione actual es Horario Vespertino.
     * 
     * @return boolean
     */
    protected boolean isHorarioVespertino() {
    	return ((SicaPage) getPage()).getEstadoActual().getIdEstado() == 
    		Estado.ESTADO_OPERACION_VESPERTINA;
    }
    
    /**
     * Activa la p&aacute;gina CapturaComision, para que el promotor edite la comisi&oacute;n a
     * cobrar. Se valida que el sistema no se encuentre en Horario Vespertino.
     *
     * @param cycle El IRequestCycle.
     */
    public void capturarComision(IRequestCycle cycle) {
        if (isHorarioVespertino()) {
            ((SicaPage) getPage()).getDelegate().record("No es posible capturar Comisi\u00f3n " +
                    "para un detalle de deal en Horario Vespertino.", null);
        }
        else {
            DealDetalle det = getDealDetalle(
                    ((Integer) cycle.getServiceParameters()[0]).intValue());
            CapturaComision nextPage = (CapturaComision) cycle.getPage("CapturaComision");
            nextPage.setDealDetalle(det);
            nextPage.activate(cycle);
        }
    }

    /**
     * Regresa el valor de disabled.
     *
     * @return boolean.
     */
    public abstract boolean isDisabled();

    /**
     * Regresa el valor de deal.
     *
     * @return Deal.
     */
    public abstract Deal getDeal();

    /**
     * Regresa el valor de recibimos.
     *
     * @return boolean.
     */
    public abstract boolean isRecibimos();
}

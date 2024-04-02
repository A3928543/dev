/*
 * $Id: DatosSwap.java,v 1.13 2010/04/13 17:00:08 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.interbancario;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.ValidationConstraint;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Swap;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.sdo.SwapsServiceData;
import com.ixe.ods.sica.services.MailKondorService;

/**
 * P&aacute;gina que permite la captura de los datos del Swap.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.13 $ $Date: 2010/04/13 17:00:08 $
 */
public abstract class DatosSwap extends SicaPage {

	/**
	 * Asigna lo valores necesarios al activarse la p&aacute;gina.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        buscarDealsPendientes();
    }

	/**
	 * Define si el swap es o no cancelable.
	 * 
	 * @return boolean.
	 */
    public boolean isSwapCancelable() {
        boolean resultado = true;
        if (Swap.STATUS_CANCELADO.equals(getSwap().getStatus())) {
            return false;
        }
        else {
            for (Iterator it = getSwap().getDeals().iterator(); it.hasNext();) {
                Deal deal = (Deal) it.next();
                if (!(Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(deal.getStatusDeal()) ||
                        Deal.STATUS_DEAL_CAPTURADO.equals(deal.getStatusDeal()))) {
                    resultado = false;
                }
            }
        }
        return resultado;
    }
    
    /**
     * Realiza la b&uacte;squeda de deals con estatus pendiente.
     */
    private void buscarDealsPendientes() {
        List deals = getSicaServiceData().findDealsPendientesAsignarASwap(getSwap().
                getContratoSica().getNoCuenta());
        for (Iterator it = deals.iterator(); it.hasNext();) {
            Deal deal = (Deal) it.next();
            if (getSwap().getDeals().contains(deal)) {
                it.remove();
            }
        }
        setDealsPendientes(deals);
    }
    
    /**
     * Obtiene el monto en pesos si la operacion del deal fue compra.
     * 
     * @param compra La operaci&oacute;n del deal.
     * @param deal El deal.
     * @return double.
     */
    public double getMonto(boolean compra, Deal deal) {
        double monto = 0.0;
        if (deal.isCompra() == compra) {
            for (Iterator it = (compra ? deal.getRecibimos() : deal.getEntregamos()).iterator();
                 it.hasNext(); ) {
                DealDetalle detalle = (DealDetalle) it.next();
                monto += detalle.getMonto();
            }
        }
        return monto;
    }

    /**
     * Obtiene la divisa del deal.
     * 
     * @param deal El deal
     * @return Divisa
     */
    public Divisa getDivisa(Deal deal) {
        DealDetalle det = (DealDetalle) (deal.isCompra() ?
                deal.getRecibimos() : deal.getEntregamos()).get(0);
        return det.getDivisa();
    }

    /**
     * Define si la asignacion del deal es o no deshabilitada.
     * 
     * @param deal El deal
     * @return boolean
     */
    public boolean isAsignacionDeshabilitada(Deal deal) {
        return getSwap().isCompra() == deal.isCompra() ||
                getSwap().getMontoPorAsignar() < getMonto(deal.isCompra(), deal) ||
                !getSwap().getDivisa().getIdDivisa().equals(getDivisa(deal).getIdDivisa());
    }

    /**
     * No hace nada.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
    }
    
    /**
     * Procesa el deal y el swap Cancelaci&oacute;n de Deals en Modo Swap
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cancelarTodo(IRequestCycle cycle) {
        try {
            Swap swap = getSwap();
            getWorkFlowServiceData().modificarSwap(getTicket(), swap,
                    SicaServiceData.TIPO_MOD_SWAP_CAN_TOT, ((Visit) getVisit()).getUser());
            MailKondorService mailKondorService = (MailKondorService) getApplicationContext().
                    getBean("mailKondorService");
            if (StringUtils.isEmpty(swap.getFolderk())) {
                mailKondorService.enviarCorreo(swap.getIdConf(), swap.getIdFolioSwap(),
                        swap.getFolderk(), MailKondorService.TIPO_CANCELA_OPERACION);
            }
        }
        catch (SicaException e) {
            warn("Error al modificar el swap.", e);
            getDelegate().record(e.getMessage(), ValidationConstraint.REQUIRED);
            SwapsServiceData swapsServiceData = (SwapsServiceData) getApplicationContext().
                    getBean("swapsServiceData");
            setSwap(swapsServiceData.findSwap(getSwap().getIdFolioSwap()));
        }
    }
        
    /**
     * Regresa el valor de swap.
     *
     * @return Swap.
     */
    public abstract Swap getSwap();

    /**
     * Establece el valor de swap.
     *
     * @param swap El valor a asignar.
     */
    public abstract void setSwap(Swap swap);

    /**
     * Regresa el valor de dealsPendientes.
     *
     * @return List.
     */
    public abstract List getDealsPendientes();

    /**
     * Establece el valor de dealsPendientes.
     *
     * @param dealsPendientes El valor a asignar.
     */
    public abstract void setDealsPendientes(List dealsPendientes);
}

/*
 * $Id: InfoSwap.java,v 1.12 2010/05/20 20:40:41 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import java.util.Iterator;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.deals.interbancario.CapturaDealInterbancario;

/**
 * El componente que contiene la informaci&oacute;n referente a una operaci&oacute;n Swap.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2010/05/20 20:40:41 $
 */
public class InfoSwap extends BaseComponent {

    /**
     * Constructor vac&iacute;o.
     */
    public InfoSwap() {
        super();
    }

    /**
	 * Obtiene el monto del Swap.
	 * 
	 * @param compra Define si la operaci&oacute;n es compra o venta.
	 * @param deal El deal Swap.
	 * @return double.
	 */
    public double getMonto(boolean compra, Deal deal) {
        double monto = 0.0;
        if (deal.isCompra() == compra) {
            for (Iterator it = (compra ? deal.getRecibimos() : deal.getEntregamos()).iterator();
                 it.hasNext(); ) {
                DealDetalle detalle = (DealDetalle) it.next();
                if (!detalle.isCancelado()) {
                    monto += detalle.getMonto();
                }
            }
        }
        return monto;
    }

    /**
     * Obtiene la divisa del deal.
     * 
     * @param deal El deal Swap. 
     * @return Divisa.
     */
    public Divisa getDivisa(Deal deal) {
        DealDetalle det = (DealDetalle) (deal.isCompra() ?
                deal.getRecibimos() : deal.getEntregamos()).get(0);
        return det.getDivisa();
    }

    /**
     * Activa la p&aacute;gina CapturaDealInterbancario y le pasa como par&aacute;metro el deal
     * seleccionado por el usuario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void verDeal(IRequestCycle cycle) {
        SicaPage currentPage = (SicaPage) getPage();
        CapturaDealInterbancario nextPage = (CapturaDealInterbancario) cycle.
                getPage("CapturaDealInterbancario");
        nextPage.setModoInsercion(false);
        Integer idDeal = (Integer) cycle.getServiceParameters()[0];
        nextPage.setDeal(currentPage.getSicaServiceData().findDeal(idDeal.intValue()));
        nextPage.activate(cycle);
    }

}

/*
 * $Id: DealsPendientesDia.java,v 1.1 2008/05/01 03:09:14 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.mesa;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.deals.interbancario.CapturaDealInterbancario;

/**
 * Al inicio de operaciones y durante el d&iacute;a, el promotor no puede saber cu&aacute;ntos 
 * Deals capturados se encuentran pendientes, es decir, que a&uacute;n no se encuentren 
 * Totalmente Liquidados, Cancelados o que estén en Alta, o tengan autorizaciones 
 * pendientes por parte de la Mesa, salvo por medio de la consulta general de Deals; 
 * si estas operaciones no son procesadas, no es posible que la mesa de cambios 
 * realice el cierre del d&iacute;a.  
 * 
 * @author Eduardo Sanchez
 * @version $Revision: 1.1 $ $Date: 2008/05/01 03:09:14 $
 */
public abstract class  DealsPendientesDia extends BaseComponent {
	
    /**
     * Activa la p&aacute;gina CapturaDealInterbancario y le pasa como par&aacute;metro el deal
     * seleccionado por el usuario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void verDealPendientesDia(IRequestCycle cycle) {
    	SicaPage page = (SicaPage) getPage();
        try {
            CapturaDealInterbancario nextPage = (CapturaDealInterbancario) cycle.
                    getPage("CapturaDealInterbancario");
            nextPage.setModoInsercion(false);     
            nextPage.setPaginaRetorno(DEALS_PENDIENTES_DIA);
            nextPage.setDeal(page.getSicaServiceData().findDeal(
                    ((Integer) cycle.getServiceParameters()[0]).intValue()));
            nextPage.activate(cycle);
        }
        catch (SicaException e) {
        	page.debug(e);            
        	page.getDelegate().record(e.getMessage(), null);
        }
    }	
    /**
     * Constante para identificar que vengo de la pa&acute;gina DealsPendientesDia   
     */
    private static final boolean DEALS_PENDIENTES_DIA = true;    
}

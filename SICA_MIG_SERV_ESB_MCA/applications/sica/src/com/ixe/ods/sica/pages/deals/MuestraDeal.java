/*
 * $Id: MuestraDeal.java,v 1.11 2008/10/27 23:20:10 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.deals;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.DealPosicion;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.deals.interbancario.DatosDealInterbancario;
import com.ixe.ods.sica.sdo.SicaServiceData;

/**
 * Permite visualizar un deal normal o interbancario desde el blotter del monitor de la
 * posici&oacute;n.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/10/27 23:20:10 $
 */
public class MuestraDeal extends SicaPage implements IExternalPage {

    /**
     * Constructor vac&iacute;o.
     */
    public MuestraDeal() {
        super();
    }

    /**
     * Activa la p&aacute;gina 'DatosDeal' o 'DatosDealInterbancario' con la informaci&oacute;n del
     * deal seleccionado.
     *
     * @param params Los par&aacute;metros de la petici&oacute;n; el cero corresponde al
     * idDealPosicion.
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activateExternalPage(Object[] params, IRequestCycle cycle) {
        try {
            activate(cycle);
            SicaServiceData sd = getSicaServiceData();
            DealDetalle dealPosicion = (DealDetalle) sd.find(DealDetalle.class,
                    (Integer) params[0]);
            if (DealPosicion.TIPO_DEAL_CLIENTE.equals(dealPosicion.getTipoDeal())) {
                DatosDeal nextPage = (DatosDeal) cycle.getPage("DatosDeal");
                nextPage.setDeal(sd.findDealDetalle(dealPosicion.getIdDealPosicion()).getDeal());
                nextPage.activate(cycle);
            }
            else if (DealPosicion.TIPO_DEAL_INTERBANCARIO.equals(dealPosicion.getTipoDeal())) {
                DatosDealInterbancario nextPage = (DatosDealInterbancario) cycle.
                        getPage("DatosDealInterbancario");
                nextPage.setDeal(sd.findDealDetalle(dealPosicion.getIdDealPosicion()).getDeal());
                nextPage.activate(cycle);
            }
        }
        catch (SicaException e) {
            throw new ApplicationRuntimeException(e);
        }
    }
}
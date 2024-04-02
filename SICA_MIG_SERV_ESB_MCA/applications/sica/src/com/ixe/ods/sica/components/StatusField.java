/*
 * $Id: StatusField.java,v 1.1 2008/10/27 23:20:10 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import java.util.HashMap;

import org.apache.tapestry.BaseComponent;

import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;

/**
 * Componente que despliega el status de un deal o un detalle de deal.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1 $ $Date: 2008/10/27 23:20:10 $
 */
public class StatusField extends BaseComponent {

    /**
     * Regresa la clave del status correcto, considerando el campo de reversado.
     *
     * @param det El detalle de deal.
     * @return String
     */
    public static String getStatusDealDetalle(DealDetalle det) {
        if (Deal.REVERSADO == det.getReversado()) {
            return "RE";
        }
        else if (Deal.PROC_REVERSO == det.getReversado()) {
            return "PR";
        }
        else {
            return det.getStatusDetalleDeal();
        }
    }

    /**
     * Regresa la clave del status correcto, considerando el campo de reversado.
     *
     * @param deal El deal.
     * @return String
     */
    public static String getStatusDeal(Deal deal) {
        if (Deal.REVERSADO == deal.getReversado()) {
            return "RE";
        }
        else if (Deal.PROC_REVERSO == deal.getReversado()) {
            return "PR";
        }
        else {
            return deal.getStatusDeal();
        }
    }

    /**
     * Regresa la clave del status correcto, considerando el campo de reversado.
     *
     * @param dealMap El deal.
     * @return String
     */
    public static String getStatusDeal(HashMap dealMap) {
        int reversado = ((Integer) dealMap.get("reversado")).intValue();
        if (Deal.REVERSADO == reversado) {
            return "RE";
        }
        else if (Deal.PROC_REVERSO == reversado) {
            return "PR";
        }
        else {
            return (String) dealMap.get("statusDeal");
        }
    }
}

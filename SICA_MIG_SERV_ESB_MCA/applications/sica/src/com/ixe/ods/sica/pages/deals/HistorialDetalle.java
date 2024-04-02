/*
 * $Id: HistorialDetalle.java,v 1.11 2008/02/22 18:25:42 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.deals;

import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.SicaPage;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;

/**
 * P&aacute;gina que muestra el detalle del historial de Deal.
 *
 * @author Edgar Leija
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:42 $
 */
public abstract class HistorialDetalle extends SicaPage implements IExternalPage {

    /**
     * Coordina todas las posibles opciones de operaci&oacute;n, como crear un Historial Deal
     * Detalle.
     *
     * @param params Los par&aacute;metros del IRequestCycle.
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activateExternalPage(Object[] params, IRequestCycle cycle) {
        activate(cycle);
        setDealDetalle((DealDetalle) getSicaServiceData().find(DealDetalle.class,
                (Integer) params[0]));
    }
    
    /**
     * Regresa por default el estado de operacion normal, restringida y vespertina. Las subclases
     * deben sobre escribir para indicar estados adicionales en los que &eacute;stas son
     * v&aacute;lidas.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[]{Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
                Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_FIN_LIQUIDACIONES,
                Estado.ESTADO_GENERACION_CONTABLE, Estado.ESTADO_OPERACION_NOCTURNA};
    }

    /**
     * Regresa el valor de DealDetalle
     *
     * @return  DealDetalle
     */
    public abstract DealDetalle getDealDetalle();
    
    /**
     * Fija el valor del detalle de  deal.
     *
     * @param dd El valor a asignar.
     */
    public abstract void setDealDetalle(DealDetalle dd);

}
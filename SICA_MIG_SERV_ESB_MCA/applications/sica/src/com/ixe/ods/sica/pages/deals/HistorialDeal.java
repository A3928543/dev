/*
 * $Id: HistorialDeal.java,v 1.11 2008/02/22 18:25:42 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.deals;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.SicaPage;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;

/**
 * P&aacute;gina que permite generar historiales para Deals.
 *
 * @author Edgar Leija
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:42 $
 */
public abstract class HistorialDeal extends SicaPage implements IExternalPage {
	
	/**
     * M&eacut;etodo que coordina todas las posibles opciones de operaci&oacute;n, como crear un Historial Deal.
     *
     * @param params Los par&aacute;metros del Request Cycle.
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activateExternalPage(Object[] params, IRequestCycle cycle) {
        activate(cycle);
        try {
            setDeal(getSicaServiceData().findDeal(((Integer) params[0]).intValue()));
        }
        catch (SicaException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
            throw new ApplicationRuntimeException(e);
        }
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
     * Regresa el valor de Deal.
     *
     * @return Deal.
     */
    public abstract Deal getDeal();
    
   /**
     * Fija el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public abstract void setDeal(Deal deal);

}

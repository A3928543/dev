/*
 * $Id: DatosDeal.java,v 1.13.86.1.6.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * Despliega los datos de un deal.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.13.86.1.6.1 $ $Date: 2020/12/01 04:53:01 $
 */
public abstract class DatosDeal extends SicaPage implements IExternalPage {

    /**
     * P&aacute;gina para desplegar la informaci&oacute;n completa de un deal.
     *
     * @param params El arreglo de par&aacute;metros. Se espera en el 0 el n&uacute;mero de deal.
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activateExternalPage(Object[] params, IRequestCycle cycle) {
        activate(cycle);
        try {
            Integer idDeal = (Integer) params[0];
            auditar(idDeal, LogAuditoria.CONSULTA_DATOS_DEAL, null, "INFO", "E");
            setDeal(getSicaServiceData().findDeal(idDeal.intValue()));
        }
        catch (SicaException e) {
            throw new ApplicationRuntimeException(e.getMessage());
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
                Estado.ESTADO_GENERACION_CONTABLE, Estado.ESTADO_OPERACION_NOCTURNA,
                Estado.ESTADO_INICIO_DIA};
    }
    

    /**
     * Establece el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public abstract void setDeal(Deal deal);
    
    /**
     * Obtiene el Deal asociado
     * @return Deal
     */
    public abstract Deal getDeal();
    
    
}

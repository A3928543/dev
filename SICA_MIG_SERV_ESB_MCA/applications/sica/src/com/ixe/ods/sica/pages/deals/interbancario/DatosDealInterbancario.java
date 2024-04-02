/*
 * $Id: DatosDealInterbancario.java,v 1.12.92.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.interbancario;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * P&aacute;gina que despliega la informaci&oacute;n de un deal interbancario.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12.92.1 $ $Date: 2020/12/01 04:53:01 $
 */
public abstract class DatosDealInterbancario extends SicaPage implements IExternalPage {

    /**
     * Inicializa el deal a desplegar. El primer par&aacute;metro debe ser el n&uacute;mero de deal.
     *
     * @param params El arreglo de par&aacute;metros.
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
     * Establece el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public abstract void setDeal(Deal deal);
}

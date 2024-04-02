/*
 * $Id: PizarronExt.java,v 1.11 2008/02/22 18:25:10 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;

/**
 * P&aacute;gina utilizada por el monitor de la posici&oacute;n para desplegar el applet del
 * pizarr&oacute;n.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.11 $ $Date: 2008/02/22 18:25:10 $
 */
public abstract class PizarronExt extends SicaPage implements IExternalPage {

    /**
     * Asigna el par&aacute;metro cero al identificador de la mesa de cambio, y el par&aacute;metro
     * 1 al identificador del canal.
     *
     * @param params El arreglo de par&aacute;metros.
     * @param cycle El IRequestCycle.
     */
    public void activateExternalPage(Object[] params, IRequestCycle cycle) {
        setIdMesaCambio(((Integer) params[0]).intValue());
        setIdTipoPizarron(Integer.parseInt(params[1].toString()));
    }

    /**
     * Establece el valor de idMesaCambio.
     *
     * @param idMesaCambio El valor a asignar.
     */
    public abstract void setIdMesaCambio(int idMesaCambio);

    /**
     * Establece el valor de idTipoCambio.
     *
     * @param idTipoPizarron El valor para el idTipoCambio
     */
    public abstract void setIdTipoPizarron(int idTipoPizarron);
}

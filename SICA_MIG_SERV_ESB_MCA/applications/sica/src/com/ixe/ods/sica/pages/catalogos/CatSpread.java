/*
 * $Id: CatSpread.java,v 1.10 2008/02/22 18:25:39 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.catalogos;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

import com.ixe.ods.sica.model.Spread;

/**
 * Permite al usuario capturar nuevos spreads.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:39 $
 */
public abstract class CatSpread extends AbstractCatEdicion {

    /**
     * Valida que el spread capturado no est&eacute; activo y actualiza el registro en la base de
     * datos.
     *
     * @see #existeCombinacion().
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void actualizar(IRequestCycle cycle) {
        IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
        if (delegate != null && delegate.getHasErrors()) {
            return;
        }
        if (isModoUpdate()) {
            getSicaServiceData().update(getRegistro());
        }
        else {
            if (!existeCombinacion()) {
                getSicaServiceData().storeSpread((Spread) getRegistro());
            }
            else if (delegate != null) {
                delegate.record("Ya est\u00e1 asignado el producto seleccionado.", null);
                return;
            }
        }
        cycle.activate(getNombrePaginaConsulta());
    }

    /**
     * Regresa true si existe un spread actual con la misma mesa, canal, divisa y
     * claveFormaLiquidaci&oacute;n.
     *
     * @return boolean.
     */
    private boolean existeCombinacion() {
        Spread r = (Spread) getRegistro();
        return getSicaServiceData().findSpreadsActualesByTipoPizarronDivisaFormaLiquidacion(
            r.getDivisa().getIdDivisa(),
            r.getClaveFormaLiquidacion(), r.getTipoPizarron()).size() != 0;
    }
}
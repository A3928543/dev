/*
 * $Id: CapturaEfectivo.java,v 1.10 2008/02/22 18:25:00 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.deals.formliq;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * Subclase de AbstractCapturaLiq que permite capturar una denominaci&oacute;n preferida por el usuario.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:00 $
 */
public abstract class CapturaEfectivo extends AbstractCapturaLiq {

    /**
     * Valida la captura y actualiza el registro, redirige a la p&aacute;gina 'CapturaDeal'.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
        IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
        if (delegate.getHasErrors()) {
            return;
        }
        getSicaServiceData().update(getDealDetalle());
        cycle.activate(cycle.getPage("CapturaDeal"));
    }
}

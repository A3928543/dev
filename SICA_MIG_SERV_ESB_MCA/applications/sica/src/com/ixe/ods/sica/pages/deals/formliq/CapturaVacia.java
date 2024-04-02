/*
 * $Id: CapturaVacia.java,v 1.10 2008/02/22 18:25:00 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.deals.formliq;

import org.apache.tapestry.IRequestCycle;

/**
 * Subclase de AbstractCapturaLiq. Muestra solamente la informaci&oacute;n del detalle de deal, sin permitir realizar
 * modificaciones.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:00 $
 */
public abstract class CapturaVacia extends AbstractCapturaLiq {

    /**
     * Activa la p&aacute;gina 'CapturaDeal'.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
        cycle.activate(cycle.getPage("CapturaDeal"));
    }
}

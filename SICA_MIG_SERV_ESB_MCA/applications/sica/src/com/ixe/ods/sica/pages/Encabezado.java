/*
 * $Id: Encabezado.java,v 1.11 2008/02/22 18:25:10 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages;

import com.ixe.ods.sica.SicaException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;

/**
 * P&aacute;gina que muestra el logo de ixe, y la informaci&oacute;n del usuario conectado a la aplicaci&oacute;n.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:10 $
 */
public class Encabezado extends SicaPage implements PageRenderListener {

    /**
     * Refresca el estado actual del sistema.
     *
     * @param pageEvent El evento de Tapestry de BeginRender.
     */
    public void pageBeginRender(PageEvent pageEvent) {
        try {
            refrescarEstadoActual();
        }
        catch (SicaException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
    }
}

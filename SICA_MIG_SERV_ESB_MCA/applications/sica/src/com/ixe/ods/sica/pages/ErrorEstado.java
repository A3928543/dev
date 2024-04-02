/*
 * $Id: ErrorEstado.java,v 1.1 2008/06/05 01:15:14 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;

/**
 * P&aacute;gina que permite desplegar un mensaje al usuario cuando la p&aacute;gina no esta
 * disponible para el horario actual.
 *
 * @author  Gustavo Gonzalez (ggonzalez)
 * @version $Revision: 1.1 $ $Date: 2008/06/05 01:15:14 $
 */
public abstract class ErrorEstado extends Mensaje implements PageRenderListener {

	/**
     * Llamado cuando la p&aacute;gina efect&uacute;a el "render" de la misma.
     *
     * @param event el evento de la p&aacute;gina.
     */
    public void pageBeginRender(PageEvent event) {
    	refrescarEstadoActual();
    	setMensaje("Esta funcionalidad no est&aacute; disponible cuando el sistema se " +
                "encuentra en estado de " + getEstadoActual().getNombreEstado());
    } 
}

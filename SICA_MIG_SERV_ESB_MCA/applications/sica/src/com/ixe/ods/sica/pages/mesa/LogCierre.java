/*
 * $Id: LogCierre.java,v 1.11 2008/02/22 18:25:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.mesa;

import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.deals.CapturaDeal;

import org.apache.tapestry.IRequestCycle;

/**
 * P&aacute;gina que muestra el Log del Cierre del d&iacute;a
 *
 * @author Edgar Leija
 * @version  $Revision: 1.11 $ $Date: 2008/02/22 18:25:32 $
 */
public abstract class LogCierre extends SicaPage {
    
	/**
     * Coordina el despliegue de los diferentes estados posibles del sistema.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
    }
    
	/**
	 * Activa el submit de la forma.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void formSubmit(IRequestCycle cycle) {
		ControlHorarios nextPage = (ControlHorarios) cycle.getPage("ControlHorarios");
        nextPage.activate(cycle);
    }
    
    /**
    * Fija el valor del log.
     *
     * @param sb El valor a asignar.
     */
    public abstract void setLog(StringBuffer sb);

    /**
     * Regresa el string del log.
     *
     * @return StringBuffer
     */
    public abstract StringBuffer getLog();
}


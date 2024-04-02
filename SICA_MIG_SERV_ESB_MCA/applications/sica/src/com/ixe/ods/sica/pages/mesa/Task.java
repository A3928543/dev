/*
* $Id: Task.java,v 1.10 2008/02/22 18:25:32 ccovian Exp $
* Ixe, Sep 13, 2003
* Copyright (C) 2001-2004 Grupo Financiero Ixe
*/

package com.ixe.ods.sica.pages.mesa;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.sdo.SicaServiceData;

import java.util.TimerTask;

/**
 * Clase que extiende de <code>TimerTask</code> que permite modificar el 
 * estado del sistema.
 * 
 * @author  Edgar Leija
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:32 $
 */
public class Task extends TimerTask {
	
	/**
	 * Constructor de la clase.
	 */
	public Task() {
		super();
	}

	/**
	 * Constructor de la clase. Asigna el valor del servicio sicaServiceData.
	 * 
	 * @param sicaServiceData El servicio sicaServiceData
	 */
	public Task(SicaServiceData sicaServiceData) {
		this();
		this.sicaServiceData = sicaServiceData;
	}

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	public void run() {
        try {
            sicaServiceData.cambiarEstadosSistema();
        }
        catch (SicaException e) {
             
        }
    }
	
	/**
	 * Referencia al servicio sicaServiceData.
	 */
	private SicaServiceData sicaServiceData;
}

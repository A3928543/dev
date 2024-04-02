/*
* $Id: HorarioTimer.java,v 1.10 2008/02/22 18:25:32 ccovian Exp $
* Ixe, Sep 13, 2003
* Copyright (C) 2001-2004 Grupo Financiero Ixe
*/

package com.ixe.ods.sica.pages.mesa;

import com.ixe.ods.sica.sdo.SicaServiceData;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase que verifica el horario en el que se encuentra sistema. 
 * Se ejecuta cada 2 minutos para definir cuando hay que cambiar
 * el horario de operaci&oacute;n.
 *
 * @author  Edgar Leija
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:32 $
 */
public class HorarioTimer {

	/**
	 * Constructor de la clase.
	 */
	public HorarioTimer() {
		super();
	}

	/**
	 * Asigna el valor para sicaServiceData.
	 * 
	 * @param sicaServiceData El servicio sicaServiceData.
	 */
	public void setSicaServiceData(SicaServiceData sicaServiceData) {
		this.sicaServiceData = sicaServiceData;
		Timer  timer = new Timer();
		TimerTask task = new Task(sicaServiceData);
		timer.schedule( task, 5000, 150000 );
	}
	
	/**
	 * Referencia a sicaServiceData
	 */
	private SicaServiceData sicaServiceData;
}

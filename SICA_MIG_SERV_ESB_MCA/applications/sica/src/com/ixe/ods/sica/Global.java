/*
 * $Id: Global.java,v 1.12 2008/02/22 18:25:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */
package com.ixe.ods.sica;

import com.ixe.ods.seguridad.sdo.SeguridadServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.tapestry.BaseGlobal;

/**
 * Clase que contiene par&aacute;metros e instancias Globales del Sistema. Define los m&eacute;todos de conveniencia
 * getSicaServiceData() y getSeguridadServiceData().
 *
 * @author Jean C. Favila, Javier Cordova (jcordova)
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:33 $
 */
public class Global extends BaseGlobal {

    /**
     * Obtiene una referencia al Bean de Servicios del SICA.
     *
     * @return SicaServiceData El Bean de Servicios del SICA.
     */
	public SicaServiceData getSicaServiceData() {
        return (SicaServiceData) getApplicationContext().getBean("sicaServiceData");
    }

	/**
     * Obtiene una referencia al Bean de Servicios de Seguridad.
     *
     * @return SeguridadServiceData El Bean de Servicios de Seguridad.
     */
	public SeguridadServiceData getSeguridadServiceData() {
        return (SeguridadServiceData) getApplicationContext().getBean("securityServiceData");
    }
}
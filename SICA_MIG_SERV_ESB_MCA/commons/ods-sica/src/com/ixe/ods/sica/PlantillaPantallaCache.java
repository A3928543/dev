/*
 * $Id: PlantillaPantallaCache.java,v 1.12 2008/02/22 18:25:02 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.ods.sica.model.PlantillaPantalla;
import com.ixe.ods.sica.sdo.SicaServiceData;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Bean que act&uacute;a como Cach&eacute; para los registros de la tabla SC_PLANTILLA_PANTALLA. El
 * Cach&eacute; se refresca cada 60 segundos.
 * 
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:02 $
 */
public class PlantillaPantallaCache implements Serializable {

    /**
     * Constructor vac&iacute;o.
     */
    public PlantillaPantallaCache() {
        super();
    }

    /**
     * Regresa la plantilla pantalla que tenga el mnem&oacute;nico especificado, tom&aacute;ndola
     * del Cach&eacute;.
     *
     * @param mnemonico El mnem&oacute;nico a buscar.
     * @return PlantillaPantalla.
     */
    public synchronized PlantillaPantalla getPlantillaPantalla(String mnemonico) {
        java.util.Date fechaActual = new java.util.Date();
        if (fechaUltimaActualizacion != null && fechaActual.getTime()
                - fechaUltimaActualizacion.getTime() > MILISEGUNDOS_CADUCIDAD) {
            limpiar();
        }
        if (plantillasPantalla == null) {
            plantillasPantalla = sicaServiceData.findAll(PlantillaPantalla.class);
            fechaUltimaActualizacion = new java.util.Date();
        }
        for (Iterator it = plantillasPantalla.iterator(); it.hasNext();) {
            PlantillaPantalla plantillaPantalla = (PlantillaPantalla) it.next();
            if (plantillaPantalla.getMnemonico().equals(mnemonico)) {
                return plantillaPantalla;
            }
        }
        return null;
    }

    /**
     * Fija el valor de sicaServiceData.
     * 
     * @param sicaServiceData El valor a asignar.
     */
    public void setSicaServiceData(SicaServiceData sicaServiceData) {
        this.sicaServiceData = sicaServiceData;
    }

    /**
     * Vuelve null la lista de plantillasPantalla para que se refresque en la siguiente
     * petici&oacute;n.
     */
    public void limpiar() {
        plantillasPantalla = null;
    }

    /**
     * El cach&eacute; de objetos PlantillasPantalla.
     */
    private List plantillasPantalla;

    /**
     * La referencia al SicaServiceData para realizar las operaciones a la base de datos.
     */
    private SicaServiceData sicaServiceData;

    /**
     * La fecha y hora de la &uacute;ltima actualizaci&oacute;n.
     */
    private java.util.Date fechaUltimaActualizacion;

    /**
     * El n&uacute;mero en milisecundos de la vigencia del cach&eacute;.
     */
    private static final long MILISEGUNDOS_CADUCIDAD = 300000;

    /**
     * El UID de serializaci&oacute;n.
     */
    private static final long serialVersionUID = 6305791427328804167L;
}

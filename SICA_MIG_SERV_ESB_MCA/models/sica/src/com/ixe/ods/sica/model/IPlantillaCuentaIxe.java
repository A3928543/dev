/*
 * $Id: IPlantillaCuentaIxe.java,v 1.12 2008/02/22 18:25:21 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

/**
 * Interfaz para la clase persistente PlantillaCuentaIxe.
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:21 $
 */
public interface IPlantillaCuentaIxe extends IPlantilla {

    /**
     * Regresa el valor de noCuentaIxe.
     *
     * @return String.
     */
    String getNoCuentaIxe();

    /**
     * Fija el valor de noCuentaIxe.
     *
     * @param noCuentaIxe El valor a asignar.
     */
    void setNoCuentaIxe(String noCuentaIxe);

}

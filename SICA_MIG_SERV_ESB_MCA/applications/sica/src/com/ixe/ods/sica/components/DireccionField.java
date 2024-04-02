/*
 * $Id: DireccionField.java,v 1.2 2009/08/03 21:53:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import org.apache.tapestry.BaseComponent;

import com.ixe.ods.bup.model.Direccion;

/**
 * Componente para desplegar los datos de una direcci&oacute;n de la bup.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2009/08/03 21:53:33 $
 */
public abstract class DireccionField extends BaseComponent {

    /**
     * Regresa una cadena con los datos de la direcci&oacute;n.
     *
     * @return String.
     */
    public String getDireccionCompleta() {
        StringBuffer sb = new StringBuffer("");
        Direccion dir = getDireccion();
        if (dir != null) {
            if (dir.getCalleYNumero() != null) {
                sb = sb.append(dir.getCalleYNumero()).append(" ");
            }
            if (dir.getNumExterior() != null) {
                sb = sb.append(dir.getNumExterior()).append(" ");
            }
            if (dir.getNumInterior() != null) {
                sb = sb.append(dir.getNumInterior()).append(" ");
            }
            if (dir.getColonia() != null) {
                sb = sb.append("Col.: ").append(dir.getColonia()).append(" ");
            }
            if (dir.getDelegacionMunicipio() != null) {
                sb = sb.append("<br />").append(dir.getDelegacionMunicipio()).append(" ");
            }
            if (dir.getCiudad() != null) {
                sb = sb.append(dir.getCiudad()).append(" ");
            }
            if (dir.getIdEntFederativa() != null) {
                sb = sb.append("<br />").append(dir.getIdEntFederativa()).append(" ");
            }
            if (dir.getCodigoPostal() != null) {
                sb = sb.append("C.P.: ").append(dir.getCodigoPostal());
            }
        }
        return sb.toString();
    }

    /**
     * Regresa el valor de la direcci&oacute;n.
     *
     * @return Direccion.
     */
    public abstract Direccion getDireccion();
}

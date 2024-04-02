/*
 * $Id: BupCodigoPostal.java,v 1.1.2.1 2010/10/08 01:30:18 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase persistente para la tabla BUP_CODIGO_POSTAL.
 *
 * Revisar BupCodigoPostal.hbm.xml que contiene el mapeo de hibernate.
 *
 * @author lvillegas
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:30:18 $
 */
public class BupCodigoPostal implements Serializable {
    /**
     * Constructor vac&iacute;o.
     */
    public BupCodigoPostal() {
        super();
    }

    /**
     * Regresa el valor de id.
     * 
     * @return BupCodigoPostalPK
     */
    public BupCodigoPostalPK getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(BupCodigoPostalPK id) {
        this.id = id;
    }
    
    /**
     * Regresa el valor de idColonia.
     *
     * @return String.
     */
    public String getColonia() {
        return colonia;
    }
    
    /**
     * Fija el valor de colonia.
     * 
     * @param colonia El valor a asignar.
     */
    public void setColonia(String colonia) {
        this.colonia = colonia;
    }
    
	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof BupCodigoPostal)) {
            return false;
        }
        BupCodigoPostal castOther = (BupCodigoPostal) other;
        return getId().equals(castOther.getId());
    }

    /**
     * Regresa el hashCode.
     *
     * @return <code>int</code>
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Regresa una cadena con la descripci&oacute;n de los campos de esta instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("colonia", colonia)
                .toString();
    }
    
    /**
     * Id del c&oacute;digo postal.
     */
    private BupCodigoPostalPK id;
    
    /**
     * Nombre de la colonia perteneciente al c&oacute;digo postal.
     */
    private String colonia;
    
    /**
     * Serializador.
     */
    private static final long serialVersionUID = 3967887960822630780L;
}

/*
 * $Id: MaximoDealPK.java,v 1.12 2008/02/22 18:25:22 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Clase que representa la llave primaria de la tabla SC_MAXIMO_DEAL.
 *
 * @author Gerardo
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:22 $
 */
public class MaximoDealPK implements Serializable {

    /**
     * Regresa el valor de idDivisa.
     *
     * @hibernate.property column="ID_DIVISA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getIdDivisa() {
        return idDivisa;
    }

    /**
     * Fija el valor de idDivisa.
     *
     * @param idDivisa El valor a asignar.
     */
    public void setIdDivisa(String idDivisa) {
        this.idDivisa = idDivisa;
    }

    /**
     * Regresa el valor de idMesaCambio.
     *
     * @hibernate.property column="ID_MESA_CAMBIO"
     * not-null="true"
     * unique="false"
     * @return Integer.
     */
    public Integer getIdMesaCambio() {
        return idMesaCambio;
    }

    /**
     * Fija el valor de idMesaCambio.
     *
     * @param idMesaCambio El valor a asignar.
     */
    public void setIdMesaCambio(Integer idMesaCambio) {
        this.idMesaCambio = idMesaCambio;
    }

    /**
     * Implements a standard way to compare instances
     *
     * @param other
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (!(other instanceof MaximoDealPK)) {
            return false;
        }
        MaximoDealPK object = (MaximoDealPK) other;
        return new EqualsBuilder()
                .append(this.getIdDivisa(), object.getIdDivisa())
                .append(this.getIdMesaCambio(), object.getIdMesaCambio())
                .isEquals();
    }

	/**
	 * @return int
	 */
	public int hashCode() {
		return new HashCodeBuilder().append(getIdDivisa()).append(getIdMesaCambio()).toHashCode();
	}

    /**
     * El identificador del registro.
     */
    private String idDivisa;

    /**
     * El identificador del registro.
     */
    private Integer idMesaCambio;
}

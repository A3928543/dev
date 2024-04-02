/*
 * $Id: MaximoDeal.java,v 1.12 2008/02/22 18:25:22 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Clase persistente para la tabla SC_MAXIMO_DEAL.
 *
 * @hibernate.class table="SC_MAXIMO_DEAL"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.MaximoDeal"
 * dynamic-update="true"
 *
 * @hibernate.query name="findMaximoDealByIdMesaCambio"
 * query="FROM MaximoDeal as md WHERE md.id.idMesaCambio = ?"
 * 
 * @hibernate.query name="findMaximoDealByIdMesaCambioAndIdDivisa"
 * query="FROM MaximoDeal as md WHERE md.id.idMesaCambio = ? AND md.id.idDivisa = ?"
 * 
 * @author Gerardo
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:22 $
 */
public class MaximoDeal implements Serializable {

	/**
	 * Returns the id.
	 *
	 * @return MaximoDealPK.
	 * @hibernate.id
	 */
	public MaximoDealPK getId() {
		return _id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id The id to set.
	 */
	public void setId(MaximoDealPK id) {
		_id = id;
	}

	/**
	 * Regresa el valor de montoMaximo.
	 *
	 * @hibernate.property column="MONTO_MAXIMO"
	 * not-null="true"
	 * unique="false"
	 * @return double.
	 */
	public double getMontoMaximo() {
		return montoMaximo;
	}

    /**
     * Implements a standard way to compare instances
     *
     * @param other Otro objeto
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (!(other instanceof MaximoDeal)) {
            return false;
        }
        MaximoDeal object = (MaximoDeal) other;
        return new EqualsBuilder()
                .append(this.getId(), object.getId())
                .isEquals();
    }

    /**
     * Regresa el hashcode de la instanacia utilizando HashCodeBuilder para crearlo.
     *
     * @return int.
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

	/**
	 * Fija el valor de montoMaximo.
	 *
	 * @param montoMaximo .
	 */
	public void setMontoMaximo(double montoMaximo) {
		this.montoMaximo = montoMaximo;
	}
	
	/**
	 * Variable para el monto m&aacute;ximo del deal.
	 */
	private double montoMaximo;

	/**
	 * Variable de la llave primaria de la tabla del monto m&aacute;ximo de deal.
	 */
	private MaximoDealPK _id;
}

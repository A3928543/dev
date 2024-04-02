/*
 * $Id: TipoPizarron.java,v 1.2 2008/04/16 18:21:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_TIPO_PIZARRON. Define el tipo de pizarron para determinado
 * canal de operacion.
 *
 * @hibernate.class table="SC_TIPO_PIZARRON"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.TipoPizarron"
 * dynamic-update="true"
 *
 * @author Gustavo Gonzalez.
 * @version $Revision: 1.2 $ $Date: 2008/04/16 18:21:32 $
 *
 */
public class TipoPizarron implements Serializable {

	/**
	 *
	 * Constructor por default.
	 */
	public TipoPizarron() {
		super();
	}

	/**
     * Regresa el valor de descripcion.
     *
     * @return String.
     * @hibernate.property column="DESCRIPCION"
     * not-null="true"
     * unique="false"
     */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Asigna el valor para descripcion.
	 *
	 * @param descripcion La descripcion del Tipo de Pizarron.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
     * Regresa el valor de idDeal.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_TIPO_PIZARRON"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_TIPO_PIZARRON_SEQ"
     * @return int.
     */
	public Integer getIdTipoPizarron() {
		return idTipoPizarron;
	}

	/**
	 * Asigna el valor para el id del Tipo de Pizarron.
	 * @param idTipoPizarron El id del tipo de Pizarron
	 */
	public void setIdTipoPizarron(Integer idTipoPizarron) {
		this.idTipoPizarron = idTipoPizarron;
	}

	/**
     * Obtiene si la divisa divide o no
     *
     * @return boolean
     * @hibernate.property column="GENERA_PIZARRON"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * update="true"
     * insert="true"
     * unique="false"
     */
	public boolean isGeneraPizarron() {
		return generaPizarron;
	}

	/**
	 * Asigna el valor para generaPizaarron
	 *
	 * @param generaPizarron El valor para generaPizarron.
	 */
	public void setGeneraPizarron(boolean generaPizarron) {
		this.generaPizarron = generaPizarron;
	}

	 /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof TipoPizarron)) {
            return false;
        }
        TipoPizarron castOther = (TipoPizarron) other;
        return new EqualsBuilder().append(this.getIdTipoPizarron(),
        		castOther.getIdTipoPizarron()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdTipoPizarron().intValue()).toHashCode();
    }

	/**
	 * El id del Tipo de Pizarron
	 */
	private Integer idTipoPizarron = new Integer(0);

	/**
	 * La descripcion para el pizarron.
	 */
	private String descripcion;

	 /**
     * Identificador que determina si divide o no la divisa.
     */
    private boolean generaPizarron;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 6228140365112482878L;
}

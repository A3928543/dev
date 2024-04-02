/*
 * $Id: BupMunicipio.java,v 1.1.2.1 2010/10/08 01:30:18 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase persistente para la tabla BUP_MUNICIPIO.
 *
 * @hibernate.class table="BUP_MUNICIPIO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.BupMunicipio"
 * dynamic-update="true"
 *
 * @author lvillegas
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:30:18 $
 */
public class BupMunicipio implements Serializable {
    /**
     * Constructor vac&iacute;o.
     */
    public BupMunicipio() {
        super();
    }
    
    /**
     * Regresa el valor de id.
     *
     * @hibernate.id
     * @return BupMunicipioPK.
     */
    public BupMunicipioPK getId() {
		return id;
	}

    /**
     * Fija el valor de id.
     *
     * @param id El valor a asignar.
     */
	public void setId(BupMunicipioPK id) {
		this.id = id;
	}

    /**
     * Regresa el valor de nombre.
     *
     * @hibernate.property column="NOMBRE_MUNICIPIO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Fija el valor de nombre.
     * 
     * @param nombre El valor a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof BupMunicipio)) {
            return false;
        }
        BupMunicipio castOther = (BupMunicipio) other;
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
                .append("nombre", nombre)
                .toString();
    }

	/**
     * Id del objeto.
     */
    private BupMunicipioPK id;
    
    /**
     * Nombre del municipio.
     */
    private String nombre;
    
    /**
     * Serializador.
     */
    private static final long serialVersionUID = 3967887960822630780L;
}

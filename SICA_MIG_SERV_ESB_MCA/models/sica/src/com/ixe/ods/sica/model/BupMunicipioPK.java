/*
 * $Id: BupMunicipioPK.java,v 1.1.2.1 2010/10/08 01:30:18 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase persistente para la tabla BUP_MUNICIPIO.
 *
 * @author lvillegas
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:30:18 $
 */
public class BupMunicipioPK implements Serializable {
    /**
     * Constructor vac&iacute;o.
     */
    public BupMunicipioPK() {
        super();
    }
    
    /**
     * Regresa el valor de idMunicipio.
     *
     * @return String.
     * @hibernate.property column="ID_MUNICIPIO"
     * not-null="true"
     * unique="false"
     */
    public Integer getIdMunicipio() {
		return idMunicipio;
	}

    /**
     * Fija el valor de idMunicipio.
     *
     * @param idMunicipio El valor a asignar.
     */
	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
    
    /**
     * Regresa el valor del estado.
     *
     * @hibernate.many-to-one column="ID_ENTIDAD_FEDERATIVA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.BupEstado"
     * outer-join="auto"
     * unique="false"
     * @return String.
     */
    public BupEstado getEstado() {
        return estado;
    }

    /**
     * Fija el valor de estado.
     * 
     * @param estado El valor a asignar.
     */
    public void setEstado(BupEstado estado) {
        this.estado = estado;
    }

	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof BupMunicipioPK)) {
            return false;
        }
        BupMunicipioPK castOther = (BupMunicipioPK) other;
        return getIdMunicipio().equals(castOther.getIdMunicipio()) &&
            getEstado().equals(castOther.getEstado());
    }

    /**
     * Regresa el hashCode.
     *
     * @return <code>int</code>
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getIdMunicipio().hashCode();
    }

    /**
     * Regresa una cadena con la descripci&oacute;n de los campos de esta instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("idMunicipio", idMunicipio)
                .append("estado", estado)
                .toString();
    }

	/**
     * Id del objeto.
     */
    private Integer idMunicipio;
    
    /**
     * Estado perteneciente al municipio.
     */
    private BupEstado estado;
    
    /**
     * Serializador.
     */
    private static final long serialVersionUID = 3967887960822630780L;
}

/*
 * $Id: BupEstado.java,v 1.1.2.1 2010/10/08 01:30:18 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase persistente para la tabla BUP_ENTIDAD_FEDERATIVA.
 *
 * @hibernate.class table="BUP_ENTIDAD_FEDERATIVA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.BupEstado"
 * dynamic-update="true"
 *
 * @author lvillegas
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:30:18 $
 */
public class BupEstado implements Serializable {
    /**
     * Constructor vac&iacute;o.
     */
    public BupEstado() {
        super();
    }
    
    /**
     * Regresa el valor de idEstado.
     *
     * @return String.
     * @hibernate.id generator-class="assigned"
     * column="ID_ENTIDAD_FEDERATIVA"
     * unsaved-value="null"
     */
    public Integer getIdEstado() {
		return idEstado;
	}

    /**
     * Fija el valor de idEstado.
     *
     * @param idEstado El valor a asignar.
     */
	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}
    
    /**
     * Regresa el valor de abreEstado.
     *
     * @hibernate.property column="ABRE_ENTIDAD_FEDERATIVA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getAbreEstado() {
        return abreEstado;
    }

    /**
     * Fija el valor de abreEstado.
     * 
     * @param abreEstado El valor a asignar.
     */
    public void setAbreEstado(String abreEstado) {
        this.abreEstado = abreEstado;
    }

    /**
     * Regresa el valor de nombreEstado.
     *
     * @hibernate.property column="NOMBRE_ENT_FEDERATIVA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getNombreEstado() {
        return nombreEstado;
    }
    
    /**
     * Fija el valor de nombreMnombreEstadounicipio.
     * 
     * @param nombreEstado El valor a asignar.
     */
    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }
    
	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof BupEstado)) {
            return false;
        }
        BupEstado castOther = (BupEstado) other;
        return getIdEstado().equals(castOther.getIdEstado());
    }

    /**
     * Regresa el hashCode.
     *
     * @return <code>int</code>
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getIdEstado().hashCode();
    }

    /**
     * Regresa una cadena con la descripci&oacute;n de los campos de esta instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("idEstado", idEstado)
                .append("abreEstado", abreEstado)
                .append("nombreEstado", nombreEstado)
                .toString();
    }

	/**
     * Id del objeto.
     */
    private Integer idEstado;
    
    /**
     * C&oacute;digo del estado.
     */
    private String abreEstado;
    
    /**
     * Nombre del estado.
     */
    private String nombreEstado;
    
    /**
     * Serializador.
     */
    private static final long serialVersionUID = 3967887960822630780L;
}

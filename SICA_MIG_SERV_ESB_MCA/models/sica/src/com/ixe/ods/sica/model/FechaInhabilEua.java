/*
 * $Id: FechaInhabilEua.java,v 1.2 2009/11/17 22:36:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla <code>SC_FECHA_INHABIL_EUA</code>.
 * 
 * @hibernate.class table="SC_FECHA_INHABIL_EUA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.FechaInhabilEua"
 * dynamic-update="true"
 * 
 * @author Israel Rebollar
 * @version $Revision: 1.2 $ $Date: 2009/11/17 22:36:23 $
 */
public class FechaInhabilEua implements Serializable {

    /**
	 * Constructor vac&iacute;o. No hace nada.
	 */
	public FechaInhabilEua() {
		super();
	}

    /**
     * Regresa el valor de idFechaInhabilEua.
     *
     * @hibernate.id
     * @return FechaInhabilEUAPK.
     */
    public FechaInhabilEuaPK getIdFechaInhabilEua() {
        return idFechaInhabilEua;
    }

    /**
     * Establece el valor de idFechaInhabilEua.
     *
     * @param idFechaInhabilEua El valor a asignar.
     */
    public void setIdFechaInhabilEua(FechaInhabilEuaPK idFechaInhabilEua) {
        this.idFechaInhabilEua = idFechaInhabilEua;
    }

    /**
     * Regresa el valor de status.
     *
     * @hibernate.property column="STATUS"
     * not-null="true"
     * unique="false"
     * @return String.
     */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
     * Regresa el valor de ultimaModificacion.
     *
     * @hibernate.property column="FECHA_ULT_MOD"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
	public Date getFechaUltMod() {
		return fechaUltMod;
	}

	/**
	 * @param fechaUltMod the fechaUltMod to set
	 */
	public void setFechaUltMod(Date fechaUltMod) {
		this.fechaUltMod = fechaUltMod;
	}
	
	/**
     * Regresa el valor de status.
     *
     * @hibernate.property column="USUARIO_ULT_MOD"
     * not-null="true"
     * unique="false"
     * @return String.
     */
	public String getClaveUsuarioModificacion() {
		return claveUsuarioModificacion;
	}

	/**
	 * @param claveUsuarioModificacion the claveUsuarioModificacion to set
	 */
	public void setClaveUsuarioModificacion(String claveUsuarioModificacion) {
		this.claveUsuarioModificacion = claveUsuarioModificacion;
	}

	/**
     * Regresa el valor de status.
     *
     * @hibernate.property column="USUARIO_ALTA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
	public String getClaveUsuarioCaptura() {
		return claveUsuarioCaptura;
	}

	/**
	 * @param claveUsuarioCaptura the claveUsuarioCaptura to set
	 */
	public void setClaveUsuarioCaptura(String claveUsuarioCaptura) {
		this.claveUsuarioCaptura = claveUsuarioCaptura;
	}

	/**
     * Regresa el valor de status.
     *
     * @hibernate.property column="FECHA_ALTA"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
	public Date getFechaCaptura() {
		return fechaCaptura;
	}

	/**
	 * @param fechaCaptura the fechaCaptura to set
	 */
	public void setFechaCaptura(Date fechaCaptura) {
		this.fechaCaptura = fechaCaptura;
	}

	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof FechaInhabilEua)) {
            return false;
        }
        FechaInhabilEua object = (FechaInhabilEua) other;
        return new EqualsBuilder().append(this.idFechaInhabilEua,
                object.getIdFechaInhabilEua()).isEquals();
    }
    
    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdFechaInhabilEua()).toHashCode();
    }
    
    /**
     * La fecha inh&aacute;bil. 
     */
    private FechaInhabilEuaPK idFechaInhabilEua = new FechaInhabilEuaPK();
    
    /**
     * Indica el usuario que creo/modifico el registro. 
     */
    private String claveUsuarioModificacion;
    
	/**
	 * Estatus de la fecha - ACtiva, INactiva
	 */
	private String status;
	
	/**
	 * La fecha de la &uacute;ltima modificaci&oacute;n al registro.
	 */
	private Date fechaUltMod;
	
	/**
	 * Clave del usuario que inserto la operaci&oacute;n
	 */
	private String claveUsuarioCaptura;
	
	/**
	 * Fecha en la que se inserto el registro.
	 */
	private Date fechaCaptura;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 5117437177735519159L;
}

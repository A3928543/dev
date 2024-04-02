/*
 * $Id: GrupoTrabajo.java,v 1.2 2010/04/30 17:39:31 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="SC_GRUPO_TRABAJO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.GrupoTrabajo"
 * dynamic-update="true" 
 * 
 * @author Israel Rebollar
 * @version  $Revision: 1.2 $ $Date: 2010/04/30 17:39:31 $
 */
public class GrupoTrabajo implements Serializable {
	
	/**
	 *  Constructor vac&iacute;o. No hace nada.
	 */
	public GrupoTrabajo() {
		super();
	}
	
	/**
	 * @hibernate.id generator-class="assigned"
	 * column="ID_GRUPO_TRABAJO"
     * unsaved-value="null"
     * @return String.
	 */
	public String getIdGrupoTrabajo() {
		return idGrupoTrabajo;
	}

	/**
	 * @param idGrupoTrabajo the idGrupoTrabajo to set
	 */
	public void setIdGrupoTrabajo(String idGrupoTrabajo) {
		this.idGrupoTrabajo = idGrupoTrabajo;
	}

    /**
     * Regresa el valor de nombre.
     *
     * @hibernate.property column="NOMBRE"
     * not-null="true"
     * unique="false"
     * @return String.
     */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

    /**
     * Regresa el valor de email.
     *
     * @hibernate.property column="EMAIL"
     * not-null="true"
     * unique="false"
     * @return String.
     */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof GrupoTrabajo)) {
            return false;
        }
        GrupoTrabajo castOther = (GrupoTrabajo) other;
        return new EqualsBuilder().append(this.getIdGrupoTrabajo().trim(), 
        		castOther.getIdGrupoTrabajo().trim()).isEquals();
    }
    
    /**
     * Concatena en forma de cadenas las propiedades del Deal Detalle.
     *
     * @return El Objeto como cadena.
     */
    public String toString() {
    	return new ToStringBuilder(this).append("idGrupoTrabajo", idGrupoTrabajo).
    		append("nombre", nombre).append("email", email).toString();
    }

    /**
     * Hibernate lo utiliza para cuestiones de herencia.
     * Regresa el hashCode del Objeto.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code> El HashCode identificador del objeto.
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdGrupoTrabajo()).toHashCode();
    }
    
	
	/**
	 * El identificador del registro.
	 */
	private String idGrupoTrabajo = "";
	
	/**
	 * El nombre del grupo de trabajo.
	 */
	private String nombre = "";
	
	/**
	 * El email del(los) responsable(s) del grupo de trabajo.
	 */
	private String email = "";
	
	/**
	 * El UID para serializaci&oacute;n.
	 */
	private static final long serialVersionUID = 5855165988885736879L;

	
	
}

/*
 * $Id: GrupoTrabajoPromotorPK.java,v 1.2 2010/04/30 17:40:46 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.ixe.ods.bup.model.EmpleadoIxe;

/**
 * Llave primaria de la clase GrupoTrabajoPromotor.
 *
 * @author Israel Rebollar
 * @version 1.0 $Date: 2010/04/30 17:40:46 $
 * @see GrupoTrabajoPromotor
 */
public class GrupoTrabajoPromotorPK implements Serializable {
		
    /**
     * Returns the empleadoIxe.
     *
     * @hibernate.many-to-one column="ID_PROMOTOR"
     * cascade="none"
     * class="com.ixe.ods.bup.model.EmpleadoIxe"
     * outer-join="auto"
     * unique="false"
     * @return EmpleadoIxe.
     */
	public EmpleadoIxe getPromotor() {
		return promotor;
	}

	/**
	 * @param promotor the promotor to set
	 */
	public void setPromotor(EmpleadoIxe promotor) {
		this.promotor = promotor;
	}
	
    /**
     * @hibernate.many-to-one column="ID_GRUPO_TRABAJO"
     * cascade="none"
     * class="com.ixe.ods.sica.model.GrupoTrabajo"
     * outer-join="auto"
     * unique="false"
     * @return the gpoTrabajo
	 */
	public GrupoTrabajo getGrupoTrabajo() {
		return grupoTrabajo;
	}

	/**
	 * @param gpoTrabajo the gpoTrabajo to set
	 */
	public void setGrupoTrabajo(GrupoTrabajo gpoTrabajo) {
		this.grupoTrabajo = gpoTrabajo;
	}

    /**
     * Implements a standard way to compare instances.
     * 
     * @param other Another object.
     * @return  <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (!(other instanceof GrupoTrabajoPromotorPK)) {
            return false;
        }
        GrupoTrabajoPromotorPK object = (GrupoTrabajoPromotorPK) other;
        return new EqualsBuilder()
                .append(this.getPromotor().getIdPersona(),
                        object.getPromotor().getIdPersona()).isEquals();
    }
      
	/**
	 * Hibernate lo utiliza para cuestiones de herencia.
	 * 
	 * @return int El HashCode identificador del objeto.
	 */
	public int hashCode() {
		return new HashCodeBuilder().append(promotor.getIdPersona()).toHashCode();
	}
	
	/**
	 * Relaci&oacute;n muchos-a-uno con EmpleadoIxe
	 */
	private EmpleadoIxe promotor;
	
	/**
	 * 
	 */
	private GrupoTrabajo grupoTrabajo;
	
	/**
	 * El UID para serialziaci&oacute;n.
	 */
	private static final long serialVersionUID = 7185464504146134714L;

}

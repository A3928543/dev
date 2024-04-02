/*
 * $Id: FechaInhabilEuaPK.java,v 1.1 2009/11/17 16:49:22 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Llave primaria compuesta para la entidad FechaInhabilEUA.
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1 $ $Date: 2009/11/17 16:49:22 $
 */
public class FechaInhabilEuaPK implements Serializable {

    /**
	 * Contructor defaul. No hace nada.
	 */
	public FechaInhabilEuaPK() {
		super();
	}

	/**
	 * Contructor inicializa el objeto con la fecha.
	 *  
	 * @param fecha La fecha festiva en EUA.
	 */
    public FechaInhabilEuaPK(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Regresa el valor de status.
     *
     * @hibernate.property column="FECHA"
     * not-null="true"
     * unique="true"
     * @return Date.
     */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof FechaInhabilEuaPK)) {
            return false;
        }
        FechaInhabilEuaPK object = (FechaInhabilEuaPK) other;
        return new EqualsBuilder().append(this.fecha, object.getFecha()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getFecha()).toHashCode();
    }

    /**
     * La fecha inh&aacute;bil en Estados Unidos.
     */
    private Date fecha;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -1131794990241845383L;
}

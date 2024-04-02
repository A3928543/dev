package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Class ScControlProcesoCifras.
 *
 * @author i.hernandez.chavez
 */
@Entity
@Table(name = "SC_CONTROL_PROCESO_CIFRAS")
public class ScControlProcesoCifras implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id cifra control. */
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CIFRAS_CONTROL")
    private Long idCifrasControl;
  
    /** The fecha creacion. */
    @Basic(optional = false)
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    /** The fecha ult mod. */
    @Column(name = "FECHA_ULT_MOD")
    private Date fechaUltMod;
    
    /** The fecha recepcion str. */
    @Basic(optional = false)
    @Column(name = "FECHA_RECEPCION_STR")
    private String fechaRecepcionStr;

	/**
	 * Gets the id cifra control.
	 *
	 * @return the id cifra control
	 */
	public Long getIdCifrasControl() {
		return idCifrasControl;
	}

	/**
	 * Sets the id cifra control.
	 *
	 * @param idCifrasControl the new id cifra control
	 */
	public void setIdCifrasControl(Long idCifraControl) {
		this.idCifrasControl = idCifraControl;
	}

	/**
	 * Gets the fecha creacion.
	 *
	 * @return the fecha creacion
	 */
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * Sets the fecha creacion.
	 *
	 * @param fechaCreacion the new fecha creacion
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * Gets the fecha ult mod.
	 *
	 * @return the fecha ult mod
	 */
	public Date getFechaUltMod() {
		return fechaUltMod;
	}

	/**
	 * Sets the fecha ult mod.
	 *
	 * @param fechaUltMod the new fecha ult mod
	 */
	public void setFechaUltMod(Date fechaUltMod) {
		this.fechaUltMod = fechaUltMod;
	}

	/**
	 * Gets the fecha recepcion str.
	 *
	 * @return the fecha recepcion str
	 */
	public String getFechaRecepcionStr() {
		return fechaRecepcionStr;
	}

	/**
	 * Sets the fecha recepcion str.
	 *
	 * @param fechaRecepcionStr the new fecha recepcion str
	 */
	public void setFechaRecepcionStr(String fechaRecepcionStr) {
		this.fechaRecepcionStr = fechaRecepcionStr;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idCifrasControl == null) ? 0 : idCifrasControl.hashCode());
		return result;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScControlProcesoCifras other = (ScControlProcesoCifras) obj;
		if (idCifrasControl == null) {
			if (other.idCifrasControl != null)
				return false;
		} else if (!idCifrasControl.equals(other.idCifrasControl))
			return false;
		return true;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "ScControlProcesoCifras [idCifrasControl=" + idCifrasControl
				+ ", fechaCreacion=" + fechaCreacion + ", fechaUltMod="
				+ fechaUltMod + ", fechaRecepcionStr=" + fechaRecepcionStr
				+ "]";
	}
}

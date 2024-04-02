package com.banorte.ods.sica.batch.transfint.domain;

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
 * The Class BupFechaInhabil.
 */
@Entity
@Table(name = "BUP_FECHA_INHABIL")
public class BupFechaInhabil implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The fecha. */
	@Id
	@Basic(optional = false)
	@Column(name = "FECHA")
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	/** The descripcion. */
	@Basic(optional = false)
	@Column(name = "DESCRIPCION")
	private String descripcion;

	/**
	 * Instantiates a new bup fecha inhabil.
	 */
	public BupFechaInhabil() {
	}

	/**
	 * Gets the fecha.
	 *
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * Sets the fecha.
	 *
	 * @param fecha the new fecha
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * Gets the descripcion.
	 *
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Sets the descripcion.
	 *
	 * @param descripcion the new descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}

package com.banorte.ods.sica.batch.transfint.domain;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Class ScReporteTransfint.
 *
 * @author i.hernandez.chavez
 */
@Entity
@Table(name = "SC_REPORTE_TRANSFINT")
@SequenceGenerator(name = "ScReporteTransfintSeq", sequenceName = "SC_REPORTE_TRANSFINT_SEQ",
					allocationSize = 1)
public class ScReporteTransfint implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id reporte transfint. */
	@Id
	@Basic(optional = false)
	@Column(name = "ID_REPORTE_TRANSFINT")
	@GeneratedValue(strategy = SEQUENCE, generator = "ScReporteTransfintSeq")
	private Long idReporteTransfint;

	/** The tipo reporte. */
	@Basic(optional = false)
	@Column(name = "TIPO_REPORTE")
	private Short tipoReporte;

	/** The nombre archivo. */
	@Column(name = "NOMBRE_ARCHIVO")
	private String nombreArchivo;

	/** The num operaciones. */
	@Column(name = "NUM_OPERACIONES")
	private Integer numOperaciones;

	/** The reproceso. */
	@Basic(optional = false)
	@Column(name = "REPROCESO")
	private short reproceso;

	/** The fecha ini. */
	@Basic(optional = false)
	@Column(name = "FECHA_INI")
	private String fechaIni;

	/** The fecha fin. */
	@Basic(optional = false)
	@Column(name = "FECHA_FIN")
	private String fechaFin;

	/** The fase. */
	@Basic(optional = false)
	@Column(name = "FASE")
	private Short fase;

	/** The fecha creacion. */
	@Basic(optional = false)
	@Column(name = "FECHA_CREACION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCreacion;

	/** The fecha ult mod. */
	@Column(name = "FECHA_ULT_MOD")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaUltMod;

	/** The sc detalle rep transfint list. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idReporteTransfint", fetch = FetchType.EAGER)
	private List<ScDetalleRepTransfint> detalles;

	/**
	 * Instantiates a new sc reporte transfint.
	 */
	public ScReporteTransfint() {
	}

	/**
	 * Instantiates a new sc reporte transfint.
	 *
	 * @param idReporteTransfint the id reporte transfint
	 */
	public ScReporteTransfint(Long idReporteTransfint) {
		this.idReporteTransfint = idReporteTransfint;
	}

	/**
	 * Instantiates a new sc reporte transfint.
	 *
	 * @param idReporteTransfint the id reporte transfint
	 * @param tipoReporte the tipo reporte
	 * @param reproceso the reproceso
	 * @param fechaIni the fecha ini
	 * @param fechaFin the fecha fin
	 * @param fase the fase
	 * @param fechaCreacion the fecha creacion
	 */
	public ScReporteTransfint(Long idReporteTransfint, short tipoReporte,
			Short reproceso, String fechaIni, String fechaFin, Short fase,
			Date fechaCreacion) {
		this.idReporteTransfint = idReporteTransfint;
		this.tipoReporte = tipoReporte;
		this.reproceso = reproceso;
		this.fechaIni = fechaIni;
		this.fechaFin = fechaFin;
		this.fase = fase;
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * Gets the id reporte transfint.
	 *
	 * @return the id reporte transfint
	 */
	public Long getIdReporteTransfint() {
		return idReporteTransfint;
	}

	/**
	 * Sets the id reporte transfint.
	 *
	 * @param idReporteTransfint the new id reporte transfint
	 */
	public void setIdReporteTransfint(Long idReporteTransfint) {
		this.idReporteTransfint = idReporteTransfint;
	}

	/**
	 * Gets the tipo reporte.
	 *
	 * @return the tipo reporte
	 */
	public Short getTipoReporte() {
		return tipoReporte;
	}

	/**
	 * Sets the tipo reporte.
	 *
	 * @param tipoReporte the new tipo reporte
	 */
	public void setTipoReporte(short tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	/**
	 * Gets the nombre archivo.
	 *
	 * @return the nombre archivo
	 */
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	/**
	 * Sets the nombre archivo.
	 *
	 * @param nombreArchivo the new nombre archivo
	 */
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	/**
	 * Gets the num operaciones.
	 *
	 * @return the num operaciones
	 */
	public Integer getNumOperaciones() {
		return numOperaciones;
	}

	/**
	 * Sets the num operaciones.
	 *
	 * @param numOperaciones the new num operaciones
	 */
	public void setNumOperaciones(Integer numOperaciones) {
		this.numOperaciones = numOperaciones;
	}

	/**
	 * Gets the reproceso.
	 *
	 * @return the reproceso
	 */
	public short getReproceso() {
		return reproceso;
	}

	/**
	 * Sets the reproceso.
	 *
	 * @param reproceso the new reproceso
	 */
	public void setReproceso(short reproceso) {
		this.reproceso = reproceso;
	}

	/**
	 * Gets the fecha ini.
	 *
	 * @return the fecha ini
	 */
	public String getFechaIni() {
		return fechaIni;
	}

	/**
	 * Sets the fecha ini.
	 *
	 * @param fechaIni the new fecha ini
	 */
	public void setFechaIni(String fechaIni) {
		this.fechaIni = fechaIni;
	}

	/**
	 * Gets the fecha fin.
	 *
	 * @return the fecha fin
	 */
	public String getFechaFin() {
		return fechaFin;
	}

	/**
	 * Sets the fecha fin.
	 *
	 * @param fechaFin the new fecha fin
	 */
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * Gets the fase.
	 *
	 * @return the fase
	 */
	public Short getFase() {
		return fase;
	}

	/**
	 * Sets the fase.
	 *
	 * @param fase the new fase
	 */
	public void setFase(short fase) {
		this.fase = fase;
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
	 * Gets the detalles.
	 *
	 * @return the detalles
	 */
	public List<ScDetalleRepTransfint> getDetalles() {
		return detalles;
	}
	
	/**
	 * Sets the detalles.
	 *
	 * @param detalles the new detalles
	 */
	public void setDetalles(List<ScDetalleRepTransfint> detalles) {
		this.detalles = detalles;
	}
	
	/**
	 * Adds the detalle.
	 *
	 * @param detalle the detalle
	 */
	public void addDetalle(ScDetalleRepTransfint detalle) {
		if (this.detalles == null) {
			this.detalles = 
					new ArrayList<ScDetalleRepTransfint>();
		}
		this.detalles.add(detalle);
	}

	
	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idReporteTransfint != null ? idReporteTransfint.hashCode() : 0);
		return hash;
	}

	
	/**
	 * Equals.
	 *
	 * @param object the object
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ScReporteTransfint)) {
			return false;
		}
		ScReporteTransfint other = (ScReporteTransfint) object;
		if ((this.idReporteTransfint == null && other.idReporteTransfint != null)
				|| (this.idReporteTransfint != null && !this.idReporteTransfint
						.equals(other.idReporteTransfint))) {
			return false;
		}
		return true;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "ScReporteTransfint [idReporteTransfint=" + idReporteTransfint
				+ ", tipoReporte=" + tipoReporte + ", nombreArchivo="
				+ nombreArchivo + ", numOperaciones=" + numOperaciones
				+ ", reproceso=" + reproceso + ", fechaIni=" + fechaIni
				+ ", fechaFin=" + fechaFin + ", fase=" + fase
				+ ", fechaCreacion=" + fechaCreacion + ", fechaUltMod="
				+ fechaUltMod + ", detalles=" + detalles + "]";
	}

}

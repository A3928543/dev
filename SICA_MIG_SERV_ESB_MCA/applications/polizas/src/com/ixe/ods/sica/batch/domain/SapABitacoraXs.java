package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author IHernandez
 */
@Entity
@Table(name = "SAP_A_BITACORA_XS")
public class SapABitacoraXs implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "FEC_DOCUMENTO")
	@Temporal(TemporalType.DATE)
	private Date fecDocumento;
	
	@Column(name = "FEC_GENERACION")
	@Temporal(TemporalType.DATE)
	private Date fecGeneracion;
	
	@Column(name = "FEC_PROCESO")
	@Temporal(TemporalType.DATE)
	private Date fecProceso;
	
	@Column(name = "APLICACION")
	private String aplicacion;
	
	@Column(name = "SUBAPLICACION")
	private String subaplicacion;
	
	@Column(name = "OPERACION")
	private String operacion;
	
	@Column(name = "FUENTE")
	private String fuente;
	
	@Id
	@Basic(optional = false)
	@Column(name = "ID_CARGA")
	private Long idCarga;
	
	@Column(name = "REGAPROCESAR")
	private Long regaprocesar;
	
	@Column(name = "REGPROCESADOS")
	private Long regprocesados;
	
	@Column(name = "TOT_CARGOS")
	private BigDecimal totCargos;
	
	@Column(name = "TOT_ABONOS")
	private BigDecimal totAbonos;
	
	@Column(name = "TOT_REG_CARGOS")
	private Long totRegCargos;
	
	@Column(name = "TOT_REG_ABONOS")
	private Long totRegAbonos;
	
	@Column(name = "ESTATUS")
	private Character estatus;

	public SapABitacoraXs() {
	}

	public SapABitacoraXs(Long idCarga) {
		this.idCarga = idCarga;
	}

	public Date getFecDocumento() {
		return fecDocumento;
	}

	public void setFecDocumento(Date fecDocumento) {
		this.fecDocumento = fecDocumento;
	}

	public Date getFecGeneracion() {
		return fecGeneracion;
	}

	public void setFecGeneracion(Date fecGeneracion) {
		this.fecGeneracion = fecGeneracion;
	}

	public Date getFecProceso() {
		return fecProceso;
	}

	public void setFecProceso(Date fecProceso) {
		this.fecProceso = fecProceso;
	}

	public String getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}

	public String getSubaplicacion() {
		return subaplicacion;
	}

	public void setSubaplicacion(String subaplicacion) {
		this.subaplicacion = subaplicacion;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getFuente() {
		return fuente;
	}

	public void setFuente(String fuente) {
		this.fuente = fuente;
	}

	public Long getIdCarga() {
		return idCarga;
	}

	public void setIdCarga(Long idCarga) {
		this.idCarga = idCarga;
	}

	public Long getRegaprocesar() {
		return regaprocesar;
	}

	public void setRegaprocesar(Long regaprocesar) {
		this.regaprocesar = regaprocesar;
	}

	public Long getRegprocesados() {
		return regprocesados;
	}

	public void setRegprocesados(Long regprocesados) {
		this.regprocesados = regprocesados;
	}

	public BigDecimal getTotCargos() {
		return totCargos;
	}

	public void setTotCargos(BigDecimal totCargos) {
		this.totCargos = totCargos;
	}

	public BigDecimal getTotAbonos() {
		return totAbonos;
	}

	public void setTotAbonos(BigDecimal totAbonos) {
		this.totAbonos = totAbonos;
	}

	public Long getTotRegCargos() {
		return totRegCargos;
	}

	public void setTotRegCargos(Long totRegCargos) {
		this.totRegCargos = totRegCargos;
	}

	public Long getTotRegAbonos() {
		return totRegAbonos;
	}

	public void setTotRegAbonos(Long totRegAbonos) {
		this.totRegAbonos = totRegAbonos;
	}

	public Character getEstatus() {
		return estatus;
	}

	public void setEstatus(Character estatus) {
		this.estatus = estatus;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idCarga != null ? idCarga.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof SapABitacoraXs)) {
			return false;
		}
		SapABitacoraXs other = (SapABitacoraXs) object;
		if ((this.idCarga == null && other.idCarga != null)
				|| (this.idCarga != null && !this.idCarga.equals(other.idCarga))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.ixe.ods.sica.batch.domain.SapABitacoraXs[ idCarga="
				+ idCarga + " ]";
	}

}

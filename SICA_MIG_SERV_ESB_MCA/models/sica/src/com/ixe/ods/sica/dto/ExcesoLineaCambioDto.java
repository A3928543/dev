package com.ixe.ods.sica.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ExcesoLineaCambioDto implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	private Integer idLineaCambio;
	private Integer idLineaCambioLog;
	private Date fechaOperacionExceso;
	private BigDecimal importeLineaAutorizada;
	private String tipoExceso;
	private BigDecimal importeExceso;
	private Integer idPersonaCliente;
	private String nombreCliente;
	private Integer idPersonaPromotor;
	private String nombrePromotor;
	private Integer numeroExcesoTrimestre;
	
	public Integer getIdLineaCambioLog() {
		return idLineaCambioLog;
	}
	public void setIdLineaCambioLog(Integer idLineaCambioLog) {
		this.idLineaCambioLog = idLineaCambioLog;
	}
	public Date getFechaOperacionExceso() {
		return fechaOperacionExceso;
	}
	public void setFechaOperacionExceso(Date fechaOperacionExceso) {
		this.fechaOperacionExceso = fechaOperacionExceso;
	}
	public BigDecimal getImporteLineaAutorizada() {
		return importeLineaAutorizada;
	}
	public void setImporteLineaAutorizada(BigDecimal importeLineaAutorizada) {
		this.importeLineaAutorizada = importeLineaAutorizada;
	}
	public String getTipoExceso() {
		return tipoExceso;
	}
	public void setTipoExceso(String tipoExceso) {
		this.tipoExceso = tipoExceso;
	}
	public BigDecimal getImporteExceso() {
		return importeExceso;
	}
	public void setImporteExceso(BigDecimal importeExceso) {
		this.importeExceso = importeExceso;
	}
	public Integer getIdPersonaCliente() {
		return idPersonaCliente;
	}
	public void setIdPersonaCliente(Integer idPersonaCliente) {
		this.idPersonaCliente = idPersonaCliente;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public Integer getIdPersonaPromotor() {
		return idPersonaPromotor;
	}
	public void setIdPersonaPromotor(Integer idPersonaPromotor) {
		this.idPersonaPromotor = idPersonaPromotor;
	}
	public String getNombrePromotor() {
		return nombrePromotor;
	}
	public void setNombrePromotor(String nombrePromotor) {
		this.nombrePromotor = nombrePromotor;
	}
	public Integer getNumeroExcesoTrimestre() {
		return numeroExcesoTrimestre;
	}
	public void setNumeroExcesoTrimestre(Integer numeroExcesoTrimestre) {
		this.numeroExcesoTrimestre = numeroExcesoTrimestre;
	}
	public Integer getIdLineaCambio() {
		return idLineaCambio;
	}
	public void setIdLineaCambio(Integer idLineaCambio) {
		this.idLineaCambio = idLineaCambio;
	}
}

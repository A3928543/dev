package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author IHernandez
 */
@Entity
@Table(name = "SC_POLIZA")
public class ScPoliza implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "ID_POLIZA")
	private BigDecimal idPoliza;
	
	@Basic(optional = false)
	@Column(name = "FECHA_VALOR")
	@Temporal(TemporalType.DATE)
	private Date fechaValor;
	
	@Basic(optional = false)
	@Column(name = "CUENTA_CONTABLE")
	private String cuentaContable;
	
	@Basic(optional = false)
	@Column(name = "ENTIDAD")
	private String entidad;
	
	@Basic(optional = false)
	@Column(name = "CARGO_ABONO")
	private String cargoAbono;
	
	@Basic(optional = false)
	@Column(name = "IMPORTE")
	private BigDecimal importe;
	
	@Basic(optional = false)
	@Column(name = "ID_DEAL")
	private BigInteger idDeal;
	
	@Basic(optional = false)
	@Column(name = "REFERENCIA")
	private String referencia;
	
	@Basic(optional = false)
	@Column(name = "FECHA_CREACION")
	@Temporal(TemporalType.DATE)
	private Date fechaCreacion;
	
	@Basic(optional = false)
	@Column(name = "TIPO_DEAL")
	private String tipoDeal;
	
	@Basic(optional = false)
	@Column(name = "SUCURSAL_ORIGEN")
	private String sucursalOrigen;
	
	@Column(name = "CLIENTE")
	private String cliente;
	
	@Column(name = "FECHA_PROCESAMIENTO")
	@Temporal(TemporalType.DATE)
	private Date fechaProcesamiento;
	
	@Basic(optional = false)
	@Column(name = "STATUS_PROCESO")
	private Character statusProceso;
	
	@Column(name = "FOLIO_DETALLE")
	private Short folioDetalle;
	
	@Basic(optional = false)
	@Column(name = "TIPO_CAMBIO")
	private BigDecimal tipoCambio;
	
	@Column(name = "NO_CONTRATO_SICA")
	private String noContratoSica;
	
	@Column(name = "CLAVE_REFERENCIA")
	private String claveReferencia;
	
	@Column(name = "ID_DEAL_POSICION")
	private BigInteger idDealPosicion;
	
	@Column(name = "SAP_REFERENCIA")
	private String sapReferencia;
	
	@JoinColumn(name = "ID_DIVISA", referencedColumnName = "ID_DIVISA")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private ScDivisa scDivisa;

	public ScPoliza() {
	}

	public ScPoliza(BigDecimal idPoliza) {
		this.idPoliza = idPoliza;
	}

	public ScPoliza(BigDecimal idPoliza, Date fechaValor,
			String cuentaContable, String entidad, String cargoAbono,
			BigDecimal importe, BigInteger idDeal, String referencia,
			Date fechaCreacion, String tipoDeal, String sucursalOrigen,
			Character statusProceso, BigDecimal tipoCambio) {
		this.idPoliza = idPoliza;
		this.fechaValor = fechaValor;
		this.cuentaContable = cuentaContable;
		this.entidad = entidad;
		this.cargoAbono = cargoAbono;
		this.importe = importe;
		this.idDeal = idDeal;
		this.referencia = referencia;
		this.fechaCreacion = fechaCreacion;
		this.tipoDeal = tipoDeal;
		this.sucursalOrigen = sucursalOrigen;
		this.statusProceso = statusProceso;
		this.tipoCambio = tipoCambio;
	}

	public BigDecimal getIdPoliza() {
		return idPoliza;
	}

	public void setIdPoliza(BigDecimal idPoliza) {
		this.idPoliza = idPoliza;
	}

	public Date getFechaValor() {
		return fechaValor;
	}

	public void setFechaValor(Date fechaValor) {
		this.fechaValor = fechaValor;
	}

	public String getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getCargoAbono() {
		return cargoAbono;
	}

	public void setCargoAbono(String cargoAbono) {
		this.cargoAbono = cargoAbono;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public BigInteger getIdDeal() {
		return idDeal;
	}

	public void setIdDeal(BigInteger idDeal) {
		this.idDeal = idDeal;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getTipoDeal() {
		return tipoDeal;
	}

	public void setTipoDeal(String tipoDeal) {
		this.tipoDeal = tipoDeal;
	}

	public String getSucursalOrigen() {
		return sucursalOrigen;
	}

	public void setSucursalOrigen(String sucursalOrigen) {
		this.sucursalOrigen = sucursalOrigen;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public Date getFechaProcesamiento() {
		return fechaProcesamiento;
	}

	public void setFechaProcesamiento(Date fechaProcesamiento) {
		this.fechaProcesamiento = fechaProcesamiento;
	}

	public Character getStatusProceso() {
		return statusProceso;
	}

	public void setStatusProceso(Character statusProceso) {
		this.statusProceso = statusProceso;
	}

	public Short getFolioDetalle() {
		return folioDetalle;
	}

	public void setFolioDetalle(Short folioDetalle) {
		this.folioDetalle = folioDetalle;
	}

	public BigDecimal getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(BigDecimal tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public String getNoContratoSica() {
		return noContratoSica;
	}

	public void setNoContratoSica(String noContratoSica) {
		this.noContratoSica = noContratoSica;
	}

	public String getClaveReferencia() {
		return claveReferencia;
	}

	public void setClaveReferencia(String claveReferencia) {
		this.claveReferencia = claveReferencia;
	}

	public BigInteger getIdDealPosicion() {
		return idDealPosicion;
	}

	public void setIdDealPosicion(BigInteger idDealPosicion) {
		this.idDealPosicion = idDealPosicion;
	}

	public String getSapReferencia() {
		return sapReferencia;
	}

	public void setSapReferencia(String sapReferencia) {
		this.sapReferencia = sapReferencia;
	}

	public ScDivisa getScDivisa() {
		return scDivisa;
	}

	public void setScDivisa(ScDivisa scDivisa) {
		this.scDivisa = scDivisa;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idPoliza != null ? idPoliza.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ScPoliza)) {
			return false;
		}
		ScPoliza other = (ScPoliza) object;
		if ((this.idPoliza == null && other.idPoliza != null)
				|| (this.idPoliza != null && !this.idPoliza
						.equals(other.idPoliza))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.ixe.ods.sica.batch.domain.ScPoliza[ idPoliza=" + idPoliza
				+ " ]";
	}

}

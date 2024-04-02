package com.banorte.h2h.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DetalleH2H implements Serializable 
{

	private static final long serialVersionUID = 1L;
	
	private Long idDealPosicion = null; 
	private Long idDeal = null; 
	private Date fechaEnvio = null;
	private Integer version = null; 
	private String tipoOperacion = null;
	private Date fechaCaptura = null;
	private Date fechaEfectiva = null;
	private Date fechaLiquidacion = null;
	private BigDecimal importe = null;
	private BigDecimal montoDolarizado = null; 
	private BigDecimal factorDivisaUsd = null;
	private BigDecimal tipoCambio = null;
	private String idDivisa = null;
	private String plazo = null;
	private String producto = null;
	private String claveContraparte = null;
	private String nombreCliente = null; // Solo 50 caracteres 
	private String rfc = null;
	private String tipoCliente = null;
	private String statusH2H = null;
	private String enviada = null;
	private String folioBanxico = null;
	private Integer codigoError = null; 
	private String descripcionError = null;
	private Long idPersonaPromotor = null;
	private Integer cierreVespertino = null; 
	private Date fechaCancelacion = null;
	private String cancelarDetalle = null;
	
	private boolean procesadoOk = false;
	private long indiceH2H = 0;
	
	public Long getIndiceParaH2H()
	{
		if(idDealPosicion == null)
			return new Long(0);
		
		if(indiceH2H < 0)
			indiceH2H *= -1;
		
		return new Long(indiceH2H + idDealPosicion.longValue());
	}
	
	public Long getIdDealPosicion() {
		return idDealPosicion;
	}
	public void setIdDealPosicion(Long idDealPosicion) {
		this.idDealPosicion = idDealPosicion;
	}
	public Long getIdDeal() {
		return idDeal;
	}
	public void setIdDeal(Long idDeal) {
		this.idDeal = idDeal;
	}
	public Date getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	public Date getFechaCaptura() {
		return fechaCaptura;
	}
	public void setFechaCaptura(Date fechaCaptura) {
		this.fechaCaptura = fechaCaptura;
	}
	public Date getFechaEfectiva() {
		return fechaEfectiva;
	}
	public void setFechaEfectiva(Date fechaEfectiva) {
		this.fechaEfectiva = fechaEfectiva;
	}
	public Date getFechaLiquidacion() {
		return fechaLiquidacion;
	}
	public void setFechaLiquidacion(Date fechaLiquidacion) {
		this.fechaLiquidacion = fechaLiquidacion;
	}
	public BigDecimal getImporte() {
		return importe;
	}
	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}
	public BigDecimal getMontoDolarizado() {
		return montoDolarizado;
	}
	public void setMontoDolarizado(BigDecimal montoDolarizado) {
		this.montoDolarizado = montoDolarizado;
	}
	public BigDecimal getFactorDivisaUsd() {
		return factorDivisaUsd;
	}
	public void setFactorDivisaUsd(BigDecimal factorDivisaUsd) {
		this.factorDivisaUsd = factorDivisaUsd;
	}
	public BigDecimal getTipoCambio() {
		return tipoCambio;
	}
	public void setTipoCambio(BigDecimal tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	public String getIdDivisa() {
		return idDivisa;
	}
	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}
	public String getPlazo() {
		return plazo;
	}
	public void setPlazo(String plazo) {
		this.plazo = plazo;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public String getClaveContraparte() {
		return claveContraparte;
	}
	public void setClaveContraparte(String claveContraparte) {
		this.claveContraparte = claveContraparte;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getTipoCliente() {
		return tipoCliente;
	}
	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}
	public String getStatusH2H() {
		return statusH2H;
	}
	public void setStatusH2H(String statusH2H) {
		this.statusH2H = statusH2H;
	}
	public String getEnviada() {
		return enviada;
	}
	public void setEnviada(String enviada) {
		this.enviada = enviada;
	}
	public String getFolioBanxico() {
		return folioBanxico;
	}
	public void setFolioBanxico(String folioBanxico) {
		this.folioBanxico = folioBanxico;
	}
	public Integer getCodigoError() {
		return codigoError;
	}
	public void setCodigoError(Integer codigoError) {
		this.codigoError = codigoError;
	}
	public String getDescripcionError() {
		return descripcionError;
	}
	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}
	public Long getIdPersonaPromotor() {
		return idPersonaPromotor;
	}
	public void setIdPersonaPromotor(Long idPersonaPromotor) {
		this.idPersonaPromotor = idPersonaPromotor;
	}
	public Integer getCierreVespertino() {
		return cierreVespertino;
	}
	public void setCierreVespertino(Integer cierreVespertino) {
		this.cierreVespertino = cierreVespertino;
	}
	public Date getFechaCancelacion() {
		return fechaCancelacion;
	}
	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}
	public String getCancelarDetalle() {
		return cancelarDetalle;
	}
	public void setCancelarDetalle(String cancelarDetalle) {
		this.cancelarDetalle = cancelarDetalle;
	}
	public boolean isProcesadoOk() {
		return procesadoOk;
	}
	public void setProcesadoOk(boolean procesadoOk) {
		this.procesadoOk = procesadoOk;
	}
	public long getIndiceH2H() {
		return indiceH2H;
	}
	public void setIndiceH2H(long indiceH2H) {
		this.indiceH2H = indiceH2H;
	} 
	
}

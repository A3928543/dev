package com.banorte.contrapartes.dto;

import java.math.BigDecimal;
import java.util.Date;

public class OperacionDto 
{
	private String cliente;
	private String contratoSica;
	private String tipoPersona;
	private String rfc;
	private int    numeroDeal;
	private String statusDetalleDeal;
	private String statusDeal;
	private String dealCorreccion;
	private Integer numDealOriginal;
	private Date fechaCreacion;
	private String fechaValor;
	private String promotor;
	private String idCanal;
	private int folioDetalle;
	private String entregarRecibir;
	private String idDivisa;
	private BigDecimal montoDivisaOriginal;
	private BigDecimal montoUsdDetalle;
	
	public OperacionDto(){}

	public OperacionDto(String cliente,             String contratoSica,      String tipoPersona,      
			            String rfc,                 Integer numeroDeal,       int folioDetalle, 
			            String entregarRecibir,     String idDivisa,          BigDecimal montoDivisaOriginal,
			            BigDecimal montoUsdDetalle, String statusDetalleDeal, String promotor,
			            String idCanal,             Date fechaCreacion,       String fechaValor,
			            String statusDeal,          String dealCorreccion,    Integer numDealOriginal     
						 ) 
	{
		this.cliente = cliente;
		this.contratoSica = contratoSica;
		this.tipoPersona = tipoPersona;
		this.rfc = rfc;
		this.numeroDeal = numeroDeal;
		this.folioDetalle = folioDetalle;
		this.entregarRecibir = entregarRecibir;
		this.idDivisa = idDivisa;
		this.montoDivisaOriginal = montoDivisaOriginal;
		this.montoUsdDetalle = montoUsdDetalle;
		this.statusDetalleDeal = statusDetalleDeal;
		this.promotor = promotor;
		this.idCanal = idCanal;
		this.fechaCreacion = fechaCreacion;
		this.fechaValor = fechaValor;
		this.statusDeal = statusDeal;
		this.dealCorreccion = dealCorreccion;
		this.numDealOriginal = numDealOriginal;
	}
	
	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getContratoSica() {
		return contratoSica;
	}

	public void setContratoSica(String contratoSica) {
		this.contratoSica = contratoSica;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public int getNumeroDeal() {
		return numeroDeal;
	}

	public void setNumeroDeal(int numeroDeal) {
		this.numeroDeal = numeroDeal;
	}

	public String getStatusDeal() {
		return statusDeal;
	}

	public void setStatusDeal(String statusDeal) {
		this.statusDeal = statusDeal;
	}

	public String getDealCorreccion() {
		return dealCorreccion;
	}

	public void setDealCorreccion(String dealCorreccion) {
		this.dealCorreccion = dealCorreccion;
	}

	public Integer getNumDealOriginal() {
		return numDealOriginal;
	}

	public void setNumDealOriginal(Integer numDealOriginal) {
		this.numDealOriginal = numDealOriginal;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getFechaValor() {
		return fechaValor;
	}

	public void setFechaValor(String fechaValor) {
		this.fechaValor = fechaValor;
	}

	public String getPromotor() {
		return promotor;
	}

	public void setPromotor(String promotor) {
		this.promotor = promotor;
	}

	public String getIdCanal() {
		return idCanal;
	}

	public void setIdCanal(String idCanal) {
		this.idCanal = idCanal;
	}

	public int getFolioDetalle() {
		return folioDetalle;
	}

	public void setFolioDetalle(int folioDetalle) {
		this.folioDetalle = folioDetalle;
	}

	public String getEntregarRecibir() {
		return entregarRecibir;
	}

	public void setEntregarRecibir(String entregarRecibir) {
		this.entregarRecibir = entregarRecibir;
	}

	public String getIdDivisa() {
		return idDivisa;
	}

	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}

	public BigDecimal getMontoDivisaOriginal() {
		return montoDivisaOriginal;
	}

	public void setMontoDivisaOriginal(BigDecimal montoDivisaOriginal) {
		this.montoDivisaOriginal = montoDivisaOriginal;
	}

	public BigDecimal getMontoUsdDetalle() {
		return montoUsdDetalle;
	}

	public void setMontoUsdDetalle(BigDecimal montoUsdDetalle) {
		this.montoUsdDetalle = montoUsdDetalle;
	}

	public String getStatusDetalleDeal() {
		return statusDetalleDeal;
	}

	public void setStatusDetalleDeal(String statusDetalleDeal) {
		this.statusDetalleDeal = statusDetalleDeal;
	}
}

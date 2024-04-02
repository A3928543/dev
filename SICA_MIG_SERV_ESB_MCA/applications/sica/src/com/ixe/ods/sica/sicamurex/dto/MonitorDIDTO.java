package com.ixe.ods.sica.sicamurex.dto;

import java.math.BigDecimal;

public class MonitorDIDTO {

	/** Identificador de Corte * */
    private int idCorte;
    
    /** Fecha de envio a MUREX del CorteDetalle * */
    private String fechaUltMod;
    
    /**
     * Divisa  del acumulado de compra/venta de Divisas
     */
    private String divisa="";
    
    /**
     * Fecha valor del acumulado de compra/venta de Divisas
     */
	private String fechaValor="";
	
	/**
	 * Tipo de operacion del acumulado de compra/venta de Divisas 
	 */
	private String operacion="";
	

	/**
	 * Monto en Divisa  del acumulado de compra/venta de Divisas
	 */
	private BigDecimal montoDivisa;
	
	/**
	 * Tipo de cambio  del acumulado de compra/venta de Divisas
	 */
	private BigDecimal tc;
	

	/**
	 * Ruta de la imagen de la divisa
	 */
	private String icono;
	
	/**
	 * Numero de Deal Interbancario
	 */
	private int idDeal;
	
	public int getIdDeal() {
		return idDeal;
	}

	public void setIdDeal(int idDeal) {
		this.idDeal = idDeal;
	}

	public int getIdCorte() {
		return idCorte;
	}

	public void setIdCorte(int idCorte) {
		this.idCorte = idCorte;
	}

	public String getFechaUltMod() {
		return fechaUltMod;
	}

	public void setFechaUltMod(String fechaUltMod) {
		this.fechaUltMod = fechaUltMod;
	}

	public String getDivisa() {
		return divisa;
	}

	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}

	public String getFechaValor() {
		return fechaValor;
	}

	public void setFechaValor(String fechaValor) {
		this.fechaValor = fechaValor;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}


	public BigDecimal getMontoDivisa() {
		return montoDivisa;
	}

	public void setMontoDivisa(BigDecimal montoDivisa) {
		this.montoDivisa = montoDivisa;
	}

	public BigDecimal getTc() {
		return tc;
	}

	public void setTc(BigDecimal tc) {
		this.tc = tc;
	}

	public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

}

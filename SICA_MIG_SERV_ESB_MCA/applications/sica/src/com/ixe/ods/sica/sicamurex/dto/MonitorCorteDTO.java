package com.ixe.ods.sica.sicamurex.dto;

import java.math.BigDecimal;

public class MonitorCorteDTO {
	
	/** Identificador de Corte * */
    private int idCorte;
    
    /** Fecha de envio a MUREX del CorteDetalle * */
    private String fechaCorteEM;
    
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
	 * Monto en Pesos del acumulado de compra/venta de Divisas  
	 */
	private BigDecimal montoMXNCliente;
	
	/**
	 * Monto en Divisa  del acumulado de compra/venta de Divisas
	 */
	private BigDecimal montoDivisa;
	
	/**
	 * Tipo de cambio  del acumulado de compra/venta de Divisas
	 */
	private BigDecimal tc;
	
	/**
	 * Tipo de cambio de fondeo  del acumulado de compra/venta de Divisas
	 */
	private BigDecimal tcFondeo;
	
	/**
	 * Estado del Corte 
	 */
	private String statusCorte;
	
	
	/**
	 * Ruta de la imagen de la divisa
	 */
	private String icono;

	public int getIdCorte() {
		return idCorte;
	}

	public void setIdCorte(int idCorte) {
		this.idCorte = idCorte;
	}

	public String getFechaCorteEM() {
		return fechaCorteEM;
	}

	public void setFechaCorteEM(String fechaCorteEM) {
		this.fechaCorteEM = fechaCorteEM;
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

	public BigDecimal getMontoMXNCliente() {
		return montoMXNCliente;
	}

	public void setMontoMXNCliente(BigDecimal montoMXNCliente) {
		this.montoMXNCliente = montoMXNCliente;
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

	public BigDecimal getTcFondeo() {
		return tcFondeo;
	}

	public void setTcFondeo(BigDecimal tcFondeo) {
		this.tcFondeo = tcFondeo;
	}

	public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public String getStatusCorte() {
		return statusCorte;
	}

	public void setStatusCorte(String statusCorte) {
		this.statusCorte = statusCorte;
	}
	
	
}

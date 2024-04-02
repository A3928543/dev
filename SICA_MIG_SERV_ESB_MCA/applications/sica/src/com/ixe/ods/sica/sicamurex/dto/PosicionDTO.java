package com.ixe.ods.sica.sicamurex.dto;

import java.math.BigDecimal;


/**
 * 
 * @author HMASG771
 *Estructura que se utiliza en la vista previa de
 *acumulados de compra/venta de divisas
 *
 */
public class PosicionDTO {
	
	private String divisa="";
	private String fechaValor="";
	private String operacion="";
	private BigDecimal montoMXNCliente;
	private BigDecimal montoDivisa;
	private BigDecimal tc;
	private BigDecimal tcFondeo;
	private String icono;
	private BigDecimal montoDivisaDI;
	private String operacionDI;
	private BigDecimal tcDI;
	private int idDealDI;
	
	/**
	 * 
	 * @param divisa Divisa frecuente, no frecuente o metal amonedado 
	 * @param fechaValor Puede ser CASH, TOM, SPOT, VFUT, 72hrs
	 * @param operacion Puede ser compra o venta
	 * @param montomxn Monto del acumulado de compra/venta de divisas en moneda nacional
	 * @param montoDivisa Monto del acumulado de compra/venta en DIVISA. 
	 * @param tc Tipo de Cambio
	 */
	public PosicionDTO(String divisa, String fechaValor, String operacion,
			BigDecimal montomxn, BigDecimal montoDivisa, BigDecimal tc,BigDecimal tcFondeo) {
		super();
		this.divisa = divisa;
		this.fechaValor = fechaValor;
		this.operacion = operacion;
		this.montoMXNCliente = montomxn;
		this.montoDivisa = montoDivisa;
		this.tc = tc;
		this.tcFondeo = tcFondeo;
	}
	/**
	 * 
	 * @param divisa Divisa frecuente, no frecuente o metal amonedado 
	 * @param fechaValor Puede ser CASH, TOM, SPOT, VFUT, 72hrs
	 * @param operacion Puede ser compra o venta
	 * @param montomxn Monto del acumulado de compra/venta de divisas en moneda nacional
	 * @param montoDivisa Monto del acumulado de compra/venta en DIVISA. 
	 * @param tc Tipo de Cambio
	 * @param tcFondeo Tipo de Cambio de la Mesa
	 * @param icono de la bandera
	 */
	public PosicionDTO(String divisa, String fechaValor, String operacion,
			BigDecimal montomxn, BigDecimal montoDivisa, BigDecimal tc,BigDecimal tcFondeo, String icono) {
		super();
		this.divisa = divisa;
		this.fechaValor = fechaValor;
		this.operacion = operacion;
		this.montoMXNCliente = montomxn;
		this.montoDivisa = montoDivisa;
		this.tc = tc;
		this.tcFondeo = tcFondeo;
		this.icono= icono;
	}
	
	
	public String getOperacionDI() {
		return operacionDI;
	}
	public void setOperacionDI(String operacionDI) {
		this.operacionDI = operacionDI;
	}
	public BigDecimal getTcDI() {
		return tcDI;
	}
	public void setTcDI(BigDecimal tcDI) {
		this.tcDI = tcDI;
	}
	public PosicionDTO() {
		// TODO Auto-generated constructor stub
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
	public BigDecimal getMontoDivisaDI() {
		return montoDivisaDI;
	}
	public void setMontoDivisaDI(BigDecimal montoDivisaDI) {
		this.montoDivisaDI = montoDivisaDI;
	}
	public int getIdDealDI() {
		return idDealDI;
	}
	public void setIdDealDI(int idDealDI) {
		this.idDealDI = idDealDI;
	}
}

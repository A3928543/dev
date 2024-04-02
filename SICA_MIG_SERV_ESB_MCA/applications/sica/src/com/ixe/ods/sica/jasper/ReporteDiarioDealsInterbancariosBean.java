/*
 * $Id: ReporteDiarioDealsInterbancariosBean.java,v 1.10 2008/02/22 18:25:03 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean para generar el reporte de Conciliacion Diaria 
 * 
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:03 $
 */
public class ReporteDiarioDealsInterbancariosBean implements Serializable{
	
	/**
	 * Constructor de la clase ReporteDiarioDealsInterbancarios, que se utiliza 
     * para almacenar los datos para la prueba del reporte. 
	 * 
	 * @param divisa La divisa del deal.
	 * @param fechaOperacion La fecha de operaci&oacute;n.
	 * @param idDeal El id del deal.
	 * @param fechaValor La fecha valor del deal.
	 * @param fecha La fecha actual.
	 * @param operacion El tipo de operacion.
	 * @param cliente El nombre del cliente.
	 * @param producto El tipo del producto.
	 * @param monto El monto del deal.
	 * @param tipoCambio El tipo de cambio.
	 * @param montoMN El monto en moneda nacional.
	 */
	public ReporteDiarioDealsInterbancariosBean(String divisa, Date fechaOperacion, String idDeal, String fechaValor, Date fecha, String operacion, String cliente,
			String producto, Double monto, Double tipoCambio, Double montoMN){
		
		this.divisa=divisa;
		this.fechaOperacion=fechaOperacion;
		this.idDeal=idDeal;
		this.fechaValor=fechaValor;
		this.fecha=fecha;
		this.operacion=operacion;
		this.cliente=cliente;
		this.producto=producto;
		this.tipoCambio=tipoCambio;
		this.monto=monto;
		this.montoMN=montoMN;
	}
	
	/**
	 * Constructor de la clase ReporteDiarioDealsInterbancarios, que se utiliza 
     * para almacenar los datos para la prueba del reporte. 
	 * 
	 * @param divisa La divisa del deal.
	 * @param fechaOperacion La fecha de operaci&oacute;n.
	 * @param idDealInteger El id del deal.
	 * @param folioDetalle El folio del detalle.
	 * @param fechaValor La fecha valor del deal.
	 * @param fecha La fecha actual.
	 * @param operacion El tipo de operacion.
	 * @param cliente El nombre del cliente.
	 * @param producto El tipo del producto.
	 * @param monto El monto del deal.
	 * @param tipoCambio El tipo de cambio.
	 * @param montoMN El monto en moneda nacional.
	 */
	public ReporteDiarioDealsInterbancariosBean(String divisa, Date fechaOperacion,Integer idDealInteger, Integer folioDetalle, String fechaValor, Date fecha, String operacion, String cliente,
			String producto, Double monto, Double tipoCambio, Double montoMN){

		this.divisa=divisa;
		this.fechaOperacion=fechaOperacion;
		this.idDealInteger=idDealInteger;
		this.folioDetalle=folioDetalle;
		this.fechaValor=fechaValor;
		this.fecha=fecha;
		this.operacion=operacion;
		this.cliente=cliente;
		this.producto=producto;
		this.tipoCambio=tipoCambio;
		this.monto=monto;
		this.montoMN=montoMN;
	}
		
	/**
	 * Regresa el valor de cliente.
	 *	
	 * @return String.
	 */
	public String getCliente() {
		return cliente;
	}

	/**
	 * Asigna el valor para cliente.
	 *
	 * @param cliente El valor para cliente.
	 */
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	/**
	 * Regresa el valor de divisa.
	 *	
	 * @return String.
	 */
	public String getDivisa() {
		return divisa;
	}

	/**
	 * Asigna el valor para divisa.
	 *
	 * @param divisa El valor para divisa.
	 */
	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}

	/**
	 * Regresa el valor de fecha.
	 *	
	 * @return Date.
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * Asigna el valor para fecha.
	 *
	 * @param fecha El valor para fecha.
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * Regresa el valor de fechaOperacion.
	 *	
	 * @return Date.
	 */
	public Date getFechaOperacion() {
		return fechaOperacion;
	}

	/**
	 * Asigna el valor para fechaOperacion.
	 *
	 * @param fechaOperacion El valor para fechaOperacion.
	 */
	public void setFechaOperacion(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}

	/**
	 * Regresa el valor de fechaValor.
	 *	
	 * @return String.
	 */
	public String getFechaValor() {
		return fechaValor;
	}

	/**
	 * Asigna el valor para fechaValor.
	 *
	 * @param fechaValor El valor para fechaValor.
	 */
	public void setFechaValor(String fechaValor) {
		this.fechaValor = fechaValor;
	}

	/**
	 * Regresa el valor de folioDetalle.
	 *	
	 * @return Integer.
	 */
	public Integer getFolioDetalle() {
		return folioDetalle;
	}

	/**
	 * Asigna el valor para folioDetalle.
	 *
	 * @param folioDetalle El valor para folioDetalle.
	 */
	public void setFolioDetalle(Integer folioDetalle) {
		this.folioDetalle = folioDetalle;
	}

	/**
	 * Regresa el valor de idDeal.
	 *	
	 * @return String.
	 */
	public String getIdDeal() {
		return idDeal;
	}

	/**
	 * Asigna el valor para idDeal.
	 *
	 * @param idDeal El valor para idDeal.
	 */
	public void setIdDeal(String idDeal) {
		this.idDeal = idDeal;
	}

	/**
	 * Regresa el valor de idDealInteger.
	 *	
	 * @return Integer.
	 */
	public Integer getIdDealInteger() {
		return idDealInteger;
	}

	/**
	 * Asigna el valor para idDealInteger.
	 *
	 * @param idDealInteger El valor para idDealInteger.
	 */
	public void setIdDealInteger(Integer idDealInteger) {
		this.idDealInteger = idDealInteger;
	}

	/**
	 * Regresa el valor de monto.
	 *	
	 * @return Double.
	 */
	public Double getMonto() {
		return monto;
	}

	/**
	 * Asigna el valor para monto.
	 *
	 * @param monto El valor para monto.
	 */
	public void setMonto(Double monto) {
		this.monto = monto;
	}

	/**
	 * Regresa el valor de montoMN.
	 *	
	 * @return Double.
	 */
	public Double getMontoMN() {
		return montoMN;
	}

	/**
	 * Asigna el valor para montoMN.
	 *
	 * @param montoMN El valor para montoMN.
	 */
	public void setMontoMN(Double montoMN) {
		this.montoMN = montoMN;
	}

	/**
	 * Regresa el valor de operacion.
	 *	
	 * @return String.
	 */
	public String getOperacion() {
		return operacion;
	}

	/**
	 * Asigna el valor para operacion.
	 *
	 * @param operacion El valor para operacion.
	 */
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	/**
	 * Regresa el valor de producto.
	 *	
	 * @return String.
	 */
	public String getProducto() {
		return producto;
	}

	/**
	 * Asigna el valor para producto.
	 *
	 * @param producto El valor para producto.
	 */
	public void setProducto(String producto) {
		this.producto = producto;
	}

	/**
	 * Regresa el valor de tipoCambio.
	 *	
	 * @return Double.
	 */
	public Double getTipoCambio() {
		return tipoCambio;
	}

	/**
	 * Asigna el valor para tipoCambio.
	 *
	 * @param tipoCambio El valor para tipoCambio.
	 */
	public void setTipoCambio(Double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	
	/**
	 * Divisa de operaci&oacute;n.
	 */
	private String divisa;
	
	/**
	 * Fecha de operaci&oacute;n del deal. 
	 */
	private Date fechaOperacion;
	
	/**
	 * Id del deal.
	 */
	private String idDeal;
	
	/**
	 * Folio del detalle de deal.
	 */
	private Integer folioDetalle;
	
	/**
	 * Fecha valor del deal.
	 */
	private String fechaValor;
	
	/**
	 * Fecha actual.
	 */
	private Date fecha;
	
	/**
	 * Tipo de operaci&oacute;n
	 */
	private String operacion;
	
	/**
	 * Nombre del cliente.
	 */
	private String cliente;
	
	/**
	 * Tipo de producto.
	 */
	private String producto;
	
	/**
	 * Monto del deal.
	 */
	private Double monto;
	
	/**
	 * Tipo de cambio.
	 */
	private Double tipoCambio;
	
	/**
	 * Monto en moneda nacional.
	 */
	private Double montoMN;
	
	/**
	 * Id deal de tipo Integer.
	 */
	private Integer idDealInteger;
}

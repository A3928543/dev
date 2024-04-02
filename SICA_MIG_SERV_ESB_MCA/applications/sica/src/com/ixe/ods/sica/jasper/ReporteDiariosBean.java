/*
 * $Id: ReporteDiariosBean.java,v 1.10 2008/02/22 18:25:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Bean que permite probar el reporte de diario
 *
 * @author Gerardo Corzo Herrera
 */
public class ReporteDiariosBean implements Serializable {

	/**
	 * Constructor del bean para el Reporte Diarios de Deal Interbancarios.
	 * 
	 * @param fechaActual La fecha del reporte.	
	 * @param divisa La divisa para el deal.
	 * @param claveFormaLiquidacion La forma de liquidaci&oacute;n del deal.
	 * @param promotor El nombre del promotor.
	 * @param idDeal El id del deal.
	 * @param monto El monto del deal.
	 * @param importe El importe del deal.
	 * @param tipoCambio EL tipo de cambio del deal.
	 * @param cliente El nombre del cliente.
	 * @param totalDivisa El monto total por divisa.
	 * @param fechaCaptura La fecha de captura del deal.
	 * @param fechaLiquidacion La fecha de liquidaci&oacute;n del deal.
	 * @param status El estats del deal.
	 * @param folioDetalle El folio del detalle del deal.
	 * @param isRecibimos Define si la operaci&oacute;n fue compra o venta.
	 * @param image La imagen de encabezado del reporte.
	 */
	public ReporteDiariosBean(Date fechaActual, String divisa,
			String claveFormaLiquidacion, String promotor, Integer idDeal,
			Double monto, Double importe, Double tipoCambio, String cliente,
			Double totalDivisa, Date fechaCaptura, Date fechaLiquidacion,
			String status, Integer folioDetalle, Boolean isRecibimos, InputStream image) {
		super();
		this.fechaActual = fechaActual;
		this.divisa = divisa;
		this.claveFormaLiquidacion = claveFormaLiquidacion;
		this.promotor = promotor;
		this.idDeal = idDeal;
		this.monto = monto;
		this.importe = importe;
		this.tipoCambio = tipoCambio;
		this.cliente = cliente;
		this.totalDivisa = totalDivisa;
		this.fechaCaptura = fechaCaptura;
		this.fechaLiquidacion = fechaLiquidacion;
		this.status = status;
		this.folioDetalle = folioDetalle;
		this.isRecibimos = isRecibimos;
		this.image = image;
	}

	/**
	 * Regresa el valor de claveFormaLiquidacion.
	 *	
	 * @return String.
	 */
	public String getClaveFormaLiquidacion() {
		return claveFormaLiquidacion;
	}

	/**
	 * Asigna el valor para claveFormaLiquidacion.
	 *
	 * @param claveFormaLiquidacion El valor para claveFormaLiquidacion.
	 */
	public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
		this.claveFormaLiquidacion = claveFormaLiquidacion;
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
	 * Regresa el valor de fechaActual.
	 *	
	 * @return Date.
	 */
	public Date getFechaActual() {
		return fechaActual;
	}

	/**
	 * Asigna el valor para fechaActual.
	 *
	 * @param fechaActual El valor para fechaActual.
	 */
	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	/**
	 * Regresa el valor de fechaCaptura.
	 *	
	 * @return Date.
	 */
	public Date getFechaCaptura() {
		return fechaCaptura;
	}

	/**
	 * Asigna el valor para fechaCaptura.
	 *
	 * @param fechaCaptura El valor para fechaCaptura.
	 */
	public void setFechaCaptura(Date fechaCaptura) {
		this.fechaCaptura = fechaCaptura;
	}

	/**
	 * Regresa el valor de fechaLiquidacion.
	 *	
	 * @return Date.
	 */
	public Date getFechaLiquidacion() {
		return fechaLiquidacion;
	}

	/**
	 * Asigna el valor para fechaLiquidacion.
	 *
	 * @param fechaLiquidacion El valor para fechaLiquidacion.
	 */
	public void setFechaLiquidacion(Date fechaLiquidacion) {
		this.fechaLiquidacion = fechaLiquidacion;
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
	 * @return Integer.
	 */
	public Integer getIdDeal() {
		return idDeal;
	}

	/**
	 * Asigna el valor para idDeal.
	 *
	 * @param idDeal El valor para idDeal.
	 */
	public void setIdDeal(Integer idDeal) {
		this.idDeal = idDeal;
	}

	/**
	 * Regresa el valor de image.
	 *	
	 * @return InputStream.
	 */
	public InputStream getImage() {
		return image;
	}

	/**
	 * Asigna el valor para image.
	 *
	 * @param image El valor para image.
	 */
	public void setImage(InputStream image) {
		this.image = image;
	}

	/**
	 * Regresa el valor de importe.
	 *	
	 * @return Double.
	 */
	public Double getImporte() {
		return importe;
	}

	/**
	 * Asigna el valor para importe.
	 *
	 * @param importe El valor para importe.
	 */
	public void setImporte(Double importe) {
		this.importe = importe;
	}

	/**
	 * Regresa el valor de isRecibimos.
	 *	
	 * @return Boolean.
	 */
	public Boolean getIsRecibimos() {
		return isRecibimos;
	}

	/**
	 * Asigna el valor para isRecibimos.
	 *
	 * @param isRecibimos El valor para isRecibimos.
	 */
	public void setIsRecibimos(Boolean isRecibimos) {
		this.isRecibimos = isRecibimos;
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
	 * Regresa el valor de promotor.
	 *	
	 * @return String.
	 */
	public String getPromotor() {
		return promotor;
	}

	/**
	 * Asigna el valor para promotor.
	 *
	 * @param promotor El valor para promotor.
	 */
	public void setPromotor(String promotor) {
		this.promotor = promotor;
	}

	/**
	 * Regresa el valor de status.
	 *	
	 * @return String.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Asigna el valor para status.
	 *
	 * @param status El valor para status.
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * Regresa el valor de totalDivisa.
	 *	
	 * @return Double.
	 */
	public Double getTotalDivisa() {
		return totalDivisa;
	}

	/**
	 * Asigna el valor para totalDivisa.
	 *
	 * @param totalDivisa El valor para totalDivisa.
	 */
	public void setTotalDivisa(Double totalDivisa) {
		this.totalDivisa = totalDivisa;
	}
	
	/**
	 * La fecha actual del sistema
	 */
	private Date fechaActual;
	
	/**
	 * La divisa
	 */
	private String divisa;
	
	/**
	 * La forma de liquidaci&oacute;n del Deal
	 */
	private String claveFormaLiquidacion;
	
	/**
	 * Nombre del promotor
	 */
	private String promotor;
	
	/**
	 * El id del Deal
	 */
	private Integer idDeal;
	
	/**
	 * El monto del deal
	 */
	private Double monto;
	
	/**
	 * El importe del detalle del Deal
	 */
	private Double importe;
	
	/**
	 * El tipo de cambio del detalle del Deal
	 */
	private Double tipoCambio;
	
	/**
	 * El nombre del cliente
	 */
	private String cliente;
	
	/**
	 * El total de la divisa del Deal
	 */
	private Double totalDivisa;
	
	/**
	 * La fecha de captura del Deal
	 */
	private Date fechaCaptura = new Date();
	
	/**
	 * La fecha de liquidaci&oacute;n del Deal
	 */
	private Date fechaLiquidacion = new Date();
	
	/**
	 * Estatus del Deal
	 */
    private String status;
    
	/**
	 * Folio del Detalle del Deal
	 */
    private Integer folioDetalle;
    
	/**
	 * Booleano isRecibimos del Detalle 
	 * del Deal
	 */
    private Boolean isRecibimos;
    
	/**
	 * Logo de IXE
	 */
    private InputStream image;
}

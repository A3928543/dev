/*
 * $Id: ReporteBanxicoAclmeBean.java,v 1.10 2008/02/22 18:25:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean para generar el reporte del ACLME para Banxico
 * 
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:04 $
 */
public class ReporteBanxicoAclmeBean implements Serializable {
	
	/** 
	 * Constructor de la clase ReporteBanxicoAclmeBean, que se utiliza 
     * para almacenar los datos para la prueba del reporte del Deal. 
	 * 
	 * @param claveGfIXE La clave para IXE por parte de Banxico.
	 * @param compra_venta Define si el deal es compra o venta.
	 * @param fechaPacto La fecha de pactaci&oacute;n del deal.
	 * @param fechaLiquidacion La fecha de liquidaci&oacute;n del deal.
	 * @param monto El monto del deal.
	 * @param divisa La divisa del deal.
	 * @param folioInterno El folio del deal.
	 * @param claveContraparte La clave de contraparte del cliente.
	 * @param estrategia La estrategia (Swap) del deal.
	 * @param folioEstrategia El folio de la estrategia (Swap) del deal.
	 * @param claveSector La clave del sector Banxico del deal.
	 * @param descripcionClaveSector la descripci&oacute;n de la calve del sector.
	 * @param descripcionDivisa La descripci&oacute;n de la divisa.
	 * @param tipoCambio El tipo de cambio del deal.
	 * @param contratoSica El contrato Sica del cliente.
	 * @param status El estatus del deal.
	 * @param cliente El nombre del cliente.
	 */
	public ReporteBanxicoAclmeBean(String claveGfIXE, String compra_venta,
			Date fechaPacto, Date fechaLiquidacion, Double monto,
			String divisa, String folioInterno, String claveContraparte,
			String estrategia, String folioEstrategia, String claveSector,
			String descripcionClaveSector, String descripcionDivisa,
			Double tipoCambio, String contratoSica, String status,
			String cliente) {
		super();
		this.claveGfIXE = claveGfIXE;
		this.compra_venta = compra_venta;
		this.fechaPacto = fechaPacto;
		this.fechaLiquidacion = fechaLiquidacion;
		this.monto = monto;
		this.divisa = divisa;
		this.folioInterno = folioInterno;
		this.claveContraparte = claveContraparte;
		this.estrategia = estrategia;
		this.folioEstrategia = folioEstrategia;
		this.claveSector = claveSector;
		this.descripcionClaveSector = descripcionClaveSector;
		this.descripcionDivisa = descripcionDivisa;
		this.tipoCambio = tipoCambio;
		this.contratoSica = contratoSica;
		this.status = status;
		this.cliente = cliente;
	}
	
	/**
	 * Constructor para generar el reporte de Deals Pendientes.
	 * 
	 * @param fechaSeleccionada La fecha seleccionada para buscar Deals Pendientes.
	 * @param fechaPacto La fecha valor del deal.
	 * @param fechaLiquidacion La fecha de liquidacion del deal. 
	 * @param monto El monto de la operaci&oacute;n.
	 * @param divisa La divisa del deal.
	 * @param folioInterno El id Deal.
	 * @param cliente El nombre del cliente.
	 * @param isRecibimos Define si la operacion es compra o venta.
	 */
	public ReporteBanxicoAclmeBean(	Date fechaSeleccionada, Date fechaPacto, Date fechaLiquidacion, Double monto,
			String divisa, String folioInterno, String cliente, Boolean isRecibimos, Boolean isPesos, Double tipoCambioCliente) {
		super();
		this.fechaSeleccionada = fechaSeleccionada;
		this.fechaPacto = fechaPacto;
		this.fechaLiquidacion = fechaLiquidacion;
		this.monto = monto;
		this.divisa = divisa;
		this.folioInterno = folioInterno;
		this.cliente = cliente;
		this.isRecibimos = isRecibimos;
		this.isPesos = isPesos;
		this.tipoCambioCliente = tipoCambioCliente;
	}
	
	/**
	 * Regresa el valor de claveContraparte.
	 * 
	 * @return String.
	 */
	public String getClaveContraparte() {
		return claveContraparte;
	}
	
	/**
	 * Asigna el valor para claveContraparte.
	 * 
	 * @param claveContraparte El valor para claveContraparte
	 */
	public void setClaveContraparte(String claveContraparte) {
		this.claveContraparte = claveContraparte;
	}
	
	/**
	 * Regresa el valor de claveGfIXE.
	 * 
	 * @return String
	 */
	public String getClaveGfIXE() {
		return claveGfIXE;
	}
	
	/**
	 * Asigna el valor para claveGfIXE
	 * 
	 * @param claveGfIXE El valor para claveGfIXE
	 */
	public void setClaveGfIXE(String claveGfIXE) {
		this.claveGfIXE = claveGfIXE;
	}
	
	/**
	 * Regrea el valor de claveSector.
	 * 
	 * @return String.
	 */
	public String getClaveSector() {
		return claveSector;
	}
	
	/**
	 * Asigna el valor para claveSector.
	 * 
	 * @param claveSector El valor para claveSector.
	 */
	public void setClaveSector(String claveSector) {
		this.claveSector = claveSector;
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
	 * Regresa el valor de compra_venta.
	 * 
	 * @return String.
	 */
	public String getCompra_venta() {
		return compra_venta;
	}
	
	/**
	 * Asigna el valor para compra_venta.
	 * 
	 * @param compra_venta ELl valor para compra_venta.
	 */
	public void setCompra_venta(String compra_venta) {
		this.compra_venta = compra_venta;
	}
	
	/**
	 * Regresa el valor de contratoSica.
	 * 
	 * @return String.
	 */
	public String getContratoSica() {
		return contratoSica;
	}
	
	/**
	 * Asigna el valor para contratoSica.
	 * 
	 * @param contratoSica El valor para contratoSica.
	 */
	public void setContratoSica(String contratoSica) {
		this.contratoSica = contratoSica;
	}
	
	/**
	 * Regresa el valor de descripcionClaveSector.
	 * 
	 * @return String.
	 */
	public String getDescripcionClaveSector() {
		return descripcionClaveSector;
	}
	
	/**
	 * Asigna el valor para descripcionClaveSector.
	 * 
	 * @param descripcionClaveSector El valor para descripcionClaveSector.
	 */
	public void setDescripcionClaveSector(String descripcionClaveSector) {
		this.descripcionClaveSector = descripcionClaveSector;
	}
	
	/**
	 * Regresa el valor de descripcionDivisa.
	 * 
	 * @return String.
	 */
	public String getDescripcionDivisa() {
		return descripcionDivisa;
	}
	
	/**
	 * Asigna el valor para descripcionDivisa.
	 * 
	 * @param descripcionDivisa El valor para descripcionDivisa.
	 */
	public void setDescripcionDivisa(String descripcionDivisa) {
		this.descripcionDivisa = descripcionDivisa;
	}
	
	/**
	 * Regresa el valoe de divisa.
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
	 * Regresa el valor de estrategia
	 * 
	 * @return String.
	 */
	public String getEstrategia() {
		return estrategia;
	}
	
	/**
	 * Asigna el valor para estrategia.
	 * 
	 * @param estrategia El valor para estrategia.
	 */
	public void setEstrategia(String estrategia) {
		this.estrategia = estrategia;
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
	 * Regresa el valor de fechaPacto.
	 * 
	 * @return Date.
	 */
	public Date getFechaPacto() {
		return fechaPacto;
	}
	
	/**
	 * Asigna el valor para fechaPacto.
	 * 
	 * @param fechaPacto El valor para fechaPacto.
	 */
	public void setFechaPacto(Date fechaPacto) {
		this.fechaPacto = fechaPacto;
	}
	
	/**
	 * Regresa el valor de folioEstrategia. 
	 *  
	 * @return String.
	 */
	public String getFolioEstrategia() {
		return folioEstrategia;
	}
	
	/**
	 * Asigna el valor para folioEstrategia.
	 * 
	 * @param folioEstrategia El valor para folioEstrategia.
	 */
	public void setFolioEstrategia(String folioEstrategia) {
		this.folioEstrategia = folioEstrategia;
	}
	
	/**
	 * Regresa el valor de folioInterno.
	 * 
	 * @return String.
	 */
	public String getFolioInterno() {
		return folioInterno;
	}
	
	/**
	 * Asigna el valor para folioInterno.
	 * 
	 * @param folioInterno El valor para folioInterno.
	 */
	public void setFolioInterno(String folioInterno) {
		this.folioInterno = folioInterno;
	}
	
	/**
	 * Regresa el valor de monto.
	 * 
	 * @return String.
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
	 * Asigna el valor de tipoCambio.
	 * 
	 * @param tipoCambio El valor para tipoCambio.
	 */
	public void setTipoCambio(Double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	
	/**
	 * Obtiene el valor de isRecibimos
	 * 
	 * @return Boolean
	 */
	public Boolean getIsRecibimos() {
		return isRecibimos;
	}

	/**
	 * Asigna el valor para isRecibimos
	 * 
	 * @param isRecibimos El valor de isRecibimos
	 */
	public void setIsRecibimos(Boolean isRecibimos) {
		this.isRecibimos = isRecibimos;
	}
	
	/**
	 * Obtiene el valor de fechaSeleccionada.
	 * 
	 * @return Date
	 */
	public Date getFechaSeleccionada() {
		return fechaSeleccionada;
	}

	/**
	 * Asigna el valor para fecha seleccionada.
	 * 
	 * @param fechaSeleccionada El valor para fechaSeleccionada.
	 */
	public void setFechaSeleccionada(Date fechaSeleccionada) {
		this.fechaSeleccionada = fechaSeleccionada;
	}
	
	/**
	 * Obtiene el valor de isPesos.
	 * 
	 * @return Boolean
	 */
	public Boolean getIsPesos() {
		return isPesos;
	}

	/**
	 * Asigna el valor de isPesos.
	 * 
	 * @param isPesos El valor para isPesos
	 */
	public void setIsPesos(Boolean isPesos) {
		this.isPesos = isPesos;
	}

	public Double getTipoCambioCliente() {
		return tipoCambioCliente;
	}

	public void setTipoCambioCliente(Double tipoCambioCliente) {
		this.tipoCambioCliente = tipoCambioCliente;
	}
	
	
	/**
	 * Clave de Banxico para IXE.
	 */
	private String claveGfIXE;
	
	/**
	 * Compra o venta.
	 */
	private String compra_venta;
	
	/**
	 * Divisa del deal.
	 */
	private String divisa;
	
	/**
	 * El folio interno del deal.
	 */
	private String folioInterno;
	
	/**
	 * Clave contraparte del cliente.
	 */
	private String claveContraparte;
	
	/**
	 * Estrategia (Swap).
	 */
	private String estrategia;
	
	/**
	 * Folio estrategia (Swap) del deal
	 */
	private String folioEstrategia;
	
	/**
	 * Fecha de pactaci&oacute;n del deal.
	 */
	private Date fechaPacto;
	
	/**
	 * Fecha de liquidaci&oacute;n del deal.
	 */
	private Date fechaLiquidacion;
	
	/**
	 * Monto del deal.
	 */
	private Double monto;
	
	/**
	 * Descripci&oacute;n de la divisa.
	 */
	private String descripcionDivisa;
	
	/**
	 * Clave del sector de Banxico.
	 */
	private String claveSector;
	
	/**
	 * Descripcion de la clave del sector.
	 */
	private String descripcionClaveSector;
	
	/**
	 * Tipo de cambio del deal.
	 */
	private Double tipoCambio;
	
	/**
	 * Contrato Sica del cliente.
	 */
	private String contratoSica;
	
	/**
	 * Estatus del deal.
	 */
	private String status;
	
	/**
	 * Nombre del cliente.
	 */
	private String cliente;
	
	/**
	 * Define si la operacion es compra o Venta.
	 */
	private Boolean isRecibimos;
	
	/**
	 * Define la fecha de deals pendientes.
	 */
	private Date fechaSeleccionada;
	
	/**
	 * Define si el detalle de deal es de pesos
	 */
	private Boolean isPesos;
	
	private Double tipoCambioCliente;
}
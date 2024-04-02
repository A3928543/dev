/*
 * $Id: ReporteBanxicoBean.java,v 1.10 2008/02/22 18:25:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Bean para generar el reporte Banxico
 * 
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:04 $
 */
public class ReporteBanxicoBean implements Serializable {

	/**
	 * Constructor de la clase ReporteBanxicoBean, que se utiliza 
     * para almacenar los datos para la prueba del reporte del Deal. 
	 * 
	 * @param comprado El monto de las compras.
	 * @param fechaCaptura La fecha de captura del deal.
	 * @param fechaValor La fecha valor del deal.
	 * @param sector El sector Banxico del cliente.
	 * @param vendido El monto de las ventas.
	 * @param mes El mes de captura.
	 * @param cliente El nombre del cliente.
	 * @param divisa La divisa del deal.
	 * @param factorDivisa El factor divisa acutal.
	 * @param idSector El id del sector Banxico.
	 * @param image La imagen de encabezado del deal.
	 */
	public ReporteBanxicoBean(Double comprado, Date fechaCaptura,
			Date fechaValor, String sector, Double vendido, String mes,
			String cliente, String divisa, Double factorDivisa,
			Integer idSector, InputStream image) {
		super();
		this.comprado = comprado;
		this.fechaCaptura = fechaCaptura;
		this.fechaValor = fechaValor;
		this.sector = sector;
		this.vendido = vendido;
		this.mes = mes;
		this.cliente = cliente;
		this.divisa = divisa;
		this.factorDivisa = factorDivisa;
		this.idSector = idSector;
		this.image = image;
	}

	/**
	 * Constructor de la clase ReporteBanxicoBean, que se utiliza 
     * para almacenar los datos para la prueba del reporte del Deal. 
	 * 
	 * @param fechaValor La fecha valor del deal.
	 * @param divisa La divisa del deal.
	 * @param folioInterno El folio interno del deal.
	 * @param estrategia La estrategia (Swap) del deal.
	 * @param folioEstrategia El folio de la estrategia (Swap).
	 * @param comprado El monto de las compras.
	 * @param vendido El monto de las ventas.
	 * @param claveContraparte La clave contraparte del cliente.
	 * @param cliente El nombre del cliente.
	 * @param codigoContraparte El codigo de la contraparte.
	 * @param tipoContraparte El tipo de contraparte
	 */
	public ReporteBanxicoBean(Date fechaValor, String divisa,
			String folioInterno, String estrategia, String folioEstrategia,
			Double comprado, Double vendido, String claveContraparte,
			String cliente, String codigoContraparte, String tipoContraparte) {
		super();
		this.fechaValor = fechaValor;
		this.divisa = divisa;
		this.folioInterno = folioInterno;
		this.estrategia = estrategia;
		this.folioEstrategia = folioEstrategia;
		this.comprado = comprado;
		this.vendido = vendido;
		this.claveContraparte = claveContraparte;
		this.cliente = cliente;
		this.codigoContraparte = codigoContraparte;
		this.tipoContraparte = tipoContraparte;

	}

	/**
	 * Constructor de la clase ReporteBanxicoBean, que se utiliza 
     * para almacenar los datos para la prueba del reporte del Deal. 
	 * 
	 * @param fechaValor La fecha valor del deal.
	 * @param divisa La divisa del deal.
	 * @param folioInterno El folio interno del deal.
	 * @param dealDivisaDivisa Define si el deal es Divisa-Divisa.
	 * @param estrategia La estrategia (Swap) 
	 * @param folioEstrategia El folio de la estrategia (Swap).
	 * @param comprado El monto de las compras.
	 * @param vendido El monto de las ventas.
	 * @param claveContraparte La clave de contraparte del cliente.
	 * @param cliente El nombre del cliente.
	 * @param codigoContraparte El codigo de la contraparte.
	 * @param tipoContraparte El tipo de la contratparte.
	 */
	public ReporteBanxicoBean(Date fechaValor, String divisa,
			String folioInterno, String dealDivisaDivisa, String estrategia,
			String folioEstrategia, Double comprado, Double vendido,
			String claveContraparte, String cliente, String codigoContraparte,
			String tipoContraparte) {
		super();
		this.fechaValor = fechaValor;
		this.divisa = divisa;
		this.folioInterno = folioInterno;
		this.dealDivisaDivisa = dealDivisaDivisa;
		this.estrategia = estrategia;
		this.folioEstrategia = folioEstrategia;
		this.comprado = comprado;
		this.vendido = vendido;
		this.claveContraparte = claveContraparte;
		this.cliente = cliente;
		this.codigoContraparte = codigoContraparte;
		this.tipoContraparte = tipoContraparte;

	}

	/**
	 * Constructor de la clase ReporteBanxicoBean, que se utiliza 
     * para almacenar los datos para la prueba del reporte del Deal.
     *  
	 * @param claveGfIXE La clave para IXE de Banxico
	 * @param compra_venta Define si la operacion es compra o venta.
	 * @param fechaPacto La fecha de pactaci&oacute;n del deal.
	 * @param fechaLiquidacion La fecha de liquidaci&oacute;n del deal.
	 * @param monto El monto del deal.
	 * @param divisa La divisa del deal.
	 * @param folioInterno El folio interno del deal.
	 * @param claveContraparte La clave contaparte del cliente.
	 * @param estrategia La estrategia (Swap) del deal.
	 * @param folioEstrategia El folio de la estrategia (Swap) del deal.
	 */
	public ReporteBanxicoBean(String claveGfIXE, String compra_venta,
			Date fechaPacto, Date fechaLiquidacion, String monto,
			String divisa, String folioInterno, String claveContraparte,
			String estrategia, String folioEstrategia) {
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

	}

	/**
	 * Regresa el valor de comprado
	 * 
	 * @return Double.
	 */
	public Double getComprado() {
		return comprado;
	}

	/**
	 * Asigna el valor para comrpado.
	 * 
	 * @param comprado El valor para comprado.
	 */
	public void setComprado(Double comprado) {
		this.comprado = comprado;
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
	 * Regresa el valor de fechaValor.
	 * 
	 * @return Date.
	 */
	public Date getFechaValor() {
		return fechaValor;
	}

	/**
	 * Asigna el valor para fechaValor.
	 * 
	 * @param fechaValor El valor para fechaValor.
	 */
	public void setFechaValor(Date fechaValor) {
		this.fechaValor = fechaValor;
	}

	/**
	 * Regresa el valor de sector.
	 * 
	 * @return String.
	 */
	public String getSector() {
		return sector;
	}

	/**
	 * Asigna el valor para sector.
	 * 
	 * @param sector El valor para sector.
	 */
	public void setSector(String sector) {
		this.sector = sector;
	}

	/**
	 * Regresa el valor de vendido.
	 * 
	 * @return Double.
	 */
	public Double getVendido() {
		return vendido;
	}

	/**
	 * Asigna el valor para vendido.
	 * 
	 * @param vendido El valor para vendido.
	 */
	public void setVendido(Double vendido) {
		this.vendido = vendido;
	}

	/**
	 * Regresa el valor de mes.
	 * 
	 * @return String.
	 */
	public String getMes() {
		return mes;
	}

	/**
	 * Asigna el valor para mes.
	 * 
	 * @param mes El valor para mes.
	 */
	public void setMes(String mes) {
		this.mes = mes;
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
	 * Regresa el valor de factorDivisa.
	 * 
	 * @return Double.
	 */
	public Double getFactorDivisa() {
		return factorDivisa;
	}

	/**
	 * Asigna el valor para factorDivisa.
	 * 
	 * @param factorDivisa El valor para factorDivisa.
	 */
	public void setFactorDivisa(Double factorDivisa) {
		this.factorDivisa = factorDivisa;
	}

	/**
	 * Regresa el valor de idSector.
	 * 
	 * @return Integer.
	 */
	public Integer getIdSector() {
		return idSector;
	}

	/**
	 * Asigna el valor para idSector.
	 * 
	 * @param idSector El valor para idSector.
	 */
	public void setIdSector(Integer idSector) {
		this.idSector = idSector;
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
	 * Regresa el valor de claveContraparte
	 * 
	 * @return String.
	 */
	public String getClaveContraparte() {
		return claveContraparte;
	}

	/**
	 * Asigna el valor para claveContraparte.
	 * 
	 * @param claveContraparte El valor para claveContraparte.
	 */
	public void setClaveContraparte(String claveContraparte) {
		this.claveContraparte = claveContraparte;
	}

	/**
	 * Regresa el valor de codigoContaparte
	 * 
	 * @return String.
	 */
	public String getCodigoContraparte() {
		return codigoContraparte;
	}

	/**
	 * Asigna el valor para condigoContraparte.
	 *
	 * @param codigoContraparte El valor para codigoContraparte.
	 */
	public void setCodigoContraparte(String codigoContraparte) {
		this.codigoContraparte = codigoContraparte;
	}

	/**
	 * Regresa el valor de estrategia.
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
	 * Regresa el valor de tipoContraparte.
	 * 
	 * @return String.
	 */
	public String getTipoContraparte() {
		return tipoContraparte;
	}

	/**
	 * Asigna el valor para tipoContraparte.
	 * 
	 * @param tipoContraparte El valor para codigoContraparte.
	 */
	public void setTipoContraparte(String tipoContraparte) {
		this.tipoContraparte = tipoContraparte;
	}

	/**
	 * Regresa el valor de claveGfIxe
	 * 
	 * @return String.
	 */
	public String getClaveGfIXE() {
		return claveGfIXE;
	}

	/**
	 * Asigna el valor para claveGfIxe.
	 * 
	 * @param claveGfIXE El valor para claveGfIxe.
	 */
	public void setClaveGfIXE(String claveGfIXE) {
		this.claveGfIXE = claveGfIXE;
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
	 * @param compra_venta El valor para compra_venta.
	 */
	public void setCompra_venta(String compra_venta) {
		this.compra_venta = compra_venta;
	}

	/**
	 * Resgresa el valor de fechaLiquidacion.
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
	 * Regresa el valor de monto.
	 * 
	 * @return String.
	 */
	public String getMonto() {
		return monto;
	}

	/**
	 * Asigna el valor para monto.
	 * 
	 * @param monto El valor para monto.
	 */
	public void setMonto(String monto) {
		this.monto = monto;
	}

	/**
	 * Regresa el valor divisaDivisa.
	 * 
	 * @return String.
	 */
	public String getDealDivisaDivisa() {
		return dealDivisaDivisa;
	}

	/**
	 * Asigna el valor para divisaDivisa.
	 * 
	 * @param dealDivisaDivisa El valor para divisaDivisa.
	 */
	public void setDealDivisaDivisa(String dealDivisaDivisa) {
		this.dealDivisaDivisa = dealDivisaDivisa;
	}

	/**
	 * Folio interno del deal.
	 */
	private String folioInterno;

	/**
	 * Folio de la estrategia (Swap).
	 */
	private String folioEstrategia;

	/**
	 * Estrategia (Swap).
	 */
	private String estrategia;

	/**
	 * Clave contraparte del cliente.
	 */
	private String claveContraparte;

	/**
	 * Codigo contraparte del cliente.
	 */
	private String codigoContraparte;

	/**
	 * Tipo contraparte del cliente.
	 */
	private String tipoContraparte;

	/**
	 * Fecha de captura del deal.
	 */
	private Date fechaCaptura;

	/**
	 * Sector Banxico del cliente.
	 */
	private String sector;

	/**
	 * Monto de las compras.
	 */
	private Double comprado;

	/**
	 * Monto de las ventas
	 */
	private Double vendido;

	/**
	 * Fecha valor del deal.
	 */
	private Date fechaValor;

	/**
	 * Mes de captura del deal.
	 */
	private String mes;
	
	/**
	 * Nombre del cliente.
	 */
	private String cliente;

	/**
	 * Divisa del deal.
	 */
	private String divisa;

	/**
	 * Factor divisa acutal.
	 */
	private Double factorDivisa;

	/**
	 * Id sector Banxico del cliente.
	 */
	private Integer idSector;

	/**
	 * Imagen de encabezado del reporte.
	 */
	private InputStream image;
	
	/**
	 * Clave de IXE para Banxico.
	 */
	private String claveGfIXE;

	/**
	 * Compra o venta.
	 */
	private String compra_venta;

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
	private String monto;

	/**
	 * Deal divisa-divisa.
	 */
	private String dealDivisaDivisa;

}

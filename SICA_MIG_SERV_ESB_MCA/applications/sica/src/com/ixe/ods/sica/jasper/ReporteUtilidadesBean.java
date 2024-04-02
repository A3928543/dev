/*
 * $Id: ReporteUtilidadesBean.java,v 1.11 2008/02/22 18:25:03 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Bean que permite probar el reporte de utlidades
 *
 * @author Gerardo Corzo Herrera
 */
public class ReporteUtilidadesBean implements Serializable {

	/**
	 * @param cliente
	 * @param costoCobrado
	 * @param costoNoCobrado
	 * @param fechaCaptura
	 * @param idDeal
	 * @param folioDetalle
	 * @param monto
	 * @param producto
	 * @param sobrePrecio
	 * @param tipoCambio
	 * @param tipoOperacion
	 * @param utilidad
	 * @param divisa
	 * @param promotor
	 * @param de
	 * @param hasta
	 * @param canal
	 * @param idPromotor
	 * @param numeroCompras
	 * @param numeroVentas
	 * @param numeroTotalC
	 * @param numeroTotalV
	 * @param granTotalNumeroDeals
	 * @param montoCompras
	 * @param montoVentas
	 * @param utilidadCompras
	 * @param utilidadVentas
	 * @param utilidadTotalDivisa
	 * @param granTotalCompras
	 * @param granTotalVentas
	 * @param granTotal
	 * @param image
	 */
	public ReporteUtilidadesBean(String cliente, Double costoCobrado, Double costoNoCobrado, Date fechaCaptura, Integer idDeal, Integer folioDetalle , Double monto, String producto, 
			Double sobrePrecio, Double tipoCambio, String tipoOperacion, Double utilidad, String divisa, String promotor, Date de, Date hasta, String canal, String idPromotor,
			Integer numeroCompras, Integer numeroVentas, Integer numeroTotalC, Integer numeroTotalV, Integer granTotalNumeroDeals,
			Double montoCompras, Double montoVentas, Double utilidadCompras, Double utilidadVentas, Double utilidadTotalDivisa, Double granTotalCompras,
			Double granTotalVentas, Double granTotal, InputStream image) {
		super();
		// TODO Auto-generated constructor stub
		this.cliente = cliente;
		this.costoCobrado = costoCobrado;
		this.costoNoCobrado = costoNoCobrado;
		this.fechaCaptura = fechaCaptura;
		this.idDeal = idDeal;
		this.monto = monto;
		this.producto = producto;
		this.sobrePrecio = sobrePrecio;
		this.tipoCambio = tipoCambio;
		this.tipoOperacion = tipoOperacion;
		this.utilidad = utilidad;
		this.promotor = promotor;
		this.divisa = divisa;
		this.folioDetalle = folioDetalle;
		this.de = de;
		this.hasta = hasta;
		this.canal = canal;
		this.idPromotor = idPromotor;
		this.numeroCompras = numeroCompras;
		this.numeroVentas = numeroVentas;
		this.numeroTotalC = numeroTotalC;
		this.numeroTotalV = numeroTotalV;
		this.granTotalNumeroDeals = granTotalNumeroDeals;
		this.montoCompras = montoCompras;
		this.montoVentas= montoVentas;
		this.utilidadCompras= utilidadCompras;
		this.utilidadVentas= utilidadVentas;
		this.utilidadTotalDivisa= utilidadTotalDivisa;
		this.granTotalCompras= granTotalCompras;
		this.granTotalVentas= granTotalVentas;
		this.granTotal= granTotal;
		this.image= image;
	}
	
	public ReporteUtilidadesBean(String cliente, Double costoCobrado, Double costoNoCobrado, Date fechaCaptura, Integer idDeal, Integer folioDetalle , Double monto, String producto, 
			Double sobrePrecio, Double tipoCambio, String tipoOperacion, Double utilidad, String divisa, String promotor, Date de, Date hasta, String canal, String idPromotor,
			Integer numeroCompras, Integer numeroVentas, Integer numeroTotalC, Integer numeroTotalV, Integer granTotalNumeroDeals,
			Double montoCompras, Double montoVentas, Double utilidadCompras, Double utilidadVentas, Double utilidadTotalDivisa, Double granTotalCompras,
			Double granTotalVentas, Double granTotal, InputStream image, Boolean utilidadesIxeDirecto) {
		super();
		// TODO Auto-generated constructor stub
		this.cliente = cliente;
		this.costoCobrado = costoCobrado;
		this.costoNoCobrado = costoNoCobrado;
		this.fechaCaptura = fechaCaptura;
		this.idDeal = idDeal;
		this.monto = monto;
		this.producto = producto;
		this.sobrePrecio = sobrePrecio;
		this.tipoCambio = tipoCambio;
		this.tipoOperacion = tipoOperacion;
		this.utilidad = utilidad;
		this.promotor = promotor;
		this.divisa = divisa;
		this.folioDetalle = folioDetalle;
		this.de = de;
		this.hasta = hasta;
		this.canal = canal;
		this.idPromotor = idPromotor;
		this.numeroCompras = numeroCompras;
		this.numeroVentas = numeroVentas;
		this.numeroTotalC = numeroTotalC;
		this.numeroTotalV = numeroTotalV;
		this.granTotalNumeroDeals = granTotalNumeroDeals;
		this.montoCompras = montoCompras;
		this.montoVentas= montoVentas;
		this.utilidadCompras= utilidadCompras;
		this.utilidadVentas= utilidadVentas;
		this.utilidadTotalDivisa= utilidadTotalDivisa;
		this.granTotalCompras= granTotalCompras;
		this.granTotalVentas= granTotalVentas;
		this.granTotal= granTotal;
		this.image= image;
		this.utilidadesIxeDirecto = utilidadesIxeDirecto;
	}
	
	/**
	 * Regresa el valor de canal.
	 *	
	 * @return String.
	 */
	public String getCanal() {
		return canal;
	}

	/**
	 * Asigna el valor para canal.
	 *
	 * @param canal El valor para canal.
	 */
	public void setCanal(String canal) {
		this.canal = canal;
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
	 * Regresa el valor de costoCobrado.
	 *	
	 * @return Double.
	 */
	public Double getCostoCobrado() {
		return costoCobrado;
	}

	/**
	 * Asigna el valor para costoCobrado.
	 *
	 * @param costoCobrado El valor para costoCobrado.
	 */
	public void setCostoCobrado(Double costoCobrado) {
		this.costoCobrado = costoCobrado;
	}

	/**
	 * Regresa el valor de costoNoCobrado.
	 *	
	 * @return Double.
	 */
	public Double getCostoNoCobrado() {
		return costoNoCobrado;
	}

	/**
	 * Asigna el valor para costoNoCobrado.
	 *
	 * @param costoNoCobrado El valor para costoNoCobrado.
	 */
	public void setCostoNoCobrado(Double costoNoCobrado) {
		this.costoNoCobrado = costoNoCobrado;
	}

	/**
	 * Regresa el valor de de.
	 *	
	 * @return Date.
	 */
	public Date getDe() {
		return de;
	}

	/**
	 * Asigna el valor para de.
	 *
	 * @param de El valor para de.
	 */
	public void setDe(Date de) {
		this.de = de;
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
	 * Regresa el valor de granTotal.
	 *	
	 * @return Double.
	 */
	public Double getGranTotal() {
		return granTotal;
	}

	/**
	 * Asigna el valor para granTotal.
	 *
	 * @param granTotal El valor para granTotal.
	 */
	public void setGranTotal(Double granTotal) {
		this.granTotal = granTotal;
	}

	/**
	 * Regresa el valor de granTotalCompras.
	 *	
	 * @return Double.
	 */
	public Double getGranTotalCompras() {
		return granTotalCompras;
	}

	/**
	 * Asigna el valor para granTotalCompras.
	 *
	 * @param granTotalCompras El valor para granTotalCompras.
	 */
	public void setGranTotalCompras(Double granTotalCompras) {
		this.granTotalCompras = granTotalCompras;
	}

	/**
	 * Regresa el valor de granTotalNumeroDeals.
	 *	
	 * @return Integer.
	 */
	public Integer getGranTotalNumeroDeals() {
		return granTotalNumeroDeals;
	}

	/**
	 * Asigna el valor para granTotalNumeroDeals.
	 *
	 * @param granTotalNumeroDeals El valor para granTotalNumeroDeals.
	 */
	public void setGranTotalNumeroDeals(Integer granTotalNumeroDeals) {
		this.granTotalNumeroDeals = granTotalNumeroDeals;
	}

	/**
	 * Regresa el valor de granTotalVentas.
	 *	
	 * @return Double.
	 */
	public Double getGranTotalVentas() {
		return granTotalVentas;
	}

	/**
	 * Asigna el valor para granTotalVentas.
	 *
	 * @param granTotalVentas El valor para granTotalVentas.
	 */
	public void setGranTotalVentas(Double granTotalVentas) {
		this.granTotalVentas = granTotalVentas;
	}

	/**
	 * Regresa el valor de hasta.
	 *	
	 * @return Date.
	 */
	public Date getHasta() {
		return hasta;
	}

	/**
	 * Asigna el valor para hasta.
	 *
	 * @param hasta El valor para hasta.
	 */
	public void setHasta(Date hasta) {
		this.hasta = hasta;
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
	 * Regresa el valor de idPromotor.
	 *	
	 * @return String.
	 */
	public String getIdPromotor() {
		return idPromotor;
	}

	/**
	 * Asigna el valor para idPromotor.
	 *
	 * @param idPromotor El valor para idPromotor.
	 */
	public void setIdPromotor(String idPromotor) {
		this.idPromotor = idPromotor;
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
	 * Regresa el valor de montoCompras.
	 *	
	 * @return Double.
	 */
	public Double getMontoCompras() {
		return montoCompras;
	}

	/**
	 * Asigna el valor para montoCompras.
	 *
	 * @param montoCompras El valor para montoCompras.
	 */
	public void setMontoCompras(Double montoCompras) {
		this.montoCompras = montoCompras;
	}

	/**
	 * Regresa el valor de montoVentas.
	 *	
	 * @return Double.
	 */
	public Double getMontoVentas() {
		return montoVentas;
	}

	/**
	 * Asigna el valor para montoVentas.
	 *
	 * @param montoVentas El valor para montoVentas.
	 */
	public void setMontoVentas(Double montoVentas) {
		this.montoVentas = montoVentas;
	}

	/**
	 * Regresa el valor de numeroCompras.
	 *	
	 * @return Integer.
	 */
	public Integer getNumeroCompras() {
		return numeroCompras;
	}

	/**
	 * Asigna el valor para numeroCompras.
	 *
	 * @param numeroCompras El valor para numeroCompras.
	 */
	public void setNumeroCompras(Integer numeroCompras) {
		this.numeroCompras = numeroCompras;
	}

	/**
	 * Regresa el valor de numeroTotalC.
	 *	
	 * @return Integer.
	 */
	public Integer getNumeroTotalC() {
		return numeroTotalC;
	}

	/**
	 * Asigna el valor para numeroTotalC.
	 *
	 * @param numeroTotalC El valor para numeroTotalC.
	 */
	public void setNumeroTotalC(Integer numeroTotalC) {
		this.numeroTotalC = numeroTotalC;
	}

	/**
	 * Regresa el valor de numeroTotalV.
	 *	
	 * @return Integer.
	 */
	public Integer getNumeroTotalV() {
		return numeroTotalV;
	}

	/**
	 * Asigna el valor para numeroTotalV.
	 *
	 * @param numeroTotalV El valor para numeroTotalV.
	 */
	public void setNumeroTotalV(Integer numeroTotalV) {
		this.numeroTotalV = numeroTotalV;
	}

	/**
	 * Regresa el valor de numeroVentas.
	 *	
	 * @return Integer.
	 */
	public Integer getNumeroVentas() {
		return numeroVentas;
	}

	/**
	 * Asigna el valor para numeroVentas.
	 *
	 * @param numeroVentas El valor para numeroVentas.
	 */
	public void setNumeroVentas(Integer numeroVentas) {
		this.numeroVentas = numeroVentas;
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
	 * Regresa el valor de sobrePrecio.
	 *	
	 * @return Double.
	 */
	public Double getSobrePrecio() {
		return sobrePrecio;
	}

	/**
	 * Asigna el valor para sobrePrecio.
	 *
	 * @param sobrePrecio El valor para sobrePrecio.
	 */
	public void setSobrePrecio(Double sobrePrecio) {
		this.sobrePrecio = sobrePrecio;
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
	 * Regresa el valor de tipoOperacion.
	 *	
	 * @return String.
	 */
	public String getTipoOperacion() {
		return tipoOperacion;
	}

	/**
	 * Asigna el valor para tipoOperacion.
	 *
	 * @param tipoOperacion El valor para tipoOperacion.
	 */
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	/**
	 * Regresa el valor de utilidad.
	 *	
	 * @return Double.
	 */
	public Double getUtilidad() {
		return utilidad;
	}

	/**
	 * Asigna el valor para utilidad.
	 *
	 * @param utilidad El valor para utilidad.
	 */
	public void setUtilidad(Double utilidad) {
		this.utilidad = utilidad;
	}

	/**
	 * Regresa el valor de utilidadCompras.
	 *	
	 * @return Double.
	 */
	public Double getUtilidadCompras() {
		return utilidadCompras;
	}

	/**
	 * Asigna el valor para utilidadCompras.
	 *
	 * @param utilidadCompras El valor para utilidadCompras.
	 */
	public void setUtilidadCompras(Double utilidadCompras) {
		this.utilidadCompras = utilidadCompras;
	}

	/**
	 * Regresa el valor de utilidadTotalDivisa.
	 *	
	 * @return Double.
	 */
	public Double getUtilidadTotalDivisa() {
		return utilidadTotalDivisa;
	}

	/**
	 * Asigna el valor para utilidadTotalDivisa.
	 *
	 * @param utilidadTotalDivisa El valor para utilidadTotalDivisa.
	 */
	public void setUtilidadTotalDivisa(Double utilidadTotalDivisa) {
		this.utilidadTotalDivisa = utilidadTotalDivisa;
	}

	/**
	 * Regresa el valor de utilidadVentas.
	 *	
	 * @return Double.
	 */
	public Double getUtilidadVentas() {
		return utilidadVentas;
	}

	/**
	 * Asigna el valor para utilidadVentas.
	 *
	 * @param utilidadVentas El valor para utilidadVentas.
	 */
	public void setUtilidadVentas(Double utilidadVentas) {
		this.utilidadVentas = utilidadVentas;
	}
	
	public Boolean getUtilidadesIxeDirecto() {
		return utilidadesIxeDirecto;
	}

	public void setUtilidadesIxeDirecto(Boolean utilidadesIxeDirecto) {
		this.utilidadesIxeDirecto = utilidadesIxeDirecto;
	}
	
	/**
	 * Id del Deal
	 */
	private Integer idDeal;
	
	/**
	 * La fecha de captura del Deal
	 */
	private Date fechaCaptura;
	
	/**
	 * el tipo de operaci&oacute;n
	 */
	private String tipoOperacion;
	
	/**
	 * Nombre del producto
	 */
	private String producto;
	
	/**
	 * Nombre del cliente
	 */
	private String cliente;
	
	/**
	 * Monto del detalle del Deal
	 */
	private Double monto;
	
	/**
	 * tipo de Cambio del detalle del Deal
	 */
	private Double tipoCambio;
	
	/**
	 * Sobreprecio del detalle del Deal
	 */
	private Double sobrePrecio;
	
	/**
	 * Utilidad del detalle del Deal
	 */
	private Double utilidad;
	
	/**
	 * Sin uso
	 */
	private Double costoCobrado;
	
	/**
	 * Sin uso
	 */
	private Double costoNoCobrado;
	
	/**
	 * foilo del detalle del Deal
	 */
	private Integer folioDetalle;
	
	/**
	 * Nombre del promotor
	 */
	private String promotor;
	
	/**
	 * Divisa del detalle del Deal
	 */
	private String divisa;
	
	/**
	 * Fecha del comienzo del rango de fechas
	 */
	private Date de;
	
	/**
	 * Fecha del final del rango de fechas
	 */
	private Date hasta;
	
	/**
	 * El nombre del canal
	 */
	private String canal;
	
	/**
	 * Id del promotor
	 */
	private String idPromotor;
	
	/**
	 * N&uacute;mero de detalles de compras
	 */
	private Integer numeroCompras;
	
	/**
	 * N&uacute;mero de detalles de ventas
	 */
	private Integer numeroVentas;
	
	/**
	 * N&uacute;mero total de deals de ventas
	 * por promotor
	 */
	private Integer numeroTotalV;
	
	/**
	 * N&uacute;mero total de deals de compras 
	 * por promotor
	 */
	private Integer numeroTotalC;
	
	/**
	 * N&uacute;mero total de deals
	 */
	private Integer granTotalNumeroDeals;
	
	/**
	 * Monto de compras por divisa 
	 */
	private Double montoCompras;
	
	/**
	 * Monto de ventas por divisa 
	 */
	private Double montoVentas;
	
	/**
	 * Utilidad de compras por divisa 
	 */
	private Double utilidadCompras;
	
	/**
	 * Utilidad de ventas por divisa 
	 */
	private Double utilidadVentas;
	
	/**
	 * Utilidad total por divisa
	 */
	private Double utilidadTotalDivisa;
	
	/**
	 * N&uacute;mero total de deals de compras
	 * por rango de fechas
	 */
	private Double granTotalCompras;
	
	/**
	 * N&uacute;mero total de deals de ventas
	 * por rango de fechas
	 */
	private Double granTotalVentas;
	
	/**
	 * Total de Utilidades por rango de fechas
	 */
	private Double granTotal;
	
	/**
	 * Logo de IXE
	 */
	private InputStream image;
	
	/**
	 * Define si el reporte de utilidades es para Ixe Directo.
	 */
	private Boolean utilidadesIxeDirecto;

	
}

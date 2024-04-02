/*
 * $Id: RepUtMayoreoContableBean.java,v 1.11 2008/02/22 18:25:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Permite modificar el Reporte de Utilidades de Mayoreo Contable.
 *
 * @author Edgar Ivan Leija
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:04 $
 */
public class RepUtMayoreoContableBean implements Serializable {

	/**
	 * Constructor que permite inicializar los datos del Reporte de Utilidades de Mayoreo Contable.
	 * 
	 * @param image la imagen que se despliega en el reporte
	 * @param idMesaCambio el tipo de Deal
	 * @param tipoDeal el tipo de Deal
	 * @param divisaDescripcion el nombre de la divisa
	 * @param utilidadCompras el valor de las utilidad en las compras de cierta divisa
	 * @param utilidadVentas el valor de las utilidades en la ventas de cierta divisa
	 * @param utilidadPeriodo la suma de las utilidad por periodo de una divisa, compra y venta
	 * @param montoCompras el valor total de la compras
	 * @param montoVentas el valor total de las ventas
	 * @param tipoDeCambioCompra el tipo de cambio ponderado para las compras
	 * @param tipoDeCambioVenta el tipo de cambio para las ventas
	 * @param fechaReporte la fecha de consulta del reporte
	 *
	 */
	public RepUtMayoreoContableBean(InputStream image, String idMesaCambio, String tipoDeal, String divisaDescripcion, Double utilidadGlobal, Double utilidadMesa, Double utilidadCompras, Double utilidadVentas,
			Double utilidadPeriodo, Double montoCompras, Double montoVentas, Double tipoDeCambioCompra, Double tipoDeCambioVenta, Date fechaReporte) {
		super();
		this.image = image;
		this.idMesaCambio = idMesaCambio;
		this.tipoDeal = tipoDeal;
		this.divisaDescripcion = divisaDescripcion;
		this.utilidadCompras = utilidadCompras;
		this.utilidadVentas = utilidadVentas;
		this.utilidadPeriodo = utilidadPeriodo;
		this.utilidadGlobal = utilidadGlobal;
		this.utilidadMesa=  utilidadMesa;
		this.montoCompras = montoCompras;
		this.montoVentas = montoVentas;
		this.tipoDeCambioCompra = tipoDeCambioCompra;
		this.tipoDeCambioVenta = tipoDeCambioVenta;
		this.fechaReporte = fechaReporte;
	}

	/**
	 * Regresa una referencia para desplegar la im&aacute;gen en el Reporte
	 * @return el <code>InputStream</code> hacia la im&aacute;gen
	 */
	public InputStream getImage() {
		return image;
	}
	
	/**
	 * Fija la referencia hacia la im&aacute;gen que va a desplegar el reporte.
	 * @param image el <code>InputStream</code> de la im&aacute;n
	 */
	public void setImage(InputStream image) {
		this.image = image;
	}
	
	/**
     * Regresa el tipo de idMesaCambio
     * @return <code>String</code> el tipo de Deal
     */
	public String getIdMesaCambio() {
		return idMesaCambio;
	}
	
	/**
	 * Fija el tipo de idMesaCambio.
	 * @param idMesaCambio el tipo de idMesaCambio.
	 */
	public void setIdMesaCambio(String idMesaCambio) {
		this.idMesaCambio = idMesaCambio;
	}
	
	/**
     * Regresa el tipo de <code>Deal</code>.
     * @return <code>String</code> el tipo de Deal
     */
	public String getTipoDeal() {
		return tipoDeal;
	}
	
	/**
	 * Fija el tipo de <code>Deal</code>
	 * @param tipoDeal el tipo de <code>Deal</code>.
	 */
	public void setTipoDeal(String tipoDeal) {
		this.tipoDeal = tipoDeal;
	}
    
	/**
     * Regresa el nombre de la Divisa
     * @return <code>String</code> nombre de la divisa
     */
	public String getDivisaDescripcion() {
		return divisaDescripcion;
	}
	
	/**
	 * Fija el nombre de la Divisa
	 * @param divisaDescripcion el <code>String</code> que describe el nombre de la Divisa
	 */
	public void setDivisaDescripcion(String divisaDescripcion) {
		this.divisaDescripcion = divisaDescripcion;
	}
	
	/**
	 * Regresa el el valor de las Utilidad en las compras.
	 * @return <code>Double</code> el valor de las utilidades
	 */
	public Double getUtilidadCompras() {
		return utilidadCompras;
	}

	/**
	 * Fija el valor de las Utilidades.
	 * @param utilidadCompras el <code>Double</code> que representa el total de las Utilidades.
	 */
	public void setUtilidadCompras(Double utilidadCompras) {
		this.utilidadCompras = utilidadCompras;
	}
	
	/**
	 * Regresa el el valor de las Utilidad en las compras.
	 * @return <code>Double</code> el valor de las utilidades
	 */
	public Double getUtilidadGlobal() {
		return utilidadGlobal;
	}

	/**
	 * Fija el valor de las Utilidades.
	 * @param utilidadCompras el <code>Double</code> que representa el total de las Utilidades.
	 */
	public void setUtilidadGlobal(Double utilidadGlobal) {
		this.utilidadGlobal = utilidadGlobal;
	}
	
	/**
	 * Regresa el el valor de las Utilidad en las compras.
	 * @return <code>Double</code> el valor de las utilidades
	 */
	public Double getUtilidadMesa() {
		return utilidadMesa;
	}

	/**
	 * Fija el valor de las Utilidades.
	 * @param utilidadCompras el <code>Double</code> que representa el total de las Utilidades.
	 */
	public void setUtilidadMesa(Double utilidadMesa) {
		this.utilidadMesa = utilidadMesa;
	}
	
	
	
	/**
	 * Regresa el el valor de las Utilidad en las ventas.
	 * @return <code>Double</code> el valor de las utilidades
	 */
	public Double getUtilidadVentas() {
		return utilidadVentas;
	}

	/**
	 * Fija el valor de las Utilidades.
	 * @param utilidadVentas el <code>Double</code> que representa el total de las Utilidades.
	 */
	public void setUtilidadVentas(Double utilidadVentas) {
		this.utilidadVentas = utilidadVentas;
	}
	
	/**
	 * Regresa el el valor total de las Utilidades.
	 * @return <code>Double</code> el valor de las utilidades
	 */
	public Double getUtilidadPeriodo() {
		return utilidadPeriodo;
	}

	/**
	 * Fija el valor de las Utilidades.
	 * @param utilidadPeriodo el <code>Double</code> que representa el total de las Utilidades.
	 */
	public void setUtilidadPeriodo(Double utilidadPeriodo) {
		this.utilidadPeriodo = utilidadPeriodo;
	}
	
	/**
	 * Regresa el valor total del monto en las compras
	 * @return <code>Double</code> el monto total.
	 */
	public Double getMontoCompras() {
		return montoCompras;
	}

	/**
	 * Fija el valor total para la compras
	 * @param montoCompras el <code>Double</code> que representa el valor de las compras.
	 */
	public void setMontoCompras(Double montoCompras) {
		this.montoCompras = montoCompras;
	}
	
	/**
	 * Regresa el valor total del monto en las ventas
	 * @return <code>Double</code> el monto total.
	 */
	public Double getMontoVentas() {
		return montoVentas;
	}

	/**
	 * Fija el valor total para la ventas.
	 * @param montoVentas el <code>Double</code> que representa el valor de las compras.
	 */
	public void setMontoVentas(Double montoVentas) {
		this.montoVentas = montoVentas;
	}

	/**
	 * Regresa el valor del tipo de cambio para compras.
	 * @return <code>Double</code> el tipo de cambio para las compras
	 */
	public Double getTipoDeCambioCompra() {
		return tipoDeCambioCompra;
	}

	/**
	 * Fija el valor total para el tipo de cambio en lac compras
	 * @param tipoDeCambioCompra el <code>Double</code> que representa el valor del tipo de cambio
	 */
	public void setTipoDeCambioCompra(Double tipoDeCambioCompra) {
		this.tipoDeCambioCompra = tipoDeCambioCompra;
	}
	
	/**
	 * Regresa el valor del tipo de cambio para ventas
	 * @return <code>Double</code> el tipo de cambio para las ventas
	 */
	public Double getTipoDeCambioVenta() {
		return tipoDeCambioVenta;
	}

	/**
	 * Fija el valor total para el tipo de cambio en las ventas
	 * @param tipoDeCambioVenta el <code>Double</code> que representa el valor del tipo de cambio
	 */
	public void setTipoDeCambioVenta(Double tipoDeCambioVenta) {
		this.tipoDeCambioVenta = tipoDeCambioVenta;
	}
	
	/**
	 * Regresa el el valor de las Utilidad en las compras.
	 * @return <code>Double</code> el valor de las utilidades
	 */
	public Double getUtilidadComprasTotal() {
		return utilidadComprasTotal;
	}

	/**
	 * Fija el valor de las Utilidades.
	 * @param utilidadComprasTotal el <code>Double</code> que representa el total de las Utilidades.
	 */
	public void setUtilidadComprasTotal(Double utilidadComprasTotal) {
		this.utilidadComprasTotal = utilidadComprasTotal;
	}
	
	/**
	 * Regresa el el valor de las Utilidad en las ventas.
	 * @return <code>Double</code> el valor de las utilidades
	 */
	public Double getUtilidadVentasTotal() {
		return utilidadVentasTotal;
	}

	/**
	 * Fija el valor de las Utilidades.
	 * @param utilidadVentasTotal el <code>Double</code> que representa el total de las Utilidades.
	 */
	public void setUtilidadVentasTotal(Double utilidadVentasTotal) {
		this.utilidadVentasTotal = utilidadVentasTotal;
	}
	
	/**
	 * Regresa el el valor total de las Utilidades.
	 * @return <code>Double</code> el valor de las utilidades
	 */
	public Double getUtilidadPeriodoTotal() {
		return utilidadPeriodoTotal;
	}

	/**
	 * Fija el valor de las Utilidades.
	 * @param utilidadPeriodoTotal el <code>Double</code> que representa el total de las Utilidades.
	 */
	public void setUtilidadPeriodoTotal(Double utilidadPeriodoTotal) {
		this.utilidadPeriodoTotal = utilidadPeriodoTotal;
	}
	
	/**
	 * Regresa el valor total del monto en las compras
	 * @return <code>Double</code> el monto total.
	 */
	public Double getMontoComprasTotal() {
		return montoComprasTotal;
	}

	/**
	 * Fija el valor total para la compras
	 * @param montoComprasTotal el <code>Double</code> que representa el valor de las compras.
	 */
	public void setMontoComprasTotal(Double montoComprasTotal) {
		this.montoComprasTotal = montoComprasTotal;
	}
	
	/**
	 * Regresa el valor total del monto en las ventas
	 * @return <code>Double</code> el monto total.
	 */
	public Double getMontoVentasTotal() {
		return montoVentasTotal;
	}

	/**
	 * Fija el valor total para la ventas.
	 * @param montoVentasTotal el <code>Double</code> que representa el valor de las compras.
	 */
	public void setMontoVentasTotal(Double montoVentasTotal) {
		this.montoVentasTotal = montoVentasTotal;
	}

	/**
	 * Regresa el valor del tipo de cambio para compras.
	 * @return <code>Double</code> el tipo de cambio para las compras
	 */
	public Double getTipoDeCambioCompraTotal() {
		return tipoDeCambioCompraTotal;
	}

	/**
	 * Fija el valor total para el tipo de cambio en lac compras
	 * @param tipoDeCambioCompraTotal el <code>Double</code> que representa el valor del tipo de cambio
	 */
	public void setTipoDeCambioCompraTotal(Double tipoDeCambioCompraTotal) {
		this.tipoDeCambioCompraTotal = tipoDeCambioCompraTotal;
	}
	
	/**
	 * Regresa el valor del tipo de cambio para ventas
	 * @return <code>Double</code> el tipo de cambio para las ventas
	 */
	public Double getTipoDeCambioVentaTotal() {
		return tipoDeCambioVentaTotal;
	}

	/**
	 * Fija el valor total para el tipo de cambio en las ventas
	 * @param tipoDeCambioVentaTotal el <code>Double</code> que representa el valor del tipo de cambio
	 */
	public void setTipoDeCambioVentaTotal(Double tipoDeCambioVentaTotal) {
		this.tipoDeCambioVentaTotal = tipoDeCambioVentaTotal;
	}
	
	/**
	 * Regresa el el valor de las Utilidad en las compras.
	 * @return <code>Double</code> el valor de las utilidades
	 */
	public Double getUtilidadComprasNormal() {
		return utilidadComprasNormal;
	}

	/**
	 * Fija el valor de las Utilidades.
	 * @param utilidadComprasNormal el <code>Double</code> que representa el total de las Utilidades.
	 */
	public void setUtilidadComprasNormal(Double utilidadComprasNormal) {
		this.utilidadComprasNormal = utilidadComprasNormal;
	}
	
	/**
	 * Regresa el el valor de las Utilidad en las ventas.
	 * @return <code>Double</code> el valor de las utilidades
	 */
	public Double getUtilidadVentasNormal() {
		return utilidadVentasNormal;
	}

	/**
	 * Fija el valor de las Utilidades.
	 * @param utilidadVentasNormal el <code>Double</code> que representa el total de las Utilidades.
	 */
	public void setUtilidadVentasNormal(Double utilidadVentasNormal) {
		this.utilidadVentasNormal = utilidadVentasNormal;
	}
	
	/**
	 * Regresa el el valor total de las Utilidades.
	 * @return <code>Double</code> el valor de las utilidades
	 */
	public Double getUtilidadPeriodoNormal() {
		return utilidadPeriodoNormal;
	}

	/**
	 * Fija el valor de las Utilidades.
	 * @param utilidadPeriodoNormal el <code>Double</code> que representa el total de las Utilidades.
	 */
	public void setUtilidadPeriodoNormal(Double utilidadPeriodoNormal) {
		this.utilidadPeriodoNormal = utilidadPeriodoNormal;
	}
	
	/**
	 * Regresa el valor total del monto en las compras
	 * @return <code>Double</code> el monto total.
	 */
	public Double getMontoComprasNormal() {
		return montoComprasNormal;
	}

	/**
	 * Fija el valor total para la compras
	 * @param montoComprasNormal el <code>Double</code> que representa el valor de las compras.
	 */
	public void setMontoComprasNormal(Double montoComprasNormal) {
		this.montoComprasNormal = montoComprasNormal;
	}
	
	/**
	 * Regresa el valor total del monto en las ventas
	 * @return <code>Double</code> el monto total.
	 */
	public Double getMontoVentasNormal() {
		return montoVentasNormal;
	}

	/**
	 * Fija el valor total para la ventas.
	 * @param montoVentasNormal el <code>Double</code> que representa el valor de las compras.
	 */
	public void setMontoVentasNormal(Double montoVentasNormal) {
		this.montoVentasNormal = montoVentasNormal;
	}

	/**
	 * Regresa el valor del tipo de cambio para compras.
	 * @return <code>Double</code> el tipo de cambio para las compras
	 */
	public Double getTipoDeCambioCompraNormal() {
		return tipoDeCambioCompraNormal;
	}

	/**
	 * Fija el valor total para el tipo de cambio en lac compras
	 * @param tipoDeCambioCompraNormal el <code>Double</code> que representa el valor del tipo de cambio
	 */
	public void setTipoDeCambioCompraNormal(Double tipoDeCambioCompraNormal) {
		this.tipoDeCambioCompraNormal = tipoDeCambioCompraNormal;
	}
	
	/**
	 * Regresa el valor del tipo de cambio para ventas
	 * @return <code>Double</code> el tipo de cambio para las ventas
	 */
	public Double getTipoDeCambioVentaNormal() {
		return tipoDeCambioVentaNormal;
	}

	/**
	 * Fija el valor total para el tipo de cambio en las ventas
	 * @param tipoDeCambioVentaNormal el <code>Double</code> que representa el valor del tipo de cambio
	 */
	public void setTipoDeCambioVentaNormal(Double tipoDeCambioVentaNormal) {
		this.tipoDeCambioVentaNormal = tipoDeCambioVentaNormal;
	}
	
	/**
	 * Regresa el el valor de las Utilidad en las compras.
	 * @return <code>Double</code> el valor de las utilidades
	 */
	public Double getUtilidadComprasSucursal() {
		return utilidadComprasSucursal;
	}

	/**
	 * Fija el valor de las Utilidades.
	 * @param utilidadComprasSucursal el <code>Double</code> que representa el total de las Utilidades.
	 */
	public void setUtilidadComprasSucursal(Double utilidadComprasSucursal) {
		this.utilidadComprasSucursal = utilidadComprasSucursal;
	}
	
	/**
	 * Regresa el el valor de las Utilidad en las ventas.
	 * @return <code>Double</code> el valor de las utilidades
	 */
	public Double getUtilidadVentasSucursal() {
		return utilidadVentasSucursal;
	}

	/**
	 * Fija el valor de las Utilidades.
	 * @param utilidadVentasSucursal el <code>Double</code> que representa el total de las Utilidades.
	 */
	public void setUtilidadVentasSucursal(Double utilidadVentasSucursal) {
		this.utilidadVentasSucursal = utilidadVentasSucursal;
	}
	
	/**
	 * Regresa el el valor total de las Utilidades.
	 * @return <code>Double</code> el valor de las utilidades
	 */
	public Double getUtilidadPeriodoSucursal() {
		return utilidadPeriodoSucursal;
	}

	/**
	 * Fija el valor de las Utilidades.
	 * @param utilidadPeriodoSucursal el <code>Double</code> que representa el total de las Utilidades.
	 */
	public void setUtilidadPeriodoSucursal(Double utilidadPeriodoSucursal) {
		this.utilidadPeriodoSucursal = utilidadPeriodoSucursal;
	}
	
	/**
	 * Regresa el valor total del monto en las compras
	 * @return <code>Double</code> el monto total.
	 */
	public Double getMontoComprasSucursal() {
		return montoComprasSucursal;
	}

	/**
	 * Fija el valor total para la compras
	 * @param montoComprasSucursal el <code>Double</code> que representa el valor de las compras.
	 */
	public void setMontoComprasSucursal(Double montoComprasSucursal) {
		this.montoComprasSucursal = montoComprasSucursal;
	}
	
	/**
	 * Regresa el valor total del monto en las ventas
	 * @return <code>Double</code> el monto total.
	 */
	public Double getMontoVentasSucursal() {
		return montoVentasSucursal;
	}

	/**
	 * Fija el valor total para la ventas.
	 * @param montoVentasSucursal el <code>Double</code> que representa el valor de las compras.
	 */
	public void setMontoVentasSucursal(Double montoVentasSucursal) {
		this.montoVentasSucursal = montoVentasSucursal;
	}

	/**
	 * Regresa el valor del tipo de cambio para compras.
	 * @return <code>Double</code> el tipo de cambio para las compras
	 */
	public Double getTipoDeCambioCompraSucursal() {
		return tipoDeCambioCompraSucursal;
	}

	/**
	 * Fija el valor total para el tipo de cambio en lac compras
	 * @param tipoDeCambioCompraSucursal el <code>Double</code> que representa el valor del tipo de cambio
	 */
	public void setTipoDeCambioCompraSucursal(Double tipoDeCambioCompraSucursal) {
		this.tipoDeCambioCompraSucursal = tipoDeCambioCompraSucursal;
	}
	
	/**
	 * Regresa el valor del tipo de cambio para ventas
	 * @return <code>Double</code> el tipo de cambio para las ventas
	 */
	public Double getTipoDeCambioVentaSucursal() {
		return tipoDeCambioVentaSucursal;
	}

	/**
	 * Fija el valor total para el tipo de cambio en las ventas
	 * @param tipoDeCambioVentaSucursal el <code>Double</code> que representa el valor del tipo de cambio
	 */
	public void setTipoDeCambioVentaSucursal(Double tipoDeCambioVentaSucursal) {
		this.tipoDeCambioVentaSucursal = tipoDeCambioVentaSucursal;
	}

	/**
	 * Regresa la Fecha de consulta del Reporte
	 * @return <code>Date</code> la fecha de consulta
	 */
	public Date getFechaReporte() {
		return fechaReporte;
	}

	/**
	 * Fija la fecha de consulta del Reporte.
	 * @param fechaReporte la fecha de Consulta
	 */
	public void setFechaReporte(Date fechaReporte) {
		this.fechaReporte = fechaReporte;
	}
	
	/**
	 * Logo de IXE.
	 */
	private InputStream image;
	
	/**
	 * Nombre de la Divisa.
	 */
	private String divisaDescripcion;

	/**
	 * Tipo de Deal.
	 */
	private String tipoDeal;
	
	/**
	 * el id de la Mesa.
	 */
	private String idMesaCambio;
	
	/**
	 * Utilidades de Compras.
	 */
	private Double utilidadCompras;
	
	/**
	 * Utilidades de Compras.
	 */
	private Double utilidadGlobal;
	
	/**
	 * Utilidades de Compras.
	 */
	private Double utilidadMesa;

	/**
	 * Utilidades de ventas.
	 */
	private Double utilidadVentas;

	/**
	 * Suma de utilidades por perido.
	 */
	private Double utilidadPeriodo;

	/**
	 * Monto de las compras.
	 */
	private Double montoCompras;

	/**
	 * Tipo de Cambio de compras.
	 */
	private Double tipoDeCambioCompra;
	
	/**
	 * Monto de las ventas.
	 */
	private Double montoVentas;

	/**
	 * Tipo de Cambio de ventas.
	 */
	private Double tipoDeCambioVenta;

	/**
	 * Fecha del Reporte.
	 */
	private Date fechaReporte;
	
	//Variables Totales
	
	/**
	 * Utilidades de Compras.
	 */
	private Double utilidadComprasTotal;

	/**
	 * Utilidades de ventas.
	 */
	private Double utilidadVentasTotal;

	/**
	 * Suma de utilidades por periodo.
	 */
	private Double utilidadPeriodoTotal;

	/**
	 * Monto de las compras.
	 */
	private Double montoComprasTotal;

	/**
	 * Tipo de Cambio de compras.
	 */
	private Double tipoDeCambioCompraTotal;
	
	/**
	 * Monto de las ventas.
	 */
	private Double montoVentasTotal;

	/**
	 * Tipo de Cambio de ventas.
	 */
	private Double tipoDeCambioVentaTotal;
	
	//variables d normales
	
	/**
	 * Utilidades de Compras.
	 */
	private Double utilidadComprasNormal;

	/**
	 * Utilidades de ventas.
	 */
	private Double utilidadVentasNormal;

	/**
	 * Suma de utilidades por periodo.
	 */
	private Double utilidadPeriodoNormal;

	/**
	 * Monto de las compras.
	 */
	private Double montoComprasNormal;

	/**
	 * Tipo de Cambio de compras.
	 */
	private Double tipoDeCambioCompraNormal;
	
	/**
	 * Monto de las ventas.
	 */
	private Double montoVentasNormal;

	/**
	 * Tipo de Cambio de ventas.
	 */
	private Double tipoDeCambioVentaNormal;
	
	//variables d sucursales
	
	/**
	 * Utilidades de Compras.
	 */
	private Double utilidadComprasSucursal;

	/**
	 * Utilidades de ventas.
	 */
	private Double utilidadVentasSucursal;

	/**
	 * Suma de utilidades por periodo.
	 */
	private Double utilidadPeriodoSucursal;

	/**
	 * Monto de las compras.
	 */
	private Double montoComprasSucursal;

	/**
	 * Tipo de Cambio de compras.
	 */
	private Double tipoDeCambioCompraSucursal;
	
	/**
	 * Monto de las ventas.
	 */
	private Double montoVentasSucursal;

	/**
	 * Tipo de Cambio de ventas.
	 */
	private Double tipoDeCambioVentaSucursal;

}

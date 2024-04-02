/*
 * $Id: ReporteConciliacionDiariaBean.java,v 1.10 2008/02/22 18:25:03 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Bean para generar el reporte de Conciliacion Diaria 
 * 
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:03 $
 */
public class ReporteConciliacionDiariaBean implements Serializable {
	
	/**
	 * Constructor del bean para el Reporte de Conciliacion Diaria.
	 *  
	 * @param fechaOperacion La fecha de operaci&oacute;n del deal.
	 * @param divisa La divisa del deal.
	 * @param mesa La mesa de donde proviene el deal.
	 * @param divisaReferencia La divisa de referencia.S
	 * @param operacionesCash Las operaciones realizadas en fecha valor CASH.
	 * @param comprasCash Las compras en fecha valor CASH
	 * @param ventasCash Las ventas en fecha valor CASH
	 * @param posicionCash El valor de la posicion en para fecha valor CASH.
	 * @param operacionesTom Las operaciones realizadas en fecha valor TOM.
	 * @param comprasTom Las compras en fecha valor TOM
	 * @param ventasTom Las ventas en fecha valor TOM
	 * @param posicionTom El valor de la posicion en para fecha valor TOM.
	 * @param operacionesSpot Las operaciones realizadas en fecha valor SPOT.
	 * @param comprasSpot Las compras en fecha valor SPOT
	 * @param ventasSpot Las ventas en fecha valor SPOT
	 * @param posicionSpot El valor de la posicion en para fecha valor SPOT.
	 * @param operaciones72Hr Las operaciones realizadas en fecha valor 72HR.
	 * @param compras72Hr Las compras en fecha valor 72HR
	 * @param ventas72Hr Las ventas en fecha valor 72HR
	 * @param posicion72Hr El valor de la posicion en para fecha valor 72HR.
	 * @param operacionesVFut Las operaciones realizadas en fecha valor VFUT.
	 * @param comprasVFut Las compras en fecha valor VFUT
	 * @param ventasVFut Las ventas en fecha valor VFUT
	 * @param posicionVFut El valor de la posicion en para fecha valor VFUT.
	 * @param fechaPosicionFinalAnterior La fecha de la posici&oacute;n final del d&iacute;a anterior.
	 * @param montoPosicionFinalAnterior El monto de la posici&oacute;n final del d&iacute;a anterior.
	 * @param vencimientosTom Los vencimientos para la fecha valor TOM.
	 * @param fechaVencimientoTom La fecha de vencimiento para operaciones TOM.
	 * @param montoComprasVencimientosTom El monto de compras de vencimientos  para operaciones TOM.
	 * @param montoVentasVencimientosTom El monto de ventas de vencimientos para operaciones TOM.
	 * @param montoPosicionVencimientosTom El monto de la posici&oacute;n para vencimientos de operaciones TOM.
	 * @param vencimientosSpot Los vencimientos para operaciones SPOT.
	 * @param fechaVencimientoSpot La fecha de vencimiento para operaciones SPOT.
	 * @param montoComprasVencimientosSpot El monto de compras de vencimientos para operaciones SPOT.
	 * @param montoVentasVencimientosSpot El monto de ventas de vencimientos para operaciones SPOT.
	 * @param montoPosicionVencimientosSpot El monto de la posici&oacute;n para vencimientos de operaciones SPOT.
	 * @param vencimientos72Hr Los vencimientos para operaciones 72HR.
	 * @param fechaVencimiento72Hr La fecha de vencimiento de operaciones 72HR
	 * @param montoComprasVencimientos72Hr El monto de compras de vencimientos para operaciones 72HR.
	 * @param montoVentasVencimientos72Hr El monto de ventas  de vencimientos para operaciones 72HR.
	 * @param montoPosicionVencimientos72Hr El monto de la posici&oacute;n para vencimientos de operaciones 72HR.
	 * @param vencimientosVFut Los vencimientos para operaciones VFUT. 
	 * @param fechaVencimientoVFut La fecha de vencimiento para operaciones VFUT.
	 * @param montoComprasVencimientosVFut El monto de compras de vencimientos para operaciones VFUT.
	 * @param montoVentasVencimientosVFut El monto de ventas de vencimientos para operaciones VFUT.
	 * @param montoPosicionVencimientosVFut El monto de la posici&oacute;n para vencimientos de operaciones VFUT.
	 * @param posicionHoy El monto de la posici&oacute;n para el d&iacute;a de hoy.
	 * @param montoComprasPosicionCash El monto de compras para operaciones CASH.
	 * @param montoVentasPosicionCash El monto de ventas para operaciones CASH.
	 * @param montoPosicionCash El monto de la posici&oacute;n para operaciones CASH.
	 * @param montoPosicionDiaSiguiente El monto de la posici&oacute;n para el d&iacute;a siguiente.
	 * @param imagen La imagen de encabezado del reporte
	 */
	public ReporteConciliacionDiariaBean(Date fechaOperacion, String divisa, String mesa, String divisaReferencia, 
			String operacionesCash, Double comprasCash, Double ventasCash, Double posicionCash,
			String operacionesTom, Double comprasTom, Double ventasTom, Double posicionTom,
			String operacionesSpot, Double comprasSpot, Double ventasSpot, Double posicionSpot,
			String operaciones72Hr, Double compras72Hr, Double ventas72Hr, Double posicion72Hr,
			String operacionesVFut, Double comprasVFut, Double ventasVFut, Double posicionVFut,
			Date fechaPosicionFinalAnterior, Double montoPosicionFinalAnterior,
			String vencimientosTom, Date fechaVencimientoTom, Double montoComprasVencimientosTom, Double montoVentasVencimientosTom, Double montoPosicionVencimientosTom,  
			String vencimientosSpot, Date fechaVencimientoSpot, Double montoComprasVencimientosSpot, Double montoVentasVencimientosSpot, Double montoPosicionVencimientosSpot,
			String vencimientos72Hr, Date fechaVencimiento72Hr, Double montoComprasVencimientos72Hr, Double montoVentasVencimientos72Hr, Double montoPosicionVencimientos72Hr,
			String vencimientosVFut, Date fechaVencimientoVFut, Double montoComprasVencimientosVFut, Double montoVentasVencimientosVFut, Double montoPosicionVencimientosVFut,
			String posicionHoy, Double montoComprasPosicionCash, Double montoVentasPosicionCash,  Double montoPosicionCash, Double montoPosicionDiaSiguiente,
			InputStream imagen) {
		super();
		this.fechaOperacion = fechaOperacion;
		this.divisa = divisa;
		this.mesa = mesa;
		this.divisaReferencia = divisaReferencia;
		this.operacionesCash = operacionesCash;
		this.comprasCash = comprasCash;
		this.ventasCash = ventasCash;
		this.posicionCash = posicionCash;
		this.operacionesTom = operacionesTom;
		this.comprasTom = comprasTom;
		this.ventasTom = ventasTom;
		this.posicionTom = posicionTom;
		this.operacionesSpot = operacionesSpot;
		this.comprasSpot = comprasSpot;
		this.ventasSpot = ventasSpot;
		this.posicionSpot = posicionSpot;
		this.operaciones72Hr = operaciones72Hr;
		this.compras72Hr = compras72Hr;
		this.ventas72Hr = ventas72Hr;
		this.posicion72Hr = posicion72Hr;
		this.operacionesVFut = operacionesVFut;
		this.comprasVFut = comprasVFut;
		this.ventasVFut = ventasVFut;
		this.posicionVFut = posicionVFut;
		this.fechaPosicionFinalAnterior = fechaPosicionFinalAnterior;
		this.montoPosicionFinalAnterior = montoPosicionFinalAnterior;
		this.vencimientosTom = vencimientosTom;
		this.fechaVencimientoTom = fechaVencimientoTom;
		this.montoComprasVencimientosTom = montoComprasVencimientosTom;
		this.montoVentasVencimientosTom = montoVentasVencimientosTom;
		this.montoPosicionVencimientosTom = montoPosicionVencimientosTom;
		this.vencimientosSpot = vencimientosSpot;
		this.fechaVencimientoSpot = fechaVencimientoSpot;
		this.montoComprasVencimientosSpot = montoComprasVencimientosSpot;
		this.montoVentasVencimientosSpot = montoVentasVencimientosSpot;
		this.montoPosicionVencimientosSpot = montoPosicionVencimientosSpot;
		this.vencimientos72Hr = vencimientos72Hr;
		this.fechaVencimiento72Hr = fechaVencimiento72Hr;
		this.montoComprasVencimientos72Hr = montoComprasVencimientos72Hr;
		this.montoVentasVencimientos72Hr = montoVentasVencimientos72Hr;
		this.montoPosicionVencimientos72Hr = montoPosicionVencimientos72Hr;
		this.posicionHoy = posicionHoy;
		this.montoComprasPosicionCash = montoComprasPosicionCash;
		this.montoVentasPosicionCash = montoVentasPosicionCash;
		this.montoPosicionCash = montoPosicionCash;
		this.montoPosicionDiaSiguiente = montoPosicionDiaSiguiente;
		this.image = imagen;
	}

	/**
	 * Regresa el valor de compras72Hr.
	 *	
	 * @return Double.
	 */
	public Double getCompras72Hr() {
		return compras72Hr;
	}
	/**
	 * Asigna el valor para compras72Hr.
	 *
	 * @param compras72Hr El valor para compras72Hr.
	 */
	public void setCompras72Hr(Double compras72Hr) {
		this.compras72Hr = compras72Hr;
	}
	/**
	 * Regresa el valor de comprasCash.
	 *	
	 * @return Double.
	 */
	public Double getComprasCash() {
		return comprasCash;
	}
	/**
	 * Asigna el valor para comprasCash.
	 *
	 * @param comprasCash El valor para comprasCash.
	 */
	public void setComprasCash(Double comprasCash) {
		this.comprasCash = comprasCash;
	}
	/**
	 * Regresa el valor de comprasSpot.
	 *	
	 * @return Double.
	 */
	public Double getComprasSpot() {
		return comprasSpot;
	}
	/**
	 * Asigna el valor para comprasSpot.
	 *
	 * @param comprasSpot El valor para comprasSpot.
	 */
	public void setComprasSpot(Double comprasSpot) {
		this.comprasSpot = comprasSpot;
	}
	/**
	 * Regresa el valor de comprasTom.
	 *	
	 * @return Double.
	 */
	public Double getComprasTom() {
		return comprasTom;
	}
	/**
	 * Asigna el valor para comprasTom.
	 *
	 * @param comprasTom El valor para comprasTom.
	 */
	public void setComprasTom(Double comprasTom) {
		this.comprasTom = comprasTom;
	}
	/**
	 * Regresa el valor de comprasVFut.
	 *	
	 * @return Double.
	 */
	public Double getComprasVFut() {
		return comprasVFut;
	}
	/**
	 * Asigna el valor para comprasVFut.
	 *
	 * @param comprasVFut El valor para comprasVFut.
	 */
	public void setComprasVFut(Double comprasVFut) {
		this.comprasVFut = comprasVFut;
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
	 * Regresa el valor de divisaReferencia.
	 *	
	 * @return String.
	 */
	public String getDivisaReferencia() {
		return divisaReferencia;
	}
	/**
	 * Asigna el valor para divisaReferencia.
	 *
	 * @param divisaReferencia El valor para divisaReferencia.
	 */
	public void setDivisaReferencia(String divisaReferencia) {
		this.divisaReferencia = divisaReferencia;
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
	 * Regresa el valor de fechaPosicionFinalAnterior.
	 *	
	 * @return Date.
	 */
	public Date getFechaPosicionFinalAnterior() {
		return fechaPosicionFinalAnterior;
	}
	/**
	 * Asigna el valor para fechaPosicionFinalAnterior.
	 *
	 * @param fechaPosicionFinalAnterior El valor para fechaPosicionFinalAnterior.
	 */
	public void setFechaPosicionFinalAnterior(Date fechaPosicionFinalAnterior) {
		this.fechaPosicionFinalAnterior = fechaPosicionFinalAnterior;
	}
	/**
	 * Regresa el valor de fechaVencimiento72Hr.
	 *	
	 * @return Date.
	 */
	public Date getFechaVencimiento72Hr() {
		return fechaVencimiento72Hr;
	}
	/**
	 * Asigna el valor para fechaVencimiento72Hr.
	 *
	 * @param fechaVencimiento72Hr El valor para fechaVencimiento72Hr.
	 */
	public void setFechaVencimiento72Hr(Date fechaVencimiento72Hr) {
		this.fechaVencimiento72Hr = fechaVencimiento72Hr;
	}
	/**
	 * Regresa el valor de fechaVencimientoSpot.
	 *	
	 * @return Date.
	 */
	public Date getFechaVencimientoSpot() {
		return fechaVencimientoSpot;
	}
	/**
	 * Asigna el valor para fechaVencimientoSpot.
	 *
	 * @param fechaVencimientoSpot El valor para fechaVencimientoSpot.
	 */
	public void setFechaVencimientoSpot(Date fechaVencimientoSpot) {
		this.fechaVencimientoSpot = fechaVencimientoSpot;
	}
	/**
	 * Regresa el valor de fechaVencimientoTom.
	 *	
	 * @return Date.
	 */
	public Date getFechaVencimientoTom() {
		return fechaVencimientoTom;
	}
	/**
	 * Asigna el valor para fechaVencimientoTom.
	 *
	 * @param fechaVencimientoTom El valor para fechaVencimientoTom.
	 */
	public void setFechaVencimientoTom(Date fechaVencimientoTom) {
		this.fechaVencimientoTom = fechaVencimientoTom;
	}
	/**
	 * Regresa el valor de fechaVencimientoVFut.
	 *	
	 * @return Date.
	 */
	public Date getFechaVencimientoVFut() {
		return fechaVencimientoVFut;
	}
	/**
	 * Asigna el valor para fechaVencimientoVFut.
	 *
	 * @param fechaVencimientoVFut El valor para fechaVencimientoVFut.
	 */
	public void setFechaVencimientoVFut(Date fechaVencimientoVFut) {
		this.fechaVencimientoVFut = fechaVencimientoVFut;
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
	 * Regresa el valor de mesa.
	 *	
	 * @return String.
	 */
	public String getMesa() {
		return mesa;
	}
	/**
	 * Asigna el valor para mesa.
	 *
	 * @param mesa El valor para mesa.
	 */
	public void setMesa(String mesa) {
		this.mesa = mesa;
	}
	/**
	 * Regresa el valor de montoComprasPosicionCash.
	 *	
	 * @return Double.
	 */
	public Double getMontoComprasPosicionCash() {
		return montoComprasPosicionCash;
	}
	/**
	 * Asigna el valor para montoComprasPosicionCash.
	 *
	 * @param montoComprasPosicionCash El valor para montoComprasPosicionCash.
	 */
	public void setMontoComprasPosicionCash(Double montoComprasPosicionCash) {
		this.montoComprasPosicionCash = montoComprasPosicionCash;
	}
	/**
	 * Regresa el valor de montoComprasVencimientos72Hr.
	 *	
	 * @return Double.
	 */
	public Double getMontoComprasVencimientos72Hr() {
		return montoComprasVencimientos72Hr;
	}
	/**
	 * Asigna el valor para montoComprasVencimientos72Hr.
	 *
	 * @param montoComprasVencimientos72Hr El valor para montoComprasVencimientos72Hr.
	 */
	public void setMontoComprasVencimientos72Hr(Double montoComprasVencimientos72Hr) {
		this.montoComprasVencimientos72Hr = montoComprasVencimientos72Hr;
	}
	/**
	 * Regresa el valor de montoComprasVencimientosSpot.
	 *	
	 * @return Double.
	 */
	public Double getMontoComprasVencimientosSpot() {
		return montoComprasVencimientosSpot;
	}
	/**
	 * Asigna el valor para montoComprasVencimientosSpot.
	 *
	 * @param montoComprasVencimientosSpot El valor para montoComprasVencimientosSpot.
	 */
	public void setMontoComprasVencimientosSpot(Double montoComprasVencimientosSpot) {
		this.montoComprasVencimientosSpot = montoComprasVencimientosSpot;
	}
	/**
	 * Regresa el valor de montoComprasVencimientosTom.
	 *	
	 * @return Double.
	 */
	public Double getMontoComprasVencimientosTom() {
		return montoComprasVencimientosTom;
	}
	/**
	 * Asigna el valor para montoComprasVencimientosTom.
	 *
	 * @param montoComprasVencimientosTom El valor para montoComprasVencimientosTom.
	 */
	public void setMontoComprasVencimientosTom(Double montoComprasVencimientosTom) {
		this.montoComprasVencimientosTom = montoComprasVencimientosTom;
	}
	/**
	 * Regresa el valor de montoComprasVencimientosVFut.
	 *	
	 * @return Double.
	 */
	public Double getMontoComprasVencimientosVFut() {
		return montoComprasVencimientosVFut;
	}
	/**
	 * Asigna el valor para montoComprasVencimientosVFut.
	 *
	 * @param montoComprasVencimientosVFut El valor para montoComprasVencimientosVFut.
	 */
	public void setMontoComprasVencimientosVFut(Double montoComprasVencimientosVFut) {
		this.montoComprasVencimientosVFut = montoComprasVencimientosVFut;
	}
	/**
	 * Regresa el valor de montoPosicionCash.
	 *	
	 * @return Double.
	 */
	public Double getMontoPosicionCash() {
		return montoPosicionCash;
	}
	/**
	 * Asigna el valor para montoPosicionCash.
	 *
	 * @param montoPosicionCash El valor para montoPosicionCash.
	 */
	public void setMontoPosicionCash(Double montoPosicionCash) {
		this.montoPosicionCash = montoPosicionCash;
	}
	/**
	 * Regresa el valor de montoPosicionDiaSiguiente.
	 *	
	 * @return Double.
	 */
	public Double getMontoPosicionDiaSiguiente() {
		return montoPosicionDiaSiguiente;
	}
	/**
	 * Asigna el valor para montoPosicionDiaSiguiente.
	 *
	 * @param montoPosicionDiaSiguiente El valor para montoPosicionDiaSiguiente.
	 */
	public void setMontoPosicionDiaSiguiente(Double montoPosicionDiaSiguiente) {
		this.montoPosicionDiaSiguiente = montoPosicionDiaSiguiente;
	}
	/**
	 * Regresa el valor de montoPosicionFinalAnterior.
	 *	
	 * @return Double.
	 */
	public Double getMontoPosicionFinalAnterior() {
		return montoPosicionFinalAnterior;
	}
	/**
	 * Asigna el valor para montoPosicionFinalAnterior.
	 *
	 * @param montoPosicionFinalAnterior El valor para montoPosicionFinalAnterior.
	 */
	public void setMontoPosicionFinalAnterior(Double montoPosicionFinalAnterior) {
		this.montoPosicionFinalAnterior = montoPosicionFinalAnterior;
	}
	/**
	 * Regresa el valor de montoPosicionVencimientos72Hr.
	 *	
	 * @return Double.
	 */
	public Double getMontoPosicionVencimientos72Hr() {
		return montoPosicionVencimientos72Hr;
	}
	/**
	 * Asigna el valor para montoPosicionVencimientos72Hr.
	 *
	 * @param montoPosicionVencimientos72Hr El valor para montoPosicionVencimientos72Hr.
	 */
	public void setMontoPosicionVencimientos72Hr(
			Double montoPosicionVencimientos72Hr) {
		this.montoPosicionVencimientos72Hr = montoPosicionVencimientos72Hr;
	}
	/**
	 * Regresa el valor de montoPosicionVencimientosSpot.
	 *	
	 * @return Double.
	 */
	public Double getMontoPosicionVencimientosSpot() {
		return montoPosicionVencimientosSpot;
	}
	/**
	 * Asigna el valor para montoPosicionVencimientosSpot.
	 *
	 * @param montoPosicionVencimientosSpot El valor para montoPosicionVencimientosSpot.
	 */
	public void setMontoPosicionVencimientosSpot(
			Double montoPosicionVencimientosSpot) {
		this.montoPosicionVencimientosSpot = montoPosicionVencimientosSpot;
	}
	/**
	 * Regresa el valor de montoPosicionVencimientosTom.
	 *	
	 * @return Double.
	 */
	public Double getMontoPosicionVencimientosTom() {
		return montoPosicionVencimientosTom;
	}
	/**
	 * Asigna el valor para montoPosicionVencimientosTom.
	 *
	 * @param montoPosicionVencimientosTom El valor para montoPosicionVencimientosTom.
	 */
	public void setMontoPosicionVencimientosTom(Double montoPosicionVencimientosTom) {
		this.montoPosicionVencimientosTom = montoPosicionVencimientosTom;
	}
	/**
	 * Regresa el valor de montoPosicionVencimientosVFut.
	 *	
	 * @return Double.
	 */
	public Double getMontoPosicionVencimientosVFut() {
		return montoPosicionVencimientosVFut;
	}
	/**
	 * Asigna el valor para montoPosicionVencimientosVFut.
	 *
	 * @param montoPosicionVencimientosVFut El valor para montoPosicionVencimientosVFut.
	 */
	public void setMontoPosicionVencimientosVFut(
			Double montoPosicionVencimientosVFut) {
		this.montoPosicionVencimientosVFut = montoPosicionVencimientosVFut;
	}
	/**
	 * Regresa el valor de montoVentasPosicionCash.
	 *	
	 * @return Double.
	 */
	public Double getMontoVentasPosicionCash() {
		return montoVentasPosicionCash;
	}
	/**
	 * Asigna el valor para montoVentasPosicionCash.
	 *
	 * @param montoVentasPosicionCash El valor para montoVentasPosicionCash.
	 */
	public void setMontoVentasPosicionCash(Double montoVentasPosicionCash) {
		this.montoVentasPosicionCash = montoVentasPosicionCash;
	}
	/**
	 * Regresa el valor de montoVentasVencimientos72Hr.
	 *	
	 * @return Double.
	 */
	public Double getMontoVentasVencimientos72Hr() {
		return montoVentasVencimientos72Hr;
	}
	/**
	 * Asigna el valor para montoVentasVencimientos72Hr.
	 *
	 * @param montoVentasVencimientos72Hr El valor para montoVentasVencimientos72Hr.
	 */
	public void setMontoVentasVencimientos72Hr(Double montoVentasVencimientos72Hr) {
		this.montoVentasVencimientos72Hr = montoVentasVencimientos72Hr;
	}
	/**
	 * Regresa el valor de montoVentasVencimientosSpot.
	 *	
	 * @return Double.
	 */
	public Double getMontoVentasVencimientosSpot() {
		return montoVentasVencimientosSpot;
	}
	/**
	 * Asigna el valor para montoVentasVencimientosSpot.
	 *
	 * @param montoVentasVencimientosSpot El valor para montoVentasVencimientosSpot.
	 */
	public void setMontoVentasVencimientosSpot(Double montoVentasVencimientosSpot) {
		this.montoVentasVencimientosSpot = montoVentasVencimientosSpot;
	}
	/**
	 * Regresa el valor de montoVentasVencimientosTom.
	 *	
	 * @return Double.
	 */
	public Double getMontoVentasVencimientosTom() {
		return montoVentasVencimientosTom;
	}
	/**
	 * Asigna el valor para montoVentasVencimientosTom.
	 *
	 * @param montoVentasVencimientosTom El valor para montoVentasVencimientosTom.
	 */
	public void setMontoVentasVencimientosTom(Double montoVentasVencimientosTom) {
		this.montoVentasVencimientosTom = montoVentasVencimientosTom;
	}
	/**
	 * Regresa el valor de montoVentasVencimientosVFut.
	 *	
	 * @return Double.
	 */
	public Double getMontoVentasVencimientosVFut() {
		return montoVentasVencimientosVFut;
	}
	/**
	 * Asigna el valor para montoVentasVencimientosVFut.
	 *
	 * @param montoVentasVencimientosVFut El valor para montoVentasVencimientosVFut.
	 */
	public void setMontoVentasVencimientosVFut(Double montoVentasVencimientosVFut) {
		this.montoVentasVencimientosVFut = montoVentasVencimientosVFut;
	}
	/**
	 * Regresa el valor de operaciones72Hr.
	 *	
	 * @return String.
	 */
	public String getOperaciones72Hr() {
		return operaciones72Hr;
	}
	/**
	 * Asigna el valor para operaciones72Hr.
	 *
	 * @param operaciones72Hr El valor para operaciones72Hr.
	 */
	public void setOperaciones72Hr(String operaciones72Hr) {
		this.operaciones72Hr = operaciones72Hr;
	}
	/**
	 * Regresa el valor de operacionesCash.
	 *	
	 * @return String.
	 */
	public String getOperacionesCash() {
		return operacionesCash;
	}
	/**
	 * Asigna el valor para operacionesCash.
	 *
	 * @param operacionesCash El valor para operacionesCash.
	 */
	public void setOperacionesCash(String operacionesCash) {
		this.operacionesCash = operacionesCash;
	}
	/**
	 * Regresa el valor de operacionesSpot.
	 *	
	 * @return String.
	 */
	public String getOperacionesSpot() {
		return operacionesSpot;
	}
	/**
	 * Asigna el valor para operacionesSpot.
	 *
	 * @param operacionesSpot El valor para operacionesSpot.
	 */
	public void setOperacionesSpot(String operacionesSpot) {
		this.operacionesSpot = operacionesSpot;
	}
	/**
	 * Regresa el valor de operacionesTom.
	 *	
	 * @return String.
	 */
	public String getOperacionesTom() {
		return operacionesTom;
	}
	/**
	 * Asigna el valor para operacionesTom.
	 *
	 * @param operacionesTom El valor para operacionesTom.
	 */
	public void setOperacionesTom(String operacionesTom) {
		this.operacionesTom = operacionesTom;
	}
	/**
	 * Regresa el valor de operacionesVFut.
	 *	
	 * @return String.
	 */
	public String getOperacionesVFut() {
		return operacionesVFut;
	}
	/**
	 * Asigna el valor para operacionesVFut.
	 *
	 * @param operacionesVFut El valor para operacionesVFut.
	 */
	public void setOperacionesVFut(String operacionesVFut) {
		this.operacionesVFut = operacionesVFut;
	}
	/**
	 * Regresa el valor de posicion72Hr.
	 *	
	 * @return Double.
	 */
	public Double getPosicion72Hr() {
		return posicion72Hr;
	}
	/**
	 * Asigna el valor para posicion72Hr.
	 *
	 * @param posicion72Hr El valor para posicion72Hr.
	 */
	public void setPosicion72Hr(Double posicion72Hr) {
		this.posicion72Hr = posicion72Hr;
	}
	/**
	 * Regresa el valor de posicionCash.
	 *	
	 * @return Double.
	 */
	public Double getPosicionCash() {
		return posicionCash;
	}
	/**
	 * Asigna el valor para posicionCash.
	 *
	 * @param posicionCash El valor para posicionCash.
	 */
	public void setPosicionCash(Double posicionCash) {
		this.posicionCash = posicionCash;
	}
	/**
	 * Regresa el valor de posicionHoy.
	 *	
	 * @return String.
	 */
	public String getPosicionHoy() {
		return posicionHoy;
	}
	/**
	 * Asigna el valor para posicionHoy.
	 *
	 * @param posicionHoy El valor para posicionHoy.
	 */
	public void setPosicionHoy(String posicionHoy) {
		this.posicionHoy = posicionHoy;
	}
	/**
	 * Regresa el valor de posicionSpot.
	 *	
	 * @return Double.
	 */
	public Double getPosicionSpot() {
		return posicionSpot;
	}
	/**
	 * Asigna el valor para posicionSpot.
	 *
	 * @param posicionSpot El valor para posicionSpot.
	 */
	public void setPosicionSpot(Double posicionSpot) {
		this.posicionSpot = posicionSpot;
	}
	/**
	 * Regresa el valor de posicionTom.
	 *	
	 * @return Double.
	 */
	public Double getPosicionTom() {
		return posicionTom;
	}
	/**
	 * Asigna el valor para posicionTom.
	 *
	 * @param posicionTom El valor para posicionTom.
	 */
	public void setPosicionTom(Double posicionTom) {
		this.posicionTom = posicionTom;
	}
	/**
	 * Regresa el valor de posicionVFut.
	 *	
	 * @return Double.
	 */
	public Double getPosicionVFut() {
		return posicionVFut;
	}
	/**
	 * Asigna el valor para posicionVFut.
	 *
	 * @param posicionVFut El valor para posicionVFut.
	 */
	public void setPosicionVFut(Double posicionVFut) {
		this.posicionVFut = posicionVFut;
	}
	/**
	 * Regresa el valor de vencimientos72Hr.
	 *	
	 * @return String.
	 */
	public String getVencimientos72Hr() {
		return vencimientos72Hr;
	}
	/**
	 * Asigna el valor para vencimientos72Hr.
	 *
	 * @param vencimientos72Hr El valor para vencimientos72Hr.
	 */
	public void setVencimientos72Hr(String vencimientos72Hr) {
		this.vencimientos72Hr = vencimientos72Hr;
	}
	/**
	 * Regresa el valor de vencimientosSpot.
	 *	
	 * @return String.
	 */
	public String getVencimientosSpot() {
		return vencimientosSpot;
	}
	/**
	 * Asigna el valor para vencimientosSpot.
	 *
	 * @param vencimientosSpot El valor para vencimientosSpot.
	 */
	public void setVencimientosSpot(String vencimientosSpot) {
		this.vencimientosSpot = vencimientosSpot;
	}
	/**
	 * Regresa el valor de vencimientosTom.
	 *	
	 * @return String.
	 */
	public String getVencimientosTom() {
		return vencimientosTom;
	}
	/**
	 * Asigna el valor para vencimientosTom.
	 *
	 * @param vencimientosTom El valor para vencimientosTom.
	 */
	public void setVencimientosTom(String vencimientosTom) {
		this.vencimientosTom = vencimientosTom;
	}
	/**
	 * Regresa el valor de vencimientosVFut.
	 *	
	 * @return String.
	 */
	public String getVencimientosVFut() {
		return vencimientosVFut;
	}
	/**
	 * Asigna el valor para vencimientosVFut.
	 *
	 * @param vencimientosVFut El valor para vencimientosVFut.
	 */
	public void setVencimientosVFut(String vencimientosVFut) {
		this.vencimientosVFut = vencimientosVFut;
	}
	/**
	 * Regresa el valor de ventas72Hr.
	 *	
	 * @return Double.
	 */
	public Double getVentas72Hr() {
		return ventas72Hr;
	}
	/**
	 * Asigna el valor para ventas72Hr.
	 *
	 * @param ventas72Hr El valor para ventas72Hr.
	 */
	public void setVentas72Hr(Double ventas72Hr) {
		this.ventas72Hr = ventas72Hr;
	}
	/**
	 * Regresa el valor de ventasCash.
	 *	
	 * @return Double.
	 */
	public Double getVentasCash() {
		return ventasCash;
	}
	/**
	 * Asigna el valor para ventasCash.
	 *
	 * @param ventasCash El valor para ventasCash.
	 */
	public void setVentasCash(Double ventasCash) {
		this.ventasCash = ventasCash;
	}
	/**
	 * Regresa el valor de ventasSpot.
	 *	
	 * @return Double.
	 */
	public Double getVentasSpot() {
		return ventasSpot;
	}
	/**
	 * Asigna el valor para ventasSpot.
	 *
	 * @param ventasSpot El valor para ventasSpot.
	 */
	public void setVentasSpot(Double ventasSpot) {
		this.ventasSpot = ventasSpot;
	}
	/**
	 * Regresa el valor de ventasTom.
	 *	
	 * @return Double.
	 */
	public Double getVentasTom() {
		return ventasTom;
	}
	/**
	 * Asigna el valor para ventasTom.
	 *
	 * @param ventasTom El valor para ventasTom.
	 */
	public void setVentasTom(Double ventasTom) {
		this.ventasTom = ventasTom;
	}
	/**
	 * Regresa el valor de ventasVFut.
	 *	
	 * @return Double.
	 */
	public Double getVentasVFut() {
		return ventasVFut;
	}
	/**
	 * Asigna el valor para ventasVFut.
	 *
	 * @param ventasVFut El valor para ventasVFut.
	 */
	public void setVentasVFut(Double ventasVFut) {
		this.ventasVFut = ventasVFut;
	}
	
	/**
	 * Fecha de operaci&oacute;n del deal.
	 */
	private Date fechaOperacion;
	
	/**
	 * Divisa de operaci&oacute;n del deal.
	 */
	private String divisa;
	
	/**
	 * Operaciones para CASH.
	 */
	private String operacionesCash;
	
	/**
	 * Compras CASH.
	 */
	private Double comprasCash;
	
	/**
	 * Ventas CASH.
	 */
	private Double ventasCash;
	
	/**
	 * Posici&oacute;n para CASH.
	 */
	private Double posicionCash;
	
	/**
	 * Operaciones para TOM.
	 */
	private String operacionesTom;
	
	/**
	 * Compras TOM.
	 */
	private Double comprasTom;
	
	/**
	 * Ventas TOM.
	 */
	private Double ventasTom;
	
	/**
	 * Posicion para TOM.
	 */
	private Double posicionTom;
	
	/**
	 * Operaciones para SPOT.
	 */
	private String operacionesSpot;
	
	/**
	 * Compras SPOT.
	 */
	private Double comprasSpot;
	
	/**
	 * Ventas SPOT.
	 */
	private Double ventasSpot;
	
	/**
	 * Posicion para SPOT.
	 */
	private Double posicionSpot;
	
	/**
	 * Operaciones 72HR.
	 */
	private String operaciones72Hr;
	
	/**
	 * Compras 72HR.
	 */
	private Double compras72Hr;
	
	/**
	 * Ventas 72HR.
	 */
	private Double ventas72Hr;
	
	/**
	 * Posicion para 72HR.
	 */
	private Double posicion72Hr;
	
	/**
	 * Operaciones VFUT.
	 */
	private String operacionesVFut;
	
	/***
	 * Compras VFUT.
	 */
	private Double comprasVFut;
	
	/**
	 * Ventas VFUT.
	 */
	private Double ventasVFut;
	
	/**
	 * Posicion para VFUT.
	 */
	private Double posicionVFut;
	
	/**
	 * Fecha posici&oacute;n final d&iacute;a anterior.
	 */
	private Date fechaPosicionFinalAnterior;
	
	/**
	 * Monto posici&oacute;n final d&iacute;a anterior.
	 */
	private Double montoPosicionFinalAnterior;
	
	/**
	 *Vencimientos TOM.
	 */
	private String vencimientosTom;
	
	/**
	 * Fecha vencimiento operaciones TOM.
	 */
	private Date fechaVencimientoTom;
	
	/**
	 * Monto compras vencimientos TOM.
	 */
	private Double montoComprasVencimientosTom;
	
	/**
	 * Monto ventas vencimiento TOM.
	 */
	private Double montoVentasVencimientosTom;
	
	/**
	 * Monto posici&oacute;n vencmientos TOM
	 */
	private Double montoPosicionVencimientosTom;
	
	/**
	 * Vencimientos SPOT.
	 */
	private String vencimientosSpot;
	
	/**
	 * Fecha vencimiento operaciones SPOT.
	 */
	private Date fechaVencimientoSpot;
	
	/**
	 * Monto compras vencimientos SPOT.
	 */
	private Double montoComprasVencimientosSpot;
	
	/**
	 * Monto ventas vencimientos SPOT.
	 */
	private Double montoVentasVencimientosSpot;
	
	/**
	 * Monto posici&oacute;n vencmientos SPOT
	 */
	private Double montoPosicionVencimientosSpot;
	
	/**
	 * Vencimientos 72HR.
	 */
	private String vencimientos72Hr;
	
	/**
	 * fecha vencimiento operaciones 72HR
	 */
	private Date fechaVencimiento72Hr;
	
	/**
	 * Monto compras vencimientos 72HR.
	 */
	private Double montoComprasVencimientos72Hr;
	
	/**
	 * Monto ventas vencimientos 72HR.
	 */
	private Double montoVentasVencimientos72Hr;
	
	/**
	 * Monto posici&oacute;n vencmientos 72HR
	 */
	private Double montoPosicionVencimientos72Hr;
	
	/**
	 * Vencimientos VFUT
	 */
	private String vencimientosVFut;
	
	/**
	 * Fecha de vencimiento operaciones VFUT.
	 */
	private Date fechaVencimientoVFut;
	
	/**
	 * Monto compras vencimientos VFUT.
	 */
	private Double montoComprasVencimientosVFut;
	
	/**
	 * Monto ventas vencimientos VFUT.
	 */
	private Double montoVentasVencimientosVFut;
	
	/**
	 * Monto posici&oacute;n vencmientos VFUT.
	 */
	private Double montoPosicionVencimientosVFut;
	
	/**
	 * Posici&oacute;n del d&iacute;a de hoy.
	 */
	private String posicionHoy;
	
	/**
	 * Monto compras posici&oacute;n CASH.
	 */
	private Double montoComprasPosicionCash;
	
	/**
	 * Monto compras posici&oacute;n CASH.
	 */
	private Double montoVentasPosicionCash;
	
	/**
	 * Monto posici&oacute;n CASH.
	 */
	private Double montoPosicionCash;
	
	/**
	 * Monto posici&oacute;n siguiente d&iacute;a.
	 */
	private Double montoPosicionDiaSiguiente;
	
	/**
	 * Imagen de encabezado del reporte.
	 */
	private InputStream image;
	
	/**
	 * Mesa de operaci&oacute;n.
	 */
	private String mesa;
	
	/**
	 * Divisa de referencia.
	 */
	private String divisaReferencia;
}
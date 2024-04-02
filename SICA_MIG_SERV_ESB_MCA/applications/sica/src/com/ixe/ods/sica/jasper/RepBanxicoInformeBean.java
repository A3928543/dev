/*
 * $Id: RepBanxicoInformeBean.java,v 1.12 2008/02/22 18:25:03 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Bean que permite probar los reportes para Banxico.
 *
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:03 $
 */
public class RepBanxicoInformeBean implements Serializable {

	/**
	 * Constructor de la clase RepBanxicoInformeBean, que se utiliza
	 * para almacenar los datos para la prueba del reporte del Deal
	 * con las nuevas variables de fechas valor 72 hr y valor futuro.
	 *
	 * @param cliente El nombre del cliente.
	 * @param fechaValor La fecha valor de la operaci&oacute;n.
	 * @param fechaLiquidacion La fecha de liquidaci&oacute;n del deal.
	 * @param posicionInicial El valor de la posici&oacute;n inicial.
	 * @param sector El sector banxico del cliente.
	 * @param sectorCash La fecha valor CASH de la operaci&oacute;n.
	 * @param sectorSpot La fecha valor SPOT de la operaci&oacute;n.
	 * @param sectorTom La fecha valor TOM de la operaci&oacute;n.
	 * @param sector72Hr La fecha valor 72HR de la operaci&oacute;n.
	 * @param sectorVFut La fecha valor VFUT de la operaci&oacute;n.
	 * @param totalCompras El valor total de compras.
	 * @param totalVentas EL valor total de ventas.
	 * @param image La imagen de encabezado del reporte.
	 * @param isRecibimos Define si recibimos o entregamos.
	 * @param idDivisa El id de la divisa.
	 * @param flag La bandera de cada deal.
	 * @param swap El valor del swap.
	 * @param conSwaps El valor de las operaciones con Swaps.
	 * @param sinSwaps El valor de las operaciones sin Swaps.
	 * @param posicionInicialCash El monto de la Posici&oacute;n Incial para Cash.
	 * @param posicionInicialTom El monto de la Posici&oacute;n Incial para Tom.
	 * @param posicionInicialSpot El monto de la Posici&oacute;n Incial para Spot.
	 * @param posicionInicial72Hr El monto de la Posici&oacute;n Incial para 72Hr.
	 * @param posicionInicialVFut El monto de la Posici&oacute;n Incial para VFut.
	 * @param tipoCambioCompra El tipo de cambio global para compras.
	 * @param tipoCambioCompraCash El tipo de cambio para compras Cash.
	 * @param tipoCambioCompraTom El tipo de cambio para compras Tom.
	 * @param tipoCambioCompraSpot El tipo de cambio para compras Spot.
	 * @param tipoCambioCompra72Hr El tipo de cambio para compras 72Hr.
	 * @param tipoCambioCompraVFut El tipo de cambio para compras VFut.
	 * @param tipoCambioVenta El tipo de cambio global para ventas.
	 * @param tipoCambioVentaCash El tipo de cambio para ventas Cash.
	 * @param tipoCambioVentaTom El tipo de cambio para ventas Tom.
	 * @param tipoCambioVentaSpot El tipo de cambio para ventas Spot.
	 * @param tipoCambioVenta72Hr El tipo de cambio para ventas 72Hr.
	 * @param tipoCambioVentaVFut El tipo de cambio para ventas VFut.
	 * @param swapMn El monto en moneda nacional para Swap.
	 */
	public RepBanxicoInformeBean(String cliente, Date fechaValor,
		Date fechaLiquidacion, Double posicionInicial, String sector,
		Double sectorCash, Double sectorSpot, Double sectorTom,
		Double sector72Hr, Double sectorVFut, Double totalCompras,
		Double totalVentas, InputStream image, Boolean isRecibimos,
		String idDivisa, String flag, Double swap, String conSwaps,
		String sinSwaps, Double posicionInicialCash, Double posicionInicialTom,
		Double posicionInicialSpot, Double posicionInicial72Hr,
		Double posicionInicialVFut, Double tipoCambioCompra,
		Double tipoCambioCompraCash, Double tipoCambioCompraTom,
		Double tipoCambioCompraSpot, Double tipoCambioCompra72Hr,
		Double tipoCambioCompraVFut, Double tipoCambioVenta,
		Double tipoCambioVentaCash, Double tipoCambioVentaTom,
		Double tipoCambioVentaSpot, Double tipoCambioVenta72Hr,
		Double tipoCambioVentaVFut, Double swapMn, Boolean isForward) {
		super();
		this.posicionInicial = posicionInicial;
		this.sector = sector;
		this.sectorCash = sectorCash;
		this.sectorSpot = sectorSpot;
		this.sectorTom = sectorTom;
		this.sector72Hr = sector72Hr;
		this.sectorVFut = sectorVFut;
		this.totalCompras = totalCompras;
		this.totalVentas = totalVentas;
		this.image = image;
		this.isRecibimos = isRecibimos;
		this.idDivisa = idDivisa;
		this.flag = flag;
		this.swap = swap;
		this.cliente = cliente;
		this.conSwaps = conSwaps;
		this.sinSwaps = sinSwaps;
		this.fechaValor = fechaValor;
		this.fechaLiquidacion = fechaLiquidacion;
		this.posicionInicialCash = posicionInicialCash;
		this.posicionInicialTom = posicionInicialTom;
		this.posicionInicialSpot = posicionInicialSpot;
		this.posicionInicial72Hr = posicionInicial72Hr;
		this.posicionInicialVFut = posicionInicialVFut;
		this.tipoCambioCompra = tipoCambioCompra;
		this.tipoCambioCompraCash = tipoCambioCompraCash;
		this.tipoCambioCompraTom = tipoCambioCompraTom;
		this.tipoCambioCompraSpot = tipoCambioCompraSpot;
		this.tipoCambioCompra72Hr = tipoCambioCompra72Hr;
		this.tipoCambioCompraVFut = tipoCambioCompraVFut;
		this.tipoCambioVenta = tipoCambioVenta;
		this.tipoCambioVentaCash = tipoCambioVentaCash;
		this.tipoCambioVentaTom = tipoCambioVentaTom;
		this.tipoCambioVentaSpot = tipoCambioVentaSpot;
		this.tipoCambioVenta72Hr = tipoCambioVenta72Hr;
		this.tipoCambioVentaVFut = tipoCambioVentaVFut;
		this.swapMn = swapMn;
		this.isForward = isForward;
	}

	/**
	 * Constructor de la clase RepBanxicoInformeBean, que se utiliza
	 * para almacenar los datos para la prueba del reporte del Deal
	 * con las nuevas variables de fechas valor 72 hr y valor futuro.
	 *
	 * @param cliente El nombre del cliente.
	 * @param fechaValor La fecha valor de la operaci&oacute;n.
	 * @param fechaLiquidacion La fecha de liquidaci&oacute;n del deal.
	 * @param posicionInicial El valor de la posici&oacute;n inicial.
	 * @param sector El sector banxico del cliente.
	 * @param sectorCash La fecha valor CASH de la operaci&oacute;n.
	 * @param sectorSpot La fecha valor SPOT de la operaci&oacute;n.
	 * @param sectorTom La fecha valor TOM de la operaci&oacute;n.
	 * @param sector72Hr La fecha valor 72HR de la operaci&oacute;n.
	 * @param sectorVFut La fecha valor VFUT de la operaci&oacute;n.
	 * @param totalCompras El valor total de compras.
	 * @param totalVentas EL valor total de ventas.
	 * @param image La imagen de encabezado del reporte.
	 * @param isRecibimos Define si recibimos o entregamos.
	 * @param idDivisa El id de la divisa.
	 * @param flag La bandera de cada deal.
	 * @param swap El valor del swap.
	 * @param conSwaps El valor de las operaciones con Swaps.
	 * @param sinSwaps El valor de las operaciones sin Swaps.
	 * @param montoIxeForward El monto para operaciones Ixe Forwards.
	 * @param posicionInicialCash El monto de la Posici&oacute;n Incial para Cash.
	 * @param posicionInicialTom El monto de la Posici&oacute;n Incial para Tom.
	 * @param posicionInicialSpot El monto de la Posici&oacute;n Incial para Spot.
	 * @param posicionInicial72Hr El monto de la Posici&oacute;n Incial para 72Hr.
	 * @param posicionInicialVFut El monto de la Posici&oacute;n Incial para VFut.
	 * @param tipoCambioCompra El tipo de cambio global para compras.
	 * @param tipoCambioCompraCash El tipo de cambio para compras Cash.
	 * @param tipoCambioCompraTom El tipo de cambio para compras Tom.
	 * @param tipoCambioCompraSpot El tipo de cambio para compras Spot.
	 * @param tipoCambioCompra72Hr El tipo de cambio para compras 72Hr.
	 * @param tipoCambioCompraVFut El tipo de cambio para compras VFut.
	 * @param tipoCambioVenta El tipo de cambio global para ventas.
	 * @param tipoCambioVentaCash El tipo de cambio para ventas Cash.
	 * @param tipoCambioVentaTom El tipo de cambio para ventas Tom.
	 * @param tipoCambioVentaSpot El tipo de cambio para ventas Spot.
	 * @param tipoCambioVenta72Hr El tipo de cambio para ventas 72Hr.
	 * @param tipoCambioVentaVFut El tipo de cambio para ventas VFut.
	 * @param swapMn El monto en moneda nacional para Swap.
	 */
	public RepBanxicoInformeBean(String cliente, Date fechaValor,
		Date fechaLiquidacion, Double posicionInicial, String sector,
		Double sectorCash, Double sectorSpot, Double sectorTom,
		Double sector72Hr, Double sectorVFut, Double totalCompras,
		Double totalVentas, InputStream image, Boolean isRecibimos,
		String idDivisa, String flag, Double swap, String conSwaps,
		String sinSwaps, Double posicionInicialCash, Double posicionInicialTom,
		Double posicionInicialSpot, Double posicionInicial72Hr,
		Double posicionInicialVFut, Double posicionFinal,
		Double posicionFinalCash, Double posicionFinalTom,
		Double posicionFinalSpot, Double posicionFinal72Hr,
		Double posicionFinalVFut,Double posicionFinalCompras,
		Double posicionFinalComprasCash, Double posicionFinalComprasTom,
		Double posicionFinalComprasSpot, Double posicionFinalCompras72Hr,
		Double posicionFinalComprasVFut,Double posicionFinalVentas,
		Double posicionFinalVentasCash, Double posicionFinalVentasTom,
		Double posicionFinalVentasSpot, Double posicionFinalVentas72Hr,
		Double posicionFinalVentasVFut,Double tipoCambioCompra,
		Double tipoCambioCompraCash, Double tipoCambioCompraTom,
		Double tipoCambioCompraSpot, Double tipoCambioCompra72Hr,
		Double tipoCambioCompraVFut, Double tipoCambioVenta,
		Double tipoCambioVentaCash, Double tipoCambioVentaTom,
		Double tipoCambioVentaSpot, Double tipoCambioVenta72Hr,
		Double tipoCambioVentaVFut, Double swapMn, Double montoIxeForward,
		Boolean isForward, Double resmerca, Boolean isSectorResmerca,
		Double montoIxeForwardCompras, Double montoIxeForwardVentas,
		String stringresmercaTotalVentas, String resmercaTotalCompras,
		String totalResmercaAgrupadoCpa, String totalResmercaAgrupadoVta) {
		super();
		this.posicionInicial = posicionInicial;
		this.sector = sector;
		this.sectorCash = sectorCash;
		this.sectorSpot = sectorSpot;
		this.sectorTom = sectorTom;
		this.sector72Hr = sector72Hr;
		this.sectorVFut = sectorVFut;
		this.totalCompras = totalCompras;
		this.totalVentas = totalVentas;
		this.image = image;
		this.isRecibimos = isRecibimos;
		this.idDivisa = idDivisa;
		this.flag = flag;
		this.swap = swap;
		this.cliente = cliente;
		this.conSwaps = conSwaps;
		this.sinSwaps = sinSwaps;
		this.fechaValor = fechaValor;
		this.fechaLiquidacion = fechaLiquidacion;
		this.posicionInicialCash = posicionInicialCash;
		this.posicionInicialTom = posicionInicialTom;
		this.posicionInicialSpot = posicionInicialSpot;
		this.posicionInicial72Hr = posicionInicial72Hr;
		this.posicionInicialVFut = posicionInicialVFut;
		this.posicionFinal = posicionFinal;
		this.posicionFinalCash = posicionFinalCash;
		this.posicionFinalTom = posicionFinalTom;
		this.posicionFinalSpot = posicionFinalSpot;
		this.posicionFinal72Hr = posicionFinal72Hr;
		this.posicionFinalVfut = posicionFinalVFut;
		this.posicionFinalCompras = posicionFinalCompras;
		this.posicionFinalComprasCash = posicionFinalComprasCash;
		this.posicionFinalComprasTom = posicionFinalComprasTom;
		this.posicionFinalComprasSpot = posicionFinalComprasSpot;
		this.posicionFinalCompras72Hr = posicionFinalCompras72Hr;
		this.posicionFinalComprasVFut = posicionFinalComprasVFut;
		this.posicionFinalVentas = posicionFinalVentas;
		this.posicionFinalVentasCash = posicionFinalVentasCash;
		this.posicionFinalVentasTom = posicionFinalVentasTom;
		this.posicionFinalVentasSpot = posicionFinalVentasSpot;
		this.posicionFinalVentas72Hr = posicionFinalVentas72Hr;
		this.posicionFinalVentasVFut = posicionFinalVentasVFut;
		this.tipoCambioCompra = tipoCambioCompra;
		this.tipoCambioCompraCash = tipoCambioCompraCash;
		this.tipoCambioCompraTom = tipoCambioCompraTom;
		this.tipoCambioCompraSpot = tipoCambioCompraSpot;
		this.tipoCambioCompra72Hr = tipoCambioCompra72Hr;
		this.tipoCambioCompraVFut = tipoCambioCompraVFut;
		this.tipoCambioVenta = tipoCambioVenta;
		this.tipoCambioVentaCash = tipoCambioVentaCash;
		this.tipoCambioVentaTom = tipoCambioVentaTom;
		this.tipoCambioVentaSpot = tipoCambioVentaSpot;
		this.tipoCambioVenta72Hr = tipoCambioVenta72Hr;
		this.tipoCambioVentaVFut = tipoCambioVentaVFut;
		this.swapMn = swapMn;
		this.montoIxeForward = montoIxeForward;
		this.isForward = isForward;
		this.resmerca = resmerca;
		this.isSectorResmerca = isSectorResmerca;
		this.resmercaTotalVentas = resmercaTotalVentas;
		this.resmercaTotalCompras = resmercaTotalCompras;
		this.totalResmercaAgrupadoCpa = totalResmercaAgrupadoCpa;
		this.totalResmercaAgrupadoVta = totalResmercaAgrupadoVta;
	}
	
	/**
	 * Define si el deal es Forward
	 *
	 * @return Boolean
	 */
	public Boolean getIsForward() {
		return isForward;
	}

	/**
	 * Asigna el valor para isForward.
	 *
	 * @param forward El valor a asignar.
	 */
	public void setIsForward(Boolean forward) {
		this.isForward = forward;
	}

	/**
	 * Regresa el valor de fecha
	 *
	 * @return fecha.
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * Asigna el valor para fecha
	 *
	 * @param fecha La fecha a asignar.
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * Regresa el valor de flag.
     *
     * @return String.
	 */
	public String getFlag() {
		return flag;
	}

	/**
     * Establece el valor de flag.
     *
	 * @param flag La bandera a asignar.
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * Regresa el valor de posicionInicial.
	 *
	 * @return posicionInicial.
	 */
	public Double getPosicionInicial() {
		return posicionInicial;
	}

	/**
	 * Asigna el valor para posicionInicial.
	 *
	 * @param posicionInicial El valor para posicionInicial.
	 */
	public void setPosicionInicial(Double posicionInicial) {
		this.posicionInicial = posicionInicial;
	}

	/**
	 * Regresa el valor de sector
	 *
	 * @return sector.
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
	 * Regresa el valor de conSwaps.
	 *
	 * @return conSwaps.
	 */
	public String getConSwaps() {
		return conSwaps;
	}

	/**
	 * Asigna el valor para conSwaps.
	 *
	 * @param conSwaps El valor para conSwaps.
	 */
	public void setConSwaps(String conSwaps) {
		this.conSwaps = conSwaps;
	}

	/**
	 * Regresa el valor de sinSwaps.
	 *
	 * @return sinSwaps.
	 */
	public String getSinSwaps() {
		return sinSwaps;
	}

	/**
	 * Asigna el valor para sinSwaps.
	 *
	 * @param sinSwaps El valor para sinSwaps
	 */
	public void setSinSwaps(String sinSwaps) {
		this.sinSwaps = sinSwaps;
	}

	/**
	 * Regresa el valor de idDivisa.
	 * @return idDivisa.
	 */
	public String getIdDivisa() {
		return idDivisa;
	}

	/**
	 * Asigna el valor para idDivisa
	 *
	 * @param idDivisa El valor para idDivisa.
	 */
	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}

	/**
	 * Regresa el valor de sectorCash.
	 *
	 * @return sectorCash.
	 */
	public Double getSectorCash() {
		return sectorCash;
	}

	/**
	 * Asigna el valor para sectorCash.
	 *
	 * @param sectorCash El valor para sector cash.
	 */
	public void setSectorCash(Double sectorCash) {
		this.sectorCash = sectorCash;
	}

	/**
	 * Regresa el valor de sectorSpot.
	 *
	 * @return sectorSpot.
	 */
	public Double getSectorSpot() {
		return sectorSpot;
	}

	/**
	 * Asigna el valor para sectorSpot.
	 *
	 * @param sectorSpot El valor para sectorSpot.
	 */
	public void setSectorSpot(Double sectorSpot) {
		this.sectorSpot = sectorSpot;
	}

	/**
	 * Regresa el valor de sectorTom.
	 *
	 * @return sectorTom.
	 */
	public Double getSectorTom() {
		return sectorTom;
	}

	/**
	 * Asigna el valor para sectorTom
	 *
	 * @param sectorTom El valor para sectorTom.
	 */
	public void setSectorTom(Double sectorTom) {
		this.sectorTom = sectorTom;
	}

	/**
	 * Regresa el valor de sector72Hr.
	 *
	 * @return sector72Hr.
	 */
	public Double getSector72Hr() {
		return sector72Hr;
	}

	/**
	 * Asigna el valor para sector72Hr.
	 *
	 * @param sector72Hr El valor para sector72Hr.
	 */
	public void setSector72Hr(Double sector72Hr) {
		this.sector72Hr = sector72Hr;
	}

	/**
	 * Regresa el valor de sectorVFut.
	 *
	 * @return sectorVFut.
	 */
	public Double getSectorVFut() {
		return sectorVFut;
	}

	/**
	 * Asigna el valor para sectorVFut.
	 *
	 * @param sectorVFut El valor para sectorVFut.
	 */
	public void setSectorVFut(Double sectorVFut) {
		this.sectorVFut = sectorVFut;
	}

	/**
	 * Regresa el valor de totalCompras.
	 *
	 * @return totalCompras.
	 */
	public Double getTotalCompras() {
		return totalCompras;
	}

	/**
	 * Asigna el valor para totalCompras.
	 *
	 * @param totalCompras El valor para totalCompras.
	 */
	public void setTotalCompras(Double totalCompras) {
		this.totalCompras = totalCompras;
	}

	/**
	 * Regresa el valor de swap.
	 *
	 * @return swap.
	 */
	public Double getSwap() {
		return swap;
	}

	/**
	 * Asigna el valor para swap
	 *
	 * @param swap El valor para swap.
	 */
	public void setSwap(Double swap) {
		this.swap = swap;
	}

	/**
	 * Regresa el valor de totalVentas.
	 *
	 * @return totalVentas.
	 */
	public Double getTotalVentas() {
		return totalVentas;
	}

	/**
	 * Asigna el valor para totalVentas.
	 *
	 * @param totalVentas El valor de totalVentas.
	 */
	public void setTotalVentas(Double totalVentas) {
		this.totalVentas = totalVentas;
	}

	/**
	 * Regresa el valor de image.
	 *
	 * @return image.
	 */
	public InputStream getImage() {
		return image;
	}

	/**
	 * Asigna el valor para image
	 *
	 * @param image El valor para image.
	 */
	public void setImage(InputStream image) {
		this.image = image;
	}

	/**
	 * Regresa el valor de isRecibimos.
	 *
	 * @return isRecibimos.
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
	 * Regresa el valor de cliente.
	 *
	 * @return cliente.
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
	 * Regresa el valor de fechaLiquidacion.
	 *
	 * @return fechaLiquidacion.
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
	 * Regresa el valor de fechaValor.
	 *
	 * @return fechaValor.
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
	 * Obtiene el monto de la Posici&oacute;n Inicial para Cash.
	 *
	 * @return Double
	 */
	public Double getPosicionInicialCash() {
		return posicionInicialCash;
	}

	/**
	 * Asigna el monto de la Posici&oacute;n Incial para Cash.
	 *
	 * @param posicionInicialCash El monto para la Posici&oacute;n Inicial para Cash.
	 */
	public void setPosicionInicialCash(Double posicionInicialCash) {
		this.posicionInicialCash = posicionInicialCash;
	}

	/**
	 * Obtiene el monto de la Posici&oacute;n Inicial para Tom.
	 *
	 * @return Double
	 */
	public Double getPosicionInicialTom() {
		return posicionInicialTom;
	}

	/**
	 * Asigna el monto de la Posici&oacute;n Incial para Tom.
	 *
	 * @param posicionInicialTom El monto para la Posici&oacute;n Inicial para Tom.
	 */
	public void setPosicionInicialTom(Double posicionInicialTom) {
		this.posicionInicialTom = posicionInicialTom;
	}

	/**
	 * Obtiene el monto de la Posici&oacute;n Inicial para Spot.
	 *
	 * @return Double
	 */
	public Double getPosicionInicialSpot() {
		return posicionInicialSpot;
	}

	/**
	 * Asigna el monto de la Posici&oacute;n Incial para Spot.
	 *
	 * @param posicionInicialSpot El monto para la Posici&oacute;n Inicial para Spot.
	 */
	public void setPosicionInicialSpot(Double posicionInicialSpot) {
		this.posicionInicialSpot = posicionInicialSpot;
	}

	/**
	 * Obtiene el monto de la Posici&oacute;n Inicial para 72Hr.
	 *
	 * @return Double
	 */
	public Double getPosicionInicial72Hr() {
		return posicionInicial72Hr;
	}

	/**
	 * Asigna el monto de la Posici&oacute;n Incial para 72Hr.
	 *
	 * @param posicionInicial72Hr El monto para la Posici&oacute;n Inicial para 72Hr.
	 */
	public void setPosicionInicial72Hr(Double posicionInicial72Hr) {
		this.posicionInicial72Hr = posicionInicial72Hr;
	}

	/**
	 * Obtiene el monto de la Posici&oacute;n Inicial para VFut.
	 *
	 * @return Double
	 */
	public Double getPosicionInicialVFut() {
		return posicionInicialVFut;
	}

	/**
	 * Asigna el monto de la Posici&oacute;n Incial para VFut.
	 *
	 * @param posicionInicialVFut El monto para la Posici&oacute;n Inicial para VFut.
	 */
	public void setPosicionInicialVFut(Double posicionInicialVFut) {
		this.posicionInicialVFut = posicionInicialVFut;
	}

	/**
	 * Obtiene el monto de operaciones Ixe Forward.
	 *
	 * @return Double
	 */
	public Double getMontoIxeForward() {
		return montoIxeForward;
	}

	/**
	 * Asigna el monto para operaciones Ixe Forward.
	 *
	 * @param montoIxeForward El monto para operaciones Ixe Forward.
	 */
	public void setMontoIxeForward(Double montoIxeForward) {
		this.montoIxeForward = montoIxeForward;
	}

	/**
	 * Obtiene el monto para el Tipo de Cambio Promedio para Compras.
	 *
	 * @return Double
	 */
	public Double getTipoCambioCompra() {
		return tipoCambioCompra;
	}

	/**
	 * Asigna el monto para el Tipo de Cambio Promedio para Compras.
	 *
	 * @param tipoCambioCompra El valor para el Tipo de Cambio.
	 */
	public void setTipoCambioCompra(Double tipoCambioCompra) {
		this.tipoCambioCompra = tipoCambioCompra;
	}

	/**
	 * Obtiene el monto para el Tipo de Cambio Promedio
	 * para Compras con fecha valor Cash.
	 *
	 * @return Double
	 */
	public Double getTipoCambioCompraCash() {
		return tipoCambioCompraCash;
	}

	/**
	 * Asigna el monto para el Tipo de Cambio Promedio
	 * para compras con fecha valor Cash.
	 *
	 * @param tipoCambioCompraCash El valor para el Tipo de Cambio.
	 */
	public void setTipoCambioCompraCash(Double tipoCambioCompraCash) {
		this.tipoCambioCompraCash = tipoCambioCompraCash;
	}

	/**
	 * Obtiene el monto para el Tipo de Cambio Promedio
	 * para Compras con fecha valor 72Hr.
	 *
	 * @return Double
	 */
	public Double getTipoCambioCompra72Hr() {
		return tipoCambioCompra72Hr;
	}

	/**
	 * Asigna el monto para el Tipo de Cambio Promedio
	 * para compras con fecha valor 72Hr.
	 *
	 * @param tipoCambioCompra72Hr El valor para el Tipo de Cambio.
	 */
	public void setTipoCambioCompra72Hr(Double tipoCambioCompra72Hr) {
		this.tipoCambioCompra72Hr = tipoCambioCompra72Hr;
	}

	/**
	 * Obtiene el monto para el Tipo de Cambio Promedio
	 * para Compras con fecha valor Spot.
	 *
	 * @return Double
	 */
	public Double getTipoCambioCompraSpot() {
		return tipoCambioCompraSpot;
	}

	/**
	 * Asigna el monto para el Tipo de Cambio Promedio
	 * para compras con fecha valor Spot.
	 *
	 * @param tipoCambioCompraSpot El valor para el Tipo de Cambio.
	 */
	public void setTipoCambioCompraSpot(Double tipoCambioCompraSpot) {
		this.tipoCambioCompraSpot = tipoCambioCompraSpot;
	}

	/**
	 * Obtiene el monto para el Tipo de Cambio Promedio
	 * para Compras con fecha valor Tom.
	 *
	 * @return Double
	 */
	public Double getTipoCambioCompraTom() {
		return tipoCambioCompraTom;
	}

	/**
	 * Asigna el monto para el Tipo de Cambio Promedio
	 * para compras con fecha valor Tom.
	 *
	 * @param tipoCambioCompraTom El valor para el Tipo de Cambio.
	 */
	public void setTipoCambioCompraTom(Double tipoCambioCompraTom) {
		this.tipoCambioCompraTom = tipoCambioCompraTom;
	}

	/**
	 * Obtiene el monto para el Tipo de Cambio Promedio
	 * para Compras con fecha valor VFut.
	 *
	 * @return Double
	 */
	public Double getTipoCambioCompraVFut() {
		return tipoCambioCompraVFut;
	}

	/**
	 * Asigna el monto para el Tipo de Cambio Promedio
	 * para ventas con fecha valor VFut.
	 *
	 * @param tipoCambioCompraVFut El valor para el Tipo de Cambio.
	 */
	public void setTipoCambioCompraVFut(Double tipoCambioCompraVFut) {
		this.tipoCambioCompraVFut = tipoCambioCompraVFut;
	}

	/**
	 * Obtiene el monto para el Tipo de Cambio Promedio para Ventas.
	 *
	 * @return Double
	 */
	public Double getTipoCambioVenta() {
		return tipoCambioVenta;
	}

	/**
	 * Asigna el monto para el Tipo de Cambio Promedio Ventas.
	 *
	 * @param tipoCambioVenta El valor para el Tipo de Cambio.
	 */
	public void setTipoCambioVenta(Double tipoCambioVenta) {
		this.tipoCambioVenta = tipoCambioVenta;
	}

	/**
	 * Obtiene el monto para el Tipo de Cambio Promedio
	 * para Ventas con fecha valor 72Hr.
	 *
	 * @return Double
	 */
	public Double getTipoCambioVenta72Hr() {
		return tipoCambioVenta72Hr;
	}

	/**
	 * Asigna el monto para el Tipo de Cambio Promedio
	 * para Ventas con fecha valor 72Hr.
	 *
	 * @param tipoCambioVenta72Hr El valor para el Tipo de Cambio.
	 */
	public void setTipoCambioVenta72Hr(Double tipoCambioVenta72Hr) {
		this.tipoCambioVenta72Hr = tipoCambioVenta72Hr;
	}

	/**
	 * Obtiene el monto para el Tipo de Cambio Promedio
	 * para Ventas con fecha valor Cash.
	 *
	 * @return Double
	 */
	public Double getTipoCambioVentaCash() {
		return tipoCambioVentaCash;
	}

	/**
	 * Asigna el monto para el Tipo de Cambio Promedio
	 * para Ventas con fecha valor Cash.
	 *
	 * @param tipoCambioVentaCash El valor para el Tipo de Cambio.
	 */
	public void setTipoCambioVentaCash(Double tipoCambioVentaCash) {
		this.tipoCambioVentaCash = tipoCambioVentaCash;
	}

	/**
	 * Obtiene el monto para el Tipo de Cambio Promedio
	 * para Ventas con fecha valor Spot.
	 *
	 * @return Double
	 */
	public Double getTipoCambioVentaSpot() {
		return tipoCambioVentaSpot;
	}

	/**
	 * Asigna el monto para el Tipo de Cambio Promedio
	 * para Ventas con fecha valor Spot.
	 *
	 * @param tipoCambioVentaSpot El valor para el Tipo de Cambio.
	 */
	public void setTipoCambioVentaSpot(Double tipoCambioVentaSpot) {
		this.tipoCambioVentaSpot = tipoCambioVentaSpot;
	}

	/**
	 * Obtiene el monto para el Tipo de Cambio Promedio
	 * para Ventas con fecha valor Tom.
	 *
	 * @return Double
	 */
	public Double getTipoCambioVentaTom() {
		return tipoCambioVentaTom;
	}

	/**
	 * Asigna el monto para el Tipo de Cambio Promedio
	 * para Ventas con fecha valor Tom.
	 *
	 * @param tipoCambioVentaTom El valor para el Tipo de Cambio.
	 */
	public void setTipoCambioVentaTom(Double tipoCambioVentaTom) {
		this.tipoCambioVentaTom = tipoCambioVentaTom;
	}

	/**
	 * Obtiene el monto para el Tipo de Cambio Promedio
	 * para Ventas con fecha valor VFut.
	 *
	 * @return Double
	 */
	public Double getTipoCambioVentaVFut() {
		return tipoCambioVentaVFut;
	}

	/**
	 * Asigna el monto para el Tipo de Cambio Promedio
	 * para Ventas con fecha valor VFut.
	 *
	 * @param tipoCambioVentaVFut El valor para el Tipo de Cambio.
	 */
	public void setTipoCambioVentaVFut(Double tipoCambioVentaVFut) {
		this.tipoCambioVentaVFut = tipoCambioVentaVFut;
	}

	/**
	 * Obtiene el monto en Moneda Nacional para el Swap.
	 *
	 * @return Double
	 */
	public Double getSwapMn() {
		return swapMn;
	}

	/**
	 * Asigna el monto en Moneda Nacional para el Swap.
	 *
	 * @param swapMn El valor del monto para el Swap.
	 */
	public void setSwapMn(Double swapMn) {
		this.swapMn = swapMn;
	}

	/**
	 * Obtiene el valor de Resmerca
	 *
	 * @return Double
	 */
	public Double getResmerca() {
		return resmerca;
	}

	/**
	 * Asigna el valor para resmerca.
	 *
	 *@param resmerca El valor para resmerca.
	 */
	public void setResmerca(Double resmerca) {
		this.resmerca = resmerca;
	}

	/**
	 * Obtiene el valor para isSectorResmerca.
	 *
	 * @return Boolean.
	 */
	public Boolean getIsSectorResmerca() {
		return isSectorResmerca;
	}

	/**
	 * Asigna el valor para isSectorResmerca.
	 *
	 * @param isSectorResmerca El valor para isSectorResmerca.
	 */
	public void setIsSectorResmerca(Boolean isSectorResmerca) {
		this.isSectorResmerca = isSectorResmerca;
	}

    public Double getPosicionFinal() {
		return posicionFinal;
	}

	public void setPosicionFinal(Double posicionFinal) {
		this.posicionFinal = posicionFinal;
	}

	public Double getPosicionFinal72Hr() {
		return posicionFinal72Hr;
	}

	public void setPosicionFinal72Hr(Double posicionFinal72Hr) {
		this.posicionFinal72Hr = posicionFinal72Hr;
	}

	public Double getPosicionFinalCash() {
		return posicionFinalCash;
	}

	public void setPosicionFinalCash(Double posicionFinalCash) {
		this.posicionFinalCash = posicionFinalCash;
	}

	public Double getPosicionFinalCompras() {
		return posicionFinalCompras;
	}

	public void setPosicionFinalCompras(Double posicionFinalCompras) {
		this.posicionFinalCompras = posicionFinalCompras;
	}

	public Double getPosicionFinalCompras72Hr() {
		return posicionFinalCompras72Hr;
	}

	public void setPosicionFinalCompras72Hr(Double posicionFinalCompras72Hr) {
		this.posicionFinalCompras72Hr = posicionFinalCompras72Hr;
	}

	public Double getPosicionFinalComprasCash() {
		return posicionFinalComprasCash;
	}

	public void setPosicionFinalComprasCash(Double posicionFinalComprasCash) {
		this.posicionFinalComprasCash = posicionFinalComprasCash;
	}

	public Double getPosicionFinalComprasSpot() {
		return posicionFinalComprasSpot;
	}

	public void setPosicionFinalComprasSpot(Double posicionFinalComprasSpot) {
		this.posicionFinalComprasSpot = posicionFinalComprasSpot;
	}

	public Double getPosicionFinalComprasTom() {
		return posicionFinalComprasTom;
	}

	public void setPosicionFinalComprasTom(Double posicionFinalComprasTom) {
		this.posicionFinalComprasTom = posicionFinalComprasTom;
	}

	public Double getPosicionFinalComprasVFut() {
		return posicionFinalComprasVFut;
	}

	public void setPosicionFinalComprasVFut(Double posicionFinalComprasVFut) {
		this.posicionFinalComprasVFut = posicionFinalComprasVFut;
	}

	public Double getPosicionFinalSpot() {
		return posicionFinalSpot;
	}

	public void setPosicionFinalSpot(Double posicionFinalSpot) {
		this.posicionFinalSpot = posicionFinalSpot;
	}

	public Double getPosicionFinalTom() {
		return posicionFinalTom;
	}

	public void setPosicionFinalTom(Double posicionFinalTom) {
		this.posicionFinalTom = posicionFinalTom;
	}

	public Double getPosicionFinalVentas() {
		return posicionFinalVentas;
	}

	public void setPosicionFinalVentas(Double posicionFinalVentas) {
		this.posicionFinalVentas = posicionFinalVentas;
	}

	public Double getPosicionFinalVentas72Hr() {
		return posicionFinalVentas72Hr;
	}

	public void setPosicionFinalVentas72Hr(Double posicionFinalVentas72Hr) {
		this.posicionFinalVentas72Hr = posicionFinalVentas72Hr;
	}

	public Double getPosicionFinalVentasCash() {
		return posicionFinalVentasCash;
	}

	public void setPosicionFinalVentasCash(Double posicionFinalVentasCash) {
		this.posicionFinalVentasCash = posicionFinalVentasCash;
	}

	public Double getPosicionFinalVentasSpot() {
		return posicionFinalVentasSpot;
	}

	public void setPosicionFinalVentasSpot(Double posicionFinalVentasSpot) {
		this.posicionFinalVentasSpot = posicionFinalVentasSpot;
	}

	public Double getPosicionFinalVentasTom() {
		return posicionFinalVentasTom;
	}

	public void setPosicionFinalVentasTom(Double posicionFinalVentasTom) {
		this.posicionFinalVentasTom = posicionFinalVentasTom;
	}

	public Double getPosicionFinalVentasVFut() {
		return posicionFinalVentasVFut;
	}

	public void setPosicionFinalVentasVFut(Double posicionFinalVentasVFut) {
		this.posicionFinalVentasVFut = posicionFinalVentasVFut;
	}

	public Double getPosicionFinalVfut() {
		return posicionFinalVfut;
	}

	public void setPosicionFinalVfut(Double posicionFinalVfut) {
		this.posicionFinalVfut = posicionFinalVfut;
	}
	 
	public Double getMontoIxeForwardCompras() {
		return montoIxeForwardCompras;
	}

	public void setMontoIxeForwardCompras(Double montoIxeForwardCompras) {
		this.montoIxeForwardCompras = montoIxeForwardCompras;
	}

	public Double getMontoIxeForwardVentas() {
		return montoIxeForwardVentas;
	}

	public void setMontoIxeForwardVentas(Double montoIxeForwardVentas) {
		this.montoIxeForwardVentas = montoIxeForwardVentas;
	}
	
	public String getResmercaTotalCompras() {
		return resmercaTotalCompras;
	}

	public void setResmercaTotalCompras(String resmercaTotalCompras) {
		this.resmercaTotalCompras = resmercaTotalCompras;
	}

	public String getResmercaTotalVentas() {
		return resmercaTotalVentas;
	}

	public void setResmercaTotalVentas(String resmercaTotalVentas) {
		this.resmercaTotalVentas = resmercaTotalVentas;
	}


	public String getTotalResmercaAgrupadoCpa() {
		return totalResmercaAgrupadoCpa;
	}

	public void setTotalResmercaAgrupadoCpa(String totalResmercaAgrupadoCpa) {
		this.totalResmercaAgrupadoCpa = totalResmercaAgrupadoCpa;
	}

	public String getTotalResmercaAgrupadoVta() {
		return totalResmercaAgrupadoVta;
	}

	public void setTotalResmercaAgrupadoVta(String totalResmercaAgrupadoVta) {
		this.totalResmercaAgrupadoVta = totalResmercaAgrupadoVta;
	}
    
	/**
     * Valor double del monto total de compras
     */
    private Double totalCompras;

    /**
     * Valor double del monto total de ventas
     */
    private Double totalVentas;

    /**
     * Valor double de monto en sector Cash
     */
    private Double sectorCash;

    /**
     * Valor double de monto en sector Tom
     */
    private Double sectorTom;

    /**
     * Valor double de monto en sector Spot
     */
    private Double sectorSpot;

    /**
     * Valor double de monto en sector 72 Horas
     */
    private Double sector72Hr;

    /**
     * Valor double de monto en sector Valor Futuro
     */
    private Double sectorVFut;

    /**
     * Valor del sector economico
     */
    private String sector;

    /**
     * Tipo de la divisa
     */
    private String idDivisa;

    /**
     * Fecha actual
     */
    private Date fecha;

    /**
     * Imagen que es incertada para solicitud de mensajeria, factura, etc.
     */
    private InputStream image;

    /**
     * Define si la operaci&oacute;n es compra o venta
     */
    private Boolean isRecibimos;

    /**
     * Valor de flag
     */
    private String flag;

    /**
     * Define si la operaci&oacute;n es no con swap.
     */
    private Double swap;

    /**
     * Nombre del cliente
     */
    private String cliente;

    /**
     * Valor double del monto con swaps
     */
    private String conSwaps;

    /**
     * Valor double del monto sin swaps
     */
    private String sinSwaps;

    /**
     * Fecha que define el valor del monto (Cash, Tom o Spot)
     */
    private Date fechaValor;

    /**
     * Fecha de liquidaci&oacute;n del Deal
     */
    private Date fechaLiquidacion;

    /**
     * Variable que contiene el monto de operaciones Ixe Forward.
     *
     */
    private Double montoIxeForward;

    /**
     * Valor dpuble de la posicionInicial
     */
    private Double posicionInicial;

    /**
     * Variable para el valor de la Posicion Incial para Cash.
     */
    private Double posicionInicialCash;

    /**
     * Variable para el valor de la Posicion Incial para Tom.
     */
    private Double posicionInicialTom;

    /**
     * Variable para el valor de la Posicion Incial para Spot.
     */
    private Double posicionInicialSpot;

    /**
     * Variable para el valor de la Posicion Incial para 72Hr.
     */
    private Double posicionInicial72Hr;

    /**
     * Variable para el valor de la Posicion Incial para VFut.
     */
    private Double posicionInicialVFut;

    /**
     * Variable para el valor del Tipo de Cambio Promedio Global para Compras.
     */
    private Double tipoCambioCompra;

    /**
     * Variable para el valor del Tipo de Cambio Promedio para Compras
     * para la fecha valor Cash.
     */
    private Double tipoCambioCompraCash;

    /**
     * Variable para el valor del Tipo de Cambio Promedio para Compras
     * para la fecha valor Tom.
     */
    private Double tipoCambioCompraTom;

    /**
     * Variable para el valor del Tipo de Cambio Promedio para Compras
     * para la fecha valor Spot.
     */
    private Double tipoCambioCompraSpot;

    /**
     * Variable para el valor del Tipo de Cambio Promedio para Compras
     * para la fecha valor 72Hr.
     */
    private Double tipoCambioCompra72Hr;

    /**
     * Variable para el valor del Tipo de Cambio Promedio para Compras
     * para la fecha valor VFut.
     */
    private Double tipoCambioCompraVFut;

    /**
     * Variable para el valor del Tipo de Cambio Promedio Global para Ventas.
     */
    private Double tipoCambioVenta;

    /**
     * Variable para el valor del Tipo de Cambio Promedio para Ventas
     * para la fecha valor Cash.
     */
    private Double tipoCambioVentaCash;

    /**
     * Variable para el valor del Tipo de Cambio Promedio para Ventas
     * para la fecha valor Tom.
     */
    private Double tipoCambioVentaTom;

    /**
     * Variable para el valor del Tipo de Cambio Promedio para Ventas
     * para la fecha valor Spot.
     */
    private Double tipoCambioVentaSpot;

    /**
     * Variable para el valor del Tipo de Cambio Promedio para Ventas
     * para la fecha valor 72Hr.
     */
    private Double tipoCambioVenta72Hr;

    /**
     * Variable para el valor del Tipo de Cambio Promedio para Ventas
     * para la fecha valor VFut.
     */
    private Double tipoCambioVentaVFut;

    /**
     * Varialbe para el valor del monto en Moneda Nacional del Swap.
     */
    private Double swapMn;

    /**
     * Variable que define si el detalle es de una operacion Swap.
     */
    private Boolean isForward;

    /**
     * Variable para el valor del monto de los sectores de Resmerca.
     */
    private Double resmerca;

    /**
     * Define si el cliente pertenece al sector para el monto de Resmerca.
     */
    private Boolean isSectorResmerca;
    
	/**
	 *  Valor de la posicion final.
	 */
    private Double posicionFinal;
    
    /**
	 *  Valor de la posicion final Cash.
	 */
    private Double posicionFinalCash;
    
    /**
	 *  Valor de la posicion final Tom.
	 */
    private Double posicionFinalTom;
    
    /**
	 *  Valor de la posicion final Spot.
	 */
    private Double posicionFinalSpot;
    
    /**
	 *  Valor de la posicion final 72 Hrs.
	 */
    private Double posicionFinal72Hr;
    
    /**
	 *  Valor de la posicion final Valor futuro.
	 */
    private Double posicionFinalVfut;
    
    /**
	 *  Valor de la posicion final Para Compras.
	 */
    private Double posicionFinalCompras;
    
    /**
	 *  Valor de la posicion final para Compras Cash.
	 */
    private Double posicionFinalComprasCash;
    
    /**
	 *  Valor de la posicion final para Compras Tom.
	 */
    private Double posicionFinalComprasTom;
    
    /**
	 *  Valor de la posicion final para Compras Spot.
	 */
    private Double posicionFinalComprasSpot;
    
    /**
	 *  Valor de la posicion final para Compras 72 Hrs.
	 */
    private Double posicionFinalCompras72Hr;
    
    /**
	 *  Valor de la posicion final para Compras Valor Futuro.
	 */
    private Double posicionFinalComprasVFut;
    
    /**
	 *  Valor de la posicion final Para Ventas.
	 */
    private Double posicionFinalVentas;
    
    /**
	 *  Valor de la posicion final Para Ventas Cash.
	 */
    private Double posicionFinalVentasCash;
    
    /**
	 *  Valor de la posicion final Para Ventas Tom.
	 */
    private Double posicionFinalVentasTom;
    
    /**
	 *  Valor de la posicion final Para Ventas Spot.
	 */
    private Double posicionFinalVentasSpot;
    
    /**
	 *  Valor de la posicion final Para Ventas Cash 72 Hrs.
	 */
    private Double posicionFinalVentas72Hr;
    
    /**
	 *  Valor de la posicion final Para Ventas Valor Futuro	.
	 */
    private Double posicionFinalVentasVFut;
    
    private Double montoIxeForwardCompras;
    
    private Double montoIxeForwardVentas;
    
    private String resmercaTotalVentas;
    
    private String resmercaTotalCompras;

    private String totalResmercaAgrupadoCpa;
    
    private String totalResmercaAgrupadoVta;

}	

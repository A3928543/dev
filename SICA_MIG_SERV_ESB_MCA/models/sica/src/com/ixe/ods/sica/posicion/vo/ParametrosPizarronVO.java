/*
 * $Id: ParametrosPizarronVO.java,v 1.11 2008/02/22 18:25:26 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */
package com.ixe.ods.sica.posicion.vo;

import java.io.Serializable;

/**
 * Value Object para ParametrosPizarron para la comunicaci&oacute;n del SICA con las applicaciones en Flex (Monitor de la Posici&oacute;n).
 * 
 * @author rchavez
 */
public class ParametrosPizarronVO implements Serializable {

	/**
	 * Constructor que inicializa los valores para VO.
	 * 
	 * @param tipoCambioSpot El valor del tipo de cambio para fecha valor Spot.
	 * @param spreadCompraVenta El valor del spread de compra y venta.
	 * @param carry El valor del carry.
	 * @param tipoCambioCompraCash El valor del tipo de cambio para compra Cash.
	 * @param tipoCambioVentaCash El valor del tipo de cambio para venta Cash.
	 * @param tipoCambioCompraTom El valor del tipo de cambio para compra Tom.
	 * @param tipoCambioVentaTom El valor del tipo de cambio para venta Tom.
	 * @param tipoCambioCompraSpot El valor del tipo de cambio para compra Spot.
	 * @param tipoCambioVentaSpot El valor del tipo de cambio para venta Spot.
	 * @param tipoCambioCompra72Hr El valor del tipo de cambio para compra 72Hr.
	 * @param tipoCambioVenta72Hr El valor del tipo de cambio para venta 72Hr.
	 * @param tipoCambioCompraVFut El valor del tipo de cambio para compra VFut.
	 * @param tipoCambioVentaVFut El valor del tipo de cambio para venta VFut.
	 *  
	 * 
	 */
	public ParametrosPizarronVO(double tipoCambioSpot, double spreadCompraVenta, double carry,
			double tipoCambioCompraCash, double tipoCambioVentaCash, double tipoCambioCompraTom,
			double tipoCambioVentaTom, double tipoCambioCompraSpot, double tipoCambioVentaSpot, 
			double tipoCambioCompra72Hr, double tipoCambioVenta72Hr, double tipoCambioCompraVFut,
			double tipoCambioVentaVFut) {
		this.tipoCambioSpot = tipoCambioSpot;
		this.spreadCompraVenta = spreadCompraVenta;
		this.carry = carry;
		this.tipoCambioCompraCash = tipoCambioCompraCash;
		this.tipoCambioVentaCash = tipoCambioVentaCash;
		this.tipoCambioCompraTom = tipoCambioCompraTom;
		this.tipoCambioVentaTom = tipoCambioVentaTom;
		this.tipoCambioCompraSpot = tipoCambioCompraSpot;
		this.tipoCambioVentaSpot = tipoCambioVentaSpot;
		this.tipoCambioCompra72Hr = tipoCambioCompra72Hr;
		this.tipoCambioVenta72Hr = tipoCambioVenta72Hr;
		this.tipoCambioCompraVFut = tipoCambioCompraVFut;
		this.tipoCambioVentaVFut = tipoCambioVentaVFut;
	}

	/**
	 * @return double
	 */
	public double getTipoCambioSpot() {
		return tipoCambioSpot;
	}

	/**
	 * @return double
	 */
	public double getSpreadCompraVenta() {
		return spreadCompraVenta;
	}

	/**
	 * @return double
	 */
	public double getCarry() {
		return carry;
	}

	/**
	 * @return double
	 */
	public double getTipoCambioCompraCash() {
		return tipoCambioCompraCash;
	}

	/**
	 * @return double
	 */
	public double getTipoCambioVentaCash() {
		return tipoCambioVentaCash;
	}

	/**
	 * @return double
	 */
	public double getTipoCambioCompraTom() {
		return tipoCambioCompraTom;
	}

	/**
	 * @return double
	 */
	public double getTipoCambioVentaTom() {
		return tipoCambioVentaTom;
	}

	/**
	 * @return double
	 */
	public double getTipoCambioCompraSpot() {
		return tipoCambioCompraSpot;
	}

	/**
	 * @return double
	 */
	public double getTipoCambioVentaSpot() {
		return tipoCambioVentaSpot;
	}
	
	/**
	 * @return double
	 */
	public double getTipoCambioCompra72Hr() {
		return tipoCambioCompra72Hr;
	}

	/**
	 * @return double
	 */
	public double getTipoCambioVenta72Hr() {
		return tipoCambioVenta72Hr;
	}

	/**
	 * @return double
	 */
	public double getTipoCambioCompraVFut() {
		return tipoCambioCompraVFut;
	}

	/**
	 * @return double
	 */
	public double getTipoCambioVentaVFut() {
		return tipoCambioVentaVFut;
	}
	
	/**
	 * El tipo de cambio Spot.
	 */
	private double  tipoCambioSpot;
	
	/**
	 * El spread para compra y venta.
	 */
	private double  spreadCompraVenta;
	
	/**
	 * El carry.
	 */
	private double  carry;
	
	/**
	 * El tipo de cambio para Compra Cash.
	 */
	private double  tipoCambioCompraCash;
	
	/**
	 * El tipo de cambio para Venta Cash.
	 */
	private double  tipoCambioVentaCash;
	
	/**
	 * El tipo de cambio para Compra Tom.
	 */
	private double  tipoCambioCompraTom;
	
	/**
	 * El tipo de cambio para Venta Tom.
	 */
	private double  tipoCambioVentaTom;
	
	/**
	 * El tipo de cambio para Compra Spot.
	 */
	private double  tipoCambioCompraSpot;
	
	/**
	 * El tipo de cambio para Venta Spot.
	 */
	private double  tipoCambioVentaSpot;
	
	/**
	 * El tipo de cambio para Compra 72Hr.
	 */
	private double  tipoCambioCompra72Hr;
	
	/**
	 * El tipo de cambio para Venta 72Hr.
	 */
	private double  tipoCambioVenta72Hr;
	
	/**
	 * El tipo de cambio para Compra VFut.
	 */
	private double  tipoCambioCompraVFut;
	
	/**
	 * El tipo de cambio para Venta VFut.
	 */
	private double  tipoCambioVentaVFut;
	
}
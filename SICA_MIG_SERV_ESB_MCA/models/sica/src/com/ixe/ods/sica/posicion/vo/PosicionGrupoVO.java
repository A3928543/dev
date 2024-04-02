/*
 * $Id: PosicionGrupoVO.java,v 1.11 2008/02/22 18:25:26 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.posicion.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Value Object para PosicionGrupo para la comunicaci&oacute;n del SICA con las applicaciones en Flex (Monitor de la Posici&oacute;n).
 * 
 * @author Rogelio Chavez
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:26 $
 */
public class PosicionGrupoVO implements Serializable {

	/**
	 * Constructor que inicializa los valores para VO.
	 * 
	 * @param nombre El nombre del grupo de la posici&oacute;n.
	 * @param compraCash El monto del tipo de cambio para compra Cash.
	 * @param ventaCash El monto del tipo de cambio para venta Cash.
	 * @param compraTom El monto del tipo de cambio para compra Tom.
	 * @param ventaTom El monto del tipo de cambio para venta Tom.
	 * @param compraSpot El monto del tipo de cambio para compra Spot.
	 * @param ventaSpot El monto del tipo de cambio para venta Spot.
	 * @param compra72Hr El monto del tipo de cambio para compra 72Hr.
	 * @param venta72Hr El monto del tipo de cambio para venta 72Hr.
	 * @param compraVFut El monto del tipo de cambio para compra VFut.
	 * @param ventaVFut El monto del tipo de cambio para venta VFut.
	 * @param compraMnClienteCash El monto en moneda nacional del tipo de cambio cliente para compra Cash.
	 * @param ventaMnClienteCash El monto en moneda nacional del tipo de cambio cliente para venta Cash.
	 * @param compraMnClienteTom El monto en moneda nacional del tipo de cambio cliente para compra Tom.
	 * @param ventaMnClienteTom El monto en moneda nacional del tipo de cambio cliente para venta Tom.
	 * @param compraMnClienteSpot El monto en moneda nacional del tipo de cambio cliente para compra Spot.
	 * @param ventaMnClienteSpot El monto en moneda nacional del tipo de cambio cliente para venta Spot.
	 * @param compraMnCliente72Hr El monto en moneda nacional del tipo de cambio cliente para compra 72Hr.
	 * @param ventaMnCliente72Hr El monto en moneda nacional del tipo de cambio cliente para venta 72Hr.
	 * @param compraMnClienteSpotVFut El monto en moneda nacional del tipo de cambio cliente para compra VFut.
	 * @param ventaMnClienteSpotVFut El monto en moneda nacional del tipo de cambio cliente para venta VFut.
	 */
	public PosicionGrupoVO(String nombre, double compraCash, double ventaCash, double compraTom, double ventaTom, 
			double compraSpot, double ventaSpot, double compra72Hr, double venta72Hr, double compraVFut, double ventaVFut,
			double compraMnClienteCash, double ventaMnClienteCash, double compraMnClienteTom, double ventaMnClienteTom, double compraMnClienteSpot,
			double ventaMnClienteSpot, double compraMnCliente72Hr, double ventaMnCliente72Hr, double compraMnClienteVFut, double ventaMnClienteVFut,
			double compraInCash, double ventaInCash, double compraInTom, double ventaInTom, double compraInSpot, double ventaInSpot,
			double compraIn72Hr, double ventaIn72Hr, double compraInVFut, double ventaInVFut) {
		this.nombre = nombre;
		montoCash = new BigDecimal((compraCash - ventaCash)  + "");
		montoTom = new BigDecimal((compraTom - ventaTom) + "");
		montoSpot = new BigDecimal((compraSpot - ventaSpot) + "");
		monto72Hr = new BigDecimal(compra72Hr - venta72Hr + "");
		montoVFut = new BigDecimal(compraVFut - ventaVFut + "");
		montoMnCash = new BigDecimal((compraMnClienteCash - ventaMnClienteCash) + "");
		montoMnTom = new BigDecimal((compraMnClienteTom - ventaMnClienteTom) + "");
		montoMnSpot = new BigDecimal((compraMnClienteSpot - ventaMnClienteSpot) + "");
		montoMn72Hr = new BigDecimal((compraMnCliente72Hr - ventaMnCliente72Hr) + "");
		montoMnVFut = new BigDecimal((compraMnClienteVFut - ventaMnClienteVFut) + "");
		montoInCash = new BigDecimal((compraInCash - ventaInCash) + "");
		montoInTom = new BigDecimal((compraInTom - ventaInTom) + "");
		montoInSpot = new BigDecimal((compraInSpot - ventaInSpot) + "");
		montoIn72Hr = new BigDecimal((compraIn72Hr - ventaIn72Hr) + "");
		montoInVFut = new BigDecimal((compraInVFut - ventaInVFut) + "");
	}

	/**
	 * Regresa el nombre del canal
	 *
	 * @return String
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Regresa el monto cash en la divisa
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMontoCash() {
		return montoCash;
	}

	/**
	 * Asigna el monto cash en la divisa
	 *
	 * @param montoCash
	 */
	public void setMontoCash(BigDecimal montoCash) {
		this.montoCash = montoCash;
	}

	/**
	 * Regresa el monto cash en moneda nacional
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMontoMnCash() {
		return montoMnCash;
	}

	/**
	 * Asigan el monto cash en moneda nacional
	 *
	 * @param montoMnCash
	 */
	public void setMontoMnCash(BigDecimal montoMnCash) {
		this.montoMnCash = montoMnCash;
	}

	/**
	 * Regresa el monto Tom en moneda nacional
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMontoTom() {
		return montoTom;
	}

	/**
	 * Asigna el monto Tom en la divisa
	 *
	 * @param montoTom
	 */
	public void setMontoTom(BigDecimal montoTom) {
		this.montoTom = montoTom;
	}

	/**
	 * Asigan el monto Tom en moneda nacional
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMontoMnTom() {
		return montoMnTom;
	}

	/**
	 * Regresa el monto Tom en moneda nacional
	 *
	 * @param montoMnTom
	 */
	public void setMontoMnTom(BigDecimal montoMnTom) {
		this.montoMnTom = montoMnTom;
	}

	/**
	 * Regresa el monto Spot en moneda nacional
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMontoSpot() {
		return montoSpot;
	}

	/**
	 * Asigna el monto Spot en la divisa
	 *
	 * @param montoSpot
	 */
	public void setMontoSpot(BigDecimal montoSpot) {
		this.montoSpot = montoSpot;
	}

	/**
	 * Regresa el monto Spot en moneda nacional
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMontoMnSpot() {
		return montoMnSpot;
	}

	/**
	 * Asigna el monto Spot en la divisa
	 *
	 * @param montoMnSpot
	 */
	public void setMontoMnSpot(BigDecimal montoMnSpot) {
		this.montoMnSpot = montoMnSpot;
	}

	/**
	 * Regresa el monto Cash interbancario
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMontoInCash() {
		return montoInCash;
	}

	/**
	 * Asigna el monto Cash interbancario
	 *
	 * @param montoInCash
	 */
	public void setMontoInCash(BigDecimal montoInCash) {
		this.montoInCash = montoInCash;
	}

	/**
	 * Regresa el monto Tom interbancario
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMontoInTom() {
		return montoInTom;
	}

	/**
	 * Asigna el monto Tom interbancario
	 *
	 * @param montoInTom
	 */
	public void setMontoInTom(BigDecimal montoInTom) {
		this.montoInTom = montoInTom;
	}

	/**
	 * Regresa el monto Spot interbancario
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMontoInSpot() {
		return montoInSpot;
	}

	/**
	 * Asigna el monto Spot interbancario
	 *
	 * @param montoInSpot
	 */
	public void setMontoInSpot(BigDecimal montoInSpot) {
		this.montoInSpot = montoInSpot;
	}

    /**
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMonto72Hr() {
		return monto72Hr;
	}

    /**
	 *
	 * @param monto72Hr
	 */
	public void setMonto72Hr(BigDecimal monto72Hr) {
		this.monto72Hr = monto72Hr;
	}

    /**
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMontoMn72Hr() {
		return montoMn72Hr;
	}

    /**
	 *
	 * @param montoMn72Hr
	 */
	public void setMontoMn72Hr(BigDecimal montoMn72Hr) {
		this.montoMn72Hr = montoMn72Hr;
	}

    /**
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMontoMnVFut() {
		return montoMnVFut;
	}

    /**
	 *
	 * @param montoMnVFut
	 */
	public void setMontoMnVFut(BigDecimal montoMnVFut) {
		this.montoMnVFut = montoMnVFut;
	}

    /**
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMontoVFut() {
		return montoVFut;
	}

    /**
	 *
	 * @param montoVFut
	 */
	public void setMontoVFut(BigDecimal montoVFut) {
		this.montoVFut = montoVFut;
	}

    /**
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMontoIn72Hr() {
		return montoIn72Hr;
	}

    /**
	 *
	 * @param montoIn72Hr
	 */
	public void setMontoIn72Hr(BigDecimal montoIn72Hr) {
		this.montoIn72Hr = montoIn72Hr;
	}

    /**
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getMontoInVFut() {
		return montoInVFut;
	}

    /**
	 *
	 * @param montoInVFut
	 */
	public void setMontoInVFut(BigDecimal montoInVFut) {
		this.montoInVFut = montoInVFut;
	}

    /**
	 * Nombre del canal
	 */
	private String nombre;

    /**
	 * Monto cash en la divisa
	 */
	private BigDecimal montoCash;

    /**
	 * Monto Cash en moneda nacional
	 */
	private BigDecimal montoMnCash;

    /**
	 * Monto Tom en la divisa
	 */
	private BigDecimal montoTom;

    /**
	 * Monto Tom en moneda nacional
	 */
	private BigDecimal montoMnTom;

    /**
	 * Monto Spot en la divisa
	 */
	private BigDecimal montoSpot;

    /**
	 * Monto Spot en moneda nacional
	 */
	private BigDecimal montoMnSpot;

    /**
	 * Monto 72Hr en la divisa
	 */
	private BigDecimal monto72Hr;

    /**
	 * Monto 72Hr en moneda nacional
	 */
	private BigDecimal montoMn72Hr;

    /**
	 * Monto VFut en la divisa
	 */
	private BigDecimal montoVFut;

    /**
	 * Monto 72Hr en moneda nacional
	 */
	private BigDecimal montoMnVFut;

    /**
	 * Monto Cash Interbancario
	 */
	private BigDecimal montoInCash;

    /**
	 * Monto Tom Interbancario
	 */
	private BigDecimal montoInTom;

    /**
	 * Monto Spot Interbancario
	 */
	private BigDecimal montoInSpot;

    /**
	 * Monto 72 Horas Interbancario
	 */
	private BigDecimal montoIn72Hr;

    /**
	 * Monto Valor Futuro Interbancario
	 */
	private BigDecimal montoInVFut;
}

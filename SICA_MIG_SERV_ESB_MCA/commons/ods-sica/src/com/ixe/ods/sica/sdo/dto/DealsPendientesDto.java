/*
 * $Id: DealsPendientesDto.java,v 1.1 2008/02/22 18:25:40 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.sdo.dto;

import java.io.Serializable;

/**
 * DTO para Deals pendientes por liquidar capturados por el Teller.
 *
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.1 $ $Date: 2008/02/22 18:25:40 $
 */
public class DealsPendientesDto implements Serializable {

    /**
	 * Constructor sin parametros.
	 */
	public DealsPendientesDto() {
		super();
	}

	/**
	 * Constructor que recibe como parametros el idDivisa, descripcion y los montos de compras,
	 * ventas y balance del deal capturado por el Teller.
	 *
	 * @param idDivisa El id de la divisa.
	 * @param descripcion La descripcion de la divisa.
	 * @param compras El monto de compras.
     * @param ventas El monto para ventas.
     */
    public DealsPendientesDto(String idDivisa, String descripcion, double compras, double ventas) {
        this();
        setIdDivisa(idDivisa);
        setDescripcion(descripcion);
		setCompras(compras);
		setVentas(ventas);
	}

	/**
	 * Regresa el monto de compras.
	 *
	 * @return double
	 */
	public double getCompras() {
		return compras;
	}

	/**
	 * Asigna el valor para compras.
	 *
	 * @param compras El valor para compras.
	 */
	public void setCompras(double compras) {
		this.compras = compras;
	}

	/**
	 * Regresa la descripcion de la divisa.
	 *
	 * @return String.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Asigna el valor para la descripcion de la divisa.
	 *
	 * @param descripcion La descripcion de la divisa.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Regresa el valor del id de la divisa.
	 *
	 * @return String.
	 */
	public String getIdDivisa() {
		return idDivisa;
	}

	/**
	 * Asigna el valor para el id de la divisa.
	 *
	 * @param idDivisa El id de la divisa.
	 */
	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}

	/**
	 * Regresa el monto de ventas.
	 *
	 * @return double
	 */
	public double getVentas() {
		return ventas;
	}

	/**
	 * Asigna el valor para ventas.
	 *
	 * @param ventas El valor para ventas.
	 */
	public void setVentas(double ventas) {
		this.ventas = ventas;
	}

	/**
	 * Regresa el valor del balance (compras - ventas).
	 *
	 * @return double
	 */
	public double getBalance() {
		return getCompras() - getVentas();
	}

	/**
	 * Asigna el valor para el balance.
	 *
	 * @param balance El valor para el balance
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}


	/**
	 * Suma el monto para compras o ventas del dto por divisa.
	 *
	 * @param recibimos Define si es compra o venta.
	 * @param monto El monto a sumar.
	 */
	public void sumaMonto(boolean recibimos, double monto) {
		if (recibimos) {
			double compras = getCompras() + monto;
			setCompras(compras);
		}
		else {
			double ventas = getVentas() + monto;
			setVentas(ventas);
		}
	}

	/**
	 * El id de la divisa.
	 */
	private String idDivisa;

	/**
	 * La descripcion de la divisa.
	 */
	private String descripcion;

	/**
	 * El monto de compras.
	 */
	private double compras;

	/**
	 * El monto de ventas.
	 */
	private double ventas;

	/**
	 * El monto del balance
	 */
	private double balance;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -8990936004649026832L;
}

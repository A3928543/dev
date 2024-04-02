/*
 * $Id: BlotterVO.java,v 1.11 2008/02/22 18:25:25 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */
package com.ixe.ods.sica.posicion.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Value Object del Blotter del Monitor de la Posicion para la comunicaci&oacute;n del SICA con las applicaciones en Flex.
 * 
 * @author rchavez
 */
public class BlotterVO implements Serializable {

	/**
	 * Constructor que inicializa los valores para VO.
	 * 
	 * @param numeroDeal El id del deal.
     * @param fechaValor La fecha valor de la operaci&oacute;n.
	 * @param tipoOperacion El tipo de operaci&oacute;n.
	 * @param monto El monto de la operaci&oacute;n.
	 * @param tipoCambioMesa El tipo de cambio para la mesa.
	 * @param tipoCambioCliente El tipo de cambio para el cliente.
	 * @param promotor El nombre del promotor. 
	 * @param cliente El nombre del cliente
	 * @param idDealPosicion El idPosicion del deal.
	 * @param precioReferencia El precio de referencia de la divisa.
	 * @param montoMn El monto en moneda nacional de la operaci&oacute;n
	 * @param divisa La divisa del deal.
	 * @param nombreMesaCambio El nombre de la mesa que realiz&oacute; la opeaci&oacute;n.
	 */
    public BlotterVO(int idPosicionLog, Integer numeroDeal, String fechaValor, String tipoOperacion, 
    		double monto, double tipoCambioMesa, double tipoCambioCliente, String promotor,	String cliente, 
    		int idDealPosicion, double precioReferencia, double montoMn, String divisa, String nombreMesaCambio) {
    	this.idPosicionLog = idPosicionLog;
    	this.numeroDeal = numeroDeal;
        this.fechaValor = fechaValor;
        this.tipoOperacion = tipoOperacion;
    	this.promotor = promotor;
    	this.monto = new BigDecimal(monto + "");
    	this.tipoCambioCliente = new BigDecimal(tipoCambioCliente + "");
    	this.tipoCambioMesa = new BigDecimal(tipoCambioMesa + "");
    	this.cliente = cliente;
    	this.idDealPosicion = idDealPosicion;
    	this.precioReferencia = new BigDecimal(precioReferencia + "");
    	this.montoMn = new BigDecimal(montoMn + "");
    	this.divisa = divisa;
    	this.nombreMesaCambio = nombreMesaCambio;
    }

    /**
     * Regresa el idDealPosicion
     * 
     * @return int
     */
    public int getIdPosicionLog() {
    	return idPosicionLog;
    }

    /**
     * Regresa el nombre del cliente
     * 
     * @return String
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * Regresa el monto de la operacion
     * 
     * @return BigDecimal
     */
    public BigDecimal getMonto() {
        return monto;
    }

    /**
     * Regresa el numero de deal
     * 
     * @return BigDecimal
     */
    public Integer getNumeroDeal() {
        return numeroDeal;
    }

    /**
     * Regresa el nombre del promotor
     * 
     * @return BigDecimal
     */
    public String getPromotor() {
        return promotor;
    }

    /**
     * Regresa el tipo de cambio de la mesa
     * 
     * @return BigDecimal
     */
    public BigDecimal getTipoCambioMesa() {
        return tipoCambioMesa;
    }

    /**
     * Regresa el tipo de cambio del cliente
     * 
     * @return BigDecimal
     */
    public BigDecimal getTipoCambioCliente() {
        return tipoCambioCliente;
    }

    /**
     * Regresa el tipo de operacion
     * 
     * @return String
     */
    public String getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * Regresa el idDealPosicion
     * 
     * @return BigDecimal
     */
    public int getIdDealPosicion() {
    	return idDealPosicion;
    }

    /**
     * Regresa el Monto en Moneda Nacional
     * 
     * @return BigDecimal
     */
    public BigDecimal getMontoMN() {
        return getMonto().multiply(getTipoCambioCliente());
    }

    /**
     * Regresa precioReferencia
     * 
     * @return BigDecimal
     */
    public BigDecimal getPrecioReferencia() {
    	return precioReferencia;
    }

    /**
     * Regresa el monto en moneda nacional
     * 
     * @return BigDecimal
     */
    public BigDecimal getMontoMn() {
    	return montoMn;
    }

    /**
     * Regresa la descripcion de la divisa
     * 
     * @return String 
     */
    public String getDivisa() {
    	return divisa;
    }

    /**
     * Regresa el nombre de la mesa de cambio
     * 
     * @return String 
     */
    public String getNombreMesaCambio() {
    	return nombreMesaCambio;
    }

    /**
     * Regresa el valor de fechaValor.
     *
     * @return String.
     */
    public String getFechaValor() {
        return fechaValor;
    }

    /**
     * Establece el valor de fechaValor.
     *
     * @param fechaValor El valor a asignar.
     */
    public void setFechaValor(String fechaValor) {
        this.fechaValor = fechaValor;
    }

    /**
     * Identificador del log de la posicion
     */
    private int idPosicionLog;
    /**
     * Identificador hacia la tabla de SC_POSICION
     */
	private int idDealPosicion;
	/**
	 * Identificador del deal
	 */
    private Integer numeroDeal;
    /**
     * Tipo de operacion realizada
     */
	private String tipoOperacion;
	/**
	 * Nombre del promotor
	 */
	private String promotor;
	/**
	 * Nombre del cliente
	 */
	private String cliente;
	/**
	 * Monto del deal
	 */
	private BigDecimal monto;
	/**
	 * Tipo de cambio para la mesa
	 */
	private BigDecimal tipoCambioMesa;
	/**
	 * Tipo de cambio otorgado al cliente
	 */
	private BigDecimal tipoCambioCliente;
	/**
	 * Precio de referencia
	 */
	private BigDecimal precioReferencia;

	/**
	 * Monto en Moneda Nacional
	 */
	private BigDecimal montoMn;

	/**
	 * Descripcion de la divisa
	 */
	private String divisa;

	/**
	 * Nombre de la mesa de cambio
	 */
	private String nombreMesaCambio;

    /**
     * La fecha valor: CASH | TOM | SPOT.
     */
    private String fechaValor;
}
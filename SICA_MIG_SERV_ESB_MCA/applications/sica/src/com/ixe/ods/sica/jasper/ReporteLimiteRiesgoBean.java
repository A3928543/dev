/*
 * $Id: ReporteLimiteRiesgoBean.java,v 1.11 2008/03/19 01:16:44 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.Serializable;

/**
 * Bean para generar el reporte de L&iacute;mite de Riesgo
 *
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.11 $ $Date: 2008/03/19 01:16:44 $
 */
public class ReporteLimiteRiesgoBean implements Serializable {

    /**
     * Constructor del bean para el Reporte de L&iacute;mite de Riesgo.
     *
     * @param limReg El limite de riesgo.
     * @param mesaCambio La mesa de cambio.
     * @param nivel El nivel.
     * @param edoSica El estado de SICA.
	 * @param consumo El consumo.
	 * @param divisa La divisa de operaci&oacute;n.
	 * @param tcValuacion El tipo de cambio de valuaci&oacute;n.
	 * @param tcPromedio El tipo de cambio promedio.
	 * @param posicionInicial La posici&oacute;n final.
	 * @param cash El monto para CASH.
	 * @param tom El monto para TOM.
     * @param spot El monto para SPOT.
     * @param posicionFinal El monto de la posici&oacute;n final.
     * @param utilidad El monto de la utilidad.
     * @param spotMas El monto para 72Hr.
     * @param vFut El monto para 96Hr.
     */
    public ReporteLimiteRiesgoBean(String limReg, String mesaCambio, String nivel, String edoSica,
                                   String consumo, String divisa, String tcValuacion,
                                   String tcPromedio, String posicionInicial, String cash,
                                   String tom, String spot, String posicionFinal, String utilidad,
                                   Double spotMas, Double vFut) {
        super();
        this.limReg = limReg;
        this.mesaCambio = mesaCambio;
        this.nivel = nivel;
		this.edoSica = edoSica;
		this.consumo = consumo;
		this.divisa = divisa;
		this.tcValuacion = tcValuacion;
		this.tcPromedio = tcPromedio;
		this.posicionInicial = posicionInicial;
		this.cash = cash;
		this.tom = tom;
		this.spot = spot;
		this.posicionFinal = posicionFinal;
		this.utilidad = utilidad;
		this.spotMas = spotMas;
		this.vFut = vFut;
	}

	/**
	 * Constructor del bean para el Reporte de L&iacute;mite de Riesgo.
	 *
	 * @param limReg El limite de riesgo.
	 * @param mesaCambio La mesa de cambio.
	 * @param nivel El nivel.
	 * @param edoSica El estado de SICA.
	 * @param consumo El consumo.
	 * @param divisa La divisa de operaci&oacute;n.
	 * @param tcValuacion El tipo de cambio de valuaci&oacute;n.
	 * @param tcPromedio El tipo de cambio promedio.
	 * @param posicionInicial La posici&oacute;n final.
	 * @param cash El monto para CASH.
	 * @param tom El monto para TOM.
	 * @param spot El monto para SPOT.
	 * @param posicionFinal El monto de la posici&oacute;n final.
	 * @param utilidad El monto de la utilidad.
     * @param spotMas El monto para 72Hr.
     * @param vFut El monto para 96Hr.
     */
    public ReporteLimiteRiesgoBean(Double limReg, String mesaCambio, Double nivel, String edoSica,
                                   Double consumo, String divisa, Double tcValuacion,
                                   Double tcPromedio, Double posicionInicial, Double cash,
                                   Double tom, Double spot, Double posicionFinal, Double utilidad,
                                   Double spotMas, Double vFut) {
        super();
        this.limRegDouble = limReg;
        this.mesaCambio = mesaCambio;
        this.nivelDouble = nivel;
        this.edoSica = edoSica;
		this.consumoDouble = consumo;
		this.divisa = divisa;
		this.tcValuacionDouble = tcValuacion;
		this.tcPromedioDouble = tcPromedio;
		this.posicionInicialDouble = posicionInicial;
		this.cashDouble = cash;
		this.tomDouble = tom;
		this.spotDouble = spot;
		this.posicionFinalDouble = posicionFinal;
		this.utilidadDouble = utilidad;
		this.spotMas = spotMas;
		this.vFut = vFut;
	}

	/**
	 * Regresa el valor de cash.
	 *
	 * @return String.
	 */
	public String getCash() {
		return cash;
	}

	/**
	 * Asigna el valor para cash.
	 *
	 * @param cash El valor para cash.
	 */
	public void setCash(String cash) {
		this.cash = cash;
	}

	/**
	 * Regresa el valor de cashDouble.
	 *
	 * @return Double.
	 */
	public Double getCashDouble() {
		return cashDouble;
	}

	/**
	 * Asigna el valor para cashDouble.
	 *
	 * @param cashDouble El valor para cashDouble.
	 */
	public void setCashDouble(Double cashDouble) {
		this.cashDouble = cashDouble;
	}

	/**
	 * Regresa el valor de consumo.
	 *
	 * @return String.
	 */
	public String getConsumo() {
		return consumo;
	}

	/**
	 * Asigna el valor para consumo.
	 *
	 * @param consumo El valor para consumo.
	 */
	public void setConsumo(String consumo) {
		this.consumo = consumo;
	}

	/**
	 * Regresa el valor de consumoDouble.
	 *
	 * @return Double.
	 */
	public Double getConsumoDouble() {
		return consumoDouble;
	}

	/**
	 * Asigna el valor para consumoDouble.
	 *
	 * @param consumoDouble El valor para consumoDouble.
	 */
	public void setConsumoDouble(Double consumoDouble) {
		this.consumoDouble = consumoDouble;
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
	 * Regresa el valor de edoSica.
	 *
	 * @return String.
	 */
	public String getEdoSica() {
		return edoSica;
	}

	/**
	 * Asigna el valor para edoSica.
	 *
	 * @param edoSica El valor para edoSica.
	 */
	public void setEdoSica(String edoSica) {
		this.edoSica = edoSica;
	}

	/**
	 * Regresa el valor de limReg.
	 *
	 * @return String.
	 */
	public String getLimReg() {
		return limReg;
	}

	/**
	 * Asigna el valor para limReg.
	 *
	 * @param limReg El valor para limReg.
	 */
	public void setLimReg(String limReg) {
		this.limReg = limReg;
	}

	/**
	 * Regresa el valor de limRegDouble.
	 *
	 * @return Double.
	 */
	public Double getLimRegDouble() {
		return limRegDouble;
	}

	/**
	 * Asigna el valor para limRegDouble.
	 *
	 * @param limRegDouble El valor para limRegDouble.
	 */
	public void setLimRegDouble(Double limRegDouble) {
		this.limRegDouble = limRegDouble;
	}

	/**
	 * Regresa el valor de mesaCambio.
	 *
	 * @return String.
	 */
	public String getMesaCambio() {
		return mesaCambio;
	}

	/**
	 * Asigna el valor para mesaCambio.
	 *
	 * @param mesaCambio El valor para mesaCambio.
	 */
	public void setMesaCambio(String mesaCambio) {
		this.mesaCambio = mesaCambio;
	}

	/**
	 * Regresa el valor de nivel.
	 *
	 * @return String.
	 */
	public String getNivel() {
		return nivel;
	}

	/**
	 * Asigna el valor para nivel.
	 *
	 * @param nivel El valor para nivel.
	 */
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	/**
	 * Regresa el valor de nivelDouble.
	 *
	 * @return Double.
	 */
	public Double getNivelDouble() {
		return nivelDouble;
	}

	/**
	 * Asigna el valor para nivelDouble.
	 *
	 * @param nivelDouble El valor para nivelDouble.
	 */
	public void setNivelDouble(Double nivelDouble) {
		this.nivelDouble = nivelDouble;
	}

	/**
	 * Regresa el valor de posicionFinal.
	 *
	 * @return String.
	 */
	public String getPosicionFinal() {
		return posicionFinal;
	}

	/**
	 * Obtiene el valor para 72Hrs
	 *
	 * @return Double
	 */
	public Double getSpotMas() {
		return spotMas;
	}

	/**
	 * Asigna el valor para 72Hr
	 *
	 * @param spotMas El valor para 72Hr
	 */
	public void setSpotMas(Double spotMas) {
		this.spotMas = spotMas;
	}

	/**
	 * Obtiene el valor para Valor Futuro
	 *
	 * @return Double
	 */
	public Double getVFut() {
		return vFut;
	}

	/**
	 * Asigna el valor para valor futuro
	 *
	 * @param vFut El valor para vFut
	 */
	public void setVFut(Double vFut) {
		this.vFut = vFut;
	}

	/**
	 * Asigna el valor para posicionFinal.
	 *
	 * @param posicionFinal El valor para posicionFinal.
	 */
	public void setPosicionFinal(String posicionFinal) {
		this.posicionFinal = posicionFinal;
	}

	/**
	 * Regresa el valor de posicionFinalDouble.
	 *
	 * @return Double.
	 */
	public Double getPosicionFinalDouble() {
		return posicionFinalDouble;
	}

	/**
	 * Asigna el valor para posicionFinalDouble.
	 *
	 * @param posicionFinalDouble El valor para posicionFinalDouble.
	 */
	public void setPosicionFinalDouble(Double posicionFinalDouble) {
		this.posicionFinalDouble = posicionFinalDouble;
	}

	/**
	 * Regresa el valor de posicionInicial.
	 *
	 * @return String.
	 */
	public String getPosicionInicial() {
		return posicionInicial;
	}

	/**
	 * Asigna el valor para posicionInicial.
	 *
	 * @param posicionInicial El valor para posicionInicial.
	 */
	public void setPosicionInicial(String posicionInicial) {
		this.posicionInicial = posicionInicial;
	}

	/**
	 * Regresa el valor de posicionInicialDouble.
	 *
	 * @return Double.
	 */
	public Double getPosicionInicialDouble() {
		return posicionInicialDouble;
	}

	/**
	 * Asigna el valor para posicionInicialDouble.
	 *
	 * @param posicionInicialDouble El valor para posicionInicialDouble.
	 */
	public void setPosicionInicialDouble(Double posicionInicialDouble) {
		this.posicionInicialDouble = posicionInicialDouble;
	}

	/**
	 * Regresa el valor de spot.
	 *
	 * @return String.
	 */
	public String getSpot() {
		return spot;
	}

	/**
	 * Asigna el valor para spot.
	 *
	 * @param spot El valor para spot.
	 */
	public void setSpot(String spot) {
		this.spot = spot;
	}

	/**
	 * Regresa el valor de spotDouble.
	 *
	 * @return Double.
	 */
	public Double getSpotDouble() {
		return spotDouble;
	}

	/**
	 * Asigna el valor para spotDouble.
	 *
	 * @param spotDouble El valor para spotDouble.
	 */
	public void setSpotDouble(Double spotDouble) {
		this.spotDouble = spotDouble;
	}

	/**
	 * Regresa el valor de tcPromedio.
	 *
	 * @return String.
	 */
	public String getTcPromedio() {
		return tcPromedio;
	}

	/**
	 * Asigna el valor para tcPromedio.
	 *
	 * @param tcPromedio El valor para tcPromedio.
	 */
	public void setTcPromedio(String tcPromedio) {
		this.tcPromedio = tcPromedio;
	}

	/**
	 * Regresa el valor de tcPromedioDouble.
	 *
	 * @return Double.
	 */
	public Double getTcPromedioDouble() {
		return tcPromedioDouble;
	}

	/**
	 * Asigna el valor para tcPromedioDouble.
	 *
	 * @param tcPromedioDouble El valor para tcPromedioDouble.
	 */
	public void setTcPromedioDouble(Double tcPromedioDouble) {
		this.tcPromedioDouble = tcPromedioDouble;
	}

	/**
	 * Regresa el valor de tcValuacion.
	 *
	 * @return String.
	 */
	public String getTcValuacion() {
		return tcValuacion;
	}

	/**
	 * Asigna el valor para tcValuacion.
	 *
	 * @param tcValuacion El valor para tcValuacion.
	 */
	public void setTcValuacion(String tcValuacion) {
		this.tcValuacion = tcValuacion;
	}

	/**
	 * Regresa el valor de tcValuacionDouble.
	 *
	 * @return Double.
	 */
	public Double getTcValuacionDouble() {
		return tcValuacionDouble;
	}

	/**
	 * Asigna el valor para tcValuacionDouble.
	 *
	 * @param tcValuacionDouble El valor para tcValuacionDouble.
	 */
	public void setTcValuacionDouble(Double tcValuacionDouble) {
		this.tcValuacionDouble = tcValuacionDouble;
	}

	/**
	 * Regresa el valor de tom.
	 *
	 * @return String.
	 */
	public String getTom() {
		return tom;
	}

	/**
	 * Asigna el valor para tom.
	 *
	 * @param tom El valor para tom.
	 */
	public void setTom(String tom) {
		this.tom = tom;
	}

	/**
	 * Regresa el valor de tomDouble.
	 *
	 * @return Double.
	 */
	public Double getTomDouble() {
		return tomDouble;
	}

	/**
	 * Asigna el valor para tomDouble.
	 *
	 * @param tomDouble El valor para tomDouble.
	 */
	public void setTomDouble(Double tomDouble) {
		this.tomDouble = tomDouble;
	}

	/**
	 * Regresa el valor de utilidad.
	 *
	 * @return String.
	 */
	public String getUtilidad() {
		return utilidad;
	}

	/**
	 * Asigna el valor para utilidad.
	 *
	 * @param utilidad El valor para utilidad.
	 */
	public void setUtilidad(String utilidad) {
		this.utilidad = utilidad;
	}

	/**
	 * Regresa el valor de utilidadDouble.
	 *
	 * @return Double.
	 */
	public Double getUtilidadDouble() {
		return utilidadDouble;
	}

	/**
	 * Asigna el valor para utilidadDouble.
	 *
	 * @param utilidadDouble El valor para utilidadDouble.
	 */
	public void setUtilidadDouble(Double utilidadDouble) {
		this.utilidadDouble = utilidadDouble;
	}

	/**
	 * El limite registrado.
	 */
	private String limReg;

	/**
	 * Mesa de de cambio.
	 */
	private String mesaCambio;

	/**
	 * Nivel.
	 */
	private String nivel;

	/**
	 * Estado del SICA
	 */
	private String edoSica;

	/**
	 * Consumo registrado.
	 */
	private String consumo;

	/**
	 * Divisa de operaci&oacute;n.
	 */
	private String divisa;

	/**
	 * Tipo de cambio de valuaci&oacute;n.
	 */
	private String tcValuacion;

	/**
	 * Posici&oacute;n final.
	 */
	private String posicionFinal;

	/**
	 * Utilidad final.
	 */
	private String utilidad;

	/**
	 * Tipo de cambio promedio.
	 */
	private String tcPromedio;

	/**
	 * Posici&oacute;n final.
	 */
	private String posicionInicial;

	/**
	 * Monto CASH.
	 */
	private String cash;

	/**
	 * Monto TOM.
	 */
	private String tom;

	/**
	 * Monto SPOT.
	 */
	private String spot;

	/**
	 * Monto 72 Hr
	 */
	private Double spotMas;

	/**
	 * Monto Valor Futuro
	 */
	private Double vFut;

	/**
	 * Limite registrado Double.
	 */
	private Double limRegDouble;

	/**
	 * Nivel Double.
	 */
	private Double nivelDouble;

	/**
	 * Consumo Double.
	 */
	private Double consumoDouble;

	/**
	 * Tipo cambio valuaci&oacute;n Double.
	 */
	private Double tcValuacionDouble;

	/**
	 * Posici&oacute;n final double
	 */
	private Double posicionFinalDouble;

	/**
	 * Utilidad final Double.
	 */
	private Double utilidadDouble;

	/**
	 * Tipo de cambio promedio Double.
	 */
	private Double tcPromedioDouble;

	/**
	 * Posicion Inicial Double.
	 */
	private Double posicionInicialDouble;

	/**
	 * Monto CASH Double.
	 */
	private Double cashDouble;

	/**
	 * Monto TOM Double.
	 */
	private Double tomDouble;

	/**
	 * Monto SPOT Double
	 */
	private Double spotDouble;

    /**
     * EL UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -6345157818996462795L;
}
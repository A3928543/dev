/*
 * $Id: HistoricoDealPosicion.java,v 1.12 2008/02/22 18:25:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;


import java.io.Serializable;

/**
 *
 * @hibernate.class table="SC_H_DEAL_POSICION"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoDealPosicion"
 * dynamic-update="true"
 *
 * @author Edgar Leija
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:23 $
 */
public class HistoricoDealPosicion implements Serializable {

	/**
     * Constructor vacio.
     */
    public HistoricoDealPosicion() {
    	super();
    }
	/**
     * Constructor que recibe un Deal Detalle e inicializa todas sus variables.
     * 
     * @param dd El detalle de deal que inicializa el hist&oacute;rico.
     */
    public HistoricoDealPosicion(DealDetalle dd) {
        super();
        setIdDealPosicion(dd.getIdDealPosicion());
        setIdUsuario(dd.getIdUsuario());
        setMonto(dd.getMonto());
        setRecibimos(dd.isRecibimos());
        setObservaciones(dd.getObservaciones()!= null ? dd.getObservaciones(): null);
        setTipoCambio(dd.getTipoCambio());
        setTipoDeal(dd.getTipoDeal());
        setDivisa(dd.getDivisa().getIdDivisa());
        setMesaCambio(dd.getMesaCambio().getIdMesaCambio());
    }

    /**
     * Regresa el valor de idDeal.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_DEAL_POSICION"
     * unsaved-value="null"
     * @return int.
     */
    public int getIdDealPosicion() {
        return idDealPosicion;
    }


    /**
     * Fija el valor de idDealPosicion.
     *
     * @param idDealPosicion El valor a asignar.
     */
    public void setIdDealPosicion(int idDealPosicion) {
        this.idDealPosicion = idDealPosicion;
    }

    /**
     * Regresa el valor de idUsuario.
     *
     * @hibernate.property column="ID_USUARIO"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Fija el valor de idUsuario.
     *
     * @param idUsuario El valor a asignar.
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Regresa el valor de monto.
     *
     * @hibernate.property column="MONTO"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Fija el valor de monto.
     *
     * @param monto El valor a asignar.
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * Regresa el valor de recibimos.
     *
     * @hibernate.property column="RECIBIMOS"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isRecibimos() {
        return recibimos;
    }

    /**
     * Fija el valor de recibimos.
     *
     * @param recibimos El valor a asignar.
     */
    public void setRecibimos(boolean recibimos) {
        this.recibimos = recibimos;
    }

    /**
     * Regresa el valor de observaciones.
     *
     * @hibernate.property column="OBSERVACIONES"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Fija el valor de observaciones.
     *
     * @param observaciones El valor a asignar.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Regresa el valor de tipoCambio.
     *
     * @hibernate.property column="TIPO_CAMBIO"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getTipoCambio() {
        return tipoCambio;
    }

    /**
     * Fija el valor de tipoCambio.
     *
     * @param tipoCambio El valor a asignar.
     */
    public void setTipoCambio(double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    /**
     * Regresa el valor de tipoDeal.
     *
     * @hibernate.property column="TIPO_DEAL"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoDeal() {
        return tipoDeal;
    }

    /**
     * Fija el valor de tipoDeal.
     *
     * @param tipoDeal El valor a asignar.
     */
    public void setTipoDeal(String tipoDeal) {
        this.tipoDeal = tipoDeal;
    }

    /**
     * Regresa el valor de tipoDeal.
     *
     * @hibernate.property column="ID_DIVISA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getDivisa() {
        return _divisa;
    }

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(String divisa) {
        _divisa = divisa;
    }

    /**
     * Regresa el valor de tipoDeal.
     *
     * @hibernate.property column="ID_MESA_CAMBIO"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getMesaCambio() {
        return _mesaCambio;
    }

    /**
     * Fija el valor de mesaCambio.
     *
     * @param mesaCambio El valor a asignar.
     */
    public void setMesaCambio(int mesaCambio) {
        _mesaCambio = mesaCambio;
    }

    /**
     * El identificador del registro.
     */
    protected int idDealPosicion;

    /**
     * El identificador del &uacute;ltimo usuario que modific&oacute; el
     * registro.
     */
    private int idUsuario;

    /**
     * Monto en la divisa que modific&oacute; PosicionLog.
     */
    private double monto;

    /**
     * True compra | False venta.
     */
    private boolean recibimos;

    /**
     * Notas en general opcionales.
     */
    private String observaciones;

    /**
     * Tipo del cambio del cliente.
     */
    private double tipoCambio;

    /**
     * C)liente, I)nterbancario, T)raspaso.
     */
    private String tipoDeal;

    /**
     * Relaci&oacute;n muchos-a-uno con Divisa.
     */
    private String _divisa;

    /**
     * Relaci&oacute;n muchos-a-uno con MesaCambio.
     */
    private int _mesaCambio;

    /**
     * Constante para representar el Tipo de Deal Cliente.
     */
    public final static String TIPO_DEAL_CLIENTE = "C";

    /**
     * Constante  para representar el Tipo de Deal Interbancario
     */
    public final static String TIPO_DEAL_INTERBANCARIO = "I";

    /**
     * Constante para representar el Tipo de Deal Traspaso
     */
    public final static String TIPO_DEAL_TRASPASO = "T";
    
    /**
     * Constante para representar el Tipo de Deal Utilidades
     */
    public final static String TIPO_DEAL_UTILIDADES = "U";
    
}

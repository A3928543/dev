/*
 * $Id: PosicionLogCorte.java,v 1.1.4.2 2014/10/29 16:38:54 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;


import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="SC_POSICION_LOG_CORTES"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.PosicionLogCorte"
 * dynamic-update="true"
 *
 * @hibernate.query name="findMaxIdPosicionLog"
 * query="SELECT max (pl.idPosicionLog) FROM PosicionLogCorte AS pl " 
 * 
 * @author JDCH
 * @version  $Revision: 1.1.4.2 $ $Date: 2014/10/29 16:38:54 $
 */
public class PosicionLogCorte implements Serializable {

    /**
     * Constructor por default.
     */
    public PosicionLogCorte() {
        super();
    }
    
    /**
     * Constructor que recibe los parametros necesarios para generar el log de la Posici&oacute;n Corte.
     * 
     * @param det El valor de PosicionLog.
     * @param precioReferencia El precio de referencia de la divisa.
     */
    public PosicionLogCorte(PosicionLog pl) {
    	this.idPosicionLog = pl.getIdPosicionLog();
        this.tipoValor = pl.getTipoValor();
        this._deal = pl.getDeal().getIdDeal();
        this._dealPosicion = pl.getDealPosicion().getIdDealPosicion();
        this._divisa = pl.getDivisa().getIdDivisa();
        this.monto = pl.getMonto();
        this.montoMn = pl.getMontoMn();
        this.precioReferencia = pl.getPrecioReferencia();
        this.tipoCambioCliente = pl.getTipoCambioCliente();
        this.tipoCambioMesa = pl.getTipoCambioMesa();
        this.tipoOperacion = pl.getTipoOperacion();
        this.fecha = pl.getFecha();        
    }
    
 
    
    /**
     * Define si la operaci&oacute;n es compra.
     * 
     * @return boolean
     */
    public boolean isCompra() {
        return COMPRA.equals(getTipoOperacion()) || COMPRA_INTERBANCARIA.equals(getTipoOperacion());
    }

    /**
     * Define si la operaci&oacute;n es venta.
     * 
     * @return boolean
     */
    public boolean isVenta() {
        return VENTA.equals(getTipoOperacion()) || VENTA_INTERBANCARIA.equals(getTipoOperacion());
    }
    
    /**
     * Regresa el valor de idPosicionLogCorte.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_POSICION_LOG_CORTE"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_POSICION_LOG_CORTES_SEQ" 		  
     * @return int.
     */
    public int getIdPosicionLogCorte() {
        return idPosicionLogCorte;
    }

    /**
     * Fija el valor de idPosicionLogCorte.
     *
     * @param idPosicionLogCorte El valor a asignar.
     */
    public void setIdPosicionLogCorte(int idPosicionLogCorte) {
        this.idPosicionLogCorte = idPosicionLogCorte;
    }

    /**
     * Regresa el valor de idPosicionLog.
     * @hibernate.property column="ID_POSICION_LOG"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getIdPosicionLog() {
        return idPosicionLog;
    }

    /**
     * Fija el valor de idPosicionLog.
     *
     * @param idPosicionLog El valor a asignar.
     */
    public void setIdPosicionLog(int idPosicionLog) {
        this.idPosicionLog = idPosicionLog;
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
     * Regresa el valor de montoMn.
     *
     * @hibernate.property column="MONTO_MN"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getMontoMn() {
        return montoMn;
    }

    /**
     * Fija el valor de montoMn.
     *
     * @param montoMn El valor a asignar.
     */
    public void setMontoMn(double montoMn) {
        this.montoMn = montoMn;
    }



    /**
     * Regresa el valor de precioReferencia.
     *
     * @hibernate.property column="PRECIO_REFERENCIA"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getPrecioReferencia() {
        return precioReferencia;
    }

    /**
     * Fija el valor de precioReferencia.
     *
     * @param precioReferencia El valor a asignar.
     */
    public void setPrecioReferencia(double precioReferencia) {
        this.precioReferencia = precioReferencia;
    }

    /**
     * Regresa el valor de tipoOperacion.
     *
     * @hibernate.property column="TIPO_OPERACION"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * Fija el valor de tipoOperacion.
     *
     * @param tipoOperacion El valor a asignar.
     */
    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    /**
     * Regresa el valor de tipoCambioCliente.
     *
     * @hibernate.property column="TIPO_CAMBIO_CLIENTE"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getTipoCambioCliente() {
        return tipoCambioCliente;
    }

    /**
     * Fija el valor de tipoCambioCliente.
     *
     * @param tipoCambioCliente El valor a asignar.
     */
    public void setTipoCambioCliente(double tipoCambioCliente) {
        this.tipoCambioCliente = tipoCambioCliente;
    }

    /**
     * Regresa el valor de tipoCambioCliente.
     *
     * @hibernate.property column="TIPO_CAMBIO_MESA"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getTipoCambioMesa() {
        return tipoCambioMesa;
    }

    /**
     * Fija el valor det tipoCambioMesa.
     *
     * @param tipoCambioMesa El valor a asignar.
     */
    public void setTipoCambioMesa(double tipoCambioMesa) {
        this.tipoCambioMesa = tipoCambioMesa;
    }

    /**
     * Regresa el valor de tipoValor.
     *
     * @hibernate.property column="TIPO_VALOR"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoValor() {
        return tipoValor;
    }

    /**
     * Fija el valor de tipoValor.
     *
     * @param tipoValor El valor a asignar.
     */
    public void setTipoValor(String tipoValor) {
        this.tipoValor = tipoValor;
    }

    /**
     * Regresa el valor de dealPosicion.
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getDealPosicion() {
        return _dealPosicion;
    }

    /**
     * Fija el valor de dealPosicion.
     *
     * @param int El valor a asignar.
     */
    public void setDealPosicion(int dealPosicion) {
        _dealPosicion = dealPosicion;
    }

    /**
     * Regresa el valor de idDeal.
     *
     * @hibernate.property column="ID_DEAL"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getDeal() {
        return _deal;
    }

    /**
     * Fija el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public void setDeal(int deal) {
        _deal = deal;
    }

    /**
     * Regresa el valor de divisa.
     * @hibernate.property column="ID_DIVISA"
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
     * Regresa el valor de corte.
     * @hibernate.property column="ID_CORTE"
     * unique="false"
     * @return Integer.
     */
    public Integer getCorte() {
        return _corte;
    }

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setCorte(Integer corte) {
        _corte = corte;
    }

    /**
     * Regresa el valor de fecha.
     *
     * @hibernate.property column="FECHA"
     * not-null="false"
     * unique="false"
     * @return Date.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Fija el valor de fecha.
     *
     * @param fecha El valor a asignar.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

//    /**
//     * Regresa true si ambos objetos son iguales.
//     *
//     * @param other El otro objeto a comparar.
//     * @see java.lang.Object#equals(java.lang.Object).
//     * @return boolean.
//     */
//    public boolean equals(Object other) {
//        if (!(other instanceof PosicionLog)) {
//            return false;
//        }
//        PosicionLog castOther = (PosicionLog) other;
//        return new EqualsBuilder().append(this.getIdPosicionLog(),
//                castOther.getIdPosicionLog()).isEquals();
//    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdPosicionLog()).toHashCode();
    }

//    /**
//     * Regresa la descripci&oacute;n de los campos del objeto.
//     *
//     * @return String.
//     */
//    public String toString() {
//        return new ToStringBuilder(this).append("idPosicionLog", idPosicionLog).
//                append("cfl", claveFormaLiquidacion).append("idUsuario", idUsuario).
//                append("monto", monto).append("montoMn", montoMn).
//                append("nombreCliente", nombreCliente).append("precioReferencia", precioReferencia).
//                append("tcc", tipoCambioCliente).append("tipoCambioMesa", tipoCambioMesa).
//                append("tipoOperacion", tipoOperacion).
//                append("divisa", _divisa.getIdDivisa()).toString();
//    }
    
    /**
     * El identificador del registro.
     */
    private int idPosicionLogCorte;

    /**
     * El identificador de la posicion log.
     */
    private int idPosicionLog;

   /**
     * El monto del detalle del deal.
     */
    private double monto;

    /**
     * El monto del detalle del deal en moneda nacional.
     */
    private double montoMn;


    /**
     * Precio de referencia SPOT en el momento de la captura.
     */
    private double precioReferencia;

    /**
     * El tipo de cambio pactado con el cliente.
     */
    private double tipoCambioCliente;

    /**
     * El tipo de cambio que exist&iacute;a en la Mesa de Cambios en el momento
     * de captura (este campo no tiene funcionalidad para los deals
     * interbancarios que son las operaciones de CI y VI). Su funci&oacute;n es
     * &uacute;nicamente de referencia y c&aacute;lculo de la utilidad por deal.
     *
     */
    private double tipoCambioMesa;

    /**
     * El tipo de operaci&oacute;n (COMPRA | VENTA | COMPRA_INTERBANCARIA |
     * VENTA_INTERBANCARIA | CANCELACION).
     */
    private String tipoOperacion;

    /**
     * C)ash | T)om | S)pot.
     */
    private String tipoValor;

    /**
     * Relaci&oacute;n muchos-a-uno con Deal.
     */
    private int _deal;

    /**
     * Relaci&oacute;n muchos-a-uno con DealPosicion.
     */
    private int _dealPosicion;

    /**
     * Relaci&oacute;n muchos a uno con Divisa.
     */
    private String _divisa;

    /**
     * La Fecha de registro del Log.
     */
    private Date fecha = new Date();
    
    /**
     * El id del Corte
     */
    private Integer _corte;
    
    /**
     * Constante que define el tipo de operaci&oacute;n como compra.
     */
    public static final String COMPRA = "CO";
    
    /**
     * Constante que define el tipo de operaci&oacute;n como venta.
     */
    public static final String VENTA = "VE";
    
    /**
     * Constante que define el tipo de operaci&oacute;n como compra interbancaria.
     */
    public static final String COMPRA_INTERBANCARIA = "CI";
    
    /**
     * Constante que define el tipo de operaci&oacute;n como venta interbancaria.
     */
    public static final String VENTA_INTERBANCARIA = "VI";
    
    /**
     * Constante que define el tipo de operaci&oacute;n como compra entre portafolios (traspaso).
     */
    public static final String COMPRA_TRASPASO_PORTAFOLIOS = "CM";
    
    /**
     * Constante que define el tipo de operaci&oacute;n como venta entre portafolios (traspaso).
     */
    public static final String VENTA_TRASPASO_PORTAFOLIOS = "VM";

    /**
     * Reconocimiento de Utilidades D&oacute;lares
     */
    public static final String DOLARES_UTILIDADES = "DU";

    /**
     * Reconocimiento de Utilidades Pesos
     */
    public static final String PESOS_UTILIDADES = "PU";

    /**
     * Reconocimiento de P&eacute;rdida D&oacute;lares
     */
    public static final String DOLARES_PERDIDA = "DP";

    /**
     * Reconocimiento de P&eacute;rdida Pesos
     */
    public static final String PESOS_PERDIDA = "PP";
    
    /**
     * Constante que define la operacion como cancelaci&oacute;n de una compra.
     */
    public static final String CANCELACION_COMPRA = "CC";
    
    /**
     * Constante que define la operacion como cancelaci&oacute;n de una venta.
     */
    public static final String CANCELACION_VENTA = "VC";
    
    /**
     * Constante que define la operacion como cancelaci&oacute;n de una compra interbancaria.
     */
    public static final String CANCELACION_COMPRA_INTERBANCARIA = "CN";
    
    /**
     * Constante que define la operacion como cancelaci&oacute;n de una venta interbancaria.
     */
    public static final String CANCELACION_VENTA_INTERBANCARIA = "VN";

    
    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 2750424703776086586L;
}

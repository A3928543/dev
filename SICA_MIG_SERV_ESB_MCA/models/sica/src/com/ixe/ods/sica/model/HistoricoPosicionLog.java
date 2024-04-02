/*
 * $Id: HistoricoPosicionLog.java,v 1.13 2008/05/01 03:06:40 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;
import java.io.Serializable;

/**
 * * <p>Clase persistente para la tabla SC_H_POSICION_LOG, donde se almacena el
 * hist&oacute;rico de los cambios de Posici&oacute;n Log. </p>
 *
 * @hibernate.class table="SC_H_POSICION_LOG"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoPosicionLog"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.13 $ $Date: 2008/05/01 03:06:40 $
 */
public class HistoricoPosicionLog implements Serializable {

   /**
    * Constructor por default. No hace nada.
    */
    public HistoricoPosicionLog() {
        super();
    }

    /**
    * Inicializa el objeto, recibiendo como par&aacute;metro posici&oacute;n log.
    *
    * @param posicionLog La posici&oacute;n log que inicializa el hist&oacute;rico.
    */
    public HistoricoPosicionLog(PosicionLog posicionLog) {
        this();
        setCanalMesa(posicionLog.getCanalMesa());
        setClaveFormaLiquidacion(posicionLog.getClaveFormaLiquidacion());
        setDeal(posicionLog.getDeal());
        setDealPosicion(posicionLog.getDealPosicion());
        setDivisa(posicionLog.getDivisa());
        setIdPosicionLog(posicionLog.getIdPosicionLog());
        setIdUsuario(posicionLog.getIdUsuario());
        setMonto(posicionLog.getMonto());
        setMontoMn(posicionLog.getMontoMn());
        setNombreCliente(posicionLog.getNombreCliente());
        setPrecioReferencia(posicionLog.getPrecioReferencia());
        setTipoCambioCliente(posicionLog.getTipoCambioCliente());
        setTipoCambioMesa(posicionLog.getTipoCambioMesa());
        setTipoOperacion(posicionLog.getTipoOperacion());
        setTipoValor(posicionLog.getTipoValor());
        setUltimaModificacion(new Date());
    }

    /**
     * Regresa el valor de idPosicionLog.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_POSICION_LOG"
     * unsaved-value="null"
     * @return int.
     */
    public int getIdPosicionLog() {
        return idPosicionLog;
    }

    /**
     * Regresa el valor de ultimaModificacion.
     *
     * @hibernate.property column="ULTIMA_MODIFICACION"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getUltimaModificacion() {
        return ultimaModificacion;
    }

    /**
     * Fija la fecha de la &uacute;ltima modificaci&oacute;n
     *
     * @param ultimaModificacion Fecha de &uacute;ltima modificaci&oacute;n
     *
     */
    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
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
     * Regresa el valor de claveFormaLiquidacion.
     *
     * @hibernate.property column="CLAVE_FORMA_LIQUIDACION"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getClaveFormaLiquidacion() {
        return claveFormaLiquidacion;
    }

    /**
     * Fija el valor de claveFormaLiquidacion.
     *
     * @param claveFormaLiquidacion La claveFormaLiquidacion a asignar.
     */
    public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
        this.claveFormaLiquidacion = claveFormaLiquidacion;
    }

    /**
     * Regresa el valor de canalMesa.
     *
     * @hibernate.component class="com.ixe.ods.sica.model.CanalMesa"
     * @return CanalMesa.
     */
    public CanalMesa getCanalMesa() {
        return _canalMesa;
    }

    /**
     * Fija el valor de canalMesa.
     *
     * @param canalMesa El valor a asignar.
     */
    public void setCanalMesa(CanalMesa canalMesa) {
        _canalMesa = canalMesa;
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
     * Regresa el valor de nombreCliente.
     *
     * @hibernate.property column="NOMBRE_CLIENTE"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Fija el valor de nombreCliente.
     *
     * @param nombreCliente El valor a asignar.
     */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
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
     *
     * @hibernate.many-to-one column="ID_DEAL_POSICION"
     * cascade="none"
     * class="com.ixe.ods.sica.model.DealPosicion"
     * outer-join="auto"
     * unique="false"
     * @return DealPosicion.
     */
    public DealPosicion getDealPosicion() {
        return _dealPosicion;
    }

    /**
     * Fija el valor de dealPosicion.
     *
     * @param dealPosicion El valor a asignar.
     */
    public void setDealPosicion(DealPosicion dealPosicion) {
        _dealPosicion = dealPosicion;
    }

    /**
     * Regresa el valor de deal.
     *
     * @hibernate.many-to-one column="ID_DEAL"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Deal"
     * outer-join="auto"
     * unique="false"
     * @return Deal.
     */
    public Deal getDeal() {
        return _deal;
    }

    /**
     * Fija el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public void setDeal(Deal deal) {
        _deal = deal;
    }

    /**
     * Regresa el valor de divisa.
     *
     * @hibernate.many-to-one column="ID_DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return Divisa.
     */
    public Divisa getDivisa() {
        return _divisa;
    }

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        _divisa = divisa;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof HistoricoPosicionLog)) {
            return false;
        }
        HistoricoPosicionLog castOther = (HistoricoPosicionLog) other;
        return new EqualsBuilder().append(this.getIdPosicionLog(), castOther.getIdPosicionLog()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdPosicionLog()).toHashCode();
    }

    /**
     * Regresa la descripci&oacute;n de los campos del objeto.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("idPosicionLog", idPosicionLog).append("cfl", claveFormaLiquidacion).
                append("idUsuario", idUsuario).append("monto", monto).append("montoMn", montoMn).append("nombreCliente", nombreCliente).
                append("precioReferencia", precioReferencia).append("tcc", tipoCambioCliente).append("tipoCambioMesa", tipoCambioMesa).
                append("tipoOperacion", tipoOperacion).append("divisa", _divisa.getIdDivisa()).toString();
    }

    /**
     * El identificador del registro.
     */
    protected int idPosicionLog;

    /**
     * La clave del producto.
     */
    private String claveFormaLiquidacion;

    /**
     * El componente CanalMesa.
     */
    private CanalMesa _canalMesa;

    /**
     * El usuario que hizo la operaci&oacute;n.
     */
    private int idUsuario;

    /**
     * El monto del detalle del deal.
     */
    private double monto;

    /**
     * El monto del detalle del deal en moneda nacional.
     */
    private double montoMn;

    /**
     * El nombre del cliente.
     */
    private String nombreCliente;

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
    private Deal _deal;

    /**
     * Relaci&oacute;n muchos-a-uno con DealPosicion.
     */
    private DealPosicion _dealPosicion;

    /**
     * Relaci&oacute;n muchos a uno con Divisa.
     */
    private Divisa _divisa;

    /**
     * Identificador de la &uacute;ltima modificaci&oacute;n
     */
    private Date ultimaModificacion;
}

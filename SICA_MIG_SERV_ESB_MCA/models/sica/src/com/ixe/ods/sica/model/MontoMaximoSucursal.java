/*
 * $Id: MontoMaximoSucursal.java,v 1.1 2008/10/27 22:59:09 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_MONTO_MAXIMO_SUCURSAL. Las sucursales pueden realizar
 * operaciones durante el horario vespertino y nocturno, siempre y cuando la posici&oacute;n
 * acumulada no rebase el monto m&aacute;ximo especificado.
 *
 * @hibernate.class table="SC_MONTO_MAXIMO_SUCURSAL"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.MontoMaximoSucursal"
 * dynamic-update="true"
 *
 * @hibernate.query name="findMontosMaximosByCanalProducto"
 * query="FROM MontoMaximoSucursal AS mms WHERE mms.canal.idCanal = ? AND
 * mms.claveFormaLiquidacion = ?"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1 $ $Date: 2008/10/27 22:59:09 $
 */
public class MontoMaximoSucursal implements Serializable {

    /**
     * Constructor por default.
     */
    public MontoMaximoSucursal() {
        super();
    }

    /**
     * Constructor que inicializa el canal y la divisa. 
     *
     * @param canal El canal a asignar.
     * @param divisa La divisa a asignar.
     * @param claveFormaLiquidacion La clave de producto a asignar.
     */
    public MontoMaximoSucursal(Canal canal, Divisa divisa, String claveFormaLiquidacion) {
        this();
        this.canal = canal;
        this.divisa = divisa;
        this.claveFormaLiquidacion = claveFormaLiquidacion;
    }

    /**
     * Regresa el porcentaje de 
     * @return double.
     */
    public double getPorcentaje() {
        return Math.abs(montoOperado.doubleValue()) * 100.0 / montoMaximo.doubleValue();
    }

    /**
     * Regresa el hash map con las variables para la alerta.
     *
     * @return HashMap.
     */
    public HashMap getContextoAlertas() {
        HashMap ctx = new HashMap();
        ctx.put("SUCURSAL", canal.getNombre());
        ctx.put("ID_DIVISA", divisa.getIdDivisa());
        ctx.put("MONTO_OPERADO", MONEY_FORMAT.format(montoOperado));
        ctx.put("MONTO_MAXIMO", MONEY_FORMAT.format(montoMaximo));
        ctx.put("PORCENTAJE", PERCENT_FORMAT.format(getPorcentaje()));
        return ctx;
    }

    /**
     * Regresa el valor de idMontoMaximoSucursal.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_MONTO_MAXIMO_SUC"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_MONTO_MAXIMO_SUC_SEQ"
     * @return Integer.
     */
    public Integer getIdMontoMaximoSucursal() {
        //value="SC_MONTO_MAX_SUC_SEQ"
        return idMontoMaximoSucursal;
    }

    /**
     * Establece el valor de idMontoMaximoSucursal.
     *
     * @param idMontoMaximoSucursal El valor a asignar.
     */
    public void setIdMontoMaximoSucursal(Integer idMontoMaximoSucursal) {
        this.idMontoMaximoSucursal = idMontoMaximoSucursal;
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
     * Establece el valor de claveFormaLiquidacion.
     *
     * @param claveFormaLiquidacion El valor a asignar.
     */
    public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
        this.claveFormaLiquidacion = claveFormaLiquidacion;
    }

    /**
     * Regresa el valor de montoMaximo.
     *
     * @hibernate.property column="MONTO_MAXIMO"
     * not-null="true"
     * unique="false"
     * @return BigDecimal.
     */
    public BigDecimal getMontoMaximo() {
        return montoMaximo;
    }

    /**
     * Establece el valor de montoMaximo.
     *
     * @param montoMaximo El valor a asignar.
     */
    public void setMontoMaximo(BigDecimal montoMaximo) {
        this.montoMaximo = montoMaximo;
    }

    /**
     * Regresa el valor de montoOperado.
     *
     * @hibernate.property column="MONTO_OPERADO"
     * not-null="true"
     * unique="false"
     * @return BigDecimal.
     */
    public BigDecimal getMontoOperado() {
        return montoOperado;
    }

    /**
     * Establece el valor de montoOperado.
     *
     * @param montoOperado El valor a asignar.
     */
    public void setMontoOperado(BigDecimal montoOperado) {
        this.montoOperado = montoOperado;
    }

    /**
     * Regresa el valor de canal.
     *
     * @hibernate.many-to-one column="ID_CANAL"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Canal"
     * outer-join="auto"
     * unique="false"
     * @return com.ixe.ods.sica.model.Canal.
     */
    public Canal getCanal() {
        return canal;
    }

    /**
     * Establece el valor de canal.
     *
     * @param canal El valor a asignar.
     */
    public void setCanal(Canal canal) {
        this.canal = canal;
    }

    /**
     * Regresa el valor de divisa.
     *
     * @hibernate.many-to-one column="ID_DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return com.ixe.ods.sica.model.Divisa.
     */
    public Divisa getDivisa() {
        return divisa;
    }

    /**
     * Establece el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        this.divisa = divisa;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof MontoMaximoSucursal)) {
            return false;
        }
        MontoMaximoSucursal castOther = (MontoMaximoSucursal) other;
        return new EqualsBuilder().append(this.getIdMontoMaximoSucursal(),
                castOther.getIdMontoMaximoSucursal()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdMontoMaximoSucursal()).toHashCode();
    }

    /**
     * La llave primaria para este registro.
     */
    private Integer idMontoMaximoSucursal;

    /**
     * La clave del producto.
     */
    private String claveFormaLiquidacion;

    /**
     * El monto m&aacute;ximo permitido.
     */
    private BigDecimal montoMaximo = new BigDecimal("" + 0);

    /**
     * El monto operado hasta el momento.
     */
    private BigDecimal montoOperado = new BigDecimal("" + 0);

    /**
     * El canal de la sucursal.
     */
    private Canal canal;

    /**
     * La divisa en la que se opera.
     */
    private Divisa divisa;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -8653256187635783673L;

    /**
     * Formato monetario.
     */
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.00");

    /**
     * Formato de porcentaje.
     */
    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("0.00");
}

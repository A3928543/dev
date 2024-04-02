/*
 * $Id: Variacion.java,v 1.12.42.1 2011/06/02 16:29:08 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase persistente para la tabla SICA_VARIACION. En esta tabla, el excel de reuters escribe los valores que se van
 * leyendo. El SICA no debe actualizar esta tabla.
 *
 * @hibernate.class table="SICA_VARIACION"
 * mutable="false"
 * proxy="com.ixe.ods.sica.model.Variacion"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12.42.1 $ $Date: 2011/06/02 16:29:08 $
 */
public class Variacion implements Serializable {

    public Variacion() {
        super();
    }

    /**
     * Regresa el valor de idVariacion.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_VARIACION"
     * unsaved-value="0"
     * @return int.
     */
    public int getIdVariacion() {
        return idVariacion;
    }

    /**
     * Establece el valor de idVariacion.
     *
     * @param idVariacion El valor a asignar.
     */
    public void setIdVariacion(int idVariacion) {
        this.idVariacion = idVariacion;
    }

    /**
     * Regresa el valor de fecha.
     *
     * @hibernate.property column="FECHA"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece el valor de fecha.
     *
     * @param fecha El valor a asignar.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Regresa el valor de midSpot.
     *
     * @hibernate.property column="VARIACION_MID"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getMidSpot() {
        return midSpot;
    }

    /**
     * Establece el valor de midSpot.
     *
     * @param midSpot El valor a asignar.
     */
    public void setMidSpot(double midSpot) {
        this.midSpot = midSpot;
    }

    /**
     * Regresa el valor de usdCompra.
     *
     * @hibernate.property column="VARIACION_VTA_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getUsdCompra() {
        return usdCompra;
    }

    /**
     * Establece el valor de usdCompra.
     *
     * @param usdCompra El valor a asignar.
     */
    public void setUsdCompra(double usdCompra) {
        this.usdCompra = usdCompra;
    }

    /**
     * Regresa el valor de usdVenta.
     *
     * @hibernate.property column="VARIACION_VTA_SPOT_ASK"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getUsdVenta() {
        return usdVenta;
    }

    /**
     * Establece el valor de usdVenta.
     *
     * @param usdVenta El valor a asignar.
     */
    public void setUsdVenta(double usdVenta) {
        this.usdVenta = usdVenta;
    }

    /**
     * Regresa el valor de factorCadCompra.
     *
     * @hibernate.property column="CAD_VARIACION_VTA_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getFactorCadCompra() {
        return factorCadCompra;
    }

    /**
     * Establece el valor de factorCadCompra.
     *
     * @param factorCadCompra El valor a asignar.
     */
    public void setFactorCadCompra(double factorCadCompra) {
        this.factorCadCompra = factorCadCompra;
    }

    /**
     * Regresa el valor de factorCadVenta.
     *
     * @hibernate.property column="CAD_VARIACION_VTA_SPOT_ASK"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getFactorCadVenta() {
        return factorCadVenta;
    }

    /**
     * Establece el valor de factorCadVenta.
     *
     * @param factorCadVenta El valor a asignar.
     */
    public void setFactorCadVenta(double factorCadVenta) {
        this.factorCadVenta = factorCadVenta;
    }

    /**
     * Regresa el valor de factorChfCompra.
     *
     * @hibernate.property column="CHF_VARIACION_VTA_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getFactorChfCompra() {
        return factorChfCompra;
    }

    /**
     * Establece el valor de factorChfCompra.
     *
     * @param factorChfCompra El valor a asignar.
     */
    public void setFactorChfCompra(double factorChfCompra) {
        this.factorChfCompra = factorChfCompra;
    }

    /**
     * Regresa el valor de factorChfVenta.
     *
     * @hibernate.property column="CHF_VARIACION_VTA_SPOT_ASK"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getFactorChfVenta() {
        return factorChfVenta;
    }

    /**
     * Establece el valor de factorChfVenta.
     *
     * @param factorChfVenta El valor a asignar.
     */
    public void setFactorChfVenta(double factorChfVenta) {
        this.factorChfVenta = factorChfVenta;
    }

    /**
     * Regresa el valor de factorEurCompra.
     *
     * @hibernate.property column="EUR_VARIACION_VTA_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getFactorEurCompra() {
        return factorEurCompra;
    }

    /**
     * Establece el valor de factorEurCompra.
     *
     * @param factorEurCompra El valor a asignar.
     */
    public void setFactorEurCompra(double factorEurCompra) {
        this.factorEurCompra = factorEurCompra;
    }

    /**
     * Regresa el valor de factorEurVenta.
     *
     * @hibernate.property column="EUR_VARIACION_VTA_SPOT_ASK"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getFactorEurVenta() {
        return factorEurVenta;
    }

    /**
     * Establece el valor de factorEurVenta.
     *
     * @param factorEurVenta El valor a asignar.
     */
    public void setFactorEurVenta(double factorEurVenta) {
        this.factorEurVenta = factorEurVenta;
    }

    /**
     * Regresa el valor de factorGbpCompra.
     *
     * @hibernate.property column="GBP_VARIACION_VTA_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getFactorGbpCompra() {
        return factorGbpCompra;
    }

    /**
     * Establece el valor de factorGbpCompra.
     *
     * @param factorGbpCompra El valor a asignar.
     */
    public void setFactorGbpCompra(double factorGbpCompra) {
        this.factorGbpCompra = factorGbpCompra;
    }

    /**
     * Regresa el valor de factorGbpVenta.
     *
     * @hibernate.property column="GBP_VARIACION_VTA_SPOT_ASK"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getFactorGbpVenta() {
        return factorGbpVenta;
    }

    /**
     * Establece el valor de factorGbpVenta.
     *
     * @param factorGbpVenta El valor a asignar.
     */
    public void setFactorGbpVenta(double factorGbpVenta) {
        this.factorGbpVenta = factorGbpVenta;
    }

    /**
     * Regresa el valor de factorJpyCompra.
     *
     * @hibernate.property column="JPY_VARIACION_VTA_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getFactorJpyCompra() {
        return factorJpyCompra;
    }

    /**
     * Establece el valor de factorJpyCompra.
     *
     * @param factorJpyCompra El valor a asignar.
     */
    public void setFactorJpyCompra(double factorJpyCompra) {
        this.factorJpyCompra = factorJpyCompra;
    }

    /**
     * Regresa el valor de factorJpyVenta.
     *
     * @hibernate.property column="JPY_VARIACION_VTA_SPOT_ASK"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getFactorJpyVenta() {
        return factorJpyVenta;
    }

    /**
     * Establece el valor de factorJpyVenta.
     *
     * @param factorJpyVenta El valor a asignar.
     */
    public void setFactorJpyVenta(double factorJpyVenta) {
        this.factorJpyVenta = factorJpyVenta;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Variacion)) {
            return false;
        }
        Variacion castOther = (Variacion) other;
        return new EqualsBuilder().append(this.getIdVariacion(), castOther.getIdVariacion()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdVariacion()).toHashCode();
    }

    /**
     * Regresa una descripci&oacute;n de las propiedades del objeto.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("idVariacion", idVariacion).append("fecha", fecha).toString();
    }

    /**
     * El identificador del registro.
     */
    private int idVariacion;

    /**
     * La fecha en la que fue insertado el registro.
     */
    private Date fecha;

    /**
     * El valor del MidSpot USD.
     */
    private double midSpot;

    /**
     * El valor en pesos de USD para compra.
     */
    private double usdCompra;

    /**
     * El valor en pesos de USD para venta.
     */
    private double usdVenta;

    /**
     * El factor de conversi&oacute;n de CAD a USD para compra.
     */
    private double factorCadCompra;

    /**
     * El factor de conversi&oacute;n de CAD a USD para venta.
     */
    private double factorCadVenta;

    /**
     * El factor de conversi&oacute;n de CHF a USD para compra.
     */
    private double factorChfCompra;

    /**
     * El factor de conversi&oacute;n de CHF a USD para venta.
     */
    private double factorChfVenta;

    /**
     * El factor de conversi&oacute;n de EUR a USD para compra.
     */
    private double factorEurCompra;

    /**
     * El factor de conversi&oacute;n de EUR a USD para venta.
     */
    private double factorEurVenta;

    /**
     * El factor de conversi&oacute;n de GBP a USD para compra.
     */
    private double factorGbpCompra;

    /**
     * El factor de conversi&oacute;n de GBP a USD para venta.
     */
    private double factorGbpVenta;

    /**
     * El factor de conversi&oacute;n de JPY a USD para compra.
     */
    private double factorJpyCompra;

    /**
     * El factor de conversi&oacute;n de JPY a USD para venta.
     */
    private double factorJpyVenta;
    
    /**
     * Identifica al archivo Excel de Reuters como origen de variaci&oacute;n para actualizaci&oacute;n del pizarr&oacute;n
     */
    public static final String ORIG_VAR_EXCEL = "ORIG_VAR_EXCEL";
    
    /**
     * Identifica a Reuters Market Data System como Origen de variaci&oacute;n para actualizaci&oacute;n del pizarr&oacute;n
     */
    public static final String ORIG_VAR_RMDS = "ORIG_VAR_RMDS";
    
}

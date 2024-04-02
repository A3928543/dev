/*
 * $Id: FacDiv.java,v 1.12.42.1 2011/04/26 00:28:39 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12.42.1 $ $Date: 2011/04/26 00:28:39 $
 */
public class FacDiv implements Serializable {

    /**
     * Constructor vac&iacute;o, no hace nada.
     */
    public FacDiv() {
        super();
    }

    /**
     * Inicializa la variable de instancia factorTmp. Si alguna de las divisas debe dividir, se asigna el valor inverso
     * del factor.
     */
    public void inicializarFactorTmp() {
        if (getFromDivisa().isDivide() || getToDivisa().isDivide()) {
            setFactorTmp(1.0 / getFactor());
            setFactorCompraTmp(1.0 / getFactorCompra().doubleValue());
        }
        else {
            setFactorTmp(getFactor());
            setFactorCompraTmp(getFactorCompra().doubleValue());
        }
    }
    
    /**
     * Obtiene el valor de _factorCompraTmp.
     * 
     * @return double
     */
    public double getFactorCompraTmp() {
        return _factorCompraTmp;
    }

    /**
     * Asigna el valor para factorCompraTmp
     * 
     * @param factorCompraTmp El valor para _factorCompraTmp
     */
    public void setFactorCompraTmp(double factorCompraTmp) {
        _factorCompraTmp = factorCompraTmp;
    }

    /**
     * Regresa el valor de factorTmp.
     *
     * @return double.
     */
    public double getFactorTmp() {
        return _factorTmp;
    }

    /**
     * Establece el valor de factorTmp.
     *
     * @param factorTmp El valor a asignar.
     */
    public void setFactorTmp(double factorTmp) {
        _factorTmp = factorTmp;
    }

    /**
     * Aplica el factorTmp de acuerdo a ciertos criterios de la divisa.
     */
    public void aplicarFactorTmp() {
        if (getFromDivisa().isDivide() || getToDivisa().isDivide()) {
            setFactor(1.0 / getFactorTmp());
            setFactorCompra(new Double(1.0 / getFactorCompraTmp()));
        }
        else {
            setFactor(getFactorTmp());
            setFactorCompra(new Double(getFactorCompraTmp()));
        }
    }

    /**
     * Regresa el valor de factor.
     *
     * @hibernate.property column="FACTOR"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getFactor() {
        return factor;
    }

    /**
     * Fija el valor de factor.
     *
     * @param factor El valor a asginar.
     */
    public void setFactor(double factor) {
        this.factor = factor;
    }

    /**
     *
     * Regresa el valor de factorCompra.
     *
     * @hibernate.property column="FACTOR_COMPRA"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getFactorCompra() {
        return factorCompra;
    }

    /**
     * Establece el valor de factorCompra.
     *
     * @param factorCompra El valor a asignar.
     */
    public void setFactorCompra(Double factorCompra) {
        this.factorCompra = factorCompra;
    }

    /**
     * Regresa el valor de spreadReferencia.
     *
     * @hibernate.property column="SPREAD_REFERENCIA"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getSpreadReferencia() {
        return spreadReferencia;
    }

    /**
     * Fija el valor de spreadReferencia.
     *
     * @param spreadReferencia El valor a asignar.
     */
    public void setSpreadReferencia(double spreadReferencia) {
        this.spreadReferencia = spreadReferencia;
    }

    /**
     * Regresa el valor de spreadCompra.
     *
     * @hibernate.property column="SPREAD_COMPRA"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getSpreadCompra() {
        return spreadCompra;
    }

    /**
     * Fija el valor de spreadCompra.
     *
     * @param spreadCompra El valor a asignar.
     */
    public void setSpreadCompra(double spreadCompra) {
        this.spreadCompra = spreadCompra;
    }

    /**
     * Regresa el valor de carry.
     *
     * @hibernate.property column="CARRY"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCarry() {
        return carry;
    }

    /**
     * Fija el valor de carry.
     *
     * @param carry El valor a asignar.
     */
    public void setCarry(double carry) {
        this.carry = carry;
    }

    /**
     * Regresa el valor de metodoActualizacion.
     *
     * @hibernate.property column="METODO_ACTUALIZACION"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getMetodoActualizacion() {
        return metodoActualizacion;
    }

    /**
     * Fija el valor de metodoActualizacion.
     *
     * @param metodoActualizacion El valor a asginar.
     */
    public void setMetodoActualizacion(String metodoActualizacion) {
        this.metodoActualizacion = metodoActualizacion;
    }

    /**
     * Regresa el valor de slack.
     *
     * @hibernate.property column="SLACK"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getSlack() {
        return slack;
    }

    /**
     * Establece el valor de slack.
     *
     * @param slack El valor a asignar.
     */
    public void setSlack(Double slack) {
        this.slack = slack;
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
     * Fija el valor de ultimaModificacion.
     *
     * @param ultimaModificacion El valor a asginar.
     */
    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    /**
     * Regresa el valor de fromDivisa.
     *
     * @hibernate.many-to-one column="FROM_ID_DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return Divisa.
     */
    public Divisa getFromDivisa() {
        return this.fromDivisa;
    }

    /**
     * Fija el valor de fromDivisa.
     *
     * @param fromDivisa El valor a asignar.
     */
    public void setFromDivisa(Divisa fromDivisa) {
        this.fromDivisa = fromDivisa;
    }

    /**
     * Regresa el valor de toDivisa.
     *
     * @hibernate.many-to-one column="TO_ID_DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return Divisa.
     */
    public Divisa getToDivisa() {
        return this.toDivisa;
    }

    /**
     * Fija el valor de toDivisa.
     *
     * @param toDivisa El valor a asginar.
     */
    public void setToDivisa(Divisa toDivisa) {
        this.toDivisa = toDivisa;
    }

    /**
     * El factor de conversion de una divisa a otra.
     */
    private double factor;

    /**
     * El factor de conversi&oacute;n de una divisa a otra con respecto a la compra.
     */
    private Double factorCompra = new Double(0);

    /**
     * El factor de conversi&oacute; de divisa temporal.
     */
    private double _factorTmp;

    /**
     * El factor de compra de conversi&oacute;n de divisa temporal.
     */
    private double _factorCompraTmp;

    /**
     * El spread que se suma al precioSpot.
     */
    private double spreadReferencia;

    /**
     * El spread para calcular la compra a partir de la venta.
     */
    private double spreadCompra;

    /**
     * El spread por d&iacute;a.
     */
    private double carry;

    /**
     * A)utom&aacute;tico M)anual
     */
    private String metodoActualizacion;

    /**
     * El valor que define si despu&eacute;s de una actualizaci&oacute;n autom&aacute;tica, se debe o no notificar a los
     * pizarrones.
     */
    private Double slack = new Double(0);

    /**
     * La fecha de la &uacute;ltima modificaci&oacute;n del registro.
     */
    private Date ultimaModificacion = new Date();

    /**
     * La divisa de referencia. Relaci&oacute;n muchos-a-uno con Divisa.
     */
    private Divisa fromDivisa;

    /**
     * A qu&eacute; divisa se aplica el factor. Relaci&oacute;n muchos-a-uno con
     * Divisa.
     */
    private Divisa toDivisa;
    
}

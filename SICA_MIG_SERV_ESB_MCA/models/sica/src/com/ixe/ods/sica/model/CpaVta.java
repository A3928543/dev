/*
 * $Id: CpaVta.java,v 1.13 2008/04/16 18:21:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * Componente de hibernate que contiene valores cash, tom y spot; para compra y
 * venta. Pueden utilizarse como spreads (Spread) o como montos (Posicion).
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.13 $ $Date: 2008/04/16 18:21:32 $
 */
public class CpaVta implements Serializable {

    /**
     * Constructor vac&iacute;o. No hace nada.
     */
    public CpaVta() {
        super();
    }

    /**
     * Constructor por default. Inicializa todas las variables de instancia.
     *
     * @param compraCash El valor para compra cash.
     * @param ventaCash El valor para venta cash.
     * @param compraTom El valor para compra tom.
     * @param ventaTom El valor para venta tom.
     * @param compraSpot El valor para compra spot.
     * @param ventaSpot El valor para venta spot.
     * @param compra72Hr El valor para compra 72Hr.
     * @param venta72Hr El valor para venta 72Hr.
     * @param compraVFut El valor para compra VFut.
     * @param ventaVFut El valor para venta VFut.
     */
    public CpaVta(double compraCash, double ventaCash, double compraTom, double ventaTom,
                  double compraSpot, double ventaSpot, double compra72Hr, double venta72Hr,
                  double compraVFut, double ventaVFut) {
        this();
        this.compraCash = compraCash;
        this.ventaCash = ventaCash;
        this.compraTom = compraTom;
        this.ventaTom = ventaTom;
        this.compraSpot = compraSpot;
        this.ventaSpot = ventaSpot;
        this.compra72Hr = new Double(compra72Hr);
        this.venta72Hr = new Double(venta72Hr);
        this.compraVFut = new Double(compraVFut);
        this.ventaVFut = new Double(ventaVFut);
    }

    /**
     * Regresa el valor de compraCash.
     *
     * @hibernate.property column="COMPRA_CASH"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraCash() {
        return compraCash;
    }

    /**
     * Fija el valor de compraCash.
     *
     * @param compraCash El valor a asignar.
     */
    public void setCompraCash(double compraCash) {
        this.compraCash = compraCash;
    }

    /**
     * Regresa el valor de ventaCash.
     *
     * @hibernate.property column="VENTA_CASH"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaCash() {
        return ventaCash;
    }

    /**
     * Fija el valor de ventaCash.
     *
     * @param ventaCash El valor a asignar.
     */
    public void setVentaCash(double ventaCash) {
        this.ventaCash = ventaCash;
    }

    /**
     * Regresa el valor de compraTom.
     *
     * @hibernate.property column="COMPRA_TOM"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraTom() {
        return compraTom;
    }

    /**
     * Fija el valor de compraTom.
     *
     * @param compraTom El valor a asignar.
     */
    public void setCompraTom(double compraTom) {
        this.compraTom = compraTom;
    }

    /**
     * Regresa el valor de ventaTom.
     *
     * @hibernate.property column="VENTA_TOM"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaTom() {
        return ventaTom;
    }

    /**
     * Fija el valor de ventaTom.
     *
     * @param ventaTom El valor a asignar.
     */
    public void setVentaTom(double ventaTom) {
        this.ventaTom = ventaTom;
    }

    /**
     * Regresa el valor de compraSpot.
     *
     * @hibernate.property column="COMPRA_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraSpot() {
        return compraSpot;
    }

    /**
     * Fija el valor de compraSpot.
     *
     * @param compraSpot El valor a asignar.
     */
    public void setCompraSpot(double compraSpot) {
        this.compraSpot = compraSpot;
    }

    /**
     * Regresa el valor de ventaSpot.
     *
     * @hibernate.property column="VENTA_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaSpot() {
        return ventaSpot;
    }

    /**
     * Fija el valor de ventaSpot.
     *
     * @param ventaSpot El valor a asignar.
     */
    public void setVentaSpot(double ventaSpot) {
        this.ventaSpot = ventaSpot;
    }

    /**
     * Regresa el valor de compra72Hr.
     *
     * @hibernate.property column="COMPRA_72HR"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public Double getCompra72Hr() {
        return compra72Hr;
    }

    /**
     * Establece el valor de compra72Hr.
     * @param compra72Hr El valor a asignar.
     */
    public void setCompra72Hr(Double compra72Hr) {
        this.compra72Hr = compra72Hr;
    }

    /**
     *
     * @hibernate.property column="VENTA_72HR"
     * not-null="true"
     * unique="false"
     * @return Double.
     */
    public Double getVenta72Hr() {
        return venta72Hr;
    }

    /**
     * Establece el valor de venta72Hr.
     *
     * @param venta72Hr El valor a asignar.
     */
    public void setVenta72Hr(Double venta72Hr) {
        this.venta72Hr = venta72Hr;
    }

    /**
     * Regresa el valor de compraVFut
     *
     * @hibernate.property column="COMPRA_VFUT"
     * not-null="true"
     * unique="false"
     * @return Double
     */
    public Double getCompraVFut() {
        return compraVFut;
    }

    /**
     * Establece el valor de compraVFut.
     *
     * @param compraVFut El valor a asignar.
     */
    public void setCompraVFut(Double compraVFut) {
        this.compraVFut = compraVFut;
    }

	/**
     * Regresa el valor de ventaVFut.
     *
     * @hibernate.property column="VENTA_VFUT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public Double getVentaVFut() {
        return ventaVFut;
    }

    /**
     * Establece el valor de ventaVFut.
     *
     * @param ventaVFut El valor a asignar.
     */
    public void setVentaVFut(Double ventaVFut) {
        this.ventaVFut = ventaVFut;
    }

    /**
     * Regresa el valor de cash
     *
     * @return double.
     */
    public double getCash() {
    	return compraCash - ventaCash;
    }

    /**
     * Regresa el valor de tom.
     *
     * @return double.
     */
    public double getTom() {
    	return compraTom - ventaTom;
    }

    /**
     * Regresa el valor de spot
     *
     * @return double.
     */
    public double getSpot() {
    	return compraSpot - ventaSpot;
    }

    /**
     * Regresa el valor de 72H.
     *
     * @return double
     */
    public double get72Hr() {
        return compra72Hr.doubleValue() - venta72Hr.doubleValue();
    }

    /**
     * Regresa el valor de VF.
     * @return double.
     */
    public double getVFut() {
        return compraVFut.doubleValue() - ventaVFut.doubleValue();
    }
    
    /**
     * Obtiene el valor total de Compras.
     * 
     * @return double
     */
    public double getTotalCompras() {
    	return getCompraCash() + getCompraTom() + getCompraSpot() + 
    		getCompra72Hr().doubleValue() + getCompraVFut().doubleValue();
    }
    
    /**
     * Obtiene el valor total de Ventas.
     * 
     * @return double
     */
    public double getTotalVentas() {
    	return getVentaCash() + getVentaTom() + getVentaSpot() + 
    		getVenta72Hr().doubleValue() + getVentaVFut().doubleValue();
    }

    /**
     * El valor o spread de compra cash.
     */
    protected double compraCash;

    /**
     * El valor o spread de venta cash.
     */
    protected double ventaCash;

    /**
     * El valor o spread de compra tom.
     */
    protected double compraTom;

    /**
     * El valor o spread de venta tom.
     */
    protected double ventaTom;

    /**
     * El valor o spread de compra spot.
     */
    protected double compraSpot;

    /**
     * El valor o spread de venta spot.
     */
    protected double ventaSpot;

    /**
     * El valor o spread de compra a 72 hrs.
     */
    protected Double compra72Hr = new Double(0);

    /**
     * El valor o spread de venta a 72 hrs.
     */
    protected Double venta72Hr = new Double(0);

    /**
     * El valor o spread de compra a valor futuro.
     */
    private Double compraVFut = new Double(0);

    /**
     * El valor o spread de venta a valor futuro.
     */
    private Double ventaVFut = new Double(0);

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 4556515596208425013L;
}

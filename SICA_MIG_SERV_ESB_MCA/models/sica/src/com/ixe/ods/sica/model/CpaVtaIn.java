/*
 * $Id: CpaVtaIn.java,v 1.12 2008/02/22 18:25:20 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * Componente de hibernate que contiene valores cash, tom y spot; para compra y
 * venta, para datos de interbancario.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:20 $
 */
public class CpaVtaIn implements Serializable {

    /**
     * Constructor vac&iacute;o.
     */
    public CpaVtaIn() {
        super();
    }
    
    
    /**
     * 
     * Constructor que recibe todos los valores. 
     * 
     * @param compraInCash El valor de compra interbancaria cash.
     * @param ventaInCash El valor de venta interbancaria cash.
     * @param compraInTom El valor de compra interbancaria tom.
     * @param ventaInTom El valor de venta interbancaria tom.
     * @param compraInSpot El valor de compra interbancaria spot.
     * @param ventaInSpot El valor de venta interbancaria spot.
     * @param compraIn72Hr El valor de compra interbancaria 72Hr.
     * @param ventaIn72Hr El valor de venta interbancaria 72Hr.
     * @param compraInVFut El valor de compra interbancaria VFut.
     * @param ventaInVFut El valor de venta interbancaria VFut.
     */
    public CpaVtaIn(double compraInCash, double ventaInCash, double compraInTom, double ventaInTom, double compraInSpot, double ventaInSpot,
    		double compraIn72Hr, double ventaIn72Hr, double compraInVFut, double ventaInVFut) {
        this();
        this.compraInCash = compraInCash;
        this.ventaInCash = ventaInCash;
        this.compraInTom = compraInTom;
        this.ventaInTom = ventaInTom;
        this.compraInSpot = compraInSpot;
        this.ventaInSpot = ventaInSpot;
        this.compraIn72Hr = new Double(compraIn72Hr);
        this.ventaIn72Hr = new Double(ventaIn72Hr);
        this.compraInVFut = new Double(compraInVFut);
        this.ventaInVFut = new Double(ventaInVFut);
    }

    /**
     * Regresa el valor de compraInCash.
     *
     * @hibernate.property column="COMPRA_IN_CASH"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraInCash() {
        return compraInCash;
    }

    /**
     * Fija el valor de compraInCash.
     *
     * @param compraInCash El valor a asignar.
     */
    public void setCompraInCash(double compraInCash) {
        this.compraInCash = compraInCash;
    }

    /**
     * Regresa el valor de ventaInCash.
     * 
     * @hibernate.property column="VENTA_IN_CASH"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaInCash() {
        return ventaInCash;
    }

    /**
     * Fija el valor de ventaInCash.
     *
     * @param ventaInCash El valor a asignar.
     */
    public void setVentaInCash(double ventaInCash) {
        this.ventaInCash = ventaInCash;
    }

    /**
     * Regresa el valor de compraInTom.
     *
     * @hibernate.property column="COMPRA_IN_TOM"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraInTom() {
        return compraInTom;
    }

    /**
     * Fija el valor de compraInTom.
     *
     * @param compraInTom El valor a asignar.
     */
    public void setCompraInTom(double compraInTom) {
        this.compraInTom = compraInTom;
    }

    /**
     * Regresa el valor de ventaInTom.
     *
     * @hibernate.property column="VENTA_IN_TOM"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaInTom() {
        return ventaInTom;
    }

    /**
     * Fija el valor de ventaInTom.
     *
     * @param ventaInTom El valor a asignar.
     */
    public void setVentaInTom(double ventaInTom) {
        this.ventaInTom = ventaInTom;
    }

    /**
     * Regresa el valor de compraInSpot.
     *
     * @hibernate.property column="COMPRA_IN_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraInSpot() {
        return compraInSpot;
    }

    /**
     * Fija el valor de compraInSpot.
     *
     * @param compraInSpot El valor a asignar.
     */
    public void setCompraInSpot(double compraInSpot) {
        this.compraInSpot = compraInSpot;
    }

    /**
     * Regresa el valor de ventaInSpot.
     *
     * @hibernate.property column="VENTA_IN_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaInSpot() {
        return ventaInSpot;
    }

    /**
     * Fija el valor de ventaInSpot.
     *
     * @param ventaInSpot El valor a asignar.
     */
    public void setVentaInSpot(double ventaInSpot) {
        this.ventaInSpot = ventaInSpot;
    }

    /**
     * Regresa el valor de compraIn72Hr
     * 
     * @hibernate.property column="COMPRA_IN_72HR"
     * not-null="true"
     * unique="false" 
     * @return Double.
     */
    public Double getCompraIn72Hr() {
        return compraIn72Hr;
    }

    /**
     * Establece el valor de compraIn72Hr.
     *
     * @param compraIn72Hr El valor a asignar.
     */
    public void setCompraIn72Hr(Double compraIn72Hr) {
        this.compraIn72Hr = compraIn72Hr;
    }

    /**
     * Regresa el valor de ventaIn72Hr
     * 
     * @hibernate.property column="VENTA_IN_72HR"
     * not-null="true"
     * unique="false" 
     * @return Double.
     */
    public Double getVentaIn72Hr() {
        return ventaIn72Hr;
    }

    /**
     * Establece el valor de ventaIn72Hr.
     *
     * @param ventaIn72Hr El valor a asignar.
     */
    public void setVentaIn72Hr(Double ventaIn72Hr) {
        this.ventaIn72Hr = ventaIn72Hr;
    }
    
    
    /**
     * Regresa el valor de compraInVFut
     * 
     * @hibernate.property column="COMPRA_IN_VFUT"
     * not-null="true"
     * unique="false" 
     * @return Double.
     */
    public Double getCompraInVFut() {
        return compraInVFut;
    }

    /**
     * Establece el valor de compraInVFut.
     *
     * @param compraInVFut El valor a asignar.
     */
    public void setCompraInVFut(Double compraInVFut) {
        this.compraInVFut = compraInVFut;
    }
    
    /**
     * Regresa el valor de ventaInVFut
     * 
     * @hibernate.property column="VENTA_IN_VFUT"
     * not-null="true"
     * unique="false"  
     * @return Double.
     */
    public Double getVentaInVFut() {
        return ventaInVFut;
    }

    /**
     * Establece el valor de ventaInVFut.
     *
     * @param ventaInVFut El valor a asignar.
     */
    public void setVentaInVFut(Double ventaInVFut) {
        this.ventaInVFut = ventaInVFut;
    }

    /**
     * El valor o spread de compra cash.
     */
    private double compraInCash;

    /**
     * El valor o spread de venta cash.
     */
    private double ventaInCash;

    /**
     * El valor o spread de compra tom.
     */
    private double compraInTom;

    /**
     * El valor o spread de venta tom.
     */
    private double ventaInTom;

    /**
     * El valor o spread de compra spot.
     */
    private double compraInSpot;

    /**
     * El valor o spread de venta spot.
     */
    private double ventaInSpot;

    /**
     * El valor o spread de compra a 72 hrs.
     */
    private Double compraIn72Hr = new Double(0);

    /**
     * El valor o spread de venta a 72 hrs.
     */
    private Double ventaIn72Hr = new Double(0);

    /**
     * El valor o spread de compra a Valor futuro.
     */
    private Double compraInVFut = new Double(0);

    /**
     * El valor o spread de venta a Valor futuro.
     */
    private Double ventaInVFut = new Double(0);
}

/*
 * $Id: CpaVtaMn.java,v 1.17 2008/12/26 23:17:26 ccovian Exp $
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
 * @version  $Revision: 1.17 $ $Date: 2008/12/26 23:17:26 $
 */
public class CpaVtaMn implements Serializable {

    /**
     * Constructor vac&iacute;o. No hace nada.
     */
    public CpaVtaMn() {
        super();
    }

    /**
     * Constructor por default inicializa todas las variables de instancia.
     *
     * @param compraMnClienteCash El monto en moneda nacional de tipo de cambio cliente para compra
     * en CASH.
     * @param ventaMnClienteCash El monto en moneda nacional de tipo de cambio cliente para venta en
     * CASH.
     * @param compraMnClienteTom El monto en moneda nacional de tipo de cambio cliente para compra
     * en TOM.
     * @param ventaMnClienteTom El monto en moneda nacional de tipo de cambio cliente para venta en
     * TOM.
     * @param compraMnClienteSpot El monto en moneda nacional de tipo de cambio cliente para compra
     * en Spot.
     * @param ventaMnClienteSpot El monto en moneda nacional de tipo de cambio cliente para venta en
     * Spot.
     * @param compraMnPizarronCash El monto en moneda nacional de tipo de cambio pizarr&oacute;n
     * para compra en CASH.
     * @param ventaMnPizarronCash El monto en moneda nacional de tipo de cambio pizarr&oacute;n para
     * venta en CASH.
     * @param compraMnPizarronTom El monto en moneda nacional de tipo de cambio pizarr&oacute;n para
     * compra en TOM.
     * @param ventaMnPizarronTom El monto en moneda nacional de tipo de cambio pizarr&oacute;n para
     * venta en TOM.
     * @param compraMnPizarronSpot El monto en moneda nacional de tipo de cambio pizarr&oacute;n
     * para compra en Spot.
     * @param ventaMnPizarronSpot El monto en moneda nacional de tipo de cambio pizarr&oacute;n para
     * venta en Spot.
     * @param compraMnMesaCash El monto en moneda nacional de tipo de cambio mesa para compra en
     * CASH.
     * @param ventaMnMesaCash El monto en moneda nacional de tipo de cambio mesa para venta en CASH.
     * @param compraMnMesaTom El monto en moneda nacional de tipo de cambio mesa para compra en TOM.
     * @param ventaMnMesaTom El monto en moneda nacional de tipo de cambio mesa para venta en TOM.
     * @param compraMnMesaSpot El monto en moneda nacional de tipo de cambio mesa para compra en
     * Spot.
     * @param ventaMnMesaSpot El monto en moneda nacional de tipo de cambio mesa para venta en
     * Spot.
     */
    public CpaVtaMn(double compraMnClienteCash, double ventaMnClienteCash,
                    double compraMnClienteTom, double ventaMnClienteTom, double compraMnClienteSpot,
                    double ventaMnClienteSpot, double compraMnCliente72Hr,
                    double ventaMnCliente72Hr, double compraMnClienteVFut,
                    double ventaMnClienteVfut, double compraMnPizarronCash,
                    double ventaMnPizarronCash, double compraMnPizarronTom,
                    double ventaMnPizarronTom, double compraMnPizarronSpot,
                    double ventaMnPizarronSpot, double compraMnPizarron72Hr,
                    double ventaMnPizarron72Hr, double compraMnPizarronVFut,
                    double ventaMnPizarronVfut, double compraMnMesaCash, double ventaMnMesaCash,
                    double compraMnMesaTom, double ventaMnMesaTom, double compraMnMesaSpot,
                    double ventaMnMesaSpot, double compraMnMesa72Hr, double ventaMnMesa72Hr,
                    double compraMnMesaVFut, double ventaMnMesaVfut) {
        this.compraMnClienteCash = compraMnClienteCash;
        this.ventaMnClienteCash = ventaMnClienteCash;
        this.compraMnClienteTom = compraMnClienteTom;
        this.ventaMnClienteTom = ventaMnClienteTom;
        this.compraMnClienteSpot = compraMnClienteSpot;
        this.ventaMnClienteSpot = ventaMnClienteSpot;
        this.compraMnCliente72Hr = new Double(compraMnCliente72Hr);
        this.ventaMnCliente72Hr = new Double(ventaMnCliente72Hr);
        this.compraMnClienteVFut = new Double(compraMnClienteVFut);
        this.ventaMnClienteVFut = new Double(ventaMnClienteVfut);
        this.compraMnPizarronCash = compraMnPizarronCash;
        this.ventaMnPizarronCash = ventaMnPizarronCash;
        this.compraMnPizarronTom = compraMnPizarronTom;
        this.ventaMnPizarronTom = ventaMnPizarronTom;
        this.compraMnPizarronSpot = compraMnPizarronSpot;
        this.ventaMnPizarronSpot = ventaMnPizarronSpot;
        this.compraMnPizarron72Hr = new Double(compraMnPizarron72Hr);
        this.ventaMnPizarron72Hr = new Double(ventaMnPizarron72Hr);
        this.compraMnPizarronVFut = new Double(compraMnPizarronVFut);
        this.ventaMnPizarronVFut = new Double(ventaMnPizarronVfut);
        this.compraMnMesaCash = compraMnMesaCash;
        this.ventaMnMesaCash = ventaMnMesaCash;
        this.compraMnMesaTom = compraMnMesaTom;
        this.ventaMnMesaTom = ventaMnMesaTom;
        this.compraMnMesaSpot = compraMnMesaSpot;
        this.ventaMnMesaSpot = ventaMnMesaSpot;
        this.compraMnMesa72Hr = new Double(compraMnMesa72Hr);
        this.ventaMnMesa72Hr = new Double(ventaMnMesa72Hr);
        this.compraMnMesaVFut = new Double(compraMnMesaVFut);
        this.ventaMnMesaVFut = new Double(ventaMnMesaVfut);
    }


    /**
     * Regresa el valor de compraMnClienteCash.
     *
     * @hibernate.property column="COMPRA_MN_CLIENTE_CASH"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraMnClienteCash() {
        return compraMnClienteCash;
    }

    /**
     * Fija el valor de compraMnClienteCash.
     *
     * @param compraMnClienteCash El valor a asignar.
     */
    public void setCompraMnClienteCash(double compraMnClienteCash) {
        this.compraMnClienteCash = compraMnClienteCash;
    }

    /**
     * Regresa el valor de ventaMnClienteCash.
     *
     * @hibernate.property column="VENTA_MN_CLIENTE_CASH"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaMnClienteCash() {
        return ventaMnClienteCash;
    }

    /**
     * Fija el valor de ventaMnClienteCash.
     *
     * @param ventaMnClienteCash El valor a asignar.
     */
    public void setVentaMnClienteCash(double ventaMnClienteCash) {
        this.ventaMnClienteCash = ventaMnClienteCash;
    }

    /**
     * Regresa el valor de compraMnClienteTom.
     *
     * @hibernate.property column="COMPRA_MN_CLIENTE_TOM"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraMnClienteTom() {
        return compraMnClienteTom;
    }

    /**
     * Fija el valor de compraMnClienteTom.
     *
     * @param compraMnClienteTom El valor a asignar.
     */
    public void setCompraMnClienteTom(double compraMnClienteTom) {
        this.compraMnClienteTom = compraMnClienteTom;
    }

    /**
     * Regresa el valor de ventaMnClienteTom.
     *
     * @hibernate.property column="VENTA_MN_CLIENTE_TOM"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaMnClienteTom() {
        return ventaMnClienteTom;
    }

    /**
     * Fija el valor de ventaMnClienteTom.
     *
     * @param ventaMnClienteTom El valor a asignar.
     */
    public void setVentaMnClienteTom(double ventaMnClienteTom) {
        this.ventaMnClienteTom = ventaMnClienteTom;
    }

    /**
     * Regresa el valor de compraMnClienteSpot.
     *
     * @hibernate.property column="COMPRA_MN_CLIENTE_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraMnClienteSpot() {
        return compraMnClienteSpot;
    }

    /**
     * Fija el valor de compraMnClienteSpot.
     *
     * @param compraMnClienteSpot El valor a asignar.
     */
    public void setCompraMnClienteSpot(double compraMnClienteSpot) {
        this.compraMnClienteSpot = compraMnClienteSpot;
    }

    /**
     * Regresa el valor de ventaMnClienteSpot.
     *
     * @hibernate.property column="VENTA_MN_CLIENTE_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaMnClienteSpot() {
        return ventaMnClienteSpot;
    }

    /**
     * Fija el valor de ventaMnClienteSpot.
     *
     * @param ventaMnClienteSpot El valor a asignar.
     */
    public void setVentaMnClienteSpot(double ventaMnClienteSpot) {
        this.ventaMnClienteSpot = ventaMnClienteSpot;
    }

    /**
     * Regresa el valor de compraMnCliente72HR
     *
     * @hibernate.property column="COMPRA_MN_CLIENTE_72HR"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public Double getCompraMnCliente72Hr() {
        return compraMnCliente72Hr;
    }

    /**
     * Establece el valor de compraMnCliente72Hr.
     *
     * @param compraMnCliente72Hr El valor a asignar.
     */
    public void setCompraMnCliente72Hr(Double compraMnCliente72Hr) {
        this.compraMnCliente72Hr = compraMnCliente72Hr;
    }

    /**
     * Regresa el valor de ventaMnCliente72Hr
     *
     * @hibernate.property column="VENTA_MN_CLIENTE_72HR"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public Double getVentaMnCliente72Hr() {
        return ventaMnCliente72Hr;
    }

    /**
     * Establece el valor de ventaMnCliente72Hr.
     *
     * @param ventaMnCliente72Hr El valor a asignar.
     */
    public void setVentaMnCliente72Hr(Double ventaMnCliente72Hr) {
        this.ventaMnCliente72Hr = ventaMnCliente72Hr;
    }

    /**
     * Regresa el valor de compraMnClienteVFut
     *
     * @hibernate.property column="COMPRA_MN_CLIENTE_VFUT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public Double getCompraMnClienteVFut() {
        return compraMnClienteVFut;
    }

    /**
     * Establece el valor de compraMnClienteVFut.
     *
     * @param compraMnClienteVFut El valor a asignar.
     */
    public void setCompraMnClienteVFut(Double compraMnClienteVFut) {
        this.compraMnClienteVFut = compraMnClienteVFut;
    }

    /**
     * Regresa el valor de ventaMnClienteVFut
     *
     * @hibernate.property column="VENTA_MN_CLIENTE_VFUT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public Double getVentaMnClienteVFut() {
        return ventaMnClienteVFut;
    }

    /**
     * Establece el valor de ventaMnClienteVFut.
     *
     * @param ventaMnClienteVFut El valor a asignar.
     */
    public void setVentaMnClienteVFut(Double ventaMnClienteVFut) {
        this.ventaMnClienteVFut = ventaMnClienteVFut;
    }

    /**
     * Regresa el valor de compraMnPizarronCash.
     *
     * @hibernate.property column="COMPRA_MN_PIZARRON_CASH"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraMnPizarronCash() {
        return compraMnPizarronCash;
    }

    /**
     * Fija el valor de compraMnPizarronCash.
     *
     * @param compraMnPizarronCash El valor a asignar.
     */
    public void setCompraMnPizarronCash(double compraMnPizarronCash) {
        this.compraMnPizarronCash = compraMnPizarronCash;
    }

    /**
     * Regresa el valor de ventaMnPizarronCash.
     *
     * @hibernate.property column="VENTA_MN_PIZARRON_CASH"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaMnPizarronCash() {
        return ventaMnPizarronCash;
    }

    /**
     * Fija el valor de ventaMnPizarronCash.
     *
     * @param ventaMnPizarronCash El valor a asignar.
     */
    public void setVentaMnPizarronCash(double ventaMnPizarronCash) {
        this.ventaMnPizarronCash = ventaMnPizarronCash;
    }

    /**
     * Regresa el valor de compraMnPizarronTom.
     *
     * @hibernate.property column="COMPRA_MN_PIZARRON_TOM"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraMnPizarronTom() {
        return compraMnPizarronTom;
    }

    /**
     * Fija el valor de compraMnPizarronTom.
     *
     * @param compraMnPizarronTom El valor a asignar.
     */
    public void setCompraMnPizarronTom(double compraMnPizarronTom) {
        this.compraMnPizarronTom = compraMnPizarronTom;
    }

    /**
     * Regresa el valor de ventaMnPizarronTom.
     *
     * @hibernate.property column="VENTA_MN_PIZARRON_TOM"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaMnPizarronTom() {
        return ventaMnPizarronTom;
    }

    /**
     * Fija el valor de ventaMnPizarronTom.
     *
     * @param ventaMnPizarronTom El valor a asignar.
     */
    public void setVentaMnPizarronTom(double ventaMnPizarronTom) {
        this.ventaMnPizarronTom = ventaMnPizarronTom;
    }

    /**
     * Regresa el valor de compraMnPizarronSpot.
     *
     * @hibernate.property column="COMPRA_MN_PIZARRON_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraMnPizarronSpot() {
        return compraMnPizarronSpot;
    }

    /**
     * Fija el valor de compraMnPizarronSpot.
     *
     * @param compraMnPizarronSpot El valor a asignar.
     */
    public void setCompraMnPizarronSpot(double compraMnPizarronSpot) {
        this.compraMnPizarronSpot = compraMnPizarronSpot;
    }

    /**
     * Regresa el valor de ventaMnPizarronSpot.
     *
     * @hibernate.property column="VENTA_MN_PIZARRON_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaMnPizarronSpot() {
        return ventaMnPizarronSpot;
    }

    /**
     * Fija el valor de ventaMnPizarronSpot.
     *
     * @param ventaMnPizarronSpot El valor a asignar.
     */
    public void setVentaMnPizarronSpot(double ventaMnPizarronSpot) {
        this.ventaMnPizarronSpot = ventaMnPizarronSpot;
    }

    /**
     * Regresar el valor de compraMnPizarron72Hr
     *
     * @hibernate.property column="COMPRA_MN_PIZARRON_72HR"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public Double getCompraMnPizarron72Hr() {
        return compraMnPizarron72Hr;
    }

    /**
     * Establece el valor de compraMnPizarron72Hr.
     *
     * @param compraMnPizarron72Hr El valor a asignar.
     */
    public void setCompraMnPizarron72Hr(Double compraMnPizarron72Hr) {
        this.compraMnPizarron72Hr = compraMnPizarron72Hr;
    }

    /**
     * Regresa el valor de ventaMnPizarron72Hr
     *
     * @hibernate.property column="VENTA_MN_PIZARRON_72HR"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public Double getVentaMnPizarron72Hr() {
        return ventaMnPizarron72Hr;
    }

    /**
     * Establece el valor de ventaMnPizarron72Hr.
     *
     * @param ventaMnPizarron72Hr El valor a asignar.
     */
    public void setVentaMnPizarron72Hr(Double ventaMnPizarron72Hr) {
        this.ventaMnPizarron72Hr = ventaMnPizarron72Hr;
    }

    /**
     * Regresa el valor de compraMnPizarronVFut
     *
     * @hibernate.property column="COMPRA_MN_PIZARRON_VFUT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public Double getCompraMnPizarronVFut() {
        return compraMnPizarronVFut;
    }

    /**
     * Establece el valor de compraMnPizarronVFut.
     *
     * @param compraMnPizarronVFut El valor a asignar.
     */
    public void setCompraMnPizarronVFut(Double compraMnPizarronVFut) {
        this.compraMnPizarronVFut = compraMnPizarronVFut;
    }

    /**
     * Regresa el valor de ventaMnPizarrionVFut
     *
     * @hibernate.property column="VENTA_MN_PIZARRON_VFUT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public Double getVentaMnPizarronVFut() {
        return ventaMnPizarronVFut;
    }

    /**
     * Establece el valor de ventaMnPizarronVFut.
     *
     * @param ventaMnPizarronVFut El valor a asignar.
     */
    public void setVentaMnPizarronVFut(Double ventaMnPizarronVFut) {
        this.ventaMnPizarronVFut = ventaMnPizarronVFut;
    }

    /**
     * Regresa el valor de compraMnMesaCash.
     *
     * @hibernate.property column="COMPRA_MN_MESA_CASH"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraMnMesaCash() {
        return compraMnMesaCash;
    }

    /**
     * Fija el valor de compraMnMesaCash.
     *
     * @param compraMnMesaCash El valor a asignar.
     */
    public void setCompraMnMesaCash(double compraMnMesaCash) {
        this.compraMnMesaCash = compraMnMesaCash;
    }

    /**
     * Regresa el valor de ventaMnMesaCash.
     *
     * @hibernate.property column="VENTA_MN_MESA_CASH"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaMnMesaCash() {
        return ventaMnMesaCash;
    }

    /**
     * Fija el valor de ventaMnMesaCash.
     *
     * @param ventaMnMesaCash El valor a asignar.
     */
    public void setVentaMnMesaCash(double ventaMnMesaCash) {
        this.ventaMnMesaCash = ventaMnMesaCash;
    }

    /**
     * Regresa el valor de compraMnMesaTom.
     *
     * @hibernate.property column="COMPRA_MN_MESA_TOM"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraMnMesaTom() {
        return compraMnMesaTom;
    }

    /**
     * Fija el valor de compraMnMesaTom.
     *
     * @param compraMnMesaTom El valor a asignar.
     */
    public void setCompraMnMesaTom(double compraMnMesaTom) {
        this.compraMnMesaTom = compraMnMesaTom;
    }

    /**
     * Regresa el valor de ventaMnMesaTom.
     *
     * @hibernate.property column="VENTA_MN_MESA_TOM"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaMnMesaTom() {
        return ventaMnMesaTom;
    }

    /**
     * Fija el valor de ventaMnMesaTom.
     *
     * @param ventaMnMesaTom El valor a asignar.
     */
    public void setVentaMnMesaTom(double ventaMnMesaTom) {
        this.ventaMnMesaTom = ventaMnMesaTom;
    }

    /**
     * Regresa el valor de compraMnMesaSpot.
     *
     * @hibernate.property column="COMPRA_MN_MESA_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getCompraMnMesaSpot() {
        return compraMnMesaSpot;
    }

    /**
     * Fija el valor de compraMnMesaSpot.
     *
     * @param compraMnMesaSpot El valor a asignar.
     */
    public void setCompraMnMesaSpot(double compraMnMesaSpot) {
        this.compraMnMesaSpot = compraMnMesaSpot;
    }

    /**
     * Regresa el valor de ventaMnMesaSpot.
     *
     * @hibernate.property column="VENTA_MN_MESA_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVentaMnMesaSpot() {
        return ventaMnMesaSpot;
    }

    /**
     * Fija el valor de ventaMnMesaSpot.
     *
     * @param ventaMnMesaSpot El valor a asignar.
     */
    public void setVentaMnMesaSpot(double ventaMnMesaSpot) {
        this.ventaMnMesaSpot = ventaMnMesaSpot;
    }

    /**
     * Regresa el valor de copraMnMesa72Hr
     *
     * @hibernate.property column="COMPRA_MN_MESA_72HR"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public Double getCompraMnMesa72Hr() {
        return compraMnMesa72Hr;
    }

    /**
     * Establece el valor de compraMnMesa72Hr.
     *
     * @param compraMnMesa72Hr El valor a asignar.
     */
    public void setCompraMnMesa72Hr(Double compraMnMesa72Hr) {
        this.compraMnMesa72Hr = compraMnMesa72Hr;
    }

    /**
     * Regresa el valor de compraMnMesa72Hr
     *
     * @hibernate.property column="VENTA_MN_MESA_72HR"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public Double getVentaMnMesa72Hr() {
        return ventaMnMesa72Hr;
    }

    /**
     * Establece el valor de ventaMnMesa72Hr.
     *
     * @param ventaMnMesa72Hr El valor a asignar.
     */
    public void setVentaMnMesa72Hr(Double ventaMnMesa72Hr) {
        this.ventaMnMesa72Hr = ventaMnMesa72Hr;
    }

    /**
     * Regresa el valor de compraMnMesaVFut
     *
     * @hibernate.property column="COMPRA_MN_MESA_VFUT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public Double getCompraMnMesaVFut() {
        return compraMnMesaVFut;
    }

    /**
     * Establece el valor de compraMnMesaVFut.
     *
     * @param compraMnMesaVFut El valor a asignar.
     */
    public void setCompraMnMesaVFut(Double compraMnMesaVFut) {
        this.compraMnMesaVFut = compraMnMesaVFut;
    }

    /**
     * Regresa el valor de ventaMnMesaVFut
     *
     * @hibernate.property column="VENTA_MN_MESA_VFUT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public Double getVentaMnMesaVFut() {
        return ventaMnMesaVFut;
    }

    /**
     * Establece el valor de ventaMnMesaVFut.
     *
     * @param ventaMnMesaVFut El valor a asignar.
     */
    public void setVentaMnMesaVFut(Double ventaMnMesaVFut) {
        this.ventaMnMesaVFut = ventaMnMesaVFut;
    }

    /**
     * Regresa el valor de cashMnMesa
     *
     * @return double
     */
    public double getCashMnMesa() {
        return compraMnMesaCash - ventaMnMesaCash;
    }

    /**
     * Regresa el valor de tomMnMesa.
     *
     * @return double.
     */
    public double getTomMnMesa() {
    	return compraMnMesaTom - ventaMnMesaTom;
    }

    /**
     * Regresa el valor de spotMnMesa
     *
     * @return double.
     */
    public double getSpotMnMesa() {
    	return compraMnMesaSpot - ventaMnMesaSpot;
    }

    /**
     * Regresa compraMnMesa72Hr - ventaMnMesa72Hr.
     * @return double.
     */
    public double get72HrMnMesa() {
        return compraMnMesa72Hr.doubleValue() - ventaMnMesa72Hr.doubleValue();
    }

    /**
     * Regresa compraMnMesaVFut - ventaMnMesaVFut.
     * @return double.
     */
    public double getVFutMnMesa() {
        return compraMnMesaVFut.doubleValue() - ventaMnMesaVFut.doubleValue();
    }

    /**
     * Regresa el valor de cashMnPizarron
     *
     * @return double
     */
    public double getCashMnPizarron() {
        return compraMnPizarronCash - ventaMnPizarronCash;
    }

    /**
     * Regresa el valor de tomMnPizarron.
     *
     * @return double.
     */
    public double getTomMnPizarron() {
    	return compraMnPizarronTom - ventaMnPizarronTom;
    }

    /**
     * Regresa el valor de spotMnPizarron
     *
     * @return double.
     */
    public double getSpotMnPizarron() {
    	return compraMnPizarronSpot - ventaMnPizarronSpot;
    }

    /**
     * Regresa compraMnPizarron72Hr - ventaMnPizarron72Hr.
     * @return double.
     */
    public double get72HrMnPizarron() {
        return compraMnPizarron72Hr.doubleValue() - ventaMnPizarron72Hr.doubleValue();
    }

    /**
     * Regresa compraMnPizarronVFut - ventaMnPizarronVFut.
     * @return double.
     */
    public double getVFutMnPizarron() {
        return compraMnPizarronVFut.doubleValue() - ventaMnPizarronVFut.doubleValue();
    }

    /**
     * Obtiene el valor total de Compras para la Mesa.
     *
     * @return double
     */
    public double getTotalComprasMnMesa() {
        return getCompraMnMesaCash() + getCompraMnMesaTom() + getCompraMnMesaSpot() +
                getCompraMnMesa72Hr().doubleValue() + getCompraMnMesaVFut().doubleValue();
    }

    /**
     * Obtiene el valor total de Ventas para la Mesa.
     *
     * @return double
     */
    public double getTotalVentasMnMesa() {
        return getVentaMnMesaCash() + getVentaMnMesaTom() + getVentaMnMesaSpot() +
                getVentaMnMesa72Hr().doubleValue() + getVentaMnMesaVFut().doubleValue();
    }

    /**
     * Obtiene el valor total de Compras para el Cliente.
     *
     * @return double
     */
    public double getTotalComprasMnCliente() {
        return getCompraMnClienteCash() + getCompraMnClienteTom() + getCompraMnClienteSpot() +
                getCompraMnCliente72Hr().doubleValue() + getCompraMnClienteVFut().doubleValue();
    }

    /**
     * Obtiene el valor total de Ventas para el Cliente.
     *
     * @return double
     */
    public double getTotalVentasMnCliente() {
        return getVentaMnClienteCash() + getVentaMnClienteTom() + getVentaMnClienteSpot() +
                getVentaMnCliente72Hr().doubleValue() + getVentaMnClienteVFut().doubleValue();
    }

    /**
     * Obtiene el valor total de Compras para el Pizarron.
     *
     * @return double
     */
    public double getTotalComprasMnPizarron() {
        return getCompraMnPizarronCash() + getCompraMnPizarronTom() + getCompraMnPizarronSpot() +
                getCompraMnPizarron72Hr().doubleValue() + getCompraMnPizarronVFut().doubleValue();
    }

    /**
     * Obtiene el valor total de Ventas para el Pizarron.
     *
     * @return double
     */
    public double getTotalVentasMnPizarron() {
        return getVentaMnPizarronCash() + getVentaMnPizarronTom() + getVentaMnPizarronSpot() +
                getVentaMnPizarron72Hr().doubleValue() + getVentaMnPizarronVFut().doubleValue();
    }

    /**
     * Variable Compra Moneda Nacional Cliente Cash
     */
    private double compraMnClienteCash;

    /**
     * Variable Venta Moneda Nacional Cliente Cash
     */
    private double ventaMnClienteCash;

    /**
     * Variable Compra Moneda Nacional Cliente Tom
     */
    private double compraMnClienteTom;

    /**
     * Variable Venta Moneda Nacional Cliente Tom
     */
    private double ventaMnClienteTom;

    /**
     * Variable Compra Moneda Nacional Cliente Spot
     */
    private double compraMnClienteSpot;

    /**
     * Variable Venta Moneda Nacional Cliente Spot
     */
    private double ventaMnClienteSpot;

    /**
     * Compra en moneda nacional para cliente a 72Hr.
     */
    private Double compraMnCliente72Hr;

    /**
     * Venta en moneda nacional para cliente a 72Hr.
     */
    private Double ventaMnCliente72Hr;

    /**
     * Compra en moneda nacional para cliente a Valor Futuro.
     */
    private Double compraMnClienteVFut;

    /**
     * Venta en moneda nacional para cliente a Valor Futuro.
     */
    private Double ventaMnClienteVFut;

    /**
     * Variable Compra Moneda Nacional Pizarr&oacute;n Cash
     */
    private double compraMnPizarronCash;

    /**
     * Variable Venta Moneda Nacional Pizarr&oacute;n Cash
     */
    private double ventaMnPizarronCash;

    /**
     * Variable Compra Moneda Nacional Pizarr&oacute;n Tom
     */
    private double compraMnPizarronTom;

    /**
     * Variable Venta Moneda Nacional Pizarr&oacute;n Tom
     */
    private double ventaMnPizarronTom;

    /**
     * Variable Compra Moneda Nacional Pizarr&oacute;n Spot
     */
    private double compraMnPizarronSpot;

    /**
     * Variable Venta Moneda Nacional Pizarr&oacute;n Spot
     */
    private double ventaMnPizarronSpot;

    /**
     * Compra en moneda nacional para pizarr&oacute;n a 72Hr.
     */
    private Double compraMnPizarron72Hr;

    /**
     * Venta en moneda nacional para pizarr&oacute;n a 72Hr.
     */
    private Double ventaMnPizarron72Hr;

    /**
     * Compra en moneda nacional para pizarr&oacute;n a Valor Futuro.
     */
    private Double compraMnPizarronVFut;

    /**
     * Compra en moneda nacional para pizarr&oacute;n a Valor Futuro.
     */
    private Double ventaMnPizarronVFut;

    /**
     * Variable Compra Moneda Nacional Mesa Cash
     */
    private double compraMnMesaCash;

    /**
     * Variable Venta Moneda Nacional Mesa Cash
     */
    private double ventaMnMesaCash;

    /**
     * Variable Compra Moneda Nacional Mesa Tom
     */
    private double compraMnMesaTom;

    /**
     * Variable Venta Moneda Nacional Mesa Tom
     */
    private double ventaMnMesaTom;

    /**
     * Variable Compra Moneda Nacional Mesa Spot
     */
    private double compraMnMesaSpot;

    /**
     * Variable Venta Moneda Nacional Mesa Spot
     */
    private double ventaMnMesaSpot;

    /**
     * Variable Compra Moneda Nacional Mesa 72Hr
     */
    private Double compraMnMesa72Hr = new Double(0);

    /**
     * Variable Venta Moneda Nacional Mesa 72Hr
     */
    private Double ventaMnMesa72Hr = new Double(0);

    /**
     * Variable Compra Moneda Nacional Mesa VFut
     */
    private Double compraMnMesaVFut = new Double(0);

    /**
     * Variable Venta Moneda Nacional Mesa VFut
     */
    private Double ventaMnMesaVFut = new Double(0);

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 1594296251796835580L;
}

/*
 * $Id: HistoricoSpread.java,v 1.13 2008/02/22 18:25:19 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase persistente para la tabla SC_H_SPREAD.
 *
 * @hibernate.class table="SC_H_SPREAD"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoSpread"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.13 $ $Date: 2008/02/22 18:25:19 $
 */
public class HistoricoSpread implements Serializable {

    /**
     * Constructor por default. no hace nada.
     */
    public HistoricoSpread() {
        super();
    }

    /**
     * Constructor que recibe un spread e inicializa todas sus variables.
     *
     * @param s El spread que inicializa el hist&oacute;rico.
     */
    public HistoricoSpread(Spread s) {
        this();
        setIdSpread(s.getIdSpread());
        setClaveFormaLiquidacion(s.getClaveFormaLiquidacion());
        setUltimaModificacion(s.getUltimaModificacion());
        setDivisa(s.getDivisa().getIdDivisa());
        setCompraCash(s.getCpaVta().getCompraCash());
        setVentaCash(s.getCpaVta().getVentaCash());
        setCompraTom(s.getCpaVta().getCompraTom());
        setVentaTom(s.getCpaVta().getVentaTom());
        setCompraSpot(s.getCpaVta().getCompraSpot());
        setVentaSpot(s.getCpaVta().getVentaSpot());
        setCompra72Hr(s.getCpaVta().getCompra72Hr());
        setVenta72Hr(s.getCpaVta().getVenta72Hr());
        setCompraVFut(s.getCpaVta().getCompraVFut());
        setVentaVFut(s.getCpaVta().getVentaVFut());
        setTipoPizarron(s.getTipoPizarron());
    }

    /**
     * Regresa el valor de idSpread.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_SPREAD"
     * not-null="true"
     * @return int.
     */
    public int getIdSpread() {
        return idSpread;
    }

    /**
     * Fija el valor de idSpread.
     *
     * @param idSpread El valor a asignar.
     */
    public void setIdSpread(int idSpread) {
        this.idSpread = idSpread;
    }

    /**
     * Regresa el valor de idMesaCambio
     *
     * @hibernate.property column ="ID_MESA_CAMBIO"
     * not-null="true"
     * @return int.
     */
    public int getMesaCambio() {
        return idMesaCambio;
    }

    /**
     * Fija el valor de idMesaCambio.
     *
     * @param idMesaCambio El valor a asignar.
     */

    public void setMesaCambio(int idMesaCambio) {
        this.idMesaCambio = idMesaCambio;
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
     * Regresa el valor de claveFormaLiquidacion.
     *
     * @hibernate.property column="ID_CANAL"
     * not-null="true"
     * unique="false"
     * @return String.
     */

    public String getIdCanalMesa() {
        return _canalMesa;
    }

    /**
     * Fija el valor de canalMesa.
     *
     * @param canalMesa El valor a asignar.
     */
    public void setIdCanalMesa(String canalMesa) {
        _canalMesa = canalMesa;
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
     * @param ultimaModificacion El valor a asignar.
     */
    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    /**
     * Regresa el valor de ultimaModificacion.
     *
     * @hibernate.property column="ID_DIVISA"
     * not-null="true"
     * unique="false"
     * @return Date.
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
     * @return Double.
     */
    public Double getCompra72Hr() {
        return compra72Hr;
    }

    /**
     * Establece el valor de compra72Hr.
     *
     * @param compra72Hr El valor a asignar.
     */
    public void setCompra72Hr(Double compra72Hr) {
        this.compra72Hr = compra72Hr;
    }

    /**
     * Regresa el valor de ventaSpot.
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
     * Regresa el valor de ventaSpot.
     *
     * @hibernate.property column="COMPRA_VFUT"
     * not-null="true"
     * unique="false"
     * @return Double.
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
     * @return Double.
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
     * Obtiene el tipo de Pizarron para el spread.
     *
     * @hibernate.many-to-one column="ID_TIPO_PIZARRON"
     * cascade="none"
     * class="com.ixe.ods.sica.model.TipoPizarron"
     * outer-join="auto"
     * unique="false"
     * @return TipoPizarron
     */
    public TipoPizarron getTipoPizarron() {
    	return tipoPizarron;
    }

   /**
    * Asigna el valor para tipoPizarron.
    *
    * @param tipoPizarron El valor para tipoPizarron.
    */
	public void setTipoPizarron(TipoPizarron tipoPizarron) {
		this.tipoPizarron = tipoPizarron;
	}

    /**
     * El identificador del registro.
     */
    private int idSpread;

    /**
     * El identificador del registro.
     */
    private int idMesaCambio;

    /**
     * La clave del producto.
     */
    private String claveFormaLiquidacion;

    /**
     * El componente canalMesa.
     */
    private String _canalMesa;

    /**
     * La fecha en que fue modificado el registro.
     */
    private Date ultimaModificacion = new Date();

    /**
     * El valor o spread de compra cash.
     */
    private double compraCash;

    /**
     * El valor o spread de venta cash.
     */
    private double ventaCash;

    /**
     * El valor o spread de compra tom.
     */
    private double compraTom;

    /**
     * El valor o spread de venta tom.
     */
    private double ventaTom;

    /**
     * El valor o spread de compra spot.
     */
    private double compraSpot;

    /**
     * El valor o spread de venta spot.
     */
    private double ventaSpot;

    /**
     * El spread de compra de 72 hrs.
     */
    private Double compra72Hr;

    /**
     * El spread de venta de 72 hrs.
     */
    private Double venta72Hr;

    /**
     * El spread de compra de Valor Futuro.
     */
    private Double compraVFut;

    /**
     * El spread de compra de Valor Futuro.
     */
    private Double ventaVFut;

    /**
     * Relaci&oacute;n muchos a uno con Divisa.
     */
    private String _divisa;

    /**
     * El id del tipo de pizarron para el spread.
     */
    private TipoPizarron tipoPizarron;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -2240299097192295467L;
}
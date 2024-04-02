/*
 * $Id: DetalleSwapVO.java,v 1.11 2008/02/22 18:25:24 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Value Object que representa el detalle de un swap, que es de hecho un deal. Contiene la informaci&oacute;n
 * m&iacute;nima indispensable para generar un deal interbancario.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:24 $
 */
public class DetalleSwapVO implements Serializable {

    /**
     * Constructor por default.
     */
    public DetalleSwapVO() {
        super();
    }

    /**
     * Regresa el valor de idDeal.
     *
     * @return int.
     */
    public int getIdDeal() {
        return idDeal;
    }

    /**
     * Establece el valor de idDeal.
     *
     * @param idDeal El valor a asignar.
     */
    public void setIdDeal(int idDeal) {
        this.idDeal = idDeal;
    }

    /**
     * Regresa la fecha de captura del deal.
     *
     * @return Date.
     */
    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    /**
     * Establece el valor de fechaCaptura.
     *
     * @param fechaCaptura El valor a asignar.
     */
    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    /**
     * Regresa el valor de fechaLiquidacion.
     *
     * @return Date.
     */
    public Date getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    /**
     * Establece el valor de fechaLiquidacion.
     *
     * @param fechaLiquidacion El valor a asignar.
     */
    public void setFechaLiquidacion(Date fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    /**
     * Regresa el valor de tipo.
     *
     * @return String.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el valor de tipo.
     *
     * @param tipo El valor a asignar.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Regresa el valor de compra.
     *
     * @return boolean.
     */
    public boolean isCompra() {
        return compra;
    }

    /**
     * Establece el valor de compra.
     *
     * @param compra El valor a asignar.
     */
    public void setCompra(boolean compra) {
        this.compra = compra;
    }

    /**
     * Regresa el valor de fechaValor.
     *
     * @return String.
     */
    public String getFechaValor() {
        return fechaValor;
    }

    /**
     * Establece el valor de fechaValor.
     *
     * @param fechaValor El valor a asignar.
     */
    public void setFechaValor(String fechaValor) {
        this.fechaValor = fechaValor;
    }

    /**
     * Regresa el valor de claveFormaLiquidacion.
     *
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
     * Regresa el valor de monto.
     *
     * @return double.
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Establece el valor de monto.
     *
     * @param monto El valor a asignar.
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * Regresa el valor de tipoCambio.
     *
     * @return double.
     */
    public double getTipoCambio() {
        return tipoCambio;
    }

    /**
     * Establece el valor de tipoCambio.
     *
     * @param tipoCambio El valor a asignar.
     */
    public void setTipoCambio(double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    /**
     * Override para regresar los valores de las propiedades del objeto en un String.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("tipo", tipo).
                append("compra", compra).append("fechaValor", fechaValor).
                append("claveFormaLiquidacion", claveFormaLiquidacion).append("monto", monto).
                append("tipoCambio", tipoCambio).toString();
    }

    /**
     * El n&uacute;mero de deal.
     */
    private int idDeal;

    /**
     * El tipo de deal (Inicio, Fin, etc).
     */
    private String tipo;

    /**
     * Si es compra o venta.
     */
    private boolean compra;

    /**
     * CASH | TOM | SPOT.
     */
    private String fechaValor;

    /**
     * La fecha de captura del swap.
     */
    private Date fechaCaptura;

    /**
     * La fecha de liquidaci&oacute;n, de acuerdo a la fecha valor.
     */
    private Date fechaLiquidacion;

    /**
     * La clave del producto.
     */
    private String claveFormaLiquidacion;

    /**
     * El monto total del deal.
     */
    private double monto;

    /**
     * El tipo de cambio para el detalle de la operaci&oacute;n.
     */
    private double tipoCambio;
}

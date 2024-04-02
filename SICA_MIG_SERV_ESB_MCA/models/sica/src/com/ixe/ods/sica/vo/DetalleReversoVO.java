/*
 * $Id: DetalleReversoVO.java,v 1.2 2008/02/22 18:25:25 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.vo;

/**
 * Value Object para el modelo de Hibernate DetalleReverso.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.2 $ $Date: 2008/02/22 18:25:25 $
 */
public class DetalleReversoVO {

    /**
     * Constructor vac&iacute;o. No hace nada.
     */
    public DetalleReversoVO() {
        super();
    }

    /**
     * Regresa el valor de folioDetalle.
     *
     * @return int.
     */
    public int getFolioDetalle() {
        return folioDetalle;
    }

    /**
     * Establece el valor de folioDetalle.
     *
     * @param folioDetalle El valor a asignar.
     */
    public void setFolioDetalle(int folioDetalle) {
        this.folioDetalle = folioDetalle;
    }

    /**
     * Regresa el valor de monto.
     *
     * @return double.
     */
    public Double getMonto() {
        return monto;
    }

    /**
     * Establece el valor de monto.
     *
     * @param monto El valor a asignar.
     */
    public void setMonto(Double monto) {
        this.monto = monto;
    }

    /**
     * Regresa el valor de tipoCambio.
     *
     * @return double.
     */
    public Double getTipoCambio() {
        return tipoCambio;
    }

    /**
     * Establece el valor de tipoCambio.
     *
     * @param tipoCambio El valor a asignar.
     */
    public void setTipoCambio(Double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    /**
     * El folio del detalle de deal.
     */
    private int folioDetalle;

    /**
     * El monto por el que se reversa el detalle de deal (opcional).
     */
    private Double monto;

    /**
     * El tipo de cambio por el que reversa el detalle de deal (opcional).
     */
    private Double tipoCambio;
}

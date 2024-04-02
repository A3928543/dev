/*
 * $Id: HistoricoTipoCambio.java,v 1.13 2008/04/16 18:21:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * <p>Clase persistente para la tabla SC_H_TIPO_CAMBIO, donde se almacena el
 * hist&oacute;rico de tipos de cambio con el que cerr&oacute; el
 * d&iacute;a.</p>
 * <p>Este hist&oacute;rico se utiliza para el c&aacute;lculo del l&iacute;mite
 * de riesgo del VaR por lo que deber&aacute; existir un m&iacute;nimo de 100
 * registros en la tabla.</p>
 *
 * @hibernate.class table="SC_H_TIPO_CAMBIO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoTipoCambio"
 * dynamic-update="true"
 * 
 * @hibernate.query name="findTipoCambioCierreCienUltimosDias"
 * query="FROM HistoricoTipoCambio AS htc WHERE htc.fecha BETWEEN ? AND ? ORDER BY htc.fecha DESC"
 * 
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.13 $ $Date: 2008/04/16 18:21:32 $
 */
public class HistoricoTipoCambio implements Serializable {

    /**
     * Constructor vac&iacute;o, no hace nada.
     */
    public HistoricoTipoCambio() {
        super();
    }

    /**
     * Regresa la volatilidad de acuerdo a la lista de HistoricoTipoCambio proporcionada.
     *
     * @param tiposCambio La lista de objetos tiposCambio.
     * @return BigDecimal.
     */
    public static BigDecimal getVolatilidad(List tiposCambio) {
        for (int i = tiposCambio.size() - 1; i > 0; i--) {
            HistoricoTipoCambio hoy = (HistoricoTipoCambio) tiposCambio.get(i - 1);
            HistoricoTipoCambio ayer = (HistoricoTipoCambio) tiposCambio.get(i);
            BigDecimal variacion = new BigDecimal("" + Math.log(new BigDecimal("" +
                    hoy.getTipoCambio() / ayer.getTipoCambio()).doubleValue()) * CIEN).
                    setScale(DIEZ, BigDecimal.ROUND_HALF_UP);
            ayer.setVariacion(variacion);
        }
        if (!tiposCambio.isEmpty()) {
            HistoricoTipoCambio ultimo = (HistoricoTipoCambio) tiposCambio.
                    get(tiposCambio.size() - 1);
            ultimo.setX(ultimo.getY().setScale(DIEZ, BigDecimal.ROUND_HALF_UP));
            for (int i = tiposCambio.size() - 2; i >= 0; i--) {
                HistoricoTipoCambio hoy = (HistoricoTipoCambio) tiposCambio.get(i);
                HistoricoTipoCambio ayer = (HistoricoTipoCambio) tiposCambio.get(i + 1);
                BigDecimal primero = LAMBDA.multiply(ayer.getX());
                primero = primero.setScale(DIEZ, BigDecimal.ROUND_HALF_UP);
                BigDecimal segundo = (new BigDecimal("" + 1).subtract(LAMBDA)).multiply(hoy.getY());
                segundo = segundo.setScale(DIEZ, BigDecimal.ROUND_HALF_UP);
                hoy.setX(primero.add(segundo).setScale(DIEZ, BigDecimal.ROUND_HALF_UP));
            }
            return new BigDecimal("" + (Math.sqrt(((HistoricoTipoCambio) tiposCambio.get(1)).getX().
                    doubleValue()))).divide(
                        new BigDecimal("" + CIEN).setScale(DIEZ, BigDecimal.ROUND_HALF_UP),
                            BigDecimal.ROUND_HALF_UP).setScale(DIEZ, BigDecimal.ROUND_HALF_UP);
        }
        return new BigDecimal("" + 0);
    }

    /**
     * Regresa el valor de idHistorico.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_HISTORICO"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_H_TIPO_CAMBIO_SEQ"
     * @return int.
     */
    public int getIdHistorico() {
        return idHistorico;
    }

    /**
     * Fija el valor de idHistorico.
     *
     * @param idHistorico El valor a asignar.
     */
    public void setIdHistorico(int idHistorico) {
        this.idHistorico = idHistorico;
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
     * Fija el valor de fecha.
     *
     * @param fecha El valor a asignar.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Regresa el valor de tipoCambio.
     *
     * @hibernate.property column="TIPO_CAMBIO"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getTipoCambio() {
        return tipoCambio;
    }

    /**
     * Fija el valor de tipoCambio.
     *
     * @param tipoCambio El valor a asignar.
     */
    public void setTipoCambio(double tipoCambio) {
        this.tipoCambio = tipoCambio;
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
     * Regresa el valor de variacion.
     *
     * @return BigDecimal.
     */
    public BigDecimal getVariacion() {
        return variacion;
    }

    /**
     * Establece el valor de variacion.
     *
     * @param variacion El valor a asignar.
     */
    public void setVariacion(BigDecimal variacion) {
        this.variacion = variacion;
    }

    /**
     * Regresa la potencia 2 de la variaci&oacute;n.
     *
     * @return BigDecimal.
     */
    public BigDecimal getY() {
        return variacion.multiply(variacion).setScale(DIEZ, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Regresa el valor de x.
     *
     * @return BigDecimal.
     */
    public BigDecimal getX() {
        return x;
    }

    /**
     * Establece el valor de x.
     *
     * @param x El valor a asignar.
     */
    public void setX(BigDecimal x) {
        this.x = x;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof HistoricoTipoCambio)) {
            return false;
        }
        HistoricoTipoCambio castOther = (HistoricoTipoCambio) other;
        return new EqualsBuilder().append(this.getIdHistorico(),
                castOther.getIdHistorico()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdHistorico()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private int idHistorico;

    /**
     * La fecha para el tipo de cambio, debe ser &uacute;nica.
     */
    private Date fecha;

    /**
     * El valor del valor SPOT en ese d&iacute;a al cierre.
     */
    private double tipoCambio;

    /**
     * Relaci&oacute;n muchos-a-uno con Divisa.
     */
    private Divisa _divisa;

    /**
     * El valor inicial para variacion (no peristente).
     */
    private BigDecimal variacion = new BigDecimal("" + 0);

    /**
     * El valor inicial para x (no peristente).
     */
    private BigDecimal x = new BigDecimal("" + 0);

    /**
     * Constante para el valor 10.
     */
    private static final int DIEZ = 10;

    /**
     * Constante para el valor 99.0.
     */
    private static final double NUM_99 = 99.0;

    /**
     * Constante para el valor 100.0.
     */
    private static final double CIEN = 100.0;

    /**
     * Constante para el valor 10.01.
     */
    private static final double NUM_0_01 = 0.01;

    /**
     * Constante LAMBDA para calcular el nivel VaR.
     */
    private static final BigDecimal LAMBDA = new BigDecimal("" +
            Math.exp(Math.log(NUM_0_01) / NUM_99)).setScale(8, BigDecimal.ROUND_HALF_UP);
    
    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 1027103566778858098L;
}

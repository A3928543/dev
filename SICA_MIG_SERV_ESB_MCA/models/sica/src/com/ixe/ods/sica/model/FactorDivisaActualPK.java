/*
 * $Id: FactorDivisaActualPK.java,v 1.12 2008/02/22 18:25:22 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:22 $
 */
public class FactorDivisaActualPK implements Serializable {

    /**
     * Constructor vac&iacute;o. No hace nada.
     */
    public FactorDivisaActualPK() {
        super();
    }

    /**
     * Constructor de conveniencia. Inicializa las variables de instancia.
     *
     * @param fromDivisa La divisa From.
     * @param toDivisa La divisa To.
     */
    public FactorDivisaActualPK(Divisa fromDivisa, Divisa toDivisa) {
        this();
        this.fromDivisa = fromDivisa;
        this.toDivisa = toDivisa;
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
        return fromDivisa;
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
        return toDivisa;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof FactorDivisaActualPK)) {
            return false;
        }
        FactorDivisaActualPK object = (FactorDivisaActualPK) other;
        return new EqualsBuilder()
                .append(this.getFromDivisa(), object.getFromDivisa())
                .append(this.getToDivisa(), object.getToDivisa())
                .isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getFromDivisa()).append(getToDivisa()).toHashCode();
    }

    /**
     * Fija el valor de toDivisa.
     *
     * @param toDivisa El valor a asignar.
     */
    public void setToDivisa(Divisa toDivisa) {
        this.toDivisa = toDivisa;
    }

    /**
     * La divisa de referenia.
     */
    private Divisa fromDivisa;

    /**
     * La divisa a la que se aplica el factor.
     */
    private Divisa toDivisa;
}

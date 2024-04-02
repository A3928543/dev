/*
 * $Id: CombinacionDivisaPK.java,v 1.2 2010/04/13 20:13:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Llave primaria de la clase CombinacionDivisa.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2010/04/13 20:13:33 $
 * @see com.ixe.ods.sica.model.CombinacionDivisa
 */
public class CombinacionDivisaPK implements Serializable {

    public CombinacionDivisaPK() {
        super();
    }

    /**
     * Regresa el valor de divisa.
     *
     * @hibernate.many-to-one column="DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return Divisa.
     */
    public Divisa getDivisa() {
        return divisa;
    }

    /**
     * Establece el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        this.divisa = divisa;
    }

    /**
     * Regresa el valor de contraDivisa.
     *
     * @hibernate.many-to-one column="CONTRA_DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return Divisa.
     */
    public Divisa getContraDivisa() {
        return contraDivisa;
    }

    /**
     * Establece el valor de contraDivisa.
     *
     * @param contraDivisa El valor a asignar.
     */
    public void setContraDivisa(Divisa contraDivisa) {
        this.contraDivisa = contraDivisa;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof CombinacionDivisaPK)) {
            return false;
        }
        CombinacionDivisaPK object = (CombinacionDivisaPK) other;
        return new EqualsBuilder()
                .append(this.getDivisa(), object.getDivisa())
                .append(this.getContraDivisa(), object.getContraDivisa())
                .isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getDivisa()).append(getContraDivisa()).toHashCode();
    }

    private Divisa divisa;
    private Divisa contraDivisa;
}

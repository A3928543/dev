/*
 * $Id: CombinacionDivisa.java,v 1.2 2010/04/13 20:13:12 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_COMBINACION_DIVISA, que utiliza el sistema KONDOR para conocer
 * las combinaciones de divisas que es posible operar en el SICA.
 *
 * @hibernate.class table="SC_COMBINACION_DIVISA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.CombinacionDivisa"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2010/04/13 20:13:12 $
 */
public class CombinacionDivisa implements Serializable {

    /**
     * Constructor vac&iacute;o. No hace nada.
     */
    public CombinacionDivisa() {
        super();
    }

    /**
     * Regresa el valor de id.
     *
     * @hibernate.id
     * @return CombinacionDivisaPK.
     */
    public CombinacionDivisaPK getId() {
        return id;
    }

    /**
     * Establece el valor de id.
     *
     * @param id El valor a asignar.
     */
    public void setId(CombinacionDivisaPK id) {
        this.id = id;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof CombinacionDivisa)) {
            return false;
        }
        CombinacionDivisa castOther = (CombinacionDivisa) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    /**
     * El objeto que representa la llave primaria del registro.
     */
    private CombinacionDivisaPK id = new CombinacionDivisaPK();
}

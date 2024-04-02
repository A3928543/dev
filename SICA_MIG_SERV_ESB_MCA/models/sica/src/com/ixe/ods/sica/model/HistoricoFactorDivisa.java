/*
 * $Id: HistoricoFactorDivisa.java,v 1.12 2008/02/22 18:25:19 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * @hibernate.class table="SC_H_FACTOR_DIVISA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoFactorDivisa"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:19 $
 */
public class HistoricoFactorDivisa implements Serializable {

    /**
     * Constructor vac&iacute;o, no hace nada.
     */
    public HistoricoFactorDivisa() {
        super();
    }

    /**
     * Regresa el valor de idFactorDivisa.
     *
     * @hibernate.id column="ID_FACTOR_DIVISA"
     * unsaved-value="0"
     * generator-class="assigned"
     * @return int.
     */
    public int getIdFactorDivisa() {
        return idFactorDivisa;
    }

    /**
     * Fija el valor de idFactorDivisa.
     *
     * @param idFactorDivisa El valor a asginar.
     */
    public void setIdFactorDivisa(int idFactorDivisa) {
        this.idFactorDivisa = idFactorDivisa;
    }

    /**
     * Regresa el valor de facDiv.
     *
     * @hibernate.component class="com.ixe.ods.sica.model.FacDiv"
     * @return FacDiv.
     */
    public FacDiv getFacDiv() {
        return _facDiv;
    }

    /**
     * Fija el valor de facDiv.
     *
     * @param facDiv El valor a asignar.
     */
    public void setFacDiv(FacDiv facDiv) {
        _facDiv = facDiv;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof HistoricoFactorDivisa)) {
            return false;
        }
        HistoricoFactorDivisa castOther = (HistoricoFactorDivisa) other;
        return new EqualsBuilder().append(this.getIdFactorDivisa(), castOther.getIdFactorDivisa()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdFactorDivisa()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private int idFactorDivisa;

    /**
     * El componente FacDiv.
     */
    private FacDiv _facDiv = new FacDiv();
}

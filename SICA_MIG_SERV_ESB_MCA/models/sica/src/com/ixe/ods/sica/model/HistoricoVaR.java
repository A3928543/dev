/*
 * $Id: HistoricoVaR.java,v 1.12 2008/02/22 18:25:22 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Clase persistente para la tabla SC_H_VAR.
 *
 * @hibernate.class table="SC_H_VAR"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoVaR"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:22 $
 */
public class HistoricoVaR implements Serializable {

    /**
     * Constructor vac&iacute;o, no hace nada.
     */
    public HistoricoVaR() {
        super();
    }

    /**
     * Regresa el valor de idHistorico.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_H_VAR"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_H_VAR_SEQ"
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
     * Regresa el valor de hora.
     *
     * @hibernate.property column="HORA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getHora() {
        return hora;
    }

    /**
     * Fija el valor de hora.
     *
     * @param hora El valor a asignar.
     */
    public void setHora(String hora) {
        this.hora = hora;
    }

    /**
     * Regresa el valor de var.
     *
     * @hibernate.property column="VAR"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getVar() {
        return var;
    }

    /**
     * Fija el valor de var.
     *
     * @param var El valor a asignar.
     */
    public void setVar(double var) {
        this.var = var;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof HistoricoVaR)) {
            return false;
        }
        HistoricoVaR castOther = (HistoricoVaR) other;
        return new EqualsBuilder().append(this.getIdHistorico(), castOther.getIdHistorico()).isEquals();
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
     * La hora en la que se calcul&oacute; el VaR.
     */
    private String hora;

    /**
     * El valor del VaR.
     */
    private double var;
}

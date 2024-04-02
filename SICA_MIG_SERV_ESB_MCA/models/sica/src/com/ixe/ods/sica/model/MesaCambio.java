/*
 * $Id: MesaCambio.java,v 1.12 2008/02/22 18:25:19 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Clase persistente para la tabla SC_MESA_CAMBIO.
 *
 * @hibernate.class table="SC_MESA_CAMBIO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.MesaCambio"
 * dynamic-update="true"
 * 
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:19 $
 */
public class MesaCambio implements Serializable {

    /**
     * Constructor por default, no hace nada.
     */
    public MesaCambio() {
        super();
    }

    /**
     * Regresa el valor de idMesaCambio.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_MESA_CAMBIO"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_MESA_CAMBIO_SEQ"
     * @return int.
     */
    public int getIdMesaCambio() {
        return idMesaCambio;
    }

    /**
     * Fija el valor para idMesaCambio.
     *
     * @param idMesaCambio El valor a asignar.
     */
    public void setIdMesaCambio(int idMesaCambio) {
        this.idMesaCambio = idMesaCambio;
    }

    /**
     * Regresa el valor de nombre.
     *
     * @hibernate.property column="NOMBRE"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Fija el valor para nombre.
     *
     * @param nombre El valor a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Regresa el valor de divisaReferencia.
     *
     * @hibernate.many-to-one column="ID_DIVISA_REFERENCIA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return Divisa.
     */
    public Divisa getDivisaReferencia() {
        return _divisaReferencia;
    }

    /**
     * Regresa el valor de reportaBanxico.
     *
     * @hibernate.property column="REPORTA_BANXICO"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isReportaBanxico() {
        return reportaBanxico;
    }

    /**
     * Fija el valor de reportaBanxico.
     *
     * @param reportaBanxico El valor a asignar.
     */
    public void setReportaBanxico(boolean reportaBanxico) {
        this.reportaBanxico = reportaBanxico;
    }


    /**
     * Fija el valor para divisaReferencia.
     *
     * @param divisaReferencia El valor a asignar.
     */
    public void setDivisaReferencia(Divisa divisaReferencia) {
        _divisaReferencia = divisaReferencia;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof MesaCambio)) {
            return false;
        }
        MesaCambio castOther = (MesaCambio) other;
        return new EqualsBuilder().append(this.getIdMesaCambio(), castOther.getIdMesaCambio()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdMesaCambio()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private int idMesaCambio;

    /**
     * El nombre de la mesa de cambios.
     */
    private String nombre;

    /**
     * La divisa de referencia. Relaci&oacute;n muchos-a-uno con Divisa.
     */
    private Divisa _divisaReferencia;

    /**
     * Si reporta a Banxico o no.
     */
    private boolean reportaBanxico;
    
    /**
     * Constante para la Mesa de Cambio de Operacion.
     */
    public static final int OPERACION = 1;
}

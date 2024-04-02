/*
 * $Id: ReversoDetalle.java,v 1.2 2008/02/22 18:25:24 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la entidad SC_REVERSO_DETALLE, que guarda la informaci&oacute;n sobre los
 * reversos realizados.
 *
 * @hibernate.class table="SC_REVERSO_DETALLE"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.ReversoDetalle"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2008/02/22 18:25:24 $
 */
public class ReversoDetalle implements Serializable {

    /**
     * Constructor por default. No hace nada.
     */
    public ReversoDetalle() {
        super();
    }

    /**
     * Regresa el valor de idReversoDetalle.
     *
     * @return int.
     * @hibernate.id generator-class="sequence"
     * column="ID_REVERSO_DETALLE"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_REVERSO_DETALLE_SEQ"
     */
    public int getIdReversoDetalle() {
        return idReversoDetalle;
    }

    /**
     * Establece el valor de idReversoDetalle.
     *
     * @param idReversoDetalle El valor a asignar.
     */
    public void setIdReversoDetalle(int idReversoDetalle) {
        this.idReversoDetalle = idReversoDetalle;
    }

    /**
     * Regresa el valor de montoNuevo.
     *
     * @hibernate.property column="MONTO_NUEVO"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return Double.
     */
    public Double getMontoNuevo() {
        return montoNuevo;
    }

    /**
     * Establece el valor de montoNuevo.
     *
     * @param montoNuevo El valor a asignar.
     */
    public void setMontoNuevo(Double montoNuevo) {
        this.montoNuevo = montoNuevo;
    }

    /**
     * Regresa el valor de tipoCambioNuevo.
     *
     * @hibernate.property column="TIPO_CAMBIO_NUEVO"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return Double.
     */
    public Double getTipoCambioNuevo() {
        return tipoCambioNuevo;
    }

    /**
     * Establece el valor de tipoCambioNuevo.
     *
     * @param tipoCambioNuevo El valor a asignar.
     */
    public void setTipoCambioNuevo(Double tipoCambioNuevo) {
        this.tipoCambioNuevo = tipoCambioNuevo;
    }

    /**
     * Regresa el valor de reverso.
     *
     * @hibernate.many-to-one column="ID_REVERSO"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Reverso"
     * outer-join="auto"
     * unique="false"
     * @return Reverso.
     */
    public Reverso getReverso() {
        return reverso;
    }

    /**
     * Establece el valor de reverso.
     *
     * @param reverso El valor a asignar.
     */
    public void setReverso(Reverso reverso) {
        this.reverso = reverso;
    }

    /**
     * Regresa el valor de dealDetalle.
     *
     * @hibernate.many-to-one column="ID_DEAL_POSICION"
     * cascade="none"
     * class="com.ixe.ods.sica.model.DealDetalle"
     * outer-join="auto"
     * unique="false"
     * @return DealDetalle.
     */
    public DealDetalle getDealDetalle() {
        return dealDetalle;
    }

    /**
     * Establece el valor de dealDetalle.
     *
     * @param dealDetalle El valor a asignar.
     */
    public void setDealDetalle(DealDetalle dealDetalle) {
        this.dealDetalle = dealDetalle;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof ReversoDetalle)) {
            return false;
        }
        ReversoDetalle castOther = (ReversoDetalle) other;
        return new EqualsBuilder().append(this.getIdReversoDetalle(),
                castOther.getIdReversoDetalle()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdReversoDetalle()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private int idReversoDetalle;

    /**
     * El monto que debi&oacute; capturar el usuario inicialmente (opcional).
     */
    private Double montoNuevo;

    /**
     * El tipo de cambio al que debi&oacute; pactarse el detalle original (opcional).
     */
    private Double tipoCambioNuevo;

    /**
     * Relaci&oacute;n muchos-a-uno con el encabezado del reverso.
     */
    private Reverso reverso;

    /**
     * Relaci&oacute;n muchos-a-uno con el detalle original del deal que se reversa.
     */
    private DealDetalle dealDetalle;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -6531520117739159829L;
}

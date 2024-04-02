/*
 * $Id: DealLog.java,v 1.12 2008/02/22 18:25:21 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase persistente para la tabla SC_DEAL_LOG. Almacena las modificaciones
 * realizadas al deal.
 *
 * @hibernate.class table="SC_DEAL_LOG"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.DealLog"
 * dynamic-update="true"
 * 
 * @hibernate.query name="findLogByDealTodos"
 * query="FROM DealLog AS d WHERE d.deal.idDeal = ?"
 * 
 * @hibernate.query name="findLogByDeal"
 * query="FROM DealLog AS d WHERE d.deal.idDeal = ? AND d.folioDetalle is null"
 * 
 * @hibernate.query name="findLogByDetalle"
 * query="FROM DealLog AS d WHERE d.idDealPosicion = ? AND d.folioDetalle is not null"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:21 $
 */
public class DealLog implements Serializable {

    /**
     * Constructor por default, no hace nada.
     */
    public DealLog() {
        super();
    }

    /**
     * Regresa el valor de idDealLog.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_DEAL_LOG"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_DEAL_LOG_SEQ"
     * @return int.
     */
    public int getIdDealLog() {
        return idDealLog;
    }

    /**
     * Fija el valor de idDealLog.
     *
     * @param idDealLog El valor a asignar.
     */
    public void setIdDealLog(int idDealLog) {
        this.idDealLog = idDealLog;
    }

    /**
     * Regresa el valor de fechaCambio.
     *
     * @hibernate.property column="FECHA_CAMBIO"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaCambio() {
        return fechaCambio;
    }

    /**
     * Fija el valor de fechaCambio.
     *
     * @param fechaCambio El valor a asignar.
     */
    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    /**
     * Regresa el valor de folioDetalle.
     *
     * @hibernate.property column="FOLIO_DETALLE"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getFolioDetalle() {
        return folioDetalle;
    }

    /**
     * Fija el valor de folioDetalle.
     *
     * @param folioDetalle El valor a asignar.
     */
    public void setFolioDetalle(Integer folioDetalle) {
        this.folioDetalle = folioDetalle;
    }

    /**
     * Regresa el valor de idDealPosicion.
     *
     * @hibernate.property column="ID_DEAL_POSICION"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getIdDealPosicion() {
        return idDealPosicion;
    }

    /**
     * Fija el valor de idDealPosicion.
     *
     * @param idDealPosicion El valor a asignar.
     */
    public void setIdDealPosicion(Integer idDealPosicion) {
        this.idDealPosicion = idDealPosicion;
    }

    /**
     * Regresa el valor de nombreCampo.
     *
     * @hibernate.property column="NOMBRE_CAMPO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getNombreCampo() {
        return nombreCampo;
    }

    /**
     * Fija el valor de nombreCampo.
     *
     * @param nombreCampo El valor a asignar.
     */
    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    /**
     * Regresa el valor de valorPrevio.
     *
     * @hibernate.property column="VALOR_PREVIO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getValorPrevio() {
        return valorPrevio;
    }

    /**
     * Fija el valor de valorPrevio.
     *
     * @param valorPrevio El valor a asignar.
     */
    public void setValorPrevio(String valorPrevio) {
        this.valorPrevio = valorPrevio;
    }

    /**
     * Regresa el valor de valorNuevo.
     *
     * @hibernate.property column="VALOR_NUEVO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getValorNuevo() {
        return valorNuevo;
    }

    /**
     * Fija el valor de valorNuevo.
     *
     * @param valorNuevo El valor a asignar.
     */
    public void setValorNuevo(String valorNuevo) {
        this.valorNuevo = valorNuevo;
    }

    /**
     * Regresa el valor de deal.
     *
     * @hibernate.many-to-one column="ID_DEAL"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Deal"
     * outer-join="auto"
     * unique="false"
     * @return Deal.
     */
    public Deal getDeal() {
        return _deal;
    }

    /**
     * Fija el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public void setDeal(Deal deal) {
        _deal = deal;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof DealLog)) {
            return false;
        }
        DealLog castOther = (DealLog) other;
        return new EqualsBuilder().append(this.getIdDealLog(), castOther.getIdDealLog()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdDealLog()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    protected int idDealLog;

    /**
     * La fecha del cambio de valores.
     */
    private Date fechaCambio;

    /**
     * El folio del detalle del deal.
     */
    private Integer folioDetalle;

    /**
     * El identificador del detalle del deal.
     */
    private Integer idDealPosicion;

    /**
     * El nombre del campo que fue modificado.
     */
    private String nombreCampo;

    /**
     * El valor previo a la modificaci&oacute;n.
     */
    private String valorPrevio;

    /**
     * El nuevo valor.
     */
    private String valorNuevo;

    /**
     * Relaci&oacute;n muchos-a-uno con Deal.
     */
    private Deal _deal;
}

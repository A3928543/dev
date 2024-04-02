/*
 * $Id: SpreadActual.java,v 1.12 2008/02/22 18:25:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la table SC_SPREAD_ACTUAL. Indice donde se almacena
 * cu&aacute;l es el &uacute;ltimo spread que se utiliz&oacute; en el
 * pizarr&oacute;n para la consulta.
 *
 * @hibernate.class table="SC_SPREAD_ACTUAL"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.SpreadActual"
 * dynamic-update="true"
 *
 * @hibernate.query name="findSpreadActualByMesaCanalFormaLiquidacionDivisa"
 * query="FROM SpreadActual AS sa WHERE sa.id.mesaCambio.idMesaCambio = ? AND
 * sa.id.canal.idCanal = ? AND sa.id.claveFormaLiquidacion = ? AND sa.id.divisa.idDivisa = ?"
 *
 * @hibernate.query name="findSpreadActualByTipoPizarronFormaLiquidacionDivisa"
 * query="FROM SpreadActual AS sa WHERE sa.id.tipoPizarron.idTipoPizarron = ? AND
 * sa.id.claveFormaLiquidacion = ? AND sa.id.divisa.idDivisa = ?"
 *
 * @hibernate.query name="findSpreadActual"
 * query="FROM SpreadActual AS sa WHERE sa.idSpread = ?"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:23 $
 */
public class SpreadActual implements Serializable {

    /**
     * Constructor por default, no hace nada.
     */
    public SpreadActual() {
        super();
    }

    /**
     * Regresa el valor de idSpreadActual.
     *
     * @hibernate.id
     * @return SpreadActualPK.
     */
    public SpreadActualPK getId() {
        return _id;
    }

    /**
     * Fija el valor de idSpreadActual.
     *
     * @param id El valor a asignar.
     */
    public void setId(SpreadActualPK id) {
        _id = id;
    }

    /**
     * Regresa el valor de ultimaModificacion.
     *
     * @hibernate.property column="ID_SPREAD"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getIdSpread() {
        return idSpread;
    }

    /**
     * Fija el valor de idSpread.
     *
     * @param idSpread El valor a asignar.
     */
    public void setIdSpread(int idSpread) {
        this.idSpread = idSpread;
    }

    /**
     * Regresa el valor de ultimaModificacion.
     *
     * @hibernate.property column="ULTIMA_MODIFICACION"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getUltimaModificacion() {
        return ultimaModificacion;
    }

    /**
     * Fija el valor de ultimaModificacion.
     *
     * @param ultimaModificacion El valor a asignar.
     */
    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof SpreadActual)) {
            return false;
        }
        SpreadActual object = (SpreadActual) other;
        return new EqualsBuilder()
                .append(this.getId(), object.getId())
                .isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private SpreadActualPK _id;

    /**
     * El identificador del spread m&aacute;s reciente.
     */
    private int idSpread;

    /**
     * La fecha de la &uacute;ltima modificaci&oacute;n.
     */
    private Date ultimaModificacion = new Date();

    /**
     * El UID para Serializaci&oacute;n.
     */
    private static final long serialVersionUID = -6159573211927688751L;
}


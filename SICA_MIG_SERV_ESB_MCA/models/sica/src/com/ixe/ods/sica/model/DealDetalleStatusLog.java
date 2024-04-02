/*
 * $Id: DealDetalleStatusLog.java,v 1.15 2008/11/12 05:53:13 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_DEAL_DETALLE_STATUS_LOG.
 *
 * @hibernate.class table="SC_DEAL_DETALLE_STATUS_LOG"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.DealDetalleStatusLog"
 * dynamic-update="true"
 * 
 * @hibernate.query name="findStatusLogByDetalle"
 * query="FROM DealDetalleStatusLog AS d WHERE d.dealDetalle.idDealPosicion = ? "
 *
 * @hibernate.query name="findStatusLogByStatusNuevo"
 * query="FROM DealDetalleStatusLog AS d WHERE d.cambStatus.statusNuevo = 'CO' OR
 * d.cambStatus.statusNuevo = 'TT' AND d.dealDetalle.statusDetalleDeal != 'CA' ORDER BY
 * d.dealDetalle.deal.idDeal "
 *
 * @hibernate.query name="findStatusLogByStatusNuevoAndFecha"
 * query="FROM DealDetalleStatusLog AS d INNER JOIN FETCH d.dealDetalle AS det INNER JOIN FETCH
 * det.deal AS deal INNER JOIN FETCH det.divisa AS div LEFT JOIN FETCH det.plantilla AS pl
 * INNER JOIN FETCH deal.detalles WHERE d.cambStatus.fechaCambio BETWEEN ? AND ? AND
 * (d.cambStatus.statusNuevo = 'CO' OR d.cambStatus.statusNuevo = 'TT') AND
 * d.dealDetalle.statusDetalleDeal != 'CA' ORDER BY d.dealDetalle.deal.idDeal "
 *
 * @hibernate.query name="findDetsLiquidacionHoy"
 * query="SELECT d.dealDetalle FROM DealDetalleStatusLog AS d WHERE d.cambStatus.statusNuevo = 'TT'
 * AND d.cambStatus.fechaCambio BETWEEN ? AND ? ORDER BY d.dealDetalle.deal.idDeal"
 * 
 * @hibernate.query name="findDetsNoLiquidacionHoy"
 * query="FROM DealDetalleStatusLog AS d 
 * WHERE d.cambStatus.statusNuevo != 'CA'
 * AND d.cambStatus.fechaCambio  BETWEEN ? AND ? ORDER BY d.dealDetalle.idDealPosicion, d.cambStatus.fechaCambio DESC"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.15 $ $Date: 2008/11/12 05:53:13 $
 */
public class DealDetalleStatusLog implements Serializable {

    /**
     * Constructor por default, no hace nada.
     */
    public DealDetalleStatusLog() {
        super();
    }

    /**
     * Regresa el valor de idDetalleStatusLog.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_DETALLE_STATUS_LOG"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_DEAL_DETALLE_STATUS_LOG_SEQ"
     * @return int.
     */
    public int getIdDetalleStatusLog() {
        return idDetalleStatusLog;
    }

    /**
     * Fija el valor de idDetalleStatusLog.
     *
     * @param idDetalleStatusLog El valor a asignar.
     */
    public void setIdDetalleStatusLog(int idDetalleStatusLog) {
        this.idDetalleStatusLog = idDetalleStatusLog;
    }

    /**
     * Regresa el valor de cambStatus.
     *
     * @hibernate.component class="com.ixe.ods.sica.model.CambStatus"
     * @return CambStatus.
     */
    public CambStatus getCambStatus() {
        return cambStatus;
    }

    /**
     * Fija el valor de cambStatus.
     *
     * @param cambStatus El valor a asignar.
     */
    public void setCambStatus(CambStatus cambStatus) {
        this.cambStatus = cambStatus;
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
        return _dealDetalle;
    }
    
    /**
     * Regresa una descripci&oacute;n significativa para el status del deal.
     *
     * @return String.
     */
    public String getDescripcionStatusViejo() {
        String statusDeal = cambStatus.getStatusNuevo();
        if (DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(statusDeal)) {
            return "Alta";
        }
        else if (DealDetalle.STATUS_DET_COMPLETO.equals(statusDeal)) {
            return "Proces\u00e1ndose";
        }
        else if (DealDetalle.STATUS_DET_PARCIALMENTE_PAG_LIQ.equals(statusDeal)) {
            return "Liq. Parcial";
        }
        else if (DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(statusDeal)) {
            return "Totalmente Liq.";
        }
        else if (DealDetalle.STATUS_DET_CANCELADO.equals(statusDeal)) {
            return "Cancelado";
        }
        return "";
    }
    
    /**
     * Regresa una descripci&oacute;n significativa para el status del deal.
     *
     * @return String.
     */
    public String getDescripcionStatusNuevo() {
        String statusDeal = cambStatus.getStatusNuevo();
        if (DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(statusDeal)) {
            return "Alta";
        }
        else if (DealDetalle.STATUS_DET_COMPLETO.equals(statusDeal)) {
            return "Proces\u00e1ndose";
        }
        else if (DealDetalle.STATUS_DET_PARCIALMENTE_PAG_LIQ.equals(statusDeal)) {
            return "Liq. Parcial";
        }
        else if (DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(statusDeal)) {
            return "Totalmente Liq.";
        }
        else if (DealDetalle.STATUS_DET_CANCELADO.equals(statusDeal)) {
            return "Cancelado";
        }
        return "";
    }
    /**
     * Fija el valor de dealDetalle.
     *
     * @param dealDetalle El valor a asignar.
     */
    public void setDealDetalle(DealDetalle dealDetalle) {
        _dealDetalle = dealDetalle;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof DealDetalleStatusLog)) {
            return false;
        }
        DealDetalleStatusLog castOther = (DealDetalleStatusLog) other;
        return new EqualsBuilder().append(this.getIdDetalleStatusLog(),
                castOther.getIdDetalleStatusLog()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdDetalleStatusLog()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private int idDetalleStatusLog;

    /**
     * El componente cambStatus.
     */
    private CambStatus cambStatus;

    /**
     * Relaci&oacute;n muchos-a-uno con DealDetalle.
     */
    private DealDetalle _dealDetalle;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 7147720338034996597L;
}

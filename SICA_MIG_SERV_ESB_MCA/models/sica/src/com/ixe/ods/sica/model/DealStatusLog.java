/*
 * $Id: DealStatusLog.java,v 1.14 2008/02/22 18:25:24 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_DEAL_STATUS_LOG.
 *
 * @hibernate.class table="SC_DEAL_STATUS_LOG"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.DealStatusLog"
 * dynamic-update="true"
 * 
 * @hibernate.query name="findStatusLogByDeal"
 * query="FROM DealStatusLog AS d WHERE d.deal.idDeal = ? "
 * 
 * @hibernate.query name="findStatusCanceladoByFecha"
 * query="FROM DealStatusLog AS ds INNER JOIN FETCH ds.deal d  WHERE ds.cambStatus.statusNuevo = 'CA' AND
 * ds.cambStatus.fechaCambio BETWEEN ? AND ? "
 * 
 * @author Jean C. Favila
 * @version  $Revision: 1.14 $ $Date: 2008/02/22 18:25:24 $
 */
public class DealStatusLog implements Serializable {

    /**
     * Constructor por default, no hace nada.
     */
    public DealStatusLog() {
        super();
    }

    /**
     * Regresa el valor de idDealStatusLog.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_DEAL_STATUS_LOG"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_DEAL_STATUS_LOG_SEQ"
     * @return int.
     */
    public int getIdDealStatusLog() {
        return idDealStatusLog;
    }
    
    /**
     * Regresa una descripci&oacute;n significativa para el status del deal.
     *
     * @return String.
     */
    
    public String getDescripcionStatusViejo() {
        String statusDeal = cambStatus.getStatusAnterior();
        if (Deal.STATUS_DEAL_CAPTURADO.equals(statusDeal)) {
            return "Proces\u00e1ndose";
        }
        else if (Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(statusDeal)) {
            return "Alta";
        }
        else if (Deal.STATUS_DEAL_PARCIAL_PAGADO_LIQ.equals(statusDeal)) {
            return "Liq. Parcial";
        }
        else if (Deal.STATUS_DEAL_TOTALMENTE_PAGADO.equals(statusDeal)) {
            return "Totalmente Pag.";
        }
        else if (Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO.equals(statusDeal)) {
            return "Totalmente Liq.";
        }
        else if (Deal.STATUS_DEAL_CANCELADO.equals(statusDeal)) {
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
        if (Deal.STATUS_DEAL_CAPTURADO.equals(statusDeal)) {
            return "Proces\u00e1ndose";
        }
        else if (Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(statusDeal)) {
            return "Alta";
        }
        else if (Deal.STATUS_DEAL_PARCIAL_PAGADO_LIQ.equals(statusDeal)) {
            return "Liq. Parcial";
        }
        else if (Deal.STATUS_DEAL_TOTALMENTE_PAGADO.equals(statusDeal)) {
            return "Totalmente Pag.";
        }
        else if (Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO.equals(statusDeal)) {
            return "Totalmente Liq.";
        }
        else if (Deal.STATUS_DEAL_CANCELADO.equals(statusDeal)) {
            return "Cancelado";
        }
        return "";
    }
    
    /**
     * Fija el valor de idDealStatusLog.
     *
     * @param idDealStatusLog El valor a asignar.
     */
    public void setIdDealStatusLog(int idDealStatusLog) {
        this.idDealStatusLog = idDealStatusLog;
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
     * Regresa el valor de cliente.
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
        if (!(other instanceof DealStatusLog)) {
            return false;
        }
        DealStatusLog castOther = (DealStatusLog) other;
        return new EqualsBuilder().append(this.getIdDealStatusLog(),
                castOther.getIdDealStatusLog()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdDealStatusLog()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private int idDealStatusLog;

    /**
     * El componente cambStatus.
     */
    private CambStatus cambStatus;

    /**
     * Relaci&oacute;n muchos-a-uno con Deal.
     */
    private Deal _deal;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 2332220949506470680L;
}

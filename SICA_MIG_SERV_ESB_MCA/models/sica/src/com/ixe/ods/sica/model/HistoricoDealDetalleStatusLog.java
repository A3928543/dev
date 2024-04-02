/*
 * $Id: HistoricoDealDetalleStatusLog.java,v 1.12 2008/02/22 18:25:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Clase persistente para la tabla SC_H_DEAL_DETALLE_STATUS_LOG, donde se almacenan el
 * hist&oacute;rico de los cambios de status de los Detalles de Deals. </p>
 *
 * @hibernate.class table="SC_H_DEAL_DETALLE_STATUS_LOG"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoDealDetalleStatusLog"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:23 $
 */

public class HistoricoDealDetalleStatusLog implements Serializable{

    /**
    * Constructor por default. No hace nada.
    */
    public HistoricoDealDetalleStatusLog() {
        super();
    }
    
    /**
     * Constructor que recibe un DealDetalleStatusLog e inicializa todas sus variables.
     * 
     * @param ddsl El statusLog del detalle de deal que inicializa el hist&oacute;rico.
     */
     public HistoricoDealDetalleStatusLog(DealDetalleStatusLog ddsl) {
    	 setIdDetalleStatusLog(ddsl.getIdDetalleStatusLog());
    	 setFechaCambio(ddsl.getCambStatus().getFechaCambio());
    	 setIdDealPosicion(new Integer (ddsl.getDealDetalle().getIdDealPosicion()));
    	 setStatusNuevo(ddsl.getCambStatus().getStatusNuevo());
    	 setStatusAnterior(ddsl.getCambStatus().getStatusAnterior());
    	 
     }

    /**
     * Regresa el valor de idDetalleStatusLog.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_DETALLE_STATUS_LOG"
     * unsaved-value="null"
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
     * Regresa el valor de statusAnterior.
     *
     * @hibernate.property column="STATUS_ANTERIOR"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getStatusAnterior() {
        return statusAnterior;
    }

    /**
     * Fija el valor de statusAnterior.
     *
     * @param statusAnterior El valor a asignar.
     */
    public void setStatusAnterior(String statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    /**
     * Regresa el valor de statusNuevo.
     *
     * @hibernate.property column="STATUS_NUEVO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getStatusNuevo() {
        return statusNuevo;
    }

    /**
     * Fija el valor de statusNuevo.
     *
     * @param statusNuevo El valor a asignar.
     */
    public void setStatusNuevo(String statusNuevo) {
        this.statusNuevo = statusNuevo;
    }

    /**
     * El id que lo identifica
     */
    private int idDetalleStatusLog;
    
    /**
     * La fecha del cambio de valores.
     */
    private Date fechaCambio;
    
    /**
     * El identificador del detalle del deal.
     */
    private Integer idDealPosicion;
    
    /**
     * El status previo.
     */
    private String statusAnterior;

    /**
     * El nuevo status.
     */
    private String statusNuevo;
}

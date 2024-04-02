/*
 * $Id: HistoricoDealStatusLog.java,v 1.12 2008/02/22 18:25:22 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>Clase persistente para la tabla SC_H_DEAL_STATUS_LOG, donde se almacenan el
 * hist&oacute;rico de los cambios de los Status de Deal. </p>
 *
 * @hibernate.class table="SC_H_DEAL_STATUS_LOG"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoDealStatusLog"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:22 $
 */

public class HistoricoDealStatusLog implements Serializable{

    /**
    * Constructor por default. No hace nada.
    */
    public HistoricoDealStatusLog() {
        super();
    }
    
    /**
     * Constructor que recibe un DealStatusLog e inicializa todas sus variables.
     * 
     * @param dsl El status log del deal que inicializa el hist&oacute;rico. 
     */
     public HistoricoDealStatusLog(DealStatusLog dsl) {
         super();
    	 setIdDealStatusLog(dsl.getIdDealStatusLog());
    	 setIdDeal(dsl.getDeal().getIdDeal());
         setFechaCambio(dsl.getCambStatus().getFechaCambio());
         setStatusAnterior(dsl.getCambStatus().getStatusAnterior());
         setStatusNuevo(dsl.getCambStatus().getStatusNuevo());
     }

    /**
     * Regresa el valor de idDealStatusLog.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_DEAL_STATUS_LOG"
     * unsaved-value="null"
     * @return int.
     */
    public int getIdDealStatusLog() {
        return idDealStatusLog;
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
     * Regresa el valor de valorNuevo.
     *
     * @hibernate.property column="ID_DEAL"
     * not-null="true"
     * unique="false"
     * @return Integer.
     */
    public int getIdDeal() {
        return idDeal;
    }

    /**
     * Fija el valor de idDeal.
     *
     * @param idDeal El valor a asignar.
     */
    public void setIdDeal(int idDeal) {
        this.idDeal = idDeal;
    }

    /**
     * El identificador del registro.
     */
    protected int idDealStatusLog;

    /**
     * Relaci&oacute;n muchos-a-uno con Deal.
     */
    private int idDeal;
    
    /**
     * La fecha en que ocurri&oacute; el cambio de status.
     */
    private Date fechaCambio = new Date();

    /**
     * El status previo.
     */
    private String statusAnterior;

    /**
     * El nuevo status.
     */
    private String statusNuevo;

}

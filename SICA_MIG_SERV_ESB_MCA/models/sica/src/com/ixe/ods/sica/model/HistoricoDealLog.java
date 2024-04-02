/*
 * $Id: HistoricoDealLog.java,v 1.12 2008/02/22 18:25:19 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>Clase persistente para la tabla SC_H_DEAL_LOG, donde se almacenan el
 * hist&oacute;rico de los cambios a nivel encabezado de Deal. </p>
 *
 * @hibernate.class table="SC_H_DEAL_LOG"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoDealLog"
 * dynamic-update="true"
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:19 $
 */

public class HistoricoDealLog implements Serializable{

    /**
    * Constructor por default. No hace nada.
    */
    public HistoricoDealLog() {
        super();
    }
    
    /**
     * Constructor que recibe un DealLog e inicializa todas sus variables.
     * 
     * @param dLog El DealLog que inicializa el hist&oacute;rico.
     */
     public HistoricoDealLog(DealLog dLog) {
         super();
         setIdDealLog(dLog.getIdDealLog());
         setFechaCambio(dLog.getFechaCambio());
         setFolioDetalle(dLog.getFolioDetalle());
         setIdDealPosicion(dLog.getIdDealPosicion());
         setNombreCampo(dLog.getNombreCampo());
         setValorPrevio(dLog.getValorPrevio());
         setValorNuevo(dLog.getValorNuevo());
         setDeal(new Integer (dLog.getDeal().getIdDeal()));
     }

    /**
     * Regresa el valor de idDealLog.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_DEAL_LOG"
     * unsaved-value="null"
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
     * Regresa el valor de valorNuevo.
     *
     * @hibernate.property column="ID_DEAL"
     * not-null="true"
     * unique="false"
     * @return Integer.
     */
    public Integer getDeal() {
        return _deal;
    }

    /**
     * Fija el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public void setDeal(Integer deal) {
        _deal = deal;
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
    private Integer _deal;

}

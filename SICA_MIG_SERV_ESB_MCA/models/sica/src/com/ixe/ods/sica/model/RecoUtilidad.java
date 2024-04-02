/*
 * $Id: RecoUtilidad.java,v 1.22 2008/02/22 18:25:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import com.ixe.ods.sica.Redondeador;
import java.util.Date;

/**
 * Clase persistente para la tabla SC_RECO_UTILIDAD. Almacena utilidades de manera Manual y
 * Autom&aacute;tica
 *
 * @hibernate.joined-subclass table="SC_RECO_UTILIDAD"
 * proxy="com.ixe.ods.sica.model.RecoUtilidad"
 *
 * @hibernate.joined-subclass-key column="ID_DEAL_POSICION"
 *
 * @hibernate.query name="findReconocimientos" query="FROM RecoUtilidad AS ru where ru.fechaOperacion > ? order by ru.idDealPosicion"
 * @hibernate.query name="findReconocimientoByMesaAndDivisa" query="FROM RecoUtilidad AS ru where ru.fechaOperacion > ? AND ru.mesaCambio.idMesaCambio = ? AND ru.divisa.idDivisa = ?"
 *
 * @hibernate.query name="findUtilidadCierre"
 * query="FROM RecoUtilidad AS ru WHERE ru.fechaOperacion BETWEEN ? AND ?"
 * 
 * @hibernate.query name="findUtilidadCierreVespertino"
 * query="FROM RecoUtilidad AS ru WHERE ru.mesaCambio.divisaReferencia = 'MXN' AND (ru.fechaOperacion BETWEEN ? AND ?)"
 * 
 * @hibernate.query name="findUtilidadGlobales"
 * query="FROM RecoUtilidad AS ru WHERE (ru.fechaOperacion BETWEEN ? AND ?) AND ru.divisa.idDivisa = ? AND ru.mesaCambio.idMesaCambio = ?"
 *
 * @author Edgar Leija, Javier Cordova
 * @version  $Revision: 1.22 $ $Date: 2008/02/22 18:25:23 $
 */
public class RecoUtilidad  extends DealPosicion {

    /**
     * Constructor por default.  No hace nada.
     */
    public RecoUtilidad() {
        super();
    }

    /**
     * Regresa el valor de fechaOperacion.
     *
     * @hibernate.property column="FECHA_OPERACION"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaOperacion() {
        return fechaOperacion;
    }

    /**
     * Fija el valor de fechaOperacion.
     *
     * @param fechaOperacion El valor a asignar.
     */
    public void setFechaOperacion(Date fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    /**
     * Regresa el valor de utilidadGlobal.
     *
     * @hibernate.property column="UTILIDAD_GLOBAL"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getUtilidadGlobal() {
        return _utilidadGlobal;
    }

    /**
     * Fija el valor de utilidadGlobal.
     *
     * @param utilidadGlobal El valor a asignar.
     */
    public void setUtilidadGlobal(double utilidadGlobal) {
        _utilidadGlobal = utilidadGlobal;
    }

    /**
     * Regresa el valor de utilidadMesa.
     *
     * @hibernate.property column="UTILIDAD_MESA"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getUtilidadMesa() {
        return _utilidadMesa;
    }

    /**
     * Fija el valor de utilidadMesa.
     *
     * @param utilidadMesa El valor a asignar.
     */
    public void setUtilidadMesa(double utilidadMesa) {
        _utilidadMesa = utilidadMesa;
    }

    /**
     * Regresa el valor de tipoReco.
     *
     * @hibernate.property column="TIPO_RECO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoReco() {
        return _tipoReco;
    }

    /**
     * Fija el valor de tipoReco. A: Autom&aacute;tico, M: Manual.
     *
     * @param tipoReco El valor a asignar.
     */
    public void setTipoReco(String tipoReco) {
        _tipoReco = tipoReco;
    }
    
    /**
     * Regresa el valor de tipoCambio.
     *
     * @hibernate.property column="TIPO_CAMBIO_OTRA_DIV_REF"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getTipoCambioOtraDivRef() {
        return Redondeador.redondear6Dec(tipoCambioDivRef);
    }

    /**
     * Fija el valor de tipoCambio.
     *
     * @param tipoCambioDivRef El valor a asignar.
     */
    public void setTipoCambioOtraDivRef(double tipoCambioDivRef) {
        this.tipoCambioDivRef = tipoCambioDivRef;
    }

    /**
     * Regresa true si la utilidad es Manual
     *
     * @return boolean.
     */
    public boolean isManual() {
        return MANUAL.equals(getTipoReco() != null ? getTipoReco().trim() : "");
    }

    /**
     * La fecha en que ocurri&oacute; la operaci&oacute;n.
     */
    private Date fechaOperacion = new Date();

    /**
     * El valor de la utilidad global
     */
    private double _utilidadGlobal;
    
    /**
     * Tipo de cambio de la divisa referencia a pesos.
     */
    private double tipoCambioDivRef = 1;

    /**
     * El valor de la utilidad mesa
     */
    private double _utilidadMesa;

    /**
     * Si se trata de una Utilidad Manual 'M' o Autom&aacute;tica 'A'.
     */
    private String _tipoReco;

    /**
     * Constante Tipo Utilidad Manual
     */
    public static final String MANUAL = "M";
    
    /**
     * Constante Tipo Utilidad Autom&aacute;tico
     */
    public static final String AUTOMATICO = "A";
}

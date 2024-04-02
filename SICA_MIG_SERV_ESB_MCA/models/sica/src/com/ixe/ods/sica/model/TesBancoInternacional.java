/*
 * $Id: TesBancoInternacional.java,v 1.12 2008/02/22 18:25:22 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * <p>Clase persistente para la tabla TES_BANCO_INTERNACIONAL</p>
 * @hibernate.class table="TES_BANCO_INTERNACIONAL"
 * mutable="false"
 * proxy="com.ixe.ods.sica.model.TesBancoInternacional"
 * dynamic-update="false"
 *
 * @hibernate.query name="findBancoInternacionalByNombreAndPais"
 * query="SELECT t.nombre, p.nombreLargo, t.ciudad, t.claveRuteoPais, t.numeroRuteoPais, t.claveSwift, t.claveChips, t.status, t.clavePais, t.idBancoInternacional FROM TesBancoInternacional AS t, TesPais p WHERE t.status = 'ACTI' AND t.clavePais = p.claveIso AND t.nombre like ? AND t.clavePais = ? ORDER BY t.nombre, p.nombreLargo, t.ciudad, t.claveRuteoPais, t.numeroRuteoPais, t.claveSwift, t.claveChips"
 *
 * @author Edgar I. Leija
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:22 $
 */
public class TesBancoInternacional implements Serializable {

    /**
     * Constructor por default, no hace nada.
     */
    public TesBancoInternacional() {
        super();
    }

    /**
	 * Regresa el valor de idBancoInternacional.
	 *
	 * @hibernate.id column="ID_BANCO_INTERNACIONAL"
     * unsaved-value="0"
     * generator-class="assigned"
	 * @return int.
	 */
    public int getIdBancoInternacional() {
        return _idBancoInternacional;
    }

    /**
     * Fija el valor de idBancoInternacional.
     *
     * @param idBancoInternacional El valor a asignar.
     */
    public void setIdBancoInternacional(int idBancoInternacional) {
        this._idBancoInternacional = idBancoInternacional;
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
        return _nombre;
    }

    /**
     * Fija el valor de nombre.
     *
     * @param nombre El valor a asignar.
     */
    public void setNombre(String nombre) {
        this._nombre = nombre;
    }

    /**
     * Regresa el valor de nombre.
     *
     * @hibernate.property column="CLAVE_PAIS"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getClavePais() {
        return _clavePais;
    }

    /**
     * Fija el valor de clavePais.
     *
     * @param clavePais El valor a asignar.
     */
    public void setClavePais(String clavePais) {
       _clavePais = clavePais;
    }

    /**
     * Regresa el valor de Ciudad.
     *
     * @hibernate.property column="CIUDAD"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getCiudad() {
        return _ciudad;
    }

    /**
     * Fija el valor de ciudad.
     *
     * @param ciudad El valor a asignar.
     */
    public void setCiudad(String ciudad) {
        this._ciudad = ciudad;
    }
    
    /**
     * Regresa el valor de claveRuteoPais.
     *
     * @hibernate.property column="CLAVE_RUTEO_PAIS"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClaveRuteoPais() {
        return _claveRuteoPais;
    }

    /**
     * Fija el valor de claveRuteoPais.
     *
     * @param claveRuteoPais El valor a asignar.
     */
    public void setClaveRuteoPais(String claveRuteoPais) {
        this._claveRuteoPais = claveRuteoPais;
    }

    /**
     * Regresa el valor de numeroRuteoPais.
     *
     * @hibernate.property column="NUMERO_RUTEO_PAIS"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getNumeroRuteoPais() {
        return _numeroRuteoPais;
    }

    /**
     * Fija el valor de numeroRuteoPais.
     *
     * @param numeroRuteoPais El valor a asignar.
     */
    public void setNumeroRuteoPais(String numeroRuteoPais) {
        this._numeroRuteoPais = numeroRuteoPais;
    }
    
    /**
     * Regresa el valor de claveSwift.
     *
     * @hibernate.property column="CLAVE_SWIFT"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClaveSwift() {
        return _claveSwift;
    }

    /**
     * Fija el valor de claveSwift.
     *
     * @param claveSwift El valor a asignar.
     */
    public void setClaveSwift(String claveSwift) {
        this._claveSwift = claveSwift;
    }

    
    /**
     * Regresa el valor de claveChips.
     *
     * @hibernate.property column="CLAVE_CHIPS"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClaveChips() {
        return _claveChips;
    }

    /**
     * Fija el valor de claveChips.
     *
     * @param claveChips El valor a asignar.
     */
    public void setClaveChips(String claveChips) {
        this._claveChips = claveChips;
    }

    /**
     * Regresa el valor de status.
     *
     * @hibernate.property column="STATUS"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getStatus() {
        return _status;
    }

    /**
     * Fija el valor de status.
     *
     * @param status El valor a asignar.
     */
    public void setStatus(String status) {
        this._status = status;
    }

    
    /**
     * El identificador del registro.
     */
    private int _idBancoInternacional;

    /**
     * El identificador del registro.
     */
    private String _nombre;

    /**
     * El identificador del registro.
     */
    private String _ciudad;
    
    /**
     * El identificador del registro.
     */
    private String _clavePais;

    /**
     * El identificador del registro.
     */
    private String _claveRuteoPais;

    /**
     * El identificador del registro.
     */
    private String _numeroRuteoPais;

    /**
     * El identificador del registro.
     */
    private String _claveSwift;

    /**
     * El identificador del registro.
     */
    private String _claveChips;

    /**
     * El identificador del registro.
     */
    private String _status;

}

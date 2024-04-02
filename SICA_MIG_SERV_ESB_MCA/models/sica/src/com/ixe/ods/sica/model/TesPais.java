/*
 * $Id: TesPais.java,v 1.12.70.1 2014/06/18 23:43:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;


import java.io.Serializable;

/**
 * <p>Clase persistente para la tabla TES_PAIS</p>
 * @hibernate.class table="TES_PAIS"
 * mutable="false"
 * proxy="com.ixe.ods.sica.model.TesPais"
 * dynamic-update="false"
 *
 * @author Edgar I. Leija
 * @version  $Revision: 1.12.70.1 $ $Date: 2014/06/18 23:43:01 $
 */
public class TesPais implements Serializable {

    /**
     * Constructor por default, no hace nada.
     */
    public TesPais() {
        super();
    }

    /**
	 * Regresa el valor de claveIso.
	 *
	 * @hibernate.id column="CLAVE_ISO"
     * unsaved-value="0"
     * generator-class="assigned"
	 * @return String.
	 */
    public int getClaveIso() {
        return _claveIso;
    }

    /**
     * Fija el valor de claveIso
     *
     * @param claveIso El valor a asignar.
     */
    public void setClaveIso(int claveIso) {
       _claveIso = claveIso;
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
     * Regresa el valor de nombreLargo.
     *
     * @hibernate.property column="NOMBRE_LARGO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getNombreLargo() {
        return _nombreLargo;
    }

    /**
     * Fija el valor de nombreLargo.
     *
     * @param nombreLargo El valor a asignar.
     */
    public void setNombreLargo(String nombreLargo) {
        this._nombreLargo = nombreLargo;
    }

    /**
     * Regresa el valor de ClaveIso3.
     *
     * @hibernate.property column="CLAVE_ISO3"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClaveIso3() {
        return _claveIso3;
    }

    /**
     * Fija el valor de claveIso3.
     *
     * @param claveIso3 El valor a asignar.
     */
    public void setClaveIso3(String claveIso3) {
        this._claveIso3 = claveIso3;
    }
    
    /**
     * Regresa el valor de codigo.
     *
     * @hibernate.property column="CODIGO"
     * not-null="false"
     * unique="false"
     * @return int.
     */
    public int getCodigo() {
        return _codigo;
    }

    /**
     * Fija el valor de codigo.
     *
     * @param codigo El valor a asignar.
     */
    public void setCodigo(int codigo) {
        this._codigo = codigo;
    }
    
	/**
	 * @return sancionadoOFAC.
	 */
	public String getSancionadoOFAC() {
		return _sancionadoOFAC;
	}
	
	/**
	 * @param sancionadoOFAC The sancionadoOFAC to set.
	 */
	public void setSancionadoOFAC(String sancionadoOFAC) {
		this._sancionadoOFAC = sancionadoOFAC;
	}
	
	/**
	 * @return altoRiesgoGAFI.
	 */
	public String getAltoRiesgoGAFI() {
		return _altoRiesgoGAFI;
	}
	
	/**
	 * @param altoRiesgoGAFI The altoRiesgoGAFI to set.
	 */
	public void setAltoRiesgoGAFI(String altoRiesgoGAFI) {
		this._altoRiesgoGAFI = altoRiesgoGAFI;
	}

       
    /**
     * El identificador del registro.
     */
    private int _claveIso;

    /**
     * El identificador del registro.
     */
    private String _nombre;

    /**
     * El identificador del registro.
     */
    private String _nombreLargo;
    
    /**
     * El identificador del registro.
     */
    private String _claveIso3;

    /**
     * El identificador del registro.
     */
    private int _codigo;
	private String 	_sancionadoOFAC;//jdch 04/06/2014
	private String 	_altoRiesgoGAFI;//jdch 04/06/2014
}

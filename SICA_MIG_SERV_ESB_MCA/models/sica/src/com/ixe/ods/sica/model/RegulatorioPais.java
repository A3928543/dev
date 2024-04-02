package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * Clase persistente para la tabla SC_REG_PAIS. 
 *
 * @hibernate.class table="SC_REG_PAIS"
 * proxy="com.ixe.ods.sica.model.RegulatorioPais"
 * dynamic-update="true"
 */
public class RegulatorioPais implements Serializable 
{
	private String claveIso;
	private String nombrePais;
	
	public RegulatorioPais() 
	{
		super();
	}
	
	public RegulatorioPais(String claveIso, String nombrePais) 
	{
		super();
		this.claveIso = claveIso;
		this.nombrePais = nombrePais;
	}
	//--------------------------------------------------------------------------
	/**
     * Regresa el valor de la clave iso del pais.
     *
     * @hibernate.id generator-class="assigned"
     * column="CLAVEISO2"
     * unsaved-value="null"
     * @return String.
     */
    public String getClaveIso() {
        return claveIso;
    }

    /**
     * Fija el valor de la clave iso del pais.
     *
     * @param claveIso El valor a asignar.
     */
    public void setClaveIso(String claveIso) {
        this.claveIso = claveIso;
    }
  //--------------------------------------------------------------------------
    /**
     * Regresa el valor de la clave iso del pais.
     *
     * @hibernate.property column="NOMBRE"
     * @return String.
     */
    public String getNombrePais() {
        return nombrePais;
    }

    /**
     * Fija el valor de la clave iso del pais.
     *
     * @param nombrePais El valor a asignar.
     */
    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }
}

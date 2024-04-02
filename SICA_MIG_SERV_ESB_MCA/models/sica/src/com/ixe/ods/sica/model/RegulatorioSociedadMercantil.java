package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * Clase persistente para la tabla SC_REG_SOCMERCANTIL. 
 *
 * @hibernate.class table="SC_REG_SOCMERCANTIL"
 * proxy="com.ixe.ods.sica.model.RegulatorioSociedadMercantil"
 * dynamic-update="true"
 */
public class RegulatorioSociedadMercantil implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String descripcionCorta;
	private String descripcionLarga;
	
	public RegulatorioSociedadMercantil() 
	{
		super();
	}
	
	public RegulatorioSociedadMercantil(Integer id, String descripcionCorta, String descripcionLarga) 
	{
		super();
		this.id = id;
		this.descripcionCorta = descripcionCorta;
		this.descripcionLarga = descripcionLarga;
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el valor del id del registro.
	 * 
	 * @hibernate.id generator-class="assigned"
	 * column="ID_SOCIEDAD"
	 * unsaved-value="null"
	 * @return Integer.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Fija el valor del id del registro.
	 *
	 * @param id El valor a asignar.
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	//--------------------------------------------------------------------------
	/**
     * Regresa el valor de la descripcion corta de la sociedad mercantil.
     *
     * @hibernate.property column="NOMBRE_CORTO"
     * @return String.
     */
    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    /**
     * Fija el valor de la descripcion corta de la sociedad mercantil.
     *
     * @param descrpcionCorta El valor a asignar.
     */
    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }
  //--------------------------------------------------------------------------
    /**
     * Regresa el valor de la descripcion larga de la sociedad mercantil.
     *
     * @hibernate.property column="DESCRIPCION"
     * @return String.
     */
    public String getDescrpcionLarga() {
        return descripcionLarga;
    }

    /**
     * Fija el valor de la descripcion larga de la sociedad mercantil.
     *
     * @param descripcionLarga El valor a asignar.
     */
    public void setDescrpcionLarga(String descripcionLarga) {
        this.descripcionLarga = descripcionLarga;
    }
}

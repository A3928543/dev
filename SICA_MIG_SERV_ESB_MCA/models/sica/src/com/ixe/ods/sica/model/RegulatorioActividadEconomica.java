package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * Clase persistente para la tabla SC_REG_ACTIVIDAD_ECONOMICA. 
 *
 * @hibernate.class table="SC_REG_ACTIVIDAD_ECONOMICA"
 * proxy="com.ixe.ods.sica.model.RegulatorioActividadEconomica"
 * dynamic-update="true"
 */
public class RegulatorioActividadEconomica implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String descripcion;
	
	public RegulatorioActividadEconomica() 
	{
		super();
	}
	
	public RegulatorioActividadEconomica(Integer id, String descripcion) 
	{
		super();
		this.id = id;
		this.descripcion = descripcion;
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el valor del id del registro.
	 * @hibernate.id generator-class="assigned"
	 * column="ID_ACTIVIDAD"
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
     * Regresa el valor de la descripcion de la actividad economica de la contraparte.
     *
     * @hibernate.property column="DESCRIPCION"
     * @return String.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Fija el valor de la descripcion de la actividad economica de la contraparte.
     *
     * @param descrpcion El valor a asignar.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

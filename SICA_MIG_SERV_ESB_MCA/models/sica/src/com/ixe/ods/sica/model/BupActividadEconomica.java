package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * Clase persistente para la tabla BUP_ACTIVIDAD_ECONOMICA. 
 *
 * @hibernate.class table="BUP_ACTIVIDAD_ECONOMICA"
 * proxy="com.ixe.ods.sica.model.BupActividadEconomica"
 * dynamic-update="true"
 */

public class BupActividadEconomica implements Serializable 
{
	private String idActividad;
	private String descripcion;
	private String idOriginal;
	private Integer idGiroEconomico;
	private String indicadorRiesgo;
	
	public BupActividadEconomica() 
	{
		super();
	}
	
	/**
	 * Regresa el valor del id de la actividad economica.
	 * 
	 * @hibernate.id generator-class="assigned"
	 * column="ID_ACTIVIDAD"
	 * unsaved-value="null"
	 * @return String
	 */
	public String getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(String idActividad) {
		this.idActividad = idActividad;
	}
	
	/**
     * Regresa el valor de la descripcion de la actividad economica.
     *
     * @hibernate.property column="DESCRIPCION"
     * @return String.
     */
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	/**
     * Regresa el valor del id original.
     *
     * @hibernate.property column="ID_ORIGINAL"
     * @return String.
     */
	public String getIdOriginal() {
		return idOriginal;
	}

	public void setIdOriginal(String idOriginal) {
		this.idOriginal = idOriginal;
	}
	
	/**
     * Regresa el valor del id del giro economico asociado a la actividad economica.
     *
     * @hibernate.property column="ID_GIRO_ECONOMICO"
     * @return Integer.
     */
	public Integer getIdGiroEconomico() {
		return idGiroEconomico;
	}

	public void setIdGiroEconomico(Integer idGiroEconomico) {
		this.idGiroEconomico = idGiroEconomico;
	}
	
	/**
     * Regresa el valor del indicador de la actividad economica. 
     *
     * @hibernate.property column="INDICADOR_RIESGO"
     * @return String.
     */
	public String getIndicadorRiesgo() {
		return indicadorRiesgo;
	}

	public void setIndicadorRiesgo(String indicadorRiesgo) {
		this.indicadorRiesgo = indicadorRiesgo;
	}
}

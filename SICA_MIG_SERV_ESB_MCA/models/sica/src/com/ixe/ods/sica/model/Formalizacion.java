package com.ixe.ods.sica.model;


/**
 * Clase persistente para la tabla SC_FORMALIZACION
 * @hibernate.class table="SC_LC_FORMALIZACION"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Formalizacion"
 * dynamic-update="true"
 * @author HMASG771
 *
 */
public class Formalizacion {

	/**
	 * Identificador del catálogo de Formalizacion
	 */
	private Integer idFormalizacion;
	
	/**
	 * Descripcion del catálogo de formalizacion
	 */
	private String descripcion;
	
	/**
	 * Constructor por default
	 */
	public Formalizacion() {
		super();
	}
	
	/**
     * Regresa el valor de idFormalizacion.
     * 
     * @hibernate.id generator-class="assigned"
     * column="ID_FORMALIZACION"
     * unsaved-value="null"
     * @return Integer.
     * 
     */
	public Integer getIdFormalizacion() {
		return idFormalizacion;
	}

	/**
     * Establece el valor de idFormalizacion
     *
     * @param idFormalizacion El valor a asignar.
     */
	public void setIdFormalizacion(Integer idFormalizacion) {
		this.idFormalizacion = idFormalizacion;
	}	
	
	/**
	 * Regresa el valor de la descripcion de la Entidad Formalizacion
	 * @return String
	 * @hibernate.property column="DESCRIPCION"
	 * not-null="true"
     * unique="false"
	 * 
	 */
	public String getDescripcion() {
		return descripcion;
	}
	
	/**
	 * Establece el valor de la descripcion de la entidad Formalizacion
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}

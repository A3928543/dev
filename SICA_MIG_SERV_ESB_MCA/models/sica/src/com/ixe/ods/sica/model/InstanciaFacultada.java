package com.ixe.ods.sica.model;


/**
 * Clase persistente para la tabla SC_INSTANCIA_FACULTADA
 * @hibernate.class table = "SC_LC_INSTANCIA_FACULTADA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.InstanciaFacultada"
 * dynamic-update="true"
 * @author HMASG771
 *
 */
public class InstanciaFacultada {
	
	/**
	 * Identificador de la instancia Facultada
	 */
	private Integer idInstanciaFacultada;
	
	/**
	 *Descripcion de la Instancia Facultada 
	 */
	private String descripcion;
	
	
	/**
	 * Constructor por default
	 */
	public InstanciaFacultada() {
		super();
	}

	
	/**
	 * Regresa el valor del identificador de la Instancia Facultada
	 * 
	 * 
	 * @hibernate.id generator-class= "assigned"
	 * column ="ID_INSTANCIA_FACULTADA"
	 * unsaved-value="null"
	 * 
	 * @return Integer
	 */
	public Integer getIdInstanciaFacultada() {
		return idInstanciaFacultada;
	}

	/**
	 * Establece el valor del IdInstanciaFacultada
	 * @param idInstanciaFacultada
	 */
	public void setIdInstanciaFacultada(Integer idInstanciaFacultada) {
		this.idInstanciaFacultada = idInstanciaFacultada;
	}
	
	/**
	 * Regresa el valor de la Descripcion de la Instancia Facultada
	 * @return String
	 * @hibernate.property column="DESCRIPCION"
	 * not-null="true"
     * unique="false"
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Establece la descripcion de la Instancia Facultada
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	

}

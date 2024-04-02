package com.ixe.ods.sica.model;

import org.apache.commons.lang.StringUtils;

/**
 * Clase persistente para la tabla SC_TIPO_AUTORIZACION
 * @hibernate.class table="SC_LC_TIPO_AUTORIZACION" 
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.TipoAutorizacion"
 * dynamic-update="true" 
 * @author HMASG771
 *
 */
public class TipoAutorizacion {

	/**
	 * Identificador de Tipo de Autorizacion
	 */
	private Integer idTipoAutorizacion;
	
	/**
	 * Descripcion del tipo de Autorizacion
	 */
	private String descripcion;
	
	/**
	 * @hibernate.id generator-class="assigned" 
	 * column="ID_TIPO_AUTORIZACION"
	 * unsaved-value="null"
	 * 
	 * @return Integer
	 * 
	 */
	public Integer getIdTipoAutorizacion() {
		return idTipoAutorizacion;
	}

	/**
	 * Establece el valor de idTipoAutorizacion
	 * @param idTipoAutorizacion
	 */
	public void setIdTipoAutorizacion(Integer idTipoAutorizacion) {
		this.idTipoAutorizacion = idTipoAutorizacion;
	}
	
	/**
	 * Regresa el valor de la Descripcion del Tipo de Autorizacion
	 * @hibernate.property column="DESCRIPCION"
	 * @return String
	 */
	public String getDescripcion() {
		return StringUtils.defaultString(descripcion,"");
	}
	
	/**
	 * Establece el valor de la Descripcoin del Tipo de Autorizacion
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}

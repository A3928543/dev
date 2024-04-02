package com.ixe.ods.sica.model;

import java.util.Date;

/**
 * Sistema para el que se captura una autorizacion de Tipo de Cambio Especial
 * 
 * @hibernate.class table="SC_SISTEMA"
 * 
 * @hibernate.query name="findAllSistemas"
 * query="FROM SistemaTce"
 * 
 * @author Cesar Jeronimo Gomez
 */
public class SistemaTce {

	/** Identificador del sistema */
	private int idSistema;
	
	/** Descripcion del sistema */
	private String description;
	
	/** Id del usuario que crea el registro del sistema */
	private int usuarioCreacion;
	
	/** Fecha en que se crea el registro */
	private Date fechaCreacion;
	
	/** Id de usuario que hace la ultima modificacion al registro */
	private int usuarioUltModif;
	
	/** Fecha de la ultima modificacion del registro */
	private Date fechaUltModif;
	
	/**
	 * Minimal constructor
	 */
	public SistemaTce() {
		super();
	}
	
	/**
	 * Full constructor
	 * 
	 * @param idSistema
	 * @param description
	 * @param usuarioCreacion
	 * @param fechaCreacion
	 * @param usuarioUltModif
	 * @param fechaUltModif
	 */
	public SistemaTce(
		int idSistema, 
		String description, 
		int usuarioCreacion,
		Date fechaCreacion, 
		int usuarioUltModif, 
		Date fechaUltModif
	) {
		super();
		this.idSistema = idSistema;
		this.description = description;
		this.usuarioCreacion = usuarioCreacion;
		this.fechaCreacion = fechaCreacion;
		this.usuarioUltModif = usuarioUltModif;
		this.fechaUltModif = fechaUltModif;
	}

	/**
	 * Obtiene identificador del sistema
	 * 
	 * @hibernate.id generator-class="assigned"
	 * column="ID_SISTEMA"
	 * unsaved-value="null"
	 * 
	 * @return
	 */
	public int getIdSistema() {
		return idSistema;
	}

	/**
	 * Establece el identficador del sistema
	 * 
	 * @param idSistema
	 */
	public void setIdSistema(int idSistema) {
		this.idSistema = idSistema;
	}

	/**
	 * Obtiene la descripcion del sistema
	 * 
	 * @hibernate.property column="DESCRIPCION"
	 * not-null="true"
	 * 
	 * @return
	 */
	public String getDescription() {
		if(description == null) description = "";
		return description;
	}

	/**
	 * Establece la descripcion
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Obtiene el identificador del usuario que crea la tabla
	 * 
	 * @hibernate.property column="USUARIO_CREACION"
	 * not-null="true"
	 * 
	 * @return
	 */
	public int getUsuarioCreacion() {
		return usuarioCreacion;
	}

	/**
	 * Establece el usuario que crea el registro
	 * 
	 * @param usuarioCreacion
	 */
	public void setUsuarioCreacion(int usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	/**
	 * Obtiene la fecha de creacion
	 * 
	 * @hibernate.property column="FECHA_CREACION"
	 * not-null="true"
	 * 
	 * @return
	 */
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * Establece la fecha de creacion
	 * 
	 * @param fechaCreacion
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * Obtiene el id del usuario que hizo la ultima modificacion
	 * 
	 * @hibernate.property column="USUARIO_ULT_MODIF"
	 * not-null="false"
	 * 
	 * @return
	 */
	public int getUsuarioUltModif() {
		return usuarioUltModif;
	}

	/**
	 * Establece el usuario de ultima modificacion
	 * 
	 * @param usuarioUltModif
	 */
	public void setUsuarioUltModif(int usuarioUltModif) {
		this.usuarioUltModif = usuarioUltModif;
	}

	/**
	 * Obtiene la fecha de ultima modificacion del registro
	 * 
	 * @hibernate.property column="FECHA_ULT_MODIF"
	 * not-null="false"
	 * 
	 * @return
	 */
	public Date getFechaUltModif() {
		return fechaUltModif;
	}

	/**
	 * Establece la fecha de ultima modificacion
	 * 
	 * @param fechaUltModif
	 */
	public void setFechaUltModif(Date fechaUltModif) {
		this.fechaUltModif = fechaUltModif;
	}

	public String toString() {
		return "SistemaTce [idSistema=" + idSistema + ", description="
			+ description + ", usuarioCreacion=" + usuarioCreacion
			+ ", fechaCreacion=" + fechaCreacion + ", usuarioUltModif="
			+ usuarioUltModif + ", fechaUltModif=" + fechaUltModif + "]";
	}

}

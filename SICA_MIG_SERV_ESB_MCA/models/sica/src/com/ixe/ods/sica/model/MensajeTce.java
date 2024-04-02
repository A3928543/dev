package com.ixe.ods.sica.model;

import java.util.Date;

import com.ixe.ods.bup.model.EmpleadoIxe;

/**
 * Mensaje que se publica en la captura una autorizacion de Tipo de Cambio Especial
 * 
 * @hibernate.class table="SC_MENSAJE"
 * 
 * @hibernate.query name="findMensajesTceByFechaCreacion"
 * query="select new MensajeTce(mtce.idMensaje, mtce.prioridad, mtce.mensaje, mtce.fechaCreacion, mtce.usuarioCreacion.claveUsuario) FROM MensajeTce mtce WHERE mtce.fechaCreacion BETWEEN ? AND ? ORDER BY mtce.fechaCreacion DESC"
 * 
 * @author Cesar Jeronimo Gomez
 */
public class MensajeTce {
	
	/**
	 * Identifica una prioridad de mensaje Alta
	 */
	public static final String PRIORIDAD_ALTA = "A";
	
	/**
	 * Identifica una prioridad de mensaje Media
	 */
	public static final String PRIORIDAD_MEDIA = "M";
	
	/**
	 * Identifica una prioridad de mensaje Baja
	 */
	public static final String PRIORIDAD_BAJA = "B";

	/** Identificador del mensaje */
	private int idMensaje;
	
	/** Prioridad del mensaje */
	private String prioridad;
	
	/** Mensaje */
	private String mensaje;
	
	/** Id del usuario que crea el registro del mensaje */
	private EmpleadoIxe usuarioCreacion;
	
	/** Fecha en que se crea el registro */
	private Date fechaCreacion;
	
	/** Id de usuario que hace la ultima modificacion al registro */
	private EmpleadoIxe usuarioUltModif;
	
	/** Fecha de la ultima modificacion del registro */
	private Date fechaUltModif;
	
	/** Contiene la clave de usuario autor del mensaje */
	private String nombreUsuarioCreacion;
	
	/**
	 * Minimal constructor
	 */
	public MensajeTce() {
		super();
	}
	
	/**
	 * Full constructor
	 * 
	 * @param idMensaje
	 * @param prioridad
	 * @param mensaje
	 * @param usuarioCreacion
	 * @param fechaCreacion
	 * @param usuarioUltModif
	 * @param fechaUltModif
	 */
	public MensajeTce(
		int idMensaje, 
		String prioridad, 
		String mensaje, 
		EmpleadoIxe usuarioCreacion,
		Date fechaCreacion, 
		EmpleadoIxe usuarioUltModif, 
		Date fechaUltModif
	) {
		super();
		this.idMensaje = idMensaje; 
		this.prioridad = prioridad; 
		this.mensaje = mensaje;
		this.usuarioCreacion = usuarioCreacion;
		this.fechaCreacion = fechaCreacion;
		this.usuarioUltModif = usuarioUltModif;
		this.fechaUltModif = fechaUltModif;
	}
	
	/**
	 * Constructor without id
	 * 
	 * @param prioridad
	 * @param mensaje
	 * @param usuarioCreacion
	 * @param fechaCreacion
	 * @param usuarioUltModif
	 * @param fechaUltModif
	 */
	public MensajeTce(
		String prioridad, 
		String mensaje, 
		EmpleadoIxe usuarioCreacion,
		Date fechaCreacion, 
		EmpleadoIxe usuarioUltModif, 
		Date fechaUltModif
	) {
		this(0, prioridad, mensaje, usuarioCreacion, fechaCreacion, usuarioUltModif, fechaUltModif);
	}
	
	/**
	 * Constructor sin id, setea la fecha de ultima modificacion con la fecha de creacion, y el usuario ultima modif con
	 * el usuario de creacion
	 * 
	 * @param prioridad
	 * @param mensaje
	 * @param usuarioCreacion
	 * @param fechaCreacion
	 */
	public MensajeTce(
		String prioridad, 
		String mensaje, 
		EmpleadoIxe usuarioCreacion,
		Date fechaCreacion
	) {
		this(0, prioridad, mensaje, usuarioCreacion, fechaCreacion, usuarioCreacion, fechaCreacion);
	}
	
	/**
	 * Constructor para poblar los datos a presentar en el banner
	 * 
	 * @param idMensaje
	 * @param prioridad
	 * @param mensaje
	 * @param fechaCreacion
	 * @param nombreUsuarioCreacion
	 */
	public MensajeTce(
		int idMensaje, 
		String prioridad, 
		String mensaje,
		Date fechaCreacion, 
		String nombreUsuarioCreacion
	) {
		super();
		this.idMensaje = idMensaje;
		this.prioridad = prioridad;
		this.mensaje = mensaje;
		this.fechaCreacion = fechaCreacion;
		this.nombreUsuarioCreacion = nombreUsuarioCreacion;
	}

	/**
	 * Obtiene identificador del mensaje
	 * 
	 * @hibernate.id generator-class="sequence"
     * column="ID_MENSAJE"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_MENSAJE_SEQ"
     * @return int.
	 */
	public int getIdMensaje() {
		return idMensaje;
	}

	/**
	 * Establece el identficador del mensaje
	 * 
	 * @param idMensaje
	 */
	public void setIdMensaje(int idMensaje) {
		this.idMensaje = idMensaje;
	}

	/**
	 * Obtiene la prioridad del mensaje
	 * 
	 * @hibernate.property column="PRIORIDAD"
	 * not-null="true"
	 * unique="false"
     * update="true"
     * insert="true"
	 * 
	 * @return String.
	 */
	public String getPrioridad() {
		if(prioridad == null) prioridad = "";
		return prioridad;
	}

	/**
	 * Establece la prioridad del mensaje
	 * 
	 * @param prioridad
	 */
	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}
	
	/**
	 * Obtiene el mensaje
	 * 
	 * @hibernate.property column="MENSAJE"
	 * not-null="true"
	 * unique="false"
     * update="true"
     * insert="true"
	 * @return String.
	 */
	public String getMensaje() {
		if(mensaje == null) mensaje = "";
		return mensaje;
	}

	/**
	 * Establece el mensaje
	 * 
	 * @param prioridad
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * Obtiene el identificador del usuario que crea el mensaje
	 * 
	 * @hibernate.many-to-one column="USUARIO_CREACION"
     * cascade="none"
     * class="com.ixe.ods.bup.model.EmpleadoIxe"
     * outer-join="auto"
     * unique="false"
     * @return EmpleadoIxe.
	 */
	public EmpleadoIxe getUsuarioCreacion() {
		return usuarioCreacion;
	}

	/**
	 * Establece el usuario que crea el mensaje
	 * 
	 * @param usuarioCreacion
	 */
	public void setUsuarioCreacion(EmpleadoIxe usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	/**
	 * Obtiene la fecha de creacion
	 * 
	 * @hibernate.property column="FECHA_CREACION"
	 * not-null="true"
     * unique="false"
	 * @return Date.
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
	 * @hibernate.many-to-one column="USUARIO_ULT_MODIF"
     * cascade="none"
     * class="com.ixe.ods.bup.model.EmpleadoIxe"
     * outer-join="auto"
     * unique="false"
     * @return EmpleadoIxe.
	 */
	public EmpleadoIxe getUsuarioUltModif() {
		return usuarioUltModif;
	}

	/**
	 * Establece el usuario de ultima modificacion
	 * 
	 * @param usuarioUltModif
	 */
	public void setUsuarioUltModif(EmpleadoIxe usuarioUltModif) {
		this.usuarioUltModif = usuarioUltModif;
	}

	/**
	 * Obtiene la fecha de ultima modificacion del registro
	 * 
	 * @hibernate.property column="FECHA_ULT_MODIF"
	 * not-null="false"
     * unique="false"
	 * @return Date.
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
		return "MensajeTce [idMensaje=" + idMensaje + ", prioridad="
			+ prioridad + ", mensaje=" + mensaje + ", usuarioCreacion="
			+ usuarioCreacion + ", fechaCreacion=" + fechaCreacion
			+ ", usuarioUltModif=" + usuarioUltModif + ", fechaUltModif=" + fechaUltModif
			+ ", nombreUsuarioCreacion=" + nombreUsuarioCreacion
			+ "]";
	}

}

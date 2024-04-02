package com.ixe.ods.sica.model;

// default package
import java.util.Date;


/**
 * Clase persistente para la tabla SC_CORTE.
 *
 * @hibernate.class table="SC_CORTE"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Corte"
 * dynamic-update="true"
 *
 * @hibernate.query name="findCorteById" query="FROM Corte as c where c.idCorte = ?"
 * @hibernate.query name="findCorteByFechaHoy" query="FROM Corte as c where to_char(fecha_alta, 'dd/MM/yyyy') = ? and c.idCorte = ?"
 * @hibernate.query name="findCortesByFechaHoy" query="FROM Corte as c where to_char(fecha_alta, 'dd/MM/yyyy') = ?"
 *  
 *
 * @author Banorte
 * 				Diego Pazarán	
 * 			
 * @version $Revision: 1.1.2.1.16.1 $
 */public class Corte implements java.io.Serializable {
    // Fields    

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Identificador de Corte * */
    private int idCorte;

    /** Estatus del Corte * */
    private String estatusCorte;

    /** Fecha alta del registro * */
    private Date fechaAlta;

    /** Usuario que crea el registro * */
    private String usuario;

    /** Usuario ultima modificacion * */
    private String usuarioUltMod;

    /** Fecha Ultima Modificacion * */
    private Date fechaUltMod;

    // Constructors

    /** default constructor */
    public Corte() {
    	super();
    }

    /** minimal constructor */
    public Corte(int idCorte, String estatusCorte, Date fechaAlta, String usuario,
        String usuarioUltMod, Date fechaUltMod) {
        this.idCorte = idCorte;
        this.estatusCorte = estatusCorte;
        this.fechaAlta = fechaAlta;
        this.usuario = usuario;
        this.usuarioUltMod = usuarioUltMod;
        this.fechaUltMod = fechaUltMod;
    }

    

    /**
     * Regresa el valor de idCorte
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_CORTE"
     * unsaved-value="null"
     * 
     * @hibernate.generator-param name="sequence"
     * value="SC_CORTE_SEQ"
     * @return int.
     */
    public int getIdCorte() {
        return this.idCorte;
    }

    /**
     * Set idCorte
     *
     * @param idCorte con el idCorte
     */
    public void setIdCorte(int idCorte) {
        this.idCorte = idCorte;
    }

    /**
     * Regresa el EstatusCorte
     *
     * @return String
     * @hibernate.property  column="ESTATUS_CORTE"
     * not-null="true"
     * unique="false"
     */
    public String getEstatusCorte() {
        return this.estatusCorte;
    }

    /**
     * Asigna estatusCorte
     *
     * @param estatusCorte con el 
     * 		valor a asignar.
     */
    public void setEstatusCorte(String estatusCorte) {
        this.estatusCorte = estatusCorte;
    }

    /**
     * Regresa el valor de la 
     * 	fecha en que se genera el corte.
     *
     * @return Date.
     * @hibernate.property column="FECHA_ALTA"
     * not-null="true"
     * unique="false"
     * 
     */
    public Date getFechaAlta() {
        return this.fechaAlta;
    }

    /**
     * Asigna el valor de la
     * 		fecha de alta.
     *
     * @param fechaAlta con el 
     * 		valor a asignar.
     */
    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    /**
     * Regresa el valor de Usuario
     *
     * @return String.
     * @hibernate.property column="USUARIO"
     * not-null="true"
     * unique="false"
     */
    public String getUsuario() {
        return this.usuario;
    }

    /**
     * Asigna el valor del Usuario.
     *
     * @param usuario con el 
     * 	valor a asignar.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Regresa el usuario de ultima
     * 		modificacion.
     *
     * @return String.
     * @hibernate.property column="USUARIO_ULT_MOD"
     * not-null="true"
     * unique="false" 
     */
    public String getUsuarioUltMod() {
        return this.usuarioUltMod;
    }

    /**
     * Asigna el valor del usuario
     * 		de ultima modificacion.
     *
     * @param usuarioUltMod con el valor a asignar.
     */
    public void setUsuarioUltMod(String usuarioUltMod) {
        this.usuarioUltMod = usuarioUltMod;
    }

    /**
     * Regresa la fecha de ultima
     * 	modificacion
     *
     * @return Date.
     * @hibernate.property column="FECHA_ULT_MOD"
     * not-null="true"
     * unique="false"
     */
    public Date getFechaUltMod() {
        return this.fechaUltMod;
    }

    /**
     * Asigna la fecha de ultima modificacion
     *
     * @param fechaUltMod con el 
     * 	valor a asignar.
     */
    public void setFechaUltMod(Date fechaUltMod) {
        this.fechaUltMod = fechaUltMod;
    }

	
    
}

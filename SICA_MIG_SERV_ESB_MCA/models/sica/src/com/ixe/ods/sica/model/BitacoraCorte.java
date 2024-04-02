package com.ixe.ods.sica.model;

// default package
import java.util.Date;


/**
 * Clase persistente para la tabla SC_BITACORA_CORTE.
 *
 * @hibernate.class table="SC_BITACORA_CORTE"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.BitacoraCorte"
 * dynamic-update="true"
 *
 * @hibernate.query name="findBitacoraCorteById" query="FROM BitacoraCorte as b where b.idBitacora = ?"
 * @hibernate.query name="findBitacoraCorteByIdCorte" query="FROM BitacoraCorte as b where b.scCorte.idCorte = ?"
 * @hibernate.query name="findMaxBitacoraCorteByIdCorte" query="FROM BitacoraCorte as b where b.idCorte = ? and b.idBitacora in (select max(b1.id_Bitacora) from BitacoraCorte as b1 where b1.idCorte = ?) order by b.idBitacora desc"
 * @author Banorte
 *                        Diego Pazarán
 *
 * @version $Revision: 1.1.2.1.16.1 $
 */
public class BitacoraCorte implements java.io.Serializable {
    // Fields    

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**Identificador del registro*/
    private int idBitacora;

    /**Identificador del Corte */
    private String estatusCorte;

    /**Identificador del corte */
    private int idCorte;

    /** Fecha en que se registra el evento en bitácora */
    private Date fecha;

    /**Usuario que genera el evento en bitácora.*/
    private String usuario;

    /** Comentarios del evento generado. */
    private String comentarios;

    // Constructors

    /** default constructor */
    public BitacoraCorte() {
        super();
    }

    /** full constructor */
    public BitacoraCorte(int idBitacora, String estatusCorte, int idCorte, Date fecha,
        String usuario, String comentarios) {
        this.idBitacora = idBitacora;
        this.estatusCorte = estatusCorte;
        this.idCorte = idCorte;
        this.fecha = fecha;
        this.usuario = usuario;
        this.comentarios = comentarios;
    }
    
    /**
     * constructor que recibe Corte
     * @param scCorte <code>Corte</code>
     * 	que se actualiza en la bitácora.
     */
    public BitacoraCorte(Corte scCorte) {
    	
    	this.estatusCorte = scCorte.getEstatusCorte();
        this.idCorte = scCorte.getIdCorte();
        //this.fecha =   scCorte.getFechaUltMod();
        this.fecha =   new Date();//para que registre la hora en que se genera el nuevo status
        this.usuario = scCorte.getUsuarioUltMod();
        
    }
    
    /**
     * constructor que recibe Corte
     * @param scCorte <code>Corte</code>
     * 	que se actualiza en la bitácora.
     */
    public BitacoraCorte(Corte scCorte,String usuario) {
    	
    	this.estatusCorte = scCorte.getEstatusCorte();
        this.idCorte = scCorte.getIdCorte();
        //this.fecha =   scCorte.getFechaUltMod();
        this.fecha =   new Date();//para que registre la hora en que se genera el nuevo status
        this.usuario = usuario;
        
    }

    // Property accessors

    /**
     * Regresa el valor de idCorteDetalle
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_BITACORA"
     * unsaved-value="null"
     *
     * @hibernate.generator-param name="sequence"
     * value="SC_BITACORA_CORTE_SEQ"
     * @return int.
     */
    public int getIdBitacora() {
        return this.idBitacora;
    }

    /**
     * Asigna idBitacora
     *
     * @param idBitacora con el valor
     * 		a asignar.
     */
    public void setIdBitacora(int idBitacora) {
        this.idBitacora = idBitacora;
    }

    /**
     * Regresa el estatusCorte.
     *
     * @return String.
     * @hibernate.property column="ESTATUS_CORTE"
     * not-null="true"
     * unique="false"
     * 
     */
    public String getEstatusCorte() {
        return this.estatusCorte;
    }

    /**
     * Asigna EstatusCorte
     *
     * @param scEstatusCorte con el valor 
     * 		a Asignar.
     */
    public void setEstatusCorte(String estatusCorte) {
        this.estatusCorte = estatusCorte;
    }

    /**
     * Regresa el Corte
     *
     * @return int.
     * @hibernate.property column="ID_CORTE"
     * not-null="true"
     * unique="false"
     */
    public int getidCorte() {
        return this.idCorte;
    }

    /**
     * Asigna idCorte
     *
     * @param scCorte con el valor a asignar.
     */
    public void setidCorte(int idCorte) {
        this.idCorte = idCorte;
    }

    /**
     * Regresa el valor de fecha.
     *
     * @return Date.
     *
     * @hibernate.property column="FECHA"
     * not-null="true"
     * unique="false"
     */
    public Date getFecha() {
        return this.fecha;
    }

    /**
     * Asigna fecha
     *
     * @param fecha con el valor a asignar.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Regresa el valor de usuario.
     *
     * @return String.
     *
     * @hibernate.property column="USUARIO"
     * not-null="true"
     * unique="false"
     */
    public String getUsuario() {
        return this.usuario;
    }

    /**
     * Asigna el Usuario
     *
     * @param usuario con el valor a asignar.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Regresa el valor de comentarios.
     *
     * @return String.
     *
     * @hibernate.property column="COMENTARIOS"
     * not-null="true"
     * unique="false"
     */
    public String getComentarios() {
        return this.comentarios;
    }

    /**
     * Asigna comentarios
     *
     * @param comentarios con el valor 
     * 		a asignar.
     */
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}

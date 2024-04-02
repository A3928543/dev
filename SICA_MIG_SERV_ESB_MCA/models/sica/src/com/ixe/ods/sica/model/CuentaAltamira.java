package com.ixe.ods.sica.model;

import java.io.Serializable;

import java.util.Date;




/**
 * Clase Persistente para la tabla SC_CUENTA_ALTAMIRA
 *
 *
 * 
 * @hibernate.class table="SC_CUENTA_ALTAMIRA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.CuentaAltamira"
 * dynamic-update="true"
 * 
 * @hibernate.query name="findCuentaAltamiraByNoCuenta" query="FROM CuentaAltamira AS c WHERE c.cuentaAltamira = ?"
 *
 * @author Diego Pazaran
 *  Banorte
 *
 * @version $Revision: 1.1.2.1 $
 */
public class CuentaAltamira implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor por default.
	 */
	public CuentaAltamira() {
		super();
	}
	
    /**
     * Regresa el numero de cuenta altamira

     * @return <code>String</code> con el numero
     * 	de cuenta en formato altamira.
     * @hibernate.id generator-class="assigned" 
     * column="CUENTA_ALTAMIRA"
     * not-null="true"
     * unique="false"
     *
     */
    public String getCuentaAltamira() {
        return cuentaAltamira;
    }

    /**
     * Asigna la cuenta altamira
     *
     * @param cuentaAltamira con el numero de cuenta.
     */
    public void setCuentaAltamira(String cuentaAltamira) {
        this.cuentaAltamira = cuentaAltamira;
    }

    /**
     * Regresa el identificador de la persona
     * 
     * @return <code>Integer</code> con el identificador
     * de la persona
     *
     *@hibernate.property column="ID_PERSONA"
     *not-null="false"
     *unique="false"
     */
    public Integer getIdPersona() {
        return idPersona;
    }

    /**
     * Asigna el identificador de la persona.
     *
     * @param idPersona con el identificador
     * 	 de la persona en la bup.
     */
    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    /**
     * Regresa el numero de cliente
     * 	 de Altamira.
     *
     * @return <code>Integer </code> con el
     * 	numero de SIC.
     * 
     * @hibernate.property column="SIC"
     * not-null="false"
     * unique="false"
     */
    public Integer getSic() {
        return sic;
    }

    /**
     * Asigna el numero de SIC
     *
     * @param sic con el identificador
     * 	del cliente en Altamira.
     */
    public void setSic(Integer sic) {
        this.sic = sic;
    }

    /**
     * Regresa el Centro de Responsabilidad
     *
     * @return <code>String</code> con el 
     * 	numero de CR.
     * 
     * @hibernate.property column="CR"
     * not-null="false"
     * unique="false"
     */
    public String getCr() {
        return cr;
    }

    /**
     * Asigna el Centro de Responsabilidad
     *
     * @param cr con el valor del 
     * 		Centro de Responsabilidad.
     */
    public void setCr(String cr) {
        this.cr = cr;
    }

    /**
     * Regresa el Usuario realiza
     * o modifica el registro.
     *
     * @return <code>String</code> con 
     * el usuario.
     * 
     * @hibernate.property column="USUARIO"
     * not-null="true"
     * unique="false" 
     * 
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Asigna el usuario.
     *
     * @param usuario el nombre del usuario
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * La fecha en que se da de alta el registro.
     *
     * @return <code>Date</code> con la fecha 
     *         de alta.
     *         
     * @hibernate.property column="FECHA_ALTA"
     * not-null="true"
     * unique="false"
     */
    public Date getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Asigna la fecha de alta del registro
     *
     * @param fechaAlta con la fecha de alta.
     */
    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    /**
     * Regresa la fecha de ultima modificacion
     * 	 del registro.
     *
     * @return <code>Date</code> con la fecha
     * 	de registro
     * 
     * @hibernate.property column="FECHA_ULT_MOD"
     */
    public Date getFechaUltMod() {
        return fechaUltMod;
    }

    /**
     * Asigna la fecha de modificación 
     * 	del registro.
     *
     * @param fechaUltMod con la fecha
     * en que se modifica el registro.
     */
    public void setFechaUltMod(Date fechaUltMod) {
        this.fechaUltMod = fechaUltMod;
    }
    
    /** El numero de cuenta en formato altamira* */
    private String cuentaAltamira;

    /** Identificador de la persona titular de la cuenta en la bup * */
    private Integer idPersona;

    /** El numero de cliente en Altamira* */
    private Integer sic;

    /** El centro de responsabilidad * */
    private String cr;

    /** El usuario que crea o modifica el registro * */
    private String usuario;

    /** Fecha de Alta del registro. * */
    private Date fechaAlta;

    /** Ultima modificacion del Registro. * */
    private Date fechaUltMod;

}

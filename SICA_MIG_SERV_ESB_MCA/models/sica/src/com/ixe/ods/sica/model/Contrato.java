package com.ixe.ods.sica.model;

import java.util.Date;
import java.lang.Integer;
/**
 * Clase persistente para la tabla Contrato.
 *
 * @hibernate.class table="SC_CONTRATO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Contrato"
 * dynamic-update="true"
 *
 * @hibernate.query name="findContratosByCuenta" query="FROM Contrato AS c WHERE c.noCuenta=?"
 * @hibernate.query name="findContratosByCorto" query="FROM Contrato AS c WHERE c.idContrato=?"
 *
 * @author Eduardo Hamue Moral
 * @version $Revision: 1.1.2.4.14.1.2.1 $ $Date: 2013/12/21 03:11:45 $
 */
public class Contrato {	
	public Contrato(){
		super();
	}
	
	/**
     * Regresa el valor de idContrato.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_CONTRATO"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_CONTRATO_SEQ"
     * @return int.
     */
	public int getIdContrato() {
		return idContrato;
	}

	/**
     * Fija el valor de idContrato.
     *
     * @param ID_CONTRATO El valor a asignar.
     */
	public void setIdContrato(int _idContrato) {
		idContrato = _idContrato;
	}
	
	/**
     * Regresa el valor de idCliente.
     *
     * @return Integer.
     * @hibernate.property column="ID_CLIENTE"
     * not-null="true"
     * unique="false"
     */
	public Integer getIdCliente() {
		return idCliente;
	}

	/**
     * Fija el valor de idCliente.
     *
     * @param ID_CLIENTE El valor a asignar.
     */
	public void setIdCliente(Integer _idCliente) {
		idCliente = _idCliente;
	}

	/**
     * Regresa el valor de noCuenta.
     *
     * @return String.
     * @hibernate.property column="NO_CUENTA"
     * not-null="true"
     * unique="false"
     */
	public String getNoCuenta() {
		return noCuenta;
	}

	/**
     * Fija el valor de noCuenta.
     *
     * @param NO_CUENTA El valor a asignar.
     */
	public void setNoCuenta(String _noCuenta) {
		noCuenta = _noCuenta;
	}

	/**
     * Regresa el valor de usuarioCreacion.
     *
     * @return int.
     * @hibernate.property column="USUARIO_CREACION"
     * not-null="true"
     * unique="false"
     */
	public int getUsuarioCreacion() {
		return usuarioCreacion;
	}

	/**
     * Fija el valor de usuarioCreacion.
     *
     * @param USUARIO_CREACION El valor a asignar.
     */
	public void setUsuarioCreacion(int _usuarioCreacion) {
		usuarioCreacion = _usuarioCreacion;
	}

	/**
     * Regresa el valor de fechaCreacion.
     *
     * @return Date.
     * @hibernate.property column="FECHA_CREACION"
     * not-null="true"
     * unique="false"
     */
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
     * Fija el valor de fechaCreacion.
     *
     * @param FECHA_CREACION El valor a asignar.
     */
	public void setFechaCreacion(Date _fechaCreacion) {
		fechaCreacion = _fechaCreacion;
	}

	/**
     * Regresa el valor de usuarioUltMod.
     *
     * @return int.
     * @hibernate.property column="USUARIO_ULT_MOD"
     * not-null="false"
     * unique="false"
     */
	public int getUsuarioUltMod() {
		return usuarioUltMod;
	}

	/**
     * Fija el valor de usuarioUltMod.
     *
     * @param USUARIO_ULT_MOD El valor a asignar.
     */
	public void setUsuarioUltMod(int _usuarioUltMod) {
		usuarioUltMod = _usuarioUltMod;
	}

	/**
     * Regresa el valor de fechaUltMod.
     *
     * @return Date.
     * @hibernate.property column="FECHA_ULT_MOD"
     * not-null="false"
     * unique="false"
     */
	public Date getFechaUltMod() {
		return fechaUltMod;
	}

	/**
     * Fija el valor de fechaUltMod.
     *
     * @param FECHA_ULT_MOD El valor a asignar.
     */
	public void setFechaUltMod(Date _fechaUltMod) {
		fechaUltMod = _fechaUltMod;
	}
	
	/**
     * Regresa el valor de bloqueo.
     *
     * @return int.
     * @hibernate.property column="ID_BLOQUEO"
     * not-null="false"
     * unique="false"
     */
	public Integer getIdBloqueo(){
		return idBloqueo;
	}
	
	/**
     * Fija el valor de idBloqueo.
     *
     * @param ID_BLOQUEO El valor a asignar.
     */
	public void setIdBloqueo(Integer _bloqueo){
		idBloqueo = _bloqueo;
	}
	
    /**
     * Regresa el valor de emailsComprobantes.
     *
     * @hibernate.property column="EMAILS_COMPROBANTES"
     * not-null="false"
     * @return String.
     */
    public String getEmailsComprobantes() {
        return emailsComprobantes;
    }

    /**
     * Fija el valor de emailsComprobantes.
     *
     * @param emailsComprobantes El valor a asignar.
     */
    public void setEmailsComprobantes(String emailsComprobantes) {
        this.emailsComprobantes = emailsComprobantes;
    }
	
	/**
     * La clave unica del contrato
     */
	private int idContrato;
	
	/**
     * La clave del cliente con el que se hiso el contrato
     */
	private Integer idCliente;
	
	/**
     * La clave de la cuenta sica del contrato
     */
	private String noCuenta;
	
	/**
     * La persona que regirstro el contrato
     */
	private int usuarioCreacion;
	
	/**
     * La fecha en que se registro el contrato
     */
	private Date fechaCreacion;
	
	/**
     * Ultimo usuario que modifico el contrato
     */
	private int usuarioUltMod;
	
	/**
     * Fecha en que se realizo el registro del contrato
     */
	private Date fechaUltMod;
	
	/**
     * El status del contrato
     */
	private Integer idBloqueo;

    /**
     * Una lista separada por punto y coma con los correos electr&oacute;nicos a los que se les
     * enviar&aacute; los comprobantes de operaci&oacute;n.
     */
    private String emailsComprobantes;
}

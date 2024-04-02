package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase persistente para la tabla SC_EMPRESA.
 *
 * @hibernate.class table="SC_EMPRESA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.EmpresaCambios"
 * dynamic-update="true"
 *
 * @hibernate.query name="findAllEmpresasByOrdenAlfabetico" query="FROM EmpresaCambios"
 *
 * @author Eduardo Hamue Moral
 * @version $Revision: 1.1.2.4 $ $Date: 2012/08/10 02:51:12 $
 */
public class EmpresaCambios implements Serializable
{
	/**
     * Constructor por default, no hace nada.
     */
	public EmpresaCambios(){
		super();
	}
    
	/**
     * Regresa el valor de idEmpresa.
     *
     * @return String.
     * @hibernate.id column="ID_EMPRESA"
     * unsaved-value="0"
     * generator-class="assigned"
     */
    public String getIdEmpresa() {
		return idEmpresa;
	}
    
    /**
     * Fija el valor de idEmpresa.
     *
     * @param idEmpresa El valor a asignar.
     */
	public void setIdEmpresa(String _idEmpresa) {
		idEmpresa= _idEmpresa;
	}
	
	/**
     * Regresa el valor de descripcion.
     *
     * @return String.
     * @hibernate.property column="DESCRIPCION"
     * not-null="true"
     * unique="false"
     */
	public String getDescripcion() {
		return descripcion;
	}
	
	/**
     * Fija el valor de descripcion.
     *
     * @param descripcion El valor a asignar.
     */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	/**
     * Regresa el valor de usuarioCreacion.
     *
     * @return String.
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
		this.usuarioCreacion = _usuarioCreacion;
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
		this.fechaCreacion = _fechaCreacion;
	}

	/**
     * Regresa el valor de usuarioUltMod.
     *
     * @return String.
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
		this.usuarioUltMod = _usuarioUltMod;
	}

	/**
     * Regresa el valor de fechaUltMod.
     *
     * @return String.
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
     * Regresa el valor de ESTATUS.
     *
     * @return String.
     * @hibernate.property column="ESTATUS"
     * not-null="true"
     * unique="false"
     */
	public String getEstatus() {
		return estatus;
	}
	
	/**
     * Fija el valor de ESTATUS.
     *
     * @param ESTATUS El valor a asignar.
     */
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	/**
     * La clave unica de la empresa
     */
    private String idEmpresa;
    
    /**
     * El nombre de la empresa
     */
    private String descripcion;
    
    /**
     * Usuario que registro la empresa
     */
    private int usuarioCreacion;
    
    /**
     * Fecha en que se creo el registro de la empresa
     */
    private Date fechaCreacion;
    
    /**
     * Usuario que hiso la ultima modificacion de la empresa
     */
    private int usuarioUltMod;
    
    /**
     * Ultima fecha en que se modifico la informacion de lka empresa
     */
    private Date fechaUltMod;
    
	/**
     * 
     */
    private String estatus;
    
    /**
     * Constante que identifica a la empresa Banorte
     */
    public static final String ID_EMPRESA_IXE = "IXE";
    
    /**
     * Constante que identifica a la empresa Ixe
     */
    public static final String ID_EMPRESA_BANORTE = "BNTE";
    
    
    
    
}

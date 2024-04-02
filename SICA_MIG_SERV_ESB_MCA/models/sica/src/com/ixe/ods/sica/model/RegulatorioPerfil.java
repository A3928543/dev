package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase persistente para la tabla SC_REG_PERFIL. 
 *
 * @hibernate.class table="SC_REG_PERFIL"
 * proxy="com.ixe.ods.sica.model.RegulatorioPerfil"
 * dynamic-update="true"
 */

public class RegulatorioPerfil implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Integer idPersona;
	private String contratoSica;
	//private String perfilMdd;
	private String datosRegulatoriosActualizados;
	private String regInstitucionActualizados;
	private Date fechaUltMod;
	private Date fechaAlta;
	private Integer idPersonaUltMod;
	private Integer idPersonaAlta;
	
	public RegulatorioPerfil() 
	{
		super();
	}
	//--------------------------------------------------------------------------
	/**
     * Regresa el valor de idPersona.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_PERSONA"
     * unsaved-value="null"
     * @return Integer.
     */
    public Integer getIdPersona() {
        return idPersona;
    }

    /**
     * Fija el valor de idPersona.
     *
     * @param idPersona El valor a asignar.
     */
    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }
	//--------------------------------------------------------------------------
	/**
     * Regresa el valor del contrato sica.
     *
     * @hibernate.property column="NO_CUENTA"
     * @return String.
     */
    public String getContratoSica() {
        return contratoSica;
    }

    /**
     * Fija el valor del contrato sica.
     *
     * @param contratoSica El valor a asignar.
     */
    public void setContratoSica(String contratoSica) {
        this.contratoSica = contratoSica;
    }
    //--------------------------------------------------------------------------
	/**
	 * Regresa el valor para determinar si la contraparte operara 1 millon de dolares americanos.
	 * 
	 * @hibernate.property column="PERFIL_MDD"
	 * @return String.
	 */
	/*public String getPerfilMdd() {
		return perfilMdd;
	}*/
	
	/**
	 * Fija el valor del perfil de cliente para operar 1 millon de dolares americanos
	 * @param perfilMdd
	 */
	/*public void setPerfilMdd(String perfilMdd) {
		this.perfilMdd = perfilMdd;
	}*/
	//--------------------------------------------------------------------------
	/**
	 * Regresa el valor para determinar si los datos de la contraparte han sido actualizados.
	 * 
	 * @hibernate.property column="REG_PM_ACTUALIZADO"
	 * @return String.
	 */
	public String getDatosRegulatoriosActualizados() {
		return datosRegulatoriosActualizados;
	}
	
	/**
	 * Fija el valor para indicar que los datos de la contraparte han sido actualizados. 
	 * @param regActualizado
	 */
	public void setDatosRegulatoriosActualizados(String datosRegulatoriosActualizados) {
		this.datosRegulatoriosActualizados = datosRegulatoriosActualizados;
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el valor para determinar si los datos de la relacion de la contraparte con la institucion han sido actualizados.
	 * 
	 * @hibernate.property column="REG_INST_ACTUALIZADO"
	 * @return String.
	 */
	public String getRegInstitucionActualizados() {
		return regInstitucionActualizados;
	}
	
	/**
	 * Fija el valor para indicar que los datos de la relacion de la contraparte con la institucion han sido actualizados. 
	 * @param regActualizado
	 */
	public void setRegInstitucionActualizados(String regInstitucionActualizados) {
		this.regInstitucionActualizados = regInstitucionActualizados;
	}
	//--------------------------------------------------------------------------
	/**
     * Regresa el valor de la fecha de la ultima modificacion.
     *
     * @hibernate.property column="FECHA_ULT_MOD"
     * unique="false"
     * @return Date.
     */
    public Date getFechaUltimaModificacion() {
        return fechaUltMod;
    }

    /**
     * Fija el valor de la fecha de ultima modificacion.
     *
     * @param fechaUltMod El valor a asignar.
     */
    public void setFechaUltimaModificacion(Date fechaUltMod) {
        this.fechaUltMod = fechaUltMod;
    }
  //--------------------------------------------------------------------------
    /**
     * Regresa el valor de la fecha de alta.
     *
     * @hibernate.property column="FECHA_ALTA"
     * unique="false"
     * @return Date.
     */
    public Date getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Fija el valor de la fecha de alta.
     *
     * @param fechaAlta El valor a asignar.
     */
    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
  //--------------------------------------------------------------------------
    /**
	 * Regresa el idPersona que hizo la ultima modificacion de datos.
	 * 
	 * @hibernate.property column="USUARIO_ULT_MOD"
	 * @return Integer.
	 */
    public Integer getIdPersonaUltimaMod()
    {
    	return idPersonaUltMod;
    }
    
    /**
	 * Fija el idPersona que hizo la ultima modificacion de datos
	 * @param idPersonaUltMod
	 */
    public void setIdPersonaUltimaMod(Integer idPersonaUltMod)
    {
    	this.idPersonaUltMod = idPersonaUltMod;
    }
  //--------------------------------------------------------------------------
    /**
	 * Regresa el idPersona que hizo el alta del registro.
	 * 
	 * @hibernate.property column="USUARIO_ALTA"
	 * @return Integer.
	 */
    public Integer getIdPersonaAlta()
    {
    	return idPersonaAlta;
    }
    
    /**
	 * Fija el idPersona que hizo el alta del registro
	 * @param idPersonaAlta
	 */
    public void setIdPersonaAlta(Integer idPersonaAlta)
    {
    	this.idPersonaAlta = idPersonaAlta;
    }
  //--------------------------------------------------------------------------
	public String toString() 
	{
		return "{RegulatorioPerfil[contratoSica: " + contratoSica +
		                          //"perfilMdd: " + perfilMdd +
		                          "regContraparteActualizado: " + datosRegulatoriosActualizados +
		                          "regInstitucionActualizado: " + regInstitucionActualizados +
		                          "fechaUltMod: " + fechaUltMod + 
		                          "fechaAlta: " + fechaAlta +
		                          "idPersonaUltMod: " + idPersonaUltMod +
		                          "idPersonaAlta: " + idPersonaAlta +
		"]}";
	}
}

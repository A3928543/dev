package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase persistente para la tabla SC_REG_INSTITUCION. 
 *
 * @hibernate.class table="SC_REG_INSTITUCION"
 * proxy="com.ixe.ods.sica.model.RegulatorioInstitucion"
 * dynamic-update="true"
 */
public class RegulatorioInstitucion implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer idPersona;
	private String contratoSica;
	private RegulatorioTipoRelacion tipoRelacion;
	private Integer perteneceGrupoFinanciero;
	private RegulatorioEventoRelacion eventoRelacion;
	private Date fechaEvento;
	private Date fechaSistema;
	private Integer idPersonaUltMod;
	
	public RegulatorioInstitucion() 
	{
		super();
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el valor del id del registro.
	 *
	 * @hibernate.id generator-class="sequence"
	 * column="ID_REG_INST"
	 * unsaved-value="null"
	 * @hibernate.generator-param name="sequence"
	 * value="SC_REG_INSTITUCION_SEQ"
	 * @return Integer.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Fija el valor del id del registro.
	 *
	 * @param id El valor a asignar.
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	//--------------------------------------------------------------------------
	/**
     * Regresa el valor de idPersona.
     *
     * @hibernate.property column="ID_PERSONA"
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
	 * Regresa el tipo de relacion que tiene la contraparte con la institucion.
     * @hibernate.many-to-one column="TIPO_RELACION"
     * cascade="none"
     * class="com.ixe.ods.sica.model.RegulatorioTipoRelacion"
     * outer-join="auto"
     * unique="false"
     * @return RegulatorioTipoRelacion.
     */
	public RegulatorioTipoRelacion getTipoRelacion() {
		return tipoRelacion;
	}
	
	/**
	 * Fija el valor de la relacion que tiene la contraparte con la institucion. 
	 * @param tipoRelacion
	 */
	public void setTipoRelacion(RegulatorioTipoRelacion tipoRelacion) {
		this.tipoRelacion = tipoRelacion;
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el valor que indica si la contraparte pertenece al grupo financiero de la institucion.
	 * SI:1  NO:2
	 * 
	 * @hibernate.property column="EN_GPO_FINANCIERO"
	 * @return Integer.
	 */
    public Integer getPerteneceGrupoFinanciero()
    {
    	return perteneceGrupoFinanciero;
    }
    
    /**
	 * Fija el valor para indicar si la contraparte pertenece al grupo financiero de la institucion
	 * SI:1  NO:2
	 * @param perteneceGrupoFinanciero
	 */
    public void setPerteneceGrupoFinanciero(Integer perteneceGrupoFinanciero)
    {
    	this.perteneceGrupoFinanciero = perteneceGrupoFinanciero;
    }
  //--------------------------------------------------------------------------
    /**
	 * Regresa el tipo de evento de la relacion que tiene la contraparte con la institucion.
     * @hibernate.many-to-one column="TIPO_EVENTO"
     * cascade="none"
     * class="com.ixe.ods.sica.model.RegulatorioEventoRelacion"
     * outer-join="auto"
     * unique="false"
     * @return RegulatorioEventoRelacion.
     */
	public RegulatorioEventoRelacion getEventoRelacion() {
		return eventoRelacion;
	}
	
	/**
	 * Fija el tipo de evento de la relacion que tiene la contraparte con la institucion.
	 * @param tipoRelacion
	 */
	public void setEventoRelacion(RegulatorioEventoRelacion eventoRelacion) {
		this.eventoRelacion = eventoRelacion;
	}
	//--------------------------------------------------------------------------
	/**
     * Regresa el valor de la fecha capturada en el alta o modificación de la relacion.
     *
     * @hibernate.property column="FECHA_EVENTO"
     * unique="false"
     * @return Date.
     */
    public Date getFechaEvento() {
        return fechaEvento;
    }

    /**
     * Fija el valor de la fecha capturada en el alta o modificación de la relacion.
     *
     * @param fechaUltMod El valor a asignar.
     */
    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }
  //--------------------------------------------------------------------------
    /**
     * Regresa el valor de la fecha del sistema de cuando se registraron los datos o se modificaron.
     *
     * @hibernate.property column="FECHA_ALTA"
     * unique="false"
     * @return Date.
     */
    public Date getFechaSistema() {
        return fechaSistema;
    }

    /**
     * Fija el valor de la fecha del sistema de cuando se registraron los datos o se modificaron.
     *
     * @param fechaSistema El valor a asignar.
     */
    public void setFechaSistema(Date fechaSistema) {
        this.fechaSistema = fechaSistema;
    }
    //--------------------------------------------------------------------------
    /**
	 * Regresa el idPersona que hizo la ultima modificacion de datos.
	 * 
	 * @hibernate.property column="USUARIO_ALTA"
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
	
}

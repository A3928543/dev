package com.ixe.ods.sica.model;

import java.util.Date;
import java.util.List;

import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.seguridad.model.Jerarquia;

/**
 * Clase persistente de la tabla SEGU_NODO_JERARQUIA
 * 
 * @hibernate.class table="SEGU_NODO_JERARQUIA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.SicaNodoJerarquia"
 * dynamic-update="true"
 * 
 * @author Efren Trinidad
 * @version 
 */
public class SicaNodoJerarquia {

	private int idNodoJerarquia;
	private SicaNodoJerarquia jefe;
	private Jerarquia jerarquia;
	private EmpleadoIxe persona;
	private EmpleadoIxe alterno;
	private Date fechaInicioAlterno;
	private Date fechaFinAlterno;
	//private List seguNodoJerarquias = new ArrayList(0);

	// Constructors

	/** default constructor */
	public SicaNodoJerarquia() {
	}

	/** minimal constructor */
	public SicaNodoJerarquia(EmpleadoIxe persona) {
		this.persona = persona;
	}
	
	

	/** full constructor */
	public SicaNodoJerarquia(SicaNodoJerarquia jefe,
			Jerarquia jerarquia,
			EmpleadoIxe persona,
			EmpleadoIxe alterno, Date fechaInicioAlterno,
			Date fechaFinAlterno, List seguNodoJerarquias) {
		this.jefe = jefe;
		this.jerarquia = jerarquia;
		this.persona = persona;
		this.alterno = alterno;
		this.fechaInicioAlterno = fechaInicioAlterno;
		this.fechaFinAlterno = fechaFinAlterno;
		//this.seguNodoJerarquias = seguNodoJerarquias;
	}

	/**
	 * Regresa el valor de idNodoJerarquia
	 * 
	 * @hibernate.id generator-class="sequence"
	 * column="ID_NODO_JERARQUIA"
     * unsaved-value="null"
	 * @hibernate.generator-param name="sequence"
	 * value="SEGU_NODO_JERARQUIA_SEQ"
	 * 
	 * @return the idNodoJerarquia
	 */
	public int getIdNodoJerarquia() {
		return idNodoJerarquia;
	}

	/**
	 * @param idNodoJerarquia the idNodoJerarquia to set
	 */
	public void setIdNodoJerarquia(int idNodoJerarquia) {
		this.idNodoJerarquia = idNodoJerarquia;
	}

	/**
	 * Regresa el valor de jefe
	 * @hibernate.many-to-one column="ID_JEFE"
	 * cascade="none"
     * class="com.ixe.ods.sica.model.SicaNodoJerarquia"
     * outer-join="auto"
     * unique="false"
	 * 
	 * @return the jefe
	 */
	public SicaNodoJerarquia getJefe() {
		return jefe;
	}

	/**
	 * @param jefe the jefe to set
	 */
	public void setJefe(SicaNodoJerarquia jefe) {
		this.jefe = jefe;
	}

	/**
	 * Regresa el valor de jerarquia
	 * @hibernate.many-to-one column="ID_JERARQUIA"
	 * cascade="none"
	 * class="com.ixe.ods.seguridad.model.Jerarquia"
	 * outer-join="auto"
     * unique="false"
	 * 
	 * @return the jerarquia
	 */
	public Jerarquia getJerarquia() {
		return jerarquia;
	}

	/**
	 * @param jerarquia the jerarquia to set
	 */
	public void setJerarquia(Jerarquia jerarquia) {
		this.jerarquia = jerarquia;
	}

	/**
	 * Regresa el valor de persona
	 * 
	 * @hibernate.many-to-one column="ID_PERSONA"
	 * cascade="none"
	 * class="com.ixe.ods.bup.model.EmpleadoIxe"
	 * outer-join="auto"
	 * unique="false"
	 * 
	 * @return the persona
	 */
	public EmpleadoIxe getPersona() {
		return persona;
	}

	/**
	 * @param persona the persona to set
	 */
	public void setPersona(EmpleadoIxe persona) {
		this.persona = persona;
	}

	/**
	 * Regresa el valor de alterno
	 * 
	 * @hibernate.many-to-one column="ID_ALTERNO"
	 * cascade="none"
	 * class="com.ixe.ods.bup.model.EmpleadoIxe"
	 * outer-join="auto"
	 * unique="false"
	 * 
	 * @return the alterno
	 */
	public EmpleadoIxe getAlterno() {
		return alterno;
	}

	/**
	 * @param alterno the alterno to set
	 */
	public void setAlterno(EmpleadoIxe alterno) {
		this.alterno = alterno;
	}

	/**
	 * Regresa el valor de fechaInicioAlterno
	 * 
	 * @hibernate.property column="FECHA_INICIO_ALTERNO"
	 * not-null="false"
	 * unique="false"
	 * 
	 * @return the fechaInicioAlterno
	 */
	public Date getFechaInicioAlterno() {
		return fechaInicioAlterno;
	}

	/**
	 * @param fechaInicioAlterno the fechaInicioAlterno to set
	 */
	public void setFechaInicioAlterno(Date fechaInicioAlterno) {
		this.fechaInicioAlterno = fechaInicioAlterno;
	}

	/**
	 * Regresa el valor de fechaFinAlterno
	 * 
	 * @hibernate.property column="FECHA_FIN_ALTERNO"
	 * not-null="false"
	 * unique="false"
	 * 
	 * @return the fechaFinAlterno
	 */
	public Date getFechaFinAlterno() {
		return fechaFinAlterno;
	}

	/**
	 * @param fechaFinAlterno the fechaFinAlterno to set
	 */
	public void setFechaFinAlterno(Date fechaFinAlterno) {
		this.fechaFinAlterno = fechaFinAlterno;
	}

	
	/**
	 * @return the seguNodoJerarquias
	 */
	/*public List getSeguNodoJerarquias() {
		return seguNodoJerarquias;
	}*/

	/**
	 * @param seguNodoJerarquias the seguNodoJerarquias to set
	 */
	/*public void setSeguNodoJerarquias(List seguNodoJerarquias) {
		this.seguNodoJerarquias = seguNodoJerarquias;
	}*/

	


}

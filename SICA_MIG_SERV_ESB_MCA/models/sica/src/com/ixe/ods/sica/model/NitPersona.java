package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * The Class NitPersona.
 * 
 * @hibernate.class table="SC_NIT_PERSONA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.NitPersona"
 * dynamic-update="true"
 *
 * @hibernate.query name="findNitByIdPersona" query="FROM NitPersona as n where n.idPersona = ?"
 */
public class NitPersona implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id persona. */
	private Integer idPersona;
	
	/**
	 * Número de Identificación Tributaria nit
	 */
	private String nit;// VARCHAR(9 BYTE),
	
	/**
	 * Instantiates a new nit persona.
	 */
	public NitPersona() {
	}
	
	/**
	 * Instantiates a new nit persona.
	 *
	 * @param idPersona the id persona
	 * @param nit the NIT
	 */
	public NitPersona(Integer idPersona, String nit) {
		this.idPersona = idPersona;
		this.nit = nit;
	}

	/**
     * Gets the id persona.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_PERSONA"
     * unsaved-value="null"
     * @return the id persona
     */
	public Integer getIdPersona() {
		return idPersona;
	}

	/**
	 * Sets the id persona.
	 *
	 * @param idPersona the new id persona
	 */
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}
	
	/**
     * Regresa el valor de nit.
     *
     * @return String.
     * @hibernate.property column="NIT"
     * not-null="false"
     * unique="false"
     */
	public String getNit() {
		return nit;
	}
	
	/**
     * Fija el valor de nit.
     *
     * @param NIT El valor a asignar.
     */
	public void setNit(String _nit) {
		nit = _nit;
	}
	
	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idPersona == null) ? 0 : idPersona.hashCode());
		return result;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NitPersona other = (NitPersona) obj;
		if (idPersona == null) {
			if (other.idPersona != null)
				return false;
		} else if (!idPersona.equals(other.idPersona))
			return false;
		return true;
	}
	

}

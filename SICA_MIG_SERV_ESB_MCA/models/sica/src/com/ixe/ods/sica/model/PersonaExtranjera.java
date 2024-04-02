package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * The Class PersonaExtranjera.
 * 
 * @hibernate.class table="SC_PERSONA_EXTRANJERA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.PersonaExtranjera"
 * dynamic-update="true"
 *
 */
public class PersonaExtranjera implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id persona. */
	private Integer idPersona;
	
	/** The id extranjero. */
	private String idExtranjero;
	
	/**
	 * Instantiates a new persona extranjera.
	 */
	public PersonaExtranjera() {
	}
	
	/**
	 * Instantiates a new persona extranjera.
	 *
	 * @param idPersona the id persona
	 * @param idExtranjero the id extranjero
	 */
	public PersonaExtranjera(Integer idPersona, String idExtranjero) {
		this.idPersona = idPersona;
		this.idExtranjero = idExtranjero;
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
	 * 
	 *
	 * @return the id extranjero
	 */
	/**
     * Gets the id extranjero.
     *
     * @return String.
     * @hibernate.property column="ID_EXTRANJERO"
     * not-null="true"
     * unique="true"
     */
	public String getIdExtranjero() {
		return idExtranjero;
	}

	/**
	 * Sets the id extranjero.
	 *
	 * @param idExtranjero the new id extranjero
	 */
	public void setIdExtranjero(String idExtranjero) {
		this.idExtranjero = idExtranjero;
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
		PersonaExtranjera other = (PersonaExtranjera) obj;
		if (idPersona == null) {
			if (other.idPersona != null)
				return false;
		} else if (!idPersona.equals(other.idPersona))
			return false;
		return true;
	}
	

}

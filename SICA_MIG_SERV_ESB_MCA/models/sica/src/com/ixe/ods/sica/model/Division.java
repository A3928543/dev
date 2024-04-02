package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="SC_DIVISION"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Division"
 * dynamic-update="true" 
 * 
 * @hibernate.query name="findDivisiones"
 * query="FROM Division AS d ORDER BY d.nombre"
 * 
 * @author César Jerónimo Gómez
 */
public class Division implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6910088612583191711L;

	/**
	 * Identificador de la Division
	 */
	private Integer idDivision;
	
	/**
	 * Descripción de la division
	 */
	private String nombre;
	
	/**
	 * Relación 1..n con Plaza 
	 */
	private List plazas;
	
	/**
	 * Constructor por default
	 */
	public Division() {
		super();
	}
	
	/**
	 * Constructor con argumentos
	 * 
	 * @param idDivision
	 * @param nombre
	 */
	public Division(Integer idDivision, String nombre, List plazas) {
		super();
		this.idDivision = idDivision;
		this.nombre = nombre;
		this.plazas = plazas;
	}

	/**
     * Regresa el valor de idDivision.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_DIVISION"
     * unsaved-value="null"
     * @return Integer.
     */
	public Integer getIdDivision() {
		return idDivision;
	}
	
	/**
     * Regresa el valor del nombre de la división.
     *
     * @hibernate.property column="NOMBRE"
     * not-null="false"
     * unique="false"
     * @return String.
     */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Establece el valor de idDivision
	 * @param idDivision
	 */
	public void setIdDivision(Integer idDivision) {
		this.idDivision = idDivision;
	}
	
	/**
	 * Establece el valor de nommbre
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
     * Regresa el valor de plazas.
     *
     * @hibernate.bag inverse="true"
     * lazy="true"
     * cascade="none"
     * order-by="ID_PLAZA"
     * @hibernate.collection-key column="ID_DIVISION"
     * @hibernate.collection-one-to-many class="com.ixe.ods.sica.model.Plaza"
     * @return List.
     */
	public List getPlazas() {
		return plazas;
	}

	/**
	 * Establece la relación 1..n con la entidad plaza
	 * @param plazas
	 */
	public void setPlazas(List plazas) {
		this.plazas = plazas;
	}
	
	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Division)) {
            return false;
        }
        Division castOther = (Division) other;
        return new EqualsBuilder().append(this.getIdDivision(), castOther.getIdDivision()).isEquals();
    }

    /**
     * Hibernate lo utiliza para cuestiones de herencia.
     * Regresa el hashCode del Objeto.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code> El HashCode identificador del objeto.
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdDivision()).toHashCode();
    }
	
}

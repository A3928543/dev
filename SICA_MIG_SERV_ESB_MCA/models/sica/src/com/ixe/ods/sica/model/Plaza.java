package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="SC_PLAZA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Plaza"
 * dynamic-update="true" 
 * 
 * @hibernate.query name="findPlazasByDivision"
 * query="FROM Plaza AS p WHERE p.division.idDivision = ? ORDER BY p.nombre"
 * 
 * @author César Jerónimo Gómez
 *
 */
public class Plaza implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4182326157679914612L;

	/**
	 * Identificador de la plaza
	 */
	private Integer idPlaza;
	
	/**
	 * Division asociada
	 */
	private Division division;
	
	/**
	 * Descripción de la plaza
	 */
	private String nombre;
	
	/**
	 * Relación 1..n con la entidad Zona
	 */
	private List zonas;

	/**
	 * Constructor por default
	 */
	public Plaza() {
		super();
	}
	
	/**
	 * Constructor con argumentos
	 * @param idPlaza
	 * @param idDivision
	 * @param nombre
	 */
	public Plaza(Integer idPlaza, Division division, String nombre, List zonas) {
		super();
		this.idPlaza = idPlaza;
		this.division = division;
		this.nombre = nombre;
		this.zonas = zonas;
	}
	
	/**
     * Regresa el valor de idPlaza.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_PLAZA"
     * unsaved-value="null"
     * @return Integer.
     */
	public Integer getIdPlaza() {
		return idPlaza;
	}

	/**
     * Regresa la division asociada.
     *
     * @hibernate.many-to-one column="ID_DIVISION"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Division"
     * outer-join="auto"
     * unique="false"
     * @return Division.
     */
	public Division getDivision() {
		return division;
	}

	/**
     * Regresa el valor del nombre de la plaza.
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
	 * Establece el identificador de la plaza
	 * @param idPlaza
	 */
	public void setIdPlaza(Integer idPlaza) {
		this.idPlaza = idPlaza;
	}

	/**
	 * Establece la division asociada
	 * @param division
	 */
	public void setDivision(Division division) {
		this.division = division;
	}

	/**
	 * Establece la decripción de la plaza
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
     * Regresa el valor de zonas.
     *
     * @hibernate.bag inverse="true"
     * lazy="true"
     * cascade="none"
     * order-by="ID_ZONA"
     * @hibernate.collection-key column="ID_PLAZA"
     * @hibernate.collection-one-to-many class="com.ixe.ods.sica.model.Zona"
     * @return List.
     */
	public List getZonas() {
		return zonas;
	}

	/**
	 * Establece la relación 1..n con la entidad Zona
	 * @param zonas
	 */
	public void setZonas(List zonas) {
		this.zonas = zonas;
	}
	
	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Plaza)) {
            return false;
        }
        Plaza castOther = (Plaza) other;
        return new EqualsBuilder().append(this.getIdPlaza(), castOther.getIdPlaza()).isEquals();
    }

    /**
     * Hibernate lo utiliza para cuestiones de herencia.
     * Regresa el hashCode del Objeto.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code> El HashCode identificador del objeto.
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdPlaza()).toHashCode();
    }
	
}

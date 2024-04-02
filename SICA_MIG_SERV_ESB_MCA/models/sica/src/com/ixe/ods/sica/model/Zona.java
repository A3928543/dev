package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="SC_ZONA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Zona"
 * dynamic-update="true" 
 * 
 * @hibernate.query name="findZonasByPlaza"
 * query="FROM Zona AS z WHERE z.plaza.idPlaza = ? ORDER BY z.nombre"
 * 
 * @author César Jerónimo Gómez
 *
 */
public class Zona implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6206477718416165526L;

	/**
	 * Identificador de la zona
	 */
	private Integer idZona;
	
	/**
	 * Plaza asociada
	 */
	private Plaza plaza;
	
	/**
	 * Descripción de la zona
	 */
	private String nombre;

	/**
	 * Constructor por default
	 */
	public Zona() {
		super();
	}
	
	/**
	 * Constructor con parámetros
	 * @param idZona
	 * @param plaza
	 * @param nombre
	 */
	public Zona(Integer idZona, Plaza plaza, String nombre) {
		super();
		this.idZona = idZona;
		this.plaza = plaza;
		this.nombre = nombre;
	}

	/**
     * Regresa el valor de idZona.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_ZONA"
     * unsaved-value="null"
     * @return Integer.
     */
	public Integer getIdZona() {
		return idZona;
	}

	/**
     * Regresa la plaza asociada.
     *
     * @hibernate.many-to-one column="ID_PLAZA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Plaza"
     * outer-join="auto"
     * unique="false"
     * @return Plaza.
     */
	public Plaza getPlaza() {
		return plaza;
	}

	/**
     * Regresa el valor del nombre de la zona.
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
	 * Establece el identificador de la zona
	 * @param idZona
	 */
	public void setIdZona(Integer idZona) {
		this.idZona = idZona;
	}

	/**
	 * Establece la plaza asociada
	 * @param plaza
	 */
	public void setPlaza(Plaza plaza) {
		this.plaza = plaza;
	}

	/**
	 * Establece la descripción de la zona
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Zona)) {
            return false;
        }
        Zona castOther = (Zona) other;
        return new EqualsBuilder().append(this.getIdZona(), castOther.getIdZona()).isEquals();
    }

    /**
     * Hibernate lo utiliza para cuestiones de herencia.
     * Regresa el hashCode del Objeto.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code> El HashCode identificador del objeto.
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdZona()).toHashCode();
    }
	
}

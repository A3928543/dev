package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.ixe.ods.bup.model.Sucursal;

/**
 * @hibernate.class table="SC_SUCURSAL_ZONA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.SucursalZona"
 * dynamic-update="true" 
 * 
 * @hibernate.query name="findSucursalesByZona"
 * query="FROM SucursalZona sz WHERE sz.zona.idZona = ? ORDER BY sz.sucursal.nombre"
 * 
 * @author César Jerónimo Gómez
 *
 */
public class SucursalZona implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6934821696663413719L;

	/**
	 * Identificador de la sucursal
	 */
	private Integer idSucursal;
	
	/**
	 * Identificador de la sucursal
	 */
	private Sucursal sucursal;
	
	/**
	 * Zona asociada
	 */
	private Zona zona;

	/**
	 * Constructor por default
	 */
	public SucursalZona() {
		super();
	}
	
	/**
	 * Constructor parametrizado
	 * @param idSucursal
	 * @param zona
	 */
	public SucursalZona(Integer idSucursal, Sucursal sucursal, Zona zona) {
		super();
		this.idSucursal = idSucursal;
		this.sucursal = sucursal;
		this.zona = zona;
	}

	/**
     * Regresa el valor de idSucursal.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_SUCURSAL"
     * unsaved-value="null"
     * @return Integer.
     */
	public Integer getIdSucursal() {
		return idSucursal;
	}

	/**
	 * Establece el valor para el identificador de la sucursal
	 * @param idSucursal
	 */
	public void setIdSucursal(Integer idSucursal) {
		this.idSucursal = idSucursal;
	}

	/**
     * Regresa el valor del identificador de la sucursal.
     *
     * @hibernate.one-to-one column="ID_SUCURSAL"
     * cascade="none"
     * class="com.ixe.ods.bup.model.Sucursal"
     * outer-join="auto"
     * @return Sucursal
     */
	public Sucursal getSucursal() {
		return sucursal;
	}

	/**
     * Regresa la zona asociada.
     *
     * @hibernate.many-to-one column="ID_ZONA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Zona"
     * outer-join="auto"
     * unique="false"
     * @return Zona.
     */
	public Zona getZona() {
		return zona;
	}

	/**
	 * Establece el valor del identificador de la sucursal
	 * @param idSucursal
	 */
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	/**
	 * Establece la zona asociada
	 * @param zona
	 */
	public void setZona(Zona zona) {
		this.zona = zona;
	}
	
	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof SucursalZona)) {
            return false;
        }
        SucursalZona castOther = (SucursalZona) other;
        return new EqualsBuilder().append(this.getSucursal().getIdSucursal(), castOther.getSucursal().getIdSucursal()).isEquals();
    }

    /**
     * Hibernate lo utiliza para cuestiones de herencia.
     * Regresa el hashCode del Objeto.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code> El HashCode identificador del objeto.
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getSucursal().getIdSucursal()).toHashCode();
    }
	
}

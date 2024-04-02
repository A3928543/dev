/*
 * $Id: DivisaDto.java,v 1.1.4.2 2010/10/08 01:31:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.teller.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Data Transfer Object utilizado en la consulta de Deal.
 *
 * @author Israel Rebollar
 * @version $Revision: 1.1.4.2 $ $Date: 2010/10/08 01:31:04 $
 */
public class DivisaDto implements Serializable {

	/**
	 * Constructor default, no hace nada.
	 */
	public DivisaDto() {
		super();
	}
	
	/**
	 * Constructor que inicializa los valores del objeto.
	 * 
	 * @param idDivisa
	 * @param descripcion
	 * @param idMoneda
	 * @param tipo
	 */
	public DivisaDto(String idDivisa, String descripcion, String idMoneda, String tipo) {
		this.idDivisa = idDivisa;
		this.descripcion = descripcion;
		this.idMoneda = idMoneda;
		this.tipo = tipo;
	}
	
	/**
	 * @return the idDivisa
	 */
	public String getIdDivisa() {
		return idDivisa;
	}

	/**
	 * @param idDivisa the idDivisa to set
	 */
	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the idMoneda
	 */
	public String getIdMoneda() {
		return idMoneda;
	}

	/**
	 * @param idMoneda the idMoneda to set
	 */
	public void setIdMoneda(String idMoneda) {
		this.idMoneda = idMoneda;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	/**
     * Regresa una cadena con los nombres y valores de las variables de instancia.
     *
     * @return String.
     */
	public String toString() {
		return new ToStringBuilder(this).append("idDivisa", idDivisa).
        append("descripcion", descripcion).append("idMoneda", idMoneda).
        append("tipo", tipo).toString();
	}
	
	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
    	if (!(other instanceof DivisaDto)) {
            return false;
        }
    	DivisaDto castOther = (DivisaDto) other;
    	return new EqualsBuilder().append(this.getIdDivisa(), castOther.getIdDivisa()).isEquals();
    }
    
    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
    	return new HashCodeBuilder().append(getIdDivisa()).toHashCode();
    }

	/**
     * La clave de la divisa.
     */
    private String idDivisa;
    
    /**
     * La descripci&oacute;n de la divisa.
     */
    private String descripcion;
    
    /**
     * El identificador de la moneda en el sistema Phoenix.
     */
    private String idMoneda;
    
    /**
     * F)recuente, N)o frecuente, M)etal amonedado.
     */
    private String tipo;
    
	/**
	 * EL UID. 
	 */
	private static final long serialVersionUID = -2787325691502537805L;

	
}

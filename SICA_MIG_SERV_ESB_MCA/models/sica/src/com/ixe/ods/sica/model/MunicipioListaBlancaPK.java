package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Llave primaria de la clase MunicipioListaBlancaPK.
 *
 * @author lvillegas
 * @version 1.0 $Date: 2010/10/08 01:30:18 $
 * @see MunicipioListaBlancaPK
 */
public class MunicipioListaBlancaPK implements Serializable {
		
    /**
     * Returns the idMunicipio.
     *
     * @hibernate.property column="ID_MUNICIPIO"
     * not-null="true"
     * unique="false"
     * @return EmpleadoIxe.
     */
	public Integer getIdMunicipio() {
		return idMunicipio;
	}

	/**
	 * @param idMunicipio the idMunicipio to set
	 */
	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	
    /**
     * @hibernate.property column="ID_ENTIDAD_FEDERATIVA"
     * not-null="true"
     * unique="false"
     * @return the idEstado
	 */
	public Integer getIdEstado() {
		return idEstado;
	}

	/**
	 * @param idEstado the idEstado to set
	 */
	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}

    /**
     * Implements a standard way to compare instances.
     * 
     * @param other Another object.
     * @return  <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (!(other instanceof MunicipioListaBlancaPK)) {
            return false;
        }
        MunicipioListaBlancaPK object = (MunicipioListaBlancaPK) other;
        return new EqualsBuilder()
                .append(this.getIdEstado(),
                        object.getIdEstado())
                .append(this.getIdMunicipio(),
                        object.getIdMunicipio()).isEquals();
    }
      
	/**
	 * Hibernate lo utiliza para cuestiones de herencia.
	 * 
	 * @return int El HashCode identificador del objeto.
	 */
	public int hashCode() {
		return new HashCodeBuilder().append(idMunicipio).append(idEstado).toHashCode();
	}
	
    /**
     * Id del municipio perteneciente al c&oacute;digo postal.
     */
    private Integer idMunicipio;
    
    /**
     * Id del estado perteneciente al c&oacute;digo postal.
     */
    private Integer idEstado;
	
	/**
	 * El UID para serialziaci&oacute;n.
	 */
	private static final long serialVersionUID = 7185464504146134714L;

}

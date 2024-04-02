/*
 * $Id: CodigoPostalListaBlanca.java,v 1.1.2.1 2010/10/08 01:30:18 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Utilizado para transportar la informaci&oacute;n relacionada a los c&oacute;digos postales.
 *
 * @author lvillegas
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:30:18 $
 */
public class CodigoPostalListaBlanca implements Serializable {
    /**
     * Constructor vac&iacute;o.
     */
    public CodigoPostalListaBlanca() {
        super();
    }
    
    /**
     * Constructor que inicializa todas las variables de instancia.
     *
     * @param codigoPostal C&oacute;digo postal.
     * @param zonaFronteriza Determina si el c&oacute;digo postal es una zona fronteriza.
     * @param zonaTuristica Determina si el c&oacute;digo postal es una zona tur&iacute;stica.
     * @param idMunicipio Id del municipio perteneciente al c&oacute;digo postal.
	 * @param idEstado Id del estado perteneciente al c&oacute;digo postal.
     */
    public CodigoPostalListaBlanca(String codigoPostal, Boolean zonaFronteriza,
			Boolean zonaTuristica, Integer idMunicipio, Integer idEstado) {
		super();
		this.codigoPostal = codigoPostal;
		this.zonaFronteriza = zonaFronteriza;
		this.zonaTuristica = zonaTuristica;
		this.idMunicipio = idMunicipio;
		this.idEstado = idEstado;
	}

	/**
     * Regresa el valor de codigoPostal.
     *
     * @return String.
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Fija el valor de codigoPostal.
     *
     * @param codigoPostal El valor a asignar.
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * Regresa el valor de zonaTuristica.
     *
     * @return Boolean.
     */
    public Boolean getZonaTuristica() {
        return zonaTuristica;
    }

    /**
     * Fija el valor de zonaTuristica.
     *
     * @param zonaTuristica El valor a asignar.
     */
    public void setZonaTuristica(Boolean zonaTuristica) {
        this.zonaTuristica = zonaTuristica;
    }
    
    /**
     * Regresa el valor de zonaFronteriza.
     *
     * @return Boolean.
     */
    public Boolean getZonaFronteriza() {
        return zonaFronteriza;
    }

    /**
     * Fija el valor de zonaFronteriza.
     *
     * @param zonaFronteriza El valor a asignar.
     */
    public void setZonaFronteriza(Boolean zonaFronteriza) {
        this.zonaFronteriza = zonaFronteriza;
    }
    
    /**
     * Regresa el valor del idMunicipio.
     *
     * @return Integer
     */
	public Integer getIdMunicipio() {
		return idMunicipio;
	}
	
	/**
	 * Fija el valor del idMunicipio.
	 *
	 * @param idMunicipio
	 */
	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	
	/**
	 * Regresa el valor del idEstado.
	 *
	 * @return Integer.
	 */
	public Integer getIdEstado() {
		return idEstado;
	}
	
	/**
	 * Fija el valor del idEstado.
	 *
	 * @param idEstado
	 */
	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}

	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof CodigoPostalListaBlanca)) {
            return false;
        }
        CodigoPostalListaBlanca castOther = (CodigoPostalListaBlanca) other;
        return new EqualsBuilder()
            .append(this.getCodigoPostal(),
                castOther.getCodigoPostal())
            .append(this.getIdMunicipio(),
                castOther.getIdMunicipio())
            .append(this.getIdEstado(),
                castOther.getIdEstado())
            .isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @return <code>int</code>
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getCodigoPostal().hashCode();
    }

    /**
     * Regresa una cadena con la descripci&oacute;n de los campos de esta instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("codigoPostal", codigoPostal)
                .append("zonaFronteriza", zonaFronteriza)
                .append("zonaTuristica", zonaTuristica)
                .append("idMunicipio", idMunicipio)
                .append("idEstado", idEstado)
                .append("nombreEstado", nombreEstado)
                .append("nombreMunicipio", nombreMunicipio)
                .toString();
    }
    
    /**
     * C&oacute;digo postal.
     */
    private String codigoPostal;
    
    /**
     * Determina si el c&oacute;digo postal es una zona fronteriza.
     */
    private Boolean zonaFronteriza;
    
    /**
     * Determina si el c&oacute;digo postal es una zona tur&iacute;stica.
     */
    private Boolean zonaTuristica;
    
    /**
     * Determina el id del municipio.
     */
    private Integer idMunicipio;
    
    /**
     * Determina el id del estado.
     */
    private Integer idEstado;
    
    /**
     * Determina el nombre del municipio.
     */
    private String nombreMunicipio;
    
    /**
     * Determna el nombre del estado.
     */
    private String nombreEstado;

    /**
     * Serializador.
     */
    private static final long serialVersionUID = 3967887960822630780L;
}

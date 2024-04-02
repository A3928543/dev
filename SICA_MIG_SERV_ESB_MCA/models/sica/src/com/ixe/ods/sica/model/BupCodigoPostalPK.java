/*
 * $Id: BupCodigoPostalPK.java,v 1.1.2.1 2010/10/08 01:30:18 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase persistente para la tabla BUP_CODIGO_POSTAL.
 *
 * @author lvillegas
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:30:18 $
 */
public class BupCodigoPostalPK implements Serializable {
    /**
     * Constructor vac&iacute;o.
     */
    public BupCodigoPostalPK() {
        super();
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
     * Regresa el valor del municipio.
     *
     * @return BupMunicipio.
     */
    public BupMunicipio getMunicipio() {
        return municipio;
    }

    /**
     * Fija el valor de municipio.
     * 
     * @param municipio El valor a asignar.
     */
    public void setMunicipio(BupMunicipio municipio) {
        this.municipio = municipio;
    }

    /**
     * Regresa el valor de idColonia.
     *
     * @return Integer.
     */
    public Integer getIdColonia() {
        return idColonia;
    }
    
    /**
     * Fija el valor de idColonia.
     * 
     * @param idColonia El valor a asignar.
     */
    public void setIdColonia(Integer idColonia) {
        this.idColonia = idColonia;
    }
    
	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof BupCodigoPostalPK)) {
            return false;
        }
        BupCodigoPostalPK castOther = (BupCodigoPostalPK) other;
        return this.getCodigoPostal().equals(castOther.getCodigoPostal()) &&
            this.getMunicipio().equals(castOther.getMunicipio()) &&
            this.getIdColonia().equals(castOther.getIdColonia());
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
                .append("municipio", municipio)
                .append("idColonia", idColonia)
                .toString();
    }
    
    /**
     * C&oacute;digo postal.
     */
    private String codigoPostal;
    
    /**
     * Municipio perteneciente al c&oacute;digo postal.
     */
    private BupMunicipio municipio;
    
    /**
     * Id de la colonia perteneciente al c&oacute;digo postal.
     */
    private Integer idColonia;
    
    /**
     * Serializador.
     */
    private static final long serialVersionUID = 3967887960822630780L;
}

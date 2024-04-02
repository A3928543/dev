/*
 *  $Id: BitacoraEnviadasPK.java,v 1.12 2008/02/22 18:25:20 ccovian Exp $
 *  Ixe, Ene 27, 2006
 *  Copyright (C) 2001-2006 Grupo Financiero Ixe
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;


/**
 * Llave primaria de la clase BitacoraEnviadas.
 *
 * @author Javier Cordova
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:20 $
 * @see BitacoraEnviadas
 */
public class BitacoraEnviadasPK implements Serializable {
			
	/**
	 * Constructor vac&iacute;o.
	 */
	public BitacoraEnviadasPK() {
		super();
	}
			
	/**
     * Regresa el valor de idConf.
     *
     * @hibernate.property column="ID_CONF"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getIdConf() {
        return idConf;
    }

    /**
     * Fija el valor de idConf.
     *
     * @param idConf El valor a asignar.
     */
    public void setIdConf(String idConf) {
        this.idConf = idConf;
    }
    
    /**
     * Regresa el valor de divisa.
     *
     * @hibernate.property column="DIVISA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getDivisa() {
        return divisa;
    }
    
    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof BitacoraEnviadasPK)) {
            return false;
        }
        BitacoraEnviadasPK object = (BitacoraEnviadasPK) other;
        return new EqualsBuilder()
                .append(this.getIdConf(), object.getIdConf())
                .append(this.getDivisa(), object.getDivisa())
                .isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdConf()).append(getDivisa()).toHashCode();
    }

    /**
     * El numero de deal (id_deal) + el folio del detalle del deal a reportar a Banxico.
     */
    private String idConf;
    
    /**
     * Id Divisa a la que se refiere la Operacion a Reportar. 
     */
    private String divisa;
    
}

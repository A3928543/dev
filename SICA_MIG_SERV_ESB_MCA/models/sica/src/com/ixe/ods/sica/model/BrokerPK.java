/*
 *  $Id: BrokerPK.java,v 1.12 2008/02/22 18:25:23 ccovian Exp $
 *  Ixe, Sep 8, 2003
 *  Copyright (C) 2001-2003 Grupo Financiero Ixe
 */
package com.ixe.ods.sica.model;
import com.ixe.ods.bup.model.PersonaMoral;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Llave primaria de la clase Broker.
 *
 * @author Gerardo Corzo, Javier Cordova
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:23 $
 * @see Broker
 */
public class BrokerPK implements Serializable {
			
	/**
	 * Constructor vac&iacute;o.
	 */
	public BrokerPK() {
		super();
	}
			
    /**
     * Returns the personaMoral.
     *
     * @hibernate.many-to-one column="id_persona"
     * cascade="none"
     * class="com.ixe.ods.bup.model.PersonaMoral"
     * outer-join="auto"
     * unique="false"
     * @return PersonaMoral.
     */
	public PersonaMoral getPersonaMoral() {
		return _personaMoral;
	}
	
	/**
     * Fija el valor de personaMoral.
     *
	 * @param moral El valor a asignar.
	 */
	public void setPersonaMoral(PersonaMoral moral) {
		_personaMoral = moral;
	}

    /**
     * Implements a standard way to compare instances.
     * 
     * @param other Another object.
     * @return  <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (!(other instanceof BrokerPK)) {
            return false;
        }
        BrokerPK object = (BrokerPK) other;
        return new EqualsBuilder()
                .append(this.getPersonaMoral().getIdPersona(), object.getPersonaMoral().getIdPersona())
                .isEquals();
    }
    
	/**
	 * Hibernate lo utiliza para cuestiones de herencia.
	 * 
	 * @return int El HashCode identificador del objeto.
	 */
	public int hashCode() {
		return new HashCodeBuilder().append(_personaMoral.getIdPersona()).toHashCode();
	}

    /**
     * Relaci&oacute;n muchos-a-uno con PersonaMoral.
     */
    private PersonaMoral _personaMoral;
}

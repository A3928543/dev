/*
 * $Id: PersonaDto.java,v 1.1.4.2 2010/10/08 01:31:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.teller.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1.4.2 $ $Date: 2010/10/08 01:31:04 $
 */
public class PersonaDto implements Serializable {

    /**
     * Constructor por default.
     */
    public PersonaDto() {
        super();
    }

    /**
     * Constructor que inicializa todas las variables de instancia.
     *
     * @param idPersona El n&uacute;mero de persona.
     * @param nombreCompleto El nombre de la persona.
     */
    public PersonaDto(int idPersona, String nombreCompleto) {
        this();
        this.idPersona = idPersona;
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * Regresa el valor de idPersona.
     *
     * @return int.
     */
    public int getIdPersona() {
        return idPersona;
    }

    /**
     * Establece el valor de idPersona.
     *
     * @param idPersona El valor a asignar.
     */
    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    /**
     * Regresa el valor de nombreCompleto.
     *
     * @return String.
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Establece el valor de nombreCompleto.
     *
     * @param nombreCompleto El valor a asignar.
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * Regresa una cadena con el contenido de las variables de instancia.'
     *
     * @return String.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer(getClass().getName()).append(" idPersona: ").
                append(idPersona).append(", nombreCompleto: ").append(nombreCompleto);
        
        return sb.toString();
    }
    
    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
    	if (!(other instanceof PersonaDto)) {
            return false;
        }
    	PersonaDto castOther = (PersonaDto) other;
    	return new EqualsBuilder().append(this.getIdPersona(), 
    			castOther.getIdPersona()).isEquals();
    }
    
    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
    	return new HashCodeBuilder().append(getIdPersona()).toHashCode();
    }

    /**
     * El n&uacute;mero de persona.
     */
    private int idPersona;

    /**
     * El nombre completo de la persona.
     */
    private String nombreCompleto;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 1801364281893245030L;
}

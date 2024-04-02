/*
 * $Id: AutMedioContacto.java,v 1.2 2009/08/03 22:34:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.model.IUsuario;

/**
 * Clase persistente para la tabla SC_AUT_MEDIO_CONTACTO.
 *
 * @hibernate.class table="SC_AUT_MEDIO_CONTACTO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.AutMedioContacto"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2009/08/03 22:34:33 $
 */
public class AutMedioContacto implements Serializable {

    /**
     * Constructor por default.
     */
    public AutMedioContacto() {
        super();
    }

    /**
     * Regresa el valor de idAutMedioContacto.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_AUT_MEDIO_CONTACTO"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_AUT_MEDIO_CONTACTO_SEQ"
     * @return int.
     */
    public int getIdAutMedioContacto() {
        return idAutMedioContacto;
    }

    /**
     * Establece el valor de idAutMedioContacto.
     *
     * @param idAutMedioContacto El valor a asignar.
     */
    public void setIdAutMedioContacto(int idAutMedioContacto) {
        this.idAutMedioContacto = idAutMedioContacto;
    }

    /**
     * Regresa el valor de email.
     *
     * @hibernate.property column="EMAIL"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el valor de email.
     *
     * @param email El valor a asignar.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Regresa el valor de fechaCreacion.
     *
     * @hibernate.property column="FECHA_CREACION"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece el valor de fechaCreacion.
     *
     * @param fechaCreacion El valor a asignar.
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Regresa el valor de fechaTerminacion.
     *
     * @hibernate.property column="FECHA_TERMINACION"
     * not-null="false"
     * unique="false"
     * @return Date.
     */
    public Date getFechaTerminacion() {
        return fechaTerminacion;
    }

    /**
     * Establece el valor de fechaTerminacion.
     *
     * @param fechaTerminacion El valor a asignar.
     */
    public void setFechaTerminacion(Date fechaTerminacion) {
        this.fechaTerminacion = fechaTerminacion;
    }

    /**
     * Regresa el valor de status.
     *
     * @hibernate.property column="STATUS"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Establece el valor de status.
     *
     * @param status El valor a asignar.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Regresa el valor de persona.
     *
     * @hibernate.many-to-one column="ID_PERSONA"
     * cascade="none"
     * class="com.ixe.ods.bup.model.Persona"
     * outer-join="auto"
     * unique="false"
     * @return Persona.
     */
    public Persona getPersona() {
        return persona;
    }

    /**
     * Establece el valor de persona.
     *
     * @param persona El valor a asignar.
     */
    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    /**
     * Regresa el valor de promotor.
     *
     * @hibernate.many-to-one column="ID_PROMOTOR"
     * cascade="none"
     * class="com.ixe.ods.bup.model.EmpleadoIxe"
     * outer-join="auto"
     * unique="false"
     * @return EmpleadoIxe.
     */
    public EmpleadoIxe getPromotor() {
        return promotor;
    }

    /**
     * Establece el valor de promotor.
     *
     * @param promotor El valor a asignar.
     */
    public void setPromotor(EmpleadoIxe promotor) {
        this.promotor = promotor;
    }

    /**
     * Regresa el valor de usuario.
     *
     * @hibernate.many-to-one column="ID_USUARIO"
     * cascade="none"
     * class="com.ixe.ods.seguridad.model.Usuario"
     * outer-join="auto"
     * unique="false"
     * @return IUsuario.
     */
    public IUsuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el valor de usuario.
     *
     * @param usuario El valor a asignar.
     */
    public void setUsuario(IUsuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof AutMedioContacto)) {
            return false;
        }
        AutMedioContacto castOther = (AutMedioContacto) other;
        return new EqualsBuilder().append(this.getIdAutMedioContacto(),
                castOther.getIdAutMedioContacto()).isEquals();
    }

    /**
     * Hibernate lo utiliza para cuestiones de herencia.
     * Regresa el hashCode del Objeto.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code> El HashCode identificador del objeto.
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdAutMedioContacto()).toHashCode();
    }

    /**
     * La llave primaria del registro.
     */
    private int idAutMedioContacto;

    /**
     * El correo electr&oacute;nico que se debe autorizar.
     */
    private String email;

    /**
     * La fecha de creaci&oacute;n del registro.
     */
    private Date fechaCreacion = new Date();

    /**
     * La fecha de autorizaci&oacute;n o no autorizaci&oacute;n.
     */
    private Date fechaTerminacion;

    /**
     * El status: 'Pendiente', 'Autorizado', 'NoAutorizado'.
     */
    private String status = STATUS_PENDIENTE;

    /**
     * La persona a la cual se desea agregar el medio de contacto.
     */
    private Persona persona;

    /**
     * El promotor que solicita el registro del medio de contacto.
     */
    private EmpleadoIxe promotor;

    /**
     * El usuario que da o no la autorizaci&oacute;n del alta de medio de contacto.
     */
    private IUsuario usuario;

    /**
     * Constante para identificar el status de Pendiente.
     */
    public static final String STATUS_PENDIENTE = "Pendiente";

    /**
     * Constante para identificar el status de NoAutorizado.
     */
    public static final String STATUS_NO_AUTORIZADO = "NoAutorizado";

    /**
     * Constante para identificar el status de Autorizado.
     */
    public static final String STATUS_AUTORIZADO = "Autorizado";    
}

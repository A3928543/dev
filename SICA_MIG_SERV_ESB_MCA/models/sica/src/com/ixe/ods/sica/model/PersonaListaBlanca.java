/*
 * $Id: PersonaListaBlanca.java,v 1.1.2.1 2010/10/08 01:30:18 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase persistente para la tabla SC_LB_PERSONA.
 *
 * @hibernate.class table="SC_LB_PERSONA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.PersonaListaBlanca"
 * dynamic-update="true"
 *
 * @author lvillegas
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:30:18 $
 */
public class PersonaListaBlanca implements Serializable {
    /**
     * Constructor vac&iacute;o.
     */
    public PersonaListaBlanca() {
        super();
    }
    
    /**
     * Constructor que inicializa todas las variables de instancia.
     *
     * @param noCuenta n&uacute;mero de contrato sica.
     * @param tipoExcepcion tipo de excepci&oacute;n a utilizarse.
     * @param limiteDiario L&iacute;mite diario de la regla.
     * @param limiteMensual L&iacute;mite mensual de la regla.
     * @param observaciones Observaciones asociadas a la regla.
     * @param fechaCreacion fecha de creaci&oacute;n.
     * @param fechaUltimaModificacion fecha de &uacute;ltima modificaci&oacute;n.
     * @param usuarioModificacion Id del usuario que realiz&oacute;
     *  la &uacute;ltima modificaci&oacute;n.
     */
    public PersonaListaBlanca(String noCuenta, String tipoExcepcion,
            Double limiteDiario, Double limiteMensual, String observaciones,
            Date fechaCreacion, Date fechaUltimaModificacion,
            Integer usuarioModificacion) {
        this();
        this.noCuenta = noCuenta;
        this.tipoExcepcion = tipoExcepcion;
        this.limiteDiario = limiteDiario;
        this.limiteMensual = limiteMensual;
        this.observaciones = observaciones;
        this.fechaCreacion = fechaCreacion;
        this.fechaUltimaModificacion = fechaUltimaModificacion;
        this.usuarioModificacion = usuarioModificacion;
    }
    
    /**
     * Constructor que inicializa todas las variables de instancia.
     *
     * @param noCuenta n&uacute;mero de contrato sica.
     * @param tipoExcepcion tipo de excepci&oacute;n a utilizarse.
     * @param limiteDiario L&iacute;mite diario de la regla.
     * @param limiteMensual L&iacute;mite mensual de la regla.
     * @param observaciones Observaciones asociadas a la regla.
     * @param fechaCreacion fecha de creaci&oacute;n.
     * @param fechaUltimaModificacion fecha de &uacute;ltima modificaci&oacute;n.
     * @param usuarioModificacion Id del usuario que realiz&oacute;
     *  la &uacute;ltima modificaci&oacute;n.
	 * @param nombreCorto nombre corto de la cuenta.
     */
    public PersonaListaBlanca(String noCuenta, String tipoExcepcion,
            Double limiteDiario, Double limiteMensual, String observaciones,
            Date fechaCreacion, Date fechaUltimaModificacion,
            Integer usuarioModificacion, String nombreCorto) {
        this(noCuenta, tipoExcepcion,
            limiteDiario, limiteMensual, observaciones,
            fechaCreacion, fechaUltimaModificacion,
            usuarioModificacion);
        this.nombreCorto = nombreCorto;
    }
    
    /**
     * Regresa el valor de noCuenta.
     *
     * @return String.
     * @hibernate.id generator-class="assigned"
     * column="NO_CUENTA"
     * unsaved-value="null"
     */
    public String getNoCuenta() {
		return noCuenta;
	}

    /**
     * Fija el valor de noCuenta.
     *
     * @param noCuenta El valor a asignar.
     */
	public void setNoCuenta(String noCuenta) {
		this.noCuenta = noCuenta;
	}
    
    /**
     * Regresa el valor de tipoExcepcion.
     *
     * @hibernate.property column="TIPO_EXCEPCION"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoExcepcion() {
        return tipoExcepcion;
    }

    /**
     * Fija el valor de tipoExcepcion.
     *
     * @param tipoExcepcion El valor a asignar.
     */
    public void setTipoExcepcion(String tipoExcepcion) {
        this.tipoExcepcion = tipoExcepcion;
    }

	/**
     * Regresa el valor del limiteDiario.
     *
     * @hibernate.property column="LIMITE_DIARIO"
     * not-null="true"
     * unique="false"
     * @return Double.
     */
	public Double getLimiteDiario() {
		return limiteDiario;
	}

	/**
	 * Fija el valor de limiteDiario.
	 * 
	 * @param limiteDiario El valor a asignar.
	 */
	public void setLimiteDiario(Double limiteDiario) {
		this.limiteDiario = limiteDiario;
	}

    /**
     * Regresa el valor del limiteMensual.
     *
     * @hibernate.property column="LIMITE_MENSUAL"
     * not-null="true"
     * unique="false"
     * @return Double.
     */
    public Double getLimiteMensual() {
        return limiteMensual;
    }

    /**
     * Fija el valor de limiteMensual.
     * 
     * @param limiteMensual El valor a asignar.
     */
    public void setLimiteMensual(Double limiteMensual) {
        this.limiteMensual = limiteMensual;
    }

    /**
     * Regresa el valor de observaciones.
     *
     * @hibernate.property column="OBSERVACIONES"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Fija el valor de observaciones.
     * 
     * @param observaciones El valor a asignar.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
     * Fija el valor de fechaCreacion.
     * 
     * @param fechaCreacion El valor a asignar.
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Regresa el valor de fechaUltimaModificacion.
     *
     * @hibernate.property column="FECHA_ULTIMA_MOD"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    /**
     * Fija el valor de fechaUltimaModificacion.
     * 
     * @param fechaUltimaModificacion El valor a asignar.
     */
    public void setFechaUltimaModificacion(Date fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }
    
    /**
     * Regresa el valor de usuarioModificacion.
     *
     * @hibernate.property column="ID_USUARIO_MOD"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getUsuarioModificacion() {
        return usuarioModificacion;
    }

    /**
     * Fija el valor de usuarioModificacion.
     * 
     * @param usuarioModificacion El valor a asignar.
     */
    public void setUsuarioModificacion(Integer usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    /**
     * Regresa el valor de nombreCorto.
     *
     * @return String.
     */
    public String getNombreCorto() {
        return nombreCorto;
    }
    
    /**
     * Fija el valor de nombreCorto.
     * 
     * @param nombreCorto El valor a asignar.
     */
    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }
    
	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof PersonaListaBlanca)) {
            return false;
        }
        PersonaListaBlanca castOther = (PersonaListaBlanca) other;
        return new EqualsBuilder().append(this.getNoCuenta(),
                castOther.getNoCuenta()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @return <code>int</code>
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getNoCuenta().hashCode();
    }

    /**
     * Regresa una cadena con la descripci&oacute;n de los campos de esta instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("noCuenta", noCuenta)
                .append("tipoExcepcion", tipoExcepcion)
                .append("limiteDiario", limiteDiario)
                .append("limiteMensual", limiteMensual)
                .append("observaciones", observaciones)
                .append("fechaCreacion", fechaCreacion)
                .append("fechaUltimaModificacion", fechaUltimaModificacion)
                .append("usuarioModificacion", usuarioModificacion)
                .toString();
    }

	/**
     * N&uacute;mero de contrato SICA.
     */
    private String noCuenta;
    
    /**
     * Tipo de excepci&oacute;n que se va a utilizar.
     */
    private String tipoExcepcion;
    
    /**
     * L&iacute;mite diario.
     */
    private Double limiteDiario;
    
    /**
     * L&iacute;mite mensual.
     */
    private Double limiteMensual;
    
    /**
     * Observaciones.
     */
    private String observaciones;
    
    /**
     * Fecha de creaci&oacute;n del registro.
     */
    private Date fechaCreacion;
    
    /**
     * Fecha de modificaci&oacute;n del registro.
     */
    private Date fechaUltimaModificacion;
    
    /**
     * Usuario que hizo la &uacute;ltima modificaci&oacute;n. 
     */
    private Integer usuarioModificacion;
    
    /**
     * Raz&oacute;n social perteneciente al contrato.
     */
    private String nombreCorto;
    
    /**
     * Serializador.
     */
    private static final long serialVersionUID = 3967887960822630780L;

    /**
     * Constante de sin excepci&oacute;n.
     */
    public static final String SIN_EXCEPCION = "SE";

    /**
     * Constante de excepci&oacute;n de gobierno.
     */
    public static final String EXCEPCION_SHCP = "SHCP";
    
    /**
     * Constante de exepci&oacute;n de Ixe.
     */
    public static final String EXCEPCION_IXE = "IXE";
}

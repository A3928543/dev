/*
 * $Id: LimiteEfectivo.java,v 1.1.2.1 2010/10/08 01:30:18 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.ixe.ods.seguridad.model.IUsuario;

/**
 * Clase persistente para la tabla SC_LIMITE_EFECTIVO.
 *
 * @hibernate.class table="SC_LIMITE_EFECTIVO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.LimiteEfectivo"
 * dynamic-update="true"
 *
 * @author lvillegas
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:30:18 $
 */
public class LimiteEfectivo implements Serializable {
    /**
     * Constructor vac&iacute;o.
     */
    public LimiteEfectivo() {
        super();
    }
    
    /**
     * Constructor que inicializa todas las variables de instancia.
     *
     * @param divisa Divisa a la cual esta asociada a la regla.
     * @param compra Determina si es una compra asociada a al regla.
     * @param tipoPersona El tipo de persona asociado a la regla.
     * @param cliente Determina si es un cliente el que est&aacute; asociado a la regla.
     * @param tipoZona El tipo de zona asociado a la regla.
     * @param mexicano Determina si es un mexicano el que est&aacute; asociado a la regla.
     * @param limiteDiario L&iacute;mite diario de la regla.
     * @param limiteMensual L&iacute;mite mensual de la regla.
     * @param observaciones Observaciones asociadas a la regla.
     * @param fechaCreacion fecha de creaci&oacute;n.
     * @param fechaUltimaModificacion fecha de &uacute;ltima modificaci&oacute;n.
     * @param usuarioModificacion con el id del usuario que modifica.
     */
    public LimiteEfectivo(String divisa, Boolean compra, String tipoPersona, 
            Boolean cliente, String tipoZona, Boolean mexicano, 
            Double limiteDiario, Double limiteMensual, String observaciones,
            Date fechaCreacion, Date fechaUltimaModificacion, IUsuario usuarioModificacion) {
        this();
        this.divisa = divisa;
        this.compra = compra;
        this.tipoPersona = tipoPersona;
        this.cliente = cliente;
        this.tipoZona = tipoZona;
        this.mexicano = mexicano;
        this.limiteDiario = limiteDiario;
        this.limiteMensual = limiteMensual;
        this.observaciones = observaciones;
        this.fechaCreacion = fechaCreacion;
        this.fechaUltimaModificacion = fechaUltimaModificacion;
        this.usuarioModificacion = new Integer(usuarioModificacion.getIdUsuario());
    }
    
    
    
    /**
     * Regresa el valor de idLimiteEfectivo.
     *
     * @return String.
     * @hibernate.id generator-class="uuid.hex"
     * column="ID_LIMITE_EFECTIVO"
     * unsaved-value="null"
     */
    public String getIdLimiteEfectivo() {
		return idLimiteEfectivo;
	}

    /**
     * Fija el valor de idLimiteEfectivo.
     *
     * @param idLimiteEfectivo El valor a asignar.
     */
	public void setIdLimiteEfectivo(String idLimiteEfectivo) {
		this.idLimiteEfectivo = idLimiteEfectivo;
	}
    
    /**
     * Regresa el valor de compra.
     *
     * @hibernate.property column="COMPRA"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return Boolean.
     */
    public Boolean getCompra() {
        return compra;
    }

    /**
     * Fija el valor de compra.
     *
     * @param compra El valor a asignar.
     */
    public void setCompra(Boolean compra) {
        this.compra = compra;
    }
    
	/**
     * Regresa el valor de divisa.
     *
     * @hibernate.property column="DIVISA"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
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
     * Regresa el valor de tipoPersona.
     *
     * @hibernate.property column="TIPO_PERSONA"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return String.
     */
	public String getTipoPersona() {
		return tipoPersona;
	}

	/**
     * Fija el valor de tipoPersona.
     *
     * @param tipoPersona El valor a asignar.
     */
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	/**
     * Regresa el valor de cliente.
     *
     * @hibernate.property column="CLIENTE"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return Boolean.
     */
	public Boolean getCliente() {
		return cliente;
	}

	/**
     * Fija el valor de cliente.
     *
     * @param cliente El valor a asignar.
     */
	public void setCliente(Boolean cliente) {
		this.cliente = cliente;
	}
	
	/**
     * Regresa el valor de tipoZona.
     *
     * @hibernate.property column="TIPO_ZONA"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return String.
     */
	public String getTipoZona() {
		return tipoZona;
	}

	/**
	 * Fija el valor de tipoZona.
	 * 
	 * @param tipoZona El valor a asignar.
	 */
	public void setTipoZona(String tipoZona) {
		this.tipoZona = tipoZona;
	}

	/**
     * Regresa el valor del mexicano.
     *
     * @hibernate.property column="MEXICANO"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return Boolean.
     */
	public Boolean getMexicano() {
		return mexicano;
	}

	/**
     * Fija el valor del mexicano.
     *
     * @param mexicano El valor a asignar.
     */
	public void setMexicano(Boolean mexicano) {
		this.mexicano = mexicano;
	}

	/**
     * Regresa el valor del limiteDiario.
     *
     * @hibernate.property column="LIMITE_DIARIO"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
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
     * update="true"
     * insert="true"
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
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
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
     * insert="true"
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
     * update="true"
     * insert="true"
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
     * update="true"
     * insert="true"
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
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof LimiteEfectivo)) {
            return false;
        }
        LimiteEfectivo castOther = (LimiteEfectivo) other;
        return new EqualsBuilder().append(this.getIdLimiteEfectivo(),
                castOther.getIdLimiteEfectivo()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @return <code>int</code>
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getIdLimiteEfectivo().hashCode();
    }

    /**
     * Regresa una cadena con la descripci&oacute;n de los campos de esta instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("idLimiteEfectivo", idLimiteEfectivo)
                .append("compra", compra)
                .append("divisa", divisa)
                .append("tipoPersona", tipoPersona)
                .append("cliente", cliente)
                .append("tipoZona", tipoZona)
                .append("mexicano", mexicano)
                .append("limiteDiario", limiteDiario)
                .append("limiteMensual", limiteMensual)
                .append("observaciones", observaciones)
                .append("fechaCreacion", fechaCreacion)
                .append("fechaUltimaModificacion", fechaUltimaModificacion)
                .append("usuarioModificacion", usuarioModificacion)
                .toString();
    }
    
    public boolean valuesEquals(Object other) {
        if (!(other instanceof LimiteEfectivo)) {
            return false;
        }
        boolean eq = true;
        LimiteEfectivo castOther = (LimiteEfectivo) other;
        if (!(getCompra() == null && castOther.getCompra() == null) &&
            !(getCompra() != null && castOther.getCompra() != null &&
                getCompra().equals(castOther.getCompra()))) {
            eq = false;
        }
        else if (!(getDivisa() == null && castOther.getDivisa() == null) &&
            !(getDivisa() != null && castOther.getDivisa() != null &&
                getDivisa().equals(castOther.getDivisa()))) {
            eq = false;
        }
        else if (!(getTipoPersona() == null && castOther.getTipoPersona() == null) &&
            !(getTipoPersona() != null && castOther.getTipoPersona() != null &&
                    getTipoPersona().equals(castOther.getTipoPersona()))) {
            eq = false;
        }
        else if (!(getCliente() == null && castOther.getCliente() == null) &&
            !(getCliente() != null && castOther.getCliente() != null &&
                    getCliente().equals(castOther.getCliente()))) {
            eq = false;
        }
        else if (!(getTipoZona() == null && castOther.getTipoZona() == null) &&
            !(getTipoZona() != null && castOther.getTipoZona() != null &&
                    getTipoZona().equals(castOther.getTipoZona()))) {
            eq = false;
        }
        else if (!(getMexicano() == null && castOther.getMexicano() == null) &&
            !(getMexicano() != null && castOther.getMexicano() != null &&
                    getMexicano().equals(castOther.getMexicano()))) {
            eq = false;
        }
        return eq;
    }

	/**
     * La llave primaria del registro.
     */
    private String idLimiteEfectivo;
    
    /**
     * Divisa a la cual esta asociada una regla.
     */
    private String divisa;
    
    /**
     * Tipo de persona.
     */
    private String tipoPersona;
    
    /**
     * Determina si la regla aplica para cliente o no.
     */
    private Boolean cliente;
    
    /**
     * Tipo de zona.
     */
    private String tipoZona;
    
    /**
     * Determina si la regla aplica para mexicano o no.
     */
    private Boolean mexicano;
    
   /**
     * L&iacute;mite diario.
     */
    private Double limiteDiario;
    
    /**
     * L&iacute;mite mensual.
     */
    private Double limiteMensual;
    
    /**
     * Determina si se trata de una compra.
     */
    private Boolean compra;
    
    /**
     * Observaciones del registro.
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
     * Serializador.
     */
    private static final long serialVersionUID = 3967887960822630780L;
}

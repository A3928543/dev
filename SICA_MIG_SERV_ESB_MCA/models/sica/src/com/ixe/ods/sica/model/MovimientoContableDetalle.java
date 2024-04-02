/*
 * $Id: MovimientoContableDetalle.java,v 1.15 2008/02/22 18:25:24 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_MOVIMIENTO_CONT_CONTABLE.
 *
 * @hibernate.class table="SC_MOVIMIENTO_CONT_DETALLE"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.MovimientoContableDetalle"
 * dynamic-update="true"
 * 
 * @author Edgar Leija, Javier Cordova
 * @version  $Revision: 1.15 $ $Date: 2008/02/22 18:25:24 $
 */

public class MovimientoContableDetalle implements Serializable{

	/**
	 * Constructor por default
	 */
	public MovimientoContableDetalle(){
		super();
	}
	
	/**
     * Regresa el valor de idPoliza.
     *
     * @hibernate.id generator-class="uuid.hex"
     * column="ID_MOVIMIENTO_CONT_DETALLE"
     * unsaved-value="null"
     * @return String.
     */
    public String getIdMovimientoContableDetalle() {
        return idMovimientoContableDetalle;
    }
    
    /**
     * Fija el valor de Movimiento Contable Detalle
     *
     * @param
     */
    public void setIdMovimientoContableDetalle(String idMovimientoContableDetalle) {
    	this.idMovimientoContableDetalle = idMovimientoContableDetalle;
    }
    
    /**
     * Regresa el valor de movimiento contable.
     *
     * @hibernate.many-to-one column="ID_MOVIMIENTO_CONTABLE"
     * cascade="none"
     * class="com.ixe.ods.sica.model.MovimientoContable"
     * outer-join="auto"
     * unique="false"
     * @return MovimientoContable.
     */
    public MovimientoContable getMovimientoContable() {
        return _movimientoContable;
    }

    /**
     * Fija el valor de movimientoContable.
     *
     * @param movimientoContable El valor a asignar.
     */
    public void setMovimientoContable(MovimientoContable movimientoContable) {
        _movimientoContable = movimientoContable;
    }
    
    /**
	 * Regresa el valor de tipoDetalle
	 * 
     * @hibernate.property column="TIPO_DETALLE"
     * not-null="true"
     * unique="false"
     * @return String.
	 */
	public String getTipoDetalle() {
		return tipoDetalle;
	}
	
	/**
	 * Fija el valor de tipoDetalle
	 * 
	 * @param tipoDetalle El valor a tiopDetalle
	 */
	public void setTipoDetalle(String tipoDetalle) {
		this.tipoDetalle = tipoDetalle;
	}
	
	/**
     * Regresa el valor de divisa.
     *
     * @hibernate.many-to-one column="ID_DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return Divisa.
     */
    public Divisa getDivisa() {
        return _divisa;
    }

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        _divisa = divisa;
    }
    
    /**
     * Regresa el valor de monto
     *
     * @hibernate.property column="MONTO"
     * not-null="true"
     * unique="false"
     * @return double
     */
	public double getMonto() {
		return monto;
	}
	
	/**
	 * Fijar el valor de monto
	 * 
	 * @param monto Valor a asignar
	 */
	public void setMonto(double monto) {
		this.monto = monto;
	}
	
	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals (java.lang.Object) .
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof MovimientoContableDetalle)) {
            return false;
        }
        MovimientoContableDetalle castOther = (MovimientoContableDetalle) other;
        return new EqualsBuilder().append(this.getIdMovimientoContableDetalle(), castOther.
                getIdMovimientoContableDetalle()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdMovimientoContableDetalle()).toHashCode();
    }
    
    /**
     * El identificador del registro.
     */
    private String idMovimientoContableDetalle;
    
    /**
     * Relaci&oacute;n muchos-a-uno con MovimientoContable.
     */
    private MovimientoContable _movimientoContable;
    
    /**
     * Relaci&oacute;n muchos-a-uno con Divisa.
     */
    private Divisa _divisa;
    
    /**
     * El monto en divisa, pesos, de utilidades, de p&eacute;rdidas, de comisi&oacute;n, de iva de
     * comisi&oacute;n o de liquidaci&oacute;n (monto en pesos + comisi&oacute;n + iva de
     * comisi&oacute;n) del movimiento contable. Hay un registro de detalle por cada rubro.
     */
    private double monto;
            
    /**
     *  El tipo de detalle del movimiento contable: Importe en Divisa, Importe en Pesos,
     * Comisi&oacute;n, IVA de Comisi&oacute;n, Utilidades, P&eacute;rdidas, Importe
     * Liquidaci&oacute;n.
     */
    private String tipoDetalle;
}

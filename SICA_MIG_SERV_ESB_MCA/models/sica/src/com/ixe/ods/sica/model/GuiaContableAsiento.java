/*
 * $Id: GuiaContableAsiento.java,v 1.14 2009/12/22 18:02:52 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_GUIA_CONTABLE_ASIENTO.
 *
 * @hibernate.class table="SC_GUIA_CONTABLE_ASIENTO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.GuiaContableAsiento"
 * dynamic-update="true"
 *
 * @author Edgar Leija, Javier Cordova
 * @version  $Revision: 1.14 $ $Date: 2009/12/22 18:02:52 $
 */
public class GuiaContableAsiento implements Serializable {

    /**
	 * Constructor por default.
	 */
	public GuiaContableAsiento(){
		super();
	}

	/**
     * Regresa el valor de idGuiaContableAsiento.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_GUIA_CONTABLE_ASIENTO"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_GUIA_CONTABLE_ASIENTO_SEQ"
     * @return int.
     */
    public int getIdGuiaContableAsiento() {
        return idGuiaContableAsiento;
    }

    /**
     * Fija el valor de idGuiaContableAsiento.
     *
     * @param idGuiaContableAsiento El valor a asignar.
     */
    public void setIdGuiaContableAsiento(int idGuiaContableAsiento) {
        this.idGuiaContableAsiento = idGuiaContableAsiento;
    }

    /**
	 * Regresa el valor de Tipo	Operacion
	 *
     * @hibernate.property column="TIPO_OPERACION"
     * not-null="true"
     * unique="false"
     * @return String.
	 */
	public String getTipoOperacion() {
		return tipoOperacion;
	}

	/**
	 * Fija el valor de TipoOperacion
	 *
	 * @param tipoOperacion El valor a TipoOperacion
	 */
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	/**
	 * Regresa el valor de fase
	 *
     * @hibernate.property column="FASE_CONTABLE"
     * not-null="true"
     * unique="false"
     * @return String.
	 */
	public String getFaseContable() {
		return faseContable;
	}

	/**
	 * Fija el valor de faseContable
	 *
	 * @param faseContable El valor a asignar
	 */
	public void setFaseContable(String faseContable) {
		this.faseContable = faseContable;
	}

	/**
     * Regresa el valor de cargoabono.
     *
     * @hibernate.property column="CARGO_ABONO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getCargoAbono() {
        return cargoabono;
    }

    /**
     * Fija el valor de cargoabono.
     *
     * @param cargoabono El cargoabono a asignar.
     */
    public void setCargoAbono(String cargoabono) {
        this.cargoabono = cargoabono;
    }

    /**
     * Regresa el valor de cuentaContable.
     *
     * @hibernate.property column="CUENTA_CONTABLE"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getCuentaContable() {
        return cuentaContable;
    }

    /**
     * Fija el valor de cuentaContable.
     *
     * @param cuentaContable La cuentaContable a asignar.
     */
    public void setCuentaContable(String cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    /**
     * Regresa el valor de entidad.
     *
     * @hibernate.property column="ENTIDAD"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getEntidad() {
        return entidad;
    }

    /**
     * Fija el valor de entidad.
     *
     * @param entidad La entidad a asignar.
     */
    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    /**
     * Regresa el valor de moneda.
     *
     * @hibernate.property column="MONEDA"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * Fija el valor de moneda.
     *
     * @param moneda La moneda a asignar.
     */
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    /**
     * Regresa el valor de descripcion.
     *
     * @hibernate.property column="DESCRIPCION"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Fija el valor de descripcion.
     *
     * @param descripcion La descripcion a asignar.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Regresa el valor de tipo SAP.
     *
     * @hibernate.property column="TIPO_SAP"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoSap() {
        return tipoSap;
    }

    /**
     * Fija el valor de descripcion.
     *
     * @param tipoSap TipoSap a asignar.
     */
    public void setTipoSap(String tipoSap) {
        this.tipoSap = tipoSap;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals (java.lang.Object) .
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof GuiaContableAsiento)) {
            return false;
        }
        GuiaContableAsiento castOther = (GuiaContableAsiento) other;
        return new EqualsBuilder().append(this.getIdGuiaContableAsiento(), castOther.
                getIdGuiaContableAsiento()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdGuiaContableAsiento()).toHashCode();
    }

    /**
     * Identificador de la gu&iacute;a contable
     */
    private int idGuiaContableAsiento;

    /**
     * Describe el tipo de operaci&oacute;n, Compra o Venta.
     */
    private String tipoOperacion;

    /**
     * Describe la fase Contable a la que pertenece el registro.
     */
    private String faseContable;

    /**
     * Describe si es un cargo o es un abono el que aplica.
     */
    private String cargoabono;

    /**
     * Describe el n&uacute;mero de la cuenta contable a donde se aplica el cargo o abono.
     */
    private String cuentaContable;

    /**
     * Describe la entidad de la cuenta contable.
     */
    private String entidad;

    /**
     * Describe la Divisa (Ejemplo: USD) o los Pesos de la Divisa (Ejemplo: MXN-USD). (Puede ser
     * tambi&eacute;n MXN o NULL).
     */
    private String moneda;

    /**
     * La descripci&oacute;n del Asiento Contable.
     */
    private String descripcion;

    /**
     * Si la guia contable es de SAP.
     */
    private String tipoSap;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 5929292594091642238L;
}

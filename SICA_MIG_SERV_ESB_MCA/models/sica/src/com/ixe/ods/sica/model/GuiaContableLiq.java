/*
 * $Id: GuiaContableLiq.java,v 1.14 2009/12/22 18:03:18 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_GUIA_CONTABLE_LIQ.
 *
 * @author Edgar Leija, Javier Cordova
 * @version $Revision: 1.14 $ $Date: 2009/12/22 18:03:18 $
 * @hibernate.class table="SC_GUIA_CONTABLE_LIQ"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.GuiaContableLiq"
 * dynamic-update="true"
 */
public class GuiaContableLiq implements Serializable {

    /**
     * Constructor por default.
     */
    public GuiaContableLiq() {
        super();
    }

    /**
     * Regresa el valor de idGuiaContableLiq.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_GUIA_CONTABLE_LIQ"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_GUIA_CONTABLE_LIQ_SEQ"
     * @return int.
     */
    public int getIdGuiaContableLiq() {
        return idGuiaContableLiq;
    }

    /**
     * Fija el valor de idMovimientoContable.
     *
     * @param idGuiaContableLiq El valor a asignar.
     */
    public void setIdGuiaContableLiq(int idGuiaContableLiq) {
        this.idGuiaContableLiq = idGuiaContableLiq;
    }

    /**
     * Regresa el valor de mnemonico.
     *
     * @hibernate.property column="MNEMONICO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getMnemonico() {
        return mnemonico;
    }

    /**
     * Fija el valor de mnemonico.
     *
     * @param mnemonico El mnemonico a asignar.
     */
    public void setMnemonico(String mnemonico) {
        this.mnemonico = mnemonico;
    }

    /**
     * Regresa el valor de cargoabono.
     *
     * @hibernate.property column="CARGO_ABONO"
     * not-null="false"
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
     * not-null="false"
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
     * not-null="false"
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
     * Regresa el valor de claveTipoLiquidacion.
     *
     * @hibernate.property column="CLAVE_TIPO_LIQUIDACION"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClaveTipoLiquidacion() {
        return claveTipoLiquidacion;
    }

    /**
     * Fija el valor de claveTipoLiquidacion.
     *
     * @param claveTipoLiquidacion La claveTipoLiquidacion a asignar.
     */
    public void setClaveTipoLiquidacion(String claveTipoLiquidacion) {
        this.claveTipoLiquidacion = claveTipoLiquidacion;
    }

    /**
     * Regresa el valor de claveFormaLiquidacion.
     *
     * @hibernate.property column="CLAVE_FORMA_LIQUIDACION"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClaveFormaLiquidacion() {
        return claveFormaLiquidacion;
    }

    /**
     * Fija el valor de claveFormaLiquidacion.
     *
     * @param claveFormaLiquidacion La claveFormaLiquidacion a asignar.
     */
    public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
        this.claveFormaLiquidacion = claveFormaLiquidacion;
    }

    /**
     * Regresa el valor de descripcion.
     *
     * @hibernate.property column="DESCRIPCION"
     * not-null="false"
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
	 * Regresa el valor de liquidacionEspecial
	 *
     * @hibernate.property column="LIQUIDACION_ESPECIAL"
     * not-null="false"
     * unique="false"
     * @return String.
	 */
	public String getLiquidacionEspecial() {
		return liquidacionEspecial;
	}

	/**
	 * Fija el valor de liquidacionEspecial
	 *
	 * @param liquidacionEspecial El valor a Fijar
	 */
	public void setLiquidacionEspecial(String liquidacionEspecial) {
		this.liquidacionEspecial = liquidacionEspecial;
	}

    /**
	 * Regresa el valor de liquidacionEspecial
	 *
     * @hibernate.property column="TIPO_SAP"
     * not-null="false"
     * unique="false"
     * @return String.
	 */
	public String getTipoSap() {
		return tipoSap;
	}

	/**
	 * @param tipoSap the tipoSap to set
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
        if (!(other instanceof GuiaContableLiq)) {
            return false;
        }
        GuiaContableLiq castOther = (GuiaContableLiq) other;
        return new EqualsBuilder().append(this.getIdGuiaContableLiq(), castOther.
                getIdGuiaContableLiq()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdGuiaContableLiq()).toHashCode();
    }

    /**
     * Identificador del id de la gu&iacute;a Contable Liquidaci&oacute;n.
     */
    private int idGuiaContableLiq;

    /**
     * Describe el mnemonico de la gu&iacute;a contable Liquidaci&oacute;n.
     */
    private String mnemonico;

    /**
     * Describe si es un cargo o Abono.
     *
     */
    private String cargoabono;

    /**
     * Describe el n&uacute;mero de la cuenta Contable a donde se aplica el cargo o Abono.
     */
    private String cuentaContable;

    /**
     * Describe el n&uacute;mero de la entidad de la cuenta contable.
     */
    private String entidad;

    /**
     * Describe la Divisa (Ejemplo: USD) o los Pesos de la Divisa (Ejemplo: MXN-USD). (Puede ser
     * tambi&eacute;n MXN o NULL).
     */
    private String moneda;

    /**
     * Describe el la clave tipo liquidaci&oacute;n.
     */
    private String claveTipoLiquidacion;

    /**
     * Describe la clave forma liquidaci&oacute;n.
     */
    private String claveFormaLiquidacion;

    /**
     * La descripci&oacute;n de la Gu&iacute;a Contable de Liquidaci&oacute;n.
     */
    private String descripcion;

    /**
     * Describe si de trata de una Liquidaci&oacute;n Especial VISA o TESORERIA.
     */
    private String liquidacionEspecial;

    /**
     * Indica si el registro es tipo sap.
     */
    private String tipoSap;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -2286783548323735071L;
}
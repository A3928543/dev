/*
 * $Id: SpreadActualPK.java,v 1.12 2008/02/22 18:25:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase de llave primaria para la clase SpreadActual.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:23 $
 */
public class SpreadActualPK implements Serializable {

    /**
     * Constructor vac&iacute;o, no hace nada.
     */
    public SpreadActualPK() {
        super();
    }

    /**
     * Constructor por default, inicializa todas las variables de instancia.
     *
     * @param claveFormaLiquidacion La clave del producto.
     * @param divisa La divisa.
     * @param tipoPizarron El tipo de pizarr&oacute;n a consultar.
     */
    public SpreadActualPK(String claveFormaLiquidacion, Divisa divisa, TipoPizarron tipoPizarron) {
        this();
        this.claveFormaLiquidacion = claveFormaLiquidacion;
        this.divisa = divisa;
        this.tipoPizarron = tipoPizarron;
    }

    /**
     * Regresa el valor de claveFormaLiquidacion.
     *
     * @hibernate.property column="CLAVE_FORMA_LIQUIDACION"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getClaveFormaLiquidacion() {
        return claveFormaLiquidacion;
    }

    /**
     * Fija el valor de claveFormaLiquidacion.
     *
     * @param claveFormaLiquidacion El valor a asignar.
     */
    public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
        this.claveFormaLiquidacion = claveFormaLiquidacion;
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
        return divisa;
    }

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
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
        if (!(other instanceof SpreadActualPK)) {
            return false;
        }
        SpreadActualPK object = (SpreadActualPK) other;
        return new EqualsBuilder()
        		.append(this.getTipoPizarron(), object.getTipoPizarron())
                .append(this.getClaveFormaLiquidacion(), object.getClaveFormaLiquidacion())
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
        return new HashCodeBuilder().append(getClaveFormaLiquidacion()).append(getDivisa()).
                append(getTipoPizarron().getIdTipoPizarron()).toHashCode();
    }

    /**
     * Obtiene el tipo de Pizarron para el spread.
     *
     * @hibernate.many-to-one column="ID_TIPO_PIZARRON"
     * cascade="none"
     * class="com.ixe.ods.sica.model.TipoPizarron"
     * outer-join="auto"
     * unique="false"
     * @return TipoPizarron
     */
    public TipoPizarron getTipoPizarron() {
		return tipoPizarron;
	}

    /**
     * Asigna el valor para IdTipoPizarron.
     *
     * @param tipoPizarron El valor para IdTipoPizarron.
     */
	public void setTipoPizarron(TipoPizarron tipoPizarron) {
		this.tipoPizarron = tipoPizarron;
	}

    /**
     * La clave del producto.
     */
    private String claveFormaLiquidacion;

    /**
     * El Tipo de Pizarron para el Spread.
     */
    private TipoPizarron tipoPizarron;

    /**
     * Relaci&oacute;n muchos-a-uno con Divisa.
     */
    private Divisa divisa;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 8989860280551793719L;
}

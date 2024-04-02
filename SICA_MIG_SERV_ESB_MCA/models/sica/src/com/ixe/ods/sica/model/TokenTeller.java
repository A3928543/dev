/*
 * $Id: TokenTeller.java,v 1.1.4.2 2010/10/08 01:30:18 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.ixe.ods.sica.teller.dto.TipoCambioDto;

/**
 * Clase persistente para la tabla SC_TOKEN_TELLER.
 *
 * @hibernate.class table="SC_TOKEN_TELLER"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.TokenTeller"
 * dynamic-update="true"
 * 
 * @author Israel Rebollar
 * @version  $Revision: 1.1.4.2 $ $Date: 2010/10/08 01:30:18 $
 */
public class TokenTeller implements Serializable {
	
	/**
	 * Constructor default, no hace nada.
	 */
	public TokenTeller() {
		super();
	}
	
	public TokenTeller(Date fechaVigencia, double minPrecioCompra, double minPrecioVenta, 
				double maxPrecioCompra, double maxPrecioVenta, int idSpread, int idFactorDivisa, 
				int idPrecioReferencia) {
		this();
		this.fechaVigencia = fechaVigencia;
		this.minPrecioCompra = minPrecioCompra;
		this.minPrecioVenta = minPrecioVenta;
		this.maxPrecioCompra = maxPrecioCompra;
		this.maxPrecioVenta = maxPrecioVenta;
		this.idSpread = idSpread;
		this.idFactorDivisa = idFactorDivisa;
		this.idPrecioReferencia = idPrecioReferencia;
	}

	
	/**
     * Regresa el valor de idTokenTeller.
     *
     * @return String.
     * @hibernate.id generator-class="uuid.hex"
     * column="ID_TOKEN_TELLER"
     * unsaved-value="null"
     */
	public String getIdTokenTeller() {
		return idTokenTeller;
	}

	/**
	 * Fija el valor de idTokenTeller
	 * 
	 * @param idTokenTeller El valor a asignar.
	 */
	public void setIdTokenTeller(String idTokenTeller) {
		this.idTokenTeller = idTokenTeller;
	}

	
	/**
     * Regresa el valor de fecha de vigencia.
     *
     * @return Date.
     * @hibernate.property column="VIGENCIA"
     * not-null="true"
     * unique="false"
     */
	public Date getFechaVigencia() {
		return fechaVigencia;
	}

	/**
	 * Fija el valor de fechaVigencia.
	 * 
	 * @param fechaVigencia El valor a asignar.
	 */
	public void setFechaVigencia(Date fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}

	/**
     * Regresa el valor de minPrecioCompra
     *
     * @return double
     * @hibernate.property column="MIN_PRECIO_COMPRA"
     * not-null="false"
     * unique="false"
     */
	public double getMinPrecioCompra() {
		return minPrecioCompra;
	}

	/**
	 * Fija el valor de minPrecioCompra
	 * 
	 * @param minPrecioCompra El valor a asignar.
	 */
	public void setMinPrecioCompra(double minPrecioCompra) {
		this.minPrecioCompra = minPrecioCompra;
	}

	/**
     * Regresa el valor de minPrecioVenta
     *
     * @return double
     * @hibernate.property column="MIN_PRECIO_VENTA"
     * not-null="false"
     * unique="false"
     */
	public double getMinPrecioVenta() {
		return minPrecioVenta;
	}

	/**
	 * Fija el valor de minPrecioVenta
	 * 
	 * @param minPrecioVenta El valor a asignar.
	 */
	public void setMinPrecioVenta(double minPrecioVenta) {
		this.minPrecioVenta = minPrecioVenta;
	}

	/**
     * Regresa el valor de maxPrecioCompra
     *
     * @return double
     * @hibernate.property column="MAX_PRECIO_COMPRA"
     * not-null="false"
     * unique="false"
     */
	public double getMaxPrecioCompra() {
		return maxPrecioCompra;
	}

	/**
	 * Fija el valor de maxPrecioCompra
	 * 
	 * @param maxPrecioCompra El valor a asignar.
	 */
	public void setMaxPrecioCompra(double maxPrecioCompra) {
		this.maxPrecioCompra = maxPrecioCompra;
	}

	/**
     * Regresa el valor de maxPrecioVenta
     *
     * @return double
     * @hibernate.property column="MAX_PRECIO_VENTA"
     * not-null="false"
     * unique="false"
     */
	public double getMaxPrecioVenta() {
		return maxPrecioVenta;
	}

	/**
	 * Fija el valor de maxPrecioVenta
	 * 
	 * @param maxPrecioVenta El valor a asignar.
	 */
	public void setMaxPrecioVenta(double maxPrecioVenta) {
		this.maxPrecioVenta = maxPrecioVenta;
	}

	/**
     * Regresa el valor de idSpread
     *
     * @return int.
     * @hibernate.property column="ID_SPREAD"
     * not-null="true"
     * unique="false"
     */
	public int getIdSpread() {
		return idSpread;
	}

	/**
	 * @param idSpread the idSpread to set
	 */
	public void setIdSpread(int idSpread) {
		this.idSpread = idSpread;
	}

	/**
     * Regresa el valor de idFactorDivisa
     *
     * @return int.
     * @hibernate.property column="ID_FACTOR_DIVISA"
     * not-null="true"
     * unique="false"
     */
	public int getIdFactorDivisa() {
		return idFactorDivisa;
	}

	/**
	 * @param idFactorDivisa the idFactorDivisa to set
	 */
	public void setIdFactorDivisa(int idFactorDivisa) {
		this.idFactorDivisa = idFactorDivisa;
	}

	/**
     * Regresa el valor de idPrecioReferencia
     *
     * @return int.
     * @hibernate.property column="ID_PRECIO_REFERENCIA"
     * not-null="true"
     * unique="false"
     */
	public int getIdPrecioReferencia() {
		return idPrecioReferencia;
	}

	/**
	 * @param idPrecioReferencia the idPrecioReferencia to set
	 */
	public void setIdPrecioReferencia(int idPrecioReferencia) {
		this.idPrecioReferencia = idPrecioReferencia;
	}

	/**
	 * Regresa el valor de idPrecioReferencia
	 * 
	 * @return boolean
	 */
	public boolean isVigente() {
		Date fechaActual = new Date();
		if (getFechaVigencia() != null) {
			return fechaActual.before(getFechaVigencia());
		}
		return false;
	}
	
	/**
     * Regresa una cadena con los nombres y valores de las variables de instancia.
     *
     * @return String.
     */
	public String toString() {
		return new ToStringBuilder(this).append("idTokenTeller", idTokenTeller).
        append("fechaVigencia", fechaVigencia).append("minPrecioCompra", minPrecioCompra).
        append("minPrecioVenta", minPrecioVenta).append("maxPrecioCompra", maxPrecioCompra).
        append("maxPrecioVenta", maxPrecioVenta).append("idSpread", idSpread).
        append("idFactorDivisa", idFactorDivisa).append("idPrecioReferencia", idPrecioReferencia).
        	toString();
	}
	
	/**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
    	return new HashCodeBuilder().append(idTokenTeller).toHashCode();
    }
    
    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
    	if (!(other instanceof TokenTeller)) {
            return false;
        }
    	TokenTeller castOther = (TokenTeller) other;
    	return new EqualsBuilder().append(getIdTokenTeller(), castOther.getIdTokenTeller()).
    		isEquals();
    }

	/**
	 * El identificador del registro.
	 */
	private String idTokenTeller;
	
	/**
	 * El valor que indica la vigencia del token.
	 */
	private Date fechaVigencia;
	
	/**
	 * El valor del precio m&iacute;nimo de compra. 
	 */
	private double minPrecioCompra;
	
	/**
	 * El valor del precio m&iacute;nimo de venta.
	 */
	private double minPrecioVenta;
	
	/**
	 * El valor del precio m&aacute;ximo de compra.
	 */
	private double maxPrecioCompra;
	
	/**
	 * El valor del precio m&aacute;ximo de venta.
	 */
	private double maxPrecioVenta;
	
	/**
	 * El identificador del Spread.
	 */
	private int idSpread;
	
	/**
	 * El identificador del factot divisa.
	 */
	private int idFactorDivisa;
	
	/**
	 * El identificador del precio de referencia.
	 */
	private int idPrecioReferencia;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1897879120092L;

}

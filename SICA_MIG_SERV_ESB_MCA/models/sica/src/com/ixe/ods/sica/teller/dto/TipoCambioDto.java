/*
 * $Id: TipoCambioDto.java,v 1.1.4.2 2010/10/08 01:31:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */


package com.ixe.ods.sica.teller.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Data Transfer Object utilizado en la consulta de Tipo de Cambio.
 *
 * @author Israel Rebollar
 * @version $Revision: 1.1.4.2 $ $Date: 2010/10/08 01:31:04 $
 */
public class TipoCambioDto implements Serializable {
	
	/**
	 * Contructor default, no hace nada.
	 */
	public TipoCambioDto() {
		super();
	}
	
	/**
	 * Inicializa el objetos obteniendo las valores de un mapa.
	 * 
	 * @param mapa Mapa que contiene los valores para el objeto.
	 */
	public TipoCambioDto(Map mapa){
		this();
		this.minimoCompra = (Double) mapa.get("minimoCompra");
		this.maximoCompra = (Double) mapa.get("maximoCompra");
		this.minimoVenta = (Double) mapa.get("minimoVenta");
		this.maximoVenta = (Double) mapa.get("maximoVenta");
		this.idSpread = (Integer) mapa.get("idSpread");
		this.idFactorDivisa = (Integer) mapa.get("idFactorDivisa");
		this.idPrecioReferencia = (Integer) mapa.get("idPrecioReferencia");
		this.token = (String) mapa.get("idToken");
		this.vigencia = (Date) mapa.get("vigencia");
	}

	/**
	 * @return the minimoCompra
	 */
	public Double getMinimoCompra() {
		return minimoCompra;
	}

	/**
	 * @param minimoCompra the minimoCompra to set
	 */
	public void setMinimoCompra(Double minimoCompra) {
		this.minimoCompra = minimoCompra;
	}

	/**
	 * @return the maximoCompra
	 */
	public Double getMaximoCompra() {
		return maximoCompra;
	}

	/**
	 * @param maximoCompra the maximoCompra to set
	 */
	public void setMaximoCompra(Double maximoCompra) {
		this.maximoCompra = maximoCompra;
	}

	/**
	 * @return the minimoVenta
	 */
	public Double getMinimoVenta() {
		return minimoVenta;
	}

	/**
	 * @param minimoVenta the minimoVenta to set
	 */
	public void setMinimoVenta(Double minimoVenta) {
		this.minimoVenta = minimoVenta;
	}

	/**
	 * @return the maximoVenta
	 */
	public Double getMaximoVenta() {
		return maximoVenta;
	}

	/**
	 * @param maximoVenta the maximoVenta to set
	 */
	public void setMaximoVenta(Double maximoVenta) {
		this.maximoVenta = maximoVenta;
	}

	/**
	 * @return the idSpread
	 */
	public Integer getIdSpread() {
		return idSpread;
	}

	/**
	 * @param idSpread the idSpread to set
	 */
	public void setIdSpread(Integer idSpread) {
		this.idSpread = idSpread;
	}

	/**
	 * @return the idFactorDivisa
	 */
	public Integer getIdFactorDivisa() {
		return idFactorDivisa;
	}

	/**
	 * @param idFactorDivisa the idFactorDivisa to set
	 */
	public void setIdFactorDivisa(Integer idFactorDivisa) {
		this.idFactorDivisa = idFactorDivisa;
	}

	/**
	 * @return the idPrecioReferencia
	 */
	public Integer getIdPrecioReferencia() {
		return idPrecioReferencia;
	}

	/**
	 * @param idPrecioReferencia the idPrecioReferencia to set
	 */
	public void setIdPrecioReferencia(Integer idPrecioReferencia) {
		this.idPrecioReferencia = idPrecioReferencia;
	}
	
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the vigencia
	 */
	public Date getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia the vigencia to set
	 */
	public void setVigencia(Date vigencia) {
		this.vigencia = vigencia;
	}

	/**
     * Regresa una cadena con los nombres y valores de las variables de instancia.
     *
     * @return String.
     */
	public String toString() {
		return new ToStringBuilder(this).append("minimoCompra", minimoCompra).
        append("maximoCompra", maximoCompra).append("minimoVenta", minimoVenta).
        append("maximoVenta", maximoVenta).append("idSpread", idSpread).
        append("idFactorDivisa", idFactorDivisa).append("idPrecioReferencia", idPrecioReferencia).
        append("token", token).append("vigencia", vigencia).
        	toString();
	}
	
	/**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
    	return new HashCodeBuilder().append(token).toHashCode();
    }
    
    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
    	if (!(other instanceof TipoCambioDto)) {
            return false;
        }
    	TipoCambioDto castOther = (TipoCambioDto) other;
    	return new EqualsBuilder().append(getToken(), castOther.getToken()).isEquals();
    }

	/**
	 * El precio m&iacute;nimo de compra.
	 */
	private Double minimoCompra;
	
	/**
	 * El precio m&aacute;ximo de compra.
	 */
	private Double maximoCompra;
	
	/**
	 * El precio m&iacute;nimo de venta.
	 */
	private Double minimoVenta;
	
	/**
	 * El precio m&aacute;ximo de venta.
	 */
	private Double maximoVenta;
	
	/**
	 * El ID del spread.
	 */
	private Integer idSpread;
	
	/**
	 * El ID del factor de la divisa.
	 */
	private Integer idFactorDivisa;
	
	/**
	 * El ID del precio de referncia.
	 */
	private Integer idPrecioReferencia;
	
	/**
	 * El token de la vigencia del tipo de cambio
	 */
	private String token;
	
	/**
	 * La fecha de la vigencia del tipo de cambio
	 */
	private Date vigencia;
	/**
	 * UID para serializaci&oacute;n..
	 */
	private static final long serialVersionUID = -5008142555966859774L;

}
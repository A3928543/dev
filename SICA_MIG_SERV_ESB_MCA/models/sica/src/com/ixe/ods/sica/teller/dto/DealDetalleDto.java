/*
 * $Id: DealDetalleDto.java,v 1.1.4.2.6.1 2011/04/26 01:04:05 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.teller.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Data Transfer Object.
 *
 * @author Israel Rebollar
 * @version $Revision: 1.1.4.2.6.1 $ $Date: 2011/04/26 01:04:05 $
 */
public class DealDetalleDto implements Serializable {

	/**
	 * Constructor default, no hace nada.
	 */
	public DealDetalleDto() {
		super();
	}
	
	/**
	 * 
	 * 
	 * @param recibimos true para compra (recibimos) o false para venta (entregamos).
	 * @param monto El monto en la divisa.
	 * @param tcc El Tipo de Cambio dado al Cliente.
	 * @param tcm El tipo de cambio de la mesa.
	 * @param idDivisa El ID de la divisa.
	 * @param claveFormaLiquidacion La clave de la forma de liquidacion.
	 * @param idPlantilla La clave de la plantilla
	 * @param factorDivisa El valor del factor divisa aplicado.
	 * @param idFactorDivisa La clave del factor divisa aplicado.
	 * @param idSpread La clave del spread aplicado.
     * @param precioReferenciaMidSpot El precio referencia Mid Spot en el momento de la captura.
     * @param precioReferenciaSpot El precio referencia Spot en el momento de la captura.
     * @param idPrecioReferencia La clave del precio de referencia aplicado.
     * @param instruccionesBeneficiario Las instrucciones para el beneficiario.
     * @param instruccionesIntermediario Las instrucciones para el intermediario.
     * @param instruccionesPagador Las instrucciones para el pagador.
     * @param token El token de tipo de cambio.
     * @param aLaPar true cuando es operaci&oacute;n a la par.
     */
    public DealDetalleDto(boolean recibimos, Double monto, Double tcc, Double tcm, String idDivisa,
                          String claveFormaLiquidacion, Integer idPlantilla, Double factorDivisa, Integer idFactorDivisa,
                          Integer idSpread, Double precioReferenciaMidSpot, Double precioReferenciaSpot, Integer idPrecioReferencia,
                          String instruccionesBeneficiario, String instruccionesIntermediario,
                          String instruccionesPagador, String token, boolean aLaPar) {

        this();
        this.recibimos = recibimos;
        this.monto = monto;
        this.tcc = tcc;
        this.tcm = tcm;
        this.idDivisa = idDivisa;
        this.claveFormaLiquidacion = claveFormaLiquidacion;
        this.idPlantilla = idPlantilla;
        this.precioReferenciaMidSpot = precioReferenciaMidSpot;
        this.precioReferenciaSpot = precioReferenciaSpot;
        this.idPrecioReferencia = idPrecioReferencia;
        this.factorDivisa = factorDivisa;
        this.idFactorDivisa = idFactorDivisa;
        this.idSpread = idSpread;
        this.aLaPar = aLaPar;
        this.instruccionesBeneficiario = instruccionesBeneficiario;
		this.instruccionesIntermediario = instruccionesIntermediario;
		this.instruccionesPagardor = instruccionesPagador;
		this.token = token;
	}
	
	/**
	 * Regresa el idDealPosicion del Detalle
	 * @return Integer. 
	 */
	public Integer getIdDealPosicion() {
		return idDealPosicion;
	}

    /**
     * Establece el valor de idDealPosicion.
     *
     * @param idDealPosicion El valor a asignar.
     */
	public void setIdDealPosicion(Integer idDealPosicion) {
		this.idDealPosicion = idDealPosicion;
	}

	/**
	 * @return the folioDetalle
	 */
	public Integer getFolioDetalle() {
		return folioDetalle;
	}

	/**
	 * @param folioDetalle the folioDetalle to set
	 */
	public void setFolioDetalle(Integer folioDetalle) {
		this.folioDetalle = folioDetalle;
	}

	/**
	 * @return the recibimos
	 */
	public boolean isRecibimos() {
		return recibimos;
	}

	/**
	 * @param recibimos the compra to set
	 */
	public void setRecibimos(boolean recibimos) {
		this.recibimos = recibimos;
	}

	/**
	 * @return the monto
	 */
	public Double getMonto() {
		return monto;
	}

	/**
	 * @param monto the monto to set
	 */
	public void setMonto(Double monto) {
		this.monto = monto;
	}

	/**
	 * @return the tcc
	 */
	public Double getTcc() {
		return tcc;
	}

	/**
	 * @param tcc the tcc to set
	 */
	public void setTcc(Double tcc) {
		this.tcc = tcc;
	}

	/**
	 * @return the tcm
	 */
	public Double getTcm() {
		return tcm;
	}

	/**
	 * @param tcm the tcm to set
	 */
	public void setTcm(Double tcm) {
		this.tcm = tcm;
	}

	/**
	 * @return the idDivisa
	 */
	public String getIdDivisa() {
		return idDivisa;
	}

	/**
	 * @param idDivisa the idDivisa to set
	 */
	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}

	/**
	 * @return the idPlantilla
	 */
	public Integer getIdPlantilla() {
		return idPlantilla;
	}

	/**
	 * @param idPlantilla the idPlantilla to set
	 */
	public void setIdPlantilla(Integer idPlantilla) {
		this.idPlantilla = idPlantilla;
	}

	/**
	 * @return the claveFormaLiquidacion
	 */
	public String getClaveFormaLiquidacion() {
		return claveFormaLiquidacion;
	}

	/**
	 * @param claveFormaLiquidacion the claveFormaLiquidacion to set
	 */
	public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
		this.claveFormaLiquidacion = claveFormaLiquidacion;
	}

	/**
	 * @deprecated Se utilizar&aacute; el valor directo del Precio Referencia
	 * @return the idPrecioReferencia
	 */
	public Integer getIdPrecioReferencia() {
		return idPrecioReferencia;
	}

	/**
	 * @deprecated Se utilizar&aacute; el valor directo del Precio Referencia
	 * @param idPrecioReferencia the idPrecioReferencia to set
	 */
	public void setIdPrecioReferencia(Integer idPrecioReferencia) {
		this.idPrecioReferencia = idPrecioReferencia;
	}
	
	/**
     * Regresa el valor de precioReferenciaMidSpot.
     * 
     * @return double
     */
    public Double getPrecioReferenciaMidSpot() {
        return precioReferenciaMidSpot;
    }

    /**
     * Fija el valor de precioReferenciaMidSpot.
     *
     * @param precioReferenciaMidSpot El valor a asignar.
     */
    public void setPrecioReferenciaMidSpot(Double precioReferenciaMidSpot) {
        this.precioReferenciaMidSpot = precioReferenciaMidSpot;
    }
    
    /**
     * Regresa el valor de precioReferenciaSpot.
     * 
     * @return double
     */
    public Double getPrecioReferenciaSpot() {
        return precioReferenciaSpot;
    }

    /**
     * Fija el valor de precioReferenciaMidSpot.
     *
     * @param precioReferenciaSpot El valor a asignar.
     */
    public void setPrecioReferenciaSpot(Double precioReferenciaSpot) {
        this.precioReferenciaSpot = precioReferenciaSpot;
    }


	/**
	 * @deprecated Se debe utilizar el valor directo del Factor Divisa.
	 * @return the idFactorDivisa
	 */
	public Integer getIdFactorDivisa() {
		return idFactorDivisa;
	}

	/**
	 * @deprecated Se debe utlizar el valor directo del Factor Divisa.
	 * @param idFactorDivisa the idFactorDivisa to set
	 */
	public void setIdFactorDivisa(Integer idFactorDivisa) {
		this.idFactorDivisa = idFactorDivisa;
	}
	
	/**
	 * Regresa el valor del Factor Divisa utlizado en la captura del detalle.
	 * @return El factorDivisa
	 */
	public Double getFactorDivisa() {
		return factorDivisa;
	}

	/**
	 * Establece el valor del Factor Divisa para la captura del detalle.
	 * @param factorDivisa El valor del Factor Divisa.
	 */
	public void setFactorDivisa(Double factorDivisa) {
		this.factorDivisa = factorDivisa;
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
	 * @return the aLaPar
	 */
	public boolean isALaPar() {
		return aLaPar;
	}

	/**
	 * @param laPar the aLaPar to set
	 */
	public void setALaPar(boolean laPar) {
		aLaPar = laPar;
	}

	/**
	 * @return the instruccionesBeneficiario
	 */
	public String getInstruccionesBeneficiario() {
		return instruccionesBeneficiario;
	}

	/**
	 * @param instruccionesBeneficiario the instruccionesBeneficiario to set
	 */
	public void setInstruccionesBeneficiario(String instruccionesBeneficiario) {
		this.instruccionesBeneficiario = instruccionesBeneficiario;
	}

	/**
	 * @return the instruccionesIntermediario
	 */
	public String getInstruccionesIntermediario() {
		return instruccionesIntermediario;
	}

	/**
	 * @param instruccionesIntermediario the instruccionesIntermediario to set
	 */
	public void setInstruccionesIntermediario(String instruccionesIntermediario) {
		this.instruccionesIntermediario = instruccionesIntermediario;
	}

	/**
	 * @return the instruccionesPagardor
	 */
	public String getInstruccionesPagardor() {
		return instruccionesPagardor;
	}

	/**
	 * @param instruccionesPagardor the instruccionesPagardor to set
	 */
	public void setInstruccionesPagardor(String instruccionesPagardor) {
		this.instruccionesPagardor = instruccionesPagardor;
	}

	/**
	 * @return the eventosDetalle
	 */
	public String getEventosDetalle() {
		return eventosDetalle;
	}

	/**
	 * @param eventosDetalle the eventosDetalle to set
	 */
	public void setEventosDetalle(String eventosDetalle) {
		this.eventosDetalle = eventosDetalle;
	}
	
	/**
	 * @return String.
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * 
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

    /**
     * Regresa una cadena con los nombres y valores de las variables de instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).
                append("idDealPosicion", idDealPosicion).append("folioDetalle", folioDetalle).
                append("recibimos", recibimos).
                append("monto", monto).append("tcc", tcc).
                append("tcm", tcm).append("idDivisa", idDivisa).
                append("claveFormaLiquidacion", claveFormaLiquidacion).
                append("idPlantilla", idPlantilla).append("idPrecioReferencia", idPrecioReferencia).
                append("idFactorDivisa", factorDivisa).append("idSpread", idSpread).
                append("aLaPar", aLaPar).toString();
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
    	if (!(other instanceof DealDetalleDto)) {
            return false;
        }
    	DealDetalleDto castOther = (DealDetalleDto) other;
        return new EqualsBuilder().append(this.getIdDealPosicion(), castOther.getIdDealPosicion()).
                append(this.getFolioDetalle(), castOther.getFolioDetalle()).isEquals();
    }
    
    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
    	return new HashCodeBuilder().append(getIdDealPosicion()).append(folioDetalle).toHashCode();
    }
    
    /**
     * El ID de la posici&oacute;n del deal.
     */
    private Integer idDealPosicion;
    
    /**
     * El folio del deal detalle.
     */
    private Integer folioDetalle;
	
	/**
	 * El valor de recibimos.
	 */
	private boolean recibimos;
	
	/**
	 * El valor de monto.
	 */
	private Double monto;
	
	/**
	 * El valor de tipo cambio cliente.
	 */
	private Double tcc;
	
	/**
	 * El valor del tipo de cambio de la mesa.
	 */
	private Double tcm;
	
	/**
	 * El valor del ID de la divisa.
	 */
	private String idDivisa;
	
	/**
	 * El valor del ID de la plantilla.
	 */
	private Integer idPlantilla;
	
	/**
     * La clave del producto.
     */
    private String claveFormaLiquidacion;
	
	/**
	 * El valor del identificado de precio de referencia.
	 * 
	 * @deprecated Se debe utilizar el valor directo del Precio Referencia.
	 */
	private Integer idPrecioReferencia;
	
	/**
     * El precio referencia Mid Spot utilizado al momento de la
     * captura.
     */
    private Double precioReferenciaMidSpot;
    
    /**
     * El precio referencia Spot utilizado al momento de la
     * captura.
     */
    private Double precioReferenciaSpot;
	
	/**
	 * El valor identificador del factor divisa.
	 * 
	 * @deprecated Se debe utilizar el valor directo del Factor Divisa.
	 */
	private Integer idFactorDivisa;
    
    /**
	 * El valor del factor divisa.
	 */
	private Double factorDivisa;
	
	/**
	 * El valor de identificador del spread aplicado.
	 */
	private Integer idSpread;
	
	/**
	 * El valor se trata de un detalle de deal de opers. a la par o no.
	 */
	private boolean aLaPar;
	
	/**
	 * Las instrucciones para el beneficiario.
	 */
	private String instruccionesBeneficiario;
	
	/**
	 * Las instrucciones para el intermediario.
	 */
	private String instruccionesIntermediario;
	
	/**
	 * Las instrucciones del banco pagador.
	 */
	private String instruccionesPagardor;
	
	/**
	 * Los eventos del detalle del deal.
	 */
	private String eventosDetalle;
	
	/**
	 * La clave del token.
	 */
	private String token;

	/**
	 * UID para serealizaci&oacute;n. 
	 */
	private static final long serialVersionUID = -6146037405405846081L;

}

/*
 * $Id: DealDto.java,v 1.1.4.2 2010/10/08 01:31:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.teller.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Data Transfer Object utilizado en la consulta de Deal.
 *
 * @author Israel Rebollar
 * @version $Revision: 1.1.4.2 $ $Date: 2010/10/08 01:31:04 $
 */
public class DealDto implements Serializable {
	
	/**
	 * Constructor default, no hace nada.
	 */
	public DealDto() {
		super();
	}
	
	/**
	 * Constructor que inicializa los valores del objeto.
	 * 
	 * @param recibimos
	 * @param idMesaCambio
	 * @param idCanal
	 * @param noCuenta
	 * @param eventosDeal
	 * @param fechaCaptura
	 * @param fechaLiquidacion
	 * @param idDeal
	 * @param idLiquidaciones
	 * @param observaciones
	 * @param idPromotor
	 * @param tipoDeal
	 * @param tipoValor
	 * @param idUsuario
	 */
	public DealDto(boolean recibimos, Integer idMesaCambio, String idCanal, String noCuenta,
				String eventosDeal, Date fechaCaptura, Date fechaLiquidacion, Integer idDeal, 
				Integer idLiquidaciones, String observaciones, Integer idPromotor, String tipoDeal, 
				String statusDeal, String tipoValor, Integer idUsuario){
		this();
		this.recibimos = recibimos;
		this.idMesaCambio = idMesaCambio;
		this.idCanal = idCanal;
		this.noCuenta = noCuenta;
		this.eventosDeal = eventosDeal;
		this.fechaCaptura = fechaCaptura;
		this.fechaLiquidacion = fechaLiquidacion;
		this.idDeal = idDeal;
		this.idLiquidacion = idLiquidaciones;
		this.observaciones = observaciones;
		this.idPromotor = idPromotor;
		this.tipoDeal = tipoDeal;
		this.statusDeal = statusDeal;
		this.tipoValor = tipoValor;
		this.idUsuario = idUsuario;
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
	 * @return the idMesaCambio
	 */
	public Integer getIdMesaCambio() {
		return idMesaCambio;
	}

	/**
	 * @param idMesaCambio the idMesaCambio to set
	 */
	public void setIdMesaCambio(Integer idMesaCambio) {
		this.idMesaCambio = idMesaCambio;
	}

	/**
	 * @return the idCanal
	 */
	public String getIdCanal() {
		return idCanal;
	}

	/**
	 * @param idCanal the idCanal to set
	 */
	public void setIdCanal(String idCanal) {
		this.idCanal = idCanal;
	}

	/**
	 * @return the noCuenta
	 */
	public String getNoCuenta() {
		return noCuenta;
	}

	/**
	 * @param noCuenta the noCuenta to set
	 */
	public void setNoCuenta(String noCuenta) {
		this.noCuenta = noCuenta;
	}

	/**
	 * @return the eventosDeal
	 */
	public String getEventosDeal() {
		return eventosDeal;
	}

	/**
	 * @param eventosDeal the eventosDeal to set
	 */
	public void setEventosDeal(String eventosDeal) {
		this.eventosDeal = eventosDeal;
	}

	/**
	 * @return the fechaCaptura
	 */
	public Date getFechaCaptura() {
		return fechaCaptura;
	}

	/**
	 * @param fechaCaptura the fechaCaptura to set
	 */
	public void setFechaCaptura(Date fechaCaptura) {
		this.fechaCaptura = fechaCaptura;
	}

	/**
	 * @return the fechaLiquidacion
	 */
	public Date getFechaLiquidacion() {
		return fechaLiquidacion;
	}

	/**
	 * @param fechaLiquidacion the fechaLiquidacion to set
	 */
	public void setFechaLiquidacion(Date fechaLiquidacion) {
		this.fechaLiquidacion = fechaLiquidacion;
	}

	/**
	 * @return the idDeal
	 */
	public Integer getIdDeal() {
		return idDeal;
	}

	/**
	 * @param idDeal the idDeal to set
	 */
	public void setIdDeal(Integer idDeal) {
		this.idDeal = idDeal;
	}

	/**
	 * @return the idLiquidacion
	 */
	public Integer getIdLiquidacion() {
		return idLiquidacion;
	}

	/**
	 * @param idLiquidacion the idLiquidacion to set
	 */
	public void setIdLiquidacion(Integer idLiquidacion) {
		this.idLiquidacion = idLiquidacion;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the idPromotor
	 */
	public Integer getIdPromotor() {
		return idPromotor;
	}

	/**
	 * @param idPromotor the idPromotor to set
	 */
	public void setIdPromotor(Integer idPromotor) {
		this.idPromotor = idPromotor;
	}

	/**
	 * @return the statusDeal
	 */
	public String getStatusDeal() {
		return statusDeal;
	}

	/**
	 * @param statusDeal the statusDeal to set
	 */
	public void setStatusDeal(String statusDeal) {
		this.statusDeal = statusDeal;
	}

	/**
	 * @return the tipoDeal
	 */
	public String getTipoDeal() {
		return tipoDeal;
	}

	/**
	 * @param tipoDeal the tipoDeal to set
	 */
	public void setTipoDeal(String tipoDeal) {
		this.tipoDeal = tipoDeal;
	}

	/**
	 * @return the tipoValor
	 */
	public String getTipoValor() {
		return tipoValor;
	}

	/**
	 * @param tipoValor the tipoValor to set
	 */
	public void setTipoValor(String tipoValor) {
		this.tipoValor = tipoValor;
	}

	/**
	 * @return the idUsuario
	 */
	public Integer getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	/**
	 * Regresa la lista de detalles DealDetalleDto.
	 * 
	 * @return List.
	 */
	public List getDetalles() {
		return detalles;
	}
	
	/**
	 * La lista de detalles DealDetalleDto a asignar.
	 * @param detalles
	 */
	public void setDetalles(List detalles) {
		this.detalles = detalles;
	}

	/**
     * Regresa una cadena con los nombres y valores de las variables de instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("recibimos", recibimos).
                append("idCanal", idCanal).append("noCuenta", noCuenta).
                append("eventosDeal", eventosDeal).append("fechaCaptura", fechaCaptura).
                append("fechaLiquidacion", fechaLiquidacion).append("idDeal", idDeal).
                append("idLiquidacion", idLiquidacion).append("observaciones", observaciones).
                append("idPromotor", idPromotor).append("idPromotor", idPromotor).
                append("statusDeal", statusDeal).append("tipoDeal", tipoDeal).
                append("tipoValor", tipoValor).append("idUsuario", idUsuario).
                append("detalles", detalles).toString();
    }
    
    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
    	if (!(other instanceof DealDto)) {
            return false;
        }
    	DealDto castOther = (DealDto) other;
    	return new EqualsBuilder().append(this.getIdDeal(), castOther.getIdDeal()).isEquals();
    }
    
    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
    	return new HashCodeBuilder().append(getIdDeal()).toHashCode();
    }

	/**
	 * Indicador de COMPRA o VENTA.
	 */
	private boolean recibimos;
	
	/**
	 * El ID de la mesa de cambio.
	 */
	private Integer idMesaCambio;
	
	/**
	 * El ID del Canal.
	 */
	private String idCanal;
	
	/**
	 * El n&uacute;mero de cuenta.
	 */
	private String noCuenta;
	
	/**
	 * El valor de eventos.
	 */
	private String eventosDeal;
	
	/**
	 * El valor de la fecha de captura.
	 */
	private Date fechaCaptura;
	
	/**
	 * El valor de la fecha de liquidaci&oacute;n.
	 */
	private Date fechaLiquidacion;
	
	/**
	 * El valor del ID del Deal.
	 */
	private Integer idDeal;
	
	/**
	 * El valor del ID de liquidaci&oacute;n
	 */
	private Integer idLiquidacion;
	
	/**
	 * El valor de la observaciones.
	 */
	private String observaciones;
	
	/**
	 * El valor del ID del Promotor.
	 */
	private Integer idPromotor;
	
	/**
	 * El valir del STATUS del Deal.
	 */
	private String statusDeal;
	
	/**
	 * El valor del Tipo de Deal.
	 */
	private String tipoDeal;
	
	/**
	 * El valor del Tipo de Valor.
	 */
	private String tipoValor;
	
	/**
	 * El valor del ID del Usuario.
	 */
	private Integer idUsuario;
	
	/**
	 * La lista de DealDetalleDto
	 */
	private List detalles;
	
	/**
	 *  El UID para serializaci&oacute;n.
	 */
	private static final long serialVersionUID = 7020545384018104089L;

}

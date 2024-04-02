/*
 * $Id: RepUtIxeForwardsBean.java,v 1.1 2008/06/23 21:20:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Bean que permite probar el reporte de utlidades.
 *
 * @author Edgar Leija
 * @version  $Revision: 1.1 $ $Date: 2008/06/23 21:20:23 $
 */
public class RepUtIxeForwardsBean implements Serializable {

    /**
	 * Constructor por default
	 * 
	 * @param image Im&aacute;gen
	 * @param fechaReporte Fecha de csonulta del Reporte
	 * @param idMesaCambio Identificador de la mesa
	 * @param idDeal N&uacute;mero de deal
     * @param tipoOperacion Compra o Venta
     * @param fechaValor CASH | TOM | SPOT | 72HR | VFUT
	 * @param idDivisa Identificador de la divisa
	 * @param monto Monto
     * @param montoEquiv El Monto en la divisa de referencia de la mesa.
     * @param tipoCambioOperado El tipo de cambio cliente.
     * @param iniciaSwap Si es o no inicio de swap.
	 * @param utilidad Utilidad.
	 */
	public RepUtIxeForwardsBean(InputStream image,  Integer idDeal, String tipoOperacion,
                                String fechaValor, String idDivisa,  Double monto,
                                Double montoEquiv, Double tipoCambioOperado, Double utilidad,
                                String iniciaSwap,  Date fechaReporte, String idMesaCambio) {
		super();
		this.idDeal = idDeal;
		this.tipoOperacion = tipoOperacion;
		this.fechaValor = fechaValor;
		this.idDivisa = idDivisa;
		this.monto = monto;
		this.montoEquiv = montoEquiv;
		this.tipoCambioOperado = tipoCambioOperado;
		this.utilidad = utilidad;
		this.iniciaSwap = iniciaSwap;
		this.idMesaCambio = idMesaCambio;
        this.image = image;
        this.fechaReporte = fechaReporte;
    }

	/**
	 * Regresa el valor de fechaReporte.
	 *	
	 * @return Date.
	 */
	public Date getFechaReporte() {
		return fechaReporte;
	}

	/**
	 * Asigna el valor para fechaReporte.
	 *
	 * @param fechaReporte El valor para fechaReporte.
	 */
	public void setFechaReporte(Date fechaReporte) {
		this.fechaReporte = fechaReporte;
	}

	/**
	 * Regresa el valor de idDeal.
	 *	
	 * @return Integer.
	 */
	public Integer getIdDeal() {
		return idDeal;
	}

	/**
	 * Asigna el valor para idDeal.
	 *
	 * @param idDeal El valor para idDeal.
	 */
	public void setIdDeal(Integer idDeal) {
		this.idDeal = idDeal;
	}
	
	/**
	 * Regresa el tipo de Operacion , ejem Compra Venta
	 *	
	 * @return String.
	 */
	public String getTipoOperacion() {
		return tipoOperacion;
	}

	/**
	 * Asigna el valor para utilidad.
	 *
	 * @param tipoOperacion El valor para utilidad.
	 */
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	
	/**
	 * Regresa el tipo fecha Valor, ejem Cash, Tom, Spot, 72 hr.
	 *	
	 * @return String.
	 */
	public String getFechaValor() {
		return fechaValor;
	}

	/**
	 * Asigna el valor de fecha Valor
	 *
	 * @param fechaValor El valor para utilidad.
	 */
	public void setFechaValor(String fechaValor) {
		this.fechaValor = fechaValor;
	}
	
	/**
	 * Regresa el identificador de la Divisa
	 *	
	 * @return String.
	 */
	public String getIdDivisa() {
		return idDivisa;
	}

	/**
	 * Asigna el valor para divisa
	 *
	 * @param idDivisa El valor para divisa
	 */
	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}

	/**
	 * Regresa el valor de monto.
	 *	
	 * @return Double.
	 */
	public Double getMonto() {
		return monto;
	}

	/**
	 * Asigna el valor para monto.
	 *
	 * @param monto El valor para monto.
	 */
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	
	/**
	 * Regresa el valor de montoEquiv.
	 *	
	 * @return Double.
	 */
	public Double getMontoEquiv() {
		return montoEquiv;
	}

	/**
	 * Asigna el valor para monto.
	 *
	 * @param montoEquiv El valor para monto Equivalente.
	 */
	public void setMontoEquiv(Double montoEquiv) {
		this.montoEquiv = montoEquiv;
	}
	
	/**
	 * Regresa el valor de tipoCambioOperado.
	 *	
	 * @return Double.
	 */
	public Double getTipoCambioOperado() {
		return tipoCambioOperado;
	}

	/**
	 * Asigna el valor para el tipo de Cambio
	 *
	 * @param tipoCambioOperado El valor para tipoCambioOperado.
	 */
	public void setTipoCambioOperado(Double tipoCambioOperado) {
		this.tipoCambioOperado = tipoCambioOperado;
	}
	
	/**
	 * Regresa el valor de image.
	 *	
	 * @return InputStream.
	 */
	public InputStream getImage() {
		return image;
	}

	/**
	 * Asigna el valor para image.
	 *
	 * @param image El valor para image.
	 */
	public void setImage(InputStream image) {
		this.image = image;
	}

	/**
	 * Regresa el valor de utilidad.
	 *	
	 * @return Double.
	 */
	public Double getUtilidad() {
		return utilidad;
	}

	/**
	 * Asigna el valor para utilidad.
	 *
	 * @param utilidad El valor para utilidad.
	 */
	public void setUtilidad(Double utilidad) {
		this.utilidad = utilidad;
	}
	
	/**
	 * Regresa el valor de utilidad.
	 *	
	 * @return String.
	 */
	public String getIdMesaCambio() {
		return idMesaCambio;
	}

	/**
	 * Asigna el valor para utilidad.
	 *
	 * @param idMesaCambio El valor para utilidad.
	 */
	public void setIdMesaCambio(String idMesaCambio) {
		this.idMesaCambio = idMesaCambio;
	}
	
	/**
	 * Regresa el identificador si el deal inicia Swap.
	 *	
	 * @return String.
	 */
	public String getIniciaSwap() {
		return iniciaSwap;
	}

	/**
	 * Asigna el valor para iniciaSwap
	 *
	 * @param iniciaSwap El valor en caso que inicia Swap.
	 */
	public void setIniciaSwap(String iniciaSwap) {
		this.iniciaSwap = iniciaSwap;
	}
	
	/**
	 * Id del Deal
	 */
	private Integer idDeal;
	
	/**
	 * La fecha de consulta del Reporte.
	 */
	private Date fechaReporte;
	
	/**
	 * Monto 
	 */
	private Double monto;
	
	/**
	 * Monto Equivalente 
	 */
	private Double montoEquiv;
	
	/**
	 * Tipo de Cambio Operado
	 */
	private Double tipoCambioOperado;
	
	/**
	 * Utilidad 
	 */
	private Double utilidad;
	
	/**
	 * Identificador que permite saber si el deal inicia Swap 
	 */
	private String iniciaSwap;
	
	/**
	 * Identificador de la Mesa 
	 */
	private String idMesaCambio;
	
	/**
	 * Identificador del tipo de Operaci&oacute;n 
	 */
	private String tipoOperacion;
	
	/**
	 * Identificador del tipo de Fecha Valor 
	 */
	private String fechaValor;
	
	/**
	 * Identificador de la Divisa 
	 */
	private String idDivisa;
	
	/**
	 * Logo de IXE
	 */
	private InputStream image;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -4147323690873962954L;
}

/*
 * $Id: ReporteDealsPagoAntBean.java,v 1.10 2008/02/22 18:25:03 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Bean que permite probar el reporte de Deals con
 * pago anticipado 
 *
 * @author Gerardo Corzo Herrera
 */
public class ReporteDealsPagoAntBean implements Serializable {
	
	/**
     * Constructor de la clase ReporteDealsPagoAntBean, que se utiliza 
     * para almacenar los datos para la prueba del reporte del Deal 
	 * 
	 * @param fechaActual La fecha del reporte.
	 * @param claveFormaLiquidacion La clave de la forma de liquidaci&oacute;n.
	 * @param promotor El nombre del promotor.
	 * @param idDeal El id del deal.
	 * @param monto El monto del deal.
	 * @param fechaCaptura La fecha de captura del deal.
	 * @param status El estatus del deal.
	 * @param folioDetalle El folio del detalle.
	 * @param noCuenta El n&uacute;mero de cuenta.
	 * @param excedente El excedente de la l&iacute;nea de cr&eacute;dito.
	 * @param image La imagen de encabezado del reporte.
	 */
	public ReporteDealsPagoAntBean(Date fechaActual,
			String claveFormaLiquidacion, String promotor, Integer idDeal,
			Double monto, Date fechaCaptura, String status,
			Integer folioDetalle, String noCuenta, String excedente, InputStream image) {
		super();
		this.fechaActual = fechaActual;
		this.claveFormaLiquidacion = claveFormaLiquidacion;
		this.promotor = promotor;
		this.idDeal = idDeal;
		this.monto = monto;
		this.fechaCaptura = fechaCaptura;
		this.status = status;
		this.folioDetalle = folioDetalle;
		this.noCuenta = noCuenta;
		this.excedente = excedente;
		this.image = image;

		
	}
	
	/**
	 * Regresa el valor de claveFormaLiquidacion.
	 *	
	 * @return String.
	 */
	public String getClaveFormaLiquidacion() {
		return claveFormaLiquidacion;
	}

	/**
	 * Asigna el valor para claveFormaLiquidacion.
	 *
	 * @param claveFormaLiquidacion El valor para claveFormaLiquidacion.
	 */
	public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
		this.claveFormaLiquidacion = claveFormaLiquidacion;
	}

	/**
	 * Regresa el valor de excedente.
	 *	
	 * @return String.
	 */
	public String getExcedente() {
		return excedente;
	}

	/**
	 * Asigna el valor para excedente.
	 *
	 * @param excedente El valor para excedente.
	 */
	public void setExcedente(String excedente) {
		this.excedente = excedente;
	}

	/**
	 * Regresa el valor de fechaActual.
	 *	
	 * @return Date.
	 */
	public Date getFechaActual() {
		return fechaActual;
	}

	/**
	 * Asigna el valor para fechaActual.
	 *
	 * @param fechaActual El valor para fechaActual.
	 */
	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	/**
	 * Regresa el valor de fechaCaptura.
	 *	
	 * @return Date.
	 */
	public Date getFechaCaptura() {
		return fechaCaptura;
	}

	/**
	 * Asigna el valor para fechaCaptura.
	 *
	 * @param fechaCaptura El valor para fechaCaptura.
	 */
	public void setFechaCaptura(Date fechaCaptura) {
		this.fechaCaptura = fechaCaptura;
	}

	/**
	 * Regresa el valor de folioDetalle.
	 *	
	 * @return Integer.
	 */
	public Integer getFolioDetalle() {
		return folioDetalle;
	}

	/**
	 * Asigna el valor para folioDetalle.
	 *
	 * @param folioDetalle El valor para folioDetalle.
	 */
	public void setFolioDetalle(Integer folioDetalle) {
		this.folioDetalle = folioDetalle;
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
	 * Regresa el valor de noCuenta.
	 *	
	 * @return String.
	 */
	public String getNoCuenta() {
		return noCuenta;
	}

	/**
	 * Asigna el valor para noCuenta.
	 *
	 * @param noCuenta El valor para noCuenta.
	 */
	public void setNoCuenta(String noCuenta) {
		this.noCuenta = noCuenta;
	}

	/**
	 * Regresa el valor de promotor.
	 *	
	 * @return String.
	 */
	public String getPromotor() {
		return promotor;
	}

	/**
	 * Asigna el valor para promotor.
	 *
	 * @param promotor El valor para promotor.
	 */
	public void setPromotor(String promotor) {
		this.promotor = promotor;
	}

	/**
	 * Regresa el valor de status.
	 *	
	 * @return String.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Asigna el valor para status.
	 *
	 * @param status El valor para status.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Fecha actuald del sistema
	 */
	private Date fechaActual;
	
	/**
	 * La forma de liquidaci&oacute;n del detalle 
	 * del Deal
	 */
	private String claveFormaLiquidacion;
	
	/**
	 * Nombre del promotor
	 */
	private String promotor;
	
	/**
	 * Id del Deal
	 */
	private Integer idDeal;
	
	/**
	 * Monto del detalle del Deal
	 */
	private Double monto;
	
	/**
	 * Fecha de captura del Deal
	 */
	private Date fechaCaptura = new Date();
	
	/**
	 * Estatus del Deal
	 */
    private String status;
    
	/**
	 * Folio del detalle del Deal
	 */
    private Integer folioDetalle;
    
	/**
	 * N&uacute;mero de cuenta
	 */
    private String noCuenta;
    
	/**
	 * Tiene excedente el Deal
	 */
    private String excedente;
    
	/**
	 * Logo IXE
	 */
    private InputStream image;
}

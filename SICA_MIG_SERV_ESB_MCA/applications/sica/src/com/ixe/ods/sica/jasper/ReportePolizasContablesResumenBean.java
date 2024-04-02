/*
 * $Id: ReportePolizasContablesResumenBean.java,v 1.10 2008/02/22 18:25:03 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
         
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Bean para generar el reporte de Polizas Contables 
 * 
 * @author Gustavo Gonzalez
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:03 $
 */
public class ReportePolizasContablesResumenBean implements Serializable{
		
	/**
	 * Constructor del bean para el reporte de P&oacute;lizas Contables.
	 * 
	 * @param divisa La divisa del deal.
	 * @param fechaValor La fecha valor del deal.
	 * @param cuentaContable La cuenta contable de la p&oacute;liza.
	 * @param cargos Los cargos de la p&oacute;liza.
	 * @param abonos Los abonos de la p&oacute;liza.
	 * @param entidad La entidad de la cuenta.
	 */
	public ReportePolizasContablesResumenBean(String divisa, Date fechaValor, String cuentaContable, Double cargos, Double abonos, String entidad){
		super();
		this.divisa = divisa;
		this.fechaValor = fechaValor;
		this.cuentaContable = cuentaContable;
		this.cargos = cargos;
		this.abonos = abonos;
		this.entidad = entidad;
	}
	
	/**
	 * Constructor del bean para el reporte de P&oacute;lizas Contables.
	 * 
	 * @param divisa La divisa del deal.
	 * @param fechaValor La fecha valor del deal.
	 * @param cuentaContable La cuenta contable de la p&oacute;liza.
	 * @param cargos Los cargos de la p&oacute;liza.
	 * @param abonos Los abonos de la p&oacute;liza.
	 * @param entidad La entidad de la cuenta.
	 * @param imagen La imagen de encabezado del reporte.
	 */
	public ReportePolizasContablesResumenBean(String divisa, Date fechaValor, String cuentaContable, Double cargos, Double abonos, String entidad, InputStream imagen){
		super();
		this.divisa = divisa;
		this.fechaValor = fechaValor;
		this.cuentaContable = cuentaContable;
		this.cargos = cargos;
		this.abonos = abonos;
		this.image = imagen;
		this.entidad = entidad;
	}
	
	/**
	 * Regresa el valor de abonos.
	 *	
	 * @return Double.
	 */
	public Double getAbonos() {
		return abonos;
	}

	/**
	 * Asigna el valor para abonos.
	 *
	 * @param abonos El valor para abonos.
	 */
	public void setAbonos(Double abonos) {
		this.abonos = abonos;
	}

	/**
	 * Regresa el valor de cargos.
	 *	
	 * @return Double.
	 */
	public Double getCargos() {
		return cargos;
	}

	/**
	 * Asigna el valor para cargos.
	 *
	 * @param cargos El valor para cargos.
	 */
	public void setCargos(Double cargos) {
		this.cargos = cargos;
	}

	/**
	 * Regresa el valor de cuentaContable.
	 *	
	 * @return String.
	 */
	public String getCuentaContable() {
		return cuentaContable;
	}

	/**
	 * Asigna el valor para cuentaContable.
	 *
	 * @param cuentaContable El valor para cuentaContable.
	 */
	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	/**
	 * Regresa el valor de divisa.
	 *	
	 * @return String.
	 */
	public String getDivisa() {
		return divisa;
	}

	/**
	 * Asigna el valor para divisa.
	 *
	 * @param divisa El valor para divisa.
	 */
	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}

	/**
	 * Regresa el valor de entidad.
	 *	
	 * @return String.
	 */
	public String getEntidad() {
		return entidad;
	}

	/**
	 * Asigna el valor para entidad.
	 *
	 * @param entidad El valor para entidad.
	 */
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	/**
	 * Regresa el valor de fechaValor.
	 *	
	 * @return Date.
	 */
	public Date getFechaValor() {
		return fechaValor;
	}

	/**
	 * Asigna el valor para fechaValor.
	 *
	 * @param fechaValor El valor para fechaValor.
	 */
	public void setFechaValor(Date fechaValor) {
		this.fechaValor = fechaValor;
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
	 * Divisa de la p&oacute;liza
	 */
	private String divisa;
	
	/**
	 * Fecha valor
	 */
	private Date fechaValor;
	
	/**
	 * Cuenta contable de la p&oacute;liza
	 */
	private String cuentaContable;
	
	/**
	 * Cargos de la cuenta
	 */
	private Double cargos;
	
	/**
	 * Abonos de la cuenta
	 */
	private Double abonos;
	
	/**
	 * Entidad de la cuenta
	 */
	private String entidad;
	
	/**
	 * Imagen de encabezado del reporte.
	 */
	private InputStream image;
	
}

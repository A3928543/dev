/*
 * $Id: ReporteAuxiliaresContablesBean.java,v 1.10 2008/02/22 18:25:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Bean para generar el reporte de Auxiliares Contables
 * 
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:04 $
 */
public class ReporteAuxiliaresContablesBean implements Serializable {
	
	/**
	 * Constructor de la clase ReporteAuxiliaresContablesBean, que se utiliza 
     * para almacenar los datos para la prueba del reporte del Deal. 
	 * 
	 * @param divisa La divisa de la operaci&oacute;n.
	 * @param fechaValor La fecha valor de la operaci&oacute;n.
	 * @param cuentaContable La cuenta contable de cliente.
	 * @param idDeal El id del Deal.
	 * @param cargos El monto de los cargos.
	 * @param abonos El monto de los abonos.
	 */
	public ReporteAuxiliaresContablesBean(String divisa, Date fechaValor, String cuentaContable, String idDeal, 
			Double cargos, Double abonos, String referencia, String cliente, String entidad, String contratoSica){
		super();
		this.divisa = divisa;
		this.fechaValor = fechaValor;
		this.cuentaContable = cuentaContable;
		this.idDeal = idDeal;
		this.cargos = cargos;
		this.abonos = abonos;
		this.referencia = referencia;
		this.cliente = cliente;
		this.entidad = entidad;
		this.contratoSica = contratoSica;
	}
	
	/**
	 * Constructor de la clase ReporteAuxiliaresContablesBean, que se utiliza 
     * para almacenar los datos para la prueba del reporte del Deal.
     *  
	 * @param divisa La divisa de la operaci&oacute;n.
	 * @param fechaValor La fecha valor de la operaci&oacute;n.
	 * @param cuentaContable La cuenta contable de cliente.
	 * @param idDeal El id del Deal.
	 * @param cargos El monto de los cargos.
	 * @param abonos El monto de los abonos.
	 */
	public ReporteAuxiliaresContablesBean(String divisa, Date fechaValor, String cuentaContable, String idDeal, 
			Double cargos, Double abonos, String referencia, String cliente, String entidad, String contratoSica, InputStream imagen){
		super();
		this.divisa = divisa;
		this.fechaValor = fechaValor;
		this.cuentaContable = cuentaContable;
		this.idDeal = idDeal;
		this.cargos = cargos;
		this.abonos = abonos;
		this.referencia = referencia;
		this.image = imagen;
		this.cliente = cliente;
		this.contratoSica = contratoSica;
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
	 * @param cargos El valor oara cargos.
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
	 * @param divisa El valor de para divisa.
	 */
	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}
	
	/**
	 * Regresa el valor de fechaValor
	 * 
	 * @return Date
	 */
	public Date getFechaValor() {
		return fechaValor;
	}
	
	/**
	 * Asigna el valor para fechaValor
	 * 
	 * @param fechaValor El valor para fechaValor
	 */
	public void setFechaValor(Date fechaValor) {
		this.fechaValor = fechaValor;
	}
	
	/**
	 * Regresa el valor de idDeal
	 * 
	 * @return String
	 */
	public String getIdDeal() {
		return idDeal;
	}
	
	/**
	 * Asigna el valor para idDeal
	 * 
	 * @param idDeal El valor para idDeal
	 */
	public void setIdDeal(String idDeal) {
		this.idDeal = idDeal;
	}
	
	/**
	 * Regresa el valor de referencia.
	 * 
	 * @return String.
	 */
	public String getReferencia() {
		return referencia;
	}
	
	/**
	 * Asigna el valor para referencia.
	 * 
	 * @param referencia El valor para referencia.
	 */
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	
	/**
	 * Regresa el valor de image
	 * 
	 * @return InputStream.
	 */
	public InputStream getImage() {
		return image;
	}
	
	/**
	 * Asigna el valor para imagen.
	 * 
	 * @param image El valor para imagen.
	 */
	public void setImage(InputStream image) {
		this.image = image;
	}
	
	/**
	 * Regresa el valor de cliente.
	 * 
	 * @return String.
	 */
	public String getCliente() {
		return cliente;
	}
	
	/**
	 * Asigna el valor para cliente.
	 * 
	 * @param cliente El valor para cliente.
	 */
	public void setCliente(String cliente) {
		this.cliente = cliente;
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
	 * Regresa el valor de contratoSica.
	 * 
	 * @return String.
	 */
	public String getContratoSica() {
		return contratoSica;
	}

	/**
	 * Asigna el valor para contratoSica.
	 * 
	 * @param contratoSica El valor para contratoSica.
	 */
	public void setContratoSica(String contratoSica) {
		this.contratoSica = contratoSica;
	}
	
	/**
	 * Divisa del deal.
	 */
	private String divisa;
	
	/**
	 * Fecha Valor del deal.
	 */
	private Date fechaValor;
	
	/**
	 * Cuenta contable del cliente.
	 */
	private String cuentaContable;
	
	/**
	 * Id del Deal.
	 */
	private String idDeal;
	
	/**
	 * Los cargos del deal.
	 */
	private Double cargos;
	
	/**
	 * Los abonos del deal.
	 */
	private Double abonos;
	
	/**
	 * Referencia de la cuenta contable.
	 */
	private String referencia;
	
	/**
	 * Cliente.
	 */
	private String cliente;
	
	/**
	 * Entidad de la cuenta contable.
	 */
	private String entidad;
	
	/**
	 * Contrato Sica del cliente.
	 */
	private String contratoSica;
	
	/**
	 * Imagen de encabezado del reporte.
	 */
	private InputStream image;
}

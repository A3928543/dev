/*
 * $Id: ReporteLinCredAutBean.java,v 1.10 2008/02/22 18:25:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Bean que permite probar el reporte de l&iacute;nea de 
 * Cr&eacute;dito
 *
 * @author Gerardo Corzo Herrera
 */
public class ReporteLinCredAutBean implements Serializable{
	
	/**
	 * Constructor del bean para el Reporte de Autorizaci&oacute;n de L&iacute;neas de Cr&eacute;dito.
	 * 
	 * @param operacion La operacion de la l&iacute;nea.
	 * @param producto El producto de la l&iacute;nea.
	 * @param fechaOperacion La fecha de operacion de la l&iacute;nea. 
	 * @param fechaCaptura La fecha de captura de la l&iacute;nea.
	 * @param idDeal El id deal.
	 * @param divisa La divisa del deal.
	 * @param monto El monto del deal.
	 * @param montoLinea El monto de la l&iacute;nea.
	 * @param corporativo El corporativo que realiz&oacute; la operaci&oacute;n
	 * @param status El estatus de la l&iacute;nea.
	 * @param ultimaModificacion La fecha de la &uacute;ltima modificacion de la l&iacute;nea.
	 * @param vencimiento La fecha de vencimiento de la l&iacute;nea.
	 * @param importe El importe de la l&iacute;nea.
	 * @param promedioLinea El promedio de uso de la l&iacute;nea.
	 * @param numeroExcepciones El n&uacute;mero de excepciones de la l&iacute;nea.
	 * @param numeroExcepcionesMes El n&uacute;mero de excepciones de la l&iacute;nea en el mes.
	 * @param tipoLineaCredito El tipo de la l&iacute;nea de cr&eacute;dito.
	 * @param image La imagen de encabezado del reporte.
	 * @param usuario El usuario.
	 * @param importeUsd El importe en USD de la operaci&oacute;n.
	 * @param saldoFinal El sa
	 * @param saldo
	 */
	public ReporteLinCredAutBean(String operacion, String producto,
			Date fechaOperacion, Date fechaCaptura, Integer idDeal,
			String divisa, Double monto, Double montoLinea, String corporativo,
			String status, Date ultimaModificacion, Date vencimiento,
			Double importe, Double promedioLinea, Integer numeroExcepciones,
			Integer numeroExcepcionesMes, String tipoLineaCredito, InputStream image, 
			String usuario, Double importeUsd, Double saldoFinal, Double saldo) {
		super();
		this.operacion = operacion;
		this.producto = producto;
		this.fechaOperacion = fechaOperacion;
		this.fechaCaptura = fechaCaptura;
		this.idDeal = idDeal;
		this.divisa = divisa;
		this.monto = monto;
		this.montoLinea = montoLinea;
		this.corporativo = corporativo;
		this.status = status;
		this.ultimaModificacion = ultimaModificacion;
		this.vencimiento = vencimiento;
		this.importe = importe;
		this.promedioLinea = promedioLinea;
		this.numeroExcepciones = numeroExcepciones;
		this.numeroExcepcionesMes = numeroExcepcionesMes;
		this.tipoLineaCredito = tipoLineaCredito;
		this.image = image;
		this.usuario = usuario;
		this.importeUsd = importeUsd;
		this.saldoFinal = saldoFinal;
		this.saldo = saldo;
	}

	/**
	 * Regresa el valor de corporativo.
	 *	
	 * @return String.
	 */
	public String getCorporativo() {
		return corporativo;
	}

	/**
	 * Asigna el valor para corporativo.
	 *
	 * @param corporativo El valor para corporativo.
	 */
	public void setCorporativo(String corporativo) {
		this.corporativo = corporativo;
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
	 * Regresa el valor de fechaOperacion.
	 *	
	 * @return Date.
	 */
	public Date getFechaOperacion() {
		return fechaOperacion;
	}

	/**
	 * Asigna el valor para fechaOperacion.
	 *
	 * @param fechaOperacion El valor para fechaOperacion.
	 */
	public void setFechaOperacion(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
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
	 * Regresa el valor de importe.
	 *	
	 * @return Double.
	 */
	public Double getImporte() {
		return importe;
	}

	/**
	 * Asigna el valor para importe.
	 *
	 * @param importe El valor para importe.
	 */
	public void setImporte(Double importe) {
		this.importe = importe;
	}

	/**
	 * Regresa el valor de importeUsd.
	 *	
	 * @return Double.
	 */
	public Double getImporteUsd() {
		return importeUsd;
	}

	/**
	 * Asigna el valor para importeUsd.
	 *
	 * @param importeUsd El valor para importeUsd.
	 */
	public void setImporteUsd(Double importeUsd) {
		this.importeUsd = importeUsd;
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
	 * Regresa el valor de montoLinea.
	 *	
	 * @return Double.
	 */
	public Double getMontoLinea() {
		return montoLinea;
	}

	/**
	 * Asigna el valor para montoLinea.
	 *
	 * @param montoLinea El valor para montoLinea.
	 */
	public void setMontoLinea(Double montoLinea) {
		this.montoLinea = montoLinea;
	}

	/**
	 * Regresa el valor de numeroExcepciones.
	 *	
	 * @return Integer.
	 */
	public Integer getNumeroExcepciones() {
		return numeroExcepciones;
	}

	/**
	 * Asigna el valor para numeroExcepciones.
	 *
	 * @param numeroExcepciones El valor para numeroExcepciones.
	 */
	public void setNumeroExcepciones(Integer numeroExcepciones) {
		this.numeroExcepciones = numeroExcepciones;
	}

	/**
	 * Regresa el valor de numeroExcepcionesMes.
	 *	
	 * @return Integer.
	 */
	public Integer getNumeroExcepcionesMes() {
		return numeroExcepcionesMes;
	}

	/**
	 * Asigna el valor para numeroExcepcionesMes.
	 *
	 * @param numeroExcepcionesMes El valor para numeroExcepcionesMes.
	 */
	public void setNumeroExcepcionesMes(Integer numeroExcepcionesMes) {
		this.numeroExcepcionesMes = numeroExcepcionesMes;
	}

	/**
	 * Regresa el valor de operacion.
	 *	
	 * @return String.
	 */
	public String getOperacion() {
		return operacion;
	}

	/**
	 * Asigna el valor para operacion.
	 *
	 * @param operacion El valor para operacion.
	 */
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	/**
	 * Regresa el valor de producto.
	 *	
	 * @return String.
	 */
	public String getProducto() {
		return producto;
	}

	/**
	 * Asigna el valor para producto.
	 *
	 * @param producto El valor para producto.
	 */
	public void setProducto(String producto) {
		this.producto = producto;
	}

	/**
	 * Regresa el valor de promedioLinea.
	 *	
	 * @return Double.
	 */
	public Double getPromedioLinea() {
		return promedioLinea;
	}

	/**
	 * Asigna el valor para promedioLinea.
	 *
	 * @param promedioLinea El valor para promedioLinea.
	 */
	public void setPromedioLinea(Double promedioLinea) {
		this.promedioLinea = promedioLinea;
	}

	/**
	 * Regresa el valor de saldo.
	 *	
	 * @return Double.
	 */
	public Double getSaldo() {
		return saldo;
	}

	/**
	 * Asigna el valor para saldo.
	 *
	 * @param saldo El valor para saldo.
	 */
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	/**
	 * Regresa el valor de saldoFinal.
	 *	
	 * @return Double.
	 */
	public Double getSaldoFinal() {
		return saldoFinal;
	}

	/**
	 * Asigna el valor para saldoFinal.
	 *
	 * @param saldoFinal El valor para saldoFinal.
	 */
	public void setSaldoFinal(Double saldoFinal) {
		this.saldoFinal = saldoFinal;
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
	 * Regresa el valor de tipoLineaCredito.
	 *	
	 * @return String.
	 */
	public String getTipoLineaCredito() {
		return tipoLineaCredito;
	}

	/**
	 * Asigna el valor para tipoLineaCredito.
	 *
	 * @param tipoLineaCredito El valor para tipoLineaCredito.
	 */
	public void setTipoLineaCredito(String tipoLineaCredito) {
		this.tipoLineaCredito = tipoLineaCredito;
	}

	/**
	 * Regresa el valor de ultimaModificacion.
	 *	
	 * @return Date.
	 */
	public Date getUltimaModificacion() {
		return ultimaModificacion;
	}

	/**
	 * Asigna el valor para ultimaModificacion.
	 *
	 * @param ultimaModificacion El valor para ultimaModificacion.
	 */
	public void setUltimaModificacion(Date ultimaModificacion) {
		this.ultimaModificacion = ultimaModificacion;
	}

	/**
	 * Regresa el valor de usuario.
	 *	
	 * @return String.
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Asigna el valor para usuario.
	 *
	 * @param usuario El valor para usuario.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Regresa el valor de vencimiento.
	 *	
	 * @return Date.
	 */
	public Date getVencimiento() {
		return vencimiento;
	}

	/**
	 * Asigna el valor para vencimiento.
	 *
	 * @param vencimiento El valor para vencimiento.
	 */
	public void setVencimiento(Date vencimiento) {
		this.vencimiento = vencimiento;
	}
	
	/**
	 * El tipo de operaci&oacute;n
	 */
	private String operacion;
	
	/**
	 * El producto
	 */
	private String producto;
	
	/**
	 * La fecha de operaci&oacute;n 
	 */
	private Date fechaOperacion;
	
	/**
	 * La fecha de captura operaci&oacute;n 
	 */
	private Date fechaCaptura;
	
	/**
	 * Id del Deal
	 */
	private Integer idDeal;
	
	/**
	 * la Divisa del detalle del Deal
	 */
	private String divisa;
	
	/**
	 * Monto del Detalle del Deal
	 */
	private Double monto;
	
	/**
	 * Monto de la l&iacute;nea de Cr&eacute;dito
	 */
	private Double montoLinea;
	
	/**
	 * El nombre del corporativo
	 */
	private String corporativo;
	
	/**
	 * Estatus de la l&iacute;nea de Cr&eacute;dito
	 */
	private String status;
	
	/**
	 * Fecha de la &uacute;ltima modificaci&oacute;n
	 */
	private Date ultimaModificacion;
	
	/**
	 * Fecha de vencimiento de la l&iacute;nea de Cr&eacute;dito
	 */
	private Date vencimiento;
	
	/**
	 * Importe de la l&iacute;nea de Cr&eacute;dito
	 */
	private Double importe;
	
	/**
	 * Promedio de uso de la l&iacute;nea de Cr&eacute;dito
	 */
	private Double promedioLinea;
	
	/**
	 * N&uacutemero de excepciones de la l&iacute;nea de Cr&eacute;dito
	 * 
	 */
	private Integer numeroExcepciones;
	
	/**
	 * N&uacutemero de excepciones de la l&iacute;nea de Cr&eacute;dito
	 * por mes
	 * 
	 */
	private Integer numeroExcepcionesMes;
	
	/**
	 * Tipo de l&iacute;nea de Cr&eacute;dito
	 * 
	 */
	private String tipoLineaCredito;
	
	/**
	 * Logo de IXE
	 * 
	 */
	private InputStream image;
	
	/**
	 * Nombre del promotor que maneja la l&iacute;nea de Cr&eacute;dito
	 * 
	 */
	private String usuario;
	
	/**
	 * Importe en USD de la l&iacute;nea de Cr&eacute;dito
	 * 
	 */
	private Double importeUsd;
	
	/**
	 * Saldo final de la l&iacute;nea de Cr&eacute;dito
	 * 
	 */
	private Double saldoFinal;
	
	/**
	 * Saldo la l&iacute;nea de Cr&eacute;dito
	 * 
	 */
	private Double saldo;

}

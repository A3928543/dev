package com.ixe.ods.sica.rmds.process.dao;

import java.util.Date;

/**
 * @author Efren Trinidad, Na-at Technologies
 *
 * Jul 22, 2011 6:21:10 PM
 */
public interface DepuracionTablasPreciosDao {

	/**
	 * Realiza dos principales funciones:
	 * 
	 * 1.- Env&iacute;a a hist&oacute;rico los registros de Precio 
	 * Referencia con los cuales se pacto una operaci&oacute;n.
	 * 2.- Elimina los registros de Precio Referencia que se generaron
	 * entre las fechas inicio y fin que recibe como par&aacute;metro.
	 * 
	 * @param fechaInicio La fecha de inicio de captura de una operaci&oacute;n o
	 * de generaci&oacute;n del Precio Referencia.
	 * @param fechaFin La fecha de fin de captura de una operaci&oacute;n o
	 * de generaci&oacute;n del Precio Referencia.
	 */
	public boolean limpiarPreciosReferencia(Date fechaInicio, Date fechaFin);

	/**
	 * Realiza dos principales funciones:
	 * 
	 * 1.- Env&iacute;a a hist&oacute;rico los registros de Factor 
	 * Divisa con los cuales se pacto una operaci&oacute;n.
	 * 2.- Elimina los registros de Factor Divisa que se generaron
	 * entre las fechas inicio y fin que recibe como par&aacute;metro.
	 * 
	 * @param fechaInicio La fecha de inicio de captura de una operaci&oacute;n o
	 * de generaci&oacute;n del Factor Divisa.
	 * @param fechaFin La fecha de fin de captura de una operaci&oacute;n o
	 * de generaci&oacute;n del Factor Divisa.
	 */
	public boolean limpiarFactoresDivisa(Date fechaInicio, Date fechaFin);

	/**
	 * Elimina los registros en SICA_VARIACION generados entre las
	 * fechas que recibe como par&aacute;metro.
	 * 
	 * @param fechaInicio La fecha inicio de la generaci&oacute;n de la variaci&oacute;n.
	 * @param fechaFin La fecha fin de la de la generaci&oacute;n de la variaci&oacute;n.
	 */
	public boolean limpiarVariacionesDivisa(Date fechaInicio, Date fechaFin);

}
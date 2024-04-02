/**
 * 
 */
package com.ixe.ods.sica.sdo.impl;

import java.sql.Date;
import java.util.List;

import com.ixe.ods.sica.dao.ReportesDao;
import com.ixe.ods.sica.sdo.ReportesServiceData;

/**
 * @author PEspinosa
 *
 */
public class JdbcReportesServiceData implements ReportesServiceData {

	/**
	 * Interfaz del DAO
	 */
	private ReportesDao reportesDao;
	
	/**
	 * Consulta el CR_GENERICO almacenado en SC_PARAMETRO
	 * @return String CR_GENERICO
	 */
	public String findCrGenerico()
	{
		return getReportesDao().findCrGenerico();
	}
	
	/**
	 * Consulta los deals y sus detalles para generar el reporte 'Clientes Banorte en SICA'. 
	 * Los deals deben tener cr asignado. 25/03/2015 11:48 am
	 * @param fechaInicial 
	 * @param fechaFinal
	 * @return List
	 */
	public List findCrClientesBanorteEnSica(Date fechaInicial, Date fechaFinal)
	{
		return getReportesDao().findCrClientesBanorteEnSica(fechaInicial, fechaFinal);
	}
	
	/**
	 * Obtiene la lista de polizas contables de las fechas especificadas
	 * 
	 * @param diaInicial Dia a partir del cual se buscan las polizas
	 * @param diaFinal Dia limite para las polizas
	 * @return List
	 */
	public List findPolizasContables(Date diaInicial, Date diaFinal) {
		return getReportesDao().findPolizasContables(diaInicial, diaFinal);
	}
	
	/**
	 * @return the reportesDao
	 */
	public ReportesDao getReportesDao() {
		return reportesDao;
	}

	/**
	 * @param reportesDao
	 *            the reportesDao to set
	 */
	public void setReportesDao(ReportesDao reportesDao) {
		this.reportesDao = reportesDao;
	}

	/**
     * Realiza la b&uacute;squeda para generar el reporte de operaciones de compras/ventas.
     *
     * @param idDivisa El id de la divisa seleccionada.
     * @param formaLiquidacion La forma de liquidacion.
     * @param operacion El tipo de operacion (compra o venta).
     * @param origen El origen de la operacion (teller, resto o todas)
     * @param promotor El id del promotor.
     * @param desde La fecha de inicio de la busqueda.
     * @param hasta La fecha de fin de la busqueda.
     * @param montoMinimo El monto minimo de la busqueda.
     * @param montoMaximo El monto maximo de la busqueda.
     * @return List.
     */
	public List findReporteOperacionesComprasVentas(
		String idDivisa,
		String formaLiquidacion, 
		String operacion, 
		String origen,
		Integer promotor, 
		Date desde, 
		Date hasta, 
		double montoMinimo,
		double montoMaximo,
		Integer division,
        Integer plaza,
		Integer sucursal,
        Integer zona,
        String contratoSica,
        String tipoCliente,
        boolean esMontoEquivUsd,
        List usuariosNoStaff
	) {
		return getReportesDao().findReporteOperacionesComprasVentas(
			idDivisa, 
			formaLiquidacion, 
			operacion, 
			origen, 
			promotor, 
			desde, 
			hasta, 
			montoMinimo, 
			montoMaximo,
			division,
	        plaza,
			sucursal,
	        zona,
	        contratoSica,
	        tipoCliente,
	        esMontoEquivUsd,
	        usuariosNoStaff
		);
	}
	
	/**
	 * 
	 */
	public List findReporteOperacionesDiarias(Date desde,Date hasta) {
		return getReportesDao().findReporteOperacionesDiarias(desde, hasta);
	}
	
	/**
	 * 
	 */
	public List findReporteOperacionesDiariasIxedir(Date desde,Date hasta) {
		return getReportesDao().findReporteOperacionesDiariasIxedir(desde, hasta);
	}
	
	/**
	 * Realiza la busqueda de los clientes que se dieron de alta durante el dia
	 * a fin de enviar el Layout de clientes hacia ARMS
	 * @param fechaProceso La fecha del dia en que se extrae la informacion 
	 */
	public List findAllClientes(Date fechaProceso) {
		return getReportesDao().findAllClientes(fechaProceso);
	}
	
	/**
	 * Realiza la busqueda de los contratos que se dieron de alta durante el dia
	 * a fin de enviar el Layout de contratos hacia ARMS
	 * @param fechaProceso La fecha del dia en que se extrae la informacion 
	 */
	public List findAllContratos(Date fechaProceso) {
		return getReportesDao().findAllContratos(fechaProceso);
	}
	
	/**
	 * Realiza la busqueda de las transacciones que se liquidaron durante el dia
	 * a fin de enviar el Layout de transacciones hacia ARMS
	 * @param fechaProceso La fecha del dia en que se extrae la informacion 
	 */
	public List findAllTransacciones(Date fechaProceso) {
		return getReportesDao().findAllTransacciones(fechaProceso);
	}
	
	 /**
     * Realiza la b&uacute;squeda para generar el reporte de autorizaciones de mesa de cambios.
     *
     * @param idCanal El id del canal seleccionado.
     * @param status El status del detalle de deal.
     * @param tipoAutorizacion El filtro para autorizaciones por plantilla o por falta de documentos.
     * @param desde La fecha de inicio de la busqueda.
     * @param hasta La fecha de fin de la busqueda.
     * @return List.
     */
	public List findReporteAutorizacionesMesaControl(String idCanal,
			String status, String tipoAutorizacion, Date desde, Date hasta) {
		
		return getReportesDao().findReporteAutorizacionesMesaControl(idCanal, 
				status, tipoAutorizacion, desde, hasta);
	}

	/**
     * Realiza la b&uacute;squeda para generar el reporte de operaciones de compras/ventas.
     *
     * @param operacion El tipo de operacion (compra o venta).
     * @param promotor El id del promotor.
     * @param desde La fecha de inicio de la busqueda.
     * @param hasta La fecha de fin de la busqueda.
     * @param idCanal El identificador del canal sobre el cual se van a calcular las utilidades
     * @return List.
     */
	public List findReporteUtilidadPromotres(Integer promotor, Date desde,
			Date hasta, String operacion, String idCanal) {
		
		return getReportesDao().findReporteUtilidadPromotres(promotor, desde,
				hasta, operacion, idCanal);
	}

	 /**
     * Realiza la b&uacute;squeda para generar el reporte de documentacib&oacute;n pendiente
     * 
     * @author PEspinosa
     * @param nombreCorto - El nombre corto del cliente
     * @param noContrato - El nb&uacute;mero de contrato SICA
     * 
     * @return List
     */
	public List findReporteDocumentacionFaltante(String nombreCorto,
			String noContrato) {
		
		return getReportesDao().findReporteDocumentacionFaltante(nombreCorto,
				noContrato);
	}
}

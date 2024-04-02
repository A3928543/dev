package com.banorte.ods.sica.batch.transfint.service;

/**
 * The Interface ReporteService.
 */
public interface ReporteService {
	
	/**
	 * Genera reporte.
	 */
	void generaReporte();
	
	/**
	 * Send error email.
	 *
	 * @param error the error
	 */
	void sendErrorEmail(String error);
	
	/**
	 * Genera reporte tardias.
	 */
	void generaReporteTardias();
	
	/**
	 * Send error email.
	 *
	 * @param error the error
	 */
	void sendEmail(String error);
}

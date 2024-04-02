package com.ixe.ods.sica.batch.poliza.service;

/**
 * The Interface FileService.
 */
public interface FileService {
	
	/**
	 * Depura historico archivos detalle polizas.
	 */
	void depuraHistoricoArchivosDetallePolizas();
	
	/**
	 * Generar archivo detalle polizas.
	 *
	 * @return true, if successful
	 */
	boolean generarArchivoDetallePolizas();
	
	/**
	 * Crea archivo bandera.
	 */
	void creaArchivoBandera();
	
	/**
	 * Send error email.
	 *
	 * @param mensaje the mensaje
	 */
	void sendErrorEmail(String mensaje);
	
}

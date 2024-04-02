package com.ixe.ods.sica.batch.cfdi.service;

import java.util.Locale;


/**
 * The Interface FileIndicesService.
 */
public interface FileIndicesService {
	
	/** The Constant LOCALE_ES. */
	static final Locale LOCALE_ES = new Locale("es");
	
	/** The Constant DEFAULT_MSG. */
	static final String DEFAULT_MSG = "Error de negocio, favor de revisar " 
							+ "log para mayor informaci\u00F3n.";
	
	/** The Constant REPROCESO_OK. */
	static final String REPROCESO_OK = "OK";
	
	/** The Constant REPROCESO_KO. */
	static final String REPROCESO_KO = "KO";
	
	/** The Constant ID_STATUS_FACTURADO. */
	static final String ID_STATUS_FACTURADO = "FACT";
	
	/** The Constant DIR_TMP_ENTRADA. */
	static final String DIR_TMP_ENTRADA = "ENTRADA";
	
	/** The Constant DIR_TMP_SALIDA. */
	static final String DIR_TMP_SALIDA = "SALIDA";
	
	/** The Constant DIR_TEMP_CORRECTOS. */
	static final String DIR_TEMP_CORRECTOS = "CORRECTOS";
	
	/** The Constant DIR_TEMP_ERRORES. */
	static final String DIR_TEMP_ERRORES = "ERRORES";
	
	/**
	 * Procesa archivo.
	 */
	void procesaArchivo();
	
	/**
	 * Depurar historico archivos.
	 */
	void depurarHistoricoArchivos();
}

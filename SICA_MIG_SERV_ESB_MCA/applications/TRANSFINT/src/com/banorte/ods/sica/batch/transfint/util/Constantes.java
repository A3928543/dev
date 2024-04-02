package com.banorte.ods.sica.batch.transfint.util;

import java.util.Locale;

/**
 * The Interface Constantes.
 */
public interface Constantes {
	
	/** The Constant FASE_INICIO_CARGA. */
	static final Short FASE_INICIO_CARGA = 1;
	
	/** The Constant FASE_CARGA_TERMINADA. */
	static final Short FASE_CARGA_TERMINADA = 2;
	
	/** The Constant FASE_ARCHIVO_GERENERADO. */
	static final Short FASE_ARCHIVO_GERENERADO = 3;
	
	/** The Constant FASE_ARCHIVO_ENVIADO. */
	static final Short FASE_ARCHIVO_ENVIADO = 4;
	
	/** The Constant TIPO_REPORTE_TRANSFERENCIAS_DIARIAS. */
	static final Short TIPO_REPORTE_TRANSFERENCIAS_DIARIAS = 1;
	
	/** The Constant TIPO_REPORTE_TRANSFERENCIAS_TARDIAS. */
	static final Short TIPO_REPORTE_TRANSFERENCIAS_TARDIAS = 2;
	
	/** The Constant TIPO_REPORTE_TRANSFERENCIAS_CANCELADAS. */
	static final Short TIPO_REPORTE_TRANSFERENCIAS_CANCELADAS = 3;
	
	/** The Constant TIPO_REPORTE_TRENSFERENCIA_INHABILES. */
	static final Short TIPO_REPORTE_TRENSFERENCIA_INHABILES = 4;
	
	/** The Constant TIPO_REPORTE_TRENSFERENCIA_INHABILES. */
	static final Short TIPO_REPORTE_REPROCESO = 5;
	
	/** The Constant STATUS_DETALLE_CANCELADO. */
	static final String STATUS_DETALLE_CANCELADO = "CA";
	
	/** The Constant SI. */
	static final String SI = "S";
	
	/** The Constant ROL_TITULAR. */
	static final String ROL_TITULAR = "TIT";
	
	/** The Constant PERSONA_MORAL. */
	static final String PERSONA_MORAL = "PM";
	
	/** The Constant PERSONA_FISICA. */
	static final String PERSONA_FISICA = "PF";
	
	/** The Constant PERSONA_FISICA_ACT_EMPRESARIAL. */
	static final String PERSONA_FISICA_ACT_EMPRESARIAL = "AE";
	
	/** The Constant PAIS_MEXICO. */
	static final String PAIS_MEXICO = "48";
	
	/** The Constant CLIENTE_PERSONA_FISICA. */
	static final Short CLIENTE_PERSONA_FISICA = 1;
	
	/** The Constant CLIENTE_PERSONA_MORAL. */
	static final Short CLIENTE_PERSONA_MORAL = 2;
	
	/** The Constant NACIONALIDAD_MEXICANA. */
	static final String NACIONALIDAD_MEXICANA = "MX";
	
	/** The Constant TIPO_IDENTIFICADOR_FISCAL. */
	static final Short TIPO_IDENTIFICADOR_FISCAL = 1;
	
	/** The Constant EXITO. */
	static final Integer EXITO = 0;
	
	/** The Constant SIN_TRANSFERENCIAS. */
	static final Integer SIN_TRANSFERENCIAS = -10;
	
	/** The Constant ERROR_SISTEMA. */
	static final Integer ERROR_SISTEMA = -20;
	
	/** The Constant LOCALE_ES. */
	static final Locale LOCALE_ES = new Locale("es");
	
	/** The Constant DEFAULT_MSG. */
	static final String DEFAULT_MSG = "Error de negocio, favor de revisar " 
							+ "log para mayor informaci\u00F3n.";
	
	/** The Constant RUTA_ARCHIVO_COPIA. */
	static final String RUTA_ARCHIVO_COPIA = "archivos/reportes/diarios/";
	
	/** The Constant CORREOS_COPIA_TRANSFINT. */
	static final String CORREOS_COPIA_TRANSFINT = "CORREOS_COPIA_TRANSFINT";
	
	
	
}

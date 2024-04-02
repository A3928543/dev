package com.banorte.ods.sica.batch.transfint.service;

import java.util.Date;

import com.banorte.ods.sica.batch.transfint.util.Archivo;

/**
 * The Interface ReporteTransfintService.
 */
public interface ReporteTransfintService extends ReporteService {
	
	
	/**
	 * Genera reporte.
	 *
	 * @param fechaIni the fecha ini
	 * @param fechaFin the fecha fin
	 * @param tipoReporte the tipo reporte
	 * @param nombre the nombre
	 * @return the archivo
	 */
	Archivo generaReporte(Date fechaIni, Date fechaFin, Short tipoReporte, String nombre);
}

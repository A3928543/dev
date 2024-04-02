package com.banorte.ods.sica.batch.transfint.dao;

import java.util.Date;
import java.util.Map;

/**
 * The Interface ScSpCargaDao.
 */
public interface ScSpCargaDao {
	
	/** The Constant SP_CARGA_TRANSFINT. */
	static final String SP_CARGA_TRANSFINT = "PAQ_TRANSFINT.SC_SP_CARGA_TRANSFINT";
	
	/**
	 * Ejecuta carga.
	 *
	 * @param fechaIni the fecha ini
	 * @param fechaFin the fecha fin
	 * @param tipoReporte the tipo reporte
	 * @return the map
	 */
	Map<String, Object> ejecutaCarga(Date fechaIni, Date fechaFin, Short tipoReporte);
}

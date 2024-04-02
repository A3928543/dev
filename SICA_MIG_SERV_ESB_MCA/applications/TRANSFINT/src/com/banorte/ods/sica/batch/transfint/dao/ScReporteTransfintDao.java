package com.banorte.ods.sica.batch.transfint.dao;

import com.banorte.ods.sica.batch.transfint.domain.ScReporteTransfint;

/**
 * The Interface ScReporteTransfintDao.
 */
public interface ScReporteTransfintDao {
	
	/**
	 * Save.
	 *
	 * @param transfint the transfint
	 */
	void save(ScReporteTransfint transfint);
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the sc reporte transfint
	 */
	ScReporteTransfint findById(Long id);
	
	/**
	 * Update.
	 *
	 * @param reporteTransfint the reporte transfint
	 * @return the int
	 */
	void update(ScReporteTransfint reporteTransfint);

}

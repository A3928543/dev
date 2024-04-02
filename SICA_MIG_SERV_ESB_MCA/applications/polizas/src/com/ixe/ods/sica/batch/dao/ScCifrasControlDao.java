package com.ixe.ods.sica.batch.dao;

import com.ixe.ods.sica.batch.domain.ScCifrasControlCfdi;
import com.ixe.ods.sica.batch.domain.ScControlProcesoCifras;

/**
 * The Interface ScCifrasControlDao.
 */
public interface ScCifrasControlDao {
		
	/**
	 * Save sc cifras control.
	 *
	 * @param cifras the cifras
	 */
	void saveScCifrasControl(ScCifrasControlCfdi cifras);
	
	/**
	 * Update sc cifras control.
	 *
	 * @param cifras the cifras
	 */
	void updateScCifrasControl(ScCifrasControlCfdi cifras);
	
	/**
	 * Find sc cifras control by fecha.
	 *
	 * @param fecha the fecha
	 * @return the sc cifras control cfdi
	 */
	ScCifrasControlCfdi findScCifrasControlByFecha(String fecha);
	
	/**
	 * Find sc cifras control by id.
	 *
	 * @param id the id
	 * @return the sc cifras control cfdi
	 */
	ScCifrasControlCfdi findScCifrasControlById(Long id);
	
	/**
	 * Save sc control proceso cifras.
	 *
	 * @param control the control
	 */
	void saveScControlProcesoCifras(ScControlProcesoCifras control);
	
	/**
	 * Update sc control proceso cifras.
	 *
	 * @param control the control
	 */
	void updateScControlProcesoCifras(ScControlProcesoCifras control);
	
	/**
	 * Find sc control proceso cifras.
	 *
	 * @return the sc control proceso cifras
	 */
	ScControlProcesoCifras findScControlProcesoCifras();
	
	/**
	 * Delete sc control proceso cifras.
	 */
	void deleteScControlProcesoCifras();
}
	
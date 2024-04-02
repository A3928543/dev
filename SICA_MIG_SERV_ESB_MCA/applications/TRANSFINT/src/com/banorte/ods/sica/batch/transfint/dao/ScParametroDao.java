package com.banorte.ods.sica.batch.transfint.dao;

import com.banorte.ods.sica.batch.transfint.domain.ScParametro;

/**
 * The Interface ScParametroDao.
 */
public interface ScParametroDao {
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the sc parametro
	 */
	ScParametro findById(String id);
}

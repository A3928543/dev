package com.ixe.ods.sica.batch.dao;

import java.util.List;

import com.ixe.ods.sica.batch.domain.ScParametro;

/**
 * The Interface ScParametroDao.
 */
public interface ScParametroDao {
	
	/**
	 * Find sc parametro by id.
	 *
	 * @param id the id
	 * @return the sc parametro
	 */
	ScParametro findScParametroById(String id);
	
	/**
	 * Find sc parametro by prefijo.
	 *
	 * @param prefijo the prefijo
	 * @return the list
	 */
	List<ScParametro> findScParametroByPrefijo(String prefijo);
	
}

package com.ixe.ods.sica.batch.clientes.dao;

import java.util.Map;

/**
 * The Interface BitacoraDao.
 */
public interface BitacoraDao {
	
	/**
	 * Registrar bitacora.
	 *
	 * @param datos the datos
	 * @return the int
	 */
	public int registrarBitacora(Map<String, Object> datos);
	
}

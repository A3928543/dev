package com.ixe.ods.sica.batch.clientes.dao;

import java.util.List;
import java.util.Map;

/**
 * The Interface CuentaAltamiraDao.
 */
public interface CuentaAltamiraDao {
	
	/**
	 * Gets the cuentas.
	 *
	 * @param sic the sic
	 * @return the cuentas
	 */
	public List<Map<String, Object>> getCuentas(Long sic);
	
	/**
	 * Actualiza cuenta.
	 *
	 * @param datos the datos
	 * @return the int
	 */
	public int actualizaCuenta(Map<String, Object> datos);
	
}

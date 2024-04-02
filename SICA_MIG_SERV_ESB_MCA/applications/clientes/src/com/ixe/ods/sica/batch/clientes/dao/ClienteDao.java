package com.ixe.ods.sica.batch.clientes.dao;

import java.util.List;
import java.util.Map;

public interface ClienteDao {

	/**
	 * Get clientes.
	 *
	 * @param sic the sic
	 * @return the list
	 */
	public List<Map<String, Object>> getClientes(String sic);
	
	/**
	 * Actualiza cliente.
	 *
	 * @param datos the datos
	 * @return the int
	 */
	public int actualizaCliente(Map<String, Object> datos);
}

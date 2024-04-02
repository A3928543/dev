package com.ixe.ods.sica.dao;

import java.util.List;

public interface InformacionNegociacionDao {

	/**
	 * Recupera los clientes de un promotor junto con la informaci&oacute;n de negociaci&oacute;n
	 * asociada a ellos. 
	 * 
	 * @param idPersonaEjecutivo
	 * @return
	 */
	public abstract List findClienteEjecutivoConInfoNegociacion(
			Integer idPersonaEjecutivo, boolean favoritos);
	
	public abstract boolean updateClienteFavorito(
			String noCuenta, boolean favorito);
	
	/**
	 * Obtiene las ultimas 5 operaciones de un cliente 
	 * 
	 * @param idCliente Identificador del cliente
	 * @return
	 */
	public abstract List findLastFiveDeals(Integer idCliente);
	
	/**
	 * Modifica la informacion del contacto
	 * 
	 * @param idCliente
	 * @param phoneNumber
	 * @param name
	 * @param email
	 */
	public abstract boolean editContactInfo(Integer idCliente, String phoneNumber, String name, String email);
	
	/**
	 * Obtiene las ultimas 5 operaciones de un contrato sica concatenadas por comas
	 * 
	 * @param contratoSica
	 * @return
	 */
	public abstract String getLastFiveDealsCsv(String contratoSica);
	
	/**
	 * Obtiene el identificador del cliente dado el contrato sica
	 * 
	 * @param contratoSica
	 * @return
	 */
	public abstract Integer getIdClienteByContratoSica(String contratoSica);
	
	/**
	 * Actualiza el campo de las ultimas operaciones de un cliente
	 * 
	 * @param idCliente
	 * @param lastFiveDealsCsv
	 * @return
	 */
	public abstract boolean updateClienteLastFiveDeals(Integer idCliente, String lastFiveDealsCsv); 

}
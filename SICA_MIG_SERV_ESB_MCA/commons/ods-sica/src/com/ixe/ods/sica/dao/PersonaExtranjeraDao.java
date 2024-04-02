package com.ixe.ods.sica.dao;

import net.sf.hibernate.HibernateException;

import com.ixe.ods.sica.model.PersonaExtranjera;

/**
 * The Interface PersonaExtranjeraDao.
 */
public interface PersonaExtranjeraDao {
	
	/**
	 * Buscar por id.
	 *
	 * @param idPersona the id persona
	 * @return the persona extranjera
	 */
	PersonaExtranjera buscarPorId(Integer idPersona);
	
	/**
	 * Buscar por id.
	 *
	 * @param idExtranjero the id
	 * @return the persona extranjera
	 * @throws HibernateException the hibernate exception
	 */
	PersonaExtranjera buscarPorIdExtranjero(String id);
	
	/**
	 * Registrar.
	 *
	 * @param persona the persona
	 */
	void registrar(PersonaExtranjera persona);
	
	/**
	 * Actualizar.
	 *
	 * @param persona the persona
	 */
	void actualizar(PersonaExtranjera persona);
	
	/**
	 * Guardar.
	 *
	 * @param persona the persona
	 */
	void guardar(PersonaExtranjera persona);
	
	/**
	 * Removes the entidad sesion.
	 *
	 * @param persona the persona
	 */
	void removeEntidadSesion(PersonaExtranjera persona);
}

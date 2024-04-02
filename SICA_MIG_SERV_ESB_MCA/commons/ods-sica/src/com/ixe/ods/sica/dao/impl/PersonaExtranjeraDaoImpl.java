package com.ixe.ods.sica.dao.impl;

import java.util.List;

import net.sf.hibernate.HibernateException;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import com.ixe.ods.sica.dao.PersonaExtranjeraDao;
import com.ixe.ods.sica.model.PersonaExtranjera;

/**
 * The Class PersonaExtranjeraDaoImpl.
 */
public class PersonaExtranjeraDaoImpl extends HibernateDaoSupport implements
		PersonaExtranjeraDao {

	/**
	 * Buscar por id.
	 *
	 * @param idPersona the id persona
	 * @return the persona extranjera
	 * @throws HibernateException the hibernate exception
	 */
	public PersonaExtranjera buscarPorId(Integer idPersona) {
		return (PersonaExtranjera) this.getHibernateTemplate().
				get(PersonaExtranjera.class, idPersona);
	}

	/**
	 * Buscar por id extranjero.
	 *
	 * @param idExtranjero the id extranjero
	 * @return the persona extranjera
	 */
	public PersonaExtranjera buscarPorIdExtranjero(String id) {
		PersonaExtranjera persona = null;
		StringBuffer hql = new StringBuffer("From PersonaExtranjera PE Where PE.idExtranjero = :id");
		List personas = this.getHibernateTemplate().findByNamedParam(hql.toString(), "id", id);
		if (personas != null && !personas.isEmpty()) {
			persona = (PersonaExtranjera) personas.get(0);
		}
		
		return persona;
	}
	
	/**
	 * Registrar.
	 *
	 * @param persona the persona
	 */
	public void registrar(PersonaExtranjera persona) {
		this.getHibernateTemplate().save(persona);
	}

	/**
	 * Actualizar.
	 *
	 * @param persona the persona
	 */
	public void actualizar(PersonaExtranjera persona) {
		this.getHibernateTemplate().saveOrUpdateCopy(persona);
	}
	
	/**
	 * Guardar.
	 *
	 * @param persona the persona
	 */
	public void guardar(PersonaExtranjera persona) {
		this.getHibernateTemplate().saveOrUpdate(persona);
	}
	
	/**
	 * Removes the entidad sesion.
	 *
	 * @param persona the persona
	 */
	public void removeEntidadSesion(PersonaExtranjera persona) {
		this.getHibernateTemplate().evict(persona);
	}

}

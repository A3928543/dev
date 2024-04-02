package com.banorte.ods.sica.batch.transfint.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * The Class BaseDao.
 */
public class BaseDao {

	/**
	 * Instantiates a new base dao.
	 */
	public BaseDao() {
	}
	
	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Gets the entity manager.
	 *
	 * @return the entity manager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Sets the entity manager.
	 *
	 * @param entityManager the new entity manager
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}

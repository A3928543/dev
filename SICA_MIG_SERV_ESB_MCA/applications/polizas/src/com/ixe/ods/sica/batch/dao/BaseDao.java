package com.ixe.ods.sica.batch.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
}

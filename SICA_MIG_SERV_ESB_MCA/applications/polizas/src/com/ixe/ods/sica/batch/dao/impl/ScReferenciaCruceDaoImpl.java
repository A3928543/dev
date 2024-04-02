package com.ixe.ods.sica.batch.dao.impl;

import java.util.Date;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.batch.dao.BaseDao;
import com.ixe.ods.sica.batch.dao.ScReferenciaCruceDao;
import com.ixe.ods.sica.batch.domain.ScReferenciaCruce;

@Repository("scReferenciaCruceDao")
public class ScReferenciaCruceDaoImpl extends BaseDao implements ScReferenciaCruceDao {
	
	private Logger LOG = LoggerFactory.getLogger(ScReferenciaCruceDaoImpl.class);

	public ScReferenciaCruceDaoImpl() {
	}
   
	public void saveScReferenciaCruce(ScReferenciaCruce referencia) {
		this.getEntityManager().persist(referencia);
	}
	
	public ScReferenciaCruce findScReferenciaCruceById(Long id) {
		return this.getEntityManager().find(ScReferenciaCruce.class, id);
	}
	
	public ScReferenciaCruce findScReferenciaCruceByReferencia(Date fechaCont, String refCruce) {
		StringBuilder jpql = new StringBuilder("Select R From ScReferenciaCruce R ");
		jpql.append("Where R.fechaCont = :fechaCont And R.referenciaCruce = :refCruce");
		LOG.info("findScReferenciaCruceByReferencia({}, {})", fechaCont, refCruce);
		Query query = this.getEntityManager().createQuery(jpql.toString());
		query.setParameter("fechaCont", fechaCont);
		query.setParameter("refCruce", refCruce);
		
		return (ScReferenciaCruce) query.getSingleResult();
	}
	
	
	public ScReferenciaCruce findScReferenciaCruceWithRefCruce(Date fechaCont, String refCruce) {
		StringBuilder jpql = new StringBuilder("Select R From ScReferenciaCruce R ");
		jpql.append("Where R.fechaCont = :fechaCont And R.referenciaCruce Like '");
		jpql.append(refCruce);
		jpql.append("%'");
		LOG.info("findScReferenciaCruceWithRefCruce({}, {})", fechaCont, refCruce);
		Query query = this.getEntityManager().createQuery(jpql.toString());
		query.setParameter("fechaCont", fechaCont);
		
		return (ScReferenciaCruce) query.getSingleResult();
	}
	
}

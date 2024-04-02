package com.ixe.ods.sica.batch.dao.impl;

import java.math.BigInteger;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.batch.dao.BaseDao;
import com.ixe.ods.sica.batch.dao.ScDealDao;
import com.ixe.ods.sica.batch.domain.ScDeal;

@Repository("scDealDao")
public class ScDealDaoImpl extends BaseDao implements ScDealDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(ScDealDaoImpl.class);
		
	public ScDeal findScDealById(BigInteger id) {
		LOG.debug("findScDealById({})", id);
		return getEntityManager().find(ScDeal.class, id);
	}
	
	public Object[] findSpecificInfoScDealbyId(BigInteger id) {
		Object[] deal = null;
		StringBuilder jpql = new StringBuilder("Select D.noCuenta, D.fechaCaptura ");
		jpql.append("From ScDeal D Where D.idDeal = :idDeal");
		try {
			Query query = getEntityManager().createQuery(jpql.toString());
			query.setParameter("idDeal", id);
			LOG.debug("findSpecificInfoScDealbyId({})", id);
			deal = (Object[]) query.getSingleResult();
		} catch (NoResultException ex) {//Para personalizar mensaje
			StringBuilder msg = new StringBuilder("No se encontr\u00F3 informaci\u00F3n registrada ");
			msg.append("para el deal ");
			msg.append(id);
			throw new NoResultException(msg.toString());
		}
		
		return deal; 
	}
}

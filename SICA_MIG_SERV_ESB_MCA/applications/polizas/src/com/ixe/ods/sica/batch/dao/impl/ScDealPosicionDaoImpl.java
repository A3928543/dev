package com.ixe.ods.sica.batch.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.batch.dao.BaseDao;
import com.ixe.ods.sica.batch.dao.ScDealPosicionDao;
import com.ixe.ods.sica.batch.domain.ScDealPosicion;

@Repository("scDealPosicionDao")
public class ScDealPosicionDaoImpl extends BaseDao implements ScDealPosicionDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(ScDealPosicionDaoImpl.class);
	
	public ScDealPosicion findScDealPosicionById(BigInteger id) {
		LOG.debug("findScDealPosicionById({})", id);
		
		return getEntityManager().find(ScDealPosicion.class, id);
	}
	
	public BigDecimal findTipoCambioScDealPosicionById(BigInteger id) {
		BigDecimal tipoCambio = null;
		StringBuilder jpql = new StringBuilder("Select P.tipoCambio From ScDealPosicion P ");
		jpql.append("Where P.idDealPosicion = :id");
		Query query = getEntityManager().createQuery(jpql.toString());
		query.setParameter("id", id);
		LOG.debug("findTipoCambioScDealPosicionById({})", id);
		try {
			tipoCambio = (BigDecimal) query.getSingleResult();
		}
		catch (NoResultException ex) {//Un mensaje mss claro
			throw new NoResultException("No se encontr\u00F3 informaci\u00F3n registrada del tipo " +
										"de cambio para el identificador de posici\u00F3n " + id);
		}
		
		return tipoCambio;
	}
}

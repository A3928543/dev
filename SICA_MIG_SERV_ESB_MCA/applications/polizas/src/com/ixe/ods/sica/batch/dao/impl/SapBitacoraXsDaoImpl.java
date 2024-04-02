package com.ixe.ods.sica.batch.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.batch.dao.BaseDao;
import com.ixe.ods.sica.batch.dao.SapBitacoraXsDao;
import com.ixe.ods.sica.batch.domain.SapABitacoraXs;

@Repository("sapBitacoraXsDao")
public class SapBitacoraXsDaoImpl extends BaseDao implements SapBitacoraXsDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(SapBitacoraXsDaoImpl.class);

	public SapABitacoraXs findSapBitacoraXsById(Long id) {
		return getEntityManager().find(SapABitacoraXs.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<SapABitacoraXs> findSapABitacoraXs(Long idCarga) {
		LOG.debug("findSapABitacoraXs({})", idCarga);
		StringBuilder jpql = new StringBuilder("Select B From SapABitacoraXs B ");
		jpql.append("Where B.idCarga > :idCarga Order by B.idCarga");
		Query query = getEntityManager().createQuery(jpql.toString());
		query.setParameter("idCarga", idCarga);
		List<SapABitacoraXs> list = query.getResultList();
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<SapABitacoraXs> findSapABitacoraXs(Long ini, Long fin) {
		LOG.debug("findSapABitacoraXs({}, {})", ini, fin);
		StringBuilder jpql = new StringBuilder("Select B From SapABitacoraXs B ");
		jpql.append("Where B.idCarga Between :ini and :fin Order by B.idCarga");
		Query query = getEntityManager().createQuery(jpql.toString());
		query.setParameter("ini", ini);
		query.setParameter("fin", fin);
		
		return query.getResultList();
	}
	

}

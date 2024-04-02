package com.ixe.ods.sica.batch.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.batch.dao.BaseDao;
import com.ixe.ods.sica.batch.dao.SapGenpolXsDao;
import com.ixe.ods.sica.batch.domain.SapAGenpolXs;
import com.ixe.ods.sica.batch.domain.SapAGenpolXsPK;

@Repository("sapGenpolXsDao")
public class SapGenpolXsDaoImpl extends BaseDao implements SapGenpolXsDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(SapGenpolXsDaoImpl.class);

	@SuppressWarnings("unchecked")
	public List<SapAGenpolXs> findSapAGenpolXsByLote(Long idLote) {
		String jql = "SELECT s FROM SapAGenpolXs s WHERE s.loteCarga = :loteCarga";
		Query query = getEntityManager().createQuery(jql);
		query.setParameter("loteCarga", idLote);
		
		return query.getResultList();
	}
	
	public SapAGenpolXs findSapAGenpolXsById(String referencia, BigDecimal idRegistro) {
		LOG.debug("findSapAGenpolXsById({}, {})", referencia, idRegistro);
		SapAGenpolXsPK pk = new SapAGenpolXsPK(referencia, idRegistro);
		SapAGenpolXs sapAGenpolXs = getEntityManager().find(SapAGenpolXs.class, pk);
		if (sapAGenpolXs == null) {
			StringBuilder msg = new StringBuilder("No existe informaci\u00F3n en SapAGenpolXs ");
			msg.append("para el idRegistro ");
			msg.append(pk);
			throw new NoResultException(msg.toString());
		}
		
		return sapAGenpolXs;
	}
	
	public SapAGenpolXs findSapAGenpolXsById(SapAGenpolXsPK pk) {
		LOG.debug("findSapAGenpolXsById({})", pk);
		
		return getEntityManager().find(SapAGenpolXs.class, pk);
	}
	
	@SuppressWarnings("unchecked")
	public List<SapAGenpolXsPK> findSapAGenpolXsIdsByLote(Long idLote) {
		StringBuilder jpql = new StringBuilder("Select S.sapAGenpolXsPK From SapAGenpolXs S ");
		jpql.append("Where S.loteCarga = :loteCarga Order By S.sapAGenpolXsPK.idRegistro");
		Query query = getEntityManager().createQuery(jpql.toString());
		query.setParameter("loteCarga", idLote);
		
		return query.getResultList();
	}
}

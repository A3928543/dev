package com.ixe.ods.sica.batch.dao.impl;

import java.math.BigInteger;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.batch.dao.BaseDao;
import com.ixe.ods.sica.batch.dao.ScDealDetalleDao;
import com.ixe.ods.sica.batch.domain.ScDealDetalle;

@Repository("scDealDetalleDao")
public class ScDealDetalleDaoImpl extends BaseDao implements ScDealDetalleDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(ScDealDetalleDaoImpl.class);

	public ScDealDetalleDaoImpl() {
	}
	
	public ScDealDetalle findScDealDetalleById(BigInteger id) {
		return this.getEntityManager().find(ScDealDetalle.class, id);
	}
	
	
	public ScDealDetalle findScDealDetalle(BigInteger idDeal, BigInteger idDetalleLiq) {
    	StringBuilder jpql = new StringBuilder("Select D From ScDealDetalle D ");
    	jpql.append("Where D.scDeal.idDeal = :idDeal And D.idLiquidacionDetalle = :idDetalleLiq");
    	LOG.info("findScDealDetalle({}, {})", idDeal, idDetalleLiq);
    	Query query = this.getEntityManager().createQuery(jpql.toString());
    	query.setParameter("idDeal", idDeal);
    	query.setParameter("idDetalleLiq", idDetalleLiq);
    	
    	return (ScDealDetalle) query.getSingleResult();
    }
}

package com.ixe.ods.sica.batch.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.batch.dao.BaseDao;
import com.ixe.ods.sica.batch.dao.ScReferenciaCruceDetalleDao;
import com.ixe.ods.sica.batch.domain.ScReferenciaCruceDetalle;

import static org.apache.commons.lang.StringUtils.removeEnd;

@Repository("scRefCruceDetalleDao")
public class ScReferenciaCruceDetalleDaoImpl extends BaseDao implements 
		ScReferenciaCruceDetalleDao {
	
	private static final Logger LOG = 
			LoggerFactory.getLogger(ScReferenciaCruceDetalleDaoImpl.class); 

	public ScReferenciaCruceDetalleDaoImpl() {
	}
	
	public ScReferenciaCruceDetalle findScReferenciaCruceDetalleById(Long id) {
		return this.getEntityManager().find(ScReferenciaCruceDetalle.class, id);
	}
	
	public void saveScReferenciaCruceDetalle(ScReferenciaCruceDetalle scRefDet) {
		this.getEntityManager().persist(scRefDet);
	}
	
	@SuppressWarnings("unchecked")
	public List<ScReferenciaCruceDetalle> findFoliosByIdRefCruce(Long idRefCruce, List<String> folios) {
		String inSql = "";
		StringBuilder jpql = new StringBuilder("Select D From ScReferenciaCruceDetalle D ");
		jpql.append("Where D.scReferenciaCruce.idReferenciaCruce = :idRefCruce ");
		jpql.append("And D.status = :status ");
		LOG.info("idRefCruce: {}", idRefCruce);
		if (!folios.isEmpty()) {
			for (String folio : folios) {
				inSql = inSql + "'" + folio + "',";
			}
			inSql = removeEnd(inSql, ",");
			LOG.info("folios: {}", inSql);
			jpql.append("And D.folioFiscal In (");
			jpql.append(inSql);
			jpql.append(")");
		}
		
		Query query = this.getEntityManager().createQuery(jpql.toString());
		query.setParameter("idRefCruce", idRefCruce);
		query.setParameter("status", ScReferenciaCruceDetalle.STATUS_ACTIVO);
		
		return query.getResultList();
	}
	
	public ScReferenciaCruceDetalle findScReferenciaCruceDetalleByIdRefCruceAndFolio(
			Long idRefCruce, String folio) {
		StringBuilder jpql = new StringBuilder("Select D From ScReferenciaCruceDetalle D ");
		jpql.append("Where D.scReferenciaCruce.idReferenciaCruce = :idRefCruce ");
		jpql.append("And D.folioFiscal = :folio");
		LOG.info("findScReferenciaCruceDetalleByIdRefCruceAndFolio({}, {})", idRefCruce, folio);
		Query query = this.getEntityManager().createQuery(jpql.toString());
		query.setParameter("idRefCruce", idRefCruce);
		query.setParameter("folio", folio);
		
		return (ScReferenciaCruceDetalle) query.getSingleResult();
	}
	
	public void update(ScReferenciaCruceDetalle detalle) {
		this.getEntityManager().merge(detalle);
	}

}

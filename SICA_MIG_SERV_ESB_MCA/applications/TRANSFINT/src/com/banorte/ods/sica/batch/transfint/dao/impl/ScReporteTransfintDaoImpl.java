package com.banorte.ods.sica.batch.transfint.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.banorte.ods.sica.batch.transfint.dao.BaseDao;
import com.banorte.ods.sica.batch.transfint.dao.ScReporteTransfintDao;
import com.banorte.ods.sica.batch.transfint.domain.ScReporteTransfint;

/**
 * The Class ScReporteTransfintDaoImpl.
 */
@Repository("scReporteTransfintDao")
public class ScReporteTransfintDaoImpl extends BaseDao implements ScReporteTransfintDao {

	/**
	 * Instantiates a new sc reporte transfint dao impl.
	 */
	public ScReporteTransfintDaoImpl() {
	}

	/**
	 * Save.
	 *
	 * @param transfint the transfint
	 */
	@Override
	public void save(ScReporteTransfint reporteTransfint) {
		this.getEntityManager().persist(reporteTransfint);
	}

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the sc reporte transfint
	 */
	@Override
	public ScReporteTransfint findById(Long idReporte) {
		StringBuilder jpql = new StringBuilder("Select A From ScReporteTransfint A ");
		jpql.append("Where A.idReporteTransfint = :idReporte");
		Query query = this.getEntityManager().createQuery(jpql.toString());
		query.setParameter("idReporte", idReporte);
		
		return (ScReporteTransfint) query.getSingleResult();
	}

	/**
	 * Update.
	 *
	 * @param reporteTransfint the reporte transfint
	 */
	@Override
	public void update(ScReporteTransfint reporteTransfint) {
		this.getEntityManager().merge(reporteTransfint);
	}

}

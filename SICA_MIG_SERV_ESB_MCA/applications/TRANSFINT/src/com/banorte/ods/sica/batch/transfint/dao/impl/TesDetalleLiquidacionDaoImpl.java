package com.banorte.ods.sica.batch.transfint.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.banorte.ods.sica.batch.transfint.dao.BaseDao;
import com.banorte.ods.sica.batch.transfint.dao.TesDetalleLiquidacionDao;
import com.banorte.ods.sica.batch.transfint.domain.TesDetalleLiquidacion;

/**
 * The Class TesDetalleLiquidacionDaoImpl.
 */
@Repository("TesDetalleLiquidacionDao")

public class TesDetalleLiquidacionDaoImpl extends BaseDao implements
		TesDetalleLiquidacionDao {
	
	/**
	 * Instantiates a new tes detale liquidaicon dao impl.
	 */
	public TesDetalleLiquidacionDaoImpl() {
	}

	/**
	 * Save.
	 *
	 * @param transfint the transfint
	 */
	@Override
	public void save(TesDetalleLiquidacion detalleLiquidacion) {
		this.getEntityManager().persist(detalleLiquidacion);
	}
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the tes detalle liquidacion
	 */
	@Override
	public TesDetalleLiquidacion findById(Long idDetalleLiquidacion) {
		// TODO Auto-generated method stub
		StringBuilder jpql = new StringBuilder("Select A From TesDetalleLiquidacion A ");
		jpql.append("Where A.idDetalleLiquidacion = :idDetalleLiquidacion");
		Query query = this.getEntityManager().createQuery(jpql.toString());
		query.setParameter("idDetalleLiquidacion", idDetalleLiquidacion);
		
		return (TesDetalleLiquidacion) query.getSingleResult();
	}
	
	/**
	 * Update.
	 *
	 * @param reporteTransfint the reporte transfint
	 */
	@Override
	public void update(TesDetalleLiquidacion detalleLiquidacion) {
		this.getEntityManager().merge(detalleLiquidacion);
	}

}

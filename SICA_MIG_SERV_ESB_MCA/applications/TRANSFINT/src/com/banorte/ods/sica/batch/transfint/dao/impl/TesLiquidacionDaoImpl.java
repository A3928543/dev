/**
 * 
 */
package com.banorte.ods.sica.batch.transfint.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.banorte.ods.sica.batch.transfint.dao.BaseDao;
import com.banorte.ods.sica.batch.transfint.dao.TesLiquidacionDao;
import com.banorte.ods.sica.batch.transfint.domain.TesLiquidacion;

/**
 * The Class TesDetalleLiquidacionDaoImpl.
 */
@Repository("TesLiquidacionDao")

public class TesLiquidacionDaoImpl extends BaseDao implements
		TesLiquidacionDao {

	/**
	 * Instantiates a new tes liquidaicon dao impl.
	 */
	public TesLiquidacionDaoImpl() {
	}

	/**
	 * Save.
	 *
	 * @param liquidacion the liquidacion
	 */
	@Override
	public void save(TesLiquidacion liquidacion) {
		this.getEntityManager().persist(liquidacion);
	}
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the tes liquidacion
	 */
	@Override
	public TesLiquidacion findById(Long idLiquidacion) {
		// TODO Auto-generated method stub
		StringBuilder jpql = new StringBuilder("Select A From TesLiquidacion A ");
		jpql.append("Where A.idLiquidacion = :idLiquidacion");
		Query query = this.getEntityManager().createQuery(jpql.toString());
		query.setParameter("idLiquidacion", idLiquidacion);
		
		return (TesLiquidacion) query.getSingleResult();
	}
	
	/**
	 * Update.
	 *
	 * @param reporteTransfint the reporte transfint
	 */
	@Override
	public void update(TesLiquidacion liquidacion) {
		this.getEntityManager().merge(liquidacion);
	}

}



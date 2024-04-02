package com.banorte.ods.sica.batch.transfint.dao.impl;

import org.springframework.stereotype.Repository;

import com.banorte.ods.sica.batch.transfint.dao.BaseDao;
import com.banorte.ods.sica.batch.transfint.dao.ScDetalleRepTransferintDao;
import com.banorte.ods.sica.batch.transfint.domain.ScDetalleRepTransfint;

/**
 * The Class ScDetalleRepTransferintDaoImpl.
 */
@Repository("scDetalleRepTransferintDao")
public class ScDetalleRepTransferintDaoImpl extends BaseDao implements ScDetalleRepTransferintDao {

	/**
	 * Instantiates a new sc detalle rep transferint dao impl.
	 */
	public ScDetalleRepTransferintDaoImpl() {
	}
	
	/**
	 * Save.
	 *
	 * @param detalle the detalle
	 */
	public void save(ScDetalleRepTransfint detalle) {
		this.getEntityManager().persist(detalle);
	}
	
}

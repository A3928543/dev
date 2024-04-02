package com.banorte.ods.sica.batch.transfint.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.banorte.ods.sica.batch.transfint.dao.BaseDao;
import com.banorte.ods.sica.batch.transfint.dao.BupFechaInhabilDao;
import com.banorte.ods.sica.batch.transfint.domain.BupFechaInhabil;


/**
 * The Class BupFechaInhabilDaoImpl.
 */
@Repository("bupFechaInhabilDao")
public class BupFechaInhabilDaoImpl extends BaseDao implements BupFechaInhabilDao {

	/**
	 * Instantiates a new bup fecha inhabil dao impl.
	 */
	public BupFechaInhabilDaoImpl() {
	}
	
	/**
	 * Find fecha inhabil.
	 *
	 * @param fecha the fecha
	 * @return the bup fecha inhabil
	 */
	public BupFechaInhabil findFechaInhabil(Date fecha) {
		return this.getEntityManager().find(BupFechaInhabil.class, fecha);
	}
	
}

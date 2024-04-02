package com.banorte.ods.sica.batch.transfint.dao;

import java.util.Date;

import com.banorte.ods.sica.batch.transfint.domain.BupFechaInhabil;

/**
 * The Interface BupFechaInhabilDao.
 */
public interface BupFechaInhabilDao {
	
	/**
	 * Find fecha inhabil.
	 *
	 * @param fecha the fecha
	 * @return the bup fecha inhabil
	 */
	BupFechaInhabil findFechaInhabil(Date fecha);

}

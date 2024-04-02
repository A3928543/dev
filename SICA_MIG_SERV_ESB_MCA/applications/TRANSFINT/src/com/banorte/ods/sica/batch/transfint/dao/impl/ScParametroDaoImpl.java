package com.banorte.ods.sica.batch.transfint.dao.impl;

import org.springframework.stereotype.Repository;

import com.banorte.ods.sica.batch.transfint.dao.BaseDao;
import com.banorte.ods.sica.batch.transfint.dao.ScParametroDao;
import com.banorte.ods.sica.batch.transfint.domain.ScParametro;

/**
 * The Class ScParametroDaoImpl.
 */
@Repository("scParametroDao")
public class ScParametroDaoImpl extends BaseDao implements ScParametroDao {

	/**
	 * Instantiates a new sc parametro dao impl.
	 */
	public ScParametroDaoImpl() {
	}
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the sc parametro
	 */
	public ScParametro findById(String id) {
		return this.getEntityManager().find(ScParametro.class, id);
	}

}

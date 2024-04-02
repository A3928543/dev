package com.ixe.ods.sica.batch.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.batch.dao.BaseDao;
import com.ixe.ods.sica.batch.dao.ScParametroDao;
import com.ixe.ods.sica.batch.domain.ScParametro;

/**
 * The Class ScParametroDaoImpl.
 */
@Repository("scParametroDao")
public class ScParametroDaoImpl extends BaseDao implements ScParametroDao {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ScParametroDaoImpl.class);
	
	/**
	 * Find sc parametro by id.
	 *
	 * @param id the id
	 * @return the sc parametro
	 */
	@Override
	public ScParametro findScParametroById(String id) {
		ScParametro scParametro = null;
		LOG.debug("findScParametrobyId({})", id);
		scParametro = getEntityManager().find(ScParametro.class, id);
		
		return scParametro;
	}
	
	/**
	 * Find sc parametro by prefijo.
	 *
	 * @param prefijo the prefijo
	 * @return the list
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ScParametro> findScParametroByPrefijo(String prefijo) {
		StringBuilder jpql = new StringBuilder("Select P From ScParametro ");
		jpql.append("P Where P.idParametro like '");
		jpql.append(prefijo);
		jpql.append("%'");
		LOG.debug("Buscando parametros que inicien: {}", prefijo);
		Query query = getEntityManager().createQuery(jpql.toString());
		List<ScParametro> list = query.getResultList();
		if (list != null && !list.isEmpty()) {
			LOG.debug("Parametros encontrados: {}", list.size());
		}
		
		return list;
	}
}

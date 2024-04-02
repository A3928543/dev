package com.ixe.ods.sica.batch.dao.impl;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.batch.dao.BaseDao;
import com.ixe.ods.sica.batch.dao.ScLoteProcesadoDao;
import com.ixe.ods.sica.batch.domain.ScLoteProcesado;

@Repository("scLoteProcesadoDao")
public class ScLoteProcesadoDaoImpl extends BaseDao implements ScLoteProcesadoDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(ScLoteProcesadoDaoImpl.class);
	
	public void saveScLoteProcesado(ScLoteProcesado lote) {
		this.getEntityManager().persist(lote);
	}
	
	public ScLoteProcesado findScLoteProcesadoById(Long id) {
		LOG.debug("findScLoteProcesadoById({})", id);
		return this.getEntityManager().find(ScLoteProcesado.class, id);
	}
	
	public Long findMaxLoteProcesado() {
		Long idLoteProcesado = null;
		StringBuilder jpql = new StringBuilder("Select Max(L.idCarga) ");
		jpql.append("From ScLoteProcesado L");
		Query query = getEntityManager().createQuery(jpql.toString());
		idLoteProcesado = (Long) query.getSingleResult();
		LOG.debug("idLoteProcesado: {}", idLoteProcesado);
		
		return idLoteProcesado;
	}

}

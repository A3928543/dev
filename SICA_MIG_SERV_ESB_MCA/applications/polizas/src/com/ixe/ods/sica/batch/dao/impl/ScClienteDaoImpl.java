package com.ixe.ods.sica.batch.dao.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.batch.dao.BaseDao;
import com.ixe.ods.sica.batch.dao.ScClienteDao;
import com.ixe.ods.sica.batch.domain.ScCliente;

@Repository("scClienteDao")
public class ScClienteDaoImpl extends BaseDao implements ScClienteDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(ScClienteDaoImpl.class);
	
	public ScCliente findScClienteById(Integer id) {
		LOG.debug("findScClienteById({})", id);
		return getEntityManager().find(ScCliente.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public String findSicByIdPersona(BigInteger idPersona) {
		String sic = "";
		StringBuffer jpql = new StringBuffer("Select C.sic From ScCliente C ");
		jpql.append("Where C.idPersona = :idPersona");
		Query query = getEntityManager().createQuery(jpql.toString());
		query.setParameter("idPersona", idPersona);
		LOG.debug("findSicByIdPersona({})", idPersona);
		List<String> list = query.getResultList();
		if (!list.isEmpty()) {
			sic = list.get(0);
		}
		else {
			StringBuilder msg = new StringBuilder("No se encontro la informaci\u00F3n de ");
			msg.append("SIC para la persona ").append(idPersona);
			throw new NoResultException(msg.toString());
		}
		
		return sic;
	}
	
	@SuppressWarnings("unchecked")
	public ScCliente findScClienteByIdPersona(BigInteger idPersona) {
		ScCliente cliente = null;
		StringBuffer jpql = new StringBuffer("Select C From ScCliente C ");
		jpql.append("Where C.idPersona = :idPersona");
		Query query = getEntityManager().createQuery(jpql.toString());
		query.setParameter("idPersona", idPersona);
		LOG.debug("findSicByIdPersona({})", idPersona);
		List<ScCliente> list = query.getResultList();
		if (!list.isEmpty()) {
			cliente = list.get(0);
		}
		else {
			StringBuilder msg = new StringBuilder("No se encontro la informaci\u00F3n del ");
			msg.append("cliente para la persona ").append(idPersona);
			throw new NoResultException(msg.toString());
		}
		
		return cliente;
	}
}

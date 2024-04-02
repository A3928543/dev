package com.ixe.ods.sica.batch.dao.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.batch.dao.BaseDao;
import com.ixe.ods.sica.batch.dao.BupPersonaCuentaRolDao;
import com.ixe.ods.sica.batch.domain.BupPersona;
import com.ixe.ods.sica.batch.domain.BupPersonaCuentaRol;
import com.ixe.ods.sica.batch.domain.BupPersonaCuentaRolPK;

@Repository("bupPersonaCuentaRolDao")
public class BupPersonaCuentaRolDaoImpl extends BaseDao implements BupPersonaCuentaRolDao {
	
	public BupPersonaCuentaRol findBupPersonaCuentaRolById(BigInteger idPersona, 
			String noCuenta, String idRol) {
		BupPersonaCuentaRolPK pk = new BupPersonaCuentaRolPK(idPersona, noCuenta, idRol);
		
		return getEntityManager().find(BupPersonaCuentaRol.class, pk);
	}
	
	@SuppressWarnings("unchecked")
	public BupPersona findPersonaByCuentaRol(String noCuenta, String idRol) {
		BupPersona bupPersona = null;
		StringBuilder jpql = new StringBuilder("Select C From BupPersonaCuentaRol C ");
		jpql.append("Where C.noCuenta = :noCuenta And C.idRol = :idRol");
		Query query = getEntityManager().createQuery(jpql.toString());
		query.setParameter("noCuenta", noCuenta);
		query.setParameter("idRol", idRol);
		List<BupPersonaCuentaRol> list = query.getResultList();
		if (!list.isEmpty()) {
			bupPersona = list.get(0).getBupPersona();
		}
		
		return bupPersona;
	}
	
	@SuppressWarnings("unchecked")
	public String findTipoPersonaTitularByCuenta(String noCuenta) {
		String tipoPersona = "";
		StringBuilder jpql = new StringBuilder("Select C.bupPersona.tipoPersona ");
		jpql.append("From BupPersonaCuentaRol C Where C.noCuenta = :noCuenta And ");
		jpql.append("C.idRol = :idRol");
		Query query = getEntityManager().createQuery(jpql.toString());
		query.setParameter("noCuenta", noCuenta);
		query.setParameter("idRol", ID_ROL_TITULAR);
		List<String> tipos = query.getResultList();
		if (!tipos.isEmpty()) {
			tipoPersona = tipos.get(0);
		}
		else {
			StringBuilder msg = new StringBuilder("No se encontr\u00F3 el tipo de ");
			msg.append("persona para cuenta ");
			msg.append(noCuenta);
			throw new NoResultException(msg.toString());
		}
		
		return tipoPersona;
	}
	
	@SuppressWarnings("unchecked")
	public Object[] findInfoPersonaTitularByCuenta(String noCuenta) {
		Object[] info = null;
		StringBuilder jpql = new StringBuilder("Select C.bupPersona.idPersona, ");
		jpql.append("C.bupPersona.tipoPersona From BupPersonaCuentaRol C Where ");
		jpql.append("C.noCuenta = :noCuenta And C.idRol = :idRol");
		Query query = getEntityManager().createQuery(jpql.toString());
		query.setParameter("noCuenta", noCuenta);
		query.setParameter("idRol", ID_ROL_TITULAR);
		List<Object[]> list = query.getResultList();
		if (!list.isEmpty()) {
			info = list.get(0);
		}
		else {
			StringBuilder msg = new StringBuilder("No se encontr\u00F3 informaci\u00F3n registrada ");
			msg.append("de la persona para cuenta ");
			msg.append(noCuenta);
			throw new NoResultException(msg.toString());
		}
		
		return info;
	}
}

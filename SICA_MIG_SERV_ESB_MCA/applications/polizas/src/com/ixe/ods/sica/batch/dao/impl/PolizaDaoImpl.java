package com.ixe.ods.sica.batch.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.batch.dao.BaseDao;
import com.ixe.ods.sica.batch.dao.PolizaDao;
import com.ixe.ods.sica.batch.domain.ScPoliza;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

@Repository("polizaDao")
public class PolizaDaoImpl extends BaseDao implements PolizaDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(PolizaDaoImpl.class);
	
	public ScPoliza findScPolizaById(BigDecimal id) {
		LOG.debug("findScPolizaById({})", id);
		return getEntityManager().find(ScPoliza.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<ScPoliza> findScPolizasBySapReferencia(String ref) {
		String jpql = "Select P From ScPoliza P Where P.sapReferencia = :ref";
		Query query = getEntityManager().createQuery(jpql);
		query.setParameter("ref", ref);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Object[] findPolizaInfoBySapReferencia(String ref) {
		Object[] poliza = null;
		StringBuilder jpql = new StringBuilder("Select P.idPoliza, P.referencia, P.idDeal, ");
		jpql.append("P.idDealPosicion From ScPoliza P Where P.sapReferencia = :ref");
		Query query = this.getEntityManager().createQuery(jpql.toString());
		query.setParameter("ref", ref);
		List<Object[]> list = query.getResultList();
		if (!list.isEmpty()) {
			poliza = list.get(0);
		}
		else {
			StringBuilder msg = new StringBuilder("No existe informaci\u00F3n registrada ");
			msg.append("para la poliza con sap_referencia ");
			msg.append(ref);
			throw new NoResultException(ref);
		}
		
		return poliza;
	}
	
	@SuppressWarnings("unchecked")
	public Object[] findPolizaInfoBySapAGenPolXS(BigInteger idDeal, String sapRef, 
			String cargoAbono, String cuentaContable, String idDivisa) {
		Object[] poliza = null;
		StringBuilder jpql = new StringBuilder("Select P.idPoliza, P.referencia, P.idDeal, ");
		jpql.append("P.idDealPosicion From ScPoliza P Where P.idDeal = :idDeal And ");
		jpql.append("P.sapReferencia = :sapRef And P.cargoAbono = :cargoAbono And ");
		jpql.append("P.cuentaContable = :cuentaContable ");
		if (isNotEmpty(idDivisa)) {
			jpql.append("And P.scDivisa.idDivisa = :idDivisa");
		}
		Query query = this.getEntityManager().createQuery(jpql.toString());
		query.setParameter("idDeal", idDeal);
		query.setParameter("sapRef", sapRef);
		query.setParameter("cargoAbono", cargoAbono);
		query.setParameter("cuentaContable", cuentaContable);
		if (isNotEmpty(idDivisa)) {
			query.setParameter("idDivisa", idDivisa);
		}
		Object[] prints = new Object[]{idDeal, sapRef, cargoAbono, cuentaContable, idDivisa};
		LOG.debug("findPolizaInfoBySapAGenPolXS('{}', '{}', '{}', '{}', '{}')", prints);
		List<Object[]> list = query.getResultList();
		LOG.debug("Registros en poliza: {}", list.size());
		if (!list.isEmpty()) {
			poliza = list.get(0);
		}
		else {
			StringBuilder msg = new StringBuilder("No existe informaci\u00F3n registrada ");
			msg.append("para la poliza.");
			throw new NoResultException(msg.toString());
		}
		
		return poliza;
	}
	
	
	/*@SuppressWarnings("unchecked")
	public Object[] findPolizaInfoBySapAGenpolXs(SapAGenpolXs genpolXs) {
		Object[] poliza = null;
		StringBuilder jpql = new StringBuilder("Select P.idPoliza, P.referencia, P.idDeal ");
		jpql.append("From ScPoliza P Where P.sapReferencia = :ref And P.");
		Query query = this.getEntityManager().createQuery(jpql.toString());
		query.setParameter("ref", ref);
		List<Object[]> list = query.getResultList();
		if (!list.isEmpty()) {
			poliza = list.get(0);
		}
		else {
			StringBuilder msg = new StringBuilder("No existe informaci\u00F3n registrada ");
			msg.append("para la poliza con sap_referencia ");
			msg.append(ref);
			throw new NoResultException(ref);
		}
		
		return poliza;
	}*/
}

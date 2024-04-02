package com.ixe.ods.sica.batch.dao.impl;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.batch.dao.BaseDao;
import com.ixe.ods.sica.batch.dao.ScCifrasControlDao;
import com.ixe.ods.sica.batch.domain.ScCifrasControlCfdi;
import com.ixe.ods.sica.batch.domain.ScControlProcesoCifras;

/**
 * The Class ScCifrasControlDaoImpl.
 */
@Repository("scCifrasControlDao")
public class ScCifrasControlDaoImpl extends BaseDao implements ScCifrasControlDao {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ScCifrasControlDaoImpl.class);

	/**
	 * Instantiates a new sc cifras control dao impl.
	 */
	public ScCifrasControlDaoImpl() {
	}

	/**
	 * Save sc cifras control.
	 *
	 * @param cifras the cifras
	 */
	@Override
	public void saveScCifrasControl(ScCifrasControlCfdi cifras) {
		LOG.debug("saveScCifrasControl(): {}", cifras);
		this.getEntityManager().persist(cifras);
		LOG.debug("cifras.getIdCifrasControl(): {}", cifras.getIdCifrasControl());
	}

	/**
	 * Update sc cifras control.
	 *
	 * @param cifras the cifras
	 */
	@Override
	public void updateScCifrasControl(ScCifrasControlCfdi cifras) {
		LOG.debug("updateScCifrasControl(): {}", cifras);
		this.getEntityManager().merge(cifras);
	}

	/**
	 * Find sc cifras control by fecha.
	 *
	 * @param fecha the fecha
	 * @return the sc cifras control cfdi
	 */
	@Override
	public ScCifrasControlCfdi findScCifrasControlByFecha(String fecha) {
		StringBuilder jql = new StringBuilder("Select C From ScCifrasControlCfdi C ");
		jql.append("Where C.fechaRecepcionStr = :fechaRecepcion");
		LOG.debug("findScCifrasControlByFecha(): {}", fecha);
		Query query = this.getEntityManager().createQuery(jql.toString());
		query.setParameter("fechaRecepcion", fecha);
		
		return (ScCifrasControlCfdi) query.getSingleResult();
	}

	/**
	 * Find sc cifras control by id.
	 *
	 * @param id the id
	 * @return the sc cifras control cfdi
	 */
	@Override
	public ScCifrasControlCfdi findScCifrasControlById(Long id) {
		LOG.debug("findScCifrasControlById(): {}", id);
		return this.getEntityManager().find(ScCifrasControlCfdi.class, id);
	}
	
	/**
	 * Save sc control proceso cifras.
	 *
	 * @param control the control
	 */
	@Override
	public void saveScControlProcesoCifras(ScControlProcesoCifras control) {
		this.getEntityManager().persist(control);
	}
	
	/**
	 * Update sc control proceso cifras.
	 *
	 * @param control the control
	 */
	@Override
	public void updateScControlProcesoCifras(ScControlProcesoCifras control) {
		this.getEntityManager().merge(control);
	}
	
	/**
	 * Delete sc control proceso cifras.
	 */
	@Override
	public void deleteScControlProcesoCifras() {
		StringBuilder sql = new StringBuilder("Delete From SC_CONTROL_PROCESO_CIFRAS");
		Query query = this.getEntityManager().createNativeQuery(sql.toString());
		
		int rows = query.executeUpdate();
		LOG.debug("Registros eliminados: {}", rows);
	}
	
	/**
	 * Find sc control proceso cifras.
	 *
	 * @return the sc control proceso cifras
	 */
	@Override
	public ScControlProcesoCifras findScControlProcesoCifras() {
		StringBuilder jpql = new StringBuilder("Select P From ScControlProcesoCifras P");
		Query query = this.getEntityManager().createQuery(jpql.toString());
		
		return (ScControlProcesoCifras) query.getSingleResult();
	}
}

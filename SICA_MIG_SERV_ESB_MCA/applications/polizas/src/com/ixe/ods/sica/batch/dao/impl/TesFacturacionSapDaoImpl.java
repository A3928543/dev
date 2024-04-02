package com.ixe.ods.sica.batch.dao.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.batch.dao.BaseDao;
import com.ixe.ods.sica.batch.dao.TesFacturacionSapDao;
import com.ixe.ods.sica.batch.domain.TesFacturacionSap;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * The Class TesFacturacionSapDaoImpl.
 */
@Repository("tesFacturacionSapDao")
public class TesFacturacionSapDaoImpl extends BaseDao implements TesFacturacionSapDao {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(TesFacturacionSapDaoImpl.class);

	/**
	 * Instantiates a new tes facturacion sap dao impl.
	 */
	public TesFacturacionSapDaoImpl() {
	}
	
	/**
	 * Find tes facturacion sap by id.
	 *
	 * @param id the id
	 * @return the tes facturacion sap
	 */
	public TesFacturacionSap findTesFacturacionSapById(BigInteger id) {
		return this.getEntityManager().find(TesFacturacionSap.class, id);
	}
	
	/**
	 * Find tes facturacion sap.
	 *
	 * @param param the param
	 * @return the tes facturacion sap
	 */
	public TesFacturacionSap findTesFacturacionSap(Map<String, Object> param) {
		TesFacturacionSap facturacionSap = null;
		String keyDeal    = "idDeal";
		String keyDetalle = "idDetalleLiq";
		String keyFolio   = "folioFiscal";
		String KeyStatus  = "status";
		LOG.debug("param: {}", param);
		StringBuilder jpql = new StringBuilder("Select F From TesFacturacionSap F ");
		jpql.append("Where F.idDeal = :idDeal And F.tesDetalleLiquidacion.");
		jpql.append("idDetalleLiquidacion = :idDetalleLiq And F.folioFiscalFactura = ");
		jpql.append(":folioFiscal And F.estatusFactura = :status");
		Query query = this.getEntityManager().createQuery(jpql.toString());
		query.setParameter(keyDeal, param.get(keyDeal));
		query.setParameter(keyDetalle, param.get(keyDetalle));
		query.setParameter(keyFolio, param.get(keyFolio));
		query.setParameter(KeyStatus, param.get(KeyStatus));
		facturacionSap = (TesFacturacionSap) query.getSingleResult();
		
		return facturacionSap;
	}
	
	/**
	 * Find tes facturacion sap by folio fiscal.
	 *
	 * @param idDeal the id deal
	 * @param idDetLiq the id det liq
	 * @param folio the folio
	 * @return the tes facturacion sap
	 */
	public TesFacturacionSap findTesFacturacionSapByFolioFiscal(BigInteger idDeal, 
			BigInteger idDetLiq, String folio) {
		StringBuilder jpql = new StringBuilder("Select F From TesFacturacionSap F ");
		jpql.append("Where F.idDeal = :idDeal And F.tesDetalleLiquidacion.");
		jpql.append("idDetalleLiquidacion = :idDetLiq And F.folioFiscalFactura = ");
		jpql.append(":folio And F.estatusFactura =:status");
		Query query = this.getEntityManager().createQuery(jpql.toString());
		query.setParameter("idDeal", idDeal);
		query.setParameter("idDetLiq", idDetLiq);
		query.setParameter("folio", folio);
		query.setParameter("status", TesFacturacionSap.STATUS_FACT);
		
		return (TesFacturacionSap) query.getSingleResult();
	}
	
	/**
	 * Find tes facturacion sap.
	 *
	 * @param idDeal the id deal
	 * @param idDetLiq the id det liq
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public List<TesFacturacionSap> findTesFacturacionSap(BigInteger idDeal, BigInteger idDetLiq) {
		StringBuilder jpql = new StringBuilder("Select F From TesFacturacionSap F ");
		jpql.append("Where F.idDeal = :idDeal And F.tesDetalleLiquidacion.");
		jpql.append("idDetalleLiquidacion = :idDetalleLiq Order By F.idMovimiento Desc");
		Query query = this.getEntityManager().createQuery(jpql.toString());
		query.setParameter("idDeal", idDeal);
		query.setParameter("idDetalleLiq", idDetLiq);
		LOG.info("findTesFacturacion({}, {})", idDeal, idDetLiq);
		
		return query.getResultList();
	}
	
	/**
	 * Find max movimiento by deal detalle status.
	 *
	 * @param idDeal the id deal
	 * @param idDetLiq the id det liq
	 * @param status the status
	 * @return the big integer
	 */
	public BigInteger findMaxMovimientoByDealDetalleStatus(BigInteger idDeal, 
			BigInteger idDetLiq, String status) {
		Object[] printParam = new Object[]{idDeal, idDetLiq, status};
		LOG.info("findMaxMovimientoByDealDetalleStatus({}, {}, {})", printParam);
		StringBuilder jpql = new StringBuilder("Select T.idMovimiento From ");
		jpql.append("TesFacturacionSap T Where T.idDeal = :idDeal And ");
		jpql.append("T.tesDetalleLiquidacion.idDetalleLiquidacion = :idDetLiq And ");
		jpql.append("T.estatusFactura = :status");
		Query query = this.getEntityManager().createQuery(jpql.toString());
		query.setParameter("idDeal", idDeal);
		query.setParameter("idDetLiq", idDetLiq);
		query.setParameter("status", status);
		
		return (BigInteger) query.getSingleResult();
	}

	/**
	 * Find tes facturacion sap by folio fiscal.
	 *
	 * @param folioFiscal the folio fiscal
	 * @return the object[]
	 */
	public Object[] findTesFacturacionSapByFolioFiscal(String folioFiscal) {
		LOG.debug("findTesFacturacionSapByFolioFiscal({})", folioFiscal);
		StringBuilder jpql = new StringBuilder("Select F.idDeal, F.tesDetalleLiquidacion.");
		jpql.append("idDetalleLiquidacion, F.estatusFactura, F.idMovimiento, ");
		jpql.append("F.tesDetalleLiquidacion.fechaLiquidado, F.fechaLecturaSite ");
		jpql.append("From TesFacturacionSap F ");
		jpql.append("Where F.folioFiscalFactura = :folioFiscal" );
		Query query = this.getEntityManager().createQuery(jpql.toString()); 
		query.setParameter("folioFiscal", folioFiscal);
		
		return (Object[]) query.getSingleResult();
	}

	/**
	 * Find fecha ultimo cfdi recibido SAP.
	 *
	 * @param estatus the estatus
	 * @return the date
	 */
	@Override
	public Date findFechaUltimoCfdiRecibidoSAP(String estatus) {
		StringBuilder jql = new StringBuilder("Select Max(F.fechaLecturaSite) ");
		jql.append("From TesFacturacionSap F Where F.estatusFactura = :estatus ");
		jql.append("And folioFiscalFactura Is Not Null");
		Query query = this.getEntityManager().createQuery(jql.toString());
		query.setParameter("estatus", estatus);
		
		return (Date) query.getSingleResult();
	}
	
	/**
	 * Find cantidad cfdis recibidos SAP.
	 *
	 * @param ini the ini
	 * @param fin the fin
	 * @param estatus the estatus
	 * @return the long
	 */
	@Override
	public Long findCantidadCfdisRecibidosSAP(Date ini, Date fin, String estatus) {
		StringBuilder jql = new StringBuilder("Select Count(*) ");
		jql.append("From TesFacturacionSap F Where F.fechaLecturaSite Between :ini And :fin ");
		jql.append("And F.estatusFactura = :estatus And folioFiscalFactura Is Not Null");
		Object[] param = new Object[] {ini, fin, estatus};
		LOG.debug("findCantidadCfdisRecibidosSAP({}, {}, {}): ", param);
		Query query = this.getEntityManager().createQuery(jql.toString());
		query.setParameter("ini", ini, TIMESTAMP);
		query.setParameter("fin", fin, TIMESTAMP);
		query.setParameter("estatus", estatus);
		
		return (Long) query.getSingleResult();
	}
	
	/**
	 * Find cantidad cfdis recibidos arch indices.
	 *
	 * @param ini the ini
	 * @param fin the fin
	 * @param estatus the estatus
	 * @return the integer
	 */
	@Override
	public Long findCantidadCfdisRecibidosArchIndices(Date ini, Date fin, String estatus) {
		StringBuilder jql = new StringBuilder("Select Sum(F.recibidoArchIndices) ");
		jql.append("From TesFacturacionSap F Where F.fechaLecturaSite Between :ini And :fin ");
		jql.append("And F.estatusFactura = :estatus And folioFiscalFactura Is Not Null");
		Object[] param = new Object[] {ini, fin, estatus};
		LOG.debug("findCantidadCfdisRecibidosSAP({}, {}, {}): ", param);
		Query query = this.getEntityManager().createQuery(jql.toString());
		query.setParameter("ini", ini, TIMESTAMP);
		query.setParameter("fin", fin, TIMESTAMP);
		query.setParameter("estatus", estatus);
		
		return (Long) query.getSingleResult();
	}
	
	/**
	 * Encuentra los folios fiscales que no fueron recibidos en el archivo de indices.
	 *
	 * @param ini the ini
	 * @param fin the fin
	 * @param estatus the estatus
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findCfdisNoRecibidosArchIndices(Date ini, Date fin, String estatus) {
		StringBuilder jql = new StringBuilder("Select F.idLiquidacion, F.idDeal, ");
		jql.append("F.folioFiscalFactura From TesFacturacionSap F Where ");
		jql.append("F.fechaLecturaSite Between :ini And :fin And ");
		jql.append("F.recibidoArchIndices = :recibido And ");
		jql.append("F.estatusFactura = :estatus And F.folioFiscalFactura Is Not Null");
		Short recibido = 0;
		Object[] param = new Object[] {ini, fin, estatus};
		LOG.debug("Inicio: {} Fin: {} Estatus: {}", param);
		Query query = this.getEntityManager().createQuery(jql.toString());
		query.setParameter("ini", ini, TIMESTAMP);
		query.setParameter("fin", fin, TIMESTAMP);
		query.setParameter("recibido", recibido);
		query.setParameter("estatus", estatus);
		
		return query.getResultList();
	}
	
	/**
	 * Update cfdi recibido archivo indices.
	 *
	 * @param id the id
	 */
	@Override
	public int updateCfdiRecibidoArchivoIndices(BigInteger id) {
		Integer recibido = 1;
		StringBuilder sql = new StringBuilder("Update Tes_Facturacion_Sap ");
		sql.append("Set Recibido_Arch_Indices = :recibido Where Id_Movimiento = :id");
		LOG.debug("IdMovimiento: {}", id);
		Query query = this.getEntityManager().createNativeQuery(sql.toString());
		query.setParameter("recibido", recibido);
		query.setParameter("id", id);
		
		return query.executeUpdate();
	}
	
	/**
	 * Update no recibidos cfdis arch indices.
	 *
	 * @param ini the ini
	 * @param fin the fin
	 * @return the int
	 */
	public int updateNoRecibidosCfdisArchIndices(Date ini, Date fin) {
		String estatus = TesFacturacionSap.STATUS_FACT;
		Short noRecibido = new Short("0");
		StringBuilder sql = new StringBuilder("Update TES_FACTURACION_SAP ");
		sql.append("Set RECIBIDO_ARCH_INDICES = :recibido Where FECHA_LECTURA_SITE ");
		sql.append("Between :ini And :fin And ESTATUS_FACTURA = :estatus And ");
		sql.append("FOLIO_FISCAL_FACTURA Is Not Null");
		LOG.debug("updateNoRecibidosCfdisArchIndices({}, {}): ", ini, fin);
		Query query = this.getEntityManager().createNativeQuery(sql.toString());
		query.setParameter("recibido", noRecibido);
		query.setParameter("ini", ini, TIMESTAMP);
		query.setParameter("fin", fin, TIMESTAMP);
		query.setParameter("estatus", estatus);
		
		return query.executeUpdate();
	}

}

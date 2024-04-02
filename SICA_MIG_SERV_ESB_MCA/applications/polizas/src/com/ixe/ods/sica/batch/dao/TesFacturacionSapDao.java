package com.ixe.ods.sica.batch.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ixe.ods.sica.batch.domain.TesFacturacionSap;

/**
 * The Interface TesFacturacionSapDao.
 */
public interface TesFacturacionSapDao {
	
	/**
	 * Find tes facturacion sap by id.
	 *
	 * @param id the id
	 * @return the tes facturacion sap
	 */
	TesFacturacionSap findTesFacturacionSapById(BigInteger id);
	
	/**
	 * Find tes facturacion sap.
	 *
	 * @param param the param
	 * @return the tes facturacion sap
	 */
	TesFacturacionSap findTesFacturacionSap(Map<String, Object> param);
	
	/**
	 * Find tes facturacion sap.
	 *
	 * @param idDeal the id deal
	 * @param idDetLiq the id det liq
	 * @return the list
	 */
	List<TesFacturacionSap> findTesFacturacionSap(BigInteger idDeal, BigInteger idDetLiq);
	
	/**
	 * Find max movimiento by deal detalle status.
	 *
	 * @param idDeal the id deal
	 * @param idDetLiq the id det liq
	 * @param status the status
	 * @return the big integer
	 */
	BigInteger findMaxMovimientoByDealDetalleStatus(BigInteger idDeal, 
			BigInteger idDetLiq, String status);
	
	/**
	 * Find tes facturacion sap by folio fiscal.
	 *
	 * @param idDeal the id deal
	 * @param idDetLiq the id det liq
	 * @param folio the folio
	 * @return the tes facturacion sap
	 */
	TesFacturacionSap findTesFacturacionSapByFolioFiscal(BigInteger idDeal, 
			BigInteger idDetLiq, String folio);
	
	/**
	 * Find tes facturacion sap by folio fiscal.
	 *
	 * @param folioFiscal the folio fiscal
	 * @return the object[]
	 */
	Object[] findTesFacturacionSapByFolioFiscal(String folioFiscal);
	
	/**
	 * Find fecha ultimo cfdi recibido sap.
	 *
	 * @param estatus the estatus
	 * @return the date
	 */
	Date findFechaUltimoCfdiRecibidoSAP(String estatus);
	
	/**
	 * Find cantidad cfdis recibidos SAP.
	 *
	 * @param ini the ini
	 * @param fin the fin
	 * @param estatus the estatus
	 * @return the long
	 */
	Long findCantidadCfdisRecibidosSAP(Date ini, Date fin, String estatus);
	
	/**
	 * Find cantidad cfdis recibidos arch indices.
	 *
	 * @param ini the ini
	 * @param fin the fin
	 * @param estatus the estatus
	 * @return the integer
	 */
	Long findCantidadCfdisRecibidosArchIndices(Date ini, Date fin, String estatus);
	
	/**
	 * Find cfdis no recibidos arch indices.
	 *
	 * @param ini the ini
	 * @param fin the fin
	 * @param estatus the estatus
	 * @return the list
	 */
	List<Object[]> findCfdisNoRecibidosArchIndices(Date ini, Date fin, String estatus);
	
	
	/**
	 * Update cfdi recibido archivo indices.
	 *
	 * @param id the id
	 */
	int updateCfdiRecibidoArchivoIndices(BigInteger id);
	
	/**
	 * Update no recibidos cfdis arch indices.
	 *
	 * @param ini the ini
	 * @param fin the fin
	 * @return the int
	 */
	int updateNoRecibidosCfdisArchIndices(Date ini, Date fin);
}

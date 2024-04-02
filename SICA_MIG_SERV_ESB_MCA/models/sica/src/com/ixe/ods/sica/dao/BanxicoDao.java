/* 
 * $Id: BanxicoDao.java,v 1.6 2008/02/22 18:25:31 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2007 LegoSoft S.C.
 */
package com.ixe.ods.sica.dao;

import java.sql.Date;
import java.util.List;

/**
 * Interfaz para el dao de b&uacute;squeda de Detalles de Deal para el reporte de Banxico.
 * 
 * @author Gustavo Gonzalez (gonzalez)
 * @version $Revision: 1.6 $ $Date: 2008/02/22 18:25:31 $
 */
public interface BanxicoDao {

	/**
	 * Encuentra los detalles de deal capturados entre las fechas seleccionadas, incluyendo
	 * aquellos que se encuentren en estado de alta.
	 * 
	 * @param incioDia La fecha de incio del dia.
	 * @param finDia La fecha del fin del dia
	 * @return List.
	 */
	List findDetallesIncluyeStatusAlta(Date incioDia, Date finDia);
	
	/**
	 * Encuentra los detalles de deal capturados entre las fechas seleccionadas, excluyendo
	 * aquellos que se encuentren en estado de alta.  
	 * 
	 * @param incioDia La fecha de incio del dia.
	 * @param finDia La fecha del fin del dia.
	 * @return List.
	 */
	List findDetallesExcluyeStatusAlta(Date incioDia, Date finDia);
	
	/**
	 * Encuentra los detalles de deal capturados entre las fechas seleccionadas
	 * para el cliente Ixe Forward. 
	 * 
     * @param idPersonaForward El id persona de Ixe Forward.
     * @param incioDia La fecha de incio del dia.
     * @param finDia La fecha del fin del dia.
	 * @return List.
	 */
	List findDetallesForwards(Integer idPersonaForward, Date incioDia, Date finDia);
	
	/**
	 * Obtiene el monto total de las operaciones de sectores que componen el RESMERCA
	 * por divisa y operacion, para el reporte de banxico.
	 * 
	 * @param incioDia La fecha de incio del dia.
	 * @param finDia La fecha del fin del dia.
	 * @param idDivisa El id de la divisa.
	 * @param sectorCasasDeBolsa La descripcion del sector Casas de Bolsa.
	 * @param sectorEmpresas La descripcion del sector Empresas.
	 * @param sectorOtrosIntermediarios La descripcion del sector Otros Intermediarios.
	 * @param recibimos Define si la operacion es compra o venta.
	 * @return List
	 */
	List findMontoTotalResmerca(Date inicioDia, Date finDia,  Integer idPersonaIxeForward, String idDivisa,String sectorCasasDeBolsa,
			String sectorEmpresas, String sectorOtrosIntermediarios, String recibimos);
	
	/**
	 * Obtiene el monto de los deals que pertenecen a una estrategia Swap
	 * excluyendo aquellos que sean de la contraparte Ixe Forward.
	 * 
	 * @param incioDia La fecha de incio del dia.
	 * @param finDia La fecha del fin del dia.
	 * @param idDivisa El id de la divisa.
	 * @param idPersonaForward El id persona de Ixe Forward.
	 * @param recibimos Define si la operacion es compra o venta.
	 * @return List
	 */
	List findMontoForwardsEstrategia(Date inicioDia, Date finDia, String idDivisa, Integer idPersonaForward,
			String recibimos);
	
	/**
	 * Obtiene el monto de los deals que pertenecen al sector de la seccion del Resmerca. 
	 * 
	 * @param incioDia La fecha de incio del dia.
	 * @param finDia La fecha del fin del dia.
	 * @param idPersonaForward El id persona de Ixe Forward.
	 * @param idDivisa El id de la divisa.
	 * @param recibimos Define si la operacion es compra o venta.
	 * @return List
	 */
	List findMontoTotalesResmerca(Date inicioDia, Date finDia, Integer idPersonaForward, String idDivisa, 
			String recibimos);
	
	
	/**
	 * 
	 * @param incioDia La fecha de incio del dia.
	 * @param finDia La fecha del fin del dia.
	 * @param recibimos Define si la operacion es compra o venta.
	* @param idPersonaForward El id persona de Ixe Forward.
	 * @return List
	 */
	List findMontoForwardsEstrategiaSectores(Date inicioDia, Date finDia, String recibimos, Integer idPersonaForward, String idDivisa);
}

/*
 * $Id: ContabilidadDao.java,v 1.4 2010/01/27 22:32:50 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import java.util.Date;
import java.util.List;

/**
 * Interfaz para el DAO de Contabilidad.
 * 
 * @author Israel J. Rebollar
 * @version $Revision: 1.4 $ $Date: 2010/01/27 22:32:50 $
 */
public interface ContabilidadDao {

	/**
	 * Genera los registros para SAP del d&iacute;a dado.
	 * 
     * @param fechaSistema La fecha actual de sistema.
     * @param fechaProceso La fecha seleccionada para procesar la contabilidad.
     * @param contratosLiquidacionEspecial La lista de contratos de liquidaci&oacute;n especial.
	 */
	void generarRegistosContablesSAP(Date fechaSistema, Date fechaProceso,
                                    String contratosLiquidacionEspecial);
	
	/**
	 * Genera los registros para SAP del d&iacute;a dado dada una lista de polizas.
	 * 
	 * @param polizas La lista de polizas a insertar
	 * @param fechaSistema La fecha actual de sistema.
     * @param fechaProceso La fecha seleccionada para procesar la contabilidad.
     * @param contratosLiquidacionEspecial La lista de contratos de liquidaci&oacute;n especial.
	 */
	void generarRegistosContablesSAP(List polizas, Date fechaSistema, Date fechaProceso,
                                    String contratosLiquidacionEspecial);
	
	/**
	 * Regenera los registros contables de SAP dada una fecha.
	 * 
	 * @param fechaSistema La fecha actual de sistema.
	 * @param fechaProceso La fecha seleccionada para procesar la contabilidad.
     * @param contratosLiquidacionEspecial La lista de contratos de liquidaci&oacute;n especial.
	 */
	void reprocesarRegistrosContablesSAP(Date fechaSistema, Date fechaProceso,
                                        String contratosLiquidacionEspecial);
	
	/**
	 * Verifica los deal que est&aacute;n liquidados en SITE pero no en el SICA
     * 
	 * @param fechaSistema La fecha a revisar.
	 */
	void actualizarEstatusDealsSicaVsSite(Date fechaSistema);

    /**
     * Obtiene los deal que cuentan con detalles duplicados dada una fecha.
     *
     * @param fechaSistema La fecha a revisar.
     * @return List Los ID de los deals.
     */
    List obtenerDealsCuentanDetallesDuplicados(Date fechaSistema);    
}

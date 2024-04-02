/*
 * $Id: DealDao.java,v 1.2.2.2.18.1 2012/08/25 23:59:32 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import java.util.Date;
import java.util.List;

import com.ixe.ods.sica.sdo.dto.FiltroDealsTceDto;

/**
 * Data Access Objects para la consulta general de deals.  
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.2.2.2.18.1 $ $Date: 2012/08/25 23:59:32 $
 */
public interface DealDao {

    /**
     * Obtiene una lista de Deals.
     *
     * @param interbancario Define si el Deal es o no interbancario
     * @param tipoFecha Si la fecha es de Operaci&oacute;n, Liquidaci&oacute;n o Ninguna. 
     * @param fechaInicio La fecha inicio
     * @param fechaFinal La fecha final
     * @param status Define el status del Deal
     * @param id Id del Deal
     * @param idPromotor Id del promotor que proces&pacute; el Deal
     * @param tipoValor Fecha valor para el Deal
     * @param noCuenta N&uacute;mero de cuenta del cliente
     * @param eventos Define los eventos que marca el Deal
     * @param idCanal Id del Canal (Puede ser null para no restringirlo).
     * @param opcionMensajeria Si el deal es o no con mensajer&iacute;a.
     * @param claveFormaLiquidacion La clave de la forma de liquidaci&oacute;n
     * @param idDivisa El id de la divisa.
     * @param idGrupoTrabajo El ID del Grupo de Trabajo para consulta de deals en guardia.
     * @return List.
     */
    List findDeals(boolean interbancario,
                          String tipoFecha, Date fechaInicio,
                          Date fechaFinal, String status, Integer id, Integer idPromotor,
                          String tipoValor, String noCuenta, String eventos, String idCanal,
                          Boolean opcionMensajeria, String claveFormaLiquidacion, String idDivisa, String idGrupoTrabajo);
    
    /**
     * Busca deals de tipo de cambio especial
     * 
     * @param filtroDealsTce Filtro
     * @return Deals encontrados
     */
    List findDealsTce(FiltroDealsTceDto filtroDealsTce);
    
}

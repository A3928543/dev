/*
 * $Id: ReporteUtilidadesDao.java,v 1.2 2008/08/04 20:47:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import java.util.Date;
import java.util.List;

/**
 * Interfaz para el Data Access Object que permite realizar la consulta del Reporte de Utilidades de
 * Promoci&oacute;n.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2008/08/04 20:47:36 $
 */
public interface ReporteUtilidadesDao {

    /**
     * Obtiene los detalles de deal para el reporte de utilidades, cada detalle debe ser regresado
     * como un mapa.
     * 
     * @param de La fecha de inicio de la busqueda.
     * @param hasta La fecha de fin de la busqueda.
     * @param idPersona El id persona del promotor seleccionado.
     * @param idDivisa El id de la divisa seleccionada.
     * @param idCanal El id del canal seleccionado.
     * @param idsPromotores La lista de n&uacute;meros de Promotor (Opcional).
     * @return List de Maps.
     */
    public List findDetallesUtilidades(Date de, Date hasta, Integer idPersona, String idDivisa,
                                       String idCanal, List idsPromotores);

    /**
     * Obtiene los detalles de deal para el reporte de utilidades, cada detalle debe ser regresado
     * como un mapa.
     * 
     * @param de La fecha de inicio de la busqueda.
     * @param hasta La fecha de fin de la busqueda.
     * @param idCanal El id del canal seleccionado.
     * @param mnemonicos La lista de mnem&oacute;nicos a aplicar.
     * @param sucursales True para sucurales, false para sucursal virtual.
     * @return List de Maps.
     */
    List findDetallesUtilidadesIxeDirecto(Date de, Date hasta, String idCanal, List mnemonicos,
                                          boolean sucursales);
}

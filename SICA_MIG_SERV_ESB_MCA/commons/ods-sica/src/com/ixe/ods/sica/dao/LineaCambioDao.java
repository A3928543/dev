/*
 * $Id: LineaCambioDao.java,v 1.3.68.1 2016/07/13 23:58:47 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object para la entidad SC_LINEA_CAMBIO.
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.3.68.1 $ $Date: 2016/07/13 23:58:47 $
 */
public interface LineaCambioDao {

    /**
     * Recalcula los usos de todas las l&iacute;neas de cambio de acuerdo a los deals con que
     * est&aacute;n relacionados y que no est&acute; liquidados.
     *
     * @return List de String que describe el n&uacute;mero de registros que fueron actualizados.
     */    
    List cuadrarLineasCambio();

    /**
     * Regresa el n&uacute;mero de d&iacute;s que un cliente ha incumplido su pago con l&iacute;nea
     * de cambios.
     *
     * @param idLineaCambio El n&uacute;mero de la l&iacute;nea de cambios a revisar.
     * @return int.
     */
    int getDiasAdeudoParaLineaCambio(int idLineaCambio);
    
    /**
     * Consulta los registros de excesos de "lineas de cambio log" generados en un rango de fechas
     * @param idJerarquia
     * @param facultadCanal
     * @param fechaInicial
     * @param fechaFinal
     * @return List de objectos ExcesoLineaCambioDto
     */
    public List consultarDatosReporteExceso(Integer idJerarquia, String facultadCanal, Date fechaInicial, Date fechaFinal);
    
    /**
     * Consulta si el usuario especificado tiene los roles del area de riesgos
     * @param idUsuario
     * @param sistema
     * @return int Indica el numero de roles del area de riesgos que tiene el usuario
     */
    public int consultarUsuarioAreaRiesgos(Integer idUsuario, String sistema);
}

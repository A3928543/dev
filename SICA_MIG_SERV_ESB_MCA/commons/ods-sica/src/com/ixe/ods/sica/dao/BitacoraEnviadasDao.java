/*
 * $Id: BitacoraEnviadasDao.java,v 1.5 2010/04/13 17:36:49 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import java.util.List;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.BitacoraEnviadas;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;

/**
 * Interfaz del DAO para la entidad SC_BITACORA_ENVIADAS. Esta tabla se encuentra en el servidor
 * de Base de Datos de TAS BANCO.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.5 $ $Date: 2010/04/13 17:36:49 $
 */
public interface BitacoraEnviadasDao {

    /**
     * Inserta un registro en la tabla SC_BITACORA_ENVIADAS.
     *
     * @param be El objeto a insertar.
     */
    void store(BitacoraEnviadas be);
    
    /**
     * Actualiza el registro en la base de datos.
     *
     * @param be El registro a actualizar.
     */
    void actualizar(BitacoraEnviadas be);

    /**
     * Encuentra un Registro de un Deal Reportado a Banxico por su idConf y divisa.
     *
     * @param idConf El identificador del Deal - Detalle.
     * @param divisa La divisa de la operaci&oacute;n.
     * @return BitacoraEnviada El Registro de la Operaci&oacute;n reportada a Banxico.
     */
    BitacoraEnviadas findBitacoraEnviadaByIdConfAndDivisa(String idConf, String divisa);

    /**
     * Regresa el registro de SC_BITACORA_ENVIADAS que tiene el folio Tas especificado.
     *
     * @param folioTas El folio tas.
     * @return BitacoraEnviadas.
     * @throws com.ixe.ods.sica.SicaException Si no se encuentra el registro, o no tiene
     *              statusEstrategia 'PE' (Pendiente).
     */
    BitacoraEnviadas findBitacoraEnviadaByFolioTas(int folioTas) throws SicaException;

    /**
     * Regresa el registro de SC_BITACORA_ENVIADAS que tiene el folio Tas especificado.
     *
     * @param folioTas El folio tas.
     * @return BitacoraEnviadas.
     * @throws com.ixe.ods.sica.SicaException Si no se encuentra el registro, o no tiene
     *              statusEstrategia 'PE' (Pendiente).
     */
    List findBitacorasEnviadasByFolioTas(int folioTas) throws SicaException;

    /**
     * Encuentra los Registros de Operaciones de un Deal Reportadas a Banxico por su idConf.
     *
     * @param idConf	El identificador de una Operaci&oacute;n de un Deal Reportada a Banxico.
     * 					Se concatena 'idDeal' + 'folioDetalle'.
     * @return List		Los Registros de las Operaciones Reportadas a Banxico.
     */
    BitacoraEnviadas findBitacoraEnviadasByIdConf(String idConf);

    /**
     * Regresa la lista de Reportes de Operaciones que fueron rechazadas por Banxico.
     *
     * @return List.
     */
    List findBitacorasEnviadasConError();

    /**
     * Inserta la lista de bitacoras enviadas en la base de datos.
     *
     * @param bitacorasEnviadas Lista de objetos BitacoraEnviadas.
     */
    void insertarBitacorasEnviadas(List bitacorasEnviadas);

    /**
     * Inserta el objeto en la tabla SC_BITACORA_ENVIADAS.
     *
     * @param be El objeto a insertar.
     */
    void insertarBitacoraEnviadas(BitacoraEnviadas be);

    /**
     * Permite avisarle a Banxico la cancelaci&oacute;n de un Deal completo o uno de sus detalles.
     *
     * @param deal		El deal de la operaci&oacute;n.
     * @param dealDet	El detalle del deal de la operaci&oacute;n, si es que solo se cancela un
     * 					detalle y no todos los detalles del deal.
     */
    void revisarReportadoBanxico(Deal deal, DealDetalle dealDet);
}
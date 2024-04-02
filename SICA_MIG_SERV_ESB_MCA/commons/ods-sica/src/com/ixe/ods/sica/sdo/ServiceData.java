/*
 * $Id: ServiceData.java,v 1.22.24.1.52.1 2020/12/01 22:10:42 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sdo.BaseServiceData;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.seguridad.model.IUsuario;

/**
 * Service Data Object para las operaciones a la base de datos que requiere el SICA.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.22.24.1.52.1 $ $Date: 2020/12/01 22:10:42 $
 */
public interface ServiceData extends BaseServiceData {

    /**
     * Crea e inserta un registro en la tabla SC_LOG_AUDITORIA con los par&aacute;metros
     * especificados.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param ip La direcci&oacute;n IP del usuario.
     * @param idDeal El n&uacute;mero de deal relacionado (opcional).
     * @param usuario El usuario que realiza la operaci&oacute;n.
     * @param tipoOperacion El tipo de operaci&oacute;n realizada.
     * @param datosAdicionales Descripci&oacute;n adicional de la operaci&oacute;n.
     */
    void auditar(String ticket, String ip, Integer idDeal, IUsuario usuario,
                        String tipoOperacion, String datosAdicionales, String tipoEvento, String indicador);

    /**
     * Regresa el objeto de la clase <i>clase</i> que tiene la llave primaria <i>id</i>
     *
     * @param clase La clase del objeto.
     * @param id La llave primaria.
     * @return Object.
     */
    Object find(Class clase, Serializable id);

    /**
     * Regresa todos los objetos del tipo <i>clase</i>.
     *
     * @param clase La clase a buscar.
     * @return List.
     */
    List findAll(Class clase);

    /**
     * Encuentra el Canal con base en su idCanal
     *
     * @param idCanal Id del Canal a encontrar
     * @return Canal
     * @throws SicaException Si no existe el canal.
     */
    Canal findCanal(String idCanal) throws SicaException;

    /**
     * Regresa el deal con el n&uacute;mero especificado;
     *
     * @param idDeal El n&uacute;mero de deal a buscar.
     * @return Deal
     * @throws SicaException Si el deal no se encuentra en la base de datos.
     */
    Deal findDeal(int idDeal) throws SicaException;

    /**
     * Regresa el deal con el n&uacute;mero especificado, inicializando el canal y/o sucursal.
     *
     * @param idDeal El n&uacute;mero de deal a buscar.
     * @return Deal
     * @throws SicaException Si el deal no se encuentra en la base de datos.
     */
    Deal findDealConCanal(int idDeal) throws SicaException;

    /**
     * Encuentra el Historial de un Deal por si ID.
     * @param idDeal El ID del Deal
     * @return List El Historial del Deal.
     */
    List findLogByDealTodos(int idDeal);
    
    /**
     * Encuentra los factores divisa actuales.
     * 
     * @return List.
     */
    List findFactoresDivisaActuales();

    /**
     * Encuentra la Persona de un Contrato Sica.
     *
     * @param noCuenta El n&uacute;mero del Contrato SICA.
     * @return Persona La persona a la que corresponde el Contrato SICA.
     */
    Persona findPersonaContratoSica(String noCuenta);
    
    /**
     * Encuentra el &uacute;ltimo Precio de Referencia registrado para el Sistema.
     *
     * @return PrecioReferenciaActual
     */
    PrecioReferenciaActual findPrecioReferenciaActual();   
    
    /**
     * Encuentra el valor actual de los spreads actuales para un canal.
     * 
     * @param idCanal El id del canal
     * @return List
     */
    List findSpreadsActualesByCanal(String idCanal);

    /**
     * Encuentra el valor actual de los spreads actuales para un canal.
     * 
     * @param idTipoPizarron El id del tipo de pizarron.
     * @return List
     */
    List findSpreadsActualesByTipoPizarron(Integer idTipoPizarron);
    
    /**
     * Encuentra el valor actual de los spreads actuales para una mesa y un canal.
     * 
     * @param idMesaCambio El id de la mesa.
     * @param idCanal El id del canal.
     * @return List
     */
    List findSpreadsActualesRefByMesaCanal(int idMesaCambio, String idCanal);
    
    /**
     * Encuentra los spreads actuales por el Id del Tipo de Pizarron.
     *
     * @param idTipoPizarron El id del Tipo de Pizarron
     * @return List
     */
    List findSpreadsActualesRefByTipoPizarron(int idTipoPizarron);

    /**
     * Obtiene las Utilidades al Cierre de D&iacute;a de Hoy, de las 00:00:00 h
     * al momento en que se ejecuta el Cierre.
     *
     * @param fechaHoy	Hoy a las 00:00:00 h.
     * @param fecha		Hoy al momento del Cierre.
     * @return	List	Las Utilidades.
     */
    List findUtilidadCierre(Date fechaHoy , Date fecha);
    
    /**
     * Obtiene las Utilidades al Cierre de D&iacute;a de Hoy, de las 00:00:00 h
     * al momento en que se ejecuta el Cierre Vespertino.
     *
     * @param fechaHoy	Hoy a las 00:00:00 h.
     * @param fecha		Hoy al momento del Cierre.
     * @return	List	Las Utilidades.
     */
    List findUtilidadCierreVespertino(Date fechaHoy , Date fecha);
    
    /**
     * Obtiene el factor de conversi&oacute;n entre dos divisas.
     * 
     * @param idFromDivisa El id de la divisa "a".
     * @param idToDivisa El id de la divisa "d".
     * @return double
     * @throws SicaException Si ocurre alg&uacute;n problema.
     */
    double getFactorConversionFromTo(String idFromDivisa, String idToDivisa) throws SicaException;    

    /**
     * Asigna los atributos del deal.
     * 
     * @param deal El deal.
     */
    void inicializarDeal(Deal deal);
    
    /**
     * Encuentra las operaciones realizadas desde tres meses a la fecha.
     * 
     * @param de La fecha de inicio.
     * @return List
     */
    List findAllDealsTresMeses(Date de);

    /**
     * Actualiza el valor de los registros.
     * 
     * @param registros La lista de registros a actualizar.
     */
    void update(List registros);
}

/*
 * $Id: PosicionServiceData.java,v 1.13.28.2 2010/07/23 02:26:22 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2007 LegoSoft S.C.
 */
package com.ixe.ods.sica.sdo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.ixe.ods.sica.posicion.vo.UtilidadGlobalVO;

/**
 * Service Data Object para las operaciones a la base de datos que requiere el Monitor de la
 * Posici&oacute;n.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.13.28.2 $ $Date: 2010/07/23 02:26:22 $
 */
public interface PosicionServiceData {

	/**
	 * Obtiene la lista de divisas ligadas a una mesa en particular
	 * 
	 * @param idMesaCambio El identificador de la mesa de cambio.
	 * @return List
	 */
    List findDivisasSpreadActualByMesa(Integer idMesaCambio);

    /**
     * Obtiene la lista de productos ligados a una mesa en particular
     * 
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @return List
     */
    List findProductosSpreadActualByMesa(Integer idMesaCambio);

    /**
     * Obtiene el Blotter (SC_POSICION_LOG)
     *
     * @param idMesaCambio El n&uacute;mero de mesa de cambio.
     * @param monto Monto minimo a partir del cual se regresaran los registros que conforman el
     *  Blotter
     * @return List Lista de objetos del tipo BlotterVO que contiene los datos de la tabla
     *  Posicion_Log
     */
    List getBlotter(Integer idMesaCambio, BigDecimal monto);

    /**
     * Obtiene la Posicion de la Mesa (SC_POSICION)
     *
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @param idDivisa La clave de la divisa.
     * @return BigDecimal
     */
    PosicionVO getPosicionMesa(Integer idMesaCambio, String idDivisa);

    /**
     * Obtiene el valor de la Tasa de Cambio del Mercado (SC_PRECIO_REFERENCIA y
     * SC_PRECIO_REFERENCIA_ACTUAL)
     *
     * @return BigDecimal
     */
    BigDecimal getTasaCambioMercado();

    /**
     * Obtiene la Posicion de la Mesa para un Canal especifico
     *
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @param idDivisa La clave de la divisa.
     * @param idCanal La clave del canal.
     * @return PosicionVO.
     */
    PosicionVO getPosicionCanal(Integer idMesaCambio, String idDivisa, String idCanal);

    /**
     * Obtiene la Posicion de la mesa para un Producto especifico
     *
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @param idDivisa La clave de la divisa.
     * @param idProducto La clave del producto.
     * @return PosicionVO
     */
    PosicionVO getPosicionProducto(Integer idMesaCambio, String idDivisa, String idProducto);

    /**
     * Obtiene la posicion para el conjunto de canales que comparten la misma mesa y divisa.
     *
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @param idDivisa La clave de la divisa.
     * @return List
     */
    List getPosicionCanales(Integer idMesaCambio, String idDivisa);

    /**
     * Obtiene la posicion para el conjunto de productos que comparten la misma mesa y divisa
     *
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @param idDivisa La clave de la divisa.
     * @return List
     */
    List getPosicionProductos(Integer idMesaCambio, String idDivisa);

    /**
     * Obtiene la posicion de los canales (sucursales) que comparten la misma mesa y divisa
     *
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @param idDivisa La clave de la divisa.
     * @return PosicionVO
     */
    PosicionVO getPosicionSucursales(Integer idMesaCambio, String idDivisa);

    /**
     * Obtiene la posicion del efectivo para un canal y divisa
     *
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @param idDivisa La clave de la divisa.
     * @return PosicionVO
     */
    PosicionVO getPosicionEfectivo(Integer idMesaCambio, String idDivisa);

    /**
     * Obtiene la posicion de inventario de efectivo para todas las sucursales.
     *
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @param idDivisa La clave de la divisa.
     * @return PosicionVO
     */
    PosicionVO getPosicionInventarioEfectivo(Integer idMesaCambio, String idDivisa);

    /**
     * Query para obtener la posicion de un conjunto de canales que comparten la misma mesa y
     * divisa.
     *
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @param idDivisa La clave de la divisa.
     * @param canales La lista de canales.
     * @return PosicionVO
     */
    PosicionVO getQueryPosicionCanales(Integer idMesaCambio, String idDivisa, List canales);

    /**
     * Query para obtener la posicion de un conjunto de productos que comparten la misma mesa y
     * divisa.
     *
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @param idDivisa La clave de la divisa.
     * @param productos La lista de productos.
     * @return PosicionVO
     */
    PosicionVO getQueryPosicionProductos(Integer idMesaCambio, String idDivisa, List productos);

    /**
     * Query para obtener la posicion de un conjunto de canales y productos que comparten la misma
     * mesa y divisa.
     *
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @param idDivisa La clave de la divisa.
     * @param canales La lista de canales.
     * @param productos La lista de productos.
     * @return PosicionVO.
     */
    PosicionVO getQueryPosicionCanalesProductos(Integer idMesaCambio, String idDivisa, List canales,
                                                List productos);

    /**
     * Obtiene todas las mesas de cambio.
     *  
     * @return List.
     */
    List getMesasCambio();

    /**
     * Obtiene los canales ligados a una mesa en particular.
     * 
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @return List.
     */
    List getCanales(Integer idMesaCambio);

    /**
     * Obtiene las sucursales ligadas a una mesa en particular.
     * 
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @return List.
     */
    List getSucursales(Integer idMesaCambio);

    /**
     * Obtiene la utilidad global de todas las mesas
     * 
     * @return List
     */
    UtilidadGlobalVO getUtilidadGlobal();

    /**
     * Obtiene el factor de conversion para la utilidad (mandando el valor de to_id_divisa la
     * divisa de referencia de la mesa)
     * 
     * @param idMesaCambio El id de la mesa de cambio.
     * @param toIdDivisa El identificador de la divisa.
     * @return El factor de conversion para la utilidad
     */
    double getFactorDivisaUtilidad(Integer idMesaCambio, String toIdDivisa);
    
    /**
     * Obtiene el factor de conversion en la mesa (mandando el valor de to_id_divisa USD)
     * 
     * @param toIdDivisa El identificador de la divisa.
     * @return el factor de conversion en la mesa
     */
    double getFactorDivisaMesa(String toIdDivisa);

    /**
     * Devuelve la posici&oacute;n para el id
     * de la mesa que recibe como par&aacute;metro.
     * 
     * @param idMesaCambio El id de la mesa de cambio.
     * @return una lista con la posicion de las divisas
     */
	List getPosicionDivisas( Integer idMesaCambio);

	/**
	 * Obtiene la sumatoria de la posici&oacute;n de inventario de efectivo para una lista de
     * sucursales (puede ser solo una sucursal).
	 * 
	 * @param idMesaCambio El id de la mesa de cambio.
	 * @param idDivisa El id de la divisa.
	 * @param sucursales La lista de sucursales.
	 * @return la posicion total de la lista de sucursales
	 */
	PosicionVO getQueryPosicionInventarioEfectivo(Integer idMesaCambio, String idDivisa,
                                                  List sucursales);
	/**
	 * Obtiene las posiciones de la divisa y fecha seleccionada. Si el parametro 
	 * <code>idDivisa</code> tiene valor <code>null</code>, obtiene la posicion
	 * para todas las divisas.
	 * 
	 * Si el parametro <code>historico</code> tiene el valor <code>false</code>,
	 * obtiene la posicion del dia actual, de lo contrario obtiene la posicion de
	 * la fecha seleccionada.
	 * 
	 * @param idDivisa El id de la divisa.
	 * @param dia La fecha de busqueda.
     * @param idMesaCambio La clave de la mesa de cambio.
	 * @param historico Define si es la posicion del dia actual o historica.
	 * @return List.
	 */
	List findPosicionesParaSiar(String idDivisa, Date dia, Integer idMesaCambio, 
			boolean historico);
}
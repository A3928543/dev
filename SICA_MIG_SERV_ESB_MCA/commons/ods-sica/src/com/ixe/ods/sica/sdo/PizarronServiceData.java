/*
 * $Id: PizarronServiceData.java,v 1.21.26.1.14.4.52.2 2020/12/03 21:59:07 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.RenglonPizarron;
import com.ixe.ods.sica.model.TipoPizarron;
import com.ixe.ods.sica.model.Variacion;
import com.ixe.ods.sica.posicion.vo.ParametrosPizarronVO;

/**
 * Service Data Object para las operaciones a la base de datos que requiere el pizarr&oacute;n.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.21.26.1.14.4.52.2 $ $Date: 2020/12/03 21:59:07 $
 */
public interface PizarronServiceData {

    /**
     * Regresa el precio para el canal, producto, tipo de operaci&oacute;n y fecha valor
     * especificados. Levanta SicaException Si no se puede calcular el precio.
     *
     * @param idCanal La clave del canal.
     * @param idDivisa La clave de la divisa.
     * @param idProducto La clave de la forma de liquidaci&oacute;n.
     * @param recibimos True para compra, False para venta.
     * @param fechaValor La fecha valor a consultar.
     * @return double.
     */
    double findPrecioPizarronPesos(String idCanal, String idDivisa, String idProducto,
                                          boolean recibimos, String fechaValor);
        
    /**
     * Regresa el precio para el canal, producto, tipo de operaci&oacute;n y fecha valor
     * especificados.
     *
     * @param tipoPizarron El tipo de pizarron para el canal.
     * @param idDivisa La clave de la divisa.
     * @param idProducto La clave de la forma de liquidaci&oacute;n.
     * @param recibimos True para compra, False para venta.
     * @param fechaValor La fecha valor a consultar.
     * @return double.
     */
    double findPrecioPizarronPesos(TipoPizarron tipoPizarron, String idDivisa, String idProducto,
            boolean recibimos, String fechaValor);
    
    /**
     * Regresa el rengl&oacute;n pizarr&oacute;n por tipo pizarr&oacute;n, divisa y producto.
     * 
     * @param tipoPizarron El tipo de pizarr&oacte;n.
     * @param idDivisa La clave de la divisa.
     * @param idProducto La clave forma liquidaci&oacute;n
     * @return RenglonPizarron.
     */
    RenglonPizarron findRenglonPizarron(TipoPizarron tipoPizarron, String idDivisa, 
    		String idProducto);
        
    /**
     * Encuentra el &uacute;ltimo Precio de Referencia registrado para el Sistema.
     *
     * @return PrecioReferencia
     */
    PrecioReferenciaActual findPrecioReferenciaActual();    

    /**
     * Obtiene los parametros para el Pizarr&oacute;n
     *
     * @return un objeto <code>ParametrosPizarronVO</code> que contiene los renglones para el
     * pizarr&oacute;n
     */
	ParametrosPizarronVO getParametrosPizarron();

	/**
     * @deprecated Se deja de usar este m&eacute;todo, en favor del uso del stored procedure SC_SP_GENERAR_PIZARRONES_SICA
     * 
     * Crea la Pizarra de la aplicacion.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param tipoPizarron El tipo de pizarron del canal.
     * @param sucursal Si es o no para sucursales.
     * @return un <code>Map</code> con los valores de la Pizarra.
     */
	Map crearPizarron(String ticket, TipoPizarron tipoPizarron, boolean sucursal);

	/**
     * Regresa una lista que contiene los carries en diferentes fechas valor a partir de una fecha.
     *
     * @param fechaCash La fecha cash de referencia.
     * @return el <code>Map</code> que contiene los carries para una fecha determinada
     */
	Map getDifCarryMap(Date fechaCash);

    /**
     * Regresa la lista de renglones del pizarr&oacute;n para el canal especificado.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param tipoPizarron El tipo de pizarron del canal.
     * @param sucursales Si es o no para sucursales. Este par&aacute;metro no se utiliza para las tres referencias actuales
     * 		hacia este m&eacute;todo, DatosPizarron, CatMecanicaNeteos y ModificaMontoProductoDetalleDeal
     * @return List.
     */
    List getRenglonesPizarron(String ticket, Integer tipoPizarron, boolean sucursales);

    /**
     * Llama al stored procedure encargado de actualizar los pizarrones
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @see #refrescarPizarron(String, com.ixe.ods.sica.model.TipoPizarron).
     */
    void refrescarPizarrones(String ticket, String ip, IUsuario usuario);

    /**
     * Llama al stored procedure encargado de actualizar los pizarrones
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param tipoPizarron El tipo de pizarron del canal.
     */
    void refrescarPizarron(String ticket, TipoPizarron tipoPizarron, String ip, IUsuario usuario);
    
    /**
     *
     * Permite conocer el tipo de cambio (para uso del teller).
     *
     * @param idDivisa el id de la Divisa
     * @param claveInstrum el <code>String</code> que representa la clave Instrumento.
     * @param idMesaCambio el id de la Mesa de Cambio
     * @param idSpread	el id del Spread
     * @return el <code>Map</code> con los m&aacute;ximos y m&iacute;nimos para &eacute;sta divisa
     */
    Map obtenerTipoCambioPorDivisa(String idDivisa, String claveInstrum, int idMesaCambio,
                                   int idSpread);

    /**
     * Regresa los renglones para pintar el Pizarron para Divisas no Frecuentes o Metales
     * Amonedados.
     *
     * @param metales	<code>true</code> si es para metales amonedados
     * @return	una <code>List</code> con los resultados para pintar el pizarr&oacute;n
     */
    List obtenerPizarronOtrasDivisas(boolean metales);

    /**
     * Obtiene la fecha actual del sistema sin milisegundos, segundos ni minutos.
     *
     * @return un objeto <code>Date</code> con la fecha actual.
     */
    Date getFechaOperacion();

    /**
     * Obtiene la fecha cash de operaci&oacute;n sin milisegundos, segundos ni minutos.
     *
     * @param fechaCash La fecha cash de referencia.
     * @return un objeto <code>Date</code> con la fecha cash.
     */
    Date getFechaOperacion(Date fechaCash);

    /**
     * Obtiene la fecha TOM del sistema.
     *
     * @return Date La fecha TOM del sistema.
     */
    Date getFechaTOM();

    /**
     * Obtiene la fecha TOM del sistema con respecto a la fecha cash de referencia.
     *
     * @param fechaCash La fecha cash de referencia.
     * @return Date La fecha TOM del sistema.
     */
    Date getFechaTOM(Date fechaCash);

    /**
     * Obtiene la fecha SPOT del sistema.
     *
     * @return Date La fecha SPOT del sistema.
     */
    Date getFechaSPOT();

    /**
     * Obtiene la fecha SPOT del sistema con respecto a la fecha cash de referencia.
     *
     * @param fechaCash La fecha cash de referencia.
     * @return Date La fecha SPOT del sistema.
     */
    Date getFechaSPOT(Date fechaCash);

    /**
     * Obtiene la fecha SPOT del sistema con respecto a la fecha cash de referencia,
     * tomando en cuenta si la fecha calculada es dia festivo en EUA o en Mexico.
     * 
     * @param fechaCash La fecha cash de referencia
     * @return Date
     */
    Date getFechaSPOTSiar(Date fechaCash);
    
    /**
     * Obtiene la fecha 72HR del sistema.
     *
     * @return Date La fecha 72HR del sistema.
     */
    Date getFecha72HR();

    /**
     * Obtiene la fecha 72HR del sistema, con respecto a la fecha cash de referencia.
     *
     * @param fechaCash La fecha cash de referencia.
     * @return Date La fecha 72HR del sistema.
     */
    Date getFecha72HR(Date fechaCash);

    /**
     * Obtiene la fecha VFUT del sistema.
     *
     * @return Date La fecha VFUT del sistema.
     */
    Date getFechaVFUT();

    /**
     * Obtiene la fecha VFUT del sistema, con respecto a la fecha cash de referencia.
     *
     * @param fechaCash La fecha cash de referencia.
     * @return Date La fecha VFUT del sistema.
     */
    Date getFechaVFUT(Date fechaCash);

    /**
     * Regresa true si la fecha tom, spot o 72HR son d&iacute;s festivos en Estados Unidos, en cuyo
     * caso, se debe habilitar en el sistema la opci&oacute;n de valor futuro para operaciones
     * interbancarias.
     *
     * @return true;
     */
    boolean isValorFuturoHabilitado();

    /**
     * Regresa el &uacute;ltimo registro de la tabla SICA_VARIACION.
     *
     * @return Variacion.
     */
    Variacion findVariacionActual();

    /**
     * Calcula la fecha valor adecuada con respecto a una fecha de captura y liquidaci&oacute;n
     * determinadas. Levanta SicaFechaValorException Si no es posible calcular la fecha valor.
     *
     * @param fechaCaptura     La fecha de captura para un deal.
     * @param fechaLiquidacion La fecha de liquidaci&oacute;n de un deal.
     * @return String.
     */
    String calcularFechaValorParaFechasCapturaLiquidacion(Date fechaCaptura, Date fechaLiquidacion);

    /**
     * Regresa la fecha valor para modificar la posici&oacute;n cuando se quiere cancelar o reversar
     * un deal.
     *
     * @param fechaCaptura La fecha de captura del deal.
     * @param fechaValorCaptura La fecha valor que tiene el deal.
     * @param regresarCashEnError Si debe regresar 'CASH' si no se puede calcular, o levantar una
     *          excepci&oacute;n.
     * @return String.
     */
    String fechaValorParaCancelacion(Date fechaCaptura, String fechaValorCaptura,
                                     boolean regresarCashEnError);
    
    /**
     * Genera los tipos de cambio para cada canal de operacion y por divisa. 
     * 
     * @param canal El canal de operacion.
     * @param idDivisa El id de la divisa
     * @param claveFormaFormaLiquidacion La forma de liquidacion (efectivo.).
     * @return Map
     */
    Map obtenerTipoDeCambioPorDivisa(Canal canal, String idDivisa, 
    								 String claveFormaFormaLiquidacion);
    
    
    /**
     * Obtiene la lista de las divisas menos Frecuentes del Pizarron
     *
     * @param metales Var para buscar entre divisas no frecuentes y metales amonedados.
     * @return List
     */
    List findDivisasNoFrecuentesPizarron(boolean metales);
}

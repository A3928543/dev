/*
 * $Id: LineaCambioServiceData.java,v 1.3.84.8 2016/10/11 22:07:34 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo;

import java.util.Date;
import java.util.List;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.LineaCreditoReinicioExceso;

/**
 * Interfaz para el Service Data Object que realiza todas las operaciones a la base de datos que
 * tienen que ver con Lineas de Cambio.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.3.84.8 $ $Date: 2016/10/11 22:07:34 $
 */
public interface LineaCambioServiceData {
	void validarLineaCredito(DealDetalle detalle, String ticket);
	
    /**
     * Regresa la l&iacute;nea de cambios que corresponde a la persona con el identificador
     * especificado, o null, si no existe.
     *
     * @param idPersona El n&uacute;mero de persona a consultar.
     * @return LineaCambio.
     */
    LineaCambio findLineaCambioParaCliente(Integer idPersona);

    /**
     * Encuentra las L&iacute;neas de Cr&eacute;dito con los siguientes criterios de
     * b&uacute;squeda:
     * 1) Uno o varios ejecutivos
     * 2) Estatus
     * 3) Un rango de porcentaje de uso, con l&iacute;mites inferior y superior
     * 4) Proximidad a la fecha de renovaci&oacute;n de acuerdo al par&aacute;metro D&iacute;as
     * de Vencimiento de las L&iacute;neas de Cr&eacute;dito.
     *
     * @param ejecutivos La lista con el o los ejecutivos de los cuales se quieren buscar las
     *      l&iacute;neas.
     * @param status El status de las l&iacute;neas.
     * @param limiteInferior Rango inferior de porcentaje de uso de las l&iacute;neas.
     * @param limiteSuperior Rango superior de porcentaje de uso de las l&iacute;neas.
     * @param isLineasPorRenovar Si se quieren buscar las l&iacute;neas pr&oacute;ximas a renovar o
     *      no.
     * @param fechaActual La fecha actual de la consulta.
     * @param diasVencimientoLineas Par&aacute;metro D&iacute;as de Vencimiento de las L&iacute;neas
     *      de Cr&eacute;dito.
     * @return List con las L&iacute;neas de Cre&acute;dito.
     */
    List findLineasCambioByEjecutivos(List ejecutivos, String status, double limiteInferior,
                                       double limiteSuperior, boolean isLineasPorRenovar,
                                       Date fechaActual, Integer diasVencimientoLineas);    

    /**
     * Encuentra Las L&iacute;neas de Cr&eacute;dito de los Corporativos asignados a un grupo de
     * Ejecutivos por Estatus.
     *
     * @param clientes Los clientes encontrados de acuerdo al criterio de b&uacute;squeda
     *      Raz&oacute;n Social.
     * @param idTipoEjecutivo El Tipo Ejecutivo del SICA.
     * @param ejecutivos El Grupo de Ejecutivos.
     * @param estatus El Estatus seleccionado.
     * @return List Las L&iacute;neas de Cr&eacute;dito.
     */
    List findLineasCambioByClientesAndEjecutivosAndEstatus(List clientes, String idTipoEjecutivo,
                                                              List ejecutivos, String estatus);

    /**
     * Encuentra Las L&iacute;neas de Cr&eacute;dito de los Corporativos asignados a un Ejecutivo
     * por Estatus.
     *
     * @param clientes Los clientes encontrados de acuerdo al criterio de b&uacute;squeda
     *          Raz&oacute;n Social.
     * @param idTipoEjecutivo El Tipo Ejecutivo del SICA.
     * @param idEjecutivo El Ejecutivo seleccionado.
     * @param estatus El Estatus seleccionado.
     * @return List Las L&iacute;neas de Cr&eacute;dito.
     */
    List findLineasDeCreditoByClientesAndEjecutivoAndEstatus(List clientes, String idTipoEjecutivo,
                                                             Integer idEjecutivo, String estatus);
    
    /**
     * Encuentra el Historial de una L&iacute;nea de Cr&eacute;dito.
     *
     * @param idLineaCambio El ID de la L&iacute;nea de Cr&eacute;dito.
     * @return List El Historial de Movimientos de la L&iacute;nea de Cr&eacute;dito.
     */
    List findHistoriaLineaCambioLogByIdLineaCredito(Integer idLineaCambio);

    

   
    
    /**
     * Si el deal est&aacute; marcado para toma en firme y est&aacute; en evento no determinado,
     * hace uso de las l&iacute;neas de cr&eacute;dito para toma en firme, validando que no se
     * exceda del l&iacute;mite de la l&iacute;nea. Si no tiene toma en firme y tiene pago
     * anticipado con evento no determinado, hace uso de las l&iacute;neas de cr&eacute;dito de pago
     * anticipado validando que no se exceda el l&iacute;mite de la l&iacute;. En estos
     * &uacute;ltimos casos, se solicita autorizaci&oacute;n por excedente de l&iacute;nea de
     * cr&eacute;dito para toma en firme o pago anticipado.
     *
     * Si el deal es reprocesado, no se vuelve a hacer uso de las l&iacute;neas afectadas.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El deal a revisar.
     * @param validar Si se requiere validaci&oacute;n o no (cuando se va a permitir el excedente).
     * @throws com.ixe.ods.sica.SicaException Si no hay mnem&oacute;nicos aplicables a
     *  l&iacute;neas de cr&eacute;dito.
     * @return String Mensaje de error.
     */
    String usar(String ticket, Deal deal, boolean validar) throws SicaException;

    /**
     * Valida que el deal tenga detalles aplicables a l&iacute;neas de cr&eacute;dito
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El deal a revisar.
     * @throws SicaException Si no hay detalles aplicables.
     */
    double revisarAplicacionLineaCredito(String ticket,  Deal deal) throws SicaException;

    List findDealsLineaCambioCliente(String ticket, Integer idPersona);
    
    /**
     * Obtiene todos los registros de la tabla de SC_TIPO_AUTORIZACION
     * @return List
     */
	List findCatalogoTipoAutorizacion();
	
	/**
	 * Obtiene todos los registros de la tabla SC_FORMALIZACION
	 * @return List
	 */
	List findCatalogoFormalizacion();
	
	/**
	 * Obtiene todos los registros de la tabla SC_INSTANCIA_FACULTADA
	 * @return List
	 */
	List findCatalogoInstanciaFacultada();

	/**
     * Hace uso de la l&iacute;nea de cr&eacute;dito para el deal especificado tomando en cuenta que dicho 
     * Deal no tiene activada la opcion de Pago Anticipado o toma en Firme.
     * Si no existe LC o no esta ACTIVA solo se pueden capturan operaciones FV=CASH
     * @param deal El deal a revisar.
     * @param DealDetalle El Deal Detalle que aplicara que consume la linea de credito
     * @throws SicaException 
     */
	void consumirLineaCredito(Deal deal, DealDetalle det);

	/**
	 * Hace uso de la linea de credito al seleccionar la opcion de Pago Anticipado y/o Toma en Firme en base a todos los detalles del Deal, 
	 * solo se efecta el monto PAyTF debido a que anteriormente en el flujo anterior se afecto al montoFV
	 * @param ticket 
	 * @param linea
	 * @param deal
	 */
	void consumirLineaCreditoByDeal(String ticket, LineaCambio linea, Deal deal);
	
	/**
	 * Se afecta la linea de credito al capturar un detalle de un Deal tomando
	 * en cuenta que se tiene seleccionado la opcion de Pago Anticipado y/o Toma en Firme
	 * @param deal Deal con pago anticipado
	 * @param det DealDetalle 
	 */
	void consumirLineaCreditoPA(Deal deal, DealDetalle det);
	
	/**
     * Se valida el monto a aplicar sobre una linea de credito Activa y la fecha valor diferente de CASH
     * tomando en cuenta que el Deal no tiene marcada la opcion de Pago Anticipado o Toma en firme.
     * @param linea Linea de Credito asociada al Cliente
     * @param deal Deal Deal que contiene al Deal Detalle
     * @param detalle Deal Detalle que aplica la linea de credito
     */
	void consumirLineaCreditoFechaValor(LineaCambio linea, Deal deal,
			DealDetalle det);

	/**
	 * Los SPLITS No deben afectar línea de credito porque el detalle original ya realizo la afectacion 
	 * solo hay que actualizar los USOS con los nuevos importes, solo se actualiza el SC_LINEA_CAMBIO_LOG.
	 * @param det Detalle del Deal
	 */
	void aplicarUsoLCDetalleSplit(DealDetalle det);

	/**
	 * Efectua la liberacion de un Detalle de Deal sin afectar la linea de credito, porque ya se realizo la afectacion
	 * anteriormente, solo se actualiza el SC_LINEA_CAMBIO_LOG. 
	 * @param detOrig
	 */
	void insertarLiberacionLineaCambioLog(DealDetalle detOrig);

	/**
	 * Efectua la aplicacion de un Detalle de Deal sin afectar la linea de credito, porque ya se realizo la afectacion
	 * @param det
	 */
	void aplicarUsoLCDetalleGoma(DealDetalle det);

	
	/**
	 * Valida si se trata de un Deal con pago anticipado o no para liberar la linea de credito.
	 * @param ticket 
	 * @param det Detalle a liberar 
	 * @return 
	 */
	public LineaCambio liberarLineaCreditoDealDetalle(String ticket, DealDetalle det);
	
	/**
	 * Efectua la liberacion de Linea de Credito en base al monto del Detalle tomando en cuenta que el Deal 
	 * tiene pago anticipado  
	 * @param ticket
	 * @param detalle
	 * @param liberarRemesas
	 * @return 
	 */
	LineaCambio liberarDetalleLineaCreditoPA(String ticket, DealDetalle detalle);
	
	/**
	 * Efectua la liberacion de Linea de Creidto en base al monto del Detalle tomando en cuenta que el Deal 
	 * no tiene pago anticipado
	 * @param ticket
	 * @param detalle
	 * @param liberarRemesas
	 * @return 
	 */
	LineaCambio liberarDetalleLineaCredito(String ticket, DealDetalle detalle);

	/**
	 * Valida la aplicacion de la linea de credito para Deals sin Pago Anticipado
	 * @param idPersona asociada a la linea de credito
	 * @param deal que contiene los montos a validar con respecto a la linea de credito
	 * @param nuevaFechaValor fecha valor a validar con respecto a la linea de credito
	 * @param montoModificado Valor opcional, solo si se realizo una modificacion de Monto en el reverso 
	 * @param isCambioCliente TODO
	 * @param isCambioFechaValor TODO
	 * @param isCambioMonto TODO
	 * @param booleanisCambioTC TODO
	 * @param ticket TODO
	 */
	void validarLineaCreditoReverso(Integer idPersona, Deal deal , String nuevaFechaValor, double montoModificado, boolean isCambioCliente, boolean isCambioFechaValor, boolean isCambioMonto, boolean booleanisCambioTC, String ticket);

	
	/**
	 * Valida la aplicacion de la linea de credito para Deals con Pago Anticipado
	 * @param idPersona asociada a la linea de credito
	 * @param deal Deal que contiene los montos a validar con respecto a la linea de credito
	 * @param nuevaFechaValor fecha valor a validar con respecto a la linea de credito
	 * @param montoModificado Valor opcional, solo si se realizo una modificacion de Monto en el reverso
	 */
	void validarMontoLineaCreditoPA(Integer idPersona, Deal deal , String nuevaFechaValor, double montoModificado);
	
       /**
	 * Consulta las lineas de credi to activas
	 * @return List
	 */
	public List consultarLineasCreditoActivas();
	
	/**
	 * Consulta los registros de exceso autorizados a las lineas de credito activas
	 * @param fechaInicioTrimestre
	 * @param fechaFinTrimestre
	 * @return List
	 */
	List consultarExcesosLineasCreditoActivas(final Date fechaInicioTrimestre,
								              final Date fechaFinTrimestre);
	
	/**
	 * Consulta el registro del ultimo reinicio de excesos de las lineas de credito que no ha sido ejecutado
	 * @return LineaCreditoReinicioExceso
	 */
	LineaCreditoReinicioExceso consultarReinicioTrimestralNoEjecutado();
	
	/**
	 * Aplica el reinicio de los contadores de excesos de las lineas de credito activas.
	 * @param reinicio Ejecucion actual
	 * @param fechaInicioTrimestre 
	 * @param fechaFinTrimestre
	 * @param fechaSiguienteReinicio
	 */
	void aplicarReinicioContadoresExcesos(LineaCreditoReinicioExceso reinicio, Date fechaInicioTrimestre, 
                                          Date fechaFinTrimestre, Date fechaSiguienteReinicio, StringBuffer logger);
	
	/**
     * Reinicia a cero los contadores de excesos delas lineas de credito activas. 
     */
    public void reiniciarContadoresExcesosLineasCreditoActivas(StringBuffer logger);

	/**
	 * Consulta los datos del reporte de excesos de lineas de cambio
	 * @param facultadCanal
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public List consultarDatosReporteExceso(final String facultadCanal, final Date fechaInicio, final Date fechaFin);
	
	/**
	 * Consulta si el usuario especificado es del area de Riesgos
	 * @param idUsuario
	 * @param sistema
	 * @return int Indica cuantos roles del area riesgos tiene el usuario
	 */
	public int consultarSiUsuarioPerteneceAreaRiesgos(Integer idUsuario, String sistema);
	
	 /**
     * Regresa true si el mnem&oacute;nico especificado est&aacute; marcado para usar l&iacute;nea
     * de cr&eacute;dito de alg&uacute;n tipo.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param mnemonico El mnem&oacute;nico a revisar, puede ser null si hay claveFormaLiquidacion.
     * @param claveFormaLiquidacion La clave del producto, puede ser null si hay mnemonico.
     * @param recibimos True para recibimos, False para entregamos.
     * @return boolean.
     */
    public boolean validarAplicablesTfPagAnt(String ticket, String mnemonico,
                                              String claveFormaLiquidacion, boolean recibimos);
   
    /**
     * Efectua la liberacion de la linea por cancelacion del Detalle e inserta el nuevo USO por motivo de
     *  la creacion del nuevo Detalle del Deal
     * @param detalleOriginal Detalle del Deal con el que se libera la linea de credito
     * @param detalleNuevo Detalle del Deal con el que se consume el nuevo Monto de la Linea de Credito
     *
     */
	void aplicarCambioMontoDetalleLC(DealDetalle detalleOriginal, DealDetalle detalleNuevo);

	/**
	 * Consume la linea de credito para el proceso de modificacion de monto de un deal (lapiz) cuando este se 
	 * encuentra con pago Anticipado
	 * @param deal
	 * @param detalleNuevo
	 * @param montoAplicarLC
	 */
	void consumirLineaCreditoCambioMontoDealDetallePA(Deal deal, DealDetalle detalleNuevo, double montoAplicarLC);

	/**
	 * Consume la linea de credito para el proceso de modificacion de monto de un deal (lapiz) cuando este se 
	 * encuentra sin pago Anticipado
	 * @param deal
	 * @param detalleNuevo
	 * @param montoAplicarLC
	 */
	void consumirLineaCreditoCambioMontoDealDetalle(Deal deal, DealDetalle detalleNuevo, double montoAplicarLC);
	
	/**
	 * 
	 * Devuelve el monto USD del detalle otorgado 
	 * @param detalle
	 * @return
	 */
	public double getMontoUsdDealDetalle(DealDetalle detalle);

    
}

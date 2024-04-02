/*
 * $Id: DealService.java,v 1.14.30.1.8.1.6.1 2011/04/26 02:26:55 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import java.util.Date;
import java.util.Map;

import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.LimitesRestriccionOperacion;
import com.ixe.treasury.dom.common.DetalleLiquidacion;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.ixe.treasury.dom.common.Liquidacion;

/**
 * Componente que contiene l&oacute;gica de negocio para el manejo de deals y sus detalles.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.14.30.1.8.1.6.1 $ $Date: 2011/04/26 02:26:55 $
 */
public interface DealService {

    /**
     * Utilizado para construir un detalle de contraparte de un deal.
     *
     * @param deal                  El encabezado del deal.
     * @param recibimos             true para recibimos, false para entregamos.
     * @param fechaValor            CASH | TOM | SPOT | 72HR.
     * @param claveFormaLiquidacion La clave del producto.
     * @param div                   El objeto Divisa.
     * @param tipoCambioMesa        El tipo de cambio del pizarr&oacute;n.
     * @param monto                 El monto en la divisa.
     * @param tipoCambio            El tipo de cambio dado al cliente.
     * @param precioReferenciaMidSpot El precio referencia Mid Spot utilizado al momento de la captura.
     * @param precioReferenciaSpot  El precio referencia Spot utilizado al momento de la captura.
     * @param factorDivisa          El Factor Divisa actual.
     * @param idPrecioReferencia    El identificador del precio de referencia actual.
     * @param idFactorDivisa        El identificador del Factor Divisa actual.
     * @param idSpread              El identificador del spread actual.
     * @param mnemonico             El mnem&oacute;nico a aplicar.
     * @param fechasValor           El arreglo de fechas valor (h&aacute;biles).
     * @param montoMaximoExcedido   Si el montoMaximo ha sido excedido o no.
     * @param estado                El estado actual del sistema.
     * @param limRestOper Los l&iacute;mites de restricci&oacute;n de operaci&oacute;n.
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param ip La direcci&oacute;n IP.
     * @param usuario El usuario que realiza la operaci&oacute;n.
     * @return DealDetalle.
     * @throws SicaException Si algo sale mal.
     */
    DealDetalle crearDealDetalle(Deal deal, boolean recibimos, String fechaValor,
                                 String claveFormaLiquidacion, Divisa div, double tipoCambioMesa,
                                 double monto, double tipoCambio, Double precioReferenciaMidSpot, Double precioReferenciaSpot,
                                 Double factorDivisa, Integer idPrecioReferencia, Integer idFactorDivisa, int idSpread, String mnemonico,
                                 Date[] fechasValor, boolean montoMaximoExcedido, Estado estado,
                                 LimitesRestriccionOperacion limRestOper, String ticket, String ip,
                                 IUsuario usuario)
            throws SicaException;

    /**
     * Asigna los datos de comisi&oacute;n al detalle de acuerdo a su mnem&oacute;nico.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param det    El detalle del deal.
     * @param cobrar Si es para cobrar o pagar.
     */
    void calcularComisionDealDetalle(String ticket, DealDetalle det, boolean cobrar);

    /**
     * Crea y regresa un objeto Liquidacion a partir de la informaci&oacute;n del encabezado del
     * deal.
     *
     * @param deal                    El deal a liquidar.
     * @param ignorarPagoAntTomaFirme Si debe asignarse valor a Pago Anticipado y Toma en Firme o
     *      no (para el caso de que sea necesaria una autorizaci&oacute;n de cr&eacute;dito.
     * @return Liquidacion.
     */
    Liquidacion crearLiquidacion(Deal deal, boolean ignorarPagoAntTomaFirme);

    /**
     * Crea y regresa un objeto DetalleLiquidacion a partir de los datos de este detalle de deal y
     * de la forma de pago y liquidaci&oacute;n especificados.
     *
     * @param det El detalle del deal.
     * @param fpl La FormaPagoLiq.
     * @return DetalleLiquidacion.
     */
    DetalleLiquidacion crearDetalleLiquidacion(DealDetalle det, FormaPagoLiq fpl);

    /**
     * Permite saber si el detalle es liquidado por el SITE.
     *
     * @param deal El deal a revisar.
     * @return True o False.
     */
    boolean isLiquidadoPorSite(Deal deal);

    /**
     * Permite saber si el detalle es liquidado por el SITE.
     *
     * @param det El detalle de deal a a analizar.
     * @return True o False.
     */
    boolean isLiquidadoPorSite(DealDetalle det);

    /**
     * Regresa true si el deal no es simple, o si el detalle de entregamos tiene un producto
     * asociado.
     *
     * @param deal El deal a revisar.
     * @return boolean.
     */
    boolean isValidoBalanceoRec(Deal deal);

    /**
     * Regresa true si el deal no es simple, o si el detalle de recibimos tiene un producto
     * asociado.
     *
     * @param deal El deal a revisar.
     * @return boolean.
     */
    boolean isValidoBalanceoEnt(Deal deal);

    /**
     * Regresa un Map con todas las divisas involucradas en un Deal y sus respectivos detalles.
     *
     * @param deal El deal a inspeccionar.
     * @return Map Las divisas y sus detalles.
     */
    Map getDetallesDivisas(Deal deal);

    /**
     * Regresa un string con todas las claves de liquidaci&oacute;n encontradas en los detalles.
     *
     * @param deal El deal a inspeccionar.
     * @return String.
     */
    String getClavesFormasLiquidacion(Deal deal);

    /**
     * Regresa una referencia al bean formasPagoLiqService para realizar operaciones con
     * FormasPagoLiq.
     *
     * @return FormasPagoLiqService.
     */
    FormasPagoLiqService getFormasPagoLiqService();

    /**
     * Obtiene el valor por "default" de la fecha l&iacute;mite de captura de contrato
     * especificada en sc_parametro.
     *
     * @return Date La fecha l&iacute;mite de captura de contrato.
     */
    Date getFechaLimiteCapturaContrato();

    /**
     * Regresa la sumatoria de la diferencia entre el tipo de cambio de la mesa
     * y el tipo de cambio del cliente, multiplicada para el monto, de todos los
     * detalles que no est&aacute;n en Pesos Mexicanos.
     *
     * @param deal El deal a inspeccionar.
     * @return double.
     */
    double getUtilidadPromotor(Deal deal);

    /**
     * Regresa las formaPagoLiq para el mnem&oacute;nico especificado.
     *
     * @param ticket    El ticket de la sesi&oacute;n del usuario.
     * @param mnemonico El mnem&oacute;nico a encontrar.
     * @return FormaPagoLiq.
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getFormaPagoLiq(String, String).
     */
    FormaPagoLiq getFormaPagoLiq(String ticket, String mnemonico);

    /**
     * Regresa el valor de valorFuturoService.
     *
     * @return ValorFuturoService.
     */
    public ValorFuturoService getValorFuturoService();
}

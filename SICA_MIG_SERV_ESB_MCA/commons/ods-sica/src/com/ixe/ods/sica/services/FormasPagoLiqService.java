/*
 * $Id: FormasPagoLiqService.java,v 1.17 2010/04/30 17:21:34 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import java.util.List;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.treasury.dom.common.FormaPagoLiq;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.17 $ $Date: 2010/04/30 17:21:34 $
 */
public interface FormasPagoLiqService {

    /**
     * Regresa la lista de todas FormaPagoLiq llamando al bean SicaSiteCache que se encuentra
     * declarado en el applicationContext.xml.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @return List.
     * @see com.ixe.ods.sica.SicaSiteCache#obtenerFormasPagoLiq(String).
     */    
    List getFormasTiposLiq(String ticket);

    /**
     * Regresa el objeto FormaPagoLiq que corresponde al idMatriz especificado.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idMatriz La llave primaria del objeto que se desea encontrar.
     * @return FormaPagoLiq.
     * @throws SicaException Si no hay FormaPagoLiq con el idMatriz especificado.
     */    
    FormaPagoLiq getFormaPagoLiq(String ticket, Long idMatriz) throws SicaException;    

    /**
     * Regresa la lista de FormaPagoLiq de los productos del pizarr&oacute;n.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @return List.
     */    
    List getProductosPizarron(String ticket);

    /**
     * Filtra los Productos del Pizarr&oacute;n para obtener s&oacute;lo los de compra o
     * recepci&oacute;n, de una cierta divisa y de una cierta clave.
     *
     * @param ticket                El ticket de la sesi&oacute;n del usuario.
     * @param recibimos             True si es recepci&oacute;n.
     * @param idDivisa              La divisa a filtrar. Puede ser nulla si es que se
     *                              quieren productos de todas las divisas.
     * @param claveFormaLiquidacion La clave de la formas que se quieren
     *                              en particular. Puede ser nula si es que se quieren todos
     *                              los productos.
     * @return List Los productos del pizarr&oacute; filtrados.
     */
    List getProductosPizarron(String ticket, boolean recibimos, String idDivisa,
                              String claveFormaLiquidacion);

    /**
     * Regresa los productos que no son del pizarr&oacute;n, filtrando por recibimos o entregamos,
     * divisa y producto.
     *
     * @param ticket                El ticket de la sesi&oacute;n del usuario.
     * @param recibimos             true recibimos, false entregamos.
     * @param idDivisa              El identificador de la divisa.
     * @param claveFormaLiquidacion El identificador del producto.
     * @return List.
     */
    List getProductosNoPizarron(String ticket, boolean recibimos, String idDivisa,
                                String claveFormaLiquidacion);
            
    /**
     * Regresa un arreglo con las claves de las formas de liquidaci&oacute;n del pizarr&oacute;n o
     * en PESOS.
     *
     * @param ticket   El ticket de la sesi&oacute;n del usuario.
     * @param pizarron true para divisas, false para pesos.
     * @return String[].
     */
    String[] getClavesFormasLiquidacion(String ticket, boolean pizarron);
    
    /**
     * Regresa una lista con las claves de las formas de liquidaci&oacute;n del pirzarr&oacute;n,
     * filtrando por compra o venta y divisa.
     * 
     * @param ticket El Ticket de la sesi&oacute;n del usuario.
     * @param recibimos Si es recibimos o entregamos
     * @param idDivisa La clave de la divisa que queremos buscar.
     * @return String[].
     */
    String[] getClavesFormasLiquidacionPorPizarron(String ticket, boolean recibimos, String idDivisa);
            
    /**
     * Regresa la descripci&oacute;n del producto para la claveFormaLiquidacion proporcionada.
     *
     * @param ticket                El ticket de la sesi&oacute;n del usuario.
     * @param claveFormaLiquidacion El identificador del producto.
     * @return String.
     */
    String getNombreFormaLiquidacion(String ticket, String claveFormaLiquidacion);
            
    /**
     * Regresa la formaPagoLiq a la que corresponde el mnem&oacute;nico proporcionado.
     *
     * @param ticket    El ticket de la sesi&oacute;n del usuario.
     * @param mnemonico El mnem&oacute;nico a buscar.
     * @return FormaPagoLiq.
     */
    FormaPagoLiq getFormaPagoLiq(String ticket, String mnemonico);

    /**
     * Regresa la lista de mnem&oacute;nicos aplicbles, auxili&aacute;ndose de
     * <code>getFormasPagoLiqPosibles()</code>.
     *
     * @param ticket El ticket de la sesi&ocute;n del usuario.
     * @param det El detalle de deal.
     * @return List.
     */    
    List getMnemonicosPosiblesParaDealDetalle(String ticket, DealDetalle det);

    /**
     * Regresa true si la clave de forma de liquidaci&oacute;n o el mnem&oacute;nico corresponden a
     * la linea de cambios 2.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param claveFormaLiquidacion La clave del producto.
     * @param mnemonico El mnem&oacute;nico (opcional).
     * @param recibimos Si es recibimos o entregamos.
     * @return boolean.
     */
    boolean isRemesa(String ticket, String claveFormaLiquidacion, String mnemonico, 
                     boolean recibimos);
}

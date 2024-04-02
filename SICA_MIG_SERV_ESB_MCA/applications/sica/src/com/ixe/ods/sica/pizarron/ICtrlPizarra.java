/*
 * $Id: ICtrlPizarra.java,v 1.11.30.1.14.1 2011/04/26 02:52:10 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pizarron;

/**
 * <p>Interfaz para obtener la operaci&oacute;n capturada en el panel del pizarr&oacute;n.</p>
 *
 * @author Jean C. Favila
 * @version $Revision: 1.11.30.1.14.1 $ $Date: 2011/04/26 02:52:10 $
 */
public interface ICtrlPizarra {

    /**
     * Recibe los par&aacute;metros de la operaci&oacute;n capturada en el panel del
     * pizarr&oacute;n.
     *
     * Si quien implementa esta interfaz es el cliente del SICA en el Teller, &eacute;ste debe
     * utilizar SicaTellerSessionBean.agregarDetalleDeal(), pasando estos mismos par&aacute;metros
     * como referencia, para generar el detalle del deal con los datos de la operaci&oacute;n.
     *
     * @param cpra true para compra, false para venta.
     * @param tv Tipo Valor: CASH | TOM | SPOT.
     * @param idProd La clave del producto (TRAEXT, EFECTI, CHVIAJ, etc).
     * @param idDiv La clave de la divisa, seg&uacute;n la tabla SC_DIVISA.
     * @param tcm El tipo de cambio de la mesa.
     * @param monto El monto en la divisa de la operaci&oacute;n.
     * @param tcc El tipo de cambio otorgado al cliente.
     * @param prMidSpot El precio referencia Mid Spot utilizado.
     * @param prSpot El precio referencia Spot utilizado.
     * @param factorDivisa El Factor Divisa utilizado para la captura del detalle.
     * @param idSp El identificador del spread utilizado.
     * @param idPr El identificador del precio de referencia utilizado.
     * @param idFd El identificador del factor de divisa utilizado.
     */
    void operar(boolean cpra, String tv, String idProd, String idDiv, double tcm, double monto,
                double tcc, double prMidSpot, double prSpot, double factorDivisa, int idSp, int idPr, int idFd);
    
    
    /**
     * Recibe los par&aacute;metros de la operaci&oacute;n capturada en el panel de
     * mec&aacute;nica de neteos.
     *
     *
     * @param cpra Define que la operacion es una compra.
     * @param tvCpra Tipo valor del deal, del lado de la compra.
     * @param idProdCpra Id del producto de la compra.
     * @param idDivCpra Id de la divisa de la compra.
     * @param tcmCpra Tipo de cambio de la mesa para la compra.
     * @param montoCpra Monto del deal de la compra.
     * @param tccCpra Valor del tipo de cambio para el cliente, del lado de la compra.
     * @param prMidSpotCpra El precio referencia Mid Spot utilizado para la captura del detalle Compra.
     * @param prSpotCpra El precio referencia Spot utilizado para la captura del detalle Compra.
     * @param factorDivisaCpra El Factor Divisa utilizado para la captura del detalle Compra.
     * @param idPrCpra Id del precio de referencia actual para la compra.
     * @param idFdCpra Id del factor divisa para la compra.
     * @param idSpCpra Id del Spread para la compra.
     * @param vta Define que la operacion es una venta.
     * @param tvVta Tipo valor del deal, del lado de la venta.
     * @param idProdVta Id del producto de la venta.
     * @param idDivVta Id de la divisa de la venta.
     * @param tcmVta Tipo de cambio de la mesa para la venta.
     * @param montoVta Monto del deal de la venta.
     * @param tccVta Valor del tipo de cambio para el cliente, del lado de la venta.
     * @param prMidSpotVta El precio referencia Mid Spot utilizado para la captura del detalle Venta.
     * @param prSpotVta El precio referencia Spot utilizado para la captura del detalle Venta.
     * @param factorDivisaVta El Factor Divisa utilizado para la captura del detalle Venta.
     * @param idPrVta Id del precio de referencia actual para la venta.
     * @param idFdVta Id del factor divisa para la venta.
     * @param idSpVta Id del Spread para la venta.
     */
    void operarNeteos(boolean cpra, String tvCpra, String idProdCpra, String idDivCpra,
                      double tcmCpra, double montoCpra, double tccCpra, double prMidSpotCpra, 
                      double prSpotCpra, double factorDivisaCpra, int idPrCpra, int idFdCpra, int idSpCpra, 
                      boolean vta, String tvVta, String idProdVta, String idDivVta,
                      double tcmVta, double montoVta, double tccVta, double prMidSpotVta, 
                      double prSpotVta, double factorDivisaVta, int idPrVta, int idFdVta, int idSpVta);
}

/*
 * $Id: DealDetalleVO.as,v 1.2.42.1 2011/04/26 02:42:01 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

/**
 * Value Object para transmitir la informaci&oacute;n de un detalle de deal entre Flex y Java.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.2.42.1 $ $Date: 2011/04/26 02:42:01 $
 */
class com.ixe.ods.sica.vo.DealDetalleVO
{
    /**
     * La clase para el VO de DealDetalle.
     */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.DealDetalleVO", DealDetalleVO);

    /**
     * El n&uacute;mero de detalle de deal (llave primaria).
     */
    var idDealPosicion : Number;

    /**
     * El folio del detalle de deal.
     */
    var folioDetalle : Number;

    /**
     * La clave del producto.
     */
    var claveFormaLiquidacion : String;

    /**
     * True para compra, false para venta.
     */
    var recibimos : Boolean;

    /**
     * El mnem&oacute;nico de la forma de cobro o pago.
     */
    var mnemonico : String;

    /**
     * El monto de la operaci&oacute;n en divisa.
     */
    var monto : Number;

    /**
     * El tipo de cambio del pizarr&oacute;n.
     */
    var tipoCambioMesa : Number;

    /**
     * El tipo de cambio pactado con el cliente.
     */
    var tipoCambio : Number;

    /**
     * El n&uacute;mero de precio de referencia para este detalle.
     * @deprecated Se debe utilizar el valor del PR directo.
     */ 
     var idPrecioReferencia : Number;
    
    /**
     * El precio referencia Mid Spot utilizado en la captua del detalle.
     */
    var precioReferenciaSpot : Number;
    
    /**
     * El precio referencia Spot utilizado en la captua del detalle.
     */
    var  precioReferenciaMidSpot : Number;

    /**
     * El n&uacute;mero de factor divisa para este detalle.
     * @deprecated Se debe utilizar el valor directo del Factor Divisa.
     */
     var idFactorDivisa : Number;
     
    /**
     * El Factor Divisa utilizado en la captura del detalle.
     */
     var factorDivisa : Number;

    /**
     * El n&uacute;mero de spread para este detalle.
     */
    var idSpread : Number;

    /**
     * El importe de la operaci&oacute;n.
     */
    var importe : Number;

    /**
     * El status del detalle de deal.
     */
    var statusDetalleDeal : String;

    /**
     * La descripci&oacute;n legible del status del deal.
     */
    var descripcionStatus : String;

    /**
     * El objeto Divisa relacionado con este detalle de deal.
     */
    var idDivisa : String;

    /**
     * F)recuente, N)o frecuente, M)etal amonedado.
     */
    var tipoDivisa : String;

    /**
     * Para uso del m&oacute;dulo de reversos, indica si fue modificado el monto original del
     * detalle de deal.
     */
    var montoModificado : Boolean;

    /**
     * Para uso del m&oacute;dulo de reversos, indica si fue modificado el tipo de cambio del
     * detalle de deal.
     */
    var tipoCambioModificado : Boolean;

    /**
     * El identificador de la plantilla (puede ser null).
     */
    var idPlantilla : Number;

    /**
     * Una lista de HashMaps con la informaci&oacute;n de la plantilla asignada a este deal.
     */
    var datosAdicionales : Array;
}
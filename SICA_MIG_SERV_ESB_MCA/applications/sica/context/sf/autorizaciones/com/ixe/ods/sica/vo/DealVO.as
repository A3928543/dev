/*
 * $Id: DealVO.as,v 1.5.42.1 2011/04/26 02:41:53 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

import com.ixe.ods.sica.vo.DealDetalleVO;

/**
 * Value Object para transmitir la informaci&oacute;n de un deal entre Flex y Java.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.5.42.1 $ $Date: 2011/04/26 02:41:53 $
 */
class com.ixe.ods.sica.vo.DealVO
{
    /**
     * La clase para el VO de Deal.
     */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.DealVO", DealVO);

    /**
     * El n&uacute;mero de deal.
     */
    var idDeal : Number;

    /**
     * El n&uacute;mero de contrato Sica.
     */
    var noCuenta : String;

    /**
     * True para compra, false para venta.
     */
    var compra : Boolean;

    /**
     * El nombre de la persona de la bup a la que est&aacute; ligado el Contrato Sica.
     */
    var nombreCliente : String;

    /**
     * La fecha valor del deal (CASH, TOM, SPOT, 72HR, VFUT).
     */
    var fechaValor : String;

    /**
     * La fecha en que se captur&oacute; el deal.
     */
    var fechaCaptura : String;

    /**
     * La fecha en que debe liquidarse el deal, calculada con base a la fecha valor.
     */
    var fechaLiquidacion : String;

    /**
     * El status actual del Deal.
     */
    var statusDeal : String;

    /**
     * La descripci&oacute; legible del status del deal.
     */
    var descripcionStatus : String;

    /**
     * El n&uacute;mero de persona del promotor al que est&aacute; ligado el Contrato Sica.
     */
    var idPromotor : Number;

    /**
     * El nombre del promotor al que est&aacute; ligado el Contrato Sica.
     */
    var nombrePromotor : String;

    /**
     * El n&uacute;mero del usuario que captur&oacute; el deal.
     */
    var idUsuario : Number;

    /**
     * El nombre del usuario que captur&oacute; el deal.
     */
    var nombreUsuario : String;

    /**
     * True si el deal es interbancario.
     */
    var interbancario : Boolean;

    /**
     * Para uso del m&oacute;dulo de reversos: Si el deal se revers&oacute; por equivocaci&oacute;n
     * en el cliente.
     */
    var reversoPorCliente : Boolean;

    /**
     * Para uso del m&oacute;dulo de reversos: Si el deal se revers&oacute; por equivocaci&oacute;n
     * en la fecha valor.
     */
    var reversoPorFechaValor : Boolean;

    /**
     * El valor de la utilidad del deal para el promotor:
     */
    var utilidad : Number;

    /**
     * La lista de detalles de deal de compra o recibimos.
     */
    var detallesRecibimos : Array;

    /**
     * La lista de detalles de deal de venta o entregamos.
     */
    var detallesEntregamos : Array;

    public function tomarValores(deal : Object) : Void
    {
        idDeal = deal.idDeal;
        noCuenta = deal.noCuenta;
        compra = deal.compra;
        nombreCliente = deal.nombreCliente;
        fechaValor = deal.fechaValor;
        fechaCaptura = deal.fechaCaptura;
        fechaLiquidacion = deal.fechaLiquidacion;
        statusDeal = deal.statusDeal;
        descripcionStatus = deal.descripcionStatus;
        idPromotor = deal.idPromotor;
        nombrePromotor = deal.nombrePromotor;
        idUsuario = deal.idUsuario;
        nombreUsuario = deal.nombreUsuario;
        interbancario = deal.interbancario;
        reversoPorCliente = deal.reversoPorCliente;
        reversoPorFechaValor = deal.reversoPorFechaValor;
        utilidad = deal.utilidad;
        detallesRecibimos = crearDetalles(deal.detallesRecibimos);
        detallesEntregamos = crearDetalles(deal.detallesEntregamos);
		calcularUtilidad();
    }

    private function crearDetalles(detalles : Array) : Array
    {
        var resultados : Array = new Array();
        for (var i : Number = 0; i < detalles.length; i++) {
            var det = detalles[i];
            var detVO : DealDetalleVO = new DealDetalleVO();
            detVO.idDealPosicion = det.idDealPosicion;
            detVO.folioDetalle = det.folioDetalle;
            detVO.claveFormaLiquidacion = det.claveFormaLiquidacion;
            detVO.recibimos = det.recibimos;
            detVO.mnemonico = det.mnemonico;
            detVO.monto = det.monto;
            detVO.tipoCambioMesa = det.tipoCambioMesa;
            detVO.tipoCambio = det.tipoCambio;
            detVO.importe = det.importe;
            detVO.precioReferenciaMidSpot = det.precioReferenciaMidSpot;
            detVO.precioReferenciaSpot = det.precioReferenciaSpot;
            detVO.factorDivisa = det.factorDivisa;
            detVO.idPrecioReferencia = det.idPrecioReferencia;
            detVO.idFactorDivisa = det.idFactorDivisa;
            detVO.idSpread = det.idSpread;
            detVO.statusDetalleDeal = det.statusDetalleDeal;
            detVO.descripcionStatus = det.descripcionStatus;
            detVO.idDivisa = det.idDivisa;
            detVO.idPlantilla = det.idPlantilla;
            detVO.tipoDivisa = det.tipoDivisa;
            detVO.montoModificado = det.montoModificado;
            detVO.datosAdicionales = det.datosAdicionales;
			resultados[i] = detVO;
        }
        return resultados;
    }

	public function getBalance() : Number
	{
		var balance : Number = 0;
		var detalles : Array = new Array();

		detalles[0] = detallesRecibimos;
		detalles[1] = detallesEntregamos;
		for (var i = 0; i < detalles.length; i++) {
			for (var j = 0; j < detalles[i].length; j++) {
				var detalle : Object = detalles[i][j];
				balance = detalle.recibimos ? balance + detalle.importe : balance - detalle.importe;
			}
		}
		return balance;
	}

	public function calcularUtilidad() : Void
	{
		var detalles : Array = new Array();

		detalles[0] = detallesRecibimos;
		detalles[1] = detallesEntregamos;
		utilidad = 0;
		for (var i = 0; i < detalles.length; i++) {
			for (var j = 0; j < detalles[i].length; j++) {
				var detalle : Object = detalles[i][j];
				if (detalle.statusDetalleDeal != 'CA') {
					utilidad += detalle.monto
						* (detalle.recibimos ? detalle.tipoCambioMesa - detalle.tipoCambio
						: detalle.tipoCambio - detalle.tipoCambioMesa);
				}
			}
		}
	}
}
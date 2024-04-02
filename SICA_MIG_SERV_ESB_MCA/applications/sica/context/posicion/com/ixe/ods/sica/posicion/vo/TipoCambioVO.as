/*
 * $Id: TipoCambioVO.as,v 1.9 2008/02/22 18:25:45 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2004 LegoSoft S.C.
 */

/**
 * Value Object que es utilizado en la vista del Accordion con los distintos tipos de cambio
 * (Mesa, Cliente e Interbancario); este objeto se utiliza en cada renglon de esta vista,
 * contiene la siguiente informaci&oacute;n :
 * <li>Montos de la Posici&oacute;n Inicial para cada fecha valor en divisa y moneda nacional.</li>
 * <li>Montos de los tipos de cambio para compra y venta en moneda nacional.</li>
 * <li>Montos de documentos de transferencias en divisa y moneda nacional.</li>
 * <li>Montos para los Ticks de Tipo de Cambio para compra y venta en divisa y moneda nacional,
 * Tipo de Cambio de la Posici&oacute;n Final, Monto de la posici&oacute;n final, monto de la
 * Utilidad Global.</li>
 * 
 * @author David Solis, Jean C. Favila
 * @version  $Revision: 1.9 $ $Date: 2008/02/22 18:25:45 $
 */
class com.ixe.ods.sica.posicion.vo.TipoCambioVO extends com.ixe.ods.sica.posicion.vo.BasePosicionVO
{
	/**
	 * Monto de la posici&oacute;n inicial para Cash en moneda Nacional.
	 */
    var posicionInicialMnCash : Number;
    
    /**
	 * Monto de la posici&oacute;n inicial para TOM en moneda Nacional.
	 */
    var posicionInicialMnTom  : Number;
    
	/**
	 * Monto de la posici&oacute;n inicial para SPOT en moneda Nacional.
	 */
    var posicionInicialMnSpot : Number;

	/**
	 * Monto de la posici&oacute;n inicial para 72HR en moneda Nacional.
	 */
    var posicionInicialMn72Hr : Number;

	/**
	 * Monto de la posici&oacute;n inicial para VFUT en moneda Nacional.
	 */
    var posicionInicialMnVFut : Number;

	/**
	 * Monto del tipo de cambio para Compra Cash en moneda nacional.
	 */
    var compraMnCash : Number;
    
	/**
	 * Monto del tipo de cambio para Venta Cash en moneda nacional.
	 */
    var ventaMnCash  : Number;

	/**
	 * Monto del tipo de cambio para Compra TOM en moneda nacional.
	 */
    var compraMnTom  : Number;

	/**
	 * Monto del tipo de cambio para Venta TOM en moneda nacional.
	 */   
    var ventaMnTom   : Number;
    
	/**
	 * Monto del tipo de cambio para Compra SPOT en moneda nacional.
	 */    
    var compraMnSpot : Number;

	/**
	 * Monto del tipo de cambio para Venta SPOT en moneda nacional.
	 */    
    var ventaMnSpot  : Number;

	/**
	 * Monto del tipo de cambio para Compra 72HR en moneda nacional.
	 */
	var compraMn72Hr : Number;

	/**
	 * Monto del tipo de cambio para Venta 72HR en moneda nacional.
	 */
    var ventaMn72Hr  : Number;

	/**
	 * Monto del tipo de cambio para Compra VFUT en moneda nacional.
	 */
	var compraMnVFut : Number;

	/**
	 * Monto del tipo de cambio para Venta VFUT en moneda nacional.
	 */
    var ventaMnVFut  : Number;

	/**
	 * Monto de la posicion para documentos de transferencias.
	 */
    var documentosTransferencias  : Number;
    
	/**
	 * Monto de la posicion para efectivo.
	 */
    var efectivo                  : Number;

	/**
	 * Monto de la posicion para documentos de transferencias en moneda nacional.
	 */
    var documentosTransferenciasMn  : Number;
    
    /**
	 * Monto de la posicion para efectivo en moneda nacional.
	 */
    var efectivoMn                  : Number;

	/**
	 * Monto para el Tick del Tipo de Cambio para Compra en Divisa.
	 */
    var tickTipoCambioCompra        : Number = -1;
    
	/**
	 * Monto para el Tick del Tipo de Cambio para Compra en moneda nacional.
	 */
    var tickCompraMn                : Number = -1;
    
	/**
	 * Monto para el Tick del Tipo de Cambio para Venta en Divisa.
	 */    
    var tickTipoCambioVenta         : Number = -1;

	/**
	 * Monto para el Tick del Tipo de Cambio para Venta en moneda nacional.
	 */    
    var tickVentaMn                 : Number = -1;

	/**
	 * Monto para el Tick de la Posici&oacute;n Final en Divisa.
	 */
    var tickTipoCambioPosicionFinal : Number = -1;

	/**
	 * Monto para el Tick de la Posici&oacute;n Final en moneda nacional.
	 */
    var tickPosicionFinalMn         : Number = -1;

	/**
	 * Monto para el Tick de la Utilidad.
	 */
    var tickUtilidad 				: Number = -1;

	/**
	 * Monto del tipo de cambio para Compra.
	 */
    var compraMn : Number;
    
    /**
	 * Monto del tipo de cambio para Venta.
	 */
    var ventaMn : Number;
    
	/**
	 * Monto de la posici&oacute;n final en moneda nacional para Cash.
	 */
    var posicionFinalMnCash : Number;

	/**
	 * Monto de la posici&oacute;n final en moneda nacional para TOM.
	 */
    var posicionFinalMnTom : Number;

	/**
	 * Monto de la posici&oacute;n final en moneda nacional para SPOT.
	 */
    var posicionFinalMnSpot : Number;

	/**
	 * Monto de la posici&oacute;n final en moneda nacional para 72HR.
	 */
    var posicionFinalMn72Hr : Number;

	/**
	 * Monto de la posici&oacute;n final en moneda nacional para VFUT.
	 */
    var posicionFinalMnVFut : Number;

	/**
	 * Monto de la posici&oacute;n final en moneda nacional.
	 */
    var posicionFinalMn : Number;
    
    /**
	 * Monto del tipo de cambio para la posici&oacute;n inicial.
	 */
    var tipoCambioPosicionInicial : Number;
    
    /**
	 * Monto del tipo de cambio para Compra.
	 */
    var tipoCambioCompra : Number;

    /**
	 * Monto del tipo de cambio para Compra Cash.
	 */
    var tipoCambioCompraCash : Number;
    
    /**
	 * Monto del tipo de cambio para Compra TOM.
	 */    
    var tipoCambioCompraTom : Number;

    /**
	 * Monto del tipo de cambio para Compra SPOT.
	 */    
    var tipoCambioCompraSpot : Number;

    /**
	 * Monto del tipo de cambio para Compra 72HR.
	 */    
    var tipoCambioCompra72Hr : Number;

    /**
	 * Monto del tipo de cambio para Compra VFUT.
	 */    
    var tipoCambioCompraVFut : Number;
    
    /**
	 * Monto del tipo de cambio para Venta.
	 */    
    var tipoCambioVenta : Number;

    /**
	 * Monto del tipo de cambio para Venta Cash.
	 */    
    var tipoCambioVentaCash : Number;

    /**
	 * Monto del tipo de cambio para Venta TOM.
	 */    
    var tipoCambioVentaTom : Number;

    /**
	 * Monto del tipo de cambio para Venta SPOT.
	 */    
    var tipoCambioVentaSpot : Number;

    /**
	 * Monto del tipo de cambio para Venta 72HR.
	 */    
    var tipoCambioVenta72Hr : Number;

    /**
	 * Monto del tipo de cambio para Venta VFUT.
	 */    
    var tipoCambioVentaVFut : Number;
    
    /**
	 * Monto del tipo de cambio de la posici&oacute;n final Cash.
	 */        
    var tipoCambioPosicionFinalCash : Number;

    /**
	 * Monto del tipo de cambio de la posici&oacute;n final TOM.
	 */        
    var tipoCambioPosicionFinalTom : Number;

    /**
	 * Monto del tipo de cambio de la posici&oacute;n final SPOT.
	 */        
    var tipoCambioPosicionFinalSpot : Number;

    /**
	 * Monto del tipo de cambio de la posici&oacute;n final 72HR.
	 */        
    var tipoCambioPosicionFinal72Hr : Number;

    /**
	 * Monto del tipo de cambio de la posici&oacute;n final VFUT.
	 */        
    var tipoCambioPosicionFinalVFut : Number;

    /**
	 * Monto del tipo de cambio de la posici&oacute;n final.
	 */        
    var tipoCambioPosicionFinal : Number;
    
    /**
	 * Monto del BreakEven de la mesa.
	 */        
	var breakEven : Number;

    /**
	 * Monto del tipo de cambio para documentos de transferencias.
	 */        
    var tipoCambioDocumentosTransferencias  : Number;
   
    /**
	 * Monto del tipo de cambio para efectivo.
	 */        
    var tipoCambioEfectivo                  : Number;

    /**
	 * Monto de la posic&oacute;n inicial.
	 */        
	var posicionInicial : Number;

    /**
	 * Asigna los valores a los tipos de cambio, posicion final para cada fecha valor y compra-venta.
	 */        
    function initialise() : Void
    {
        compraMn = compraMnCash + compraMnTom + compraMnSpot + compraMn72Hr + compraMnVFut;
        ventaMn = ventaMnCash + ventaMnTom + ventaMnSpot + ventaMn72Hr + ventaMnVFut;
        posicionFinalMnCash = compraMnCash - ventaMnCash;
        posicionFinalMnTom = compraMnTom - ventaMnTom;
        posicionFinalMnSpot = compraMnSpot - ventaMnSpot;
        posicionFinalMn72Hr = compraMn72Hr - ventaMn72Hr;
        posicionFinalMnVFut = compraMnVFut - ventaMnVFut;
        posicionFinalMn = getPosicionInicialMn() + compraMn - ventaMn;
        tipoCambioPosicionInicial = getPosicionInicial() == 0 ? 0 : getPosicionInicialMn() / getPosicionInicial();
        tipoCambioCompra = compra == 0 ? 0 : compraMn / compra;
        tipoCambioCompraCash = compraCash == 0 ? 0 : compraMnCash / compraCash;
        tipoCambioCompraTom = compraTom == 0 ? 0 : compraMnTom / compraTom;
        tipoCambioCompraSpot = compraSpot == 0 ? 0 : compraMnSpot / compraSpot;
        tipoCambioCompra72Hr = compra72Hr == 0 ? 0 : compraMn72Hr / compra72Hr;
        tipoCambioCompraVFut = compraVFut == 0 ? 0 : compraMnVFut / compraVFut;
        tipoCambioVenta = venta == 0 ? 0 : ventaMn / venta;
        tipoCambioVentaCash = ventaCash == 0 ? 0 : ventaMnCash / ventaCash;
        tipoCambioVentaTom = ventaTom == 0 ? 0 : ventaMnTom / ventaTom;
        tipoCambioVentaSpot = ventaSpot == 0 ? 0 : ventaMnSpot / ventaSpot;
        tipoCambioVenta72Hr = venta72Hr == 0 ? 0 : ventaMn72Hr / venta72Hr;
        tipoCambioVentaVFut = ventaVFut == 0 ? 0 : ventaMnVFut / ventaVFut;
        tipoCambioPosicionFinalCash = posicionFinalCash == 0 ? 0 : posicionFinalMnCash / posicionFinalCash;
        tipoCambioPosicionFinalTom = posicionFinalTom == 0 ? 0 : posicionFinalMnTom / posicionFinalTom;
        tipoCambioPosicionFinalSpot = posicionFinalSpot == 0 ? 0 : posicionFinalMnSpot / posicionFinalSpot;
        tipoCambioPosicionFinal72Hr = posicionFinal72Hr == 0 ? 0 : posicionFinalMn72Hr / posicionFinal72Hr;
        tipoCambioPosicionFinalVFut = posicionFinalVFut == 0 ? 0 : posicionFinalMnVFut / posicionFinalVFut;

        breakEven = posicionFinal == 0 ? 0 : posicionFinalMn / posicionFinal;
        tipoCambioPosicionFinal = posicionFinal > 0 ? (getPosicionInicial() > 0 ? (compraMn + getPosicionInicialMn()) / (compra + getPosicionInicial()) : tipoCambioCompra) : (getPosicionInicial() > 0 ? tipoCambioVenta : (ventaMn - getPosicionInicialMn()) / (venta - getPosicionInicial()));

        tipoCambioDocumentosTransferencias = documentosTransferencias == 0 ? 0 : documentosTransferenciasMn / documentosTransferencias;
        tipoCambioEfectivo = efectivo == 0 ? 0 : efectivoMn / efectivo;

        posicionInicial = getPosicionInicial();
    }

    /**
     * Regresa la suma de posicionInicialMnCash m&#225;s posicionInicialMnTom m&#225;s posicionInicialMnSpot.
     *
     * @return Number.
     */
    function getPosicionInicialMn() : Number
    {
        return posicionInicialMnCash + posicionInicialMnTom + posicionInicialMnSpot + posicionInicialMn72Hr + posicionInicialMnVFut;
    }
    
    
    /**
	 * Obtiene el monto de la utilidad restando el tipo de cambio de la Posici&oacute;n Final al tipo de cambio del mercado.
	 */        
    function getUtilidad(tipoCambioMercado : Number) : Number
    {
      return (tipoCambioMercado - tipoCambioPosicionFinal) * posicionFinal;
    }
}
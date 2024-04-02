/*
 * $Id: PosicionVO.as,v 1.9 2008/02/22 18:25:45 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2004 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.vo.TipoCambioVO;

/**
 * Value Object que contiene la informacion para las vistas de la Posicion y sus Ticks:
 * contiene los montos de los siguientes valores:
 * 
 * <li>Montos de Posicion Final e Incial en moneda nacional y divisa para cada fecha valor.</li> 
 * <li>Montos de tipos de cambio para compras y ventas e interbancarias, en moneda nacional y divisa.</li> 
 * <li>Montos de la Posici&oacute;n para documentos  efectivo.</li>
 * <li>Montos para los Ticks de cada monto,</li>
 * 
 * @author David Solis, Jean C. Favila
 * @version  $Revision: 1.9 $ $Date: 2008/02/22 18:25:45 $
 */
class com.ixe.ods.sica.posicion.vo.PosicionVO extends com.ixe.ods.sica.posicion.vo.BasePosicionVO
{
	
	//Variables con los montos de los tipos de cambio y posicion final.
	/**
	 * Clase para el VO, PosicionVO. 
	 */
    static var registered = Object.registerClass("com.ixe.ods.sica.posicion.vo.PosicionVO", PosicionVO);

	/**
	 * Monto del Tick de la Posici&oacute;n Final Cash.
	 */
    var tickPosicionFinalCash : Number = -1;
    
	/**
	 * Monto del Tick de la Posici&oacute;n Final TOM.
	 */
    var tickPosicionFinalTom  : Number = -1;

	/**
	 * Monto del Tick de la Posici&oacute;n Final SPOT.
	 */
    var tickPosicionFinalSpot : Number = -1;

	/**
	 * Monto del Tick de la Posici&oacute;n Final 72HR.
	 */
	var tickPosicionFinal72Hr : Number = -1;

	/**
	 * Monto del Tick de la Posici&oacute;n Final VFUT.
	 */
	var tickPosicionFinalVFut : Number = -1;

	/**
	 * Monto en moneda nacional de la Posici&oacute;n Inicial Cash.
	 */
    var posicionInicialMnCash   : Number;

	/**
	 * Monto en moneda nacional de la Posici&oacute;n Inicial TOM.
	 */
    var posicionInicialMnTom    : Number;

	/**
	 * Monto en moneda nacional de la Posici&oacute;n Inicial SPOT.
	 */
    var posicionInicialMnSpot   : Number;

	/**
	 * Monto en moneda nacional de la Poici&oacute;n Inicial 72HR.
	 */
	var posicionInicialMn72Hr   : Number;

	/**
	 * Monto en moneda nacional de la Posici&oacute;n Inicial VFUT.
	 */
	var posicionInicialMnVFut   : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Cliente para Compra Cash.
	 */
    var compraMnClienteCash : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Cliente para Venta Cash.
	 */
    var ventaMnClienteCash  : Number;
    
    /**
	 * Monto en moneda nacional para el Tipo de Cambio Cliente para Compra TOM.
	 */
    var compraMnClienteTom  : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Cliente para Venta TOM.
	 */    
    var ventaMnClienteTom   : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Cliente para Compra SPOT.
	 */    
    var compraMnClienteSpot : Number;
    
	/**
	 * Monto en moneda nacional para el Tipo de Cambio Cliente para Venta SPOT.
	 */   
    var ventaMnClienteSpot  : Number;
    
	/**
	 * Monto en moneda nacional para el Tipo de Cambio Cliente para Compra 72HR.
	 */    
    var compraMnCliente72Hr : Number;
    
	/**
	 * Monto en moneda nacional para el Tipo de Cambio Cliente para Venta 72HR.
	 */    
    var ventaMnCliente72Hr  : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Cliente para Compra VFUT.
	 */	
	var compraMnClienteVFut : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Cliente para Venta VFUT.
	 */    
    var ventaMnClienteVFut  : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Pizarr&oacute;n para Compra Cash.
	 */	
    var compraMnPizarronCash : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Pizarr&oacute;n para Venta Cash.
	 */		
    var ventaMnPizarronCash  : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Pizarr&oacute;n para Compra TOM.
	 */	
    var compraMnPizarronTom  : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Pizarr&oacute;n para Venta TOM.
	 */	
    var ventaMnPizarronTom   : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Pizarr&oacute;n para Compra SPOT.
	 */	
    var compraMnPizarronSpot : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Pizarr&oacute;n para Venta SPOT.
	 */	
    var ventaMnPizarronSpot  : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Pizarr&oacute;n para Compra 72HR.
	 */	
	var compraMnPizarron72Hr : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Pizarr&oacute;n para Venta 72HR.
	 */	
    var ventaMnPizarron72Hr  : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Pizarr&oacute;n para Compra VFUT.
	 */	
	var compraMnPizarronVFut : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Pizarr&oacute;n para Venta VFUT.
	 */	
    var ventaMnPizarronVFut  : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Mesa para Compra Cash.
	 */	
    var compraMnMesaCash : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Mesa para Venta Cash.
	 */	
    var ventaMnMesaCash  : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Mesa para Compra TOM.
	 */	
    var compraMnMesaTom  : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Mesa para Venta TOM.
	 */	
    var ventaMnMesaTom   : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Mesa para Compra SPOT.
	 */	
    var compraMnMesaSpot : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Mesa para Venta SPOT.
	 */	
    var ventaMnMesaSpot  : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Mesa para Compra 72HR.
	 */	
    var compraMnMesa72Hr : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Mesa para Venta 72HR.
	 */	
    var ventaMnMesa72Hr  : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Mesa para Compra VFUT.
	 */	
	var compraMnMesaVFut : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio Mesa para Venta VFUT.
	 */	
    var ventaMnMesaVFut  : Number;

	/**
	 * Monto en moneda nacional para el Tipo de Cambio de Mercado.
	 */	
    var tipoCambioMercado : Number;

    /*
     * Utilizado para posicion de mesa / divisa.
     */
     /**
	 * El id de la mesa de cambios.
	 */	
    var idMesaCambio : Number;

    /*
     * Utilizado para posicion de mesa / divisa y posicion de divisas.
     */
	/**
	 * El id de la divisa.
	 */	     
    var idDivisa : String;

	/**
	 * Monto del Tick de Utilidad Realizada.
	 */	
    var tickUtilidadRealizadaMesa : Number = -1;

	/**
	 * Monto del Tick de la Utilidad Global.
	 */	
    var tickUtilidadRealizadaGlobal : Number = -1;

	/**
	 * Monto del Tick de Utilidad Realizada Global.
	 */	
    var utilidadRealizadaGlobal : Number = 0;
    
    	/**
	 * Monto del Tick de Utilidad Realizada de la Mesa.
	 */	
    var utilidadRealizadaMesa  : Number = 0;

	/**
	 * Monto del tipo de cambio para documentos de transferencia.
	 */	
    var documentosTransferencias  : Number = 1.0;
    
    /**
	 * Monto del tipo de camvio para efectivo..
	 */	
    var efectivo                  : Number = 1.0;

	/**
	 * Monto en moneda nacional del tipo de cambio para documentos de transferencias para el Cliente.
	 */	
    var documentosTransferenciasMnCliente  : Number = 0.0;

	/**
	 * Monto en moneda nacional del tipo de cambio para documentos de transferencias para el Pizarr&oacute;n.
	 */	
    var documentosTransferenciasMnPizarron : Number = 0.0;

	/**
	 * Monto en moneda nacional del tipo de cambio para documentos de transferencias para la Mesa.
	 */	
    var documentosTransferenciasMnMesa     : Number = 0.0;
    
	/**
	 * Monto en moneda nacional del tipo de cambio de efectivo para Cliente.
	 */	    
    var efectivoMnCliente                  : Number = 0.0;

	/**
	 * Monto en moneda nacional del tipo de cambio de efectivo para el Pizarr&oacute;n.
	 */	    
    var efectivoMnPizarron                 : Number = 0.0;

	/**
	 * Monto en moneda nacional del tipo de cambio de efectivo para la Mesa.
	 */	    
  	var efectivoMnMesa                     : Number = 0.0;

    /*
     * Para Interbancario
     */
     
	/**
	 * Monto del tipo de cambio para Compra Cash Interbancaria.
	 */	         
    var compraInCash : Number;

	/**
	 * Monto del tipo de cambio para venta Cash Interbancaria.
	 */	         
    var ventaInCash  : Number;

	/**
	 * Monto del tipo de cambio para Compra TOM Interbancaria.
	 */	         
    var compraInTom  : Number;

	/**
	 * Monto del tipo de cambio para Venta TOM Interbancaria.
	 */	         
    var ventaInTom   : Number;

	/**
	 * Monto del tipo de cambio para Compra SPOT Interbancaria.
	 */	         
    var compraInSpot : Number;

	/**
	 * Monto del tipo de cambio para Venta SPOT Interbancaria.
	 */	         
    var ventaInSpot  : Number;

	/**
	 * Monto del tipo de cambio para Compra 72HR Interbancaria.
	 */	         
	var compraIn72Hr : Number;

	/**
	 * Monto del tipo de cambio para Venta 72HR Interbancaria.
	 */	         
    var ventaIn72Hr  : Number;

	/**
	 * Monto del tipo de cambio para Compra VFUT Interbancaria.
	 */	         
	var compraInVFut : Number;

	/**
	 * Monto del tipo de cambio para Venta VFUT Interbancaria.
	 */	         
    var ventaInVFut  : Number;

	/**
	 * Monto del tipo de cambio para Compra Interbancaria.
	 */	         
    var compraIn            : Number;
    
	/**
	 * Monto del tipo de cambio para Venta Interbancaria.
	 */	             
    var ventaIn             : Number;
    
   	/**
	 * Monto de la posici&oacute;n final para Cash Interbancaria.
	 */	         
    var posicionFinalInCash : Number;

   	/**
	 * Monto de la posici&oacute;n final para TOM Interbancaria.
	 */
    var posicionFinalInTom  : Number;

   	/**
	 * Monto de la posici&oacute;n final para SPOT Interbancaria.
	 */
	var posicionFinalInSpot : Number;

   	/**
	 * Monto de la posici&oacute;n final para 72HR Interbancaria.
	 */
	var posicionFinalIn72Hr : Number;

   	/**
	 * Monto de la posici&oacute;n final para VFUT Interbancaria.
	 */
	var posicionFinalInVFut : Number;

   	/**
	 * Monto de la posici&oacute;n final Interbancaria.
	 */
    var posicionFinalIn     : Number;

   	/**
	 * Monto del Tick para Compra Interbancaria.
	 */
    var tickCompraIn            : Number = -1;

   	/**
	 * Monto del Tick para Venta Interbancaria.
	 */
    var tickVentaIn             : Number = -1;

   	/**
	 * Monto del Tick para posici&oacute;n final Cash Interbancaria.
	 */
    var tickPosicionFinalInCash : Number = -1;

   	/**
	 * Monto del Tick para posici&oacute;n final TOM Interbancaria.
	 */
    var tickPosicionFinalInTom  : Number = -1;

   	/**
	 * Monto del Tick para posici&oacute;n final SPOT Interbancaria.
	 */
    var tickPosicionFinalInSpot : Number = -1;

   	/**
	 * Monto del Tick para posici&oacute;n final 72HR Interbancaria.
	 */
    var tickPosicionFinalIn72Hr : Number = -1;

   	/**
	 * Monto del Tick para posici&oacute;n final VFUT Interbancaria.
	 */
    var tickPosicionFinalInVFut : Number = -1;

   	/**
	 * Monto del Tick para posici&oacute;n final Interbancaria.
	 */
    var tickPosicionFinalIn     : Number = -1;
    
	/*
     * Tipos de cambio
     */

   	/**
	 * VO para el tipo de cambio del Cliente.
	 */     
    private var __tipoCambioCliente  : TipoCambioVO;
    
   	/**
	 * VO para el tipo de cambio del Pizarr&oacute;n.
	 */         
    private var __tipoCambioPizarron : TipoCambioVO;

   	/**
	 * VO para el tipo de cambio de la Mesa.
	 */         
    private var __tipoCambioMesa     : TipoCambioVO;

   	/**
	 * Inicializa los valores de los monts de compras y ventas, posicion final para cada fecha valor y utilidad.
	 */     
    function initialise() : Void
    {
       compra = compraCash + compraTom + compraSpot + compra72Hr + compraVFut;
       compraIn = compraInCash + compraInTom + compraInSpot + compraIn72Hr + compraInVFut;
       venta = ventaCash + ventaTom + ventaSpot + venta72Hr + ventaVFut;
       ventaIn = ventaInCash + ventaInTom + ventaInSpot + ventaIn72Hr + ventaInVFut;
       posicionFinalCash = compraCash - ventaCash;
       posicionFinalInCash = compraInCash - ventaInCash;
       posicionFinalTom = compraTom - ventaTom;
       posicionFinalInTom = compraInTom - ventaInTom;
       posicionFinalSpot = compraSpot - ventaSpot;
       posicionFinalInSpot = compraInSpot - ventaInSpot;
       posicionFinal72Hr = compra72Hr - venta72Hr;
       posicionFinalIn72Hr = compraIn72Hr - ventaIn72Hr;
       posicionFinalVFut = compraVFut - ventaVFut;
       posicionFinalInVFut = compraInVFut - ventaInVFut;
       posicionFinal = getPosicionInicial() + compra - venta;
       posicionFinalIn = compraIn - ventaIn;
       utilidadRealizadaMesa = utilidadRealizadaMesa;
    }

   	/**
	 * Inicializa el valor de los tipos de cambio para Cliente.
	 * 
	 * @return TipoCambioVO.
	 */     
    function get tipoCambioCliente() : TipoCambioVO
    {
       if (__tipoCambioCliente != undefined)
          return __tipoCambioCliente;
       var tipoCambio = new TipoCambioVO();
       initTipoCambio(tipoCambio);
       tipoCambio.posicionInicialMnCash = posicionInicialMnCash;
       tipoCambio.posicionInicialMnTom = posicionInicialMnTom;
       tipoCambio.posicionInicialMnSpot = posicionInicialMnSpot;
       tipoCambio.posicionInicialMn72Hr = posicionInicialMn72Hr;
       tipoCambio.posicionInicialMnVFut = posicionInicialMnVFut;
       tipoCambio.compraMnCash = compraMnClienteCash;
       tipoCambio.ventaMnCash  = ventaMnClienteCash;
       tipoCambio.compraMnTom  = compraMnClienteTom;
       tipoCambio.ventaMnTom   = ventaMnClienteTom;
       tipoCambio.compraMnSpot = compraMnClienteSpot;
       tipoCambio.ventaMnSpot  = ventaMnClienteSpot;
       tipoCambio.compraMn72Hr = compraMnCliente72Hr;
       tipoCambio.ventaMn72Hr  = ventaMnCliente72Hr;
       tipoCambio.compraMnVFut = compraMnClienteVFut;
       tipoCambio.ventaMnVFut  = ventaMnClienteVFut;
       tipoCambio.documentosTransferenciasMn = documentosTransferenciasMnCliente;
       tipoCambio.efectivoMn = efectivoMnCliente;
       tipoCambio.initialise();
       __tipoCambioCliente = tipoCambio;
       return tipoCambio;
    }

   	/**
	 * Inicializa el valor de los tipos de cambio para el Pizarr&oacute;n con el valor definido.
	 * 
	 * @param tipoCambio El valor para el TipoCambioVO del cliente.
	 */     
    private function initTipoCambio(tipoCambio : TipoCambioVO)
    {
       tipoCambio.posicionInicialCash = posicionInicialCash;
       tipoCambio.posicionInicialTom = posicionInicialTom;
       tipoCambio.posicionInicialSpot = posicionInicialSpot;
       tipoCambio.posicionInicial72Hr = posicionInicial72Hr;
       tipoCambio.posicionInicialVFut = posicionInicialVFut;
       tipoCambio.compraCash = compraCash;
       tipoCambio.ventaCash = ventaCash;
       tipoCambio.compraTom = compraTom;
       tipoCambio.ventaTom = ventaTom;
       tipoCambio.compraSpot = compraSpot;
       tipoCambio.ventaSpot = ventaSpot;
       tipoCambio.compra72Hr = compra72Hr;
       tipoCambio.venta72Hr = venta72Hr;
       tipoCambio.compraVFut = compraVFut;
       tipoCambio.ventaVFut = ventaVFut;
       tipoCambio.tickCompra = tickCompra;
       tipoCambio.tickVenta = tickVenta;
       tipoCambio.tickPosicionFinal = tickPosicionFinal;
       tipoCambio.tickPosicionFinalCash = tickPosicionFinalCash;
       tipoCambio.tickPosicionFinalTom  = tickPosicionFinalTom;
       tipoCambio.tickPosicionFinalSpot = tickPosicionFinalSpot;
       tipoCambio.tickPosicionFinal72Hr = tickPosicionFinal72Hr;
       tipoCambio.tickPosicionFinalVFut = tickPosicionFinalVFut;

       tipoCambio.compra = compra;
       tipoCambio.venta = venta;
       tipoCambio.posicionFinalCash = posicionFinalCash;
       tipoCambio.posicionFinalTom = posicionFinalTom;
       tipoCambio.posicionFinalSpot = posicionFinalSpot;
       tipoCambio.posicionFinal72Hr = posicionFinal72Hr;
       tipoCambio.posicionFinalVFut = posicionFinalVFut;
       tipoCambio.posicionFinal = posicionFinal;
       tipoCambio.documentosTransferencias = documentosTransferencias;
       tipoCambio.efectivo = efectivo;
    }

	/**
	 * Inicializa el valor de los tipos de cambio para Pizarron.
	 * 
	 * @return TipoCambioVO.
	 */     
    function get tipoCambioPizarron() : TipoCambioVO
    {
       if (__tipoCambioPizarron != undefined)
          return __tipoCambioPizarron;
       var tipoCambio = new TipoCambioVO();
       initTipoCambio(tipoCambio);
       tipoCambio.posicionInicialMnCash = posicionInicialMnCash;
       tipoCambio.posicionInicialMnTom = posicionInicialMnTom;
       tipoCambio.posicionInicialMnSpot = posicionInicialMnSpot;
       tipoCambio.posicionInicialMn72Hr = posicionInicialMn72Hr;
       tipoCambio.posicionInicialMnVFut = posicionInicialMnVFut;
       tipoCambio.compraMnCash = compraMnPizarronCash;
       tipoCambio.ventaMnCash  = ventaMnPizarronCash;
       tipoCambio.compraMnTom  = compraMnPizarronTom;
       tipoCambio.ventaMnTom   = ventaMnPizarronTom;
       tipoCambio.compraMnSpot = compraMnPizarronSpot;
       tipoCambio.ventaMnSpot  = ventaMnPizarronSpot;
       tipoCambio.compraMn72Hr = compraMnPizarron72Hr;
       tipoCambio.ventaMn72Hr  = ventaMnPizarron72Hr;
       tipoCambio.compraMnVFut = compraMnPizarronVFut;
       tipoCambio.ventaMnVFut  = ventaMnPizarronVFut;
       tipoCambio.documentosTransferenciasMn = documentosTransferenciasMnPizarron;
       tipoCambio.efectivoMn = efectivoMnPizarron;
       tipoCambio.initialise();
       __tipoCambioPizarron = tipoCambio;
       return tipoCambio;
    }

	/**
	 * Inicializa el valor de los tipos de cambio para la Mesa.
	 * 
	 * @return TipoCambioVO.
	 */     
    function get tipoCambioMesa() : TipoCambioVO
    {
       if (__tipoCambioMesa != undefined)
          return __tipoCambioMesa;
       var tipoCambio = new TipoCambioVO();
       initTipoCambioMesa(tipoCambio);
       tipoCambio.posicionInicialMnCash = posicionInicialMnCash;
       tipoCambio.posicionInicialMnTom = posicionInicialMnTom;
       tipoCambio.posicionInicialMnSpot = posicionInicialMnSpot;
       tipoCambio.posicionInicialMn72Hr = posicionInicialMn72Hr;
       tipoCambio.posicionInicialMnVFut = posicionInicialMnVFut;
       tipoCambio.compraMnCash = compraMnMesaCash;
       tipoCambio.ventaMnCash  = ventaMnMesaCash;
       tipoCambio.compraMnTom  = compraMnMesaTom;
       tipoCambio.ventaMnTom   = ventaMnMesaTom;
       tipoCambio.compraMnSpot = compraMnMesaSpot;
       tipoCambio.ventaMnSpot  = ventaMnMesaSpot;
       tipoCambio.compraMn72Hr = compraMnMesa72Hr;
       tipoCambio.ventaMn72Hr  = ventaMnMesa72Hr;
       tipoCambio.compraMnVFut = compraMnMesaVFut;
       tipoCambio.ventaMnVFut  = ventaMnMesaVFut;
       tipoCambio.documentosTransferenciasMn = documentosTransferenciasMnMesa;
       tipoCambio.efectivoMn = efectivoMnMesa;
       tipoCambio.initialise();
       __tipoCambioMesa = tipoCambio;
       return tipoCambio;
    }

	/**
	 * Inicializa el valor de los tipos de cambio para Mesa con el valor definido.
	 * 
	 * @param tipoCambio El valor para el TipoCambioVO de la Mesa.
	 */     
    private function initTipoCambioMesa(tipoCambio : TipoCambioVO)
    {
       tipoCambio.posicionInicialCash = posicionInicialCash;
       tipoCambio.posicionInicialTom = posicionInicialTom;
       tipoCambio.posicionInicialSpot = posicionInicialSpot;
       tipoCambio.posicionInicial72Hr = posicionInicial72Hr;
       tipoCambio.posicionInicialVFut = posicionInicialVFut;
       tipoCambio.compraCash = compraInCash;
       tipoCambio.ventaCash = ventaInCash;
       tipoCambio.compraTom = compraInTom;
       tipoCambio.ventaTom = ventaInTom;
       tipoCambio.compraSpot = compraInSpot;
       tipoCambio.ventaSpot = ventaInSpot;
       tipoCambio.compra72Hr = compraIn72Hr;
       tipoCambio.venta72Hr = ventaIn72Hr;
       tipoCambio.compraVFut = compraVFut;
       tipoCambio.ventaVFut = ventaVFut;
       tipoCambio.tickCompra = tickCompraIn;
       tipoCambio.tickVenta = tickVentaIn;
       tipoCambio.tickPosicionFinal = tickPosicionFinalIn;
       tipoCambio.tickPosicionFinalCash = tickPosicionFinalInCash;
       tipoCambio.tickPosicionFinalTom  = tickPosicionFinalInTom;
       tipoCambio.tickPosicionFinalSpot = tickPosicionFinalInSpot;
       tipoCambio.tickPosicionFinal72Hr = tickPosicionFinalIn72Hr;
       tipoCambio.tickPosicionFinalVFut = tickPosicionFinalInVFut;
       tipoCambio.compra = compraIn;
       tipoCambio.venta = ventaIn;
       tipoCambio.posicionFinalCash = posicionFinalInCash;
       tipoCambio.posicionFinalTom = posicionFinalInTom;
       tipoCambio.posicionFinalSpot = posicionFinalInSpot;
       tipoCambio.posicionFinal72Hr = posicionFinalIn72Hr;
       tipoCambio.posicionFinalVFut = posicionFinalInVFut;
       tipoCambio.posicionFinal = posicionFinalIn;
       tipoCambio.documentosTransferencias = documentosTransferencias;
       tipoCambio.efectivo = efectivo;
	}
	
	/**
	 * Calcula los montos de los Ticks de las vistas de la Posici&oacute;n.
	 * 
	 * @param previous El PosicionVO con lo montos de la Posici&oacue;n.
	 */     
    function computeTicks(previous : PosicionVO) : Void
    {
        tickCompra        = computeTickValue(previous.compra, compra, false);
        tickVenta         = computeTickValue(previous.venta, venta, false);
        tickPosicionFinal = computeTickValue(previous.posicionFinal, posicionFinal, false);

        tickCompraIn        = computeTickValue(previous.compraIn, compraIn, false);
        tickVentaIn         = computeTickValue(previous.ventaIn, ventaIn, false);
        tickPosicionFinalIn = computeTickValue(previous.posicionFinalIn, posicionFinalIn, false);

        tickPosicionFinalCash = computeTickValue(previous.posicionFinalCash, posicionFinalCash, false);
        tickPosicionFinalTom = computeTickValue(previous.posicionFinalTom, posicionFinalTom, false);
        tickPosicionFinalSpot = computeTickValue(previous.posicionFinalSpot, posicionFinalSpot, false);
        tickPosicionFinal72Hr = computeTickValue(previous.posicionFinal72Hr, posicionFinal72Hr, false);
        tickPosicionFinalVFut = computeTickValue(previous.posicionFinalVFut, posicionFinalVFut, false);

        tickPosicionFinalInCash = computeTickValue(previous.posicionFinalInCash, posicionFinalInCash, false);
        tickPosicionFinalInTom = computeTickValue(previous.posicionFinalInTom, posicionFinalInTom, false);
        tickPosicionFinalInSpot = computeTickValue(previous.posicionFinalInSpot, posicionFinalInSpot, false);
        tickPosicionFinalIn72Hr = computeTickValue(previous.posicionFinalIn72Hr, posicionFinalIn72Hr, false);
        tickPosicionFinalInVFut = computeTickValue(previous.posicionFinalInVFut, posicionFinalInVFut, false);

        tickUtilidadRealizadaMesa = computeTickValue(previous.utilidadRealizadaMesa, utilidadRealizadaMesa, false);
        tickUtilidadRealizadaGlobal = computeTickValue(previous.utilidadRealizadaGlobal, utilidadRealizadaGlobal, false);

        tipoCambioCliente.tickTipoCambioCompra        =  computeTickValue(previous.tipoCambioCliente.tipoCambioCompra, tipoCambioCliente.tipoCambioCompra, true);
        tipoCambioCliente.tickCompraMn                =  computeTickValue(previous.tipoCambioCliente.compraMn, tipoCambioCliente.compraMn, false);
        tipoCambioCliente.tickTipoCambioVenta         =  computeTickValue(previous.tipoCambioCliente.tipoCambioVenta, tipoCambioCliente.tipoCambioVenta, true);
        tipoCambioCliente.tickVentaMn                 =  computeTickValue(previous.tipoCambioCliente.ventaMn, tipoCambioCliente.ventaMn, false);
        tipoCambioCliente.tickTipoCambioPosicionFinal =  computeTickValue(previous.tipoCambioCliente.tipoCambioPosicionFinal, tipoCambioCliente.tipoCambioPosicionFinal, true);
        tipoCambioCliente.tickPosicionFinalMn         =  computeTickValue(previous.tipoCambioCliente.posicionFinalMn, tipoCambioCliente.posicionFinalMn, false);
        tipoCambioCliente.tickUtilidad                = computeTickValue(previous.tipoCambioCliente.getUtilidad(previous.tipoCambioMercado), tipoCambioCliente.getUtilidad(tipoCambioMercado), false);

        tipoCambioPizarron.tickTipoCambioCompra        = computeTickValue(previous.tipoCambioPizarron.tipoCambioCompra, tipoCambioPizarron.tipoCambioCompra, true);
        tipoCambioPizarron.tickCompraMn                = computeTickValue(previous.tipoCambioPizarron.compraMn, tipoCambioPizarron.compraMn, false);
        tipoCambioPizarron.tickTipoCambioVenta         = computeTickValue(previous.tipoCambioPizarron.tipoCambioVenta, tipoCambioPizarron.tipoCambioVenta, true);
        tipoCambioPizarron.tickVentaMn                 = computeTickValue(previous.tipoCambioPizarron.ventaMn, tipoCambioPizarron.ventaMn, false);
        tipoCambioPizarron.tickTipoCambioPosicionFinal = computeTickValue(previous.tipoCambioPizarron.tipoCambioPosicionFinal, tipoCambioPizarron.tipoCambioPosicionFinal, true);
        tipoCambioPizarron.tickPosicionFinalMn         = computeTickValue(previous.tipoCambioPizarron.posicionFinalMn, tipoCambioPizarron.posicionFinalMn, false);
        tipoCambioPizarron.tickUtilidad                = computeTickValue(previous.tipoCambioPizarron.getUtilidad(previous.tipoCambioMercado), tipoCambioPizarron.getUtilidad(tipoCambioMercado), false);

        tipoCambioMesa.tickTipoCambioCompra        =  computeTickValue(previous.tipoCambioMesa.tipoCambioCompra, tipoCambioMesa.tipoCambioCompra, true);
        tipoCambioMesa.tickCompraMn                =  computeTickValue(previous.tipoCambioMesa.compraMn, tipoCambioMesa.compraMn, false);
        tipoCambioMesa.tickTipoCambioVenta         =  computeTickValue(previous.tipoCambioMesa.tipoCambioVenta, tipoCambioMesa.tipoCambioVenta, true);
        tipoCambioMesa.tickVentaMn                 =  computeTickValue(previous.tipoCambioMesa.ventaMn, tipoCambioMesa.ventaMn, false);
        tipoCambioMesa.tickTipoCambioPosicionFinal =  computeTickValue(previous.tipoCambioMesa.tipoCambioPosicionFinal, tipoCambioMesa.tipoCambioPosicionFinal, true);
        tipoCambioMesa.tickPosicionFinalMn         =  computeTickValue(previous.tipoCambioMesa.posicionFinalMn, tipoCambioMesa.posicionFinalMn, false);
        tipoCambioMesa.tickUtilidad                = computeTickValue(previous.tipoCambioMesa.getUtilidad(previous.tipoCambioMercado), tipoCambioMesa.getUtilidad(tipoCambioMercado), false);
    }
 }

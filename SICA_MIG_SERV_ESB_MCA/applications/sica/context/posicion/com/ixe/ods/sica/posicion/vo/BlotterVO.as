/*
 * $Id: BlotterVO.as,v 1.9 2008/02/22 18:25:44 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

/**
 * Value Object que contiene la lista de Deals capturados que se muestran en el monitor de la posici&oacute;n.
 * 
 * @author David Solis, Jean C. Favila
 * @version  $Revision: 1.9 $ $Date: 2008/02/22 18:25:44 $
 */
class com.ixe.ods.sica.posicion.vo.BlotterVO
{
	
	/**
	 * Clase para el VO del Blotter.
	 */
   static var registered = Object.registerClass("com.ixe.ods.sica.posicion.vo.BlotterVO", BlotterVO);

	/**
	 * El idDealPosicion del Deal que se muestra en el Blotter.
	 */
   var idDealPosicion : Number;
   
	/**
	 * El &iacute;ndice del Deal en el Blotter.
	 */
   var numeroDeal : Number;
   
   	/**
	 * El tipo de operaci&oacute;n.
	 */
   var tipoOperacion : String;
   
   	/**
	 * El nombre del promotor.
	 */
   var promotor : String;

   	/**
	 * El nombre del cliente.
	 */
   var cliente : String;
   
   	/**
	 * El monto del Deal.
	 */
   var monto : Number;
   
   	/**
	 * El monto del Deal en pesos mexicanos.
	 */
   var montoMn : Number;
   
   	/**
	 * El tipo de cambio para la Mesa
	 */
   var tipoCambioMesa : Number;
   
   	/**
	 * El tipo de cambio para el cliente.
	 */
   var tipoCambioCliente : Number;
   
   	/**
	 * El precio de referencia de la divisa.
	 */
   var precioReferencia : Number;
   
   	/**
	 * La divisa del deal.
	 */
   var divisa : String;
   
   	/**
	 * El nombre de la mesa de cambio.
	 */
   var nombreMesaCambio : String;
   
   	/**
	 * La fecha valor del Deal.
	 */
   var fechaValor : String;
}
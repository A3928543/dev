/*
 * $Id: ParametrosPizarronVO.as,v 1.9 2008/02/22 18:25:45 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

/**
 * Value Object que contiene los valores de los de los parametros del pizarr&oacute;n
 * de tipos de cambio que se obtiene desde el Monitor de la Posici&oacute;n.
 * 
 * @author David Solis, Jean C. Favila
 * @version  $Revision: 1.9 $ $Date: 2008/02/22 18:25:45 $
 */
class com.ixe.ods.sica.posicion.vo.ParametrosPizarronVO
{
	
	/**
	 * La clase para el VO de ParametrosPizarron. 
	 */
   static var registered = Object.registerClass("com.ixe.ods.sica.posicion.vo.ParametrosPizarronVO", ParametrosPizarronVO);

	/**
	 * EL tipo cambio para Spot.
	 */
   var tipoCambioSpot : Number;
   
   	/**
	 * El valor del spread para Compra y Venta.
	 */
   var spreadCompraVenta : Number;
   
   	/**
	 * El valor del carry.
	 */
   var carry : Number;

	/**
	 * El valor del tipo de cambio para Compra Cash,
	 */
   var tipoCambioCompraCash : Number;
   
   	/**
	 * El valor del tipo de cambio para Venta Cash.
	 */
   var tipoCambioVentaCash : Number;
   
	/**
	 * El valor del tipo de cambio para Compra TOM.
	 */
   var tipoCambioCompraTom : Number;
   
   	/**
	 * El valor del tipo de cambio para Venta TOM.
	 */
   var tipoCambioVentaTom : Number;
   
   	/**
	 * El valor del tipo de cambio para Compra SPOT.
	 */
   var tipoCambioCompraSpot : Number;
   
   	/**
	 * El valor del tipo de cambio para Venta SPOT.
	 */
   var tipoCambioVentaSpot : Number;
   
   	/**
	 * El valor del tipo de cambio para Compra 72HR. 
	 */
   var tipoCambioCompra72Hr : Number;
   
   	/**
	 * El valor del tipo de cambio para Venta 72HR.
	 */
   var tipoCambioVenta72Hr : Number;
   
  	/**
	 * El valor del tipo de cambio para Compra VFUT.
	 */
   var tipoCambioCompraVFut : Number;
   
   	/**
	 * El valor del tipo de cambio para Venta VFUT. 
	 */
   var tipoCambioVentaVFut : Number;
}

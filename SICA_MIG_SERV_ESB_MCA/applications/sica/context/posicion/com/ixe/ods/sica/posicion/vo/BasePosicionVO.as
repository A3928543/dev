/*
 * $Id: BasePosicionVO.as,v 1.9.38.1 2010/12/21 02:57:33 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright � 2004 LegoSoft S.C.
 */

/**
 * Value Object que contiene los montos para Posici&oacute;nn Incial, Compras-Ventas y Posici&oacute;n Final
 * para cada Fecha Valor; adem&aacute;s, calcula el monto de la Posicion Inicial para el Tick que se muestra en el 
 * monitor de la posici&oacute;n.
 * 
 * 
 * @author David Solis, Jean C. Favila
 * @version  $Revision: 1.9.38.1 $ $Date: 2010/12/21 02:57:33 $
 */
class com.ixe.ods.sica.posicion.vo.BasePosicionVO
{
	/**
	 * El monto para la Posici&pacute;n Inicial Cash.
	 */
    var posicionInicialCash : Number;
    
	/**
	 * El monto para la Posici&oacute;n Inicial TOM.
	 */
    var posicionInicialTom : Number;
    
	/**
	 * El monto para la Posici&oacute;n Inicial Spot.
	 */
    var posicionInicialSpot : Number;
    
	/**
	 * El monto para la Posici&oacute;n Inicial 72Hr
	 */
	var posicionInicial72Hr : Number;
	
	/**
	 * El monto para la Posici&oacute;n Inicial VFut
	 */
	var posicionInicialVFut : Number;
	
	/**
	 * El monto del Tipo de Cambio para Compras Cash.
	 */
    var compraCash : Number;
    
    /**
	 * El monto del Tipo de Cambio para Ventas Cash.
	 */
    var ventaCash  : Number;
    
	/**
	 * El monto del Tipo de Cambio para Comras TOM.
	 */
    var compraTom  : Number;
    
	/**
	 * El monto del Tipo de Cambio para Ventas TOM.
	 */
    var ventaTom   : Number;
    
	/**
	 * El monto del Tipo de Cambio para Compras SPOT.
	 */
    var compraSpot : Number;
    
	/**
	 * El monto del Tipo de Cambio para Ventas SPOT.
	 */
    var ventaSpot  : Number;
    
	/**
	 * El monto del Tipo de Cambio para Compras 72Hr.
	 */
	var compra72Hr : Number;

	/**
	 * El monto del Tipo de Cambio para Ventas 72Hr.
	 */
    var venta72Hr  : Number;
    
	/**
	 * El monto del Tipo de Cambio para Compras VFut.
	 */
	var compraVFut : Number;
	
	/**
	 * El monto del Tipo de Cambio para Ventas VFut.
	 */
    var ventaVFut  : Number;
	
	/**
	 * El valor inicial del monto de Compras para el Tick.
	 */
    var tickCompra : Number = -1;
    
	/**
	 * El valor inicial del monto Ventas para el Tick.
	 */
    var tickVenta  : Number = -1;
    
	/**
	 * El valor incial de la Posici&oacute;n Incial para el Tick.
	 */
    var tickPosicionFinal : Number = -1;

	/**
	 * El monto del Tick de la Posic&oacute;n Final para Cash.
	 */
    var tickPosicionFinalCash : Number = -1;
    
	/**
	 * El monto del Tick de la Posici&oacute;n Final para TOM.
	 */
    var tickPosicionFinalTom  : Number = -1;
    
	/**
	 * El monto del Tick de la Posici&oacute;n Final para SPOT.
	 */
    var tickPosicionFinalSpot : Number = -1;
     
	/**
	 * El monto del Tick de la Posici&oacute;n Final para 72Hr.
	 */
	var tickPosicionFinal72Hr : Number = -1;
	
	/**
	 * El monto del Tick de la Posici&oacute;n Final para VFut.
	 */
	var tickPosicionFinalVFut : Number = -1;
	
	/**
	 * El monto para Compras.
	 */
    var compra            : Number;
    
	/**
	 * El monto para Ventas.
	 */
    var venta             : Number;
    
	/**
	 * El monto de la Posici&oacute;n Final para Cash.
	 */
    var posicionFinalCash : Number;
    
    /**
	 * El monto de la Posici&oacute;n Final para TOM,
	 */
    var posicionFinalTom  : Number;
    
	/**
	 * El monto de la Posici&oacute;n Final para SPOT.
	 */
    var posicionFinalSpot : Number;
    
	/**
	 * El monto de la Posici&oacute;n Final para 72HR
	 */
	var posicionFinal72Hr : Number;
	
	/**
	 * El monto de la Posici&oacute;n Final para VFut.
	 */
	var posicionFinalVFut : Number;
	
	/**
	 * El monto de la Posici&oacute;n Final.
	 */
    var posicionFinal     : Number;

     /**
     * Indica si el tipo de cambio de 
     * divisa se tiene que dividir entre 1
     * cuando se presente dicha divisa con respecto
     * al dolar
     */  
   	var divideDivisa : Boolean;


	/**
	 * Define el contenido de los montos del Tick para el monitor de la posici&oacute;n.
	 * 
	 * @param currentNumber El valor actual del Tick.
	 * @param newNumber El nuevo valor para el Tick.
	 * @param foreignExchange Define si el valor es una divisa.
	 * @return Number 
	 */
    function computeTickValue(currentNumber : Number, newNumber : Number, foreignExchange : Boolean) : Number
    {
      if (currentNumber == undefined || newNumber == undefined)
         return -1;
      if (isNaN(currentNumber) || isNaN(newNumber))
         return -1;
      var currentInt = Math.ceil(foreignExchange ? currentNumber * 10000 : currentNumber);
      var newInt = Math.ceil(foreignExchange ? newNumber * 10000 : newNumber);
      return currentInt == newInt ? -1 : currentInt > newInt ? 0 : 1;
    }

   /**
     * Regresa la suma de posicionInicialCash m&#225;s posicionInicialTom m&#225;s posicionInicialSpot m&#225s;s
     * posicionInicial72Hr m&#225;s posicionInicialVFut.
     *
     * @return Number.
     */
    public function getPosicionInicial() : Number
    {
        return posicionInicialCash + posicionInicialTom + posicionInicialSpot + posicionInicial72Hr + posicionInicialVFut;
    }
}

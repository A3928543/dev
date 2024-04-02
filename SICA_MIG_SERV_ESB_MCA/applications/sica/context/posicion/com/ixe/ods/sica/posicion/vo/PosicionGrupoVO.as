
/**
 * Value Object que contiene la informaci&oacute;n de los montos de la Posici&oacute;n 
 * agupados por Divisa y Productos.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.vo.PosicionGrupoVO
{
	/**
	 * La clase para el VO de PosicionGrupo.
	 */
   static var registered = Object.registerClass("com.ixe.ods.sica.posicion.vo.PosicionGrupoVO", PosicionGrupoVO);
	
	/**
	 * El nombre del grupo.
	 */
   var nombre      : String;

   	/**
	 * El monto para Cash.
	 */
   var montoCash   : Number;
   
   	/**
	 * El monto en pesos mexicanos para Cash.
	 */
   var montoMnCash : Number;
   
   	/**
	 * El monto para Tom.
	 */
   var montoTom    : Number;
   
   	/**
	 * El monto en pesos mexicanos para TOM.
	 */
   var montoMnTom  : Number;
   
   	/**
	 * El monto para SPOT.
	 */
   var montoSpot   : Number;
   
   	/**
	 * El monto en pesos mexicanos para SPOT.
	 */
   var montoMnSpot : Number;
   
   	/**
	 * El monto para 72HR.
	 */
   var monto72Hr   : Number;
   
   	/**
	 * EL monto en pesos mexicanos para 72HR.
	 */
   var montoMn72Hr : Number
   
   	/**
	 * El monto para VFUT.
	 */
   var montoVFut   : Number;
   
   	/**
	 * El monto en pesos mexicanos para VFUT.
	 */
   var montoMnVFut : Number;

	/**
	 * El valor del tipo de cambio para Cash.
	 */
   var tipoCambioCash : Number;
   
  	/**
	 * El valor del tipo de cambio para TOM.
	 */
   var tipoCambioTom : Number;
   
   	/**
	 * El valor del tipo de cambio para SPOT.
	 */
   var tipoCambioSpot : Number;
   
   	/**
	 * El valor del tipo de cambio para 72HR.
	 */
   var tipoCambio72Hr : Number;
   
   	/**
	 * El valor del tipo de cambio para VFUT. 
	 */
   var tipoCambioVFut : Number;

	/**
	 * Inicializa el valor para los montos de los tipos de cambio de las fechas valor.
	 */
   function initialise() : Void
   {	
      tipoCambioCash = montoCash != 0 ? montoMnCash / montoCash : 0;
      tipoCambioTom =  montoTom != 0 ? montoMnTom / montoTom : 0;
      tipoCambioSpot = montoSpot != 0 ? montoMnSpot / montoSpot : 0; 
      tipoCambio72Hr = monto72Hr != 0 ? montoMn72Hr / monto72Hr : 0;
      tipoCambioVFut = montoVFut != 0 ? montoMnVFut / montoVFut : 0;
   }
}
import com.ixe.ods.sica.posicion.vo.TipoCambioVO;

/**
 * Value Object que contiene la informacion sobre el monto de la Utilidad Globla del SICA.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.vo.UtilidadGlobalVO
{
	
	/**
	 * Clase para el VO de UtilidadGlobal.
	 */
   static var registered = Object.registerClass("com.ixe.ods.sica.posicion.vo.UtilidadGlobalVO", UtilidadGlobalVO);

	/**
	 * El monto de la utilidad global.
	 */
   var utilidadGlobalMn : Number =  0.0;
   
   	/**
	 * El arreglo de las divisas.
	 */
   var divisas : Array;

	/**
	 * El valor inicial del tick de la utilidad.
	 */
   var tickUtilidad : Number = -1;

	/**
	 * Costructor de la clase.
	 */
   function UtilidadGlobalVO()
   {
   }

	/**
	 * Define el valor de la utilidad global:
	 * <li>-1 Si currentNumber es igual a newNumber</li>
	 * <li> 0 Si currentNumber es mayor que newNumber</li>
	 * <li>1 Si currentNumber es menor que newNumber</li>
	 * 
	 * @param currentNumber El valor inicial.
	 * @param newNumber El valor a comparar.
	 * @return Number
	 */
   private function computeTickValue(currentNumber : Number, newNumber : Number) : Number
   {
      var currentInt = Math.ceil(currentNumber);
      var newInt = Math.ceil(newNumber);
      return currentInt == newInt ? -1 : currentInt > newInt ? 0 : 1;
   }


	/**
	 * Asigna el valor del Tick para la Utilidad Global.
	 * 
	 * @param previous El valor a comparar.
	 */
   function computeTick(previous : UtilidadGlobalVO) : Void
   {
      tickUtilidad = computeTickValue(previous.utilidadGlobalMn, utilidadGlobalMn);
   }

	/**
	 * Obtiene el valor de los atributos del objecto en forma de String,
	 * 
	 * @return String.
	 */
   function toString() : String
   {
      var s : String = "UtilidadGlobalVO[utilidadGlobalMn=";
      s += utilidadGlobalMn;
      s += ", divisas=";
      s += divisas;
      s += "]";
      return s;
   }
}

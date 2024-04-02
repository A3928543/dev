/**
 * Value Object que contiene los valores del monto de la Utilidad por Divisa.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.vo.UtilidadDivisaVO
{
	
	/**
	 * La clase para el VO de UtilidadDivisa.
	 */
	static var registered = Object.registerClass("com.ixe.ods.sica.posicion.vo.UtilidadDivisaVO", UtilidadDivisaVO);

	/**
	 * El id de la Divisa.
	 */
	public var idDivisa : String;
	
	/**
	 * El monto de la utilidad. 
	 */
	public var utilidad : Number;

	/**
	 * Constructor de la clase. 
	 */
	public function UtilidadDivisaVO()
	{
	}

	/**
	 * Obtiene el valor de los atributos del objeto en forma de String.
	 */
   function toString() : String
   {
      var s : String = "UtilidadDivisaVO[idDivisa=";
      s += idDivisa;
      s += ", utilidad=";
      s += utilidad;
      s += "]";
      return s;
   }
}
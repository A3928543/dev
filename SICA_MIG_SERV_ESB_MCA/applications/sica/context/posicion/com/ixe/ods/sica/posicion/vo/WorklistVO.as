/**
 * Value Object que contiene la lista de DealWorkitems que se muestran en la
 * interfaz de Autorozaciones.
 * 
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.vo.WorklistVO
{
	/**
	 * Clase para el VO de Worklist. 
	 */
   static var registered = Object.registerClass("com.ixe.ods.sica.vo.WorklistVO", WorklistVO);
	
	/**
	 * El tipo del Worklist.
	 */
   var tipo : String;
   
   	/**
	 * La lista de Workitems.
	 */
   var workitems : Array;
}

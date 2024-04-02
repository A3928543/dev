
/**
 * Value Object que contiene la informacion para los Productos que se muestran
 * en el monitor de la posici&oacute;n
 */
class com.ixe.ods.sica.posicion.vo.ProductoVO
{
	/**
	 * Clase para el VO de Producto.
	 */
   static var registered = Object.registerClass("com.ixe.ods.sica.posicion.vo.ProductoVO", ProductoVO);

	/**
	 * El id del prodcto.
	 */
   var idProducto : String;
   
   	/**
	 * La descripci&oacute;n del producto.
	 */
   var descripcion : String;

	/**
	 * Regresa el valor de los atributos del objecto en forma de String.
	 * 
	 * @return String.
	 */
   function toString() : String
   {
      var s : String = "ProductoVO[idProducto=";
      s += idProducto;
      s += ", descripcion=";
      s += descripcion;
      s += "]";
      return s;
   }
}
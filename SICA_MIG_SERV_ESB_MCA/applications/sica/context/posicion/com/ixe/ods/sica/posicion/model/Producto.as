/*
 * $Id: Producto.as,v 1.8 2007/06/21 22:41:52 fribarra Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

/**
 * Modelo que define los atributos de Producto.
 */
class com.ixe.ods.sica.posicion.model.Producto
{
	/**
	 * El id del producto,
	 */
   var idProducto : String;
   	
   	/**
	 * La descripc&oacute;on de producto.
	 */
   var descripcion : String;
	
	/**
	 * Asigna los valores iniciales de las variables de instancia.
	 * 
	 * @param idProducto El id del producto.
	 * @param descripcion La descripci&oacute;n del producto.
	 */
   function Producto(idProducto : String, descripcion : String)
   {
      this.idProducto = idProducto;
      this.descripcion = descripcion;
   }

	/**
	 * Obtiene el valor de idProducto.
	 * 
	 * @return Boolean.
	 */
   function get id() :  String
   {
      return idProducto;
   }

	/**
	 * Obtiene el valor de isCanal.
	 * 
	 * @return Boolean.
	 */
   function get isCanal() : Boolean
   {
      return false;
   }

	/**
	 * Obtiene el valor de isSucursal.
	 * 
	 * @return Boolean.
	 */
   function get isSucursal() : Boolean
   {
      return false;
   }

	/**
	 * Obtiene el valor de isProducto.
	 * 
	 * @return Boolean.
	 */
   function get isProducto() : Boolean
   {
      return true;
   }

	/**
	 * Obtiene el valor de isMesa.
	 * 
	 * @return Boolean.
	 */
   function get isMesa() : Boolean
   {
      return false;
   }

	/**
	 * Obtiene el valor de isGrupoCanales.
	 * 
	 * @return Boolean.
	 */
   function get isGrupoCanales() : Boolean
   {
      return false;
   }

	/**
	 * Obtiene el valor de isGrupoSucursales.
	 * 
	 * @return Boolean.
	 */
   function get isGrupoSucursales() : Boolean
   {
      return false;
   }

	/**
	 * Obtiene el valor de isGrupoProductos.
	 * 
	 * @return Boolean.
	 */
   function get isGrupoProductos() : Boolean
   {
      return false;
   }

	/**
	 * Obtiene el valor de isDraggable.
	 * 
	 * @return Boolean.
	 */
   function get isDraggable() : Boolean
   {
      return true;
   }

	/**
	 * Obtiene los valores de los atrubutos del objeto en forma de String.
	 * 
	 * @return String.
	 */
   function toString() : String
   {
      var s : String = "Producto[idProducto=";
      s += idProducto;
      s += ", descripcion=";
      s += descripcion;
      s += "]";
      return s;
   }
}
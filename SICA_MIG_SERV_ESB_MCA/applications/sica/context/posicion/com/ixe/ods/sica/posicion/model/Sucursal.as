/*
 * $Id: Sucursal.as,v 1.8 2007/06/21 22:41:52 fribarra Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

/**
 * Modelo que define los atributos de Sucursal.
 */
class com.ixe.ods.sica.posicion.model.Sucursal
{
	/**
	 * El id del canal.
	 */
   var idCanal : String;
   
   	/**
	 * El nombre de la sucursal.
	 */
   var nombre : String;

	/**
	 * Inicializa los valores para las variables de instancia.
	 * 
	 * @param idCanal El id del canal de operaci&oacute;n.
	 * @param nombre El nombre del canal.
	 */
   function Sucursal(idCanal : String, nombre : String)
   {
      this.idCanal = idCanal;
      this.nombre = nombre;
   }

	/**
	 * Obtiene el valor de idCanal
	 * 
	 * @return String.
	 */
   function get id() :  String
   {
      return idCanal;
   }

	/**
	 * Obtiene el valor de isCanal.
	 * 
	 * @return Boolean.
	 */
   function get isCanal() : Boolean
   {
      return true;
   }

	/**
	 * Obtiene el valor de isSucursal.
	 * 
	 * @return Boolean.
	 */
   function get isSucursal() : Boolean
   {
      return true;
   }

	/**
	 * Obtiene el valor de isProducto.
	 * 
	 * @return Boolean.
	 */
   function get isProducto() : Boolean
   {
      return false;
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
	 * Obtiene los valores de los atributos del objeto en forma de String,
	 * 
	 * @return String.
	 */
   function toString() : String
   {
      var s : String = "Canal[idCanal=";
      s += idCanal;
      s += ", nombre=";
      s += nombre;
      s += "]";
      return s;
   }
}
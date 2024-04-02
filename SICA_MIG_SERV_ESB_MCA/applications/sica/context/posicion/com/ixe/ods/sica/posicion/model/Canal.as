/*
 * $Id: Canal.as,v 1.9 2008/02/22 18:25:49 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

/**
 * Modelo que define los atributos de Canal.
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.model.Canal
{
   /**
   * El id del canal.
   */
   var idCanal : String;
   
   /**
   * El nombre del canal. 
   */
   var nombre : String;
	
	/**
	* El id del Tipo de Pizarron.
	*/
	var idTipoPizarron : Number;

	/**
	 * Asigna los valores iniciales de las variables de instancia.
	 * 
	 * @param idCanal El id del Canal de Operaci&oacute;n.
	 * @param nombre El nombre del Canal.
	 * @param idTipoPizarron El id del Tipo de Pizarron.
	 */
   function Canal(idCanal : String, nombre : String, idTipoPizarron :  Number)
   {
      this.idCanal = idCanal;
      this.nombre = nombre;
	  this.idTipoPizarron = idTipoPizarron;
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
	 * Obtiene el valor de isTipoPizarron.
	 * 
	 * @return Boolean.
	 */	
   function get isTipoPizarron() : Boolean
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
      return false;
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
	 * Obtiene los valores de los atributos del objeto en forma de String.
	 * 
	 * @return String.
	 */
     function toString() : String
   {
      var s : String = "Canal[idCanal=";
      s += idCanal;
      s += ", nombre=";
      s += nombre;
      s += ", tipoPizarron=";
      s += idTipoPizarron;
      s += "]";
      return s;
   }
}
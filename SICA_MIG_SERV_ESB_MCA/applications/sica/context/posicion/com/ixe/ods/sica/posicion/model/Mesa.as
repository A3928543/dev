/*
 * $Id: Mesa.as,v 1.8 2007/06/21 22:41:52 fribarra Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

/**
 * Modelo que define los atributos de Mesa.
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.model.Mesa
{
	/**
	 * El id de la mesa.
	 */
   var idMesaCambio : Number;
   
   /**
    * El nombre de la mesa, 
    */
   var nombre : String;
   
   /**
	 * El id de la divisa
	 */
   var idDivisa : String;

	/**
	 * El arrgerlo de canales.
	 */
   var canales : Array;
   
   /**
	 * El arrgelo de sucursales.
	 */
   var sucursales : Array;
   
   /**
	 * El arreglo de productos.
	 */
   var productos : Array;

	/**
	 * Inicializa los valores de las variables de instancia.
	 * 
	 * @param idMesaCambio El id de la mesa de cambio.
	 * @param nombre El nombre de la mesa de cambio.
	 * @param idDivisa El id de la divisa.
	 */
   function Mesa(idMesaCambio : Number, nombre : String, idDivisa : String)
   {
      this.idMesaCambio = idMesaCambio;
      this.nombre = nombre;
      this.idDivisa = idDivisa;
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
      return false;
   }

	/**
	 * Obtiene el valor de isMesa.
	 * 
	 * @return Boolean.
	 */
   function get isMesa() : Boolean
   {
      return true;
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
      return false;
   }

	/**
	 * Obtiene los valores de los atributos del objeto en forma de String.
	 * 
	 * @return String.
	 */
   function toString() : String
   {
      var s : String = "Mesa[idMesaCambio=";
      s += idMesaCambio;
      s += ", nombre=";
      s += nombre;
      s += "]";
      return s;
   }
}
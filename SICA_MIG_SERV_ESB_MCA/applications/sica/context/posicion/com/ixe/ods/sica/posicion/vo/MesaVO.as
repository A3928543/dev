/*
 * $Id: MesaVO.as,v 1.9 2008/02/22 18:25:45 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

/**
 * Value Object que contiene la informaci&oacute;n de las Mesas de Operaci&oacute;n para
 * el DataGrid de Mesas.
 * 
 * @author David Solis, Jean C. Favila
 * @version  $Revision: 1.9 $ $Date: 2008/02/22 18:25:45 $
 */
class com.ixe.ods.sica.posicion.vo.MesaVO
{
	
	/**
	 * La clase para el VO de Mesa. 
	 */
   static var registered = Object.registerClass("com.ixe.ods.sica.posicion.vo.MesaVO", MesaVO);

	/**
	 * El id de la mesa de cambio.
	 */
   var idMesaCambio : Number;
   
	/**
	 * El nombre de la mesa de cambio.
	 */
   var nombre : String;
   
	/**
	 * El id de la divisa.
	 */
   var idDivisa : String;

	/**
	 * El arreglo de los canales de la mesa.
	 */
   var canales : Array;
   
	/**
	 * El arreglo de sucursales para la mesa,
	 */
   var sucursales : Array;
   
	/**
	 * El arreglo de productos para la mesa.
	 */
   var productos : Array;

	/**
	 * Regresa el valor de los atributos del objeto en forma de String,
	 * 
	 * @return String,
	 */
   function toString() : String
   {
      var s : String = "MesaVO[idMesaCambio=";
      s += idMesaCambio;
      s += ", nombre=";
      s += nombre;
      s += ", idDivisa=";
      s += idDivisa; 
      s += ", canales=";
      s += canales;
      s += ", sucursales=";
      s += sucursales;
      s += ", productos=";
      s += productos;
      s += "]";
      return s;
   }
}

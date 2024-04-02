/*
 * $Id: CanalVO.as,v 1.9 2008/02/22 18:25:45 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright ? 2006 LegoSoft S.C.
 */

/**
 * Value Object que contiene la informaci&oacute;n de los Canales de Operacion
 * para el DataGrid de Canales.
 * 
 * 
 * @author David Solis, Jean C. Favila
 * @version  $Revision: 1.9 $ $Date: 2008/02/22 18:25:45 $
 */
class com.ixe.ods.sica.posicion.vo.CanalVO
{
	/**
	 * Clase para el VO de Canal.
	 */
   static var registered = Object.registerClass("com.ixe.ods.sica.posicion.vo.CanalVO", CanalVO);

	/**
	 * El id del Canal.
	 */
   var idCanal : String;
   
	/**
	 * El id del Tipo de Pizarron.
	 */
   var idTipoPizarron : Number;

   	/**
	 * El nombre del Canal.
	 */
   var nombre : String;

	/**
	 * Obtiene el valor de los atributos del VO en forma de String.
	 * 
	 * @return String.
	 */
   function toString() : String
   {
      var s : String = "CanalVO[idCanal=";
      s += idCanal;
      s += ", nombre=";
      s += nombre;
	  s += ", idTipoPizarron=";
	  s += idTipoPizarron;	
      s += "]";
      return s;
   }
}
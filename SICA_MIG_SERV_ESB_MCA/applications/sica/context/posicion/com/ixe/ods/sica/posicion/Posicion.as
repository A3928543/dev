/*
 * $Id: Posicion.as,v 1.9.30.1 2010/06/16 16:02:51 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */


/**
 * Clase que contiene las constantes con los indices de las vistas que contiene el Monitor de la Posici&oacute;n
 * y los nombres de los comandos y servicios de las vistas para la comuncacion de los componentes con la Interfaz.
 * 
 * @author: David Solis, Jean C. Favila
 * @version $Revision: 1.9.30.1 $ $Date: 2010/06/16 16:02:51 $
 */
class com.ixe.ods.sica.posicion.Posicion
{
	//Constantes para el ViewStack del Monitor.
    /**
	 * Constante que define el valor para TOP_EMPTY_VIEW_INDEX.
	 */        
   static var TOP_EMPTY_VIEW_INDEX       : Number = 0;

    /**
	 * Constante que define el valor para MESA_VIEW_INDEX.
	 */        
   static var MESA_VIEW_INDEX            : Number = 1;

    /**
	 * Constante que define el valor para UTILIDAD_GLOBAL_VIEW_INDEX
	 */        
   static var UTILIDAD_GLOBAL_VIEW_INDEX : Number = 2;

    /**
	 * Constante que define el valor para TOP_NOT_AVAILABLE_INDEX.
	 */        
   static var TOP_NOT_AVAILABLE_INDEX    : Number = 3;

	//Constantes para los indices de las vistas del Monitor.
    /**
	 * Constante que define el valor para BOTOM_EMPTY_VIEW_INDEX.
	 */        
   static var BOTTOM_EMPTY_VIEW_INDEX        : Number = 0;
   
    /**
	 * Constante que define el valor para CANAL_VIEW_INDEX.
	 */           
   static var CANAL_VIEW_INDEX               : Number = 1;

    /**
	 * Constante que define el valor para SUCURSAL_VIEW_INDEX.
	 */           
   static var SUCURSAL_VIEW_INDEX            : Number = 1;

    /**
	 * Constante que define el valor para EFECTIVO_VIEW_INDEX.
	 */        
   static var EFECTIVO_VIEW_INDEX            : Number = 2;

    /**
	 * Constante que define el valor para PRODUCTO_VIEW_INDEX.
	 */        
   static var PRODUCTO_VIEW_INDEX            : Number = 3;

    /**
	 * Constante que define el valor para GRUPO_VIEW_INDEX.
	 */        
   static var GRUPO_VIEW_INDEX               : Number = 4;

    /**
	 * Constante que define el valor para PRODUCTOS_VIEW_INDEX.
	 */        
   static var PRODUCTOS_VIEW_INDEX           : Number = 4;

    /**
	 * Constante que define el valor para CANALES_VIEW_INDEX.
	 */        
   static var CANALES_VIEW_INDEX             : Number = 4;

    /**
	 * Constante que define el valor para BOTTOM_NOT_AVAILABLE_INDEX.
	 */        
   static var BOTTOM_NOT_AVAILABLE_INDEX     : Number = 5;

    /**
	 * Constante que define el valor para INVENTARIO_EFECTIVO_VIEW_INDEX
	 */        
   static var INVENTARIO_EFECTIVO_VIEW_INDEX : Number = 6;
	
   /**
   * Id mesa cambio OPERACION
   */
   static var OPERACION : Number = 1;
   
   /**
   * Id mesa cambio TRADER_USD
   */
   static var TRADER_USD : Number = 22;
   
   /**
   * Id mesa cambio TRADER_MXN
   */
   static var TRADER_MXN : Number = 25;
   
   /**
   * Id mesa cambio ESTRATEGIA
   */
   static var ESTRATEGIA : Number = 33;
	
	//Constantes para los nombres de las vistas del Monitor.
    /**
	 * Constante que define el valor para APPLICATION_VIEW.
	 */        
   static var APPLICATION_VIEW                  : String = "Application";

    /**
	 * Constante que define el valor para BLOTTER_VIEW.
	 */        
   static var BLOTTER_VIEW                      : String = "Blotter";

    /**
	 * Constante que define el valor para _VIEW.
	 */        
   static var PARAMETROS_PIZARRON_VIEW          : String = "ParametrosPizarron";

    /**
	 * Constante que define el valor para POSICION_CANAL_VIEW.
	 */        
   static var POSICION_CANAL_VIEW               : String = "PosicionCanal";

    /**
	 * Constante que define el valor para POSICION_DIVISAS_VIEW.
	 */        
   static var POSICION_DIVISAS_VIEW             : String = "PosicionDivisas";

    /**
	 * Constante que define el valor para POSICION_EFECTIVO_VIEW.
	 */        
   static var POSICION_EFECTIVO_VIEW            : String = "PosicionEfectivo";

    /**
	 * Constante que define el valor para POSICION_GRUPO_VIEW.
	 */        
   static var POSICION_GRUPO_VIEW               : String = "PosicionGrupo";

    /**
	 * Constante que define el valor para POSICION_MESA_VIEW.
	 */        
   static var POSICION_MESA_VIEW                : String = "PosicionMesa";

    /**
	 * Constante que define el valor para POSICION_NAVIGATOR_VIEW.
	 */        
   static var POSICION_NAVIGATOR_VIEW           : String = "PosicionNavigator";

    /**
	 * Constante que define el valor para POSICION_PRODUCTO_VIEW.
	 */        
   static var POSICION_PRODUCTO_VIEW            : String = "PosicionProducto";
    
    /**
	 * Constante que define el valor para UTILIDAD_GLOBAL_VIEW.
	 */        
   static var UTILIDAD_GLOBAL_VIEW              : String = "UtilidadGlobal";

    /**
	 * Constante que define el valor para POSICION_INVENTARIO_EFECTIVO_VIEW.
	 */        
   static var POSICION_INVENTARIO_EFECTIVO_VIEW : String = "PosicionInventarioEfectivo";

	//Constantes para los nombres de los comandos de las vistas.
    /**
	 * Constante que define el valor para _COMMAND
	 */        	
   static var BLOTTER_COMMAND                            : String = "getBlotter";

    /**
	 * Constante que define el valor para PARAMETROS_PIZARRON_COMMAND.
	 */
   static var PARAMETROS_PIZARRON_COMMAND                : String = "getParametrosPizarron";

    /**
	 * Constante que define el valor para DIVISAS_COMMAND.
	 */
   static var DIVISAS_COMMAND                            : String = "getDivisas";

    /**
	 * Constante que define el valor para POSICION_MESAS_COMMAND
	 */
   static var POSICION_MESA_COMMAND                      : String = "getPosicionMesa";

    /**
	 * Constante que define el valor para TIMER_COMMAND
	 */
   static var TIMER_COMMAND                              : String = "synchInformacion";

    /**
	 * Constante que define el valor para POSICION_DIVISAS_COMMAND
	 */
   static var POSICION_DIVISAS_COMMAND                   : String = "getPosicionDivisas";

    /**
	 * Constante que define el valor para UTILIDAD_GLOBAL_COMMAND
	 */
   static var UTILIDAD_GLOBAL_COMMAND                    : String = "getUtilidadGlobal";

    /**
	 * Constante que define el valor para POSICION_CANALES_COMMAND
	 */
   static var POSICION_CANALES_COMMAND                   : String = "getPosicionCanales";

    /**
	 * Constante que define el valor para POSICION_PRODUCTOS_COMMAND
	 */
   static var POSICION_PRODUCTOS_COMMAND                 : String = "getPosicionProductos";

    /**
	 * Constante que define el valor para POSICION_SUCURSALES_COMMAND
	 */
   static var POSICION_SUCURSALES_COMMAND                : String = "getPosicionSucursales";

    /**
	 * Constante que define el valor para POSICION_EFECTIVO_COMMAND
	 */
   static var POSICION_EFECTIVO_COMMAND                  : String = "getPosicionEfectivo";

    /**
	 * Constante que define el valor para POSICION_PRODUCTO_COMMAND
	 */
   static var POSICION_PRODUCTO_COMMAND                  : String = "getPosicionProducto";

    /**
	 * Constante que define el valor para POSICION_CANAL_COMMAND
	 */
   static var POSICION_CANAL_COMMAND                     : String = "getPosicionCanal";

    /**
	 * Constante que define el valor para QUERY_POSICION_CANALES_COMMAND
	 */
   static var QUERY_POSICION_CANALES_COMMAND             : String = "runQueryPosicionCanales";

    /**
	 * Constante que define el valor para QUERY_POSICION_PRODUCTOS_COMMAND
	 */
   static var QUERY_POSICION_PRODUCTOS_COMMAND           : String = "runQueryPosicionProductos";

    /**
	 * Constante que define el valor para QUERY_POSICION_CANALES_PRODUCTOS_COMMAND
	 */
   static var QUERY_POSICION_CANALES_PRODUCTOS_COMMAND   : String = "runQueryPosicionCanalesProductos";

    /**
	 * Constante que define el valor para SHOW_MESA_COMMAND
	 */
   static var SHOW_MESA_COMMAND                          : String = "showMesa";

    /**
	 * Constante que define el valor para ARBOL_COMMAND
	 */
   static var ARBOL_COMMAND                              : String = "getArbol";

    /**
	 * Constante que define el valor para SHOW_APPLET_COMMAND
	 */
   static var SHOW_APPLET_COMMAND                        : String = "showApplet";

    /**
	 * Constante que define el valor para UPDATE_TIPO_CAMBIO_COMMAND
	 */
   static var UPDATE_TIPO_CAMBIO_COMMAND                 : String = "updateTipoCambio";

    /**
	 * Constante que define el valor para POSICION_INVENTARIO_EFECTIVO_COMMAND
	 */
   static var POSICION_INVENTARIO_EFECTIVO_COMMAND       : String = "getPosicionInventarioEfectivo";

    /**
	 * Constante que define el valor para QUERY_POSICION_INVENTARIO_EFECTIVO_COMMAND
	 */
   static var QUERY_POSICION_INVENTARIO_EFECTIVO_COMMAND : String = "runQueryPosicionInventarioEfectivo";

    /**
	 * Constante que define el valor para VALOR_FUTURO_COMMAND
	 */
   static var VALOR_FUTURO_COMMAND                       : String = "getValorFuturo";

	//Constantes de los nombre de las vistas.
    /**
	 * Constante que define el valor para _COMMAND
	 */	
   static var EFECTIVO            : String = "EFECTIVO";

    /**
	 * Constante que define el valor para POSICION
	 */
   static var POSICION            : String = "POSICION";

    /**
	 * Constante que define el valor para CANALES
	 */
   static var CANALES             : String = "CANALES";

    /**
	 * Constante que define el valor para SUCURSALES
	 */
   static var SUCURSALES          : String = "SUCURSALES";

    /**
	 * Constante que define el valor para PRODUCTOS.
	 */
   static var PRODUCTOS           : String = "PRODUCTOS";

    /**
	 * Constante que define el valor para INVENTARIO_EFECTIVO.
	 */
   static var INVENTARIO_EFECTIVO : String = "INVENTARIO DE EFECTIVO";

	//Constantes de los nombres de las vistas para el PosicionNavigator.
	/**
	 * Constante que define el valor para EFECTI.
	 */
   static var EFECTIVO_KEY              : String = "EFECTI";
	
	/**
	 * Constante que define el valor para SUC
	 */
   static var SUCURSALES_KEY            : String = "SUC";

    /**
	 * Constante que define el valor para INVEFEC
	 */
   static var INVENTARIO_EFECTIVO_KEY   : String = "INVEFEC";

	//Constantes para los url's externos a la aplicacion del Monitor (Pizarron de Tipos de Cambio y Vista del Detalle de Deal).
	/**
	 * Constante que define el valor para APPLET_URL
	 */
   static var APPLET_URL       : String = "/sica/app?service=external/PizarronExt&sp=";
   
    /**
	 * Constante que define el valor para MESA_DETALLE_URL
	 */
   static var MESA_DETALLE_URL : String = "/sica/app?service=external/MuestraDeal&sp=";

	//Constante para el nombre del servicio para lel Monitor.
    /**
	 * Constante que define el valor para POSICION_SERVICE.
	 */
   static var POSICION_SERVICE : String = "posicionService";
}
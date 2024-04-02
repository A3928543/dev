/*
 * $Id: PosicionDelegate.as,v 1.8.40.2 2010/07/23 02:16:37 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

import org.nevis.cairngorm.business.*;

import mx.utils.Delegate;

/**
 * El business delegate de cairngorm para el monitor de la posici&#243;n.
 *
 * @author: David Solis, Jean C. Favila
 * @version $Revision: 1.8.40.2 $ $Date: 2010/07/23 02:16:37 $
 */
class com.ixe.ods.sica.posicion.business.PosicionDelegate
{	
	/**
	 * El contrato para el servicio.
	 */
	private var responder : Responder;
	
	/**
	 * Asigna el valor para responder. 
	 * 
	 * @param responder El valor para responder.
	 */
    function PosicionDelegate(responder:Responder)
    {
		this.responder = responder;
    }
	
	/**
	 * Obtiene y resuelve las llamadas a los servicios para el Monitor de la Posici&oacute;n
	 * 
	 * @param call La llamada al servicio de la posici&oacute;n.
	 */
	private function setupResultHandler(call) : Void
	{
		call.resultHandler = Delegate.create(Object(responder), responder.onResult);
		call.faultHandler = Delegate.create(Object(responder), responder.onFault);
	}
	
	/**
	 * Obtiene el servicio para el Monitor de la Posici&oacute;n. 
	 */
    private function getPosicionService()
    {
       return ServiceLocator.getInstance().getService("posicionService");
    }

    /**
     * Obtiene el resumen de deals filtrado por un monto minimo para una mesa de cambio.
     * 
     * @param idMesaCambio El id de la mesa de cambio.
     * @param montoMinimoDeal El monto minimo de deals para mostrar.
     * 
     */
	function getBlotter(idMesaCambio : Number, montoMinimoDeal : Number): Void
	{
  	    var service = getPosicionService();
		var call = service.getBlotter(idMesaCambio, montoMinimoDeal);
		setupResultHandler(call);
	}

    /**
     * Obtiene los par&aacute;metros del pizarr&oacute;n.
     */
	function getParametrosPizarron(): Void
	{
  	    var service = getPosicionService();
		var call = service.getParametrosPizarron();
		setupResultHandler(call);
	}

    /**
     * Obtiene las divisas de una mesa.
     */
	function getDivisas(idMesaCambio : Number): Void
	{
  	    var service = getPosicionService();
		var call = service.getDivisas(idMesaCambio);
		setupResultHandler(call);
	}
	
	/**
	 * Obtiene del servicio, la posici&oacute;n de la mesa para una divisa.
	 * 
	 * @param idMesa El id de la mesa de cambios.
	 * @param idDivisa El id de la divisa.
	 */
	function getPosicionMesa(idMesa : Number, idDivisa : String) : Void
	{
  	    var service = getPosicionService();
		var call = service.getPosicionMesa(idMesa, idDivisa);
		setupResultHandler(call);
	}

	/**
	 * Obtiene del servicio, la posici&oacute;n para cada divisa.
	 * @param idMesaCambio El id de la mesa de cambios.
	 */
	function getPosicionDivisas(idMesaCambio: Number) : Void
	{
  	    var service = getPosicionService();
		var call = service.getPosicionDivisas(idMesaCambio);
		setupResultHandler(call);
	}
	
	
	/**
	 * Obtiene del servicio, la utilidad global.
	 */
	function getUtilidadGlobal() : Void
	{
  	    var service = getPosicionService();
		var call = service.getUtilidadGlobal();
		setupResultHandler(call);
	}
	
	/**
	 * Obtiene del servicio, la posici&oacute;n para un canal para una divisa.
	 * 
	 * @param idMesa El id de la mesa de cambios.
	 * @param idDivisa El id de la divisa.
	 */
	function getPosicionCanales(idMesa : Number, idDivisa : String) : Void
	{
  	    var service = getPosicionService();
		var call = service.getPosicionCanales(idMesa, idDivisa);
		setupResultHandler(call);
	}

	/**
	 * Obtiene del servicio, la posici&oacute;n de una divisa para cada producto.
	 * 
	 * @param idMesa El id de la mesa de cambios.
	 * @param idDivisa El id de la divisa.
	 */
	function getPosicionProductos(idMesa : Number, idDivisa : String) : Void
	{
  	    var service = getPosicionService();
		var call = service.getPosicionProductos(idMesa, idDivisa);
		setupResultHandler(call);
	}

	/**
	 * Obtiene del servicio, la posici&oacute;n de una sucursal para una divisa.
	 * 
	 * @param idMesa El id de la mesa de cambios.
	 * @param idDivisa El id de la divisa.
	 */
	function getPosicionSucursales(idMesa : Number, idDivisa : String) : Void
	{
  	    var service = getPosicionService();
		var call = service.getPosicionSucursales(idMesa, idDivisa);
		setupResultHandler(call);
	}

	/**
	 * Obtiene del servicio, la posici&oacute;n en efectivo para una divisa.
	 * 
	 * @param idMesa El id de la mesa de cambios.
	 * @param idDivisa El id de la divisa.
	 */
	function getPosicionEfectivo(idMesa : Number, idDivisa : String) : Void
	{
  	    var service = getPosicionService();
		var call = service.getPosicionEfectivo(idMesa, idDivisa);
		setupResultHandler(call);
	}

	/**
	 * Obtiene del servicio, la posici&oacute;n para producto de una divisa.
	 * 
	 * @param idMesa El id de la mesa de cambios.
	 * @param idProducto El id del producto de la operaci&oacute;n.
	 */
	function getPosicionProducto(idMesa : Number, idDivisa : String, idProducto : String) : Void
	{
  	    var service = getPosicionService();
		var call = service.getPosicionProducto(idMesa, idDivisa, idProducto);
		setupResultHandler(call);
	}
	
	/**
	 * Obtiene del servicio, la posici&oacute;n de un canal para una divisa.
	 * 
	 * @param idMesa El id de la mesa de cambios.
	 * @param idDivisa El id de la divisa.
	 * @param idCanal El id del canal de operacion.
	 */
	function getPosicionCanal(idMesa : Number, idDivisa : String, idCanal : String) : Void
	{
  	    var service = getPosicionService();
		var call = service.getPosicionCanal(idMesa, idDivisa, idCanal);
		setupResultHandler(call);
	}
	
	/**
	 * Ejecuta la llamada al servicio para acutalizar los montos de la posici&oacute;n para los canales.
	 * 
	 * @param idMesa El id de la mesa de cambios.
	 * @param canales El arreglo con los canales de operaci&oacute;n.
	 */
	function runQueryPosicionCanales(idMesa : Number, idDivisa : String, canales : Array) : Void
	{
	    trace("canales.length->" + canales.length);
  	    var service = getPosicionService();
		var call = service.getQueryPosicionCanales(idMesa, idDivisa, canales);
		setupResultHandler(call);
	}
	
	/**
	 * Ejecuta la llamada al servicio para actualizar los montos de la posici&oacute;n de cada prodcto.
	 * 
	 * @param idMesa El id de la mesa de cambios.
	 * @param idDivisa El id de la divisa.
	 * @param productos El arreglo de productos.
	 */
	function runQueryPosicionProductos(idMesa : Number, idDivisa : String, productos : Array) : Void
	{
	    trace("productos.length->" + productos.length);
  	    var service = getPosicionService();
		var call = service.getQueryPosicionProductos(idMesa, idDivisa, productos);
		setupResultHandler(call);
	}

	/**
	 * Ejecuta la llamada al servicio para actualizar los montos de la posici&oacute;n de los canales y sus productos.
	 * 
	 * @param idMesa El id de la mesa de cambios.
	 * @param idDivisa El id de la divisa.
	 * @param canales El arreglo de los canales de operaci&oacute;n.
	 * @param prodcutos El arreglo de los productos.
	 */
	function runQueryPosicionCanalesProductos(idMesa : Number, idDivisa : String, canales : Array, productos : Array) : Void
	{
  	    var service = getPosicionService();
		var call = service.getQueryPosicionCanalesProductos(idMesa, idDivisa, canales, productos);
		setupResultHandler(call);
	}
	
	/**
	 * Obtiene el arbol de los canales de operaci&oacute;n.
	 */
	function getArbol() : Void
	{
  	    var service = getPosicionService();
		var call = service.getArbol();
		setupResultHandler(call);
	}

	/**
	 * Obtiene el monto de la Posicion para inventario en efectivo.
	 * 
	 * @param idMesa El id de la mesa de cambios.
	 * @param idDivisa El id de la divisa.
	 */
	function getPosicionInventarioEfectivo(idMesa : Number, idDivisa : String) : Void
	{
  	    var service = getPosicionService();
		var call = service.getPosicionInventarioEfectivo(idMesa, idDivisa);
		setupResultHandler(call);
	}
	
	/**
	 * Ejecuta la llamada al servicio para actualizar el monto de la posici&oacute;n para efectivo.
	 * 
	 * @param idMesa El id de la mesa de cambios.
	 * @param idDivisa El id de la divisa.
	 * @param sucursales El arreglo de sucursales.
	 */ 
	function runQueryPosicionInventarioEfectivo(idMesa : Number, idDivisa : String, sucursales : Array) : Void
	{
	    trace("sucursales.length->" + sucursales.length);
  	    var service = getPosicionService();
		var call = service.getQueryPosicionInventarioEfectivo(idMesa, idDivisa, sucursales);
		setupResultHandler(call);
	}

    /**
     * Ejecuta el servicio getValorFuturo() sobre el servicio de posici&#243;n.
     */
	function getValorFuturo() : Void
	{
  	    var service = getPosicionService();
		var call = service.getValorFuturo();
		setupResultHandler(call);
	}
}

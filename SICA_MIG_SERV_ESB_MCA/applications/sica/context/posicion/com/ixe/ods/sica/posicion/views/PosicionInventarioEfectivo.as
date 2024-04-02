/*
 * $Id: PosicionInventarioEfectivo.as,v 1.9 2008/02/22 18:25:27 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.EventBroadcaster;

import mx.utils.Delegate;

/**
 * Clase que define el comportamiento para la vista de la Posici7oacute;n de Inventario de Efectivo.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.PosicionInventarioEfectivo extends mx.containers.VBox
{
	
	/**
	 * Constante que define el separador del texto.
	 */
    private static var SEPARATOR : String = ", ";
    
	/**
	 * El DataProvider para la clase.
	 */
    var dataProvider : PosicionVO;

	/**
	 * Constructor de la clase.
	 */
    function PosicionInventarioEfectivo()
    {
    }

	/**
	 * M&eacute;todo que se ejecuta al iniciar el componente; registra los eventos del componente.
	 */
	function init() : Void
	{
	   super.init();
       addEventListener("childrenCreated", Delegate.create(this, childrenCreated));
	}

	/**
	 * Registra los eventos del componente; registra la vista de la Posici&oacute;n para Inventario de Efectivo.
	 */
	private function childrenCreated() : Void
	{
       ViewLocator.getInstance().registerView(Posicion.POSICION_INVENTARIO_EFECTIVO_VIEW, this);
	}

	/**
	 * Define si la etiqueta para la vista de Inventario de Efectivo es o no visible.
	 */
	function updateLabel4InventarioEfectivo() : Void
	{
	   this['sucursalesBox'].visible = false;
	}

	/**
	 * Define si la etiqueta para la vista de Inventario de Efectivo de Sucursales es o no visible.
	 * 
	 * @return sucursales El arreglo con los nombres de las sucursales.
	 */
	function updateLabel4Sucursales(sucursales: Array) : Void
	{
       this['sucursalLabel'].text = sucursales.length == 1 ? "Sucursal:" : "Sucursales:";
       this['sucursales'].text = sucursales.join(SEPARATOR);
       this['sucursalesBox'].visible = true;
	}
}
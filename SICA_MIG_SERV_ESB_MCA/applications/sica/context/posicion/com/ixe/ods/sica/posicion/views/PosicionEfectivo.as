/*
 * $Id: PosicionEfectivo.as,v 1.9 2008/02/22 18:25:27 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.EventBroadcaster;

import mx.utils.Delegate;

/**
 * Clase que define el comportamiento para la vista de la Posicion para Efectivo.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.PosicionEfectivo extends mx.containers.VBox
{
	
	/**
	 * El DataProvider de la vista.
	 */
    var dataProvider : PosicionVO;


	/**
	 * Constructor para la clase.
	 */
    function PosicionEfectivo()
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
	 * M&eacute;todo que registra los eventos para el componente; registra la vista
	 * para la divisa seleccionada.
	 */
	private function childrenCreated() : Void
	{
       ViewLocator.getInstance().registerView(Posicion.POSICION_EFECTIVO_VIEW, this);
	}
}

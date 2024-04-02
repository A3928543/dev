/*
 * $Id: PosicionGrupo.as,v 1.9 2008/02/22 18:25:27 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.EventBroadcaster;

import mx.utils.Delegate;

/**
 * Clase que define el comportamiento para la vista de la Posici&oacute;n por Divisas y Productos.
 * 
 * @author David Solis, Jean C. Favila.
 * 
 */
class com.ixe.ods.sica.posicion.views.PosicionGrupo extends mx.containers.VBox
{
	
	/**
	 * El DataProvider de la clase.
	 */
    var dataProvider : Array;
   	
   	/**
	 * Define si el grupo de la posici&oacute;n es por canales. 
	 */
    var isGrupoCanales : Boolean = true;

	/**
	 * Constructor de la clase.
	 */
    function PosicionGrupo()
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
	 * Registra los eventos del componente; registra la vista de la Posici&oacute;n del grupo seleccionado.
	 */
	private function childrenCreated() : Void
	{
       ViewLocator.getInstance().registerView(Posicion.POSICION_GRUPO_VIEW, this);
	}
}

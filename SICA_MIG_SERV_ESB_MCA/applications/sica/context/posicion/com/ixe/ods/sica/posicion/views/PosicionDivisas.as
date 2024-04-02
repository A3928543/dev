/*
 * $Id: PosicionDivisas.as,v 1.9.30.1 2010/06/16 16:00:32 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.EventBroadcaster;

import mx.utils.Delegate;

/**
 * Clase que define el comportamiento de la vista para de la Posici&oacute;n para Divisas.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.PosicionDivisas extends mx.containers.VBox
{
	/**
	 * El DataProvider de la vista.
	 */
    var dataProvider : Array;

	/**
	 * Constructor de la clase.
	 */
    function PosicionDivisas()
    {
    }

	/**
	 * M&eacute;todo que se ejecuta al iniciar el coponente; registra los eventos para el componente;
	 */
	function init() : Void
	{
	   super.init();
       addEventListener("childrenCreated", Delegate.create(this, childrenCreated));
	}

	/**
	 * M&eacute;todo que registra los eventos para el componente.
	 */
	private function childrenCreated() : Void
	{
       this['synchLink'].addEventListener("click", Delegate.create(this, sync));
       ViewLocator.getInstance().registerView(Posicion.POSICION_DIVISAS_VIEW, this);
       EventBroadcaster.getInstance().broadcastEvent(Posicion.POSICION_DIVISAS_COMMAND,{idMesaCambio: Posicion.OPERACION});
	}

	/**
	 * Envia un mensaje de BroadCast a la aplicacion para sincrinizar la informaci&oacute;n.
	 */
    private function sync() : Void
    {
       var idMesaCambio: Number =this['accordion'].selectedChild.idMesaCambio
       EventBroadcaster.getInstance().broadcastEvent(Posicion.POSICION_DIVISAS_COMMAND, {idMesaCambio: idMesaCambio});
    }
	
	/**
	 * Actualiza los datos actuales con un nuevo DataPovider .
	 * 
	 * @param dataProvider El nuevo dataProvider del componente.
	 */
    function updateDataProvider(dataProvider : Array)
    {
       this.dataProvider = dataProvider;
       this['accordion'].selectedChild['changeDataGridSelection']();
    }
}

/*
 * $Id: BaseCommand.as,v 1.9 2008/02/22 18:25:37 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.business.PosicionDelegate;
import com.legosoft.view.ViewLocator;

import mx.core.UIObject;

/**
 * Clase que define los comandos onResult y onFault para  el Monitor de la Posici&oacute;n.
 * 
 * @author David Solis, Jean C. Favila.
 *  
 */
class com.ixe.ods.sica.posicion.commands.BaseCommand extends com.ixe.ods.sica.posicion.commands.AbstractCommand implements org.nevis.cairngorm.business.Responder
{	
	/**
	 * El Delegate para la comunicacion con los servicios del monitor.
	 */
    private var delegate : PosicionDelegate;

	/**
	 * Asigna el valor para el delegate.
	 */
    public function BaseCommand()
    {
        delegate = new PosicionDelegate(this);
    }
	
	/**
	 * No hace nada.
	 * 
	 * @param event El evento generado por el comando.
	 */
    public function onResult(event : Object) : Void
    {
        // To be implemented by subclasses
    }
	
	/**
	 * Toma el mensaje en caso de error.
	 * 
	 * @param event El evento generado por el comando.
	 */
	public function onFault(event : Object) : Void
	{
       trace("onFault->" + event.fault.faultstring);
       selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.BOTTOM_NOT_AVAILABLE_INDEX);
    }
	
	/**
	 * Obtiene el valor del id de la mesa.
	 * 
	 * @return Number.
	 */
    function get idMesaCambio() : Number
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_NAVIGATOR_VIEW);
       return view['idMesaCambio'];
    }
	
	/**
	 * Obtiene el valor del id de la divisa.
	 * 
	 * @return String.
	 */
    function get idDivisa() : String
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_NAVIGATOR_VIEW);
       return view['idDivisa'];
    }
}
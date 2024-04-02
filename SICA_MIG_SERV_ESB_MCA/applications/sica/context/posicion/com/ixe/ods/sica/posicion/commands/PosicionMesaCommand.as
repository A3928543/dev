/*
 * $Id: PosicionMesaCommand.as,v 1.9 2008/02/22 18:25:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.model.Mesa;
import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.Event;

import mx.core.UIObject;

/**
 * Este comando implementa una secuencia de comandos:
 * <ul>
 * <li>Obtiene la posici&oacute;n para la mesa / divisa.</li>
 * <li>Obtiene el DataProvider para la vista de la posici&oacute;n de la mesa / divisa. </li>
 * </ul>
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.commands.PosicionMesaCommand extends com.ixe.ods.sica.posicion.commands.BaseSequenceCommand
{
	
	/**
	 * Constructor de la clase; asigna el siguiente evento que se refleja en el Blotter. 
	 * 
	 * @param event El evento generado por el comando.
	 */
    public function PosicionMesaCommand()
    {
        super();
        nextEvent = Posicion.BLOTTER_COMMAND;
    }
    
	/**
	 * Obtiene la posici&oacute;n para la mesa / divisa.
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       trace("PosicionMesaCommand idMesaCambio->" + idMesaCambio + " idDivisa->" + idDivisa);
       if (idMesaCambio != undefined && idDivisa != undefined)
          delegate.getPosicionMesa(idMesaCambio, idDivisa);
       else
          executeNextCommand();
    }
	
	/**
	 * Obtiene el DataProvider para la vista de la posici&oacute;n de la mesa / divisa.
	 * 
	 * @param event El evento generado por el comando.
	 */
    public function onResult(event : Object) : Void
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_MESA_VIEW);
	   var dataProvider : PosicionVO = event.result;
       if (dataProvider != null) {
          view['updateDataProvider'](dataProvider);
          view['reset']();
          var navigatorView : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_NAVIGATOR_VIEW);
          var mesa : Mesa = navigatorView['mesa'];
          view['title'] = mesa.nombre;
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX);
       }
       else {
          selectTopAndBottomViews(Posicion.TOP_NOT_AVAILABLE_INDEX, Posicion.BOTTOM_EMPTY_VIEW_INDEX);
       }
       executeNextCommand();
    }
	
	/**
	 * Toma el mensaje en caso de error.
	 * 
	 * @param event El evento generado por el comando.
	 */ 
	public function onFault(event : Object) : Void
	{
       trace("onFault->" + event.fault.faultstring);
       selectTopAndBottomViews(Posicion.TOP_NOT_AVAILABLE_INDEX);
    }
}

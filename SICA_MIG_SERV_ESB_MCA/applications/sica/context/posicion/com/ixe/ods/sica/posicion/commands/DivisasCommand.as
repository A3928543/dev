/*
 * $Id: DivisasCommand.as,v 1.9 2008/02/22 18:25:37 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.vo.DivisaVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.Event;

import mx.core.UIObject;

/**
 * Este comando implementa una secuencia de comandos:
 * <ul>
 * <li>Obtiene las divisas para una mesa.</li>
 * <li>Obtiene la posici&oacute;n para una mesa / divisa. </li>
 * </ul>
 * 
 * @author David Solis, Jean C,Favila
 */
class com.ixe.ods.sica.posicion.commands.DivisasCommand extends com.ixe.ods.sica.posicion.commands.BaseSequenceCommand
{	
	/**
	 * Constructor de la clase; asigna el valor del del siguiente evento.
	 */
    public function DivisasCommand()
    {
        super();
        nextEvent = Posicion.POSICION_MESA_COMMAND;
    }

	/**
	 * Obtiene la lista de las divisas para una mesa.
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       var idDivisa : Number = Number(event.data);
       trace("DivisasCommand.idDivisa->" + idDivisa);
       delegate.getDivisas(idDivisa);
    }

	/**
	 * Obtiene la posici&oacute;n para una mesa / divisa.
	 * 
	 * @param event El evento generado por el comando.
	 */
    public function onResult(event : Object) : Void
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_NAVIGATOR_VIEW);
       var dataProvider : Array = event.result;
       view['divisaComboBox'].dataProvider = dataProvider;
       if (dataProvider.length > 0) {
          view['showDivisaBox']();
          executeNextCommand();
       }
       else {
          view['hideDivisaBox']();
          selectTopAndBottomViews(Posicion.TOP_NOT_AVAILABLE_INDEX, Posicion.BOTTOM_EMPTY_VIEW_INDEX);
       }
    }

	/**
	 * Toma el mensaje en caso de error.
	 * 
	 * @param event El evento generado por el comando.
	 */ 
	public function onFault(event : Object) : Void
	{
       trace("onFault->" + event.fault.faultstring);
    }
}

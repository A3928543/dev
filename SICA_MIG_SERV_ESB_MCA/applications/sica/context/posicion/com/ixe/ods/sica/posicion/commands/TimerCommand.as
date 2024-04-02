/*
 * $Id: TimerCommand.as,v 1.9 2008/02/22 18:25:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.vo.ParametrosPizarronVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.Event;

import mx.core.UIObject;

/**
 * Este comando implementa una secuencia de comandos:
 * <ul>
 * <li>Obtiene los datos del pizarr&oacute;n.</li>
 * <li>Obtiene el DataProvider para actualizar los valores del pizarr&oacute;n. </li>
 * </ul>
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.commands.TimerCommand extends com.ixe.ods.sica.posicion.commands.BaseSequenceCommand
{
	/**
	 * Constructor de la clase; asigna el siguiente evento que se refleja en la vista de la posici&oacute;n de la mesa.
	 */
    public function TimerCommand()
    {
        super();
        nextEvent = Posicion.POSICION_MESA_COMMAND;
    }

	/**
	 * Obtiene los datos del pizarro&oacute;n.
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       delegate.getParametrosPizarron();
    }

	/**
	 * Obtiene el DataProvider para actualizar los valores del pizarr&oacute;n
	 * 
	 * @param event El evento generado por el comando.
	 */
    public function onResult(event : Object) : Void
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.PARAMETROS_PIZARRON_VIEW);
	   var dataProvider : ParametrosPizarronVO = event.result;
       view['dataProvider'] = dataProvider;
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
    }
}

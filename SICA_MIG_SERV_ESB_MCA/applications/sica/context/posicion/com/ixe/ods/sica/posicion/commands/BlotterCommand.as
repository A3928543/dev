/*
 * $Id: BlotterCommand.as,v 1.9.30.2 2010/07/23 02:17:18 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.vo.BlotterVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.Event;

import mx.core.UIObject;

/**
 * Este comando implementa una secuencia de comandos:
 * <ul>
 * <li>Obtiene monto m&iacute;nimo para de deal para mostrar en el Blotter.</li>
 * <li>Asigna el DataProvider al Blotter. </li>
 * </ul>
 *
 * @author: David Solis, Jean C. Favila
 * @version $Revision: 1.9.30.2 $ $Date: 2010/07/23 02:17:18 $
 */
class com.ixe.ods.sica.posicion.commands.BlotterCommand extends com.ixe.ods.sica.posicion.commands.BaseCommand
{	
	
	/**
	 * Obtiene el valor del Monto M&iacute;nimo de Deal para mostrar en el Blotter. 
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       var montoMinimoDeal : Number = ViewLocator.getInstance().getView(Posicion.BLOTTER_VIEW)['minimumAmount'];
       trace("BlotterCommand.montoMinimoDeal->" + montoMinimoDeal);
       delegate.getBlotter(mx.core.Application.application.idMesaCambio, montoMinimoDeal);
    }
	
	/**
	 * Asigna el DataProvider para el Blotter.
	 * 
	 * @param El evento generado por el comando.
	 */
    public function onResult(event : Object) : Void
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.BLOTTER_VIEW);
       var dataProvider : Array = event.result;
       dataProvider.sortItemsBy(ViewLocator.getInstance().getView(Posicion.BLOTTER_VIEW)['dataField'], ViewLocator.getInstance().getView(Posicion.BLOTTER_VIEW)['sortDirection']);
       view['dataProvider'] = dataProvider;
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

/*
 * $Id: ArbolCommand.as,v 1.9 2008/02/22 18:25:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */
 
import com.ixe.ods.sica.posicion.Posicion;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.Event;

import mx.core.UIObject;

/**
 * Este comando implementa una secuencia de comandos:
 * <ul>
 * <li>Obtiene el servicio.</li>
 * <li>Asigna el DataProvider del &aacute;rbol. </li>
 * </ul>
 *
 * @author: David Solis, Jean C. Favila
 * @version $Revision: 1.9 $ $Date: 2008/02/22 18:25:36 $ 
 */
class com.ixe.ods.sica.posicion.commands.ArbolCommand extends com.ixe.ods.sica.posicion.commands.BaseCommand
{	
	/**
	 * Obtiene del servicio, los datos del arbol de los canales.
	 */ 
    function execute(event : Event) : Void
    {
       trace("ArbolCommand.execute");
       delegate.getArbol();
    }

	/**
	 * Envia la peticion al servicio; asigna el DataProvider para el arbol del Monitor de la posici&oacute;n.
	 * 
	 * @param event El evento generado por el comando.
	 */ 	
    public function onResult(event : Object) : Void
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_NAVIGATOR_VIEW);
       var mesas : Array = event.result;
       view['setTreeDataProvider'](mesas);
    }
	
	/**
	 * Toma el mensaje en caso de error.
	 * 
	 * @param event El evento generado por comando.
	 */
	public function onFault(event : Object) : Void
	{
       trace("onFault->" + event.fault.faultstring);
    }
}

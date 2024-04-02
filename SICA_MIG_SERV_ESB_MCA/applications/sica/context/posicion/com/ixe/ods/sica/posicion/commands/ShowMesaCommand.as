/*
 * $Id: ShowMesaCommand.as,v 1.9 2008/02/22 18:25:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import org.nevis.cairngorm.control.Event;

/**
 * Este comando implementa una secuencia de comandos:
 * <ul>
 * <li>Obtiene la vista de la posici&oacute;n de la mesa selecciondada.</li>
 * </ul>
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.commands.ShowMesaCommand extends com.ixe.ods.sica.posicion.commands.AbstractCommand
{
	/**
	 * Obtiene la vista de la posici&oacute;n mesa selecciondada.
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX);
    }
}

/*
 * $Id: ValorFuturoCommand.as,v 1.9 2008/02/22 18:25:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import org.nevis.cairngorm.control.Event;

/**
 * El command de cairngorm para obtener la bandera de valor futuro.
 *
 * @author: David Solis, Jean C. Favila
 * @version $Revision: 1.9 $ $Date: 2008/02/22 18:25:36 $
 */
class com.ixe.ods.sica.posicion.commands.ValorFuturoCommand extends com.ixe.ods.sica.posicion.commands.BaseCommand
{
	/**
	 * Obtiene del servicio si hay o no valor futuro.
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       delegate.getValorFuturo();
    }
	
	/**
	 * Comunica a la aplicaci&oacute;n si hay valor futuro.
	 * 
	 * @param event El evento generado por el comando.
	 */
    public function onResult(event : Object) : Void
    {
		mx.core.Application.application.vFut = event.result;
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

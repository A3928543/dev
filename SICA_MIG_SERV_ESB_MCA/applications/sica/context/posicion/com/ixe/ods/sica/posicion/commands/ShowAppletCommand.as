/*
 * $Id: ShowAppletCommand.as,v 1.9 2008/02/22 18:25:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.Event;

import mx.core.UIObject;

/**
 * Este comando implementa una secuencia de comandos:
 * <ul>
 * <li>Muestra el Applet del pizarr&oacute;n de tipos de cambio del SICA.</li>
 * </ul>
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.commands.ShowAppletCommand extends com.ixe.ods.sica.posicion.commands.AbstractCommand
{	
	/**
	 * Muestra el Applet del pizarr&oacute;n de tipos de cambio del SICA.
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_NAVIGATOR_VIEW);
       var idMesaCambio = view['idMesaCambio'];
       var idTipoPizarron : String = String(event.data);
	   var url : String = Posicion.APPLET_URL + idMesaCambio + "&sp=S" + idTipoPizarron;
       trace("url->" + url);
       getURL("javascript:openNewWindow('" + url + "&sp=S' + new Date().getTime(), 'PizarronExt', 'height=160,width=970,toolbar=no,scrollbars=no,status=yes')");
    }
}
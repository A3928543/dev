/*
 * $Id: AbstractCommand.as,v 1.9 2008/02/22 18:25:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.Event;

import mx.core.UIObject;

/**
 * Clase que comunica a la interfaz del Monitor de la Posici&oacute;n, que vista 
 * del ViewStack ha sido seleccionada.
 * 
 * @author: David Solis, Jean C. Favila
 * @version $Revision: 1.9 $ $Date: 2008/02/22 18:25:36 $ 
 */
class com.ixe.ods.sica.posicion.commands.AbstractCommand implements org.nevis.cairngorm.commands.Command
{
	
	/**
	 * Metodo propio de la interfaz.
	 * 
	 * @param event El evento generado por el comando. 
	 */
    function execute(event : Event) : Void
    {
        // To be implemented by subclasses
    }
	
	/**
	 * Define cual de las vistas del ViewStack ha sido seleccionada.
	 * 
	 * @param topIndex El &iacute;ndice del top del ViewStack.
	 * @param bottomIndex El &iacute;ndice del bottom del ViewStack.
	 */
    function selectTopAndBottomViews(topIndex : Number, bottomIndex : Number) : Void
    {
       var application : UIObject = ViewLocator.getInstance().getView(Posicion.APPLICATION_VIEW);
       if (topIndex != undefined)
          application['selectTopView'](topIndex);
       if (bottomIndex != undefined)
	      application['selectBottomView'](bottomIndex);
    }
}
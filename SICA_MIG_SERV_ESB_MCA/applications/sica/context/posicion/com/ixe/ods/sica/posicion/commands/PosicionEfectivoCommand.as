/*
 * $Id: PosicionEfectivoCommand.as,v 1.9 2008/02/22 18:25:37 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.Event;

import mx.core.UIObject;

/**
 * Este comando implementa una secuencia de comandos:
 * <ul>
 * <li>Obtiene la posici&oacute;n para efectivo.</li>
 * <li>Obtiene el DataProvider para la vista de la posici&oacute;n para efectivo. </li>
 * </ul>
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.commands.PosicionEfectivoCommand extends com.ixe.ods.sica.posicion.commands.BaseCommand
{	
	/**
	 * Obtiene la posici&oacute;n para efectivo,
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       trace("PosicionEfectivoCommand.execute");
       trace("PosicionEfectivoCommand idMesaCambio->" + idMesaCambio + " idDivisa->" + idDivisa);
       delegate.getPosicionEfectivo(idMesaCambio, idDivisa);
    }
	
	/**
	 * Obtiene el DataProvider para vista de la posici&oacute;n para efectivo.
	 * 
	 * @param event El evento generado por el comando.
	 */
    public function onResult(event : Object) : Void
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_EFECTIVO_VIEW);
       var dataProvider : PosicionVO = event.result;
       if (dataProvider != null) {
          dataProvider.initialise();
          view['dataProvider'] = dataProvider;
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.EFECTIVO_VIEW_INDEX);
       }
       else {
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.BOTTOM_NOT_AVAILABLE_INDEX);
       }
    }
}

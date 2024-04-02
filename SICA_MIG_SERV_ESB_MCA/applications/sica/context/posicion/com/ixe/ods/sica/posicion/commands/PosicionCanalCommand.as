/*
 * $Id: PosicionCanalCommand.as,v 1.9 2008/02/22 18:25:37 ccovian Exp $
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
 * <li>Obtiene la posici&oacute;n para una mesa / canal / divisa.</li>
 * <li>Obtiene el DataProvider para la vista de la posicion de la mesa / canal / divisa. </li>
 * </ul>
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.commands.PosicionCanalCommand extends com.ixe.ods.sica.posicion.commands.BaseCommand
{
	/**
	 * La divisa seleccionada.
	 */
    private var nombreCanal : String;

	/**
	 * Obtiene la posici&oacute;n para una mesa / canal / divisa.
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       trace("PosicionCanalCommand.execute");
       var idCanal : String = event.data.idCanal;
       nombreCanal = event.data.nombre;
       trace("PosicionCanalCommand idMesaCambio->" + idMesaCambio + " idDivisa->" + idDivisa + " idCanal->" + idCanal);
       delegate.getPosicionCanal(idMesaCambio, idDivisa, idCanal);
    }
	
	/**
	 * Obtiene el DataProvider para la vista de la posici&oacute;n de la mesa / canal / divisa.
	 * 
	 * @param event El evento generado por el comando.
	 */ 
    public function onResult(event : Object) : Void
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_CANAL_VIEW);
       var dataProvider : PosicionVO = event.result;
       if (dataProvider != null) {
          dataProvider.initialise();
          view['dataProvider'] = dataProvider;
          view['updateLabel4Canal'](nombreCanal);
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.CANAL_VIEW_INDEX);
       }
       else {
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.BOTTOM_NOT_AVAILABLE_INDEX);
       }
    }
}

/*
 * $Id: UtilidadGlobalCommand.as,v 1.9 2008/02/22 18:25:37 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.vo.UtilidadDivisaVO;
import com.ixe.ods.sica.posicion.vo.UtilidadGlobalVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.Event;

import mx.core.UIObject;

/**
 * Este comando implementa una secuencia de comandos:
 * <ul>
 * <li>Obtiene la utilidad global.</li>
 * <li>Obtiene el DataProvider para la vista de los montos de la utilidad global.
 * </ul>
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.commands.UtilidadGlobalCommand extends com.ixe.ods.sica.posicion.commands.BaseCommand
{
	/**
	 * Obtiene la utilidad global.
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       trace("UtilidadGlobalCommand.execute");
       delegate.getUtilidadGlobal();
    }
	
	/**
	 * Obtiene el DataProvider para la vista de los montos de la utilidad global.
	 * 	 * 
	 * @param event El evento generado por el comando.
	 */
    public function onResult(event : Object) : Void
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.UTILIDAD_GLOBAL_VIEW);
       var dataProvider : UtilidadGlobalVO = event.result;
       if (dataProvider != null) {
          view['updateDataProvider'](dataProvider);
          selectTopAndBottomViews(Posicion.UTILIDAD_GLOBAL_VIEW_INDEX, Posicion.BOTTOM_EMPTY_VIEW_INDEX);
       }
       else {
          selectTopAndBottomViews(Posicion.TOP_NOT_AVAILABLE_INDEX, Posicion.BOTTOM_EMPTY_VIEW_INDEX);
       }
    }
	
	/**
	 * Asigna el valor para la utilidad global.
	 * 
	 * @param dataProvider El VO que contiene los montos para la Utilidad Global.
	 */ 
    private function initialiseDataProvider(dataProvider : UtilidadGlobalVO) : Void
    {
       var divisas : Array = dataProvider.divisas;
       for (var i : Number = 0; i < divisas.length; i++) {
          var vo : UtilidadDivisaVO = UtilidadDivisaVO(dataProvider[i]);
          trace("vo->" + vo);
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
       selectTopAndBottomViews(Posicion.TOP_NOT_AVAILABLE_INDEX, Posicion.BOTTOM_EMPTY_VIEW_INDEX);
    }
}

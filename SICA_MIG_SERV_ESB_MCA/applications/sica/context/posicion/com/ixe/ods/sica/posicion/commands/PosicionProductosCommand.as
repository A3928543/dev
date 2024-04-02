/*
 * $Id: PosicionProductosCommand.as,v 1.9 2008/02/22 18:25:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.vo.PosicionGrupoVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.Event;

import mx.core.UIObject;

/**
 * Este comando implementa una secuencia de comandos:
 * <ul>
 * <li>Obtiene la posici&oacute;n para todos los productos.</li>
 * <li>Obtiene el DataProvider para la vista de la posici&oacute;n de todos los productos. </li>
 * </ul>
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.commands.PosicionProductosCommand extends com.ixe.ods.sica.posicion.commands.BaseCommand
{
	
	/**
	 * Obtiene la posici&oacute;n para todos los productos.
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       trace("PosicionProductosCommand.execute");
       trace("PosicionProductosCommand idMesaCambio->" + idMesaCambio + " idDivisa->" + idDivisa);
       delegate.getPosicionProductos(idMesaCambio, idDivisa);
    }
	
	/**
	 * Obtiene el DataProvider para la vista de los posici&oacute;n de los productos.
	 * 
	 * @param event El evento generado por el comando.
	 */ 
    public function onResult(event : Object) : Void
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_GRUPO_VIEW);
       var dataProvider : Array = event.result;
       if (dataProvider != null && dataProvider.length > 0) {
          initialiseDataProvider(dataProvider);
          view['dataProvider'] = dataProvider;
          view['isGrupoCanales'] = false;
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.PRODUCTOS_VIEW_INDEX);
       }
       else {
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.BOTTOM_NOT_AVAILABLE_INDEX);
       }
    }
	
	/**
	 * Asigna el valor para el DataProvider.
	 * 
	 * @param dataProvider El arreglos con los VO de Posicion.
	 */
    private function initialiseDataProvider(dataProvider : Array) : Void
    {
       for (var i : Number = 0; i < dataProvider.length; i++) {
          var vo : PosicionGrupoVO = PosicionGrupoVO(dataProvider[i]);
          vo.initialise();
       }
    }
}

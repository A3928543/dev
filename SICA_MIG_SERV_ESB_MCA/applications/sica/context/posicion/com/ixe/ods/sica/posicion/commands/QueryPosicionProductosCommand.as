/*
 * $Id: QueryPosicionProductosCommand.as,v 1.9 2008/02/22 18:25:36 ccovian Exp $
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
 * <li>Obtiene la posici&oacute;n para productos.</li>
 * <li>Obtiene el DataProvider para la vista de la posici&oacute;n para productos. </li>
 * </ul>
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.commands.QueryPosicionProductosCommand extends com.ixe.ods.sica.posicion.commands.BaseCommand
{	
	
	/**
	 * Obtiene la posici&oacute;n para productos.
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       trace("QueryPosicionProductosCommand.execute");
       trace("QueryPosicionProductosCommand idMesaCambio->" + idMesaCambio + " idDivisa->" + idDivisa);
       delegate.runQueryPosicionProductos(idMesaCambio, idDivisa, event.data.productos);
    }
	
	/**
	 * Obtiene el DataProvider para la vista de la posici&oacute;n para productos.
	 * 
	 * @param event El evento generado por el comando.
	 */
    public function onResult(event : Object) : Void
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_PRODUCTO_VIEW);
       var dataProvider : PosicionVO = event.result;
       if (dataProvider != null) {
          dataProvider.initialise();
          view['dataProvider'] = dataProvider;
          var navigator : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_NAVIGATOR_VIEW);
          view['updateLabel4Productos'](navigator['getIdProductos']());
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.PRODUCTO_VIEW_INDEX);
       }
       else {
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.BOTTOM_NOT_AVAILABLE_INDEX);
       }
    }
}

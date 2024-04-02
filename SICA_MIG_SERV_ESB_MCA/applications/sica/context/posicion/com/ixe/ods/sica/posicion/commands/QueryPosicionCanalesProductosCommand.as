/*
 * $Id: QueryPosicionCanalesProductosCommand.as,v 1.9 2008/02/22 18:25:37 ccovian Exp $
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
 * <li>Obtiene la posici&oacute;n de los canales / productos.</li>
 * <li>Obtiene el DataProvider para la vista de la posici&oacute;n para canales / productos. </li>
 * </ul>
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.commands.QueryPosicionCanalesProductosCommand extends com.ixe.ods.sica.posicion.commands.BaseCommand
{
	
	/**
	 * Obtiene la posici&oacute;n de los canales / productos.
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       trace("QueryPosicionCanalesProductosCommand.execute");
       trace("QueryPosicionCanalesProductosCommand idMesaCambio->" + idMesaCambio + " idDivisa->" + idDivisa);
       delegate.runQueryPosicionCanalesProductos(idMesaCambio, idDivisa, event.data.canales, event.data.productos);
    }
	
	/**
	 * Obtiene el DataProvider para la vista de la posici&oacute;n para canales / productos.  
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
          view['updateLabel4CanalesProductos'](navigator['getNombreCanales'](), navigator['getIdProductos']());
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.PRODUCTO_VIEW_INDEX);
       }
       else {
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.BOTTOM_NOT_AVAILABLE_INDEX);
       }
    }
}

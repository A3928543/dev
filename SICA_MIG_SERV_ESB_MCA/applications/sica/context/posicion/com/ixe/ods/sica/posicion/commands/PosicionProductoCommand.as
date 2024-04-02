/*
 * $Id: PosicionProductoCommand.as,v 1.9 2008/02/22 18:25:36 ccovian Exp $
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
class com.ixe.ods.sica.posicion.commands.PosicionProductoCommand extends com.ixe.ods.sica.posicion.commands.BaseCommand
{
	/**
	 * El id del producto.
	 */
    private var idProducto : String;

	/**
	 * Obtiene la Posici&oacute;n de un producto y por divisa.
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       trace("PosicionProductoCommand.execute");
       idProducto = String(event.data);
       trace("PosicionProductoCommand idMesaCambio->" + idMesaCambio + " idDivisa->" + idDivisa + " idProducto->" + idProducto);
       delegate.getPosicionProducto(idMesaCambio, idDivisa, idProducto);
    }
	
	/**
	 * Obtiene el DataProvider para la vista de la posici&oacute;n para el producto seleccionado,
	 */
    public function onResult(event : Object) : Void
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_PRODUCTO_VIEW);
       var dataProvider : PosicionVO = event.result;
       if (dataProvider != null) {
          dataProvider.initialise();
          view['dataProvider'] = dataProvider;
          view['updateLabel4Producto'](idProducto);
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.PRODUCTO_VIEW_INDEX);
       }
       else {
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.BOTTOM_NOT_AVAILABLE_INDEX);
       }
    }
}

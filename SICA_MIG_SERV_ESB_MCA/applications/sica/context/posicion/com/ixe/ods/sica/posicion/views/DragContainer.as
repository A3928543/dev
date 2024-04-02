/*
 * $Id: DragContainer.as,v 1.9 2008/02/22 18:25:27 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.legosoft.util.DelayedCall;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.EventBroadcaster;

import mx.managers.DragManager;
import mx.utils.Delegate;

/**
 * Clase que define el comportamiento de los eventos de Drag and Drop para
 * la vista canal seleccionado.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.DragContainer extends mx.containers.VBox
{
		
	/**
	 * Constructor de la clase. 
	 */
    function DragContainer()
    {
    }

	/**
	 * M&acute;todo que se ejecuta al iniciar el componoente; registra los eventos del componente. 
	 */
	function init() : Void
	{
	   super.init();
       addEventListener ("childrenCreated", Delegate.create(this, childrenCreated));
	}

	/**
	 * M&eacute;todo que registra los eventos para el componente; registra los eventos
	 * de Drag and Drop para la vista del DataGrid de canales.
	 */
	private function childrenCreated() : Void
	{
	   var dataGrid = this['canalDataGrid'];
	   setupDataGrid(dataGrid, canalDragOver);
       this['canalTrashLink'].addEventListener("click", Delegate.create(this, clearCanalDataGrid));
	   dataGrid = this['productoDataGrid'];
       setupDataGrid(dataGrid, productoDragOver);
       this['productoTrashLink'].addEventListener("click", Delegate.create(this, clearProductoDataGrid));
       this['runLink'].addEventListener("click", Delegate.create(this, runQuery));
	}

	/**
	 * Muestra los montos de inventario de efectivo para el producto del canal
	 * seleccionado.
	 */
	private function runQuery() : Void
	{
	   var canales = this['canalDataGrid'].dataProvider;
	   var productos = this['productoDataGrid'].dataProvider;

	   if (canales.length > 0 && productos.length == 1)
	   {
	      var item = this['productoDataGrid'].dataProvider[0];
	      if (item['id'] == Posicion.INVENTARIO_EFECTIVO_KEY)
             EventBroadcaster.getInstance().broadcastEvent(Posicion.QUERY_POSICION_INVENTARIO_EFECTIVO_COMMAND, {sucursales:getValues("canalDataGrid", "id")});
          else
             EventBroadcaster.getInstance().broadcastEvent(Posicion.QUERY_POSICION_CANALES_PRODUCTOS_COMMAND, {canales:getValues("canalDataGrid", "id"), productos:getIdProductos()});
	   }
	   else if (canales.length > 0 && productos.length > 0)
	   {
          EventBroadcaster.getInstance().broadcastEvent(Posicion.QUERY_POSICION_CANALES_PRODUCTOS_COMMAND, {canales:getValues("canalDataGrid", "id"), productos:getIdProductos()});
	   }
       else if (canales.length == 1)
  	   {
	      var item = this['canalDataGrid'].dataProvider[0];
	      EventBroadcaster.getInstance().broadcastEvent(Posicion.POSICION_CANAL_COMMAND, {idCanal: item.idCanal, nombre:item.nombre});
  	   }
	   else if (canales.length > 0)
	   {
          EventBroadcaster.getInstance().broadcastEvent(Posicion.QUERY_POSICION_CANALES_COMMAND, {canales:getValues("canalDataGrid", "id")});
	   }
	   else if (productos.length == 1)
	   {
	      var item = this['productoDataGrid'].dataProvider[0];
	      var idProducto : String = item['id'];
	      if (idProducto == Posicion.EFECTIVO_KEY)
             EventBroadcaster.getInstance().broadcastEvent(Posicion.POSICION_EFECTIVO_COMMAND);
	      else if (idProducto == Posicion.INVENTARIO_EFECTIVO_KEY)
             EventBroadcaster.getInstance().broadcastEvent(Posicion.POSICION_INVENTARIO_EFECTIVO_COMMAND);
          else
             EventBroadcaster.getInstance().broadcastEvent(Posicion.POSICION_PRODUCTO_COMMAND, idProducto);
	   }
	   else if (productos.length > 0)
	   {
          EventBroadcaster.getInstance().broadcastEvent(Posicion.QUERY_POSICION_PRODUCTOS_COMMAND, {productos:getIdProductos()});
	   }
	}

	/**
	 * Obtiene los valores del elemento seleccionado del DataGrid.
	 * 
	 * @param dataGridKey El id que define el DataProvider para el grid.
	 * @param key El id para el item del DataGrid.
	 * @return Array
	 */
	private function getValues(dataGridKey : String, key : String) : Array
	{
	   var array : Array = new Array;
	   var dataProvider : Array = this[dataGridKey].dataProvider;
	   var length = dataProvider.length;
	   for (var i : Number = 0; i < length; i++) {
	      var item = dataProvider[i];
	      var value = item[key];
	      array.push(value);
	   }
	   return array
	}

	/**
	 * Define si el monto para inventario de efectivo para un producto
	 * se encuentra calculado.
	 * 
	 * @return Boolean.
	 */
    private function isAlreadyInventarioEfectivo() : Boolean
    {
	   var dataProvider : Array = this['productoDataGrid'].dataProvider;
	   var length = dataProvider.length;
	   var found : Boolean = false;
	   for (var i : Number = 0; i < length && !found; i++) {
	      var item = dataProvider[i];
	      if (item.idProducto == Posicion.INVENTARIO_EFECTIVO_KEY)
	         found = true;
	   }
	   return found;
    }

	/**
	 * Define si el elemento seleccionado corresponde a una sucursal.
	 * 
	 * @return Boolean.
	 */
    private function isAllSucursales() : Boolean
    {
	   var dataProvider : Array = this['canalDataGrid'].dataProvider;
	   var length = dataProvider.length;
	   var found : Boolean = false;
	   for (var i : Number = 0; i < length && !found; i++) {
	      var item = dataProvider[i];
	      if (!item.isSucursal)
	         found = true;
	   }
	   return !found;
    }

	/**
	 * Obtiene el arreglo con los nombres de los canales.
	 * 
	 * @return Array.
	 */
	function getNombreCanales() : Array
	{
	   return getValues("canalDataGrid", "nombre");
	}

	/**
	 * Obtiene el arreglo con los id's de los productos.
	 * 
	 * @return Array.
	 */
	function getIdProductos() : Array
	{
	   return getValues("productoDataGrid", "id");
	}

	/**
	 * Registra los eventos de Drag and Drop para el DataGrid.
	 * 
	 * @param dataGrid El DataGrid de canales o productos.
	 * @param f
	 */
	private function setupDataGrid(dataGrid, f) : Void
	{
       dataGrid.addEventListener("dragEnter", Delegate.create(this, dragEnter));
       dataGrid.addEventListener("dragExit", Delegate.create(this, dragExit));
       dataGrid.addEventListener("dragDrop", Delegate.create(this, dragDrop));
       dataGrid.addEventListener("dragOver", Delegate.create(this, f));
	}

	/**
	 * Limpia el DataGrid de los canales.
	 */
	private function clearCanalDataGrid() : Void
	{
	   clearDataGrid(this['canalDataGrid']);
	}

	/**
	 * Limpia el DataGrid de los productos.
	 */
	private function clearProductoDataGrid() : Void
	{
	   clearDataGrid(this['productoDataGrid']);
	}

	/**
	 * Limpia el DataGrid que se recibe como par&aacute;metro.
	 * 
	 * @param dataGrid El DataGrid a eliminar su contenido. 
	 */
	private function clearDataGrid(dataGrid)
	{
	   var items : Array = dataGrid.selectedIndices;
       items.sort(sortByNumber);
       items.reverse();
       for (var i = 0; i < items.length; i++)
          dataGrid.removeItemAt(items[i])
       showHideRunLink();
	}

	/**
	 * Limpia los DataGrid de canales y productos.
	 */
	function clearDataGrids() : Void
	{
	   this['productoDataGrid'].dataProvider.removeAll();
	   this['canalDataGrid'].dataProvider.removeAll()
       showHideRunLink();
	}

	/**
	 * Toma el evento para Drag.
	 * 
	 * @param event El evento DAD generado por el componente.
	 */
    private function dragEnter(event) : Void
    {
        event.handled = "true";
    }

	/**
	 * Termina el evento para Drag.
	 * 
	 * @param event El evento DAD generado por el componente.
	 */
    private function dragExit(event) : Void
    {
        event.target.hideDropFeedback();
    }

	/**
	 * Toma el evento Drag para el elemento seleccionado del  DataGrid
	 * de canales al DataGrid de productos; define si el elemento seleccionado
	 * del DataGrid de canales, liga el producto para mostrarlo en el DataGrid
	 * de productos.
	 * 
	 * @param event El evento DAD generado por el componente.
	 */
    private function canalDragOver(event) : Void
    {
        var target = event.target;
        var items = event.dragSource.dataForFormat("treeItems");
        target.showDropFeedback();
        var item = items[0].getData();

        if (item.isCanal && item.isDraggable) {
           // Si no es sucursal y en el contenedor de productos se encuentra Inventario de efectivo
           if (!item.isSucursal && isAlreadyInventarioEfectivo())
              event.action = DragManager.NONE;
           else
              event.action = DragManager.LINK;
        }
        else
           event.action = DragManager.NONE;
    }

	/**
	 * Toma el evento Drag para el elemento seleccionado del DataGrid
	 * de productos; define si muestra el monto para Inventario de 
	 * Efectivo para el producto.
	 * 
	 * @param event El evento DAD generado por el componente.
	 */
    private function productoDragOver(event) : Void
    {
        var target = event.target;
        var items = event.dragSource.dataForFormat("treeItems");
        var item = items[0].getData();
        target.showDropFeedback();

        if (item.isProducto) {
           var isInventarioEfectivo : Boolean = item.idProducto == Posicion.INVENTARIO_EFECTIVO_KEY;
           // Si el producto es Inventario de efectivo y ya existen productos en el contenedor
           if (isInventarioEfectivo && this['productoDataGrid'].dataProvider.length > 0)
              event.action = DragManager.NONE;
           // Si el producto es Inventario de efectivo y se encuentran en el contenedor
           // de canales canales que no son sucursales
           else if (isInventarioEfectivo && !isAllSucursales())
              event.action = DragManager.NONE;
           // Si en el contenedor se encuentra el producto Inventario de efectivo
           else if (isAlreadyInventarioEfectivo())
              event.action = DragManager.NONE;
           else
              event.action = DragManager.LINK;
        }
        else
           event.action = DragManager.NONE;
    }

	/**
	 * Termina el evento Drag e indica el destino del Drop para
	 * el elemento seleccionado.
	 * 
	 * @param event El evento DAD generado por el componente.
	 */
    private function dragDrop(event) : Void
    {
        dragExit(event);
        if (event.action == DragManager.LINK) {
            var item = event.dragSource.dataForFormat("treeItems")[0].getData();
            var dataGrid = event.target;
            var destItems = dataGrid.dataProvider;
            var found = false;
            for (var j = 0; j < destItems.length && !found; j++)
               if (destItems[j].id == item.id) {
                  found = true;
                  break;
               }
            if (!found) {
               var dropLocation = dataGrid.getDropLocation();
               new DelayedCall(1000, this, addItem2DataGridAt, item, dataGrid, dropLocation);
            }
        }
    }

	/**
	 * Ordena los elementos numericamente.
	 * 
	 * @param a El valor incial.
	 * @param b El valor a comparar.
	 */
    private function sortByNumber(a, b)
    {
      return (a > b);
    }

	/**
	 * Agrega un nuevo elemento al DataGrid.
	 * 
	 * @param item El elemento para agregar al DataGrid.
	 * @param dataGrid El DataGrid de destino del item.
	 * @param location El item que queda seleccionado despues de agregar el nuevo elemento al DataGrid.
	 */
    private function addItem2DataGridAt(item, dataGrid, location)
    {
       dataGrid.addItemAt(location, item);
       dataGrid.selectItem(location, true);
       showHideRunLink();
    }

	/**
	 * Define si el icono de "Trash" debe estar visible o no;
	 * este icono define un evento de Drag and Drop el cual
	 * consiste en eliminar los elementos del DataGrid de
	 * Canales o Productos.
	 */
    private function showHideRunLink() : Void
    {
	   var canales = this['canalDataGrid'].dataProvider;
	   var productos = this['productoDataGrid'].dataProvider;
	   var canalesLength : Number = canales != undefined ? canales.length : 0;
	   var productosLength : Number = productos != undefined ? productos.length : 0;

	   this['canalTrashLink'].visible = canalesLength > 0;
	   this['productoTrashLink'].visible = productosLength > 0;
	   this['runLink'].visible = canalesLength > 0 || productosLength > 0;
    }
}

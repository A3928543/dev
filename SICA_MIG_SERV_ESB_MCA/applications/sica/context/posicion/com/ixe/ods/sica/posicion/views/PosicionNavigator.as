/*
 * $Id: PosicionNavigator.as,v 1.9.30.1 2010/06/16 16:01:00 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.model.*;
import com.ixe.ods.sica.posicion.vo.*;
import com.legosoft.view.ViewLocator;
import com.legosoft.util.DelayedCall;
import org.nevis.cairngorm.control.EventBroadcaster;

import mx.controls.Alert;
import mx.controls.Tree;
import mx.controls.treeclasses.TreeNode;
import mx.controls.treeclasses.TreeDataProvider;
import mx.core.UIObject;
import mx.utils.Delegate;

/**
 * Contenedor y controlador de tres vistas:
 * <ul>
 * <li> ComboBox de divisas. Las divisas son cargadas cuando se selecciona una mesa.</li>
 * <li> Tree para seleccionar las mesas, sus canales y sus productos.</li>
 * <li> Contenedor de DnD para hacer consultas por la combinaci&oacute;n de canales y productos.</li>
 * </ul>
 * Maneja los siguientes casos en la selecci&oacute;n del &aacute;rbol:
 * <ul>
 * <li> Selecci&oacute;n del node ra&iacute;z. Deber&aacute; presentar la utilidad de todoas las mesas.</li>
 * <li> Selecci&oacute;n de una mesa. Vista de la mesa.</li>
 * <li> Selecci&oacute;n del nodo CANALES.</li>
 * <li> Selecci&oacute;n del nodo SUCURSALES.</li>
 * <li> Selecci&oacute;n del nodo PRODUCTOS.</li>
 * <li> Selecci&oacute;n de un canal. Vista de los tipos de cambio del canal. Cada vez que se (des)selecciona un canal se genera un evento. </li>
 * <li> Selecci&oacute;n de un producto (excluyendo efectivo). Vista de los tipos de cambio del producto.</li>
 * <li> Selecci&oacute;n del producto efectivo. Es la misma vista que la del canal.</li>
 * </ul>
 *
 * @author: David Solis, Jean C. Favila
 * @version $Revision: 1.9.30.1 $ $Date: 2010/06/16 16:01:00 $
 */
[Event("changeChannel")]
class com.ixe.ods.sica.posicion.views.PosicionNavigator extends mx.containers.Canvas
{	
	/**
	 * El id del canal.
	 */
    var idCanal : String = null;
    
    /**
     * La mesa de cambios. 
     */
    var mesa : Mesa;
	
	/**
	 * El timeout para la actualizaci&oacute;n. 
	 */
    var timeout : Number = 0;
    
	/**
	 * El intervalo para la actualizaci&oacute;n.
	 */
    var timeoutInterval = 0;
    
	/**
	 * La etiqueta para el estatus de la actualizaci&oacute;n.
	 */
    var timeoutLabel : String;
    
	/**
	 * El estatus de la actualizaci&oacute;n
	 */
    var status : String;


	/**
	 * Constructor para la clase.
	 */
    function PosicionNavigator()
    {
    }

	/**
	 * M&eacute;todo que se ejecuta al iniciar el componente; registra los eventos para el componente.
	 */
	function init() : Void
	{
	   super.init();
       addEventListener ("childrenCreated", Delegate.create(this, childrenCreated));
	}


	/**
	 * M&acute;todo que registra los eventos del componente; registra los eventos para las vistas
	 * del arbol, el comboBox de las divisas y los timeouts.
	 */
	private function childrenCreated() : Void
	{
       this['tree'].addEventListener("change", Delegate.create(this, changeTreeSelection));
       this['divisaComboBox'].addEventListener("change", Delegate.create(this, changeDivisaSelection));
       ViewLocator.getInstance().registerView(Posicion.POSICION_NAVIGATOR_VIEW, this);
	   updateTimeoutLabel();
	   this['timeoutLink'].addEventListener("click", Delegate.create(this, showHideTimeout));
	   this['okButton'].addEventListener("click", Delegate.create(this, ok));
	   this['cancelButton'].addEventListener("click", Delegate.create(this, cancel));
       this['synchLink'].addEventListener("click", Delegate.create(this, sync));
       EventBroadcaster.getInstance().broadcastEvent(Posicion.ARBOL_COMMAND)
	}

	/**
	 * Envia un mensaje de BroadCast para notificar a la interfaz el cambio del comboBox de divisas y
	 * mostrar los montos de la posici&oacute;n correspondientes a la divisa seleccionada.
	 */
	private function changeDivisaSelection() : Void
	{
	   trace("PosicionNavigator.changeDivisaSelection");
	   hideBottomView();
       EventBroadcaster.getInstance().broadcastEvent(Posicion.POSICION_MESA_COMMAND, {idMesa : idMesaCambio, idDivisa:this['divisaComboBox'].selectedItem.idDivisa});
	}

	/**
	 * Oculta el bottom del ViewStack (la vista de la Posici&oacute;n del Canal).
	 */
	private function hideBottomView() : Void
	{
       var application : UIObject = ViewLocator.getInstance().getView(Posicion.APPLICATION_VIEW);
       application['hideBottomView']();
	}

	/**
	 * Oculta ambas vistas del ViewStack.
	 */
	private function hideTopAndBottomViews() : Void
	{
       var application : UIObject = ViewLocator.getInstance().getView(Posicion.APPLICATION_VIEW);
       application['hideTopView']();
       application['hideBottomView']();
	}
	
	/**
	 * Asigna el DataProvider para el arbol de Mesas y Canales; solo el nodo
	 * raiz se expande al iniciar la interfaz.
	 * 
	 * @param mesas El arreglo de las mesas de operaci&oacute;n.
	 */
	function setTreeDataProvider(mesas : Array) : Void
	{
	   var tree : Tree = this['tree'];
       tree.dataProvider = __buildTreeDataProvider(mesas);
       // descomentar la siguiente linea para expandir el arbol:
       // expandTree(tree);
       // Se expande solo el nodo raiz:
       tree.setIsOpen(tree.getTreeNodeAt(0), true);
    }

	/**
	 * Expande todos los nodos del arbol
	 * 
	 * @para tree El componente tree con todos los nodos.
	 */
	private function expandTree(tree : Tree) : Void {
       var i : Number = 0;
       var node : TreeNode = tree.getTreeNodeAt(i);
       while (node != undefined) {
          if (tree.getIsBranch(node) && ! tree.getIsOpen(node)){
             tree.setIsOpen(node,true);
          }
          i++;
          node = tree.getNodeDisplayedAt(i);
       }
	}

	/**
	 * Construye los nodos del &aacute;rbol. 
	 * 
	 * @param mesas El arreglo con todas las mesas de operaci&oacute;n.
	 * @return TreeDataProvider.
	 */
	private function __buildTreeDataProvider(mesas : Array) : TreeDataProvider
	{
       var treeNode = new TreeNode();
       var rootNode = treeNode.addTreeNode(Posicion.POSICION, {isCanal:false, isSucursal:false, isProducto:false, isMesa: false, isGrupoCanales: false, isGrupoSucursales: false, isGrupoProductos: false, isDraggable: false});
       for (var i : Number = 0; i < mesas.length; i++) {
          var mesa : MesaVO = MesaVO(mesas[i]);
          var mesaNode = rootNode.addTreeNode(mesa.nombre, new Mesa(mesa.idMesaCambio, mesa.nombre, mesa.idDivisa));
          var canales : Array = mesa.canales;
          var sucursales : Array = mesa.sucursales;
          if (canales.length > 0 || sucursales.length > 0) {
             var canalesNode = mesaNode.addTreeNode(Posicion.CANALES, {isCanal:false, isSucursal:false, isProducto:false, isMesa: false, isGrupoCanales: true, isGrupoSucursales: false, isGrupoProductos: false, isDraggable: false});
             for (var j : Number = 0; j < canales.length; j++) {
                var canal : CanalVO = CanalVO(canales[j]);
                canalesNode.addTreeNode(canal.nombre, new Canal(canal.idCanal, canal.nombre, canal.idTipoPizarron));
             }
             if (sucursales.length > 0) {
                var sucursalesNode = canalesNode.addTreeNode(Posicion.SUCURSALES, {isCanal:false, isSucursal:false, isProducto:false, isMesa: false, isGrupoCanales: false, isGrupoSucursales: true, isGrupoProductos: false, isDraggable: false});
                for (var k : Number = 0; k < sucursales.length; k++) {
                   var sucursal : CanalVO = CanalVO(sucursales[k]);
                   sucursalesNode.addTreeNode(sucursal.nombre, new Sucursal(sucursal.idCanal, sucursal.nombre));
                }
             }
          }
          var productos : Array = mesa.productos;
          if (productos.length > 0) {
             var productosNode = mesaNode.addTreeNode(Posicion.PRODUCTOS, {isCanal:false, isSucursal:false, isProducto:false, isMesa: false, isGrupoCanales: false, isGrupoSucursales: false, isGrupoProductos: true, isDraggable: false});
             for (var p : Number = 0; p < productos.length; p++) {
                var producto : ProductoVO = ProductoVO(productos[p]);
                productosNode.addTreeNode(producto.idProducto, new Producto(producto.idProducto));
             }
          }
       }
       return treeNode;
	}


	/**
	 * Define cuando un nodo del arbol ha sido seleccionado.
	 */
	private function changeTreeSelection() : Void
	{
	   var tree = this['tree'];
	   var selectedNode = tree.selectedNode;
	   var data = selectedNode.getData();
       dispatchEvent({type: "changeChannel", idTipoPizarron: data.isCanal ? data.idTipoPizarron : null});
	   if (selectedNode == null || selectedNode == undefined) {
	      hideTopAndBottomViews();
	      selectMesa(null);
	      return;
	   }
	   if (selectedNode == tree.getTreeNodeAt(0)) {
	      selectMesa(null);
          EventBroadcaster.getInstance().broadcastEvent(Posicion.UTILIDAD_GLOBAL_COMMAND);
	      return;
	   }
       selectMesa(getMesa(selectedNode));
	   if (data.isGrupoCanales) {
	      invokeDelayedCommamnd(Posicion.POSICION_CANALES_COMMAND);
	      return;
	   }
	   if (data.isCanal) {
	      idCanal = data.idCanal;
	      invokeDelayedCommamnd(Posicion.POSICION_CANAL_COMMAND, {idCanal:data.idCanal, nombre:data.nombre});
	      return;
	   }
	   if (data.isGrupoSucursales) {
	      invokeDelayedCommamnd(Posicion.POSICION_SUCURSALES_COMMAND);
	      return;
	   }
	   if (data.isGrupoProductos) {
	      invokeDelayedCommamnd(Posicion.POSICION_PRODUCTOS_COMMAND);
	      return;
	   }
	   if (data.isProducto) {
	      if (data.idProducto == Posicion.EFECTIVO_KEY)
	         invokeDelayedCommamnd(Posicion.POSICION_EFECTIVO_COMMAND);
	      else if (data.idProducto == Posicion.INVENTARIO_EFECTIVO_KEY)
	         invokeDelayedCommamnd(Posicion.POSICION_INVENTARIO_EFECTIVO_COMMAND);
	      else
	         invokeDelayedCommamnd(Posicion.POSICION_PRODUCTO_COMMAND, data.idProducto);
	   }
	}

	/**
	 * Envia un mensaje BroadCast para notificar la mesa que ha sido seleccionada del arbol 
	 * y mostrar los montos correspondientes a la posici&oacute;n para esta mesa.
	 * 
	 * @newMesa La mesa seleccionada del arbol.
	 */
    private function selectMesa(newMesa : Mesa) : Void
    {
        if (newMesa == null) {
            hideDivisaBox();
            parentApplication.idMesaCambio = -1;
	        // Es necesario refrescar el blotter:
	        EventBroadcaster.getInstance().broadcastEvent(Posicion.BLOTTER_COMMAND);
            return;
        }
        parentApplication.idDivisaReferencia = newMesa.idDivisa;
        parentApplication.idMesaCambio = newMesa.idMesaCambio;
        if (mesa.idMesaCambio != newMesa.idMesaCambio) {
            mesa = newMesa;
            ViewLocator.getInstance().getView(Posicion.APPLICATION_VIEW)['dragContainerBox'].clearDataGrids();
            hideBottomView();
            // Es necesario cambiar los datos de la mesa:
            EventBroadcaster.getInstance().broadcastEvent(Posicion.DIVISAS_COMMAND, mesa.idMesaCambio);
        }
        else if (this['tree'].selectedNode.getData().isMesa) {
            EventBroadcaster.getInstance().broadcastEvent(Posicion.SHOW_MESA_COMMAND);
            showDivisaBox();
        }
        // Es necesario refrescar el blotter para la mesa seleccionada:
        EventBroadcaster.getInstance().broadcastEvent(Posicion.BLOTTER_COMMAND);
    }

	/**
	 * Muestra el ComboBox de divisas.
	 */
	function showDivisaBox() : Void
	{
       this['divisaBox'].visible = true;
	}

	/**
	 * Oculta el ComboBox de divisas.
	 */
	function hideDivisaBox() : Void
	{
       this['divisaBox'].visible = false;
	}

	/**
	 * Obtiene la mesa seleccionada del arbol.
	 * 
	 * @param selectedNode El nodo seleccionado del arbol que representa una mesa de operaci&oacute;n.
	 * @return Mesa.
	 */
	private function getMesa(selectedNode) : Mesa
	{
	   var parent;
	   if (selectedNode.getData().isMesa)
	      parent = selectedNode;
	   else
          while (true) {
             parent = selectedNode.getParent();
             if (parent.getData().isMesa)
                break;
             selectedNode = parent;
          }
	   return Mesa(parent.getData());
	}

	/**
	 * Obtiene el id del a mesa de cambio,
	 * 
	 * @return Number.
	 */
	function get idMesaCambio() : Number
	{
	   return mesa.idMesaCambio;
	}

	/**
	 * Obtiene el id de la divisa.
	 * 
	 * @return String.
	 */
	function get idDivisa() : String
	{
	   return this['divisaComboBox'].selectedItem.idDivisa;
	}

	/**
	 * Obtiene el evento generado por el ComboBox de divisas para enviar un mensaje de BroadCast
	 * que notifica el evento a los componentes de la interfaz. 
	 * 
	 * @param commandName El nombre del commando ejecutado por el Combo.
	 * @param data La informaci&oacute;n del evento.
	 */
	private function invokeDelayedCommamnd(commandName : String, data)
	{
	   var eventBroadcaster : EventBroadcaster = EventBroadcaster.getInstance();
	   if (idDivisa == undefined)
          new DelayedCall(2000, eventBroadcaster, eventBroadcaster.broadcastEvent, commandName, data);
       else
          eventBroadcaster.broadcastEvent(commandName, data);
	}

	/**
	 * Obtiene un arreglo con los nombres de los canales del arbol.
	 * 
	 * @return Array,
	 */
	function getNombreCanales() : Array
	{
	   return ViewLocator.getInstance().getView(Posicion.APPLICATION_VIEW)['dragContainerBox'].getNombreCanales();
	}

	/**
	 * Obtiene un arreglo con los Id's de los productos.
	 * 
	 * @return Array.
	 */
	function getIdProductos() : Array
	{
	   return ViewLocator.getInstance().getView(Posicion.APPLICATION_VIEW)['dragContainerBox'].getIdProductos();
	}


	/**
	 * Actualiza los montos para la posici&oacute;n.
	 */
    private function sync() : Void
    {
       EventBroadcaster.getInstance().broadcastEvent(Posicion.TIMER_COMMAND);
       updateStatus();
    }

	/**
	 * Define si se muestra o no la etiqueta de actualizaci&oacute;n.
	 */
    private function showHideTimeout() : Void
    {
       var timeoutBox = this['timeoutBox'];
       var visible : Boolean = timeoutBox.visible;

       if (!visible) {
          this['timeoutStepper'].value = timeout;
          timeoutBox.visible = true;
       }
       else {
          timeoutBox.visible = false;
       }
    }

	/**
	 * Oculta el Box de actualizaci&oacute;n y refresca la etiqueta.
	 */
    private function ok() : Void
    {
       this['timeoutBox'].visible = false;
       var newTimeout : Number = this['timeoutStepper'].value;
       if (timeout != newTimeout) {
          timeout = newTimeout;
          enableTimer();
    	  updateTimeoutLabel();
       }
    }

	/**
	 * Actualiza la etiqueta de actualizac&oacute;n.
	 */
    function updateTimeoutLabel() : Void
    {
       if (timeout == 0)
          timeoutLabel = "Configurar la actualizaci\u00F3n";
       else
          timeoutLabel = "Tiempo para actualizar: " + timeout + " segundos";
    }

	/**
	 * Cancela y oculta el Box para la configuraci&oacute;n de actualizaciones.
	 */
    private function cancel() : Void
    {
       this['timeoutBox'].visible = false;
    }

	/**
	 * Habilita el timer para la actualizaci&oacute;n.
	 */
    private function enableTimer() : Void
    {
       if (timeoutInterval != 0)
          clearInterval(timeoutInterval);
       if (timeout > 0)
          timeoutInterval = setInterval(Delegate.create(this, handleTimer), timeout * 1000);
    }

	/**
	 * Sincroniza la informaci&oacute;n de la intefaz con los datos actuales.
	 */
    private function handleTimer() : Void
    {
       sync();
    }

	/**
	 * Actualiza el estatus de la &uacute;ltima actualizaci&oacute;n.
	 */
    private function updateStatus() : Void
    {
       var now : Date = new Date();
       var minutes : Number = now.getMinutes();
       var seconds : Number = now.getSeconds();
       status = "Ultima lectura: " + this['dateFormatter'].format(now) + ":" + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }
}
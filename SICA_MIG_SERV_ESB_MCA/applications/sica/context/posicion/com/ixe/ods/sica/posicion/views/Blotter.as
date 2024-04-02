/*
 * $Id: Blotter.as,v 1.9.30.2 2010/07/23 02:18:00 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.legosoft.util.DoubleClickDispatcher;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.EventBroadcaster;

import mx.utils.Delegate;

/**
 * Define el comportamiento de la vista del Blotter. El Blotter muestra
 * un DataGrid de Deals capturados durante el d&iacute;a, con base en 
 * el monto m&iacute;nimo que se establece.
 * 
 * @author: David Solis, Jean C. Favila
 * @version $Revision: 1.9.30.2 $ $Date: 2010/07/23 02:18:00 $
 */
class com.ixe.ods.sica.posicion.views.Blotter extends mx.containers.VBox
{
	/**
	 * El monto m&iacute;nimo de deals para mostrar en el Blotter.
	 */
    var minimumAmount : Number = 500000;
    
    /**
	 * Nombre del campo sobre el cual se esta haciendo click para ordenar.
	 */
	var dataField:String = "numeroDeal";
	
	/**
	 * Direccion del ordenamiento "asc" o "desc".
	 */
	var sortDirection:String = "asc";
    
    /**
	 * El DataProvider del Blotter. 
	 */
    var dataProvider : Array;
	
	/**
	 * Constructor de la clase.
	 */
    function Blotter()
    {
    }

	/**
	 * M&acute;todo que se ejecuta al iniciar el componoente; registra los eventos del componente. 
	 */
	function init() : Void
	{
	   super.init();
       addEventListener("childrenCreated", Delegate.create(this, childrenCreated));
	}
	
	/**
	 * M&eacute;todo que registra los eventos para el componente.; registra los eventos
	 * para el Blotter.
	 */
	private function childrenCreated() : Void
	{
	   var dataGrid = this['dataGrid'];
       DoubleClickDispatcher.addObject(dataGrid);
       this['minimumAmountField'].addEventListener("change", Delegate.create(this, __changeMinimumAmount));
       this['minimumAmountField'].addEventListener("enter", Delegate.create(this, changeMinimumAmount));
       this['synchLink'].addEventListener("click", Delegate.create(this, sync));
       ViewLocator.getInstance().registerView(Posicion.BLOTTER_VIEW, this);
	}

	/**
	 * Envia por broadcast el evento generado por el Blotter.
	 */
    private function sync() : Void
    {
       EventBroadcaster.getInstance().broadcastEvent(Posicion.BLOTTER_COMMAND);
    }

	/**
	 * Asigna el valor ingresado a la variable del monto m&iacute;nimo para deal.
	 * 
	 * @param event El evento generado por el componente; cambio en el monto m&iacute;nimo del Blotter.
	 */
	private function __changeMinimumAmount(event) : Void
	{
	   minimumAmount = Number(event.target.text);
	}


	/**
	 * Envia el evento de cambio en el monto m&iacute;nimo por BroadCast.
	 * 
	 * @param event El evento generado por el componente; cambio en el monto m&iacute;nimo del Blotter.
	 */
	private function changeMinimumAmount(event) : Void
	{
	   __changeMinimumAmount(event);
	   sync();
	}

	/**
	 * Abre en una nueva pantlla la vista del deal seleccionado en el Blotter.
	 */
	private function openDeal() : Void
	{
	   var selectedItem = this['dataGrid'].selectedItem;

	   if (selectedItem != null) {
	      var url : String = Posicion.MESA_DETALLE_URL + selectedItem.idDealPosicion ;
          com.legosoft.util.Tracer.trace("url->" + url);
          getURL("javascript:openNewWindow('" + url + "', 'MuestraDeal', 'height=600,width=1000,toolbar=no,scrollbars=no')");
       }
	}
	
	/**
	 * Almacena el nombre y la direcci&oacute;n en la que se est&aacute; realizando
	 * el ordenamiento.
	 */
	private function compare(event : Object) : Void
	{
		dataField = event.target.columns[event.columnIndex].columnName;
		sortDirection = event.target.sortDirection;
	}

}
/*
 * $Id: PosicionMesa.as,v 1.9 2008/02/22 18:25:27 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.ixe.ods.sica.posicion.vo.TipoCambioVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.EventBroadcaster;

import mx.utils.Delegate;

/**
 * Clase que define el comportamiento de la vista de la Posici&oacute;n para la Mesa.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.PosicionMesa extends mx.containers.VBox
{
	/**
	 * El DataProvider para la clase.
	 */
    var __dataProvider : PosicionVO;
   	
   	/**
	 * El componente del tipo de cambio. 
	 */
    var tipoCambio : TipoCambioVO;

	/**
	 * El titulo de la vista.
	 */
    var title : String;

    function PosicionMesa()
    {
    }

	/**
	 * M&eacute;todo que se ejecuta al iniciar el componente; registra los eventos del componente.
	 */
	function init() : Void
	{
	   super.init();
       addEventListener("childrenCreated", Delegate.create(this, childrenCreated));
	}

	/**
	 * M&eacute;todo que registra los eventos del componente; registra la vista correspondiente
	 * a la Posici&oacute;n de la Mesa.
	 */
	private function childrenCreated() : Void
	{
       this['synchLink'].addEventListener("click", Delegate.create(this, sync));
       // this['tipoOperacion'].addEventListener("change", Delegate.create(this, changeTipoOperacion));
       ViewLocator.getInstance().registerView(Posicion.POSICION_MESA_VIEW, this);
	}

	/**
	 * Ejecuta la actualizaci&oacute;n de los tipos de cambio.
	 * 
	 * @param event El evento DAD generado por el componente.
	 */
	private function changeTipoOperacion(event) : Void
	{
	   updateTipoCambio();
	}

	/**
	 * Actualiza los valores de los tipos de cambio.
	 */
	private function updateTipoCambio() : Void
	{
	   // Esta funcion permitia actualizar el tipo de cambio segun el indice del acordeon
       // EventBroadcaster.getInstance().broadcastEvent(Posicion.UPDATE_TIPO_CAMBIO_COMMAND, {viewName: Posicion.POSICION_MESA_VIEW, index:this['tipoOperacion'].selectedIndex});
       tipoCambio = __dataProvider.tipoCambioPizarron;
	}

	/**
	 * Envia un mensaje de BroadCast el evento generado por la vista de la Posici&oacute;n de la Mesa.
	 */
    private function sync() : Void
    {
       EventBroadcaster.getInstance().broadcastEvent(Posicion.POSICION_MESA_COMMAND);
    }

	/**
	 * Limpia el valor de utilidad.
	 */
    function reset() : Void
    {
       this['utilidad'].reset(); 
    }

	/**
	 * Actualiza los valores del DataProvider (valores de la posici&oacute;n para la mesa).
	 * 
	 * @param newDataProvider El nuevo DataProvider para actualizar los datos del componente.
	 */
    function updateDataProvider(newDataProvider : PosicionVO)
    {
       newDataProvider.initialise();
       if (dataProvider != undefined && newDataProvider.idMesaCambio == dataProvider.idMesaCambio && newDataProvider.idDivisa == dataProvider.idDivisa) {
          newDataProvider.computeTicks(dataProvider);
       }
       dataProvider = newDataProvider;
    }

	/**
	 * Obtiene el valor de dataProvider (PosicionVO).
	 * 
	 * @return PosicionVO
	 */
    [ChangeEvent("changeDataProvider")]
    function get dataProvider() : PosicionVO
    {
       return __dataProvider;
    }

	/**
	 * Asigna el valor para dataProvider.
	 * 
	 * @param value El valor del DataProvider (PosicionVO) del componente.
	 */
    function set dataProvider(value : PosicionVO) : Void
    {
       __dataProvider = value;
       updateTipoCambio();
       dispatchEvent({type:"changeDataProvider"});
    }
}

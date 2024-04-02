/*
 * $Id: PosicionCanal.as,v 1.9 2008/02/22 18:25:27 ccovian Exp $
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
 * Clase que define el comportamiento de la vista de la Posici&oacute;n para los Canales de Operaci&oacute;n. 
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.PosicionCanal extends mx.containers.VBox
{
	/**
	 * Constante que define el separadr para el texto.
	 */
    private static var SEPARATOR : String = ", ";

	/**
	 * El DataProvider de la posici&oacute;n.
	 */
    var __dataProvider : PosicionVO;
    
   	/**
	 * El DataProvider para el tipo de cambio,
	 */
    var tipoCambio : TipoCambioVO;

	/**
	 * Constructor de la clase.
	 */
    function Canal()
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
	 * M&eacute;todo que registra los eventos para el componente; define la
	 * vista de la posici&oacute;n a mostrar.
	 */
	private function childrenCreated() : Void
	{
       // this['tipoOperacion'].addEventListener("change", Delegate.create(this, changeTipoOperacion));
       ViewLocator.getInstance().registerView(Posicion.POSICION_CANAL_VIEW, this);
	}

	/**
	 * Llama a la actualizacion del tipo de cambio con base al evento
	 * que dispara el Accordion.
	 * 
	 * @param event El evento DAD generado por el componente.
	 */
	private function changeTipoOperacion(event) : Void
	{
	   updateTipoCambio();
	}

	/**
	 * Actualiza el tipo de cambio segun el indice del Accordion seleccionado.
	 */
	private function updateTipoCambio() : Void
	{
	   // EventBroadcaster.getInstance().broadcastEvent(Posicion.UPDATE_TIPO_CAMBIO_COMMAND, {viewName: Posicion.POSICION_CANAL_VIEW, index:this['tipoOperacion'].selectedIndex});
       tipoCambio = __dataProvider.tipoCambioPizarron;
	}

	/**
	 * Actualiza la etiqueta del canal seleccionado.
	 * 
	 * @param canal El nombre del canal.
	 */
	function updateLabel4Canal(canal : String) : Void
	{
       this['canalLabel'].text = "Canal:"
	   this['canales'].text = canal;
	}

	/**
	 * Actualiza el las etiquetas para los canales.
	 * 
	 * @param canales El arreglo con los nombres de los canales.
	 */
	function updateLabel4Canales(canales : Array) : Void
	{
	   if (canales.length == 1)
	      updateLabel4Canal(canales[0]);
	   else {
          this['canalLabel'].text = "Canales:"
	      this['canales'].text = canales.join(SEPARATOR);
	   }
	}

	/**
	 * Obtiene el valor para el DataProvider de Posici&oacute;n.
	 * 
	 * @return PosicionVO.
	 */
    [ChangeEvent("changeDataProvider")]
    function get dataProvider() : PosicionVO
    {
       return __dataProvider;
    }

	/**
	 * Asigna el valor para el DataProvider de Posici&oacute;n.
	 * 
	 * @param value El DataProvider (PosicionVO) para el componente.
	 */
    function set dataProvider(value : PosicionVO) : Void
    {
       __dataProvider = value;
       updateTipoCambio();
       dispatchEvent({type:"changeDataProvider"});
    }
}

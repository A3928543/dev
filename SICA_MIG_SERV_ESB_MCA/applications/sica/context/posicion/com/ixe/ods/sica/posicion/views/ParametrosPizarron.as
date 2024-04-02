/*
 * $Id: ParametrosPizarron.as,v 1.9 2008/02/22 18:25:27 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.vo.ParametrosPizarronVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.EventBroadcaster;

import mx.utils.Delegate;

/**
 * Clase que obtiene desde la vista en Flex, la p&aacute;gina del Pizarr&oacute;n de 
 * tipos de cambio para productos y divisas.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.ParametrosPizarron extends mx.containers.VBox
{
	
	/**
	 * El DataProvider para la vista.
	 */
    var dataProvider : ParametrosPizarronVO;
    
   	/**
	 * El id del canal.
	 */
    var idCanal : String;

   	/**
	 * El id del Tipo de Pizarron.
	 */
    var idTipoPizarron : Number;

	/**
	 * Constructor para la clase.
	 */
    function ParametrosPizarron()
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
	 * M&eacute;todo que registra los eventos para el componente; registra los
	 * eventos para obtener la p&aacute;gina del pizarr&oacute;n de tipos de cambio.
	 */
	private function childrenCreated() : Void
	{
	   var pizarronLink = this['pizarronLink'];
       pizarronLink.addEventListener("click", Delegate.create(this, openApplet));
       ViewLocator.getInstance().registerView(Posicion.PARAMETROS_PIZARRON_VIEW, this);
       EventBroadcaster.getInstance().broadcastEvent(Posicion.PARAMETROS_PIZARRON_COMMAND);
	}

	/**
	 * Inicializa el valor del Formatter para los montos del pizarr&oacute;n.
	 * 
	 * @param number El valor para dar formato.
	 * @return String.
	 */
	function format(number) : String
	{
	   var formattedNumber : String = this['exchangeFormatter'].format(number);
	   if (formattedNumber.charAt(0) == '.')
	      formattedNumber = "0" + formattedNumber;
	   return formattedNumber;       
	}

	/**
	 * Define si los parametros del pizarr&oacute;n se encuentran o no visibles con
	 * base en la selecci&oacute;n del canal.
	 * 
	 * @param event El evento DAD generado por el componente.
	 */
	private function changeChannel(event) : Void
	{
	    idTipoPizarron = event.idTipoPizarron;
	   this['pizarronLink'].visible = idTipoPizarron != null ? true : false;
	}

	/**
	 * Abre la pantalla del pizarr&oacute;n.
	 */
	private function openApplet()
	{
       EventBroadcaster.getInstance().broadcastEvent(Posicion.SHOW_APPLET_COMMAND, idTipoPizarron);
	}

}
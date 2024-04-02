/*
 * $Id: TablaConf.as,v 1.1.26.1 2010/06/16 17:44:17 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

import mx.core.Application;
import mx.controls.Alert;
import mx.managers.*;

/**
 * Subclase de canvas que controla a TablaNormal.mxml implementa los m&eacute;todos para realizar
 * las autorizaciones y permitir al usuario consultar un deal.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1.26.1 $ $Date: 2010/06/16 17:44:17 $
 */
class views.TablaConf extends mx.containers.Canvas
{
    /**
     * El arreglo de workitems de un tipo de autorizaci&oacute;n en particular.
     */
	var workitems : Array;

	/**
	 *  El remote object para realizar la comunicaci&oacute;n con los servicios en Java.
	 */
	var autorizacionesService : Object;

	/**
	 * Bandera para indicar que no es una autorizaci&oacute;n, sino solo una confirmaci&oacute;n.
	 */
	var soloConfirmar : Boolean;

	/**
	 * Bandera para presentar o no el bot&oacute;n de negar la autorizaci&oacute;n.
	 */
	var noRechazar : Boolean;

	/**
	 * El path del icono a desplegar que representa el tipo de autorizaci&oacute;n de esta tabla de
	 * workitems.
	 */
	var icono : String;

	/**
 	 * El nombre de la actividad a la que se refiere.
	 */
	var nombreActividad : String;

    /**
     * Llama a <code>confirmarDealsInterbancarios()</code> del remote object autorizacionesService para
	 * autorizar o no la actividad seleccionada por el usuario. Posteriormente carga de nuevo los
	 * workitems actuales llamando a <code>findActividades()</code> del parentApplication.
	 *
	 * @param autorizar True para autorizar, False para negar la autorizacion.
	 */
	function confirmarDeals() : Void
	{
        Application.application.enabled = false;
		
		var itemsConfirmed : Array = new Array();
		var item : Object;
		var allItems : Array = this['tabla'].dataProvider;
		
		for(var i : Number = 0; i <= allItems.length; i++ ){
			item = allItems[i];
			if(item.confirmado == true){
				itemsConfirmed.push(item.idDealPosicion);
			}
		}
        
        autorizacionesService.confirmarDealsInterbancarios(parentApplication.ticket,
                itemsConfirmed, this['contactoTextField'].text,
                parentApplication.idUsuario);
        parentApplication.findActividades("Confirmaciones");
        
    }

    /**
     * Muestra en una ventana por separado la informaci&oacute;n completa del deal seleccionado.
     */
	function verDeal() : Void {
		var idDealStr : String = this['tabla'].selectedItem.idDeal + '';
		getURL('javascript:verDeal(' + idDealStr.slice(0, idDealStr.length - 2) + ', ' +
			true + ')')
	}
}
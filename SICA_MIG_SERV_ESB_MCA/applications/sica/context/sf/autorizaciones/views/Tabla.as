/*
 * $Id: Tabla.as,v 1.11.30.3 2010/07/01 00:24:34 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

import mx.core.Application;
import mx.managers.*;

/**
 * Subclase de canvas que controla a TablaNormal.mxml implementa los m&eacute;todos para realizar
 * las autorizaciones y permitir al usuario consultar un deal.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.11.30.3 $ $Date: 2010/07/01 00:24:34 $
 */
class views.Tabla extends mx.containers.Canvas
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
     * Llama a <code>completarActividad()</code> del remote object autorizacionesService para
     * autorizar o no la actividad seleccionada por el usuario. Posteriormente carga de nuevo los
     * workitems actuales llamando a <code>findActividades()</code> del parentApplication.
     *
     * @param autorizar True para autorizar, False para negar la autorizacion.
     */
	function completarWorkitem(autorizar : Boolean) : Void
	{
	    var act : Object = this['tabla'].selectedItem;
        Application.application.enabled = false;
        autorizacionesService.completarActividad(parentApplication.ticket, act.idActividad,
                act.actividad, autorizar, parentApplication.idUsuario);
		parentApplication.findActividades(nombreActividad);
	}
	
	/**
     * Llama a <code>completarActividad()</code> del remote object autorizacionesService para
     * autorizar o no la actividad seleccionada por el usuario. Posteriormente carga de nuevo los
     * workitems actuales llamando a <code>findActividades()</code> del parentApplication.
     *
     * @param autorizar True para autorizar, False para negar la autorizacion.
     */
	function completarWorkitemParaModifProd(autorizar : Boolean, tcmNvo : Number) : Void
	{
		var act : Object = this['tabla'].selectedItem;
        Application.application.enabled = false;
        autorizacionesService.completarActividadParaModifProd(parentApplication.ticket,
        		act.idActividad, act.actividad, autorizar, parentApplication.idUsuario, tcmNvo);
        parentApplication.findActividades(nombreActividad);
	}

    /**
     * Muestra en una ventana por separado la informaci&oacute;n completa del deal seleccionado.
     */
	function verDeal() : Void {
		getURL('javascript:verDeal(' + this['tabla'].selectedItem.idDeal + ', ' +
		    this['tabla'].selectedItem.interbancario + ')')
	}
	
   /**
	* Abre el popup para editar el tipo de cambio de la mesa.
	*/
	private function editarTcm() : Void 
	{
		var popup = PopUpManager.createPopUp(_root, EditarTcmModificacionProducto, true,
			{deferred: true, tablaAut:this, detalle: this['tabla'].selectedItem});
		popup.centerPopUp(this);
	}
	
   /**
	* Actualiza el tipo de cambio de la mesa.
	*/
	private function modifValTcmNvo(tcmNvo : Number) : Void
	{
		var act : Object = this['tabla'].selectedItem;
		this['tabla'].selectedItem.tcmNvo = tcmNvo;
		this['tabla'].selectedItem = act;
	}
}
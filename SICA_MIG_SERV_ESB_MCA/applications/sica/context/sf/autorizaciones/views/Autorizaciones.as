/*
 * $Id: Autorizaciones.as,v 1.17.2.2.8.1 2010/10/08 01:00:19 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

import mx.core.Application;
import mx.controls.*;

/**
 * Clase controladora de la aplicaci&oacute;n de autorizaciones del SICA.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.17.2.2.8.1 $ $Date: 2010/10/08 01:00:19 $
 */
class views.Autorizaciones extends mx.core.Application
{
    /**
     * El ticket de la sesi&oacute;n del usuario.
     */
    public var ticket : String;

    /**
     * El n&uacute;mero de ceja que debe seleccionar el sistema.
     */
    public var seleccionarCeja : Boolean;

    /**
     * El identificador del usuario que utiliza la aplicaci&oacute;n y que dar&aacute; las
     * autorizaciones.
     */
    public var idUsuario : Number;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por horario.
     */
    private var porHorario : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por monto.
     */
    private var porMonto : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por desviaci&oacute;n al tipo de cambio
     * al cliente (sucursales).
     */
    private var porDesviacion : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por sobreprecio.
     */
    private var porSobreprecio : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por cancelaci&oacute;n (Mesa de Cambios).
     */
    private var porCancelacion : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por cancelaci&oacute;n de detalle de deal
     * (Mesa de Cambios).
     */
    private var porCancelacionDet : Array;
    
    /**
     * La lista de solicitudes de autorizaci&oacute;n por modificaci&oacute;n del monto del detalle 
     * del deal.
     * (Mesa de Cambios).
     */
    private var porModificacionMontoDet : Array;
    
    /**
     * La lista de solicitudes de autorizaci&oacute;n por modificaci&oacute;n del producto del 
     * detalle de deal.
     * (Mesa de Cambios).
     */
    private var porModificacionProductoDet : Array;

    /**
     * La lista de reversos.
     */
    private var porReverso : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por falta de documentaci&oacute;n.
     */
    private var porDocumentacion : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por plantilla pendiente de activaci&oacute;n.
     */
    private var porPlantilla : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por cambio en R.F.C.
     */
    private var porRfc : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por cambio en Email.
     */
    private var porEmail : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por rechazo de la mesa de control con respecto
     * a falta de documentaci&oacute;n.
     */
    private var porRechazoDoc : Array;

	/**
	 * La lista de solicitudes de alta de correo electr&oacute;nico, independiente a la captura de deal.
	 */
	private var porAltaEmail : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por pago anticipado.
     */
    private var porPagoAnticipado : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por falta de documentaci&oacute;n en deals
     * interbancarios.
     */
    private var porDocumentacionInt : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por l&iacute;nea de operaci&oacute;n.
     */
    private var porLineaOpInt : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por pago anticipado en deals interbancarios.
     */
    private var porPagoAnticipadoInt : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n de deals interbancarios con pago anticipado
     * por parte de mesa de control.
     */
    private var porPagoAnticipadoIntMCtrl : Array;

    /**
     * La lista de solicitudes de autorizaci&oacute;n por rechazo en documentaci&oacute;n, para
     * deals interbancarios.
     */
    private var porRechazoDocInt : Array;

    /**
     * La lista de deals interbancarios que no han sido confirmados.
     */
    private var porConfirmarInt : Array;
    
    /**
     * La lista de detalles de deals que sobrepasaron el limite normal de operaci&oacute;n, pero
     * se encuentran dentro del limite Ixe.
     */
    private var porLimOper : Array;

    /**
     * La lista de reversos pendientes de autorizar.
     */
    private var reversos : Array;

    /**
	 * Contador de los elementos seleccionados para confirmar. 
	 */
	var selectedCount: Number = 0;

    /**
     * Llama al servicio getWorkitems del Delegado, para cargar las solicitudes de
     * autorizaci&oacute;n pendientes.
     *
     * @param nombreActividad El tipo de actividad a buscar, puede ser null, en cuyo caso, extrae
     *  todas las autorizaciones pendientes.
     */
    private function findActividades(nombreActividad : String) : Void
    {
        this['autorizacionesService'].getWorkitems(ticket, nombreActividad);
    }

    /**
     * Recibido cuando el usuario hace click en alguna de las secciones del accordion. Llama a
     * <code>findActividades()</code> con el nombre de la actividad que corresponde a la
     * secci&oacute;n seleccionada.
     *
     * @param event El evento del click sobre el accordion.
     */
    private function click(event) : Void
    {
		var seleccionado = this['acordion'].selectedChild;

        findActividades(seleccionado.nombreActividad);
    }

    /**
     * Implementaci&oacute;n vac&iacute;a en respuesta a la modificaci&oacute;n al tipo de cambio
     * de la mesa, en las solicitudes de autorizaci&oacute;n por sobreprecio.
     */
    function respModificarTcm() : Void
    {

    }

    /**
     * En caso de alg&uacute;n error, lo muestra al usuario utilizando un Alert dialog.
     *
     * @param causa La causa del error.
     */
    function falla(causa : Object) : Void
    {
        Application.application.enabled = true;
        Alert.show(causa.description, 'Atenci\u00f3n:');
    }

    /**
     * Regresa el total ligado al registro de la lista, que corresponde al nombre de actividad
     * especificado.
     *
     * @param nombreActividad El nombre de la actividad a buscar en la lista.
     * @param lista La lista de actividades.
     * @return Number.
     */
    function getTotalPara(nombreActividad : String, lista : Array) : Number
    {
        for (var i = 0; i < lista.length; i++) {
            if (lista[i].nombreActividad == nombreActividad) {
                return lista[i].total;
            }
        }
        return 0;
    }
}

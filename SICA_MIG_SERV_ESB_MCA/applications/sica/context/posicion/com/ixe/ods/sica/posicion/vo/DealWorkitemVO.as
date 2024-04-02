/*
 * $Id: DealWorkitemVO.as,v 1.12 2009/08/03 21:45:21 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

/**
 * Value Object que contiene la informaci&oacute;n de Deals capturados que se han
 * capturado y se muestran en la interfaz de Autorizaciones.
 *
 * @author David Solis, Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2009/08/03 21:45:21 $
 */
class com.ixe.ods.sica.posicion.vo.DealWorkitemVO
{
	/**
	 * Clase para el VO de DealWorkitem.
	 */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.DealWorkitemVO", DealWorkitemVO);

	/**
	 * El id de la actividad para el Deal.
	 */
    var idActividad : Number;

	/**
	 * La descripci&oacute;n de la actividad.
	 */
    var actividad : String;

    /**
	 * El id del Deal.
	 */
    var idDeal : Number;

	/**
	 * La fecha valor del Deal.
	 */
    var fechaValor : String;

	/**
	 * Define si el Deal es o no interbancario.
	 */
    var interbancario : Boolean;

    /**
	 * El nombre del cliente.
	 */
    var nomCliente : String;

    /**
	 * El nombre del promotor.
	 */
    var nomPromotor : String;

    /**
     * El nombre del usuario.
     */
    var nomUsuario : String;

    /**
	 * El tipo de operaci&oacute;n.
	 */
    var operacion : String;

    /**
	 * La fecha de captrura, formateada.
	 */
    var fecha : String;

    /**
	 * La solicitud de sobreprecio.
	 */
    var solAutSobreprecio : String;

    /**
     * Alg&uacute;n dato adicional para el registro.
     */
    var datoAdicional : String;
    
    /**
	 * El arreglo de detalles de Deal.
	 */
    var detalles : Array;
}

/*
 * $Id: HorarioMontoWorkitemVO.as,v 1.10.30.1 2010/06/30 23:06:53 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

/**
 * Value Object que contiene la informaci&oacute;n de Deals capturados que se han
 * capturado y se muestran en la interfaz de Autorizaciones.
 *
 * @author David Solis, Jean C. Favila
 * @version  $Revision: 1.10.30.1 $ $Date: 2010/06/30 23:06:53 $
 */
class com.ixe.ods.sica.posicion.vo.HorarioMontoWorkitemVO
{
	/**
	 * Clase para el VO de DealWorkitem.
	 */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.HorarioMontoWorkitemVO",
            HorarioMontoWorkitemVO);

    /**
     * La actividad.
     */
    var actividad : String;

    /**
     * El id del deal.
     */
    var idDeal : Number;

    /**
     * El folio del detalle del deal.
     */
    var folioDetalle : Number;

    /**
     * La fecha valor del deal.
     */
    var fechaValor : String;

    /**
     * El nombre del cliente.
     */
    var nomCliente : String;

    /**
     * El nombre del promotor.
     */
    var nomPromotor : String;

    /**
     * El nombre del usuario que captur&oacute; el deal.
     */
    var nomUsuario : String;

    /**
     * La operacion del deal.
     */
    var operacion : String;

    /**
     * La fecha actual.
     */
    var fecha : String;

    /**
     * La clave de la divisa.
     */
    var idDivisa : String;

    /**
     * El monto de la operaci&oacute;n.
     */
    var monto : Number;

    /**
     * El tipo de cambio dado al cliente.
     */
    var tcc : Number;
    
    /**
     * Indica si el deal contiene detalles capturados por reglas de neteo.
     */
    var contieneNeteo : Boolean;
}
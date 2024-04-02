/*
 * $Id: ModMontoWorkitemVO.as,v 1.2.2.2 2010/06/30 23:06:53 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

/**
 * Value Object utilizado en las autorizaciones en Flex que representa un workitem 
 * de autorizaci&oacute;n para la modificaci&oacute;n del monto de un detalle de deal.
 *
 * @author Abraham L&oacute;pez A.
 * @version  $Revision: 1.2.2.2 $ $Date: 2010/06/30 23:06:53 $
 */
class com.ixe.ods.sica.posicion.vo.ModMontoWorkitemVO
{
	/**
	 * Clase para el VO de ModMontoWorkitemVO.
	 */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.ModMontoWorkitemVO",
            ModMontoWorkitemVO);

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
    * El tipo de cambio de la mesa.
    */
    var tcm : Number;

    /**
     * El tipo de cambio dado al cliente.
     */
    var tcc : Number;
	
	/**
     * Indica si fue modificaci&oacute;n de monto o de producto del detalle de un deal.
     */
    var resultado : Number;
    
    /**
     * Indica si el deal contiene detalles capturados por reglas de neteo.
     */
    var contieneNeteo : Boolean;
}
/*
 * $Id: ModProductoWorkitemVO.as,v 1.1.2.2 2010/06/30 23:06:53 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

/**
 * Value Object utilizado en las autorizaciones en Flex que representa un workitem 
 * de autorizaci&oacute;n para la modificaci&oacute;n de producto de un detalle de deal.
 *
 * @author Abraham L&oacute;pez A.
 * @version  $Revision: 1.1.2.2 $ $Date: 2010/06/30 23:06:53 $
 */
class com.ixe.ods.sica.posicion.vo.ModProductoWorkitemVO
{
	/**
	 * Clase para el VO de ModProductoWorkitemVO.
	 */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.ModProductoWorkitemVO",
            ModProductoWorkitemVO);

	/**
	* El id de la actividad
	*/
	var idActividad : Number;
	
    /**
     * La actividad.
     */
    var actividad : String;

    /**
     * El id del deal.
     */
    var idDeal : Number;
    
    /**
    * El id del idDealPosicion
    */
    var idDealPosicion : Number;

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
     * El producto seleccionado originalmente.
     */
    var producto : String;

    /**
     * El tipo de cambio dado al cliente originalmente.
     */
    var tcc : Number;
	
	/**
     * El producto seleccionado para la modificaci&oacute;n.
     */
    var productoNvo : String;
    
    /**
     * El tipo de cambio dado al cliente seleccionado para la modificaci&oacute;n.
     */
    var tccNvo : Number;
    
    /**
     * El tipo de cambio de la mesa original.
     */
    var tcm : Number;
    
    /**
     * El tipo de cambio de la mesa seleccionado para la modificaci&oacute;n.
     */
    var tcmNvo : Number;
    
    /**
     * Indica si el deal contiene detalles capturados por reglas de neteo.
     */
    var contieneNeteo : Boolean;
}
/*
 * $Id: NotifLimWorkitemVO.as,v 1.1.2.1 2010/10/08 00:58:46 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

/**
 * Value Object utilizado en las autorizaciones en Flex que representa un workitem 
 * de notificaci&oacute;n para los detalles de deal que excedieron el l&iacute;mite normal,
 * pero se encuentran dentro del l&iacute;mite Ixe.
 *
 * @author Abraham L&oacute;pez A.
 * @version  $Revision: 1.1.2.1 $ $Date: 2010/10/08 00:58:46 $
 */
class com.ixe.ods.sica.posicion.vo.NotifLimWorkitemVO
{
	/**
	 * Clase para el VO de NotifLimWorkitemVO.
	 */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.NotifLimWorkitemVO",
            NotifLimWorkitemVO);

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
     * El tipo de cambio de la mesa original.
     */
    var tcm : Number;
    
    /**
     * El l&iacute;mite diario normal.
     */
    var limiteDiario : Number;
    
    /**
     * El monto diario acumulado en ese producto, de esa divisa.
     */
    var acumDiario : Number;
    
    /**
     * El l&iacute;mite diario Ixe.
     */
    var limiteDiarioIxe : Number;
    
    /**
     * El l&iacute;mite mensual normal.
     */
    var limiteMensual : Number;
    
    /**
     * El monto mensual acumulado en ese producto, de esa divisa.
     */
    var acumMensual : Number;
    
    /**
     * El l&iacute;mite mensual Ixe.
     */
    var limiteMensualIxe : Number;
}
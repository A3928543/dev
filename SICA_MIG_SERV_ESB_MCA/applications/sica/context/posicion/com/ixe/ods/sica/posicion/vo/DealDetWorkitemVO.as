/*
 * $Id: DealDetWorkitemVO.as,v 1.9.36.1 2010/10/08 00:58:46 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

/**
 * Value Object que contiene la informaci&oacute;n de los detalles de Deal 
 * que se han capturado y que se muestran en la interfaz de Autorizaciones.
 * 
 * @author David Solis, Jean C. Favila
 * @version  $Revision: 1.9.36.1 $ $Date: 2010/10/08 00:58:46 $
 */
class com.ixe.ods.sica.posicion.vo.DealDetWorkitemVO
{
	/**
	 * Clase para el VO de DealDetWorkitem.
	 */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.DealDetWorkitemVO", DealDetWorkitemVO);

	/**
	 * El idDealPosicion del Deal.
	 */
    var idDealPosicion : Number;
    
    /**
	 * El tipo de operaci&oacue;n
	 */
    var tipoOp : String;
    
	/**
	 * La clave de la forma de liquidaci&oacute;n del Deal.
	 */
    var claveFormaLiquidacion : String;
    
	/**
	 * El id de la divisa.
	 */
    var idDivisa : Strings;
    
	/**
	 * El monto del Deal.
	 */
    var monto : Number;
    
	/**
	 * El tipo de cambio para el cliente.
	 */
    var tcc : Number;
    
    /**
	 * El tipo de cambio para la mesa. 
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

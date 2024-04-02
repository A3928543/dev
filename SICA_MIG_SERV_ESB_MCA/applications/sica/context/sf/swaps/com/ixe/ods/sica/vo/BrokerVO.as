/*
 * $Id: BrokerVO.as,v 1.9 2008/02/22 18:25:44 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

/**
 * Value Object que contiene la informaci&oacute;n del Broker de la captura de Swaps.
 * 
 * Author: Jean C. Favila
 * Version $Revision: 1.9 $ $Date: 2008/02/22 18:25:44 $
 */
class com.ixe.ods.sica.vo.BrokerVO
{
	
	/**
	 * La clase para el VO de Broker.
	 */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.BrokerVO", BrokerVO);

	/**
	 * El id persona del Broker.
	 */
    var idPersona : Number;
    
    /**
	 * El nombre completo del Broker. 
	 */
    var nombreCompleto : String;
    
    /**
	 * La clave Reuters para el Swap.
	 */
    var claveReuters : String;
    
	/**
	 * El id del promotor.
	 */
    var idPromotor : Number;
    
	/**
	 * El n&uacute;mero de cuenta del Broker.
	 */
    var noCuenta : String;
    
    /**
     * El id contraparte del Broker. 
     */
    var idSector : String;

}
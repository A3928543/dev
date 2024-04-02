/*
 * $Id: BitacoraEnviadasVO.as,v 1.9 2008/02/22 18:25:44 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.vo.*;

/**
 * Value Object que contiene la informacion de Bitacora Enviadas para la captura de Swaps.
 * 
 * Author: Jean C. Favila
 * Version $Revision: 1.9 $ $Date: 2008/02/22 18:25:44 $
 */
class com.ixe.ods.sica.vo.BitacoraEnviadasVO
{
	
	/**
	 * La clase para el VO. 
	 */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.BitacoraEnviadasVO", BitacoraEnviadasVO);

	/**
	 * El id de la divisa.
	 */
    var idDivisa : String;
    
    /**
	 * El Folio del TAS de la Bitacora.
	 */
    var folioTas : Number;
    
    /**
	 * Define si la operaci&oacute;estan es Compra o Venta.
	 */
    var compra : Boolean;
    
    /**
	 * El monto del Swap.
	 */
    var monto : Number;
    
    /**
	 * La contraparte del Swap. 
	 */
    var contraparte : BrokerVO;
}
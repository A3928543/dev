/*
 * $Id: FactorDivisaVO.as,v 1.9 2008/02/22 18:25:45 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

/**
 * Value Object que contiene la informaci&oacute;n para los objetos de tipo FactorDivisa,
 * 
 * @author David Solis, Jean C. Favila
 * @version  $Revision: 1.9 $ $Date: 2008/02/22 18:25:45 $
 */
class com.ixe.ods.sica.posicion.vo.FactorDivisaVO
{
	
	/**
	 * Clase para el VO de FactorDivisa.
	 */
    static var registered = Object.registerClass("com.ixe.ods.sica.posicion.vo.FactorDivisaVO", FactorDivisaVO);

	/**
	 * El id 'de la Divisa'.
	 */
    var idFromDivisa : String;
    
    /**
	 * El id 'a la Divisa'. 
	 */
    var idToDivisa : String;
    
	/**
	 * El valor del factor de la divisa.
	 */
    var factor : Number;
    
	/**
	 * El valor del sobreprecio de Ixe.
	 */
    var sobreprecioIxe : Number;
    
    /**
	 * El valor del spread. 
	 */
    var spread : Number;
    
    /**
	 * El valor de carry. 
	 */
    var carry : Number;
    
	/**
	 * La fecha de la ultima modificaci&oacute;n.
	 */
    var ultimaModificacion : Date;
}
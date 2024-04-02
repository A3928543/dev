/*
 * $Id: DivisaVO.as,v 1.10 2008/02/22 18:25:45 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

/**
 * Value Object que contiene la informaci&oacute;n para los objetos de tipo Divisa.
 * 
 * @author David Solis, Jean C. Favila
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:45 $
 */
class com.ixe.ods.sica.posicion.vo.DivisaVO
{
	
    /**
     * Clase para el VO de Divisa.
     */
    static var registered = Object.registerClass("com.ixe.ods.sica.posicion.vo.DivisaVO", DivisaVO);

    /**
     * El id de la divisa.
     */
    var idDivisa : String;

    /**
     * La descripci&oacute;n de la divisa.
     */
    var descripcion : String;

    /**
     * El &iacute;cono de la divisa.
     */
    var icono : String;

    /**
     * F)recuente, N)o frecuente, M)etal amonedado.
     */    
    var tipo : String;
}
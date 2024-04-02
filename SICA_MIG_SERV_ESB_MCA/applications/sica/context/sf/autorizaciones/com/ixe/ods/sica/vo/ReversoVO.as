/*
 * $Id: ReversoVO.as,v 1.3 2008/04/22 16:45:40 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

/**
 * Value Object para transmitir la informaci&oacute;n de un reverso entre Flex y Java.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.3 $ $Date: 2008/04/22 16:45:40 $
 */
class com.ixe.ods.sica.vo.ReversoVO
{
    /**
     * La clase para el VO de Reverso.
     */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.ReversoVO", ReversoVO);

    /**
     * El n&uacute;mero de reverso.
     */
    var idReverso : Number;

    /**
     * El n&uacute;mero del deal que se va a reversar.
     */
    var idDealOriginal : Number;

    /**
     * La fecha en que se solicit&oacute; el reverso por el promotor.
     */
    var fecha : String;

    /**
     * La fecha en que se captur&oacute; el deal.
     */
    var fechaOperacion : String;

    /**
     * La fecha valor del deal original.
     */
    var fechaValor : String;

    /**
     * El nombre del usuario que solicit&oacute; el reverso.
     */
    var nombreUsuarioSolicitante : String;

    /**
     * Las observaciones del reverso.
     */
    var observaciones : String;

    /**
     * Si se trata o no de un reverso por cancelaci&oacute;n.
     */
    var porCancelacion : Boolean;

    /**
     * El n&uacute;mero de cuenta que debi&oacute; capturarse en el deal original.
     */
    var porContratoSica : String;

    /**
     * La fecha valor a la que debi&oacute; capturarse el deal original.
     */
    var porFechaValor : String;

    /**
     * El status del Reverso.
     */
    var statusReverso : String;

    /**
     * Una cadena que almacena la informaci&oacute;n de los tipos de cambios de la mesa que puede
     * editar el operador de la mesa.
     */
    var datosAdicionales : String;

    /**
     * La lista de detalles de reverso.
     */
    var detalles : Array;
}
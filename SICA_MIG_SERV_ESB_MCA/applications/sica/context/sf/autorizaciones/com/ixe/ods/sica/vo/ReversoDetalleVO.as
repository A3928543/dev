/*
 * $Id: ReversoDetalleVO.as,v 1.2 2008/02/22 18:25:17 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

/**
 * Value Object para transmitir la informaci&oacute;n de un detalle de reverso entre Flex y Java.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2008/02/22 18:25:17 $
 */
class com.ixe.ods.sica.vo.ReversoDetalleVO
{
    /**
     * La clase para el VO de Reverso.
     */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.ReversoDetalleVO",
            ReversoDetalleVO);
    /**
     * El identificador del registro.
     */
    var idReversoDetalle : Number;

    /**
     * El monto que debi&oacute; capturar el usuario inicialmente (opcional).
     */
    var montoNuevo : Number;

    /**
     * El tipo de cambio al que debi&oacute; pactarse el detalle original (opcional).
     */
    var tipoCambioNuevo : Number;
}
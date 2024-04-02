/*
 * $Id: DetalleReversoVO.as,v 1.2 2008/02/22 18:25:49 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 LegoSoft S.C.
 */

/**
 * @author Jean C. Favila
 * @version  $Revision: 1.2 $ $Date: 2008/02/22 18:25:49 $
 */
class com.ixe.ods.sica.reversos.vo.DetalleReversoVO
{
    /**
     * Clase para el VO de DetalleReverso.
     */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.DetalleReversoVO",
            DetalleReversoVO);

    var folioDetalle : Number;
    var montoOriginal : Number;
    var monto : Number;
    var tipoCambioOriginal : Number;
    var tipoCambio : Number;
    var montoCambiado : String;
    var tipoCambioCambiado : String;

    public function inicializar(folioDetalle : Number, montoOriginal : Number,
                                 montoNuevo : Number, tccOriginal : Number, tccNuevo : Number)
    {
        this.folioDetalle = folioDetalle;
        this.montoOriginal = montoOriginal;
        this.monto = montoNuevo;
        this.tipoCambioOriginal = tccOriginal;
        this.tipoCambio = tccNuevo;
        this.montoCambiado = montoOriginal != monto ? 'S\u00ed' : '';
        this.tipoCambioCambiado = tipoCambioOriginal != tipoCambio ? 'S\u00ed' : '';
    }
}
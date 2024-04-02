/*
 * $Id: BitacoraEnviadasHelper.java,v 1.2 2010/04/13 20:12:03 ccovian Exp $
 * Ixe Grupo Financiero.
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import com.ixe.ods.sica.Constantes;

import java.util.Date;

/**
 * Clase auxiliar de BitacoraEnviadas.
 * 
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.2 $ $Date: 2010/04/13 20:12:03 $
 */
public class BitacoraEnviadasHelper {

    /**
     * Genera un objeto BitacoraEnviadas a partir de los par&aacute;metros proporcionados.
     *
     * @param deal 	    	El Registro a reportar a Banxico.
     * @param folioDetalle 	El Folio del Detalle (Operaci&oacute;n) del Deal a Reportar.
     * @param tipoOper  	Si es Compra o Venta.
     * @param montoUSD  	El monto a reportar a Banxico.
     * @param swap    		Si la Operacion a Reportar a Banxico es un Swap (1) o no lo es (0).
     * @param divisa		La divisa en que se efectu&oacute; la operaci&oacute;n.
     * @param folio			El folio interno de la Operaci&oacute;n a Reportar.
     * @param status		Si el Registro est&aacute; listo para Reportarse a
     * 						Banxico (Status = 'ENVIAR') o no (Status = '').
     * @return BitacoraEnviadas
     */
    public static BitacoraEnviadas crearBitacoraEnviadas(Deal deal, int folioDetalle,
                                                         Integer tipoOper, Double montoUSD,
                                                         String swap, String divisa, Integer folio,
                                                         String status) {
    	BitacoraEnviadas be = new BitacoraEnviadas();
    	BitacoraEnviadasPK bePK =  new BitacoraEnviadasPK();
        bePK.setIdConf(Integer.toString(deal.getIdDeal()) + "-" + Integer.toString(folioDetalle));
        bePK.setDivisa(divisa);
        be.setId(bePK);
        be.setFolio(folio);
        be.setAccion(BitacoraEnviadas.C_REPORTE);
        be.setPlazo(new Integer(getPlazoBanxicoParaDeal(deal)));
        be.setTipoOper(tipoOper);
        be.setMonto(montoUSD);
        be.setTipo(BitacoraEnviadas.TIPO_CONTADO);
        be.setFechaConcertacion(deal.getFechaCaptura());
        be.setFechaLiquidacion(deal.getFechaLiquidacion());
        be.setSwap(swap);
        be.setCreateDt(new Date());
        be.setStatus(status);
        return be;
    }

    /**
     * Regresa el valor entero que representa para Banxico las diferentes Fechas Valor.
     *
     * @param deal El deal a revisar.
     * @return el Tipo Valor para Banxico.
     */
    private static int getPlazoBanxicoParaDeal(Deal deal) {
        int plazo = 3;
        if (Constantes.CASH.equals(deal.getTipoValor())) {
            plazo = 0;
        }
        else if (Constantes.TOM.equals(deal.getTipoValor())) {
            plazo = 1;
        }
        else if (Constantes.SPOT.equals(deal.getTipoValor())) {
            plazo = 2;
        }
        return plazo;
    }
}

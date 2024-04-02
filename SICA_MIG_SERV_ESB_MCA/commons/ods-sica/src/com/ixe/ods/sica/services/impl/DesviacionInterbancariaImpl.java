/*
 * $Id: DesviacionInterbancariaImpl.java,v 1.2.22.1 2011/04/26 02:28:25 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import java.util.Map;

import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.DesviacionInterbancaria;
import com.ixe.ods.sica.utils.MapUtils;

/**
 * Bean que permite calcular si un detalle de deal interbancario presenta una desviaci&oacute;n
 * mayor al 1% con respecto al Pizarr&oacute;n del SICA.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.2.22.1 $ $Date: 2011/04/26 02:28:25 $
 */
public class DesviacionInterbancariaImpl implements DesviacionInterbancaria {

    /**
     * Constructor vac&iacute;o.
     */
    public DesviacionInterbancariaImpl() {
        super();
    }

    /**
     * Levanta una excepci&oacute;n si el tipo de cambio se encuentra desviado m&aacute;s de un 5%
     * con respecto al precio de referencia de la divisa. Regresa true si el porcentaje de
     * desviaci&oacute;n es de 1%.
     *
     * @param recibimos True para compra, False para venta.
     * @param div La divisa de la operaci&oacute;n.
     * @param fechaValor CASH, TOM, SPOT, 72HR o VFUT.
     * @param claveFormaLiquidacion La clave del instrumento o producto.
     * @param idDivisaReferencia La clave de divisa de la mesa de cambios en la que se opera.
     * @param tipoCambio El tipo de cambio a validar. 
     * @return boolean.
     * @throws com.ixe.ods.sica.SicaException Si el porcentaje de desviaci&oacute;n del t.c. es
     * mayor al 5%.
     */
    public Map validarDesviacionTc(boolean recibimos, Divisa div, String fechaValor,
                                   String claveFormaLiquidacion, String idDivisaReferencia,
                                   double tipoCambio) throws SicaException {
        SicaServiceData sd = getSicaServiceData();
        PrecioReferenciaActual pr = sd.findPrecioReferenciaActual();
        /*debug("Referencia cv: " + pr.getPreRef().getPrecioCompra() + " "
                + pr.getPreRef().getPrecioVenta());*/
        FactorDivisaActual fda;
        double tcRef;
        if (div.isMetalAmonedado() || div.isNoFrecuente()) {
            fda = sd.findFactorDivisaActualFromTo(Divisa.PESO, div.getIdDivisa());
            double factorVenta = fda.getFacDiv().getFactor();
            double factor = recibimos ?
                    factorVenta - fda.getFacDiv().getSpreadCompra() : factorVenta;
            tcRef = Redondeador.redondear6Dec(factor);
            //debug("tcRef-> " + tcRef + " factorDivisa: " + factor);
        }
        else {
            tcRef = getPizarronServiceData().findPrecioPizarronPesos(
                    getSicaServiceData().findCanal("PMY").getTipoPizarron(),
                    div.getIdDivisa(), claveFormaLiquidacion, recibimos,
                    fechaValor);
            //debug("tcRef: " + tcRef);
        }
        // Se divide entre el precio de referencia si la div de referencia no son Pesos:
        if (!Divisa.PESO.equals(idDivisaReferencia)) {
            tcRef = invertirTipoCambio(tcRef, pr, div);
        }
        return validarPorcentajeDesv(tipoCambio, tcRef);
    }

    /**
     * Regresa el tipo de cambio rec&iacute;proco, si es que la divisa seleccionada debe dividir.\
     *
     * @param tcRef El tipo de cambio a invertir, si es necesario.
     * @param pr El precio de referencia actual.
     * @param div La divisa de la operaci&oacute;n.
     * @return double.
     */
    private double invertirTipoCambio(double tcRef, PrecioReferenciaActual pr, Divisa div) {
        double preRef = Redondeador.redondear6Dec(pr.getPreRef().getMidSpot().doubleValue());
        tcRef /= preRef;
        if (div.isDivide()) {
            tcRef = 1 / tcRef;
        }
        return tcRef;
    }
    
    /**
     * Regresa true si el porcentaje de desviaci&oacute;n entre tcCapturado y tcRef es mayor al 1%,
     * si es mayor al 5% levanta una SicaException.
     *
     * @param tcCapturado El tipo de cambio capturado por el usuario.
     * @param tcRef El tipo de cambio de referencia de la divisa.
     * @return boolean.
     * @throws SicaException Si la desviaci&oacute;n excede el 5%.
     */
    private Map validarPorcentajeDesv(double tcCapturado, double tcRef) throws SicaException {
        SicaServiceData sd = getSicaServiceData();
        ParametroSica paramLimDesv = sd.findParametro(ParametroSica.PORC_DESV_INTERB_LIM);
        double limPorcDesv = Redondeador.redondear2Dec(Double.valueOf(paramLimDesv.getValor()).
                doubleValue());
        // Revisamos el
        double tcInferior = Redondeador.redondear6Dec(tcRef * (1 - limPorcDesv / CIEN));
        double tcSuperior = Redondeador.redondear6Dec(tcRef * (1 + limPorcDesv / CIEN));
        /*debug("1. " + tcCapturado + " " + getTipoCambio() + " "
                + getTipoCambioParaCobertura() + " " + tcInferior + " " + tcSuperior);*/

        if (tcCapturado < tcInferior || tcCapturado > tcSuperior) {
            throw new SicaException("El tipo de cambio debe estar entre " + tcInferior + " y "
                    + tcSuperior + ".");
        }
        // Ahora revisamos la desviacion con nivel de advertencia:
        paramLimDesv = sd.findParametro(ParametroSica.PORC_DESV_INTERB_WARN);
        limPorcDesv = Redondeador.redondear2Dec(Double.valueOf(paramLimDesv.getValor()).
                doubleValue());
        tcInferior = Redondeador.redondear6Dec(tcRef * (1 - limPorcDesv / CIEN));
        tcSuperior = Redondeador.redondear6Dec(tcRef * (1 + limPorcDesv / CIEN));
       /* debug("2. " + tcCapturado + " " + getTipoCambio() + " " + getTipoCambioParaCobertura()
                + " " + tcInferior + " " + tcSuperior);*/
        return MapUtils.generar(new String[] { "validar", "porcentajeDesviacion" },
                new Object[] {Boolean.valueOf(tcCapturado < tcInferior || tcCapturado > tcSuperior),
                    paramLimDesv.getValor()});
    }
    
    /**
     * Regresa el valor de pizarronServiceData.
     *
     * @return PizarronServiceData.
     */
    public PizarronServiceData getPizarronServiceData() {
        return pizarronServiceData;
    }

    /**
     * Establece el valor de pizarronServiceData.
     *
     * @param pizarronServiceData El valor a asignar.
     */
    public void setPizarronServiceData(PizarronServiceData pizarronServiceData) {
        this.pizarronServiceData = pizarronServiceData;
    }

    /**
     * Regresa el valor de sicaServiceData.
     *
     * @return SicaServiceData.
     */
    public SicaServiceData getSicaServiceData() {
        return sicaServiceData;
    }

    /**
     * Establece el valor de sicaServiceData.
     *
     * @param sicaServiceData El valor a asignar.
     */
    public void setSicaServiceData(SicaServiceData sicaServiceData) {
        this.sicaServiceData = sicaServiceData;
    }

    /**
     * Service Data Object para consultar informaci&oacute;n del Pizarr&oacute;n.
     */
    private PizarronServiceData pizarronServiceData;

    /**
     * Service Data Object para consultar informaci&oacute;n general del SICA.
     */
    private SicaServiceData sicaServiceData;

    /**
     * Constante para el valor 100.0.
     */
    public static final double CIEN = 100.0;
}

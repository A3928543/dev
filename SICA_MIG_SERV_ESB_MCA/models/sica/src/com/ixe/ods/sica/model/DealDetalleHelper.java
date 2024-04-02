/*
 * $Id: DealDetalleHelper.java,v 1.3.2.1.14.1 2011/04/25 23:51:44 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.treasury.dom.common.FormaPagoLiq;

/**
 * Clase auxiliar del modelo DealDetalle. Provee m&eacute;todos auxiliares para obtener
 * informaci&oacute;n adicional del detalle de deal.
 * 
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.3.2.1.14.1 $ $Date: 2011/04/25 23:51:44 $
 */
public class DealDetalleHelper {

    /**
     * Constructor privado.
     */
    private DealDetalleHelper() {
        super();
    }

    /**
     * Permite saber si el detalle acepta Forma de Pago Liq.
     *
     * @param det El detalle de deal a revisar.
     * @param fpl La forma de pago liq. a verificar.
     * @return True o False.
     */
    public static boolean aceptaFormaPagoLiq(DealDetalle det, FormaPagoLiq fpl) {
        double montoMinimo = fpl.getMontoMinimo().doubleValue();
        double montoMaximo = fpl.getMontoMaximo().doubleValue();
        double mult = fpl.getMultiplo() != null ? fpl.getMultiplo().doubleValue() : 0.01;
        double res = Redondeador.redondear2Dec(det.getMonto() / mult);
        boolean esMultiplo = (res) - ((long) (res)) == 0;

        if ((montoMinimo >= 0.01 && det.getMonto() < montoMinimo) ||
                (montoMaximo >= 0.01 && det.getMonto() > montoMaximo)) {
            return false;
        }
        return esMultiplo;
    }

    /**
     * Permite obtener el encabezado del deal.
     *
     * @param det El detalle de deal a revisar.
     * @return El contexto.
     */
    public static HashMap getContextoAlertas(DealDetalle det) {
        HashMap encabezado = new HashMap();
        DealHelper.getContextoEncabezado(det.getDeal(), encabezado);
        det.getContextoExcepciones(encabezado);
        getContextoDetalle(det, encabezado);
        return encabezado;
    }

    /**
     * Popula al encabezado del detalle algunos par&aacute;metros.
     *
     * @param det El detalle de deal.
     * @param encabezado El mapa con los valoeres del deal.
     */
    private static void getContextoDetalle(DealDetalle det, HashMap encabezado) {
        encabezado.put("MONTO_DLL", String.valueOf(det.getMontoUSD()));
        encabezado.put("MONTO_PAGO", String.valueOf(det.getMontoUSD()));
        encabezado.put("MONTO_EXC", String.valueOf(det.getMontoUSD()));
        encabezado.put("DIVISA", String.valueOf(det.getDivisa().getDescripcion()));
        encabezado.put("DOCUMENTACION_DET", "NULL");
        encabezado.put("PORC_MAX_DESV", "");
        if (det.isRecibimos()) {
            encabezado.put("PORC_DESV",
                    String.valueOf(((det.getTipoCambioMesa() - det.getTipoCambio()) * 100) /
                            det.getTipoCambioMesa()));
        }
        else {
            encabezado.put("PORC_DESV",
                    String.valueOf(det.getTipoCambio() - det.getTipoCambioMesa() * 100 /
                            det.getTipoCambioMesa()));
        }
    }
    
    /**
     * Permite saber la Descripci&oacute;n del Status de un Deal Detalle.
     *
     * @param det El detalle de deal a revisar.
     * @return La descripcio&oacute;n.
     */
    public static String getDescripcionStatus(DealDetalle det) {
        if (Deal.REVERSADO == det.getReversado()) {
            return "Reversado";
        }
        else if (Deal.PROC_REVERSO == det.getReversado()) {
            return "En proceso de reverso";
        }
        else if (DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(det.getStatusDetalleDeal())) {
            return "Alta";
        }
        else if (DealDetalle.STATUS_DET_COMPLETO.equals(det.getStatusDetalleDeal())) {
            return "Proces\u00e1ndose";
        }
        else if (DealDetalle.STATUS_DET_PARCIALMENTE_PAG_LIQ.equals(det.getStatusDetalleDeal())) {
            return "Liq. Parcial";
        }
        else if (DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(det.getStatusDetalleDeal())) {
            return "Totalmente Liq.";
        }
        else if (DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal())) {
            return "Cancelado";
        }
        return "";
    }

    /**
     * Permite saber informaci&oacute;n adicional de las Pantallas de las Plantillas y las Formas de
     * Pago Liq. que tiene un Detalle.
     *
     * @param det El detalle a revisar.
     * @param fpl Las formas de pago liq.
     * @param pp Las Plantillas pantallas
     * @return La informacion adicional.
     */
    public static List getInfoAdicional(DealDetalle det, FormaPagoLiq fpl, PlantillaPantalla pp) {
        List list = new ArrayList();
        Map map = new HashMap();
        String metRep = pp.getMetodoReporte();
        if (metRep != null) {
            if (PlantillaPantalla.TIPO_REP_BANCO_TES_MATRIZ.equals(metRep)) {
                map.put("nombre", "BANCO");
                String nombreBanco = fpl.getNombreBanco();
                map.put("valor", nombreBanco != null ? nombreBanco.replaceAll("\\'", "\\\\'") : "");
                list.add(map);
                map = new HashMap();
                map.put("nombre", "CUENTA");
                map.put("valor", fpl.getNoCuenta());
                list.add(map);
            }
            else if (PlantillaPantalla.TIPO_REP_DENOMINACION.equals(metRep)) {
                map.put("nombre", "DENOMINACION");
                map.put("valor", det.getDenominacion() != null ?
                        Double.toString(det.getDenominacion().doubleValue()) : "");
                list.add(map);
            }
            else if (PlantillaPantalla.TIPO_REP_PLANTILLA.equals(metRep)) {
                if (det.getPlantilla() != null) {
                    list = det.getPlantilla().getInfoAdicional();
                    if (det.getPlantilla() instanceof IPlantillaIntl) {
                        map.put("nombre", "INFORMACION PARA EL BENEFICIARIO");
                        map.put("valor", det.getInstruccionesBeneficiario());
                        list.add(map);
                        map = new HashMap();
                        map.put("nombre", "INSTRUCCIONES PARA EL BANCO PAGADOR");
                        map.put("valor", det.getInstruccionesPagador());
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }

    /**
     * Si la divisa no es frecuente, regresa cero, si no regresa el valor del monto del detalle en
     * d&oacute;lares.
     *
     * @param det El detalle de deal a revisar.
     * @return double.
     */
    public static double getMontoUSD(DealDetalle det) {
        if (! det.getDivisa().isFrecuente()) {
            return 0;
        }
        else if (Divisa.DOLAR.equals(det.getDivisa().getIdDivisa())) {
            return det.getMonto();
        }
        else if (det.getFactorDivisa() != null) {
            return det.getMonto() * det.getFactorDivisa().doubleValue();
        }
        else {
            return 0;
        }
    }

    /**
     * Permite saber si se puede o no realizar una cancelaci&oacute;n de una liquidaci&oacute;n de
     * un detalle.
     *
     * @param det El detalle de deal a revisar.
     * @return True o False.
     */
    public static boolean isCancelacionLiqPermitida(DealDetalle det) {
        Deal deal = det.getDeal();

        if (!Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
            return det.getIdLiquidacionDetalle() == null && !deal.tieneSolSobreprecioPendiente() &&
                    deal.getNumeroDetallesDivisaNoCancelados() >= 2 &&
                    !(deal.getBalance() == 0) && !deal.tieneSolCancPendiente() &&
                    !deal.tieneSolModifPendiente() &&
                    !deal.tieneSolCancPendientesDetalles() &&
                    !deal.tieneSolModifPendientesDetalles() &&
                    !deal.tieneAutPendientesHorario() &&
                    !deal.tieneAutPendientesMonto() &&
                    !det.isEvento(DealDetalle.EV_IND_GRAL_CANC_DET, new String[]{
                            Deal.EV_SOLICITUD, Deal.EV_NEGACION, Deal.EV_APROBACION}) &&
                    DateUtils.inicioDia(deal.getFechaCaptura()).equals(DateUtils.inicioDia());
        }
        else {
            return det.getClaveFormaLiquidacion() == null &&
                    det.isStatusIn(new String[]{DealDetalle.STATUS_DET_PROCESO_CAPTURA}) &&
                    det.isEvento(deal.isInterbancario() ?
                            DealDetalle.EV_IND_INT_MODIFICACION : DealDetalle.EV_IND_MODIFICACION,
                            new String[]{Deal.EV_NORMAL}) &&
                    DateUtils.inicioDia(deal.getFechaLiquidacion()).getTime() >=
                            DateUtils.inicioDia().getTime();
        }
    }

    /**
     * Permite saber si una Forma Liq. capturada es permitida o no.
     *
     * @param det El detalle de deal a revisar.
     * @return True o False.
     */
    public static boolean isCapturaFormaLiqPermitida(DealDetalle det) {
        if (det.getDeal().isInterbancario()) {
            return !det.isEvento(DealDetalle.EV_IND_INT_MODIFICACION,
                    new String[]{Deal.EV_CANCELACION}) &&
                    (DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(det.getStatusDetalleDeal()) ||
                            (!DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal()) &&
                                    det.isEvento(DealDetalle.EV_IND_INT_MODIFICACION,
                                            new String[]{Deal.EV_SOLICITUD, Deal.EV_APROBACION})));
        }
        else {
            return !det.isEvento(DealDetalle.EV_IND_MODIFICACION,
                    new String[]{Deal.EV_CANCELACION}) &&
                    (DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(det.getStatusDetalleDeal()) ||
                            (!DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal()) &&
                                    det.isEvento(DealDetalle.EV_IND_MODIFICACION,
                                            new String[]{Deal.EV_SOLICITUD, Deal.EV_APROBACION})));
        }
    }
    
    /**
     * Regresa true si el detalle a&uacute;n no ha sido liquidado, el deal no ha sido cancelado y no
     * hay solicitud de cancelaci&oacute;n o modificaci&oacute;n pendiente,
     *
     * @param det El detalle de deal a revisar.
     * @return boolean.
     */
    public static boolean isSplitPermitido(DealDetalle det) {
        if (det.getDeal().isInterbancario() && det.getDeal().getIdLiquidacion() != null) {
            return false;
        }
        if (det.isPesos()) {
            return !det.isReversadoProcesoReverso() &&
                    !det.getDeal().tieneSolSobreprecioPendiente() &&
                    !det.isStatusIn(new String[]{DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ,
                            DealDetalle.STATUS_DET_CANCELADO});
        }
        else {
            return !det.isReversadoProcesoReverso() &&
                    !det.getDeal().tieneSolSobreprecioPendiente() &&
                    !det.isStatusIn(new String[]{DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ,
                            DealDetalle.STATUS_DET_CANCELADO}) &&
                    DateUtils.inicioDia(det.getDeal().getFechaLiquidacion()).getTime() >=
                            DateUtils.inicioDia().getTime();
        }
    }    
}

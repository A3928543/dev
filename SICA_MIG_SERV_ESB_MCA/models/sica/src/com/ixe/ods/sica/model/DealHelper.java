/*
 * $Id: DealHelper.java,v 1.13.2.1.36.1.24.3 2017/11/17 18:30:52 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2013 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.treasury.dom.common.FormaPagoLiq;

/**
 * Clase auxiliar del modelo Deal. Provee m&eacute;todos auxiliares para obtener informaci&oacute;n
 * adicional del deal.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.13.2.1.36.1.24.3 $ $Date: 2017/11/17 18:30:52 $
 */
public final class DealHelper {

    /**
     * Constructor por default.
     */
    private DealHelper() {
        super();
    }

    /**
     * Regresa el contexto de las alertas.
     *
     * @param deal El deal a revisar.
     * @return HashMap.
     */
    public static HashMap getContextoAlertas(Deal deal) {
        HashMap encabezado = new HashMap();
        HashMap detalle = new HashMap();
        getContextoEncabezado(deal, encabezado);
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle dt = (DealDetalle) it.next();
            dt.getContextoExcepciones(detalle);
            if (!(Divisa.PESO.equals(dt.getDivisa().getIdDivisa()))) {
                encabezado.put("DIVISA", dt.getDivisa().getDescripcion());
            }
            if (SI.equals(detalle.get(SI_NO))) {
                encabezado.put(SI_NO, SI);
                break;
            }
            else {
                encabezado.put(SI_NO, NO);
            }
        }
        if (NO.equals(encabezado.get(SI_NO))) {
           if (deal.haySolAut() || deal.hayNegAut()) {
                   encabezado.put(SI_NO, SI);
           }
        }
        return encabezado;
    }

    /**
     * Regresa el contexto del encabezado.
     *
     * @param deal El deal a revisar.
     * @param encabezado El contexto del encabezado.
     */
    public static void getContextoEncabezado(Deal deal, HashMap encabezado) {
        if (!deal.isInterbancario()) {
            encabezado.put(NOMBRE_CLIENTE, deal.getCliente() != null ?
                    deal.getCliente().getNombreCompleto() : "");
            encabezado.put(ID_PERSONA, deal.getCliente() != null ?
                    String.valueOf(deal.getCliente().getIdPersona().intValue()) : CERO);
        }
        else {
            encabezado.put(NOMBRE_CLIENTE, deal.getBroker() != null ?
                    deal.getBroker().getId().getPersonaMoral().getNombreCorto() : "");
            encabezado.put(ID_PERSONA, deal.getCliente() != null ?
                    String.valueOf(deal.getCliente().getIdPersona().intValue()) : CERO);
        }
        encabezado.put("MONTO", new DecimalFormat("0.00").format(deal.getTotalEntregamos()));
        encabezado.put("DEAL_NO", String.valueOf(deal.getIdDeal()));
        encabezado.put("DOCUMENTACION", "NULL");
        encabezado.put("PROMOTOR" , deal.getPromotor() != null ?
                deal.getPromotor().getIdPersona().toString() : "");
    }

    /**
     * Regresa una descripci&oacute;n significativa para el status del deal.
     *
     * @param statusDeal El status del deal.
     * @param reversado El valor del campo reversado.
     * @return String.
     */
    public static String getDescripcionStatus(String statusDeal, int reversado) {
        String descripcion = "";
        if (Deal.REVERSADO == reversado) {
            descripcion = "Reversado";
        }
        else if (Deal.PROC_REVERSO == reversado) {
            descripcion = "En proceso de reverso";
        }
        else if (Deal.STATUS_DEAL_CAPTURADO.equals(statusDeal)) {
            descripcion = "Proces\u00e1ndose";
        }
        else if (Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(statusDeal)) {
            descripcion = "Alta";
        }
        else if (Deal.STATUS_DEAL_PARCIAL_PAGADO_LIQ.equals(statusDeal)) {
            descripcion = "Liq. Parcial";
        }
        else if (Deal.STATUS_DEAL_TOTALMENTE_PAGADO.equals(statusDeal)) {
            descripcion = "Totalmente Pag.";
        }
        else if (Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO.equals(statusDeal)) {
            descripcion = "Totalmente Liq.";
        }
        else if (Deal.STATUS_DEAL_CANCELADO.equals(statusDeal)) {
            descripcion = "Cancelado";
        }
        return descripcion;
    }

    /**
     * Si el tipoValor es 'CASH', regresa 'HOY'; en caso contrario regresa el mismo tipoValor.
     *
     * @param deal El deal a revisar.
     * @return String.
     */
    public static String getDescripcionTipoValor(Deal deal) {
        String tv = deal.getTipoValor();

        if (Constantes.CASH.equals(deal.getTipoValor())) {
            tv = "HOY";
        }
        else if (Constantes.VFUT.equals(deal.getTipoValor())) {
            tv = "96HR";
        }
        return tv;
    }

    /**
     * Regresa el n&uacute;mero de folio mayor encontrado en todos los detalles m&aacute;s uno.
     *
     * @param deal El deal a revisar.
     * @return int.
     */
    public static int getSiguienteFolioDetalle(Deal deal) {
        int folioDetalle = 0;
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle d = (DealDetalle) it.next();
            if (d.getFolioDetalle() > folioDetalle) {
                folioDetalle = d.getFolioDetalle();
            }
        }
        return folioDetalle + 1;
    }
    
    /**
     * Regresa el detalle de deal que contiene el folio especificado.
     *
     * @param deal El deal a revisar.
     * @param folioDetalle El folio a buscar.
     * @return DealDetalle.
     */
    public static DealDetalle getDetalleConFolio(Deal deal, int folioDetalle) {
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle detalle = (DealDetalle) it.next();
            if (detalle.getFolioDetalle() == folioDetalle) {
                return detalle;
            }
        }
        throw new SicaException("No se encontr\u00f3 el detalle de deal con folio " + folioDetalle);
    }

    /**
     * <p>Regresa true si el deal es cancelable.</p>
     * Para poder cancelarse:
     * <li>El deal debe haber sido capturado ese mismo d&iacute;a.</li>
     * <li>El status del deal debe ser Alta o Proces&aacute;ndose.</li>
     * <li>El status del deal no tiene solicitud de cancelaci&oacute;n pendiente.</li>
     * <li>El status del deal no tiene detalle de modificaci&oacute;n pendiente.</li>
     * <li>El deal debe existir en la base de datos idDeal > 0.</li>
     * <li>El deal debe contener por lo menos un detalle en alta o proces&aacute;ndose.</li>
     * <li>El deal no debe tener detalles liquidados.</li>
     *
     * @param deal El deal a revisar.
     * @return boolean.
     */
    public static boolean isCancelable(Deal deal) {
        Date fechaHoy = DateUtils.inicioDia();
        
        if (!fechaHoy.equals(DateUtils.inicioDia(deal.getFechaCaptura()))) {
            return false;
        }
        if (Deal.STATUS_DEAL_CANCELADO.equals(deal.getStatusDeal()) ||
                deal.isReversadoProcesoReverso()) {
            return false;
        }
        if (deal.isDeSucursal()) {
            // Solo si hay un detalle de transferencia liquidado, no se puede cancelar el deal:
            for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                DealDetalle detalle = (DealDetalle) it.next();
                if (DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(
                        detalle.getStatusDetalleDeal()) &&
                        Constantes.TRANSFERENCIA.equals(detalle.getClaveFormaLiquidacion())) {
                    return false;
                }
            }
            return true;
        }
        else { // No es de sucursal:
            if (deal.tieneSolCancPendientesDetalles() || deal.tieneSolSobreprecioPendiente()) {
                return false;
            }
            if (!(fechaHoy.equals(DateUtils.inicioDia(deal.getFechaCaptura())) &&
                    (Deal.STATUS_DEAL_CAPTURADO.equals(deal.getStatusDeal()) ||
                            Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(deal.getStatusDeal())) &&
                    !deal.tieneSolModifPendiente() && !deal.tieneSolCancPendiente() &&
                    deal.getIdDeal() > 0)) {
                return false;
            }
            for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                DealDetalle detalle = (DealDetalle) it.next();
                if (DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(
                        detalle.getStatusDetalleDeal()) &&
                        detalle.getMnemonico().indexOf("BALNETEO") < 0) {
                    return false;
                }
            }
            for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                DealDetalle detalle = (DealDetalle) it.next();
                if (DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(detalle.getStatusDetalleDeal()) ||
                        DealDetalle.STATUS_DET_COMPLETO.equals(detalle.getStatusDetalleDeal())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Permite saber si el deal es procesable o no.
     *
     * @param deal          El deal a revisar.
     * @param formasPagoLiq La Lista de Formas Pago Liq.
     * @return boolean
     */
    public static boolean isProcesable(Deal deal, List formasPagoLiq) {
        if (deal.isDealOriginal() && deal.isReversadoProcesoReverso()) {
            return false;
        }
        if (deal.getDetalles().size() < 1 || (deal.getBalance() != 0.00 && !deal.isDeSucursal())) {
            return false;
        }
        if (deal.isConFactura() && (deal.getDirFactura() == null ||
                StringUtils.isEmpty(deal.getRfcFactura()) ||
                StringUtils.isEmpty(deal.getNombreFactura()))) {
            return false;

        }
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (!isProcesableDetalle(det, formasPagoLiq)) {
                return false;
            }
        }
        if (!deal.isInterbancario() && (deal.getFactura() == null || "-".equals(deal.getFactura()))) {
            return false;
        }
        if (!deal.isInterbancario() && (deal.getComprobante() == null || "-".equals(deal.getComprobante()))) {
            return false;
        }

        //60057-CFDI 3.3
        if (deal.isConFactura() && (deal.getMetodoPago() == null || "".equals(deal.getMetodoPago().trim())) ) {
        	return false;
        }
        
        return !deal.tieneAutPendientesHorario() && !deal.tieneAutPendientesMonto() &&
                !deal.tieneSolModifPendiente() && !deal.tieneSolCancPendiente() &&
                (Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(deal.getStatusDeal()) ||
                        deal.isEvento(Deal.EV_IND_GRAL_MODIFICACION,
                                new String[]{Deal.EV_APROBACION}));
    }

    /**
     * Permite saber si el detalle del deal es procesable o no.
     *
     * @param det El Detalle del Deal
     * @param formasPagoLiq La Lista de Formas Pago Liq
     * @return boolean
     */
    public static boolean isProcesableDetalle(DealDetalle det, List formasPagoLiq) {
        if (! det.isCancelado() && !det.isReversadoProcesoReverso()) {
            if (det.getMnemonico() == null) {
                return false;
            }
            FormaPagoLiq fpl = null;
            for (Iterator it = formasPagoLiq.iterator(); it.hasNext() && fpl == null; ) {
                FormaPagoLiq fp = (FormaPagoLiq) it.next();
                if (det.getMnemonico().equals(fp.getMnemonico())) {
                    fpl = fp;
                }
            }
            if (fpl == null) {
                return false;
            }
            String cveFormaLiquidacion = det.getClaveFormaLiquidacion();
            if (fpl.getUsaPlantilla().booleanValue() && det.getPlantilla() == null) {
                if (!det.getDeal().isDeSucursal() || (cveFormaLiquidacion != null &&
                        Constantes.TRANSFERENCIA.equals(det.getClaveFormaLiquidacion()))) {
                    return false;
                }
            }
            if (fpl.getUsaPlantilla().booleanValue() && det.getPlantilla() != null &&
                    !det.getPlantilla().puedeProcesarse()) {
                if (!det.getDeal().isDeSucursal() || (cveFormaLiquidacion != null &&
                        Constantes.TRANSFERENCIA.equals(det.getClaveFormaLiquidacion()))) {
                    return false;
                }
            }
            if (det.tieneSolCancPendiente()) {
                return false;
            }
            
            //F65211 Complemento - Obligatorio propósito de transferencia Ley 115 LIC
            if (fpl.getUsaPlantilla().booleanValue() && det.getPlantilla() != null &&
                    det.getPlantilla().puedeProcesarse()) {
            	if(det.faltaPropositoDeTransferencia()) {
            			return false;
            	}            	
            }
            	
        }
        return true;
    }

    /**
     * Regresa true si el evento en el &iacute;ndice <i>indice</i> se encuentra en alguno de los
     * status especificados.
     *
     * @param deal El deal a revisar.
     * @param indice El indice a localizar.
     * @param status EL arreglo de status a revisar.
     * @return boolean.
     */
    public static boolean isEvento(Deal deal, int indice, String[] status) {
        String st = deal.decodificarEvento(indice);
        for (int i = 0; i < status.length; i++) {
            if (st.equals(status[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Aplica el evento del ind&iacute;ce especificado al deal.
     *
     * @param deal El deal a revisar.
     * @param status El status a asignar.
     * @param indice El &iacute;ndice del evento.
     */
    public static void setEvento(Deal deal, String status, int indice) {
        // Si el evento es de pago anticipado y ya esta en uso, no debe poder cambiarse:
        if (Deal.EV_IND_GRAL_PAG_ANT == indice &&
                deal.isEvento(Deal.EV_IND_GRAL_PAG_ANT, new String[]{Deal.EV_USO})) {
            return;
        }
        char[] evs = deal.getEventosDeal().toCharArray();
        evs[indice] = status.charAt(0);
        deal.setEventosDeal(new String(evs));
    }

    /**
     * Regresa el monto en d&oacute;lares del deal.
     *
     * @param deal El deal a revisar.
     * @return double.
     */
    public static double getMontoUSD(Deal deal) {
        double montoRecibimos = 0.0;
        double montoEntregamos = 0.0;
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (!det.isCancelado()) {
                if (det.isRecibimos()) {
                    montoRecibimos += det.getMontoUSD();
                }
                else {
                    montoEntregamos += det.getMontoUSD();
                }
            }
        }
        return montoRecibimos > montoEntregamos ? montoRecibimos : montoEntregamos;
    }

    /**
     * Regresa la suma de comisiones cobradas en pesos de todos los detalles del deal.
     *
     * @param deal El deal a revisar.
     * @return BigDecimal.
     */
    public static double getTotalComisionMxn(Deal deal) {
        double totalComision = 0.0;
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle detalle = (DealDetalle) it.next();
            if (! DealDetalle.STATUS_DET_CANCELADO.equals(detalle.getStatusDetalleDeal())) {
                if (detalle.isRecibimos()) {
                    totalComision -= detalle.getComisionCobradaMxn();
                }
                else {
                    totalComision += detalle.getComisionCobradaMxn();
                }
            }
        }
        return totalComision;
    }

    /**
     * Regresa true si alguno de los detalles del deal tiene una autorizaci&oacute;n por
     * cancelaci&oacute;n pendiente, y no es interbancario.
     *
     * @param deal El deal a revisar.
     * @return boolean.
     * @see com.ixe.ods.sica.model.DealDetalle#tieneSolCancPendiente().
     */
    public static boolean tieneSolCancPendientesDetalles(Deal deal) {
        if (deal.isInterbancario()) {
            return false;
        }
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (det.tieneSolCancPendiente()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Regresa true si alguno de los detalles del deal tiene una autorizaci&oacute;n por
     * modificaci&oacute;n pendiente.
     *
     * @param deal El deal a revisar.
     * @return boolean.
     * @see com.ixe.ods.sica.model.DealDetalle#tieneSolModifPendiente().
     */
    public static boolean tieneSolModifPendientesDetalles(Deal deal) {
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (det.tieneSolModifPendiente()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Revisa que no haya EFECTIVO en la contraparte para la misma divisa, si es el &uacute;nico
     * producto que se compr&oacute; y vendi&oacute;.
     *
     * @param detsContraparte Los detalles de contraparte.
     * @param idDivisa La divisa a buscar.
     */
    public static void validarEfectivo(List detsContraparte, String idDivisa) {
        boolean encontrado = false;
        for (Iterator it = detsContraparte.iterator(); it.hasNext(); ) {
            DealDetalle det = (DealDetalle) it.next();
            if (! det.isCancelado() && det.getClaveFormaLiquidacion() != null) {
                if (Constantes.EFECTIVO.equals(det.getClaveFormaLiquidacion())) {
                    if (idDivisa.equals(det.getDivisa().getIdDivisa())) {
                        encontrado = true;
                    }
                }
            }
        }
        if (encontrado) {
            throw new SicaException("No se puede comprar y vender efectivo en la misma divisa.");
        }
    }
    
    /**
     * Constante para la cadena 'NOMBRE_CLIENTE'.
     */
    private static final String NOMBRE_CLIENTE = "NOMBRE_CLIENTE";

    /**
     * Constante para la cadena 'ID_PERSONA'.
     */
    private static final String ID_PERSONA = "ID_PERSONA";

    /**
     * Constante para la cadena 'SI'. 
     */
    private static final String SI = "SI";

    /**
     * Constante para la cadena 'NO'.
     */
    private static final String NO = "NO";

    /**
     * Constante para la cadena 'SI_NO'.
     */
    private static final String SI_NO = "SI_NO";

    /**
     * Constante para la cadena '0'.
     */
    private static final String CERO = "0";
}

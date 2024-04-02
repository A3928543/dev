/*
 * $Id: ValidacionDealsPendietesDiaSicaPage.java,v 1.6 2009/08/03 22:10:06 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Swap;
import com.ixe.ods.sica.utils.DateUtils;

/**
 * Implementa la l&oacute;gica de validaci&oacute;n de DealsPendietesDia y define
 * m&eacute;todos de conveniencia para el acceso a los ServiceData
 *
 * @author Eduardo Sanchez (esanchezdm)
 * @version $Revision: 1.6 $ $Date: 2009/08/03 22:10:06 $
 */
public class ValidacionDealsPendietesDiaSicaPage extends SicaPage {

    /**
     * Constructor por default.
     */
    public ValidacionDealsPendietesDiaSicaPage() {
        super();
    }

    /**
     * Regresa los deals de la lista proporcionada, cuya fecha de liquidaci&oacute;n corresponde a
     * la de la fecha actual del Sistema.
     *
     * @param deals        La lista de deals.
     * @param fechaSistema La fecha de operaci&oacute;n actual del sistema.
     * @return List.
     */
    private List getDealsLiquidanHoy(List deals, Date fechaSistema) {
        List dealsLiquidanHoy = new ArrayList();
        for (Iterator it = deals.iterator(); it.hasNext();) {
            Deal deal = (Deal) it.next();
            if (deal.getFechaLiquidacion().getTime() < DateUtils.finDia(fechaSistema).getTime()) {
                dealsLiquidanHoy.add(deal);
            }
        }
        return dealsLiquidanHoy;
    }

    /**
     * Crea las listas de Deals que bloquean el cambio de estado.
     *
     * @param source   Lista de deals
     * @param criterio Criterio de filtro
     * @param soloHoy  Si debe considerar s&oacute;lo los deals que se liquidan el d&iacute;a de hoy.
     * @return List.
     */
    public List validarDealsPendietesDiaBlocker(List source, String criterio, boolean soloHoy) {
        Date fechaValorHoy = DateUtils.inicioDia(getSicaServiceData().getFechaSistema());
        List dealsRecibidos = new ArrayList();
        if (soloHoy) {
            dealsRecibidos = getDealsLiquidanHoy(source, fechaValorHoy);
        }
        else {
            dealsRecibidos.addAll(source);
        }
        List tmp = new ArrayList();
        if (DEALS_PENDIENTES_POR_HORARIO.equals(criterio)) {
            for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                Deal deal = (Deal) iterator.next();
                if (!deal.isInterbancario() && deal.tieneAutPendientesHorario()) {
                    tmp.add(deal);
                }
            }
        }
        else if (DEALS_PENDIENTES_POR_COMPLETAR.equals(criterio)) {
            for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                Deal deal = (Deal) iterator.next();
                if (Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(deal.getStatusDeal())) {
                    tmp.add(deal);
                }
            }
        }
        else if (DEALS_PENDIENTES_POR_DEALS_PENDIENTES_ALTA.equals(criterio)) {
            for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                Deal deal = (Deal) iterator.next();
                if (Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(deal.getStatusDeal())) {
                    tmp.add(deal);
                }
            }
        }
        else if (DEALS_PENDIENTES_POR_BANXICO.equals(criterio)) {
            for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                Deal deal = (Deal) iterator.next();
                if (Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_GRAL_BANXICO_C)) ||
                        Deal.EV_PROB_COM_BANXICO.equals(
                                deal.decodificarEvento(Deal.EV_IND_GRAL_BANXICO_C)) ||
                        Deal.EV_SOLICITUD.equals(
                                deal.decodificarEvento(Deal.EV_IND_GRAL_BANXICO_V)) ||
                        Deal.EV_PROB_COM_BANXICO.equals(
                                deal.decodificarEvento(Deal.EV_IND_GRAL_BANXICO_V))) {
                    if (!deal.isInterbancario()) {
                        tmp.add(deal);
                    }
                }
            }
        }
        else if (DEALS_PENDIENTES_POR_DOCUMENTACION.equals(criterio)) {
            for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                Deal deal = (Deal) iterator.next();
                if (Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_DOCUMENTACION))) {
                    if (!deal.isInterbancario()) {
                        tmp.add(deal);
                    }
                }
                if (Deal.EV_SOLICITUD.equals(deal.
                        decodificarEvento(Deal.EV_IND_INT_DOCUMENTACION))) {
                    if (deal.isInterbancario()) {
                        tmp.add(deal);
                    }
                }
            }            
        }
        else if (DEALS_PENDIENTES_POR_PLANTILLA.equals(criterio)) {
        	 for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                 Deal deal = (Deal) iterator.next();
                 if (Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_PLANTILLA))) {
                     if (!deal.isInterbancario()) {
                         tmp.add(deal);
                     }
                 }
                 if (Deal.EV_SOLICITUD.equals(deal.
                         decodificarEvento(Deal.EV_IND_INT_PLANTILLA))) {
                     if (deal.isInterbancario()) {
                         tmp.add(deal);
                     }
                 }
             }
        }        
        else if (DEALS_PENDIENTES_POR_TOMA_EN_FIRME.equals(criterio)) {
            for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                Deal deal = (Deal) iterator.next();
                if (Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_GRAL_PAG_ANT))) {
                    if (!deal.isInterbancario()) {
                        tmp.add(deal);
                    }
                }
                if (Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_GRAL_PAG_ANT)) ||
                        Deal.EV_SOLICITUD.equals(
                                deal.decodificarEvento(Deal.EV_IND_INT_LINEA_OPERACION))) {
                    if (deal.isInterbancario()) {
                        tmp.add(deal);
                    }
                }
            }
        }
        else if (DEALS_PENDIENTES_POR_SWAP.equals(criterio)) {
            for (Iterator it = dealsRecibidos.iterator(); it.hasNext();) {
                Swap sw = (Swap) it.next();
                if (!Swap.STATUS_COMPLETAMENTE_ASIGNADO.equals(sw.getStatus())) {
                    List dealsSwap = sw.getDeals();
                    for (Iterator it1 = dealsSwap.iterator(); it1.hasNext();) {
                        Deal deal = (Deal) it1.next();
                        tmp.add(deal);
                    }
                }
            }
        }
        else if (DEALS_PENDIENTES_POR_MONTO.equals(criterio)) {
            for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                Deal deal = (Deal) iterator.next();
                if (!deal.isInterbancario() && deal.tieneAutPendientesMonto()) {
                    tmp.add(deal);
                }
            }
        }
        else if (DEALS_PENDIENTES_POR_BALANCE.equals(criterio)) {
            for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                Deal deal = (Deal) iterator.next();
                if (deal.getBalance() != 0.0) {
                    if (!deal.isDeSucursal()) {
                        tmp.add(deal);
                    }
                }
            }
        }
        else if (DEALS_PENDIENTES_POR_CONTRATO.equals(criterio)) {
            for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                Deal deal = (Deal) iterator.next();
                if (deal.getContratoSica() == null) {
                    tmp.add(deal);
                }
            }
        }
        else if (DEALS_PENDIENTES_POR_RFC.equals(criterio)) {
            for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                Deal deal = (Deal) iterator.next();
                if (Deal.EV_SOLICITUD.equals(deal.getCambioRfc())) {
                    tmp.add(deal);
                }
            }
        }
        else if (DEALS_PENDIENTES_POR_EMAIL.equals(criterio)) {
            for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                Deal deal = (Deal) iterator.next();
                if (Deal.EV_SOLICITUD.equals(deal.getCambioEmail())) {
                    tmp.add(deal);
                }
            }
        }
        else if (DEALS_PENDIENTES_POR_SOLICITUD_SOBREPRECIO.equals(criterio)) {
            for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                Deal deal = (Deal) iterator.next();
                if (!deal.isInterbancario()) {
                    if (Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_SOBREPRECIO))) {
                        tmp.add(deal);
                    }
                }
            }
        }
        else if (DEALS_PENDIENTES_POR_MODIFICACION.equals(criterio)) {
            for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                Deal deal = (Deal) iterator.next();
                if (Deal.EV_SOLICITUD.equals(
                        deal.decodificarEvento(Deal.EV_IND_GRAL_MODIFICACION)) ||
                        Deal.EV_SOLICITUD.equals(
                                deal.decodificarEvento(Deal.EV_IND_GRAL_CANCELACION)) ||
                        Deal.EV_APROBACION_TESORERIA.equals(
                                deal.decodificarEvento(Deal.EV_IND_GRAL_MODIFICACION)) ||
                        Deal.EV_APROBACION_TESORERIA.equals(
                                deal.decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
                    tmp.add(deal);
                }
            }
        }
        else if (DEALS_PENDIENTES_POR_ALGUN_DETALLE.equals(criterio)) {
            for (Iterator iterator = dealsRecibidos.iterator(); iterator.hasNext();) {
                Deal deal = (Deal) iterator.next();
                for (Iterator it1 = deal.getDetalles().iterator(); it1.hasNext();) {
                    DealDetalle det = (DealDetalle) it1.next();
                    if (det.haySolAut() &&
                            !DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal())) {
                        tmp.add(deal);
                    }
                }
            }
        }
        List tmpSorted = new ArrayList();
        for (Iterator it = tmp.iterator(); it.hasNext();) {
            Deal deal = (Deal) it.next();
            if (!tmpSorted.contains(deal)) {
                tmpSorted.add(deal);
            }
        }
        return tmpSorted;
    }

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_HORARIO".
     */
    protected static final String DEALS_PENDIENTES_POR_HORARIO = "DEALS_PENDIENTES_POR_HORARIO";

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_COMPLETAR".
     */
    protected static final String DEALS_PENDIENTES_POR_COMPLETAR = "DEALS_PENDIENTES_POR_COMPLETAR";

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_BANXICO".
     */
    protected static final String DEALS_PENDIENTES_POR_BANXICO = "DEALS_PENDIENTES_POR_BANXICO";

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_DOCUMENTACION".
     */
    protected static final String DEALS_PENDIENTES_POR_DOCUMENTACION =
            "DEALS_PENDIENTES_POR_DOCUMENTACION";
    
    protected static final String DEALS_PENDIENTES_POR_PLANTILLA =
        "DEALS_PENDIENTES_POR_PLANTILLA";

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_TOMA_EN_FIRME".
     */
    protected static final String DEALS_PENDIENTES_POR_TOMA_EN_FIRME =
            "DEALS_PENDIENTES_POR_TOMA_EN_FIRME";

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_SWAP".
     */
    protected static final String DEALS_PENDIENTES_POR_SWAP = "DEALS_PENDIENTES_POR_SWAP";

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_MONTO".
     */
    protected static final String DEALS_PENDIENTES_POR_MONTO = "DEALS_PENDIENTES_POR_MONTO";

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_BALANCE".
     */
    protected static final String DEALS_PENDIENTES_POR_BALANCE = "DEALS_PENDIENTES_POR_BALANCE";

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_CONTRATO".
     */
    protected static final String DEALS_PENDIENTES_POR_CONTRATO = "DEALS_PENDIENTES_POR_CONTRATO";

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_RFC".
     */
    protected static final String DEALS_PENDIENTES_POR_RFC = "DEALS_PENDIENTES_POR_RFC";

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_EMAIL".
     */
    protected static final String DEALS_PENDIENTES_POR_EMAIL = "DEALS_PENDIENTES_POR_EMAIL";

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_SOLICITUD_SOBREPRECIO".
     */
    protected static final String DEALS_PENDIENTES_POR_SOLICITUD_SOBREPRECIO =
            "DEALS_PENDIENTES_POR_SOLICITUD_SOBREPRECIO";

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_MODIFICACION".
     */
    protected static final String DEALS_PENDIENTES_POR_MODIFICACION =
            "DEALS_PENDIENTES_POR_MODIFICACION";

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_ALGUN_DETALLE".
     */
    protected static final String DEALS_PENDIENTES_POR_ALGUN_DETALLE =
            "DEALS_PENDIENTES_POR_ALGUN_DETALLE";

    /**
     * Constante para la cadena "DEALS_PENDIENTES_POR_DEALS_PENDIENTES_ALTA".
     */
    protected static final String DEALS_PENDIENTES_POR_DEALS_PENDIENTES_ALTA =
            "DEALS_PENDIENTES_POR_DEALS_PENDIENTES_ALTA";
}

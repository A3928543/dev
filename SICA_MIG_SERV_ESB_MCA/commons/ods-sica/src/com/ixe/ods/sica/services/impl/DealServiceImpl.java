/*
 * $Id: DealServiceImpl.java,v 1.44.12.1.8.1.6.1.8.2.14.1.10.1.18.1 2020/12/01 22:10:42 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ixe.ods.sica.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.DealHelper;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.IPlantilla;
import com.ixe.ods.sica.model.IPlantillaCuentaIxe;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.model.IPlantillaTranNacional;
import com.ixe.ods.sica.model.LimitesRestriccionOperacion;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.model.PersonaListaBlanca;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.DealService;
import com.ixe.ods.sica.services.FormasPagoLiqService;
import com.ixe.ods.sica.services.ValorFuturoService;
import com.ixe.ods.sica.utils.BDUtils;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.treasury.dom.common.DetalleLiquidacion;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.ixe.treasury.dom.common.Liquidacion;

/**
 * Implementaci&oacute;n de la interfaz DealService.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.44.12.1.8.1.6.1.8.2.14.1.10.1.18.1 $ $Date: 2020/12/01 22:10:42 $
 */
public class DealServiceImpl implements DealService {

    /**
     * Constructor vac&iacute;o.
     */
    public DealServiceImpl() {
        super();
    }

    /**
     * @param deal El encabezado del deal.
     * @param recibimos true para recibimos, false para entregamos.
     * @param fechaValor CASH | TOM | SPOT | 72HR.
     * @param claveFormaLiquidacion La clave del producto.
     * @param div El objeto Divisa.
     * @param tipoCambioMesa El tipo de cambio del pizarr&oacute;n.
     * @param monto El monto en la divisa.
     * @param tipoCambio El tipo de cambio dado al cliente.
     * @param precioReferenciaMidSpot El precio referencia Mid Spot utilizado al momento de la captura.
     * @param precioReferenciaSpot El precio referencia Spot utilizado al momento de la captura.
     * @param factorDivisa El factor divisa actual.
     * @param idPrecioReferencia    El identificador del precio de referencia actual.
     * @param idFactorDivisa        El identificador del Factor Divisa actual.
     * @param idSpread El identificador del spread actual.
     * @param mnemonico El mnem&oacute;nico a aplicar.
     * @param fechasValor El arreglo de fechas valor (h&aacute;biles).
     * @param montoMaximoExcedido Si el montoMaximo ha sido excedido o no.
     * @param estado El estado actual del sistema.
     * @param limRestOper Los l&iacute;mites de restricci&oacute;n de operaci&oacute;n.
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param ip La direcci&oacute;n IP del usuario que realiza la operaci&oacute;n.
     * @param usuario El usuario que realiza la operaci&oacute;n.
     * @see com.ixe.ods.sica.services.DealService#crearDealDetalle(com.ixe.ods.sica.model.Deal,
            boolean, String, String, com.ixe.ods.sica.model.Divisa, double, double, double, int,
            com.ixe.ods.sica.model.FactorDivisa, int, String, java.util.Date[], boolean,
            com.ixe.ods.sica.model.Estado, com.ixe.ods.sica.model.LimitesRestriccionOperacion,
            String, String, com.ixe.ods.seguridad.model.IUsuario).
     * @return DealDetalle.
     * @throws SicaException Si algo sale mal.
     */
    public DealDetalle crearDealDetalle(Deal deal, boolean recibimos, String fechaValor,
                                        String claveFormaLiquidacion, Divisa div,
                                        double tipoCambioMesa, double monto, double tipoCambio,
                                        Double precioReferenciaMidSpot, Double precioReferenciaSpot, 
                                        Double factorDivisa, Integer idPrecioReferencia, Integer idFactorDivisa,
                                        int idSpread, String mnemonico, Date[] fechasValor,
                                        boolean montoMaximoExcedido, Estado estado,
                                        LimitesRestriccionOperacion limRestOper, String ticket,
                                        String ip, IUsuario usuario)
            throws SicaException {
        String tv = deal.getTipoValor() != null ? deal.getTipoValor().trim() : deal.getTipoValor();
        fechaValor = fechaValor.trim();
        if (monto < 0.01) {
            throw new SicaException("El monto no puede ser menor a 0.01.");
        }
        List detsContraparte = recibimos ? deal.getEntregamos() : deal.getRecibimos();
        if (Constantes.EFECTIVO.equals(claveFormaLiquidacion)) {
            DealHelper.validarEfectivo(detsContraparte, div.getIdDivisa());
        }
        if (tv == null || fechaValor.equals(tv)) {
            deal.setTipoValor(fechaValor);
            if (deal.getFechaLiquidacion() == null) {
                String tpVal = deal.getTipoValor();
                deal.setFechaLiquidacion(tpVal.equals(Constantes.VFUT) ? fechasValor[Num.I_4] :
                        tpVal.equals(Constantes.HR72) ? fechasValor[Num.I_3] :
                                tpVal.equals(Constantes.SPOT) ? fechasValor[2] :
                                        tpVal.equals(Constantes.TOM) ?
                                                fechasValor[1] : fechasValor[0]);
            }
            DealDetalle det = new DealDetalle(deal, recibimos, claveFormaLiquidacion, div, monto,
                    tipoCambio, DealHelper.getSiguienteFolioDetalle(deal));
            det.setMnemonico(mnemonico);
            det.setDeal(deal);
            det.setPrecioReferenciaMidSpot(precioReferenciaMidSpot);
            det.setPrecioReferenciaSpot(precioReferenciaSpot);
            det.setFactorDivisa(factorDivisa);
            det.setIdPrecioReferencia(idPrecioReferencia);
            det.setIdFactorDivisa(idFactorDivisa);
            det.setIdSpread(idSpread);
            det.setTipoCambioMesa(tipoCambioMesa);
            det.setTipoCambio(tipoCambio);
            det.setMesaCambio(deal.getCanalMesa().getMesaCambio());
            if (!deal.isInterbancario() && !deal.isDeSucursal() && !deal.isDealBalanceo()) {
                if (montoMaximoExcedido) {
                    det.setEvento(Deal.EV_SOLICITUD, DealDetalle.EV_IND_MONTO);
                }
                if (estado.getIdEstado() == Estado.ESTADO_OPERACION_RESTRINGIDA) {
                    det.setEvento(Deal.EV_SOLICITUD, DealDetalle.EV_IND_HORARIO);
                }
            }
            if (limRestOper != null && !limRestOper.getTipoExcepcion().equals(PersonaListaBlanca.
            		EXCEPCION_SHCP)) {
            	try {
            		getSicaServiceData().validaLimitesRestriccionOperacion(deal, det,
            				claveFormaLiquidacion, limRestOper, null, null,
            				deal.getFechaLiquidacion(), false);
            	}
            	catch (SicaException se) {
            		if (se.getMessage().substring(0, 9).equals("LimEfect:")) {
            			getSicaServiceData().auditar(ticket, ip, new Integer(deal.getIdDeal()),
            					usuario, LogAuditoria.EXCEDIO_LIMITE_OPERACION_TELLER, null,
            					"WARN", "F");
            			throw new SicaException(se.getMessage().substring(10, se.getMessage().
            					length()));
            		}
            		else {
            			throw new SicaException(se.getMessage());
            		}
            	}
            }
            if (!deal.isInterbancario() &&
                    Constantes.TRAVELER_CHECKS.equals(claveFormaLiquidacion) &&
                    deal.getContratoSica() != null && !det.isRecibimos()) {
                double montoChviaj = getSicaServiceData().
                        findMontoChequesViajeroUsdFechaLiquidacion(deal.getContratoSica().
                                getNoCuenta(), deal.getFechaLiquidacion());
                if (det.getMontoUSD() + montoChviaj > 25000) {
                    det.getDeal().getDetalles().remove(det);
                    det.setDeal(null);
                    if ("0.00".equals(Constantes.MONEY_FORMAT.format(25000 - montoChviaj))) {
                        throw new SicaException("El cliente ya no puede operar Cheques de " +
                                "Viajero durante el resto del d\u00eda.");
                    }
                    else {
                        throw new SicaException("El cliente solo puede operar " +
                                Constantes.MONEY_FORMAT.format(25000 - montoChviaj) +
                                " USD en Cheques de Viajero durante el resto del d\u00eda.");
                    }
                }
            }
            return det;
        }
        else {
            throw new SicaException("El primer detalle fue definido Fecha Valor " + tv +
                    ". No se permiten detalles con diferentes Fecha Valor.");
        }
    }

    /**
     * Obtiene el valor por "default" de la fecha l&iacute;mite de captura de contrato
     * especificada en sc_parametro.
     *
     * @return Date La fecha l&iacute;mite de captura de contrato.
     */
    public Date getFechaLimiteCapturaContrato() {
    	int fechaLimCaptCont = getSicaServiceData().getFechaLimiteCapturaDeal();
    	Calendar gc = new GregorianCalendar();
    	gc.add(Calendar.MINUTE, fechaLimCaptCont);
    	return gc.getTime();
    }

    /**
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param det    El detalle del deal.
     * @param cobrar Si es para cobrar o pagar.
     * @see com.ixe.ods.sica.services.DealService#calcularComisionDealDetalle(String,
            com.ixe.ods.sica.model.DealDetalle, boolean).
     */
    public void calcularComisionDealDetalle(String ticket, DealDetalle det, boolean cobrar) {
        if (det.getDeal().getContratoSica() == null) {
            return;
        }
        if (det.getMnemonico() != null) {
            FormaPagoLiq fpl = getFormaPagoLiq(ticket, det.getMnemonico());
            double porcentajeComision = det.getDeal().getContratoSica().getPorcentajeComision();
            double comisionUsd = fpl.getComision() != null ? fpl.getComision().doubleValue() : 0.0;
            det.setComisionOficialUsd(comisionUsd);
            if (cobrar) {
                det.setComisionCobradaUsd(Redondeador.redondear2Dec(
                        comisionUsd * porcentajeComision / 100));
                getSicaServiceData().calcularComisionMxn(det,
                        getSicaServiceData().findPrecioReferenciaActual().getPreRef().getPrecioSpot().doubleValue());
            }
            /*else {
                det.setComisionCobradaUsd(0);
                det.setComisionCobradaMxn(0);
            }*/
        }
    }

    /**
     * Regresa la suma de los montos en divisa de los detalles de recibimos, independientemente de
     * la divisa a la que corresponden.
     *
     * @param deal El deal a revisar.
     * @return BigDecimal.
     */
    private BigDecimal getTotalUnidades(Deal deal) {
        BigDecimal totalRecibimos = BDUtils.generar2(0);
        BigDecimal totalEntregamos = BDUtils.generar2(0);
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle dealDetalle = (DealDetalle) it.next();
            if (dealDetalle.isRecibimos() && !dealDetalle.isCancelado() &&
                    !dealDetalle.isReversadoProcesoReverso() &&
                    dealDetalle.getMnemonico() != null &&
                    dealDetalle.getMnemonico().indexOf("BALNETEO") < 0) {
                totalRecibimos = totalRecibimos.add(BDUtils.generar2(dealDetalle.getMonto()));
            }
            else if (!dealDetalle.isRecibimos() && !dealDetalle.isCancelado() &&
                    !dealDetalle.isReversadoProcesoReverso() &&
                    dealDetalle.getMnemonico() != null &&
                    dealDetalle.getMnemonico().indexOf("BALNETEO") < 0) {
                totalEntregamos = totalEntregamos.add(BDUtils.generar2(dealDetalle.getMonto()));
            }
        }
        return totalRecibimos.doubleValue() > totalEntregamos.doubleValue() ?
                totalRecibimos : totalEntregamos;
    }

    /**
     * Regresa la suma de los montos en divisa de los detalles de recibimos, independientemente de
     * la divisa a la que corresponden.
     *
     * @param deal El deal a revisar.
     * @return BigDecimal.
     */
    private BigDecimal getTotalMn(Deal deal) {
        BigDecimal totalRecibimos = BDUtils.generar2(0);
        BigDecimal totalEntregamos = BDUtils.generar2(0);
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle dealDetalle = (DealDetalle) it.next();
            if (dealDetalle.isRecibimos() && !dealDetalle.isCancelado() &&
                    !dealDetalle.isReversadoProcesoReverso() &&
                    dealDetalle.getMnemonico() != null &&
                    dealDetalle.getMnemonico().indexOf("BALNETEO") < 0) {
                totalRecibimos = totalRecibimos.add(BDUtils.generar2(dealDetalle.getImporte()));
            }
            else if (!dealDetalle.isRecibimos() && !dealDetalle.isCancelado() &&
                    !dealDetalle.isReversadoProcesoReverso() &&
                    dealDetalle.getMnemonico() != null &&
                    dealDetalle.getMnemonico().indexOf("BALNETEO") < 0) {
                totalEntregamos = totalEntregamos.add(BDUtils.generar2(dealDetalle.getImporte()));
            }
        }
        return totalRecibimos.doubleValue() > totalEntregamos.doubleValue() ?
                totalRecibimos : totalEntregamos;
    }

    /**
     * @param deal                    El deal a liquidar.
     * @param ignorarPagoAntTomaFirme Si debe asignarse valor a Pago Anticipado y Toma en Firme o
     *      no (para el caso de que sea necesaria una autorizaci&oacute;n de cr&eacute;dito.
     * @return Liquidacion.
     * @see com.ixe.ods.sica.services.DealService#crearLiquidacion(com.ixe.ods.sica.model.Deal,
            boolean).
     */
    public Liquidacion crearLiquidacion(Deal deal, boolean ignorarPagoAntTomaFirme) {
        Liquidacion liq = new Liquidacion();
        
        if (deal.getIdLiquidacion() != null) {
            liq.setIdLiquidacion(new Long(deal.getIdLiquidacion().intValue()));
        }
        liq.setAcudirCon(deal.getAcudirCon());
        liq.setCanal(deal.getCanalMesa().getCanal().getIdCanal());
        liq.setCompra(Boolean.valueOf(deal.isCompra()));
        liq.setComprobanteRequerido(new Integer(deal.isConFactura() ? 1 : 2));
        liq.setCuentaIxeCliente(deal.getNoCuentaIxe() != null ?
                deal.getNoCuentaIxe() : deal.getContratoSica().getNoCuenta());
        liq.setDetallesLiquidacion(new HashSet());
        liq.setFacturaAnticipada(new Boolean(deal.isConFactura()));
        liq.setFechaIngreso(deal.getFechaCaptura());
        liq.setTipoFechaValor(deal.getTipoValor());
        liq.setTipoOrden(deal.getTipoDeal());
        liq.setContratoSica(deal.getContratoSica().getNoCuenta());

        Contrato contrato = getSicaServiceData().findContratoByNoCuenta(liq.getContratoSica());

        if (contrato != null) {
            liq.setContratoCorto(new Long(contrato.getIdContrato()));
        }
        liq.setCanal(deal.getCanalMesa().getCanal().getIdCanal());
        liq.setFechaValor(DateUtils.inicioDia(deal.getFechaLiquidacion()));
        liq.setIdDireccion(deal.getDireccion() != null ?
                new Long(deal.getDireccion().getIdDireccion().intValue()) : null);
        Integer idDireccionCliente = getIdDireccionCliente(deal);
        liq.setIdDireccionCliente(idDireccionCliente != null ?
                new Long(idDireccionCliente.intValue()) : null);
        liq.setIdPersonaCliente(new Long(deal.getCliente().getIdPersona().intValue()));
        liq.setIdPersonaPromotor(new Long(deal.getPromotor().getIdPersona().intValue()));
        liq.setNumeroOrden(String.valueOf(deal.getIdDeal()));
        liq.setMontoDivisa(getTotalUnidades(deal));
        liq.setMontoMonedaNacional(new BigDecimal("" + getTotalMn(deal)));
        if (!ignorarPagoAntTomaFirme) {
            liq.setLiquidacionAnticipada(Boolean.valueOf(deal.isPagoAnticipado()));
            liq.setTomaEnFirme(Boolean.valueOf(deal.isTomaEnFirme()));
        }
        liq.setObservaciones(deal.getObservaciones());
	    liq.setMetodoPago(deal.getMetodoPago());
	    liq.setCuentaPago(deal.getCuentaPago());
        liq.setMensajeria(Boolean.valueOf(deal.isMensajeria()));
        liq.setSistemaRegistra(FacultySystem.SICA);
        liq.setSistemaOrigen(deal.isDeSucursal() ? "TELLER" : FacultySystem.SICA);
        liq.setFeDirMsj(deal.getDirFacturaMensajeria() != null
                ? new Long(deal.getDirFacturaMensajeria().getIdDireccion().intValue()) : null);
        if (deal.getContrarioDe() != null) {
            Deal dealOriginal = (Deal) getSicaServiceData().find(Deal.class, deal.getContrarioDe());
            if (dealOriginal.getIdLiquidacion() != null) {
                liq.setIdLiquidacionReverso(
                    new Long(String.valueOf(dealOriginal.getIdLiquidacion().intValue())));
            }
        }
        BigDecimal montoBalanceoMN = new BigDecimal("" + 0);
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (!det.isCancelado() && det.getMnemonico() != null &&
                    det.getMnemonico().indexOf("BALNETEO") >= 0) {
                montoBalanceoMN = montoBalanceoMN.add(new BigDecimal("" + det.getMonto()));
            }
        }
        liq.setMontoBalanceoMN(montoBalanceoMN);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("*** solicitando liquidacion: " + " getComprobanteRequerido " +
                    liq.getComprobanteRequerido() + " getFechaIngreso " + liq.getFechaIngreso() +
                    " getFechaValor " + liq.getFechaValor() +
                    " getIdDireccion " + liq.getIdDireccion() +
                    " getIdLiquidacion " + liq.getIdLiquidacion() +
                    " getIdPersonaCliente " + liq.getIdPersonaCliente() +
                    " getIdPersonaPromotor " + liq.getIdPersonaPromotor() +
                    " getNumeroOrden " + liq.getNumeroOrden() +
                    " getStatus " + liq.getStatus() +
                    " getStatusMensajeria " + liq.getStatusMensajeria() +
                    " getFeDirMsj " + liq.getFeDirMsj() +
                    " isCompra: " + liq.isCompra() +
                    " isLiquidacionAnticipada: " + liq.isLiquidacionAnticipada() +
                    " isTomaEnFirme: " + liq.isTomaEnFirme());
        }
        return liq;
    }

    /**
     * Regresa el id de la direcci&oacute;n fiscal del cliente, o si es null, el id de la
     * direcci&oacute;n de mensajer&iacute;a del deal, o null si ninguno de ellos est&aacute;
     * presente en el deal.
     *
     * @param deal El deal a revisar.
     * @return Integer.
     */
    private Integer getIdDireccionCliente(Deal deal) {
        return deal.getDirFactura() != null ? deal.getDirFactura().getIdDireccion() :
                deal.getDireccion() != null ? deal.getDireccion().getIdDireccion() : null;
    }

    /**
     * Elimina los '|' de las instrucciones.
     *
     * @param instrucciones Las instrucciones a revisar.
     * @return String.
     */
    private String quitarPipes(String instrucciones) {
        if (instrucciones != null) {
            return instrucciones.replaceAll("\\|", " ");
        }
        return instrucciones;
    }

    /**
     * @param det El detalle del deal.
     * @param fpl La FormaPagoLiq.
     * @return DetalleLiquidacion.
     * @see com.ixe.ods.sica.services.DealService#crearDetalleLiquidacion(
            com.ixe.ods.sica.model.DealDetalle, com.ixe.treasury.dom.common.FormaPagoLiq).
     */
    public DetalleLiquidacion crearDetalleLiquidacion(DealDetalle det, FormaPagoLiq fpl) {
        DetalleLiquidacion detLiq = new DetalleLiquidacion();
        
        detLiq.setIdMatriz(fpl.getIdMatriz());
        detLiq.setMnemonico(det.getMnemonico());
        detLiq.setFolioDetalle(det.getFolioDetalle());
        detLiq.setIdDetalleExt(new Long(det.getIdDealPosicion()));
        detLiq.setMontoDivisa(new BigDecimal(det.getMonto()));
        detLiq.setMontoMonedaNacional(new BigDecimal(det.getImporte()));
        detLiq.setTipoCambio(new Double(det.getTipoCambio()));
        detLiq.setUsaMensajeria(Boolean.valueOf(det.getDeal().isMensajeria()));
        detLiq.setComision(new Double(det.getComisionCobradaMxn()));
        detLiq.setSustituyeA(det.getSustituyeA() == null ?
                null : new Long(det.getSustituyeA().intValue()));
        detLiq.setObservaciones(det.getDeal().getObservaciones());
        // datos en general:
        detLiq.setClaveBancoCliente(String.valueOf(fpl.getIdBanco()));
        detLiq.setTipoClaveBancoCliente("BUP");
        IPlantilla plt = det.getPlantilla();
        if (plt != null) {
            detLiq.setIdBeneficiario(new Long(plt.getBeneficiario().getIdPersona().intValue()));
            if (plt instanceof IPlantillaIntl) {
                detLiq.setNumeroCuentaCliente(((IPlantillaIntl) plt).getNoCuentaBeneficiario());
                detLiq.setInstruccionesBeneficiario(
                        quitarPipes(det.getInstruccionesBeneficiario()));
                detLiq.setInstruccionesBancoBeneficiario(
                        quitarPipes(det.getInstruccionesPagador()));
                detLiq.setInstruccionesBancoIntermediario(
                        quitarPipes(det.getInstruccionesIntermediario()));
                detLiq.setClaveBancoCliente(((IPlantillaIntl) plt).getClaveBanco());
                detLiq.setTipoClaveBancoCliente(((IPlantillaIntl) plt).getTipoCuentaBanco());
                detLiq.setCuentaBancoInterm(((IPlantillaIntl) plt).getCuentaBancoInterm());
                detLiq.setClaveBancoIntermediario(((IPlantillaIntl) plt).getClaveBancoInterm());
                detLiq.setTipoClaveBancoIntermediario(
                        ((IPlantillaIntl) plt).getTipoCuentaBancoInterm());
                if (((IPlantillaIntl) plt).isTipoCuentaBancoDD()) {
                	detLiq.setNombreBancoInternacional(((IPlantillaIntl) plt).getNombreBanco());
                }
            }
            else if (plt instanceof IPlantillaCuentaIxe) {
                detLiq.setNumeroCuentaCliente(((IPlantillaCuentaIxe) plt).getNoCuentaIxe());
            }
            else if (plt instanceof IPlantillaTranNacional) {
                detLiq.setNumeroCuentaCliente(((IPlantillaTranNacional) plt).getClabe());
                detLiq.setClaveBancoCliente(
                        String.valueOf(((IPlantillaTranNacional) plt).getIdBanco()));
            }
        }
        else {
            detLiq.setIdBeneficiario(
                    new Long(det.getDeal().getCliente().getIdPersona().intValue()));
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("****El detalle de liquidacion a enviar tiene: \n" +
                    " claveBancoCliente: " + detLiq.getClaveBancoCliente() +
                    " getClaveBancoIntermediario: " + detLiq.getClaveBancoIntermediario() +
                    " getComision: " + detLiq.getComision() +
                    " getFolioFacturaComprobante: " + detLiq.getFolioFacturaComprobante() +
                    " getIdBeneficiario: " + detLiq.getIdBeneficiario() +
                    " getIdDetalleLiquidacion: " + detLiq.getIdDetalleLiquidacion() +
                    " getIdMatriz: " + detLiq.getIdMatriz() +
                    " getInstruccionesBancoBeneficiario: " +
                    detLiq.getInstruccionesBancoBeneficiario() +
                    " getInstruccionesBancoIntermediario: " +
                    detLiq.getInstruccionesBancoIntermediario() +
                    " getInstruccionesBeneficiario: " +
                    detLiq.getInstruccionesBeneficiario() +
                    " getMnemonico: " + detLiq.getMnemonico() +
                    " getMontoDivisa: " + detLiq.getMontoDivisa() +
                    " getMontoMonedaNacional:" + detLiq.getMontoMonedaNacional() +
                    " getNumeroCuentaCliente:" + detLiq.getNumeroCuentaCliente() +
                    " getNumeroDocumentoCliente: " + detLiq.getNumeroDocumentoCliente() +
                    " getObservaciones: " + detLiq.getObservaciones() +
                    " getStatus: " + detLiq.getStatus() +
                    " getTipoCambio: " + detLiq.getTipoCambio() +
                    " getTipoClaveBancoCliente: " + detLiq.getTipoClaveBancoCliente() +
                    " getTipoClaveBancoIntermediario: " + detLiq.getTipoClaveBancoIntermediario() +
                    " isUsaMensajeria: " + detLiq.isUsaMensajeria());
        }
        return detLiq;
    }

    /**
     * @see com.ixe.ods.sica.services.DealService#isLiquidadoPorSite(com.ixe.ods.sica.model.Deal).
     */
    public boolean isLiquidadoPorSite(Deal deal) {
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle detalle = (DealDetalle) it.next();
            if (isLiquidadoPorSite(detalle)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see com.ixe.ods.sica.services.DealService#isLiquidadoPorSite(
            com.ixe.ods.sica.model.DealDetalle).
     */
    public boolean isLiquidadoPorSite(DealDetalle det) {
        if (det.isEvento(DealDetalle.EV_IND_GRAL_REVERSO_NO_LIQUIDAR,
                new String[]{Deal.EV_APROBACION})) {
            return false;
        }
        if (det.getDeal().isDeSucursal()) {
            return Constantes.TRANSFERENCIA.equals(det.getClaveFormaLiquidacion()) &&
                    !DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal()) &&
                    det.getMnemonico() != null && det.getMnemonico().indexOf("BALNETEO") < 0;
        }
        else {
            return !DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal()) &&
                    det.getMnemonico() != null && det.getMnemonico().indexOf("BALNETEO") < 0;
        }
    }

    /**
     * @see com.ixe.ods.sica.services.DealService#isValidoBalanceoRec(com.ixe.ods.sica.model.Deal).
     */
    public boolean isValidoBalanceoRec(Deal deal) {
        if (!deal.isSimple()) {
            return true;
        }
        double balance = deal.getBalance();
        for (Iterator it = deal.getEntregamos().iterator(); it.hasNext(); ) {
            DealDetalle det = (DealDetalle) it.next();
            if (null != det.getClaveFormaLiquidacion() && balance < 0.0) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see com.ixe.ods.sica.services.DealService#isValidoBalanceoEnt(com.ixe.ods.sica.model.Deal).
     */
    public boolean isValidoBalanceoEnt(Deal deal) {
        if (!deal.isSimple()) {
            return true;
        }
        double balance = deal.getBalance();
        for (Iterator it = deal.getRecibimos().iterator(); it.hasNext(); ) {
            DealDetalle det = (DealDetalle) it.next();
            if (null != det.getClaveFormaLiquidacion() && balance > 0.0) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see com.ixe.ods.sica.services.DealService#getDetallesDivisas(com.ixe.ods.sica.model.Deal).
     */
    public Map getDetallesDivisas(Deal deal) {
        Map divisasDetalles = new HashMap();
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (!Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
                if (!divisasDetalles.containsKey(det.getDivisa().getIdDivisa())) {
                    List detalles = new ArrayList();
                    detalles.add(det);
                    divisasDetalles.put(det.getDivisa().getIdDivisa(), detalles);
                }
                else {
                    ((List) divisasDetalles.get(det.getDivisa().getIdDivisa())).add(det);
                }
            }
        }
        return divisasDetalles;
    }

    /**
     * Regresa un string con todas las claves de liquidaci&oacute;n encontradas en los detalles.
     *
     * @param deal El deal a inspeccionar.
     * @return String.
     */
    public String getClavesFormasLiquidacion(Deal deal) {
        StringBuffer html = new StringBuffer("<table border=\"0\" cellspacing=\"1\" " +
                "cellpadding=\"2\" width=\"100%\" class=\"portletbackground\">");
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (det.getClaveFormaLiquidacion() != null) {
                if ((Deal.STATUS_DEAL_CANCELADO.equals(deal.getStatusDeal()) &&
                        DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal())) ||
                        !DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal())) {
                    html.append("<tr class=\"normalcell\">");
                    html.append("<td width='1%'>").append(det.getClaveFormaLiquidacion()).
                            append("</td>");
                    html.append("<td width='1%'>").append(det.isRecibimos() ? "REC" : "ENT").
                            append("</td>");
                    html.append("<td align=\"right\">").
                            append(Constantes.MONEY_FORMAT.format(det.getMonto())).append("</td>");
                    html.append("<td width='1%'>").append(det.getDivisa().getIdDivisa()).
                            append("</td>");
                    html.append("</tr>");
                }
            }
        }
        html.append("</table>");
        return html.toString();
    }

    /**
     * Regresa la sumatoria de la diferencia entre el tipo de cambio de la mesa
     * y el tipo de cambio del cliente, multiplicada para el monto, de todos los
     * detalles que no est&aacute;n en Pesos Mexicanos.
     *
     * @param deal El deal a inspeccionar.
     * @return double.
     */
    public double getUtilidadPromotor(Deal deal) {
        double utilidad = 0.0;
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext(); ) {
            DealDetalle det = (DealDetalle) it.next();
            if (!Divisa.PESO.equals(det.getDivisa().getIdDivisa()) &&
                    !DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal())) {
                utilidad += det.getMonto() *
                        (det.isRecibimos() ? det.getTipoCambioMesa() - det.getTipoCambio() :
                                det.getTipoCambio() - det.getTipoCambioMesa());
            }
        }
        return utilidad;
    }

    /**
     * Regresa las formaPagoLiq para el mnem&oacute;nico especificado.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param mnemonico El mnem&oacute;nico a encontrar.
     * @return FormaPagoLiq.
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getFormaPagoLiq(String, String).
     */
    public FormaPagoLiq getFormaPagoLiq(String ticket, String mnemonico) {
        return getFormasPagoLiqService().getFormaPagoLiq(ticket, mnemonico);
    }

    /**
     * Regresa una referencia al bean formasPagoLiqService para realizar operaciones con
     * FormasPagoLiq.
     *
     * @return FormasPagoLiqService.
     */
    public FormasPagoLiqService getFormasPagoLiqService() {
        return formasPagoLiqService;
    }

    /**
     * Establece el valor de la variable formasPagoLiqService.
     *
     * @param formasPagoLiqService El valor a asignar.
     */
    public void setFormasPagoLiqService(FormasPagoLiqService formasPagoLiqService) {
        this.formasPagoLiqService = formasPagoLiqService;
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
     * Regresa el valor de valorFuturoService.
     *
     * @return ValorFuturoService.
     */
    public ValorFuturoService getValorFuturoService() {
        return valorFuturoService;
    }

    /**
     * Establece el valor de valorFuturoService.
     *
     * @param valorFuturoService El valor a asignar.
     */
    public void setValorFuturoService(ValorFuturoService valorFuturoService) {
        this.valorFuturoService = valorFuturoService;
    }

    /**
     * Referencia al SicaServiceData para poder realizar operaciones a la base de datos.
     */
    private SicaServiceData sicaServiceData;

    /**
     * El servicio de formas de pago y liquidacion.
     */
    private FormasPagoLiqService formasPagoLiqService;

    /**
     * El servicio de informaci&oacute;n del pizarr&oacute;n.
     */
    private PizarronServiceData pizarronServiceData;

    /**
     * El servicio para conocer si hay o no valor futuro.
     */
    private ValorFuturoService valorFuturoService;

    /**
     * El objeto para logging.
     */
    private static final transient Log LOGGER = LogFactory.getLog(DealServiceImpl.class);
}

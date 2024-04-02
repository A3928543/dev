/*
 * $Id: HibernateContabilidadSicaServiceData.java,v 1.61.22.2.52.1 2020/12/01 22:10:42 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.orm.hibernate.HibernateCallback;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.DateType;
import net.sf.hibernate.type.IntegerType;

import com.ixe.ods.bup.model.TipoIva;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.seguridad.services.LoginService;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaProperties;
import com.ixe.ods.sica.dao.ContabilidadDao;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.DealDetalleStatusLog;
import com.ixe.ods.sica.model.DealStatusLog;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.GuiaContableAsiento;
import com.ixe.ods.sica.model.GuiaContableLiq;
import com.ixe.ods.sica.model.HistoricoPosicion;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.model.LogExpresionContable;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.MovimientoContable;
import com.ixe.ods.sica.model.MovimientoContableDetalle;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.Poliza;
import com.ixe.ods.sica.model.Posicion;
import com.ixe.ods.sica.model.PosicionLog;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.RecoUtilidad;
import com.ixe.ods.sica.model.UtilidadForward;
import com.ixe.ods.sica.sdo.ContabilidadSicaServiceData;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.dto.GuiaContableDto;
import com.ixe.ods.sica.services.GeneralMailSender;
import com.ixe.ods.sica.services.impl.XlsServiceImpl;
import com.ixe.ods.sica.services.model.ModeloXls;
import com.ixe.ods.sica.utils.BDUtils;
import com.ixe.ods.sica.utils.DateUtils;

/**
 * <p>Subclase de AbstractHibernateSicaServiceData para realizar la Contabilidad del SICA.</p>
 *
 * @author Edgar I. Leija, Israel Rebollar
 * @version $Revision: 1.61.22.2.52.1 $ $Date: 2020/12/01 22:10:42 $
 */
public class HibernateContabilidadSicaServiceData extends AbstractHibernateSicaServiceData
        implements ContabilidadSicaServiceData {

    /**
     * Constructor vac&iacute;o.
     */
    public HibernateContabilidadSicaServiceData() {
        super();
    }

    /**
     * Regresa el Precio Referencia a partir de un idPrecioReferencia.
     *
     * @deprecated Se debe utilizar el valor directo del precio referencia.
     * @param idPrecioReferencia El precio de referencia a consultar.
     * @return precio Referencia
     */
    private PrecioReferenciaActual findPreReferencia(int idPrecioReferencia) {
        if (idPrecioReferencia == 0) {
            return precioReferenciaActual;
        }
        for (Iterator it = preciosReferencia.iterator(); it.hasNext();) {
            PrecioReferenciaActual precioReferencia = (PrecioReferenciaActual) it.next();
            if (idPrecioReferencia == precioReferencia.getIdPrecioReferencia()) {
                return precioReferencia;
            }
        }
        List prs = getHibernateTemplate().find("FROM PrecioReferenciaActual " +
                "AS pra WHERE pra.idPrecioReferencia = ?", new Integer(idPrecioReferencia));
        if (prs.isEmpty()) {
            return findPrecioReferenciaActual();
        }
        preciosReferencia.add(prs.get(0));
        return (PrecioReferenciaActual) prs.get(0);
    }
    
    /**
     * Regresa el valor del Precio Referencia Mid Spot utilizado en la captura del
     * detalle o el actual en caso de no tener uno
     *
     * @param DealDetale El detalle del cual se desea optener el valor del Precio Referencia.
     * @return El valor del Precio Referencia Mid Spot.
     */
    private double getPrecioReferenciaMidSpot(DealDetalle detalle) {
	    return detalle.getPrecioReferenciaMidSpot() == null ?
	    		precioReferenciaActual.getPreRef().getMidSpot().doubleValue() : 
	    			detalle.getPrecioReferenciaMidSpot().doubleValue();
    }

    /**
     * Seleccion a todos los deals que han sido cancelados hoy y que su fecha valor es diferente a
     * Cash.
     */
    private void seleccionCanceladosHoy() {
        List deals = getHibernateTemplate().findByNamedQuery("findStatusCanceladoByFecha",
                new Object[]{getFechaHoy12(), getFechaHoy()});
        List tmp = new ArrayList();
        Calendar fechaValorHoy = new GregorianCalendar();
        fechaValorHoy.setTime(getFechaHoy().getTime());
        fechaValorHoy.setTime(DateUtils.inicioDia(fechaValorHoy.getTime()));
        Calendar fechaCaptura = new GregorianCalendar();
        for (Iterator iterator = deals.iterator(); iterator.hasNext();) {
            DealStatusLog ds = (DealStatusLog) iterator.next();
            fechaCaptura.setTime(ds.getDeal().getFechaCaptura());
            fechaCaptura.setTime(DateUtils.inicioDia(fechaCaptura.getTime()));
            if (!Constantes.CASH.equals(ds.getDeal().getTipoValor().trim()) &&
                    (!fechaCaptura.getTime().equals(fechaValorHoy.getTime()))) {
                for (Iterator iterator1 = ds.getDeal().getDetalles().iterator();
                     iterator1.hasNext();) {
                    DealDetalle det = (DealDetalle) iterator1.next();
                    if (!Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
                        tmp.add(det);
                    }
                }
            }
        }
        crearElementosReverso(sortDealDetallesByPesos(tmp));
    }

    /**
     * Checa la condici&oacute;on de que el deal no pertenezca al Ixe Forwards.
     *
     * @param deal La operaci&oacute;n a revisar.
     * @return true si el deal no pertenece a Ixe Forwards.
     */
    private boolean noPerteneceAIxeForwards(Deal deal) {
        String contratoSicaIxeForward = parametroIxeForwards.getValor();
        if (deal.getContratoSica() != null) {
            if (!deal.getContratoSica().getNoCuenta().equals(contratoSicaIxeForward)) {
                return !deal.isDeSucursal();
            }
        }
        return false;
    }


    /**
     * Checa la condici&oacute;on de que el deal no pertenezca al Ixe Coberturas.
     *
     * @param deal El deal a revisar.
     * @return true si el deal no pertenece a Ixe Forwards.
     */
    private boolean perteneceAIxeCoberturas(Deal deal) {
        String contratoSicaIxeCoberturas = parametroCoberturas.getValor();
        if (deal.getContratoSica() != null) {
            if (deal.getContratoSica().getNoCuenta().equals(contratoSicaIxeCoberturas)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Regresa true si el deal tiene algun detalle no cancelado que no sea pesos en compra y venta.
     *
     * @param det El detalle de deal a revisar.
     * @return boolean.
     */
    private boolean isNeteoCancelado(DealDetalle det) {
        boolean neteo = false;
        boolean neteoRecibimos = false;
        for (Iterator it = det.getDeal().getDetalles().iterator(); it.hasNext();) {
            DealDetalle dt = (DealDetalle) it.next();
            if (!Divisa.PESO.equals(dt.getDivisa().getIdDivisa()) && dt.isRecibimos()) {
                if (neteo) {
                    return true;
                }
                else {
                    neteoRecibimos = true;
                }
            }
            if (!Divisa.PESO.equals(dt.getDivisa().getIdDivisa()) && !dt.isRecibimos()) {
                if (neteoRecibimos) {
                    return true;
                }
                else {
                    neteo = true;
                }
            }
        }
        return false;
    }

    /**
     * Crea los movimientos Contables para la fase de pactaci&oacute;n.
     *
     * @param dets lista de detalles correspondientes a &eacute;sta fase contable
     */
    private void crearElementosReverso(List dets) {
        for (Iterator it = dets.iterator(); it.hasNext();) {
            MovimientoContable mov = new MovimientoContable();
            DealDetalle det = (DealDetalle) it.next();
            mov.setIdDeal(det.getDeal().getIdDeal());
            mov.setFolioDetalle(det.getFolioDetalle());
            mov.setCliente("");
            mov.setFechaValor(det.getDeal().getFechaCaptura());
            mov.setDivisa(det.getDivisa());
            mov.setFaseContable(FASE_PACTACION);
            if (Constantes.HR72.equals(det.getDeal().getTipoValor())) {
                mov.setTipoFechaValor(Constantes.VFUT);
            }
            else {
                mov.setTipoFechaValor(det.getDeal().getTipoValor().trim());
            }
            mov.setMnemonico(det.getMnemonico());
            if (isNeteoCancelado(det)) {
                mov.setTipoDeal(NETEO);
            }
            else {
                if (det.getDeal().isDeSucursal() || perteneceAIxeCoberturas(det.getDeal())) {
                    mov.setTipoDeal(SUCURSAL);
                }
                else if (det.getDeal().isInterbancario()) {
                    mov.setTipoDeal(INTERBANCARIO);
                }
                else {
                    mov.setTipoDeal(NORMAL);
                }
            }
            mov.setTipoOperacion(isNeteoCancelado(det) ||
                    Divisa.PESO.equals(det.getDivisa().getIdDivisa()) ?
                    det.isRecibimos() ? VENTA : COMPRA : det.getDeal().isCompra() ? VENTA : COMPRA);
            mov.setUsuario(det.getDeal().getUsuario().getIdUsuario());
            mov.setSucursalOrigen(det.getDeal().getCanalMesa().getCanal().getIdCanal());
            mov.setStatusActual(AUTORIZADA);
            mov.setStatusAnterior(PENDIENTE);
            mov.setTipoCambio(det.getTipoCambio());
            mov.setLiquidacionEspecial(det.getDeal().getLiquidacionEspecial());
            mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, det.getDivisa(),
                    IMPORTE_DIV, det.getMonto()));
            mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                    IMPORTE_PESOS, det.getTipoCambio() * det.getMonto()));
            double monto = det.isRecibimos() ?
                    (det.getTipoCambioMesa() - det.getTipoCambio()) * det.getMonto() :
                    (det.getTipoCambio() - det.getTipoCambioMesa()) * det.getMonto();
            if (monto != 0) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                        monto < 0 ? IMPORTE_PERDIDAS : IMPORTE_UTILIDADES, monto));
            }
            mov.setStatusProceso(STATUS_NO_PROCESADO);
            mov.setFechaCreacion(getFechaHoy().getTime());
            storeMovimiento(mov, det);
            for (Iterator it1 = mov.getMovimientoContableDetalles().iterator(); it1.hasNext();) {
                MovimientoContableDetalle movDet = (MovimientoContableDetalle) it1.next();
                store(movDet);
            }
        }
    }

    /**
     * Dependiendo el canal al que pertenece el Deal, se calcula su Iva.
     *
     * @param canal <code>String</code> identificador del canal al que pertenece el deal.
     * @return double regresa el valor del Iva, .15 por default
     */
    private double calcularIvaComision(String canal) {
        for (Iterator it1 = getTiposIva().iterator(); it1.hasNext();) {
            TipoIva tipoIva = (TipoIva) it1.next();
            if (canal.equals(tipoIva.getClaveTipoIva())) {
                return 1 + (tipoIva.getPorcIva() / Num.I_100);
            }
        }
        return Num.D_1_15 / Num.I_100;
    }

    /**
     * @see ContabilidadSicaServiceData#generacionMovimientosContables() throws Exception
     */
    public void generacionMovimientosContables() {
        dealsConReferencia = new HashMap();
        parametroCoberturas = (ParametroSica) find(ParametroSica.class,
                ParametroSica.CONTRATO_SICA_IXE_COBERTURAS);
        parametroIxeForwards = (ParametroSica) find(ParametroSica.class,
                ParametroSica.CONTRATO_SICA_IXE_FORWARD);
        ParametroSica parametroModoGeneracionConta = (ParametroSica) find(ParametroSica.class,
                               ParametroSica.MODO_GEN_CONTA);
        modoSap = parametroModoGeneracionConta.getValor().trim().equals(MODO_GEN_CONTA_SAP);
        fechaSistema = isReprocesoSAP() ? fechaSistema : getFechaSistema();
        fechaProcesoContable = fechaSistema;
        GregorianCalendar cincoDiasAtras = new GregorianCalendar();
        cincoDiasAtras.setTime(fechaSistema);
        cincoDiasAtras.add(Calendar.DAY_OF_MONTH, - Num.I_5);
        
        //Esta lista de precios referencia ya no se llena
        //debido a que se utiliza el valor almacenado en 
        //cada detalle
        preciosReferencia = new ArrayList();
        precioReferenciaActual = findPrecioReferenciaActual();
        getLogExpresionContable().limpiarLogContabilidad();
        warn("*ParametroCoberturas: " + parametroCoberturas.getValor());
        warn("*ParametroIxeForwards: " + parametroIxeForwards.getValor());
        warn("*ParametroModoGeneracionContabilidad: " + parametroModoGeneracionConta.getValor());
        warn("*Fecha del Sistema: " + fechaSistema);
        warn("*Modo Sap: " + modoSap);
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:ms");
        Date fechaInicio = new Date();
        List dealParcialmenteLiquidados;
        // TODO: descomentar despues
        //verificarDealLiquidadosSicaVsSite(getFechaSistema());
        for (Iterator it = findAll(MesaCambio.class).iterator(); it.hasNext();) {
            MesaCambio mc = (MesaCambio) it.next();
            generarUtilidadesIxeForwards(mc.getIdMesaCambio(), fechaSistema);
            warn("*Utilidades Ixe Forwards generadas para la mesa ..." + mc.getIdMesaCambio(),
                    null);
        }
        setGuiasContablesAsientos(getHibernateTemplate().find(
                "FROM GuiaContableAsiento AS gca WHERE gca.tipoSap = ?", modoSap ? SI : NO));
        setGuiasContablesLiquidacion(getHibernateTemplate().find("FROM GuiaContableLiq AS gcl " +
                "WHERE gcl.tipoSap = ?", modoSap ? SI : NO));
        setTiposIva(getHibernateTemplate().find("FROM com.ixe.ods.bup.model.TipoIva"));
        warn("Inicio " + DATE_FORMAT.format(fechaInicio));
        warn("Comenzando Generacion de Movimientos Contables");
        warn("Revisando Deals Cancelados");
        seleccionCanceladosHoy();
        warn("Terminando Revision Deals Cancelados");
        //Fase de Pactacion
        warn("Comenzando Pactacion");
        seleccionPactacionHoy();
        warn("Terminando Pactacion");
        //Fase de Utilidades
        warn("Comenzando Utilidades Globales");
        // Extraemos SC_RECO_UTILIDAD:
        List faseUtilidad = getHibernateTemplate().findByNamedQuery("findUtilidadCierre",
                new Object[]{getFechaHoy12(), getFechaHoy()});
        seleccionUtilidades(faseUtilidad);
        warn("Terminando Utilidades Globales");
        //Fase Liquidacion
        Set set = new HashSet(getHibernateTemplate().
                findByNamedQuery("findStatusLogByStatusNuevoAndFecha",
                new Object[]{getFechaHoy12(), getFechaHoy()}));
        List dets = new ArrayList(set);
        List detsL4 = new ArrayList(set);
        //Filtra Deals InterBancarios Ixe Forwards
        List detsStatusLog = new ArrayList();
        List detsStatusLogDespuesPact = new ArrayList();
        for (Iterator iterator1 = dets.iterator(); iterator1.hasNext();) {
            DealDetalleStatusLog dsl = (DealDetalleStatusLog) iterator1.next();
            if (noPerteneceAIxeForwards(dsl.getDealDetalle().getDeal()) &&
                    dsl.getDealDetalle().getDeal().getLiquidacionAnticipada().equals("0")) {
                detsStatusLog.add(dsl);
            }
        }
        for (Iterator iterator1 = dets.iterator(); iterator1.hasNext();) {
            DealDetalleStatusLog dsl = (DealDetalleStatusLog) iterator1.next();
            if (noPerteneceAIxeForwards(dsl.getDealDetalle().getDeal()) &&
                    dsl.getDealDetalle().getDeal().getLiquidacionAnticipada().equals("0")) {
                detsStatusLogDespuesPact.add(dsl);
            }
        }
        //
        //****************Cambio se movio L3
        warn("Comenzando Fase L3");
        dealParcialmenteLiquidados = seleccionLiquidacionDiaProceso(detsStatusLog);
        warn("Terminando Fase L3");
        //***************
        if (detsStatusLog.size() > 0) {
            warn("Comenzando Fase L2A");
            seleccionLiquidacionHoy(detsStatusLog);
            warn("Terminando Fase L2A");
        }
        warn("Comenzando Fase L2B");
        liquidacionParcialDealsPorSplitGoma();
        seleccionNoHuboLiquidacion();
        //******************L2B de Liquidaciones Anticipadas
        //******************
        warn("Terminando Fase L2B");
        if (detsStatusLog.size() > 0) {
            warn("Comenzando Fase L4");
            seleccionLiquidacionDespuesPactado(detsStatusLog);
        }
        seleccionLiquidacionDespuesPactadoDealsLiquidacionAnt(detsL4);
        warn("Terminando Fase L4");
        warn("Terminando Generacion de Movimientos Contables");
        warn("Comenzando Explosion Contable");
        warn("Generando Polizas de Deals");
        generacionExplosionContable();
        warn("Terminando Polizas de Deals");
        warn("Generando Polizas de Utilidades Globales");
        generacionExplosionContableUtilidades();
        setGuiasContablesAsientos(null);
        setGuiasContablesLiquidacion(null);
        for (Iterator iterator = dealParcialmenteLiquidados.iterator(); iterator.hasNext();) {
            DealDetalleStatusLog ds = (DealDetalleStatusLog) iterator.next();
            ds.getDealDetalle().getDeal().setLiquidacionAnticipada("1");
            update(ds.getDealDetalle().getDeal());
        }
        warn("Terminando Polizas de Utilidades Globales");
        warn("Terminando Explosion Contable");
        getHibernateTemplate().flush();
        List descuadres = filtrarDescuadres(fechaSistema);
        getHibernateTemplate().flush();
        validaFolioDetalleDuplicado(fechaSistema);
        if (modoSap && !isReprocesoSAP()) {
            warn("Iniciando Registros contables SAP");
            generacionRegistrosContablesSAP(getFechaSistema(), fechaProcesoContable);
            enviarPorCorreoPolizasDescuadradas(descuadres);
        }
        else if (modoSap && isReprocesoSAP()) {
            warn("Reprocesando registros SAP");
            reprocesaGeneracionRegistrosContablesSAP(getFechaSistema(), fechaProcesoContable);
            enviarPorCorreoPolizasDescuadradas(descuadres);
        }
        //Se genera el archivo CC (Cierre Contable) que SAP consumira
        generaArchivoGestionDeSap("CC");
        warn("Termina Registros Contables SAP ");
        warn("Fin  ***");
    }

    /**
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param asientos La lista de objetos com.ixe.ods.sica.model.Poliza a insertar.
     * @param ip La direcci&oacute;n IP del usuario que realiza la operaci&oacute;n.
     * @param us El usuario que realiza la operaci&oacute;n.
     * @see com.ixe.ods.sica.sdo.ContabilidadSicaServiceData#insertarAsientosManualesSap(String,
     *              java.util.List, String, com.ixe.ods.seguridad.model.IUsuario).
     */
    public void insertarAsientosManualesSap(String ticket, List asientos, String ip, IUsuario us) {
        dealsConReferencia = new HashMap();
        if (asientos == null || asientos.isEmpty()) {
            throw new SicaException("La lista de asientos est\u00e1 vac\u00ed.");
        }
        List divisas = findAll(Divisa.class);
        List canales = findAll(Canal.class);
        List guiasAsiento = getHibernateTemplate().find("FROM GuiaContableAsiento AS gca WHERE " +
                "gca.tipoSap = ?", SI);
        List guiasLiq = getHibernateTemplate().find("FROM GuiaContableLiq AS gcl WHERE " +
                "gcl.tipoSap = ?", SI);
        double cargos = 0;
        double abonos = 0;
        for (Iterator it = asientos.iterator(); it.hasNext();) {
            Poliza poliza = (Poliza) it.next();

            validarCuentaContableSap(guiasAsiento, guiasLiq, poliza.getCuentaContable(),
                    poliza.getDivisa().getIdDivisa());
            validarDivisaSap(divisas, poliza.getDivisa().getIdDivisa());
            validarCanalSap(canales, poliza.getSucursalOrigen());
            if (CARGO.equals(poliza.getCargoAbono())) {
                cargos += poliza.getImporte();
            }
            else if (ABONO.equals(poliza.getCargoAbono())) {
                abonos += poliza.getImporte();
            }
            else {
                throw new SicaException("Se esperaba recibir el valor 'CARGO' o 'ABONO', en " +
                        "lugar de '" + poliza.getCargoAbono() + "'.");
            }
            // Validamos que el deal exista:
            if (poliza.getIdDeal() > 0) {
                findDeal(poliza.getIdDeal());
            }
            if (poliza.getIdDealPosicion() > 0) {
                validarIdPosicion(poliza.getIdDealPosicion());
            }
        }
        double operacion = cargos - abonos;
        double dif = Math.abs(operacion);
        if (dif >= Num.D_0_01) {
            throw new SicaException("El total de cargos es distinto al de abonos.");
        }
        List paraCvesReferencia = new ArrayList();
        // Se escriben los asientos en la tabla SC_POLIZA:
        Date fechaExcel = null;
        logger.debug("Auditoria");
        for (Iterator it = asientos.iterator(); it.hasNext();) {
            Poliza p = (Poliza) it.next();

            if (fechaExcel != null &&
                    !fechaExcel.equals(DateUtils.inicioDia(p.getFechaCreacion()))) {
                throw new SicaException("No concuerdan las fechas de creaci\u00f3n.");
            }
            if (fechaExcel != null &&
                !fechaExcel.equals(DateUtils.inicioDia(p.getFechaValor()))) {
                throw new SicaException("No concuerdan las fechas valor.");
            }
            fechaExcel = DateUtils.inicioDia(p.getFechaCreacion());
            storePoliza(p);
            HashMap map = new HashMap();
            map.put(TIPO, "pzm");
            map.put(POLIZA, p);
            paraCvesReferencia.add(map);
            auditar(ticket, ip, null, us, LogAuditoria.GEN_MANUAL_POLIZAS,
                    "idPoliza: " + p.getIdPoliza(), "INFO", "E");
        }
        getHibernateTemplate().flush();
        warn("Iniciando Registros contables SAP");
        getContabilidadDao().generarRegistosContablesSAP(asientos, getFechaSistema(), fechaExcel,
                getContratosLiquidacionesEspeciales());
        warn("Termina Registros Contables SAP ");
        logger.debug("Fin");
    }

    /**
     * @param fecha La fecha de reprocesamiento.
     * @see ContabilidadSicaServiceData#reprocesoGeneracionMovimientosContables(Date).
     */
    public void reprocesoGeneracionMovimientosContables(Date fecha) {
        borrarPolizasExistentes(fecha);
        fechaSistema = fecha;
        setReprocesoSAP(true);
        generacionMovimientosContables();
        setReprocesoSAP(false);
    }

    /**
     * Obtiene los deals que por alguna raz&oacute; cuentan con
     * folios de detalles repetidos y generan descuadres.
     *
     * @param fechaSistema La fecha a procesar.
     */
    private void validaFolioDetalleDuplicado(Date fechaSistema) {
        StringBuffer strBf = new StringBuffer();
        List dealsFolioDuplicado = getContabilidadDao().
                obtenerDealsCuentanDetallesDuplicados(fechaSistema);
        if (!dealsFolioDuplicado.isEmpty()) {
            for (Iterator iterator = dealsFolioDuplicado.iterator(); iterator.hasNext();) {
                Map registro = (Map) iterator.next();
                strBf.append("El deal ").append(registro.get("ID_DEAL")).
                        append("cuenta con detalles duplicados <Detalle : ").
                        append(registro.get("FOLIO_DETALLE")).append(">\n");
            }
            strBf.append("\n").append("No se puede continuar con la generaci\u00f3n contable, ").
                    append("\n").
	    		append("favor de contactar al administrador del sitema.");
	    	throw new SicaException(strBf.toString());
    	}
    }

    /**
     * Filtra los registros de SC_POLIZA que presentan descuadres.
     *
     * @param fechaSistema La fecha a procesar.
     * @return List de Poliza.
     */
    private List filtrarDescuadres(Date fechaSistema) {
    	List polizasBorrar = new ArrayList();
        List pols = getHibernateTemplate().find("FROM Poliza AS p WHERE p.fechaCreacion " +
                "BETWEEN ? AND ?",
                new Object[]{DateUtils.inicioDia(fechaSistema), DateUtils.finDia(fechaSistema)});
        Map cargosAbonosDeals = new HashMap();
        for (Iterator it = pols.iterator(); it.hasNext();) {
            Poliza p = (Poliza) it.next();
            BigDecimal v = (BigDecimal) cargosAbonosDeals.get(new Integer(p.getIdDeal()));

            if (v == null) {
                v = BDUtils.generar2(0);
            }
            if (CARGO.equals(p.getCargoAbono())) {
                v = v.add(BDUtils.generar2(p.getImporte()));
            }
            else {
                v = v.subtract(BDUtils.generar2(p.getImporte()));
            }
            cargosAbonosDeals.put(new Integer(p.getIdDeal()), v);
        }
        List idDeals = new ArrayList();
        for (Iterator it = cargosAbonosDeals.keySet().iterator(); it.hasNext();) {
            Integer idDeal = (Integer) it.next();
            double v = Math.abs(((BigDecimal) cargosAbonosDeals.get(idDeal)).doubleValue());
            if (v >= Num.D_0_01) {
                idDeals.add(idDeal);
            }
        }
        Set idsDeal = new HashSet(idDeals);
        if (!idsDeal.isEmpty()) {
            final Date fSistema = fechaSistema;
            final List idDealsList = new ArrayList(idsDeal);
            final String queryTodosReg = "SELECT pp FROM Poliza AS pp WHERE " +
                    "pp.idDeal IN (:listaIdsDeal) " +
                    "AND TRUNC(pp.fechaCreacion) = TRUNC(:fSistema) ORDER BY pp.idDeal";
            polizasBorrar = getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Query q = session.createQuery(queryTodosReg);
                    q.setParameterList("listaIdsDeal", idDealsList, new IntegerType());
                    q.setParameter("fSistema", fSistema);
                    return q.list();
                }
            });
            for (Iterator iterator = polizasBorrar.iterator(); iterator.hasNext();) {
                Poliza pol = (Poliza) iterator.next();
                getHibernateTemplate().delete(pol);
            }
            getHibernateTemplate().flush();
        }
        return polizasBorrar;
    }

    /**
     * Borra las polizas existente dada una fecha.
     *
     * @param fecha La fecha del d&iacute;a que se quieren borrar las polizas.
     */
    private void borrarPolizasExistentes(Date fecha) {
        warn("\tComienza borrado de p\u0021lizas existentes");
        warn("\t\tBorrando detalles movimientos contables");
        int numReg = getHibernateTemplate().delete("FROM MovimientoContableDetalle AS movContDet " +
                "WHERE movContDet.movimientoContable.idMovimientoContable IN ( " +
                "SELECT movCont.idMovimientoContable FROM MovimientoContable AS movCont WHERE " +
                "TRUNC(movCont.fechaCreacion) = TRUNC(?) " +
                ")", fecha, new DateType());
        warn("\t\tN\u0028mero detalles borrados: " + numReg);
        warn("\t\t\tBorrando movimientos contables");
        numReg = getHibernateTemplate().delete("FROM MovimientoContable AS movCont " +
                "WHERE TRUNC(movCont.fechaCreacion) = TRUNC(?) ", fecha, new DateType());
        warn("\t\t\tN\u0028mero movimientos contables borrados: " + numReg);
        warn("\t\tBorrando p\u0021lizas contables");
        numReg = getHibernateTemplate().delete("FROM Poliza AS pol " +
                "WHERE TRUNC(pol.fechaCreacion) = TRUNC(?)", fecha, new DateType());
        warn("\t\tN\u0028mero de p\u0021 borradas: " + numReg);
        warn("\tTerminando borrado de p\u0021lizas existentes");
    }

    /**
     * Reprocesa los registros contables para SAP.
     *
     * @param fechaSistema La fecha del sistema.
     * @param fechaProceso Fecha seleccionada para generar el proceso.
     */
    private void reprocesaGeneracionRegistrosContablesSAP(Date fechaSistema, Date fechaProceso) {
        contabilidadDao.reprocesarRegistrosContablesSAP(fechaSistema, fechaProceso,
                getContratosLiquidacionesEspeciales());
    }

    /**
     * Revisa la consistencia entre liquidaciones del SITE y del SICA.
     *
     * @param fechaSistema la fecha a revisar.
     */
    private void verificarDealLiquidadosSicaVsSite(Date fechaSistema) {
        contabilidadDao.actualizarEstatusDealsSicaVsSite(fechaSistema);
    }

    /**
     * Regresa la lista de contratos de liquidaciones especiales.
     *
     * @return String.
     */
    private String getContratosLiquidacionesEspeciales() {
        return ((ParametroSica) find(ParametroSica.class, ParametroSica.CONTRATOS_LIQ_ESPECIALES)).
                getValor();
    }

    /**
     * Genera los registros contables para SAP.
     *
     * @param fechaSistema La fecha de sistema.
     * @param fechaProceso Fecha seleccionada para generar el proceso.
     */
    private void generacionRegistrosContablesSAP(Date fechaSistema, Date fechaProceso) {
        contabilidadDao.generarRegistosContablesSAP(fechaSistema, fechaProceso,
                getContratosLiquidacionesEspeciales());
    }

    /**
     * Obtiene los Movimientos Contables para Utilidades
     */
    private void generacionExplosionContableUtilidades() {
        List movs = seleccionMovimientosContablesPorFase(FASE_UTILIDADES);
        List guias;
        for (Iterator iterator = movs.iterator(); iterator.hasNext();) {
            MovimientoContable mov = (MovimientoContable) iterator.next();
            if (mov.getDivisa().isMetalAmonedado()) {
                guias = seleccionGuiasContablesAsientosPactacion(FASE_UTILIDADES,
                        mov.getTipoOperacion().toUpperCase(), mov.getDivisa().getGrupo().trim());
            }
            else {
                guias = seleccionGuiasContablesAsientosPactacion(FASE_UTILIDADES,
                        mov.getTipoOperacion().toUpperCase(), mov.getDivisa().getIdDivisa().trim());
            }
            registrarPolizaUtilidades(mov, guias);
        }
    }

    /**
     * Genera un archivo excel con los campos de los asientos especificados y lo env&iacute;a por
     * correo a las direcciones definidas en el par&aacute;metro EMAIL_CONTABILIDAD.
     *
     * @param polizas El listado de asientos a escribir en el excel.
     */
    private void enviarPorCorreoPolizasDescuadradas(List polizas) {
        ParametroSica p = (ParametroSica) find(ParametroSica.class,
                ParametroSica.EMAIL_CONTABILIDAD);
        GeneralMailSender gms = (GeneralMailSender) _appContext.
                getBean("generalMailSender");
        if (!polizas.isEmpty()) {
            XlsServiceImpl xlsService = new XlsServiceImpl(new ModeloXls("Asientos descuadrados",
                    new String[]{"Fecha Valor", "Cuenta Contable", "Entidad", "Cargo/Abono",
                            "Divisa", "Importe", "Deal", "Referencia", "Fecha de Creaci\u00f3n",
                            "Tipo de Deal", "Sucursal Origen", "Folio de Detalle", "Tipo de Cambio",
                            "Contrato Sica", "Detalle de Deal"},
                    new String[]{"fechaValor", "cuentaContable", "entidad", "cargoAbono",
                            "divisa.idDivisa", "importe", "idDeal", "referencia", "fechaCreacion",
                            "tipoDeal", "sucursalOrigen", "folioDetalle", "tipoCambio",
                            "noContratoSica", "idDealPosicion"}, polizas));
            ByteArrayOutputStream contenido = null;
            FileOutputStream fos = null;
            try {
                contenido = new ByteArrayOutputStream();
                xlsService.escribirXls(contenido);
                byte[] bytes = contenido.toByteArray();
                fos = new FileOutputStream("archivos/sica/" +
                        DATE_FORMAT.format(fechaSistema) + ".xls");
                fos.write(bytes);
                fos.flush();
                gms.enviarMail("ixecambios@ixe.com.mx", p.getValor().split("\\,"), null,
                        "Generaci\u00f3n Contable - Asientos descuadrados", "\n",
                        "AsientosDescuadrados.xls", "application/vnd.ms-excel",
                        bytes);
              //Se genera el archivo DSC (Descuadres) que SAP consumira
                generaArchivoGestionDeSap("DSC");
            }
            catch (Exception e) {
	            warn(e.getMessage(), e);
	        }
	        finally {
	            if (contenido != null) {
	                try {
	                    contenido.close();
	                }
	                catch (IOException e) {
	                    debug(e.getMessage(), e);
	                }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    }
                    catch (IOException e) {
                        debug(e.getMessage(), e);
                    }
                }
            }
        }
        else {
            try {
            gms.enviarMail("ixecambios@ixe.com.mx", p.getValor().split("\\,"), null,
                    "Generaci\u00f3n Contable - Exitosa, no se presentaron descuadres",
                    "Este correo ha sido generado " +
                            "autom\u00e1ticamente por el sistema SICA. No responda ni env\u00ede " +
                            "correo a esta direcci\uf003n por favor.");
        }
            catch (Exception e) {
                warn(e.getMessage(), e);
            }
        }
    }

    /**
     * Regresa un ticket que se obtiene a partir del usuario que se encuentra configurado en
     * sica.properties.
     *
     * @return String.
     * @throws SicaException Si no se pueden obtener las propiedades de conexi&oacute;n.
     */
    private String obtenerTicket() throws SicaException {
        try {
            String pUsr = SicaProperties.getInstance().getSicaUsr();
            String pPwd = SicaProperties.getInstance().getSicaPwd();
            String pSys = SicaProperties.getInstance().getSicaSys();
            LoginService loginService = (LoginService) _appContext.getBean("loginJtaService");
            return loginService.obtieneTicket(pUsr, pSys, pPwd, pUsr, "SICA", "0.0.0.0");
        }
        catch (SeguridadException e) {
            debug(e);
            throw new SicaException(e.getMessage());
        }
    }

    /**
     * Genera un archivo de texto con el nombre de la fecha del sistema en la carpeta
     * sicaDomain/archivos/sica/cierres/ y su respectivo sufijo: FL (Fin de Liquidaciones), DSC
     * (Descuadres), CC (Cierre Contable)
     *
     * @param sufijo Sufijo a utilizarse en el nombre del archivo.
     */
    private void generaArchivoGestionDeSap(String sufijo) {
        FileOutputStream fosArchivoSap = null;
        try {
            fosArchivoSap = new FileOutputStream("archivos/sica/cierres/" +
                    DATE_FORMAT.format(fechaSistema) + "_" + sufijo + ".txt");
            fosArchivoSap.write(DATE_HOUR_FORMAT.format(new Date()).getBytes());
            fosArchivoSap.flush();
            IUsuario usuario = findUsuario();
            if (sufijo.equals("DSC")) {
            	warn("Escritura archivo Descuadres " + new Date());
                auditar(obtenerTicket(), "127.0.0.1", null, usuario,
                    "Escritura archivo Descuadres", null, "WARN", "F");
            }
            else if (sufijo.equals("CC")) {
            	warn("Escritura archivo Cierre Contable " + new Date());
            	auditar(obtenerTicket(), "127.0.0.1", null, usuario,
                        "Escritura archivo Cierre Contable", null, "WARN", "F");
            }
        }
        catch (Exception e) {
            warn(e.getMessage(), e);
        }
        finally {
            if (fosArchivoSap != null) {
                try {
                    fosArchivoSap.close();
                }
                catch (IOException e) {
                    debug(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Recibe un conjunto de movimientos contables con sus guias contables asientos correspondientes
     * las divide en las correspondientes a la Fase de pactaci&oacute;n y el resto (Fase de
     * liquidaci&oacute;n) as&iacute; como en Compras y Ventas.
     *
     * @param mov            lista de movimientos contables
     * @param guiasContables las guias contables asientos
     */
    private void registrarPolizaUtilidades(MovimientoContable mov, List guiasContables) {
        filtrarGuiaContableUtilidades(new GuiaContableDto(mov, guiasContables, FASE_UTILIDADES));
    }

    /**
     * Permite escribir las polizas que corresponden a los asientos contables de casa movimiento.
     *
     * @param gcd <code>dto</code> creado din&aacute;micamente con movimientos contables y guias.
     */
    private void filtrarGuiaContableUtilidades(GuiaContableDto gcd) {
        for (Iterator iterator = gcd.getGuiasContablesAsientos().iterator(); iterator.hasNext();) {
            GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
            if (gcd.getMovimientoContable().getMovimientoContableDetalles() != null) {
                for (Iterator iterator1 = gcd.getMovimientoContable().
                        getMovimientoContableDetalles().iterator(); iterator1.hasNext();) {
                    MovimientoContableDetalle movDet = (MovimientoContableDetalle) iterator1.next();
                    if (IMPORTE_UTILIDADES.equals(movDet.getTipoDetalle())) {
                        escribirPolizaUtilidades(gcd.getMovimientoContable(), gca,
                                obtenMonto(gcd.getMovimientoContable(), IMPORTE_UTILIDADES),
                                modoSap ? movDet.getMovimientoContable().getDivisa() : getPeso());
                    }
                    else if (IMPORTE_PERDIDAS.equals(movDet.getTipoDetalle())) {
                        escribirPolizaUtilidades(gcd.getMovimientoContable(), gca,
                                obtenMonto(gcd.getMovimientoContable(), IMPORTE_PERDIDAS),
                                modoSap ? movDet.getMovimientoContable().getDivisa() : getPeso());
                    }
                }
            }
        }
    }

    /**
     * Escribe un registro de poliza despu&eacute;s de haber aplicado varios filtros, en caso de no
     * poder escribirlo se marca con error en el status del movimientos Contable.
     *
     * @param mov   el movimiento Contable
     * @param gca   la guia contable asiento correspondiente a &eacute;sta p&oacute;liza
     * @param monto monto calculado para  &eacute;ste registro.
     * @param div   divisa correspondiente a &eacute;ste registro.
     */
    private void escribirPolizaUtilidades(MovimientoContable mov, GuiaContableAsiento gca,
                                          double monto, Divisa div) {
        if (monto != 0) {
            Poliza poliza = new Poliza();
            poliza.setFechaValor(mov.getFechaValor());
            if (!modoSap) {
                if (TRADER_USD.equals(mov.getSucursalOrigen()) &&
                        gca.getCuentaContable().startsWith("5205090")) {
                    poliza.setCuentaContable(FIJO_CUENTA_CONTABLE_MESA_TRADER);
                }
                else {
                    poliza.setCuentaContable(gca.getCuentaContable());
                }
            }
            else {
                if (TRADER_USD.equals(mov.getSucursalOrigen()) &&
                        gca.getCuentaContable().startsWith("5404010")) {
                    poliza.setCuentaContable(FIJO_CUENTA_CONTABLE_MESA_TRADER_SAP);
                }
                else {
                    poliza.setCuentaContable(gca.getCuentaContable());
                }
            }
            poliza.setEntidad(gca.getEntidad());
            poliza.setCargoAbono(gca.getCargoAbono());
            poliza.setDivisa(div);
            poliza.setImporte(monto);
            poliza.setIdDeal(mov.getIdDeal());
            poliza.setReferencia(gca.getDescripcion());
            poliza.setFechaCreacion(getFechaHoy().getTime());
            poliza.setTipoDeal(mov.getTipoDeal());
            poliza.setSucursalOrigen(mov.getSucursalOrigen());
            poliza.setCliente(" ");
            poliza.setFechaProcesamiento(null);
            poliza.setFolioDetalle(mov.getFolioDetalle());
            poliza.setTipoCambio(mov.getTipoCambio());
            poliza.setStatusProceso(STATUS_NO_PROCESADO);
            poliza.setNoContratoSica("Sin Contrato");
            storePoliza(poliza);
        }
    }


    /**
     * Obtiene los Movimientos Contables fase a fase para despues obtener sus Guias Contables
     * Asientos Si la moneda del movimiento Contable es un Metal amonedado, lo resume en ORO u OZP
     */
    private void generacionExplosionContable() {
        int contador = 1;
        warn("Comenzando p\u00f3lizas de Pactaci\u00f3n");
        List movs = seleccionMovimientosContablesPorFase(FASE_PACTACION);
        for (Iterator iterator = movs.iterator(); iterator.hasNext(); contador++) {
            MovimientoContable mov = (MovimientoContable) iterator.next();
            List guias;
            if (mov.getDivisa().isMetalAmonedado()) {
                guias = seleccionGuiasContablesAsientosPactacion(FASE_PACTACION,
                        mov.getTipoOperacion().toUpperCase(), mov.getDivisa().getGrupo().trim());
                registrarPoliza(mov, guias, null);
            }
            else {
                guias = seleccionGuiasContablesAsientosPactacion(FASE_PACTACION,
                        mov.getTipoOperacion().toUpperCase(), mov.getDivisa().getIdDivisa().trim());
                registrarPoliza(mov, guias, null);
            }
            if (contador % Num.I_10 == 0) {
                warn("**** " + contador + " / " + movs.size());
            }
        }
        warn("Terminando p\u00f3lizas de Pactaci\u00f3n");
        warn("Comenzando p\u00f3lizas de Liquidacion 2A");
        contador = 1;
        List movs2A = seleccionMovimientosContablesPorFase(FASE_LIQUIDACION_2A);
        for (Iterator iterator = movs2A.iterator(); iterator.hasNext(); contador++) {
            MovimientoContable mov = (MovimientoContable) iterator.next();
            List guias;
            if (mov.getDivisa().isMetalAmonedado()) {
                guias = seleccionGuiasContablesAsientos(FASE_LIQUIDACION_2A,
                        mov.getTipoOperacion().toUpperCase(), mov.getDivisa().getGrupo().trim());
            }
            else {
                guias = seleccionGuiasContablesAsientos(FASE_LIQUIDACION_2A,
                        mov.getTipoOperacion().toUpperCase(), mov.getDivisa().getIdDivisa().trim());
            }
            registrarPoliza(mov, guias, null);
            if (contador % Num.I_10 == 0) {
                warn("**** " + contador + " / " + movs2A.size());
            }
        }
        warn("Terminando p\u00f3lizas de Liquidaci\u00f3n 2A");
        warn("Comenzando p\u00f3lizas de Liquidaci\u00f3n 2B");
        contador = 1;
        List movs2B = seleccionMovimientosContablesPorFase(FASE_LIQUIDACION_2B);
        for (Iterator iterator = movs2B.iterator(); iterator.hasNext(); contador++) {
            MovimientoContable mov = (MovimientoContable) iterator.next();
            List guias;
            List guiasNeteo = new ArrayList();
            if (mov.getDivisa().isMetalAmonedado()) {
                guias = seleccionGuiasContablesAsientos(FASE_LIQUIDACION_2B, mov.getTipoOperacion().
                        toUpperCase(), mov.getDivisa().getGrupo().trim());
            }
            else if (!mov.getDivisa().isMetalAmonedado() && NETEO.equals(mov.getTipoDeal())) {
                guias = seleccionGuiasContablesAsientos(FASE_LIQUIDACION_2B, mov.getTipoOperacion().
                        toUpperCase(), mov.getDivisa().getIdDivisa().trim());
                guiasNeteo = seleccionGuiasContablesAsientosSinMoneda(FASE_LIQUIDACION_2B,
                        mov.getTipoOperacion().toUpperCase());
            }
            else {
                guias = seleccionGuiasContablesAsientos(FASE_LIQUIDACION_2B,
                        mov.getTipoOperacion().toUpperCase(), mov.getDivisa().getIdDivisa().trim());
            }
            registrarPoliza(mov, guias, guiasNeteo);
            if (contador % Num.I_10 == 0) {
                warn("**** " + contador + " / " + movs2B.size());
            }
        }
        warn("Terminando p\u00f3lizas de Liquidaci\u00f3n 2B");
        warn("Comenzando p\u00f3lizas de Liquidaci\u00f3n L3");
        contador = 1;
        List movsL3 = seleccionMovimientosContablesPorFase(FASE_LIQUIDACION_L3);
        for (Iterator iterator = movsL3.iterator(); iterator.hasNext(); contador++) {
            MovimientoContable mov = (MovimientoContable) iterator.next();
            List guias;
            if (mov.getDivisa().isMetalAmonedado()) {
                guias = seleccionGuiasContablesAsientos(FASE_LIQUIDACION_L3,
                        mov.getTipoOperacion().toUpperCase(), mov.getDivisa().getGrupo().trim());
            }
            else {
                guias = seleccionGuiasContablesAsientos(FASE_LIQUIDACION_L3,
                        mov.getTipoOperacion().toUpperCase(), mov.getDivisa().getIdDivisa().trim());
            }
            registrarPoliza(mov, guias, null);
            if (contador % Num.I_10 == 0) {
                warn("**** " + contador + " / " + movsL3.size());
            }
        }
        warn("Terminando p\u00f3lizas de Liquidaci\u00f3n L3");
        warn("Comenzando p\u00f3lizas de Liquidaci\u00f3n L4");
        contador = 1;
        List movsL4 = seleccionMovimientosContablesPorFase(FASE_LIQUIDACION_L4);
        for (Iterator iterator = movsL4.iterator(); iterator.hasNext(); contador++) {
            MovimientoContable mov = (MovimientoContable) iterator.next();
            List guias;
            List guiasNeteo = new ArrayList();
            if (mov.getDivisa().isMetalAmonedado()) {
                guias = seleccionGuiasContablesAsientos(FASE_LIQUIDACION_L4,
                        mov.getTipoOperacion().toUpperCase(), mov.getDivisa().getGrupo().trim());
            }
            else if (!mov.getDivisa().isMetalAmonedado() && NETEO.equals(mov.getTipoDeal())) {
                guias = seleccionGuiasContablesAsientos(FASE_LIQUIDACION_L4,
                        mov.getTipoOperacion().toUpperCase(), mov.getDivisa().getIdDivisa().trim());
                guiasNeteo = seleccionGuiasContablesAsientosSinMoneda(FASE_LIQUIDACION_L4,
                        mov.getTipoOperacion().toUpperCase());
            }
            else {
                guias = seleccionGuiasContablesAsientos(FASE_LIQUIDACION_L4,
                        mov.getTipoOperacion().toUpperCase(), mov.getDivisa().getIdDivisa().trim());
            }
            registrarPoliza(mov, guias, guiasNeteo);
            if (contador % Num.I_10 == 0) {
                warn("**** " + contador + " / " + movsL4.size());
            }
        }
        warn("Terminando p\u00f3lizas de Liquidaci\u00f3n L4");
    }

    /**
     * Contabilizaci&oacute;n de deals pactados con fecha de Captura hoy y que su estatus sea > CO
     * Esto significa que ya fueron procesados.
     */
    private void seleccionPactacionHoy() {
        Set set = new HashSet(getHibernateTemplate().findByNamedQuery("findDealsPactadosHoy",
                new Object[]{getFechaHoy12(), getFechaHoy()}));
        List detsTmp = new ArrayList(set);
        List dets = new ArrayList();
        //Los deals de Ixe forwards y de balance reverso no se contabilizan
        for (Iterator iterator1 = detsTmp.iterator(); iterator1.hasNext();) {
            Deal d = (Deal) iterator1.next();
            if (noPerteneceAIxeForwards(d)) {
                dets.add(d);
            }
        }
        List dets2 = new ArrayList();
        for (Iterator iterator = dets.iterator(); iterator.hasNext();) {
            Deal d = (Deal) iterator.next();
            if (Constantes.CASH.equals(d.getTipoValor())) {
                for (Iterator iterator1 = d.getDetalles().iterator(); iterator1.hasNext();) {
                    DealDetalle det = (DealDetalle) iterator1.next();
                    if (DealDetalle.STATUS_DET_COMPLETO.equals(det.getStatusDetalleDeal()) ||
                            DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(
                                    det.getStatusDetalleDeal())) {
                        if (!Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
                            dets2.add(det);
                        }
                    }
                }
            }
            else if (!Constantes.CASH.equals(d.getTipoValor())) {
                for (Iterator iterator1 = d.getDetalles().iterator(); iterator1.hasNext();) {
                    DealDetalle det = (DealDetalle) iterator1.next();
                    if (!DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal()) &&
                            !Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
                        dets2.add(det);
                    }
                }
            }
        }
        crearElementosFaseUno(sortDealDetallesByPesos(dets2));
    }

    /**
     * Crea los movimientos Contables para la fase de pactaci&oacute;n
     *
     * @param dets lista de detalles correspondientes a &eacute;sta fase contable
     */
    private void crearElementosFaseUno(List dets) {
        for (Iterator it = dets.iterator(); it.hasNext();) {
            MovimientoContable mov = new MovimientoContable();
            DealDetalle det = (DealDetalle) it.next();
            mov.setIdDeal(det.getDeal().getIdDeal());
            mov.setIdDealPosicion(det.getIdDealPosicion());
            mov.setFolioDetalle(det.getFolioDetalle());
            mov.setCliente("");
            mov.setFechaValor(det.getDeal().getFechaCaptura());
            mov.setDivisa(det.getDivisa());
            mov.setFaseContable(FASE_PACTACION);
            if (Constantes.HR72.equals(det.getDeal().getTipoValor())) {
                mov.setTipoFechaValor(Constantes.VFUT);
            }
            else {
                mov.setTipoFechaValor(det.getDeal().getTipoValor().trim());
            }
            //----------Ixe Coberturas
            if (perteneceAIxeCoberturas(det.getDeal())) {
                //Pesos
                String mnemonico;
                if (Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
                    mnemonico = obtenerMnemonicoPesosIxeCobertura(det, mov);
                    mov.setMnemonico(mnemonico);
                }
                //Otras Divisas USD,CAD,EUR,GBP,CHF,CRC,NZD,AUD,TWD,XOT,DKK,
                //XPL,XCE,XAZ,XHI,XMH,XCH,XQH,NOK,JPY,SEK,ORO,PLATA
                else {
                    mnemonico = obtenerMnemonicoDivisasIxeCobertura(det, mov);
                    mov.setMnemonico(mnemonico);
                }
            }
            //Cuando no es Ixe Coberturas todos pasan por aqui
            else {
                mov.setMnemonico(det.getMnemonico());
            }
            if (det.getDeal().isNeteo()) {
                mov.setTipoDeal(NETEO);
            }
            else {
                if (det.getDeal().isDeSucursal()) {
                    mov.setTipoDeal(SUCURSAL);
                }
                else if (det.getDeal().isInterbancario()) {
                    mov.setTipoDeal(INTERBANCARIO);
                }
                else {
                    mov.setTipoDeal(NORMAL);
                }
            }
            //----------Ixe Coberturas
            mov.setTipoOperacion(det.getDeal().isNeteo() ||
                    Divisa.PESO.equals(det.getDivisa().getIdDivisa()) ? det.isRecibimos() ?
                    COMPRA : VENTA : det.getDeal().isCompra() ? COMPRA : VENTA);
            mov.setUsuario(det.getDeal().getUsuario().getIdUsuario());
            mov.setSucursalOrigen(det.getDeal().getCanalMesa().getCanal().getIdCanal());
            mov.setStatusActual(AUTORIZADA);
            mov.setStatusAnterior(PENDIENTE);
            if (Divisa.PESO.equals(det.getDeal().getCanalMesa().getMesaCambio().
                    getDivisaReferencia().getIdDivisa())) {
                mov.setTipoCambio(det.getTipoCambio());
            }
            else {
                double tipoDeCambio = getPrecioReferenciaMidSpot(det) * det.getTipoCambio();
                mov.setTipoCambio(tipoDeCambio);
            }
            mov.setLiquidacionEspecial(det.getDeal().getLiquidacionEspecial());
            if (mov.getDivisa().getGrupo() != null) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, det.getDivisa(),
                        IMPORTE_DIV, mov.getDivisa().getEquivalenciaMetal().doubleValue() *
                                det.getMonto()));
            }
            else {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, det.getDivisa(),
                        IMPORTE_DIV, det.getMonto()));
            }
            mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                    IMPORTE_PESOS, mov.getTipoCambio() * det.getMonto()));
            /*
             * Volvemos a verificar si es metal amonedado para corregir el tipo de cambio,
             * se debe presentar con el tipo de cambio con respecto a la equivalencia en PESOS.
             */
            if (mov.getDivisa().getGrupo() != null) {
            	double tipoCambioMetales;
            	double equivalencia = mov.getDivisa().getEquivalenciaMetal().doubleValue();
            	tipoCambioMetales = Redondeador.redondear6Dec(mov.getTipoCambio() / equivalencia);
            	mov.setTipoCambio(tipoCambioMetales);
            }
            mov.setStatusProceso(STATUS_NO_PROCESADO);
            mov.setFechaCreacion(getFechaHoy().getTime());
            storeMovimiento(mov, det);
            for (Iterator it1 = mov.getMovimientoContableDetalles().iterator(); it1.hasNext();) {
                MovimientoContableDetalle movDet = (MovimientoContableDetalle) it1.next();
                store(movDet);
            }
        }
    }

    /**
     * Obtiene los nuevos registros correspondientes a las utilidades del d&iacute;a de hoy.
     *
     * @param utilidades la lista de registros de utilidades.
     */
    private void seleccionUtilidades(List utilidades) {
        crearElementosUtilidades(utilidades);
    }

    /**
     * Regresa la suma de utilidades de forwards para la mesa y fecha especificados.
     *
     * @param idMesaCambio El n&uacute;mero de la mesa de cambio.
     * @param idDivisa La clave de la divisa.
     * @param fechaHoy La fecha del sistema.
     * @return double.
     */
    public double findUtilidadForward(int idMesaCambio, String idDivisa, Date fechaHoy) {
        List uts = getHibernateTemplate().findByNamedQuery("findUtilidadForwardByFechaMesaDivisa",
                new Object[] {DateUtils.inicioDia(fechaHoy), DateUtils.finDia(fechaHoy),
                        new Integer(idMesaCambio), idDivisa});
        BigDecimal utilidad = new BigDecimal("" + 0.0);
        for (Iterator it = uts.iterator(); it.hasNext();) {
            UtilidadForward uf = (UtilidadForward) it.next();
            utilidad = utilidad.add(uf.getUtilidad());
        }
        return utilidad.doubleValue();

    }

    /**
     * Crea los movimientos Contables para la fase de utilidades.
     *
     * @param utilidades la lista de registros de utilidades.
     */
    private void crearElementosUtilidades(List utilidades) {
        Date fechaHoy = getFechaHoy().getTime();
        for (Iterator it = utilidades.iterator(); it.hasNext();) {
            RecoUtilidad ru = (RecoUtilidad) it.next();
            MovimientoContable mov = new MovimientoContable();
            mov.setIdDeal(ru.getIdDealPosicion());
            mov.setIdDealPosicion(ru.getIdDealPosicion());
            mov.setCliente("0");
            mov.setFechaValor(fechaHoy);
            mov.setDivisa(ru.getDivisa());
            mov.setFaseContable(FASE_UTILIDADES);
            mov.setTipoFechaValor(Constantes.CASH);
            mov.setMnemonico(null);
            mov.setTipoDeal(UTILIDADES);
            if (ru.isRecibimos()) {
                mov.setTipoOperacion(COMPRA);
            }
            else {
                mov.setTipoOperacion(VENTA);
            }
            mov.setUsuario(ru.getIdUsuario());
            mov.setSucursalOrigen(ru.getMesaCambio().getNombre());
            mov.setStatusActual(AUTORIZADA);
            mov.setStatusAnterior(PENDIENTE);
            mov.setTipoCambio(ru.getTipoCambioOtraDivRef());
            mov.setLiquidacionEspecial(null);
            /*debug("**** UTILIDADES mc: " + ru.getMesaCambio().getIdMesaCambio() +
                " div:" + ru.getDivisa().getIdDivisa());  */
            double utilFwd = findUtilidadForward(ru.getMesaCambio().getIdMesaCambio(),
                    ru.getDivisa().getIdDivisa(), fechaHoy);
            /*debug("utilFwd: " + utilFwd);
            debug("ru.monto: " + ru.getMonto()); */
            // Restamos a la utilidad global la utilidad de Ixe Forwards:
            double montoUtilidad = (ru.getMonto() - utilFwd) * ru.getTipoCambioOtraDivRef();
            //debug("montoUtilidadGlobal: " + montoUtilidad);
            if (montoUtilidad >= 0.0) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalleUtilidad(mov,
                        getPeso(), IMPORTE_UTILIDADES, Math.abs(montoUtilidad)));
            }
            else {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalleUtilidad(mov,
                        getPeso(), IMPORTE_PERDIDAS, Math.abs(montoUtilidad)));
            }
            //Se Comenta Linea hasta que se contabilizen la Utilidades por tipo de Cambio.
            //mov.getMovimientoContableDetalles().add(creaMovimientoDetalleUtilidad(mov, getPeso(),
            // IMPORTE_PESOS, Math.abs(ru.getMonto())));
            mov.setStatusProceso(STATUS_NO_PROCESADO);
            mov.setFechaCreacion(getFechaHoy().getTime());
            mov.setFechaProcesamiento(null);
            storeMovimiento(mov, null);
            for (Iterator it1 = mov.getMovimientoContableDetalles().iterator(); it1.hasNext();) {
                MovimientoContableDetalle movDet = (MovimientoContableDetalle) it1.next();
                store(movDet);
            }
        }
    }

    /**
     * Contabilizaci&oacute;n de deals a lo que se refiere a la Liquidaci&oacute;n fecha valor hoy.
     *
     * @param detsStatusLog La lista de registros del log de estatus de detalles de deal.
     */
    private void seleccionLiquidacionHoy(List detsStatusLog) {
        int size = detsStatusLog.size();
        warn("  N\u00f3mero de detalles status log: " + size);
        List dets = new ArrayList();
        int i = 1;
        for (Iterator iterator = detsStatusLog.iterator(); iterator.hasNext(); i++) {
            DealDetalleStatusLog dsl = (DealDetalleStatusLog) iterator.next();
            Calendar fechaLiq = new GregorianCalendar();
            fechaLiq.setTime(DateUtils.inicioDia(
                    dsl.getDealDetalle().getDeal().getFechaLiquidacion()));
            if (fechaLiq.equals(getFechaHoy12()) &&
                    DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(
                            dsl.getCambStatus().getStatusNuevo())) {
                dets.add(dsl.getDealDetalle());
            }
            if (i % Num.I_10 == 0) {
                warn("    Detalle " + i + " / " + size + " proc.");
            }
        }
        crearElementosFaseDosA(sortDealDetallesByPesos(doNeteo(dets)));
    }

    /**
     * Crea los movimientos Contables para la fase de deals liquidados el d&iacute;a de hoy, y que
     * han sido liquidados en fecha.
     *
     * @param dets lista de detalles correspondientes a &eacute;sta fase contable
     */
    private void crearElementosFaseDosA(List dets) {
        int size = dets.size();
        warn("     Creando elementos fase 2A, con " + size + " detalles.");
        int i = 1;
        for (Iterator it = dets.iterator(); it.hasNext(); i++) {
            MovimientoContable mov = new MovimientoContable();
            DealDetalle det = (DealDetalle) it.next();
            if (i % Num.I_10 == 0) {
                warn("    Creando movimiento contable " + i + " " + size + " det: " +
                        det.getIdDealPosicion());
            }
            String tipoIva = "";
            mov.setIdDeal(det.getDeal().getIdDeal());
            mov.setIdDealPosicion(det.getIdDealPosicion());
            mov.setFolioDetalle(det.getFolioDetalle());
            mov.setCliente("");
            mov.setDivisa(det.getDivisa());
            mov.setFechaValor(getFechaHoy().getTime());
            mov.setFaseContable(FASE_LIQUIDACION_2A);
            if (Constantes.HR72.equals(det.getDeal().getTipoValor())) {
                mov.setTipoFechaValor(Constantes.VFUT);
            }
            else {
                mov.setTipoFechaValor(det.getDeal().getTipoValor().trim());
            }
            //----------Ixe Coberturas
            if (perteneceAIxeCoberturas(det.getDeal())) {
                //Pesos
                String mnemonico;
                if (Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
                    mnemonico = obtenerMnemonicoPesosIxeCobertura(det, mov);
                    mov.setMnemonico(mnemonico);
                }
                //Otras Divisas USD,CAD,EUR,GBP,CHF,CRC,NZD,AUD,TWD,XOT,DKK,
                //XPL,XCE,XAZ,XHI,XMH,XCH,XQH,NOK,JPY,SEK,ORO,PLATA
                else {
                    mnemonico = obtenerMnemonicoDivisasIxeCobertura(det, mov);
                    mov.setMnemonico(mnemonico);
                }
            }
            //Cuando no es Ixe Coberturas todos pasan por aqui
            else {
                mov.setMnemonico(det.getMnemonico());
            }
            if (det.getDeal().isNeteo()) {
                mov.setTipoDeal(NETEO);
            }
            else {
                if (det.getDeal().isDeSucursal()) {
                    mov.setTipoDeal(SUCURSAL);
                }
                else if (det.getDeal().isInterbancario()) {
                    mov.setTipoDeal(INTERBANCARIO);
                }
                else {
                    mov.setTipoDeal(NORMAL);
                }
            }
            //----------Ixe Coberturas
            mov.setTipoOperacion(det.getDeal().isNeteo() || Divisa.PESO.equals(
                    det.getDivisa().getIdDivisa()) ? det.isRecibimos() ? COMPRA : VENTA :
                    det.getDeal().isCompra() ? COMPRA : VENTA);
            mov.setUsuario(det.getDeal().getUsuario().getIdUsuario());
            mov.setSucursalOrigen(det.getDeal().getCanalMesa().getCanal().getIdCanal());
            mov.setStatusActual(LIQUIDADA);
            mov.setStatusAnterior(PENDIENTE);
            if (Divisa.PESO.equals(det.getDeal().getCanalMesa().getMesaCambio().
                    getDivisaReferencia().getIdDivisa())) {
                double tipoDeCambio = Divisa.PESO.equals(det.getDivisa().getIdDivisa()) ?
                        1 : det.getTipoCambio();
                mov.setTipoCambio(tipoDeCambio);
            }
            else {
                double tipoDeCambio = getPrecioReferenciaMidSpot(det) * det.getTipoCambio();
                mov.setTipoCambio(tipoDeCambio);
            }
            mov.setLiquidacionEspecial(det.getDeal().getLiquidacionEspecial());
            if (mov.getDivisa().getGrupo() != null) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, det.getDivisa(),
                        IMPORTE_DIV, mov.getDivisa().getEquivalenciaMetal().doubleValue() *
                        det.getMonto()));
            }
            else {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, det.getDivisa(),
                        IMPORTE_DIV, det.getMonto()));
            }
            mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                    IMPORTE_PESOS, det.getMonto() * mov.getTipoCambio()));
            if (det.getComisionCobradaMxn() != 0) {
                Canal canal = (Canal) find(Canal.class, mov.getSucursalOrigen());
                tipoIva = canal.getTipoIva().getClaveTipoIva();
            }
            if (det.getComisionCobradaMxn() != 0) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                        IMPORTE_COM_SIN_IVA, det.getComisionCobradaMxn() /
                                calcularIvaComision(tipoIva)));
            }
            if (det.getComisionCobradaMxn() != 0) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                        IMPORTE_IVA, det.getComisionCobradaMxn() - (det.getComisionCobradaMxn() /
                        calcularIvaComision(tipoIva))));
            }
            if (det.getComisionCobradaMxn() != 0) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                        IMPORTE_LIQ, (det.getMonto() * mov.getTipoCambio()) +
                        (det.getComisionCobradaMxn() / calcularIvaComision(tipoIva)) +
                        (det.getComisionCobradaMxn() - (det.getComisionCobradaMxn() /
                                calcularIvaComision(tipoIva)))));
            }
            if (det.getComisionCobradaMxn() == 0) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                        IMPORTE_LIQ, det.getMonto() * mov.getTipoCambio()));
            }
            /*
             * Volvemos a verificar si es metal amonedado para corregir el tipo de cambio,
             * se debe presentar con el tipo de cambio con respecto a la equivalencia en PESOS.
             */
            if (mov.getDivisa().getGrupo() != null) {
            	double tipoCambioMetales;
            	double equivalencia = mov.getDivisa().getEquivalenciaMetal().doubleValue();
            	tipoCambioMetales = Redondeador.redondear6Dec(mov.getTipoCambio() / equivalencia);
            	mov.setTipoCambio(tipoCambioMetales);
            }
            mov.setStatusProceso(STATUS_NO_PROCESADO);
            mov.setFechaCreacion(getFechaHoy().getTime());
            storeMovimiento(mov, det);
            for (Iterator it1 = mov.getMovimientoContableDetalles().iterator(); it1.hasNext();) {
                MovimientoContableDetalle movDet = (MovimientoContableDetalle) it1.next();
                store(movDet);
            }
        }
    }

    /**
     * Obtiene los deals que fueron liquidados por adelantado el d&iacute;a del proceso.
     *
     * @param detsStatusLog La lista de registros del log de status de detalles de deal.
     * @return List.
     */
    private List seleccionLiquidacionDiaProceso(List detsStatusLog) {
        List dets = new ArrayList();
        List dets2b = new ArrayList();
        List tmp = new ArrayList();
        List tmp2 = new ArrayList();
        for (Iterator iterator = detsStatusLog.iterator(); iterator.hasNext();) {
            DealDetalleStatusLog dsl = (DealDetalleStatusLog) iterator.next();
            Calendar fechaLiq = new GregorianCalendar();
            fechaLiq.setTime(DateUtils.inicioDia(
                    dsl.getDealDetalle().getDeal().getFechaLiquidacion()));
            if (fechaLiq.after(getFechaHoy12()) &&
                    DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(
                            dsl.getCambStatus().getStatusNuevo())) {
                dets.add(dsl.getDealDetalle());
                if (!ENTREGAMOSBALNETEO.equals(dsl.getDealDetalle().getMnemonico()) &&
                        !RECIBIMOSBALNETEO.equals(dsl.getDealDetalle().getMnemonico())) {
                    tmp.add(dsl);
                }
                if (!tmp2.contains(dsl.getDealDetalle().getDeal()) &&
                        (!ENTREGAMOSBALNETEO.equals(dsl.getDealDetalle().getMnemonico())) &&
                        !RECIBIMOSBALNETEO.equals(dsl.getDealDetalle().getMnemonico())) {
                    tmp2.add(dsl.getDealDetalle().getDeal());
                }
            }
        }
        //L2B
        for (Iterator iterator = tmp2.iterator(); iterator.hasNext();) {
            Deal deal = (Deal) iterator.next();
            for (Iterator iterator1 = deal.getDetalles().iterator(); iterator1.hasNext();) {
                DealDetalle det = (DealDetalle) iterator1.next();
                if (!DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal()) &&
                        (DealDetalle.STATUS_DET_COMPLETO.equals(det.getStatusDetalleDeal()))) {
                    dets2b.add(det);
                }
            }
        }
        crearElementosFaseDosB(sortDealDetallesByPesos(doNeteo(dets2b)));
        crearElementosFaseTres(sortDealDetallesByPesos(doNeteo(dets)));
        return tmp;
    }

    /**
     * Crea los movimientos Contables para la fase de deal que fueron liquidados por adelantado.
     *
     * @param dets lista de detalles correspondientes a &eacute;sta fase contable
     */
    private void crearElementosFaseTres(List dets) {
        for (Iterator it = dets.iterator(); it.hasNext();) {
            MovimientoContable mov = new MovimientoContable();
            DealDetalle det = (DealDetalle) it.next();
            String tipoIva = "";
            mov.setIdDeal(det.getDeal().getIdDeal());
            mov.setIdDealPosicion(det.getIdDealPosicion());
            mov.setFolioDetalle(det.getFolioDetalle());
            mov.setCliente("");
            mov.setFechaValor(getFechaHoy().getTime());
            mov.setDivisa(det.getDivisa());
            mov.setFaseContable(FASE_LIQUIDACION_L3);
            if (Constantes.HR72.equals(det.getDeal().getTipoValor())) {
                mov.setTipoFechaValor(Constantes.VFUT);
            }
            else {
                mov.setTipoFechaValor(det.getDeal().getTipoValor().trim());
            }
            //----------Ixe Coberturas
            if (perteneceAIxeCoberturas(det.getDeal())) {
                //Pesos
                String mnemonico;
                if (Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
                    mnemonico = obtenerMnemonicoPesosIxeCobertura(det, mov);
                    mov.setMnemonico(mnemonico);
                }
                //Otras Divisas USD,CAD,EUR,GBP,CHF,CRC,NZD,AUD,TWD,XOT,DKK,
                //XPL,XCE,XAZ,XHI,XMH,XCH,XQH,NOK,JPY,SEK,ORO,PLATA
                else {
                    mnemonico = obtenerMnemonicoDivisasIxeCobertura(det, mov);
                    mov.setMnemonico(mnemonico);
                }
            }
            //Cuando no es Ixe Coberturas todos pasan por aqui
            else {
                mov.setMnemonico(det.getMnemonico());
            }
            if (det.getDeal().isNeteo()) {
                mov.setTipoDeal(NETEO);
            }
            else {
                if (det.getDeal().isDeSucursal()) {
                    mov.setTipoDeal(SUCURSAL);
                }
                else if (det.getDeal().isInterbancario()) {
                    mov.setTipoDeal(INTERBANCARIO);
                }
                else {
                    mov.setTipoDeal(NORMAL);
                }
            }
            //----------Ixe Coberturas
            mov.setTipoOperacion(det.getDeal().isNeteo() ||
                    Divisa.PESO.equals(det.getDivisa().getIdDivisa()) ? det.isRecibimos() ?
                    COMPRA : VENTA : det.getDeal().isCompra() ? COMPRA : VENTA);
            mov.setUsuario(det.getDeal().getUsuario().getIdUsuario());
            mov.setSucursalOrigen(det.getDeal().getCanalMesa().getCanal().getIdCanal());
            mov.setStatusActual(LIQUIDADA);
            mov.setStatusAnterior(PENDIENTE);
            if (Divisa.PESO.equals(det.getDeal().getCanalMesa().getMesaCambio().
                    getDivisaReferencia().getIdDivisa())) {
                double tipoDeCambio = Divisa.PESO.equals(det.getDivisa().getIdDivisa()) ?
                        1 : det.getTipoCambio();
                mov.setTipoCambio(tipoDeCambio);
            }
            else {
                double tipoDeCambio = getPrecioReferenciaMidSpot(det) * det.getTipoCambio();
                mov.setTipoCambio(tipoDeCambio);
            }
            mov.setLiquidacionEspecial(det.getDeal().getLiquidacionEspecial());
            if (mov.getDivisa().getGrupo() != null) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, det.getDivisa(),
                        IMPORTE_DIV, mov.getDivisa().getEquivalenciaMetal().doubleValue() *
                        det.getMonto()));
            }
            else {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, det.getDivisa(),
                        IMPORTE_DIV, det.getMonto()));
            }
            mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                    IMPORTE_PESOS, det.getMonto() * mov.getTipoCambio()));
            if (det.getComisionCobradaMxn() != 0) {
                Canal canal = (Canal) find(Canal.class, mov.getSucursalOrigen());
                tipoIva = canal.getTipoIva().getClaveTipoIva();
            }
            if (det.getComisionCobradaMxn() != 0) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                        IMPORTE_COM_SIN_IVA, det.getComisionCobradaMxn() /
                        calcularIvaComision(tipoIva)));
            }
            if (det.getComisionCobradaMxn() != 0) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                        IMPORTE_IVA, det.getComisionCobradaMxn() - (det.getComisionCobradaMxn() /
                        calcularIvaComision(tipoIva))));
            }
            if (det.getComisionCobradaMxn() != 0) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                        IMPORTE_LIQ, (det.getMonto() * mov.getTipoCambio()) +
                        (det.getComisionCobradaMxn() / calcularIvaComision(tipoIva)) +
                        (det.getComisionCobradaMxn() - (det.getComisionCobradaMxn() /
                                calcularIvaComision(tipoIva)))));
            }
            if (det.getComisionCobradaMxn() == 0) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                        IMPORTE_LIQ, det.getMonto() * mov.getTipoCambio()));
            }
            /*
             * Volvemos a verificar si es metal amonedado para corregir el tipo de cambio,
             * se debe presentar con el tipo de cambio con respecto a la equivalencia en PESOS.
             */
            if (mov.getDivisa().getGrupo() != null) {
            	double tipoCambioMetales;
            	double equivalencia = mov.getDivisa().getEquivalenciaMetal().doubleValue();
            	tipoCambioMetales = Redondeador.redondear6Dec(mov.getTipoCambio() / equivalencia);
            	mov.setTipoCambio(tipoCambioMetales);
            }
            mov.setStatusProceso(STATUS_NO_PROCESADO);
            mov.setFechaCreacion(getFechaHoy().getTime());
            storeMovimiento(mov, det);
            for (Iterator it1 = mov.getMovimientoContableDetalles().iterator(); it1.hasNext();) {
                MovimientoContableDetalle movDet = (MovimientoContableDetalle) it1.next();
                store(movDet);
            }
        }
    }

    /**
     * Permite buscar en los movimientos Contables de fase de deudores  que ya habian sido pactados.
     *
     * @param det El detalle de deal.
     * @return true si la condici&oacute;n se cumple
     */
    private boolean encontrarDealDetalleMovimientoContable(DealDetalle det) {
        List tmp = findMovimientosFaseDosB(det.getDeal().getIdDeal());
        for (Iterator iterator = tmp.iterator(); iterator.hasNext();) {
            MovimientoContable mov = (MovimientoContable) iterator.next();
            if (mov.getIdDealPosicion() == det.getSustituyeA().intValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Encuentra los movimientos Deudores  para fines de comparaci&oacute;n con nuevos Registrados.
     * En caso de un cambio de liquidaci&oacute;n  en un detalle de un deal, se necesita saber si el
     * movimiento ya habia sido registrado
     * Para informaci&oacute;n m&aacute;s precisa , consultar narrativa de Generaci&oacute;n de
     * Movimientos Contables.
     *
     * @param idDeal El n&uacute;mero de deal.
     * @return List.
     */
    private List findMovimientosFaseDosB(int idDeal) {
        return getHibernateTemplate().findByNamedQuery("findMovimientoPorFaseIdDeal",
                new Object[]{FASE_LIQUIDACION_2B, new Integer(idDeal), getFechaHoy12()});
    }

    /**
     * Encuentra los Deals Cancelados por split o goma para que no se vuelvan a contabilizar
     * Para informaci&oacute;n m&aacute;s precisa , consultar narrativa de Generaci&oacute;n de
     * Movimientos Contables.
     */
    private void liquidacionParcialDealsPorSplitGoma() {
        //Paso 1: Busqueda de Registros no Repetidos
        warn("--Inicia liquidacionParcialDealsPorSplitGoma.");
        List listaDealDetalleStatusLog = findLiquidacionParcialDeals(getFechaHoy12().getTime(),
                getFechaHoy().getTime());
        int size = listaDealDetalleStatusLog.size();
        warn("--Detalles split goma: " + size);
        int i = 1;
        for (Iterator iterator = listaDealDetalleStatusLog.iterator(); iterator.hasNext(); i++) {
            Object[] obj = (Object[]) iterator.next();
            Integer idDealPosicion = (Integer) obj[0];
            int reemplazadoPorA = Integer.parseInt(obj[Num.I_3].toString());
            Integer idDeal = (Integer) obj[Num.I_5];
            Integer sustituyeA = (Integer) obj[Num.I_6];

            if (sustituyeA == null) {
                // Se busca en movimiento contable algun registro cuyo idDealPosicion sea igual
                // idDealPosicion del detalle iterado, que el campo fase=L2B y el campo
                // idDeal=idDeal del detalle iterado.
                //
                //Si existe el registro se inserta otro identico al que se
                //encontro en movimiento contable pero con idDealPosicion igual al campo
                // reemplazadoporA
                //del detalle Iterado.
                List listaBuscaMovimientoContable = findLiquidacionParcialDealsMovimientoContable(
                        idDealPosicion, FASE_LIQUIDACION_2B, idDeal);
                for (Iterator it = listaBuscaMovimientoContable.iterator(); it.hasNext();) {
                    MovimientoContable mov = (MovimientoContable) it.next();
                    MovimientoContable movTmp = new MovimientoContable(mov, reemplazadoPorA);
                    storeMovimiento(movTmp, null);
                }
            }
            else {
                // Se busca un registro en movimientos contables cuyo campo idDealPosicion sea igual
                // al campo sustituyeA del detalle iterado, el campo fase=L2B y el campo
                // idDeal=idDeal del detalle iterado.
                //
                //Si existe el registro se inserta otro identico al que se encontro el movimiento
                // contable pero con idDealPosicion=al idDeal del detalle iterado
                List listaBuscaRegistroSustituyeAMovimientoContable =
                        findLiquidacionParcialDealsMovimientoContableRegistro(sustituyeA,
                                FASE_LIQUIDACION_2B, idDeal);
                for (Iterator it = listaBuscaRegistroSustituyeAMovimientoContable.iterator();
                     it.hasNext();) {
                    MovimientoContable mov = (MovimientoContable) it.next();
                    MovimientoContable movTmp = new MovimientoContable(mov,
                            idDealPosicion.intValue());
                    storeMovimiento(movTmp, null);
                }
            }
            if (i % Num.I_10 == 0) {
                warn("--Detalle " + i + " / " + size + " procesado.");
            }
        }
    }

    /**
     * Obtiene los deals que no fueron liquidados
     *
     */
    private void seleccionNoHuboLiquidacion() {
        warn("-- Inicia noHuboLiquidacion");
        List dealsDetallesLog = getHibernateTemplate().findByNamedQuery("findDetsNoLiquidacionHoy",
                new Object[]{getFechaHoy12(), getFechaHoy()});
        int size = dealsDetallesLog.size();
        warn("-- Procesando " + size + " registros...");
        List dealsTmp = new ArrayList();
        List dealPosicion = new ArrayList();
        for (Iterator iterator = dealsDetallesLog.iterator(); iterator.hasNext();) {
            DealDetalleStatusLog ddsl = (DealDetalleStatusLog) iterator.next();
            if (!dealPosicion.contains(new Integer(ddsl.getDealDetalle().getIdDealPosicion()))) {
                dealPosicion.add(new Integer(ddsl.getDealDetalle().getIdDealPosicion()));
            }
        }
        for (Iterator iterator = dealPosicion.iterator(); iterator.hasNext();) {
            Integer idDealPosicion = (Integer) iterator.next();
            for (Iterator iterator1 = dealsDetallesLog.iterator(); iterator1.hasNext();) {
                DealDetalleStatusLog ddsl1 = (DealDetalleStatusLog) iterator1.next();
                if (idDealPosicion.intValue() == ddsl1.getDealDetalle().getIdDealPosicion()) {
                    dealsTmp.add(ddsl1);
                    break;
                }
            }
        }
        for (Iterator iterator1 = dealsTmp.iterator(); iterator1.hasNext();) {
            DealDetalleStatusLog ddsl = (DealDetalleStatusLog) iterator1.next();
            if (!noPerteneceAIxeForwards(ddsl.getDealDetalle().getDeal()) ||
                    !ddsl.getDealDetalle().getDeal().getLiquidacionAnticipada().equals("0")) {
                iterator1.remove();
            }
        }
        warn("-- Se recorren los registros...");
        List dets = new ArrayList();
        for (Iterator iterator = dealsTmp.iterator(); iterator.hasNext();) {
            DealDetalleStatusLog ddsl = (DealDetalleStatusLog) iterator.next();
            Calendar fechaLiq = new GregorianCalendar();
            fechaLiq.setTime(DateUtils.inicioDia(ddsl.getDealDetalle().getDeal().
                    getFechaLiquidacion()));
            if (fechaLiq.getTime().equals(getFechaHoy12().getTime()) ||
                    fechaLiq.getTime().before(getFechaHoy12().getTime())) {
                if (!DealDetalle.STATUS_DET_PARCIALMENTE_PAG_LIQ.equals(
                        ddsl.getCambStatus().getStatusNuevo()) &&
                        !DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(
                                ddsl.getCambStatus().getStatusNuevo())) {
                    dets.add(ddsl.getDealDetalle());
                }
            }
        }
        warn("--Revisi\u00f3n de Deals que no generan movimientos en Deal Detalle Status Log");
        //Ahora se checan los Deals que no generan movimientos en Deal Detalle Status Log
        List dealsTmp2 = getHibernateTemplate().findByNamedQuery("findDealByStatusByFecha",
                new Object[]{getFechaHoy12(), getFechaHoy()});
        size = dealsTmp2.size();
        warn("--" + size + " registros.");
        List deals2 = new ArrayList();
        for (Iterator iterator1 = dealsTmp2.iterator(); iterator1.hasNext();) {
            Deal d = (Deal) iterator1.next();
            if (noPerteneceAIxeForwards(d) && d.getLiquidacionAnticipada().equals("0")) {
                deals2.add(d);
            }
        }
        warn("-- A.");
        List dets2 = new ArrayList();
        for (Iterator iterator = deals2.iterator(); iterator.hasNext();) {
            Deal d = (Deal) iterator.next();
            List detalles = d.getDetalles();
            for (Iterator iterator1 = detalles.iterator(); iterator1.hasNext();) {
                DealDetalle det = (DealDetalle) iterator1.next();
                if (!DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(det.getStatusDetalleDeal()) &&
                        !DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal())) {
                    dets2.add(det);
                }
            }
        }
        warn("-- B.");
        for (Iterator it = dets2.iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (!dets.contains(det)) {
                dets.add(det);
            }
        }
        warn("-- C.");
        for (Iterator it = dets.iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (det.getSustituyeA() != null) {
                if (encontrarDealDetalleMovimientoContable(det)) {
                    it.remove();
                }
                else {
                    if (isFechaHoyMayorATomConRespectoAFechaCash(
                            det.getDeal().getFechaLiquidacion(), getFechaHoy12().getTime())) {
                        it.remove();
                    }
                }
            }
        }
        warn("-- D.");
        crearElementosFaseDosB(sortDealDetallesByPesos(doNeteo(dets)));
    }

    /**
     * Regresa true si la fechaHoy que corresponde a la base de la fecha Cash proporcionada
     * es mayor a TOM.
     *
     * @param fechaCash La fecha de referencia.
     * @param fechaHoy  La fecha del sistema.
     * @return boolean.
     */
    private boolean isFechaHoyMayorATomConRespectoAFechaCash(Date fechaCash, Date fechaHoy) {
        return !fechaHoy.equals(getPizarronServiceData().getFechaOperacion(fechaCash)) &&
                !fechaHoy.equals(getPizarronServiceData().getFechaTOM(fechaCash));
    }

    /**
     * Crea los movimientos Contables para la fase de deal que no fueron liquidados en la fecha
     * acordada.
     *
     * @param dets lista de detalles correspondientes a &eacute;sta fase contable
     */
    private void crearElementosFaseDosB(List dets) {
        int size = dets.size();
        warn("-- CreandoElementos Fase 2B con " + size);
        int i = 1;
        for (Iterator it = dets.iterator(); it.hasNext(); i++) {
            MovimientoContable mov = new MovimientoContable();
            DealDetalle det = (DealDetalle) it.next();
            String tipoIva = "";
            Calendar fechaLiq = new GregorianCalendar();
            fechaLiq.setTime(DateUtils.inicioDia(det.getDeal().getFechaLiquidacion()));
            mov.setIdDeal(det.getDeal().getIdDeal());
            mov.setIdDealPosicion(det.getIdDealPosicion());
            mov.setFolioDetalle(det.getFolioDetalle());
            mov.setCliente("");
            mov.setFechaValor(getFechaHoy().getTime());
            mov.setDivisa(det.getDivisa());
            mov.setFaseContable(FASE_LIQUIDACION_2B);
            if (Constantes.HR72.equals(det.getDeal().getTipoValor())) {
                mov.setTipoFechaValor(Constantes.VFUT);
            }
            else {
                mov.setTipoFechaValor(det.getDeal().getTipoValor().trim());
            }
            mov.setMnemonico(det.getMnemonico());
            if (det.getDeal().isNeteo()) {
                mov.setTipoDeal(NETEO);
            }
            else {
                if (det.getDeal().isDeSucursal()) {
                    mov.setTipoDeal(SUCURSAL);
                }
                else if (det.getDeal().isInterbancario()) {
                    mov.setTipoDeal(INTERBANCARIO);
                }
                else {
                    mov.setTipoDeal(NORMAL);
                }
            }
            mov.setTipoOperacion(det.getDeal().isNeteo() ||
                    Divisa.PESO.equals(det.getDivisa().getIdDivisa()) ? det.isRecibimos() ?
                    COMPRA : VENTA : det.getDeal().isCompra() ? COMPRA : VENTA);
            mov.setUsuario(det.getDeal().getUsuario().getIdUsuario());
            mov.setSucursalOrigen(det.getDeal().getCanalMesa().getCanal().getIdCanal());
            if (fechaLiq.before(getFechaHoy12()) && !det.isRecibimos()) {
                mov.setStatusActual(ACREEDORES);
            }
            else if (fechaLiq.before(getFechaHoy12()) && det.isRecibimos()) {
                mov.setStatusActual(DEUDORES);
            }
            else if (fechaLiq.equals(getFechaHoy12()) && !det.isRecibimos()) {
                mov.setStatusActual(ACREEDORES);
            }
            else if (fechaLiq.equals(getFechaHoy12()) && det.isRecibimos()) {
                mov.setStatusActual(DEUDORES);
            }
            else {
                mov.setStatusActual(" ");
            }
            if (det.getDeal().isNeteo()) {
                if (checarPesosFormaLiquidacion(det)) {
                    mov.setStatusActual(LIQUIDADA);
                }
            }
            mov.setStatusAnterior(PENDIENTE);

            if (Divisa.PESO.equals(det.getDeal().getCanalMesa().getMesaCambio().
                    getDivisaReferencia().getIdDivisa())) {
                double tipoDeCambio = Divisa.PESO.equals(det.getDivisa().getIdDivisa()) ?
                        1 : det.getTipoCambio();
                mov.setTipoCambio(tipoDeCambio);
            }
            else {
                double tipoDeCambio = getPrecioReferenciaMidSpot(det) * det.getTipoCambio();
                mov.setTipoCambio(tipoDeCambio);
            }
            if (mov.getDivisa().getGrupo() != null) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, det.getDivisa(),
                        IMPORTE_DIV, mov.getDivisa().getEquivalenciaMetal().doubleValue() *
                        det.getMonto()));
            }
            else {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, det.getDivisa(),
                        IMPORTE_DIV, det.getMonto()));
            }
            mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                    IMPORTE_PESOS, det.getMonto() * mov.getTipoCambio()));
            if (det.getComisionCobradaMxn() != 0) {
                Canal canal = (Canal) find(Canal.class, mov.getSucursalOrigen());
                tipoIva = canal.getTipoIva().getClaveTipoIva();
            }
            if (!det.getDeal().isNeteo()) {
                if (det.getComisionCobradaMxn() != 0) {
                    mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                            IMPORTE_COM_SIN_IVA, det.getComisionCobradaMxn() /
                            calcularIvaComision(tipoIva)));
                }
                if (det.getComisionCobradaMxn() != 0) {
                    mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                            IMPORTE_IVA, det.getComisionCobradaMxn() -
                            (det.getComisionCobradaMxn() / calcularIvaComision(tipoIva))));
                }
            }
            if (det.getComisionCobradaMxn() != 0) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                        IMPORTE_LIQ, (det.getMonto() * mov.getTipoCambio()) +
                        (det.getComisionCobradaMxn() / calcularIvaComision(tipoIva)) +
                        (det.getComisionCobradaMxn() - (det.getComisionCobradaMxn() /
                                calcularIvaComision(tipoIva)))));
            }
            if (det.getComisionCobradaMxn() == 0) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                        IMPORTE_LIQ, det.getMonto() * mov.getTipoCambio()));
            }
            /*
             * Volvemos a verificar si es metal amonedado para corregir el tipo de cambio,
             * se debe presentar con el tipo de cambio con respecto a la equivalencia en PESOS.
             */
            if (mov.getDivisa().getGrupo() != null) {
            	double tipoCambioMetales;
            	double equivalencia = mov.getDivisa().getEquivalenciaMetal().doubleValue();
            	tipoCambioMetales = Redondeador.redondear6Dec(mov.getTipoCambio() / equivalencia);
            	mov.setTipoCambio(tipoCambioMetales);
            }
            mov.setLiquidacionEspecial(det.getDeal().getLiquidacionEspecial());
            mov.setStatusProceso(STATUS_NO_PROCESADO);
            mov.setFechaCreacion(getFechaHoy().getTime());
            if (mov.getStatusActual() == null) {
                mov.setStatusActual(" ");
            }
            storeMovimiento(mov, det);
            for (Iterator it1 = mov.getMovimientoContableDetalles().iterator(); it1.hasNext();) {
                MovimientoContableDetalle movDet = (MovimientoContableDetalle) it1.next();
                store(movDet);
            }
            if (i % Num.I_10 == 0) {
                warn("Registro " + i + " / " + size + " procesado.");
            }
        }
    }

    /**
     * Contabilizaci&oacute;n de deals a lo que se refiere a la Liquidaci&oacute;n fecha valor hoy.
     *
     * @param detsL4 La lista de detalles status log.
     */
    private void seleccionLiquidacionDespuesPactadoDealsLiquidacionAnt(List detsL4) {
        List detsStatusLog = new ArrayList();
        //Se limpian Ixe Forwards
        for (Iterator iterator1 = detsL4.iterator(); iterator1.hasNext();) {
            DealDetalleStatusLog dsl = (DealDetalleStatusLog) iterator1.next();
            Calendar fechaCap = new GregorianCalendar();
            fechaCap.setTime(DateUtils.inicioDia(dsl.getDealDetalle().getDeal().getFechaCaptura()));
            if (noPerteneceAIxeForwards(dsl.getDealDetalle().getDeal()) &&
                    dsl.getDealDetalle().getDeal().getLiquidacionAnticipada().equals("1")) {
                detsStatusLog.add(dsl);
            }
        }
        //
        List dets = new ArrayList();
        //Fase L2A
        for (Iterator iterator = detsStatusLog.iterator(); iterator.hasNext();) {
            DealDetalleStatusLog dsl = (DealDetalleStatusLog) iterator.next();
            Calendar fechaLiq = new GregorianCalendar();
            fechaLiq.setTime(DateUtils.inicioDia(
                    dsl.getDealDetalle().getDeal().getFechaLiquidacion()));
            if (fechaLiq.equals(getFechaHoy12()) &&
                    DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(
                            dsl.getCambStatus().getStatusNuevo())) {
                dets.add(dsl.getDealDetalle());
            }
        }
        //
        //Fase L3
        for (Iterator iterator = detsStatusLog.iterator(); iterator.hasNext();) {
            DealDetalleStatusLog dsl = (DealDetalleStatusLog) iterator.next();
            Calendar fechaLiq = new GregorianCalendar();
            fechaLiq.setTime(DateUtils.inicioDia(
                    dsl.getDealDetalle().getDeal().getFechaLiquidacion()));
            if (fechaLiq.after(getFechaHoy12()) &&
                    DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(
                            dsl.getCambStatus().getStatusNuevo())) {
                dets.add(dsl.getDealDetalle());
            }
        }
        //Fase L4
        List detsStatusLogTmp = new ArrayList();
        for (Iterator iterator = detsStatusLog.iterator(); iterator.hasNext();) {
            DealDetalleStatusLog ds = (DealDetalleStatusLog) iterator.next();
            Calendar fechaCambio = new GregorianCalendar();
            fechaCambio.setTime(DateUtils.inicioDia(ds.getCambStatus().getFechaCambio()));
            if (fechaCambio.equals(getFechaHoy12())) {
                detsStatusLogTmp.add(ds);
            }
        }
        for (Iterator iterator = detsStatusLogTmp.iterator(); iterator.hasNext();) {
            DealDetalleStatusLog dsl = (DealDetalleStatusLog) iterator.next();
            Calendar fechaLiq = new GregorianCalendar();
            fechaLiq.setTime(DateUtils.inicioDia(dsl.getDealDetalle().getDeal().
                    getFechaLiquidacion()));
            if (fechaLiq.before(getFechaHoy12()) &&
                    DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(dsl.getCambStatus().
                            getStatusNuevo())) {
                dets.add(dsl.getDealDetalle());
            }
        }
        crearElementosFaseCuatro(sortDealDetallesByPesos(doNeteo(dets)));
    }

    /**
     * Obtiene los deals que fueron liquidados despu&eacute;s de la fecha acordada
     *
     * @param detsStatusLog La lista de registros del log de status de detalles de deal.
     */
    private void seleccionLiquidacionDespuesPactado(List detsStatusLog) {
        List dets = new ArrayList();
        List detsStatusLogTmp = new ArrayList();
        for (Iterator iterator = detsStatusLog.iterator(); iterator.hasNext();) {
            DealDetalleStatusLog ds = (DealDetalleStatusLog) iterator.next();
            Calendar fechaCap = new GregorianCalendar();
            fechaCap.setTime(DateUtils.inicioDia(ds.getDealDetalle().getDeal().getFechaCaptura()));
            Calendar fechaCambio = new GregorianCalendar();
            fechaCambio.setTime(DateUtils.inicioDia(ds.getCambStatus().getFechaCambio()));
            if (fechaCambio.equals(getFechaHoy12())) {
                detsStatusLogTmp.add(ds);
            }
        }
        for (Iterator iterator = detsStatusLogTmp.iterator(); iterator.hasNext();) {
            DealDetalleStatusLog dsl = (DealDetalleStatusLog) iterator.next();
            Calendar fechaLiq = new GregorianCalendar();
            fechaLiq.setTime(DateUtils.inicioDia(
                    dsl.getDealDetalle().getDeal().getFechaLiquidacion()));
            if (fechaLiq.before(getFechaHoy12()) &&
                    DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(dsl.getCambStatus().
                            getStatusNuevo())) {
                dets.add(dsl.getDealDetalle());
            }
        }
        crearElementosFaseCuatro(sortDealDetallesByPesos(doNeteo(dets)));
    }

    /**
     * Crea los movimientos Contables para la fase de deal que fueron liquidados tarde
     *
     * @param dets lista de detalles correspondientes a &eacute;sta fase contable
     */
    private void crearElementosFaseCuatro(List dets) {
        for (Iterator it = dets.iterator(); it.hasNext();) {
            MovimientoContable mov = new MovimientoContable();
            DealDetalle det = (DealDetalle) it.next();
            String tipoIva = null;
            Calendar fechaLiq = new GregorianCalendar();
            fechaLiq.setTime(DateUtils.inicioDia(det.getDeal().getFechaLiquidacion()));
            mov.setIdDeal(det.getDeal().getIdDeal());
            mov.setIdDealPosicion(det.getIdDealPosicion());
            mov.setFolioDetalle(det.getFolioDetalle());
            mov.setCliente("");
            mov.setFechaValor(getFechaHoy().getTime());
            mov.setDivisa(det.getDivisa());
            mov.setFaseContable(FASE_LIQUIDACION_L4);
            if (Constantes.HR72.equals(det.getDeal().getTipoValor())) {
                mov.setTipoFechaValor(Constantes.VFUT);
            }
            else {
                mov.setTipoFechaValor(det.getDeal().getTipoValor().trim());
            }
            //----------Ixe Cobertutas
            if (perteneceAIxeCoberturas(det.getDeal())) {
                //Pesos
                String mnemonico;
                if (Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
                    mnemonico = obtenerMnemonicoPesosIxeCobertura(det, mov);
                    mov.setMnemonico(mnemonico);
                }
                //Otras Divisas USD,CAD,EUR,GBP,CHF,CRC,NZD,AUD,TWD,XOT,DKK,
                //XPL,XCE,XAZ,XHI,XMH,XCH,XQH,NOK,JPY,SEK,ORO,PLATA
                else {
                    mnemonico = obtenerMnemonicoDivisasIxeCobertura(det, mov);
                    mov.setMnemonico(mnemonico);
                }
            }
            //Cuando no es Ixe Coberturas todos pasan por aqui
            else {
                mov.setMnemonico(det.getMnemonico());
            }
            if (det.getDeal().isNeteo()) {
                mov.setTipoDeal(NETEO);
            }
            else {
                if (det.getDeal().isDeSucursal()) {
                    mov.setTipoDeal(SUCURSAL);
                }
                else if (det.getDeal().isInterbancario()) {
                    mov.setTipoDeal(INTERBANCARIO);
                }
                else {
                    mov.setTipoDeal(NORMAL);
                }
            }
            //----------Ixe Cobertutas
            mov.setTipoOperacion(det.getDeal().isNeteo() ||
                    Divisa.PESO.equals(det.getDivisa().getIdDivisa()) ? det.isRecibimos() ?
                    COMPRA : VENTA : det.getDeal().isCompra() ? COMPRA : VENTA);
            mov.setUsuario(det.getDeal().getUsuario().getIdUsuario());
            mov.setSucursalOrigen(det.getDeal().getCanalMesa().getCanal().getIdCanal());
            if (fechaLiq.before(getFechaHoy12()) && !det.isRecibimos()) {
                mov.setStatusAnterior(ACREEDORES);
            }
            else if (fechaLiq.before(getFechaHoy12()) && det.isRecibimos()) {
                mov.setStatusAnterior(DEUDORES);
            }
            else {
                mov.setStatusAnterior(" ");
            }
            if (mov.getStatusAnterior() == null) {
                mov.setStatusAnterior(" ");
            }
            mov.setStatusActual(LIQUIDADA);
            if (Divisa.PESO.equals(det.getDeal().getCanalMesa().getMesaCambio().
                    getDivisaReferencia().getIdDivisa())) {
                double tipoDeCambio = Divisa.PESO.equals(det.getDivisa().getIdDivisa()) ?
                        1 : det.getTipoCambio();
                mov.setTipoCambio(tipoDeCambio);
            }
            else {
                double tipoDeCambio = getPrecioReferenciaMidSpot(det) * det.getTipoCambio();
                mov.setTipoCambio(tipoDeCambio);
            }
            if (mov.getDivisa().getGrupo() != null) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, det.getDivisa(),
                        IMPORTE_DIV, mov.getDivisa().getEquivalenciaMetal().doubleValue() *
                        det.getMonto()));
            }
            else {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, det.getDivisa(),
                        IMPORTE_DIV, det.getMonto()));
            }
            mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                    IMPORTE_PESOS, det.getMonto() * mov.getTipoCambio()));
            if (det.getComisionCobradaMxn() != 0) {
                Canal canal = (Canal) find(Canal.class, mov.getSucursalOrigen());
                tipoIva = canal.getTipoIva().getClaveTipoIva();
            }
            if (det.getComisionCobradaMxn() != 0) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                        IMPORTE_LIQ, (det.getMonto() * det.getTipoCambio()) +
                        (det.getComisionCobradaMxn() / calcularIvaComision(tipoIva)) +
                        (det.getComisionCobradaMxn() - (det.getComisionCobradaMxn() /
                                calcularIvaComision(tipoIva)))));
            }
            if (det.getComisionCobradaMxn() == 0) {
                mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                        IMPORTE_LIQ, det.getMonto() * mov.getTipoCambio()));
            }
            if (det.getDeal().isNeteo()) {
                if (det.getComisionCobradaMxn() != 0) {
                    mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                            IMPORTE_COM_SIN_IVA, det.getComisionCobradaMxn() /
                            calcularIvaComision(tipoIva)));
                }
                if (det.getComisionCobradaMxn() != 0) {
                    mov.getMovimientoContableDetalles().add(creaMovimientoDetalle(mov, getPeso(),
                            IMPORTE_IVA, det.getComisionCobradaMxn() -
                            (det.getComisionCobradaMxn() / calcularIvaComision(tipoIva))));
                }
            }
            /*
             * Volvemos a verificar si es metal amonedado para corregir el tipo de cambio,
             * se debe presentar con el tipo de cambio con respecto a la equivalencia en PESOS.
             */
            if (mov.getDivisa().getGrupo() != null) {
            	double tipoCambioMetales;
            	double equivalencia = mov.getDivisa().getEquivalenciaMetal().doubleValue();
            	tipoCambioMetales = Redondeador.redondear6Dec(mov.getTipoCambio() / equivalencia);
            	mov.setTipoCambio(tipoCambioMetales);
            }
            mov.setStatusProceso(STATUS_NO_PROCESADO);
            mov.setFechaCreacion(getFechaHoy().getTime());
            storeMovimiento(mov, det);
            for (Iterator it1 = mov.getMovimientoContableDetalles().iterator(); it1.hasNext();) {
                MovimientoContableDetalle movDet = (MovimientoContableDetalle) it1.next();
                store(movDet);
            }
        }
    }

    /**
     * Recibe un conjunto de movimientos contables con sus guias contables asientos correspondientes
     * las divide en las correspondientes a la Fase de pactaci&oacute;n y el resto (Fase de
     * liquidaci&oacute;n) as&iacute; como en Compras y Ventas.
     *
     * @param mov                  lista de movimientos contables
     * @param guiasContables       las guias contables asientos
     * @param guiasContablesNeteos las guias que se escriben si hay neteo
     */
    private void registrarPoliza(MovimientoContable mov, List guiasContables,
                                 List guiasContablesNeteos) {
        if (FASE_PACTACION.equals(mov.getFaseContable())) {
            //CASO 1
            filtrarGuiaContable(new GuiaContableDto(mov, guiasContables, EXPLOSION_PAC_COMPRAS));
            //CASO 2
            filtrarGuiaContable(new GuiaContableDto(mov, guiasContables,
                    EXPLOSION_PAC_COMPRAS_CASO2));
            boolean utilidades = false;
            boolean perdidas = false;
            if (mov.getMovimientoContableDetalles() != null) {
                for (Iterator iterator = mov.getMovimientoContableDetalles().iterator();
                     iterator.hasNext();) {
                    MovimientoContableDetalle movDet = (MovimientoContableDetalle) iterator.next();
                    if (IMPORTE_UTILIDADES.equals(movDet.getTipoDetalle())) {
                        utilidades = true;
                    }
                    else if (IMPORTE_PERDIDAS.equals(movDet.getTipoDetalle())) {
                        perdidas = true;
                    }
                }
                //CASO 2B
                if (utilidades) {
                    filtrarGuiaContable(new GuiaContableDto(mov, guiasContables,
                            EXPLOSION_PAC_COMPRAS_CASO_UTILIDADES));
                }
                else if (perdidas) {
                    //CASO 2C
                    filtrarGuiaContable(new GuiaContableDto(mov, guiasContables,
                            EXPLOSION_PAC_COMPRAS_CASO_PERDIDAS));
                }
            }
        }
        else {
            if (COMPRA.equals(mov.getTipoOperacion().trim())) {
                //COMPRA L
                //CASO1
                filtrarGuiaContable(new GuiaContableDto(mov, guiasContables,
                        EXPLOSION_LIQ_COMPRAS));
                //CASO CON NETEO
                if (NETEO.equals(mov.getTipoDeal()) &&
                        (FASE_LIQUIDACION_2B.equals(mov.getFaseContable()) ||
                                FASE_LIQUIDACION_L4.equals(mov.getFaseContable()))) {
                    filtrarGuiaContable(new GuiaContableDto(mov, guiasContables,
                            guiasContablesNeteos, EXPLOSION_LIQ_COMPRAS_CASO2));
                }
                //CASO SIN NETEO
                else {
                    filtrarGuiaContable(new GuiaContableDto(mov, guiasContables,
                            EXPLOSION_LIQ_COMPRAS_CASO2));
                }
                for (Iterator iterator = mov.getMovimientoContableDetalles().iterator();
                iterator.hasNext();) {
                MovimientoContableDetalle movDet = (MovimientoContableDetalle) iterator.next();
                //en pesos
                if (IMPORTE_COM_SIN_IVA.equals(movDet.getTipoDetalle())) {
                    List guias;
                    //CASO_B
                    if (mov.getDivisa().isMetalAmonedado()) {
                               guias = findGuiaContableAsiento(FASE_LIQUIDACION,
                                mov.getTipoOperacion().toUpperCase(), "%" +
                                mov.getDivisa().getGrupo().trim() + "%");
                    }
                    else {
                        guias = findGuiaContableAsiento(FASE_LIQUIDACION, mov.getTipoOperacion().
                                toUpperCase(), "%" + mov.getDivisa().getIdDivisa().trim() +
                                "%");
                    }
                    filtrarGuiaContable(new GuiaContableDto(mov, guias,
                            EXPLOSION_LIQ_VENTAS_CASO_COMISION));
               }
           }
            }
            else {
                //VENTA L
                //CASO1
                filtrarGuiaContable(new GuiaContableDto(mov, guiasContables, EXPLOSION_LIQ_VENTAS));
                //CASO CON NETEO
                if (NETEO.equals(mov.getTipoDeal()) &&
                        (FASE_LIQUIDACION_2B.equals(mov.getFaseContable()) ||
                                FASE_LIQUIDACION_L4.equals(mov.getFaseContable()))) {
                    filtrarGuiaContable(new GuiaContableDto(mov, guiasContables,
                            guiasContablesNeteos, EXPLOSION_LIQ_VENTAS_CASO2));
                }
                //CASO SIN NETEO
                else {
                    filtrarGuiaContable(new GuiaContableDto(mov, guiasContables,
                            EXPLOSION_LIQ_VENTAS_CASO2));
                }
                for (Iterator iterator = mov.getMovimientoContableDetalles().iterator();
                     iterator.hasNext();) {
                    MovimientoContableDetalle movDet = (MovimientoContableDetalle) iterator.next();
                    //en pesos
                    if (IMPORTE_COM_SIN_IVA.equals(movDet.getTipoDetalle())) {
                        List guias;
                        //CASO_B
                        if (mov.getDivisa().isMetalAmonedado()) {
                            guias = findGuiaContableAsiento(FASE_LIQUIDACION,
                                    mov.getTipoOperacion().toUpperCase(), "%" +
                                            mov.getDivisa().getGrupo().trim() + "%");
                        }
                        else {
                            guias = findGuiaContableAsiento(FASE_LIQUIDACION,
                                    mov.getTipoOperacion(). toUpperCase(),
                                    "%" + mov.getDivisa().getIdDivisa().trim() + "%");
                        }
                        filtrarGuiaContable(new GuiaContableDto(mov, guias,
                                EXPLOSION_LIQ_VENTAS_CASO_COMISION));
                    }
                }

            }
            seleccionarFaseLiquidacion(mov);
        }
    }

    /**
     * Recibe una lista de movimientos contables que necesitan escribir una Gu&iacute;a Contable de
     * Liquidaci&oacute;n Dependiendiendo del tipo de Deal aplicamos diferentes reglas para
     * obtenci&oacute;n de las mismas , as&iacute; como para el c&aacute;lculo de los montos.
     *
     * @param mov La lista de movimientos contables
     */
    private void seleccionarFaseLiquidacion(MovimientoContable mov) {
        //Para los movimientos Contables de las Fase 2B, en la cual no fueron liquidados no se
        // escribe una guia de Liquidacion
        if (FASE_LIQUIDACION_2B.equals(mov.getFaseContable().trim())) {
            return;
        }
        double monto = 0;
        double montoMn = 0;
        List guias = cargarGuiasContLiq(mov);
        // Modifique la siguiente  linea al parecer estaba mal pues cargaba cargarGuiasContLiq(mov)
        // 24/01/2007
        List guiasNeteo = new ArrayList();
        
        for (Iterator iterator = mov.getMovimientoContableDetalles().iterator();
             iterator.hasNext();) {
            MovimientoContableDetalle movDet = (MovimientoContableDetalle) iterator.next();

            //en pesos
            if (IMPORTE_LIQ.equals(movDet.getTipoDetalle())) {
                monto = movDet.getMonto();
            }
            //en divisa (pesos si la Divisa es MXN)
            else if (IMPORTE_DIV.equals(movDet.getTipoDetalle())) {
                montoMn = movDet.getMonto();
            }
        }
        //Escribe las guias contables obtenidas.
        for (Iterator iterator = guias.iterator(); iterator.hasNext();) {
            GuiaContableLiq gcl = (GuiaContableLiq) iterator.next();
            escribirPolizaLiquidacion(mov, monto, montoMn, gcl);
        }
        //En caso de ser Deal con Neteo, se escriben algunas guias extras
        //Reestringir Deals de Mesa donde Divisa referencia es diferente de MXN
        Canal canal = (Canal) find(Canal.class, mov.getSucursalOrigen());
        if (NETEO.equals(mov.getTipoDeal()) &&
                Divisa.PESO.equals(canal.getMesaCambio().getDivisaReferencia().getIdDivisa())) {
            if (Divisa.PESO.equals(mov.getDivisa().getIdDivisa())) {
                String mnemonico = Divisa.PESO
                        .equals(mov.getDivisa().getIdDivisa()) ? COMPRA.equals(mov
                        .getTipoOperacion()) ? E + mov.getDivisa().getIdDivisa() + COMNETEO :
                        R + mov.getDivisa().getIdDivisa() + COMNETEO : COMPRA.equals(mov
                        .getTipoOperacion()) ? E + mov.getDivisa().getIdDivisa() + BALNETEO :
                        R + mov.getDivisa().getIdDivisa() + BALNETEO;
                guiasNeteo = seleccionGuiasContablesLiq(mnemonico);
                montoMn = monto;
                for (Iterator iterator = guiasNeteo.iterator(); iterator.hasNext();) {
                    GuiaContableLiq gcl = (GuiaContableLiq) iterator.next();
                    escribirPolizaLiquidacion(mov, monto, montoMn, gcl);
                }
            }
            else if (!Divisa.PESO.equals(mov.getDivisa().getIdDivisa()) &&
                    Constantes.CASH.equals(mov.getTipoFechaValor())) {
                String mnemonico = Divisa.PESO
                        .equals(mov.getDivisa().getIdDivisa()) ? COMPRA.equals(mov
                        .getTipoOperacion()) ? E + (mov.getDivisa().isMetalAmonedado() ?
                        mov.getDivisa().getGrupo() : mov.getDivisa().getIdDivisa()) + COMNETEO :
                        R + (mov.getDivisa().isMetalAmonedado() ? mov.getDivisa().getGrupo() :
                                mov.getDivisa().getIdDivisa()) + COMNETEO :
                        COMPRA.equals(mov.getTipoOperacion()) ? E +
                                (mov.getDivisa().isMetalAmonedado() ? mov.getDivisa().getGrupo() :
                                        mov.getDivisa().getIdDivisa()) + BALNETEO : R +
                                (mov.getDivisa().isMetalAmonedado() ? mov.getDivisa().getGrupo() :
                                        mov.getDivisa().getIdDivisa()) + BALNETEO;
                guiasNeteo = seleccionGuiasContablesLiq(mnemonico);
                montoMn = monto;
                for (Iterator iterator = guiasNeteo.iterator(); iterator.hasNext();) {
                    GuiaContableLiq gcl = (GuiaContableLiq) iterator.next();
                    escribirPolizaLiquidacion(mov, monto, montoMn, gcl);
                }
            }
        }
        //Si no se obtuvieron guias se escribe un error
        if (guias.size() == 0 && guiasNeteo.size() == 0) {
            mov.setStatusProceso(STATUS_ERROR);
            update(mov);
        }
    }

    /**
     * Permite escribir las polizas que corresponden a los asientos contables de casa movimiento.
     *
     * @param gcd <code>dto</code> creado din&aacute;micamente con movimientos contables y guias.
     */
    private void filtrarGuiaContable(GuiaContableDto gcd) {
        if (EXPLOSION_PAC_COMPRAS.equals(gcd.getFaseContable())) {
            for (Iterator iterator = gcd.getGuiasContablesAsientos().iterator();
                 iterator.hasNext();) {
                GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
                //Se escriben las guias que son solo P, cuando la IdDivisa coincide
                if (gcd.getIdDivisa().equals(gca.getMoneda()) &&
                        gcd.getMovimientoContable().getFaseContable().
                                equals(gca.getFaseContable())) {
                    escribirPoliza(gcd.getMovimientoContable(), gca, obtenMonto(
                            gcd.getMovimientoContable(), IMPORTE_DIV),
                            gcd.getMovimientoContable().getDivisa());
                }
                //Se escriben las guias que son P-FechaValor (fecha valor correspondiente), cuando
                // la IdDivisa coincide
                else if (!gcd.getMovimientoContable().getFaseContable().equals(
                        gca.getFaseContable()) && gcd.getIdDivisa().equals(gca.getMoneda()) &&
                        gca.getFaseContable().startsWith(gcd.getMovimientoContable().
                                getFaseContable())) {
                    if (gca.getFaseContable().endsWith(gcd.getMovimientoContable().
                            getTipoFechaValor())) {
                        escribirPoliza(gcd.getMovimientoContable(), gca,
                                obtenMonto(gcd.getMovimientoContable(), IMPORTE_DIV),
                                gcd.getMovimientoContable().getDivisa());
                    }
                }
            }
        }
        else if (EXPLOSION_PAC_COMPRAS_CASO2.equals(gcd.getFaseContable())) {
            for (Iterator iterator = gcd.getGuiasContablesAsientos().iterator();
                 iterator.hasNext();) {
                GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
                //Se escriben las guias que son solo P, cuando la IdDivisa coincide en la forma
                // MXN-idDivisa
                if (!gcd.getIdDivisa().equals(gca.getMoneda()) &&
                        gcd.getMovimientoContable().getFaseContable().equals(
                                gca.getFaseContable()) &&
                        gca.getMoneda().endsWith(gcd.getIdDivisa()) &&
                        !gca.getDescripcion().trim().equals(GUIA_CON_ASIENTO_UTILIDAD_DESC) &&
                        !gca.getDescripcion().trim().equals(GUIA_CON_ASIENTO_PERDIDA_DESC)) {
                    escribirPoliza(gcd.getMovimientoContable(), gca,
                            obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS), getPeso());
                }
                // Se escriben las guias que son P-FechaValor (fecha valor correspondiente), cuando
                // la IdDivisa coincide en la forma MXN-idDivisa
                else if (!gcd.getIdDivisa().equals(gca.getMoneda()) &&
                        !gcd.getMovimientoContable().getFaseContable().equals(
                                gca.getFaseContable()) &&
                        gca.getFaseContable().startsWith(gcd.getMovimientoContable().
                                getFaseContable()) && gca.getMoneda().endsWith(
                        gcd.getIdDivisa()) && !gca.getDescripcion().trim().equals(
                        GUIA_CON_ASIENTO_UTILIDAD_DESC) &&
                        !gca.getDescripcion().trim().equals(GUIA_CON_ASIENTO_PERDIDA_DESC)) {
                    if (gca.getFaseContable().endsWith(gcd.getMovimientoContable().
                            getTipoFechaValor()) &&
                            NETEO.equals(gcd.getMovimientoContable().getTipoDeal())) {
                        Canal canal = (Canal) find(Canal.class,
                                gcd.getMovimientoContable().getSucursalOrigen());
                        if (!Divisa.PESO.equals(canal.getMesaCambio().getDivisaReferencia().
                                getIdDivisa()) && "P-CASH".equals(gca.getFaseContable())) {
                            escribirPolizaPactacionEspecial(gcd.getMovimientoContable(), gca,
                                    obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS),
                                    gcd.getMovimientoContable().getDivisa());
                        }
                        else {
                            escribirPolizaNeteos(gcd.getMovimientoContable(), gca,
                                    obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS),
                                    getPeso());
                        }
                    }
                    else if (gca.getFaseContable().endsWith(
                            gcd.getMovimientoContable().getTipoFechaValor())) {
                        escribirPoliza(gcd.getMovimientoContable(), gca,
                                obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS), getPeso());
                    }
                }
            }
        }
        else if (EXPLOSION_PAC_COMPRAS_CASO_UTILIDADES.equals(gcd.getFaseContable())) {
            for (Iterator iterator = gcd.getGuiasContablesAsientos().iterator();
                 iterator.hasNext();) {
                GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
                if (!gcd.getIdDivisa().equals(gca.getMoneda()) &&
                        gcd.getMovimientoContable().getFaseContable().equals(
                                gca.getFaseContable()) &&
                        gca.getMoneda().endsWith(gcd.getIdDivisa()) &&
                        gca.getDescripcion().trim().equals(GUIA_CON_ASIENTO_UTILIDAD_DESC)) {
                    escribirPoliza(gcd.getMovimientoContable(), gca,
                            obtenMonto(gcd.getMovimientoContable(), IMPORTE_UTILIDADES), getPeso());
                }
            }
        }
        else if (EXPLOSION_PAC_COMPRAS_CASO_PERDIDAS.equals(gcd.getFaseContable())) {
            for (Iterator iterator = gcd.getGuiasContablesAsientos().iterator();
                 iterator.hasNext();) {
                GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
                if (!gcd.getIdDivisa().equals(gca.getMoneda()) &&
                        gcd.getMovimientoContable().getFaseContable().equals(
                                gca.getFaseContable()) &&
                        gca.getMoneda().endsWith(gcd.getIdDivisa()) &&
                        gca.getDescripcion().trim().equals(GUIA_CON_ASIENTO_PERDIDA_DESC)) {
                    escribirPoliza(gcd.getMovimientoContable(), gca,
                            obtenMonto(gcd.getMovimientoContable(), IMPORTE_PERDIDAS), getPeso());
                }
            }
        }
        //no Pesos
        else if (EXPLOSION_LIQ_COMPRAS.equals(gcd.getFaseContable())) {
            for (Iterator iterator = gcd.getGuiasContablesAsientos().iterator();
                 iterator.hasNext();) {
                GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
                if (Divisa.PESO.equals(gcd.getMovimientoContable().getDivisa().getIdDivisa()) &&
                        NETEO.equals(gcd.getMovimientoContable().getTipoDeal()) &&
                        !Constantes.CASH.equals(gcd.getMovimientoContable().getTipoFechaValor())) {
                    logger.debug("Caso no considerado.");

                }
                else {
                    if (gcd.getIdDivisa().equals(gca.getMoneda()) &&
                            gcd.getMovimientoContable().getFaseContable().equals(
                            gca.getFaseContable())) {
                        if (Divisa.PESO.equals(
                                gcd.getMovimientoContable().getDivisa().getIdDivisa())) {
                            escribirPoliza(gcd.getMovimientoContable(), gca,
                                    obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS),
                                    gcd.getMovimientoContable().getDivisa());
                        }
                        else {
                            escribirPoliza(gcd.getMovimientoContable(), gca,
                                    obtenMonto(gcd.getMovimientoContable(), IMPORTE_DIV),
                                    gcd.getMovimientoContable().getDivisa());
                        }
                    }
                    else if (!gcd.getMovimientoContable().getFaseContable().equals(
                            gca.getFaseContable()) && gcd.getIdDivisa().equals(gca.getMoneda()) &&
                            gca.getFaseContable().startsWith(
                                    gcd.getMovimientoContable().getFaseContable())) {
                        if (gca.getFaseContable().endsWith(gcd.getMovimientoContable().
                                getTipoFechaValor()) && !Constantes.CASH.equals(
                                gcd.getMovimientoContable().getTipoFechaValor())) {
                            if (Divisa.PESO.equals(gcd.getMovimientoContable().getDivisa().
                                    getIdDivisa())) {
                                escribirPoliza(gcd.getMovimientoContable(), gca,
                                        obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS),
                                        gcd.getMovimientoContable().getDivisa());
                            }
                            else {
                                escribirPoliza(gcd.getMovimientoContable(), gca,
                                        obtenMonto(gcd.getMovimientoContable(), IMPORTE_DIV),
                                        gcd.getMovimientoContable().getDivisa());
                            }
                        }
                    }
                }
            }
        }
        //
        else if (EXPLOSION_LIQ_COMPRAS_CASO2.equals(gcd.getFaseContable())) {
            for (Iterator iterator = gcd.getGuiasContablesAsientos().iterator();
                 iterator.hasNext();) {
                GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
                if (!gcd.getIdDivisa().equals(gca.getMoneda()) &&
                        gcd.getMovimientoContable().getFaseContable().equals(
                                gca.getFaseContable()) &&
                        gca.getMoneda().endsWith(gcd.getIdDivisa())) {
                    Canal canal = (Canal) find(Canal.class, gcd.getMovimientoContable().
                            getSucursalOrigen());
                    if (Divisa.PESO.equals(canal.getMesaCambio().getDivisaReferencia().
                            getIdDivisa())) {
                        if (NETEO.equals(gcd.getMovimientoContable().getTipoDeal())) {
                            if (Constantes.CASH.equals(gcd.getMovimientoContable().
                                    getTipoFechaValor())) {
                                escribirPoliza(gcd.getMovimientoContable(), gca,
                                        obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS),
                                        getPeso());
                            }
                        }
                        else {
                            escribirPoliza(gcd.getMovimientoContable(), gca,
                                    obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS),
                                    getPeso());
                        }
                    }
                }
                else if (!gcd.getIdDivisa().equals(gca.getMoneda()) &&
                        !gcd.getMovimientoContable().getFaseContable().equals(
                                gca.getFaseContable()) &&
                        gca.getFaseContable().startsWith(gcd.getMovimientoContable().
                                getFaseContable()) && gca.getMoneda().endsWith(gcd.getIdDivisa())) {
                    if (gca.getFaseContable().endsWith(gcd.getMovimientoContable().
                            getTipoFechaValor()) &&
                            !Constantes.CASH.equals(gcd.getMovimientoContable().
                                    getTipoFechaValor())) {
                        Canal canal = (Canal) find(Canal.class, gcd.getMovimientoContable().
                                getSucursalOrigen());
                        if (Divisa.PESO.equals(canal.getMesaCambio().getDivisaReferencia().
                                getIdDivisa()) &&
                                !NETEO.equals(gcd.getMovimientoContable().getTipoDeal())) {
                            escribirPoliza(gcd.getMovimientoContable(), gca, obtenMonto(
                                    gcd.getMovimientoContable(), IMPORTE_PESOS), getPeso());
                        }
                    }
                }
            }
            if (NETEO.equals(gcd.getMovimientoContable().getTipoDeal()) &&
                    (FASE_LIQUIDACION_2B.equals(gcd.getMovimientoContable().getFaseContable()) ||
                            FASE_LIQUIDACION_L4.equals(gcd.getMovimientoContable().
                                    getFaseContable())) && Constantes.CASH.equals(
                    gcd.getMovimientoContable().getTipoFechaValor())) {
                for (Iterator iterator = gcd.getGuiasContablesAsientosNeteos().iterator();
                     iterator.hasNext();) {
                    GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
                    escribirPoliza(gcd.getMovimientoContable(), gca,
                            obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS), getPeso());
                }
            }
        }
        else if (EXPLOSION_LIQ_VENTAS.equals(gcd.getFaseContable())) {
            for (Iterator iterator = gcd.getGuiasContablesAsientos().iterator();
                 iterator.hasNext();) {
                GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
                if (Divisa.PESO.equals(gcd.getMovimientoContable().getDivisa().getIdDivisa()) &&
                        NETEO.equals(gcd.getMovimientoContable().getTipoDeal()) &&
                        !Constantes.CASH.equals(gcd.getMovimientoContable().getTipoFechaValor())) {
                    logger.debug("No considerado");

                }
                else {
                    //Se escriben las guias que son solo L-, cuando la IdDivisa coincide
                    if (gcd.getIdDivisa().equals(gca.getMoneda()) &&
                            gcd.getMovimientoContable().getFaseContable().equals(
                                    gca.getFaseContable())) {
                        if (Divisa.PESO.equals(gcd.getMovimientoContable().getDivisa().
                                getIdDivisa())) {
                            escribirPoliza(gcd.getMovimientoContable(), gca,
                                    obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS),
                                    gcd.getMovimientoContable().getDivisa());
                        }
                        else {
                            escribirPoliza(gcd.getMovimientoContable(), gca,
                                    obtenMonto(gcd.getMovimientoContable(), IMPORTE_DIV),
                                    gcd.getMovimientoContable().getDivisa());
                        }
                    }
                    else
                    if (!gcd.getMovimientoContable().getFaseContable().equals(
                            gca.getFaseContable()) && gcd.getIdDivisa().equals(gca.getMoneda()) &&
                            gca.getFaseContable().startsWith(gcd.getMovimientoContable().
                                    getFaseContable())) {
                        if (gca.getFaseContable().endsWith(gcd.getMovimientoContable().
                                getTipoFechaValor()) && !Constantes.CASH.equals(gcd.
                                getMovimientoContable().getTipoFechaValor())) {
                            if (Divisa.PESO.equals(gcd.getMovimientoContable().getDivisa().
                                    getIdDivisa())) {
                                escribirPoliza(gcd.getMovimientoContable(), gca,
                                        obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS),
                                        gcd.getMovimientoContable().getDivisa());
                            }
                            else {
                                escribirPoliza(gcd.getMovimientoContable(), gca,
                                        obtenMonto(gcd.getMovimientoContable(), IMPORTE_DIV),
                                        gcd.getMovimientoContable().getDivisa());
                            }
                        }
                    }
                }
            }
        }
        else if (EXPLOSION_LIQ_VENTAS_CASO2.equals(gcd.getFaseContable())) {
            for (Iterator iterator = gcd.getGuiasContablesAsientos().iterator();
                 iterator.hasNext();) {
                GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
                if (gcd.getMovimientoContable().getFaseContable().equals(gca.getFaseContable()) &&
                        !gcd.getIdDivisa().equals(gca.getMoneda()) &&
                        gca.getMoneda().endsWith(gcd.getIdDivisa()) &&
                        !gca.getDescripcion().trim().equals(GUIA_CON_ASIENTO_COMISION_DESC) &&
                        !gca.getDescripcion().trim().equals(GUIA_CON_ASIENTO_IVA_COM_DESC)) {
                    Canal canal = (Canal) find(Canal.class, gcd.getMovimientoContable().
                            getSucursalOrigen());
                    if (Divisa.PESO.equals(canal.getMesaCambio().getDivisaReferencia().
                            getIdDivisa())) {
                        if (NETEO.equals(gcd.getMovimientoContable().getTipoDeal())) {
                            if (Constantes.CASH.equals(gcd.getMovimientoContable().
                                    getTipoFechaValor())) {
                                escribirPoliza(gcd.getMovimientoContable(), gca,
                                        obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS),
                                        getPeso());
                            }
                        }
                        else {
                            escribirPoliza(gcd.getMovimientoContable(), gca,
                                    obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS),
                                    getPeso());
                        }
                    }
                }
                else if (!gcd.getIdDivisa().equals(gca.getMoneda()) &&
                        !gcd.getMovimientoContable().getFaseContable().equals(
                                gca.getFaseContable()) &&
                        gca.getFaseContable().startsWith(gcd.getMovimientoContable().
                                getFaseContable()) &&
                        gca.getMoneda().endsWith(gcd.getIdDivisa()) &&
                        !gca.getDescripcion().trim().equals(GUIA_CON_ASIENTO_COMISION_DESC) &&
                        !gca.getDescripcion().trim().equals(GUIA_CON_ASIENTO_IVA_COM_DESC)) {
                    if (gca.getFaseContable().endsWith(gcd.getMovimientoContable().
                            getTipoFechaValor()) &&
                            !Constantes.CASH.equals(gcd.getMovimientoContable().
                                    getTipoFechaValor())) {
                        Canal canal = (Canal) find(Canal.class, gcd.getMovimientoContable().
                                getSucursalOrigen());
                        if (Divisa.PESO.equals(canal.getMesaCambio().getDivisaReferencia().
                                getIdDivisa()) &&
                                !NETEO.equals(gcd.getMovimientoContable().getTipoDeal())) {
                            escribirPoliza(gcd.getMovimientoContable(), gca,
                                    obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS),
                                    getPeso());
                        }
                    }
                }
            }
            if (NETEO.equals(gcd.getMovimientoContable().getTipoDeal()) &&
                    (FASE_LIQUIDACION_2B.equals(gcd.getMovimientoContable().getFaseContable()) ||
                            FASE_LIQUIDACION_L4.equals(gcd.getMovimientoContable().
                                    getFaseContable())) &&
                    Constantes.CASH.equals(gcd.getMovimientoContable().getTipoFechaValor())) {
                for (Iterator iterator = gcd.getGuiasContablesAsientosNeteos().iterator();
                     iterator.hasNext();) {
                    GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
                    escribirPoliza(gcd.getMovimientoContable(), gca,
                            obtenMonto(gcd.getMovimientoContable(), IMPORTE_PESOS), getPeso());
                }
            }
        }
        else if (EXPLOSION_LIQ_VENTAS_CASO_COMISION.equals(gcd.getFaseContable())) {
            for (Iterator iterator = gcd.getGuiasContablesAsientos().iterator();
                 iterator.hasNext();) {
                GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
                if (!gcd.getIdDivisa().equals(gca.getMoneda()) && gca.getMoneda().endsWith(
                        gcd.getIdDivisa()) &&
                        gca.getDescripcion().trim().equals(GUIA_CON_ASIENTO_COMISION_DESC)) {
                    escribirPoliza(gcd.getMovimientoContable(), gca, obtenMonto(
                            gcd.getMovimientoContable(), IMPORTE_COM_SIN_IVA), getPeso());
                    Canal canal = (Canal) find(Canal.class, gcd.getMovimientoContable().
                            getSucursalOrigen());
                    if (Divisa.PESO.equals(canal.getMesaCambio().getDivisaReferencia().
                            getIdDivisa()) && NETEO.equals(
                            gcd.getMovimientoContable().getTipoDeal()) &&
                            !Constantes.CASH.equals(gcd.getMovimientoContable().
                                    getTipoFechaValor())) {
                        escribirPolizaComision(gcd.getMovimientoContable(), gca,
                                obtenMonto(gcd.getMovimientoContable(), IMPORTE_COM_SIN_IVA) +
                                        obtenMonto(gcd.getMovimientoContable(), IMPORTE_IVA),
                                getPeso());
                    }
                }
            }
            for (Iterator iterator = gcd.getGuiasContablesAsientos().iterator();
                 iterator.hasNext();) {
                GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
                if (!gcd.getIdDivisa().equals(gca.getMoneda()) &&
                        gca.getMoneda().endsWith(gcd.getIdDivisa()) &&
                        gca.getDescripcion().trim().equals(GUIA_CON_ASIENTO_IVA_COM_DESC)) {
                    escribirPoliza(gcd.getMovimientoContable(), gca,
                            obtenMonto(gcd.getMovimientoContable(), IMPORTE_IVA), getPeso());
                }
            }
        }
    }

    /**
     * Escribe un registro de poliza despu&eacute;s de haber aplicado varios filtros, en caso de no
     * poder escribirlo se marca con error en el status del movimientos Contable.
     *
     * @param mov   el movimiento Contable
     * @param gca   la guia contable asiento correspondiente a &eacute;sta p&oacute;liza
     * @param monto monto calculado para  &eacute;ste registro.
     * @param div   divisa correspondiente a &eacute;ste registro.
     */
    private void escribirPolizaComision(MovimientoContable mov, GuiaContableAsiento gca,
                                        double monto, Divisa div) {
        if (monto != 0) {
            Poliza poliza = new Poliza();
            poliza.setFechaValor(mov.getFechaValor());
            if (!modoSap) {
                poliza.setCuentaContable(FIJO_CUENTA_CONTABLE);
            }
            else {
                poliza.setCuentaContable(FIJO_CUENTA_CONTABLE_SAP);
            }
            poliza.setEntidad(gca.getEntidad());
            poliza.setCargoAbono(CARGO);
            poliza.setDivisa(div);
            poliza.setImporte(monto);
            poliza.setIdDeal(mov.getIdDeal());
            poliza.setReferencia("COMPLEMENTO IMPORTE DE LA COMISION CON IVA");
            poliza.setFechaCreacion(getFechaHoy().getTime());
            poliza.setTipoDeal(mov.getTipoDeal());
            poliza.setSucursalOrigen(mov.getSucursalOrigen());
            poliza.setCliente("");
            poliza.setFechaProcesamiento(null);
            poliza.setFolioDetalle(mov.getFolioDetalle());
            poliza.setTipoCambio(mov.getTipoCambio());
            poliza.setStatusProceso(STATUS_NO_PROCESADO);
            poliza.setNoContratoSica(findTipoOperacionDeal(mov.getIdDeal()).get(CLIENTE).
                    toString());
            poliza.setIdDealPosicion(mov.getIdDealPosicion());
            storePoliza(poliza);
        }
    }

    /**
     * Escribe un registro de poliza despu&eacute;s de haber aplicado varios filtros, en caso de no
     * poder escribirlo se marca con error en el status del movimientos Contable.
     *
     * @param mov   el movimiento Contable
     * @param gca   la guia contable asiento correspondiente a &eacute;sta p&oacute;liza
     * @param monto monto calculado para  &eacute;ste registro.
     * @param div   divisa correspondiente a &eacute;ste registro.
     */
    private void escribirPolizaPactacionEspecial(MovimientoContable mov, GuiaContableAsiento gca,
                                                 double monto, Divisa div) {
        if (monto != 0) {
            Poliza poliza = new Poliza();
            poliza.setFechaValor(mov.getFechaValor());
            if (!modoSap) {
                poliza.setCuentaContable(FIJO_CUENTA_CONTABLE);
            }
            else {
                poliza.setCuentaContable(FIJO_CUENTA_CONTABLE_SAP);
            }
            if (modoSap) {
                poliza.setEntidad("0000-00000000");
            }
            else {
                poliza.setEntidad(FIJO_ENTIDAD);
            }
            poliza.setCargoAbono(gca.getCargoAbono());
            poliza.setDivisa(getPeso());
            poliza.setImporte(monto);
            poliza.setIdDeal(mov.getIdDeal());
            poliza.setReferencia(mov.getFaseContable() + " " + findTipoOperacionDeal(
                    mov.getIdDeal()).get("Compra").toString() + " " + gca.getDescripcion());
            poliza.setFechaCreacion(getFechaHoy().getTime());
            poliza.setTipoDeal(mov.getTipoDeal());
            poliza.setSucursalOrigen(mov.getSucursalOrigen());
            poliza.setCliente("");
            poliza.setFechaProcesamiento(null);
            poliza.setFolioDetalle(mov.getFolioDetalle());
            poliza.setTipoCambio(mov.getTipoCambio());
            poliza.setStatusProceso(STATUS_NO_PROCESADO);
            poliza.setNoContratoSica(findTipoOperacionDeal(mov.getIdDeal()).get(CLIENTE).
                    toString());
            poliza.setIdDealPosicion(mov.getIdDealPosicion());
            storePoliza(poliza);
        }
    }

    /**
     * Escribe un registro de poliza despu&eacute;s de haber aplicado varios filtros, en caso de no
     * poder escribirlo se marca con error en el status del movimientos Contable.
     *
     * @param mov   el movimiento Contable
     * @param gca   la guia contable asiento correspondiente a &eacute;sta p&oacute;liza
     * @param monto monto calculado para  &eacute;ste registro.
     * @param div   divisa correspondiente a &eacute;ste registro.
     */
    private void escribirPoliza(MovimientoContable mov, GuiaContableAsiento gca, double monto,
                                Divisa div) {
        if (monto != 0) {
            Poliza poliza = new Poliza();
            poliza.setFechaValor(mov.getFechaValor());
            poliza.setCuentaContable(gca.getCuentaContable());
            poliza.setEntidad(gca.getEntidad());
            poliza.setCargoAbono(gca.getCargoAbono());
            poliza.setDivisa(div);
            poliza.setImporte(monto);
            poliza.setIdDeal(mov.getIdDeal());
            poliza.setReferencia(mov.getFaseContable() + " " + findTipoOperacionDeal(
                    mov.getIdDeal()).get("Compra").toString() + " " + gca.getDescripcion());
            poliza.setFechaCreacion(getFechaHoy().getTime());
            poliza.setTipoDeal(mov.getTipoDeal());
            poliza.setSucursalOrigen(mov.getSucursalOrigen());
            poliza.setCliente("");
            poliza.setFechaProcesamiento(null);
            poliza.setFolioDetalle(mov.getFolioDetalle());
            poliza.setTipoCambio(mov.getTipoCambio());
            poliza.setStatusProceso(STATUS_NO_PROCESADO);
            poliza.setNoContratoSica(findTipoOperacionDeal(mov.getIdDeal()).get(CLIENTE).
                    toString());
            poliza.setIdDealPosicion(mov.getIdDealPosicion());
            storePoliza(poliza);
        }
    }

    /**
     * Escribe un registro de poliza despu&eacute;s de haber aplicado varios filtrospara movimientos
     * Contable que tienen Neteo, en caso de no poder escribirlo se marca con error en el status del
     * movimientos Contable.
     *
     * @param mov   el movimiento Contable
     * @param gca   la guia contable asiento correspondiente a &eacute;sta p&oacute;liza
     * @param monto monto calculado para  &eacute;ste registro.
     * @param div   divisa correspondiente a &eacute;ste registro.
     */
    private void escribirPolizaNeteos(MovimientoContable mov, GuiaContableAsiento gca, double monto,
                                      Divisa div) {
        if (monto != 0) {
            Map valores = definirCamposNeteo(mov, gca);
            Poliza poliza = new Poliza();
            poliza.setFechaValor(mov.getFechaValor());
            poliza.setCuentaContable(valores.get(CUENTA_CONTABLE).toString());
            poliza.setEntidad(valores.get(ENTIDAD).toString());
            poliza.setCargoAbono(valores.get(CARGO_ABONO).toString());
            poliza.setDivisa(div);
            poliza.setImporte(monto);
            poliza.setIdDeal(mov.getIdDeal());
            poliza.setReferencia(mov.getFaseContable() + " " + findTipoOperacionDeal(
                    mov.getIdDeal()).get("Compra").toString() + " " +
                    valores.get(DESCRIPCION).toString());
            poliza.setFechaCreacion(getFechaHoy().getTime());
            poliza.setTipoDeal(mov.getTipoDeal());
            poliza.setSucursalOrigen(mov.getSucursalOrigen());
            poliza.setCliente("");
            poliza.setFechaProcesamiento(null);
            poliza.setFolioDetalle(mov.getFolioDetalle());
            poliza.setTipoCambio(mov.getTipoCambio());
            poliza.setStatusProceso(STATUS_NO_PROCESADO);
            poliza.setNoContratoSica(findTipoOperacionDeal(mov.getIdDeal()).get(CLIENTE).
                    toString());
            poliza.setIdDealPosicion(mov.getIdDealPosicion());
            storePoliza(poliza);
        }
    }

    /**
     * Permite escribir datos especificos para una P&oacute;liza que proviene de un Deal con Neteo.
     *
     * @param mov el movimiento Contable
     * @param gca la gu&iacute;a contable asiento
     * @return <code>Map</code> mapa con llaves que indican la Cuenta Contable, la Entidad la
               Descripci&oacute;n y si es Cargo o Abono.
     */
    private Map definirCamposNeteo(MovimientoContable mov, GuiaContableAsiento gca) {
        HashMap valores = new HashMap();
        if (gca.getFaseContable().startsWith(FASE_PACTACION) &&
                !"CASH".equals(mov.getTipoFechaValor())) {
            if (!modoSap) {
                valores.put(CUENTA_CONTABLE, FIJO_CUENTA_CONTABLE);
            }
            else {
                valores.put(CUENTA_CONTABLE, FIJO_CUENTA_CONTABLE_SAP);
            }
            valores.put(ENTIDAD, gca.getEntidad());
            valores.put(DESCRIPCION, gca.getDescripcion());
            valores.put(CARGO_ABONO, gca.getCargoAbono());
            return valores;
        }
        valores.put(CUENTA_CONTABLE, gca.getCuentaContable());
        valores.put(ENTIDAD, gca.getEntidad());
        valores.put(DESCRIPCION, gca.getDescripcion());
        valores.put(CARGO_ABONO, gca.getCargoAbono());
        return valores;

    }

    /**
     * Filtra , dependiendo el tipo de movimiento Contable , para hacer la busqueda con los tres
     * diferentes criterios:
     * <p> Deal de Sucursal</p>
     * <p> Deal con liquidaci&oacute;n Especial</p>
     * <p> el resto de los casos </p>
     *
     * @param mov el movimiento Contable que prove&eacute; los valores en la b&uacute;squeda
     * @return una lista con los resultados de la busqueda de las guias Contables Liquidaci&oacute;n
     */
    private List cargarGuiasContLiq(MovimientoContable mov) {
        if (SUCURSAL.equals(mov.getTipoDeal())) {
            return seleccionGuiasContablesLiqSucursal(mov);
        }
        else if (mov.getLiquidacionEspecial() != null) {
            return seleccionGuiasContablesLiqEspecial(mov);
        }
        else {
            return seleccionGuiasContablesLiq(mov.getMnemonico());
        }
    }

    /**
     * Escribe un registro de poliza despu&eacute;s de haber aplicado varios filtros, en caso de no
     * poder escribirlo se marca con error en el status del movimientos Contable.
     *
     * @param mov     el movimiento Contable
     * @param gcl     la guia contable liquidaci&oacute;n  correspondiente a &eacute;sta
     *                  p&oacute;liza
     * @param monto   monto calculado para  &eacute;ste registro.
     * @param montoMn montoMn calculado para  &eacute;ste registro.
     */
    private void escribirPolizaLiquidacion(MovimientoContable mov, double monto, double montoMn,
                                           GuiaContableLiq gcl) {
        double importe;
        if (SUCURSAL.equals(mov.getTipoDeal()) || mov.getLiquidacionEspecial() != null) {
            importe = gcl.getMoneda().startsWith(Divisa.PESO) ? monto : montoMn;
        }
        else {
            importe = Divisa.PESO.equals(mov.getDivisa().getIdDivisa()) ? monto : montoMn;
        }
        Poliza poliza = new Poliza();
        poliza.setFechaValor(mov.getFechaValor());
        poliza.setCuentaContable(gcl.getCuentaContable().trim());
        poliza.setEntidad(gcl.getEntidad().trim());
        poliza.setCargoAbono(gcl.getCargoAbono().trim());
        if (importe == monto) {
            poliza.setDivisa(getPeso());
            poliza.setTipoCambio(mov.getTipoCambio());
        }
        else {
            poliza.setDivisa(mov.getDivisa());
            poliza.setTipoCambio(mov.getTipoCambio());
        }
        poliza.setImporte(importe);
        poliza.setIdDeal(mov.getIdDeal());
        poliza.setReferencia(mov.getFaseContable() + " " + findTipoOperacionDeal(mov.getIdDeal()).
                get("Compra").toString() + " " + gcl.getDescripcion().trim());
        poliza.setFechaCreacion(getFechaHoy().getTime());
        poliza.setTipoDeal(mov.getTipoDeal().trim());
        poliza.setSucursalOrigen(mov.getSucursalOrigen().trim());
        poliza.setCliente("");
        poliza.setFechaProcesamiento(null);
        poliza.setFolioDetalle(mov.getFolioDetalle());
        poliza.setStatusProceso(STATUS_NO_PROCESADO);
        poliza.setNoContratoSica(findTipoOperacionDeal(mov.getIdDeal()).get(CLIENTE).toString().
                trim());
        storePoliza(poliza);
    }

    /**
     * Recorre la lista de detalles y hace el neteo con Pesos para ser insertado en la
     * generaci&oacute;n de movimientos contables.
     *
     * @param dets La lista de detalles de deal.
     * @return la lista con el resultado.
     */
    private List doNeteo(List dets) {
        List deals = new ArrayList();
        List detsTmp = new ArrayList();
        for (Iterator it = dets.iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (!deals.contains(det.getDeal())) {
                deals.add(det.getDeal());
            }
        }
        for (Iterator it = deals.iterator(); it.hasNext();) {
            Deal deal = (Deal) it.next();
            //Pregunta si el Deal tiene Neteo
            if (deal.isNeteo() && deal.isDeSucursal()) {
                double entregamos = 0.0;
                double recibimos = 0.0;
                for (Iterator it1 = deal.getDetalles().iterator(); it1.hasNext();) {
                    DealDetalle det1 = (DealDetalle) it1.next();
                    if ((!Divisa.PESO.equals(det1.getDivisa().getIdDivisa())) &&
                            det1.isRecibimos()) {
                        recibimos += det1.getMonto() * det1.getTipoCambio();
                        detsTmp.add(det1);
                    }
                    else
                    if ((!Divisa.PESO.equals(det1.getDivisa().getIdDivisa())) &&
                            !det1.isRecibimos()) {
                        entregamos += det1.getMonto() * det1.getTipoCambio();
                        detsTmp.add(det1);
                    }
                    else if (Divisa.PESO.equals(det1.getDivisa().getIdDivisa())) {
                        DealDetalle det2 = DealDetalle.duplicar(det1);
                        if (checarPesosFormaLiquidacion(det2)) {
                            if (det2.isRecibimos()) {
                                det2.setMnemonico(RECIBIMOSCOMNETEO);
                            }
                            else {
                                det2.setMnemonico(ENTREGAMOSCOMNETEO);
                            }
                            detsTmp.add(det2);
                        }
                        detsTmp.add(det1);
                    }
                }
                DealDetalle det = (DealDetalle) deal.getDetalles().get(0);
                DealDetalle detNeteoR = DealDetalle.duplicar(det);
                detNeteoR.setDivisa((Divisa) find(Divisa.class, Divisa.PESO));
                detNeteoR.setMonto((entregamos > recibimos) ? recibimos : entregamos);
                detNeteoR.setRecibimos(true);
                detNeteoR.setComisionCobradaMxn(0.00);
                detNeteoR.setComisionCobradaUsd(0.00);
                detNeteoR.setMnemonico(getMnemonicoNeteo(detNeteoR));
                DealDetalle detNeteoE = DealDetalle.duplicar(det);
                detNeteoE.setDivisa((Divisa) find(Divisa.class, Divisa.PESO));
                detNeteoE.setMonto((entregamos > recibimos) ? recibimos : entregamos);
                detNeteoE.setRecibimos(false);
                detNeteoE.setComisionCobradaMxn(0.00);
                detNeteoE.setComisionCobradaUsd(0.00);
                detNeteoE.setMnemonico(getMnemonicoNeteo(detNeteoE));
            }
            else {
                for (Iterator it1 = dets.iterator(); it1.hasNext();) {
                    DealDetalle dd = (DealDetalle) it1.next();
                    if (dd.getDeal().getIdDeal() == deal.getIdDeal()) {
                        if (!DealDetalle.STATUS_DET_CANCELADO.equals(dd.getStatusDetalleDeal())) {
                            detsTmp.add(dd);
                        }
                    }
                }
            }
        }
        return detsTmp;
    }

    /**
     * Recibe una lista de detalles de deal y los acomoda por Pesos.
     *
     * @param dd la lista con los detalles
     * @return lista el resultado del sort
     */
    private List sortDealDetallesByPesos(List dd) {
        Collections.sort(dd, new Comparator() {
            public int compare(Object d1, Object d2) {
                DealDetalle det1 = (DealDetalle) d1;
                DealDetalle det2 = (DealDetalle) d2;
                if (det1.getDeal().getIdDeal() == det2.getDeal().getIdDeal()) {
                    if (Divisa.PESO.equals(det1.getDivisa().getIdDivisa())) {
                        return -1;
                    }
                    else if (Divisa.PESO.equals(det2.getDivisa().getIdDivisa())) {
                        return 1;
                    }
                    else {
                        return 0;
                    }
                }
                else {
                    return det1.getDeal().getIdDeal() < det2.getDeal().getIdDeal() ? -1 : 1;
                }
            }
        });
        return dd;
    }

    /**
     * Genera una instancia de Movimiento Contable Detalle recibiendo el movimiento Contable , una
     * Divisa, un tipo y un monto.
     *
     * @param mov   el movimiento Contable
     * @param div   la divisa
     * @param tipo  el tipo de movimiento Contable
     * @param monto el monto
     * @return un detalle de Movimiento Contable
     */
    private MovimientoContableDetalle creaMovimientoDetalle(MovimientoContable mov, Divisa div,
                                                            String tipo, double monto) {
        MovimientoContableDetalle movDet = new MovimientoContableDetalle();
        movDet.setMovimientoContable(mov);
        movDet.setDivisa(div);
        movDet.setMonto(Redondeador.redondear2Dec(monto));
        movDet.setMonto(Math.abs(movDet.getMonto()));
        movDet.setTipoDetalle(tipo);
        return movDet;
    }

    /**
     * Genera una instancia de Movimiento Contable Detalle recibiendo el movimiento Contable ,
     * una registro de Utilidad, un tipo y un monto.
     *
     * @param mov   el movimiento Contable
     * @param div   la divisa
     * @param tipo  el tipo de movimiento Contable
     * @param monto el monto
     * @return un detalle de Movimiento Contable
     */
    private MovimientoContableDetalle creaMovimientoDetalleUtilidad(MovimientoContable mov,
                                                                    Divisa div, String tipo,
                                                                    double monto) {
        MovimientoContableDetalle movDet = new MovimientoContableDetalle();
        movDet.setMovimientoContable(mov);
        movDet.setDivisa(div);
        movDet.setMonto(Redondeador.redondear2Dec(monto));
        movDet.setTipoDetalle(tipo);
        return movDet;
    }

    /**
     * Checa si el detalle de pesos es el de menor monto en el deal.
     *
     * @param det detalle de pesos
     * @return true es el de menor monto.
     */
    private boolean checarPesosFormaLiquidacion(DealDetalle det) {
        List dealDetalles = new ArrayList();
        boolean value = false;
        for (Iterator iterator = det.getDeal().getDetalles().iterator(); iterator.hasNext();) {
            DealDetalle de = (DealDetalle) iterator.next();
            if (Divisa.PESO.equals(de.getDivisa().getIdDivisa())) {
                dealDetalles.add(de);
            }
        }
        for (Iterator iterator = dealDetalles.iterator(); iterator.hasNext();) {
            DealDetalle de = (DealDetalle) iterator.next();
            value = de.getMonto() < det.getMonto();
        }
        return value;
    }

    /**
     * Asigna Mnemonico para un Deal con Neteo dependiendo si es de pesos , si es el menor de
     * ellos o si es divisa.
     *
     * @param det el detalle de deal que checa
     * @return el mnemonico resultado de la l&oacute;gica.
     */
    private String getMnemonicoNeteo(DealDetalle det) {
        if (Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
            if (checarPesosFormaLiquidacion(det)) {
                if (det.isRecibimos()) {
                    return RECIBIMOSCOMNETEO;
                }
                else {
                    return ENTREGAMOSCOMNETEO;
                }
            }
            if (det.isRecibimos()) {
                return RECIBIMOSBALNETEO;
            }
            else {
                return ENTREGAMOSBALNETEO;
            }
        }
        else {
            if (det.isRecibimos()) {
                return R + det.getDivisa().getIdDivisa() + TRFBOFAM;
            }
            else {
                return E + det.getDivisa().getIdDivisa() + TRFBOFAM;
            }
        }
    }

    /**
     * Regresa el monto dependiendo el tipo de detalle que recibe
     *
     * @param mov     el movimiento Contable
     * @param detalle el tipo de Detalle del que se quiere el monto
     * @return un monto
     */
    private double obtenMonto(MovimientoContable mov, String detalle) {
        if (mov.getMovimientoContableDetalles() == null) {
            return 0;
        }
        else {
            for (Iterator iterator = mov.getMovimientoContableDetalles().iterator();
                 iterator.hasNext();) {
                MovimientoContableDetalle movDet = (MovimientoContableDetalle) iterator.next();
                if (detalle.trim().equals(movDet.getTipoDetalle().trim())) {
                    return Math.abs(movDet.getMonto());
                }
            }
        }
        return 0;
    }

    //Servicios y Consultas a base de datos

    /**
     * Selecciona los movimientos Contables
     *
     * @param fase la fase que se consulta
     * @return una lista con los resultados de la busqueda en Movimientos Contables
     */
    private List seleccionMovimientosContablesPorFase(String fase) {
        return findMovimientoPorFase(fase);
    }

    /**
     * Selecciona las guias Contables asientos por fase , tipo de Operaci&oacute;n y idDivisa
     * la busqueda se realiza  con los criterios de la siguiente forma:
     * <p>fase%</p> <p>tipoOperacion</p> <p>%idDivisa%</p>
     *
     * @param fase          describe la fase de la consulta
     * @param tipoOperacion describe el tipo de Operaci&oacute;n (Compra o Venta) de la consulta
     * @param idDivisa      describe la Divisa de la consulta
     * @return una lista con los resultados de la busqueda
     */
    private List seleccionGuiasContablesAsientos(String fase, String tipoOperacion,
                                                 String idDivisa) {
        //return findGuiaContableAsiento(fase+"%", tipoOperacion, "%"+idDivisa+"%");
        List tmp = new ArrayList();
        for (Iterator iterator = getGuiasContablesAsientos().iterator(); iterator.hasNext();) {
            GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
            if (fase.equals(gca.getFaseContable()) || gca.getFaseContable().startsWith(fase)) {
                if (tipoOperacion.equals(gca.getTipoOperacion())) {
                    if (gca.getMoneda() != null) {
                        if (idDivisa.equals(gca.getMoneda()) ||
                                gca.getMoneda().endsWith(idDivisa)) {
                            //if (idDivisa.equals(gca.getMoneda())) {
                            tmp.add(gca);
                        }
                    }
                }
            }
        }
        return tmp;
    }

    /**
     * Selecciona las guias Contables asientos por fase , tipo de Operaci&oacute;n y idDivisa
     * la busqueda se realiza  con los criterios de la siguiente forma:
     * <p>fase%</p> <p>tipoOperacion</p> <p>%idDivisa%</p>
     *
     * @param fase          describe la fase de la consulta
     * @param tipoOperacion describe el tipo de Operaci&oacute;n (Compra o Venta) de la consulta
     * @param idDivisa      describe la Divisa de la consulta
     * @return una lista con los resultados de la busqueda
     */
    private List seleccionGuiasContablesAsientosPactacion(String fase, String tipoOperacion,
                                                          String idDivisa) {
        List tmp = new ArrayList();
        for (Iterator iterator = getGuiasContablesAsientos().iterator(); iterator.hasNext();) {
            GuiaContableAsiento gca = (GuiaContableAsiento) iterator.next();
            if (fase.equals(gca.getFaseContable()) || gca.getFaseContable().startsWith(fase)) {
                if (tipoOperacion.equals(gca.getTipoOperacion())) {
                    if (gca.getMoneda() != null) {
                        if (idDivisa.equals(gca.getMoneda()) ||
                                gca.getMoneda().endsWith(idDivisa)) {
                            tmp.add(gca);
                        }
                    }
                }
            }
        }
        return tmp;
    }

    /**
     * Selecciona las guias Contables asientos por fase , tipo de Operaci&oacute;n y idDivisa
     * la busqueda se realiza  con los criterios de la siguiente forma:
     * <p>fase%</p> <p>tipoOperacion</p>
     *
     * @param fase          describe la fase de la consulta
     * @param tipoOperacion describe el tipo de Operaci&oacute;n (Compra o Venta) de la consulta
     * @return una lista con los resultados de la busqueda
     */
    private List seleccionGuiasContablesAsientosSinMoneda(String fase, String tipoOperacion) {
        return findGuiaContableAsientoSinMoneda(fase + "%", tipoOperacion);
    }

    /**
     * Selecciona las guias contables de liquidaci&oacute;n por mnemonico
     *
     * @param mnemonico describe el mnemonico de la consulta
     * @return una lista con los resultados de la busqueda
     */
    private List seleccionGuiasContablesLiq(String mnemonico) {
        //return findGuiaContableLiq(mnemonico);
        List tmp = new ArrayList();
        for (Iterator iterator = getGuiasContablesLiquidacion().iterator(); iterator.hasNext();) {
            GuiaContableLiq gcl = (GuiaContableLiq) iterator.next();
            if (mnemonico.equals(gcl.getMnemonico())) {
                tmp.add(gcl);
            }
        }
        return tmp;
    }

    /**
     * Selecciona las guias contables de liquidaci&oacute;n por movimiento Contable
     *
     * @param mov movimiento Contable de la consulta
     * @return una lista con los resultados de la busqueda
     */
    private List seleccionGuiasContablesLiqSucursal(MovimientoContable mov) {
        return findGuiaContableLiqSucursal(mov);
    }

    /**
     * Selecciona las guias contables de liquidaci&oacute;n por movimiento Contable para
     * Liquidaci&oacute;n especial
     *
     * @param mov movimiento Contable de la consulta
     * @return una lista con los resultados de la busqueda
     */
    private List seleccionGuiasContablesLiqEspecial(MovimientoContable mov) {
        return findGuiaContableLiqEspecial(mov);
    }

    /**
     * Regresa los movimientos Contables pertenecientes a esa fase
     *
     * @param fase fase de la consulta
     * @return una lista con los resultados de la busqueda
     */
    private List findMovimientoPorFase(String fase) {
        Set movs = new HashSet(getHibernateTemplate().
                findByNamedQuery("findMovimientoPorFaseYFecha", new Object[]{fase, getFechaHoy12(),
                getFechaHoy()}));
        return new ArrayList(movs);
    }

    /**
     * Regresa las guias Contables Asiento resultado de la busqueda
     *
     * @param fase          fase de la consulta
     * @param tipoOperacion describe el tipo de Operaci&oacute;n (Compra o Venta) de la consulta
     * @param idDivisa      describe la Divisa de la consulta
     * @return una lista con los resultados de la busqueda
     */
    private List findGuiaContableAsiento(String fase, String tipoOperacion, String idDivisa) {
        return getHibernateTemplate().find("FROM GuiaContableAsiento AS gca " +
                "WHERE gca.faseContable like ? AND gca.tipoOperacion like ? AND gca.moneda " +
                "like ? AND gca.tipoSap = ?", new Object[]{fase, tipoOperacion, idDivisa,
                modoSap ? SI : NO});
    }

    /**
     * Regresa las guias Contables Asiento resultado de la busqueda donde la moneda es null
     *
     * @param fase          fase de la consulta
     * @param tipoOperacion describe el tipo de Operaci&oacute;n (Compra o Venta) de la consulta
     * @return una lista con los resultados de la busqueda
     */
    private List findGuiaContableAsientoSinMoneda(String fase, String tipoOperacion) {
        return getHibernateTemplate().find(
                "FROM GuiaContableAsiento AS gca WHERE gca.faseContable like ? AND " +
                        "gca.tipoOperacion like ? AND gca.moneda is null AND gca.tipoSap = ?",
                new Object[]{fase, tipoOperacion, modoSap ? SI : NO});
    }

    /**
     * Regresa las guias Contables Liquidaci&oacute;n resultado de la busqueda por Deals de Sucursal
     *
     * @param mov movimiento Contable de la consulta
     * @return una lista con los resultados de la busqueda
     */
    private List findGuiaContableLiqSucursal(MovimientoContable mov) {
        String mnemonico = COMPRA.equals(mov.getTipoOperacion()) ? "R%" : "E%";
        String idDivisa = mov.getDivisa().isMetalAmonedado() ?
                mov.getDivisa().getGrupo() : mov.getDivisa().getIdDivisa();
        return getHibernateTemplate().find(
                "FROM GuiaContableLiq AS gcl WHERE gcl.mnemonico like ? AND gcl.moneda like " +
                        "? AND gcl.tipoSap = ?",
                new Object[]{mnemonico, "%-" + idDivisa, modoSap ? SI : NO});
    }

    /**
     * Regresa las guias Contables Liquidaci&oacute;n resultado de la busqueda por Deals con
     * Liquidaciones Especiales
     *
     * @param mov movimiento Contable de la Consulta
     * @return una lista con los resultados de la busqueda
     */
    private List findGuiaContableLiqEspecial(MovimientoContable mov) {
        String mnemonico = COMPRA.equals(mov.getTipoOperacion()) ? R +
                mov.getDivisa().getIdDivisa() + "%" : E + mov.getDivisa().getIdDivisa() + "%";
        return getHibernateTemplate().find("FROM GuiaContableLiq AS gcl WHERE gcl.mnemonico " +
                "like ? AND gcl.liquidacionEspecial like ? AND gcl.tipoSap = ?",
                new Object[]{mnemonico, mov.getLiquidacionEspecial().trim(), modoSap ? SI : NO});
    }

    /**
     * Proporciona el Contrato Sica y el Tipo de Operaci&oacute;n del Deal.
     *
     * @param idDeal el n&uacute;mero de identificaci&oacute;n del Deal
     * @return una <code>Map</code> con llaves que identifican el contrato Sica del Cliente y el
     * tipo de Operaci&oacute;n.
     */
    private Map findTipoOperacionDeal(int idDeal) {
        HashMap datosDeal = new HashMap();
        try {
            Deal deal = (Deal) find(Deal.class, new Integer(idDeal));
            datosDeal.put("Compra", deal.isCompra() ? Deal.STATUS_DEAL_CANCELADO.equals(
                    deal.getStatusDeal().trim()) ? VENTA : COMPRA :
                    Deal.STATUS_DEAL_CANCELADO.equals(deal.getStatusDeal().trim()) ?
                            COMPRA : VENTA);
            //El campo de ContratoSica en las Polizas es not null, en  caso de venir null escribimos
            // la palabra null para identificarlo.
            datosDeal.put(CLIENTE, deal.getContratoSica() == null ? "null" :
                    deal.getContratoSica().getNoCuenta());
            return datosDeal;
            //Cachamos alguna excepcion pues no es de gran prioridad para el llenado de las Polizas.
        }
        catch (RuntimeException e) {
            warn(e.getMessage(), e);
        }
        datosDeal.put("Compra", "");
        datosDeal.put(CLIENTE, "");
        return datosDeal;
    }

    /**
     * Obtiene la fecha del d&iacute;a de hoy a las 00:00 hrs.
     *
     * @return una fecha sin minutos,segundos ni milisegundos.
     */
    private Calendar getFechaHoy12() {
        Calendar fechaActual = new GregorianCalendar();
        fechaActual.setTime(DateUtils.inicioDia(fechaSistema));
        return fechaActual;
    }

    /**
     * @see com.ixe.ods.sica.sdo.SicaServiceData#getFechaLimiteCapturaDeal().
     * @return Date.
     */
    private Date getFechaSistema() {
        try {
            ParametroSica p = (ParametroSica) find(ParametroSica.class,
                    ParametroSica.FECHA_SISTEMA);
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
            return DATE_FORMAT.parse(p.getValor());
        }
        catch (ParseException e) {
            if (logger.isErrorEnabled()) {
                logger.error("No se pudo calcular la fecha actual. Se regresa new Date()");
            }
        }
        return new Date();
    }

    /**
     * Obtiene la fecha del d&iacute;a de hoy
     *
     * @return una fecha que es la fecha actual.
     */
    private Calendar getFechaHoy() {
        Calendar fecha = new GregorianCalendar();
        fecha.setTime(fechaSistema);
        fecha.add(Calendar.HOUR, Num.I_23);
        fecha.add(Calendar.MINUTE, Num.I_59);
        return fecha;
    }

    /**
     * Construye el Mnemonico de Ixe Cobertura en Pesos
     *
     * @param det El detalle de deal.
     * @param mov El movimiento contable.
     * @return Regresa el Mnemonico
     */
    private String obtenerMnemonicoPesosIxeCobertura(DealDetalle det, MovimientoContable mov) {
        String mnemonico;
        String token1;
        token1 = det.isRecibimos() ? R : E;
        String token2;
        token2 = mov.getDivisa().getIdDivisa();
        String token3 = obtenerPesosIxeCobertura(det);
        mnemonico = token1 + token2 + token3;
        return mnemonico;
    }

    /**
     * Construye el Mnemonico de Ixe Cobertura en Otras Divisas
     *
     * @param det El detalle de deal.
     * @param mov El movimiento contable.
     * @return Regresa el Mnemonico
     */
    private String obtenerMnemonicoDivisasIxeCobertura(DealDetalle det, MovimientoContable mov) {
        String mnemonico;
        String token1;
        token1 = det.isRecibimos() ? R : E;
        String token2 = "";
        //Divisa Normal
        if (mov.getDivisa().getGrupo() == null) {
            token2 = mov.getDivisa().getIdDivisa();
        }
        //Oro o Plata
        if (mov.getDivisa().getGrupo() != null) {
            //ORO
            if (mov.getDivisa().getGrupo().equals(ORO)) {
                token2 = ORO;
            }
            //Plata
            if (mov.getDivisa().getGrupo().equals(OZP)) {
                token2 = OZP;
            }
        }
        String token3 = "TRFSCOBE";
        mnemonico = token1 + token2 + token3;
        return mnemonico;
    }

    /**
     * Obtiene las Pesos de Ixe Cobertura
     *
     * @param det El detalle de deal.
     * @return Regresa parte del mnemonico y la Divisa TRFSC+Divisa
     */
    private String obtenerPesosIxeCobertura(DealDetalle det) {
        for (Iterator iterator1 = det.getDeal().getDetalles().iterator(); iterator1.hasNext();) {
            DealDetalle det1 = (DealDetalle) iterator1.next();
            if (!Divisa.PESO.equals(det1.getDivisa().getIdDivisa())) {
                //Es Divisa si es nulo
                if (det1.getDivisa().getGrupo() == null) {
                    return TRFSC + det1.getDivisa().getIdDivisa();
                }
                //Oro o Plata
                if (det1.getDivisa().getGrupo() != null) {
                    //ORO
                    if (det1.getDivisa().getGrupo().equals(ORO)) {
                        return TRFSC + ORO;
                    }
                    //Plata
                    if (det1.getDivisa().getGrupo().equals(OZP)) {
                        return TRFSC + OZP;
                    }
                }
            }
        }
        return TRFSC + det.getDivisa().getIdDivisa();
    }

    /**
     * Obtiene los deals cancelados y que vienen por split
     *
     * @param fechaHoyIni La fecha de inicio.
     * @param fechaHoyFin La fecha de fin.
     * @return Regresa los deals cancelados de hoy que vienen de split
     */
    public List findLiquidacionParcialDeals(Date fechaHoyIni, Date fechaHoyFin) {
        List params = new ArrayList();
        params.add(fechaHoyIni);
        params.add(fechaHoyFin);
        StringBuffer sb = new StringBuffer("          " +
                " select " +
                " distinct ddsl.dealDetalle.idDealPosicion, " +
                " ddsl.cambStatus.fechaCambio,       " +
                " ddsl.cambStatus.statusNuevo,       " +
                " ddsl.dealDetalle.reemplazadoPorA,  " +
                " ddsl.dealDetalle.reemplazadoPorB,  " +
                " ddsl.dealDetalle.deal.idDeal,      " +
                " ddsl.dealDetalle.sustituyeA        " +
                " FROM   " +
                " DealDetalleStatusLog AS ddsl  " +
                " WHERE  " +
                " ddsl.cambStatus.fechaCambio BETWEEN ? AND ?          " +
                " and ddsl.cambStatus.statusNuevo = 'CA'               " +
                " and ddsl.dealDetalle.reemplazadoPorA  is not null    " +
                " and ddsl.dealDetalle.reemplazadoPorB  is null        " +
                " order by " +
                " ddsl.dealDetalle.idDealPosicion, ddsl.cambStatus.fechaCambio desc ");
        return getHibernateTemplate().find(sb.toString(), params.toArray());
    }

    /**
     * Obtiene los movimientos contables
     *
     * @param idDealPosicion El detalle de deal.
     * @param faseContable La fase a realizar.
     * @param idDeal El n&uacute;mero de deal.
     * @return Regresa los movimientos contables
     */
    public List findLiquidacionParcialDealsMovimientoContable(Integer idDealPosicion,
                                                              String faseContable, Integer idDeal) {
        List params = new ArrayList();
        params.add(idDealPosicion);
        params.add(faseContable);
        params.add(idDeal);
        StringBuffer sb = new StringBuffer("FROM MovimientoContable AS mov WHERE  " +
                " mov.idDealPosicion = ? AND mov.faseContable = ? AND mov.idDeal = ? ");
        return getHibernateTemplate().find(sb.toString(), params.toArray());
    }

    /**
     * Obtiene los movimientos contables
     *
     * @param sustituyeA El n&uacute;mero que sustituye a.
     * @param faseContable La fase contable.
     * @param idDeal El n&uacute;mero de deal.
     * @return Regresa los movimientos contables
     */
    public List findLiquidacionParcialDealsMovimientoContableRegistro(
            Integer sustituyeA, String faseContable, Integer idDeal) {
        List params = new ArrayList();
        params.add(sustituyeA);
        params.add(faseContable);
        params.add(idDeal);
        StringBuffer sb = new StringBuffer("FROM MovimientoContable AS mov WHERE " +
                "mov.idDealPosicion = ? AND mov.faseContable = ? AND mov.idDeal = ?");
        return getHibernateTemplate().find(sb.toString(), params.toArray());
    }

    /**
     * Regresa un mapa con la informaci&oacute;n de la tabla SC_UTILIDAD_FORWARD para la fecha
     * especificada cada llave corresponde a la Mesa de Cambios y el valor es la lista de registros
     * UtilidadForward para esa mesa de cambio.
     *
     * @param fecha La fecha a revisar.
     * @return Map.
     */
    public Map findUtilidadForwardByFecha(Date fecha) {
        List ufs = getHibernateTemplate().findByNamedQuery("findUtilidadForwardByFecha",
                new Object[]{DateUtils.inicioDia(fecha), DateUtils.finDia(fecha)});
        Map resultados = new LinkedHashMap();
        for (Iterator it = ufs.iterator(); it.hasNext();) {
            UtilidadForward uf = (UtilidadForward) it.next();
            MesaCambio mesaCambio = uf.getMesaCambio();
            List entradaMesa = (List) resultados.get(mesaCambio);
            if (entradaMesa == null) {
                entradaMesa = new ArrayList();
            }
            entradaMesa.add(uf);
            resultados.put(mesaCambio, entradaMesa);
        }
        return resultados;
    }

    /**
     * @param idMesaCambio El n&uacute;mero de mesa de cambio.
     * @param fecha La fecha del sistema.
     * @see ContabilidadSicaServiceData#generarUtilidadesIxeForwards(int, java.util.Date).
     */
    public void generarUtilidadesIxeForwards(int idMesaCambio, Date fecha) {
        // Obteniendo los rangos de Fechas
        List resultados = new ArrayList();
        Date fechaIni = DateUtils.inicioDia(fecha);
        Date fechaFin = DateUtils.finDia(fecha);
        Date fechaSis = DateUtils.inicioDia(fechaSistema);
/*        warn("\n\n@@@@fechaIni: " + fechaIni + ", fechaFin: " + fechaFin + ", fechaSistema: " +
                fechaSistema, null);*/
        // Inicializando variables
        double utilidadIxeForwards = 0.0;
        String contratoSicaIxeForwards = getContratoSicaIxeForwards();
        // Iterando las Mesas de Cambio
        //warn("\n\n@@@@mc.getNombre(): " + idMesaCambio, null);
        // Obteniendo las Divisas que existen en los registros de Posicion Log para la Fecha
        // seleccionada y una Mesa de Cambios en especifico.
        List divisas = getHibernateTemplate().find("SELECT DISTINCT pl.divisa FROM " +
                "PosicionLog AS pl WHERE pl.fecha BETWEEN ? AND ? AND " +
                "pl.canalMesa.mesaCambio.idMesaCambio = ? AND pl.tipoOperacion NOT IN " +
                "('PU','PP','DU','DP')", new Object[]{fechaIni, fechaFin,
                new Integer(idMesaCambio)});
        //warn("@@@@divisas.size(): " + divisas.size(), null);
        // Iterando las Divisas
        for (Iterator it2 = divisas.iterator(); it2.hasNext();) {
            Divisa d = (Divisa) it2.next();
            // Obteniendo los registros de Posicion Log para una Fecha, Mesa de Cambios y Divisa
            // especificos.
            List posicionLogList = getHibernateTemplate().find("FROM PosicionLog AS pl LEFT JOIN " +
                    "FETCH pl.deal WHERE pl.fecha BETWEEN ? AND ? AND " +
                    "pl.canalMesa.mesaCambio.idMesaCambio = ? AND pl.divisa.idDivisa = ? and " +
                    "pl.tipoOperacion not in ('PU','PP','DU','DP', 'CC', 'CN', 'VN', 'VC') " +
                    "ORDER BY pl.idPosicionLog",
                    new Object[]{fechaIni, fechaFin, new Integer(idMesaCambio), d.getIdDivisa()});
            //warn("\n\n@@@@idDivisa: " + d.getIdDivisa(), null);
            //warn("@@@@posicionLogList.size(): " + posicionLogList.size(), null);
            // Inicializando variables
            double posicionInicial = 0;
            double posicion = 0;
            double posicionInicialMXN = 0;
            double posicionMXN = 0;
            double utilidadParcialIxeForwards = 0;
            double tcCostoAnterior = 0;
            double tcCosto = 0;
            List listPosicion;
            // Revisando si se obtiene la Posicion Actual o la Historica.
            if (fechaIni.equals(fechaSis)) {
                // Obteniendo la Posicion Actual
                listPosicion = getHibernateTemplate().findByNamedQuery(
                        "findPosicionByIdMesaCambioAndIdDivisa",
                        new Object[]{new Integer(idMesaCambio), d.getIdDivisa()});
                // Si existe la Posicion
                if (listPosicion.size() > 0) {
                    Posicion p = (Posicion) listPosicion.get(0);
                    //warn("@@@@Se obtuvo la Posicion Actual.", null);
                    // Se cargan los datos de Posicion Inicial y Posicion Inicial MXN
                    posicion = posicionInicial = Redondeador.redondear2Dec(
                            p.getPosIni().getPosicionInicial());
                    posicionMXN = posicionInicialMXN = Redondeador.redondear2Dec(p.getPosIni().
                            getPosicionInicialMn());
                }
            }
            else {
                // Obteniendo la Posicion Historica de la Fecha Seleccionada
                listPosicion = getHibernateTemplate().find("FROM HistoricoPosicion AS p " +
                        "WHERE p.divisa.tipo != 'M' AND p.mesaCambio.idMesaCambio = ? AND " +
                        "p.divisa.idDivisa = ? AND p.ultimaModificacion BETWEEN ? AND ? " +
                        "ORDER BY p.idPosicion", new Object[]{new Integer(
                        idMesaCambio), d.getIdDivisa(), fechaIni, fechaFin});
                // Si existe la Posicion Historica
                if (listPosicion.size() > 0) {
                    HistoricoPosicion hp = (HistoricoPosicion) listPosicion.get(0);
                    //warn("@@@@Se obtuvo la Posicion Historica.", null);
                    // Se cargan los datos de Posicion Inicial y Posicion Inicial MXN
                    posicion = posicionInicial = Redondeador.redondear2Dec(hp.getPosIni().
                            getPosicionInicial());
                    posicionMXN = posicionInicialMXN = Redondeador.redondear2Dec(hp.getPosIni().
                            getPosicionInicialMn());
                }
            }

            // Iterando los registros de Posicion Log e inicializando variables.
            int i = 0;
            int contUtilidadCero = 0;
            for (Iterator it3 = posicionLogList.iterator(); it3.hasNext(); i++) {
                PosicionLog pl = (PosicionLog) it3.next();
                if (pl.getDeal() != null &&
                        (Deal.STATUS_DEAL_CANCELADO.equals(pl.getDeal().getStatusDeal()) ||
                                pl.getDeal().isReversadoProcesoReverso())) {
                    continue;
                }
                // Revisando si el registro es de Compra o de Venta para ir actualizando los
                // respectivos montos de posicion y posicionMXN.
                if (pl.getTipoOperacion().startsWith("C")) {
                    //warn("\n\n@@@@Posicion de un Registro de Compra", null);
                    posicion = Redondeador.redondear2Dec(posicion + pl.getMonto());
                    posicionMXN = Redondeador.redondear2Dec(posicionMXN + pl.getMontoMn());
                }
                else {
                    //warn("\n\n@@@@Posicion de un Registro de Venta", null);
                    posicion = Redondeador.redondear2Dec(posicion - pl.getMonto());
                    posicionMXN = Redondeador.redondear2Dec(posicionMXN - pl.getMontoMn());
                }
                /*warn("@@@@Monto: " + Redondeador.redondear2Dec(pl.getMonto()) + ", MontoMXN: " +
                        Redondeador.redondear2Dec(pl.getMontoMn()), null);
                warn("@@@@posicionInicial: " + posicionInicial + ", posicionInicialMXN: " +
                        posicionInicialMXN, null);
                warn("@@@@posicion: " + posicion + ", posicionMXN: " + posicionMXN, null);   */
                // Inicializando variables
                double realizableDivisa;
                // Calculando el realizable divisa.
                if (posicionInicial > 0) {
                    if (posicion > 0) {
                        realizableDivisa = Redondeador.redondear2Dec(
                                Math.abs(posicionInicial - posicion));
                    }
                    else {
                        realizableDivisa = posicionInicial;
                    }
                }
                else {
                    if (posicion < 0) {
                        realizableDivisa = Redondeador.redondear2Dec(
                                Math.abs(Math.abs(posicion) - Math.abs(posicionInicial)));
                    }
                    else {
                        realizableDivisa = Math.abs(posicionInicial);
                    }
                }
                //warn("@@@@realizableDivisa: " + realizableDivisa, null);
                // Inicializando variables
                double diferenciaTC = 0;
                double resultado = 0;
                // Si se trata del primer registro se calcula el valor del Tipo de Cambio Costo
                // con base en los valores de la posicion-posicionMXN, o de la
                // posicionInicial-posicionInicialMXN, segun el caso.
                if (i == 0) {
                    if (posicionInicial >= 0) {
                        if ('C' == pl.getTipoOperacion().charAt(0)) {
                            tcCosto = Redondeador.redondear6Dec(posicionMXN / posicion);
                        }
                        else if ('V' == pl.getTipoOperacion().charAt(0)) {
                            tcCosto = Redondeador.redondear6Dec(
                                    posicionInicialMXN / posicionInicial);
                        }
                    }
                    else {
                        if ('C' == pl.getTipoOperacion().charAt(0)) {
                            tcCosto = Redondeador.redondear6Dec(
                                    posicionInicialMXN / posicionInicial);
                        }
                        else if ('V' == pl.getTipoOperacion().charAt(0)) {
                            tcCosto = Redondeador.redondear6Dec(posicionMXN / posicion);
                        }
                    }
                }
                /*warn("@@@@tcCostoAnterior: " + tcCostoAnterior + ", tcCosto: " + tcCosto +
                        ", tcCliente: " + pl.getTipoCambioCliente(), null);*/
                // Obteniendo los Tipos de Cambio Costo, la Diferencia con el Tipo de Cambio
                // Cliente y las Utilidades de IXE Forwards que se generan si es el caso.
                if (posicionInicial >= 0) {
                    //warn("@@@@Iniciando con posicion larga...", null);
                    if ('C' == pl.getTipoOperacion().charAt(0)) {
                        //warn("@@@@Compramos... ", null);
                        tcCostoAnterior = tcCosto;
                        // Se calcula el siguiente Tipo de Cambio Costo de manera ponderada.
                        tcCosto = Redondeador.redondear6Dec(
                                ((posicionInicial * tcCostoAnterior) +
                                        ((posicion - posicionInicial) *
                                                pl.getTipoCambioCliente())) / posicion);
                        posicionInicial = posicion;
                        posicionInicialMXN = posicionMXN;
                        contUtilidadCero++;
                    }
                    else if ('V' == pl.getTipoOperacion().charAt(0)) {
                        //warn("@@@@Vendemos... ", null);
                        if (posicion >= 0) {
                            //warn("@@@@Vendiendo hasta el monto de la posicion larga...", null);
                            diferenciaTC = Redondeador.redondear6Dec(
                                    pl.getTipoCambioCliente() - tcCosto);
                            resultado = Redondeador.redondear2Dec(
                                    realizableDivisa * diferenciaTC);
                            tcCostoAnterior = tcCosto;
                            posicionInicial = posicion;
                            posicionInicialMXN = posicionMXN;
                        }
                        else {
                            //warn("@@@@Vendiendo mas del monto de la posicion larga...", null);
                            diferenciaTC = Redondeador.redondear6Dec(
                                    pl.getTipoCambioCliente() - tcCosto);
                            resultado = Redondeador.redondear2Dec(
                                    realizableDivisa * diferenciaTC);
                            tcCostoAnterior = tcCosto;
                            tcCosto = Redondeador.redondear6Dec(pl.getTipoCambioCliente());
                            posicionInicial = posicion;
                            posicionInicialMXN = posicionMXN;
                        }
                        contUtilidadCero = 0;
                    }
                }
                else {
                    //warn("@@@@Iniciando con posicion corta...", null);
                    if ('C' == pl.getTipoOperacion().charAt(0)) {
                        //warn("@@@@Compramos...", null);
                        if (posicion <= 0) {
                            //warn("@@@@Comprando hasta el monto de la posicion corta...", null);
                            diferenciaTC = Redondeador.redondear6Dec(
                                    tcCosto - pl.getTipoCambioCliente());
                            resultado = Redondeador.redondear2Dec(
                                    realizableDivisa * diferenciaTC);
                            tcCostoAnterior = tcCosto;
                            posicionInicial = posicion;
                            posicionInicialMXN = posicionMXN;
                        }
                        else {
                            //warn("@@@@Comprando mas del monto de la posicion corta...", null);
                            diferenciaTC = Redondeador.redondear6Dec(
                                    tcCosto - pl.getTipoCambioCliente());
                            resultado = Redondeador.redondear2Dec(
                                    realizableDivisa * diferenciaTC);
                            tcCostoAnterior = tcCosto;
                            tcCosto = Redondeador.redondear6Dec(pl.getTipoCambioCliente());
                            posicionInicial = posicion;
                            posicionInicialMXN = posicionMXN;
                        }
                        contUtilidadCero = 0;
                    }
                    else if ('V' == pl.getTipoOperacion().charAt(0)) {
                        //warn("@@@@Vendemos...", null);
                        tcCostoAnterior = tcCosto;
                        // Se calcula el siguiente Tipo de Cambio Costo de manera ponderada.
                        tcCosto = Redondeador.redondear6Dec(
                                ((posicionInicial * tcCostoAnterior) +
                                        ((posicion - posicionInicial) *
                                                pl.getTipoCambioCliente())) / posicion);
                        posicionInicial = posicion;
                        posicionInicialMXN = posicionMXN;
                        contUtilidadCero++;
                    }
                }
                /*warn("@@@@diferenciaTC: " + diferenciaTC + ", resultado: " + resultado +
                        ", contUtilidadCero: " + contUtilidadCero, null);     */
                // Revisando si el registro de Posicion Log es de un Deal de IXE Forward para
                // sumar la Utilidad.
                if (pl.getDeal() != null && pl.getDeal().getContratoSica().getNoCuenta().equals(
                        contratoSicaIxeForwards)) {
                    //warn("@@@@IXE FORWARD", null);
                    utilidadParcialIxeForwards = Redondeador.redondear2Dec(
                            utilidadParcialIxeForwards + resultado);
                    //warn("@@@@utilidadIxeForwards: " + utilidadParcialIxeForwards, null);
                    UtilidadForward uf = new UtilidadForward(fechaSis, pl.getDeal().getIdDeal(),
                            pl.isCompra(), pl.getTipoValor(), d.getIdDivisa(),
                            new BigDecimal("" + pl.getMonto()),
                            new BigDecimal("" + pl.getMontoMn()),
                            new BigDecimal("" + pl.getTipoCambioCliente()),
                            new BigDecimal("" + resultado), pl.getDeal().isInicioSwap(),
                            pl.getCanalMesa().getMesaCambio());
                    store(uf);
                }
            }
            // Cargando los valores a desplegar en el Front.
            HashMap mapaUtils = new HashMap();
            mapaUtils.put("mesaCambios", new Integer(idMesaCambio));
            mapaUtils.put("idDivisa", d.getIdDivisa());
            mapaUtils.put("utilidadIxeForwards", new Double(utilidadParcialIxeForwards));
            resultados.add(mapaUtils);
            utilidadIxeForwards = Redondeador.redondear2Dec(
                    utilidadIxeForwards + utilidadParcialIxeForwards);
            // Utilidad Global de IXE Forwards.
            //warn("\n\n@@@@getUtilidadIxeForwards(): " + utilidadIxeForwards, null);
        }
    }

    /**
     * Si el mnem&oacute;nico del movimiento termina en 'REF' significa que corresponde a un
     * dep&oacute;sito referenciado. En ese caso se obtiene la clave de referencia del cliente y se
     * asigna al movimiento. Posteriormente se inserta el registro en la base de datos.
     *
     * @param mov El movimiento contable.
     * @param det El detalle de deal (puede ser null).
     */
    private void storeMovimiento(MovimientoContable mov, DealDetalle det) {
        store(mov);
        String mnemonico = mov.getMnemonico();

        if (mnemonico != null && det != null && mnemonico.endsWith("REF")) {
            Integer idDeal = new Integer(det.getDeal().getIdDeal());
        	String claveReferencia = det.getDeal().getClaveReferencia();
            dealsConReferencia.put(idDeal, claveReferencia);
        }
    }

    /**
     * Asigna la clave de referencia del movimiento a la p&oacute;liza y guarda el registro en la
     * base de datos.
     *
     * @param poliza La p&oacute;liza a insertar en la base de datos.
     */
    private void storePoliza(Poliza poliza) {
        if (poliza.getIdDeal() > 0) {
            String cveReferencia = (String) dealsConReferencia.get(new Integer(poliza.getIdDeal()));
            if (cveReferencia != null) {
                poliza.setClaveReferencia(cveReferencia);
            }
        }
        store(poliza);
    }

    /**
     * Revisa que la cuenta contable exista en las gu&iacute;as contables de asientos o en las de
     * liquidaci&oacute;n.
     *
     * @param guiasAsientos La lista de gu&iaacute;s contables de asientos.
     * @param guiasLiq La lista de gu&iacute;s contables de liquidaci&oacute;n.
     * @param cuentaCont La cuenta contable a revisar.
     * @param idDivisa La clave de la divisa
     */
    private void validarCuentaContableSap(List guiasAsientos, List guiasLiq, String cuentaCont,
                                          String idDivisa) {
        boolean existeCuenta = false;
        List divisasPermitidas = new ArrayList();
        for (Iterator it = guiasAsientos.iterator(); it.hasNext();) {
            GuiaContableAsiento gca = (GuiaContableAsiento) it.next();
            if (cuentaCont.equals(gca.getCuentaContable())) {
                existeCuenta = true;
                divisasPermitidas.add(gca.getMoneda());
            }
        }
        for (Iterator it = guiasLiq.iterator(); it.hasNext();) {
            GuiaContableLiq gcl = (GuiaContableLiq) it.next();
            if (cuentaCont.equals(gcl.getCuentaContable())) {
                existeCuenta = true;
                divisasPermitidas.add(gcl.getMoneda());
            }
        }
        if (!existeCuenta) {
            throw new SicaException("No existe la cuenta contable " + cuentaCont + ".");
        }
        for (Iterator iterator = divisasPermitidas.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            if (s != null && s.indexOf(idDivisa) >= 0) {
                return;
            }
        }
        throw new SicaException("La divisa de la cuenta " + cuentaCont + " no puede ser " +
                idDivisa);
    }

    /**
     * Valida que la clave de la divisa proporcionada exista en las divisas de la lista.
     *
     * @param divisas La lista de objetos Divisa.
     * @param idDivisa La divisa a revisar.
     */
    private void validarDivisaSap(List divisas, String idDivisa) {
        for (Iterator it = divisas.iterator(); it.hasNext();) {
            Divisa divisa = (Divisa) it.next();
            if (divisa.getIdDivisa().equals(idDivisa)) {
                return;
            }
        }
        throw new SicaException("No est\u00e1 definida la divisa " + idDivisa + ".");
    }

    /**
     * Valida que la clave del canal proporcionado exista en los canales de la lista.
     *
     * @param canales La lista de objetos Canal.
     * @param idCanal El canal a revisar.
     */
    private void validarCanalSap(List canales, String idCanal) {
        for (Iterator it = canales.iterator(); it.hasNext();) {
            Canal canal = (Canal) it.next();
            if (canal.getIdCanal().equals(idCanal)) {
                return;
            }
        }
        throw new SicaException("No est\u00e1 definido el canal " + idCanal + ".");
    }

    /**
     * Valida que el ID Deal Posici&oacute;n proporcionado exista.
     *
     * @param idDealPosicion El ID Deal Posici&oacute;n.
     */
    private void validarIdPosicion(int idDealPosicion) {
    	List dealPosicion = getHibernateTemplate().find("FROM DealPosicion AS dp " +
    			"WHERE dp.idDealPosicion = ?", new Integer(idDealPosicion));
    	if (dealPosicion.isEmpty()) {
    		throw new SicaException("No se encontr\u00f3 el ID Deal Posici\u00f3n con el " +
    				"n\u00fanero " + idDealPosicion);
    	}
    }

    /**
     * Regresa el n&uacute;mero de contrato sica para Ixe Forwards.
     *
     * @return String.
     */
    public String getContratoSicaIxeForwards() {
        return parametroIxeForwards.getValor();
    }

    /**
     * Obtiene la Divisa Peso
     *
     * @return <code> Divisa </code>
     */
    private Divisa getPeso() {
        return (Divisa) find(Divisa.class, Divisa.PESO);
    }

    /**
     * Regresa la referencia al bean dealService, deal application context.
     *
     * @return DealService.
     */
    public List getGuiasContablesAsientos() {
        return _guiasContablesAsientos;
    }

    /**
     * @param guiasContablesAsientos El valor a asignar.
     */
    public void setGuiasContablesAsientos(List guiasContablesAsientos) {
        _guiasContablesAsientos = guiasContablesAsientos;
    }

    /**
     * Obtiene referencia al bean de PizarronServiceData
     *
     * @return PizarronServiceData
     */
    public PizarronServiceData getPizarronServiceData() {
        return _pizarronServiceData;
    }

    /**
     * Fija el valor de pizarronServiceData.
     *
     * @param pizarronServiceData El valor a asignar.
     */
    public void setPizarronServiceData(PizarronServiceData pizarronServiceData) {
        _pizarronServiceData = pizarronServiceData;
    }

    /**
     * Regresa la referencia al bean dealService, deal application context.
     *
     * @return DealService.
     */
    public List getGuiasContablesLiquidacion() {
        return _guiasContablesLiquidacion;
    }

    /**
     * @param guiasContablesLiquidacion El valor a asignar.
     */
    public void setGuiasContablesLiquidacion(List guiasContablesLiquidacion) {
        _guiasContablesLiquidacion = guiasContablesLiquidacion;
    }

    /**
     * Regresa una lista de los Tipos de Iva.
     *
     * @return List.
     */
    public List getTiposIva() {
        return _tiposIva;
    }

    /**
     * Establece el valor de tiposIva.
     *
     * @param tiposIva El valor a asignar.
     */
    public void setTiposIva(List tiposIva) {
        _tiposIva = tiposIva;
    }

    /**
     * Regresa el bean logExplosionContable del ApplicationContext de Spring.
     *
     * @return LogExpresionContable.
     */
    private LogExpresionContable getLogExpresionContable() {
        return (LogExpresionContable) _appContext.getBean("logExplosionContable");
    }

    /**
     * Manda el objeto al log de expresi&oacute;n contable y llama al padre.
     *
     * @param a el objeto a loggear.
     */
    public void warn(Object a) {
        LogExpresionContable log = getLogExpresionContable();
        log.agregarALogContabilidad(a.toString());
        super.warn(a);
    }

    /**
     * Manda el objeto al log de expresi&oacute;n contable y llama al padre.
     *
     * @param a el objeto a loggear.
     * @param t la excepci&oacute;n.
     */
    public void warn(Object a, Throwable t) {
        LogExpresionContable log = getLogExpresionContable();
        log.agregarALogContabilidad(a.toString());
        super.warn(a, t);
    }

    /**
     * Regresa el valor de contabilidadDao.
     *
     * @return ContabilidadDao.
     */
    public ContabilidadDao getContabilidadDao() {
        return contabilidadDao;
    }

    /**
     * Establece el valor de contabilidadDao.
     *
     * @param contabilidadDao El valor a asignar.
     */
    public void setContabilidadDao(ContabilidadDao contabilidadDao) {
        this.contabilidadDao = contabilidadDao;
    }

    /**
     * @return the reprocesoSAP
     */
    public boolean isReprocesoSAP() {
        return reprocesoSAP;
    }

    /**
     * @param reprocesoSAP the reprocesoSAP to set
     */
    public void setReprocesoSAP(boolean reprocesoSAP) {
        this.reprocesoSAP = reprocesoSAP;
    }

    /**
     * Bandera que indica: true.- Contabilidad SAP, false.- Contabilidad SOLOMON.
     */
    private boolean modoSap;

    /**
     * Data Access Object de contabilidad para escribir los registros para SAP.
     */
    private ContabilidadDao contabilidadDao;

    /**
     * Referencia a LoginService.
     */
    private List _guiasContablesAsientos;

    /**
     * La referencia a los Servicios del Pizarron
     */
    private PizarronServiceData _pizarronServiceData;

    /**
     * Referencia a LoginService.
     */
    private List _guiasContablesLiquidacion;

    /**
     * Referencia a LoginService.
     */
    private List _tiposIva;

    /**
     * La lista de movimientos contables que es enviada a la cola para asignarles su clave
     * de referencia.
     */
    private Map dealsConReferencia;

    /**
     * El par&aacute;metro que contiene el id para Ixe Coberturas.
     */
    private ParametroSica parametroCoberturas;

    /**
     * El par&aacute;metro que contiene el id para Ixe Forwards.
     */
    private ParametroSica parametroIxeForwards;

    /**
     * La fecha de la explosi&oacute;n contable, de acuerdo al par&aacute;metro FECHA_SISTEMA.
     */
    private Date fechaSistema;

    /**
     * La fecha de la explosi&oacute;n contable, de acuerdo al par&aacute;metro FECHA_SISTEMA.
     */
    private Date fechaProcesoContable;

    /**
     * La lista de precios de referencia de los &uacute;ltimos 5 d&iacute;as.
     */
    private List preciosReferencia;

    /**
     * El precio de referencia Actual.
     */
    private PrecioReferenciaActual precioReferenciaActual;

    /**
     * Indica si es reproceso SAP.
     */
    private boolean reprocesoSAP;

    /**
     * Constante para identificar la descripci&oacute;n de Abono
     */
    private static final String ABONO = "ABONO";

    /**
     * Constante para identificar el status de Operaci&oacute;n a Acreedores
     */
    private static final String ACREEDORES = "Acreedores";

    /**
     * Constante para identificar el status de Operaci&oacute;n a Autorizada
     */
    private static final String AUTORIZADA = "Autorizada";

    /**
     * Constante para la cadena 'BALNETEO'.
     */
    private static final String BALNETEO = "BALNETEO";

    /**
     * Constante para identificar la descripci&oacute;n de Cargo
     */
    private static final String CARGO = "CARGO";

    /**
     * Constante para la cadena 'CARGO_ABONO'.
     */
    private static final String CARGO_ABONO = "CARGO_ABONO";

    /**
     * Constante para la cadena 'Cliente'.
     */
    private static final String CLIENTE = "Cliente";

    /**
     * Constante para la cadena 'COMNETEO'.
     */
    private static final String COMNETEO = "COMNETEO";

    /**
     * Constante para identificar el tipo de Operaci&oacute;n Compra
     */
    private static final String COMPRA = "COMPRA";

    /**
     * Constante para la cadena 'CUENTA_CONTABLE'.
     */
    private static final String CUENTA_CONTABLE = "CUENTA_CONTABLE";

    /**
     * Constante para el formateador de fechas yyyyMMdd.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    /**
     * Constante para el formateador de fecha con la hora del sistema yyyyMMdd HH:mm
     */
    private static final SimpleDateFormat DATE_HOUR_FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm");

    /**
     * Constante para la cadena 'DESCRIPCION'.
     */
    private static final String DESCRIPCION = "DESCRIPCION";

    /**
     * Constante para identificar el status de Operaci&oacute;n a Deudores
     */
    private static final String DEUDORES = "Deudores";

    /**
     * Constante para la cadena 'E' de Entregamos.
     */
    private static final String E = "E";

    /**
     * Constante para la cadena 'ENTIDAD'.
     */
    private static final String ENTIDAD = "ENTIDAD";

    /**
     * Constante para identificar el Mnemonico EMXNBALNETEO
     */
    private static final String ENTREGAMOSBALNETEO = "EMXNBALNETEO";

    /**
     * Constante para identificar el Mnemonico EMXNCOMNETEO
     */
    private static final String ENTREGAMOSCOMNETEO = "EMXNCOMNETEO";

    /**
     * Constante para identificar la etapa de pactaci&oacute;n de Divisas
     */
    private static final String EXPLOSION_PAC_COMPRAS = "PC1";

    /**
     * Constante para identificar la etapa de pactaci&oacute;n de Pesos de Divisas
     */
    private static final String EXPLOSION_PAC_COMPRAS_CASO2 = "PC2";

    /**
     * Constante para identificar las utilidades del Sistema
     */
    private static final String EXPLOSION_PAC_COMPRAS_CASO_UTILIDADES = "PC2BU";

    /**
     * Constante para identificar las p&eacute;rdidas del Sistema
     */
    private static final String EXPLOSION_PAC_COMPRAS_CASO_PERDIDAS = "PC2CP";

    /**
     * Constante para identificar las Liquidaciones en Divisa en la operaci&oacute;n Compra
     */
    private static final String EXPLOSION_LIQ_COMPRAS = "LCC1";

    /**
     * Constante para identificar las Liquidaciones de Pesos de Divisa en la operaci&oacute;n Compra
     */
    private static final String EXPLOSION_LIQ_COMPRAS_CASO2 = "LCC2";

    /**
     * Constante para identificar las Liquidaciones en Divisa en la operaci&oacute;n Venta
     */
    private static final String EXPLOSION_LIQ_VENTAS = "LVC1";

    /**
     * Constante para identificar las Liquidaciones de Pesos de Divisa en la operaci&oacute;n Venta
     */
    private static final String EXPLOSION_LIQ_VENTAS_CASO2 = "LVC2A";

    /**
     * Constante para identificar las Liquidaciones de Comisiones en la operaci&oacute;n Venta
     */
    private static final String EXPLOSION_LIQ_VENTAS_CASO_COMISION = "LVC2B";

    /**
     * Constante para identificar la fase de Pactaci&oacute;n
     */
    private static final String FASE_PACTACION = "P";

    /**
     * Constante para identificar la fase de Liquidaci&oacute;n
     */
    private static final String FASE_LIQUIDACION = "L";

    /**
     * Constante para identificar la fase de Liquidaci&oacute;n 2A
     */
    private static final String FASE_LIQUIDACION_2A = "L2A";

    /**
     * Constante para identificar la fase de Liquidaci&oacute;n 2B
     */
    private static final String FASE_LIQUIDACION_2B = "L2B";

    /**
     * Constante para identificar la fase de Liquidaci&oacute;n L3
     */
    private static final String FASE_LIQUIDACION_L3 = "L3";

    /**
     * Constante para identificar la fase de Liquidaci&oacute;n L4
     */
    private static final String FASE_LIQUIDACION_L4 = "L4";

    /**
     * Constante para identificar la fase de Utilidades U.
     */
    private static final String FASE_UTILIDADES = "U";

    /**
     * Constante para identificar un tipo de Cuenta Contable Fija
     */
    private static final String FIJO_CUENTA_CONTABLE = "1505900028";

    /**
     * Constante para identificar un tipo de Cuenta Contable Fija pa SAP
     */
    private static final String FIJO_CUENTA_CONTABLE_SAP = "1409010200";

    /**
     * Constante para identificar la descripci&oacute;n del Canal con divisa referencia
     * d&oacute;lares.
     */
    private static final String FIJO_CUENTA_CONTABLE_MESA_TRADER = "5205090400";

    /**
     * Constante para identificar la descripci&oacute;n del Canal con divisa referencia
     * d&oacute;lares.
     */
    private static final String FIJO_CUENTA_CONTABLE_MESA_TRADER_SAP = "5404010000";

    /**
     * Constante para identificar un tipo de entidad Fija
     */
    private static final String FIJO_ENTIDAD = "0000-00000000-000000-00-0000";

    /**
     * Constante para identificar el tipo de gu&iacute;a contable asiento de la comisi&oacute;n sin
     * IVA.
     */
    private static final String GUIA_CON_ASIENTO_COMISION_DESC = "IMPORTE DE LA COMISION " +
            "SIN IVA";

    /**
     * Constante para identificar el tipo de gu&iacute;a contable asiento del IVA
     */
    private static final String GUIA_CON_ASIENTO_IVA_COM_DESC = "IMPORTE DEL IVA DE LA " +
            "COMISION";

    /**
     * Constante para identificar el tipo de gu&iacute;a contable asiento de p&eacute;rdida
     */
    private static final String GUIA_CON_ASIENTO_PERDIDA_DESC = "P\u00c9RDIDA EN LA OPERACI\u00d3N";

    /**
     * Constante para identificar el tipo de gu&iacute;a contable asiento de utilidad
     */
    private static final String GUIA_CON_ASIENTO_UTILIDAD_DESC = "UTILIDAD EN LA OPERACI\u00d3N";

    /**
     * Constante para identificar el tipo de Monto de la Comisi&oacute;n
     */
    private static final String IMPORTE_COM_SIN_IVA = "IMPORTE DE LA COMISION SIN IVA";

    /**
     * Constante para identificar el tipo de Monto en Divisa
     */
    private static final String IMPORTE_DIV = "IMPORTE EN DIVISA";

    /**
     * Constante para identificar el tipo de Monto del IVA de la Comisi&oacute;n
     */
    private static final String IMPORTE_IVA = "IMPORTE DEL IVA DE LA COMISION";

    /**
     * Constante para identificar el tipo de Monto de la Liquidaci&oacute;n
     */
    private static final String IMPORTE_LIQ = "IMPORTE LIQUIDACION";

    /**
     * Constante para identificar el tipo de Monto de las p&eacute;rdidas
     */
    private static final String IMPORTE_PERDIDAS = "IMPORTE DE PERDIDAS";

    /**
     * Constante para identificar el tipo de Monto en Pesos
     */
    private static final String IMPORTE_PESOS = "IMPORTE EN PESOS";

    /**
     * Constante para identificar el tipo de Monto de las utilidades
     */
    private static final String IMPORTE_UTILIDADES = "IMPORTE DE UTILIDADES";

    /**
     * Constante para identificar el tipo de Deal Interbancario
     */
    private static final String INTERBANCARIO = "Interbancario";

    /**
     * Constante para identificar el status de Operaci&oacute;n a Liquidada
     */
    private static final String LIQUIDADA = "Liquidada";

    /**
     * Constante para identificar el modo de generaci&oacute;n de contabiliadad para SAP
     */
    private static final String MODO_GEN_CONTA_SAP = "SAP";

    /**
     * Constante para identificar el tipo de Deal con Neteo
     */
    private static final String NETEO = "Neteo";

    /**
     * Constante para la cadena 'N'.
     */
    private static final String NO = "N";

    /**
     * Constante para identificar el tipo de Deal Normal
     */
    private static final String NORMAL = "Normal";

    /**
     * Constante para la cadena 'ORO'.
     */
    private static final String ORO = "ORO";

    /**
     * Constante para la cadena 'OZP'.
     */
    private static final String OZP = "OZP";

    /**
     * Constante para identificar el status de Operaci&oacute;n a Pendiente
     */
    private static final String PENDIENTE = "Pendiente";

    /**
     * Constante para la cadena 'poliza'.
     */
    private static final String POLIZA = "poliza";

    /**
     * Constante para la cadena 'R', de 'Recibimos'.
     */
    private static final String R = "R";

    /**
     * Constante para identificar el Mnemonico RMXNBALNETEO
     */
    private static final String RECIBIMOSBALNETEO = "RMXNBALNETEO";

    /**
     * Constante para identificar el Mnemonico RMXNCOMNETEO
     */
    private static final String RECIBIMOSCOMNETEO = "RMXNCOMNETEO";

    /**
     * Constante para la cadena 'S'.
     */
    private static final String SI = "S";

    /**
     * Constante para identificar que un registro no fue procesado
     */
    private static final String STATUS_NO_PROCESADO = "0";

    /**
     * Constante para identificar que un registro gener&oacute; un error, por lo que no fue
     * procesado.
     */
    private static final String STATUS_ERROR = "2";

    /**
     * Constante para identificar el tipo de Deal Sucursal
     */
    private static final String SUCURSAL = "Sucursal";

    /**
     * Constante para la cadena 'tipo'.
     */
    private static final String TIPO = "tipo";

    /**
     * Constante para identificar la descripci&oacute;n del Canal con divisa referencia
     * d&oacute;lares.
     */
    private static final String TRADER_USD = "TRADER USD";

    /**
     * Constante para la cadena 'TRFBOFAM'.
     */
    private static final String TRFBOFAM = "TRFBOFAM";

    /**
     * Constante para la cadena 'TRFSC'.
     */
    private static final String TRFSC = "TRFSC";

    /**
     * Constante para la cadena 'Utilidades'.
     */
    private static final String UTILIDADES = "Utilidades";

    /**
     * Constante para identificar el tipo de Operaci&oacute;n Venta
     */
    private static final String VENTA = "VENTA";
}
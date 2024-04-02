/*
 * $Id: ReporteLinCredAutMC.java,v 1.17.38.1.46.2 2016/08/05 21:07:42 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.bup.model.PersonaMoral;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.LineaCambioLog;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * P&aacute;gina que permite al usuario buscar e imprimir las l&iacute;neas de
 * Cr&eacute;dito
 *
 * @author Gerardo Corzo Herrera
 */
public abstract class ReporteLinCredAutMC extends SicaPage implements DataSourceProvider {

    /**
     * Impresi&oacute;n de reportes del Historial de L&iacute;neas de
     * Cr&eacute;dito
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        setLineasCredito(new ArrayList());
        super.activate(cycle);
        setModo((String) cycle.getServiceParameters()[0]);
        setNombre("");
        setPaterno("");
        setMaterno("");
        setRazonSocial("");
        importarListaPromotores();
        setCriterioSeleccionado(1);
    }

    /**
     * Extrae la lista de promotores con base en la jerarqu&iacute;a en la
     * cual se encuentra el usuario.
     */
    public void importarListaPromotores() {
        List promotoresTmp;
        List promotores = new ArrayList();
        Visit visit = (Visit) getVisit();
        // Para Direccion de Promocion
        if (FacultySystem.SICA_PROMOCION.equals(getModo())) {
            setTitleActionPortletBorder("Departamento de Promoci&oacute;n");
            // Obtiene subordinados incluyendo nodo raiz
            promotoresTmp = getSeguridadServiceData().
                    findSubordinadosEjecutivoYEjecutivosASustituir(((SupportEngine) getEngine()).
                            getApplicationName(), visit.getUser().getIdPersona());
        }
        // Para Mesa de Control
        else {
            setTitleActionPortletBorder("Mesa de Control");
            promotoresTmp = getSicaServiceData().
                    findAllPromotoresSICA(((SupportEngine) getEngine()).getApplicationName());
        }
        if (promotoresTmp.size() > 0) {
            HashMap hm = new HashMap();
            hm.put("idPersona", new Integer(0));
            hm.put("nombreCompleto", "TODOS");
            promotores.add(hm);
            for (Iterator it = promotoresTmp.iterator(); it.hasNext();) {
                hm = new HashMap();
                EmpleadoIxe promotor = (EmpleadoIxe) it.next();
                hm.put("idPersona", promotor.getIdPersona());
                hm.put("nombreCompleto", promotor.getNombreCompleto());
                promotores.add(hm);
            }
            setPromotor((HashMap) promotores.get(1));
        }
        setPromotoresList(promotores);
    }

    /**
     * Regresa un StringPropertySelectionModel con criterios de b&uacute;squeda
     * 'Todos', 'un mes' y 'tres meses'.
     *
     * @return StringPropertySelectionModel.
     */
    public StringPropertySelectionModel getFecha() {
        return new StringPropertySelectionModel(new String[]{"Todo", "Un Mes", "Tres Meses"});
    }

    /**
     * Realiza las operaciones de b&uacute;squeda de L&iacute;neas de
     * Cr&eacute;dito por Promotor y por Estatus.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void fetch(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        setLineasCredito(new ArrayList());
        setFechaSeleccionada(getFechaSeleccionada());
        if (StringUtils.isEmpty(getRazonSocial()) && StringUtils.isEmpty(getNombre())
                && StringUtils.isEmpty(getPaterno()) && StringUtils.isEmpty(getMaterno())) {
            delegate.record("Debe especificar la Raz\u00f3n Social o el Nombre del cliente "
                    + "para la b\u00fasqueda.", null);
            setMovimientosLinea(0);
            return;
        }
        if (StringUtils.isNotEmpty(getRazonSocial().trim())
                && (StringUtils.isNotEmpty(getPaterno().trim())
                || StringUtils.isNotEmpty(getMaterno().trim())
                || StringUtils.isNotEmpty(getNombre().trim()))) {
            delegate.record("La b\u00fasqueda es por Personas o por Empresas.", null);
            setMovimientosLinea(0);
            return;
        }
        if ((StringUtils.isNotEmpty(getNombre()) && getNombre().length() < 3)
                || (StringUtils.isNotEmpty(getPaterno()) && getPaterno().length() < 3)
                || (StringUtils.isNotEmpty(getMaterno()) && getMaterno().length() < 3)) {
            delegate.record("Los criterios de b\u00fasqueda deben tener m\u00e1s de 2 caracteres.",
                    null);
            setMovimientosLinea(0);
            return;
        }
        else {
            setNombre(getNombre());
            setPaterno(getPaterno());
            setMaterno(getMaterno());
        }
        if (StringUtils.isNotEmpty(getRazonSocial().trim()) && getRazonSocial().length() < 3) {
            delegate.record("El criterio de b\u00fasqueda Raz\u00f3n Social debe tener m\u00e1s de "
                    + "2 caracteres.", null);
            setMovimientosLinea(0);
            return;
        }
        if (StringUtils.isNotEmpty(getRazonSocial().trim()) && getRazonSocial().length() >= 3) {
            setRazonSocial(getRazonSocial());
        }
        if (getEstadoLinea() != null) {
            setDesProdFech(true);
        }
        if (getFechaSeleccionada() != null) {
            setDesRadio(true);
        }
        if (!delegate.getHasErrors()) {
            List contratosSica;
            List corporativos = new ArrayList();
            contratosSica = getSicaServiceData().findContratosSica(getRazonSocial(), getNombre(),
                    getPaterno(), getMaterno(), "");
            if (contratosSica.isEmpty()) {
                delegate.record("El Cliente no tiene Contrato SICA, o no se encontr\u00f3 el "
                        + "Cliente.", null);
                setMovimientosLinea(0);
                return;
            }
            for (Iterator iterator = contratosSica.iterator(); iterator.hasNext();) {
                Object[] obj = (Object[]) iterator.next();
                ContratoSica contratoSica = (ContratoSica) obj[0];
                Persona persona = getSicaServiceData().
                        findPersonaContratoSica(contratoSica.getNoCuenta());
                PersonaMoral corporativo = getSicaServiceData().
                        findCorporativo(persona.getIdPersona());
                Object p = corporativo != null ? corporativo : persona;
                if (!corporativos.contains(p)) {
                    corporativos.add(p);
                }
            }
            if (corporativos.isEmpty()) {
                delegate.record("No se encontr\u00f3 el Cliente.", null);
                setMovimientosLinea(0);
                return;
            }
            setCorporativos(corporativos);
        }
        List lineasClienteEjecutivoEstatus;
        if ("TODOS".equals(getPromotor().get("nombreCompleto"))) {
            lineasClienteEjecutivoEstatus = getLineaCambioServiceData().
                    findLineasCambioByClientesAndEjecutivosAndEstatus(getCorporativos(),
                            ((SupportEngine) getEngine()).getIdTipoEjecutivo(), getPromotoresList(),
                            "%");
        }
        else {
            lineasClienteEjecutivoEstatus = getLineaCambioServiceData().
                    findLineasDeCreditoByClientesAndEjecutivoAndEstatus(getCorporativos(),
                            ((SupportEngine) getEngine()).getIdTipoEjecutivo(),
                            (Integer) getPromotor().get("idPersona"), "%");
        }
        if (!(lineasClienteEjecutivoEstatus.size() > 0)) {
            delegate.record("No hay L\u00edneas de Cr\u00e9dito con el o los Promotores "
                    + "especificados.", null);
            return;
        }
        setLineasCredito(lineasClienteEjecutivoEstatus);
        if (!(getLineasCredito().size() > 0)) {
            delegate.record("No hay L\u00edneas de Cr\u00e9dito con los Criterios de B\u00fasqueda "
                    + "especificados.", null);
            setMovimientosLinea(0);
            return;
        }
        setLineasCredito(filtarLineasPorFechaOperacion(lineasClienteEjecutivoEstatus));
        if (!(getLineasCredito().size() > 0)) {
            delegate.record("No hay L\u00edneas de Cr\u00e9dito con los Criterios de B\u00fasqueda "
                    + "especificados.", null);
            setMovimientosLinea(0);
        }
    }

    /**
     * Toma las l&iacute;neas de cr&eacute;dito que se hayan usado en
     * el rango de tiempo definido por el usuario.
     *
     * @param lineas La lista de l&iacute;neas de cr&eacute;dito.
     * @return List
     */
    public List filtarLineasPorFechaOperacion(List lineas) {
        List lista = new ArrayList();
        Calendar gc = new GregorianCalendar();
        if (!("Todo").equals(getFechaSeleccionada())) {
            if ("Un Mes".equals(getFechaSeleccionada())) {
                gc.add(Calendar.DAY_OF_YEAR, -30);
                for (Iterator it = lineas.iterator(); it.hasNext();) {
                    LineaCambio element = (LineaCambio) it.next();
                    if (element.getUltimaModificacion().compareTo(gc.getTime()) > 0) {
                        lista.add(element);
                    }
                }
            }
            if ("Tres Meses".equals(getFechaSeleccionada())) {
                gc.add(Calendar.DAY_OF_YEAR, -90);
                for (Iterator it = lineas.iterator(); it.hasNext();) {
                    LineaCambio element = (LineaCambio) it.next();
                    if (element.getUltimaModificacion().compareTo(gc.getTime()) > 0) {
                        lista.add(element);
                    }
                }
            }
        }
        else {
            lista = lineas;
        }
        return lista;
    }

    /**
     * Realiza una b&uacute;squeda de las l&iacute;neas de Cr&eacute;dito por Fecha y Estatus
     *
     * @param cycle El ciclo de la p&aacute;gina
     */
    public void searchByDateStatus(IRequestCycle cycle) {
        IxenetValidationDelegate delegates = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        List lista = new ArrayList();
        List list;
        Date hoy = new Date();
        if (getCriterioSeleccionado() == CRITERIO_UNO) {
            list = getSicaServiceData().findAllLineaCreditoCierre();
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                LineaCambio elemento = (LineaCambio) iter.next();
                Date fechaVencimientoElemento = elemento.getVencimiento();
                Calendar gc1 = new GregorianCalendar();
                gc1.setTime(fechaVencimientoElemento);
                gc1.add(Calendar.DAY_OF_MONTH, -15);
                Calendar gc2 = new GregorianCalendar();
                gc2.setTime(fechaVencimientoElemento);
                gc2.add(Calendar.DAY_OF_MONTH, 15);
                if (elemento.getVencimiento().after(gc1.getTime())
                        && elemento.getVencimiento().before(gc2.getTime())
                        && fechaVencimientoElemento.before(hoy)) {
                    debug("En Periodo de Vencimiento");
                    lista.add(elemento);
                }
            }
        }
        if (getCriterioSeleccionado() == CRITERIO_DOS) {
            list = getSicaServiceData().findAllLineaCreditoCierre();
            double porcentajeUso = 0.8;
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                LineaCambio elemento = (LineaCambio) iter.next();
                double usoLinea = elemento.getUsoCash().doubleValue();
//                double importe = elemento.getImporte().doubleValue();
//                if (usoLinea >= (porcentajeUso * importe)) {
//                    lista.add(elemento);
//                }
            }
        }
        if (!(lista.size() > 0)) {
            delegates.record("No hay L\u00edneas de Cr\u00e9dito con los Criterios de B\u00fasqueda"
                    + " especificados.", null);
        }
        setLineasCredito(lista);
    }

    /**
     * Carga el detalle de la l&iacute;nea de cr&eacute;dito; adem&acute;s, llena el mapa del cual
     * se genera el archivo PDF del detalle.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void verDetalle(IRequestCycle cycle) {
        List dealDetallesMap = new ArrayList();
        Integer idLineaCredito = (Integer) cycle.getServiceParameters()[0];
        ReporteDetalleLinCred nextPage = (ReporteDetalleLinCred) cycle.
                getPage("ReporteDetalleLinCred");
        LineaCambio lc = (LineaCambio) getSicaServiceData().find(LineaCambio.class,
                idLineaCredito);
        //todo:
        /*List lineasCreditoLog = getSicaServiceData().
                findHistoriaLineaCreditoLogByidLineaCredito(idLineaCredito);*/
        List lineasCreditoLog = new ArrayList(); 
        List l = new ArrayList();
        double saldo = lc.getImportePAyTF().add(lc.getImporteFV()).doubleValue();
        double saldoAct = 0.00;
        double saldoParcial = 0.00;
        double usoAcumulado = 0.00;
        PrecioReferenciaActual pr = getSicaServiceData().findPrecioReferenciaActual();
        for (Iterator it = lineasCreditoLog.iterator(); it.hasNext();) {
            LineaCambioLog element = (LineaCambioLog) it.next();
            HashMap map = new HashMap();
            if (LineaCambio.STATUS_ACTIVADA.equals(lc.getStatusLinea())) {
                map.put("status", "ACTIVADA");
            }
            else if (LineaCambio.STATUS_APROBADA.equals(lc.getStatusLinea())) {
                map.put("status", "APROBADA");
            }
            else if (LineaCambio.STATUS_SOLICITUD.equals(lc.getStatusLinea())) {
                map.put("status", "SOLICITUD");
            }
            else if (LineaCambio.STATUS_SUSPENDIDA.equals(lc.getStatusLinea())) {
                map.put("status", "SUSPENDIDA");
            }
            else {
                map.put("status", "VENCIDA");
            }
            map.put("corporativo", lc.getCorporativo().getNombreCompleto());
            map.put("tipoLineaCredito", "TODAS");
            map.put("importe", new Double(lc.getImportePAyTF().add(lc.getImporteFV()).doubleValue()));
            map.put("promedioLinea", new Double(lc.getPromedioLinea().doubleValue()));
            map.put("ultimaModificacion", lc.getUltimaModificacion());
            map.put("vencimiento", lc.getVencimiento());
            map.put("numeroExcepciones", new Integer(lc.getNumeroExcepcionesMes()));
            map.put("numeroExcepcionesMes", new Integer(lc.getNumeroExcepcionesMes()));
            map.put("saldoFinal", new Double(lc.getImportePAyTF().add(lc.getImporteFV()).doubleValue() - lc.getUsoCash().doubleValue()));
            if ("A".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Aumento");
                saldoParcial = Math.abs(saldo) + saldoAct;
                saldo = element.getImporte().doubleValue() - usoAcumulado;
                map.put("saldo", new Double(saldo));
            }
            else if ("D".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Disminución");
                saldo = element.getImporte().doubleValue() - usoAcumulado;
                map.put("saldo", new Double(saldo));
            }
            else if ("V".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Vencimiento");
                map.put("saldo", new Double(saldo));
            }
            else if ("U".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Uso");
                usoAcumulado += element.getImporte().doubleValue();
                saldo -= element.getImporte().doubleValue();
                map.put("saldo", new Double(saldo));
            }
            else if ("L".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Liberación");
                saldo += element.getImporte().doubleValue();
                usoAcumulado -= element.getImporte().doubleValue();
                map.put("saldo", new Double(saldo));
            }
            else if ("S".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Suspensión");
                map.put("saldo", new Double(saldo));
            }
            else if ("T".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Alta");
                saldo = element.getImporte().doubleValue();
                saldoAct = element.getImporte().doubleValue();
                map.put("saldo", new Double(saldo));
            }
            else if ("P".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Aprobación");
                map.put("saldo", new Double(saldo));
            }
            else if ("C".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Activación");
                map.put("saldo", new Double(saldo));
            }
            else if ("E".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Excedente");
            }
            else {
                map.put("operacion", "Aviso");
            }
            map.put("fechaOperacion", element.getFechaOperacion());
            map.put("usuario", element.getUsuario().getPersona().getNombreCompleto());
            DealDetalle det = null;
            Deal deal = null;
            if (element.getIdDealDetalle() != null) {
            	try {
                	det = getSicaServiceData().findDealDetalle(element.getIdDealDetalle().intValue());
                	map.put("idDeal", new Integer(det.getDeal().getIdDeal()));
                	deal = getSicaServiceData().findDeal(det.getDeal().getIdDeal());
            	} catch (SicaException e) {
		            throw new ApplicationRuntimeException(e);
		        }
            }
            double fd = 0;
            double tipoCambio = 0;
            if (det != null) {
            	if (det.getFactorDivisa() != null) {
        			fd = det.getFactorDivisa().doubleValue();
            	}
    		}
            else {
            	fd = pr.getPreRef().getMidSpot().doubleValue();
            }
            if (element.getDivisa() != null) {
            	if (element.getDivisa().getIdDivisa().equals(Divisa.DOLAR)) {
                    map.put("monto", new Double(element.getImporte().doubleValue()));
                    map.put("importeUsd", new Double(element.getImporte().doubleValue()));
                }
            	else if (element.getDivisa().getIdDivisa().equals(Divisa.PESO)) {
            		map.put("monto", new Double(element.getImporte().doubleValue()));
            		if(deal != null){
            			map.put("importeUsd", new Double(deal.getMontoUSD()));
            		}
            	}
            	else {
                    map.put("monto", new Double(element.getImporte().doubleValue()));
                    map.put("importeUsd", new Double(element.getImporte().doubleValue() * fd));
                }
            	map.put("divisa", element.getDivisa().getIdDivisa());
            }
            
            else {
                map.put("importeUsd", new Double(element.getImporte().doubleValue()));
                map.put("monto", new Double(element.getImporte().doubleValue()));
            }
            dealDetallesMap.add(map);
        }
        if (!("Todo").equals(getFechaSeleccionada())) {
            Calendar gc = new GregorianCalendar();
            if ("Un Mes".equals(getFechaSeleccionada())) {
                gc.add(Calendar.DAY_OF_YEAR, -30);
            }
            else if ("Tres Meses".equals(getFechaSeleccionada())) {
                gc.add(Calendar.DAY_OF_YEAR, -90);
            }
            for (Iterator iter = dealDetallesMap.iterator(); iter.hasNext();) {
                HashMap elemento = (HashMap) iter.next();
                if (((Date) (elemento.get("fechaOperacion"))).compareTo(gc.getTime()) > 0) {
                    l.add(elemento);
                }
            }
        }
        else {
            l = dealDetallesMap;
        }
        nextPage.setCorporativo(lc.getCorporativo().getNombreCompleto());
        nextPage.setUltimaModificacion(lc.getUltimaModificacion());
        nextPage.setVencimiento(lc.getVencimiento());
        if (LineaCambio.STATUS_ACTIVADA.equals(lc.getStatusLinea())) {
            nextPage.setStatus("ACTIVADA");
        }
        else if (LineaCambio.STATUS_APROBADA.equals(lc.getStatusLinea())) {
            nextPage.setStatus("APROBADA");
        }
        else if (LineaCambio.STATUS_SOLICITUD.equals(lc.getStatusLinea())) {
            nextPage.setStatus("SOLICITUD");
        }
        else if (LineaCambio.STATUS_SUSPENDIDA.equals(lc.getStatusLinea())) {
            nextPage.setStatus("SUSPENDIDA");
        }
        else {
            nextPage.setStatus("VENCIDA");
        }
        nextPage.setIdLineaCredito(idLineaCredito);
        nextPage.setImporteGral(lc.getImportePAyTF().add(lc.getImporteFV()).doubleValue());
        nextPage.setPromedioLinea(lc.getPromedioLinea().doubleValue());
        nextPage.setNumeroExcepciones(lc.getNumeroExcepciones());
        nextPage.setNumeroExcepcionesMes(lc.getNumeroExcepcionesMes());
        nextPage.setTipoLineaCredito("TODAS");
        nextPage.setUsoLinea(lc.getUsoCash().doubleValue());
        nextPage.setSaldoFinal(lc.getImportePAyTF().add(lc.getImporteFV()).doubleValue() - lc.getUsoCash().doubleValue());
        nextPage.setLineasCreditoLog(l);
        nextPage.setPaginaAnterior("ReporteLinCredAut");
        cycle.activate("ReporteDetalleLinCred");
    }

    /**
     * Carga los datos para el reporte completo de movimientos de l&iacute;neas de cr&eacute;dito.
     *
     * @param lc La l&iacute;nea de cr&eacute;dito a buscar.
     * @return List.
     */
    private List reportesLineasCredito(LineaCambio lc) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        List dealDetallesMap = new ArrayList();
        Integer idLineaCredito = lc.getIdLineaCambio();
        // todo:
        List lineasCreditoLog = new ArrayList();
        /*List lineasCreditoLog = getSicaServiceData().
                findHistoriaLineaCreditoLogByidLineaCredito(idLineaCredito);*/
        double saldo = lc.getImportePAyTF().add(lc.getImporteFV()).doubleValue();
        double saldoAct = 0.00;
        double saldoParcial = 0.00;
        double usoAcumulado = 0.00;
        boolean primero = true;
        PrecioReferenciaActual pr = getSicaServiceData().findPrecioReferenciaActual();
        if (lineasCreditoLog.isEmpty()) {
            delegate.record("La L\u00ednea de Cr\u00e9dito no tiene ning\u00fan movimiento.", null);
            //return;
        }
        for (Iterator it = lineasCreditoLog.iterator(); it.hasNext();) {
            if (primero) {
                primero = false;
            }
            HashMap map = new HashMap();
            map.put("primero", Boolean.valueOf(primero));
            map.put("corporativo", lc.getCorporativo().getNombreCompleto());
            map.put("tipoLineaCredito", "TODOS");
            map.put("importe", new Double(lc.getImportePAyTF().add(lc.getImporteFV()).doubleValue()));
            map.put("promedioLinea", new Double(lc.getPromedioLinea().doubleValue()));
            map.put("ultimaModificacion", lc.getUltimaModificacion());
            map.put("vencimiento", lc.getVencimiento());
            map.put("numeroExcepciones", new Integer(lc.getNumeroExcepcionesMes()));
            map.put("numeroExcepcionesMes", new Integer(lc.getNumeroExcepcionesMes()));
            LineaCambioLog element = (LineaCambioLog) it.next();
            if (LineaCambio.STATUS_ACTIVADA.equals(lc.getStatusLinea())) {
                map.put("status", "ACTIVADA");
            }
            else if (LineaCambio.STATUS_APROBADA.equals(lc.getStatusLinea())) {
                map.put("status", "APROBADA");
            }
            else if (LineaCambio.STATUS_SOLICITUD.equals(lc.getStatusLinea())) {
                map.put("status", "SOLICITUD");
            }
            else if (LineaCambio.STATUS_SUSPENDIDA.equals(lc.getStatusLinea())) {
                map.put("status", "ACTIVADA");
            }
            else {
                map.put("status", "VENCIDA");
            }
            map.put("saldoFinal", new Double(lc.getImportePAyTF().add(lc.getImporteFV()).doubleValue() - lc.getUsoCash().doubleValue()));
            if ("A".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Aumento");
                saldoParcial = Math.abs(saldo) + saldoAct;
                saldo = element.getImporte().doubleValue() - usoAcumulado;
                map.put("saldo", new Double(saldo));
            }
            else if ("D".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Disminución");
                saldo = element.getImporte().doubleValue() - usoAcumulado;
                map.put("saldo", new Double(saldo));
            }
            else if ("V".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Vencimiento");
                map.put("saldo", new Double(saldo));
            }
            else if ("U".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Uso");
                usoAcumulado += element.getImporte().doubleValue();
                saldo -= element.getImporte().doubleValue();
                map.put("saldo", new Double(saldo));
            }
            else if ("L".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Liberación");
                saldo += element.getImporte().doubleValue();
                usoAcumulado -= element.getImporte().doubleValue();
                map.put("saldo", new Double(saldo));
            }
            else if ("S".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Suspensión");
                map.put("saldo", new Double(saldo));
            }
            else if ("T".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Alta");
                saldo = element.getImporte().doubleValue();
                saldoAct = element.getImporte().doubleValue();
                map.put("saldo", new Double(saldo));
            }
            else if ("P".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Aprobación");
                map.put("saldo", new Double(saldo));
            }
            else if ("C".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Activación");
                map.put("saldo", new Double(saldo));
            }
            else if ("E".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Excedente");
            }
            else {
                map.put("operacion", "Aviso");
            }
            map.put("fechaOperacion", element.getFechaOperacion());
            map.put("usuario", element.getUsuario().getPersona().getNombreCompleto());
            DealDetalle det = null;
            Deal deal = null;
            if (element.getIdDealDetalle() != null) {
            	try {
                	det = getSicaServiceData().findDealDetalle(element.getIdDealDetalle().intValue());
                	map.put("idDeal", new Integer(det.getDeal().getIdDeal()));
                	deal = getSicaServiceData().findDeal(det.getDeal().getIdDeal());
            	} catch (SicaException e) {
		            throw new ApplicationRuntimeException(e);
		        }
            }
            double fd = 0;
            double tipoCambio = 0;
            if (det != null) {
            	if (det.getFactorDivisa() != null) {
        			fd = det.getFactorDivisa().doubleValue();
            	}
    		}
            else {
            	fd = pr.getPreRef().getMidSpot().doubleValue();
            }
            if (element.getDivisa() != null) {
            	if (element.getDivisa().getIdDivisa().equals(Divisa.DOLAR)) {
                    map.put("monto", new Double(element.getImporte().doubleValue()));
                    map.put("importeUsd", new Double(element.getImporte().doubleValue()));
                }
            	else if (element.getDivisa().getIdDivisa().equals(Divisa.PESO)) {
            		map.put("monto", new Double(element.getImporte().doubleValue()));
            		if (deal != null){
            			map.put("importeUsd", new Double(deal.getMontoUSD()));
            		}
            	}
            	else {
                    map.put("monto", new Double(element.getImporte().doubleValue()));
                    map.put("importeUsd", new Double(element.getImporte().doubleValue() * fd));
                }
            	map.put("divisa", element.getDivisa().getIdDivisa());
            }
            
            else {
                map.put("importeUsd", new Double(element.getImporte().doubleValue()));
                map.put("monto", new Double(element.getImporte().doubleValue()));
            }
            dealDetallesMap.add(map);
        }
        List l = new ArrayList();
        if (!"Todo".equals(getFechaSeleccionada())) {
            Calendar gc = new GregorianCalendar();
            if ("Un Mes".equals(getFechaSeleccionada())) {
                gc.add(Calendar.DAY_OF_YEAR, -30);
            }
            else if ("Tres Meses".equals(getFechaSeleccionada())) {
                gc.add(Calendar.DAY_OF_YEAR, -90);
            }
            for (Iterator iter = dealDetallesMap.iterator(); iter.hasNext();) {
                HashMap elemento = (HashMap) iter.next();
                if (((Date) (elemento.get("fechaOperacion"))).compareTo(gc.getTime()) > 0) {
                    l.add(elemento);
                }
            }
        }
        else {
            l = dealDetallesMap;
        }
        return l;
    }

    /**
     * Obtiene el DataSource del reporte del detalle de la L&iacute;nea de Cr&eacute;dito
     *
     * @param id El identificador (no utilizado).
     * @return JRDataSource.
     */
    public JRDataSource getDataSource(String id) {
        List l = new ArrayList();
        for (Iterator it = getLineasCredito().iterator(); it.hasNext();) {
            LineaCambio lc = (LineaCambio) it.next();
            l.addAll(reportesLineasCredito(lc));
        }
        return new ListDataSource(l);
    }

    /**
     * Imprime el reporte de todas las l&iacute;neas de cr&eacute;dito encontradas
     * en la b&uacute;squeda.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void imprimirReporteCompletoXls(IRequestCycle cycle) {
        Object parameters[] = cycle.getServiceParameters();
        String resource = (String) parameters[0];
        String name = (String) parameters[1];
        String id = (String) parameters[2];
        Map reportOutParams = null;
        if (parameters.length > 3) {
            reportOutParams = (Map) parameters[3];
        }
        JasperPrint jasperPrint = null;
        try {
            JRDataSource dataSource = getDataSource(id);
            BaseGlobal global = (BaseGlobal) getGlobal();
            InputStream inputStream = global.getApplicationContext().getResource(resource).
                    getInputStream();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            jasperPrint = JasperFillManager.fillReport(jasperReport, reportOutParams, dataSource);
        }
        catch (JRException e) {
            debug(e);
        }
        catch (IOException e) {
            debug(e);
        }
        try {
            HttpServletResponse response = cycle.getRequestContext().getResponse();
            JRXlsExporter exporter = new JRXlsExporter();
            ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
            exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
                    Boolean.TRUE);
            exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
            exporter.exportReport();
            byte[] bytes2 = xlsReport.toByteArray();
            response.setContentType("application/vnd.ms-excel");
            response.setContentLength(bytes2.length);
            response.setHeader("Content-disposition", "attachment; filename=\"" + name + ".xls\"");
            try {
                ServletOutputStream ouputStream = response.getOutputStream();
                ouputStream.write(bytes2, 0, bytes2.length);
                ouputStream.flush();
                ouputStream.close();
            }
            catch (IOException ioex) {
                debug(ioex);
            }
        }
        catch (JRException e) {
            debug(e);
        }
    }

    /**
     * Obtiene la Fecha Actual del Sistema.
     *
     * @return Date La Fecha.
     */                                                                          
    public Date getFechaActual() {
        Calendar gc = new GregorianCalendar();
        return gc.getTime();
    }

    /**
     * Obtiene la Fecha Actual del Sistema m&aacute;s los D&iacute;as de
     * Vencimiento L&iacute;nea.
     *
     * @param diasVencimiento El n&uacute;mero de d&iacute;as de vencimiento.
     * @return Date.
     */
    public Date getFechaActualMasVencimiento(int diasVencimiento) {
        Calendar gc = new GregorianCalendar();
        gc.add(Calendar.DAY_OF_MONTH, diasVencimiento);
        return gc.getTime();
    }

    /**
     * Permite saber si el usuario del Sistema es la Direcci&oacute;n de
     * Promoci&oacute;n.
     *
     * @return boolean True si el usuario es la Direcci&oacute;n de
     *         Promoci&oacute;n
     */
    public boolean isModoDireccionPromocion() {
        return FacultySystem.SICA_PROMOCION.equals(getModo());
    }

    /**
     * Define si el usuario del Sistema es la Direcci&oacute;n
     * la Mesa de Control.
     *
     * @return boolean.
     */
    public boolean isModoDireccionMesaControl() {
        return FacultySystem.MODO_MESA_CONTROL.equals(getModo());
    }

    /**
     * Modelo del combo de Promotores.
     *
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getPromotoresModel() {
        return new RecordSelectionModel(getPromotoresList(), "idPersona", "nombreCompleto");
    }

    /**
     * Obtiene el valor de movimientosLinea
     *
     * @return int
     */
    public abstract int getMovimientosLinea();

    /**
     * Asigna el valor para movimientosLinea
     *
     * @param movimientosLiena Indica si la linea de credito tiene o no movimientos; 1 si si 0 en
     *  otro caso. Este valor se le asigna al delegate que valida el evento.
     */
    public abstract void setMovimientosLinea(int movimientosLiena);

    /**
     * Obtiene las L&iacute;neas de Cr&eacute;dito.
     *
     * @return List Las l&iacute;neas de Cr&eacute;dito.
     */
    public abstract List getLineasCredito();

    /**
     * Fija las L&iacute;neas de Cr&eacute;dito.
     *
     * @param lineasCredito Los datosa fijar.
     */
    public abstract void setLineasCredito(List lineasCredito);

    /**
     * Establece el Modo de Operaci&oacute;n de la P&aacute;gina:
     * Promoci&oacute;n o Cr&eacute;dito.
     *
     * @param modo El Modo de Operaci&oacute;n.
     */
    public abstract void setModo(String modo);

    /**
     * Obtiene el Modo de Operaci&oacute;n de la P&aacute;gina.
     *
     * @return String el Modo de Operaci&oacute;n de la P&aacute;gina.
     */
    public abstract String getModo();

    /**
     * Obtiene el Promotor seleccionado del combo de Promotores.
     *
     * @return HasMap El Promotor seleccionado.
     */
    public abstract HashMap getPromotor();

    /**
     * Establece el Promotor seleccionado del combo de Promotores.
     *
     * @param promotor El Promotor seleccionado.
     */
    public abstract void setPromotor(HashMap promotor);

    /**
     * Obtiene la Lista de Promotores a popular en el combo de los mismos, de
     * acuerdo a las Jerarqu&iacute;as de Seguridad.
     *
     * @return List Los Promotores.
     */
    public abstract List getPromotoresList();

    /**
     * Fija la Lista de Promotores a popular en el combo de los mismos, de
     * acuerdo a las Jerarqu&iacute;as de Seguridad.
     *
     * @param listaPromotores Los Promotores.
     */
    public abstract void setPromotoresList(List listaPromotores);

    /**
     * Obtiene el Estatus seleccionado para la b&uacute;squeda de L&iacute;neas
     * de Cr&eacute;dito.
     *
     * @return String El Estatus seleccionado.
     */
    public abstract String getEstatus();

    /**
     * Fija el Estatus seleccionado para la b&uacute;squeda de L&iacute;neas de
     * Cr&eacute;dito.
     *
     * @param estatus El Estatus seleccionado.
     */
    public abstract void setEstatus(String estatus);

    /**
     * Permite saber si el usuario del Sistema es la Direcci&Oacute;n de
     * Promoci&oacute;n o el &Aacute;rea de Promoci&oacute;n.
     *
     * @return boolean True si es o no la Direcci&Oacute;n de Promoci&oacute;n.
     */
    public abstract boolean isJefe();

    /**
     * Fija si el usuario del Sistema es la Direcci&Oacute;n de Promoci&oacute;n
     * o el &Aacute;rea de Promoci&oacute;n.
     *
     * @param esONoJefe True si es o no la Direcci&Oacute;n de Promoci&oacute;n.
     */
    public abstract void setJefe(boolean esONoJefe);

    /**
     * Obtiene, en caso de que la Caseta de Cr&eacute;dito Suspenda una
     * L&iacute;nea de Cr&eacute;dito, la Raz&oacute;n de la Suspensi&oacute;.
     *
     * @return String La Raz&oacute;n de la Suspensi&oacute;.
     */
    public abstract String getObservaciones();

    /**
     * En caso de que la Caseta de Cr&eacute;dito Suspenda una L&iacute;nea de
     * Cr&eacute;dito, guarda la Raz&oacute;n de la Suspensi&oacute;.
     *
     * @param observaciones La Raz&oacute;n de la Suspensi&oacute;.
     */
    public abstract void setObservaciones(String observaciones);

    /**
     * Obtiene el T&iacute;tulo a mostrar en el componente de B&uacute;squeda,
     * dependiendo si se entr&oacute; del men&uacute; del Departamento de
     * Promoci&oacute;n o del Departamento de Cr&eacute;dito.
     *
     * @return String El T&iacute;tulo.
     */
    public abstract String getTitleActionPortletBorder();

    /**
     * Fija el T&iacute;tulo a mostrar en el componente de B&uacute;squeda,
     * dependiendo si se entr&oacute; del men&uacute; del Departamento de
     * Promoci&oacute;n o del Departamento de Cr&eacute;dito.
     *
     * @param titleActionPortletBorder El T&iacute;tulo.
     */
    public abstract void setTitleActionPortletBorder(String titleActionPortletBorder);

    /**
     * Regresa el valor de corporativos.
     *
     * @return List.
     */
    public List getCorporativos() {
        return corporativos;
    }

    /**
     * Activa la lista de Corporativos encontrados de acuerdo al criterio de
     * B&uacute;squeda preestablecido.
     *
     * @param corporativos La lista de Corporativos encontrados.
     */
    public void setCorporativos(List corporativos) {
        this.corporativos = corporativos;
    }

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Raz&oacute;n
     * Social.
     *
     * @return String razonSocial.
     */
    public abstract String getRazonSocial();

    /**
     * Fija el valor de razonSocial.
     *
     * @param razonSocial El valor a asignar.
     */
    public abstract void setRazonSocial(String razonSocial);

    /**
     * Regresa el valor de porcentajeUso.
     *
     * @return int.
     */
    public abstract int getPorcentajeUso();

    /**
     * Fija el valor de porcentajeUso.
     *
     * @param porcentajeUso El valor a asignar.
     */
    public abstract void setPorcentajeUso(int porcentajeUso);

    /**
     * Regresa el valor de diasVencimiento.
     *
     * @return int.
     */
    public abstract int getDiasVencimiento();

    /**
     * Fija el valor de diasVencimiento.
     *
     * @param diasVencimiento El valor a asignar.
     */
    public abstract void setDiasVencimiento(int diasVencimiento);

    /**
     * Regresa el valor de fechaSeleccionada.
     *
     * @return String.
     */
    public abstract String getFechaSeleccionada();

    /**
     * Fija el valor de fechaSeleccionada.
     *
     * @param fechaSeleccionada El valor a asignar.
     */
    public abstract void setFechaSeleccionada(String fechaSeleccionada);

    /**
     * Regresa el valor de estadoLinea.
     *
     * @return Double.
     */
    public abstract Double getEstadoLinea();

    /**
     * Fija el valor de estadoLinea.
     *
     * @param estadoLinea El valor a asignar.
     */
    public abstract void setEstadoLinea(Double estadoLinea);

    /**
     * Fija el valor de desProdFech.
     *
     * @param desProdFech El valor a asignar.
     */
    public abstract void setDesProdFech(boolean desProdFech);

    /**
     * Fija el valor de desRadio.
     *
     * @param desRadio El valor a asignar.
     */
    public abstract void setDesRadio(boolean desRadio);

    /**
     * la Lista de los corporativos
     */
    private List corporativos;

    /**
     * Obtiene la lista de lineas de credito econtradas
     * durante la b&uacute;squeda
     *
     * @return List
     */
    public abstract List getResultadosLineasCredito();

    /**
     * Asigna el valor para resultadosLineasCredito.
     *
     * @param resultadosLineasCredito Lista de lineas de credito
     */
    public abstract void setResultadosLineasCredito(List resultadosLineasCredito);

    /**
     * Obtiene el valor del nombre de la persona f&iacute;sica
     *
     * @return String
     */
    public abstract String getNombre();

    /**
     * Asigna el valor del nombre de la persona f&iacute;sica
     *
     * @param nombre Nombre de la persona f&iacute;sica
     */
    public abstract void setNombre(String nombre);

    /**
     * Obtiene el valor del apellido paterno de la personaf&iacute;sica
     *
     * @return String
     */
    public abstract String getPaterno();

    /**
     * Asigna el valor del apellido paterno de la persona f&iacute;sica
     *
     * @param paterno Apellido paterno
     */
    public abstract void setPaterno(String paterno);

    /**
     * Obtiene el valor del apellido materno de la persona f&iacute;sica
     *
     * @return String
     */
    public abstract String getMaterno();

    /**
     * Asigna el valor del apellido materno de la persona f&iacute;sica
     *
     * @param materno Valor del apellido materno
     */
    public abstract void setMaterno(String materno);

    /**
     * Obtiene el valor de CriterioSeleccionado
     *
     * @return int
     */
    public abstract int getCriterioSeleccionado();

    /**
     * Establece el valor para CriterioSeleccionado
     *
     * @param criterioSeleccionado criterio de b&uacute;squeda seleccionado (tipo de
     * Liquidaci&oacute;n)
     */
    public abstract void setCriterioSeleccionado(int criterioSeleccionado);

    /**
     * CRITERIO_UNO Constante para que establece el criterio de b&uacute;squeda
     * de lineas de credito por vencer
     */
    public static final int CRITERIO_UNO = 1;

    /**
     * CRITERIO_DOS Constante para que establece el criterio de b&uacute;squeda
     * de lineas de credito con 80% de uso.
     */
    public static final int CRITERIO_DOS = 2;


}

/*
 * $Id: ReporteLinCredAut.java,v 1.15.38.1.46.2 2016/08/05 21:07:42 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.reportes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import net.sf.jasperreports.engine.JRDataSource;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.bup.model.PersonaMoral;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.seguridad.model.IPerfil;
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
public abstract class ReporteLinCredAut extends SicaPage implements DataSourceProvider {

    /**
     * Impresi&oacute;n de reportes del Historial de L&iacute;neas de
     * Cr&eacute;dito
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        Visit visit = (Visit) getVisit();
        List promotoresTmp;
        List promotores = new ArrayList();
        promotoresTmp = getSeguridadServiceData().findSubordinadosEjecutivo(
                ((SupportEngine) getEngine()).getApplicationName(),
                visit.getUser().getIdPersona(), true);
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
            setPromotor((HashMap) promotores.get(0));
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
        setFechaSeleccionada(getFechaSeleccionada());
        if (StringUtils.isNotEmpty(getRazonSocial().trim()) && getRazonSocial().length() < 3) {
            getDelegate().record("El criterio de b\u00fasqueda Raz\u00fen Social debe tener " +
                    "m\u00e1s de 2 caracteres.", null);
            return;
        }
        if (getEstadoLinea() != null) {
            setDesProdFech(true);
        }
        if (getFechaSeleccionada() != null) {
            setDesRadio(true);
        }
        if (!getDelegate().getHasErrors()) {
            // *******COMIENZA LOGICA DE RAZON SOCIAL*******
            // Verificando que el Usuario del Sistema pueda operar Las Lineas de
            // Credito
            // del Contrato Sica de la Razon Social tecleada.
            List listaVacia = new ArrayList();
            setCorporativos(listaVacia);
            if (StringUtils.isNotEmpty(getRazonSocial())) {
                List corporativos = new ArrayList();
                List contratosSica = new ArrayList();
                Visit visit = (Visit) getVisit();
                if (!isModoDireccionPromocion()) {
                    // Verifico que Existan contratos Sica para la Razon Social
                    // Especificada
                    contratosSica = getSicaServiceData().findContratosSica(getRazonSocial(), "",
                            "", "", "");
                }
                else {
//					Verifico Jerarquias
                    IPerfil perfil = getSeguridadServiceData().findPerfilByUserAndSystem(visit.
                            getUser().getIdUsuario(), ((SupportEngine) getEngine()).
                            getApplicationName());
                    List contratosSicaTemp = getSicaServiceData().findContratosSica(
                            getRazonSocial(), "", "", "", "");
                    for (Iterator iterator = contratosSicaTemp.iterator(); iterator.hasNext();) {
                        Object[] obj = (Object[]) iterator.next();
                        ContratoSica contratoSica = (ContratoSica) obj[0];
                        if (getSeguridadServiceData().isCuentaAsignadaEjecutivo(perfil,
                                contratoSica.getNoCuenta())) {
                            contratosSica.add(obj);
                        }
                    }
                }

                if (contratosSica.isEmpty()) {
                    getDelegate().record("El Cliente no tiene Contrato SICA, o no se " +
                            "encontr\u00f3 el Cliente.", null);
                    return;
                }
                // Verifico que los Contratos Sica encontrados sean de
                // Corporativos
                for (Iterator iterator4 = contratosSica.iterator(); iterator4.hasNext();) {
                    Object[] obj = (Object[]) iterator4.next();
                    ContratoSica contratoSica = (ContratoSica) obj[0];
                    Persona persona = getSicaServiceData().
                            findPersonaContratoSica(contratoSica.getNoCuenta());
                    PersonaMoral corporativo = getSicaServiceData().findCorporativo(persona.
                            getIdPersona());
                    corporativos.add(corporativo == null ? persona : corporativo);
                }
                if (corporativos.isEmpty()) {
                    getDelegate().record("No se encontr\u00f3 el Cliente.", null);
                    return;
                }
            }
            // Se cargan los Corporativos encontrados
            setCorporativos(corporativos);
        }
        // *******TERMINA LOGICA DE RAZON SOCIAL*******
        // Buscando Lineas por Clientes, Promotores, Status, Porcentaje de
        // Uso, Lineas Por Renovar.
        if ("TODOS".equals(getPromotor().get("nombreCompleto"))) {
            setLineasCredito(getLineaCambioServiceData().
                    findLineasCambioByClientesAndEjecutivosAndEstatus(getCorporativos(),
                            ((SupportEngine) getEngine()).getIdTipoEjecutivo(),
                            getPromotoresList(), "%"));
        }
        else {
            setLineasCredito(getLineaCambioServiceData().
                    findLineasDeCreditoByClientesAndEjecutivoAndEstatus(getCorporativos(),
                            ((SupportEngine) getEngine()).getIdTipoEjecutivo(),
                            (Integer) getPromotor().get("idPersona"), "%"));
        }
        if (getLineasCredito().isEmpty()) {
            getDelegate().record("No hay L\u00edneas de Cr\u00e9dito con los Criterios de " +
                    "B\u00fasqueda especificados.", null);
        }
    }

    /**
     * M&eacut;etodo que realiza una b&uacute;squeda de las l&iacute;neas de
     * Cr&eacute;dito por Fecha y Estatus
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void searchByDateStatus(IRequestCycle cycle) {
        List lista = new ArrayList();
        List list;
        if (getEstadoLinea() != null) {
            Calendar gc = new GregorianCalendar();
            gc.add(Calendar.DAY_OF_YEAR, -getDiasVencimiento().intValue());
            list = getSicaServiceData().findAllLineaCreditoCierre();
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                LineaCambio elemento = (LineaCambio) iter.next();
                if (getEstadoLinea().doubleValue() == getDiasVencimiento().intValue() &&
                        elemento.getVencimiento().compareTo(gc.getTime()) > 0) {
                    lista.add(elemento);
                }
                else
                if (getEstadoLinea().doubleValue() == getPorcentajeUso().doubleValue() &&
                        ((elemento.getUsoCash().doubleValue() * 100 /
                                elemento.getImportePAyTF().add(elemento.getImporteFV()).doubleValue()) >
                                getPorcentajeUso().doubleValue())) {
                    lista.add(elemento);
                }
            }
            setLineasCredito(lista);
        }
        if (!(lista.size() > 0)) {
            getDelegate().record("No hay L\u00edneas de Cr\u00e9dito con los Criterios de " +
                    "B\u00fasqueda especificados.", null);
        }
    }

    /**
     * Carga el detalle de la l&iacute;nea de cr&eacute;dito.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void verDetalle(IRequestCycle cycle) {
        List dealDetallesMap = new ArrayList();
        Integer idLineaCambio = (Integer) cycle.getServiceParameters()[0];
        ReporteDetalleLinCred nextPage = (ReporteDetalleLinCred) cycle.
                getPage("ReporteDetalleLinCred");
        LineaCambio lc = (LineaCambio) getSicaServiceData().find(LineaCambio.class, idLineaCambio);
        List lineasCreditoLog = getLineaCambioServiceData().
                findHistoriaLineaCambioLogByIdLineaCredito(idLineaCambio);
        List l = new ArrayList();
        double saldo = lc.getImportePAyTF().add(lc.getImporteFV()).doubleValue();
        double saldoAct = 0.00;
        double saldoParcial = 0.00;
        PrecioReferenciaActual pr = getSicaServiceData().findPrecioReferenciaActual();
        for (Iterator it = lineasCreditoLog.iterator(); it.hasNext();) {
            BaseGlobal global = (BaseGlobal) getGlobal();
            LineaCambioLog element = (LineaCambioLog) it.next();
            HashMap map = new HashMap();
            if (LineaCambio.STATUS_ACTIVADA.equals(lc.getStatusLinea())) {
                map.put("status", "ACTIVADA");
            }
            else if (LineaCambio.STATUS_APROBADA.equals(lc.getStatusLinea())) {
                map.put("status", "APROBADA");
            }
            else if (LineaCambio.STATUS_SOLICITUD
                    .equals(lc.getStatusLinea())) {
                map.put("status", "SOLICITUD");
            }
            else if (LineaCambio.STATUS_SUSPENDIDA.equals(lc
                    .getStatusLinea())) {
                map.put("status", "ACTIVADA");
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
            map.put("numeroExcepciones", new Integer(lc
                    .getNumeroExcepcionesMes()));
            map.put("numeroExcepcionesMes", new Integer(lc
                    .getNumeroExcepcionesMes()));
            map.put("saldoFinal", new Double(lc.getImportePAyTF().add(lc.getImporteFV()).doubleValue() -
                    lc.getUsoCash().doubleValue()));
            if ("A".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Aumento");
                saldoParcial = Math.abs(saldo) + saldoAct;
                map.put("saldo", new Double(saldo));
            }
            else if ("D".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Disminución");
                saldo -= (element.getImporte().doubleValue() *
                        element.getFactorDivisa().doubleValue());
                map.put("saldo", new Double(saldo));
            }
            else if ("V".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Vencimiento");
                saldo = (element.getImporte().doubleValue() *
                        element.getFactorDivisa().doubleValue());
                map.put("saldo", new Double(saldo));
            }
            else if ("U".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Uso");
                saldo -= element.getImporte().doubleValue() *
                        element.getFactorDivisa().doubleValue();
                map.put("saldo", new Double(saldo));
            }
            else if ("L".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Liberación");
                saldo += element.getImporte().doubleValue() *
                        element.getFactorDivisa().doubleValue();
                map.put("saldo", new Double(saldo));
            }
            else if ("S".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Suspención");
                map.put("saldo", new Double(0.00));
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
                saldo = element.getImporte().doubleValue() - saldoParcial;
                map.put("saldo", new Double(saldo));
            }
            else if ("E".equals(element.getTipoOper().trim())) {
                map.put("operacion", "Excedente");
            }
            else {
                map.put("operacion", "Aviso");
            }
            map.put("fechaOperacion", element.getFechaOperacion());
            map.put("usuario", element.getUsuario().getPersona()
                    .getNombreCompleto());
            DealDetalle det = null;
            Deal deal = null;
            if (element.getIdDealDetalle() != null) {
            	try {
                	det = getSicaServiceData().findDealDetalle(element.getIdDealDetalle().
                            intValue());
                	map.put("idDeal", new Integer(det.getDeal().getIdDeal()));
                	deal = getSicaServiceData().findDeal(det.getDeal().getIdDeal());
            	} catch (SicaException e) {
		            throw new ApplicationRuntimeException(e);
		        }
            }
            double fd = 0;
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
                if (((Date) (elemento.get("fechaOperacion"))).compareTo(gc
                        .getTime()) > 0)
                    l.add(elemento);
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
        nextPage.setIdLineaCredito(idLineaCambio);
        nextPage.setImporteGral(lc.getImportePAyTF().add(lc.getImporteFV()).doubleValue());
        nextPage.setPromedioLinea(lc.getPromedioLinea().doubleValue());
        nextPage.setNumeroExcepciones(lc.getNumeroExcepciones());
        nextPage.setNumeroExcepcionesMes(lc.getNumeroExcepcionesMes());
        nextPage.setTipoLineaCredito("TODAS");
        nextPage.setUsoLinea(lc.getUsoCash().doubleValue());
        nextPage.setSaldoFinal((lc.getImportePAyTF().add(lc.getImporteFV()).doubleValue() - lc.getUsoCash().doubleValue()));
        nextPage.setLineasCreditoLog(l);
        nextPage.setPaginaAnterior("ReporteLinCredAut");
        cycle.activate("ReporteDetalleLinCred");
    }


    /**
     * Obtiene el DataSource del reporte del detalle de la L&iacute;nea de Cr&eacute;dito
     *
     * @param id El identificcador.
     * @return JRDataSource
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
    public JRDataSource getDataSource(String id) {
        return new ListDataSource(getResultadosLineasCredito());
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
     * @return Date La Fecha.
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
        return FacultySystem.SICA_APROB_LINCRED_P.equals(getModo());
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
     * @return Double.
     */
    public abstract Double getPorcentajeUso();

    /**
     * Fija el valor de porcentajeUso.
     *
     * @param porcentajeUso El valor a asignar.
     */
    public abstract void setPorcentajeUso(Double porcentajeUso);

    /**
     * Regresa el valor de diasVencimiento.
     *
     * @return Integer.
     */
    public abstract Integer getDiasVencimiento();

    /**
     * Fija el valor de diasVencimiento.
     *
     * @param diasVencimiento El valor a asignar.
     */
    public abstract void setDiasVencimiento(Integer diasVencimiento);

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
	 * Regresa el valor de resultadosLineasCredito.
	 * 
	 * @return List.
	 */
	public abstract List getResultadosLineasCredito();
	
	/**
	 * Fija el valor de  resultadosLineasCredito.
	 * 
	 * @param resultadosLineasCredito La lista de resultados.
	 */
	public abstract void setResultadosLineasCredito(List resultadosLineasCredito);
}

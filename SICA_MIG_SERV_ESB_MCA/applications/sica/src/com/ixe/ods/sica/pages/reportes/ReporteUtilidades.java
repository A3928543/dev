/*
 * $Id: ReporteUtilidades.java,v 1.27.90.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.LlavesMapas;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.dao.ReporteUtilidadesDao;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.tapestry.BaseGlobal;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * P&aacute;gina que permite al usuario imprimir los reportes de Utilidades:<br>
 * <p/>
 * <li>Reporte de Utilidades</li>
 * <li>Resumen de Utilidades</li>
 * <li>Reporte de Utilidades en Excel</li>
 *
 * @author Gerardo Corzo Herrera, Jean C. Favila
 * @version $Revision: 1.27.90.1 $ $Date: 2020/12/01 04:53:01 $
 */
public abstract class ReporteUtilidades extends AbstractReporteUtilidades {

    /**
     * Carga todos los promotores en una lista
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        setModo((String) cycle.getServiceParameters()[0]);
        Visit visit = (Visit) getVisit();
        List promotoresTmp;
        List promotores = new ArrayList();
        // Verificando si se trata del Area de Mesa de Operaci&oacute;n, entonces no se restringe la
        // jerarquia:
        if (visit.hasPermission(FacultySystem.SICA_CAN_MOP)) {
            promotoresTmp = getSicaServiceData().findAllPromotoresSICA(((SupportEngine)
                    getEngine()).getApplicationName());
        }
        else {
            // Obtiene los promotores de la jerarquia para el usuario:
            promotoresTmp = getSeguridadServiceData().findSubordinadosEjecutivo(((SupportEngine)
                    getEngine()).getApplicationName(), visit.getUser().getIdPersona(), true);
        }
        if (promotoresTmp.size() > 0) {
            Collections.sort(promotoresTmp, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((EmpleadoIxe) o1).getNombreCompleto().
                            compareTo(((EmpleadoIxe) o2).getNombreCompleto());
                }
            });
            HashMap hm = new HashMap();
            hm.put(LlavesMapas.ID_PERSONA, new Integer(0));
            hm.put(LlavesMapas.NOMBRE_COMPLETO, Constantes.TODOS);
            promotores.add(hm);
            for (Iterator it = promotoresTmp.iterator(); it.hasNext();) {
                hm = new HashMap();
                EmpleadoIxe promotor = (EmpleadoIxe) it.next();
                hm.put(LlavesMapas.ID_PERSONA, promotor.getIdPersona());
                hm.put(LlavesMapas.NOMBRE_COMPLETO, promotor.getNombreCompleto());
                promotores.add(hm);
            }
            if (!promotores.isEmpty()) {
                setPromotor((HashMap) promotores.get(0));
            }
        }
        setPromotoresList(promotores);
        setDivisaSeleccionada(null);
        setCanalSeleccionado(null);
    }

    /**
     * Regresa un RecordSelectionModel con todos los promotores.
     *
     * @return IPropertySelectionModel
     */
    public IPropertySelectionModel getPromotoresModel() {
        return new RecordSelectionModel(getPromotoresList(), LlavesMapas.ID_PERSONA,
                LlavesMapas.NOMBRE_COMPLETO);
    }

    /**
     * Regresa un RecordSelectionModel con todas las divisas.
     *
     * @return IPropertySelectionModel
     */
    public IPropertySelectionModel getDivisasModel() {
        List divisas = getSicaServiceData().findAllDivisasByOrdenAlfabetico();
        Divisa primerElemento = new Divisa();
        primerElemento.setIdDivisa(ID_CERO);
        primerElemento.setDescripcion("TODAS");
        divisas.add(0, primerElemento);
        return new RecordSelectionModel(divisas, LlavesMapas.ID_DIVISA, "descripcion");
    }

    /**
     * Regresa un RecordSelectionModel con todos los canales.
     *
     * @return IPropertySelectionModel
     */
    public IPropertySelectionModel getCanalesModel() {
        List canales = getSicaServiceData().findAllCanales();
        Canal primerElemento = new Canal();
        primerElemento.setIdCanal(ID_CERO);
        primerElemento.setNombre(Constantes.TODOS);
        canales.add(0, primerElemento);
        return new RecordSelectionModel(canales, LlavesMapas.ID_CANAL, "nombre");
    }

    /**
     * Regresa la fecha de un mes posterior a la fecha de inicio, a las 00:00.
     *
     * @return Date.
     */
    private Date getFechaInicioMasUnMes() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(getRegisterDateDe());
        gc.add(Calendar.MONTH, 1);
        return DateUtils.inicioDia(gc.getTime());
    }

    /**
     * Obtiene del ServiceData los detalles de deal con los criterios de busqueda especificados.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void reportes(IRequestCycle cycle) {
        try {
            setDescargando(true);
            if (getRegisterDateDe() == null) {
                throw new SicaException("Debe definir la fecha de de Inicio.", null);
            }
            List detalles;
            boolean rangoPermitido = isRangoFechasPermitido();
            Date fechaInicio = DateUtils.inicioDia(getRegisterDateDe());
            setRegisterDateDe(fechaInicio);
            Date fechaFin = DateUtils.finDia(rangoPermitido ? getRegisterDateHasta() : fechaInicio);
            setRegisterDateHasta(fechaFin);
            if (fechaFin.getTime() > getFechaInicioMasUnMes().getTime()) {
                throw new SicaException("No se puede consultar un rango de fechas mayor a un mes.");
            }
            setDivisaSeleccionada(getDivisaSeleccionada());
            setCanalSeleccionado(getCanalSeleccionado());
            String idDivisa = "";
            String idCanal = "";
            if (!ID_CERO.equals(getDivisaSeleccionada().getIdDivisa())) {
                idDivisa = getDivisaSeleccionada().getIdDivisa();
            }
            if (!ID_CERO.equals(getCanalSeleccionado().getIdCanal())) {
                idCanal = getCanalSeleccionado().getIdCanal();
            }
            ReporteUtilidadesDao repUtDao = (ReporteUtilidadesDao) getApplicationContext().
                    getBean("reporteUtilidadesDao");
            auditar(null, LogAuditoria.REPORTE_UTILIDADES, getDatosAdicionales(), "INFO", "E");
            if (getPromotor().get(LlavesMapas.NOMBRE_COMPLETO).toString().
                    equals(Constantes.TODOS)) {
                List detallesUtils = repUtDao.findDetallesUtilidades(fechaInicio, fechaFin, null,
                        idDivisa, idCanal, getIdsPromotores());
                detalles = sortDealDetallesCanal(detallesUtils);
            }
            else {
                detalles = repUtDao.findDetallesUtilidades(fechaInicio, fechaFin,
                        (Integer) getPromotor().get(LlavesMapas.ID_PERSONA), idDivisa, idCanal,
                        null);
            }
            if (detalles.isEmpty()) {
                throw new SicaException("No se encontraron datos con los Criterios de " +
                        "B\u00fasqueda especificados.", null);
            }
            seleccionarImpresion(detalles);
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
        finally {
            setDescargando(false);
        }
    }

    /**
     * Regresa la cadena de datos adicionales para la auditor&iacute;a al reporte.
     *
     * @return String.
     */
    private String getDatosAdicionales() {
        return new StringBuffer("prom:").append(getPromotor().
                get(LlavesMapas.ID_PERSONA).toString()).append(" fi:").
                append(DATE_FORMAT.format(getRegisterDateDe())).append(" ff: ").
                append(DATE_FORMAT.format(getRegisterDateHasta())).append(" div:").
                append(getDivisaSeleccionada().getIdDivisa()).append(" cnl:").
                append(getCanalSeleccionado().getIdCanal()).append(" tr:").
                append(getTipoReporte()).toString();
    }

    /**
     * Regresa la lista de n&uacute;meros de persona de los promotores que se encuentran en el
     * combo box.
     *
     * @return List.
     */
    private List getIdsPromotores() {
        List idsPromotores = new ArrayList();
        for (Iterator it = getPromotoresList().iterator(); it.hasNext();) {
            HashMap promotorMap = (HashMap) it.next();
            if (((Integer) promotorMap.get(LlavesMapas.ID_PERSONA)).intValue() != 0) {
                idsPromotores.add(promotorMap.get(LlavesMapas.ID_PERSONA));
            }
        }
        return idsPromotores;
    }

    /**
     * Llama a imprimir(), con las opciones adecuadas.
     *
     * @param detalles La lista de mapas con la informaci&oacute;n para los reportes.
     */
    private void seleccionarImpresion(List detalles) {
        if (COMPLETO_PDF.equals(getTipoReporte())) {
            imprimir("WEB-INF/jasper/ReporteUtilidades.jasper", "ReporteUtilidades", detalles,
                    true);
        }
        else if (COMPLETO_XLS.equals(getTipoReporte())) {
            imprimir("WEB-INF/jasper/ReporteUtilidadesXls.jasper", "ReporteUtilidadesXls", detalles,
                    false);
        }
        else if (RESUMEN_PDF.equals(getTipoReporte())) {
            imprimir("WEB-INF/jasper/RepUtilResumen.jasper", "RepUtilResumen", detalles, true);
        }
        else {
            imprimir("WEB-INF/jasper/RepUtilXls.jasper", "RepUtilXls", detalles, false);
        }
    }

    /**
     * Genera el reporte en PDF o en XLS, de acuerdo a la selecci&oacute;n del usuario.
     *
     * @param resource La ruta y nombre del archivo .jasper.
     * @param name     El nombre para el archivo.
     * @param detalles La lista de detalles de deal a imprimir.
     * @param pdf      True para PDF, False para Xls.
     */
    private void imprimir(String resource, String name, List detalles, boolean pdf) {
        JasperPrint jasperPrint;
        InputStream inputStream = null;
        try {
            JRDataSource dataSource = getDataSource(detalles);
            BaseGlobal global = (BaseGlobal) getGlobal();
            inputStream = global.getApplicationContext().getResource(resource).getInputStream();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
            byte[] bytes;
            HttpServletResponse response = getRequestCycle().getRequestContext().getResponse();
            ByteArrayOutputStream report = new ByteArrayOutputStream();
            if (!pdf) {
                bytes = configurarXls(jasperPrint, report);
            }
            else {
                bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            }
            response.setContentLength(bytes.length);
            response.setContentType(pdf ? "application/pdf" : "application/vnd.ms-excel");
            if (pdf) {
                response.setHeader("Content-Disposition", "attachment;filename=\"" +
                        name + ".pdf" + "\"");
            }
            else {
                response.setHeader("Content-disposition", "attachment; filename=\"" + name +
                        ".xls\"");
            }
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(bytes, 0, bytes.length);
            ouputStream.flush();
            ouputStream.close();
        }
        catch (IOException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
        catch (JRException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException e) {
                debug(e.getMessage(), e);
            }
        }
    }

    /**
     * Configura las opciones para el reporte en excel.
     *
     * @param jasperPrint El JasperPrint.
     * @param report El stream donde se escribir&aacute;n los datos.
     * @return byte[] El arreglo de bytes.
     * @throws JRException Si ocurre un problema.
     */
    private byte[] configurarXls(JasperPrint jasperPrint, ByteArrayOutputStream report)
            throws JRException {
        byte[] bytes;
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,
                        Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
                        Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
                        Boolean.FALSE);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, report);
        exporter.exportReport();
        bytes = report.toByteArray();
        return bytes;
    }

    /**
     * Obtiene el DataSource para el reporte de Utilidades en las modalidades de excel y reporte de
     * Utilidades en PDF.
     *
     * @param detalles La lista de estructuras de detalle de deal.
     * @return JRDataSource.
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
    private JRDataSource getDataSource(List detalles) {
        List l = new ArrayList();
        List dealDetallesMap = new ArrayList();
        for (Iterator it = detalles.iterator(); it.hasNext();) {
            Map element = (Map) it.next();
            if (!Constantes.CANAL_IXE_DIRECTO.equals(element.get(LlavesMapas.ID_CANAL))) {
                HashMap map = new HashMap();
                map.put("utilidadesIxeDirecto", Boolean.FALSE);
                map.put("de", getRegisterDateDe());
                map.put("hasta", getRegisterDateHasta());
                map.put("canal", element.get(LlavesMapas.ID_CANAL));
                map.put(LlavesMapas.ID_PROMOTOR, element.get(LlavesMapas.ID_PROMOTOR).toString());
                map.put(LlavesMapas.PROMOTOR, element.get(LlavesMapas.PROMOTOR));
                map.put(LlavesMapas.DIVISA, ((String) element.get(LlavesMapas.DIVISA)).
                        toUpperCase());
                map.put(LlavesMapas.ID_DEAL, element.get(LlavesMapas.ID_DEAL));
                map.put(LlavesMapas.FOLIO_DETALLE, element.get(LlavesMapas.FOLIO_DETALLE));
                map.put(LlavesMapas.FECHA_CAPTURA, element.get(LlavesMapas.FECHA_CAPTURA));
                if (((Boolean) element.get("recibimos")).booleanValue()) {
                    map.put(LlavesMapas.TIPO_OPERACION, "Compra");
                    map.put(LlavesMapas.SOBRE_PRECIO, new Double(((Double) element.
                            get(LlavesMapas.TIPO_CAMBIO_MESA)).doubleValue() -
                            ((Double) element.get(LlavesMapas.TIPO_CAMBIO)).doubleValue()));
                }
                else {
                    map.put(LlavesMapas.TIPO_OPERACION, "Venta");
                    map.put(LlavesMapas.SOBRE_PRECIO, new Double(((Double) element.
                            get(LlavesMapas.TIPO_CAMBIO)).doubleValue() -
                            ((Double) element.get(LlavesMapas.TIPO_CAMBIO_MESA)).doubleValue()));
                }
                if (element.get(LlavesMapas.CLAVE_FORMA_LIQUIDACION) != null) {
                    String ticket = ((Visit) getVisit()).getTicket();
                    map.put("producto", getFormasPagoLiqService().getNombreFormaLiquidacion(ticket,
                            (String) element.get(LlavesMapas.CLAVE_FORMA_LIQUIDACION)));
                }
                llenarDatosAdicionales(element, map);
                if (!l.contains(map.get(LlavesMapas.ID_DEAL))) {
                    l.add(map.get(LlavesMapas.ID_DEAL));
                }
                map.put("granTotalNumeroDeals", new Integer(l.size()));
                calcularComisiones(element, map);
                dealDetallesMap.add(map);
            }
        }
        return new ListDataSource(dealDetallesMap);
    }

    /**
     * Llena los campos de cliente, monto, tipo de cambio y utilidad.
     *
     * @param element El detalle de deal.
     * @param map El resultado.
     */
    private void llenarDatosAdicionales(Map element, HashMap map) {
        map.put(LlavesMapas.CLIENTE, element.get(LlavesMapas.CLIENTE));
        map.put(LlavesMapas.MONTO, element.get(LlavesMapas.MONTO));
        map.put(LlavesMapas.TIPO_CAMBIO, element.get(LlavesMapas.TIPO_CAMBIO));
        map.put("utilidad", new Double(((Double) element.get(LlavesMapas.MONTO)).
                doubleValue() *
                ((Double) map.get(LlavesMapas.SOBRE_PRECIO)).doubleValue()));
    }

    /**
     * Llena los valores de comisiones.
     *
     * @param element El detalle de deal.
     * @param map El resultado.
     */
    private void calcularComisiones(Map element, HashMap map) {
        Double comisionOfUSD = (Double) element.get("comisionOficialUsd");
        Double comisionCobradaUSD = (Double) element.get("comisionCobradaUsd");
        if (comisionOfUSD.doubleValue() > 0) {
            map.put(LlavesMapas.COSTO_COBRADO, comisionCobradaUSD);
            if (comisionCobradaUSD.doubleValue() >= comisionOfUSD.doubleValue()) {
                map.put(LlavesMapas.COSTO_NO_COBRADO, new Double(0));
            }
            else {
                map.put(LlavesMapas.COSTO_NO_COBRADO,
                        new Double(comisionOfUSD.doubleValue() -
                                comisionCobradaUSD.doubleValue()));
            }
        }
        if (comisionOfUSD.doubleValue() == 0 && comisionOfUSD.doubleValue() == 0) {
            map.put(LlavesMapas.COSTO_COBRADO, new Double(0));
            map.put(LlavesMapas.COSTO_NO_COBRADO, new Double(0));
        }
    }

    /**
     * Ordena los Detalles de Deal alamcenados en la lista por Sucursal, Divisa,
     * Compras/Ventas, Producto y Id Deal.
     *
     * @param dd La lista de detalles de Deals.
     * @return List
     */
    private List sortDealDetallesCanal(List dd) {
        Collections.sort(dd, new Comparator() {
            public int compare(Object o1, Object o2) {
                Map dd1 = (Map) o1;
                Map dd2 = (Map) o2;
                Integer id1 = (Integer) dd1.get(LlavesMapas.ID_DEAL);
                Integer id2 = (Integer) dd2.get(LlavesMapas.ID_DEAL);
                int comp = ((String) dd1.get(LlavesMapas.ID_CANAL)).compareTo(
                        dd2.get(LlavesMapas.ID_CANAL));
                if (comp != 0) {
                    //Devuelve el valor de la comprarcion por Canal
                    return comp;
                }
                else {
                    comp = dd1.get(LlavesMapas.ID_PROMOTOR).toString().compareTo(
                            dd2.get(LlavesMapas.ID_PROMOTOR).toString());
                    if (comp != 0) {
                        //Devuelve el valor de la comprarcion por Id Promotor
                        return comp;
                    }
                    else {
                        comp = dd1.get(LlavesMapas.ID_DIVISA).toString().compareTo(
                                dd2.get(LlavesMapas.ID_DIVISA).toString());
                        if (comp != 0) {
                            //Devuelve el valor de la comparacion por Divisa.
                            return comp;
                        }
                        else {
                            //Devuelve el valor de la comprarcion por Id Deal
                            return id1.toString().compareTo(id2.toString());
                        }
                    }
                }
            }
        });
        return dd;
    }

    /**
     * Regresa un StringPropertySelectionModel con las opciones de Salida del Reporte.
     *
     * @return org.apache.tapestry.form.StringPropertySelectionModel.
     */
    public IPropertySelectionModel getTiposReportesModel() {
        return new StringPropertySelectionModel(REPORTES);
    }

    /**
     * Regresa el valor de divisaSeleccionada.
     *
     * @return Divisa.
     */
    public abstract Divisa getDivisaSeleccionada();

    /**
     * Fija el valor de divisaSeleccionada.
     *
     * @param divisaSeleccionada El valor a asignar.
     */
    public abstract void setDivisaSeleccionada(Divisa divisaSeleccionada);

    /**
     * Regresa el valor de canalSeleccionado.
     *
     * @return Canal.
     */
    public abstract Canal getCanalSeleccionado();

    /**
     * Asigna el valor para el canal.
     *
     * @param canalSeleccionado El valor para el canal.
     */
    public abstract void setCanalSeleccionado(Canal canalSeleccionado);

    /**
     * Regresa el valor de operacionSeleccionada.
     *
     * @return String.
     */
    public abstract String getOperacionSeleccionada();

    /**
     * Fija el valor de operacionSeleccionada.
     *
     * @param operacionSeleccionada El valor a asignar.
     */
    public abstract void setOperacionSeleccionada(String operacionSeleccionada);

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
     * Obtiene la Lista de Promotores a popular en el combo de los mismos, de acuerdo a las
     * Jerarqu&iacute;as de Seguridad.
     *
     * @return List Los Promotores.
     */
    public abstract List getPromotoresList();

    /**
     * Fija la Lista de Promotores a popular en el combo de los mismos,
     * de acuerdo a las Jerarqu&iacute;as de Seguridad.
     *
     * @param listaPromotores Los Promotores.
     */
    public abstract void setPromotoresList(List listaPromotores);

    /**
     * Regresa el valor de modo.
     *
     * @return String.
     */
    public abstract String getModo();

    /**
     * Establece el valor de modo.
     *
     * @param modo El valor a asignar.
     */
    public abstract void setModo(String modo);

    /**
     * Constante para la cadena "0".
     */
    public static final String ID_CERO = "0";

    /**
     * Constante para la opci&oacute;n del Resumen en PDF.
     */
    public static final String RESUMEN_PDF = "Resumen (PDF)";

    /**
     * Constante para la opci&oacute;n del Resumen en XLS.
     */
    public static final String RESUMEN_XLS = "Resumen (Excel)";

    /**
     * Constante con los 4 tipos de reportes.
     */
    public static final String[] REPORTES = {COMPLETO_PDF, COMPLETO_XLS, RESUMEN_PDF, RESUMEN_XLS};
}

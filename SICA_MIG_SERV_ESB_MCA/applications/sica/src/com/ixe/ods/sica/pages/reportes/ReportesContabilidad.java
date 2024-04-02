/*
 * $Id: ReportesContabilidad.java,v 1.28.10.2 2010/10/25 23:12:27 egordoa Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.bup.model.Sucursal;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.Posicion;
import com.ixe.ods.sica.model.RecoUtilidad;
import com.ixe.ods.sica.model.UtilidadForward;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.ContabilidadSicaServiceData;
import com.ixe.ods.sica.services.impl.XlsServiceImpl;
import com.ixe.ods.sica.services.model.ModeloXls;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.tapestry.BaseGlobal;

/**
 * P&aacute;gina que permite generar los reportes de contabilidad.
 *
 * @author Gustavo Gonzalez
 * @version $Revision: 1.28.10.2 $ $Date: 2010/10/25 23:12:27 $
 */
public abstract class ReportesContabilidad extends SicaPage {

    /**
     * Asigna los valores de los componentes de la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&acute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        setRegisterDate(new Date());
        setUtilidadesForwards(new HashMap());
    }

    /**
     * Regresa la lista de estados v&aacute;lidos: operaci&oacute;n normal, restringida, vespertina,
     * fin de liquidaciones, generaci&oacute;n contable y operaci&oacute;n nocturna.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[]{Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
                Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_GENERACION_CONTABLE,
                Estado.ESTADO_OPERACION_NOCTURNA
        };
    }

    /**
     * Modelo que contiene las  divisas frecuentes para la forma
     * de liquidaci&oacute;n Transferencia Internacional.
     *
     * @return StringPropertySelectionModel
     */
    public StringPropertySelectionModel getReporte() {
        return new StringPropertySelectionModel(new String[]{"P\u00f3lizas Contables Resumen",
                "P\u00f3lizas Contables Resumen Excel", REPORTE_UT_PROMOCION,
                REPORTE_UT_PROMOCION_EXCEL, REPORTE_UT_GLOBALES, REPORTE_UT_GLOBAL_EXCEL,
                "Utilidades IXE Forwards", REPORTE_RESUMEN_UT_PROMOCION_EXCEL,
                REPORTE_RESUMEN_UT_PROMOCION, REPORTE_DEALS_PENDIENTES_LIQUIDAR_EXCEL,
                REPORTE_DEALS_PENDIENTES_LIQUIDAR});
    }

    /**
     * Obtiene el template para generar el reporte seleccionado.
     *
     * @param cycle El ciclo de la p&acute;gina.
     */
    public void obtenerReportes(IRequestCycle cycle) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
    	if (getRegisterDate() == null) {
    		delegate.record("Debe ingresar una fecha válida.", null);
    		return;
    	}
    	
        Object parameters[] = null;
        if (REPORTE_UT_IXE_FORWARDS.equals(getReporteSeleccionado())) {
            generarReporteIxeForwards();
        }
        else if (REPORTE_RESUMEN_UT_PROMOCION_EXCEL.equals(getReporteSeleccionado())) {
            generaReporteResumenUtilidadesPromocionXls(cycle);
        }
        else if (REPORTE_DEALS_PENDIENTES_LIQUIDAR_EXCEL.equals(getReporteSeleccionado())) {
            generaReporteDealPendientesLiquidarXls(cycle);
        }
        else if (getReporteSeleccionado().indexOf("Excel") > 0) {
            if (REPORTE_POLIZAS_CONTABLES_XLS.equals(getReporteSeleccionado())) {
                parameters = new Object[]{"WEB-INF/jasper/ReportePolizasContablesResumenXls.jasper",
                        "ReportePolizasContablesResumenXls", "0"};
            }
            if (REPORTE_UT_PROMOCION_EXCEL.equals(getReporteSeleccionado())) {
                parameters = new Object[]{"WEB-INF/jasper/RepUtMayoreoXls.jasper",
                        "ReporteUtilidadesPromocionXls", "0"};
            }
            if (REPORTE_UT_GLOBAL_EXCEL.equals(getReporteSeleccionado())) {
                parameters = new Object[]{"WEB-INF/jasper/RepUtMayoreoContableXls.jasper",
                        "ReporteUtilidadesGlobalesXls", "0"};
            }
            cycle.setServiceParameters(parameters);
            imprimirReporteXls(cycle);
        }
        else {
            if (REPORTE_CONCILIACION_DIARIA.equals(getReporteSeleccionado())) {
                parameters = new Object[]{"WEB-INF/jasper/ReporteConciliacionDiaria.jasper",
                        "ReporteConciliacionDiaria", "0"};
            }
            if (REPORTE_POLIZAS_CONTABLES.equals(getReporteSeleccionado())) {
                parameters = new Object[]{"WEB-INF/jasper/ReportePolizasContablesResumen.jasper",
                        "ReportePolizasContablesResumen", "0"};
            }
            if (REPORTE_UT_PROMOCION.equals(getReporteSeleccionado())) {
                parameters = new Object[]{"WEB-INF/jasper/RepUtMayoreoContable.jasper",
                        "ReporteUtilidadesPromocion", "0"};
            }
            if (REPORTE_UT_GLOBALES.equals(getReporteSeleccionado())) {
                parameters = new Object[]{"WEB-INF/jasper/RepUtilidadesMayoreo.jasper",
                        "ReporteUtilidadesGlobales", "0"};
            }
            if (REPORTE_RESUMEN_UT_PROMOCION.equals(getReporteSeleccionado())) {
                parameters = new Object[]{"WEB-INF/jasper/ResumenUtilidadesPromocionRep.jasper",
                        "RepResumenUtilidadesPromocion", "0"};
            }
            if (REPORTE_DEALS_PENDIENTES_LIQUIDAR.equals(getReporteSeleccionado())) {
                parameters = new Object[]{"WEB-INF/jasper/ReporteDealsPendientesLiquidarT4.jasper",
                        "RepDeasPendientesLiquidarT4", "0"};
            }
            cycle.setServiceParameters(parameters);
            imprimirReporte(cycle);
        }
    }

    /**
     * Obtiene las Utilidades de IXE Forwards en una Fecha espec&iacute;fica.
     */
    private void generarReporteIxeForwards() {
        ContabilidadSicaServiceData csd = (ContabilidadSicaServiceData) getApplicationContext().
                getBean("contabilidadServiceData");
        Map utilidadesForwards = csd.findUtilidadForwardByFecha(getRegisterDate());
        setUtilidadesForwards(utilidadesForwards);
        if (utilidadesForwards.keySet().isEmpty()) {
            getDelegate().record("No se encontraron registros.", null);
        }
    }

    /**
     * Imprime el reporte de contabilidad seleccionado en formato Pdf.
     *
     * @param cycle El ciclo de la p&aacute;gina
     */
    public void imprimirReporte(IRequestCycle cycle) {
        Object parameters[] = cycle.getServiceParameters();
        String resource = (String) parameters[0];
        String name = (String) parameters[1];
        Map reportOutParams = null;
        JRDataSource dataSource = null;
        if (parameters.length > 3) {
            reportOutParams = (Map) parameters[3];
        }
        JasperPrint jasperPrint;
        InputStream inputStream = null;
        try {
            if (REPORTE_CONCILIACION_DIARIA.equals(getReporteSeleccionado())) {
                dataSource = getDataSourceReporteConciliacionDiaria("");
            }
            if (REPORTE_POLIZAS_CONTABLES.equals(getReporteSeleccionado())) {
                dataSource = getDataSourceReportePolizasContables("");
            }
            if (REPORTE_UT_PROMOCION.equals(getReporteSeleccionado())) {
                dataSource = getDataSourceReporteUtMayoreoContable();
            }
            if (REPORTE_UT_GLOBALES.equals(getReporteSeleccionado())) {
                dataSource = getDataSourceReporteUtMayoreoInterno();
            }
            if (REPORTE_UT_IXE_FORWARDS_PDF.equals(getReporteSeleccionado())) {
                dataSource = getDataSourceReporteUtIxeForwards();
            }
            if (REPORTE_RESUMEN_UT_PROMOCION.equals(getReporteSeleccionado())){
            	dataSource = getDataSourceReporteUtPromo();
            }
            if (REPORTE_DEALS_PENDIENTES_LIQUIDAR.equals(getReporteSeleccionado())){
            	dataSource = getDataSourceReporteDealsPendientesLiquidarT4();
            }
            BaseGlobal global = (BaseGlobal) getGlobal();
            inputStream = global.getApplicationContext().getResource(resource).getInputStream();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            jasperPrint = JasperFillManager.fillReport(jasperReport, reportOutParams, dataSource);
        }
        catch (Exception e) {
            warn(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
            return;
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException e) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug(e);
                }
                getDelegate().record(e.getMessage(), null);
                return;
            }
        }
        try {
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpServletResponse response = cycle.getRequestContext()
                    .getResponse();
            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);
            response.setHeader("Content-Disposition", "attachment;filename=\"" + name + ".pdf" +
                    "\"");
            try {
                ServletOutputStream ouputStream = response.getOutputStream();
                ouputStream.write(bytes, 0, bytes.length);
                ouputStream.flush();
                ouputStream.close();
            }
            catch (IOException ioex) {
                warn(ioex.getMessage(), ioex);
            }
        }
        catch (JRException e) {
            warn(e.getMessage(), e);
        }
    }

    /**
     * Genera el reporte seleccionado en formato Xls.
     *
     * @param cycle El ciclo de la p&acute;gina.
     */
    public void imprimirReporteXls(IRequestCycle cycle) {
        Object parameters[] = cycle.getServiceParameters();
        String resource = (String) parameters[0];
        String name = (String) parameters[1];
        Map reportOutParams = null;
        JRDataSource dataSource = null;
        if (parameters.length > 3) {
            reportOutParams = (Map) parameters[3];
        }
        JasperPrint jasperPrint;
        InputStream inputStream;
        try {
            if (REPORTE_POLIZAS_CONTABLES_XLS.equals(getReporteSeleccionado())) {
                dataSource = getDataSourceReportePolizasContables("");
            }
            if (REPORTE_UT_PROMOCION_EXCEL.equals(getReporteSeleccionado())) {
                dataSource = getDataSourceReporteUtMayoreoContable();
            }
            if (REPORTE_UT_GLOBAL_EXCEL.equals(getReporteSeleccionado())) {
                dataSource = getDataSourceReporteUtMayoreoInterno();
            }
            BaseGlobal global = (BaseGlobal) getGlobal();
            inputStream = global.getApplicationContext().getResource(resource).getInputStream();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            jasperPrint = JasperFillManager.fillReport(jasperReport, reportOutParams, dataSource);
        }
        catch (Exception e) {
            warn(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
            return;
        }
        try {
            HttpServletResponse response = cycle.getRequestContext()
                    .getResponse();
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
                if (_logger.isDebugEnabled()) {
                    _logger.debug(ioex);
                }
                ioex.printStackTrace();
            }
        }
        catch (JRException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
            e.printStackTrace();
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException e) {
                debug(e.getMessage(), e);
                getDelegate().record(e.getMessage(), null);
            }
        }
    }

    /**
     * Obtiene los datos para el reporte de Auxiliares Contables.
     *
     * @param id El identificador.
     * @return JRDataSource
     * @throws Exception Si algo sale mal.
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
    public JRDataSource getDataSourceReporteAuxiliaresContables(String id) throws Exception {
        Calendar gcInicio = new GregorianCalendar();
        Calendar gcFin = new GregorianCalendar();
        gcInicio.setTime(getRegisterDate());
        gcFin.setTime(getRegisterDate());
        gcInicio.set(Calendar.HOUR_OF_DAY, 0);
        gcInicio.set(Calendar.MINUTE, 0);
        gcInicio.set(Calendar.SECOND, 0);
        gcInicio.set(Calendar.MILLISECOND, 0);
        gcFin.add(Calendar.DAY_OF_MONTH, 1);
        gcFin.add(Calendar.HOUR_OF_DAY, -gcFin.get(Calendar.HOUR_OF_DAY));
        gcFin.add(Calendar.MINUTE, -gcFin.get(Calendar.MINUTE));
        gcFin.add(Calendar.SECOND, -gcFin.get(Calendar.SECOND));
        gcFin.add(Calendar.MILLISECOND, -gcFin.get(Calendar.MILLISECOND));
        List polizas = new ArrayList();
        try {
            polizas = getSicaServiceData().
                    findAllDealsReporteAuxiliaresContables(gcInicio.getTime(), gcFin.getTime());
        }
        catch (Exception e) {
            debug(e.getMessage(), e);
        }
        if (polizas.isEmpty()) {
            throw new Exception("No hay movimientos para la fecha seleccionada.");
        }
        List listaPolizasDetalles = new ArrayList();
        for (Iterator it = polizas.iterator(); it.hasNext();) {
            Object[] elements = (Object[]) it.next();
            HashMap map = new HashMap();
            Divisa divisa = getSicaServiceData().findDivisa((String) elements[0]);
            map.put("divisa", divisa.getDescripcion());
            String cuentaEntidad = (elements[1] + " " + elements[2]);
            map.put("cuentaContable", cuentaEntidad);
            Integer idDeal = (Integer) elements[3];
            map.put("idDeal", idDeal.toString());
            String cargoAbono = (String) elements[4];
            if (CARGO_ASIENTO.equals(cargoAbono) || CARGO_LIQ.equals(cargoAbono)) {
                map.put("cargos", elements[5]);
                map.put("abonos", new Double(0.00));
            }
            if (ABONO_ASIENTO.equals(cargoAbono) || ABONO_LIQ.equals(cargoAbono)) {
                map.put("cargos", new Double(0.00));
                map.put("abonos", elements[5]);
            }
            String contratoSica = (String) elements[6];
            map.put("contratoSica", contratoSica);
            map.put("cliente", "");
            map.put("fechaValor", gcInicio.getTime());
            String referencia = (String) elements[8];
            map.put("referencia", referencia);
            map.put("entidad", "");
            listaPolizasDetalles.add(map);
        }
        return new ListDataSource(listaPolizasDetalles);
    }

    /**
     * Crea el mapa con los datos necesarios para generar el reporte de utilidades.
     *
     * @param det  El detalle de deal.
     * @param tipo El tipo.
     * @return HashMap.
     */
    private HashMap crearMapaUtilidades(DealDetalle det, String tipo) {
        HashMap mapaDet = new HashMap();
        double utilCompras = (det.getTipoCambioMesa() - det.getTipoCambio()) * det.getMonto();
        double utilVentas = (det.getTipoCambio() - det.getTipoCambioMesa()) * det.getMonto();
        if (det.isRecibimos()) {
            mapaDet.put("utilidadCompras", new Double(utilCompras));
            mapaDet.put("utilidadVentas", new Double(0));
            mapaDet.put("montoCompras", new Double(det.getMonto()));
            mapaDet.put("montoVentas", new Double(0));
            mapaDet.put("tipoDeCambioCompra", new Double(det.getTipoCambio()));
            mapaDet.put("tipoDeCambioVenta", new Double(0));
            mapaDet.put("utilidadPeriodo", new Double(utilCompras));
        }
        else {
            mapaDet.put("utilidadCompras", new Double(0));
            mapaDet.put("utilidadVentas", new Double(utilVentas));
            mapaDet.put("montoCompras", new Double(0));
            mapaDet.put("montoVentas", new Double(det.getMonto()));
            mapaDet.put("tipoDeCambioCompra", new Double(0));
            mapaDet.put("tipoDeCambioVenta", new Double(det.getTipoCambio()));
            mapaDet.put("utilidadPeriodo", new Double(utilVentas));
        }
        mapaDet.put("tipoDeal", tipo);
        mapaDet.put("utilidadGlobal", new Double(0));
        mapaDet.put("divisaDescripcion", det.getDivisa().getDescripcion());
        mapaDet.put("idMesaCambio", det.getMesaCambio().getNombre() + " Divisa Referencia: " +
                det.getMesaCambio().getDivisaReferencia().getDescripcion());
        mapaDet.put("fechaReporte", det.getDeal().getFechaCaptura());
        return mapaDet;
    }

    /**
     * Crea el mapa con los datos necesarios para generar el reporte de utilidades.
     *
     * @param det  El detalle de deal.
     * @param tipo El tipo.
     * @return HashMap.
     */
    private HashMap crearMapaUtilidadesGlobales(DealDetalle det, String tipo, Date gcInicio,
                                                Date gcFin, String idDivisa, int idMesaCambio,
                                                double utilidadFwds) {
        HashMap mapaDet = new HashMap();
        boolean isDeSucursal = false;
        double utilCompras = (det.getTipoCambioMesa() - det.getTipoCambio()) * det.getMonto();
        double utilVentas = (det.getTipoCambio() - det.getTipoCambioMesa()) * det.getMonto();
        
        //Si el canal corresponde a una sucursal la utilidad se suma a la 
        //utilidad en sucursales o de otra manera a la utilidad de promocion.
        if(det.getDeal().getCanalMesa().getCanal().getSucursal() != null){
        	isDeSucursal = true;
        }
        
        if (det.isRecibimos()) {
            mapaDet.put("utilidadCompras", new Double(utilCompras));
            mapaDet.put("utilidadVentas", new Double(0));
            mapaDet.put("montoCompras", new Double(det.getMonto()));
            mapaDet.put("montoVentas", new Double(0));
            mapaDet.put("tipoDeCambioCompra", new Double(det.getTipoCambio()));
            mapaDet.put("tipoDeCambioVenta", new Double(0));
            
            if( !isDeSucursal ){
            	mapaDet.put("utilidadPeriodo", new Double(utilCompras));
            	mapaDet.put("utilidadSucursales", new Double(0));
            }else{
            	mapaDet.put("utilidadPeriodo", new Double(0));
            	mapaDet.put("utilidadSucursales", new Double(utilCompras));
            }
            
        }
        else {
            mapaDet.put("utilidadCompras", new Double(0));
            mapaDet.put("utilidadVentas", new Double(utilVentas));
            mapaDet.put("montoCompras", new Double(0));
            mapaDet.put("montoVentas", new Double(det.getMonto()));
            mapaDet.put("tipoDeCambioCompra", new Double(0));
            mapaDet.put("tipoDeCambioVenta", new Double(det.getTipoCambio()));
            
            if( !isDeSucursal ){
            	mapaDet.put("utilidadPeriodo", new Double(utilVentas));
            	mapaDet.put("utilidadSucursales", new Double(0));
            }else{
            	mapaDet.put("utilidadPeriodo", new Double(0));
            	mapaDet.put("utilidadSucursales", new Double(utilVentas));
            }
           
        }
        mapaDet.put("tipoDeal", tipo);
        List reco = getSicaServiceData().findReconocimientoByDivisaAndDate(gcInicio, gcFin,
                idDivisa, idMesaCambio);
        double montoReco = 0;
        for (Iterator it = reco.iterator(); it.hasNext();) {
            RecoUtilidad r = (RecoUtilidad) it.next();
            double tmp = 0;
            if (r.isRecibimos()) {
                tmp = r.getMonto() * -1;
            }
            else {
                tmp = r.getMonto();
            }
            if (!Divisa.PESO.equals(r.getMesaCambio().getDivisaReferencia().getIdDivisa())) {
                tmp = tmp * r.getTipoCambioOtraDivRef();
            }
            montoReco = montoReco + tmp;
        }
        mapaDet.put("utilidadGlobal", new Double(montoReco));
        mapaDet.put("utilidadForwards", new Double(utilidadFwds));
        mapaDet.put("divisaDescripcion", det.getDivisa().getDescripcion());
        mapaDet.put("idMesaCambio", det.getMesaCambio().getNombre() + " Divisa Referencia: " +
                det.getMesaCambio().getDivisaReferencia().getDescripcion());
        mapaDet.put("fechaReporte", det.getDeal().getFechaCaptura());

        return mapaDet;
    }

    /**
     * Obtiene el DataSource para el reporte de Utilidades de Mayoreo Contable.
     *
     * @return JRDataSource.
     * @throws Exception Si algo sale mal.
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
    public JRDataSource getDataSourceReporteUtMayoreoContable() throws Exception {
        
        Calendar gcInicio = new GregorianCalendar();
        Calendar gcFin = new GregorianCalendar();
        gcInicio.setTime(getRegisterDate());
        gcFin.setTime(getRegisterDate());
        gcInicio.set(Calendar.HOUR_OF_DAY, 0);
        gcInicio.set(Calendar.MINUTE, 0);
        gcInicio.set(Calendar.SECOND, 0);
        gcInicio.set(Calendar.MILLISECOND, 0);
        gcFin.set(Calendar.HOUR_OF_DAY, 23);
        gcFin.set(Calendar.MINUTE, 59);
        gcFin.set(Calendar.SECOND, 59);
        gcFin.set(Calendar.MILLISECOND, 9);

        List detalleTmp = getSicaServiceData().findDetallesUtilidadesMayoreo(gcInicio.getTime(),
                gcFin.getTime());
        if (detalleTmp.isEmpty()) {
            throw new Exception("No hay movimientos para la fecha seleccionada.");
        }
        List detalle = new ArrayList();
        for (Iterator it1 = detalleTmp.iterator(); it1.hasNext();) {
            DealDetalle det = (DealDetalle) it1.next();
            if (!perteneceAIxeForwards(det.getDeal()) && (!det.getDeal().isInterbancario())) {
                detalle.add(det);
            }
        }
        if (detalle.isEmpty()) {
            throw new Exception("No hay movimientos para la fecha seleccionada.");
        }
        List elementos = new ArrayList();
        List mesas = getSicaServiceData().findAll(MesaCambio.class);
        List divisa = getSicaServiceData().findAll(Divisa.class);
        List canales = getSicaServiceData().findCanalesOperadosHoy(gcInicio.getTime(),
                gcFin.getTime());
        
        // Ordenamiento de los canales para que todos los de sucursales aparezcan hasta el final del reporte
        java.util.Collections.sort(canales, new Comparator() {
            public int compare(Object o1, Object o2) {
            	Sucursal suc1 = ((Canal) o1).getSucursal();
            	Sucursal suc2 = ((Canal) o2).getSucursal();
            	if(suc1 == null && suc2 == null) {return 0;}
            	if(suc1 != null && suc2 == null) {return 1;}
            	if(suc1 == null && suc2 != null) {return -1;}
                return suc1.getIdSucursal().compareTo(suc2.getIdSucursal());
            }
        });
        
        for (Iterator it2 = mesas.iterator(); it2.hasNext();) {
            MesaCambio mesa = (MesaCambio) it2.next();
            for (Iterator it3 = canales.iterator(); it3.hasNext();) {
                Canal can = (Canal) it3.next();
                //Los canales que tienen asignada una sucursal se agrupan en el canal
                //'Sucursales' y se sumarizan.
                String nombreCanal = can.getNombre();
                if(can.getSucursal() != null){
                	nombreCanal = CANAL_SUCURSALES;
                }
                
                for (Iterator it = divisa.iterator(); it.hasNext();) {
                    Divisa div = (Divisa) it.next();
                    for (Iterator it1 = detalle.iterator(); it1.hasNext();) {
                        DealDetalle det = (DealDetalle) it1.next();
                        if ((mesa.getIdMesaCambio() == det.getMesaCambio().getIdMesaCambio()) &&
                                div.getIdDivisa().equals(det.getDivisa().getIdDivisa()) &&
                                (!"U".equals(det.getTipoDeal())) &&
                                (!Divisa.PESO.trim().equals(det.getDivisa().getIdDivisa())) &&
                                can.getIdCanal().equals(
                                        det.getDeal().getCanalMesa().getCanal().getIdCanal())) {
                        	
                        	
                            elementos.add(crearMapaUtilidades(det, nombreCanal));
                        }
                    }
                }
            }
        }
        if (elementos.size() < 1) {
            HashMap mapaDet = new HashMap();
            mapaDet.put("tipoDeal", " No se encontraron Deals para esta fecha");
            mapaDet.put("divisaDescripcion", " No Registros");
            mapaDet.put("utilidadCompras", new Double(0));
            mapaDet.put("utilidadVentas", new Double(0));
            mapaDet.put("montoCompras", new Double(0));
            mapaDet.put("montoVentas", new Double(0));
            mapaDet.put("tipoDeCambioCompra", new Double(0));
            mapaDet.put("tipoDeCambioVenta", new Double(0));
            mapaDet.put("utilidadPeriodo", new Double(0));
            mapaDet.put("fechaReporte", getRegisterDate());
            elementos.add(mapaDet);
        }
        return new ListDataSource(elementos);
   
    }


    /**
     * Checa la condici&oacute;on de que el deal no pertenezca al Ixe Forwards.
     *
     * @param deal El deal a revisar.
     * @return true si el deal no pertenece a Ixe Forwards.
     */
    private boolean perteneceAIxeForwards(Deal deal) {
        ParametroSica p = (ParametroSica) getSicaServiceData().find(ParametroSica.class,
                ParametroSica.CONTRATO_SICA_IXE_FORWARD);
        String contratoSicaIxeForward = p.getValor();
        if (deal.getContratoSica() != null) {
            if (!deal.getContratoSica().getNoCuenta().equals(contratoSicaIxeForward)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Obtiene el DataSource para el reporte de Utilidades de Mayoreo Interno.
     *
     * @return JRDataSource
     * @throws Exception Si algo sale mal.
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
    public JRDataSource getDataSourceReporteUtMayoreoInterno() throws Exception {
        Calendar gcInicio = new GregorianCalendar();
        Calendar gcFin = new GregorianCalendar();
        gcInicio.setTime(getRegisterDate());
        gcFin.setTime(getRegisterDate());
        gcInicio.set(Calendar.HOUR_OF_DAY, 0);
        gcInicio.set(Calendar.MINUTE, 0);
        gcInicio.set(Calendar.SECOND, 0);
        gcInicio.set(Calendar.MILLISECOND, 0);
        gcFin.set(Calendar.HOUR_OF_DAY, 23);
        gcFin.set(Calendar.MINUTE, 59);
        gcFin.set(Calendar.SECOND, 59);
        gcFin.set(Calendar.MILLISECOND, 9);
        List detalles = getSicaServiceData().findDetallesUtilidadesMayoreo(gcInicio.getTime(),
                gcFin.getTime());
        if (detalles.isEmpty()) {
            throw new Exception("**** No hay movimientos para la fecha seleccionada.");
        }
        List elementos = new ArrayList();
        List mesas = getSicaServiceData().findAll(MesaCambio.class);
        List divisa = getSicaServiceData().findAll(Divisa.class);
        
        for (Iterator it2 = mesas.iterator(); it2.hasNext();) {
            MesaCambio mesa = (MesaCambio) it2.next();
            for (Iterator it = divisa.iterator(); it.hasNext();) {
                Divisa div = (Divisa) it.next();
                boolean divisaCreada = false;
                ContabilidadSicaServiceData cssd = (ContabilidadSicaServiceData)
                        getApplicationContext().getBean("contabilidadServiceData");
                double utilidadFwds = cssd.findUtilidadForward(mesa.getIdMesaCambio(),
                        div.getIdDivisa(), gcInicio.getTime());
                for (Iterator it1 = detalles.iterator(); it1.hasNext();) {
                    DealDetalle det = (DealDetalle) it1.next();
                    if ((mesa.getIdMesaCambio() == det.getMesaCambio().getIdMesaCambio()) &&
                            div.getIdDivisa().equals(det.getDivisa().getIdDivisa()) &&
                            (!"U".equals(det.getTipoDeal())) &&
                            (!Divisa.PESO.trim().equals(det.getDivisa().getIdDivisa()))) {
                        elementos.add(crearMapaUtilidadesGlobales(det, "Todos los Tipos",
                        		gcInicio.getTime(), gcFin.getTime(), div.getIdDivisa(),
                                mesa.getIdMesaCambio(), utilidadFwds));
                        divisaCreada = true;
                    }
                }
                if (!divisaCreada) {
                    revisarRecoUtilidadSinOperaciones(gcInicio.getTime(), gcFin.getTime(), 
                    		elementos, mesa, div, utilidadFwds);
                }
            }
        }
        if (elementos.size() < 1) {
            HashMap mapaDet = new HashMap();
            mapaDet.put("tipoDeal", " No se encontraron Deals para esta fecha");
            mapaDet.put("divisaDescripcion", " No Registros");
            mapaDet.put("utilidadCompras", new Double(0));
            mapaDet.put("utilidadVentas", new Double(0));
            mapaDet.put("montoCompras", new Double(0));
            mapaDet.put("montoVentas", new Double(0));
            mapaDet.put("tipoDeCambioCompra", new Double(0));
            mapaDet.put("tipoDeCambioVenta", new Double(0));
            mapaDet.put("utilidadPeriodo", new Double(0));
            mapaDet.put("fechaReporte", getRegisterDate());
            elementos.add(mapaDet);
        }
        return new ListDataSource(elementos);
    }

    /**
     * Genera el mapa con RecoUtilidad en caso de no existir detalles de deal para esa fecha.
     *
     * @param fecInicio La fecha de inicio.
     * @param fecFin La fecha fin.
     * @param elementos Los resultados.
     * @param mesa La mesa de trabajo.
     * @param div La divisa de trabajo.
     * @param utilidadFwds El monto de la utilidad en forwards.
     */
    private void revisarRecoUtilidadSinOperaciones(Date fecInicio, Date fecFin,
                                                   List elementos, MesaCambio mesa, Divisa div,
                                                   double utilidadFwds) {
        List recos = getSicaServiceData().findReconocimientoByDivisaAndDate(
        		fecInicio, fecFin, div.getIdDivisa(),
                mesa.getIdMesaCambio());
        for (Iterator it3 = recos.iterator(); it3.hasNext();) {
            RecoUtilidad r = (RecoUtilidad) it3.next();
            double tmp = 0;
            if (r.isRecibimos()) {
                tmp = r.getMonto() * -1;
            }
            else {
                tmp = r.getMonto();
            }
            if (!Divisa.PESO.equals(r.getMesaCambio().getDivisaReferencia().getIdDivisa())) {
                tmp = tmp * r.getTipoCambioOtraDivRef();
            }
            HashMap mapa = new HashMap();
            mapa.put("utilidadCompras", new Double(0));
            mapa.put("utilidadVentas", new Double(0));
            mapa.put("montoCompras", new Double(0));
            mapa.put("montoVentas", new Double(0));
            mapa.put("tipoDeCambioCompra", new Double(0));
            mapa.put("tipoDeCambioVenta", new Double(0));
            mapa.put("utilidadPeriodo", new Double(0));
            mapa.put("utilidadGlobal", new Double(tmp));
            mapa.put("utilidadForwards", new Double(utilidadFwds));
            mapa.put("divisaDescripcion", r.getDivisa().getDescripcion());
            mapa.put("idMesaCambio", mesa.getNombre() + " Divisa Referencia: " +
                    mesa.getDivisaReferencia().getDescripcion());
            mapa.put("fechaReporte", r.getFechaOperacion());
            elementos.add(mapa);
        }
    }

    /**
     * Obtiene el template para generar el reporte de utilidades de Ixe Forwards.
     *
     * @param cycle El ciclo de la p&acute;gina.
     */
    public void obtenerReporteIxeForwards(IRequestCycle cycle) {
        Object parameters[] = new Object[]{"WEB-INF/jasper/RepUtIxeForwards.jasper",
                REPORTE_UT_IXE_FORWARDS_PDF, "0"};
        cycle.setServiceParameters(parameters);
        setReporteSeleccionado(REPORTE_UT_IXE_FORWARDS_PDF);
        imprimirReporte(cycle);
    }

    /**
     * Obtiene el DataSource para el Reporte de Utilidades de Ixe Forwards
     *
     * @return JRDataSource
     * @throws Exception Si algo sale mal.
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
    public JRDataSource getDataSourceReporteUtIxeForwards() throws Exception {
        try {
            List elementos = new ArrayList();
            for (Iterator it = getListaUtilidades().iterator(); it.hasNext();) {
                UtilidadForward u = (UtilidadForward) it.next();
                HashMap map = new HashMap();
                map.put("idDeal", new Integer(u.getIdDeal()));
                map.put("tipoOperacion", u.getRecibimosDesc());
                map.put("fechaValor", u.getFechaValor());
                map.put("idDivisa", u.getIdDivisa());
                map.put("monto", new Double(u.getMonto().doubleValue()));
                map.put("montoEquiv", new Double(u.getMontoEquivalente().doubleValue()));
                map.put("tipoCambioOperado", new Double(u.getTipoCambioCliente().doubleValue()));
                map.put("utilidad", new Double(u.getUtilidad().doubleValue()));
                map.put("iniciaSwap", u.getInicioSwapDesc());
                map.put("idMesaCambio", u.getMesaCambio().getNombre());
                map.put("fechaReporte", getRegisterDate());
                elementos.add(map);
            }
            return new ListDataSource(elementos);
        }
        catch (Exception e) {
            return null;
        }
    }


    /**
     * Obtiene el DataSource para el reporte de Utilidades de Mayoreo.
     *
     * @return JRDataSource
     * @throws Exception Si algo sale mal.
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
    public JRDataSource getDataSourceReporteUtMayoreo() throws Exception {
        try {
            Calendar gcInicio = new GregorianCalendar();
            Calendar gcFin = new GregorianCalendar();
            gcInicio.setTime(getRegisterDate());
            gcFin.setTime(getRegisterDate());
            gcInicio.set(Calendar.HOUR_OF_DAY, 0);
            gcInicio.set(Calendar.MINUTE, 0);
            gcInicio.set(Calendar.SECOND, 0);
            gcInicio.set(Calendar.MILLISECOND, 0);
            gcFin.set(Calendar.HOUR_OF_DAY, 23);
            gcFin.set(Calendar.MINUTE, 59);
            gcFin.set(Calendar.SECOND, 59);
            gcFin.set(Calendar.MILLISECOND, 9);
            List detalle = getSicaServiceData().findDetallesUtilidadesMayoreo(gcInicio.getTime(),
                    gcFin.getTime());
            if (detalle.isEmpty()) {
                throw new Exception("No hay movimientos para la fecha seleccionada.");
            }
            List elementos = new ArrayList();
            List divisa = getSicaServiceData().findAll(Divisa.class);
            for (Iterator it = divisa.iterator(); it.hasNext();) {
                Divisa div = (Divisa) it.next();
                for (Iterator it1 = detalle.iterator(); it1.hasNext();) {
                    DealDetalle det = (DealDetalle) it1.next();
                    if (div.getIdDivisa().equals(det.getDivisa().getIdDivisa()) &&
                            (!"U".equals(det.getTipoDeal())) &&
                            (!Divisa.PESO.trim().equals(det.getDivisa().getIdDivisa()))) {
                        elementos.add(crearMapaUtilidades(det, "Todos los tipos"));
                    }
                }
            }
            return new ListDataSource(elementos);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtiene el DataSource del reporte Polizas Contables.
     *
     * @param id String El identificador del reporte.
     * @return JRData Source
     * @throws Exception Si algo sale mal.
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
    public JRDataSource getDataSourceReportePolizasContables(String id) throws Exception {
        Calendar gcInicio = new GregorianCalendar();
        Calendar gcFin = new GregorianCalendar();
        gcInicio.setTime(getRegisterDate());
        gcFin.setTime(getRegisterDate());
        gcInicio.set(Calendar.HOUR_OF_DAY, 0);
        gcInicio.set(Calendar.MINUTE, 0);
        gcInicio.set(Calendar.SECOND, 0);
        gcInicio.set(Calendar.MILLISECOND, 0);
        gcFin.add(Calendar.DAY_OF_MONTH, 1);
        gcFin.add(Calendar.HOUR_OF_DAY, -gcFin.get(Calendar.HOUR_OF_DAY));
        gcFin.add(Calendar.MINUTE, -gcFin.get(Calendar.MINUTE));
        gcFin.add(Calendar.SECOND, -gcFin.get(Calendar.SECOND));
        gcFin.add(Calendar.MILLISECOND, -gcFin.get(Calendar.MILLISECOND));
        List polizas = getSicaServiceData().findAllDealsReportePolizasContables(gcInicio.getTime(),
                gcFin.getTime());
        if (polizas.isEmpty()) {
            throw new Exception("No hay movimientos para la fecha seleccionada.");
        }
        List listaPolizasDetalles = new ArrayList();
        for (Iterator it = polizas.iterator(); it.hasNext();) {
            Object[] elements = (Object[]) it.next();
            HashMap map = new HashMap();
            Divisa divisa = getSicaServiceData().findDivisa((String) elements[0]);
            map.put("divisa", divisa.getDescripcion());
            map.put("fechaValor", gcInicio.getTime());
            String cuenta = (String) elements[1];
            map.put("cuentaContable", cuenta);
            String entidad = (String) elements[2];
            map.put("entidad", entidad);
            String cargoAbono = (String) elements[3];
            if (CARGO_ASIENTO.equals(cargoAbono) || CARGO_LIQ.equals(cargoAbono)) {
                map.put("cargos", elements[4]);
                map.put("abonos", new Double(0.00));
            }
            if (ABONO_ASIENTO.equals(cargoAbono) || ABONO_LIQ.equals(cargoAbono)) {
                map.put("abonos", elements[4]);
                map.put("cargos", new Double(0.00));
            }
            String referencia = (String) elements[5];
            map.put("referencia", referencia);
            listaPolizasDetalles.add(map);
        }
        return new ListDataSource(listaPolizasDetalles);
    }


    /**
     * Obtiene el DataSource del reporte de Diarios. El reporte queda pendiente, aun no se tiene
     * una especificaci&oacute;n detallada del contenido.
     *
     * @param id El identificador del reporte.
     * @return JRDataSource
     * @throws Exception Si algo sale mal.
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
    public JRDataSource getDataSourceReporteConciliacionDiaria(String id) throws Exception {
        Calendar gc = new GregorianCalendar();
        gc.setTime(getRegisterDate());
        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);
        List posicionMap = new ArrayList();
        List posicionMesas = getSicaServiceData().findPosicionAllMesas();
        if (posicionMesas.isEmpty()) {
            throw new Exception("No hay movimientos para la fecha seleccionada.");
        }
        for (Iterator it = posicionMesas.iterator(); it.hasNext();) {
            //1a. Parte del reporte
            HashMap map = new HashMap();
            double comprasCash;
            double ventasCash;
            double posicionCash;
            double comprasTom;
            double ventasTom;
            double posicionTom;
            double comprasSpot;
            double ventasSpot;
            double posicionSpot;
            double compras72Hr;
            double ventas72Hr;
            double posicion72Hr;
            double comprasVFut;
            double ventasVFut;
            double posicionVFut;
            map.put("fechaOperacion", gc.getTime());
            Posicion posicionLista = (Posicion) it.next();
            MesaCambio mesa = getSicaServiceData().findMesaCambio(posicionLista.getMesaCambio().
                    getIdMesaCambio());
            map.put("mesa", mesa.getNombre());
            map.put("divisaReferencia", mesa.getDivisaReferencia().getDescripcion());
            map.put("divisa", posicionLista.getDivisa().getDescripcion());
            //Toma los valores de compras y ventas en cada elemento de la posicion
            comprasCash = posicionLista.getCpaVtaMn().getCompraMnClienteCash();
            ventasCash = posicionLista.getCpaVtaMn().getVentaMnClienteCash();
            comprasTom = posicionLista.getCpaVtaMn().getCompraMnClienteTom();
            ventasTom = posicionLista.getCpaVtaMn().getVentaMnClienteTom();
            comprasSpot = posicionLista.getCpaVtaMn().getCompraMnClienteSpot();
            ventasSpot = posicionLista.getCpaVtaMn().getVentaMnClienteSpot();
            compras72Hr = posicionLista.getCpaVtaMn().getCompraMnCliente72Hr().doubleValue();
            ventas72Hr = posicionLista.getCpaVtaMn().getVentaMnCliente72Hr().doubleValue();
            comprasVFut = posicionLista.getCpaVtaMn().getCompraMnClienteVFut().doubleValue();
            ventasVFut = posicionLista.getCpaVtaMn().getVentaMnClienteVFut().doubleValue();
            posicionCash = comprasCash - ventasCash;
            posicionTom = comprasTom - ventasTom;
            posicionSpot = comprasSpot - ventasSpot;
            posicion72Hr = compras72Hr - ventas72Hr;
            posicionVFut = comprasVFut - ventasVFut;
            if (posicionCash > 0) {
                map.put("operacionesCash", "HOY");
                map.put("comprasCash", new Double(comprasCash));
                map.put("ventasCash", new Double(ventasCash));
                map.put("posicionCash", new Double(posicionCash));
            }
            else {
                map.put("operacionesCash", null);
                map.put("comprasCash", new Double(0.00));
                map.put("ventasCash", new Double(0.00));
                map.put("posicionCash", new Double(0.00));
            }
            if (posicionTom > 0) {
                map.put("operacionesTom", "TOM");
                map.put("comprasTom", new Double(comprasTom));
                map.put("ventasTom", new Double(ventasTom));
                map.put("posicionTom", new Double(posicionTom));
            }
            else {
                map.put("operacionesTom", null);
                map.put("comprasTom", new Double(0.00));
                map.put("ventasTom", new Double(0.00));
                map.put("posicionTom", new Double(0.00));
            }
            if (posicionSpot > 0) {
                map.put("operacionesSpot", "SPOT");
                map.put("comprasSpot", new Double(comprasSpot));
                map.put("ventasSpot", new Double(ventasSpot));
                map.put("posicionSpot", new Double(posicionSpot));
            }
            else {
                map.put("operacionesSpot", null);
                map.put("comprasSpot", new Double(0.00));
                map.put("ventasSpot", new Double(0.00));
                map.put("posicionSpot", new Double(0.00));
            }
            if (posicion72Hr > 0) {
                map.put("operaciones72Hr", "72HR");
                map.put("compras72Hr", new Double(compras72Hr));
                map.put("ventas72Hr", new Double(ventas72Hr));
                map.put("posicion72Hr", new Double(posicion72Hr));
            }
            else {
                map.put("operaciones72Hr", null);
                map.put("compras72Hr", new Double(0.00));
                map.put("ventas72Hr", new Double(0.00));
                map.put("posicion72Hr", new Double(0.00));
            }
            if (posicionVFut > 0) {
                map.put("operacionesVFut", "VFUT");
                map.put("comprasVFut", new Double(comprasVFut));
                map.put("ventasVFut", new Double(ventasVFut));
                map.put("posicionVFut", new Double(posicionVFut));
            }
            else {
                map.put("operacionesVFut", null);
                map.put("comprasVFut", new Double(0.00));
                map.put("ventasVFut", new Double(0.00));
                map.put("posicionVFut", new Double(0.00));
            }
            //2da. Parte del Reporte
            map.put("fechaPosicionFinalAnterior", gc.getTime());
            map.put("montoPosicionFinalAnterior",
                    new Double(posicionLista.getPosIni().getPosicionInicialMn()));
            map.put("montoPosicionDiaSiguiente",
                    new Double(posicionLista.getPosIni().getPosicionInicialMn()));
            posicionMap.add(map);
        }
        return new ListDataSource(posicionMap);
    }

    /**
     * Obtiene el DataSource del reporte de Diarios. El reporte queda pendiente, aun no se tiene
     * una especificaci&oacute;n detallada del contenido.
     *
     * @param id El identificador del reporte.
     * @return JRDataSource
     * @throws Exception Si algo sale mal.
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
    public JRDataSource getDataSourceReporteDealsPendientes(String id) throws Exception {
        Calendar gc = new GregorianCalendar();
        gc.setTime(getRegisterDate());
        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);
        List dealsPendientes = getSicaServiceData().findDealsPendientesPorLiquidar(
                DateUtils.inicioDia(getRegisterDate()), DateUtils.finDia(getRegisterDate()));
        List listaMapas = new ArrayList();
        List listaMapasPesos = new ArrayList();
        if (dealsPendientes.isEmpty()) {
            throw new Exception("No hay deals pendientes por liquidar para la fecha seleccionada.");
        }
        /*
    	 * En estas lineas de codigo se realiza un ordenamiento sobre la lista que se obtiene
    	 * de getSicaServiceData.findAllDealDetallesBySectorAndDate(...).
    	 *
    	 * El ordenamiento toma los siguientes criterios:
    	 * 1. Compras o ventas.
    	 * 2. Divisa.
    	 * 3. Nombre del Cliente.
    	 * */
        Collections.sort(dealsPendientes, new Comparator() {
            public int compare(Object o1, Object o2) {
                DealDetalle dd1 = (DealDetalle) o1;
                DealDetalle dd2 = (DealDetalle) o2;
                Boolean rdd1 = Boolean.valueOf(dd1.isRecibimos());
                Boolean rdd2 = Boolean.valueOf(dd2.isRecibimos());
                int comp = rdd2.toString().compareTo(rdd1.toString());
                if (comp != 0) {
                    return comp;
                }
                else {
                    comp = dd1.getDivisa().getIdDivisa().compareTo(dd2.getDivisa().getIdDivisa());
                    if (comp != 0) {
                        return comp;
                    }
                    else {
                        return dd1.getDeal().getCliente().getNombreCompleto().
                                compareTo(dd2.getDeal().getCliente().getNombreCompleto());
                    }
                }
            }
        });
        for (Iterator it = dealsPendientes.iterator(); it.hasNext();) {
            DealDetalle element = (DealDetalle) it.next();
            HashMap dealsMap = new HashMap();
            dealsMap.put("fechaSeleccionada", getRegisterDate());
            dealsMap.put("isRecibimos", Boolean.valueOf(element.isRecibimos()));
            Integer idDeal = new Integer(element.getDeal().getIdDeal());
            Integer idDetalle = new Integer(element.getFolioDetalle());
            dealsMap.put("folioInterno", new StringBuffer().append(idDeal.toString()).
                    append("-").append(idDetalle.toString()).toString());
            dealsMap.put("divisa", element.getDivisa().getDescripcion());
            dealsMap.put("cliente", element.getDeal().getCliente().getNombreCompleto());
            dealsMap.put("fechaPacto", element.getDeal().getFechaCaptura());
            dealsMap.put("fechaLiquidacion", element.getDeal().getFechaLiquidacion());
            dealsMap.put("monto", new Double(element.getMonto()));
            dealsMap.put("tipoCambio", new Double(element.getImporte()));
            dealsMap.put("tipoCambioCliente", new Double(element.getTipoCambio()));
            if (!Divisa.PESO.equals(element.getDivisa().getIdDivisa())) {
                dealsMap.put("isPesos", Boolean.FALSE);
                listaMapas.add(dealsMap);
            }
            else {
                dealsMap.put("isPesos", Boolean.TRUE);
                listaMapasPesos.add(dealsMap);
            }

        }
        if (listaMapas.isEmpty()) {
            throw new Exception("No hay deals pendientes por liquidar para la fecha seleccionada.");
        }
        for (Iterator it = listaMapasPesos.iterator(); it.hasNext();) {
            HashMap map = (HashMap) it.next();
            listaMapas.add(map);
        }
        return new ListDataSource(listaMapas);
    }

    /**
     * Regresa la suma de la utilidad de la lista de objetos UtilidadForward que es pasada como
     * par&aacute;metro.
     *
     * @param utilidadesForwards La lista de objetos utilidadesForwards.
     * @return BigDecimal.
     */
    public BigDecimal totalUtilidadForwards(List utilidadesForwards) {
        BigDecimal total = new BigDecimal("" + 0);
        for (Iterator it = utilidadesForwards.iterator(); it.hasNext();) {
            UtilidadForward utilidadForward = (UtilidadForward) it.next();
            total = total.add(utilidadForward.getUtilidad());
        }
        return total;
    }

    /**
     * Regresa una lista de objetos UtilidadForward que se encuentra en la estructura
     * utilidadesForwards.
     *
     * @return List de UtilidadForward.
     */
    public List getListaUtilidades() {
        List resultados = new ArrayList();
        for (Iterator it = getUtilidadesForwards().keySet().iterator(); it.hasNext();) {
            MesaCambio mesaCambio = (MesaCambio) it.next();
            resultados.addAll((List) getUtilidadesForwards().get(mesaCambio));
        }
        return resultados;
    }

    /**
     * Genera el Reporte de Deals Pendientes por Liquidar en Excel.
     * @param cycle
     */
	private void generaReporteDealPendientesLiquidarXls(IRequestCycle cycle) {
        HttpServletResponse response = cycle.getRequestContext().getResponse();
        ModeloXls modelo;
        try {
            modelo = new ModeloXls("Reporte de Deals Pendientes por Liquidar",
                    new String[]{"N\u00edmero de deal", "Nombre del Cliente",
                            "Fecha de Liquidaci\u00f3n", "Divisa", "Tipo Operaci\u00f3n",
                            "Monto en Divisa", "Monto en Divisa de Referencia",
                            "Tipo de Cambio dado al cliente"},
                    new String[]{"idDeal", "nombreCompleto", "fechaLiquidacion", "idDivisa",
                            "tipoOperacion", "monto", "importe", "tipoCambio"},
                    getDealsPendientesLiquidarPorFechaT4(getRegisterDate()));
        }
        catch (Exception e) {
            warn(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
            return;
        }
        XlsServiceImpl.generarEscribir(response, REPORTE_DEALS_PENDIENTES_LIQUIDAR_EXCEL, modelo);
    }

    /**
     * Genera el Reporte de Utilidades de Promoci&oacute;n.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    private void generaReporteResumenUtilidadesPromocionXls(IRequestCycle cycle) {
        List lista;
        ModeloXls modelo;
        HttpServletResponse response = cycle.getRequestContext().getResponse();
        try {
            lista = obtieneResumenUtilidadesPromocionEspecial(
                    getResumenUtilidadesPromocion(getRegisterDate()));
            modelo = new ModeloXls("Reporte Resumen de Utilidades Promoci\u00f3n ",
                    new String[]{"Canal", "Utilidades Compras", "Utilidades Ventas",
                            "Utilidad Periodo", "Monto compras", "Monto Ventas"},
                    new String[]{"canal", "utlCpa", "utlVta", "utlPeriodo", "montoCpa", "montoVta"},
                    lista);
        }
        catch (Exception e) {
            e.printStackTrace();
            warn(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
            return;
        }
        XlsServiceImpl.generarEscribir(response, REPORTE_RESUMEN_UT_PROMOCION_EXCEL, modelo);
    }

    /**
     * Obtiene los datos para el reporte de utilidades de promoci&oacute;n.
     * @param lista
     * @return List.
     */
    private List obtieneResumenUtilidadesPromocionEspecial(List lista) {
        Map resumenUtilidades = new HashMap();
        Map mapaDet;
        try {
            for (Iterator iter = lista.iterator(); iter.hasNext();) {
                Map mapa = (Map) iter.next();
                if (resumenUtilidades.containsKey(mapa.get("tipoDeal"))) {
                    Map temp = (Map) resumenUtilidades
                            .get(mapa.get("tipoDeal"));
                    Double utilidadesCompraTemp = (Double) temp.get("utlCpa");
                    Double utilidadesVentaTemp = (Double) temp.get("utlVta");
                    Double utilidadesPeriodoTemp = (Double) temp
                            .get("utlPeriodo");
                    Double montoComprasTemp = (Double) temp.get("montoCpa");
                    Double montoVentasTemp = (Double) temp.get("montoVta");
                    utilidadesCompraTemp = new Double(utilidadesCompraTemp
                            .doubleValue() + ((Double) mapa.get("utilidadCompras"))
                            .doubleValue());
                    utilidadesVentaTemp = new Double(utilidadesVentaTemp
							.doubleValue() + ((Double) mapa.get("utilidadVentas"))
									.doubleValue());
					utilidadesPeriodoTemp = new Double(utilidadesPeriodoTemp
							.doubleValue() + ((Double) mapa.get("utilidadPeriodo"))
									.doubleValue());
					montoComprasTemp = new Double(montoComprasTemp
							.doubleValue() + ((Double) mapa.get("montoCompras")).doubleValue());
					montoVentasTemp = new Double(montoVentasTemp.doubleValue()
							+ ((Double) mapa.get("montoVentas")).doubleValue());
					mapaDet = new HashMap();
					mapaDet.put("mesa", mapa.get("idMesaCambio"));
					mapaDet.put("canal", mapa.get("tipoDeal"));
					mapaDet.put("utlCpa", utilidadesCompraTemp);
					mapaDet.put("utlVta", utilidadesVentaTemp);
                    mapaDet.put("utlPeriodo", utilidadesPeriodoTemp);
                    mapaDet.put("montoCpa", montoComprasTemp);
                    mapaDet.put("montoVta", montoVentasTemp);
                    resumenUtilidades.remove(mapa.get("tipoDeal"));
                }
                else {
                    mapaDet = new HashMap();
                    mapaDet.put("mesa", mapa.get("idMesaCambio"));
                    mapaDet.put("canal", mapa.get("tipoDeal"));
                    mapaDet.put("utlCpa", mapa.get("utilidadCompras"));
                    mapaDet.put("utlVta", mapa.get("utilidadVentas"));
                    mapaDet.put("utlPeriodo", mapa.get("utilidadPeriodo"));
                    mapaDet.put("montoCpa", mapa.get("montoCompras"));
                    mapaDet.put("montoVta", mapa.get("montoVentas"));
                }
                mapaDet.put("fechaReporte", getRegisterDate());
                resumenUtilidades.put(mapa.get("tipoDeal"), mapaDet);
            }
            return new ArrayList(resumenUtilidades.values());
        }
        catch (Exception e) {
            e.printStackTrace();
            warn(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
            return null;
        }
    }

    /**
     * Obtiene los registros para elaborar el reporte de utilidades de promoci&oacute;n.
     *
     * @param registerDate
     * @return List.
     * @throws Exception
     */
    private List getResumenUtilidadesPromocion(Date registerDate) throws Exception {
    	List detalleTmp = getSicaServiceData().findDetallesUtilidadesMayoreo(
                DateUtils.inicioDia(registerDate), DateUtils.finDia(registerDate));
    	if (detalleTmp.isEmpty()){
            throw new Exception("No hay movimientos para la fecha seleccionada.");
        }
        List detalle = new ArrayList();
        for (Iterator it1 = detalleTmp.iterator(); it1.hasNext();) {
            DealDetalle det = (DealDetalle) it1.next();
            if (!perteneceAIxeForwards(det.getDeal()) && (!det.getDeal().isInterbancario())) {
                detalle.add(det);
            }
        }
        if (detalle.isEmpty()) {
            throw new Exception("No hay movimientos para la fecha seleccionada.");
        }
        return ordenaDetallesUtilidadesPromocion(detalle, DateUtils.inicioDia(getRegisterDate()),
                DateUtils.finDia(getRegisterDate()));
    }

    /**
     * Obtiene la lista de deal pendientes por liquidar dada una fecha T+4
     *
     * @param registerDate
     * @return List.
     * @throws Exception
     */
    private List getDealsPendientesLiquidarPorFechaT4(Date registerDate) throws Exception {
        Map mapa;
        List returnList = new ArrayList();
        List lista = getSicaServiceData().findAllDealsPendientesLiquidarT4(registerDate);
        if (!lista.isEmpty()) {
            for (Iterator it = lista.iterator(); it.hasNext();) {
                DealDetalle detalle = (DealDetalle) it.next();
                mapa = new HashMap();
                mapa.put("idDeal", new Integer(detalle.getDeal().getIdDeal()));
                mapa.put("nombreCompleto", detalle.getDeal().getCliente().getNombreCompleto());
                mapa.put("fechaLiquidacion", detalle.getDeal().getFechaLiquidacion());
                mapa.put("idDivisa", detalle.getDivisa().getIdDivisa());
                mapa.put("tipoOperacion", detalle.isRecibimos() ? "COMPRA" : "VENTA");
                mapa.put("monto", new Double(detalle.getMonto()));
                mapa.put("importe", new Double(detalle.getImporte()));
                mapa.put("tipoCambio", new Double(detalle.getTipoCambio()));
                mapa.put("fechaReporte", getRegisterDate());
                returnList.add(mapa);
            }
        }
        else {
            throw new Exception("No hay movimientos para la fecha seleccionada.");
        }
        return returnList;
    }

    /**
     * Ordena la Lista de Dealles de Utiliadades de promoci&oacute;n.
     *
     * @param detalle
     * @param fecInicio
     * @param fecFin
     * @return
     */
    private List ordenaDetallesUtilidadesPromocion(List detalle, Date fecInicio, Date fecFin) {
        List elementos = new ArrayList();
        List mesas = getSicaServiceData().findAll(MesaCambio.class);
        List divisa = getSicaServiceData().findAll(Divisa.class);
        List canales = getSicaServiceData().findCanalesOperadosHoy(fecInicio,
                fecFin);
        for (Iterator it2 = mesas.iterator(); it2.hasNext();) {
            MesaCambio mesa = (MesaCambio) it2.next();
            for (Iterator it3 = canales.iterator(); it3.hasNext();) {
                Canal can = (Canal) it3.next();
                for (Iterator it = divisa.iterator(); it.hasNext();) {
                    Divisa div = (Divisa) it.next();
                    for (Iterator it1 = detalle.iterator(); it1.hasNext();) {
                        DealDetalle det = (DealDetalle) it1.next();
                        if ((mesa.getIdMesaCambio() == det.getMesaCambio().getIdMesaCambio()) &&
                                div.getIdDivisa().equals(det.getDivisa().getIdDivisa()) &&
                                (!"U".equals(det.getTipoDeal())) &&
                                (!Divisa.PESO.trim().equals(det.getDivisa().getIdDivisa())) &&
                                can.getIdCanal().equals(
                                        det.getDeal().getCanalMesa().getCanal().getIdCanal())) {
                            elementos.add(crearMapaUtilidades(det, can.getNombre()));
                        }
                    }
                }
            }
        }
        if (elementos.size() < 1) {
            HashMap mapaDet = new HashMap();
            mapaDet.put("tipoDeal", " No se encontraron Deals para esta fecha");
            mapaDet.put("divisaDescripcion", " No Registros");
            mapaDet.put("utilidadCompras", new Double(0));
            mapaDet.put("utilidadVentas", new Double(0));
            mapaDet.put("montoCompras", new Double(0));
            mapaDet.put("montoVentas", new Double(0));
            mapaDet.put("tipoDeCambioCompra", new Double(0));
            mapaDet.put("tipoDeCambioVenta", new Double(0));
            mapaDet.put("utilidadPeriodo", new Double(0));
            mapaDet.put("fechaReporte", getRegisterDate());
            elementos.add(mapaDet);
        }
        return elementos;
    }

    /**
     * Obtiene el DataSource del reporte de Deal Pendientes por Liquidar T+4.
     *
     * @return
     * @throws Exception
     */
    public JRDataSource getDataSourceReporteDealsPendientesLiquidarT4() throws Exception {
        return new ListDataSource(getDealsPendientesLiquidarPorFechaT4(getRegisterDate()));
    }

    /**
     * Obtiene el DataSource del reporte de Utilidades de Promoci&oacute;n.
     *
     * @return
     * @throws Exception
     */
    public JRDataSource getDataSourceReporteUtPromo() throws Exception {
        List datos = obtieneResumenUtilidadesPromocionEspecial(
                getResumenUtilidadesPromocion(getRegisterDate()));
        return new ListDataSource(datos);
    }

    /**
     * Obtiene el valor de mapaDivisas
     *
     * @return HashMap
     */
    public abstract HashMap getMapaDivisas();

    /**
     * Establece el valor de mapaDivisas.
     *
     * @param mapaDivisas El valor a asignar.
     */
    public abstract void setMapaDivisas(HashMap mapaDivisas);

    /**
     * Regresa el valor de utilidadesForwards.
     *
     * @return Map.
     */
    public abstract Map getUtilidadesForwards();

    /**
     * Establece el valor de utilidadesForwards.
     *
     * @param utilidadesForwards El valor a asignar.
     */
    public abstract void setUtilidadesForwards(Map utilidadesForwards);

    /**
     * Obtiene el valor de registerDate
     *
     * @return Date
     */
    public abstract Date getRegisterDate();

    /**
     * Establece el valor para registerDate
     *
     * @param registerDate Valor de la fecha seleccionada
     */
    public abstract void setRegisterDate(Date registerDate);

    /**
     * Obtiene el valor de reporteSeleccionado
     *
     * @return String
     */
    public abstract String getReporteSeleccionado();

    /**
     * Establece el valor para reporteSeleccionado
     *
     * @param reporteSeleccionado Reporte selecciondado
     */
    public abstract void setReporteSeleccionado(String reporteSeleccionado);

    /**
     * Variable estatica para del reporte de P&oacute;lizas Contables
     */
    private static final String REPORTE_POLIZAS_CONTABLES = "P\u00f3lizas Contables Resumen";

    /**
     * Variable estatica para del reporte de P&oacute;lizas Contables
     */
    private static final String REPORTE_POLIZAS_CONTABLES_XLS = "P\u00f3lizas Contables Resumen " +
            "Excel";

    /**
     * Variable estatica para del reporte de Coniciliaci&oacute;n Diaria
     */
    private static final String REPORTE_CONCILIACION_DIARIA = "Conciliaci\u00f3n Diaria";

    /**
     * Variable estatica para del reporte de Utilidades de Promoci&oacute;n.
     */
    private static final String REPORTE_UT_PROMOCION = "Utilidades de Promoci\u00f3n";

    /**
     * Variable estatica para del reporte de Utilidades Mayoreo Interno
     */
    private static final String REPORTE_UT_GLOBALES = "Utilidades Globales";

    /**
     * Variable estatica para del reporte de Utilidades de IXE Forwards
     */
    private static final String REPORTE_UT_IXE_FORWARDS = "Utilidades IXE Forwards";

    /**
     * Variable estatica para del reporte de Utilidades de Promoci&oacute;n en Excel
     */
    private static final String REPORTE_UT_PROMOCION_EXCEL = "Utilidades de Promoci\u00f3n Excel";

    /**
     * Variable estatica para del reporte de Utilidades Mayoreo
     */
    private static final String REPORTE_UT_GLOBAL_EXCEL = "Utilidades Globales Excel";

    /**
     * Variable est&aacute;tica para del reporte de Utilidades Mayoreo
     */
    private static final String REPORTE_UT_IXE_FORWARDS_PDF = "Reporte Utilidades Ixe Forwards pdf";

    /**
     * Variable est&aacute;tica para el reporte de Resumen de Utilidades en Excel.
     */
    private static final String REPORTE_RESUMEN_UT_PROMOCION_EXCEL = "Resumen de Utilidades de Promoci\u00f3n Excel";

    /**
     * Variable est&aacute;tica para el reporte de Deal Pendientes por Liquidar en Excel.
     */
    private static final String REPORTE_DEALS_PENDIENTES_LIQUIDAR_EXCEL = "Reporte de Deals Pendientes por Liquidar Excel";

    /**
     * Variable est&aacute;tica para el reporte de Resumen de Utilidades.
     */
    private static final String REPORTE_RESUMEN_UT_PROMOCION = "Resumen de Utilidades de Promoci\u00f3n";

    /**
     * Variable est&aacute;tica para el reporte de Deal Pendientes por Liquidar.
     */
    private static final String REPORTE_DEALS_PENDIENTES_LIQUIDAR = "Reporte de Deals Pendientes por Liquidar";

    /**
     * Variable estatica que define cuando es un cargo que corresponde
     * a la fase de asiento
     */
    private static final String CARGO_ASIENTO = "Cargo";

    /**
     * Variable estatica que define cuando es un abono que corresponde
     * a la fase de asiento
     */
    private static final String ABONO_ASIENTO = "Abono";

    /**
     * Variable estatica que define cuando es un cargo que corresponde
     * a la fase de liquidacion
     */
    private static final String CARGO_LIQ = "CARGO";

    /**
     * Variable estatica que define cuando es un abono que corresponde
     * a la fase de liquidacion
     */
    private static final String ABONO_LIQ = "ABONO";
    
    private static final String CANAL_SUCURSALES = "SUCURSALES";
}

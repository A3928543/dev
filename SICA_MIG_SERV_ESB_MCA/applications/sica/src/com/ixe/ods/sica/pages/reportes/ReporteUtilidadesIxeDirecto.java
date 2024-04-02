/*
 * $Id: ReporteUtilidadesIxeDirecto.java,v 1.6 2008/08/11 20:08:00 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

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
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.LlavesMapas;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.dao.ReporteUtilidadesDao;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.tapestry.BaseGlobal;

/**
 * P&aacute;gina que permite al usuario imprimir los reportes de Utilidades para el canal de 
 * operaci&oacute;n Ixe Directo y Sucursales. 
 * 
 * Las utilidades/pérdidas generadas de las operaciones de cambios que se realicen por medio
 * del  canal Ixe Directo,  seran asignadas bajo las siguientes condiciones:
 * <li>Si el Tipo de Liquidación para compra o venta es por medio de una cuenta de cheques 
 * - ya sea de Pesos Mexicanos o Dólares Americanos (Mexdolar) - la utilidad/pérdida, le es 
 * asignada a la sucursal origen de la cuenta. Los Tipos de Liquidación para los que se 
 * aplican este caso son:</li>
 * <br>
 * <li>EMXNABONIXEB</li>
 * <li>EUSDABONIXEB</li>
 * <li>EEURABONIXEB</li>
 * <li>RMXNCARGIXEB</li>
 * <li>RUSDCARGIXEB</li>
 * <li>REURCARGIXEB</li>
 *	
 * <li>Para cualquier otro tipo de liquidación, la utilidad/pérdida le será asignada
 * a la “Sucursal Virtual”.</li>
 *	
 * @author Gustavo Gonzalez.
 * @version  $Revision: 1.6 $ $Date: 2008/08/11 20:08:00 $
 */
public abstract class ReporteUtilidadesIxeDirecto extends AbstractReporteUtilidades {

	/**
     * Realiza la b&uacute;squeda de los detales de deal el reporte de Utilidades en PDF
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void busquedaDetalles(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getDelegate();
        Visit visit = (Visit) getVisit();
        setDescargando(true);
        try {
            if (getRegisterDateDe() == null) {
                throw new SicaException("Debe definir la fecha de b\u00fasqueda de Inicio.", null);
            }
            ReporteUtilidadesDao repUtDao = (ReporteUtilidadesDao) getApplicationContext().
                    getBean("reporteUtilidadesDao");
            boolean rangoPermitido = isRangoFechasPermitido();
            Date fechaInicio = DateUtils.inicioDia(getRegisterDateDe());
            setRegisterDateDe(fechaInicio);
            Date fechaFin = DateUtils.finDia(rangoPermitido ? getRegisterDateHasta() : fechaInicio);
            setRegisterDateHasta(fechaFin);
            if (fechaFin.getTime() - fechaInicio.getTime() > ((long) 1000 * 3600 * 24 * 31)) {
                throw new SicaException("No se puede consultar un rango de fechas mayor a un mes.");
            }
            List idsMnemonicos = new ArrayList();
            idsMnemonicos.add(ParametroSica.MNEM_CTA_CHQ_IXE_1);
            idsMnemonicos.add(ParametroSica.MNEM_CTA_CHQ_IXE_2);
            idsMnemonicos.add(ParametroSica.MNEM_CTA_CHQ_IXE_3);
            idsMnemonicos.add(ParametroSica.MNEM_CTA_CHQ_IXE_4);
            idsMnemonicos.add(ParametroSica.MNEM_CTA_CHQ_IXE_5);
            idsMnemonicos.add(ParametroSica.MNEM_CTA_CHQ_IXE_6);
            List mnemonicos = getSicaServiceData().
                findMnemonicosUtilidadesIxeDirecto(idsMnemonicos);
            List detallesSucursales = repUtDao.findDetallesUtilidadesIxeDirecto(fechaInicio,
                    fechaFin, visit.getIdCanal(), mnemonicos, true);
            sortDealDetallesSucursal(detallesSucursales);
            List detallesSucursalVirtual = repUtDao.findDetallesUtilidadesIxeDirecto(fechaInicio,
                    fechaFin, visit.getIdCanal(), mnemonicos, false);
            sortDealDetallesSucursalVirtual(detallesSucursalVirtual);
            List l = new ArrayList();
            l.addAll(detallesSucursales);
            l.addAll(detallesSucursalVirtual);
            if (l.isEmpty()) {
                delegate.record("No se encontraron operaciones para la fecha seleccionada", null);
            }
            else {
                seleccionarImpresion(detallesSucursales, detallesSucursalVirtual);
            }
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), ValidationConstraint.REQUIRED);
        }
        finally {
            setDescargando(false);
        }
    }

    /**
     * Llama a imprimir(), con las opciones adecuadas.
     *
     * @param detallesSucursales La lista de mapas con la informaci&oacute;n para los reportes.
     * @param detallesSucursalVirtual La lista de mapas con la informaci&oacute;n para los reportes.
     */
    private void seleccionarImpresion(List detallesSucursales, List detallesSucursalVirtual) {
        if (COMPLETO_PDF.equals(getTipoReporte())) {
            imprimir("WEB-INF/jasper/ReporteUtilidades.jasper", "ReporteUtilidades",
                    detallesSucursales, detallesSucursalVirtual, true);
        }
        else if (COMPLETO_XLS.equals(getTipoReporte())) {
            imprimir("WEB-INF/jasper/ReporteUtilidadesXls.jasper", "ReporteUtilidadesXls",
                    detallesSucursales, detallesSucursalVirtual, false);
        }
    }

    /**
     * Genera el reporte en PDF o en XLS, de acuerdo a la selecci&oacute;n del usuario.
     *
     * @param resource La ruta y nombre del archivo .jasper.
     * @param name El nombre para el archivo.
     * @param detallesSucursales La lista de mapas con la informaci&oacute;n para los reportes.
     * @param detallesSucVirtual La lista de mapas con la informaci&oacute;n para los reportes.
     * @param pdf True para PDF, False para Xls.
     */
    private void imprimir(String resource, String name, List detallesSucursales,
                          List detallesSucVirtual, boolean pdf) {
        JasperPrint jasperPrint;
        InputStream inputStream = null;
        try {
            JRDataSource dataSource = getDataSource(detallesSucursales, detallesSucVirtual);
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
     * Obtiene el DataSource para el reporte de Utilidades
     *
     * @param detallesSucursales La lista de mapas con la informaci&oacute;n para los reportes.
     * @param detallesSucVirtual La lista de mapas con la informaci&oacute;n para los reportes.
     * @return JRDataSource.
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
    public JRDataSource getDataSource(List detallesSucursales, List detallesSucVirtual) {
    	List dealDetallesMap = new ArrayList();
    	crearMapaReportePorLista(dealDetallesMap, detallesSucursales, true);
    	crearMapaReportePorLista(dealDetallesMap, detallesSucVirtual, false);
    	return new ListDataSource(dealDetallesMap);
    }
    
    /**
     * Obtiene la lista de mapas para crear el DataSource para el reporte de Utilidades.
     *
     * @param dealDetallesMap La lista con los mapas para el DataSource.
     * @param listaDetalles La lista con los detalles de Deal para obtener los datos del reporte.
     * @param sucursales Define si la lista es de sucursales o para Ixe Directo
     */
    public void crearMapaReportePorLista(List dealDetallesMap, List listaDetalles, 
    			boolean sucursales) {
    	List resultados = new ArrayList();
    	for (Iterator it = listaDetalles.iterator(); it.hasNext(); ) {
    		Map element = (Map) it.next();
            HashMap map = llenarValores(sucursales, resultados, element);
    		dealDetallesMap.add(map);
    	}
    }

    /**
     * Crea un mapa para cada registro del reporte de utilidades, de acuerdo.
     *
     * @param sucursales Si es para sucursales o no.
     * @param l La lista de resultados a donde se agregar&aacute; el mapa creado.
     * @param element El mapa con los valores extraidos de la base de datos.
     * @return HashMap.
     */
    private HashMap llenarValores(boolean sucursales, List l, Map element) {
        Visit visit = (Visit) getVisit();
        HashMap map = new HashMap();
        map.put("utilidadesIxeDirecto",  Boolean.TRUE);
        map.put("de", getRegisterDateDe());
        map.put("hasta", getRegisterDateHasta());
        map.put("canal", visit.getIdCanal());
        map.put("idPromotor", "");
        map.put(LlavesMapas.PROMOTOR, sucursales ?
                element.get("nombreSucursal") : "IXE DIRECTO");
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
        map.put(LlavesMapas.CLIENTE, element.get(LlavesMapas.CLIENTE));
        map.put(LlavesMapas.MONTO, element.get(LlavesMapas.MONTO));
        map.put(LlavesMapas.TIPO_CAMBIO, element.get(LlavesMapas.TIPO_CAMBIO));
        map.put("utilidad", new Double(((Double) element.get(LlavesMapas.MONTO)).
                doubleValue() *
                ((Double) map.get(LlavesMapas.SOBRE_PRECIO)).doubleValue()));
        if (!l.contains(map.get(LlavesMapas.ID_DEAL))) {
            l.add(map.get(LlavesMapas.ID_DEAL));
        }
        map.put("granTotalNumeroDeals", new Integer(l.size()));
        revisarComisiones(element, map);
        return map;
    }

    /**
     * Llena los campos de comisiones.
     *
     * @param element El mapa con la informaci&oacute;n del registro.
     * @param map El mapa de resultados a modificar.
     */
    private void revisarComisiones(Map element, HashMap map) {
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
    private List sortDealDetallesSucursal(List dd) {
        Collections.sort(dd, new Comparator() {
            public int compare(Object o1, Object o2) {
                Map dd1 = (Map) o1;
                Map dd2 = (Map) o2;
                Boolean rdd1 = (Boolean) dd1.get("recibimos");
                Boolean rdd2 = (Boolean) dd2.get("recibimos");
                int comp = ((String) dd1.get("nombreSucursal")).
                        compareTo((String) dd2.get("nombreSucursal"));
                if (comp != 0) {
                    //Devuelve el valor de la comprarcion por Sucursal
                    return comp;
                }
                else {
                    comp = ((String) dd1.get(LlavesMapas.ID_DIVISA)).
                            compareTo((String) dd2.get(LlavesMapas.ID_DIVISA));
                    if (comp != 0) {
                        //Devuelve el valor de la comprarcion por Id Divisa
                        return comp;
                    }
                    else {
                        comp = rdd1.toString().compareTo(rdd2.toString());
                        if (comp != 0) {
                            //Devuelve el valor de la comparacion por Compras y Ventas.
                            return comp;
                        }
                        else {
                            //Devuelve el valor de la comprarcion por Producto
                            comp = ((String) dd1.get(LlavesMapas.CLAVE_FORMA_LIQUIDACION)).
                                    compareTo(
                                            (String) dd2.get(LlavesMapas.CLAVE_FORMA_LIQUIDACION));
                            if (comp != 0) {
                                return comp;
                            }
                            else {
                                //Devuelve el valor de la comprarcion por Id Deal
                                return String.valueOf(dd1.get(LlavesMapas.ID_DEAL)).compareTo(
                                        String.valueOf(dd2.get(LlavesMapas.ID_DEAL)));
                            }
                        }
                    }
                }
            }
        });
        return dd;
    }

    /**
     * Ordena los Detalles de Deal alamcenados en la lista por Divisa, 
     * Compras/Ventas, Producto y Id Deal.
     * 
     * @param dd La lista de detalles de Deals.
     * @return List
     */
    private List sortDealDetallesSucursalVirtual(List dd) {
    	Collections.sort(dd, new Comparator() {
    		public int compare(Object o1, Object o2) {
    			Map dd1 = (Map) o1;
    			Map dd2 = (Map) o2;
                Boolean rdd1 = (Boolean) dd1.get("recibimos");
                Boolean rdd2 = (Boolean) dd2.get("recibimos");
                int comp = ((String) dd1.get(LlavesMapas.ID_DIVISA)).
                        compareTo((String) dd2.get(LlavesMapas.ID_DIVISA));
                if (comp != 0) {
                    //Devuelve el valor de la comprarcion por Id Divisa
                    return comp;
                }
                else {
                    comp = rdd1.toString().compareTo(rdd2.toString());
                    if (comp != 0) {
    					//Devuelve el valor de la comparacion por Compras y Ventas.
    					return comp;
    				}
    				else {
    					//Devuelve el valor de la comprarcion por Producto
                        comp = ((String) dd1.get(LlavesMapas.CLAVE_FORMA_LIQUIDACION)).
                                compareTo(
                                        (String) dd2.get(LlavesMapas.CLAVE_FORMA_LIQUIDACION));
    					if (comp != 0) {
    						return comp;
    					}
    					else {
                            return String.valueOf(dd1.get(LlavesMapas.ID_DEAL)).compareTo(
                                    String.valueOf(dd2.get(LlavesMapas.ID_DEAL)));
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
     * Constante con los 4 tipos de reportes.
     */
    public static final String[] REPORTES = {COMPLETO_PDF, COMPLETO_XLS};    
}

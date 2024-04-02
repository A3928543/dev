/*
 * $Id: ReporteDealsPagoAnt.java,v 1.10 2008/02/22 18:25:38 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.reportes;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * P&aacute;gina que permite al usuario imprimir los reportes de deals
 * con pago anticipado
 *
 * @author Gerardo Corzo Herrera
 */
public abstract class ReporteDealsPagoAnt extends SicaPage implements DataSourceProvider {

    /**
     * M&eacute;todo que imprime el reporte de reporte de los deals
     * que tengan pago anticipado en PDF
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void reportes(IRequestCycle cycle) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
		if (getRegisterDate() == null){
			delegate.record("Debe definir la fecha de b\u00fasqueda.", null);
			return;
		}
        Calendar gc = new GregorianCalendar();
        gc.setTime(getRegisterDate());
        gc.add(Calendar.DAY_OF_YEAR,1);
        gc.add(Calendar.SECOND, -1);
        setGc(gc.getTime()); 
    	setRegisterDate(getRegisterDate());
    	setOrdenSeleccionado(getOrdenSeleccionado());
    	if ("Cliente".equals(getOrdenSeleccionado())){
    		setDetallesTable(getSicaServiceData().findDealsDetallesPagoAnticipadoByDate(getRegisterDate(), getGc(), "dd.deal.contratoSica.noCuenta"));  
    	}
    	else if ("Promotor".equals(getOrdenSeleccionado())){
    		setDetallesTable(getSicaServiceData().findDealsDetallesPagoAnticipadoByDate(getRegisterDate(), getGc(), "dd.deal.usuario.idUsuario"));
    	}
    	else {
    		setDetallesTable(getSicaServiceData().findDealsDetallesPagoAnticipadoByDate(getRegisterDate(), getGc(), "dd.deal.idDeal"));	
    	} 
        if (getDetallesTable().isEmpty()) {
            delegate.record("No se encontraron datos con los Criterios de B\u00fasqueda especificados.", null);
        }
    }
    
    /**
     * Pobla el combo box de los criterios de ordenaci&oacute;n para la b&uacute;squeda.
     * 
     * @return StringPropertySelectionModek.
     */
    public StringPropertySelectionModel getOrdenadoPor() {
        return new StringPropertySelectionModel(new String[] {"Deal","Cliente", "Promotor"});
    }
    
    /**
     * M&eacute;todo que obtiene el DataSource para el reporte de los deals
     * que tengan pago anticipado
     * 
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
    public JRDataSource getDataSource(String id) {
        Calendar gc = new GregorianCalendar();
        String ticket = ((Visit) getVisit()).getTicket();
        List l;
        gc.setTime(getRegisterDate());
        gc.add(Calendar.DAY_OF_YEAR,1);
        gc.add(Calendar.SECOND, -1);
    	if ("Cliente".equals(getOrdenSeleccionado())){
    		l  = getSicaServiceData().findDealsDetallesPagoAnticipadoByDate(getRegisterDate(), gc.getTime(), "dd.deal.contratoSica.noCuenta");  
    	}
    	else if ("Promotor".equals(getOrdenSeleccionado())){
    		l = getSicaServiceData().findDealsDetallesPagoAnticipadoByDate(getRegisterDate(), gc.getTime(), "dd.deal.usuario.idUsuario");
    	}
    	else {
    		l = getSicaServiceData().findDealsDetallesPagoAnticipadoByDate(getRegisterDate(), gc.getTime(), "dd.deal.idDeal");	
    	}    	               
        List dealDetallesMap = new ArrayList();        
        for (Iterator it = l.iterator(); it.hasNext();) {
			BaseGlobal global = (BaseGlobal) getGlobal();
			ApplicationContext applicationContext = global.getApplicationContext();
            DealDetalle element = (DealDetalle) it.next();
            HashMap map = new HashMap();
			try {
				InputStream inputStream = applicationContext.getResource("images/reportes/logo_ixe.gif").getInputStream();
				map.put("image", inputStream);
			}
            catch (IOException e) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug(e);
                }
            }
            map.put("idDeal", new Integer(element.getDeal().getIdDeal()));
            if (element.getClaveFormaLiquidacion() != null) {
                String fpl = getFormasPagoLiqService().getNombreFormaLiquidacion(ticket, element.getClaveFormaLiquidacion());
            	map.put("claveFormaLiquidacion", fpl);
            }
            else {
            	map.put("claveFormaLiquidacion", "");
            }
            map.put("noCuenta", element.getDeal().getContratoSica().getNoCuenta());
            map.put("promotor", element.getDeal().getUsuario().getPersona().getNombreCompleto());
            map.put("folioDetalle", new Integer(element.getFolioDetalle()));
            map.put("monto", new Double(element.getMonto()));
            map.put("status", element.getStatusDetalleDeal());
            if ("S".equals(element.decodificarEvento(7))) {
                map.put("excedente", "Sí");
            }
            else {
                map.put("excedente", "No");
            }
            dealDetallesMap.add(map);
        }
        return new ListDataSource(dealDetallesMap);
    }
    
    /**
     * M&eacute;todo que genera el reporte de Deals de Pago Anticipado.
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void imprimirReporteXls(IRequestCycle cycle) {
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
            InputStream inputStream = global.getApplicationContext().getResource(resource).getInputStream();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            jasperPrint = JasperFillManager.fillReport(jasperReport, reportOutParams, dataSource);
        }
        catch (JRException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
        catch (IOException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
        try {
            HttpServletResponse response = cycle.getRequestContext().getResponse();
            JRXlsExporter exporter = new JRXlsExporter();
            ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
            exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
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
            }
        }
        catch (JRException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
    }
    
    /**
     * Regresa el valor de registerDate.
     *
     * @return Date.
     */    
    public abstract Date getRegisterDate();
    
    /**
     * Fija el valor de registerDate.
     *
     * @param registerDate El valor a asignar.
     */
    public abstract void setRegisterDate(Date registerDate);

    /**
     * Regresa el valor de gc.
     *
     * @return Date.
     */
    public abstract Date getGc();
    
    /**
     * Fija el valor de gc.
     *
     * @param gc El valor a asignar.
     */
    public abstract void setGc(Date gc);

    /**
     * Regresa el valor de detalles.
     *
     * @return getDetalles.
     */
    public abstract List getDetalles();
    
    /**
     * Fija el valor de detalles.
     *
     * @param detalles El valor a asignar.
     */
    public abstract void setDetalles(List detalles);

    /**
     * Regresa el valor de detallesTable.
     *
     * @return List.
     */
    public abstract List getDetallesTable();

    /**
     * Fija el valor de detallesTable.
     *
     * @param detallesTable El valor a asignar.
     */
    public abstract void setDetallesTable(List detallesTable);

    /**
     * Regresa el valor de ordenSeleccionado.
     *
     * @return String.
     */
    public abstract String getOrdenSeleccionado();

    /**
     * Fija el valor de ordenSeleccionado.
     *
     * @param ordenSeleccionado El valor a asignar.
     */
    public abstract void setOrdenSeleccionado(String ordenSeleccionado);    
}

package com.ixe.ods.sica.pages.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.IRequestCycle;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;

/**
 * P&aacute;gina que permite al usuario buscar e imprimir los reportes de
 * diarios
 * 
 * @author Pedro M. Espinosa (espinosapm)
 */
public abstract class ReportesDocumentacionFaltante extends SicaPage implements DataSourceProvider {

	/**
	 * Asigna los valores de los componentes de la p&aacute;gina.
	 * 
	 * @param cycle El ciclo de la p&acute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		setDetallesTable(new ArrayList());
		setDetalles(new ArrayList());
		super.activate(cycle);
		setModo((String) cycle.getServiceParameters()[0]);
		/*setRegisterDate(null);
		setRegisterDateHasta(null);*/
	}

	/**
	 * Busca los detalles con operaciones de compra y venta a imprimir.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina
	 */
	public void reportes(IRequestCycle cycle) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) 
		getBeans().getBean("delegate");
		
		List resultadoBusqueda = null;
		resultadoBusqueda = getReportesServiceData().
				findReporteDocumentacionFaltante("", getNoContrato());
		
		setDetalles(resultadoBusqueda);
		
		if(getNoContrato()==null || getNoContrato().trim().equals("")){
			Object parameters[] = new Object[3];
			parameters[0] = "WEB-INF/jasper/RepDocFaltante.jasper";
			parameters[1] = "ReporteDocumentacionFaltanteXls";
			parameters[2] = "directo";
			cycle.setServiceParameters(parameters);
			imprimirReporteDocumentacionFaltanteXls(cycle);
		}
		else{
			setDetallesTable(resultadoBusqueda);
			if (getDetallesTable().isEmpty()) {
				delegate.record("No se encontraron datos con los Criterios de " +
						"B\u00fasqueda especificados.", null);
			}
		}
	}

	/**
	 * Obtiene el DataSource del reporte de Diarios.
	 * 
	 * @return JRDataSource.
	 * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
	 */
	public JRDataSource getDataSource(String id) {
		List dealDetallesMap = new ArrayList();
		for (Iterator it = getDetalles().iterator(); it.hasNext();) {
			Object[] tupla = (Object[]) it.next();
			HashMap map = new HashMap();
			map.put("noContratoSica", (String)tupla[0]);
			map.put("fechaApertura", (String)tupla[1]);
			map.put("cliente", (String)tupla[2]);
			map.put("tipoDePersona", (String)tupla[3]);
			map.put("estado", (String)tupla[4]);
			map.put("faltantes", (String)tupla[5]);
			map.put("informacionDummy", (String)tupla[6]);
			map.put("relacionadoCon", (String)tupla[7]);
			map.put("promotor", (String)tupla[8]);
			map.put("canal", (String)tupla[9]);
			map.put("holding", (String)tupla[10]);
			map.put("credito", (String)tupla[11]);
			dealDetallesMap.add(map);
		}
		if(!id.trim().equalsIgnoreCase("")){
			setDetalles(null);
		}
		return new ListDataSource(dealDetallesMap);
	}

	/**
	 * Imprime el reporte Diario en formato de Excel
	 * 
	 * @param cycle El ciclo de la P&aacute;gina
	 */
	public void imprimirReporteDocumentacionFaltanteXls(IRequestCycle cycle) {
		Object parameters[] = cycle.getServiceParameters();
		String resource = (String) parameters[0];
		String name = (String) parameters[1];
		String id = "";
		if(parameters.length > 2){
			id = (String)parameters[2];
		}
		Map reportOutParams = null;
		if (parameters.length > 3) {
			reportOutParams = (Map) parameters[3];
		}
		JasperPrint jasperPrint = null;
		try {
			JRDataSource dataSource = getDataSource(id);
			BaseGlobal global = (BaseGlobal) getGlobal();
			InputStream inputStream = global.getApplicationContext().
			getResource(resource).getInputStream();
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
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, 
					Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
			exporter.setParameter(JRXlsExporterParameter.CHARACTER_ENCODING,"ISO-8859-1");
			exporter.exportReport();
			byte[] bytes2 = xlsReport.toByteArray();
			response.setContentType("application/vnd.ms-excel");
			response.setContentLength(bytes2.length);
			response.setHeader("Content-disposition", "attachment; filename=\""
					+ name + ".xls\"");
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

	
/*	*//**
	 * Regresa el valor de registerDate.
	 *
	 * @return Date.
	 *//*  
	public abstract Date getRegisterDate();

	*//**
	 * Fija el valor de registerDate.
	 *
	 * @param registerDate El valor a asignar.
	 *//*
	public abstract void setRegisterDate(Date registerDate);

	*//**
	 * Regresa el valor de registerDate.
	 *
	 * @return Date.
	 *//*  
	public abstract Date getRegisterDateHasta();

	*//**
	 * Fija el valor de registerDate.
	 *
	 * @param registerDateHasta El valor a asignar.
	 *//*
	public abstract void setRegisterDateHasta(Date registerDateHasta);
*/
	/**
	 * Regresa el valor de gc.
	 *
	 * @return Date.
	 */  
	public abstract Date getGc();

	/**
	 * Fija el gc de registerdate.
	 *
	 * @param gc El valor a asignar.
	 */
	public abstract void setGc(Date gc);

	/**
	 * Regresa el valor de detalles.
	 *
	 * @return List.
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
	
/*	*//**
	 * Obtiene el nombre del cliente
	 * 
	 * @return String
	 *//*
	public abstract String getNombreCorto();
	
	*//**
	 * Establece el nombre del cliente
	 * 
	 * @param nombreCorto El nombre del cliente
	 *//*
	public abstract void setNombreCorto(String nombreCorto);
*/	
	/**
	 * Obtiene el numero de contrato SICA
	 * 
	 * @return String
	 */
	public abstract String getNoContrato();
	
	/**
	 * Establece el numero de contrato SICA
	 * 
	 * @param noContrato El numero de contrato SICA
	 */
	public abstract void setNoContrato(String noContrato);
	
}


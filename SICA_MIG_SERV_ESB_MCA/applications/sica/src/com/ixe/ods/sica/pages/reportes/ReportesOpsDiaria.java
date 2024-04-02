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
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.tapestry.IRequestCycle;
import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.jasper.ListDataSource;
//import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * P&aacute;gina que permite al usuario buscar e imprimir los reportes de
 * diarios
 * 
 * @author Pedro M. Espinosa (espinosapm)
 */
public abstract class ReportesOpsDiaria extends SicaPage implements
		DataSourceProvider {
	public void activate(IRequestCycle cycle) {
		setDetalles(new ArrayList());
		super.activate(cycle);
		setModo((String) cycle.getServiceParameters()[0]);
		setFechaSeleccionada(null);
	}

	public void reportes(IRequestCycle cycle) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans()
				.getBean("delegate");
		
		Calendar gc = new GregorianCalendar();
		gc.setTime(getFechaSeleccionada());
		gc.add(Calendar.DAY_OF_YEAR,1);
		gc.add(Calendar.SECOND, -1);
		setGc(gc.getTime()); 
		setFechaSeleccionada(getFechaSeleccionada());
		
		Date inicioDia  = DateUtils.inicioDia(getFechaSeleccionada()); 
		Date finDia = DateUtils.finDia(getFechaSeleccionada());
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(finDia);
		cal.add(Calendar.DATE, 1);
		
		finDia=cal.getTime();
		java.sql.Date inicioDiaSql =  new java.sql.Date(inicioDia.getTime());
		java.sql.Date finDiaSql =  new java.sql.Date(finDia.getTime());
		
		
		
		// Buscamos los datos que mostraremos en el reporte
		List resultadoBusqueda = getReportesServiceData()
				.findReporteOperacionesDiarias(inicioDiaSql, finDiaSql);
		setDetalles(resultadoBusqueda);
		//Si no encontramos datos mandamos mensaje de error
		if (getDetalles().isEmpty()) {
			delegate.record("No se encontraron datos con los Criterios de "
					+ "B\u00fasqueda especificados.", null);
		} else {
			Object parameters[] = new Object[3];
			//Si encuentra datos se da el nombre y direccion de la plantilla del reporte
			parameters[0] = "WEB-INF/jasper/RepOpsDiaria.jasper";
			//Ponemos el nombre al reporte
			parameters[1] = "ReporteOpsDiaria";
			parameters[2] = "directo";
			cycle.setServiceParameters(parameters);
			
			//mandamos a imprimir el reporte
			delegate.record("Reporte generado.", null);
			this.imprimirReporteOperacionesDiariaXls(cycle);
			
		}
	}

	public void imprimirReporteOperacionesDiariaXls(IRequestCycle cycle) {
		Object parameters[] = cycle.getServiceParameters();

		String resource = (String) parameters[0];
		String name = (String) parameters[1];
		Map reportOutParams = null;
		if (parameters.length > 3) {
			reportOutParams = (Map) parameters[3];
		}
		JasperPrint jasperPrint = null;
		try {
			//Se asignan los campos donde ira cada dato
			JRDataSource dataSource = getDataSource("");
			BaseGlobal global = (BaseGlobal) getGlobal();
			InputStream inputStream = global.getApplicationContext().getResource(resource).getInputStream();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			jasperPrint = JasperFillManager.fillReport(jasperReport,
					reportOutParams, dataSource);
		} catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		} catch (IOException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		try {
			//Damos formato a las celdas de excel
			HttpServletResponse response = cycle.getRequestContext()
					.getResponse();
			JRXlsExporter exporter = new JRXlsExporter();
			ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(
					JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,
					Boolean.TRUE);
			exporter.setParameter(
					JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
					Boolean.TRUE);
			exporter.setParameter(
					JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					Boolean.FALSE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
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
				
			} catch (IOException ioex) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(ioex);
				}
				System.err.println(ioex.getMessage());
				ioex.printStackTrace();
			}
		} catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
			System.err.println(e.getMessage());
			e.printStackTrace();
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
			map.put("fecha", (Date)tupla[0]);
			map.put("noDeal", (Integer)tupla[1]);
			map.put("tipoOperacion", (String)tupla[2]);
			map.put("importe", (Double)tupla[3]);
			map.put("divisa", (String)tupla[4]);
			map.put("tipoCambioCliente", (Double)tupla[5]);
			map.put("tipoCambioMesa", (Double)tupla[6]);
			map.put("utilidad", (Double)tupla[7]);
			map.put("contratoCorto", (String)tupla[8]);
			map.put("contratoSica", (String)tupla[9]);
			map.put("cuentaCheques", (String)tupla[10]);
			
			map.put("nombreCliente", (String)tupla[11]);
			map.put("promotorCliente", (String)tupla[12]);
			map.put("canal", (String)tupla[13]);
			map.put("clienteBanorte", (String)tupla[14]);
			map.put("Sic", (String)tupla[15]);
			map.put("noSucursal", (String)tupla[16]);
			dealDetallesMap.add(map);
		}
		return new ListDataSource(dealDetallesMap);
	}

	private List detalles = null;

	/**
	 * Regresa el valor de detalles.
	 * 
	 * @return List.
	 */
	public List getDetalles() {
		return this.detalles;
	}

	/**
	 * Fija el valor de detalles.
	 * 
	 * @param detalles
	 *            El valor a asignar.
	 */
	public void setDetalles(List detalles) {
		this.detalles = detalles;
	}

	/**
	 * Fija el valor de divisaSeleccionada.
	 * 
	 * @param divisaSeleccionada
	 *            El valor a asignar.
	 */
	public abstract void setFechaSeleccionada(Date fechaSeleccionada);

	/**
	 * Regresa el valor de operacionSeleccionada.
	 * 
	 * @return String.
	 */
	public abstract Date getFechaSeleccionada();
	
	/**
	 * Regresa el valor de modo.
	 * 
	 * @return String.
	 */
	public abstract String getModo();

	/**
	 * Establece el valor de modo.
	 * 
	 * @param modo
	 *            El valor a asignar.
	 */
	public abstract void setModo(String modo);
	
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
}

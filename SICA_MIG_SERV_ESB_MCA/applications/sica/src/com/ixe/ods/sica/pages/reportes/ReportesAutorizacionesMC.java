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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

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
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * P&aacute;gina que permite al usuario buscar e imprimir los reportes de
 * diarios
 * 
 * @author Pedro M. Espinosa (espinosapm)
 */
public abstract class ReportesAutorizacionesMC extends SicaPage implements DataSourceProvider {

	/**
	 * Asigna los valores de los componentes de la p&aacute;gina.
	 * 
	 * @param cycle El ciclo de la p&acute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		//setDetallesTable(new ArrayList());
		//setDetalles(new ArrayList());
		super.activate(cycle);
		setModo((String) cycle.getServiceParameters()[0]);
		Visit visit = (Visit) getVisit();
	}

	/**
     * Regresa un RecordSelectionModel con todos los canales.
     *
     * @return IPropertySelectionModel
     */
    public IPropertySelectionModel getCanalesModel() {
    	List canales = getSicaServiceData().findAllCanales();
    	Canal primerElemento = new Canal();
    	primerElemento.setIdCanal("0");
    	primerElemento.setNombre("TODOS");
    	canales.add(0, primerElemento);
        return new RecordSelectionModel(canales, "idCanal", "nombre");
    }

	/**
	 * Regresa las opciones de autorizacion
	 *
	 * @return StringPropertySelectionModel
	 */
	public IPropertySelectionModel getTipoAutorizacionesModel() {
		List binomioAutorizacion = new ArrayList();
		binomioAutorizacion.add(new String[]{"TODAS","'DN Documento', 'DInt Documento', 'DN Rec. M. Ctrl.', 'DInt Rec. M. Ctrl.','DN Plantilla'"});
		binomioAutorizacion.add(new String[]{"Extraordinaria","'DN Documento', 'DInt Documento', 'DN Rec. M. Ctrl.', 'DInt Rec. M. Ctrl.'"});
		binomioAutorizacion.add(new String[]{"Por Plantilla","'DN Plantilla'"});
		return new RecordSelectionModel(binomioAutorizacion, "[1]","[0]");
//		return new StringPropertySelectionModel(new String[] {"TODAS", "Por Plantilla", "Extraordinaria"});
	}

	/**
	 * Regresa las opciones de estado de autorizacion
	 *
	 * @return StringPropertySelectionModel
	 */
	public IPropertySelectionModel getStatusAutorizacionModel() {
/*		List binomioAutorizacion = new ArrayList();
		binomioAutorizacion.add(new String[]{"TODAS","0"});
		binomioAutorizacion.add(new String[]{"Autorizadas","Autorizado"});
		binomioAutorizacion.add(new String[]{"Denegado","Denegado"});
		return new RecordSelectionModel(binomioAutorizacion, "[1]","[0]");
*/		return new StringPropertySelectionModel(new String[] {"TODAS", "Autorizado", "Denegado"});
	}
	
	/**
	 * Busca los detalles con operaciones de compra y venta a imprimir.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina
	 */
	public void reportes(IRequestCycle cycle) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) 
		getBeans().getBean("delegate");
		if (getRegisterDate() == null){
			delegate.record("Debe definir la fecha de b\u00fasqueda.", null);
			return;
		}
		if (getRegisterDateHasta() == null){
			delegate.record("Debe definir la fecha de b\u00fasqueda.", null);
			return;
		}
		
		Calendar gc = new GregorianCalendar();
		gc.setTime(getRegisterDate());
		gc.add(Calendar.DAY_OF_YEAR,1);
		gc.add(Calendar.SECOND, -1);
		setGc(gc.getTime());     		
		setRegisterDate(getRegisterDate());
		
		Date inicioDia  = DateUtils.inicioDia(getRegisterDate()); 
		Date finDia  = DateUtils.finDia(getRegisterDateHasta());
		Calendar cal = new GregorianCalendar();
		cal.setTime(finDia);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		java.sql.Date inicioDiaSql =  new java.sql.Date(inicioDia.getTime());
		java.sql.Date finDiaSql =  new java.sql.Date(cal.getTime().getTime());
		
		List resultadoBusqueda = getReportesServiceData().
			findReporteAutorizacionesMesaControl(getCanalSeleccionado().getIdCanal(), getStatus(), 
					getTipoAutorizacionSeleccionada()[1], inicioDiaSql, finDiaSql);

		setDetalles(resultadoBusqueda);
		
		if (getDetalles().isEmpty()) {
			delegate.record("No se encontraron datos con los Criterios de " +
					"B\u00fasqueda especificados.", null);
		}else{
			Object parameters[] = new Object[3];
			parameters[0] = "WEB-INF/jasper/RepAutorizacionesMesaCambios.jasper";
			parameters[1] = "ReporteAutorizacionesMC";
			parameters[2] = "directo";			
			cycle.setServiceParameters(parameters);			
			this.imprimirReporteAutorizacionesMC(cycle);			
		}
		
		
		
		//setDetallesTable(resultadoBusqueda);
		
		
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
			map.put("fechaCaptura", (Date)tupla[0]);
			map.put("cliente", (String)tupla[1]);
			map.put("noContratoSica", (String)tupla[2]);
			map.put("promotor", (String)tupla[3]);
			map.put("canal", (String)tupla[4]);
			map.put("tipoDePersona", (String)tupla[5]);
			map.put("idDeal", (Integer)tupla[6]);
			map.put("operacion", (String)tupla[7]);
			map.put("divisa", (String)tupla[8]);
			map.put("monto", (Double)tupla[9]);
			map.put("fechaLiquidacion", (Date)tupla[10]);
			map.put("producto", (String)tupla[11]);
			map.put("status", (String)tupla[12]);
			map.put("origenDeal", (String)tupla[13]);
			map.put("resultado", (String)tupla[14]);
			map.put("autorizadoPor", (String)tupla[15]);
			map.put("fechaAutorizacion", (Date)tupla[16]);
			
			dealDetallesMap.add(map);
		}
		return new ListDataSource(dealDetallesMap);
	}

	/**
	 * Imprime el reporte Diario en formato de Excel
	 * 
	 * @param cycle El ciclo de la P&aacute;gina
	 */
	public void imprimirReporteAutorizacionesMC(IRequestCycle cycle) {
		Object parameters[] = cycle.getServiceParameters();
		String resource = (String) parameters[0];
		String name = (String) parameters[1];
		Map reportOutParams = null;
		if (parameters.length > 3) {
			reportOutParams = (Map) parameters[3];
		}
		JasperPrint jasperPrint = null;
		try {
			JRDataSource dataSource = getDataSource("");
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

	/**
	 * Regresa el valor de canalSeleccionado.
	 *
	 * @return Canal.
	 */  
	public abstract Canal getCanalSeleccionado();

	/**
	 * Fija el valor de canalSeleccionado.
	 *
	 * @param canalSeleccionado El valor a asignar.
	 */
	public abstract void setCanalSeleccionado(Canal canalSeleccionado);

	/**
	 * Regresa el valor de status
	 *
	 * @return String.
	 */  
	public abstract String getStatus();

	/**
	 * Fija el valor de status.
	 *
	 * @param status El valor a asignar.
	 */
	public abstract void setStatus(String status);    

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
	 * Regresa el valor de registerDate.
	 *
	 * @return Date.
	 */  
	public abstract Date getRegisterDateHasta();

	/**
	 * Fija el valor de registerDate.
	 *
	 * @param registerDateHasta El valor a asignar.
	 */
	public abstract void setRegisterDateHasta(Date registerDateHasta);

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
	public List getDetalles(){
		return this.detalles;
	}

	/**
	 * Fija el valor de detalles.
	 *
	 * @param detalles El valor a asignar.
	 */
	public void setDetalles(List detalles){
		this.detalles=detalles;
	}

	

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
	
	public abstract String[] getTipoAutorizacionSeleccionada();
	
	public abstract void setTipoAutorizacionSeleccionada(String[] tipoAutorizacionSeleccionada);
	
	private List detalles;
}


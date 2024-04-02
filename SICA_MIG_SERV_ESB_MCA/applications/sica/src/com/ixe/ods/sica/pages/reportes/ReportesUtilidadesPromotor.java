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
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.SupportEngine;
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
public abstract class ReportesUtilidadesPromotor extends SicaPage implements DataSourceProvider {

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
		//Combo promotores
		Visit visit = (Visit) getVisit();
		List promotoresTmp;
		List promotores = new ArrayList();
		promotoresTmp = getSicaServiceData().findAllPromotoresSICA(
					((SupportEngine) getEngine()).getApplicationName());
		
		//Llenando el Combo de Promotores
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
		setOperacionSeleccionada(null);
		setRegisterDate(null);
		setRegisterDateHasta(null);
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
		setPromotorSeleccionado((Integer)getPromotorHashMap().get("idPersona"));
		
		Date inicioDia  = DateUtils.inicioDia(getRegisterDate()); 
		Date finDia  = DateUtils.finDia(getRegisterDateHasta());
		Calendar cal = new GregorianCalendar();
		cal.setTime(finDia);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		java.sql.Date inicioDiaSql =  new java.sql.Date(inicioDia.getTime());
		java.sql.Date finDiaSql =  new java.sql.Date(cal.getTime().getTime());
		
		List resultadoBusqueda = getReportesServiceData().
				findReporteUtilidadPromotres(getPromotorSeleccionado(), inicioDiaSql, 
				finDiaSql, getOperacionSeleccionada(),getCanalSeleccionado().getIdCanal());

		
		setDetalles(resultadoBusqueda);
		if (getDetalles().isEmpty()) {
			delegate.record("No se encontraron datos con los Criterios de " +
					"B\u00fasqueda especificados.", null);
		}else{
			Object parameters[] = new Object[3];
			parameters[0] = "WEB-INF/jasper/RepUtilidadesPromotor.jasper";
			parameters[1] = "RepUtilidadesPromotor";
			parameters[2] = "directo";			
			cycle.setServiceParameters(parameters);		
			this.imprimirReporteUtilidadesPromotorXls(cycle);
		}
		
	}

	/**
	 * Regresa el valor de tipoOperacion.
	 *
	 * @return StringPropertySelectionModel
	 */
	public StringPropertySelectionModel getTipoOperacion() {	
		return new StringPropertySelectionModel(new String[] {"TODAS", "Compra", "Venta"});
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
			map.put("idPromotor", (Integer)tupla[0]);
			map.put("promotor", (String)tupla[1]);
			map.put("operacion", (String)tupla[2]);
			map.put("utilidad", (Double)tupla[3]);
			map.put("canal", (String)tupla[4]);
			dealDetallesMap.add(map);
		}
		return new ListDataSource(dealDetallesMap);
	}

	/**
	 * Imprime el reporte Diario en formato de Excel
	 * 
	 * @param cycle El ciclo de la P&aacute;gina
	 */
	public void imprimirReporteUtilidadesPromotorXls(IRequestCycle cycle) {
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
	public  List getDetalles(){
		return this.detalles;
	}

	/**
	 * Fija el valor de detalles.
	 *
	 * @param detalles El valor a asignar.
	 */
	public  void setDetalles(List detalles){
		this.detalles=detalles;
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
	 * Obtiene la Lista de Promotores a popular en el combo de los mismos,
	 * de acuerdo a las Jerarqu&iacute;as de Seguridad.
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
	 * Obtiene el Promotor seleccionado del combo de Promotores.
	 *
	 * @return HasMap El Promotor seleccionado.
	 */
	public HashMap getPromotor() {
		return null;
	}

	/**
	 * No hace nada.
	 *
	 * @param promotor El Promotor.
	 */
	public void setPromotor(HashMap promotor) {

	}

	/**
	 * Obtiene el Promotor seleccionado del combo de Promotores.
	 *
	 * @return Integer El Promotor seleccionado.
	 */
	public abstract Integer getPromotorSeleccionado();

	/**
	 * Asigna el nombre del promotor seleccionado.
	 *
	 * @param promotorSeleccionado Integer el Promotor.
	 */
	public abstract void setPromotorSeleccionado(Integer promotorSeleccionado);

	/**
	 * Obtiene un HashMap con los promotores existentes
	 * 
	 * @return HashMap de Promotores
	 */
	public abstract HashMap getPromotorHashMap();

	/**
	 * Asigna el HashMap de los promotores existentes
	 * 
	 * @param promotorHashMap Promotores.
	 */
	public abstract void setPromotorHashMap(HashMap promotorHashMap);
	
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
	 * 
	 */
	private List detalles;
}


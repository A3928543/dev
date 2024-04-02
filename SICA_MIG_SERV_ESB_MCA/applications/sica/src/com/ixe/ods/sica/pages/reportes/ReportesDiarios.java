/*
 * $Id: ReportesDiarios.java,v 1.13 2008/04/14 17:43:50 ccovian Exp $
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
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * P&aacute;gina que permite al usuario buscar e imprimir los reportes de
 * diarios
 * 
 * @author Gerardo Corzo Herrera
 */
public abstract class ReportesDiarios extends SicaPage implements DataSourceProvider {

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
		List divisasFrecuentes = getSicaServiceData().findDivisasFrecuentes();
		setDivisas(divisasFrecuentes);
		//Combo promotores
		Visit visit = (Visit) getVisit();
		List promotoresTmp;
		List promotores = new ArrayList();
		// Verificando si se trata del Area de Promocion
		if (FacultySystem.SICA_CONS_DEAL.equals(getModo())) {
			// Obtiene subordinados incluyendo nodo raiz
			promotoresTmp = getSeguridadServiceData().
			findSubordinadosEjecutivoYEjecutivosASustituir(
					((SupportEngine) getEngine()).getApplicationName(), 
					visit.getUser().getIdPersona());
		}
		// Si se trata de otra Area
		else {
			promotoresTmp = getSicaServiceData().findAllPromotoresSICA(
					((SupportEngine) getEngine()).getApplicationName());
		}
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
		setProductos(null);
		setDivisaSeleccionada(null);
		setOperacionSeleccionada(null);
		setRegisterDate(null);
		setRegisterDateHasta(null);
		//setDetalles(new ArrayList());
	}

	/**
	 * Modelo del combo de Formas de Liquidaci&oacute;n (Productos). 
	 * Se incluye una opci&oacute;n para seleccionar en la 
	 * b&uacute;squeda todas las divisas.
	 *
	 * @return StringPropertySelectionModel Las formas de liquidaci&oacute;n.
	 * */
	public StringPropertySelectionModel arregloFormasLiquidacion() {
		String[] cfl = getFormasPagoLiqService().getClavesFormasLiquidacion(
				((Visit) getVisit()).getTicket(), true);
		String[] cfln = new String[cfl.length + 1];
		cfln[0] = "TODOS";
		for (int i = 0; i < cfl.length; i++) {
			cfln[i + 1] = cfl[i];
		}
		return new StringPropertySelectionModel(cfln);
	}

	/**
	 * Modelo del combo de Divisas. Se incluye una opci&oacute;n
	 * para seleccionar en la b&uacute;squeda todas las divisas.
	 *
	 * @return RecordSelectionModel Las divisas
	 */
	public IPropertySelectionModel getDivisasFrecuentes() {
		List divisasFrecuentes = getSicaServiceData().findDivisasFrecuentes();
		Divisa todas = new Divisa();
		todas.setIdDivisa("0");
		todas.setDescripcion("TODAS");
		divisasFrecuentes.add(0,todas);
		return new RecordSelectionModel(divisasFrecuentes, "idDivisa", "descripcion");
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
		if (getMontoMinimo() < 0 || getMontoMaximo() < 0) {
			delegate.record("Los rangos de los montos deben ser mayores a cero.", null);
			return;
		}
		if (getMontoMinimo()  > getMontoMaximo()) {
			delegate.record("El monto maximo debe ser mayor al monto minimo.", null);
			return;
		}
		Calendar gc = new GregorianCalendar();
		gc.setTime(getRegisterDate());
		gc.add(Calendar.DAY_OF_YEAR,1);
		gc.add(Calendar.SECOND, -1);
		setGc(gc.getTime());     		
		setRegisterDate(getRegisterDate());
		setDivisaSeleccionada(getDivisaSeleccionada());
		setOperacionSeleccionada(getOperacionSeleccionada());
		setClaveFormaLiquidacion(getClaveFormaLiquidacion());
		setPromotorSeleccionado((Integer)getPromotorHashMap().get("idPersona"));
		List resultadoBusqueda = getSicaServiceData().
		findReporteDealsDiarios(getDivisaSeleccionada().getIdDivisa(),
				getClaveFormaLiquidacion(),getOperacionSeleccionada(),
				getPromotorSeleccionado(),DateUtils.inicioDia(getRegisterDate()),
				DateUtils.finDia(getRegisterDateHasta()), getMontoMinimo(), getMontoMaximo());
		setDetallesTable(resultadoBusqueda);
		setDetalles(resultadoBusqueda);
		if (getDetallesTable().isEmpty()) {
			delegate.record("No se encontraron datos con los Criterios de " +
					"B\u00fasqueda especificados.", null);
		}
	}

	/**
	 * Regresa el valor de tipoOperacion.
	 *
	 * @return StringPropertySelectionModel
	 */
	public StringPropertySelectionModel getTipoOperacion() {	
		return new StringPropertySelectionModel(new String[] {"Compra", "Venta", "Compra-Venta"});
	}

	/**
	 * Obtiene el DataSource del reporte de Diarios.
	 * 
	 * @return JRDataSource.
	 * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
	 */
	public JRDataSource getDataSource(String id) {
		String ticket = ((Visit) getVisit()).getTicket();
		Calendar gc = new GregorianCalendar();
		gc.setTime(getRegisterDate());
		gc.add(Calendar.DAY_OF_YEAR,1);
		gc.add(Calendar.SECOND, -1);
		setGc(gc.getTime());
		List dealDetallesMap = new ArrayList();
		for (Iterator it = getDetalles().iterator(); it.hasNext();) {
			DealDetalle element = (DealDetalle) it.next();
			HashMap map = new HashMap();
			map.put("divisa", element.getDivisa().getDescripcion().toUpperCase());
			String fpl = element.getClaveFormaLiquidacion(); 
			if (fpl != null) {
				map.put("claveFormaLiquidacion", getFormasPagoLiqService().
						getNombreFormaLiquidacion(ticket, element.getClaveFormaLiquidacion()));
			}
			else {
				map.put("claveFormaLiquidacion","");
			}
			map.put("isRecibimos", Boolean.valueOf(element.isRecibimos()));
			String pat="";
			String mat="";
			String nom="";
			if (element.getDeal().getPromotor().getPaterno() != null) {
				pat = element.getDeal().getPromotor().getPaterno();
			}
			if (element.getDeal().getPromotor().getMaterno() != null) {
				mat = element.getDeal().getPromotor().getMaterno();
			}
			if (element.getDeal().getPromotor().getNombre() != null) {
				nom = element.getDeal().getPromotor().getNombre();
			}
			map.put("promotor", pat+" "+mat+" "+nom);
			map.put("idDeal", new Integer(element.getDeal().getIdDeal()));
			map.put("folioDetalle", new Integer(element.getFolioDetalle()));
			map.put("tipoCambio", new Double(element.getTipoCambio()));
			map.put("monto", new Double(element.getMonto()));
			map.put("importe", new Double(element.getImporte()));
			if (element.getDeal().getContratoSica() != null) {
				map.put("cliente", element.getDeal().getCliente().getNombreCompleto());
			} 
			else {
				map.put("cliente", "");
			}
			map.put("fechaCaptura", element.getDeal().getFechaCaptura());
			map.put("fechaLiquidacion", element.getDeal().getFechaLiquidacion());
			map.put("status", element.getDescripcionStatus());
			dealDetallesMap.add(map);
		}
		return new ListDataSource(dealDetallesMap);
	}

	/**
	 * Imprime el reporte Diario en formato de Excel
	 * 
	 * @param cycle El ciclo de la P&aacute;gina
	 */
	public void imprimirReporteDiarioXls(IRequestCycle cycle) {
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
	 * Regresa el valor de claveFormaLiquidacion.
	 *
	 * @return String.
	 */  
	public abstract String getClaveFormaLiquidacion();

	/**
	 * Fija el valor de claveFormaLiquidacion.
	 *
	 * @param claveFormaLiquidacion El valor a asignar.
	 */
	public abstract void setClaveFormaLiquidacion(String claveFormaLiquidacion);

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
	 * Obtiene el arreglo de los distintos productos para liquidaci&oacute;n,
	 * incluyendo la opci&oacute;n de elegir todos los productos.
	 * 
	 * @return String [] arreglo de productos para liquidaci&oacute;n
	 * */
	public abstract String[] getProductos();

	/**
	 * Asigna el arreglo de los distintos productos para liquidaci&oacute;n,
	 * 
	 * @param productos para liquidaci&oacute;n
	 * */
	public abstract void setProductos(String [] productos);

	/**
	 * Obtiene la lista de las distintas divisas frecuentes
	 * incluyendo la opci&oacute;n de elegir todas.
	 * 
	 * @return List lista de divisas frecuentes
	 * */
	public abstract List getDivisas();

	/**
	 * Asigna la lista de las distintas divisas frecuentes
	 * 
	 * @param divisas frecuentes
	 * */
	public abstract void setDivisas(List divisas);

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
	 * Obtiene el monto minimo para la busqueda.
	 * 
	 * @return double
	 */
	public abstract double getMontoMinimo();

	/**
	 * Asigna el valor para el monto minimo de la busqueda
	 * 
	 * @param montoMinimo El valor para el monto minimo
	 */
	public abstract void setMontoMinimo(double montoMinimo);

	/**
	 * Obtiene el valor maximo para la busqueda
	 * 
	 * @return double 
	 */
	public abstract double getMontoMaximo();

	/**
	 * Asigna el valor para el monto minimo de la busqueda
	 * 
	 * @param montoMaximo El valor para el monto maximo.
	 */
	public abstract void setMontoMaximo(double montoMaximo);
}


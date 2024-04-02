package com.ixe.ods.sica.pages.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableModel;
import org.apache.tapestry.form.IPropertySelectionModel;

//import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.dto.ExcesoLineaCambioDto;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;
import com.legosoft.tapestry.model.RecordSelectionModel;

public abstract class ReporteExcesosLc extends SicaPage implements DataSourceProvider 
{
	public String LABEL = "label";
    public String VALUE = "value";
    public String TODOS = "Todos";
    public String DEFAULT = "Default";
    public String SELECCIONE = "Seleccione un trimestre";
    public double MILLON_USD = 1000000.0;
    
    public final String resource = "WEB-INF/jasper/ExcesosLineaCambio.jasper";
    
	public void activate(IRequestCycle cycle) 
	{
		//setModo((String) cycle.getServiceParameters()[0]);
		super.activate(cycle);
		
		Visit visit = (Visit) getVisit();
		Canal canal = null;
		String nombreSistema = ((SupportEngine) getEngine()).getApplicationName();
		HashMap canalUsuario = null;
		HashMap trimestre = null;
		Integer idUsuario = new Integer(visit.getUser().getIdUsuario());
		int numRolesUsuario = 0;
		boolean usuarioRiesgos = false;
		
		setRegistrosReporte(null);
		
		SimpleTableModel m = new SimpleTableModel(new ArrayList().toArray(), obtenerEncabezados());
		m.getPagingState().setPageSize(10); 
		m.tableDataChanged(null);
		setModel((ITableModel)m);
		setRegistrosReporte(null);
		((org.apache.tapestry.contrib.table.components.Table)getComponent("table")).fireObservedStateChange();
		
		setMonto(MILLON_USD);
		numRolesUsuario = getLineaCambioServiceData().consultarSiUsuarioPerteneceAreaRiesgos(idUsuario, nombreSistema);
		usuarioRiesgos = numRolesUsuario > 0 ? true : false;
		
		trimestre = new HashMap();
		trimestre.put(VALUE, DEFAULT);
		trimestre.put(LABEL, SELECCIONE);
		setTrimestreSeleccionado(trimestre);
		
		if(!usuarioRiesgos)
		{
			canalUsuario = new HashMap();
			canal = (Canal)getSicaServiceData().find(Canal.class, visit.getIdCanal());
			
			if(canal != null)
			{
				setModoPromotor(true);
				canalUsuario = new HashMap();
				canalUsuario.put(VALUE, canal.getFacultad().getNombre());
				canalUsuario.put(LABEL, canal.getIdCanal() + " - " + canal.getNombre());
				setCanalFacultadSeleccionado(canalUsuario);
				// visit.hasFaculty(FacultySystem.ADM_SICA_LIM)
			}
			else
			{
				getDelegate().record("El usuario no tiene canal asignado.", null);
				return;
			}
		}
	}
	
	public void buscarExcesos(IRequestCycle cycle)
	{
		List registros, datosFiltradosMonto;
		Integer idLineaCambioTmp = null;
		Date fechaInicio, fechaFin;
		ITableColumn columnas[];
		String valor = null;
		ExcesoLineaCambioDto info;
		Calendar fechaIncioTrimestre;
		int trimestre = -1;
		int year = -1;
		int contadorExcesoFV = 0, contadorExcesoPAyTF = 0, contador = 0;
		String canal = null;
		
		//Object[] parameters = cycle.getServiceParameters();
		/*if(parameters != null && parameters[0] != null)
			canal = (String)parameters[0];*/
		
		if(getMonto() < MILLON_USD)
		{
			getDelegate().record("El campo Monto de exceso es obligatorio y debe ser mayor a 1,000,000.00 USD", null);
			return;
		}
		
		if(DEFAULT.equals(getTrimestreSeleccionado().get(VALUE)))
		{
			getDelegate().record("Debe seleccionar un trimestre v\u00e1lido.", null);
			return;
		}
		
		valor = (String)getTrimestreSeleccionado().get(VALUE);
		trimestre = Integer.parseInt(valor.split("-")[0]);
		year = Integer.parseInt(valor.split("-")[1]);
		
		fechaIncioTrimestre = obtenerFechaInicialTrimestre(trimestre);
		fechaIncioTrimestre.set(Calendar.YEAR, year);
		fechaInicio = new Date(fechaIncioTrimestre.getTimeInMillis());
		fechaIncioTrimestre.add(Calendar.MONTH, 3);
		fechaFin = new Date(fechaIncioTrimestre.getTimeInMillis());
		
		if(getCanalFacultadSeleccionado() != null)
			canal = (String)getCanalFacultadSeleccionado().get(VALUE);
		
		setRegistrosReporte(null);
		
		registros = getLineaCambioServiceData().consultarDatosReporteExceso(canal,fechaInicio,fechaFin);
		
		if(registros != null && registros.size() > 0)
		{
			datosFiltradosMonto = new ArrayList();
			
			for(int indice = 0; indice < registros.size(); indice++)
			{
				info = (ExcesoLineaCambioDto)registros.get(indice);
				
				if(idLineaCambioTmp == null)
					idLineaCambioTmp = info.getIdLineaCambio();
				
				if(idLineaCambioTmp.intValue() != info.getIdLineaCambio().intValue())
				{
					contadorExcesoPAyTF = 0;
					contadorExcesoFV = 0;
					idLineaCambioTmp = info.getIdLineaCambio();
				}
				
				if("Exceso PAyTF".equals(info.getTipoExceso()))
					contador = ++contadorExcesoPAyTF;
				else
					contador = ++contadorExcesoFV;
				
				info.setNumeroExcesoTrimestre(new Integer(contador));
				
				if(info.getImporteExceso().doubleValue() >= getMonto())
					datosFiltradosMonto.add(info);
			}
			
			columnas = obtenerEncabezados();
			
			if(datosFiltradosMonto == null || datosFiltradosMonto.isEmpty())
			{
				setRegistrosReporte(null);
				actualizarTabla(columnas, new ArrayList());
				getDelegate().record("No se encontraron resultados con los criterios seleccionados.", null);
			}
			else
			{
				setRegistrosReporte(datosFiltradosMonto);
				actualizarTabla(columnas, datosFiltradosMonto);
			}
		}
		else
			getDelegate().record("No se encontraron resultados con los criterios seleccionados.", null);
	}
	
	public void generarReporteExcel(IRequestCycle cycle) 
	{
		JasperPrint jasperPrint = null;
		
		try 
		{
			JRDataSource dataSource = getDataSource("");
			BaseGlobal global = (BaseGlobal) getGlobal();
			InputStream inputStream = global.getApplicationContext().getResource(resource).getInputStream();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), dataSource);
		} 
		catch (JRException e) 
		{
			e.printStackTrace();
			if (_logger.isDebugEnabled()) 
				_logger.debug(e);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			if (_logger.isDebugEnabled())
				_logger.debug(e);
		}
		
		try 
		{
			HttpServletResponse response = cycle.getRequestContext().getResponse();
			JRXlsExporter exporter = new JRXlsExporter();
			ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
			exporter.exportReport();
			
			byte[] bytes2 = xlsReport.toByteArray();
			response.setContentType("application/vnd.ms-excel");
			response.setContentLength(bytes2.length);
			response.setHeader("Content-disposition", "attachment; filename=\"ReporteExcesosLineaCambio.xls\"");
			
			try 
			{
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(bytes2, 0, bytes2.length);
				ouputStream.flush();
				ouputStream.close();
				
			} 
			catch (IOException ioex) 
			{
				ioex.printStackTrace();
				if (_logger.isDebugEnabled())
					_logger.debug(ioex);
			}
		} 
		catch (JRException e) 
		{
			e.printStackTrace();
			if (_logger.isDebugEnabled())
				_logger.debug(e);
		}
	}
	
	private void actualizarTabla(ITableColumn columnas[], List datos)
	{
		ITableModel model = null;
		SimpleTableModel m = null;

		m = new SimpleTableModel(datos.toArray(), columnas);
		m.tableDataChanged(null);
		model = m;
		model.getPagingState().setPageSize(10);
		
		setModel(model);
		((org.apache.tapestry.contrib.table.components.Table)getComponent("table")).fireObservedStateChange();
		//org.apache.tapestry.contrib.table.model.common.FullTableSessionStateManager
		//org.apache.tapestry.contrib.table.model.common.NullTableSessionStateManager
		//org.apache.tapestry.contrib.table.model.simple.SimpleTableSessionStateManager
	}
	
	private ITableColumn[] obtenerEncabezados()
	{
		ITableColumn columnas[] = new ITableColumn[]{obtenerColumna("Cliente",1), 
									                obtenerColumna("Fecha", 2),
									                obtenerColumna("Linea Autorizada USD", 3),
									                obtenerColumna("Monto USD", 4),
									                obtenerColumna("Promotor", 5),
									                obtenerColumna("Tipo", 6),
									                obtenerColumna("No. Exceso en el trimestre", 7)};
		
		return columnas;
	}
	
	private ITableColumn obtenerColumna(String encabezado, final int columna)
	{
		final SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
		final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		
		ITableColumn col = new SimpleTableColumn(encabezado, false) { 
			private static final long serialVersionUID = 1L;

			public Object getColumnValue(Object value) {
	        	ExcesoLineaCambioDto info = (ExcesoLineaCambioDto) value;
	        	Object obj = new String("");
	        	
	        	if(columna == 1)
	        		return info.getNombreCliente();
	        	else if(columna == 2)
	        		return fecha.format(info.getFechaOperacionExceso());
	        	else if(columna == 3)
	        		return decimalFormat.format(info.getImporteLineaAutorizada());
	        	else if(columna == 4)
	        		return decimalFormat.format(info.getImporteExceso());
	        	else if(columna == 5)
	        		return info.getNombrePromotor();
	        	else if(columna == 6)
	        		return info.getTipoExceso();
	        	else if(columna == 7)
	        		return info.getNumeroExcesoTrimestre();
	        	else
	        		return obj;
	        }
	    };
		
		return col;
	}
	
	public IPropertySelectionModel getTrimestresModel()
	{
		int trimestreActual = obtenerTrimestre(new Date());
		int trimestrePenultimo, trimestreUltimo;
		List datos = new ArrayList();
		HashMap opcion = null;
		Calendar calTrimestreActual = obtenerFechaInicialTrimestre(trimestreActual);
		Calendar calTrimestrePenultimo = null;
		Calendar calTrimestreUltimo = null;
		
		opcion = new HashMap();
		opcion.put(VALUE, trimestreActual + "-" + calTrimestreActual.get(Calendar.YEAR));
		opcion.put(LABEL, trimestreActual + "\u00b0 Trimestre " + calTrimestreActual.get(Calendar.YEAR));
		datos.add(opcion);
		
		calTrimestrePenultimo = Calendar.getInstance();
		calTrimestrePenultimo.setTimeInMillis(calTrimestreActual.getTimeInMillis());
		calTrimestrePenultimo.add(Calendar.MONTH, -3);
		trimestrePenultimo = obtenerTrimestre(calTrimestrePenultimo.getTime());
		opcion = new HashMap();
		opcion.put(VALUE, trimestrePenultimo + "-" + calTrimestrePenultimo.get(Calendar.YEAR));
		opcion.put(LABEL, trimestrePenultimo + "\u00b0 Trimestre " + calTrimestrePenultimo.get(Calendar.YEAR));
		datos.add(0, opcion);
		
		calTrimestreUltimo = Calendar.getInstance();
		calTrimestreUltimo.setTimeInMillis(calTrimestrePenultimo.getTimeInMillis());
		calTrimestreUltimo.add(Calendar.MONTH, -3);
		trimestreUltimo = obtenerTrimestre(calTrimestreUltimo.getTime());
		opcion = new HashMap();
		opcion.put(VALUE, trimestreUltimo + "-" + calTrimestreUltimo.get(Calendar.YEAR));
		opcion.put(LABEL, trimestreUltimo + "\u00b0 Trimestre " + calTrimestreUltimo.get(Calendar.YEAR));
		datos.add(0, opcion);
		
		opcion = new HashMap();
		opcion.put(VALUE, DEFAULT);
		opcion.put(LABEL, SELECCIONE);
		datos.add(0, opcion);
		
		return new RecordSelectionModel(datos, VALUE, LABEL);
	}
	
	public IPropertySelectionModel getCanalesModel()
	{
		int indice = 0;
		Canal canal = null;
		List datos = new ArrayList();
		List canales = getSicaServiceData().findAllCanalesByPromocionMayoreo();
		HashMap opcion = null;
		
		opcion = new HashMap();
		opcion.put(VALUE, DEFAULT);
		opcion.put(LABEL, TODOS);
		datos.add(opcion);
		
		if(canales != null && !canales.isEmpty())
		{
			for(indice = 0; indice < canales.size(); indice++)
			{
				opcion = new HashMap();
				canal = (Canal)canales.get(indice);
				opcion.put(VALUE, canal.getFacultad().getNombre());
				opcion.put(LABEL, canal.getIdCanal() + " - " + canal.getNombre());
				datos.add(opcion);
			}
		}
		
		return new RecordSelectionModel(datos, VALUE, LABEL);
	}
	
	public JRDataSource getDataSource(String arg0) {
		List excesosMap = new ArrayList();
		if(getRegistrosReporte() != null && !getRegistrosReporte().isEmpty())
		{
			for (Iterator it = getRegistrosReporte().iterator(); it.hasNext();) 
			{
				ExcesoLineaCambioDto dto = (ExcesoLineaCambioDto) it.next();
				HashMap map = new HashMap();
				map.put("nombreCliente", dto.getNombreCliente());
				map.put("fecha", dto.getFechaOperacionExceso());
				map.put("lineaAutorizadaUsd", dto.getImporteLineaAutorizada());
				map.put("montoUsd", dto.getImporteExceso());
				map.put("promotor", dto.getNombrePromotor());
				map.put("tipoExceso", dto.getTipoExceso());
				map.put("numExceso", dto.getNumeroExcesoTrimestre());
				excesosMap.add(map);
			}
		}
		
		return new ListDataSource(excesosMap);
	}
	
	public abstract ITableModel getModel();
	public abstract void setModel(ITableModel model);
	
	public abstract double getMonto();
	public abstract void setMonto(double monto);
	
	public abstract HashMap getCanalFacultadSeleccionado();
	public abstract void setCanalFacultadSeleccionado(HashMap canalFacultadSeleccionado);
	
	public abstract boolean isModoPromotor();
	public abstract void setModoPromotor(boolean modoPromotor);
	
	public abstract HashMap getTrimestreSeleccionado();
	public abstract void setTrimestreSeleccionado(HashMap trimestreSeleccionado);
	
	public abstract List getRegistrosReporte();
	public abstract void setRegistrosReporte(List registrosReporte);
}

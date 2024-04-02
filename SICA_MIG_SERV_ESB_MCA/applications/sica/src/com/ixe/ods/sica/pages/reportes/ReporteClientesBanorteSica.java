package com.ixe.ods.sica.pages.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.ixe.ods.sica.dto.OperacionClienteBanorteCrDto;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.utils.DateUtils;

import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;

public abstract class ReporteClientesBanorteSica extends SicaPage implements
		DataSourceProvider 
{
	public final int DIAS_MAX = 60;
	public final long MILISEGUNDOS_DIA = 86400000; // 1000 * 60 * 60 * 24
	public final String resource = "WEB-INF/jasper/ClientesBanorteSica.jasper";
	
	private List listaDetallesAcumulados = new ArrayList();
	
	public void activate(IRequestCycle cycle) {
		//setDetalles(new ArrayList());
		super.activate(cycle);
		//setModo((String) cycle.getServiceParameters()[0]);
		setFechaInicial(null);
		setFechaFinal(null);
		setErrorRangoFechas(false);
		setErrorFechaInicial(false);
	}
	
	public void reportes(IRequestCycle cycle) 
	{
		String msj = "";
		String crGenerico = null;
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
		Map resultado = null;
		List resultadoBusqueda = null;
		Calendar cInicial = null;
		Calendar cFinal = null;
		java.sql.Date fechaInicialSql = null;
		java.sql.Date fechaFinalSql = null;
		int diasConsulta = 0;
		long diferenciaDiasMiliSegundos = 0;
		
		setErrorFechaInicial(false);
		setErrorRangoFechas(false);
		
		if(getFechaInicial() != null && getFechaFinal() != null)
		{
			cInicial = Calendar.getInstance(); 
			cInicial.setTime(DateUtils.inicioDia(getFechaInicial()));
			cFinal = Calendar.getInstance();
			cFinal.setTime(DateUtils.finDia(getFechaFinal()));
			
			if(cInicial.after(cFinal))
			{
				msj = "La fecha inicial no debe ser mayor a la fecha final.";
				setErrorFechaInicial(true);
			}
			else 
			{
				diferenciaDiasMiliSegundos = cFinal.getTimeInMillis() - cInicial.getTimeInMillis();
				diasConsulta = (int)Math.floor(diferenciaDiasMiliSegundos / MILISEGUNDOS_DIA);
				
				if(diasConsulta > DIAS_MAX)
				{
					msj = "Debe modificar las fechas de consulta, el periodo de consulta no debe ser mayor a 60 d\u00EDas.";
					setErrorRangoFechas(true);
				}
				else
				{
					fechaInicialSql =  new java.sql.Date(cInicial.getTimeInMillis());
					fechaFinalSql =  new java.sql.Date(cFinal.getTimeInMillis());
					resultadoBusqueda = getReportesServiceData().findCrClientesBanorteEnSica(fechaInicialSql, fechaFinalSql);
					
					if(resultadoBusqueda != null && !resultadoBusqueda.isEmpty())
					{
						crGenerico = getReportesServiceData().findCrGenerico();
						resultado = agruparDetallesPorDeal(resultadoBusqueda, crGenerico);
						listaDetallesAcumulados = agruparOperaciones(resultado);
						this.generarReporteExcel(cycle);
						//parameters[0] = 
					}
					else
						msj = "No se encontraron datos con los Criterios de B\u00fasqueda especificados.";
				}
			}
		}
		else
		{
			msj = "Debe seleccionar un rango de fechas v\u00e1lidas.";
			setErrorRangoFechas(true);
		}
		
		if(StringUtils.isNotEmpty(msj))
			delegate.record(msj, null);
	}
	
	private Map agruparDetallesPorDeal(List resultadoBusqueda, String crParametro)
	{
		OperacionClienteBanorteCrDto dto = null, dtoCargo = null;
		Map resultado = new HashMap();
		Map dealsConCargo = new HashMap();
		Map operacionesAgrupadasCr = new TreeMap();
		List detalles = null;
		Integer crGenerico = null;
		Iterator it = null;
		Integer idDeal = null;
		int indice = 0;
		boolean actualizarDetalleCargo = false;
		
		try
		{
			crGenerico = new Integer(crParametro);
		}
		catch(Exception e)
		{
			System.out.println("El cr generico no es numerico");
			e.printStackTrace();
		}		
		
		for(indice = 0; indice < resultadoBusqueda.size(); indice++)
		{
			dto = (OperacionClienteBanorteCrDto)resultadoBusqueda.get(indice);
			
			// Si no es cargo a cuenta en pesos se agrega al reporte
			if(!"RMXNCARGIXEB".equals(dto.getMnemonico().toUpperCase().trim()))
			{
				if(resultado.containsKey(dto.getIdDeal()))
					((List)resultado.get(dto.getIdDeal())).add(dto);
				else
				{
					detalles = new ArrayList();
					detalles.add(dto);
					resultado.put(dto.getIdDeal(), detalles);
				}
			}
			
			// Si es cargo, solo necesito el sic de sc_cuenta_altamira y 
			// el numero de cuenta guardado en sc_plantilla_cuenta ixe 
			if("RMXNCARGIXEB".equals(dto.getMnemonico().toUpperCase().trim()) || 
			   "RUSDCARGIXEB".equals(dto.getMnemonico().toUpperCase().trim()))
			{
				if(!dealsConCargo.containsKey(dto.getIdDeal()))
					dealsConCargo.put(dto.getIdDeal(), dto);
			}
		}
		
		// Se actualizan los campos sic y numero de cuenta del detalle
		it = resultado.keySet().iterator();
		
		while(it.hasNext())
		{
			idDeal = (Integer)it.next();
			detalles = (List)resultado.get(idDeal);
			
			dtoCargo = null;
			actualizarDetalleCargo = false;

			if(dealsConCargo.containsKey(idDeal))
			{
				dtoCargo = (OperacionClienteBanorteCrDto)dealsConCargo.get(idDeal);
				actualizarDetalleCargo = true;
			}
			
			for(indice = 0; indice < detalles.size(); indice++)
			{
				dto = (OperacionClienteBanorteCrDto)detalles.get(indice);
				
				if(actualizarDetalleCargo)
				{
					dto.setSic(dtoCargo.getSic());
					dto.setCuentaCheques(dtoCargo.getCuentaCheques());
				}
				else if(crGenerico != null && dto.getSucursalOrigen().intValue() == crGenerico.intValue())
				{
					dto.setSic("");
					dto.setCuentaCheques("");
				}
				else
					; // El detalle está en un deal sin cargo a cuenta y el cr no es el generico
			}
		}
		
		return resultado;
	}
	
	
	private List agruparOperaciones(Map resultado)
	{
		Map operacionesAgrupadas = new HashMap();
		Map operacionesCr = new HashMap();
		List operaciones = null, operacionesAcumuladas = null, detallesDeal = null;
		StringBuffer key = null;
		OperacionClienteBanorteCrDto dto = null, nextDto;
		Iterator itKeys = null, itCr = null, it = null;
		Integer cr = null;
		Integer idDeal = null;
		
		it = resultado.keySet().iterator();
		while(it.hasNext())
		{
			idDeal = (Integer)it.next();
			detallesDeal = (List)resultado.get(idDeal);
			
			for(int indice = 0; indice < detallesDeal.size(); indice++)
			{
				dto = (OperacionClienteBanorteCrDto)detallesDeal.get(indice);
				key = new StringBuffer();
				key.append(dto.getSucursalOrigen()).
				    append("," + dto.getSic()).
				    append("," + dto.getContratoSica()).
				    append("," + dto.getCuentaCheques()).
				    append("," + dto.getCliente()).
				    append("," + dto.getPromotor()).
				    append("," + dto.getIdCanal());
				
				if(operacionesCr.containsKey(dto.getSucursalOrigen()))
					operacionesAgrupadas = (Map)operacionesCr.get(dto.getSucursalOrigen());
				else
				{
					operacionesAgrupadas = new HashMap();
					operacionesCr.put(dto.getSucursalOrigen(), operacionesAgrupadas);
				}
				
				if(!operacionesAgrupadas.containsKey(key.toString()))
				{
					operaciones = new ArrayList();
					operaciones.add(dto);
					operacionesAgrupadas.put(key.toString(), operaciones);
				}
				else
					((List)operacionesAgrupadas.get(key.toString())).add(dto);
			}
		}
		
		operacionesAcumuladas = new ArrayList();
		operacionesCr = new TreeMap(operacionesCr);
		itCr = operacionesCr.keySet().iterator();
		
		while(itCr.hasNext())
		{
			cr = (Integer)itCr.next();
			operacionesAgrupadas = (Map)operacionesCr.get(cr);
			itKeys = operacionesAgrupadas.keySet().iterator();
			
			while(itKeys.hasNext())
			{
				operaciones = (List)operacionesAgrupadas.get(itKeys.next());
				dto = (OperacionClienteBanorteCrDto)operaciones.get(0);
				dto.setUtilidadAcumulada(dto.getUtilidadDelDetalle());
				for(int indDto = 1; indDto < operaciones.size(); indDto++)
				{
					nextDto = (OperacionClienteBanorteCrDto)operaciones.get(indDto);
					dto.setUtilidadAcumulada(dto.getUtilidadAcumulada().add(nextDto.getUtilidadDelDetalle()));
				}
				
				operacionesAcumuladas.add(dto);
			}
		}
		
		return operacionesAcumuladas;
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
			response.setHeader("Content-disposition", "attachment; filename=\"ReporteClientesBanorteSica.xls\"");
			
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
	
	public JRDataSource getDataSource(String arg0) 
	{
		List dealDetallesMap = new ArrayList();
		for (Iterator it = listaDetallesAcumulados.iterator(); it.hasNext();) 
		{
			OperacionClienteBanorteCrDto dto = (OperacionClienteBanorteCrDto) it.next();
			HashMap map = new HashMap();
			map.put("contratoSica", dto.getContratoSica());
			map.put("cliente", dto.getCliente());
			map.put("promotor", dto.getPromotor());
			map.put("sic", StringUtils.isNotEmpty(dto.getSic())? new Integer(dto.getSic()) : null);
			map.put("sucursalOrigen", dto.getSucursalOrigen().toString());
			map.put("cuentaCheques", dto.getCuentaCheques());
			map.put("idCanal", dto.getIdCanal());
			map.put("utilidad", dto.getUtilidadAcumulada());
			dealDetallesMap.add(map);
		}
		return new ListDataSource(dealDetallesMap);
	}
	
	/**
	 * Fija el valor de la fecha inicial.
	 * 
	 * @param Date Fecha Inicial
	 */
	public abstract void setFechaInicial(Date fechaInicial);

	/**
	 * Regresa el valor de la fecha inicial.
	 * 
	 * @return Date.
	 */
	public abstract Date getFechaInicial();
	
	/**
	 * Fija el valor de la fecha final.
	 * 
	 * @param Date Fecha Final
	 */
	public abstract void setFechaFinal(Date fechaFinal);

	/**
	 * Regresa el valor de la fecha final.
	 * 
	 * @return Date.
	 */
	public abstract Date getFechaFinal();
	
	public abstract boolean getErrorRangoFechas();
	
	public abstract void setErrorRangoFechas(boolean errorRangoFechas);
	
	public abstract boolean getErrorFechaInicial();
	
	public abstract void setErrorFechaInicial(boolean errorFechaInicial);
}

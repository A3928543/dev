package com.ixe.ods.sica.pages.reportes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.model.ParametroSica;
//import com.ixe.ods.sica.model.RegulatorioDatosPM;
//import com.ixe.ods.sica.model.RegulatorioInstitucion;
import com.ixe.ods.sica.model.RegulatorioPerfil;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.sica.services.impl.GeneradorReportesRegulatoriosImpl;

public abstract class ReportesRegulatoriosBanxico extends SicaPage
{
	public final String DEFAULT = "SELECCIONE LA OPCION DESEADA";
	
	public final String REPORTE_I_PM = "SECCION I Personas Morales";
	public final String REPORTE_IV_PR = "SECCION IV Parte Relacionada";
	
	public final String FORMATO_CSV = "CSV";
	public final String FORMATO_EXCEL = "EXCEL(XLS)";
	
	public String reportes[] = new String[]{DEFAULT, REPORTE_I_PM, REPORTE_IV_PR};
	public String formatos[] = new String[]{DEFAULT, FORMATO_CSV, FORMATO_EXCEL};
	
	private final String CONTENT_TYPE_EXCEL = "application/vnd.ms-excel";
	private final String CONTENT_TYPE_CSV = "application/csv";
	
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		//setModo((String) cycle.getServiceParameters()[0]);
		setFecha(new Date());
		setErrorFecha(false);
		setErrorComboReporte(false);
		setErrorComboFormato(false);
		setReporte(DEFAULT);
		setFormato(DEFAULT);
	}
	
	private void validacionDeCampos(StringBuffer errores)
	{
		
		if(getReporte() == null || DEFAULT.equals(getReporte()))
		{
			setErrorComboReporte(true);
			errores.append("Debe seleccionar un opci\u00f3n del campo reporte. ");
		}
	
		if(getFormato() == null || DEFAULT.equals(getFormato()))
		{
			setErrorComboFormato(true);
			errores.append("Debe seleccionar un opci\u00f3n del campo formato. ");
		}
		
		if(getFecha() == null)
		{
			setErrorFecha(true);
			errores.append("Debe indicar una fecha v\u00e1lida. ");
		}
		else
		{
			if(DateUtils.inicioDia(getFecha()).after(DateUtils.inicioDia(new Date())))
			{
				setErrorFecha(true);
				errores.append("La fecha seleccionada no debe ser mayor al d\u00EDa en curso.");
			}
		}
	}
	
	public void reportes(IRequestCycle cycle) 
	{
		StringBuffer errores = new StringBuffer();
		StringBuffer datosReporte = null;
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
		Date fechaUsuario = null;
		Date fechaActual = null;
		RegulatorioPerfil perfil = null;
		List perfiles = null, contratosSica = null;
		List clientesFiltrados = null;
		GeneradorReportesRegulatoriosImpl generadorReportes = null;
		ByteArrayOutputStream outputStreamReporte = null;
		int opcionBusqueda = 0;
		String contentType = null, nombreReporte = null, extension = null;
		
		setErrorFecha(false);
		setErrorComboReporte(false);
		setErrorComboFormato(false);
		
		validacionDeCampos(errores);
		
		if(errores.length() == 0)
		{
			if(REPORTE_I_PM.equals(getReporte()))
			{
				opcionBusqueda = SicaServiceData.BUSCAR_REGULATORIOS_PM;
				nombreReporte = "ReporteSeccion1PersonasMorales";
			}
			else if(REPORTE_IV_PR.equals(getReporte()))
			{
				opcionBusqueda = SicaServiceData.BUSCAR_REGULATORIOS_PARTE_RELACIONADA;
				nombreReporte = "ReporteSeccion4ParteRelacionada";
			}
			
			if(FORMATO_EXCEL.equals(getFormato()))
			{
				extension = ".xls";  
				contentType = CONTENT_TYPE_EXCEL;
			}
			else
			{
				extension = ".csv";
				contentType = CONTENT_TYPE_CSV;
			}
			
			fechaUsuario = DateUtils.inicioDia(getFecha());
			fechaActual = DateUtils.inicioDia(new Date());
			
			if(fechaActual.compareTo(fechaUsuario) == 0) // si son iguales
			{
				perfiles = getSicaServiceData().criteriosInclusionDatosRegulatorios(opcionBusqueda);
				if(perfiles != null && perfiles.size() > 0)
				{
					System.out.println("perfiles: " + perfiles.size());
					contratosSica = new ArrayList();
					generadorReportes = new GeneradorReportesRegulatoriosImpl();
					
					for(int indice = 0; indice < perfiles.size(); indice++)
					{
						perfil = (RegulatorioPerfil)perfiles.get(indice);
						contratosSica.add(perfil.getContratoSica());
						System.out.println("contratosSica: " + perfil.getContratoSica());
					}
					
					clientesFiltrados = getSicaServiceData().consultarDatosRegulatorios(opcionBusqueda, contratosSica);
					System.out.println("clientesFiltrados.size(): " + clientesFiltrados.size());
					datosReporte = getSicaServiceData().prepararDatosReporte(opcionBusqueda, clientesFiltrados, errores);
					
					if(datosReporte == null || (datosReporte.length() == 0 && errores.length() > 0))
					{
						delegate.record(errores.toString(), null);
						return;
					}
					
					outputStreamReporte = new ByteArrayOutputStream();
					
					try
					{	
						if(FORMATO_EXCEL.equals(getFormato()))
							generadorReportes.escribirXls(outputStreamReporte, datosReporte, opcionBusqueda);
						else
							generadorReportes.excribirCSV(outputStreamReporte, datosReporte);
						
						descargarReporte(cycle, outputStreamReporte, contentType, nombreReporte + extension, opcionBusqueda);
						
						setErrorFecha(false);
						setErrorComboReporte(false);
						setErrorComboFormato(false);
						
						if(errores.length() > 0)
						{
							warn(errores.toString());
							delegate.record(errores.toString(), null);
						}
					}
					catch(IOException e)
					{
						e.printStackTrace();
						delegate.record("Ha ocurrido un error al generar el reporte regulatorio. " +
								        "Por favor, comunique el incidente al \u00e1rea de sistemas.", null);
						return;
					}
				}
				else
					delegate.record("El reporte no puede generarse porque la informaci\u00f3n regulatoria " +
							        "de los clientes no ha sido modificada o tienen su contrato bloqueado.", null);
			}
			else // buscar el reporte en el repositorio
			{
				ParametroSica param = getSicaServiceData().findParametro(ParametroSica.RUTA_REPORTES_REGULATORIOS);
				File path = null;
				File fileReporte = null;
				FileInputStream fis = null;
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				byte bytesReporte[] = null;
				
				if(param != null && StringUtils.isNotEmpty(param.getValor()))
				{
					path = new File(param.getValor());
					if(path.exists() && path.canRead())
					{	
						nombreReporte += "-" + dateFormat.format(getFecha()) + extension;
						
						try
						{
							fileReporte = new File(path.getAbsolutePath() + File.separator + nombreReporte);
							if(fileReporte.exists() && fileReporte.canRead())
							{
								bytesReporte = new byte[(int)fileReporte.length()];
								fis = new FileInputStream(fileReporte);
								fis.read(bytesReporte);
								fis.close();
								fis = null;
								
								outputStreamReporte = new ByteArrayOutputStream();
								outputStreamReporte.write(bytesReporte);
								
								descargarReporte(cycle, outputStreamReporte, contentType, nombreReporte, opcionBusqueda);
							}
							else
							{
								delegate.record("No existe el reporte de backup para la fecha solicitada: " + dateFormat.format(getFecha()), null);
								return;
							}
						}
						catch (IOException e) 
						{
							e.printStackTrace();
							delegate.record("Ha ocurrido un error al realizar la lectura del archivo: " + nombreReporte, null);
							return;
						}
						
					}
					else
					{
						delegate.record("No es posible descargar el reporte solicitado porque no existe el directorio" +
						        " de backup de los reportes regulatorios y/o no se tiene permisos de lectura.", null);
						return;
					}
				}
				else
				{
					delegate.record("No es posible descargar el reporte solicitado porque no se encuentra configurado el directorio" +
							        " de backup de los reportes regulatorios.", null);
					return;
				}
			}
		}
		else
		{
			if(StringUtils.isNotEmpty(errores.toString()))
				delegate.record(errores.toString(), null);
		}
	}
	
	/*private StringBuffer prepararDatosReporte(int opcionBusqueda, List registros, StringBuffer errores)
	{
		StringBuffer datos = new StringBuffer();
		RegulatorioDatosPM datosPersonaMoral = null;
		RegulatorioInstitucion datosParteRelacionada = null;
		SimpleDateFormat dateFormatRegulatorio = new SimpleDateFormat("yyyy/MM/dd");
		List resultado = null;
		
		for(int indice = 0; indice < registros.size(); indice++)
		{
			if(opcionBusqueda == SicaServiceData.BUSCAR_REGULATORIOS_PM)
			{
				datosPersonaMoral = (RegulatorioDatosPM)registros.get(indice);
				datos.append((StringUtils.isNotEmpty(datosPersonaMoral.getClaveBanxico()) ? datosPersonaMoral.getClaveBanxico() : "EMPTY") + ";");
				datos.append(datosPersonaMoral.getNombreContraparte() + ";");
				datos.append(datosPersonaMoral.getSociedadMercantil().getId().intValue() + ";");
				datos.append(datosPersonaMoral.getTipoIdentificador().getId().intValue() + ";");
				datos.append(datosPersonaMoral.getClaveIdentificadorContraparte() + ";");
				datos.append(datosPersonaMoral.getActividadEconomica().getId().intValue() + ";");
				datos.append(datosPersonaMoral.getClaveLei() + ";");
				datos.append(datosPersonaMoral.getSectorIndustrial().getId().intValue() + ";");
				datos.append(datosPersonaMoral.getPaisControlContraparte().getClaveIso() + ";");
				datos.append(datosPersonaMoral.getPaisResidenciaContraparte().getClaveIso() + ";");
				datos.append(datosPersonaMoral.getClaveLeiMatrizDirecta() + ";");
				datos.append(datosPersonaMoral.getClaveLeiMatrizUltimaInstancia() + ";");
				datos.append(dateFormatRegulatorio.format(datosPersonaMoral.getFechaContraparte()));
			}
			else if(opcionBusqueda == SicaServiceData.BUSCAR_REGULATORIOS_PARTE_RELACIONADA)
			{
				datosParteRelacionada = (RegulatorioInstitucion)registros.get(indice);
				System.out.println("datosParteRelacionada is null: " + (datosParteRelacionada == null));
				resultado = getSicaServiceData().findRegulatorioDatosPM(datosParteRelacionada.getIdPersona());
				System.out.println("resultado is null: " + (resultado == null));
				System.out.println("resultado size: " + (resultado != null ? resultado.size() : -1));
				if(resultado != null && resultado.size() > 0)
				{
					datosPersonaMoral = (RegulatorioDatosPM)resultado.get(0);
					if(StringUtils.isNotEmpty(datosPersonaMoral.getClaveBanxico()))
					{
						datos.append(datosPersonaMoral.getClaveBanxico() + ";");
						datos.append(datosParteRelacionada.getTipoRelacion().getId().intValue() + ";");
						datos.append(datosParteRelacionada.getPerteneceGrupoFinanciero().intValue() + ";");
						if(datosParteRelacionada.getTipoRelacion().getId().intValue() != 10) // la contraparte si está relacionada
						{
							datos.append(datosParteRelacionada.getEventoRelacion().getId().intValue() + ";");
							datos.append(dateFormatRegulatorio.format(datosParteRelacionada.getFechaEvento()) + ";");
						}
						else
						{
							datos.append("0;");    // la contraparte no está relacionada, no hay evento de relación
							datos.append("EMPTY"); // no hay fecha de relación
						}
					}
					else
					{
						errores.append("La contraparte " + datosPersonaMoral.getNombreContraparte() 
								       + " no fue agregada al reporte 'SECCION 4 Parte Relacionada' porque no tiene clave BANXICO");
					}
				}
			}
			
			if(indice < (registros.size() - 1))
				datos.append("|");
		}
		
		System.out.println("datos reporte: " + (datos != null ? datos.toString() : "no hay datos.."));
		return datos;
	}*/
	
	private void descargarReporte(IRequestCycle cycle, ByteArrayOutputStream outputStreamReporte, String contentType, String nombreReporte, int opcionBusqueda) 
	{	
		try 
		{
			HttpServletResponse response = cycle.getRequestContext().getResponse();
			
			byte[] bytes2 = outputStreamReporte.toByteArray();
			response.setContentType(contentType);
			response.setContentLength(bytes2.length);
			response.setHeader("Content-disposition", "attachment; filename=\"" + nombreReporte + "\"");
			
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
	
	/**
     * Popula el combo reportes.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getComboReportes() {
		return new StringPropertySelectionModel(reportes);
	}
	
	/**
     * Popula el combo formatos.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getComboFormatos() {
		return new StringPropertySelectionModel(formatos);
	}
	
	/**
	 * Fija el valor del reporte.
	 * 
	 * @param String reporte
	 */
	public abstract void setReporte(String reporte);

	/**
	 * Regresa el valor del reporte.
	 * 
	 * @return String.
	 */
	public abstract String getReporte();
	
	/**
	 * Fija el valor del formato reporte.
	 * 
	 * @param String formato
	 */
	public abstract void setFormato(String formato);

	/**
	 * Regresa el valor del formato del reporte.
	 * 
	 * @return String.
	 */
	public abstract String getFormato();
	
	/**
	 * Fija el valor de la fecha del reporte.
	 * 
	 * @param Date Fecha
	 */
	public abstract void setFecha(Date fecha);

	/**
	 * Regresa el valor de la fecha del reporte.
	 * 
	 * @return Date.
	 */
	public abstract Date getFecha();
	
	public abstract boolean getErrorFecha();	
	public abstract void setErrorFecha(boolean errorRangoFechas);
	
	public abstract boolean getErrorComboReporte();
	public abstract void setErrorComboReporte(boolean errorComboReporte);
	
	public abstract boolean getErrorComboFormato();	 
	public abstract void setErrorComboFormato(boolean errorComboReporte);
}

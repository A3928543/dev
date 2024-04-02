package com.ixe.ods.sica.pages.autorizatce;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.springframework.dao.DataAccessException;

import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Keys;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.dao.DealDao;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.dto.FiltroDealsTceDto;
import com.ixe.ods.sica.utils.MapUtils;
import com.ixe.ods.tapestry.BaseGlobal;

/**
 * Pantalla de consulta de deals con tipo de cambio especial
 * 
 * @author Cesar Jeronimo Gomez
 */
public abstract class ConsultaAutorizaTce extends SicaPage implements IExternalPage {
	
	/*---------------------------------------------------------------------------------------------------------
     * Metodos sobreescritos/implementados
     *---------------------------------------------------------------------------------------------------------*/
	
	/**
     * Regresa el arreglo con los estados de operaci&oacute;n normal y operaci&oacute;n restringida.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[] {Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
    		Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_OPERACION_NOCTURNA,
    		Estado.ESTADO_GENERACION_CONTABLE, Estado.ESTADO_FIN_LIQUIDACIONES};
    }

    /**
     * 
     */
    public void activate(IRequestCycle cycle) {
    	super.activate(cycle);
    	initFields();
    }

	/**
	 * 
	 */
	public void activateExternalPage(Object[] params, IRequestCycle cycle) {
		try {
			super.activate(cycle);
			initFields();
		} catch (SicaException e) {
			redirectToMessage(cycle, e.getMessage());
		}
	}
	
	/*---------------------------------------------------------------------------------------------------------
     * Listeners
     *---------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Listener que recibe los submits del form
	 * 
	 * @param cycle
	 */
	public void submit(IRequestCycle cycle) {
		debug("listener:submit ...");
		try {
			debug("getModoSubmit()=" + getModoSubmit() );
			debug("Datos capturados: " + getFiltroDealsTce());
			findDealsTce();
			setModoSubmit(MODO_SUBMIT_CONSULTAR);
		} catch (SicaException e) {
			getDelegate().record(e.getMessage(), null);
		}
	}
	
	/**
	 * Listener para cancelar un deal
	 * 
	 * @param cycle
	 */
	public void cancelarDeal(IRequestCycle cycle) {
		debug("listener: cancelarDeal ...");
		try {
			Object[] parameters = cycle.getServiceParameters();
			debug("cancelarDeal - parametros: " + parameters);
			
			if(parameters.length > 0) {
				String strIdDeal = parameters[0].toString();
				debug("cancelarDeal - Deal recibido: " + strIdDeal);
				if(NumberUtils.isDigits(strIdDeal)) {
					Deal foundDeal = secureFindDeal(strIdDeal);
					debug("cancelarDeal - Deal encontrado: " + foundDeal.getIdDeal());
					
					if(!Deal.STATUS_ESPECIAL_TC_AUTORIZADO.equals(foundDeal.getEstatusEspecial())) {
						throw new SicaException("El deal no se puede cancelar por que ya ha sido tomado por el promotor");
					}
					if(Deal.STATUS_DEAL_CANCELADO.equals(foundDeal.getStatusDeal())) {
						throw new SicaException("El deal no se puede cancelar por que ya ha sido cancelado");
					}
					
					int idDeal = Integer.parseInt(strIdDeal);
					cancelarDealSafely(idDeal);
					debug("cancelarDeal - deal cancelado");
					debug("cancelarDeal - Filtro deals: " + getFiltroDealsTce());
					findDealsTce(); // Se refresca la lista de deals para mostrar el cambio en el status
				} else {
					throw new SicaException("No se recibi\u00F3 el n\u00FAmero de deal");
				}
			} else {
				throw new SicaException("No se recibi\u00F3 el n\u00FAmero de deal");
			}
		} catch (SicaException e) {
			getDelegate().record(e.getMessage(), null);
		}
	}
	
	/**
	 * Listener para genera el reporte excel
	 * 
	 * @param cycle
	 */
	public void generarReporteExcel(IRequestCycle cycle) {
		try {
			if(getDeals() != null && !getDeals().isEmpty()) {
				long elapsedTime = new Date().getTime() - getLastSuccessQueryTime().getTime();
				if( elapsedTime > VER_EXCEL_TIMEOUT ) {
					getDelegate().record("El reporte se genera con los resultados de la \u00FAltima consulta, " +
						"la cual se ejecut\u00F3 hace " + (elapsedTime / 1000) + " segundos, " +
						"vuelva a consultar para disponer nuevamente del reporte, cuyo tiempo l\u00EDmite para su descarga es de " 
						+ (VER_EXCEL_TIMEOUT / 1000) + " segundos.", null);
					setDeals(new ArrayList());
				} else {
					generarReporteExcel(cycle, getDeals());
				}
			} else {
				getDelegate().record("No hay datos para generar el reporte, vuelva a consultar", null);
			}
		} catch (SicaException e) {
			getDelegate().record(e.getMessage(), null);
		}
	}
	
	/*---------------------------------------------------------------------------------------------------------
     * Models para los DropDown
     *---------------------------------------------------------------------------------------------------------*/
	
	/**
     * Modelo del campo capturado por
     * 
     * @return
     */
    public IPropertySelectionModel getCapturadoPorModel() {
		return new MapSelectionModel(getCatalogoCapturadoPor());
    }
    
    /**
     * Modelo para el campo estatus
     * 
     * @return
     */
    public IPropertySelectionModel getEstatusModel() {
		return new MapSelectionModel(getCatalogoStatus());
    }
	
	/*---------------------------------------------------------------------------------------------------------
     * Metodos auxiliares
     *---------------------------------------------------------------------------------------------------------*/
    
    /**
	 * Metodo auxiliar para generar el reporte excel
	 * 
	 * @param cycle
	 * @param deals
	 */
	private void generarReporteExcel(IRequestCycle cycle, List deals) {
		InputStream masterReportIs= null;
		InputStream subreportIs = null;
		ServletOutputStream ouputStream = null;
		ByteArrayOutputStream xlsReportBaos = null;
		try {
			// Se obtienen las referencias al reporte y al subreporte
			BaseGlobal global = (BaseGlobal) getGlobal();
			masterReportIs = global.getApplicationContext().getResource("WEB-INF/jasper/ConsultaAutorizaTce.jasper").getInputStream();
			subreportIs = global.getApplicationContext().getResource("WEB-INF/jasper/ConsultaAutorizaTceDetail.jasper").getInputStream();
			JasperReport masterReportJr = (JasperReport) JRLoader.loadObject(masterReportIs);
			JasperReport subreportJr = (JasperReport) JRLoader.loadObject(subreportIs);
			
			// Llenado del reporte
			Map params = new HashMap();
			params.put("paramDetalleSubreport", subreportJr);
			JRDataSource dataSource = new JRMapCollectionDataSource(deals);
			JasperPrint jasperPrint = JasperFillManager.fillReport(masterReportJr, params, dataSource);
			
			// Exportacion del reporte a Excel
			JRXlsExporter exporter = new JRXlsExporter();
			xlsReportBaos = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReportBaos);
			exporter.exportReport();
			
			// Envio del reporte por el outputstream del servlet
			HttpServletResponse response = cycle.getRequestContext().getResponse();
			byte[] bytes2 = xlsReportBaos.toByteArray();
			response.setContentType("application/vnd.ms-excel");
			response.setContentLength(bytes2.length);
			response.setHeader("Content-disposition", "attachment; filename=\""	+ REPORT_NAME + ".xls\"");
			ouputStream = response.getOutputStream();
			ouputStream.write(bytes2, 0, bytes2.length);
			ouputStream.flush();
			ouputStream.close();
		} catch (JRException e) {
			error("Error al generar el reporte", e);
			throw new SicaException("Error al generar el reporte");
		} catch (IOException e) {
			error("Errro al generar el reporte", e);
			throw new SicaException("Error al generar el reporte");
		} finally {
			closeQuietly(ouputStream);
			closeQuietly(xlsReportBaos);
			closeQuietly(subreportIs);
			closeQuietly(masterReportIs);
		}
	}
	
	/**
	 * Cierra un inputStream sin reportar excepciones
	 * 
	 * @param inputStream
	 */
	private void closeQuietly(InputStream inputStream) {
		if(inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				; // do nothing
			}
		}
	}
	
	/**
	 * Cierra un outputStream sin reportar excepciones
	 * 
	 * @param outputStream
	 */
	private void closeQuietly(OutputStream outputStream) {
		if(outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				; // do nothing
			}
		}
	}
    
    /**
     * Busca el numero de deal proporcionado
     * 
     * @param dealNumber
     * @return
     */
    protected Deal secureFindDeal(String dealNumber) {
    	try {
			if( !StringUtils.isEmpty(dealNumber) && NumberUtils.isDigits(dealNumber) ) {
				Deal deal = getSicaServiceData().findDeal(Integer.parseInt(dealNumber));
				if(deal == null) {
					throw new SicaException("Deal no encontrado: " + dealNumber);
				}
				auditar( new Integer( dealNumber ), LogAuditoria.CONSULTA_DATOS_DEAL + 
						"-BITACORA", null, "INFO", "E");
				return deal;
			} else {
				throw new SicaException("El deal " + dealNumber + " no es valido");
			}
		} catch (DataAccessException e) {
			throw new SicaException("Error al consultar informacion del deal " + dealNumber);
		}
    }
    
    /**
	 * Cancela un deal, cachando las excepciones posibles
	 * 
	 * @param idDeal
	 */
	private void cancelarDealSafely(int idDeal) {
		try {
			getWorkFlowServiceData().wfCancelarDirectamenteDeal(getTicket(), idDeal, ((Visit) getVisit()).getUser().getIdUsuario());
		} catch (RuntimeException e) {
			throw new SicaException("Error al cancelar deal: " + e.getMessage());
		}
	}
    
    /**
     * Determina si quien captura es mesa de cambios
     * @return
     */
    public boolean isMesaCambios() {
    	Visit visit = (Visit)getVisit();
    	return AutorizaTceHelper.isMesaCambios(visit);
    }
    
    /**
     * Determina si el usuario es promocion
     * @return
     */
    protected boolean isPromocion() {
    	Visit visit = (Visit)getVisit();
    	return AutorizaTceHelper.isPromocion(visit);
    }
    
    /**
     * Inicializa los valores de los campos
     */
    public void initFields() {
    	setModoSubmit(MODO_SUBMIT_CONSULTAR);
    	
    	// Inicializa los catalogos
		setCatalogoCapturadoPor(generaCatalogoCapturadoPor());
		setCatalogoStatus(generaCatalogoStatus());
		
    	FiltroDealsTceDto filtroDealsTce = null;
    	// Si es mesa cambios el campo capturado por se setea a mesa de cambios
    	if(isMesaCambios()) {
    		filtroDealsTce = new FiltroDealsTceDto(FiltroDealsTceDto.CAPTURADO_POR_MESA_CAMBIOS, "", "");
    	// si es promocion por default el filtro capturado por es seteado a promocion y se establece el id persona del promotor en el filtro
    	// para cuando se haga la consulta, se arroje solo las operaciones a nombre del promotor especificado
    	} else if (isPromocion()) {
    		filtroDealsTce = new FiltroDealsTceDto(FiltroDealsTceDto.CAPTURADO_POR_PROMOTOR, "", FiltroDealsTceDto.ESTATUS_PENDIENTE);
    		IUsuario iUsuario = AutorizaTceHelper.getUsuarioActualConPersona((Visit)getVisit());
    		if(iUsuario == null || iUsuario.getIdPersona() == null) throw new SicaException("El usuario de sesion es nulo");
    		debug("initFields - es promocion, id persona promotor: " + iUsuario.getIdPersona());
    		filtroDealsTce.setIdPersonaPromotor(iUsuario.getIdPersona().intValue());
    	}
    	setFiltroDealsTce(filtroDealsTce);
		setDeals(new ArrayList());
    }
    
    /**
     * Valida los filtros capturados
     */
    private boolean sonCamposValidos(FiltroDealsTceDto filtroDealsTce) {
    	if( filtroDealsTce.isCapturadoPorPromotor() && filtroDealsTce.isEstatusAutorizadoTce()) {
    		getDelegate().record("Las operaciones capturadas por un promotor, nos pueden tener estatus Autorizado TC Especial", null);
    		return false;
    	}
    	else
    	{
    		if(filtroDealsTce.getIdDeal()!=null & !filtroDealsTce.getIdDeal().trim().matches("[-0-9]*"))
    		{
    			String msjError = "El Deal debe de ser num\u00e9rico. ";
    			getDelegate().record(msjError, null);
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * Valida los filtros y ejecuta la consulta
     */
    private void findDealsTce() {
    	try {
    		List deals;//Hamue
    		if(sonCamposValidos(getFiltroDealsTce())) {
	    		//List deals = getDealDao().findDealsTce(getFiltroDealsTce()); 
	    		deals = getDealDao().findDealsTce(getFiltroDealsTce());
	    		
	    		if(deals == null || deals.isEmpty()) {
	    			getDelegate().record("No se encontraron deals mediante los filtros proporcionados", null);
	    			setDeals(new ArrayList());
	    		} else {
	    			// Seteando descripcion de los status y detalles para mostrar
	    	        for (Iterator it = deals.iterator(); it.hasNext();) {
	    	            Map map = (Map) it.next();
	    	            map.put("descripcionStatus", Deal.getDescripcionStatus(
		                    (String) map.get(Keys.STATUS_DEAL),
		                    ((Integer) map.get(Keys.REVERSADO)).intValue()));
	    	            
	    	            map.put("detalles", obtenerDetallesParaMostrar(map));
	    	        }
	    			setDeals( deals );
	    			// Al encontrar deals con exito, se setea el momento en que sucedio
	    			setLastSuccessQueryTime(new Date()); 
	    		}
    		}
    		else//Hamue
    		{
//    			deals=null;//Hamue
    			setDeals(new ArrayList());
    		}//Hamue
    		auditar( new Integer( 0 ), LogAuditoria.CONSULTA_DATOS_DEAL, "BITACORA", "INFO", "E");
		} catch (DataAccessException e) {
			error("Error al hacer la consulta de deals TCE", e);
			throw new SicaException("Error al hacer la consulta de deals TCE");
		}
    }
    
    /**
     * Genera una nueva lista de detalles para mostrar
     * 
     * @param deal
     * @return
     */
    public List obtenerDetallesParaMostrar(Map deal) {
    	List detallesAMostrar = new ArrayList();
    	Map curDet = null;
    	List detalles = (List) deal.get("detalles");
    	if(detalles == null) return new ArrayList();
    	for(Iterator it = detalles.iterator(); it.hasNext();) {
    		curDet = (Map) it.next();
    		if(mostrarDetalle(curDet, deal)) {
    			detallesAMostrar.add(curDet);
    		}
    	}
    	return detallesAMostrar;
    }
    
    /**
     * Regresa true si el detalle debe mostrarse o no en la pantalla.
     *
     * @param det El detalle.
     * @param deal El encabezado de deal.
     * @return boolean.
     */
    public boolean mostrarDetalle(Map det, Map deal) {
        if (det.get(Keys.CLAVE_FORMA_LIQUIDACION) != null) {
            if ((Deal.STATUS_DEAL_CANCELADO.equals(deal.get(Keys.STATUS_DEAL)) &&
                    DealDetalle.STATUS_DET_CANCELADO.equals(det.get(Keys.STATUS_DETALLE_DEAL))) ||
                    !DealDetalle.STATUS_DET_CANCELADO.equals(det.get(Keys.STATUS_DETALLE_DEAL))) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Regresa true si el estado actual es Operaci&oacute;n Normal u Operaci&oacute;n Restringida.
     *
     * @return boolean.
     */
    public boolean isVerHabilitado() {
    	return true;
    }
    
	/**
     * Regresa la referencia al bean <code>dealDao</code>.
     *
     * @return DealDao.
     */
    public DealDao getDealDao() {
        return (DealDao) getApplicationContext().getBean("dealDao");
    }
    
    /**
	 * Redirecciona a una pagina de mensaje de error en caso de haber alguno
	 * 
	 * @param cycle
	 * @param message
	 */
	protected void redirectToMessage(IRequestCycle cycle, String message) {
		AutorizaTceHelper.redirectToMessageWithoutMenu(cycle, message);
	}
	
	/**
	 * Construye el catalogo para el campo capturado por
	 * 
	 * @return El catalogo
	 */
	private List generaCatalogoCapturadoPor() {
		List catalogo = new ArrayList();
		catalogo.add( MapUtils.generar(MapSelectionModel.VALUE_LABEL_MAP_KEYS, 
			new Object[]{"", Constantes.TODOS}) );
		catalogo.add( MapUtils.generar(MapSelectionModel.VALUE_LABEL_MAP_KEYS, 
			new Object[]{FiltroDealsTceDto.CAPTURADO_POR_MESA_CAMBIOS, "Mesa de Cambios"}) );
		catalogo.add( MapUtils.generar(MapSelectionModel.VALUE_LABEL_MAP_KEYS, 
			new Object[]{FiltroDealsTceDto.CAPTURADO_POR_PROMOTOR, "Promotor"}) );
		return catalogo;
	}
	
	/**
	 * Construye el catalogo para el campo capturado por
	 * 
	 * @return
	 */
	private List generaCatalogoStatus() {
//		List catalogo = new ArrayList();
//		catalogo.add( MapUtils.generar(MapSelectionModel.VALUE_LABEL_MAP_KEYS, 
//			new Object[]{"", Constantes.TODOS}) );
//		catalogo.add( MapUtils.generar(MapSelectionModel.VALUE_LABEL_MAP_KEYS, 
//			new Object[]{FiltroDealsTceDto.ESTATUS_AUTORIZADA_TCE, "Autorizadas TC Especial"}) );
//		catalogo.add( MapUtils.generar(MapSelectionModel.VALUE_LABEL_MAP_KEYS, 
//			new Object[]{FiltroDealsTceDto.ESTATUS_PENDIENTE, "Pendientes"}) );
//		return catalogo;
		
		
		
		
		List catalogo = new ArrayList();
		catalogo.add( MapUtils.generar(MapSelectionModel.VALUE_LABEL_MAP_KEYS, 
				new Object[]{FiltroDealsTceDto.ESTATUS_PENDIENTE, "Pendientes"}) );
			
		catalogo.add( MapUtils.generar(MapSelectionModel.VALUE_LABEL_MAP_KEYS, 
			new Object[]{"", Constantes.TODOS}) );
		catalogo.add( MapUtils.generar(MapSelectionModel.VALUE_LABEL_MAP_KEYS, 
			new Object[]{FiltroDealsTceDto.ESTATUS_AUTORIZADA_TCE, "Autorizadas TC Especial"}) );
		return catalogo;
	}
	
	 /*---------------------------------------------------------------------------------------------------------
     * Campos para la pantalla de tapestry
     *---------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Retorna el objeto que almacena los filtros capturados
	 * @return
	 */
	public abstract FiltroDealsTceDto getFiltroDealsTce();
	
	/**
	 * Establece el objeto que almacena los filtros capturados
	 * @param filtroDealsTce
	 */
	public abstract void setFiltroDealsTce(FiltroDealsTceDto filtroDealsTce);
	
	/**
     * Regresa la lista de deals encontrados.
     *
     * @return List.
     */
    public abstract List getDeals();

    /**
     * Establece la lista de deals encontrados.
     *
     * @param deals El valor a asignar.
     */
    public abstract void setDeals(List deals);
	
	/*---------------------------------------------------------------------------------------------------------
     * Otros campos/propiedades
     *---------------------------------------------------------------------------------------------------------*/
	
	/** Obtiene el modo en el que se interpreta un submit {@link #MODO_SUBMIT_SAVE} o {@link #MODO_SUBMIT_UPDATE_TC} */
	public abstract int getModoSubmit();
	
	/** Establece el modo en el que se interpreta un submit */
	public abstract void setModoSubmit(int modoSubmit);
	
	/**
     * Establece el Modo de Operaci&oacute;n de la P&aacute;gina:
     * Promoci&oacute;n u Otro.
     *
     * @param modo El Modo de Operaci&oacute;n.
     */
    public abstract void setModo(String modo);

    /**
     * Obtiene el Modo de Operaci&oacute;n de la P&aacute;gina.
     *
     * @return String el Modo de Operaci&oacute;n de la P&aacute;gina.
     */
    public abstract String getModo();
    
    /**
     * Obtiene el momento en el que se obtuvieron los ultimos resultados de una consulta con exito
     * 
     * @return
     */
    public abstract Date getLastSuccessQueryTime();
    
    /**
     * Obtiene el momento en el que se obtuvieron los ultimos resultados de una consulta con exito
     * 
     * @param lastSuccessQueryTime
     */
    public abstract void setLastSuccessQueryTime(Date lastSuccessQueryTime);
	
	/*---------------------------------------------------------------------------------------------------------
     * Listas para los catalogos
     *---------------------------------------------------------------------------------------------------------*/
	
	/** Regresa la lista de opciones a seleccionar para el filtro capturado por */
	public abstract List getCatalogoCapturadoPor();
	
	/** Establece la lista de opciones a seleccionar para el filtro capturado por */
	public abstract void setCatalogoCapturadoPor(List catalogoCapturadoPor);
	
	/** Regresa la lista de opciones a seleccionar para el filtro de estatus */
	public abstract List getCatalogoStatus();
	
	/** Establece la lista de opciones a seleccionar para el filtro de estatus */
	public abstract void setCatalogoStatus(List catalogoStatus);
	
	/*---------------------------------------------------------------------------------------------------------
	 * MODOS SUBMIT: Modos en los que se interpreta un submit para esta pantalla
	 *---------------------------------------------------------------------------------------------------------*/
	
	/** Modo submit para indicar que se debe mostrar los resultados de la consulta para los filtros dados */
	public static final int MODO_SUBMIT_CONSULTAR = 10;
	
	/** Modod submit para inidicar que se debe generar el reporte en excel */
	public static final int MODO_SUBMIT_VER_EXCEL = 20;
	
	/** Nombre del reporte excel a generar */
	public static final String REPORT_NAME = "ReporteConsultaTce";
	
	/** Time out para generar el excel despues de una consulta exitosa */
	public static final long VER_EXCEL_TIMEOUT = 20 * 1000;

}

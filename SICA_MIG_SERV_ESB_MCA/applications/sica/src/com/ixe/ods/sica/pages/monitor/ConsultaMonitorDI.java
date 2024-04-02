package com.ixe.ods.sica.pages.monitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.ixe.ods.sica.LlavesMapas;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sicamurex.dto.MonitorDIDTO;
import com.ixe.ods.sica.sicamurex.service.SicaMurexService;
import com.ixe.ods.sica.sicamurex.service.SicaMurexServiceImpl;
import com.ixe.ods.sica.sicamurex.utils.ConstantesSICAMUREX;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * 
 * @author JDCH
 * 
 **/

public abstract class ConsultaMonitorDI extends SicaPage{

public static final Logger LOGGER = Logger.getLogger(ConsultaMonitorDI.class);
	
	
	
    /**
     * Activa el Modo de Operaci&oacute; as&iacute; como el T&iacute;tulo del Portlet de
     * B&uacute;squedas de la P&aacute;gina cada que esta se activa.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
	    super.activate(cycle);
	    setFechaSeleccionada(null);
	    setTituloActionMC("Consulta de deals interbancarios"); 
    }
    

    /**
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void fetch(IRequestCycle cycle) {
    	LOGGER.info("Iniciando Consulta de Deals Interbancarios del día" + new Date());
    	List detallesCorteList = new ArrayList();
        try {
        	Date fecha = getFechaSeleccionada();
        	Divisa divisa = getDivisaSeleccionada();
        	LOGGER.info("Fecha Seleccionada" + fecha);
        	LOGGER.info("Divisa Seleccionada" + getDivisaSeleccionada().getDescripcion());
        	
        	detallesCorteList = getSicaMurexService().findDatosMonitorDI(fecha,divisa);
        	
        	if(detallesCorteList!= null && detallesCorteList.size() > 0){
        		setCorteDetalleList(detallesCorteList);
        	}else{
        		throw new SicaException("No se encontraron deals interbancarios para la fecha especificada");
        	}        	
        }
        catch (SicaException e) {
            debug(e);
            getDelegate().record(e.getMessage(), null);
        }
    }
    
    
    /**
     * Regresa un RecordSelectionModel con todas las divisas.
     *
     * @return IPropertySelectionModel
     */
    public IPropertySelectionModel getDivisasModel() {
        List divisas = getSicaServiceData().findDivisasFrecuentes();
        Divisa primerElemento = new Divisa();
        primerElemento.setIdDivisa(ConstantesSICAMUREX.ID_CERO);
        primerElemento.setDescripcion(ConstantesSICAMUREX.TODAS_LAS_DIVISAS);
        divisas.add(0, primerElemento);
        return new RecordSelectionModel(divisas, LlavesMapas.ID_DIVISA, "descripcion");
    }
    
    
	public void generarExcel(IRequestCycle cycle){
	
		StringBuffer buffer = new StringBuffer();
		List corteDetList = getCorteDetalleList();
        generarRegistrosArchivoReporte(buffer,corteDetList);
        _logger.debug("Reporte armado: "+ buffer.toString());
        
        HttpServletResponse response = cycle.getRequestContext().getResponse();

        byte[] result = String.valueOf(buffer).getBytes();
        response.setContentType("text/csv");            
        response.setContentLength(result.length);
        response.setHeader("Content-disposition", "attachment; filename=\"" + "MonitorDI-"+ new Date() + ".csv\"");
        try {
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(result, 0, result.length);
			ouputStream.flush();
			ouputStream.close();
			
		} catch (IOException ioex) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(ioex);
			}
			System.err.println(ioex.getMessage());
			ioex.printStackTrace();
		}
	}

    
    private void generarRegistrosArchivoReporte(StringBuffer buffer,
			List corteDetList) {
    	generarEncabezadoCortes(buffer);
    	if(corteDetList!= null && corteDetList.size() > 0 ){
	    	for (Iterator iterator = corteDetList.iterator(); iterator.hasNext();) {
				MonitorDIDTO cd = (MonitorDIDTO) iterator.next();
				buffer.append(cd.getIdCorte())
					  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
					  .append(cd.getFechaUltMod())
					  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
					  .append(cd.getDivisa())
					  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
					  .append(cd.getFechaValor())
					  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
					  .append(cd.getOperacion())
					  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
					  .append(cd.getMontoDivisa())
					  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
					  .append(cd.getTc())
					  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
					  .append(cd.getIdDeal())
					  .append(ConstantesSICAMUREX.SALTO_LINEA);
			}
    	}
	}


	private void generarEncabezadoCortes(StringBuffer buffer) {
		
		buffer.append(ConstantesSICAMUREX.CORTE_HEADER)
			  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
			  .append(ConstantesSICAMUREX.FECHA_HEADER)
			  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
			  .append(ConstantesSICAMUREX.DIVISA_HEADER)
			  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
			  .append(ConstantesSICAMUREX.FV_HEADER)
			  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
			  .append(ConstantesSICAMUREX.TIPO_OPERACION_HEADER)
			  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
			  //.append(ConstantesSICAMUREX.PRICE_HEADER)
			  .append(ConstantesSICAMUREX.MONTO_DIVISA)
			  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
			  //.append(ConstantesSICAMUREX.F_COST_HEADER)
			  .append(ConstantesSICAMUREX.TC_HEADER)
			  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
			  .append(ConstantesSICAMUREX.DEAL_HEADER)
			  .append(ConstantesSICAMUREX.SEPARADOR_CSV)
			  .append(ConstantesSICAMUREX.SALTO_LINEA);
		
		
	}


	private SicaMurexService getSicaMurexService() {
		
		return (SicaMurexServiceImpl)getApplicationContext().
													   getBean(ConstantesSICAMUREX.SICA_MUREX_SERVICE);
	}
    
    
    /**
     * Establece el T&iacute;tulo del Portlet de B&uacute;squedas de la P&aacute;gina de
     * acuerdo al modo de operaci&oacute;n de la misma.
     * @param tituloActionPB El T&iacute;tulo del Portlet de B&uacute;squedas de la P&aacute;gina.
     */
    public abstract String getTituloActionMC();
    
    public abstract void setTituloActionMC(String tituloActionMC);
    
    

    /**
     * Método que almacena la lista de cortes seleccionados por los filtros.
     * 
     * @return Lista de Cortes
     */
    public abstract List getCorteDetalleList();
    
    public abstract void setCorteDetalleList(List corteList);
    
    
    /**
     * Obtiene la lista de divisas 
     * 
     * @return List de divisas.
     */
    public abstract List getDivisaList();
    
    public abstract void setDivisaList(List divisaList);
    
    /**
	 * Fija el valor de divisaSeleccionada.
	 * 
	 * @param divisaSeleccionada
	 *            El valor a asignar.
	 */
	public abstract void setFechaSeleccionada(Date fechaSeleccionada);
	
	public abstract Date getFechaSeleccionada();
    
    
	/**
     * Valor de la divisa Seleccionada.
     *
     * @return Divisa.
     */
    public abstract Divisa getDivisaSeleccionada();
  
    public abstract void setDivisaSeleccionada(Divisa divisaSeleccionada);
	
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


}

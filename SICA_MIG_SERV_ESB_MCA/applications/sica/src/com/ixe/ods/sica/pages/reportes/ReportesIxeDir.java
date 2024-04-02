package com.ixe.ods.sica.pages.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.text.SimpleDateFormat;
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

import com.banorte.www.ws.exception.SicaAltamiraException;
import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.dto.InfoCuentaAltamiraDto;
import com.ixe.ods.sica.model.CorteDetalle;
//import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.services.InformacionCuentaAltamiraService;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * P&aacute;gina que permite al usuario buscar e imprimir los reportes de
 * diarios de IXEDIRECTO
 * 
 * @author Jesus David Cortes Hernandez (JDCH)
 */
public abstract class ReportesIxeDir extends SicaPage {
	
    public static final String SEPARADOR_CSV = ",";

    /** Cadena Vacia  **/
    public static final String EMPTY_STR = "";
    
    /** Salto de linea * */
    public static final String SALTO_LINEA = "\n";

    /** Columna type * */
    public static final String NODEAL = "noDeal";

    /** Columna mnemonico * */
    public static final String MNEMONICO = "mnemonico";

    /**  Columna contratoSica * */
    public static final String CONTRATOSICA = "contratoSica";

    /**  Columna fechaCaptura * */
    public static final String FECHACAPTURA = "fechaCaptura";

    /**  Columna promotorSica * */
    public static final String PROMOTORSICA = "promotorSica";

    /**  Columna utilidad * */
    public static final String UTILIDAD = "utilidad";

    /** Columna  usuarioCaptura * */
    public static final String USUARIOCAPTURA = "usuarioCaptura";

    /** Columna rim * */
    public static final String RIM = "rim";

    /** Columna idPersonaCliente * */
    public static final String IDPERSONACLIENTE = "idPersonaCliente";

    /** Columna sucursal * */
    public static final String SUCURSAL = "sucursal";

    /** Columna  noSucursal * */
    public static final String NOSUCURSAL = "noSucursal";

    /** Columna monto  * */
    public static final String MONTO = "monto";

    /** Columna divisa  * */
    public static final String DIVISA = "divisa";

    /** Columna  tipoPersonaCliente * */
    public static final String TIPOPERSONACLIENTE = "tipoPersonaCliente";

    /** Columna  tipoCambioCliente* */
    public static final String TIPOCAMBIOCLIENTE = "tipoCambioCliente";

    /** Columna  tipoOperacion* */
    public static final String TIPOOPERACION = "tipoOperacion";

    /** Columna producto * */
    public static final String PRODUCTO = "producto";

    /** Columna cuentaCheques * */
    public static final String CUENTACHEQUES = "cuentaCheques";

    /** Columna  confirma* */
    public static final String CONFIRMA = "confirma";

    /** Columna zona * */
    public static final String ZONA = "zona";

    /** Columna  year* */
    public static final String YEAR = "year";
    
    /** Columna  mes* */
    public static final String MES = "mes";

    /** Columna  semanaDia * */
    public static final String SEMANADIA = "semanaDia";

    /** Columna semana * */
    public static final String SEMANA = "semana";

    /** Columna diaNumero * */
    public static final String DIANUMERO = "diaNumero";

    /** Columna  direccion* */
    public static final String DIRECCION = "direccion";

    /** Columna nombreCliente * */
    public static final String NOMBRECLIENTE = "nombreCliente";

    /** Columna  tipoCambioMesa* */
    public static final String TIPOCAMBIOMESA = "tipoCambioMesa";
	
	
	public void activate(IRequestCycle cycle) {
		setDetalles(new ArrayList());
		super.activate(cycle);
		setModo((String) cycle.getServiceParameters()[0]);
		setFechaSeleccionada(null);
	}
	
    /**
     * Genera el encabezado del Reporte
     *
     * @param b <code>StringBuffer</code>
     * 		que será poblado con el encabezado.
     */
    private void generarEncabezadoArchivoReporte(StringBuffer b) {
        b.append(NODEAL);
        b.append(SEPARADOR_CSV);

        b.append(MNEMONICO);
        b.append(SEPARADOR_CSV);

        b.append(CONTRATOSICA);
        b.append(SEPARADOR_CSV);

        b.append(FECHACAPTURA);
        b.append(SEPARADOR_CSV);

        b.append(PROMOTORSICA);
        b.append(SEPARADOR_CSV);

        b.append(UTILIDAD);
        b.append(SEPARADOR_CSV);

        b.append(USUARIOCAPTURA);
        b.append(SEPARADOR_CSV);

        b.append(RIM);
        b.append(SEPARADOR_CSV);

        b.append(IDPERSONACLIENTE);
        b.append(SEPARADOR_CSV);

        b.append(SUCURSAL);
        b.append(SEPARADOR_CSV);

        b.append(NOSUCURSAL);
        b.append(SEPARADOR_CSV);

        b.append(MONTO);
        b.append(SEPARADOR_CSV);

        b.append(DIVISA);
        b.append(SEPARADOR_CSV);

        b.append(TIPOPERSONACLIENTE);
        b.append(SEPARADOR_CSV);

        b.append(TIPOCAMBIOCLIENTE);
        b.append(SEPARADOR_CSV);

        b.append(TIPOOPERACION);
        b.append(SEPARADOR_CSV);

        b.append(PRODUCTO);
        b.append(SEPARADOR_CSV);

        b.append(CUENTACHEQUES);
        b.append(SEPARADOR_CSV);

        b.append(CONFIRMA);
        b.append(SEPARADOR_CSV);

        b.append(ZONA);
        b.append(SEPARADOR_CSV);

        b.append(YEAR);
        b.append(SEPARADOR_CSV);
        
        b.append(MES);
        b.append(SEPARADOR_CSV);

        b.append(SEMANADIA);
        b.append(SEPARADOR_CSV);

        b.append(SEMANA);
        b.append(SEPARADOR_CSV);

        b.append(DIANUMERO);
        b.append(SEPARADOR_CSV);

        b.append(DIRECCION);
        b.append(SEPARADOR_CSV);

        b.append(NOMBRECLIENTE);
        b.append(SEPARADOR_CSV);

        b.append(TIPOCAMBIOMESA);

        b.append(SALTO_LINEA);
    }

    /**
     * Genera los registros del Reporte
     *
     * @param b <code>StringBuffer</code>
     * 		que será poblado con los registros.

     * @param resultadoBusqueda <code>List</code>
     * 		de objetos <code>Detalles</code>
     * 		que son procesados.
     */
    private void generarRegistrosArchivoReporte(StringBuffer b, List resultadoBusqueda) throws SicaAltamiraException {
    	
    	Visit visit = (Visit) getVisit();

        for (Iterator iter = resultadoBusqueda.iterator(); iter.hasNext();) {
            
        	Object[] tupla = (Object[]) iter.next();

            b.append((Integer)tupla[0]);
            b.append(SEPARADOR_CSV);
            
            b.append((String)tupla[1]);
            b.append(SEPARADOR_CSV);
            
            b.append((String)tupla[2]);
            b.append(SEPARADOR_CSV);

            b.append((Date)tupla[3]);
            b.append(SEPARADOR_CSV);
            
            b.append((String)tupla[4]);
            b.append(SEPARADOR_CSV);
            
            b.append((Double)tupla[5]);
            b.append(SEPARADOR_CSV);

            b.append((String)tupla[6]);
            b.append(SEPARADOR_CSV);
            
            b.append((Integer)tupla[7]);
            b.append(SEPARADOR_CSV);
            
            b.append((String)tupla[8]);
            b.append(SEPARADOR_CSV);
            
            if ((String)tupla[17] != null){
            	InfoCuentaAltamiraDto infoCuentaAltamiraDto = null;
            	try{
                //Se consulta la cuenta en Altamira
                infoCuentaAltamiraDto = getInformacionCuentaAltamiraService().consultaInformacionCuentaParaReporte((String)tupla[17], visit.getTicket());
                b.append(infoCuentaAltamiraDto.getDescSucursal());
                b.append(SEPARADOR_CSV);
            	}
            	catch(SicaAltamiraException e){//cacha el error proveniente de la consulta en altamira y lo pinta el registro vacio
            		_logger.debug("Ocurrió un error en la consulta de la cuenta: " + (String)tupla[17]);
            		b.append((String)tupla[9]);
            		b.append(SEPARADOR_CSV);
            	}
            }
            else{
            	b.append((String)tupla[9]);
                b.append(SEPARADOR_CSV);            	
            }
            
            if ((String)tupla[17] != null) {
            	try{
                InfoCuentaAltamiraDto infoCuentaAltamiraDto = null;
                //Se consulta la cuenta en Altamira
                infoCuentaAltamiraDto = getInformacionCuentaAltamiraService().consultaInformacionCuentaParaReporte((String)tupla[17], visit.getTicket());
                b.append(infoCuentaAltamiraDto.getCr());
                b.append(SEPARADOR_CSV);
            	}
            	catch(SicaAltamiraException e){//cacha el error proveniente de la consulta en altamira y lo pinta el registro vacio
            		_logger.debug("Ocurrió un error en la consulta de la cuenta: " + (String)tupla[17]);
            		b.append((String)tupla[10]);
            		b.append(SEPARADOR_CSV);
            	}
           }
            else{
            	b.append((String) tupla[10]);
                b.append(SEPARADOR_CSV);
            }
            
            b.append((Double)tupla[11]);
            b.append(SEPARADOR_CSV);
            
            b.append((String)tupla[12]);
            b.append(SEPARADOR_CSV);
            
            b.append((String)tupla[13]);
            b.append(SEPARADOR_CSV);
            
            b.append((Double)tupla[14]);
            b.append(SEPARADOR_CSV);

            b.append((String)tupla[15]);
            b.append(SEPARADOR_CSV);
            
            b.append((String)tupla[16]);
            b.append(SEPARADOR_CSV);
            
            b.append((String)tupla[17]);
            b.append(SEPARADOR_CSV);
            
            b.append((String)tupla[18]);
            b.append(SEPARADOR_CSV);
            
            b.append((String)tupla[19]);
            b.append(SEPARADOR_CSV);
            
            b.append((String)tupla[20]);
            b.append(SEPARADOR_CSV);

            b.append((String)tupla[21]);
            b.append(SEPARADOR_CSV);
            
            b.append((String)tupla[22]);
            b.append(SEPARADOR_CSV);
            
            b.append((String)tupla[23]);
            b.append(SEPARADOR_CSV);
            
            b.append((Integer)tupla[24]);
            b.append(SEPARADOR_CSV);
            
            b.append((String)tupla[25]);
            b.append(SEPARADOR_CSV);

            b.append((String)tupla[26]);
            b.append(SEPARADOR_CSV);
            
            b.append((Double)tupla[27]);
            
            b.append(SALTO_LINEA);
        }
    }
    

	public void reportes(IRequestCycle cycle) throws SicaAltamiraException {
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
				.findReporteOperacionesDiariasIxedir(inicioDiaSql, finDiaSql);
		setDetalles(resultadoBusqueda);
		//Si no encontramos datos mandamos mensaje de error
		if (getDetalles().isEmpty()) {
			delegate.record("No se encontraron datos con los Criterios de "
					+ "B\u00fasqueda especificados.", null);
		} else{
			StringBuffer buffer = new StringBuffer();
            generarEncabezadoArchivoReporte(buffer);
            generarRegistrosArchivoReporte(buffer, resultadoBusqueda);
            _logger.debug("Reporte armado: "+ buffer.toString());
            
            HttpServletResponse response = cycle.getRequestContext().getResponse();

            byte[] bytes2 = String.valueOf(buffer).getBytes();
            //response.setContentType("application/vnd.ms-excel");
            response.setContentType("text/csv");            
            response.setContentLength(bytes2.length);
            //response.setHeader("Content-disposition", "attachment; filename=\"" + "ReporteOpsDiariaIxeDir" + ".xls\"");
            response.setHeader("Content-disposition", "attachment; filename=\"" + "ReporteOpsDiariaIxeDir" + ".csv\"");
            
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
			
		}
		
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
	
    /**
     * Getter InformacionCuentaAltamiraService
     *
     * @return InformacionCuentaAltamiraService
     */
    private InformacionCuentaAltamiraService getInformacionCuentaAltamiraService() {
        return (InformacionCuentaAltamiraService) getApplicationContext().getBean("informacionCuentaAltamiraService");
    }
}

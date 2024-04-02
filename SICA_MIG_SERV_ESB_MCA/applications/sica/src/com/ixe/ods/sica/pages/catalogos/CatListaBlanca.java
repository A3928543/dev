package com.ixe.ods.sica.pages.catalogos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.LazyInitializationException;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.request.IUploadFile;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.EjecutivoOrigen;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaValidationException;
import com.ixe.ods.sica.Visit;
//import com.ixe.ods.sica.dao.PhoenixDao; /** Apagado de Phoenix*/
import com.ixe.ods.sica.model.PersonaListaBlanca;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pizarron.Consts;
import com.ixe.ods.sica.services.BusquedaClientesService;
import com.ixe.ods.sica.services.impl.XlsServiceImpl;
import com.ixe.ods.sica.services.model.ModeloXls;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Cat&aacute;logo de lista blanca de personas para operar efectivo.
 *
 * @author lvillegas
 * @version $Revision: 1.1.2.3.32.1 $ $Date: 2014/04/24 17:45:16 $
 */
public abstract class CatListaBlanca extends SicaPage {

	/**
	 * Asigna los valores.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
        setModo(MODO_CONSULTA_PERSONAS);
        limpiarArchivoDescarga();
        actualizarLista();
	}

	/**
     * Crea una nueva instancia del registro LimiteEfectivo.
     *
     * @param cycle El IRequestCycle.
     */
	public void insert(IRequestCycle cycle) {
		limpiarArchivoDescarga();
        if (!isCamposValidos()) {
            return;
        }
        if (!getAltaTipoExcepcion().equals(PersonaListaBlanca.EXCEPCION_IXE)) {
            setAltaLimiteDiario(0.0);
            setAltaLimiteMensual(0.0);
        }
        PersonaListaBlanca persona;
        if (getPersonaOriginal() == null) {
            persona = new PersonaListaBlanca(
                getAltaNoCuenta(),
                getAltaTipoExcepcion(),
                new Double(getAltaLimiteDiario()),
                new Double(getAltaLimiteMensual()),
                getAltaObservaciones(),
                new Date(),
                new Date(),
                obtieneEmplId(((Visit) getVisit()).getUser().getIdPersona())
            );
        }
        else {
            persona = getPersonaOriginal();
            persona.setNoCuenta(getAltaNoCuenta());
            persona.setTipoExcepcion(getAltaTipoExcepcion());
            persona.setLimiteDiario(new Double(getAltaLimiteDiario()));
            persona.setLimiteMensual(new Double(getAltaLimiteMensual()));
            persona.setObservaciones(getAltaObservaciones());
            persona.setFechaUltimaModificacion(new Date());
            persona.setUsuarioModificacion(
                obtieneEmplId(((Visit) getVisit()).getUser().getIdPersona()));
        }
        /** Apagado de Phoenix*/
//        try {
//            PhoenixDao phoenixDao = (PhoenixDao)
//                getApplicationContext().getBean("phoenixDao");
//            phoenixDao.updateListasBlanca(persona);
//        }
//        catch (Exception e) {
//        	warn(e.getMessage(), e);
//            setLevel(0);
//            getDelegate().record("No se pudo actualizar la base de datos de phoenix " +
//                "para estos registros. Favor de informar a soporte.", null);
//            return;
//        }
        if (getPersonaOriginal() == null) {
        	getSicaServiceData().store(persona);
        }
        else {
        	getSicaServiceData().update(persona);
        }
        setModo(MODO_CONSULTA_PERSONAS);
        actualizarLista();
	}
    
    /**
     * Se obtiene el emplId de phoenix que es el id original de la persona.
     * @param idPersona en bup.
     * @return Integer.
     */
    private Integer obtieneEmplId(Integer idPersona) {
        EjecutivoOrigen eo = getSicaServiceData().findEjecutivoOrigenByIdPersona(idPersona);
        Integer resultado = new Integer(0);
        if (eo != null) {
            try {
                resultado = new Integer(eo.getId().getIdOriginal());
            }
            catch (Exception e) {
            	warn(e.getMessage(), e);
            }
        }
        return resultado;
    }

    /**
     * M&eacute;todo que hace la validaci&oacute;n de los campos que se desean
     * insertar.
     * @return boolean que determina si los campos son v&aacute;lidos o no.
     */
    private boolean isCamposValidos() {
        boolean valido = true;
        setLevel(0);
        if (getAltaTipoExcepcion() == null) {
            getDelegate().record("Favor de seleccionar el tipo de excepci\u00f3n.", null);
            valido = false;
        }
        else if (getAltaTipoExcepcion().equals(PersonaListaBlanca.EXCEPCION_IXE)) {
            if (getAltaLimiteDiario() <= 0.0 ||
                    getAltaLimiteDiario() > Consts.NUMD_999999999P99) {
                getDelegate().record("Para la excepciones Ixe, el l\u00edmite " +
                    "diario debe ser un valor mayor " +
                    "a cero y menor a 1,000,000,000.00.", null);
                valido = false;
            }
            else if (getAltaLimiteMensual() <= 0.0 ||
                    getAltaLimiteMensual() > Consts.NUMD_999999999P99) {
                getDelegate().record("Para las excepciones Ixe, el l\u00edmite " +
                    "mensual debe ser un valor mayor " +
                    "a cero y menor a 1,000,000,000.00.", null);
                valido = false;
            }
            else if (getAltaLimiteDiario() > getAltaLimiteMensual()) {
                getDelegate().record("El l\u00edmite diario no puede ser mayor al " +
                    "l\u00edmite mensual.", null);
                valido = false;            
            }
        }
        else if (getAltaObservaciones() == null || getAltaObservaciones().equals("")) {
            getDelegate().record("Las observaciones no pueden ir vac\u00edas.", null);
            valido = false;  
        }
        else if (getAltaObservaciones().length() > 255) {
            getDelegate().record("Las observaciones no pueden ser mayores a 255 caracteres.", null);
            valido = false;
        }
        return valido;
    }
    
    /**
     * Cambia a modo de b&uacute;squeda.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void agregar(IRequestCycle cycle) {
    	limpiarArchivoDescarga();
        setModo(MODO_BUSQUEDA_CONTRATO);
        setPersonaOriginal(null);
        setCuentas(null);
        setBusquedaRazonSocial(null);
        setBusquedaCuenta(null);
        setAltaTipoExcepcion(PersonaListaBlanca.SIN_EXCEPCION);
    }
    
    /**
     * Realiza la b&uacute;squeda de contratos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void buscar(IRequestCycle cycle) {
    	limpiarArchivoDescarga();
        if (getBusquedaRazonSocial().trim().equals("") &&
                getBusquedaCuenta().trim().equals("")) {
            setLevel(0);
            getDelegate().record("Debe de buscar por al menos alg\u00FAn criterio.", null);
            return;
        }
        setModo(MODO_RESULTADO_BUSQUEDA_CONTRATO);
        try {
            BusquedaClientesService busquedaService = (BusquedaClientesService)
                getApplicationContext().getBean("busquedaClientesService");
            setCuentas(null);
            try {
                setCuentas(busquedaService.findClientes(
                    getBusquedaRazonSocial(), "", "", "",
                    getBusquedaCuenta(), FacultySystem.MODO_MESA_CONTROL, true,
                    ((Visit) getVisit()).getUser().getIdPersona(),
                    ((Visit) getVisit()).getUser().getIdUsuario(),
                    ((Visit) getVisit()).isIxeDirecto()));
            }
            catch (Exception e) {
                
            }
        }
        catch (SicaValidationException e) {
            setLevel(-1);
            getDelegate().record(e.getMessage(), null);
        }
    }
    
    /**
     * Cambia a modo edici&oacute;n.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void editar(IRequestCycle cycle) {
    	limpiarArchivoDescarga();
        String noCuenta = cycle.getServiceParameters()[0].toString();
        PersonaListaBlanca persona = (PersonaListaBlanca)
            getSicaServiceData().findPersonaListaBlanca(noCuenta);
        boolean inicializado = false;
        try {
            if (persona != null && persona.getNoCuenta() != null) {
                persona.getTipoExcepcion();
                inicializado = true;
            }
        }
        catch (LazyInitializationException e) {}
        
        if (inicializado) {
            setPersonaOriginal(persona);
            setAltaLimiteDiario(persona.getLimiteDiario().doubleValue());
            setAltaLimiteMensual(persona.getLimiteMensual().doubleValue());
            setAltaNoCuenta(persona.getNoCuenta());
            setAltaObservaciones(persona.getObservaciones());
            setAltaTipoExcepcion(persona.getTipoExcepcion());
        }
        else {
            setAltaNoCuenta(noCuenta);
            setAltaLimiteDiario(0.0);
            setAltaLimiteMensual(0.0);
            setAltaObservaciones("");
            setAltaTipoExcepcion(PersonaListaBlanca.SIN_EXCEPCION);
        }
        if (getModo().equals(MODO_CONSULTA_PERSONAS)) {
            setCuentas(null);
        }
        setModo(MODO_EDICION_PERSONA);
    }
    
    /**
     * Se regresa a la pantalla de consulta.
     * @param cycle request.
     */
    public void regresar(IRequestCycle cycle) {
    	limpiarArchivoDescarga();
        if (getCuentas() == null) {
            setModo(MODO_CONSULTA_PERSONAS);
        }
        else {
            setModo(MODO_RESULTADO_BUSQUEDA_CONTRATO);
        }
    }
    
    /**
     * Se regresa a la consulta de registros.
     * @param cycle request.
     */
    public void regresarPrincipal(IRequestCycle cycle) {
    	limpiarArchivoDescarga();
        setModo(MODO_CONSULTA_PERSONAS);
    }
    
    /**
     * Recibe el contenido del archivo Excel, que genera la carga masiva de registros para la tabla
     * SC_LB_PERSONA.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cargaMasiva(IRequestCycle cycle) {
    	try {
    		InputStream is = getArchivo().getStream();
    		if (is.available() < 1) {
                throw new SicaException("Por favor proporcione una ruta.");
            }
    		HSSFWorkbook wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            int i = 1;
            List personasLB = new ArrayList();
            while (sheet.getRow(i) != null) {
                HSSFRow row = sheet.getRow(i);
                PersonaListaBlanca personaLB = new PersonaListaBlanca();
                crearPersonaListaBlanca(row, personaLB, i);
                personasLB.add(personaLB);
                i++;
            }
            //Se insertan o se actualizan los registros contenidos en el archivo de Excel
            updateOrSaveRecord(personasLB, cycle);
            getDelegate().record("El archivo fue procesado. Favor de revisar la " +
            		"retroalimentaci\u00f3n en el archivo de Excel.", null);
    	}
    	catch (SicaException e) {
    		warn(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
    	}
    	catch (IOException e) {
    		warn(e.getMessage(), e);
            getDelegate().record("Error al leer el archivo de carga masiva.", null);
    	}
    }
    
    /**
     * Descarga un template en Excel para la carga masiva de registros para la tabla SC_LB_PERSONA.
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void descargarArchivoBase(IRequestCycle cycle) {
    	HttpServletResponse response = cycle.getRequestContext().getResponse();
    	String nombreArchivo = "archivos/sica/BaseCargaMasivaLbP.xls";
    	FileInputStream fis = null;
        OutputStream os = null;
        try {
        	File f = new File(nombreArchivo);
        	if (!f.exists()) {
                throw new SicaException("No se encontr\u00f3 el archivo base para la carga" +
                		" masiva de registros.");
            }
        	fis = new FileInputStream(nombreArchivo);
            byte[] bytes = new byte[fis.available()];
            response.setContentType("application/vnd.ms-excel");
            response.setContentLength(fis.read(bytes));
            response.setHeader("Content-disposition", "attachment; filename=\"" +
                    f.getName() + "\"");
            os = response.getOutputStream();
            os.write(bytes, 0, bytes.length);
            os.flush();
        }
        catch (SicaException e) {
        	warn(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
        catch (FileNotFoundException e) {
        	warn(e.getMessage(), e);
            getDelegate().record("No se encontr\u00f3 el archivo " + nombreArchivo + ".",
                    null);
        }
        catch (IOException e) {
        	warn(e.getMessage(), e);
            getDelegate().record("Ocurri\u00f3 un error al leer el archivo " + nombreArchivo + ".",
                    null);
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch (IOException e) {
                    debug(e.getMessage(), e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                }
                catch (IOException e) {
                    debug(e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * Inserta o actualiza los registros de tipo PersonaListaBlanca que contiene el archivo de
     * Excel.
     *
     * @param personasLB Lista de registros obtenidos desde el archivo de Excel.
     * @param cycle El ciclo de la p&aacute;gina.
     */
    private void updateOrSaveRecord(List personasLB, IRequestCycle cycle) {
    	if (personasLB.isEmpty()) {
        	throw new SicaException("El archivo que intent\u00f3 procesar no conten\u00eda" +
        			" ning\u00fan registro.");
        }
    	HashMap resultadosPersona = null;
    	List resultados = new ArrayList();
    	for (Iterator it = personasLB.iterator(); it.hasNext();) {
    		try {
    			resultadosPersona = new HashMap();
    			PersonaListaBlanca nuevaPersona = (PersonaListaBlanca) it.next();
    			resultadosPersona.put("noCuenta", nuevaPersona.getNoCuenta());
        		ContratoSica contrato = getSicaServiceData().findContrato(nuevaPersona.
        				getNoCuenta());
        		if (contrato == null) {
        			throw new SicaException("El n\u00famero de contrato " + nuevaPersona.
        					getNoCuenta() + " no existe.");
        		}
        		PersonaListaBlanca personaInBd = getSicaServiceData().
        					findPersonaListaBlanca(nuevaPersona.getNoCuenta());
    			if (personaInBd != null) {
    				nuevaPersona.setFechaCreacion(personaInBd.getFechaCreacion());
    			}
    			/** Apagado de Phoenix */
//    			try {
//    	            PhoenixDao phoenixDao = (PhoenixDao)
//    	                getApplicationContext().getBean("phoenixDao");
//    	            phoenixDao.updateListasBlanca(nuevaPersona);
//    	        }
//    	        catch (Exception e) {
//    	        	warn(e.getMessage(), e);
//    	        	throw new SicaException("No se pudo actualizar la base de datos de phoenix " +
//        	                "para estos registros. Favor de informar a soporte.");
//    	        }
    	        if (personaInBd != null) {
    	        	personaInBd.setFechaUltimaModificacion(new Date());
    	        	personaInBd.setUsuarioModificacion(nuevaPersona.getUsuarioModificacion());
    	        	personaInBd.setLimiteDiario(nuevaPersona.getLimiteDiario());
    	        	personaInBd.setLimiteMensual(nuevaPersona.getLimiteMensual());
    	        	personaInBd.setNoCuenta(nuevaPersona.getNoCuenta());
    	        	personaInBd.setNombreCorto(nuevaPersona.getNombreCorto());
    	        	personaInBd.setObservaciones(nuevaPersona.getObservaciones());
    	        	personaInBd.setTipoExcepcion(nuevaPersona.getTipoExcepcion());
    	        	getSicaServiceData().update(personaInBd);
    				resultadosPersona.put("accion", "Actualizado");
    				resultados.add(resultadosPersona);
    	        }
    	        else {
    	        	getSicaServiceData().store(nuevaPersona);
    				resultadosPersona.put("accion", "Agregado");
    				resultados.add(resultadosPersona);
    	        }
    		}
    		catch (Exception e) {
	        	warn(e.getMessage(), e);
    			resultadosPersona.put("accion", "Error");
    			resultadosPersona.put("mensajeError", e.getMessage());
    			resultados.add(resultadosPersona);
    		}
    	}
    	guardarResultado(resultados, cycle);
    }
    
    /**
     * Almacena en una variable el archivo de respuesta para el usuario.
     *
     * @param resultados La lista de resultados de la carga masiva.
     * @param cycle El ciclo de la p&aacute;gina.
     */
    private void guardarResultado(List resultados, IRequestCycle cycle) {
    	XlsServiceImpl xlsService = new XlsServiceImpl(new ModeloXls("Resultados de la Carga" +
    			" Masiva", new String[]{"N\u00famero de Contrato", "Acci\u00f3n", "Mensaje"},
    			new String[]{"noCuenta", "accion", "mensajeError"}, resultados));
    	ByteArrayOutputStream contenido = null;
        OutputStream os = null;
        try {
        	contenido = new ByteArrayOutputStream();
        	xlsService.escribirXls(contenido);
        	setArchivoRespuesta(contenido.toByteArray());
        	setEsDescargable(new Boolean(true));
        	actualizarLista();
        	setLevel(1);
        	getDelegate().record("La carga masiva fue procesada exitosamente.", null);
        }
        catch(Exception ex) {
        	warn(ex.getMessage(), ex);
            getDelegate().record("Ocurri\u00f3 un error al tratar de enviar el archivo de" +
            		" resultados.", null);
        }
        finally {
        	if (os != null) {
                try {
                    os.close();
                }
                catch (IOException e) {
                	warn(e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * Comienza la descarga del archivo generado por el sistema para la retroalimentaci&oacute;n.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void descargarArchivoRespuesta(IRequestCycle cycle) {
    	OutputStream os = null;
        try {
        	byte[] bytes = getArchivoRespuesta();
        	HttpServletResponse response = cycle.getRequestContext().getResponse();
        	response.setContentType("application/vnd.ms-excel");
        	response.setContentLength(bytes.length);
        	response.setHeader("Content-disposition", "attachment; filename=\"" +
        			"ResultadoCargaMasivaLbP.xls\"");
        	os = response.getOutputStream();
            os.write(bytes, 0, bytes.length);
            os.flush();
        }
        catch(Exception ex) {
        	warn(ex.getMessage(), ex);
            getDelegate().record("Ocurri\u00f3 un error al tratar de enviar el archivo de" +
            		" resultados.", null);
        }
        finally {
        	if (os != null) {
                try {
                    os.close();
                }
                catch (IOException e) {
                    debug(e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * Crea un objto de tipo PersonaListaBlanca con los valores del rengl&oacute;n del Excel.
     *
     * @param row El rengl&oacute;n a revisar.
     * @param personaLB La persona en lista blanca a crear.
     * @param renglon El n&uacute;mero de rengl&oacute;n del excel.
     */
    private void crearPersonaListaBlanca(HSSFRow row, PersonaListaBlanca personaLB, int renglon) {
    	personaLB.setNoCuenta((String) getValor(row, Num.I_0, Num.I_1, renglon));
    	personaLB.setTipoExcepcion((String) getValor(row, Num.I_1, Num.I_1, renglon));
    	personaLB.setLimiteDiario((Double) getValor(row, Num.I_2, Num.I_2, renglon));
    	personaLB.setLimiteMensual((Double) getValor(row, Num.I_3, Num.I_2, renglon));
    	personaLB.setObservaciones((String) getValor(row, Num.I_4, Num.I_1, renglon));
    	personaLB.setFechaCreacion(new Date());
    	personaLB.setFechaUltimaModificacion(new Date());
    	personaLB.setUsuarioModificacion(obtieneEmplId(((Visit) getVisit()).getUser().
    			getIdPersona()));
    }
    
    /**
     * Regresa el valor de la celda en la columna especificada de un rengl&oacute;n.
     *
     * @param renglon El rengl&oacute;n a revisar.
     * @param columna El n&uacute;mero de la columna a revisar.
     * @param tipo El tipo de dato a leer 0.- Fecha, 1.- Texto, 2.- N&uacute;mero.
     * @param numRenglon El n&uacute;mero de rengl&oacute;n del excel.
     * @return Object El valor de la celda.
     */
    private Object getValor(HSSFRow renglon, int columna, int tipo, int numRenglon) {
        HSSFCell cell = renglon.getCell((short) columna);
        Object valor = null;
        if (tipo == Num.I_0) {
            try {
                valor = cell.getDateCellValue();
            }
            catch (Exception e) {
                throw new SicaException(getMensajeColumnaRenglon(columna, numRenglon,
                        " se esperaba una Fecha."));

            }
        }
        else if (tipo == Num.I_1) {
            if (HSSFCell.CELL_TYPE_STRING != cell.getCellType()) {
                throw new SicaException(getMensajeColumnaRenglon(columna, numRenglon,
                        " se esperaba una Cadena de Caracteres."));
            }
            valor = cell.getStringCellValue();
        }
        else if (tipo == Num.I_2) {
            try {
                valor = new Double(cell.getNumericCellValue());
            }
            catch (Exception e) {
                throw new SicaException(getMensajeColumnaRenglon(columna, numRenglon,
                        " se esperaba un valor num\u00e9rico."));
            }
        }
        if (valor == null) {
            throw new SicaException(getMensajeColumnaRenglon(columna, numRenglon,
                    " se esperaba un valor."));
        }
        return valor;
    }
    
    /**
     * Regresa un mensaje de error referente a una columna y rengl&oacute;n del excel.
     *
     * @param columna El n&uacute;mero de la columna.
     * @param renglon El n&uacute;mero del rengl&oacute;n.
     * @param postFijo El mensaje espec&iacute;fico.
     * @return String.
     */
    private String getMensajeColumnaRenglon(int columna, int renglon, String postFijo) {
        return "En la columna " + (columna + Num.I_1) + ", rengl\u00f3n " + (renglon + Num.I_1) +
        		postFijo;
    }
    
    /**
     * Realiza la actualizaci&oacute;n de la lista sincroniz&aacute;ndola con
     * base de datos.
     */
    private void actualizarLista() {
        setPersonas(getSicaServiceData().findPersonasListaBlanca());
    }
    
    /**
     * Inicializa las variables del archivo de descarga.
     */
    private void limpiarArchivoDescarga() {
    	setEsDescargable(new Boolean(false));
		setArchivoRespuesta(null);
    }
    
    /**
     * Regresa el modo en que se encuentra el cat&aacute;logo.
     * @return Integer.
     */
    public abstract Integer getModo();
    
    /**
     * Guarda el modo en que se encuentra el cat&aacute;logo.
     * @param numero a guardar.
     */
    public abstract void setModo(Integer numero);
    
    /**
     * Regresar la lista de personas de la lista blanca.
     * @return List.
     */
    public abstract List getPersonas();
    
    /**
     * Guarda la lista de personas de la lista blanca.
     * @param lista que se desea guardar.
     */
    public abstract void setPersonas(List lista);
    
    /**
     * Regresar la raz&oacute;n social utilizada para la b&uacute;squeda de contratos.
     * @return String.
     */
    public abstract String getBusquedaRazonSocial();
    /**
     * Guarda las raz&oacute;n social utilizada para la b&uacute;squeda de contratos.
     * @param cadena que se desea guardar.
     */
    public abstract void setBusquedaRazonSocial(String cadena);
    
    /**
     * Regresar la cuenta utilizada para la b&uacute;squeda de contratos.
     * @return String.
     */
    public abstract String getBusquedaCuenta();
    
    /**
     * Guarda la cuenta utilizada para la b&uacute;squeda de contratos.
     * @param cadena que se desea guardar.
     */
    public abstract void setBusquedaCuenta(String cadena);
    
    /**
     * Regresar la lista de cuentas.
     * @return List.
     */
    public abstract List getCuentas();
    
    /**
     * Guarda la lista de cuentas.
     * @param lista que se desea guardar.
     */
    public abstract void setCuentas(List lista);
    
    /**
     * Regresar el n&uacute;mero de cuenta utilizado para el alta de la persona
     * en la lista blanca.
     * @return String.
     */
    public abstract String getAltaNoCuenta();
    
    /**
     * Guarda el n&uacute;mero de cuenta.
     * @param cadena que se desea guardar.
     */
    public abstract void setAltaNoCuenta(String cadena);
    
    /**
     * Regresar el tipo de excepci&oacute;n utilizado para el alta de la persona
     * en la lista blanca.
     * @return String.
     */
    public abstract String getAltaTipoExcepcion();
    
    /**
     * Guarda el tipo de excepci&oacute;n.
     * @param cadena que se desea guardar.
     */
    public abstract void setAltaTipoExcepcion(String cadena);
    
    /**
     * Regresar el l&iacute;mite diario utilizado para el alta de la persona
     * en la lista blanca.
     * @return String.
     */
    public abstract double getAltaLimiteDiario();
    
    /**
     * Guarda el l&iacute;mite diario.
     * @param num que se desea guardar.
     */
    public abstract void setAltaLimiteDiario(double num);
    
    /**
     * Regresar el l&iacute;mite mensual utilizado para el alta de la persona
     * en la lista blanca.
     * @return String.
     */
    public abstract double getAltaLimiteMensual();
    
    /**
     * Guarda el l&iacute;mite mensual.
     * @param num que se desea guardar.
     */
    public abstract void setAltaLimiteMensual(double num);
    
    /**
     * Regresar las observaciones utilizadas para el alta de la persona
     * en la lista blanca.
     * @return String.
     */
    public abstract String getAltaObservaciones();
    
    /**
     * Guarda las observaciones.
     * @param cadena que se desea guardar.
     */
    public abstract void setAltaObservaciones(String cadena);
    
    /**
     * Regresar la persona utilizada durante la edici&oacute;n.
     * @return PersonaListaBlanca.
     */
    public abstract PersonaListaBlanca getPersonaOriginal();
    
    /**
     * Guarda la persona que se va a editar, para actualizarla posteriormente.
     * @param persona que se desea guardar.
     */
    public abstract void setPersonaOriginal(PersonaListaBlanca persona);
    
    /**
     * Nivel de los mensajes al usuario: -1 Error, 0 Warning, 1 Exito.
     *
     * @param level El valor a asignar.
     */
    public abstract void setLevel(int level);
    
    /**
     * Regresa el valor de archivo.
     *
     * @return IUploadFile.
     */
    public abstract IUploadFile getArchivo();
    
    /**
     * Regresa el valor del archivo guardado por el sistema para la retroalimentaci&oacute;n.
     *
     * @return byte[]
     */
    public abstract byte[] getArchivoRespuesta();
    
    /**
     * Establece el valor del archivoRespuesta.
     *
     * @param archivoRespuesta
     */
    public abstract void setArchivoRespuesta(byte[] archivoRespuesta);
    
    /**
     * Establece el valor de si hay un archivo de retroalimentaci&oacute;n para descargar.
     *
     * @param esDescargable
     */
    public abstract void setEsDescargable(Boolean esDescargable);
    
    /**
     * Regresa el valor de esDescargable.
     *
     * @return Boolean
     */
    public abstract Boolean getEsDescargable();
    
    /**
     * Constante para modo: consulta de personas.
     */
    public static final Integer MODO_CONSULTA_PERSONAS = Integer.valueOf("0");
    
    /**
     * Constante para modo: b&uacute;squeda de contrato.
     */
    public static final Integer MODO_BUSQUEDA_CONTRATO = Integer.valueOf("1");
    
    /**
     * Constante para modo: b&uacute;squeda de contrato.
     */
    public static final Integer MODO_RESULTADO_BUSQUEDA_CONTRATO = Integer.valueOf("2");
    
    /**
     * Constante para modo: edici&oacute;n de personas.
     */
    public static final Integer MODO_EDICION_PERSONA = Integer.valueOf("3");
}


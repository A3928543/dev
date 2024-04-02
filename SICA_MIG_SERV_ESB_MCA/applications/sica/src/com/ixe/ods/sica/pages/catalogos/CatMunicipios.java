package com.ixe.ods.sica.pages.catalogos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.request.IUploadFile;

import com.ixe.ods.bup.model.EjecutivoOrigen;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
//import com.ixe.ods.sica.dao.PhoenixDao; /** Apagado de Phoenix */
import com.ixe.ods.sica.model.BupMunicipio;
import com.ixe.ods.sica.model.MunicipioListaBlanca;
import com.ixe.ods.sica.model.MunicipioListaBlancaPK;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.services.impl.XlsServiceImpl;
import com.ixe.ods.sica.services.model.ModeloXls;
import com.legosoft.hibernate.type.SiNoType;

/**
 * Cat&aacute;logo de listas blancas de c&oacute;digos postales.
 *
 * @author lvillegas
 * @version $Revision: 1.1.2.5.32.1 $ $Date: 2014/04/24 17:50:08 $
 */
public abstract class CatMunicipios extends SicaPage {

	/**
	 * Asigna los valores.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		limpiarArchivoDescarga();
		setMunicipiosListaBlanca(getSicaServiceData().findAll(MunicipioListaBlanca.class));
	}

	/**
     * Crea una nueva instancia del registro MunicipioCodigoPostal.
     *
     * @param cycle El IRequestCycle.
     */
	public void insert(IRequestCycle cycle) {
		limpiarArchivoDescarga();
        if (!isCamposValidos()) {
            return;
        }
        BupMunicipio bm =
            getSicaServiceData().findMunicipioEstado(getIdMunicipio(), getIdEstado());
        MunicipioListaBlanca municipio = null;
        MunicipioListaBlancaPK idMunicipio = null;
        try {
            idMunicipio = new MunicipioListaBlancaPK();
            idMunicipio.setIdEstado(bm.getId().getEstado().getIdEstado());
            idMunicipio.setIdMunicipio(bm.getId().getIdMunicipio());
            municipio = new MunicipioListaBlanca(
                idMunicipio,                
                getEsZonaFronteriza(),
                getEsZonaTuristica(),
                new Date(),
                new Date(),
                obtieneEmplId(((Visit) getVisit()).getUser().getIdPersona())
            );
        }
        catch (NullPointerException e) {
            getDelegate().record("No se encontr\u00f3 ning\u00fan c\u00f3digo postal " +
                "asociado al municipio con id " + getIdMunicipio() +
                " y entidad federativa con id " + getIdEstado() + ".", null);
            return;
        }
        MunicipioListaBlanca municipioRegistrado =
            getSicaServiceData().findMunicipioListaBlanca(
                municipio.getId().getIdMunicipio().toString(),
                municipio.getId().getIdEstado().toString());
        MunicipioListaBlanca municipioEncontrado = null;
        if (municipioRegistrado != null) {
            municipioRegistrado.setId(municipio.getId());
            municipioRegistrado.setZonaFronteriza(municipio.getZonaFronteriza());
            municipioRegistrado.setZonaTuristica(municipio.getZonaTuristica());
            municipioRegistrado.setFechaUltimaModificacion(new Date());
            municipioRegistrado.setUsuarioModificacion(municipio.getUsuarioModificacion());
        }
        else {
        	municipioEncontrado = municipio;
        }
        /** Apagado de Phoenix */
//        try {
//            PhoenixDao phoenixDao = (PhoenixDao)
//                getApplicationContext().getBean("phoenixDao");
//            List codigosPostalesBup = null;
//            codigosPostalesBup = getSicaServiceData().
//        			findCodigosPostalesBup(municipioEncontrado.getId().getIdMunicipio().toString(),
//        					municipioEncontrado.getId().getIdEstado().toString());
//        	if (codigosPostalesBup != null && !codigosPostalesBup.isEmpty()) {
//        		phoenixDao.updateListaBlancaCodigosPostales(codigosPostalesBup,
//        				municipioEncontrado.getZonaFronteriza().booleanValue() ? SiNoType.TRUE :
//        					SiNoType.FALSE, municipioEncontrado.getZonaTuristica().booleanValue() ?
//        							SiNoType.TRUE : SiNoType.FALSE, municipioEncontrado.
//        							getFechaUltimaModificacion(),
//        							obtieneEmplId(((Visit) getVisit()).getUser().getIdPersona()));
//        	}
//        	else {
//        		throw new SicaException("No se encontr\u00f3 ning\u00fan c\u00f3digo postal " +
//        				"asociado al municipio con id "+ municipioEncontrado.getId().
//        				getIdMunicipio()+ " y entidad federativa con id " + municipioEncontrado.
//        				getId().getIdEstado() + ".");
//        	}
//        }
//        catch (Exception e) {
//        	warn(e.getMessage(), e);
//            setLevel(0);
//            getDelegate().record("No se pudo actualizar la base de datos de phoenix " +
//                "para estos registros. Favor de informar a soporte.", null);
//            return;
//        }
        if (municipioRegistrado == null) {
        	getSicaServiceData().store(municipio);
        	setLevel(1);
        	getDelegate().record("El municipio " +
                    bm.getNombre() + " en el estado " +
                    bm.getId().getEstado().getNombreEstado() +
                    " se agreg\u00f3 exitosamente.", null);
        }
        else {
        	getSicaServiceData().update(municipioRegistrado);
        	setLevel(1);
            getDelegate().record("El municipio " +
                bm.getNombre() + " en el estado " +
                bm.getId().getEstado().getNombreEstado() +
                " se actualiz\u00f3 exitosamente.", null);
        }
        setMunicipiosListaBlanca(getSicaServiceData().findAll(MunicipioListaBlanca.class));
	}

    /**
     * M&eacute;todo que hace la validaci&oacute;n de los campos que se desean
     * insertar.
     * @return boolean que determina si los campos son v&aacute;lidos o no.
     */
    private boolean isCamposValidos() {
        boolean valido = true;
        setLevel(0);
        if (getIdMunicipio() == null || getIdMunicipio().equals("")) {
            getDelegate().record("El id del municipio no puede estar vac\u00edo.", null);
            valido = false;
        }
        else if (getIdEstado() == null || getIdEstado().equals("")) {
            getDelegate().record("El id del estado no puede estar vac\u00edo.", null);
            valido = false;
        }
        return valido;
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
     * Elimina la regla de neteo seleccionada.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void eliminar(IRequestCycle cycle) {
    	limpiarArchivoDescarga();
        MunicipioListaBlanca municipioLB = (MunicipioListaBlanca) getSicaServiceData().
        		findMunicipioListaBlanca(cycle.getServiceParameters()[0].toString(),
        				cycle.getServiceParameters()[1].toString());        
        if (municipioLB != null) {
        	/** Apagado de Phoenix */
//            try {
//                PhoenixDao phoenixDao = (PhoenixDao)
//                    getApplicationContext().getBean("phoenixDao");
//                List codigosPostalesBup = null;
//                codigosPostalesBup = getSicaServiceData().
//            			findCodigosPostalesBup(municipioLB.getId().getIdMunicipio().toString(),
//            					municipioLB.getId().getIdEstado().toString());
//            	if (codigosPostalesBup != null && !codigosPostalesBup.isEmpty()) {
//            		phoenixDao.eliminarListaBlancaCodigosPostales(codigosPostalesBup);
//            	}
//            	else {
//            		throw new SicaException("No se encontr\u00f3 ning\u00fan c\u00f3digo postal " +
//            				"asociado al municipio con id "+ municipioLB.getId().
//            				getIdMunicipio()+ " y entidad federativa con id " + municipioLB.
//            				getId().getIdEstado() + ".");
//            	}
//            }
//            catch (SicaException e) {
//            	warn(e.getMessage(), e);
//                setLevel(0);
//                getDelegate().record(e.getMessage(), null);
//                return;
//            }
//            catch (Exception e) {
//            	warn(e.getMessage(), e);
//                setLevel(0);
//                getDelegate().record("No se pudo eliminar en la base de datos de phoenix. " +
//                    "Favor de informar a soporte.", null);
//                return;
//            }
            getSicaServiceData().delete(municipioLB);
            setLevel(1);
            getDelegate().record("El registro se elimin\u00f3 exitosamente.", null);
        }
        setMunicipiosListaBlanca(getSicaServiceData().findAll(MunicipioListaBlanca.class));
    }
    
    /**
     * Recibe el contenido del archivo Excel, que genera la carga masiva de registros para la tabla
     * SC_LB_CODIGO_POSTAL.
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
            List codigosPostalesLB = new ArrayList();
            while (sheet.getRow(i) != null) {
                HSSFRow row = sheet.getRow(i);
                MunicipioListaBlanca municipioLB = new MunicipioListaBlanca();
                crearMunicipioListaBlanca(row, municipioLB, i);
                codigosPostalesLB.add(municipioLB);
                i++;
            }
            //Se insertan o se actualizan los registros contenidos en el archivo de Excel
            updateOrSaveRecord(codigosPostalesLB, cycle);
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
     * Inserta o actualiza los registros de tipo CodigoPostalListaBlanca que contiene el archivo de
     * Excel.
     *
     * @param municipioLB Lista de registros obtenidos desde el archivo de Excel.
     * @param cycle El ciclo de la p&aacute;gina.
     */
    private void updateOrSaveRecord(List municipioLB, IRequestCycle cycle) {
    	if (municipioLB.isEmpty()) {
        	throw new SicaException("El archivo que intent\u00f3 procesar no conten\u00eda" +
        			" ning\u00fan registro.");
        }
    	HashMap resultadosMunicipio = null;
    	List resultados = new ArrayList();
//    	PhoenixDao phoenixDao = (PhoenixDao) getApplicationContext().getBean("phoenixDao"); /** Apagado de Phoenix */
    	for (Iterator it = municipioLB.iterator(); it.hasNext();) {
    		try {
    			resultadosMunicipio = new HashMap();
    			MunicipioListaBlanca nuevoRegistro = (MunicipioListaBlanca) it.next();
    			resultadosMunicipio.put("idEstado", nuevoRegistro.getId().getIdEstado());
    			resultadosMunicipio.put("idMunicipio", nuevoRegistro.getId().getIdMunicipio());
    			//Primero inserta o actualiza en Phoenix
	        	List codigosPostalesBup = null;
	        	codigosPostalesBup = getSicaServiceData().
	        			findCodigosPostalesBup(nuevoRegistro.getId().getIdMunicipio().toString(),
	        					nuevoRegistro.getId().getIdEstado().toString());
	        	if (codigosPostalesBup != null && !codigosPostalesBup.isEmpty()) {
	        		/** Apagado de Phoenix */
//	        		phoenixDao.updateListaBlancaCodigosPostales(codigosPostalesBup,
//	        				nuevoRegistro.getZonaFronteriza().booleanValue() ? SiNoType.TRUE :
//	        					SiNoType.FALSE, nuevoRegistro.getZonaTuristica().booleanValue() ?
//	        							SiNoType.TRUE : SiNoType.FALSE, nuevoRegistro.
//	        							getFechaUltimaModificacion(),
//	        							obtieneEmplId(((Visit) getVisit()).getUser().getIdPersona()));
	        	}
	        	else {
	        		throw new SicaException("No se encontr\u00f3 ning\u00fan c\u00f3digo postal " +
	        				"asociado a la entidad federativa con id "+ nuevoRegistro.getId().
	        				getIdEstado()+ " y municipio con id " + nuevoRegistro.
	        				getId().getIdMunicipio() + ".");
	        	}
    			MunicipioListaBlanca codPostInBd = getSicaServiceData().
    						findMunicipioListaBlanca(
                                nuevoRegistro.getId().getIdMunicipio().toString(),
                                nuevoRegistro.getId().getIdEstado().toString());
    			if (codPostInBd != null) {
    				codPostInBd.setFechaUltimaModificacion(new Date());
    				codPostInBd.setUsuarioModificacion(nuevoRegistro.getUsuarioModificacion());
    				codPostInBd.setZonaFronteriza(nuevoRegistro.getZonaFronteriza());
    				codPostInBd.setZonaTuristica(nuevoRegistro.getZonaTuristica());
    				getSicaServiceData().update(codPostInBd);
    				resultadosMunicipio.put("accion", "Actualizado");
    				resultados.add(resultadosMunicipio);
    			}
    			else {
    				getSicaServiceData().store(nuevoRegistro);
    				resultadosMunicipio.put("accion", "Agregado");
    				resultados.add(resultadosMunicipio);
    			}
    		}
    	    catch (Exception e) {
    			resultadosMunicipio.put("accion", "Error");
    			resultadosMunicipio.put("mensajeError", e.getMessage());
    			resultados.add(resultadosMunicipio);
    		}
    	}
    	try {
    		setMunicipiosListaBlanca(getSicaServiceData().findAll(MunicipioListaBlanca.class));
    	}
    	catch (Exception e) {
    		warn("Error al intentar refrescar la pantalla con la lista blanca de municipios.", e);
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
    			" Masiva", new String[]{"Estado", "Municipio", "Acci\u00f3n", "Mensaje"},
    			new String[]{"idEstado", "idMunicipio", "accion", "mensajeError"}, resultados));
    	ByteArrayOutputStream contenido = null;
        OutputStream os = null;
        try {
        	contenido = new ByteArrayOutputStream();
        	xlsService.escribirXls(contenido);
        	setArchivoRespuesta(contenido.toByteArray());
        	setEsDescargable(new Boolean(true));
        	setMunicipiosListaBlanca(getSicaServiceData().findAll(MunicipioListaBlanca.class));
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
                    debug(e.getMessage(), e);
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
        			"ResultadoCargaMasivaCP.xls\"");
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
     * Crea un objto de tipo MunicipioListaBlanca con los valores del rengl&oacute;n del Excel.
     *
     * @param row El rengl&oacute;n a revisar.
     * @param codPostLB El objeto a crear.
     * @param renglon El n&uacute;mero de rengl&oacute;n del excel.
     */
    private void crearMunicipioListaBlanca(HSSFRow row, MunicipioListaBlanca codPostLB,
    		int renglon) {
        MunicipioListaBlancaPK id = new MunicipioListaBlancaPK();
        id.setIdEstado((Integer) getValor(row, Num.I_0, Num.I_2, renglon));
        id.setIdMunicipio((Integer) getValor(row, Num.I_1, Num.I_2, renglon));
    	codPostLB.setId(id);
    	codPostLB.setZonaFronteriza(new Boolean(((String) getValor(row, Num.I_2, Num.I_1, renglon)).
    			equals(SiNoType.TRUE)));
    	codPostLB.setZonaTuristica(new Boolean(((String) getValor(row, Num.I_3, Num.I_1, renglon)).
    			equals(SiNoType.TRUE)));
    	codPostLB.setFechaCreacion(new Date());
    	codPostLB.setFechaUltimaModificacion(new Date());
    	codPostLB.setUsuarioModificacion(obtieneEmplId(((Visit) getVisit()).getUser().
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
                valor = new Integer((int) cell.getNumericCellValue());
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
     * Descarga un template en Excel para la carga masiva de registros para la tabla
     * SC_LB_MUNICIPIO_001.
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void descargarArchivoBase(IRequestCycle cycle) {
    	HttpServletResponse response = cycle.getRequestContext().getResponse();
    	String nombreArchivo = "archivos/sica/BaseCargaMasivaCP.xls";
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
                	warn(e.getMessage(), e);
                }
            }
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
    
    /** Apagado de Phoenix */
//    /**
//     * Actualiza la informaci&oacute;n de isis en Phoenix.
//     * 
//     * @param cycle El ciclo de la p&aacute;gina.
//     */
//    public void actualizarPhoenix(IRequestCycle cycle) {
//    	limpiarArchivoDescarga();
//        try {
//            getPhoenixDao().corrigeInconsistenciasIsisPhoenix();
//            setLevel(1);
//            getDelegate().record("Se sincroniz\u00f3 satisfactoriamente la informaci\u00f3n " +
//                    "con Phoenix.", null);
//        }
//        catch (Exception e) {
//            warn(e.getMessage(), e);
//            getDelegate().record("No se pudo sincronizar satisfactoriamente con Phoenix.",
//                    null);
//        }
//    }
    /** Apagado de Phoenix */
//    /**
//     * Obtiene el DAO con la conexi&oacute;n a Phoenix.
//     * 
//     * @return PhoenixDao
//     */
//    private PhoenixDao getPhoenixDao() {
//        return (PhoenixDao) getApplicationContext().getBean("phoenixDao");
//    }
    
    /**
     * Inicializa las variables del archivo de descarga.
     */
    private void limpiarArchivoDescarga() {
    	setEsDescargable(new Boolean(false));
		setArchivoRespuesta(null);
    }
    
    /**
     * Regresa el id del estado.
     * @return String
     */
    public abstract String getIdEstado();
    
    /**
     * Guarda el id del estado.
     * @param cadena que se desea guardar.
     */
    public abstract void setIdEstado(String cadena);
    
    /**
     * Regresa el id del municipio.
     * @return String
     */
    public abstract String getIdMunicipio();
    
    /**
     * Guarda el id del municipio.
     * @param cadena que se desea guardar.
     */
    public abstract void setIdMunicipio(String cadena);
    
    /**
     * Regresa si es una zona tur&iacute;sitica.
     * @return Boolean.
     */
    public abstract Boolean getEsZonaTuristica();
    
    /**
     * Selecciona si es una zona tur&iacute;sitica.
     * @param bool que se desea seleccionar.
     */
    public abstract void setEsZonaTuristica(Boolean bool);
    
    /**
     * Regresa si es una zona fronteriza.
     * @return Boolean.
     */
    public abstract Boolean getEsZonaFronteriza();
    
    /**
     * Selecciona si es una zona fronteriza.
     * @param bool que se desea seleccionar.
     */
    public abstract void setEsZonaFronteriza(Boolean bool);
    
    /**
     * Regresar la lista de los municipios que se encuentr&aacute;n en la lista blanca.
     * @return List.
     */
    public abstract List getMunicipiosListaBlanca();
    
    /**
     * Guarda la lista de municipios que se encuentr&aacute;n en la lista blanca.
     * @param lista que se desea guardar.
     */
    public abstract void setMunicipiosListaBlanca(List lista);
    
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
}


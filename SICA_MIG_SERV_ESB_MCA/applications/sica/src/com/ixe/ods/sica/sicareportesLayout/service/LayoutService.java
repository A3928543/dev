package com.ixe.ods.sica.sicareportesLayout.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

import com.ixe.ods.sica.sdo.SicaServiceData;




public interface LayoutService  {
	

    /** Prefijo del Archivo de Clientes * */
    public static final String PREFIJO_NOMBRE_ARCHIVO_CLIENTES = "sicaClientes";

    

    /** Extensión del archivo txt * */
    public static final String EXTENSION_ARCHIVO = ".txt";

	
	
    public static final String SEPARADOR_CSV = ",";

    /** Cadena Vacia  **/
    public static final String EMPTY_STR = "";
    
    /** Salto de linea * */
    public static final String SALTO_LINEA = "\n";

    /** Columna NUMCLIEN * */
    public static final String NUMCLIEN = "numClien";

    /** Columna CODIDENT * */
    public static final String CODIDENT = "codIdent";

    /**  Columna CLAIDENT * */
    public static final String CLAIDENT = "claIdent";

    /**  Columna PRIAPE * */
    public static final String PRIAPE = "priApe";

    /**  Columna promotorSica * */
    public static final String SEGAPE = "segApe";

    /**  Columna utilidad * */
    public static final String NOMBRE = "nombre";

    /** Columna  usuarioCaptura * */
    public static final String SEXO = "sexo";

    /** Columna rim * */
    public static final String ESTCIVIL = "estCivil";

    /** Columna idPersonaCliente * */
    public static final String TITULO = "titulo";

    /** Columna sucursal * */
    public static final String TRATAMIE = "tratamie";

    /** Columna  noSucursal * */
    public static final String PETDOMIC = "petDomic";

    /** Columna monto  * */
    public static final String IDEDIREC = "ideDirec";

    /** Columna divisa  * */
    public static final String NOMCALLE = "nomCalle";

    /** Columna  tipoPersonaCliente * */
    public static final String NUMCALLE = "numCalle";

    /** Columna  tipoCambioCliente* */
    public static final String TIPOVIVI = "tipoVivi";

    /** Columna  tipoOperacion* */
    public static final String ENTRADA = "entrada";

    /** Columna producto * */
    public static final String PISO = "piso";

    /** Columna cuentaCheques * */
    public static final String DEPARTAM = "departam";

    /** Columna  confirma* */
    public static final String COLONIA = "colonia";

    /** Columna zona * */
    public static final String DELMUNI = "delMuni";

    /** Columna  year* */
    public static final String DESDELMUNI = "desDelMuni";
    
    /** Columna  mes* */
    public static final String ESTADO = "estado";

    /** Columna  semanaDia * */
    public static final String DESESTADO = "desestado";

    /** Columna semana * */
    public static final String CODPOST = "codPost";

    /** Columna diaNumero * */
    public static final String CODPAIS = "codPais";

    /** Columna  direccion* */
    public static final String DIRECCION = "direccion";

    /** Columna nombreCliente * */
    public static final String NOMBRECLIENTE = "nombreCliente";

    /** Columna  tipoCambioMesa* */
    public static final String TIPOCAMBIOMESA = "tipoCambioMesa";
    
    /**
     * Genera el encabezado del Reporte de Layout de Clientes
     *
     * @param b <code>StringBuffer</code>
     * 		que será poblado con el encabezado.
     */
    public void generarEncabezadoArchivoReporteClientes(StringBuffer b);
    
    /**
     * Genera el encabezado del Reporte de Layout de Contratos
     *
     * @param b <code>StringBuffer</code>
     * 		que será poblado con el encabezado.
     */
    public void generarEncabezadoArchivoReporteContratos(StringBuffer b);
    
    /**
     * Genera el encabezado del Reporte de Layout de Transacciones
     *
     * @param b <code>StringBuffer</code>
     * 		que será poblado con el encabezado.
     */
    public void generarEncabezadoArchivoReporteTransacciones(StringBuffer b);


    /**
     * Genera los registros del Reporte de Clientes
     *
     * @param b <code>StringBuffer</code> 
     * que será poblado con los registros.
     *
     * @param resultadoBusqueda <code>List</code>
     * de objetos <code>String</code>
     * que son procesados.
     */
    public void generarRegistrosArchivoClientes(StringBuffer b, List resultadoBusqueda);
    
    /**
     * Genera los registros del Reporte de Contratos
     *
     * @param b <code>StringBuffer</code> 
     * que será poblado con los registros.
     *
     * @param resultadoBusqueda <code>List</code>
     * de objetos <code>String</code>
     * que son procesados.
     */
    public void generarRegistrosArchivoContratos(StringBuffer b, List resultadoBusqueda);
    
    /**
     * Genera los registros del Reporte de Transacciones
     *
     * @param b <code>StringBuffer</code> 
     * que será poblado con los registros.
     *
     * @param resultadoBusqueda <code>List</code>
     * de objetos <code>String</code>
     * que son procesados.
     */
    public void generarRegistrosArchivoTransacciones(StringBuffer b, List resultadoBusqueda);
    
    public File escribeArchivo (String arreglo,String prefijo);
    
    /**
     *@see com.ixe.ods.sica.util.sicareportesLayout.service.LayoutClientes#getNombreArchivoCorte().
     */
    public String getNombreArchivoCorte(String prefijo);
    
    /**
     * Este metodo se encarga de armar el corte y enviarlo a MUREX
     *
     * @param String usuario que realiza la transferencia del archivo csv
     *
     * @return boolean si la transferencia fue exitosa o no.
     *  
     */
    public boolean transferir(String usuario,File archivo, int numRegistros,String prefijo);
    
    /**
     * Este motodo se encarga de armar el layout de Clientes y enviarlo a ARMS
     *
     * @param Date fecha en que se extrae la informacion
     *
     */
    public void obtenerLayoutClientes(Date fechaProceso);
    
    /**
     * Este motodo se encarga de armar el layout de Contratos y enviarlo a ARMS
     *
     * @param Date fecha en que se extrae la informacion
     *
     */
    public void obtenerLayoutContratos(Date fechaProceso);
    
    /**
     * Este motodo se encarga de armar el layout de Transacciones y enviarlo a ARMS
     *
     * @param Date fecha en que se extrae la informacion
     *
     */
    public void obtenerLayoutTransacciones(Date fechaProceso);

    
	//public boolean enviaArchivo(FileInputStream inputStream, String usuarioSICA);
	
	public SicaServiceData getSicaServiceData();
	
	public void setSicaServiceData(SicaServiceData sicaServiceData);
	
	public FTPClient getFtp();

	public void setFtp(FTPClient ftp);

	public String getUsuario();

	public void setUsuario(String usuario);

	public String getContrasena();

	public void setContrasena(String contrasena);
	
	public String getServidorFTP();

	public void setServidorFTP(String servidorFTP);

	public String getRutaServidorFTP();

	public void setRutaServidorFTP(String rutaServidorFTP);

	public int getRespuesta();

	public void setRespuesta(int respuesta);

	public int getTimeOut();

	public void setTimeOut(int timeOut);

	public String getIpServidor();

	public void setIpServidor(String ipServidor);



}

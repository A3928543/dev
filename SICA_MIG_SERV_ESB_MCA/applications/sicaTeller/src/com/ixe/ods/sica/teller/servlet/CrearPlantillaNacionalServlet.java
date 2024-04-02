package com.ixe.ods.sica.teller.servlet;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ixe.fcesb.transformacion.TransformadorException;
import com.ixe.ods.sica.SicaTellerSession;
import com.ixe.ods.sica.SicaTellerSessionHome;
import com.ixe.ods.sica.teller.dto.ResultadoOperacionDto;
import com.ixe.teller.plantillaNacional.CrearPlantillaNacionalIn;
import com.ixe.teller.plantillaNacional.CrearPlantillaNacionalOut;
import com.ixe.teller.plantillaNacional.Response;
import com.ixe.wmi.wmbmessage.header.Error;
import com.ixe.wmi.wmbmessage.header.Header;

import com.ixe.ods.sica.teller.util.transforma.XMLTransferObjet;



/**
 *Clase que crea un Servlet.
 * @author Lorena Bernal
 *
 */
public class CrearPlantillaNacionalServlet
extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
    /**
     *Variable que almacena el xml de respuesta.
     */
    private String xmlResponse;
    /**
     *Variable que almacena los datos de entrada.
     */
    private CrearPlantillaNacionalIn requestBean;
    /**
     *Variable que almacena el jndi.
     */
    private String jndi;
    /**
     *Variable que almacena la url.
     */
    private String url;
    /**
     *Variable que almacena el initialContextFactory.
     */
    private String initialContextFactory;
    /**
     *Variable que inicializa el serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Cadena para salida XML.
     */
    private String SalXML = "text/xml";
    /**
     * Instancia a la clase XMLTransferObject.
     */
    private XMLTransferObjet servicio = new XMLTransferObjet();
    /**
     *Variable que inicializa el logger.
     */
    private static Logger logger = Logger.getLogger(CrearPlantillaNacionalIn.class);
    /**
     * Variable que controla la longitud del mensaje de error
     */
    static final  int corte = 180;
    /**
     * Bandera para error
     */
    private int bandera = 0;
    /**
     * Variable que indica el numero de error
     */
    static final  Integer numError = new Integer(3);
    /**
     *Constructor de la clase.
     */
    public CrearPlantillaNacionalServlet() {
        super();
    }
    /**
     *Metodod que invica a destroy.
     */
    public void destroy() {
        super.destroy();
    }
    /**
     * Método que crea el servlet
     * @param request parámetro de solicitud del Servlet
     * @param response parámetro de respuesta del Servlet
     * @throws ServletException error ServletException
     * @throws IOException error IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
     {
        this.Encabezado();        
        this.loadProperties();
        try {
            
        try {
            
            requestBean = (CrearPlantillaNacionalIn)
            servicio.getTransferObjectfromXML(log(request), CrearPlantillaNacionalIn.class);
            
            generaSal(requestBean, response);
            
            logger.debug("doPost Fin");
        }
        catch (TransformadorException e) {
            try {
                bandera = 1;
                Header head = (Header) requestBean.getHeader();
                response.setContentType(SalXML);
                response.getOutputStream().write(error(head, e).getBytes());
                response.getOutputStream().close();
            } 
            catch (TransformadorException e1) {
                logger.notify();
            }
        } 
        catch (NamingException e) {
            try {
                bandera = 2;
                Header head = (Header) requestBean.getHeader();
                response.setContentType(SalXML);
                response.getOutputStream().write(error(head, e).getBytes());
                response.getOutputStream().close();
            }
            catch (TransformadorException e1) {
                logger.notify();
            }
        }
        catch (CreateException e1) {
            try {
                bandera = 2;
                Header head = (Header) requestBean.getHeader();
                response.setContentType(SalXML);
                response.getOutputStream().write(error(head, e1).getBytes());
                response.getOutputStream().close();
            }
            catch (TransformadorException ee) {
                logger.notify();
            }
        }
         } catch (IOException e){
            try {
                bandera = 2;
                Header head = (Header) requestBean.getHeader();
                response.setContentType(SalXML);
                response.getOutputStream().write(error(head, e).getBytes());
                response.getOutputStream().close();
            }
            catch (TransformadorException e2) {
                logger.notify();
            }
            catch (IOException e2){
                logger.notify();
                }
            }
    }
    /**
     * @throws ServletException error Servlet
     */
    public void init() throws ServletException {
            super.init();
    }
    /**
     *Metodod que inicializa las propiedades.
     */
    private void loadProperties() {
        ResourceBundle rb = ResourceBundle.getBundle("RecursosEJB");
        jndi = rb.getString("jndi");
        url = rb.getString("url");
        initialContextFactory = rb.getString("initialContextFactory");
    }
    /**
     * Metodo que genera el mensaje de error.
     * @param head parámetro del encabezado
     * @param e parámetro de Error
     * @return responseXMLError devuelve el xml de respuesta
     * @throws TransformadorException error TransformadorException 
     */
    private String error(Header head, Exception e)throws TransformadorException {
        XMLTransferObjet servicio = new XMLTransferObjet();
        Error error = new Error();
        error.setNumError(numError);
        error.setMsgError("Error en Servlet Crear Plantilla Nacional");
        
        if (bandera == 1){
        if (e.getMessage().length()>240)
        {
        error.setMsgDesc("Descripcion: " + e.getMessage().substring(corte));
        }
        else error.setMsgDesc("Descripcion: " + e.getMessage());
        }
        if (bandera == 2){
            if (e.getCause().toString().length()>240){
        error.setMsgDesc("Descripcion: " + e.getCause().toString().substring(corte));
        }
        else error.setMsgDesc("Descripcion: "+e.getCause().toString());
        }

        head.setError(error);
        CrearPlantillaNacionalOut out = new CrearPlantillaNacionalOut();
        out.setHeader(head);
        
        String xmlResponseError = (String)
        servicio.getXMLfromTransferObject(out, CrearPlantillaNacionalOut.class);
        xmlResponseError = xmlResponseError.replaceAll("ns1","NS2");
        return xmlResponseError;
    }
    /**
     * 
     * @param requestBean parámetro de solicitud de CrearPlantillaNacionalIn
     * @return respuestaEJB devuelve el resultado del EJB
     * @throws NamingException error NamingException 
     * @throws RemoteException error RemoteException
     * @throws CreateException error CreateException
     */
    private ResultadoOperacionDto InvocaEJB(CrearPlantillaNacionalIn requestBean)
    throws NamingException, RemoteException, CreateException {
        
        Properties properties = new Properties();
        properties.put(Context.PROVIDER_URL, this.url);
        properties.put(Context.INITIAL_CONTEXT_FACTORY, this.initialContextFactory);
        
        Context initialContext = new InitialContext(properties);
        Object ref = initialContext.lookup(this.jndi);
        
        SicaTellerSessionHome sicaTellerHome = (SicaTellerSessionHome)
        PortableRemoteObject.narrow(ref, SicaTellerSessionHome.class);
        
        SicaTellerSession sicaTellerSession = (SicaTellerSession)
        sicaTellerHome.create();
        
        
        Integer idBeneficiario = Integer.valueOf(requestBean.getRequest().getIdBeneficiario() + "");
        
        ResultadoOperacionDto respuestaEJB = (ResultadoOperacionDto)sicaTellerSession.crearPlantillaNacional(
                requestBean.getHeader().getTicket(),
                requestBean.getRequest().getNoCuenta(),
                requestBean.getRequest().getMnemonico(),
                requestBean.getRequest().getClavePlantilla(),
                idBeneficiario.intValue());
        
        return respuestaEJB;
    }
    /**
     * 
     * @param request parámetro de solicitud del Servlet
     * @return requestXML retorna el xml de entrada
     * @throws IOException error IOException 
     */
    private String log(HttpServletRequest request)throws IOException {
    logger.debug("doPost Ini");
    logger.debug("request: " + request);    

    int av = request.getInputStream().available();
    
    logger.debug("available: " + av);
    
    
    byte[] bytes = new byte[av];    
    int i = request.getInputStream().read(bytes);
    
    String requestXML = new String(bytes);
    
    logger.debug("read: " + i);
    logger.debug("requestXML: " + requestXML);
    return requestXML;
        
    }
    /**
     * 
     * @param requestBean parametro solicitud de CrearPlantillaNacionalIn
     * @param response parametro de respuesta del Servlet
     * @throws NamingException error NamingException 
     * @throws CreateException error CreateException
     * @throws TransformadorException error TransformadorException
     * @throws IOException error IOException
     */
    private void generaSal(CrearPlantillaNacionalIn requestBean, HttpServletResponse response)
    throws NamingException, CreateException, TransformadorException, IOException {
        Integer respuesta = (Integer) InvocaEJB(requestBean).getObjeto();
        CrearPlantillaNacionalOut out = new CrearPlantillaNacionalOut();
        int codigo = InvocaEJB(requestBean).getCodigo();
        if (codigo >= 0){
        out.setHeader(requestBean.getHeader());
        Response responseXsd = new Response();
        responseXsd.setIdPlantilla(respuesta.longValue());
        out.setResponse(responseXsd);
        }
        else{
            Header hed = (Header) requestBean.getHeader();
            Error error = new Error();
            error.setNumError(numError);
            error.setMsgError("Codigo error: "+InvocaEJB(requestBean).getCodigo());
            error.setMsgDesc("Mensaje: "+InvocaEJB(requestBean).getDescripcion());
            hed.setError(error);
            out.setHeader(requestBean.getHeader());
            }
        xmlResponse = (String)
        servicio.getXMLfromTransferObject(out, CrearPlantillaNacionalOut.class);
        xmlResponse = xmlResponse.replaceAll("ns1","NS2");
        response.setContentType(SalXML);
        response.getOutputStream().write(xmlResponse.getBytes());
        response.getOutputStream().close();    
    }
/**
     * Metodo que inicializa el encabezado
     */        
    private void Encabezado(){
        Header head = new Header();
        head.setAreaUsuario("ErrorArea");
        head.setNombreAplic("ErrorAplic");
        head.setTicket("1234567890123456789012");
        head.setUsuario("Error");
        head.setVersionHeader("1.1.0");
        head.setVersionServicio("10.00.00");
        }
        
    
}

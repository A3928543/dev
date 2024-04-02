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
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ixe.fcesb.transformacion.TransformadorException;
import com.ixe.ods.sica.SicaTellerSession;
import com.ixe.ods.sica.SicaTellerSessionHome;
import com.ixe.ods.sica.teller.dto.ResultadoOperacionDto;
import com.ixe.sica.teller.plantillaInternacional.CrearPlantillaInternacionalIn;
import com.ixe.sica.teller.plantillaInternacional.CrearPlantillaInternacionalOut;
import com.ixe.sica.teller.plantillaInternacional.Response;
import com.ixe.wmi.wmbmessage.header.Error;
import com.ixe.wmi.wmbmessage.header.Header;

import com.ixe.ods.sica.teller.util.transforma.XMLTransferObjet;

/**
 * Clase que crea un Servlet.
 * @author Lorena Bernal
 *
 */
public class CrearPlantillaInternacionalServlet extends HttpServlet implements Servlet
{

    /**
     *Inicializa la serialversionUID.
     */
    private static final long serialVersionUID = 1L;
    /**
     *Inicializa el Logger.
     */
    private static Logger logger =
        Logger.getLogger(CrearPlantillaInternacionalServlet.class);
    /**
     *Variable que hace referencia a la url del ejb.
     */
    private String url;
    /**
     *Variable que hace referencia al jndi.
     */
    private String jndi;
    /**
     *Variable que hace referencia a initialContextFactory.
     */
    private String initialContextFactory;
    /**
     *Variable donde se almacena el mensaje de respuesta XML.
     */
    private String responseXML;
    /**
     * Cadena para la sida xml
     */
    private String salXML = "text/xml";
    /**
     * Variable que controla la longitud del mensaje de error
     */
    static final  int corte = 180;
    /**
     * Variable que indica el numero de error
     */
    static final  Integer numError = new Integer(3);
    /**
     * Bandera para error 
     */
    private int bandera = 0;
    /**
     *Variable que hace referencia a los metodos set y get
     *de Plantilla Internacional.
     */
    private CrearPlantillaInternacionalIn requestBean;
    /**
     * Invica a la clase XMLTransferObject
     */
    private XMLTransferObjet serv = new XMLTransferObjet();
    /**
     * Constructor de la clase
     */
    public CrearPlantillaInternacionalServlet() {
        super();
    }
    /**
     * Método que crea el servlet
     * @param request parámetro de solicitud del Servlet
     * @param response parámetro de respuesta del Servlet
     * @throws ServletException error ServletException
     * @throws IOException error IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        
        this.loadProperties();   
        this.Encabezado();
        
        try{
        try {
            
            requestBean = (CrearPlantillaInternacionalIn)
            serv.getTransferObjectfromXML(log(request), CrearPlantillaInternacionalIn.class);
            
            generaSal(requestBean, response);
            
            logger.debug("doPost Fin");
        } 
        catch (TransformadorException trans) {
            try {
                bandera = 1;
                Header head = (Header) requestBean.getHeader();
                response.setContentType(salXML);
                response.getOutputStream().write(error(head, trans).getBytes());
                response.getOutputStream().close();
            }
            catch (TransformadorException e) {
                logger.notify();
            }
        }
        catch (NamingException e) {
            try {
                bandera = 2;
                Header head = (Header) requestBean.getHeader();
                response.setContentType(salXML);
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
                response.setContentType(salXML);
                response.getOutputStream().write(error(head, e1).getBytes());
                response.getOutputStream().close();
            }
            catch (TransformadorException e2) {
                logger.notify();
            }
        }
        } catch (IOException e){
            try {
                bandera = 2;
                Header head = (Header) requestBean.getHeader();
                response.setContentType(salXML);
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
    public void init()throws ServletException {
        super.init();
    }
    
    /**
     *Meotodo que incializa las propiedades.
     */
    private void loadProperties() {
        ResourceBundle prop = ResourceBundle.getBundle("RecursosEJB");
        jndi = prop.getString("jndi");
        url = prop.getString("url");
        initialContextFactory = prop.getString("initialContextFactory");
    }
    
    /**
     * Metodo que genera el mensaje de error.
     * @param head parámetro del encabezado
     * @param e parámetro de Error
     * @return responseXMLError devuelve el xml de respuesta
     * @throws TransformadorException error TransformadorException 
     */

    private String error(Header head, Exception e)throws TransformadorException {
        XMLTransferObjet servicioE = new XMLTransferObjet();
        Error error = new Error();
        error.setNumError(numError);
        error.setMsgError("Error en el Servlet Crear Plantilla Internacional");
        
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
        CrearPlantillaInternacionalOut out = new CrearPlantillaInternacionalOut();
        out.setHeader(head);
        String xmlResponseError = (String)
        servicioE.getXMLfromTransferObject(out, CrearPlantillaInternacionalOut.class);
        xmlResponseError = xmlResponseError.replaceAll("ns1","NS2");
        return xmlResponseError;
    }
    
    /**
     * 
     * @param requestBean1 parámetro de solicitud de CrearPlantillaNacionalIn
     * @return respuestaEJB devuelve el resultado del EJB
     * @throws NamingException error NamingException 
     * @throws RemoteException error RemoteException
     * @throws CreateException error CreateException
     */
    private ResultadoOperacionDto InvocaEJB(CrearPlantillaInternacionalIn requestBean1)
    throws NamingException, RemoteException, CreateException  {
        Properties properties = new Properties();
        
        properties.put(Context.PROVIDER_URL, this.url);
        properties.put(Context.INITIAL_CONTEXT_FACTORY, this.initialContextFactory);
        Context initialContext = new InitialContext(properties);
        
        Object sicaTellerRemoteHome = initialContext.lookup(this.jndi);
        
        SicaTellerSessionHome sicaTH = (SicaTellerSessionHome)
        PortableRemoteObject.narrow(sicaTellerRemoteHome, SicaTellerSessionHome.class);
        
        SicaTellerSession sicaTellerSession = (SicaTellerSession) sicaTH.create();
        
        Integer idBene = Integer.valueOf(requestBean.getRequest().getIdBeneficiario() + "");
        
        ResultadoOperacionDto resEJB = (ResultadoOperacionDto)sicaTellerSession.crearPlantillaInternacional(
                requestBean1.getHeader().getTicket(),
                requestBean1.getRequest().getMnemonico(),
                requestBean1.getRequest().getNoCuenta(),
                requestBean1.getRequest().getNoCuentaBeneficiario(),
                idBene.intValue(),
                requestBean1.getRequest().getIdDivisa(),
                requestBean1.getRequest().getTipoCuentaBanco(),
                requestBean1.getRequest().getClaveBanco(),
                requestBean1.getRequest().getNombreBanco(),
                requestBean1.getRequest().getClaveIsoPais(),
                requestBean1.getRequest().getTipoCuentaBancoInterm(),
                requestBean1.getRequest().getClaveBancoInterm(),
                requestBean1.getRequest().getNombreBancoInterm(),
                requestBean1.getRequest().getCuentaBancoInterm(),
                requestBean1.getRequest().getInstruccionesBeneficiario(),
                requestBean1.getRequest().getInstruccionesIntermediario(),
                requestBean1.getRequest().getClavePlantilla()
        );
        return resEJB;
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
     * @param requestBean parametro solicitud de CrearPlantillaInternacionalIn
     * @param response parametro de respuesta del Servlet
     * @throws NamingException error NamingException 
     * @throws CreateException error CreateException
     * @throws TransformadorException error TransformadorException
     * @throws IOException error IOException
     */
    private void generaSal(CrearPlantillaInternacionalIn requestBean, HttpServletResponse response)
    throws NamingException, CreateException, TransformadorException, IOException {
        Integer respuesta = (Integer) InvocaEJB(requestBean).getObjeto();
        CrearPlantillaInternacionalOut out =  new CrearPlantillaInternacionalOut();
        int codigo = InvocaEJB(requestBean).getCodigo();
        if(codigo >= 0){
        out.setHeader(requestBean.getHeader());
        Response res = new Response();
        res.setIdPlantilla(respuesta.longValue());
        out.setResponse(res);
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
        responseXML = (String)
        serv.getXMLfromTransferObject(out, CrearPlantillaInternacionalOut.class);
        responseXML = responseXML.replaceAll("ns1","NS2");
        response.setContentType(salXML);
        response.getOutputStream().write(responseXML.getBytes());
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
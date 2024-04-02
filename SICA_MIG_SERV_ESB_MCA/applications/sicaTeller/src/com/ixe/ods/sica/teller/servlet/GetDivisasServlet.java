package com.ixe.ods.sica.teller.servlet; 

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
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
import com.ixe.ods.sica.teller.dto.DivisaDto;
import com.ixe.ods.sica.teller.dto.ResultadoOperacionDto;
import com.ixe.sica.teller.obtDivisas.Divisa;
import com.ixe.sica.teller.obtDivisas.Divisas;
import com.ixe.sica.teller.obtDivisas.GetDivisasIn;
import com.ixe.sica.teller.obtDivisas.GetDivisasOut;
import com.ixe.sica.teller.obtDivisas.types.TipoDivisaType;
import com.ixe.sica.teller.obtDivisas.Response;
import com.ixe.wmi.wmbmessage.header.Error;
import com.ixe.wmi.wmbmessage.header.Header;

import com.ixe.ods.sica.teller.util.transforma.XMLTransferObjet;
/**
 * Clase que crea al Servlet
 * @author Carlos Montero
 *
 */
public class GetDivisasServlet extends HttpServlet implements Servlet {  
    /**
     * Variable que almacena los objetos
     */
    private GetDivisasIn requestBean;
    /**
     * Cadena de salida para el mensaje de respuesta
     */
    private String xmlResponse;
    /**
     * Variable que almacena el jndi
     */
    private String jndi;
    /**
     * Variable que almacena la url
     */
    private String url;
    /**
     * Variable que almacena al initialContextFactory
     */
    private String initialContextFactory;
    /**
     * Cadena respuesta del XML
     */
    private String salXML = "text/xml";
    /**
     * Variable que controla la longitud del mensaje de error
     */
    static final  int corte = 180;
    /**
     * Badera para el error
     */
    private int bandera = 0;
    /**
     * Variable que indica el numero del error
     */
    static final Integer numError = new Integer(3);
    /**
     * Variable que inicializa el serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * Instancia a la clase XMLTransferObjet
     */
    private XMLTransferObjet servicio = new XMLTransferObjet();
    /**
     * Instancia a Logger
     */
    private static Logger logger = Logger.getLogger(GetDivisasServlet.class);
    /**
     * Constructor de la clase   
     */
    public GetDivisasServlet() {
        super();
    }
    
    /**
     * Invoca a destroy
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {	
        
        this.loadProperties();
        this.Encabezado();
        try {
        try {
            
            requestBean = (GetDivisasIn)
            servicio.getTransferObjectfromXML(log(request), GetDivisasIn.class);
            
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
    public void init() throws ServletException {
        super.init();
    }  
    /**
     * Método que inicializa a las propiedades
     */
    public void loadProperties() {
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
        XMLTransferObjet servicioE = new XMLTransferObjet();
        Error error = new Error();
        error.setNumError(numError);
        error.setMsgError("Error en el Servlet GetDivisasServlet");
        
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
        GetDivisasOut out = new GetDivisasOut();
        out.setHeader(head);
        String xmlResponseError = (String)
        servicioE.getXMLfromTransferObject(out, GetDivisasOut.class);
        xmlResponseError = xmlResponseError.replaceAll("ns1","NS2");
        return xmlResponseError;
    }
    
    /**
     * 
     * @param requestBean parámetro de solicitud de GetDivisasIn
     * @return respuestaEJB devuelve el resultado del EJB
     * @throws NamingException error NamingException 
     * @throws RemoteException error RemoteException
     * @throws CreateException error CreateException
     */
    private ResultadoOperacionDto InvocaEJB(GetDivisasIn requestBean)
    throws NamingException, RemoteException, CreateException  {
        Properties properties = new Properties();
        properties.put(Context.PROVIDER_URL, this.url);
        properties.put(Context.INITIAL_CONTEXT_FACTORY, this.initialContextFactory);
        
        InitialContext jndiContext = new InitialContext(properties);
        Object ref = jndiContext.lookup(this.jndi); 
        
        SicaTellerSessionHome sicaTellerHome = (SicaTellerSessionHome)
        PortableRemoteObject.narrow(ref, SicaTellerSessionHome.class);
        SicaTellerSession sicaTellerSession = (SicaTellerSession)
        sicaTellerHome.create();          
        
        String tikect = requestBean.getHeader().getTicket();
        String Tipo = (String) requestBean.getRequest().getTipoDivisa().toString();
        ResultadoOperacionDto respuestaEJB = (ResultadoOperacionDto)sicaTellerSession.getDivisas(tikect, Tipo);
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
     * @param requestBean parametro solicitud de GetDivisasIn
     * @param response parametro de respuesta del Servlet
     * @throws NamingException error NamingException 
     * @throws CreateException error CreateException
     * @throws TransformadorException error TransformadorException
     * @throws IOException error IOException
     */
    private void generaSal(GetDivisasIn requestBean, HttpServletResponse response)
    throws NamingException, CreateException, TransformadorException, IOException {
        
        GetDivisasOut out = new GetDivisasOut();
        
        
        int codigo = InvocaEJB(requestBean).getCodigo();
        if(codigo >= 0){
        List respuesta = (List) InvocaEJB(requestBean).getObjeto();
        
        Iterator iterador = respuesta.iterator();
        
        com.ixe.sica.teller.obtDivisas.Divisas divisas= new com.ixe.sica.teller.obtDivisas.Divisas();
        while (iterador.hasNext()) {
            
            DivisaDto objetoEJB = (DivisaDto) iterador.next(); 
            
            Divisa divisa = new Divisa();
            
            divisa.setIdDivisa(objetoEJB.getIdDivisa());
            divisa.setDescripcion(objetoEJB.getDescripcion());
            divisa.setIdMoneda(objetoEJB.getIdMoneda());
            divisa.setTipoDivisa(TipoDivisaType.valueOf(objetoEJB.getTipo()));
            divisas.addDivisa(divisa);
        }
        
        out.setHeader(requestBean.getHeader());
        Response res = new Response();
        res.setDivisas(divisas);
        out.setResponse(res);
        }
        else
        {
            Header hed = (Header) requestBean.getHeader();
            Error error = new Error();
            error.setNumError(numError);
            error.setMsgError("Codigo error: "+InvocaEJB(requestBean).getCodigo());
            error.setMsgDesc("Mensaje: "+InvocaEJB(requestBean).getDescripcion());
            hed.setError(error);
            out.setHeader(requestBean.getHeader());
            }
        
        xmlResponse = (String) servicio.getXMLfromTransferObject(out, GetDivisasOut.class);
        xmlResponse = xmlResponse.replaceAll("ns1","NS2");
        response.setContentType(salXML);
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


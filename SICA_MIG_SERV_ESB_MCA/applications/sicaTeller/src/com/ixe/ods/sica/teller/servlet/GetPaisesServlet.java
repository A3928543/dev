package com.ixe.ods.sica.teller.servlet;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
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
import com.ixe.ods.sica.teller.dto.ResultadoOperacionDto;
import com.ixe.teller.getPaises.GetPaisesIn;
import com.ixe.teller.getPaises.GetPaisesOut;
import com.ixe.teller.getPaises.Pais;
import com.ixe.teller.getPaises.Paises;
import com.ixe.teller.getPaises.Response;
import com.ixe.wmi.wmbmessage.header.Error;
import com.ixe.wmi.wmbmessage.header.Header;

import com.ixe.ods.sica.teller.util.transforma.XMLTransferObjet;




/**
 * Clase que genera servlet.
 * @author Lorena Bernal
 *
 */
public class GetPaisesServlet extends HttpServlet implements Servlet {
    /**
    * Variable que inicializa el serialVersionUID.
    */
    private static final long serialVersionUID = 1L;

    /**
     * Variable que inicializa el logger.
     */
    private static Logger logger = Logger.getLogger(GetPaisesIn.class);

    /**
     * Variable que almacena el xml de respuesta.
     */
    private String xmlResponse;

    /**
     * Variable que almacena los datos de Getpaises.
     */
    private GetPaisesIn requestBean;

    /**
     * Variable que almacena el jndi.
     */
    private String jndi;

    /**
     * Variable que almacena la url.
     */
    private String url;
    /**
     * Cadena de xml de salida
     */
    private String salXML = "text/xml";
    /**
     * Instancia a  clase TrasnsferObject
     */
     private XMLTransferObjet servicio = new XMLTransferObjet();
    /**
     * Variable que almacena el initialContextFactory.
     */
    private String initialContextFactory;
    /**
     * Variable que controla la longitud del mensaje de error
     */
    static final  int corte = 180;
    /**
     * Bandera para el error
     */
    private int bandera = 0;
    /**
     * Variable que indica el numero de error
     */
    static final Integer numError = new Integer(3);
    /**
     * Constructor de la clase.
     */
    public GetPaisesServlet() {
        super();
    }

    /**
     * Invoca a destroy.
     */
    public void destroy() {
        super.destroy();
    }

    /**
     * Metodo que crea el servlet.
     * @param request parámetro de solicitud del Servlet
     * @param response parámetro de respuesta del Servlet
     * @throws ServletException error ServletException
     * @throws IOException error IOException
     */

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        /**
        * Obtener XML de Request
        */
        this.loadProperties();
        this.Encabezado();
        try{
        try {

            requestBean = (GetPaisesIn) servicio.getTransferObjectfromXML(log(request),
                    GetPaisesIn.class);

            generaSal(requestBean, response);

            logger.debug("doPost Fin");
            
        }
        catch (TransformadorException e) {
                try {
                    bandera = 1;
                    Header head = (Header) requestBean.getHeader();
                    response.setContentType(salXML);
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
                    response.setContentType(salXML);
                    response.getOutputStream().write(error(head, e).getBytes());
                    response.getOutputStream().close();
                } 
                catch (TransformadorException e1) {
                    logger.notify();
                }
            } 
            catch (CreateException e) {
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
     * @throws ServletException error de Servlet
     */
    public void init() throws ServletException {
        super.init();
    }

    /**
     * Metodo que inicializa a las propiedades.
     */
    private void loadProperties() {
        ResourceBundle rb = ResourceBundle.getBundle("RecursosEJB");
        jndi = rb.getString("jndi");
        url = rb.getString("url");
        initialContextFactory = rb.getString("initialContextFactory");
    }

    /**
     * Metododo que crea el mensaje de error.
     * @param head parámetro del encabezado
     * @param e parámetro de Error
     * @return responseXMLError devuelve el xml de respuesta
     * @throws TransformadorException error TransformadorException
     */
    private String error(Header head, Exception e) throws TransformadorException {
        XMLTransferObjet servicio = new XMLTransferObjet();
        Error error = new Error();
        error.setNumError(numError);
        error.setMsgError("Error en Servlet Get Paises ");
       
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

        GetPaisesOut out = new GetPaisesOut();
        out.setHeader(head);

        String xmlResponseError = (String) servicio.getXMLfromTransferObject(out,
                GetPaisesOut.class);
        xmlResponseError = xmlResponseError.replaceAll("ns1","NS2");
        return xmlResponseError;
    }
    
    /**
     * 
     * @param requestBean parámetro de solicitud de GetPaises
     * @return respuestaEJB devuelve el resultado del EJB
     * @throws NamingException error NamingException 
     * @throws RemoteException error RemoteException
     * @throws CreateException error CreateException
     */
    private ResultadoOperacionDto InvocaEJB(GetPaisesIn requestBean)
    throws NamingException, RemoteException, CreateException {
        Properties properties = new Properties();
        properties.put(Context.PROVIDER_URL, this.url);
        properties.put(Context.INITIAL_CONTEXT_FACTORY,
            this.initialContextFactory);

        InitialContext jndiContext = new InitialContext(properties);
        Object ref = jndiContext.lookup(this.jndi);

        SicaTellerSessionHome sicaTellerHome = (SicaTellerSessionHome) 
        PortableRemoteObject.narrow(ref,
                SicaTellerSessionHome.class);
        SicaTellerSession sicaTellerSession = (SicaTellerSession) sicaTellerHome.create();

        ResultadoOperacionDto respuestaEJB = (ResultadoOperacionDto)sicaTellerSession.getPaises(
                                              requestBean.getHeader().getTicket());
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
     * @param requestBean parametro de solicitud de GetPaises
     * @param response parámetro de respuesta del Servlet
     * @throws NamingException error NamingException
     * @throws CreateException error CreateException
     * @throws TransformadorException error TransformadorException
     * @throws IOException error IOException
     */
    private void generaSal(GetPaisesIn requestBean, HttpServletResponse response)
    throws NamingException, CreateException, TransformadorException, IOException {
        List respuesta = (List) InvocaEJB(requestBean).getObjeto();
        GetPaisesOut out = new GetPaisesOut();
        int codigo = InvocaEJB(requestBean).getCodigo();
        if (codigo >= 0){
        out.setHeader(requestBean.getHeader());

        Iterator iterator = respuesta.iterator();
        Paises paises = new Paises();

        while (iterator.hasNext()) {
            HashMap miMapa = (HashMap) iterator.next();
            Pais pais = new Pais();
            pais.setClaveISO((String) miMapa.get("claveISO"));
            pais.setNombrePais((String) miMapa.get("nombrePais"));
            paises.addPais(pais);
        }
        Response respais= new Response();
        respais.setPaises(paises);
        out.setResponse(respais);
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
        xmlResponse = (String) servicio.getXMLfromTransferObject(out,
                GetPaisesOut.class);
        response.setContentType(salXML);
        xmlResponse = xmlResponse.replaceAll("ns1","NS2");
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

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
import com.ixe.ods.sica.teller.dto.BancoInternacionalDto;
import com.ixe.ods.sica.teller.dto.ResultadoOperacionDto;
import com.ixe.sica.teller.bancoInterNomPais.Banco;
import com.ixe.sica.teller.bancoInterNomPais.Bancos;
import com.ixe.sica.teller.bancoInterNomPais.GetBancosInternacionalesByNombrePaisIn;
import com.ixe.sica.teller.bancoInterNomPais.GetBancosInternacionalesByNombrePaisOut;
import com.ixe.sica.teller.bancoInterNomPais.Response;
import com.ixe.wmi.wmbmessage.header.Error;
import com.ixe.wmi.wmbmessage.header.Header;

import com.ixe.ods.sica.teller.util.transforma.XMLTransferObjet;

/**
 * Clase para crear un servlet.
 * @author Lorena Bernal
 *
 */
public class GetBancosInternacionalesByNombrePaisServlet extends HttpServlet
implements Servlet {
    /**
     *Variable que inicializa el serialVersionUID.
     */
    //private static final long serialVersionUID = 1L;
    /**
     * Variable que inicializa el logger.
     */
    private static Logger logger =
        Logger.getLogger(CrearPlantillaInternacionalServlet.class);
    /**
     * Variable que almacena la url.
     */
    private String url;
    /**
     * Variable que almacena el jndi.
     */
    private String jndi;
    /**
     * Variable que almacena el initialContextFactory.
     */
    private String initialContextFactory;
    /**
     * Variable que almacena el xml de respuesta.
     */
    private String responseXML;
    /**
     * 
     */
    private String SalXML = "text/xml";
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
     * Vraible que almacena los datos de GetBancosInternacionales.
     */
    private GetBancosInternacionalesByNombrePaisIn requestBean;
    /**
     * 
     */
    private XMLTransferObjet servicio = new XMLTransferObjet();
    /**
     * Constructor de la clase.
     */
    public GetBancosInternacionalesByNombrePaisServlet() {
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
        try {
        try {
            
            requestBean = (GetBancosInternacionalesByNombrePaisIn)
            
            servicio.getTransferObjectfromXML(log(request), 
                    GetBancosInternacionalesByNombrePaisIn.class);
            
            generaSal(requestBean, response);
            
            logger.debug("doPost Fin");    
            
        }
        catch (TransformadorException trans) {
            try {
                bandera = 1;
                Header head = (Header) requestBean.getHeader();
                response.setContentType(SalXML);
                response.getOutputStream().write(Error(head, trans).getBytes());
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
                response.setContentType(SalXML);
                response.getOutputStream().write(Error(head, e).getBytes());
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
                response.setContentType(SalXML);
                response.getOutputStream().write(Error(head, e).getBytes());
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
                response.setContentType(SalXML);
                response.getOutputStream().write(Error(head, e).getBytes());
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
     * Metodo que invoca a destroy.
     */
    public void destroy() {
        super.destroy();
    }
    /**
     * @throws ServletException error Servlet
     */
    public void init() throws ServletException {
            super.init();
    }
    /**
     * Metodo que inicializa las propiedades.
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
    public String Error(Header head, Exception e)throws TransformadorException {
        XMLTransferObjet servicioError = new XMLTransferObjet();
        Error error = new Error();
        error.setNumError(numError);
        error.setMsgError("Error en el Servlet GetBancosInternacionalesByNombrePais");
        
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
        GetBancosInternacionalesByNombrePaisOut out =
            new GetBancosInternacionalesByNombrePaisOut();
        out.setHeader(head);
        String responseXMLError = (String)
        servicioError.getXMLfromTransferObject(out,
                GetBancosInternacionalesByNombrePaisOut.class);
        responseXMLError = responseXMLError.replaceAll("ns1","NS2");
        return responseXMLError;
    }
    /**
     * 
     * @param requestBean parámetro de solicitud de GetBancosInternacionalesByNombrePaisIn
     * @return respuestaEJB devuelve el resultado del EJB
     * @throws NamingException error NamingException 
     * @throws RemoteException error RemoteException
     * @throws CreateException error CreateException
     */
    private ResultadoOperacionDto InvocaEJB(GetBancosInternacionalesByNombrePaisIn requestBean)
    throws NamingException, RemoteException, CreateException {
        Properties properties = new Properties();
        
        properties.put(Context.PROVIDER_URL, this.url);
        properties.put(Context.INITIAL_CONTEXT_FACTORY, this.initialContextFactory);
        
        Context initialContext = new InitialContext(properties);
        
        Object sicaTellerRemoteHome = initialContext.lookup(this.jndi);
        
        SicaTellerSessionHome sicaTellerHome = (SicaTellerSessionHome)
        PortableRemoteObject.narrow(sicaTellerRemoteHome, SicaTellerSessionHome.class);
        
        SicaTellerSession sicaTS = (SicaTellerSession)
        sicaTellerHome.create();
        
        ResultadoOperacionDto resEJB = (ResultadoOperacionDto)sicaTS.getBancosInternacionalesByNombrePais(
                requestBean.getHeader().getTicket(),
                requestBean.getRequest().getNombreBanco(),
                requestBean.getRequest().getClavePais());
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
     * @param requestBean parametro solicitud de GetBancosInternacionalesByNombrePaisIn
     * @param response parametro de respuesta del Servlet
     * @throws NamingException error NamingException 
     * @throws CreateException error CreateException
     * @throws TransformadorException error TransformadorException
     * @throws IOException error IOException
     */
    private void generaSal(GetBancosInternacionalesByNombrePaisIn requestBean, 
            HttpServletResponse response)
    throws NamingException, CreateException, TransformadorException, IOException {
        
        GetBancosInternacionalesByNombrePaisOut out =
            new GetBancosInternacionalesByNombrePaisOut();
        int codigo = InvocaEJB(requestBean).getCodigo();
        if(codigo >= 0){
        List respuesta = (List) InvocaEJB(requestBean).getObjeto();
        Iterator iterador = respuesta.iterator();
        out.setHeader(requestBean.getHeader());
        
        Bancos bancos = new Bancos();
        Response res = new Response();
        while (iterador.hasNext()) {
            
            BancoInternacionalDto objetoEJB = (BancoInternacionalDto) iterador.next();
            
            Banco banco = new Banco();
            banco.setBancoPequeno(objetoEJB.isBancoPequeno());
            banco.setClaveAba(objetoEJB.getClaveAba());
            banco.setClaveBanxico(objetoEJB.getClaveBanxico());
            banco.setClaveChips(objetoEJB.getClaveChips());
            banco.setClaveRuteoPais(objetoEJB.getClaveRuteoPais());
            banco.setClaveSwift(objetoEJB.getClaveSwift());
            banco.setEstado(objetoEJB.getEstado());
            banco.setIdBanco(objetoEJB.getIdBanco().longValue());
            banco.setIdBancoPhoenix(objetoEJB.getIdBancoPhoenix());
            banco.setIdPais(objetoEJB.getIdPais());
            banco.setNombreCorto(objetoEJB.getNombreCorto());
            banco.setPlaza(objetoEJB.getPlaza());
            banco.setStatus(objetoEJB.getStatus());
            bancos.addBanco(banco);
        }
        res.setBancos(bancos);
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
        servicio.getXMLfromTransferObject(out,
                GetBancosInternacionalesByNombrePaisOut.class);
        responseXML = responseXML.replaceAll("ns1","NS2");
        response.setContentType(SalXML);
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
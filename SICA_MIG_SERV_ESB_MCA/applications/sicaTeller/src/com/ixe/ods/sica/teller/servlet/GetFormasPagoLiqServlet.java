package com.ixe.ods.sica.teller.servlet; 

import java.io.IOException;
import java.math.BigDecimal;
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
import com.ixe.ods.sica.teller.dto.FormaPagoLiqDto;
import com.ixe.ods.sica.teller.dto.ResultadoOperacionDto;
import com.ixe.sica.teller.pagoLiq.Forma;
import com.ixe.sica.teller.pagoLiq.Formas;
import com.ixe.sica.teller.pagoLiq.GetFormasPagoLiqIn;
import com.ixe.sica.teller.pagoLiq.GetFormasPagoLiqOut;
import com.ixe.sica.teller.pagoLiq.Response;
import com.ixe.wmi.wmbmessage.header.Error;
import com.ixe.wmi.wmbmessage.header.Header;

import com.ixe.ods.sica.teller.util.transforma.XMLTransferObjet;
/**
 * Clase que genera el servlet.
 * @author Lorena Bernal
 *
 */
public class GetFormasPagoLiqServlet extends HttpServlet implements Servlet {
    /**
    * Variable que inicializa el serialVersionUID.
    */
    private static final long serialVersionUID = 1L;

    /**
     * Variable que inicializa el logger.
     */
    private static Logger logger = Logger.getLogger(CrearPlantillaInternacionalServlet.class);

    /**
     * Variable que almacena la url.
     */
    private String url;

    /**
     * Variable que almacena el jndi.
     */
    private String jndi;

    /**
     * Varaible que almacena el initialContextFactory.
     */
    private String initialContextFactory;

    /**
     * Variable que almacena el xml de respuesta.
     */
    private String responseXML;
    /**
     * Cadena para el xml de salida
     */
    private String salXML = "text/xml";
    /**
     * Variable que controla la longitud del mensaje de error
     */
    static final  int corte = 180;
    /**
     * Bandera para error
     */
    private int bandera = 0;
    /**
     * Variable que indica el numero del error
     */
    static final Integer numError = new Integer(3);
    /**
     * Instancia a la clase XMLTransferObject
     */
    private XMLTransferObjet servicio = new XMLTransferObjet();
      /**
     * Variable que almacene los datos de GetFormaPagoLiqIn.
     */
    private GetFormasPagoLiqIn requestBean;
    /**
     * Constructor de la clase.
     */
    public GetFormasPagoLiqServlet() {
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
            
            requestBean = (GetFormasPagoLiqIn) servicio.getTransferObjectfromXML(log(request),
                    GetFormasPagoLiqIn.class);

            generaSal(requestBean, response);
            
            logger.debug("doPost Fin");
            
        }
        catch (TransformadorException trans) {
            try {
                bandera = 1;
                Header head = (Header) requestBean.getHeader();
                response.setContentType(salXML);
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
                response.setContentType(salXML);
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
                response.setContentType(salXML);
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
                response.setContentType(salXML);
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
     * Invoca a destroy.
     */
    public void destroy() {
        super.destroy();
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
    private String Error(Header head, Exception e)
        throws TransformadorException {
        XMLTransferObjet servicioError = new XMLTransferObjet();
        Error error = new Error();
        error.setNumError(numError);
        error.setMsgError("Ocurrio un error en el Servlet Get Formas Pago Liq");
        
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

        GetFormasPagoLiqOut out = new GetFormasPagoLiqOut();
        out.setHeader(head);

        String responseXMLError = (String) servicioError.getXMLfromTransferObject(out,
                GetFormasPagoLiqOut.class);
        responseXMLError = responseXMLError.replaceAll("ns1","NS2");
        return responseXMLError;
    }
    /**
     * 
     * @param requestBean parámetro de solicitud de GetFormasPagoLiqIn
     * @return respuestaEJB devuelve el resultado del EJB
     * @throws NamingException error NamingException 
     * @throws RemoteException error RemoteException
     * @throws CreateException error CreateException
     */
    private ResultadoOperacionDto InvocaEJB(GetFormasPagoLiqIn requestBean)
    throws NamingException, RemoteException, CreateException {
        Properties properties = new Properties();
        properties.put(Context.PROVIDER_URL, this.url);
        properties.put(Context.INITIAL_CONTEXT_FACTORY,
            this.initialContextFactory);

        Context initialContext = new InitialContext(properties);

        Object sicaTellerRemoteHome = initialContext.lookup(this.jndi);

        SicaTellerSessionHome sicaTellerHome = (SicaTellerSessionHome) 
        PortableRemoteObject.narrow(sicaTellerRemoteHome,
                SicaTellerSessionHome.class);

        SicaTellerSession sicaTellerSession = (SicaTellerSession) sicaTellerHome.create();

        ResultadoOperacionDto respuestaEJB = (ResultadoOperacionDto)sicaTellerSession.getFormasPagoLiq(
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
     * @param requestBean parametro solicitud de GetFormasPagoLiq
     * @param response parametro de respuesta del Servlet
     * @throws NamingException error NamingException 
     * @throws CreateException error CreateException
     * @throws TransformadorException error TransformadorException
     * @throws IOException error IOException
     */
    /**
     * Instancia a la clase forma
     */
    private Forma forma = new Forma();
    /**
     * Instancia a la clase formas
     */
    private Formas formas = new Formas();
    /**
     * 
     * @param requestBean parametro de solicitud de GetFormasPagoLiq
     * @param response parámetro de respuesta del Servlet
     * @throws NamingException error NamingException
     * @throws CreateException error CreateException
     * @throws TransformadorException error TransformadorException
     * @throws IOException error IOException
     */
    private void generaSal(GetFormasPagoLiqIn requestBean, HttpServletResponse response)
    throws NamingException, CreateException, TransformadorException, IOException {
        
        GetFormasPagoLiqOut out = new GetFormasPagoLiqOut();
        int codigo = InvocaEJB(requestBean).getCodigo();
        if (codigo >= 0){
        List respuesta = (List) InvocaEJB(requestBean).getObjeto();
        Iterator iterador = respuesta.iterator();
        out.setHeader(requestBean.getHeader());
        
        while (iterador.hasNext()) {
            FormaPagoLiqDto objetoEJB = (FormaPagoLiqDto) iterador.next();
            String comision = String.valueOf(objetoEJB.getComision() + "");
            String montoMaximo = objetoEJB.getMontoMaximo().toString();
            String montoMinimo = objetoEJB.getMontoMinimo().toString();
            String operMin = objetoEJB.getOperacionMinima().toString();
            BigDecimal multiplo = new BigDecimal(objetoEJB.getMultiplo()
                                                          .doubleValue());
            
            forma.setNombreBanco(objetoEJB.getNombreBanco());

            forma.setClaveFormaLiquidacion(objetoEJB.getClaveFormaLiquidacion());
            forma.setComision(comision);
            forma.setDescripcion(objetoEJB.getDescripcion());
            forma.setIdBanco(objetoEJB.getIdBanco().intValue());
            forma.setIdDivisa(objetoEJB.getIdDivisa());
            forma.setMnemonico(objetoEJB.getMnemonico());
            forma.setMontoMaximo(montoMaximo);
            forma.setMontoMinimo(montoMinimo);
            forma.setMultiplo(multiplo);
            forma.setOperacionMinima(operMin);
            forma.setRecibimos(objetoEJB.isRecibimos());
            forma.setUsaPlantilla(objetoEJB.isUsaPlantilla());
            formas.addForma(forma);
        }
        Response res = new Response();
        res.setFormas(formas);
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
            

        responseXML = (String) servicio.getXMLfromTransferObject(out,
                       GetFormasPagoLiqOut.class);
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

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
import com.ixe.teller.registrarBeneficiario.CodigoRegistro;
import com.ixe.teller.registrarBeneficiario.RegistrarBeneficiarioParaContratoSicaIn;
import com.ixe.teller.registrarBeneficiario.RegistrarBeneficiarioParaContratoSicaOut;
import com.ixe.teller.registrarBeneficiario.Response;
import com.ixe.wmi.wmbmessage.header.Error;
import com.ixe.wmi.wmbmessage.header.Header;

import com.ixe.ods.sica.teller.util.transforma.XMLTransferObjet;

/**
 * Clase que genera al servlet.
 * @author Lorena Bernal
 *
 */
public class RegistrarBeneficiarioContratoServlet extends HttpServlet
implements Servlet {
/**
* Variable que inicializa el serialVersionUID.
*/
private static final long serialVersionUID = 1L;

/**
 * Variable que inicializa el logger.
 */
private static Logger logger = Logger.getLogger(RegistrarBeneficiarioParaContratoSicaIn.class);

/**
* Variable que almacena el xml de respuesta.
*/
private String xmlResponse;

/**
 * Varoable que almacena a los datos de RegistrarBeneficiarios.
 */
private RegistrarBeneficiarioParaContratoSicaIn requestBean;

/**
 * Variable que almacena el jndi.
 */
private String jndi;

/**
 * Variable que almacena la url.
 */
private String url;
/**
 * cadena de salida del xml
 */
private String salXML = "text/xml";
/**
 * Variable que almacena el initialContextFactory.
 */
private String initialContextFactory;
/**
 * Obtener TrasnsferObject a partir del XML de Request
 */
 private XMLTransferObjet servicio = new XMLTransferObjet();
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
 * Constructor de la clase.
 */
public RegistrarBeneficiarioContratoServlet() {
    super();
}

/**
 * Invoca a destroy.
 */
public void destroy() {
    super.destroy();
}

/**
 * Metodo que crea al Servlet.
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
       
        requestBean = (RegistrarBeneficiarioParaContratoSicaIn) 
        servicio.getTransferObjectfromXML(log(request), 
                RegistrarBeneficiarioParaContratoSicaIn.class);
        
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
 * @throws ServletException error Servlet
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
   * Método que genera el mensaje de error.
 * @param head parámetro del encabezado
 * @param e parámetro de Error
 * @return responseXMLError devuelve el xml de respuesta
 * @throws TransformadorException error TransformadorException
 */
private String error(Header head, Exception e) throws TransformadorException {
    XMLTransferObjet servicio = new XMLTransferObjet();
    Error error = new Error();
    error.setNumError(numError);
    error.setMsgError("Error en el Servlet Registrar Beneficiario Contrato");
    
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

    RegistrarBeneficiarioParaContratoSicaOut out = 
        new RegistrarBeneficiarioParaContratoSicaOut();
    out.setHeader(head);

    String xmlResponseError = (String) servicio.getXMLfromTransferObject(out,
            RegistrarBeneficiarioParaContratoSicaOut.class);
    xmlResponseError = xmlResponseError.replaceAll("ns1","NS2");
    return xmlResponseError;
}
/**
 * 
 * @param requestBean parámetro de solicitud de RegistrarBeneficiarioParaContratoSicaIn
 * @return respuestaEJB devuelve el resultado del EJB
 * @throws NamingException error NamingException 
 * @throws RemoteException error RemoteException
 * @throws CreateException error CreateException
 */
private ResultadoOperacionDto InvocaEJB(RegistrarBeneficiarioParaContratoSicaIn requestBean)
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

    int idBeneficiario = (int) requestBean.getRequest().getIdBeneficiario();
    int idCliente = (int) requestBean.getRequest().getIdCliente();
    int idUsuarioTeller = (int) requestBean.getRequest().getIdUsuarioTeller();

    ResultadoOperacionDto respuestaEJB = 
        (ResultadoOperacionDto)sicaTellerSession.registrarBeneficiarioParaContratoSica(
                                         requestBean.getHeader().getTicket(),
                                         requestBean.getRequest().getNoContratoSica(), 
                                         idBeneficiario, 
                                         idCliente,
                                         idUsuarioTeller);
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
 * @param requestBean parametro de solicitud de RegistrarBeneficiarioParaContratoSica
 * @param response parámetro de respuesta del Servlet
 * @throws NamingException error NamingException
 * @throws CreateException error CreateException
 * @throws TransformadorException error TransformadorException
 * @throws IOException error IOException
 */
private void generaSal(RegistrarBeneficiarioParaContratoSicaIn requestBean, 
        HttpServletResponse response)
throws NamingException, CreateException, TransformadorException, IOException {
    String respuesta = InvocaEJB(requestBean).getDescripcion();
    RegistrarBeneficiarioParaContratoSicaOut out = 
        new RegistrarBeneficiarioParaContratoSicaOut();
    int codigoE = InvocaEJB(requestBean).getCodigo();
    if (codigoE >= 0){
    CodigoRegistro codigo = new CodigoRegistro();
    codigo.setMensaje(respuesta);
    out.setHeader(requestBean.getHeader());
    Response resRegistraBene = new Response();
    resRegistraBene.setCodigoRegistro(codigo);
    out.setResponse(resRegistraBene);
    }
    else {
        Header hed = (Header) requestBean.getHeader();
        Error error = new Error();
        error.setNumError(numError);
        error.setMsgError("Codigo error: "+InvocaEJB(requestBean).getCodigo());
        error.setMsgDesc("Mensaje: "+InvocaEJB(requestBean).getDescripcion());
        hed.setError(error);
        out.setHeader(requestBean.getHeader());
        }
    xmlResponse = (String) servicio.getXMLfromTransferObject(out,
            RegistrarBeneficiarioParaContratoSicaOut.class);
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
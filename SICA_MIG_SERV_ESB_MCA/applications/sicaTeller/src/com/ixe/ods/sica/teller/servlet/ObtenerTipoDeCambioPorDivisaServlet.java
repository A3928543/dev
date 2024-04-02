package com.ixe.ods.sica.teller.servlet; 

import java.io.IOException;
import java.math.BigDecimal;
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
import com.ixe.ods.sica.teller.dto.TipoCambioDto;
import com.ixe.sica.teller.obttpoCambioPorDivisa.ObtenerTipoCambioDivisaIn;
import com.ixe.sica.teller.obttpoCambioPorDivisa.ObtenerTipoCambioDivisaOut;
import com.ixe.sica.teller.obttpoCambioPorDivisa.TipoCambio;
import com.ixe.sica.teller.obttpoCambioPorDivisa.Response;
import com.ixe.wmi.wmbmessage.header.Error;
import com.ixe.wmi.wmbmessage.header.Header;

import com.ixe.ods.sica.teller.util.transforma.XMLTransferObjet;

/**
 * Clase que genera un servlet.
 * @author Lorena Bernal
 *
 */
public class ObtenerTipoDeCambioPorDivisaServlet extends HttpServlet implements Servlet {
    /**
    *Variable que inicializa a serialVersionUID.
    */
    private static final long serialVersionUID = 1L;

    /**
     * Variable que inicializa a logger.
     */
    private static Logger logger = Logger.getLogger(CrearPlantillaInternacionalServlet.class);

    /**
     * Variable que almcaena la url.
     */
    private String url;

    /**
     * Vraible que almacena el jndi.
     */
    private String jndi;

    /**
     * Variable que almacena a initialContextFactory.
     */
    private String initialContextFactory;

    /**
     * Variable que almacene el mensaje de salida.
     */
    private String responseXML;
    /**
     * cadena de salida del xml
     */
    private String salXML = "text/xml";
    /**
      * Variable que controla la longitud del mensaje de error
      */
     static final  int corte = 180;
     /**
      * Bnadera para error
      */
     private int bandera = 0;
     /**
      * Variable que indica el numero de error
      */
     static final Integer numError = new Integer(3);
    /**
     * Variable que almacena a los datos de ObtenerTipoCambioDivisa.
     */
    private ObtenerTipoCambioDivisaIn requestBean;
    /**
     * Instancia a la clase XMLTRanferObject
     */
    private XMLTransferObjet servicio = new XMLTransferObjet();
    /**
     * Constructor de la clase.
     */
    public ObtenerTipoDeCambioPorDivisaServlet() {
        super();
    }

    /**
     * Metodo que genera el servlet.
    * @param request parámetro de solicitud del Servlet
     * @param response parámetro de respuesta del Servlet
     * @throws ServletException error ServletException
     * @throws IOException error IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // TODO Auto-generated method stub
        this.loadProperties();
        this.Encabezado();
        try {
        try {
            
            requestBean = (ObtenerTipoCambioDivisaIn) servicio.getTransferObjectfromXML(
                           log(request), ObtenerTipoCambioDivisaIn.class);

            generaSal(requestBean, response);
                
                logger.debug("doPost Fin");
            }
            catch (TransformadorException trans) {
                System.out.println("ErrorTransformador: "+trans.getMessage()+trans.getCause());
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
                System.out.println("Error Naming: "+e.getMessage()+e.getCause());
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
                System.out.println("Error Create: "+e.getMessage()+e.getCause());
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
                System.out.println("Error IO: "+e.getMessage()+e.getCause());
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
     * @throws ServletException error de Servlet
     */
    public void init() throws ServletException {
        super.init();
    }

    /**
     * Métododo que inicializa a las propiedades.
     */
    private void loadProperties() {
        ResourceBundle prop = ResourceBundle.getBundle("RecursosEJB");
        jndi = prop.getString("jndi");
        url = prop.getString("url");
        initialContextFactory = prop.getString("initialContextFactory");
    }

    /**
     * Método que genera el mensaje de error.
     * @param head parámetro del encabezado
     * @param e parámetro de Error
     * @return responseXMLError devuelve el xml de respuesta
     * @throws TransformadorException error TransformadorException
     */
    public String Error(Header head, Exception e) throws TransformadorException {
        XMLTransferObjet servicioError = new XMLTransferObjet();
        Error error = new Error();
        error.setNumError(numError);
        error.setMsgError("Error en el Servlet Obtener Tipo Cambio Por Divisa");
        
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

        ObtenerTipoCambioDivisaOut out = new ObtenerTipoCambioDivisaOut();
        out.setHeader(head);

        String xmlResponseError = (String) servicioError.getXMLfromTransferObject(out,
                ObtenerTipoCambioDivisaOut.class);
        xmlResponseError = xmlResponseError.replaceAll("ns1","NS2");
        return xmlResponseError;
    }
    
    /**
     * 
     * @param requestBean parámetro de solicitud de ObtenerTipoCambioDivisaIn
     * @return respuestaEJB devuelve el resultado del EJB
     * @throws NamingException error NamingException 
     * @throws RemoteException error RemoteException
     * @throws CreateException error CreateException
     */
    private ResultadoOperacionDto InvocaEJB(ObtenerTipoCambioDivisaIn requestBean)
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

        ResultadoOperacionDto resEJB = (ResultadoOperacionDto)sicaTellerSession.obtenerTipoDeCambioPorDivisa(
                                       requestBean.getHeader().getTicket(),
                                       requestBean.getRequest().getIdDivisa(),
                                       requestBean.getRequest().getClaveFormaFormaLiquidacion(),
                                       requestBean.getRequest().getIdSucursalOriginal());
        
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
     * @param requestBean parametro de solicitud de ObtenerTipoCambioDivisa
     * @param response parámetro de respuesta del Servlet
     * @throws NamingException error NamingException
     * @throws CreateException error CreateException
     * @throws TransformadorException error TransformadorException
     * @throws IOException error IOException
     */
    private void generaSal(ObtenerTipoCambioDivisaIn requestBean, HttpServletResponse response)
    throws NamingException, CreateException, TransformadorException, IOException {
        TipoCambioDto respuesta = (TipoCambioDto) InvocaEJB(requestBean).getObjeto();
       
        int codigo = InvocaEJB(requestBean).getCodigo();
        String mensaje = InvocaEJB(requestBean).getDescripcion();
        
        
        ObtenerTipoCambioDivisaOut out = new ObtenerTipoCambioDivisaOut();
        if(codigo >= 0){
        Response output = new Response();
        TipoCambio tipoCambio = new TipoCambio();

        Long idFactorDivisa = Long.valueOf(""+respuesta.getIdFactorDivisa());
        Long idPrecioReferencia = Long.valueOf(""+respuesta.getIdPrecioReferencia());
        Long idSpread = Long.valueOf(""+respuesta.getIdSpread());
        BigDecimal maximoCompra = new BigDecimal(respuesta.getMaximoCompra()
                                                          .doubleValue());
        BigDecimal minimoCompra = new BigDecimal(respuesta.getMinimoCompra()
                                                          .doubleValue());
        BigDecimal maximoVenta = new BigDecimal(respuesta.getMaximoVenta()
                                                         .doubleValue());
        BigDecimal minimoVenta = new BigDecimal(respuesta.getMinimoVenta()
                                                         .doubleValue());

        tipoCambio.setIdFactorDivisa(idFactorDivisa.longValue());
        tipoCambio.setIdPrecioReferencia(idPrecioReferencia.longValue());
        tipoCambio.setIdSpread(idSpread.intValue());
        tipoCambio.setMaximoCompra(maximoCompra);   
        tipoCambio.setMaximoVenta(maximoVenta);
        tipoCambio.setMinimoCompra(minimoCompra);
        tipoCambio.setMinimoVenta(minimoVenta);
        tipoCambio.setToken(respuesta.getToken());
        tipoCambio.setVigencia(respuesta.getVigencia());

        output.setTipoCambio(tipoCambio);

        out.setHeader(requestBean.getHeader());
        out.setResponse(output);
        }else{
             Header hed = (Header) requestBean.getHeader();
            Error error = new Error();
            error.setNumError(numError);
            error.setMsgError("Codigo error: "+InvocaEJB(requestBean).getCodigo());
            error.setMsgDesc("Mensaje: "+InvocaEJB(requestBean).getDescripcion());
            hed.setError(error);
            out.setHeader(requestBean.getHeader());
            }
        
        responseXML = (String) servicio.getXMLfromTransferObject(out,
                ObtenerTipoCambioDivisaOut.class);
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

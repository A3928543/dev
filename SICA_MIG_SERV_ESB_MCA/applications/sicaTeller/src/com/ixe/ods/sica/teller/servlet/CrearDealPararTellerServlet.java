package com.ixe.ods.sica.teller.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
import com.ixe.ods.sica.teller.dto.DealDetalleDto;
import com.ixe.ods.sica.teller.dto.DealDto;
import com.ixe.ods.sica.teller.dto.ResultadoOperacionDto;
import com.ixe.sica.teller.crearDealTeller.CrearDealParaTellerIn;
import com.ixe.sica.teller.crearDealTeller.CrearDealParaTellerOut;
import com.ixe.sica.teller.crearDealTeller.Deal;
import com.ixe.sica.teller.crearDealTeller.DetalleIn;
import com.ixe.sica.teller.crearDealTeller.DetalleOut;
import com.ixe.sica.teller.crearDealTeller.DetallesOut;
import com.ixe.sica.teller.crearDealTeller.Response;
import com.ixe.wmi.wmbmessage.header.Error;
import com.ixe.wmi.wmbmessage.header.Header;

import com.ixe.ods.sica.teller.util.transforma.XMLTransferObjet;

/**
 * Clase para crear el servlet 
 * @author Laura Martinez
 *
 */
public class CrearDealPararTellerServlet extends HttpServlet implements Servlet {

/**
 * Variable que almacena al jndi
 */
private String jndi;
/**
 * Variable que almacnea a initialContextFactory
 */
private String initialContextFactory;
/**
 * Variable que almacena la url
 */
private String url;
/**
 * Instancia a la clase CrearDealParaTellerIn 
 */
private CrearDealParaTellerIn requestBean;
/**
 * Variable que indica el numero de error
 */
static final Integer numError = new Integer(3);
/**
 * Variable que indica el numero de error
 */
static final int corte = 180;
/**
 * Bandera para error
 */
int Bandera = 0;
/**
 * Cadena para mensaje de salida
 */
private String salXML = "text/xml";
/**
 * Instancia a XMLTransferObject
 */
private XMLTransferObjet servicio = new XMLTransferObjet();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**
 * Instancia a Logger
 */
	  private static Logger logger = Logger.getLogger(CrearDealPararTellerServlet.class);
	   /**
	    * Constructor de la clase
	    */
		public CrearDealPararTellerServlet() {
			super();
		} 
		
		/**
		 * Meotodo que invoca a destroy
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
            try{
            try {
			/**
			 * Obtener TrasnsferObject a partir del XML de Request
			 */
			XMLTransferObjet servicio = new XMLTransferObjet();
			
            requestBean = (CrearDealParaTellerIn) servicio.getTransferObjectfromXML(
                           log(request), CrearDealParaTellerIn.class);		
                
            generaSal(requestBean, request, response);
                         
            logger.debug("doPost Fin");  
                    }
        catch (TransformadorException e) {
            try {
                Bandera = 1;
                com.ixe.wmi.wmbmessage.header.Header head = (com.ixe.wmi.wmbmessage.header.Header) requestBean.getHeader();
                response.setContentType(salXML);
                response.getOutputStream().write(error(head, e).getBytes());
                response.getOutputStream().close();
                } 
            catch (TransformadorException e1) {
                logger.notify();
            //} catch (IOException e1){
              //  logger.notify();
                }
        } 
        catch (NamingException e) {
            try {
                Bandera = 2;
                com.ixe.wmi.wmbmessage.header.Header head = (com.ixe.wmi.wmbmessage.header.Header) requestBean.getHeader();
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
                Bandera = 2;
                com.ixe.wmi.wmbmessage.header.Header head = (com.ixe.wmi.wmbmessage.header.Header) requestBean.getHeader();
                response.setContentType(salXML);
                response.getOutputStream().write(error(head, e1).getBytes());
                response.getOutputStream().close();
                 }
            catch (TransformadorException ee) {
                logger.notify();
                }
            }
            } catch (IOException e){
            try {
                Bandera = 2;
                com.ixe.wmi.wmbmessage.header.Header head = (com.ixe.wmi.wmbmessage.header.Header) requestBean.getHeader();
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
 * @throws ServletException error en Servlet
 */
public void init() throws ServletException {
super.init();
}   
/**
 * Inicializa las propiedades
 */
public void loadProperties() {
ResourceBundle rb = ResourceBundle.getBundle("RecursosEJB");
jndi = rb.getString("jndi");
url = rb.getString("url");
initialContextFactory = rb.getString("initialContextFactory");
} 
/**
 * Metodo para generar el mensaje de error
 * @param head encabezado
 * @param e excepcion
 * @return xmlResponse
 * @throws TransformadorException error de transformador
 */
private String error(com.ixe.wmi.wmbmessage.header.Header head, Exception e) throws TransformadorException{

XMLTransferObjet servicioError = new XMLTransferObjet();
Error error = new Error();
error.setNumError(numError);
error.setMsgError("Ocurrio un error en el Servlet Crear Deal Para Teller");

        if (Bandera == 1){
        if (e.getMessage().length()>240)
        {
        error.setMsgDesc("Descripcion: " + e.getMessage().substring(corte));
        }
        else error.setMsgDesc("Descripcion: " + e.getMessage());
        }
        if (Bandera == 2){
            if (e.getCause().toString().length()>240){
        error.setMsgDesc("Descripcion: " + e.getCause().toString().substring(corte));
        }
        else error.setMsgDesc("Descripcion: "+e.getCause().toString());
        }

head.setError(error);   
CrearDealParaTellerOut out = new CrearDealParaTellerOut();
out.setHeader(head);

String xmlResponse = (String) servicioError.getXMLfromTransferObject(
              out, CrearDealParaTellerOut.class);
xmlResponse = xmlResponse.replaceAll("ns1","NS2");

return xmlResponse;
}
/**
 * 
 * @param requestBean parámetro de solicitud de CrearPlantillaNacionalIn
 * @return respuestaEJB devuelve el resultado del EJB
 * @throws NamingException error NamingException 
 * @throws RemoteException error RemoteException
 * @throws CreateException error CreateException
 * 
 * @deprecated La creaci&oacute;n del detalle fue modificada conforme
 * a la eliminaci—n del uso de referencias a FactorDivisa y PrecioReferencia.
 * Se deber&aacute; realizar las adecuaciones necesarias a las clases del paquete  
 * <code>com.ixe.sica.teller.crearDealTeller</code> para su correcto funcionamiento.
 */


private ResultadoOperacionDto InvocaEJB(CrearDealParaTellerIn requestBean, HttpServletRequest request)
throws NamingException, RemoteException, CreateException { 
     
    Integer idUsuario =  Integer.valueOf(requestBean.getRequest().getIdUsuario() + "");
    List listaDeal = new ArrayList();
    List listaDealEntrada = requestBean.getRequest().getDetallesIn().getDetalleInAsReference();
                Iterator iterador = listaDealEntrada.iterator();
                
    while (iterador.hasNext()) {
    DetalleIn detalleIn = (DetalleIn) iterador.next();
                
    listaDeal.add(new DealDetalleDto(detalleIn.getRecibimos(),
    new Double(detalleIn.getMonto().doubleValue()),
    new Double(detalleIn.getTcc().doubleValue()),
    new Double(detalleIn.getTcm().doubleValue()),
    detalleIn.getIdDivisa(),
    detalleIn.getClaveFormaLiquidacion(),
    Integer.valueOf(detalleIn.getIdPlantilla() + ""),
    //TODO: Se debe reemplazar por el valor directo del 
    //factor divisa.
    new Double(0.0), 
    Integer.valueOf(detalleIn.getIdFactorDivisa() + ""),
    Integer.valueOf(detalleIn.getIdSpreadAplicado() + ""),
    //TODO: Se debe reemplazar por los valores directos del
    //precio referencia.
    new Double(0.0), 
    new Double(0.0),
    Integer.valueOf(detalleIn.getIdPrecioReferencia() + ""),
    detalleIn.getInstruccionesBeneficiario(),
    detalleIn.getInstruccionesIntermediario(),
    detalleIn.getInstruccionesPagador(),
    detalleIn.getToken(),
    detalleIn.getALaPar()
    ));
    }
    Properties properties = new Properties();
    properties.put(Context.PROVIDER_URL, this.url);
    properties.put(Context.INITIAL_CONTEXT_FACTORY, this.initialContextFactory);
    
    InitialContext jndiContext = new InitialContext(properties);
    Object ref = jndiContext.lookup(this.jndi);             
    SicaTellerSessionHome sicaTellerHome = (SicaTellerSessionHome)
                          PortableRemoteObject.narrow(ref, SicaTellerSessionHome.class);
    SicaTellerSession sicaTellerSession = (SicaTellerSession) sicaTellerHome.create();
                 
    ResultadoOperacionDto respuestaEJB = (ResultadoOperacionDto)sicaTellerSession.crearDealParaTeller(
                                            requestBean.getHeader().getTicket(),
                                            requestBean.getRequest().getNoCuenta(),
                                            requestBean.getRequest().getIdSucursal(),
                                            idUsuario.intValue(),
                                            listaDeal, request.getRemoteAddr());
    return respuestaEJB;
}

/**
 * 
 * @param request parámetro de solicitud del Servlet
 * @return requestXML retorna el xml de entrada
 * @throws IOException error IOException 
 */
private String log(HttpServletRequest request)throws IOException{
    String requestXML=null;
    
    logger.debug("doPost Ini");
    logger.debug("request: " + request);    
    
    int av = request.getInputStream().available();
    
    logger.debug("available: " + av);
    
    
    byte[] bytes = new byte[av];    
    int i = request.getInputStream().read(bytes);
    
    requestXML = new String(bytes);
    
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
private void generaSal(CrearDealParaTellerIn requestBean, HttpServletRequest request, HttpServletResponse response)
throws NamingException, CreateException, TransformadorException, IOException {
    
    DealDto respuesta = (DealDto) InvocaEJB(requestBean, request).getObjeto();
    
    CrearDealParaTellerOut out = new CrearDealParaTellerOut();

    int codigo = InvocaEJB(requestBean, request).getCodigo();        
    if (codigo >=  0) {

   
    Response output = new Response(); 
    
    
    output.setDeal(datosDeal(requestBean, request));
    out.setResponse(output);
    out.setHeader(requestBean.getHeader()); 
    }
    else {
   com.ixe.wmi.wmbmessage.header.Header hed = (com.ixe.wmi.wmbmessage.header.Header) requestBean.getHeader();
    Error error = new Error();
    error.setNumError(numError);
    error.setMsgError("Codigo error" + InvocaEJB(requestBean, request).getCodigo());
    error.setMsgDesc("Mensaje " + InvocaEJB(requestBean, request).getDescripcion());
                    
    hed.setError(error);
    out.setHeader(requestBean.getHeader()); 
                    
    }
                   
    String xmlResponse = (String) servicio.getXMLfromTransferObject(
                         out, CrearDealParaTellerOut.class);

    xmlResponse = xmlResponse.replaceAll("ns1","NS2");
    response.setContentType(salXML);
    response.getOutputStream().write(xmlResponse.getBytes());
    response.getOutputStream().close();
    
}
/**
 * Metodo que ingresa los datos al Deal
 * @param respuesta respuesta del Objeto
 * @return deald
 */
private Deal datosDeal(CrearDealParaTellerIn requestBean, HttpServletRequest request)
    throws NamingException, RemoteException, CreateException{
    
    DealDto respuesta = (DealDto) InvocaEJB(requestBean, request).getObjeto();
    
    Deal deald = new Deal();
    
    long mesaCambio = Long.parseLong("" + respuesta.getIdMesaCambio());
    deald.setIdMesaCambio(mesaCambio);
    deald.setIdCanal(respuesta.getIdCanal());
    deald.setNoCuenta(respuesta.getNoCuenta());
    deald.setEventosDeal(respuesta.getEventosDeal());
    deald.setFechaCaptura(respuesta.getFechaCaptura());
    deald.setFechaLiquidacion(respuesta.getFechaLiquidacion());
    deald.setIdDeal(Long.parseLong("" + respuesta.getIdDeal()));
    if(respuesta.getIdLiquidacion() != null){
    deald.setIdLiquidacion(respuesta.getIdLiquidacion().longValue());}
    deald.setObservaciones(respuesta.getObservaciones());
    deald.setIdPromotor(Long.parseLong("" + respuesta.getIdPromotor()));
    deald.setTipoDeal(respuesta.getTipoDeal());
    deald.setStatusDeal(respuesta.getStatusDeal());
    deald.setTipoValor(respuesta.getTipoValor());
    deald.setIdUsuario(Long.parseLong("" + respuesta.getIdUsuario()));
    deald.setDetallesOut(datosDetalle(requestBean, request));
    return deald;
}
/**
 * 
 * @return outDetalles
 * @throws CreateException error de create
 * @throws NamingException error de naming
 * @throws RemoteException error de remote
 * @param requestBean instancia
 * @param iterator instancia a Iterator
 * @param res Instancia a DealDetalleDto 
 */
private DetallesOut datosDetalle(CrearDealParaTellerIn requestBean, HttpServletRequest request)
    throws NamingException, RemoteException, CreateException{
    DealDto respuesta = (DealDto) InvocaEJB(requestBean, request).getObjeto();
    List salidaDeal = (List) respuesta.getDetalles();
    Iterator iterator = salidaDeal.iterator();
    DetallesOut outDetalles = new DetallesOut(); 
    DetalleOut outDetalle = new DetalleOut();
    while (iterator.hasNext()) {
    DealDetalleDto res = (DealDetalleDto)iterator.next();
    outDetalle.setFolioDetalle(Long.parseLong("" + res.getFolioDetalle()));
    BigDecimal monto = new BigDecimal("" + res.getMonto());
    BigDecimal tcc = new BigDecimal("" + res.getTcc());
    BigDecimal tcm = new BigDecimal("" + res.getTcm());
    outDetalle.setMonto(monto);
    outDetalle.setTcc(tcc);
    outDetalle.setTcm(tcm);
    outDetalle.setIdDivisa(res.getIdDivisa());
    outDetalle.setClaveFormaLiquidacion(res.getClaveFormaLiquidacion());
    outDetalle.setIdPlantilla(Long.parseLong("" + res.getIdPlantilla()));
    outDetalle.setIdPrecioReferencia(Long.parseLong("" + res.getIdPrecioReferencia()));
    outDetalle.setIdFactorDivisa(Long.parseLong("" + res.getIdFactorDivisa()));
    outDetalle.setIdSpread(Long.parseLong("" + res.getIdSpread()));
    outDetalle.setToken(res.getToken());
    outDetalle.setALaPar(res.isALaPar());                   
    outDetalle.setInstruccionesBeneficiario(res.getInstruccionesBeneficiario());                        
    outDetalle.setInstruccionesIntermediario(
            res.getInstruccionesIntermediario());                   
    outDetalle.setInstruccionesPagardor(res.getInstruccionesPagardor());
    outDetalle.setEventosDetalle(res.getEventosDetalle());
    
    outDetalles.addDetalleOut(outDetalle);       
    }    
    
    return outDetalles;
    
}
    /**
     * Metodo que inicializa el encabezado
     */    
    private void Encabezado(){
        com.ixe.wmi.wmbmessage.header.Header head = new com.ixe.wmi.wmbmessage.header.Header();
        head.setAreaUsuario("ErrorArea");
        head.setNombreAplic("ErrorAplic");
        head.setTicket("1234567890123456789012");
        head.setUsuario("Error");
        head.setVersionHeader("1.1.0");
        head.setVersionServicio("10.00.00");
        }
}

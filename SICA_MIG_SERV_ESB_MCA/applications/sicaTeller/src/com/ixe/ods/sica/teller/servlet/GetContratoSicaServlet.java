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
import com.ixe.ods.sica.teller.dto.ContratoSicaDto;
import com.ixe.ods.sica.teller.dto.PersonaDto;
import com.ixe.ods.sica.teller.dto.PlantillaCuentaIxeDto;
import com.ixe.ods.sica.teller.dto.PlantillaInternacionalDto;
import com.ixe.ods.sica.teller.dto.PlantillaNacionalDto;
import com.ixe.ods.sica.teller.dto.PlantillaTranNacionalDto;
import com.ixe.ods.sica.teller.dto.ResultadoOperacionDto;
import com.ixe.sica.teller.getContrato.Beneficiario;
import com.ixe.sica.teller.getContrato.Beneficiarios;
import com.ixe.sica.teller.getContrato.ContratoSica;
import com.ixe.sica.teller.getContrato.GetContratoSicaIn;
import com.ixe.sica.teller.getContrato.GetContratoSicaOut;
import com.ixe.sica.teller.getContrato.Limites;
import com.ixe.sica.teller.getContrato.PlantillaCuentaIxe;
import com.ixe.sica.teller.getContrato.PlantillaInternacional;
import com.ixe.sica.teller.getContrato.PlantillaNacional;
import com.ixe.sica.teller.getContrato.PlantillaTranNacional;
import com.ixe.sica.teller.getContrato.PlantillasCuentaIxe;
import com.ixe.sica.teller.getContrato.PlantillasInternacionales;
import com.ixe.sica.teller.getContrato.PlantillasNacionales;
import com.ixe.sica.teller.getContrato.PlantillasTranNacional;
import com.ixe.sica.teller.getContrato.Response;
import com.ixe.wmi.wmbmessage.header.Error;
import com.ixe.wmi.wmbmessage.header.Header;

import com.ixe.ods.sica.teller.util.transforma.XMLTransferObjet;
/**
 * Clase que genera un servlet.
 * @author Lorena Bernal
 *
 */
public class GetContratoSicaServlet extends HttpServlet implements Servlet {
    /**
     *Variable que iniccializa el serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Variable que inicializa el logger.
     */
    private static Logger logger = Logger.getLogger(GetContratoSicaIn.class);
    
    /**
     * Variable que almacene el mensaje de respuesta.
     */
    private String xmlResponse;
    
    /**
     * Variable que almacena los datos de GetContratoSica.
     */
    private GetContratoSicaIn requestBean;
    
    /**
     * Variable que almacena el njdi.
     */
    private String jndi;
    
    /**
     * Variable que almacena la url.
     */
    private String url;
    /**
     * Variable que almcaena el initialContextFactory.
     */
    private String initialContextFactory;
    /**
     * Instancia a la clase XMLTransferObject
     */
    private XMLTransferObjet servicio = new XMLTransferObjet();
    /**
     * Cadena para el xml de salida
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
     * Variable que indica el numero del error
     */
    static final Integer numError = new Integer(3);
    /**
     * Constructor de la clase.
     */
    public GetContratoSicaServlet() {
        super();
    }
    
    /**
     * Metodo que invoca a destroy.
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
            
            requestBean = (GetContratoSicaIn) servicio.getTransferObjectfromXML(log(request),
                    GetContratoSicaIn.class);
            
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
        catch (CreateException e) {
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
     * @throws ServletException error Setrvlet
     */
    public void init() throws ServletException {
            super.init();
    }
    
    /**
     * Metodo que inicializa las propiedades.
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
    private String error(Header head, Exception e) throws TransformadorException {
        XMLTransferObjet servicio = new XMLTransferObjet();
        Error error = new Error();
        error.setNumError(numError);
        error.setMsgError("Error en Servlet Get Contrato SICA");
       
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
        
        GetContratoSicaOut out = new GetContratoSicaOut();
        out.setHeader(head);
        
        String xmlResponseError = (String) servicio.getXMLfromTransferObject(out,
                GetContratoSicaOut.class);
        xmlResponseError = xmlResponseError.replaceAll("ns1","NS2");
        return xmlResponseError;
    }
    /**
     * 
     * @param requestBean parámetro de solicitud de GetContratoSicaIn
     * @return respuestaEJB devuelve el resultado del EJB
     * @throws NamingException error NamingException 
     * @throws RemoteException error RemoteException
     * @throws CreateException error CreateException
     */
    private ResultadoOperacionDto InvocaEJB(GetContratoSicaIn requestBean)
    throws NamingException, RemoteException, CreateException {
        Properties properties = new Properties();
        properties.put(Context.PROVIDER_URL, this.url);
        properties.put(Context.INITIAL_CONTEXT_FACTORY, this.initialContextFactory);
        
        InitialContext jndiContext = new InitialContext(properties);
        Object ref = jndiContext.lookup(this.jndi);
        
        SicaTellerSessionHome sicaTellerHome = (SicaTellerSessionHome) 
        PortableRemoteObject.narrow(ref,
                SicaTellerSessionHome.class);
        SicaTellerSession sicaTellerSession = (SicaTellerSession) sicaTellerHome.create();
        
        int idPersona = ((Long) requestBean.getRequest().getIdPersonaUsuario()).intValue();
        int idSucursal = new Integer(requestBean.getRequest().getIdSucursal()).intValue();
        Boolean recibimos = (Boolean) requestBean.getRequest().getRecibimos();
        ResultadoOperacionDto respuestaEJB = (ResultadoOperacionDto)sicaTellerSession.getContratoSica(
                requestBean.getHeader().getTicket(),
                requestBean.getRequest().getNoContratoSica(),
                requestBean.getRequest().getNoCuentaIxe(), requestBean.getRequest().getRfc(),
                requestBean.getRequest().getNombre(), requestBean.getRequest().getPaterno(),
                requestBean.getRequest().getMaterno(), requestBean.getRequest().getRazonSocial(),
                idPersona, requestBean.getRequest().getNacionalidad(),
                requestBean.getRequest().getPasaporte(),
                requestBean.getRequest().getIfe().getFolio(),
                requestBean.getRequest().getIfe().getAnioRegistro(),
                requestBean.getRequest().getIfe().getOcr(),
                requestBean.getRequest().getIfe().getNumeroEmision(), idSucursal,
                requestBean.getRequest().getIdDivisa(),
                requestBean.getRequest().getClaveFormaLiquidacion(), recibimos,
                new Integer(requestBean.getRequest().getIdUsuarioEjecutivo().intValue()));
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
     * @param requestBean parametro solicitud de GetContratoSicaIn
     * @param response parametro de respuesta del Servlet
     * @throws NamingException error NamingException 
     * @throws CreateException error CreateException
     * @throws TransformadorException error TransformadorException
     * @throws IOException error IOException
     */
    private void generaSal(GetContratoSicaIn requestBean, HttpServletResponse response)
    throws NamingException, CreateException, TransformadorException, IOException {
        
        ContratoSicaDto respuesta = (ContratoSicaDto) InvocaEJB(requestBean).getObjeto();
        
        GetContratoSicaOut out = new GetContratoSicaOut();
        
         int codigo = InvocaEJB(requestBean).getCodigo();
         if(codigo >= 0){
            
        List persona = (List) respuesta.getBeneficiarios();
        List plantillaTranNacional = (List)respuesta.getPlantillaTranNacional();
        List plantillaNacional = (List)respuesta.getPlantillaNacional();
        List plantillaInternacional = (List)respuesta.getPlantillaInternacional();
        List plantillaIxe = (List)respuesta.getPlantillaCuentaIxe();
        
        out.setHeader(requestBean.getHeader());
        
        Iterator iterator = persona.iterator();
        Iterator iteratorTranNacional = plantillaTranNacional.iterator();
        Iterator iteratorPlantillaNacional = plantillaNacional.iterator();
        Iterator iteratorPlantillaInternacional = plantillaInternacional.iterator();
        Iterator iteratorPlantillaIxe = plantillaIxe.iterator();
        
        Beneficiarios benes = new Beneficiarios();
        PlantillasTranNacional transNacional = new PlantillasTranNacional();
        PlantillasNacionales plantillasNacionales = new PlantillasNacionales(); 
        PlantillasInternacionales plantillasInternacionales = new PlantillasInternacionales();
        PlantillasCuentaIxe  cuentasIxe = new PlantillasCuentaIxe();
        
        ContratoSica con = new ContratoSica();
        con.setIdPersona(new Long(respuesta.getIdPersona()));
        con.setNombreCompleto(respuesta.getNombreCompleto());
        con.setNoContratoSica(respuesta.getNoCuenta());
        
        while (iterator.hasNext()) {
            PersonaDto per = (PersonaDto) iterator.next();
            Beneficiario beneficiario = new Beneficiario();
            beneficiario.setIdPersona(new Long(per.getIdPersona()));
            beneficiario.setNombreCompleto(per.getNombreCompleto());
            benes.addBeneficiario(beneficiario);
        }
        
        while (iteratorTranNacional.hasNext()){
            PlantillaTranNacionalDto tranNacionalDto = (PlantillaTranNacionalDto)iteratorTranNacional.next();
            PlantillaTranNacional tranNacional = new PlantillaTranNacional();
            tranNacional.setIdPlantilla(new Long(tranNacionalDto.getIdPlantilla()));
            tranNacional.setClavePlantilla(tranNacionalDto.getClavePlantilla());
            tranNacional.setNoContratoSica(tranNacionalDto.getNoContratoSica());
            tranNacional.setMnemonico(tranNacionalDto.getMnemonico());
            tranNacional.setUltimaModificacion(tranNacionalDto.getUltimaModificacion());
            tranNacional.setStatusPlantilla(tranNacionalDto.getStatusPlantilla());
            tranNacional.setIdBeneficiario(new Long(tranNacionalDto.getIdBeneficiario()));
            tranNacional.setNombreBeneficiario(tranNacionalDto.getNombreBeneficiario());
            tranNacional.setClabe(tranNacionalDto.getClabe());
            tranNacional.setIdBanco(new Long(tranNacionalDto.getIdBanco().longValue()));
            tranNacional.setMnemonico(tranNacionalDto.getMnemonico());
            tranNacional.setUltimaModificacion(tranNacionalDto.getUltimaModificacion());
            tranNacional.setStatusPlantilla(tranNacionalDto.getStatusPlantilla());
            tranNacional.setIdBeneficiario(new Long(tranNacionalDto.getIdBeneficiario()));
            tranNacional.setMnemonico(tranNacionalDto.getMnemonico());
            tranNacional.setNombreBeneficiario(tranNacionalDto.getNombreBeneficiario());
            tranNacional.setClabe(tranNacionalDto.getClabe());
            tranNacional.setMnemonico(tranNacionalDto.getMnemonico());
            tranNacional.setIdBanco(new Long(tranNacionalDto.getIdBanco().longValue()));
            tranNacional.setNombreBanco(tranNacionalDto.getNombreBanco());
            tranNacional.setIdDivisa(tranNacionalDto.getIdDivisa());
            transNacional.addPlantillaTranNacional(tranNacional);
            }
            
            while (iteratorPlantillaNacional.hasNext()){
                PlantillaNacionalDto plantillaNacDto = (PlantillaNacionalDto)iteratorPlantillaNacional.next();
                PlantillaNacional nacional = new PlantillaNacional();
                nacional.setIdPlantilla(new Long(plantillaNacDto.getIdPlantilla()));
                nacional.setClavePlantilla(plantillaNacDto.getClavePlantilla());
                nacional.setNoContratoSica(plantillaNacDto.getNoContratoSica());
                nacional.setMnemonico(plantillaNacDto.getMnemonico());
                nacional.setIdPlantilla(new Long(plantillaNacDto.getIdPlantilla()));
                nacional.setUltimaModificacion(plantillaNacDto.getUltimaModificacion());
                nacional.setStatusPlantilla(plantillaNacDto.getStatusPlantilla());
                nacional.setIdBeneficiario(new Long(plantillaNacDto.getIdBeneficiario()));
                nacional.setNombreBeneficiario(plantillaNacDto.getNombreBeneficiario());
                plantillasNacionales.addPlantillaNacional(nacional);
                }
                
                while (iteratorPlantillaInternacional.hasNext()){
                    PlantillaInternacionalDto internacionalDto = (PlantillaInternacionalDto)iteratorPlantillaInternacional.next();
                    PlantillaInternacional internacional = new PlantillaInternacional();
                    internacional.setIdPlantilla(new Long(internacionalDto.getIdPlantilla()));
                    internacional.setClavePlantilla(internacionalDto.getClavePlantilla());
                    internacional.setNoContratoSica(internacionalDto.getNoContratoSica());
                    internacional.setMnemonico(internacionalDto.getMnemonico());
                    internacional.setUltimaModificacion(internacionalDto.getUltimaModificacion());
                    internacional.setStatusPlantilla(internacionalDto.getStatusPlantilla());
                    internacional.setIdBeneficiario(new Long(internacionalDto.getIdBeneficiario()));
                    internacional.setNombreBeneficiario(internacionalDto.getNombreBeneficiario());
                    internacional.setIdPlantilla(new Long(internacionalDto.getIdPlantilla()));
                    internacional.setIdDivisa(internacionalDto.getIdDivisa());
                    internacional.setTipoCuentaBanco(internacionalDto.getTipoCuentaBanco());
                    internacional.setTipoCuentaBancoInterm(internacionalDto.getTipoCuentaBancoInterm());
                    internacional.setClaveBanco(internacionalDto.getClaveBanco());
                    internacional.setClaveBancoInterm(internacionalDto.getClaveBancoInterm());
                    internacional.setIdPais(internacionalDto.getIdPais());
                    internacional.setIdPaisInterm(internacionalDto.getIdPaisInterm());
                    internacional.setIdPlantilla(new Long(internacionalDto.getIdPlantilla()));
                    internacional.setNoCuentaBeneficiario(internacionalDto.getNoCuentaBeneficiario());
                    internacional.setNombreBanco(internacionalDto.getNombreBanco());
                    internacional.setCuentaBancoInterm(internacionalDto.getCuentaBancoInterm());
                    internacional.setNombreBancoIterm(internacionalDto.getNombreBancoInterm());
                    internacional.setInstrBcoBeneficiario(internacionalDto.getInstrBcoBeneficiario());
                    internacional.setInstrBcoIntermediario(internacionalDto.getInstrBcoIntermediario());
                    plantillasInternacionales.addPlantillaInternacional(internacional);
                    }
                    
                    while (iteratorPlantillaIxe.hasNext()){
                        PlantillaCuentaIxeDto ixeDto = (PlantillaCuentaIxeDto)iteratorPlantillaIxe.next();
                        PlantillaCuentaIxe ixe = new PlantillaCuentaIxe();
                        ixe.setIdPlantilla(new Long(ixeDto.getIdPlantilla()));
                        ixe.setClavePlantilla(ixeDto.getClavePlantilla());
                        ixe.setNoContratoSica(ixeDto.getNoContratoSica());
                        ixe.setMnemonico(ixeDto.getMnemonico());
                        ixe.setUltimaModificacion(ixeDto.getUltimaModificacion());
                        ixe.setStatusPlantilla(ixeDto.getStatusPlantilla());
                        ixe.setIdBeneficiario(new Long(ixeDto.getIdBeneficiario()));
                        ixe.setNombreBeneficiario(ixeDto.getNombreBeneficiario());
                        ixe.setNoCuentaIxe(ixeDto.getNoCuentaIxe());
                        cuentasIxe.addPlantillaCuentaIxe(ixe);
                        }
        
        con.setBeneficiarios(benes);
        con.setPlantillasTranNacional(transNacional);
        con.setPlantillasNacionales(plantillasNacionales);
        con.setPlantillasInternacionales(plantillasInternacionales);
        con.setPlantillasCuentaIxe(cuentasIxe);
        
        Limites limites = new Limites();
        limites.setClaveFormaLiquidacion(respuesta.getLimites().getClaveFormaLiquidacion());
        limites.setIdDivisa(respuesta.getLimites().getIdDivisa());
        limites.setLimiteDiario(new BigDecimal(respuesta.getLimites().getLimiteDiario()));
        limites.setLimiteMensual(new BigDecimal(respuesta.getLimites().getLimiteMensual()));
        limites.setRecibimos(new Boolean(respuesta.getLimites().isRecibimos()));
        con.setLimites(limites);
        
        con.setCliente(new Boolean(respuesta.isCliente()));
        
        Response resGetContrato = new Response();
        resGetContrato.setContratoSica(con);
        out.setResponse(resGetContrato);
        }
        else {
            Header hed = (Header) requestBean.getHeader();
            Error error = new Error();
            error.setNumError(numError);
            error.setMsgError("Codigo error: " + InvocaEJB(requestBean).getCodigo());
            error.setMsgDesc("Mensaje: " + InvocaEJB(requestBean).getDescripcion());
            hed.setError(error);
            out.setHeader(requestBean.getHeader());
            }
        xmlResponse = (String) servicio.getXMLfromTransferObject(out,
                GetContratoSicaOut.class);
        xmlResponse = xmlResponse.replaceAll("ns1", "NS2");
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

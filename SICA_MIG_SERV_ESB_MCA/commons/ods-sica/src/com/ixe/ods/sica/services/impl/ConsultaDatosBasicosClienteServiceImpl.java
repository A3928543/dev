package com.ixe.ods.sica.services.impl;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;

import org.apache.axis.client.Stub;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.banorte.util.ESBUtil;
import com.banorte.www.ws.ESBRequest.ESBRequestType;
import com.banorte.www.ws.ESBRequest.HeaderReq;
import com.banorte.www.ws.ESBRequest.HeaderReqVerHeaderReq;
import com.banorte.www.ws.ESBRequest.Request;
import com.banorte.www.ws.ESBRequest.RequestAsincrono;
import com.banorte.www.ws.ESBResponse.ESBResponseType;
import com.banorte.www.ws.MantenimientoDatosBasicosClienteRequest.MantenimientoDatosBasicosClienteRequestType;
import com.banorte.www.ws.MantenimientoDatosBasicosClienteResponse.Penc4001;
import com.banorte.www.ws.esb.MantenimientoDatosBasicosCliente.MantenimientoDatosBasicosClienteServiceLocator;
import com.banorte.www.ws.exception.SicaAltamiraException;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaProperties;
import com.ixe.ods.sica.dto.DatosBasicosClienteAltamiraDto;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.ConsultaDatosBasicosClienteService;



/**
 *  Clase del servicio que consulta datos
 * 	básicos de cliente en Altamira. [TX PE80]
 *
 * @author Diego Pazaran
 *  Banorte
 *
 * @version $Revision: 1.1.2.2 $
 */
public class ConsultaDatosBasicosClienteServiceImpl implements ConsultaDatosBasicosClienteService {

	
	/**
     * El objeto para logging.
     */
    private final transient Log _logger = LogFactory.getLog(getClass());
	
	
    /** header: versión del header */
    private static final String VER_HEADER = "1.0";
    
    /** request: Ejecución asincrona del servicio  */
    private static final boolean ASINCRONO = true;
    
	/** request: Version del servicio */
	private static final String VERSION_SERVICIO = "1.0";
	
	/** request: id del servicio. */
	private static final String ID_SERVICIO = "wmb570";
	
	/** Ejecución de la TX PE80 en modo de consulta*/
    private static final String OPCION_CONSULTA = "C";
    
    /** Ejecucion del servicio exitosa * */
    private static final String ID_EXITO = "1";
    
    /** El ServiceData de Sica */
    private SicaServiceData sicaServiceData;
    
    /** Locator del servicio */
    private MantenimientoDatosBasicosClienteServiceLocator locatorDatosBasicosCliente;
    

	
	/**
     * Obtiene datos basicos de un cliente en Altamira.
     *
     * @param noCliente el número de cliente (sic)
     *                   a consultar
     * @param param <code>Map</code> con los parametros
     *                   de ejecución del servicio.
     *
     * @return <code>DatosBasicosClienteAltamiraDto</code>
     *                 con la informacion del cliente Altamira.
     *
     * @throws SicaAltamiraException
     *                         en caso de un error en la ejecución del Servicio.
     */
    public DatosBasicosClienteAltamiraDto 
    getDatosBasicosClienteAltamira(Integer noCliente, Map param) throws SicaAltamiraException {
    	   	
    	
    	try {
            
            String urlServicio = null;
            String codigoError = null;
            //boolean asincrono = false;

            //ESBRequestType ESBRequest = null;
            ESBResponseType ESBResponse = null;
            
            //Obtiene usuario y password del archivos de propiedades.
        	String usrEsb = SicaProperties.getInstance().getSicaEsbUsr();
        	String pwdEsb = SicaProperties.getInstance().getSicaEsbPwd();
        	

             
            //Se obtiene la URL del Servicio
            urlServicio = getSicaServiceData().findParametro(
            		ParametroSica.URL_ESB_DATOS_BASICOS_CLIENTE).getValor();
            
            //Se asigna la url del servicio
            getLocatorDatosBasicosCliente().setESBPortEndpointAddress(urlServicio);

            _logger.debug("Url Servicio: " +
            		getLocatorDatosBasicosCliente().getESBPortAddress());

             //Se obtiene el request
             com.banorte.www.ws.esb.MantenimientoDatosBasicosCliente.ESBPort proxy = 
            	 		getLocatorDatosBasicosCliente().getESBPort();
               
             //Se asigna el mensaje de seguridad en webservices WSSE
             Stub stub = (Stub)proxy;
             _logger.debug("Creando header wsse... ");
             SOAPHeaderElement header  = ESBUtil.creaHeader(usrEsb, pwdEsb); 
             stub.setHeader(header);
               
             //Se ejecuta el servicio
             ESBResponse = proxy.mantenimientoDatosBasicosCliente(creaESBRequest(noCliente, new HashMap()));
             codigoError = ESBResponse.getHeader().getError().getIdError();

                //Si se ejecuto correctamente y hay datos
                if (codigoError.equals(ID_EXITO)) {
                	
                    DatosBasicosClienteAltamiraDto clienteDto = getDatosBasicosCliente(ESBResponse);
                    
                    _logger.debug(clienteDto.toString());
                    return clienteDto;
                    
                }
                else {
                    StringBuffer sbError = new StringBuffer(
                            "Error en la respuesta del servicio MantenimientoDatosBasicosCliente: ");
                    sbError.append("noCliente: "  + noCliente);
                    sbError.append("\n MsgError: " +
                        ESBResponse.getHeader().getError().getMsgError());
                    sbError.append("\n DescError: " +
                        ESBResponse.getHeader().getError().getDescError());
                    
                    _logger.debug(sbError.toString());
                	
                	throw new SicaAltamiraException(ESBResponse.getHeader().getError().getDescError());
                }
        }
    	catch (SOAPException ex) {
			_logger.error("SOAPException en getDatosBasicosClienteAltamira ", ex);
			throw new SicaException("SOAPException en getDatosBasicosClienteAltamira ", ex);
		}
    	 catch (RemoteException re) {
             _logger.info("Ocurrio un error al invocar el servicio remoto: ", re);
             throw new SicaException("Error al invocar el servicio remoto: ", re);
         }
         catch (ServiceException se) {
             _logger.info("Error en el servicio ConsultaDatosBasicosCliente: ", se);
             throw new SicaException("Error en el servicio consultaIdIxeAltamira: ", se);
         }
    }
    

    /**
     * Crea el ESBRequest
     *
     * @param noCliente el Cliente Altamira	
     * 		   que se consulta.
     * @param <code>Map</code> con los parámetros
     * 		  adicionales para ejecutar el servicio.
     *
     * @return objeto <code>ESBRequestType</code>
     * 		con el request.
     */
    private ESBRequestType creaESBRequest(Integer noCliente, Map param) {
        ESBRequestType type = new ESBRequestType();

        type.setHeader(
        		creaHeaderRequest(
        				(String)param.get("ticket"), 
        				(String)param.get("cr"), 
        				(String)param.get("usuario"))); //Se asigna el header
        
        type.setRequest(creaRequest(noCliente)); //Se asigna el request

        return type;
    }

    /**
     * Obtiene un nuevo header
     *
     * @return <code>Header</code>
     */
    private HeaderReq creaHeaderRequest(String ticket, String cr, String usuario) {
       HeaderReq header = new HeaderReq();

       
       header.setVerHeaderReq(HeaderReqVerHeaderReq.fromString(VER_HEADER));
       
       if(ticket != null && ticket.length() > 0) {
    	   header.setTicket(ticket);
       }
       if(cr != null && cr.length() > 0) {
    	   header.setCR(cr);
       }
       if(usuario != null && usuario.length() > 0) {
    	   header.setUsrOper(usuario);
       }
        return header;
    }
    
   

    /**
     * Crea el request con los datos
     * 	de la petición.
     *
     * @param <code>Integer</code> con el numero
     * 			de cliente Altamira.
     *     
     * @return <code>Request</code> con los
     * 		datos de la petición
     */
    private Request creaRequest(Integer numeroCliente) {
        Request req = new Request();
        
        MantenimientoDatosBasicosClienteRequestType mttoDatosBasicosRequest = new MantenimientoDatosBasicosClienteRequestType();

        mttoDatosBasicosRequest.setNumClie(numeroCliente);
        mttoDatosBasicosRequest.setOpcion(OPCION_CONSULTA); // Opcion de Consulta
        
        if (ASINCRONO) {
            req.setAsincrono(RequestAsincrono.S);
        }
        else {
            req.setAsincrono(RequestAsincrono.N);
        }
        
        req.setIdServicio(ID_SERVICIO);
        req.setVerServicio(VERSION_SERVICIO);
        req.setMantenimientoDatosBasicosClienteRequest(mttoDatosBasicosRequest);
        
        return req;
    }

    /**
     * Recupera los datos basicos del Cliente
     *
     * @param ESBResponse con la respuesta del 
     * 			servicio
     *
     * @return <code>DatosBasicosClienteAltamiraDto</code> con la 
     * 		  informacion del cliente. 
     */
    private DatosBasicosClienteAltamiraDto getDatosBasicosCliente(ESBResponseType response) {
        
    	DatosBasicosClienteAltamiraDto cliente = new DatosBasicosClienteAltamiraDto();

        Penc4001 penc4001  = response.getResponse().getMantenimientoDatosBasicosClienteResponse().getPenc4001();
        
        cliente.setNombre(penc4001.getNombre().trim());
        cliente.setPaterno(penc4001.getApel1().trim());
        cliente.setMaterno(penc4001.getApel2().trim());
        cliente.setRazonSocial(penc4001.getNombCom().trim());
        cliente.setRfc(penc4001.getRFC().trim());
        cliente.setPerJur(penc4001.getPerJur().trim());
        
        return cliente;
    }

    /**
	 * getter a SicaServiceData
	 * @return una referencia al objeto 
	 * 	de acceso a datos.
	 */
	public SicaServiceData getSicaServiceData() {
		return sicaServiceData;
	}

	/**
	 * Setter de SicaServiceData
	 * @param sicaServiceData con la referencia
	 * 	al objeto de acceso a datos. 
	 */
	public void setSicaServiceData(SicaServiceData sicaServiceData) {
		this.sicaServiceData = sicaServiceData;
	}
  
	

	/**
     * Regresa el locator del servicio.
     *
     * @return <code>MantenimientoDatosBasicosClienteServiceLocator</code>
     *         con la referencia al locator.
     */
	public MantenimientoDatosBasicosClienteServiceLocator getLocatorDatosBasicosCliente() {
		return locatorDatosBasicosCliente;
	}


	/**
    * Asigna el locator del servicio.
    *
    * @param <code>MantenimientoDatosBasicosClienteServiceLocator</code>
    *         con la referencia al locator.
    */
	public void setLocatorDatosBasicosCliente(
			MantenimientoDatosBasicosClienteServiceLocator locatorDatosBasicosCliente) {
		this.locatorDatosBasicosCliente = locatorDatosBasicosCliente;
	}
}


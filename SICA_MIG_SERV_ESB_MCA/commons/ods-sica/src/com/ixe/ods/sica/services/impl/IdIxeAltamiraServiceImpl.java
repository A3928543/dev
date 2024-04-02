package com.ixe.ods.sica.services.impl;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.banorte.www.ws.ConsultaIdIxeAltamiraRequest.ConsultaIdIxeAltamira;
import com.banorte.www.ws.ConsultaIdIxeAltamiraResponse.PemeBn0;
import com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.ESBRequestType;
import com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.HeaderReq;
import com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.Request;
import com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.RequestAsincrono;
import com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ESBResponseType;
import com.banorte.www.ws.esb.consultaIdIxeAltamira.ConsultaIdIxeAltamiraServiceLocator;
import com.banorte.www.ws.exception.SicaAltamiraException;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.IdIxeAltamiraService;


/**
 * Clase que permite acceder a los
 *  servicios de Altamira en el  ESB para obtener
 *  el identificador de clientes entre Banorte e Ixe.
 *
 *
 * @author Diego Pazaran
 *  Banorte
 *
 * @version $Revision: 1.1.2.1.6.1 $
 */
public class IdIxeAltamiraServiceImpl implements IdIxeAltamiraService {

	/**
     * El objeto para logging.
     */
    private final transient Log _logger = LogFactory.getLog(getClass());
    
    /** Consulta Exitosa */
    private static final String ID_EXITO = "1";
    
    /** Error de negocio en la consulta */
    private static final String ID_ERROR = "0";
    
    /** Mensaje de error: NO EXISTE RELACION CTE.IXE-CTE.BANORTE */
    private static final String MSG_NO_EXISTE_RELACION = "PEE0000";
    
    /** El locator del servicio de consultaIdIxeAltamira */
    private ConsultaIdIxeAltamiraServiceLocator locatorIdIxeAltamira;
    
    /** El ServiceData de Sica */
    private SicaServiceData sicaServiceData;
     
    
    /**
     * Método que obtiene el número de cliente idIxe (ID_PERSONA) a 
     * 	partir de un número de cliente Banorte (SIC)
     *
     * @param noClienteBanorte el número de cliente en Banorte 
     * @return <code>String</String> con el número de
     * 		 	cliente Ixe.
     */
    public String getIdClienteIxe(String noClienteBanorte)
    	throws SicaAltamiraException{
        try {
            // el cliente banorte
            String numClBn = noClienteBanorte; 
            String numClIxe = null;
            String urlServicio = null;
            String codigoError = null;
            String msgError = null;

            boolean asincrono = false;

            ESBRequestType ESBrequest = new ESBRequestType();
            ESBResponseType ESBResponse;
            PemeBn0[] pemeBn0 = null;

            //Se obtiene la URL del Servicio
            urlServicio = getSicaServiceData().findParametro(
            		ParametroSica.URL_ESB_ID_IXE_ALTAMIRA).getValor();
            
            //Se asigna la url del servicio
            getLocatorIdIxeAltamira().setESBPortEndpointAddress(urlServicio);
            
            _logger.debug("Url Servicio: " + getLocatorIdIxeAltamira().getESBPortAddress());
            _logger.debug("Parametro a Enviar: " + numClBn);

            //Se asigna el header
            ESBrequest.setHeader(getHeaderRequest());
            //Se asigna el request
            ESBrequest.setRequest(getRequest(numClBn, asincrono));
            
            //Se ejecuta el servicio
            ESBResponse = getLocatorIdIxeAltamira().getESBPort().consultaIdIxeAltamira(ESBrequest);
            
            codigoError = ESBResponse.getHeader().getError().getIdError();
            msgError    = ESBResponse.getHeader().getError().getMsgError();

            //Si se ejecuto correctamente y hay datos
            if(codigoError.equals(ID_EXITO)) {
           	
            	pemeBn0     = ESBResponse.getResponse().getConsultaIdIxeAltamiraResponse();
                //Se lee la respuesta
                for (int i = 0; i < pemeBn0.length; i++) {
                    final PemeBn0 element = pemeBn0[i];

                    _logger.debug("Cliente BNTE :" + numClBn + "\n" + "Cliente IXE:" +
                        element.getNumClIx());
                    
                    numClIxe = element.getNumClIx();
                }
            }//si no se encuentra la relacion cte ixe - cte bnte se regresa 0
            else if(codigoError.equals(ID_ERROR) && msgError.trim().equals(MSG_NO_EXISTE_RELACION)) {
            
            	StringBuffer sbError = new StringBuffer("Error en la respuesta del servicio consultaIdAltamiraIxe: ");
            	sbError.append("\n Codigo Error:" + codigoError);
            	sbError.append("\n MsgError: " + ESBResponse.getHeader().getError().getMsgError());
            	sbError.append("\n DescError: " + ESBResponse.getHeader().getError().getDescError());
            
            	_logger.debug(sbError.toString());
            
            	//Se regresa 0 para posteriormente buscar en la bup
            	numClIxe = ID_ERROR;
            }//En cualquier otro caso se lanza una excepción.
            else {
            	
            	StringBuffer sbError = new StringBuffer("Error en la respuesta del servicio consultaIdAltamiraIxe: ");
            	sbError.append("\n Codigo Error:" + codigoError);
            	sbError.append("\n MsgError: " + ESBResponse.getHeader().getError().getMsgError());
            	sbError.append("\n DescError: " + ESBResponse.getHeader().getError().getDescError());
            
            	_logger.debug(sbError.toString());
            	
            	throw new SicaAltamiraException(ESBResponse.getHeader().getError().getDescError());
            }
            return numClIxe; 
        }
        catch (RemoteException re) {
            _logger.info("Ocurrio un error al invocar el servicio remoto: ", re);
            throw new SicaException("Error al invocar el servicio remoto: ", re);
        }
        catch (ServiceException se) {
            _logger.info("Error en el servicio consultaIdIxeAltamira: ", se);
            throw new SicaException("Error en el servicio consultaIdIxeAltamira: ", se);
        }
    }

    /**
     * Obtiene un nuevo header
     *
     * @return <code>Header</code>
     */
    private HeaderReq getHeaderRequest() {
        HeaderReq header = new HeaderReq();
        return header;
    }

    /**
     * Metodo que forma
     *  el Request
     *
     * @param numClBn cliente Banorte
     * @param asincrono true si la petición es asincrona
     *
     * @return un objeto Request.
     */
    private Request getRequest(String numClBn, boolean asincrono) {
        ConsultaIdIxeAltamira consultaIdIxeAltamira = new ConsultaIdIxeAltamira();
        Request req = new Request();

        consultaIdIxeAltamira.setNumClBn(numClBn);

        req.setConsultaIxeAltamiraRequest(consultaIdIxeAltamira);

        if (!asincrono) {
            req.setAsincrono(RequestAsincrono.S);
        }
        else {
            req.setAsincrono(RequestAsincrono.N);
        }

        return req;
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
	 * Getter locator servicio locatorIdIxeAltamira
	 * @return la referencia al objeto locatorIdIxeAltamira
	 */
	public ConsultaIdIxeAltamiraServiceLocator getLocatorIdIxeAltamira() {
		return locatorIdIxeAltamira;
	}

	/**
	 * Setter locatorIdIxeAltamira.
	 * @param locatorIdIxeAltamira con la referencia del objeto.
	 */
	public void setLocatorIdIxeAltamira(
			ConsultaIdIxeAltamiraServiceLocator locatorIdIxeAltamira) {
		this.locatorIdIxeAltamira = locatorIdIxeAltamira;
	}
}

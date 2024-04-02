package com.ixe.ods.sica.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.banorte.www.ws.exception.SicaAltamiraException;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.dto.InfoCuentaAltamiraDto;
import com.ixe.ods.sica.services.ConsultaClabesSaldosService;

public class ConsultaClabesSaldosImpl implements ConsultaClabesSaldosService{
	
	private final transient Log logger = LogFactory.getLog(ConsultaClabesSaldosImpl.class);
	
	public InfoCuentaAltamiraDto consultaInformacionCuenta(Map parameters)  throws SicaAltamiraException {
				
		logger.info("consultaInformacionCuenta: " + parameters.toString());
		InfoCuentaAltamiraDto infoCuentaAltamira = new InfoCuentaAltamiraDto();
		
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			
			String soapAction = "http://www.banorte.com/ws/esb/ConsultaClabesSaldos/consultarClabesSaldosPeticion";
			
			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage = messageFactory.createMessage();
			
			SOAPPart soapPart = soapMessage.getSOAPPart();
			
			SOAPEnvelope envelope = soapPart.getEnvelope();
	        envelope.addNamespaceDeclaration("head", "http://www.banorte.com/ws/esb/general/Headers");
	        envelope.addNamespaceDeclaration("con", "http://www.banorte.com/ws/esb/ConsultaClabesSaldos");
	        
	        createHeaderRequest(envelope, parameters);
	        createBodyRequest(envelope, parameters);
	        
	        MimeHeaders headers = soapMessage.getMimeHeaders();
	        headers.addHeader("SOAPAction", soapAction);
	        
	        soapMessage.saveChanges();
	        
	        SOAPMessage soapResponse = soapConnection.call(soapMessage, parameters.get("URL"));
	        
	        System.out.println("---------- SOAP REQUEST ----------");
	        soapMessage.writeTo(System.out);
	        System.out.println(soapMessage.toString());
	        
	        System.out.println("---------- SOAP RESPONSE ----------");
	        soapResponse.writeTo(System.out);
	        System.out.println(soapResponse.toString());
	        
	        OutputStream responseString = new ByteArrayOutputStream(4096);
			soapResponse.writeTo(responseString);
			
			String sw = responseString.toString();
	        SAXParserFactory spf = SAXParserFactory.newInstance();
	        spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
	        spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
	        SAXParser sp = spf.newSAXParser();
	        
	        ConsultaClabesSaldosResponseHandler handler = new ConsultaClabesSaldosResponseHandler();
	        sp.parse(new InputSource(new StringReader(sw.toString())), handler);
	        
	        if(handler.exceptionResponse == null) {
	        	if(handler.headerResponse.getEstadoRespuesta().getId().equalsIgnoreCase("0")) {
		        	throw new SicaAltamiraException(handler.headerResponse.getEstadoRespuesta().getMensajeAUsuario());
		        }else {
		        	infoCuentaAltamira = handler.infoCuenta;
		        	logger.info("infoCuentaAltamira: " + infoCuentaAltamira.toString());
		        }
	        }else {
	        	throw new SicaAltamiraException(handler.exceptionResponse.getDescripcion());
	        }
	        
		}catch (SOAPException exception) {
			throw new SicaException(exception.getMessage());
		} catch (ParserConfigurationException exception) {
			throw new SicaException(exception.getMessage());
		} catch (SAXException exception) {
			throw new SicaException(exception.getMessage());
		} catch (IOException exception) {
			throw new SicaException(exception.getMessage());
		}
		
		return infoCuentaAltamira;
	}
	
	public void createHeaderRequest(SOAPEnvelope envelope, Map parameters) throws SOAPException {
		String prefix = "wsse";
		String idOperacion;
		long millis = System.currentTimeMillis();
		
		if(parameters.get("MCA_TIPO_CANAL").toString().length() < 2) {
			idOperacion = "0" + parameters.get("MCA_TIPO_CANAL").toString();
		}else {
			idOperacion = parameters.get("MCA_TIPO_CANAL").toString();
		}
		
		if(parameters.get("MCA_ID_APLICACION").toString().length() < 10) {
			String aux = "";
			for(int i = 0; i < 10 - parameters.get("MCA_ID_APLICACION").toString().length(); i++) {
				aux = aux + "0";
			}
			idOperacion = idOperacion + aux + parameters.get("MCA_ID_APLICACION").toString();
		}else {
			idOperacion = idOperacion + parameters.get("MCA_ID_APLICACION").toString();
		}
		
		idOperacion = idOperacion + millis;
		
		if(parameters.get("MCA_ID_SERVICIO_B162").toString().length() < 7) {
			String aux = "";
			for(int i = 0; i < 7 - parameters.get("MCA_ID_SERVICIO_B162").toString().length(); i++) {
				aux=aux+"0";
			}
			idOperacion = idOperacion + aux + parameters.get("MCA_ID_SERVICIO_B162").toString();
		}else {
			idOperacion = idOperacion + parameters.get("MCA_ID_SERVICIO_B162").toString();
		}
		
		SOAPHeader soapHeaderRequest = envelope.getHeader();
		
    	SOAPElement securityElem = soapHeaderRequest.addChildElement("Security",prefix,parameters.get("MCA_URI_LEG").toString());
		SOAPElement tokenElemen = securityElem.addChildElement("UsernameToken",prefix);
		SOAPElement userElemen = tokenElemen.addChildElement("Username",prefix);
		userElemen.addTextNode(parameters.get("MCA_USER_LEG").toString());
		SOAPElement passwordElemen = tokenElemen.addChildElement("Password",prefix);
		passwordElemen.addTextNode(parameters.get("MCA_PWD_LEG").toString());
		
		SOAPElement headerRequest = soapHeaderRequest.addChildElement("HeaderReq", "head");
		Name elemento = envelope.createName("locale");
		headerRequest.addAttribute(elemento, "es_MX");
		elemento = envelope.createName("codIdioma");
		headerRequest.addAttribute(elemento, "UTF8");
        
        SOAPElement acceso =  headerRequest.addChildElement("Acceso");
		SOAPElement accesoElem =  acceso.addChildElement("IdUsuario");
		accesoElem.addTextNode(parameters.get("MCA_ID_USUARIO").toString());
		
		accesoElem =  acceso.addChildElement("IdSesion");
		
		if(parameters.get("TICKET").toString().trim().equalsIgnoreCase("") || parameters.get("TICKET").toString() == null) {
			accesoElem.addTextNode("0");
		}else {
			accesoElem.addTextNode(parameters.get("TICKET").toString());
		}
		
		accesoElem =  acceso.addChildElement("IdOperacion");
		accesoElem.addTextNode(idOperacion);
		
		accesoElem =  acceso.addChildElement("TokenOperacion");
		accesoElem.addTextNode("0");
		
		SOAPElement consumidor =  headerRequest.addChildElement("Consumidor");
		SOAPElement consumidorElem =  consumidor.addChildElement("IpServer");
		consumidorElem.addTextNode(parameters.get("MCA_IP_SERVER").toString());
		
		consumidorElem =  consumidor.addChildElement("IpCliente");
		consumidorElem.addTextNode(parameters.get("MCA_IP_CLIENTE").toString());
		
		consumidorElem =  consumidor.addChildElement("TipoCanal");
		consumidorElem.addTextNode(parameters.get("MCA_TIPO_CANAL").toString());
		
		consumidorElem =  consumidor.addChildElement("IdAplicacion");
		consumidorElem.addTextNode(parameters.get("MCA_ID_APLICACION").toString());
		
		SOAPElement datosPeticion =  headerRequest.addChildElement("DatosPeticion");
		
		SOAPElement datosPeticionElem =  datosPeticion.addChildElement("IdServicio");
		datosPeticionElem.addTextNode(parameters.get("MCA_ID_SERVICIO_B162").toString());
		
		datosPeticionElem =  datosPeticion.addChildElement("VersionServicio");
		datosPeticionElem.addTextNode(parameters.get("MCA_VERSION_SERVICIO_B162").toString());
		
		datosPeticionElem =  datosPeticion.addChildElement("VersionEndpoint");
		datosPeticionElem.addTextNode(parameters.get("MCA_VERSION_ENDPOINT_B162").toString());
		
		datosPeticionElem =  datosPeticion.addChildElement("CodCR");
		datosPeticionElem.addTextNode(parameters.get("MCA_CODCR").toString());
	}
	
	public void createBodyRequest(SOAPEnvelope envelope, Map parameters) throws SOAPException{
		
		SOAPBody soapBody = envelope.getBody();
		SOAPElement peticionElem = soapBody.addChildElement("consultarClabesSaldosPeticion", "con");
		SOAPElement datosPeticionElem = peticionElem.addChildElement("TipoMovimiento");
		datosPeticionElem.addTextNode("2");
        
		datosPeticionElem = peticionElem.addChildElement("Tran_NumeroCuenta");
		datosPeticionElem.addTextNode((String)parameters.get("NO_CUENTA"));
	}
}
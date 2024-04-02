package com.banorte.www.ws.esb.consultaIdIxeAltamira;

import com.banorte.www.ws.ConsultaIdIxeAltamiraRequest.ConsultaIdIxeAltamira;
import com.banorte.www.ws.ConsultaIdIxeAltamiraResponse.PemeBn0;
import com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.ESBRequestType;
import com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.HeaderReq;
import com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.Request;
import com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.RequestAsincrono;
import com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ESBResponseType;

public class TestService {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{

		
		// el cliente banorte
		String numClBn = "25080710";
		//String otherUrl = "https://15.128.25.12:7861/ws/ConsultaIdIxeAltamiraXXX";
		boolean asincrono  = false;
		
		ESBRequestType ESBrequest = new ESBRequestType();
		ESBResponseType ESBResponse;
		PemeBn0 pemeBn0[] = null;
		
		ConsultaIdIxeAltamiraServiceLocator locator = new ConsultaIdIxeAltamiraServiceLocator();
		
		/** Esto es para comprobar que se puede asignar otra URL al Servicio */
		//locator.setESBPortEndpointAddress(otherUrl);
		
		System.out.println(locator.getESBPortAddress());
		System.out.println(locator.getESBPortWSDDServiceName());
		
		//Se asigna el header
		ESBrequest.setHeader(getHeaderRequest());
		ESBrequest.setRequest(getRequest(numClBn, asincrono));
		
		ESBResponse = locator.getESBPort().consultaIdIxeAltamira(ESBrequest);
		
		
		pemeBn0 = ESBResponse.getResponse().getConsultaIdIxeAltamiraResponse();
		
		for(int i = 0; i< pemeBn0.length; i++) {
			
			final PemeBn0 element = pemeBn0[i];
			
			
			System.out.println("Cliente BNTE :" + numClBn +
					"\n" + "Cliente IXE:" + element.getNumClIx());
			
		}

	}

	private static HeaderReq getHeaderRequest() {
		
		HeaderReq header = new HeaderReq();
		
		return header;
	}
	
	private static Request getRequest(String numClBn, boolean asincrono ) {
		
		ConsultaIdIxeAltamira consultaIdIxeAltamira = new ConsultaIdIxeAltamira(); 
		Request req = new Request();
		
		consultaIdIxeAltamira.setNumClBn(numClBn);
		
		req.setConsultaIxeAltamiraRequest(consultaIdIxeAltamira);
		
		if(!asincrono) {
			req.setAsincrono(RequestAsincrono.S);
		}
		else {
			req.setAsincrono(RequestAsincrono.N);
		}
		
		return req;
		
	}
}

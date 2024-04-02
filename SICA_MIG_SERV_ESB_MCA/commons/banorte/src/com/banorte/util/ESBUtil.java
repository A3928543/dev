package com.banorte.util;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import org.apache.axis.message.PrefixedQName;
import org.apache.axis.message.SOAPHeaderElement;

public class ESBUtil {

	 /**
     * Crea el mensaje en WSSE en el Header
     * 
     * @param usr el usuario del ESB
     * @param pwd el password del ESB
     * @return <code>SOAPHeaderElement</code>
     * @throws SOAPException en caso de algún error.
     */
    public static SOAPHeaderElement creaHeader(String usr, String pwd) throws SOAPException {
		PrefixedQName name = new PrefixedQName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
												"Security", 
												"wsse");
		SOAPHeaderElement header = new SOAPHeaderElement(name);
		SOAPElement usernameToken = header.addChildElement("UsernameToken", "wsse");
		SOAPElement username = usernameToken.addChildElement("Username", "wsse");
		username.addNamespaceDeclaration("wsu", 
										 "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
		username.addTextNode(usr);
		SOAPElement password = usernameToken.addChildElement("Password", "wsse");
		password.addTextNode(pwd);
		
		return header;
	}
}

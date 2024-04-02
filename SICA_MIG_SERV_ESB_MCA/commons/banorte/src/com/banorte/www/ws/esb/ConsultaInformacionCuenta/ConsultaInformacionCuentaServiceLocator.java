/**
 * ConsultaInformacionCuentaServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.esb.ConsultaInformacionCuenta;

public class ConsultaInformacionCuentaServiceLocator extends org.apache.axis.client.Service implements com.banorte.www.ws.esb.ConsultaInformacionCuenta.ConsultaInformacionCuentaService {

    public ConsultaInformacionCuentaServiceLocator() {
    }


    public ConsultaInformacionCuentaServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ConsultaInformacionCuentaServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ESBPort
    private java.lang.String ESBPort_address = "http://15.128.25.12:7821/ws/esb/ConsultaInformacionCuenta";

    public java.lang.String getESBPortAddress() {
        return ESBPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ESBPortWSDDServiceName = "ESBPort";

    public java.lang.String getESBPortWSDDServiceName() {
        return ESBPortWSDDServiceName;
    }

    public void setESBPortWSDDServiceName(java.lang.String name) {
        ESBPortWSDDServiceName = name;
    }

    public com.banorte.www.ws.esb.ConsultaInformacionCuenta.ESBPort getESBPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ESBPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getESBPort(endpoint);
    }

    public com.banorte.www.ws.esb.ConsultaInformacionCuenta.ESBPort getESBPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.banorte.www.ws.esb.ConsultaInformacionCuenta.SOAPBindingStub _stub = new com.banorte.www.ws.esb.ConsultaInformacionCuenta.SOAPBindingStub(portAddress, this);
            _stub.setPortName(getESBPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setESBPortEndpointAddress(java.lang.String address) {
        ESBPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.banorte.www.ws.esb.ConsultaInformacionCuenta.ESBPort.class.isAssignableFrom(serviceEndpointInterface)) {
                com.banorte.www.ws.esb.ConsultaInformacionCuenta.SOAPBindingStub _stub = new com.banorte.www.ws.esb.ConsultaInformacionCuenta.SOAPBindingStub(new java.net.URL(ESBPort_address), this);
                _stub.setPortName(getESBPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ESBPort".equals(inputPortName)) {
            return getESBPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInformacionCuenta", "ConsultaInformacionCuentaService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInformacionCuenta", "ESBPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ESBPort".equals(portName)) {
            setESBPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

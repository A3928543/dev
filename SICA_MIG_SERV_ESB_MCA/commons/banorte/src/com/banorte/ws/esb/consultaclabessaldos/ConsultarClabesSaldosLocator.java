/**
 * ConsultarClabesSaldosLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.consultaclabessaldos;

public class ConsultarClabesSaldosLocator extends org.apache.axis.client.Service implements com.banorte.ws.esb.consultaclabessaldos.ConsultarClabesSaldos {

    public ConsultarClabesSaldosLocator() {
    }


    public ConsultarClabesSaldosLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ConsultarClabesSaldosLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for consultarClabesSaldosHttpPort
    private java.lang.String consultarClabesSaldosHttpPort_address = "http://lnxsibu1i.qa.unix.banorte.com:7828/ws/esb/ConsultaClabesSaldos/V2.0";

    public java.lang.String getconsultarClabesSaldosHttpPortAddress() {
        return consultarClabesSaldosHttpPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String consultarClabesSaldosHttpPortWSDDServiceName = "consultarClabesSaldosHttpPort";

    public java.lang.String getconsultarClabesSaldosHttpPortWSDDServiceName() {
        return consultarClabesSaldosHttpPortWSDDServiceName;
    }

    public void setconsultarClabesSaldosHttpPortWSDDServiceName(java.lang.String name) {
        consultarClabesSaldosHttpPortWSDDServiceName = name;
    }

    public com.banorte.ws.esb.consultaclabessaldos.ConsultaClabesSaldos getconsultarClabesSaldosHttpPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(consultarClabesSaldosHttpPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getconsultarClabesSaldosHttpPort(endpoint);
    }

    public com.banorte.ws.esb.consultaclabessaldos.ConsultaClabesSaldos getconsultarClabesSaldosHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.banorte.ws.esb.consultaclabessaldos.ConsultarClabesSaldosHttpBindingSOAPStub _stub = new com.banorte.ws.esb.consultaclabessaldos.ConsultarClabesSaldosHttpBindingSOAPStub(portAddress, this);
            _stub.setPortName(getconsultarClabesSaldosHttpPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setconsultarClabesSaldosHttpPortEndpointAddress(java.lang.String address) {
        consultarClabesSaldosHttpPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.banorte.ws.esb.consultaclabessaldos.ConsultaClabesSaldos.class.isAssignableFrom(serviceEndpointInterface)) {
                com.banorte.ws.esb.consultaclabessaldos.ConsultarClabesSaldosHttpBindingSOAPStub _stub = new com.banorte.ws.esb.consultaclabessaldos.ConsultarClabesSaldosHttpBindingSOAPStub(new java.net.URL(consultarClabesSaldosHttpPort_address), this);
                _stub.setPortName(getconsultarClabesSaldosHttpPortWSDDServiceName());
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
        if ("consultarClabesSaldosHttpPort".equals(inputPortName)) {
            return getconsultarClabesSaldosHttpPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaClabesSaldos", "ConsultarClabesSaldos");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaClabesSaldos", "consultarClabesSaldosHttpPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("consultarClabesSaldosHttpPort".equals(portName)) {
            setconsultarClabesSaldosHttpPortEndpointAddress(address);
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

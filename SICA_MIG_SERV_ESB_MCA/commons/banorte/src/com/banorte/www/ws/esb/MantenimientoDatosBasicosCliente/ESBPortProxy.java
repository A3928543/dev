package com.banorte.www.ws.esb.MantenimientoDatosBasicosCliente;

public class ESBPortProxy implements com.banorte.www.ws.esb.MantenimientoDatosBasicosCliente.ESBPort {
  private String _endpoint = null;
  private com.banorte.www.ws.esb.MantenimientoDatosBasicosCliente.ESBPort eSBPort = null;
  
  public ESBPortProxy() {
    _initESBPortProxy();
  }
  
  public ESBPortProxy(String endpoint) {
    _endpoint = endpoint;
    _initESBPortProxy();
  }
  
  private void _initESBPortProxy() {
    try {
      eSBPort = (new com.banorte.www.ws.esb.MantenimientoDatosBasicosCliente.MantenimientoDatosBasicosClienteServiceLocator()).getESBPort();
      if (eSBPort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)eSBPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)eSBPort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (eSBPort != null)
      ((javax.xml.rpc.Stub)eSBPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.banorte.www.ws.esb.MantenimientoDatosBasicosCliente.ESBPort getESBPort() {
    if (eSBPort == null)
      _initESBPortProxy();
    return eSBPort;
  }
  
  public com.banorte.www.ws.ESBResponse.ESBResponseType mantenimientoDatosBasicosCliente(com.banorte.www.ws.ESBRequest.ESBRequestType ESB) throws java.rmi.RemoteException, com.banorte.www.ws.ESBResponse.ErrorType{
    if (eSBPort == null)
      _initESBPortProxy();
    return eSBPort.mantenimientoDatosBasicosCliente(ESB);
  }
  
  
}
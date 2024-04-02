/**
 * SOAPBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.esb.consultaIdIxeAltamira;

public class SOAPBindingStub extends org.apache.axis.client.Stub implements com.banorte.www.ws.esb.consultaIdIxeAltamira.ESBPort {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[1];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("consultaIdIxeAltamira");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "ESBRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "ESBRequestType"), com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.ESBRequestType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "ESBResponseType"));
        oper.setReturnClass(com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ESBResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "ESBResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "Error"),
                      "com.banorte.www.ws.ESBResponse.ErrorType",
                      new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "ErrorType"), 
                      true
                     ));
        _operations[0] = oper;

    }

    public SOAPBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public SOAPBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public SOAPBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraRequest", ">ConsultaIdIxeAltamira>NumClBn");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraRequest", ">ConsultaIdIxeAltamira>NumClIx");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraRequest", "ConsultaIdIxeAltamira");
            cachedSerQNames.add(qName);
            cls = com.banorte.www.ws.ConsultaIdIxeAltamiraRequest.ConsultaIdIxeAltamira.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", ">PemeBn0>Nombre");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", ">PemeBn0>NumClBn");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", ">PemeBn0>NumClIx");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", ">PemeBn0>PriApe");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", ">PemeBn0>RazonS1");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", ">PemeBn0>RazonS2");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", ">PemeBn0>SegApe");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", "ConsultaIdIxeAltamiraType");
            cachedSerQNames.add(qName);
            cls = com.banorte.www.ws.ConsultaIdIxeAltamiraResponse.PemeBn0[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", "PemeBn0");
            qName2 = new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", "PemeBn0");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", "PemeBn0");
            cachedSerQNames.add(qName);
            cls = com.banorte.www.ws.ConsultaIdIxeAltamiraResponse.PemeBn0.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", ">HeaderReq>verHeaderReq");
            cachedSerQNames.add(qName);
            cls = com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.HeaderReqVerHeaderReq.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", ">Request>asincrono");
            cachedSerQNames.add(qName);
            cls = com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.RequestAsincrono.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "ESBRequestType");
            cachedSerQNames.add(qName);
            cls = com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.ESBRequestType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "HeaderReq");
            cachedSerQNames.add(qName);
            cls = com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.HeaderReq.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "Request");
            cachedSerQNames.add(qName);
            cls = com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.Request.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", ">HeaderResType>verHeaderRes");
            cachedSerQNames.add(qName);
            cls = com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.HeaderResTypeVerHeaderRes.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "ErrorType");
            cachedSerQNames.add(qName);
            cls = com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ErrorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "ESBResponseType");
            cachedSerQNames.add(qName);
            cls = com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ESBResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "HeaderResType");
            cachedSerQNames.add(qName);
            cls = com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.HeaderResType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "ResponseType");
            cachedSerQNames.add(qName);
            cls = com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ESBResponseType consultaIdIxeAltamira(com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.ESBRequestType ESB) throws java.rmi.RemoteException, com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ErrorType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.banorte.com/ws/esb/consultaIdIxeAltamira");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "consultaIdIxeAltamira"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {ESB});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ESBResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ESBResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ESBResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ErrorType) {
              throw (com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ErrorType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}

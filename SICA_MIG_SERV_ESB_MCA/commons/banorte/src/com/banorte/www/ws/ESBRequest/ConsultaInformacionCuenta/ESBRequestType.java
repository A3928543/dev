/**
 * ESBRequestType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.ESBRequest.ConsultaInformacionCuenta;

public class ESBRequestType  implements java.io.Serializable {
    private com.banorte.www.ws.ESBRequest.ConsultaInformacionCuenta.HeaderReq header;

    private com.banorte.www.ws.ESBRequest.ConsultaInformacionCuenta.Request request;

    public ESBRequestType() {
    }

    public ESBRequestType(
           com.banorte.www.ws.ESBRequest.ConsultaInformacionCuenta.HeaderReq header,
           com.banorte.www.ws.ESBRequest.ConsultaInformacionCuenta.Request request) {
           this.header = header;
           this.request = request;
    }


    /**
     * Gets the header value for this ESBRequestType.
     * 
     * @return header
     */
    public com.banorte.www.ws.ESBRequest.ConsultaInformacionCuenta.HeaderReq getHeader() {
        return header;
    }


    /**
     * Sets the header value for this ESBRequestType.
     * 
     * @param header
     */
    public void setHeader(com.banorte.www.ws.ESBRequest.ConsultaInformacionCuenta.HeaderReq header) {
        this.header = header;
    }


    /**
     * Gets the request value for this ESBRequestType.
     * 
     * @return request
     */
    public com.banorte.www.ws.ESBRequest.ConsultaInformacionCuenta.Request getRequest() {
        return request;
    }


    /**
     * Sets the request value for this ESBRequestType.
     * 
     * @param request
     */
    public void setRequest(com.banorte.www.ws.ESBRequest.ConsultaInformacionCuenta.Request request) {
        this.request = request;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ESBRequestType)) return false;
        ESBRequestType other = (ESBRequestType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.header==null && other.getHeader()==null) || 
             (this.header!=null &&
              this.header.equals(other.getHeader()))) &&
            ((this.request==null && other.getRequest()==null) || 
             (this.request!=null &&
              this.request.equals(other.getRequest())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getHeader() != null) {
            _hashCode += getHeader().hashCode();
        }
        if (getRequest() != null) {
            _hashCode += getRequest().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ESBRequestType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "ESBRequestType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("header");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "Header"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "HeaderReq"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("request");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "Request"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "Request"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

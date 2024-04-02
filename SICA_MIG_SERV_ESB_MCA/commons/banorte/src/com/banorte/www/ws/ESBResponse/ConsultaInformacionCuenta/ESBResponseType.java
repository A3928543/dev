/**
 * ESBResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.ESBResponse.ConsultaInformacionCuenta;

public class ESBResponseType  implements java.io.Serializable {
    private com.banorte.www.ws.ESBResponse.ConsultaInformacionCuenta.HeaderResType header;

    private com.banorte.www.ws.ESBResponse.ConsultaInformacionCuenta.ResponseType response;

    public ESBResponseType() {
    }

    public ESBResponseType(
           com.banorte.www.ws.ESBResponse.ConsultaInformacionCuenta.HeaderResType header,
           com.banorte.www.ws.ESBResponse.ConsultaInformacionCuenta.ResponseType response) {
           this.header = header;
           this.response = response;
    }


    /**
     * Gets the header value for this ESBResponseType.
     * 
     * @return header
     */
    public com.banorte.www.ws.ESBResponse.ConsultaInformacionCuenta.HeaderResType getHeader() {
        return header;
    }


    /**
     * Sets the header value for this ESBResponseType.
     * 
     * @param header
     */
    public void setHeader(com.banorte.www.ws.ESBResponse.ConsultaInformacionCuenta.HeaderResType header) {
        this.header = header;
    }


    /**
     * Gets the response value for this ESBResponseType.
     * 
     * @return response
     */
    public com.banorte.www.ws.ESBResponse.ConsultaInformacionCuenta.ResponseType getResponse() {
        return response;
    }


    /**
     * Sets the response value for this ESBResponseType.
     * 
     * @param response
     */
    public void setResponse(com.banorte.www.ws.ESBResponse.ConsultaInformacionCuenta.ResponseType response) {
        this.response = response;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ESBResponseType)) return false;
        ESBResponseType other = (ESBResponseType) obj;
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
            ((this.response==null && other.getResponse()==null) || 
             (this.response!=null &&
              this.response.equals(other.getResponse())));
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
        if (getResponse() != null) {
            _hashCode += getResponse().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ESBResponseType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "ESBResponseType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("header");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "Header"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "HeaderResType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("response");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "Response"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "ResponseType"));
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

/**
 * Request.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira;

public class Request  implements java.io.Serializable {
    private com.banorte.www.ws.ConsultaIdIxeAltamiraRequest.ConsultaIdIxeAltamira consultaIxeAltamiraRequest;

    private com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.RequestAsincrono asincrono;  // attribute

    private java.lang.String idServicio;  // attribute

    private java.lang.String verServicio;  // attribute

    public Request() {
    }

    public Request(
           com.banorte.www.ws.ConsultaIdIxeAltamiraRequest.ConsultaIdIxeAltamira consultaIxeAltamiraRequest,
           com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.RequestAsincrono asincrono,
           java.lang.String idServicio,
           java.lang.String verServicio) {
           this.consultaIxeAltamiraRequest = consultaIxeAltamiraRequest;
           this.asincrono = asincrono;
           this.idServicio = idServicio;
           this.verServicio = verServicio;
    }


    /**
     * Gets the consultaIxeAltamiraRequest value for this Request.
     * 
     * @return consultaIxeAltamiraRequest
     */
    public com.banorte.www.ws.ConsultaIdIxeAltamiraRequest.ConsultaIdIxeAltamira getConsultaIxeAltamiraRequest() {
        return consultaIxeAltamiraRequest;
    }


    /**
     * Sets the consultaIxeAltamiraRequest value for this Request.
     * 
     * @param consultaIxeAltamiraRequest
     */
    public void setConsultaIxeAltamiraRequest(com.banorte.www.ws.ConsultaIdIxeAltamiraRequest.ConsultaIdIxeAltamira consultaIxeAltamiraRequest) {
        this.consultaIxeAltamiraRequest = consultaIxeAltamiraRequest;
    }


    /**
     * Gets the asincrono value for this Request.
     * 
     * @return asincrono
     */
    public com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.RequestAsincrono getAsincrono() {
        return asincrono;
    }


    /**
     * Sets the asincrono value for this Request.
     * 
     * @param asincrono
     */
    public void setAsincrono(com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.RequestAsincrono asincrono) {
        this.asincrono = asincrono;
    }


    /**
     * Gets the idServicio value for this Request.
     * 
     * @return idServicio
     */
    public java.lang.String getIdServicio() {
        return idServicio;
    }


    /**
     * Sets the idServicio value for this Request.
     * 
     * @param idServicio
     */
    public void setIdServicio(java.lang.String idServicio) {
        this.idServicio = idServicio;
    }


    /**
     * Gets the verServicio value for this Request.
     * 
     * @return verServicio
     */
    public java.lang.String getVerServicio() {
        return verServicio;
    }


    /**
     * Sets the verServicio value for this Request.
     * 
     * @param verServicio
     */
    public void setVerServicio(java.lang.String verServicio) {
        this.verServicio = verServicio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Request)) return false;
        Request other = (Request) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.consultaIxeAltamiraRequest==null && other.getConsultaIxeAltamiraRequest()==null) || 
             (this.consultaIxeAltamiraRequest!=null &&
              this.consultaIxeAltamiraRequest.equals(other.getConsultaIxeAltamiraRequest()))) &&
            ((this.asincrono==null && other.getAsincrono()==null) || 
             (this.asincrono!=null &&
              this.asincrono.equals(other.getAsincrono()))) &&
            ((this.idServicio==null && other.getIdServicio()==null) || 
             (this.idServicio!=null &&
              this.idServicio.equals(other.getIdServicio()))) &&
            ((this.verServicio==null && other.getVerServicio()==null) || 
             (this.verServicio!=null &&
              this.verServicio.equals(other.getVerServicio())));
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
        if (getConsultaIxeAltamiraRequest() != null) {
            _hashCode += getConsultaIxeAltamiraRequest().hashCode();
        }
        if (getAsincrono() != null) {
            _hashCode += getAsincrono().hashCode();
        }
        if (getIdServicio() != null) {
            _hashCode += getIdServicio().hashCode();
        }
        if (getVerServicio() != null) {
            _hashCode += getVerServicio().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Request.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "Request"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("asincrono");
        attrField.setXmlName(new javax.xml.namespace.QName("", "asincrono"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", ">Request>asincrono"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("idServicio");
        attrField.setXmlName(new javax.xml.namespace.QName("", "idServicio"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("verServicio");
        attrField.setXmlName(new javax.xml.namespace.QName("", "verServicio"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("consultaIxeAltamiraRequest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraRequest", "ConsultaIxeAltamiraRequest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraRequest", "ConsultaIdIxeAltamira"));
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

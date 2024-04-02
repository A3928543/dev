/**
 * HeaderResType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira;

public class HeaderResType  implements java.io.Serializable {
    private java.lang.String ticket;

    private com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ErrorType error;

    private com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.HeaderResTypeVerHeaderRes verHeaderRes;  // attribute

    public HeaderResType() {
    }

    public HeaderResType(
           java.lang.String ticket,
           com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ErrorType error,
           com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.HeaderResTypeVerHeaderRes verHeaderRes) {
           this.ticket = ticket;
           this.error = error;
           this.verHeaderRes = verHeaderRes;
    }


    /**
     * Gets the ticket value for this HeaderResType.
     * 
     * @return ticket
     */
    public java.lang.String getTicket() {
        return ticket;
    }


    /**
     * Sets the ticket value for this HeaderResType.
     * 
     * @param ticket
     */
    public void setTicket(java.lang.String ticket) {
        this.ticket = ticket;
    }


    /**
     * Gets the error value for this HeaderResType.
     * 
     * @return error
     */
    public com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ErrorType getError() {
        return error;
    }


    /**
     * Sets the error value for this HeaderResType.
     * 
     * @param error
     */
    public void setError(com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.ErrorType error) {
        this.error = error;
    }


    /**
     * Gets the verHeaderRes value for this HeaderResType.
     * 
     * @return verHeaderRes
     */
    public com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.HeaderResTypeVerHeaderRes getVerHeaderRes() {
        return verHeaderRes;
    }


    /**
     * Sets the verHeaderRes value for this HeaderResType.
     * 
     * @param verHeaderRes
     */
    public void setVerHeaderRes(com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira.HeaderResTypeVerHeaderRes verHeaderRes) {
        this.verHeaderRes = verHeaderRes;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof HeaderResType)) return false;
        HeaderResType other = (HeaderResType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ticket==null && other.getTicket()==null) || 
             (this.ticket!=null &&
              this.ticket.equals(other.getTicket()))) &&
            ((this.error==null && other.getError()==null) || 
             (this.error!=null &&
              this.error.equals(other.getError()))) &&
            ((this.verHeaderRes==null && other.getVerHeaderRes()==null) || 
             (this.verHeaderRes!=null &&
              this.verHeaderRes.equals(other.getVerHeaderRes())));
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
        if (getTicket() != null) {
            _hashCode += getTicket().hashCode();
        }
        if (getError() != null) {
            _hashCode += getError().hashCode();
        }
        if (getVerHeaderRes() != null) {
            _hashCode += getVerHeaderRes().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(HeaderResType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "HeaderResType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("verHeaderRes");
        attrField.setXmlName(new javax.xml.namespace.QName("", "verHeaderRes"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", ">HeaderResType>verHeaderRes"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ticket");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "Ticket"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("error");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "Error"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "ErrorType"));
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

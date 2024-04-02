/**
 * ResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira;

public class ResponseType  implements java.io.Serializable {
    private com.banorte.www.ws.ConsultaIdIxeAltamiraResponse.PemeBn0[] consultaIdIxeAltamiraResponse;

    public ResponseType() {
    }

    public ResponseType(
           com.banorte.www.ws.ConsultaIdIxeAltamiraResponse.PemeBn0[] consultaIdIxeAltamiraResponse) {
           this.consultaIdIxeAltamiraResponse = consultaIdIxeAltamiraResponse;
    }


    /**
     * Gets the consultaIdIxeAltamiraResponse value for this ResponseType.
     * 
     * @return consultaIdIxeAltamiraResponse
     */
    public com.banorte.www.ws.ConsultaIdIxeAltamiraResponse.PemeBn0[] getConsultaIdIxeAltamiraResponse() {
        return consultaIdIxeAltamiraResponse;
    }


    /**
     * Sets the consultaIdIxeAltamiraResponse value for this ResponseType.
     * 
     * @param consultaIdIxeAltamiraResponse
     */
    public void setConsultaIdIxeAltamiraResponse(com.banorte.www.ws.ConsultaIdIxeAltamiraResponse.PemeBn0[] consultaIdIxeAltamiraResponse) {
        this.consultaIdIxeAltamiraResponse = consultaIdIxeAltamiraResponse;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResponseType)) return false;
        ResponseType other = (ResponseType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.consultaIdIxeAltamiraResponse==null && other.getConsultaIdIxeAltamiraResponse()==null) || 
             (this.consultaIdIxeAltamiraResponse!=null &&
              java.util.Arrays.equals(this.consultaIdIxeAltamiraResponse, other.getConsultaIdIxeAltamiraResponse())));
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
        if (getConsultaIdIxeAltamiraResponse() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getConsultaIdIxeAltamiraResponse());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getConsultaIdIxeAltamiraResponse(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResponseType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "ResponseType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("consultaIdIxeAltamiraResponse");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", "ConsultaIdIxeAltamiraResponse"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", "ConsultaIdIxeAltamiraType"));
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

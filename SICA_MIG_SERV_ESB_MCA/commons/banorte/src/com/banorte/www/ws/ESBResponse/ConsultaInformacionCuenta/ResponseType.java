/**
 * ResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.ESBResponse.ConsultaInformacionCuenta;

public class ResponseType  implements java.io.Serializable {
    private com.banorte.www.ws.esb.ConsultaInfoResponse.ConsultaInfoCuentaResponse consultaInfoCuentaResponse;

    public ResponseType() {
    }

    public ResponseType(
           com.banorte.www.ws.esb.ConsultaInfoResponse.ConsultaInfoCuentaResponse consultaInfoCuentaResponse) {
           this.consultaInfoCuentaResponse = consultaInfoCuentaResponse;
    }


    /**
     * Gets the consultaInfoCuentaResponse value for this ResponseType.
     * 
     * @return consultaInfoCuentaResponse
     */
    public com.banorte.www.ws.esb.ConsultaInfoResponse.ConsultaInfoCuentaResponse getConsultaInfoCuentaResponse() {
        return consultaInfoCuentaResponse;
    }


    /**
     * Sets the consultaInfoCuentaResponse value for this ResponseType.
     * 
     * @param consultaInfoCuentaResponse
     */
    public void setConsultaInfoCuentaResponse(com.banorte.www.ws.esb.ConsultaInfoResponse.ConsultaInfoCuentaResponse consultaInfoCuentaResponse) {
        this.consultaInfoCuentaResponse = consultaInfoCuentaResponse;
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
            ((this.consultaInfoCuentaResponse==null && other.getConsultaInfoCuentaResponse()==null) || 
             (this.consultaInfoCuentaResponse!=null &&
              this.consultaInfoCuentaResponse.equals(other.getConsultaInfoCuentaResponse())));
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
        if (getConsultaInfoCuentaResponse() != null) {
            _hashCode += getConsultaInfoCuentaResponse().hashCode();
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
        elemField.setFieldName("consultaInfoCuentaResponse");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "ConsultaInfoCuentaResponse"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "ConsultaInfoCuentaResponse"));
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

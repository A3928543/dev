/**
 * ResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.ESBResponse;

public class ResponseType  implements java.io.Serializable {
    private com.banorte.www.ws.MantenimientoDatosBasicosClienteResponse.MantenimientoDatosBasicosClienteResponseType mantenimientoDatosBasicosClienteResponse;

    public ResponseType() {
    }

    public ResponseType(
           com.banorte.www.ws.MantenimientoDatosBasicosClienteResponse.MantenimientoDatosBasicosClienteResponseType mantenimientoDatosBasicosClienteResponse) {
           this.mantenimientoDatosBasicosClienteResponse = mantenimientoDatosBasicosClienteResponse;
    }


    /**
     * Gets the mantenimientoDatosBasicosClienteResponse value for this ResponseType.
     * 
     * @return mantenimientoDatosBasicosClienteResponse
     */
    public com.banorte.www.ws.MantenimientoDatosBasicosClienteResponse.MantenimientoDatosBasicosClienteResponseType getMantenimientoDatosBasicosClienteResponse() {
        return mantenimientoDatosBasicosClienteResponse;
    }


    /**
     * Sets the mantenimientoDatosBasicosClienteResponse value for this ResponseType.
     * 
     * @param mantenimientoDatosBasicosClienteResponse
     */
    public void setMantenimientoDatosBasicosClienteResponse(com.banorte.www.ws.MantenimientoDatosBasicosClienteResponse.MantenimientoDatosBasicosClienteResponseType mantenimientoDatosBasicosClienteResponse) {
        this.mantenimientoDatosBasicosClienteResponse = mantenimientoDatosBasicosClienteResponse;
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
            ((this.mantenimientoDatosBasicosClienteResponse==null && other.getMantenimientoDatosBasicosClienteResponse()==null) || 
             (this.mantenimientoDatosBasicosClienteResponse!=null &&
              this.mantenimientoDatosBasicosClienteResponse.equals(other.getMantenimientoDatosBasicosClienteResponse())));
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
        if (getMantenimientoDatosBasicosClienteResponse() != null) {
            _hashCode += getMantenimientoDatosBasicosClienteResponse().hashCode();
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
        elemField.setFieldName("mantenimientoDatosBasicosClienteResponse");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/MantenimientoDatosBasicosClienteResponse", "MantenimientoDatosBasicosClienteResponse"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/MantenimientoDatosBasicosClienteResponse", "MantenimientoDatosBasicosClienteResponseType"));
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

/**
 * BGM1624.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.esb.ConsultaInfoResponse;

public class BGM1624  implements java.io.Serializable {
    private java.lang.String descripcionBloqueo;

    public BGM1624() {
    }

    public BGM1624(
           java.lang.String descripcionBloqueo) {
           this.descripcionBloqueo = descripcionBloqueo;
    }


    /**
     * Gets the descripcionBloqueo value for this BGM1624.
     * 
     * @return descripcionBloqueo
     */
    public java.lang.String getDescripcionBloqueo() {
        return descripcionBloqueo;
    }


    /**
     * Sets the descripcionBloqueo value for this BGM1624.
     * 
     * @param descripcionBloqueo
     */
    public void setDescripcionBloqueo(java.lang.String descripcionBloqueo) {
        this.descripcionBloqueo = descripcionBloqueo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BGM1624)) return false;
        BGM1624 other = (BGM1624) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.descripcionBloqueo==null && other.getDescripcionBloqueo()==null) || 
             (this.descripcionBloqueo!=null &&
              this.descripcionBloqueo.equals(other.getDescripcionBloqueo())));
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
        if (getDescripcionBloqueo() != null) {
            _hashCode += getDescripcionBloqueo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BGM1624.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "BGM1624"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionBloqueo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "DescripcionBloqueo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

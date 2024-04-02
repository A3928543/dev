/**
 * MantenimientoDatosBasicosClienteResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.MantenimientoDatosBasicosClienteResponse;

public class MantenimientoDatosBasicosClienteResponseType  implements java.io.Serializable {
    private com.banorte.www.ws.MantenimientoDatosBasicosClienteResponse.Penc4001 penc4001;

    public MantenimientoDatosBasicosClienteResponseType() {
    }

    public MantenimientoDatosBasicosClienteResponseType(
           com.banorte.www.ws.MantenimientoDatosBasicosClienteResponse.Penc4001 penc4001) {
           this.penc4001 = penc4001;
    }


    /**
     * Gets the penc4001 value for this MantenimientoDatosBasicosClienteResponseType.
     * 
     * @return penc4001
     */
    public com.banorte.www.ws.MantenimientoDatosBasicosClienteResponse.Penc4001 getPenc4001() {
        return penc4001;
    }


    /**
     * Sets the penc4001 value for this MantenimientoDatosBasicosClienteResponseType.
     * 
     * @param penc4001
     */
    public void setPenc4001(com.banorte.www.ws.MantenimientoDatosBasicosClienteResponse.Penc4001 penc4001) {
        this.penc4001 = penc4001;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MantenimientoDatosBasicosClienteResponseType)) return false;
        MantenimientoDatosBasicosClienteResponseType other = (MantenimientoDatosBasicosClienteResponseType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.penc4001==null && other.getPenc4001()==null) || 
             (this.penc4001!=null &&
              this.penc4001.equals(other.getPenc4001())));
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
        if (getPenc4001() != null) {
            _hashCode += getPenc4001().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MantenimientoDatosBasicosClienteResponseType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/MantenimientoDatosBasicosClienteResponse", "MantenimientoDatosBasicosClienteResponseType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("penc4001");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/MantenimientoDatosBasicosClienteResponse", "Penc4001"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/MantenimientoDatosBasicosClienteResponse", "Penc4001"));
        elemField.setMinOccurs(0);
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

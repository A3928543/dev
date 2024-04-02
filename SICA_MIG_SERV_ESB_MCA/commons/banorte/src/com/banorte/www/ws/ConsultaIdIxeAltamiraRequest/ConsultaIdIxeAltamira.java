/**
 * ConsultaIdIxeAltamira.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.ConsultaIdIxeAltamiraRequest;

public class ConsultaIdIxeAltamira  implements java.io.Serializable {
    private java.lang.String numClIx;

    private java.lang.String numClBn;

    public ConsultaIdIxeAltamira() {
    }

    public ConsultaIdIxeAltamira(
           java.lang.String numClIx,
           java.lang.String numClBn) {
           this.numClIx = numClIx;
           this.numClBn = numClBn;
    }


    /**
     * Gets the numClIx value for this ConsultaIdIxeAltamira.
     * 
     * @return numClIx
     */
    public java.lang.String getNumClIx() {
        return numClIx;
    }


    /**
     * Sets the numClIx value for this ConsultaIdIxeAltamira.
     * 
     * @param numClIx
     */
    public void setNumClIx(java.lang.String numClIx) {
        this.numClIx = numClIx;
    }


    /**
     * Gets the numClBn value for this ConsultaIdIxeAltamira.
     * 
     * @return numClBn
     */
    public java.lang.String getNumClBn() {
        return numClBn;
    }


    /**
     * Sets the numClBn value for this ConsultaIdIxeAltamira.
     * 
     * @param numClBn
     */
    public void setNumClBn(java.lang.String numClBn) {
        this.numClBn = numClBn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaIdIxeAltamira)) return false;
        ConsultaIdIxeAltamira other = (ConsultaIdIxeAltamira) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.numClIx==null && other.getNumClIx()==null) || 
             (this.numClIx!=null &&
              this.numClIx.equals(other.getNumClIx()))) &&
            ((this.numClBn==null && other.getNumClBn()==null) || 
             (this.numClBn!=null &&
              this.numClBn.equals(other.getNumClBn())));
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
        if (getNumClIx() != null) {
            _hashCode += getNumClIx().hashCode();
        }
        if (getNumClBn() != null) {
            _hashCode += getNumClBn().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultaIdIxeAltamira.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraRequest", "ConsultaIdIxeAltamira"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numClIx");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraRequest", "NumClIx"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numClBn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraRequest", "NumClBn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

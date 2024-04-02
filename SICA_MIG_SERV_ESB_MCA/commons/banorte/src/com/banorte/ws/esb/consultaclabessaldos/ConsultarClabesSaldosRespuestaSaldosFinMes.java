/**
 * ConsultarClabesSaldosRespuestaSaldosFinMes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.consultaclabessaldos;

public class ConsultarClabesSaldosRespuestaSaldosFinMes  implements java.io.Serializable {
    /* TIT1. SALDOS FIN MES */
    private java.lang.String titUnoSaldosFinMes;

    public ConsultarClabesSaldosRespuestaSaldosFinMes() {
    }

    public ConsultarClabesSaldosRespuestaSaldosFinMes(
           java.lang.String titUnoSaldosFinMes) {
           this.titUnoSaldosFinMes = titUnoSaldosFinMes;
    }


    /**
     * Gets the titUnoSaldosFinMes value for this ConsultarClabesSaldosRespuestaSaldosFinMes.
     * 
     * @return titUnoSaldosFinMes   * TIT1. SALDOS FIN MES
     */
    public java.lang.String getTitUnoSaldosFinMes() {
        return titUnoSaldosFinMes;
    }


    /**
     * Sets the titUnoSaldosFinMes value for this ConsultarClabesSaldosRespuestaSaldosFinMes.
     * 
     * @param titUnoSaldosFinMes   * TIT1. SALDOS FIN MES
     */
    public void setTitUnoSaldosFinMes(java.lang.String titUnoSaldosFinMes) {
        this.titUnoSaldosFinMes = titUnoSaldosFinMes;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultarClabesSaldosRespuestaSaldosFinMes)) return false;
        ConsultarClabesSaldosRespuestaSaldosFinMes other = (ConsultarClabesSaldosRespuestaSaldosFinMes) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.titUnoSaldosFinMes==null && other.getTitUnoSaldosFinMes()==null) || 
             (this.titUnoSaldosFinMes!=null &&
              this.titUnoSaldosFinMes.equals(other.getTitUnoSaldosFinMes())));
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
        if (getTitUnoSaldosFinMes() != null) {
            _hashCode += getTitUnoSaldosFinMes().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultarClabesSaldosRespuestaSaldosFinMes.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaClabesSaldos", ">consultarClabesSaldosRespuesta>SaldosFinMes"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("titUnoSaldosFinMes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TitUnoSaldosFinMes"));
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

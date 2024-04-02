/**
 * ConsultarClabesSaldosRespuestaDatosSaldoMen.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.consultaclabessaldos;

public class ConsultarClabesSaldosRespuestaDatosSaldoMen  implements java.io.Serializable {
    private java.util.Date fechaFinMes;

    /* SALDO FIN MES */
    private java.lang.String saldoFinMes;

    public ConsultarClabesSaldosRespuestaDatosSaldoMen() {
    }

    public ConsultarClabesSaldosRespuestaDatosSaldoMen(
           java.util.Date fechaFinMes,
           java.lang.String saldoFinMes) {
           this.fechaFinMes = fechaFinMes;
           this.saldoFinMes = saldoFinMes;
    }


    /**
     * Gets the fechaFinMes value for this ConsultarClabesSaldosRespuestaDatosSaldoMen.
     * 
     * @return fechaFinMes
     */
    public java.util.Date getFechaFinMes() {
        return fechaFinMes;
    }


    /**
     * Sets the fechaFinMes value for this ConsultarClabesSaldosRespuestaDatosSaldoMen.
     * 
     * @param fechaFinMes
     */
    public void setFechaFinMes(java.util.Date fechaFinMes) {
        this.fechaFinMes = fechaFinMes;
    }


    /**
     * Gets the saldoFinMes value for this ConsultarClabesSaldosRespuestaDatosSaldoMen.
     * 
     * @return saldoFinMes   * SALDO FIN MES
     */
    public java.lang.String getSaldoFinMes() {
        return saldoFinMes;
    }


    /**
     * Sets the saldoFinMes value for this ConsultarClabesSaldosRespuestaDatosSaldoMen.
     * 
     * @param saldoFinMes   * SALDO FIN MES
     */
    public void setSaldoFinMes(java.lang.String saldoFinMes) {
        this.saldoFinMes = saldoFinMes;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultarClabesSaldosRespuestaDatosSaldoMen)) return false;
        ConsultarClabesSaldosRespuestaDatosSaldoMen other = (ConsultarClabesSaldosRespuestaDatosSaldoMen) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fechaFinMes==null && other.getFechaFinMes()==null) || 
             (this.fechaFinMes!=null &&
              this.fechaFinMes.equals(other.getFechaFinMes()))) &&
            ((this.saldoFinMes==null && other.getSaldoFinMes()==null) || 
             (this.saldoFinMes!=null &&
              this.saldoFinMes.equals(other.getSaldoFinMes())));
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
        if (getFechaFinMes() != null) {
            _hashCode += getFechaFinMes().hashCode();
        }
        if (getSaldoFinMes() != null) {
            _hashCode += getSaldoFinMes().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultarClabesSaldosRespuestaDatosSaldoMen.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaClabesSaldos", ">consultarClabesSaldosRespuesta>DatosSaldoMen"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaFinMes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FechaFinMes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saldoFinMes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SaldoFinMes"));
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

/**
 * ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.consultaclabessaldos;

public class ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria  implements java.io.Serializable {
    /* FECHAOP – Tran_FechaOperacion */
    private java.util.Date tran_FechaOperacion;

    /* Numero de cuenta */
    private java.lang.String tran_NumeroCuenta;

    /* Estatus Cuenta – Estatus de la cuenta */
    private java.lang.String estatusCuenta;

    /* Nombre del Titular */
    private java.lang.String nombreTitular;

    /* LIMITE DE LA CUENTA */
    private java.math.BigDecimal limiteCuenta;

    public ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria() {
    }

    public ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria(
           java.util.Date tran_FechaOperacion,
           java.lang.String tran_NumeroCuenta,
           java.lang.String estatusCuenta,
           java.lang.String nombreTitular,
           java.math.BigDecimal limiteCuenta) {
           this.tran_FechaOperacion = tran_FechaOperacion;
           this.tran_NumeroCuenta = tran_NumeroCuenta;
           this.estatusCuenta = estatusCuenta;
           this.nombreTitular = nombreTitular;
           this.limiteCuenta = limiteCuenta;
    }


    /**
     * Gets the tran_FechaOperacion value for this ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria.
     * 
     * @return tran_FechaOperacion   * FECHAOP – Tran_FechaOperacion
     */
    public java.util.Date getTran_FechaOperacion() {
        return tran_FechaOperacion;
    }


    /**
     * Sets the tran_FechaOperacion value for this ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria.
     * 
     * @param tran_FechaOperacion   * FECHAOP – Tran_FechaOperacion
     */
    public void setTran_FechaOperacion(java.util.Date tran_FechaOperacion) {
        this.tran_FechaOperacion = tran_FechaOperacion;
    }


    /**
     * Gets the tran_NumeroCuenta value for this ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria.
     * 
     * @return tran_NumeroCuenta   * Numero de cuenta
     */
    public java.lang.String getTran_NumeroCuenta() {
        return tran_NumeroCuenta;
    }


    /**
     * Sets the tran_NumeroCuenta value for this ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria.
     * 
     * @param tran_NumeroCuenta   * Numero de cuenta
     */
    public void setTran_NumeroCuenta(java.lang.String tran_NumeroCuenta) {
        this.tran_NumeroCuenta = tran_NumeroCuenta;
    }


    /**
     * Gets the estatusCuenta value for this ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria.
     * 
     * @return estatusCuenta   * Estatus Cuenta – Estatus de la cuenta
     */
    public java.lang.String getEstatusCuenta() {
        return estatusCuenta;
    }


    /**
     * Sets the estatusCuenta value for this ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria.
     * 
     * @param estatusCuenta   * Estatus Cuenta – Estatus de la cuenta
     */
    public void setEstatusCuenta(java.lang.String estatusCuenta) {
        this.estatusCuenta = estatusCuenta;
    }


    /**
     * Gets the nombreTitular value for this ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria.
     * 
     * @return nombreTitular   * Nombre del Titular
     */
    public java.lang.String getNombreTitular() {
        return nombreTitular;
    }


    /**
     * Sets the nombreTitular value for this ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria.
     * 
     * @param nombreTitular   * Nombre del Titular
     */
    public void setNombreTitular(java.lang.String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }


    /**
     * Gets the limiteCuenta value for this ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria.
     * 
     * @return limiteCuenta   * LIMITE DE LA CUENTA
     */
    public java.math.BigDecimal getLimiteCuenta() {
        return limiteCuenta;
    }


    /**
     * Sets the limiteCuenta value for this ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria.
     * 
     * @param limiteCuenta   * LIMITE DE LA CUENTA
     */
    public void setLimiteCuenta(java.math.BigDecimal limiteCuenta) {
        this.limiteCuenta = limiteCuenta;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria)) return false;
        ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria other = (ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tran_FechaOperacion==null && other.getTran_FechaOperacion()==null) || 
             (this.tran_FechaOperacion!=null &&
              this.tran_FechaOperacion.equals(other.getTran_FechaOperacion()))) &&
            ((this.tran_NumeroCuenta==null && other.getTran_NumeroCuenta()==null) || 
             (this.tran_NumeroCuenta!=null &&
              this.tran_NumeroCuenta.equals(other.getTran_NumeroCuenta()))) &&
            ((this.estatusCuenta==null && other.getEstatusCuenta()==null) || 
             (this.estatusCuenta!=null &&
              this.estatusCuenta.equals(other.getEstatusCuenta()))) &&
            ((this.nombreTitular==null && other.getNombreTitular()==null) || 
             (this.nombreTitular!=null &&
              this.nombreTitular.equals(other.getNombreTitular()))) &&
            ((this.limiteCuenta==null && other.getLimiteCuenta()==null) || 
             (this.limiteCuenta!=null &&
              this.limiteCuenta.equals(other.getLimiteCuenta())));
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
        if (getTran_FechaOperacion() != null) {
            _hashCode += getTran_FechaOperacion().hashCode();
        }
        if (getTran_NumeroCuenta() != null) {
            _hashCode += getTran_NumeroCuenta().hashCode();
        }
        if (getEstatusCuenta() != null) {
            _hashCode += getEstatusCuenta().hashCode();
        }
        if (getNombreTitular() != null) {
            _hashCode += getNombreTitular().hashCode();
        }
        if (getLimiteCuenta() != null) {
            _hashCode += getLimiteCuenta().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultarClabesSaldosRespuestaConsultaCuentaBeneficiaria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaClabesSaldos", ">consultarClabesSaldosRespuesta>ConsultaCuentaBeneficiaria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tran_FechaOperacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Tran_FechaOperacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tran_NumeroCuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Tran_NumeroCuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estatusCuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EstatusCuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreTitular");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NombreTitular"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("limiteCuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LimiteCuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
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

/**
 * ConsultarClabesSaldosPeticion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.consultaclabessaldos;

public class ConsultarClabesSaldosPeticion  implements java.io.Serializable {
    /* TIPO DE MOVIMIENTO */
    private java.lang.String tipoMovimiento;

    /* NUMERO DE CUENTA */
    private java.lang.String tran_NumeroCuenta;

    private java.math.BigInteger fechaInicio;

    private java.math.BigInteger fechaFinal;

    /* CLAVE D SERVICIO NTF */
    private java.lang.String claveServicioNtf;

    public ConsultarClabesSaldosPeticion() {
    }

    public ConsultarClabesSaldosPeticion(
           java.lang.String tipoMovimiento,
           java.lang.String tran_NumeroCuenta,
           java.math.BigInteger fechaInicio,
           java.math.BigInteger fechaFinal,
           java.lang.String claveServicioNtf) {
           this.tipoMovimiento = tipoMovimiento;
           this.tran_NumeroCuenta = tran_NumeroCuenta;
           this.fechaInicio = fechaInicio;
           this.fechaFinal = fechaFinal;
           this.claveServicioNtf = claveServicioNtf;
    }


    /**
     * Gets the tipoMovimiento value for this ConsultarClabesSaldosPeticion.
     * 
     * @return tipoMovimiento   * TIPO DE MOVIMIENTO
     */
    public java.lang.String getTipoMovimiento() {
        return tipoMovimiento;
    }


    /**
     * Sets the tipoMovimiento value for this ConsultarClabesSaldosPeticion.
     * 
     * @param tipoMovimiento   * TIPO DE MOVIMIENTO
     */
    public void setTipoMovimiento(java.lang.String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }


    /**
     * Gets the tran_NumeroCuenta value for this ConsultarClabesSaldosPeticion.
     * 
     * @return tran_NumeroCuenta   * NUMERO DE CUENTA
     */
    public java.lang.String getTran_NumeroCuenta() {
        return tran_NumeroCuenta;
    }


    /**
     * Sets the tran_NumeroCuenta value for this ConsultarClabesSaldosPeticion.
     * 
     * @param tran_NumeroCuenta   * NUMERO DE CUENTA
     */
    public void setTran_NumeroCuenta(java.lang.String tran_NumeroCuenta) {
        this.tran_NumeroCuenta = tran_NumeroCuenta;
    }


    /**
     * Gets the fechaInicio value for this ConsultarClabesSaldosPeticion.
     * 
     * @return fechaInicio
     */
    public java.math.BigInteger getFechaInicio() {
        return fechaInicio;
    }


    /**
     * Sets the fechaInicio value for this ConsultarClabesSaldosPeticion.
     * 
     * @param fechaInicio
     */
    public void setFechaInicio(java.math.BigInteger fechaInicio) {
        this.fechaInicio = fechaInicio;
    }


    /**
     * Gets the fechaFinal value for this ConsultarClabesSaldosPeticion.
     * 
     * @return fechaFinal
     */
    public java.math.BigInteger getFechaFinal() {
        return fechaFinal;
    }


    /**
     * Sets the fechaFinal value for this ConsultarClabesSaldosPeticion.
     * 
     * @param fechaFinal
     */
    public void setFechaFinal(java.math.BigInteger fechaFinal) {
        this.fechaFinal = fechaFinal;
    }


    /**
     * Gets the claveServicioNtf value for this ConsultarClabesSaldosPeticion.
     * 
     * @return claveServicioNtf   * CLAVE D SERVICIO NTF
     */
    public java.lang.String getClaveServicioNtf() {
        return claveServicioNtf;
    }


    /**
     * Sets the claveServicioNtf value for this ConsultarClabesSaldosPeticion.
     * 
     * @param claveServicioNtf   * CLAVE D SERVICIO NTF
     */
    public void setClaveServicioNtf(java.lang.String claveServicioNtf) {
        this.claveServicioNtf = claveServicioNtf;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultarClabesSaldosPeticion)) return false;
        ConsultarClabesSaldosPeticion other = (ConsultarClabesSaldosPeticion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tipoMovimiento==null && other.getTipoMovimiento()==null) || 
             (this.tipoMovimiento!=null &&
              this.tipoMovimiento.equals(other.getTipoMovimiento()))) &&
            ((this.tran_NumeroCuenta==null && other.getTran_NumeroCuenta()==null) || 
             (this.tran_NumeroCuenta!=null &&
              this.tran_NumeroCuenta.equals(other.getTran_NumeroCuenta()))) &&
            ((this.fechaInicio==null && other.getFechaInicio()==null) || 
             (this.fechaInicio!=null &&
              this.fechaInicio.equals(other.getFechaInicio()))) &&
            ((this.fechaFinal==null && other.getFechaFinal()==null) || 
             (this.fechaFinal!=null &&
              this.fechaFinal.equals(other.getFechaFinal()))) &&
            ((this.claveServicioNtf==null && other.getClaveServicioNtf()==null) || 
             (this.claveServicioNtf!=null &&
              this.claveServicioNtf.equals(other.getClaveServicioNtf())));
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
        if (getTipoMovimiento() != null) {
            _hashCode += getTipoMovimiento().hashCode();
        }
        if (getTran_NumeroCuenta() != null) {
            _hashCode += getTran_NumeroCuenta().hashCode();
        }
        if (getFechaInicio() != null) {
            _hashCode += getFechaInicio().hashCode();
        }
        if (getFechaFinal() != null) {
            _hashCode += getFechaFinal().hashCode();
        }
        if (getClaveServicioNtf() != null) {
            _hashCode += getClaveServicioNtf().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultarClabesSaldosPeticion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaClabesSaldos", "consultarClabesSaldosPeticion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoMovimiento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TipoMovimiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tran_NumeroCuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Tran_NumeroCuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaInicio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FechaInicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaFinal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FechaFinal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("claveServicioNtf");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ClaveServicioNtf"));
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

/**
 * ConsultaInfoRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.esb.ConsultaInfoRequest;

public class ConsultaInfoRequest  implements java.io.Serializable {
    private java.lang.String tipoMovimiento;

    private java.lang.String numeroCuenta;

    private java.lang.Integer fechaInicio;

    private java.lang.Integer fechaFin;

    private java.lang.String claveServicio;

    public ConsultaInfoRequest() {
    }

    public ConsultaInfoRequest(
           java.lang.String tipoMovimiento,
           java.lang.String numeroCuenta,
           java.lang.Integer fechaInicio,
           java.lang.Integer fechaFin,
           java.lang.String claveServicio) {
           this.tipoMovimiento = tipoMovimiento;
           this.numeroCuenta = numeroCuenta;
           this.fechaInicio = fechaInicio;
           this.fechaFin = fechaFin;
           this.claveServicio = claveServicio;
    }


    /**
     * Gets the tipoMovimiento value for this ConsultaInfoRequest.
     * 
     * @return tipoMovimiento
     */
    public java.lang.String getTipoMovimiento() {
        return tipoMovimiento;
    }


    /**
     * Sets the tipoMovimiento value for this ConsultaInfoRequest.
     * 
     * @param tipoMovimiento
     */
    public void setTipoMovimiento(java.lang.String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }


    /**
     * Gets the numeroCuenta value for this ConsultaInfoRequest.
     * 
     * @return numeroCuenta
     */
    public java.lang.String getNumeroCuenta() {
        return numeroCuenta;
    }


    /**
     * Sets the numeroCuenta value for this ConsultaInfoRequest.
     * 
     * @param numeroCuenta
     */
    public void setNumeroCuenta(java.lang.String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }


    /**
     * Gets the fechaInicio value for this ConsultaInfoRequest.
     * 
     * @return fechaInicio
     */
    public java.lang.Integer getFechaInicio() {
        return fechaInicio;
    }


    /**
     * Sets the fechaInicio value for this ConsultaInfoRequest.
     * 
     * @param fechaInicio
     */
    public void setFechaInicio(java.lang.Integer fechaInicio) {
        this.fechaInicio = fechaInicio;
    }


    /**
     * Gets the fechaFin value for this ConsultaInfoRequest.
     * 
     * @return fechaFin
     */
    public java.lang.Integer getFechaFin() {
        return fechaFin;
    }


    /**
     * Sets the fechaFin value for this ConsultaInfoRequest.
     * 
     * @param fechaFin
     */
    public void setFechaFin(java.lang.Integer fechaFin) {
        this.fechaFin = fechaFin;
    }


    /**
     * Gets the claveServicio value for this ConsultaInfoRequest.
     * 
     * @return claveServicio
     */
    public java.lang.String getClaveServicio() {
        return claveServicio;
    }


    /**
     * Sets the claveServicio value for this ConsultaInfoRequest.
     * 
     * @param claveServicio
     */
    public void setClaveServicio(java.lang.String claveServicio) {
        this.claveServicio = claveServicio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaInfoRequest)) return false;
        ConsultaInfoRequest other = (ConsultaInfoRequest) obj;
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
            ((this.numeroCuenta==null && other.getNumeroCuenta()==null) || 
             (this.numeroCuenta!=null &&
              this.numeroCuenta.equals(other.getNumeroCuenta()))) &&
            ((this.fechaInicio==null && other.getFechaInicio()==null) || 
             (this.fechaInicio!=null &&
              this.fechaInicio.equals(other.getFechaInicio()))) &&
            ((this.fechaFin==null && other.getFechaFin()==null) || 
             (this.fechaFin!=null &&
              this.fechaFin.equals(other.getFechaFin()))) &&
            ((this.claveServicio==null && other.getClaveServicio()==null) || 
             (this.claveServicio!=null &&
              this.claveServicio.equals(other.getClaveServicio())));
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
        if (getNumeroCuenta() != null) {
            _hashCode += getNumeroCuenta().hashCode();
        }
        if (getFechaInicio() != null) {
            _hashCode += getFechaInicio().hashCode();
        }
        if (getFechaFin() != null) {
            _hashCode += getFechaFin().hashCode();
        }
        if (getClaveServicio() != null) {
            _hashCode += getClaveServicio().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultaInfoRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoRequest", "ConsultaInfoRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoMovimiento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoRequest", "TipoMovimiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroCuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoRequest", "NumeroCuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaInicio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoRequest", "FechaInicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaFin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoRequest", "FechaFin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("claveServicio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoRequest", "ClaveServicio"));
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

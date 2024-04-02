/**
 * BGM1621.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.esb.ConsultaInfoResponse;

public class BGM1621  implements java.io.Serializable {
    private int fechaProceso;

    private java.lang.String numeroCuenta;

    private java.lang.String clabeCuenta;

    private java.lang.String CR;

    private java.lang.String descripcionSucursal;

    private java.math.BigDecimal saldoInicial;

    private java.lang.String tipoDivisa;

    private java.math.BigDecimal saldoDisponible;

    private java.math.BigDecimal saldoRetenido;

    private java.math.BigDecimal saldoDia;

    private java.lang.String regimenFirmas;

    private java.lang.String nombrePromotor;

    public BGM1621() {
    }

    public BGM1621(
           int fechaProceso,
           java.lang.String numeroCuenta,
           java.lang.String clabeCuenta,
           java.lang.String CR,
           java.lang.String descripcionSucursal,
           java.math.BigDecimal saldoInicial,
           java.lang.String tipoDivisa,
           java.math.BigDecimal saldoDisponible,
           java.math.BigDecimal saldoRetenido,
           java.math.BigDecimal saldoDia,
           java.lang.String regimenFirmas,
           java.lang.String nombrePromotor) {
           this.fechaProceso = fechaProceso;
           this.numeroCuenta = numeroCuenta;
           this.clabeCuenta = clabeCuenta;
           this.CR = CR;
           this.descripcionSucursal = descripcionSucursal;
           this.saldoInicial = saldoInicial;
           this.tipoDivisa = tipoDivisa;
           this.saldoDisponible = saldoDisponible;
           this.saldoRetenido = saldoRetenido;
           this.saldoDia = saldoDia;
           this.regimenFirmas = regimenFirmas;
           this.nombrePromotor = nombrePromotor;
    }


    /**
     * Gets the fechaProceso value for this BGM1621.
     * 
     * @return fechaProceso
     */
    public int getFechaProceso() {
        return fechaProceso;
    }


    /**
     * Sets the fechaProceso value for this BGM1621.
     * 
     * @param fechaProceso
     */
    public void setFechaProceso(int fechaProceso) {
        this.fechaProceso = fechaProceso;
    }


    /**
     * Gets the numeroCuenta value for this BGM1621.
     * 
     * @return numeroCuenta
     */
    public java.lang.String getNumeroCuenta() {
        return numeroCuenta;
    }


    /**
     * Sets the numeroCuenta value for this BGM1621.
     * 
     * @param numeroCuenta
     */
    public void setNumeroCuenta(java.lang.String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }


    /**
     * Gets the clabeCuenta value for this BGM1621.
     * 
     * @return clabeCuenta
     */
    public java.lang.String getClabeCuenta() {
        return clabeCuenta;
    }


    /**
     * Sets the clabeCuenta value for this BGM1621.
     * 
     * @param clabeCuenta
     */
    public void setClabeCuenta(java.lang.String clabeCuenta) {
        this.clabeCuenta = clabeCuenta;
    }


    /**
     * Gets the CR value for this BGM1621.
     * 
     * @return CR
     */
    public java.lang.String getCR() {
        return CR;
    }


    /**
     * Sets the CR value for this BGM1621.
     * 
     * @param CR
     */
    public void setCR(java.lang.String CR) {
        this.CR = CR;
    }


    /**
     * Gets the descripcionSucursal value for this BGM1621.
     * 
     * @return descripcionSucursal
     */
    public java.lang.String getDescripcionSucursal() {
        return descripcionSucursal;
    }


    /**
     * Sets the descripcionSucursal value for this BGM1621.
     * 
     * @param descripcionSucursal
     */
    public void setDescripcionSucursal(java.lang.String descripcionSucursal) {
        this.descripcionSucursal = descripcionSucursal;
    }


    /**
     * Gets the saldoInicial value for this BGM1621.
     * 
     * @return saldoInicial
     */
    public java.math.BigDecimal getSaldoInicial() {
        return saldoInicial;
    }


    /**
     * Sets the saldoInicial value for this BGM1621.
     * 
     * @param saldoInicial
     */
    public void setSaldoInicial(java.math.BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }


    /**
     * Gets the tipoDivisa value for this BGM1621.
     * 
     * @return tipoDivisa
     */
    public java.lang.String getTipoDivisa() {
        return tipoDivisa;
    }


    /**
     * Sets the tipoDivisa value for this BGM1621.
     * 
     * @param tipoDivisa
     */
    public void setTipoDivisa(java.lang.String tipoDivisa) {
        this.tipoDivisa = tipoDivisa;
    }


    /**
     * Gets the saldoDisponible value for this BGM1621.
     * 
     * @return saldoDisponible
     */
    public java.math.BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }


    /**
     * Sets the saldoDisponible value for this BGM1621.
     * 
     * @param saldoDisponible
     */
    public void setSaldoDisponible(java.math.BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }


    /**
     * Gets the saldoRetenido value for this BGM1621.
     * 
     * @return saldoRetenido
     */
    public java.math.BigDecimal getSaldoRetenido() {
        return saldoRetenido;
    }


    /**
     * Sets the saldoRetenido value for this BGM1621.
     * 
     * @param saldoRetenido
     */
    public void setSaldoRetenido(java.math.BigDecimal saldoRetenido) {
        this.saldoRetenido = saldoRetenido;
    }


    /**
     * Gets the saldoDia value for this BGM1621.
     * 
     * @return saldoDia
     */
    public java.math.BigDecimal getSaldoDia() {
        return saldoDia;
    }


    /**
     * Sets the saldoDia value for this BGM1621.
     * 
     * @param saldoDia
     */
    public void setSaldoDia(java.math.BigDecimal saldoDia) {
        this.saldoDia = saldoDia;
    }


    /**
     * Gets the regimenFirmas value for this BGM1621.
     * 
     * @return regimenFirmas
     */
    public java.lang.String getRegimenFirmas() {
        return regimenFirmas;
    }


    /**
     * Sets the regimenFirmas value for this BGM1621.
     * 
     * @param regimenFirmas
     */
    public void setRegimenFirmas(java.lang.String regimenFirmas) {
        this.regimenFirmas = regimenFirmas;
    }


    /**
     * Gets the nombrePromotor value for this BGM1621.
     * 
     * @return nombrePromotor
     */
    public java.lang.String getNombrePromotor() {
        return nombrePromotor;
    }


    /**
     * Sets the nombrePromotor value for this BGM1621.
     * 
     * @param nombrePromotor
     */
    public void setNombrePromotor(java.lang.String nombrePromotor) {
        this.nombrePromotor = nombrePromotor;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BGM1621)) return false;
        BGM1621 other = (BGM1621) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.fechaProceso == other.getFechaProceso() &&
            ((this.numeroCuenta==null && other.getNumeroCuenta()==null) || 
             (this.numeroCuenta!=null &&
              this.numeroCuenta.equals(other.getNumeroCuenta()))) &&
            ((this.clabeCuenta==null && other.getClabeCuenta()==null) || 
             (this.clabeCuenta!=null &&
              this.clabeCuenta.equals(other.getClabeCuenta()))) &&
            ((this.CR==null && other.getCR()==null) || 
             (this.CR!=null &&
              this.CR.equals(other.getCR()))) &&
            ((this.descripcionSucursal==null && other.getDescripcionSucursal()==null) || 
             (this.descripcionSucursal!=null &&
              this.descripcionSucursal.equals(other.getDescripcionSucursal()))) &&
            ((this.saldoInicial==null && other.getSaldoInicial()==null) || 
             (this.saldoInicial!=null &&
              this.saldoInicial.equals(other.getSaldoInicial()))) &&
            ((this.tipoDivisa==null && other.getTipoDivisa()==null) || 
             (this.tipoDivisa!=null &&
              this.tipoDivisa.equals(other.getTipoDivisa()))) &&
            ((this.saldoDisponible==null && other.getSaldoDisponible()==null) || 
             (this.saldoDisponible!=null &&
              this.saldoDisponible.equals(other.getSaldoDisponible()))) &&
            ((this.saldoRetenido==null && other.getSaldoRetenido()==null) || 
             (this.saldoRetenido!=null &&
              this.saldoRetenido.equals(other.getSaldoRetenido()))) &&
            ((this.saldoDia==null && other.getSaldoDia()==null) || 
             (this.saldoDia!=null &&
              this.saldoDia.equals(other.getSaldoDia()))) &&
            ((this.regimenFirmas==null && other.getRegimenFirmas()==null) || 
             (this.regimenFirmas!=null &&
              this.regimenFirmas.equals(other.getRegimenFirmas()))) &&
            ((this.nombrePromotor==null && other.getNombrePromotor()==null) || 
             (this.nombrePromotor!=null &&
              this.nombrePromotor.equals(other.getNombrePromotor())));
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
        _hashCode += getFechaProceso();
        if (getNumeroCuenta() != null) {
            _hashCode += getNumeroCuenta().hashCode();
        }
        if (getClabeCuenta() != null) {
            _hashCode += getClabeCuenta().hashCode();
        }
        if (getCR() != null) {
            _hashCode += getCR().hashCode();
        }
        if (getDescripcionSucursal() != null) {
            _hashCode += getDescripcionSucursal().hashCode();
        }
        if (getSaldoInicial() != null) {
            _hashCode += getSaldoInicial().hashCode();
        }
        if (getTipoDivisa() != null) {
            _hashCode += getTipoDivisa().hashCode();
        }
        if (getSaldoDisponible() != null) {
            _hashCode += getSaldoDisponible().hashCode();
        }
        if (getSaldoRetenido() != null) {
            _hashCode += getSaldoRetenido().hashCode();
        }
        if (getSaldoDia() != null) {
            _hashCode += getSaldoDia().hashCode();
        }
        if (getRegimenFirmas() != null) {
            _hashCode += getRegimenFirmas().hashCode();
        }
        if (getNombrePromotor() != null) {
            _hashCode += getNombrePromotor().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BGM1621.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "BGM1621"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaProceso");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "FechaProceso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroCuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "NumeroCuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clabeCuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "ClabeCuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "CR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionSucursal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "DescripcionSucursal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saldoInicial");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "SaldoInicial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoDivisa");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "TipoDivisa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saldoDisponible");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "SaldoDisponible"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saldoRetenido");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "SaldoRetenido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saldoDia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "SaldoDia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regimenFirmas");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "RegimenFirmas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombrePromotor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "NombrePromotor"));
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

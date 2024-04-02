/**
 * ConsultarClabesSaldosRespuestaConsultaSaldos.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.consultaclabessaldos;

public class ConsultarClabesSaldosRespuestaConsultaSaldos  implements java.io.Serializable {
    private java.util.Date fechaProceso;

    /* NUMERO DE CUENTA */
    private java.lang.String tran_NumeroCuenta;

    /* CLABE CUENTA */
    private java.lang.String clabeCuenta;

    /* CR ORIGEN */
    private java.lang.String crOrigen;

    /* DESCRIPCION SUCURSAL */
    private java.lang.String descripcionSucursal;

    /* SALDO INICIAL */
    private java.math.BigDecimal saldoInicial;

    /* TIPO DE DIVISA */
    private java.lang.String tran_TipoDivisa;

    /* SALDO DISPONIBLE */
    private java.math.BigDecimal saldoDisponible;

    /* SALDO RETENIDO */
    private java.math.BigDecimal saldoRetenido;

    /* SALDO DEL DIA */
    private java.math.BigDecimal saldoDia;

    /* REGIMEN DE FIRMAS */
    private java.lang.String regimenFirmas;

    /* NOMBRE PROMOTOR */
    private java.lang.String nombrePromotor;

    public ConsultarClabesSaldosRespuestaConsultaSaldos() {
    }

    public ConsultarClabesSaldosRespuestaConsultaSaldos(
           java.util.Date fechaProceso,
           java.lang.String tran_NumeroCuenta,
           java.lang.String clabeCuenta,
           java.lang.String crOrigen,
           java.lang.String descripcionSucursal,
           java.math.BigDecimal saldoInicial,
           java.lang.String tran_TipoDivisa,
           java.math.BigDecimal saldoDisponible,
           java.math.BigDecimal saldoRetenido,
           java.math.BigDecimal saldoDia,
           java.lang.String regimenFirmas,
           java.lang.String nombrePromotor) {
           this.fechaProceso = fechaProceso;
           this.tran_NumeroCuenta = tran_NumeroCuenta;
           this.clabeCuenta = clabeCuenta;
           this.crOrigen = crOrigen;
           this.descripcionSucursal = descripcionSucursal;
           this.saldoInicial = saldoInicial;
           this.tran_TipoDivisa = tran_TipoDivisa;
           this.saldoDisponible = saldoDisponible;
           this.saldoRetenido = saldoRetenido;
           this.saldoDia = saldoDia;
           this.regimenFirmas = regimenFirmas;
           this.nombrePromotor = nombrePromotor;
    }


    /**
     * Gets the fechaProceso value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @return fechaProceso
     */
    public java.util.Date getFechaProceso() {
        return fechaProceso;
    }


    /**
     * Sets the fechaProceso value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @param fechaProceso
     */
    public void setFechaProceso(java.util.Date fechaProceso) {
        this.fechaProceso = fechaProceso;
    }


    /**
     * Gets the tran_NumeroCuenta value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @return tran_NumeroCuenta   * NUMERO DE CUENTA
     */
    public java.lang.String getTran_NumeroCuenta() {
        return tran_NumeroCuenta;
    }


    /**
     * Sets the tran_NumeroCuenta value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @param tran_NumeroCuenta   * NUMERO DE CUENTA
     */
    public void setTran_NumeroCuenta(java.lang.String tran_NumeroCuenta) {
        this.tran_NumeroCuenta = tran_NumeroCuenta;
    }


    /**
     * Gets the clabeCuenta value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @return clabeCuenta   * CLABE CUENTA
     */
    public java.lang.String getClabeCuenta() {
        return clabeCuenta;
    }


    /**
     * Sets the clabeCuenta value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @param clabeCuenta   * CLABE CUENTA
     */
    public void setClabeCuenta(java.lang.String clabeCuenta) {
        this.clabeCuenta = clabeCuenta;
    }


    /**
     * Gets the crOrigen value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @return crOrigen   * CR ORIGEN
     */
    public java.lang.String getCrOrigen() {
        return crOrigen;
    }


    /**
     * Sets the crOrigen value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @param crOrigen   * CR ORIGEN
     */
    public void setCrOrigen(java.lang.String crOrigen) {
        this.crOrigen = crOrigen;
    }


    /**
     * Gets the descripcionSucursal value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @return descripcionSucursal   * DESCRIPCION SUCURSAL
     */
    public java.lang.String getDescripcionSucursal() {
        return descripcionSucursal;
    }


    /**
     * Sets the descripcionSucursal value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @param descripcionSucursal   * DESCRIPCION SUCURSAL
     */
    public void setDescripcionSucursal(java.lang.String descripcionSucursal) {
        this.descripcionSucursal = descripcionSucursal;
    }


    /**
     * Gets the saldoInicial value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @return saldoInicial   * SALDO INICIAL
     */
    public java.math.BigDecimal getSaldoInicial() {
        return saldoInicial;
    }


    /**
     * Sets the saldoInicial value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @param saldoInicial   * SALDO INICIAL
     */
    public void setSaldoInicial(java.math.BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }


    /**
     * Gets the tran_TipoDivisa value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @return tran_TipoDivisa   * TIPO DE DIVISA
     */
    public java.lang.String getTran_TipoDivisa() {
        return tran_TipoDivisa;
    }


    /**
     * Sets the tran_TipoDivisa value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @param tran_TipoDivisa   * TIPO DE DIVISA
     */
    public void setTran_TipoDivisa(java.lang.String tran_TipoDivisa) {
        this.tran_TipoDivisa = tran_TipoDivisa;
    }


    /**
     * Gets the saldoDisponible value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @return saldoDisponible   * SALDO DISPONIBLE
     */
    public java.math.BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }


    /**
     * Sets the saldoDisponible value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @param saldoDisponible   * SALDO DISPONIBLE
     */
    public void setSaldoDisponible(java.math.BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }


    /**
     * Gets the saldoRetenido value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @return saldoRetenido   * SALDO RETENIDO
     */
    public java.math.BigDecimal getSaldoRetenido() {
        return saldoRetenido;
    }


    /**
     * Sets the saldoRetenido value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @param saldoRetenido   * SALDO RETENIDO
     */
    public void setSaldoRetenido(java.math.BigDecimal saldoRetenido) {
        this.saldoRetenido = saldoRetenido;
    }


    /**
     * Gets the saldoDia value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @return saldoDia   * SALDO DEL DIA
     */
    public java.math.BigDecimal getSaldoDia() {
        return saldoDia;
    }


    /**
     * Sets the saldoDia value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @param saldoDia   * SALDO DEL DIA
     */
    public void setSaldoDia(java.math.BigDecimal saldoDia) {
        this.saldoDia = saldoDia;
    }


    /**
     * Gets the regimenFirmas value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @return regimenFirmas   * REGIMEN DE FIRMAS
     */
    public java.lang.String getRegimenFirmas() {
        return regimenFirmas;
    }


    /**
     * Sets the regimenFirmas value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @param regimenFirmas   * REGIMEN DE FIRMAS
     */
    public void setRegimenFirmas(java.lang.String regimenFirmas) {
        this.regimenFirmas = regimenFirmas;
    }


    /**
     * Gets the nombrePromotor value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @return nombrePromotor   * NOMBRE PROMOTOR
     */
    public java.lang.String getNombrePromotor() {
        return nombrePromotor;
    }


    /**
     * Sets the nombrePromotor value for this ConsultarClabesSaldosRespuestaConsultaSaldos.
     * 
     * @param nombrePromotor   * NOMBRE PROMOTOR
     */
    public void setNombrePromotor(java.lang.String nombrePromotor) {
        this.nombrePromotor = nombrePromotor;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultarClabesSaldosRespuestaConsultaSaldos)) return false;
        ConsultarClabesSaldosRespuestaConsultaSaldos other = (ConsultarClabesSaldosRespuestaConsultaSaldos) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fechaProceso==null && other.getFechaProceso()==null) || 
             (this.fechaProceso!=null &&
              this.fechaProceso.equals(other.getFechaProceso()))) &&
            ((this.tran_NumeroCuenta==null && other.getTran_NumeroCuenta()==null) || 
             (this.tran_NumeroCuenta!=null &&
              this.tran_NumeroCuenta.equals(other.getTran_NumeroCuenta()))) &&
            ((this.clabeCuenta==null && other.getClabeCuenta()==null) || 
             (this.clabeCuenta!=null &&
              this.clabeCuenta.equals(other.getClabeCuenta()))) &&
            ((this.crOrigen==null && other.getCrOrigen()==null) || 
             (this.crOrigen!=null &&
              this.crOrigen.equals(other.getCrOrigen()))) &&
            ((this.descripcionSucursal==null && other.getDescripcionSucursal()==null) || 
             (this.descripcionSucursal!=null &&
              this.descripcionSucursal.equals(other.getDescripcionSucursal()))) &&
            ((this.saldoInicial==null && other.getSaldoInicial()==null) || 
             (this.saldoInicial!=null &&
              this.saldoInicial.equals(other.getSaldoInicial()))) &&
            ((this.tran_TipoDivisa==null && other.getTran_TipoDivisa()==null) || 
             (this.tran_TipoDivisa!=null &&
              this.tran_TipoDivisa.equals(other.getTran_TipoDivisa()))) &&
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
        if (getFechaProceso() != null) {
            _hashCode += getFechaProceso().hashCode();
        }
        if (getTran_NumeroCuenta() != null) {
            _hashCode += getTran_NumeroCuenta().hashCode();
        }
        if (getClabeCuenta() != null) {
            _hashCode += getClabeCuenta().hashCode();
        }
        if (getCrOrigen() != null) {
            _hashCode += getCrOrigen().hashCode();
        }
        if (getDescripcionSucursal() != null) {
            _hashCode += getDescripcionSucursal().hashCode();
        }
        if (getSaldoInicial() != null) {
            _hashCode += getSaldoInicial().hashCode();
        }
        if (getTran_TipoDivisa() != null) {
            _hashCode += getTran_TipoDivisa().hashCode();
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
        new org.apache.axis.description.TypeDesc(ConsultarClabesSaldosRespuestaConsultaSaldos.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaClabesSaldos", ">consultarClabesSaldosRespuesta>ConsultaSaldos"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaProceso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FechaProceso"));
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
        elemField.setFieldName("clabeCuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ClabeCuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("crOrigen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CrOrigen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionSucursal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DescripcionSucursal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saldoInicial");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SaldoInicial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tran_TipoDivisa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Tran_TipoDivisa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saldoDisponible");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SaldoDisponible"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saldoRetenido");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SaldoRetenido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saldoDia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SaldoDia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regimenFirmas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RegimenFirmas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombrePromotor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NombrePromotor"));
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

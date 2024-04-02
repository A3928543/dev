/**
 * ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.consultaclabessaldos;

public class ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl  implements java.io.Serializable {
    /* PERSONALIDAD LEGAL */
    private java.lang.String personalidadLegal;

    /* FECHA DE LA CANCELACION */
    private java.util.Date tran_FechaCancelacion;

    /* RAZON DE LA CANCELACION */
    private java.lang.String razonCancelacion;

    /* DESCRIPCION RAZON DE LA CANCELACION */
    private java.lang.String descripcionRazonCancelacion;

    /* BALANCE DE COMPENSACION */
    private java.math.BigDecimal balanceCompensacion;

    public ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl() {
    }

    public ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl(
           java.lang.String personalidadLegal,
           java.util.Date tran_FechaCancelacion,
           java.lang.String razonCancelacion,
           java.lang.String descripcionRazonCancelacion,
           java.math.BigDecimal balanceCompensacion) {
           this.personalidadLegal = personalidadLegal;
           this.tran_FechaCancelacion = tran_FechaCancelacion;
           this.razonCancelacion = razonCancelacion;
           this.descripcionRazonCancelacion = descripcionRazonCancelacion;
           this.balanceCompensacion = balanceCompensacion;
    }


    /**
     * Gets the personalidadLegal value for this ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl.
     * 
     * @return personalidadLegal   * PERSONALIDAD LEGAL
     */
    public java.lang.String getPersonalidadLegal() {
        return personalidadLegal;
    }


    /**
     * Sets the personalidadLegal value for this ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl.
     * 
     * @param personalidadLegal   * PERSONALIDAD LEGAL
     */
    public void setPersonalidadLegal(java.lang.String personalidadLegal) {
        this.personalidadLegal = personalidadLegal;
    }


    /**
     * Gets the tran_FechaCancelacion value for this ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl.
     * 
     * @return tran_FechaCancelacion   * FECHA DE LA CANCELACION
     */
    public java.util.Date getTran_FechaCancelacion() {
        return tran_FechaCancelacion;
    }


    /**
     * Sets the tran_FechaCancelacion value for this ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl.
     * 
     * @param tran_FechaCancelacion   * FECHA DE LA CANCELACION
     */
    public void setTran_FechaCancelacion(java.util.Date tran_FechaCancelacion) {
        this.tran_FechaCancelacion = tran_FechaCancelacion;
    }


    /**
     * Gets the razonCancelacion value for this ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl.
     * 
     * @return razonCancelacion   * RAZON DE LA CANCELACION
     */
    public java.lang.String getRazonCancelacion() {
        return razonCancelacion;
    }


    /**
     * Sets the razonCancelacion value for this ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl.
     * 
     * @param razonCancelacion   * RAZON DE LA CANCELACION
     */
    public void setRazonCancelacion(java.lang.String razonCancelacion) {
        this.razonCancelacion = razonCancelacion;
    }


    /**
     * Gets the descripcionRazonCancelacion value for this ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl.
     * 
     * @return descripcionRazonCancelacion   * DESCRIPCION RAZON DE LA CANCELACION
     */
    public java.lang.String getDescripcionRazonCancelacion() {
        return descripcionRazonCancelacion;
    }


    /**
     * Sets the descripcionRazonCancelacion value for this ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl.
     * 
     * @param descripcionRazonCancelacion   * DESCRIPCION RAZON DE LA CANCELACION
     */
    public void setDescripcionRazonCancelacion(java.lang.String descripcionRazonCancelacion) {
        this.descripcionRazonCancelacion = descripcionRazonCancelacion;
    }


    /**
     * Gets the balanceCompensacion value for this ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl.
     * 
     * @return balanceCompensacion   * BALANCE DE COMPENSACION
     */
    public java.math.BigDecimal getBalanceCompensacion() {
        return balanceCompensacion;
    }


    /**
     * Sets the balanceCompensacion value for this ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl.
     * 
     * @param balanceCompensacion   * BALANCE DE COMPENSACION
     */
    public void setBalanceCompensacion(java.math.BigDecimal balanceCompensacion) {
        this.balanceCompensacion = balanceCompensacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl)) return false;
        ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl other = (ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.personalidadLegal==null && other.getPersonalidadLegal()==null) || 
             (this.personalidadLegal!=null &&
              this.personalidadLegal.equals(other.getPersonalidadLegal()))) &&
            ((this.tran_FechaCancelacion==null && other.getTran_FechaCancelacion()==null) || 
             (this.tran_FechaCancelacion!=null &&
              this.tran_FechaCancelacion.equals(other.getTran_FechaCancelacion()))) &&
            ((this.razonCancelacion==null && other.getRazonCancelacion()==null) || 
             (this.razonCancelacion!=null &&
              this.razonCancelacion.equals(other.getRazonCancelacion()))) &&
            ((this.descripcionRazonCancelacion==null && other.getDescripcionRazonCancelacion()==null) || 
             (this.descripcionRazonCancelacion!=null &&
              this.descripcionRazonCancelacion.equals(other.getDescripcionRazonCancelacion()))) &&
            ((this.balanceCompensacion==null && other.getBalanceCompensacion()==null) || 
             (this.balanceCompensacion!=null &&
              this.balanceCompensacion.equals(other.getBalanceCompensacion())));
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
        if (getPersonalidadLegal() != null) {
            _hashCode += getPersonalidadLegal().hashCode();
        }
        if (getTran_FechaCancelacion() != null) {
            _hashCode += getTran_FechaCancelacion().hashCode();
        }
        if (getRazonCancelacion() != null) {
            _hashCode += getRazonCancelacion().hashCode();
        }
        if (getDescripcionRazonCancelacion() != null) {
            _hashCode += getDescripcionRazonCancelacion().hashCode();
        }
        if (getBalanceCompensacion() != null) {
            _hashCode += getBalanceCompensacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultarClabesSaldosRespuestaDatosComplementariosCentroControl.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaClabesSaldos", ">consultarClabesSaldosRespuesta>DatosComplementariosCentroControl"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personalidadLegal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PersonalidadLegal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tran_FechaCancelacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Tran_FechaCancelacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("razonCancelacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RazonCancelacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionRazonCancelacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DescripcionRazonCancelacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balanceCompensacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BalanceCompensacion"));
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

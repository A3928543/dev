/**
 * PemeBn0.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.ConsultaIdIxeAltamiraResponse;

public class PemeBn0  implements java.io.Serializable {
    private java.lang.String numClIx;

    private java.lang.String numClBn;

    private java.lang.String nombre;

    private java.lang.String priApe;

    private java.lang.String segApe;

    private java.lang.String razonS1;

    private java.lang.String razonS2;

    public PemeBn0() {
    }

    public PemeBn0(
           java.lang.String numClIx,
           java.lang.String numClBn,
           java.lang.String nombre,
           java.lang.String priApe,
           java.lang.String segApe,
           java.lang.String razonS1,
           java.lang.String razonS2) {
           this.numClIx = numClIx;
           this.numClBn = numClBn;
           this.nombre = nombre;
           this.priApe = priApe;
           this.segApe = segApe;
           this.razonS1 = razonS1;
           this.razonS2 = razonS2;
    }


    /**
     * Gets the numClIx value for this PemeBn0.
     * 
     * @return numClIx
     */
    public java.lang.String getNumClIx() {
        return numClIx;
    }


    /**
     * Sets the numClIx value for this PemeBn0.
     * 
     * @param numClIx
     */
    public void setNumClIx(java.lang.String numClIx) {
        this.numClIx = numClIx;
    }


    /**
     * Gets the numClBn value for this PemeBn0.
     * 
     * @return numClBn
     */
    public java.lang.String getNumClBn() {
        return numClBn;
    }


    /**
     * Sets the numClBn value for this PemeBn0.
     * 
     * @param numClBn
     */
    public void setNumClBn(java.lang.String numClBn) {
        this.numClBn = numClBn;
    }


    /**
     * Gets the nombre value for this PemeBn0.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this PemeBn0.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the priApe value for this PemeBn0.
     * 
     * @return priApe
     */
    public java.lang.String getPriApe() {
        return priApe;
    }


    /**
     * Sets the priApe value for this PemeBn0.
     * 
     * @param priApe
     */
    public void setPriApe(java.lang.String priApe) {
        this.priApe = priApe;
    }


    /**
     * Gets the segApe value for this PemeBn0.
     * 
     * @return segApe
     */
    public java.lang.String getSegApe() {
        return segApe;
    }


    /**
     * Sets the segApe value for this PemeBn0.
     * 
     * @param segApe
     */
    public void setSegApe(java.lang.String segApe) {
        this.segApe = segApe;
    }


    /**
     * Gets the razonS1 value for this PemeBn0.
     * 
     * @return razonS1
     */
    public java.lang.String getRazonS1() {
        return razonS1;
    }


    /**
     * Sets the razonS1 value for this PemeBn0.
     * 
     * @param razonS1
     */
    public void setRazonS1(java.lang.String razonS1) {
        this.razonS1 = razonS1;
    }


    /**
     * Gets the razonS2 value for this PemeBn0.
     * 
     * @return razonS2
     */
    public java.lang.String getRazonS2() {
        return razonS2;
    }


    /**
     * Sets the razonS2 value for this PemeBn0.
     * 
     * @param razonS2
     */
    public void setRazonS2(java.lang.String razonS2) {
        this.razonS2 = razonS2;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PemeBn0)) return false;
        PemeBn0 other = (PemeBn0) obj;
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
              this.numClBn.equals(other.getNumClBn()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.priApe==null && other.getPriApe()==null) || 
             (this.priApe!=null &&
              this.priApe.equals(other.getPriApe()))) &&
            ((this.segApe==null && other.getSegApe()==null) || 
             (this.segApe!=null &&
              this.segApe.equals(other.getSegApe()))) &&
            ((this.razonS1==null && other.getRazonS1()==null) || 
             (this.razonS1!=null &&
              this.razonS1.equals(other.getRazonS1()))) &&
            ((this.razonS2==null && other.getRazonS2()==null) || 
             (this.razonS2!=null &&
              this.razonS2.equals(other.getRazonS2())));
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
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getPriApe() != null) {
            _hashCode += getPriApe().hashCode();
        }
        if (getSegApe() != null) {
            _hashCode += getSegApe().hashCode();
        }
        if (getRazonS1() != null) {
            _hashCode += getRazonS1().hashCode();
        }
        if (getRazonS2() != null) {
            _hashCode += getRazonS2().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PemeBn0.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", "PemeBn0"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numClIx");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", "NumClIx"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numClBn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", "NumClBn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", "Nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("priApe");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", "PriApe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("segApe");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", "SegApe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("razonS1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", "RazonS1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("razonS2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ConsultaIdIxeAltamiraResponse", "RazonS2"));
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

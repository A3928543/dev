/**
 * DespliegueType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.mantenimientodatosbasicos.headers;

public class DespliegueType  implements java.io.Serializable {
    /* Dispositivo desde el
     * 						cual se ejecuta la peticion */
    private java.lang.String dispositivo;

    /* Marca dispositivo
     * 						desde el cual se ejecuta la
     * 						peticion */
    private java.lang.String marcaDispositivo;

    /* -Nombre del Browser
     * 						utilizado para ejecutar la peticion
     * 						---ATRIBUTOS--
     * 						-version: Version
     * 						del Navegador utilizado */
    private com.banorte.ws.esb.mantenimientodatosbasicos.headers.NavegadorType navegador;

    public DespliegueType() {
    }

    public DespliegueType(
           java.lang.String dispositivo,
           java.lang.String marcaDispositivo,
           com.banorte.ws.esb.mantenimientodatosbasicos.headers.NavegadorType navegador) {
           this.dispositivo = dispositivo;
           this.marcaDispositivo = marcaDispositivo;
           this.navegador = navegador;
    }


    /**
     * Gets the dispositivo value for this DespliegueType.
     * 
     * @return dispositivo   * Dispositivo desde el
     * 						cual se ejecuta la peticion
     */
    public java.lang.String getDispositivo() {
        return dispositivo;
    }


    /**
     * Sets the dispositivo value for this DespliegueType.
     * 
     * @param dispositivo   * Dispositivo desde el
     * 						cual se ejecuta la peticion
     */
    public void setDispositivo(java.lang.String dispositivo) {
        this.dispositivo = dispositivo;
    }


    /**
     * Gets the marcaDispositivo value for this DespliegueType.
     * 
     * @return marcaDispositivo   * Marca dispositivo
     * 						desde el cual se ejecuta la
     * 						peticion
     */
    public java.lang.String getMarcaDispositivo() {
        return marcaDispositivo;
    }


    /**
     * Sets the marcaDispositivo value for this DespliegueType.
     * 
     * @param marcaDispositivo   * Marca dispositivo
     * 						desde el cual se ejecuta la
     * 						peticion
     */
    public void setMarcaDispositivo(java.lang.String marcaDispositivo) {
        this.marcaDispositivo = marcaDispositivo;
    }


    /**
     * Gets the navegador value for this DespliegueType.
     * 
     * @return navegador   * -Nombre del Browser
     * 						utilizado para ejecutar la peticion
     * 						---ATRIBUTOS--
     * 						-version: Version
     * 						del Navegador utilizado
     */
    public com.banorte.ws.esb.mantenimientodatosbasicos.headers.NavegadorType getNavegador() {
        return navegador;
    }


    /**
     * Sets the navegador value for this DespliegueType.
     * 
     * @param navegador   * -Nombre del Browser
     * 						utilizado para ejecutar la peticion
     * 						---ATRIBUTOS--
     * 						-version: Version
     * 						del Navegador utilizado
     */
    public void setNavegador(com.banorte.ws.esb.mantenimientodatosbasicos.headers.NavegadorType navegador) {
        this.navegador = navegador;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DespliegueType)) return false;
        DespliegueType other = (DespliegueType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dispositivo==null && other.getDispositivo()==null) || 
             (this.dispositivo!=null &&
              this.dispositivo.equals(other.getDispositivo()))) &&
            ((this.marcaDispositivo==null && other.getMarcaDispositivo()==null) || 
             (this.marcaDispositivo!=null &&
              this.marcaDispositivo.equals(other.getMarcaDispositivo()))) &&
            ((this.navegador==null && other.getNavegador()==null) || 
             (this.navegador!=null &&
              this.navegador.equals(other.getNavegador())));
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
        if (getDispositivo() != null) {
            _hashCode += getDispositivo().hashCode();
        }
        if (getMarcaDispositivo() != null) {
            _hashCode += getMarcaDispositivo().hashCode();
        }
        if (getNavegador() != null) {
            _hashCode += getNavegador().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DespliegueType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", "DespliegueType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dispositivo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Dispositivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("marcaDispositivo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MarcaDispositivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("navegador");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Navegador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", "NavegadorType"));
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

/**
 * ExcepcionGenericaType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.mantenimientodatosbasicos.error;

public class ExcepcionGenericaType  extends org.apache.axis.AxisFault  implements java.io.Serializable {
    /* Campo que contiene los codigos a ser enviados
     * 						desde los diferentes
     * 						proveedores */
    private java.lang.String codigo;

    /* Componente que Genero el Error */
    private java.lang.String componente;

    /* Metodo que Genero el Error */
    private java.lang.String metodo;

    /* Descripcion detallada del Error */
    private java.lang.String descripcion;

    public ExcepcionGenericaType() {
    }

    public ExcepcionGenericaType(
           java.lang.String codigo,
           java.lang.String componente,
           java.lang.String metodo,
           java.lang.String descripcion) {
        this.codigo = codigo;
        this.componente = componente;
        this.metodo = metodo;
        this.descripcion = descripcion;
    }


    /**
     * Gets the codigo value for this ExcepcionGenericaType.
     * 
     * @return codigo   * Campo que contiene los codigos a ser enviados
     * 						desde los diferentes
     * 						proveedores
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this ExcepcionGenericaType.
     * 
     * @param codigo   * Campo que contiene los codigos a ser enviados
     * 						desde los diferentes
     * 						proveedores
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the componente value for this ExcepcionGenericaType.
     * 
     * @return componente   * Componente que Genero el Error
     */
    public java.lang.String getComponente() {
        return componente;
    }


    /**
     * Sets the componente value for this ExcepcionGenericaType.
     * 
     * @param componente   * Componente que Genero el Error
     */
    public void setComponente(java.lang.String componente) {
        this.componente = componente;
    }


    /**
     * Gets the metodo value for this ExcepcionGenericaType.
     * 
     * @return metodo   * Metodo que Genero el Error
     */
    public java.lang.String getMetodo() {
        return metodo;
    }


    /**
     * Sets the metodo value for this ExcepcionGenericaType.
     * 
     * @param metodo   * Metodo que Genero el Error
     */
    public void setMetodo(java.lang.String metodo) {
        this.metodo = metodo;
    }


    /**
     * Gets the descripcion value for this ExcepcionGenericaType.
     * 
     * @return descripcion   * Descripcion detallada del Error
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this ExcepcionGenericaType.
     * 
     * @param descripcion   * Descripcion detallada del Error
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExcepcionGenericaType)) return false;
        ExcepcionGenericaType other = (ExcepcionGenericaType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigo==null && other.getCodigo()==null) || 
             (this.codigo!=null &&
              this.codigo.equals(other.getCodigo()))) &&
            ((this.componente==null && other.getComponente()==null) || 
             (this.componente!=null &&
              this.componente.equals(other.getComponente()))) &&
            ((this.metodo==null && other.getMetodo()==null) || 
             (this.metodo!=null &&
              this.metodo.equals(other.getMetodo()))) &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              this.descripcion.equals(other.getDescripcion())));
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
        if (getCodigo() != null) {
            _hashCode += getCodigo().hashCode();
        }
        if (getComponente() != null) {
            _hashCode += getComponente().hashCode();
        }
        if (getMetodo() != null) {
            _hashCode += getMetodo().hashCode();
        }
        if (getDescripcion() != null) {
            _hashCode += getDescripcion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExcepcionGenericaType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/common/ExcepcionGeneral", "ExcepcionGenericaType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("componente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Componente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metodo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Metodo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Descripcion"));
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


    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, this);
    }
}

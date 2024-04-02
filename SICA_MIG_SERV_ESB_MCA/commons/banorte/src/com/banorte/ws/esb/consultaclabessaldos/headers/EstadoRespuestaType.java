/**
 * EstadoRespuestaType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.consultaclabessaldos.headers;

public class EstadoRespuestaType  implements java.io.Serializable {
    /* Identificador del mensaje de Error */
    private java.lang.String id;

    /* Mensaje de negocio para el usuario(Catalogo) */
    private java.lang.String mensajeAUsuario;

    /* StackTrace, lista completa del error de sistema. */
    private java.lang.String mensajeDetallado;

    /* Nivel de seguridad de requerido por la operacion
     * 						en el caso de que no existieran permisos
     * 						suficientes para su ejecucion (Modulo de
     * 						Seguridad). */
    private java.lang.Integer nivelSegRequerido;

    public EstadoRespuestaType() {
    }

    public EstadoRespuestaType(
           java.lang.String id,
           java.lang.String mensajeAUsuario,
           java.lang.String mensajeDetallado,
           java.lang.Integer nivelSegRequerido) {
           this.id = id;
           this.mensajeAUsuario = mensajeAUsuario;
           this.mensajeDetallado = mensajeDetallado;
           this.nivelSegRequerido = nivelSegRequerido;
    }


    /**
     * Gets the id value for this EstadoRespuestaType.
     * 
     * @return id   * Identificador del mensaje de Error
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this EstadoRespuestaType.
     * 
     * @param id   * Identificador del mensaje de Error
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the mensajeAUsuario value for this EstadoRespuestaType.
     * 
     * @return mensajeAUsuario   * Mensaje de negocio para el usuario(Catalogo)
     */
    public java.lang.String getMensajeAUsuario() {
        return mensajeAUsuario;
    }


    /**
     * Sets the mensajeAUsuario value for this EstadoRespuestaType.
     * 
     * @param mensajeAUsuario   * Mensaje de negocio para el usuario(Catalogo)
     */
    public void setMensajeAUsuario(java.lang.String mensajeAUsuario) {
        this.mensajeAUsuario = mensajeAUsuario;
    }


    /**
     * Gets the mensajeDetallado value for this EstadoRespuestaType.
     * 
     * @return mensajeDetallado   * StackTrace, lista completa del error de sistema.
     */
    public java.lang.String getMensajeDetallado() {
        return mensajeDetallado;
    }


    /**
     * Sets the mensajeDetallado value for this EstadoRespuestaType.
     * 
     * @param mensajeDetallado   * StackTrace, lista completa del error de sistema.
     */
    public void setMensajeDetallado(java.lang.String mensajeDetallado) {
        this.mensajeDetallado = mensajeDetallado;
    }


    /**
     * Gets the nivelSegRequerido value for this EstadoRespuestaType.
     * 
     * @return nivelSegRequerido   * Nivel de seguridad de requerido por la operacion
     * 						en el caso de que no existieran permisos
     * 						suficientes para su ejecucion (Modulo de
     * 						Seguridad).
     */
    public java.lang.Integer getNivelSegRequerido() {
        return nivelSegRequerido;
    }


    /**
     * Sets the nivelSegRequerido value for this EstadoRespuestaType.
     * 
     * @param nivelSegRequerido   * Nivel de seguridad de requerido por la operacion
     * 						en el caso de que no existieran permisos
     * 						suficientes para su ejecucion (Modulo de
     * 						Seguridad).
     */
    public void setNivelSegRequerido(java.lang.Integer nivelSegRequerido) {
        this.nivelSegRequerido = nivelSegRequerido;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EstadoRespuestaType)) return false;
        EstadoRespuestaType other = (EstadoRespuestaType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.mensajeAUsuario==null && other.getMensajeAUsuario()==null) || 
             (this.mensajeAUsuario!=null &&
              this.mensajeAUsuario.equals(other.getMensajeAUsuario()))) &&
            ((this.mensajeDetallado==null && other.getMensajeDetallado()==null) || 
             (this.mensajeDetallado!=null &&
              this.mensajeDetallado.equals(other.getMensajeDetallado()))) &&
            ((this.nivelSegRequerido==null && other.getNivelSegRequerido()==null) || 
             (this.nivelSegRequerido!=null &&
              this.nivelSegRequerido.equals(other.getNivelSegRequerido())));
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getMensajeAUsuario() != null) {
            _hashCode += getMensajeAUsuario().hashCode();
        }
        if (getMensajeDetallado() != null) {
            _hashCode += getMensajeDetallado().hashCode();
        }
        if (getNivelSegRequerido() != null) {
            _hashCode += getNivelSegRequerido().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EstadoRespuestaType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", "EstadoRespuestaType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mensajeAUsuario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MensajeAUsuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mensajeDetallado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MensajeDetallado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nivelSegRequerido");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NivelSegRequerido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

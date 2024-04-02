/**
 * AccesoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.mantenimientodatosbasicos.headers;

public class AccesoType  implements java.io.Serializable {
    /* Identificador de Usuario */
    private java.lang.String idUsuario;

    /* Clave de acceso a usuario para envio de solicitudes incluidas
     * hacia servicios expuestos en Altamira. */
    private java.lang.String claveAcceso;

    /* Usuario generico o especifico que realiza la
     * 						peticion(Usuario de Back-End) */
    private java.lang.String idUsuarioHost;

    /* Identificador de sesion para correlar sesiones */
    private java.lang.String idSesion;

    /* Identificador de operacion para correlar operaciones,  usuado
     * por BBT
     * para hacer la relacion del mensaje de entrada con el de salida */
    private java.lang.String idOperacion;

    /* Token de la operacion utilizado por interfaz
     * 						Valida Transaccion */
    private java.lang.String tokenOperacion;

    /* Sic utilizado por interfaz Valida Transaccion. */
    private java.lang.String sic;

    /* Campo de seguridad para validar en la tabla de
     * 						operaciones, informacion adicional requerida por
     * 						el motor de reglas para validar la transaccion
     * 						es abierto y valida entre servicios. */
    private java.lang.String customSeg;

    public AccesoType() {
    }

    public AccesoType(
           java.lang.String idUsuario,
           java.lang.String claveAcceso,
           java.lang.String idUsuarioHost,
           java.lang.String idSesion,
           java.lang.String idOperacion,
           java.lang.String tokenOperacion,
           java.lang.String sic,
           java.lang.String customSeg) {
           this.idUsuario = idUsuario;
           this.claveAcceso = claveAcceso;
           this.idUsuarioHost = idUsuarioHost;
           this.idSesion = idSesion;
           this.idOperacion = idOperacion;
           this.tokenOperacion = tokenOperacion;
           this.sic = sic;
           this.customSeg = customSeg;
    }


    /**
     * Gets the idUsuario value for this AccesoType.
     * 
     * @return idUsuario   * Identificador de Usuario
     */
    public java.lang.String getIdUsuario() {
        return idUsuario;
    }


    /**
     * Sets the idUsuario value for this AccesoType.
     * 
     * @param idUsuario   * Identificador de Usuario
     */
    public void setIdUsuario(java.lang.String idUsuario) {
        this.idUsuario = idUsuario;
    }


    /**
     * Gets the claveAcceso value for this AccesoType.
     * 
     * @return claveAcceso   * Clave de acceso a usuario para envio de solicitudes incluidas
     * hacia servicios expuestos en Altamira.
     */
    public java.lang.String getClaveAcceso() {
        return claveAcceso;
    }


    /**
     * Sets the claveAcceso value for this AccesoType.
     * 
     * @param claveAcceso   * Clave de acceso a usuario para envio de solicitudes incluidas
     * hacia servicios expuestos en Altamira.
     */
    public void setClaveAcceso(java.lang.String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }


    /**
     * Gets the idUsuarioHost value for this AccesoType.
     * 
     * @return idUsuarioHost   * Usuario generico o especifico que realiza la
     * 						peticion(Usuario de Back-End)
     */
    public java.lang.String getIdUsuarioHost() {
        return idUsuarioHost;
    }


    /**
     * Sets the idUsuarioHost value for this AccesoType.
     * 
     * @param idUsuarioHost   * Usuario generico o especifico que realiza la
     * 						peticion(Usuario de Back-End)
     */
    public void setIdUsuarioHost(java.lang.String idUsuarioHost) {
        this.idUsuarioHost = idUsuarioHost;
    }


    /**
     * Gets the idSesion value for this AccesoType.
     * 
     * @return idSesion   * Identificador de sesion para correlar sesiones
     */
    public java.lang.String getIdSesion() {
        return idSesion;
    }


    /**
     * Sets the idSesion value for this AccesoType.
     * 
     * @param idSesion   * Identificador de sesion para correlar sesiones
     */
    public void setIdSesion(java.lang.String idSesion) {
        this.idSesion = idSesion;
    }


    /**
     * Gets the idOperacion value for this AccesoType.
     * 
     * @return idOperacion   * Identificador de operacion para correlar operaciones,  usuado
     * por BBT
     * para hacer la relacion del mensaje de entrada con el de salida
     */
    public java.lang.String getIdOperacion() {
        return idOperacion;
    }


    /**
     * Sets the idOperacion value for this AccesoType.
     * 
     * @param idOperacion   * Identificador de operacion para correlar operaciones,  usuado
     * por BBT
     * para hacer la relacion del mensaje de entrada con el de salida
     */
    public void setIdOperacion(java.lang.String idOperacion) {
        this.idOperacion = idOperacion;
    }


    /**
     * Gets the tokenOperacion value for this AccesoType.
     * 
     * @return tokenOperacion   * Token de la operacion utilizado por interfaz
     * 						Valida Transaccion
     */
    public java.lang.String getTokenOperacion() {
        return tokenOperacion;
    }


    /**
     * Sets the tokenOperacion value for this AccesoType.
     * 
     * @param tokenOperacion   * Token de la operacion utilizado por interfaz
     * 						Valida Transaccion
     */
    public void setTokenOperacion(java.lang.String tokenOperacion) {
        this.tokenOperacion = tokenOperacion;
    }


    /**
     * Gets the sic value for this AccesoType.
     * 
     * @return sic   * Sic utilizado por interfaz Valida Transaccion.
     */
    public java.lang.String getSic() {
        return sic;
    }


    /**
     * Sets the sic value for this AccesoType.
     * 
     * @param sic   * Sic utilizado por interfaz Valida Transaccion.
     */
    public void setSic(java.lang.String sic) {
        this.sic = sic;
    }


    /**
     * Gets the customSeg value for this AccesoType.
     * 
     * @return customSeg   * Campo de seguridad para validar en la tabla de
     * 						operaciones, informacion adicional requerida por
     * 						el motor de reglas para validar la transaccion
     * 						es abierto y valida entre servicios.
     */
    public java.lang.String getCustomSeg() {
        return customSeg;
    }


    /**
     * Sets the customSeg value for this AccesoType.
     * 
     * @param customSeg   * Campo de seguridad para validar en la tabla de
     * 						operaciones, informacion adicional requerida por
     * 						el motor de reglas para validar la transaccion
     * 						es abierto y valida entre servicios.
     */
    public void setCustomSeg(java.lang.String customSeg) {
        this.customSeg = customSeg;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AccesoType)) return false;
        AccesoType other = (AccesoType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idUsuario==null && other.getIdUsuario()==null) || 
             (this.idUsuario!=null &&
              this.idUsuario.equals(other.getIdUsuario()))) &&
            ((this.claveAcceso==null && other.getClaveAcceso()==null) || 
             (this.claveAcceso!=null &&
              this.claveAcceso.equals(other.getClaveAcceso()))) &&
            ((this.idUsuarioHost==null && other.getIdUsuarioHost()==null) || 
             (this.idUsuarioHost!=null &&
              this.idUsuarioHost.equals(other.getIdUsuarioHost()))) &&
            ((this.idSesion==null && other.getIdSesion()==null) || 
             (this.idSesion!=null &&
              this.idSesion.equals(other.getIdSesion()))) &&
            ((this.idOperacion==null && other.getIdOperacion()==null) || 
             (this.idOperacion!=null &&
              this.idOperacion.equals(other.getIdOperacion()))) &&
            ((this.tokenOperacion==null && other.getTokenOperacion()==null) || 
             (this.tokenOperacion!=null &&
              this.tokenOperacion.equals(other.getTokenOperacion()))) &&
            ((this.sic==null && other.getSic()==null) || 
             (this.sic!=null &&
              this.sic.equals(other.getSic()))) &&
            ((this.customSeg==null && other.getCustomSeg()==null) || 
             (this.customSeg!=null &&
              this.customSeg.equals(other.getCustomSeg())));
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
        if (getIdUsuario() != null) {
            _hashCode += getIdUsuario().hashCode();
        }
        if (getClaveAcceso() != null) {
            _hashCode += getClaveAcceso().hashCode();
        }
        if (getIdUsuarioHost() != null) {
            _hashCode += getIdUsuarioHost().hashCode();
        }
        if (getIdSesion() != null) {
            _hashCode += getIdSesion().hashCode();
        }
        if (getIdOperacion() != null) {
            _hashCode += getIdOperacion().hashCode();
        }
        if (getTokenOperacion() != null) {
            _hashCode += getTokenOperacion().hashCode();
        }
        if (getSic() != null) {
            _hashCode += getSic().hashCode();
        }
        if (getCustomSeg() != null) {
            _hashCode += getCustomSeg().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AccesoType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", "AccesoType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idUsuario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdUsuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("claveAcceso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ClaveAcceso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idUsuarioHost");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdUsuarioHost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idSesion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdSesion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idOperacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdOperacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tokenOperacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TokenOperacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sic");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Sic"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customSeg");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CustomSeg"));
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

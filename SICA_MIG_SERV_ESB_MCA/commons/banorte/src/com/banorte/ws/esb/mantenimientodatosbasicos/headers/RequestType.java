/**
 * RequestType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.mantenimientodatosbasicos.headers;


/**
 * define los elementos genericos del elemento Request
 */
public class RequestType  implements java.io.Serializable {
    /* Identificador de servicio que se quiere invocar,
     * 						como servicio final. Se usara para consultar al
     * 						WSRR */
    private java.lang.String idServicio;

    /* Identifica la version del servicio a invocar */
    private java.lang.String versionServicio;

    /* Version Mensaje Negocio */
    private java.lang.String versionEndpoint;

    /* Entidad a la que pertenece la terminal conectada */
    private java.lang.String codEmpresa;

    /* Oficina a la que pertenece la terminal conectada */
    private java.math.BigInteger codCR;

    public RequestType() {
    }

    public RequestType(
           java.lang.String idServicio,
           java.lang.String versionServicio,
           java.lang.String versionEndpoint,
           java.lang.String codEmpresa,
           java.math.BigInteger codCR) {
           this.idServicio = idServicio;
           this.versionServicio = versionServicio;
           this.versionEndpoint = versionEndpoint;
           this.codEmpresa = codEmpresa;
           this.codCR = codCR;
    }


    /**
     * Gets the idServicio value for this RequestType.
     * 
     * @return idServicio   * Identificador de servicio que se quiere invocar,
     * 						como servicio final. Se usara para consultar al
     * 						WSRR
     */
    public java.lang.String getIdServicio() {
        return idServicio;
    }


    /**
     * Sets the idServicio value for this RequestType.
     * 
     * @param idServicio   * Identificador de servicio que se quiere invocar,
     * 						como servicio final. Se usara para consultar al
     * 						WSRR
     */
    public void setIdServicio(java.lang.String idServicio) {
        this.idServicio = idServicio;
    }


    /**
     * Gets the versionServicio value for this RequestType.
     * 
     * @return versionServicio   * Identifica la version del servicio a invocar
     */
    public java.lang.String getVersionServicio() {
        return versionServicio;
    }


    /**
     * Sets the versionServicio value for this RequestType.
     * 
     * @param versionServicio   * Identifica la version del servicio a invocar
     */
    public void setVersionServicio(java.lang.String versionServicio) {
        this.versionServicio = versionServicio;
    }


    /**
     * Gets the versionEndpoint value for this RequestType.
     * 
     * @return versionEndpoint   * Version Mensaje Negocio
     */
    public java.lang.String getVersionEndpoint() {
        return versionEndpoint;
    }


    /**
     * Sets the versionEndpoint value for this RequestType.
     * 
     * @param versionEndpoint   * Version Mensaje Negocio
     */
    public void setVersionEndpoint(java.lang.String versionEndpoint) {
        this.versionEndpoint = versionEndpoint;
    }


    /**
     * Gets the codEmpresa value for this RequestType.
     * 
     * @return codEmpresa   * Entidad a la que pertenece la terminal conectada
     */
    public java.lang.String getCodEmpresa() {
        return codEmpresa;
    }


    /**
     * Sets the codEmpresa value for this RequestType.
     * 
     * @param codEmpresa   * Entidad a la que pertenece la terminal conectada
     */
    public void setCodEmpresa(java.lang.String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }


    /**
     * Gets the codCR value for this RequestType.
     * 
     * @return codCR   * Oficina a la que pertenece la terminal conectada
     */
    public java.math.BigInteger getCodCR() {
        return codCR;
    }


    /**
     * Sets the codCR value for this RequestType.
     * 
     * @param codCR   * Oficina a la que pertenece la terminal conectada
     */
    public void setCodCR(java.math.BigInteger codCR) {
        this.codCR = codCR;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RequestType)) return false;
        RequestType other = (RequestType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idServicio==null && other.getIdServicio()==null) || 
             (this.idServicio!=null &&
              this.idServicio.equals(other.getIdServicio()))) &&
            ((this.versionServicio==null && other.getVersionServicio()==null) || 
             (this.versionServicio!=null &&
              this.versionServicio.equals(other.getVersionServicio()))) &&
            ((this.versionEndpoint==null && other.getVersionEndpoint()==null) || 
             (this.versionEndpoint!=null &&
              this.versionEndpoint.equals(other.getVersionEndpoint()))) &&
            ((this.codEmpresa==null && other.getCodEmpresa()==null) || 
             (this.codEmpresa!=null &&
              this.codEmpresa.equals(other.getCodEmpresa()))) &&
            ((this.codCR==null && other.getCodCR()==null) || 
             (this.codCR!=null &&
              this.codCR.equals(other.getCodCR())));
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
        if (getIdServicio() != null) {
            _hashCode += getIdServicio().hashCode();
        }
        if (getVersionServicio() != null) {
            _hashCode += getVersionServicio().hashCode();
        }
        if (getVersionEndpoint() != null) {
            _hashCode += getVersionEndpoint().hashCode();
        }
        if (getCodEmpresa() != null) {
            _hashCode += getCodEmpresa().hashCode();
        }
        if (getCodCR() != null) {
            _hashCode += getCodCR().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RequestType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", "RequestType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idServicio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdServicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("versionServicio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "VersionServicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("versionEndpoint");
        elemField.setXmlName(new javax.xml.namespace.QName("", "VersionEndpoint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codEmpresa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodEmpresa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codCR");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodCR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
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

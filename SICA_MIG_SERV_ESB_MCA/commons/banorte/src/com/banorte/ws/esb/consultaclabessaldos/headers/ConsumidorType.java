/**
 * ConsumidorType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.consultaclabessaldos.headers;

public class ConsumidorType  implements java.io.Serializable {
    /* IP/Nombre de la
     * 						maquina que realiza la peticion */
    private java.lang.String ipServer;

    /* Direccion IP/Nombre
     * 						de la maquina del terminal o
     * 						usuario cliente */
    private java.lang.String ipCliente;

    /* Identifidor del canal
     * 						consumidor */
    private java.lang.String tipoCanal;

    /* Identificador
     * 						aplicacion que ejecuta la
     * 						operacion */
    private java.lang.String idAplicacion;

    public ConsumidorType() {
    }

    public ConsumidorType(
           java.lang.String ipServer,
           java.lang.String ipCliente,
           java.lang.String tipoCanal,
           java.lang.String idAplicacion) {
           this.ipServer = ipServer;
           this.ipCliente = ipCliente;
           this.tipoCanal = tipoCanal;
           this.idAplicacion = idAplicacion;
    }


    /**
     * Gets the ipServer value for this ConsumidorType.
     * 
     * @return ipServer   * IP/Nombre de la
     * 						maquina que realiza la peticion
     */
    public java.lang.String getIpServer() {
        return ipServer;
    }


    /**
     * Sets the ipServer value for this ConsumidorType.
     * 
     * @param ipServer   * IP/Nombre de la
     * 						maquina que realiza la peticion
     */
    public void setIpServer(java.lang.String ipServer) {
        this.ipServer = ipServer;
    }


    /**
     * Gets the ipCliente value for this ConsumidorType.
     * 
     * @return ipCliente   * Direccion IP/Nombre
     * 						de la maquina del terminal o
     * 						usuario cliente
     */
    public java.lang.String getIpCliente() {
        return ipCliente;
    }


    /**
     * Sets the ipCliente value for this ConsumidorType.
     * 
     * @param ipCliente   * Direccion IP/Nombre
     * 						de la maquina del terminal o
     * 						usuario cliente
     */
    public void setIpCliente(java.lang.String ipCliente) {
        this.ipCliente = ipCliente;
    }


    /**
     * Gets the tipoCanal value for this ConsumidorType.
     * 
     * @return tipoCanal   * Identifidor del canal
     * 						consumidor
     */
    public java.lang.String getTipoCanal() {
        return tipoCanal;
    }


    /**
     * Sets the tipoCanal value for this ConsumidorType.
     * 
     * @param tipoCanal   * Identifidor del canal
     * 						consumidor
     */
    public void setTipoCanal(java.lang.String tipoCanal) {
        this.tipoCanal = tipoCanal;
    }


    /**
     * Gets the idAplicacion value for this ConsumidorType.
     * 
     * @return idAplicacion   * Identificador
     * 						aplicacion que ejecuta la
     * 						operacion
     */
    public java.lang.String getIdAplicacion() {
        return idAplicacion;
    }


    /**
     * Sets the idAplicacion value for this ConsumidorType.
     * 
     * @param idAplicacion   * Identificador
     * 						aplicacion que ejecuta la
     * 						operacion
     */
    public void setIdAplicacion(java.lang.String idAplicacion) {
        this.idAplicacion = idAplicacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsumidorType)) return false;
        ConsumidorType other = (ConsumidorType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ipServer==null && other.getIpServer()==null) || 
             (this.ipServer!=null &&
              this.ipServer.equals(other.getIpServer()))) &&
            ((this.ipCliente==null && other.getIpCliente()==null) || 
             (this.ipCliente!=null &&
              this.ipCliente.equals(other.getIpCliente()))) &&
            ((this.tipoCanal==null && other.getTipoCanal()==null) || 
             (this.tipoCanal!=null &&
              this.tipoCanal.equals(other.getTipoCanal()))) &&
            ((this.idAplicacion==null && other.getIdAplicacion()==null) || 
             (this.idAplicacion!=null &&
              this.idAplicacion.equals(other.getIdAplicacion())));
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
        if (getIpServer() != null) {
            _hashCode += getIpServer().hashCode();
        }
        if (getIpCliente() != null) {
            _hashCode += getIpCliente().hashCode();
        }
        if (getTipoCanal() != null) {
            _hashCode += getTipoCanal().hashCode();
        }
        if (getIdAplicacion() != null) {
            _hashCode += getIdAplicacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsumidorType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", "ConsumidorType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ipServer");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IpServer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ipCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IpCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoCanal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TipoCanal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAplicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdAplicacion"));
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

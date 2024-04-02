/**
 * HeaderResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.consultaclabessaldos.headers;

public class HeaderResponseType  implements java.io.Serializable {
    /* Identificador de operacion para correlar
     * 						operaciones */
    private java.lang.String idOperacion;

    /* Identificador de operacion en la entidad
     * 						destino(Back-End) */
    private java.lang.String numReferencia;

    /* Numero de registros retornados */
    private java.math.BigInteger totalOcurrencias;

    /* -Elemento que agrupa el detalle del error de
     * 						Negocio */
    private com.banorte.ws.esb.consultaclabessaldos.headers.EstadoRespuestaType estadoRespuesta;

    /* Token de operacion retornada por la interfaz Valida Transaccion,
     * es un token que se genera inicialmente al momento de la utentificacion
     * y es enviado en cada una de las invocaciones posteriores.
     * 
     * El mismo cambia con cada invocacion, es decir un nuevo token es generado
     * como respuesta a una operacion y el cual debera ser enviado en la
     * invocacion subsecuente.
     * 
     * El id de la ssesion es el mismo pero el token de operacion cambia
     * con cada invocacion. */
    private java.lang.String tokenOperacion;

    private java.util.Calendar fechaHora;  // attribute

    public HeaderResponseType() {
    }

    public HeaderResponseType(
           java.lang.String idOperacion,
           java.lang.String numReferencia,
           java.math.BigInteger totalOcurrencias,
           com.banorte.ws.esb.consultaclabessaldos.headers.EstadoRespuestaType estadoRespuesta,
           java.lang.String tokenOperacion,
           java.util.Calendar fechaHora) {
           this.idOperacion = idOperacion;
           this.numReferencia = numReferencia;
           this.totalOcurrencias = totalOcurrencias;
           this.estadoRespuesta = estadoRespuesta;
           this.tokenOperacion = tokenOperacion;
           this.fechaHora = fechaHora;
    }


    /**
     * Gets the idOperacion value for this HeaderResponseType.
     * 
     * @return idOperacion   * Identificador de operacion para correlar
     * 						operaciones
     */
    public java.lang.String getIdOperacion() {
        return idOperacion;
    }


    /**
     * Sets the idOperacion value for this HeaderResponseType.
     * 
     * @param idOperacion   * Identificador de operacion para correlar
     * 						operaciones
     */
    public void setIdOperacion(java.lang.String idOperacion) {
        this.idOperacion = idOperacion;
    }


    /**
     * Gets the numReferencia value for this HeaderResponseType.
     * 
     * @return numReferencia   * Identificador de operacion en la entidad
     * 						destino(Back-End)
     */
    public java.lang.String getNumReferencia() {
        return numReferencia;
    }


    /**
     * Sets the numReferencia value for this HeaderResponseType.
     * 
     * @param numReferencia   * Identificador de operacion en la entidad
     * 						destino(Back-End)
     */
    public void setNumReferencia(java.lang.String numReferencia) {
        this.numReferencia = numReferencia;
    }


    /**
     * Gets the totalOcurrencias value for this HeaderResponseType.
     * 
     * @return totalOcurrencias   * Numero de registros retornados
     */
    public java.math.BigInteger getTotalOcurrencias() {
        return totalOcurrencias;
    }


    /**
     * Sets the totalOcurrencias value for this HeaderResponseType.
     * 
     * @param totalOcurrencias   * Numero de registros retornados
     */
    public void setTotalOcurrencias(java.math.BigInteger totalOcurrencias) {
        this.totalOcurrencias = totalOcurrencias;
    }


    /**
     * Gets the estadoRespuesta value for this HeaderResponseType.
     * 
     * @return estadoRespuesta   * -Elemento que agrupa el detalle del error de
     * 						Negocio
     */
    public com.banorte.ws.esb.consultaclabessaldos.headers.EstadoRespuestaType getEstadoRespuesta() {
        return estadoRespuesta;
    }


    /**
     * Sets the estadoRespuesta value for this HeaderResponseType.
     * 
     * @param estadoRespuesta   * -Elemento que agrupa el detalle del error de
     * 						Negocio
     */
    public void setEstadoRespuesta(com.banorte.ws.esb.consultaclabessaldos.headers.EstadoRespuestaType estadoRespuesta) {
        this.estadoRespuesta = estadoRespuesta;
    }


    /**
     * Gets the tokenOperacion value for this HeaderResponseType.
     * 
     * @return tokenOperacion   * Token de operacion retornada por la interfaz Valida Transaccion,
     * es un token que se genera inicialmente al momento de la utentificacion
     * y es enviado en cada una de las invocaciones posteriores.
     * 
     * El mismo cambia con cada invocacion, es decir un nuevo token es generado
     * como respuesta a una operacion y el cual debera ser enviado en la
     * invocacion subsecuente.
     * 
     * El id de la ssesion es el mismo pero el token de operacion cambia
     * con cada invocacion.
     */
    public java.lang.String getTokenOperacion() {
        return tokenOperacion;
    }


    /**
     * Sets the tokenOperacion value for this HeaderResponseType.
     * 
     * @param tokenOperacion   * Token de operacion retornada por la interfaz Valida Transaccion,
     * es un token que se genera inicialmente al momento de la utentificacion
     * y es enviado en cada una de las invocaciones posteriores.
     * 
     * El mismo cambia con cada invocacion, es decir un nuevo token es generado
     * como respuesta a una operacion y el cual debera ser enviado en la
     * invocacion subsecuente.
     * 
     * El id de la ssesion es el mismo pero el token de operacion cambia
     * con cada invocacion.
     */
    public void setTokenOperacion(java.lang.String tokenOperacion) {
        this.tokenOperacion = tokenOperacion;
    }


    /**
     * Gets the fechaHora value for this HeaderResponseType.
     * 
     * @return fechaHora
     */
    public java.util.Calendar getFechaHora() {
        return fechaHora;
    }


    /**
     * Sets the fechaHora value for this HeaderResponseType.
     * 
     * @param fechaHora
     */
    public void setFechaHora(java.util.Calendar fechaHora) {
        this.fechaHora = fechaHora;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof HeaderResponseType)) return false;
        HeaderResponseType other = (HeaderResponseType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idOperacion==null && other.getIdOperacion()==null) || 
             (this.idOperacion!=null &&
              this.idOperacion.equals(other.getIdOperacion()))) &&
            ((this.numReferencia==null && other.getNumReferencia()==null) || 
             (this.numReferencia!=null &&
              this.numReferencia.equals(other.getNumReferencia()))) &&
            ((this.totalOcurrencias==null && other.getTotalOcurrencias()==null) || 
             (this.totalOcurrencias!=null &&
              this.totalOcurrencias.equals(other.getTotalOcurrencias()))) &&
            ((this.estadoRespuesta==null && other.getEstadoRespuesta()==null) || 
             (this.estadoRespuesta!=null &&
              this.estadoRespuesta.equals(other.getEstadoRespuesta()))) &&
            ((this.tokenOperacion==null && other.getTokenOperacion()==null) || 
             (this.tokenOperacion!=null &&
              this.tokenOperacion.equals(other.getTokenOperacion()))) &&
            ((this.fechaHora==null && other.getFechaHora()==null) || 
             (this.fechaHora!=null &&
              this.fechaHora.equals(other.getFechaHora())));
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
        if (getIdOperacion() != null) {
            _hashCode += getIdOperacion().hashCode();
        }
        if (getNumReferencia() != null) {
            _hashCode += getNumReferencia().hashCode();
        }
        if (getTotalOcurrencias() != null) {
            _hashCode += getTotalOcurrencias().hashCode();
        }
        if (getEstadoRespuesta() != null) {
            _hashCode += getEstadoRespuesta().hashCode();
        }
        if (getTokenOperacion() != null) {
            _hashCode += getTokenOperacion().hashCode();
        }
        if (getFechaHora() != null) {
            _hashCode += getFechaHora().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(HeaderResponseType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", "HeaderResponseType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("fechaHora");
        attrField.setXmlName(new javax.xml.namespace.QName("", "fechaHora"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idOperacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdOperacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numReferencia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NumReferencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalOcurrencias");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TotalOcurrencias"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estadoRespuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EstadoRespuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", "EstadoRespuestaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tokenOperacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TokenOperacion"));
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

/**
 * ErrorType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.ESBResponse.ConsultaIdIxeAltamira;

public class ErrorType  extends org.apache.axis.AxisFault  implements java.io.Serializable {
    private java.lang.String msgError;

    private java.lang.String descError;

    private java.lang.String idError;  // attribute

    public ErrorType() {
    }

    public ErrorType(
           java.lang.String idError,
           java.lang.String msgError,
           java.lang.String descError) {
        this.idError = idError;
        this.msgError = msgError;
        this.descError = descError;
    }


    /**
     * Gets the msgError value for this ErrorType.
     * 
     * @return msgError
     */
    public java.lang.String getMsgError() {
        return msgError;
    }


    /**
     * Sets the msgError value for this ErrorType.
     * 
     * @param msgError
     */
    public void setMsgError(java.lang.String msgError) {
        this.msgError = msgError;
    }


    /**
     * Gets the descError value for this ErrorType.
     * 
     * @return descError
     */
    public java.lang.String getDescError() {
        return descError;
    }


    /**
     * Sets the descError value for this ErrorType.
     * 
     * @param descError
     */
    public void setDescError(java.lang.String descError) {
        this.descError = descError;
    }


    /**
     * Gets the idError value for this ErrorType.
     * 
     * @return idError
     */
    public java.lang.String getIdError() {
        return idError;
    }


    /**
     * Sets the idError value for this ErrorType.
     * 
     * @param idError
     */
    public void setIdError(java.lang.String idError) {
        this.idError = idError;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ErrorType)) return false;
        ErrorType other = (ErrorType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.msgError==null && other.getMsgError()==null) || 
             (this.msgError!=null &&
              this.msgError.equals(other.getMsgError()))) &&
            ((this.descError==null && other.getDescError()==null) || 
             (this.descError!=null &&
              this.descError.equals(other.getDescError()))) &&
            ((this.idError==null && other.getIdError()==null) || 
             (this.idError!=null &&
              this.idError.equals(other.getIdError())));
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
        if (getMsgError() != null) {
            _hashCode += getMsgError().hashCode();
        }
        if (getDescError() != null) {
            _hashCode += getDescError().hashCode();
        }
        if (getIdError() != null) {
            _hashCode += getIdError().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ErrorType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "ErrorType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("idError");
        attrField.setXmlName(new javax.xml.namespace.QName("", "idError"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("msgError");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "MsgError"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descError");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBResponse", "DescError"));
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


    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, this);
    }
}

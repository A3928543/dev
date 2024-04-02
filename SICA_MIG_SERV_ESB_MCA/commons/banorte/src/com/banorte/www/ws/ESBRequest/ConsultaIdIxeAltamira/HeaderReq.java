/**
 * HeaderReq.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira;

public class HeaderReq  implements java.io.Serializable {
    private java.lang.String ticket;

    private java.lang.String CR;

    private java.lang.String usrOper;

    private com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.HeaderReqVerHeaderReq verHeaderReq;  // attribute

    public HeaderReq() {
    }

    public HeaderReq(
           java.lang.String ticket,
           java.lang.String CR,
           java.lang.String usrOper,
           com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.HeaderReqVerHeaderReq verHeaderReq) {
           this.ticket = ticket;
           this.CR = CR;
           this.usrOper = usrOper;
           this.verHeaderReq = verHeaderReq;
    }


    /**
     * Gets the ticket value for this HeaderReq.
     * 
     * @return ticket
     */
    public java.lang.String getTicket() {
        return ticket;
    }


    /**
     * Sets the ticket value for this HeaderReq.
     * 
     * @param ticket
     */
    public void setTicket(java.lang.String ticket) {
        this.ticket = ticket;
    }


    /**
     * Gets the CR value for this HeaderReq.
     * 
     * @return CR
     */
    public java.lang.String getCR() {
        return CR;
    }


    /**
     * Sets the CR value for this HeaderReq.
     * 
     * @param CR
     */
    public void setCR(java.lang.String CR) {
        this.CR = CR;
    }


    /**
     * Gets the usrOper value for this HeaderReq.
     * 
     * @return usrOper
     */
    public java.lang.String getUsrOper() {
        return usrOper;
    }


    /**
     * Sets the usrOper value for this HeaderReq.
     * 
     * @param usrOper
     */
    public void setUsrOper(java.lang.String usrOper) {
        this.usrOper = usrOper;
    }


    /**
     * Gets the verHeaderReq value for this HeaderReq.
     * 
     * @return verHeaderReq
     */
    public com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.HeaderReqVerHeaderReq getVerHeaderReq() {
        return verHeaderReq;
    }


    /**
     * Sets the verHeaderReq value for this HeaderReq.
     * 
     * @param verHeaderReq
     */
    public void setVerHeaderReq(com.banorte.www.ws.ESBRequest.ConsultaIdIxeAltamira.HeaderReqVerHeaderReq verHeaderReq) {
        this.verHeaderReq = verHeaderReq;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof HeaderReq)) return false;
        HeaderReq other = (HeaderReq) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ticket==null && other.getTicket()==null) || 
             (this.ticket!=null &&
              this.ticket.equals(other.getTicket()))) &&
            ((this.CR==null && other.getCR()==null) || 
             (this.CR!=null &&
              this.CR.equals(other.getCR()))) &&
            ((this.usrOper==null && other.getUsrOper()==null) || 
             (this.usrOper!=null &&
              this.usrOper.equals(other.getUsrOper()))) &&
            ((this.verHeaderReq==null && other.getVerHeaderReq()==null) || 
             (this.verHeaderReq!=null &&
              this.verHeaderReq.equals(other.getVerHeaderReq())));
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
        if (getTicket() != null) {
            _hashCode += getTicket().hashCode();
        }
        if (getCR() != null) {
            _hashCode += getCR().hashCode();
        }
        if (getUsrOper() != null) {
            _hashCode += getUsrOper().hashCode();
        }
        if (getVerHeaderReq() != null) {
            _hashCode += getVerHeaderReq().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(HeaderReq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "HeaderReq"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("verHeaderReq");
        attrField.setXmlName(new javax.xml.namespace.QName("", "verHeaderReq"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", ">HeaderReq>verHeaderReq"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ticket");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "Ticket"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "CR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usrOper");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/ESBRequest", "UsrOper"));
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

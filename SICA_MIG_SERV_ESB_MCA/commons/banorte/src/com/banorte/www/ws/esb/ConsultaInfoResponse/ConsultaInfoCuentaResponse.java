/**
 * ConsultaInfoCuentaResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.www.ws.esb.ConsultaInfoResponse;

public class ConsultaInfoCuentaResponse  implements java.io.Serializable {
    private com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1621 bgm1621;

    private com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1622 bgm1622;

    private com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1623 bgm1623;

    private com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1624[] bgm1624;

    private com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1625 bgm1625;

    private com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1626[] bgm1626;

    public ConsultaInfoCuentaResponse() {
    }

    public ConsultaInfoCuentaResponse(
           com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1621 bgm1621,
           com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1622 bgm1622,
           com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1623 bgm1623,
           com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1624[] bgm1624,
           com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1625 bgm1625,
           com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1626[] bgm1626) {
           this.bgm1621 = bgm1621;
           this.bgm1622 = bgm1622;
           this.bgm1623 = bgm1623;
           this.bgm1624 = bgm1624;
           this.bgm1625 = bgm1625;
           this.bgm1626 = bgm1626;
    }


    /**
     * Gets the bgm1621 value for this ConsultaInfoCuentaResponse.
     * 
     * @return bgm1621
     */
    public com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1621 getBgm1621() {
        return bgm1621;
    }


    /**
     * Sets the bgm1621 value for this ConsultaInfoCuentaResponse.
     * 
     * @param bgm1621
     */
    public void setBgm1621(com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1621 bgm1621) {
        this.bgm1621 = bgm1621;
    }


    /**
     * Gets the bgm1622 value for this ConsultaInfoCuentaResponse.
     * 
     * @return bgm1622
     */
    public com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1622 getBgm1622() {
        return bgm1622;
    }


    /**
     * Sets the bgm1622 value for this ConsultaInfoCuentaResponse.
     * 
     * @param bgm1622
     */
    public void setBgm1622(com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1622 bgm1622) {
        this.bgm1622 = bgm1622;
    }


    /**
     * Gets the bgm1623 value for this ConsultaInfoCuentaResponse.
     * 
     * @return bgm1623
     */
    public com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1623 getBgm1623() {
        return bgm1623;
    }


    /**
     * Sets the bgm1623 value for this ConsultaInfoCuentaResponse.
     * 
     * @param bgm1623
     */
    public void setBgm1623(com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1623 bgm1623) {
        this.bgm1623 = bgm1623;
    }


    /**
     * Gets the bgm1624 value for this ConsultaInfoCuentaResponse.
     * 
     * @return bgm1624
     */
    public com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1624[] getBgm1624() {
        return bgm1624;
    }


    /**
     * Sets the bgm1624 value for this ConsultaInfoCuentaResponse.
     * 
     * @param bgm1624
     */
    public void setBgm1624(com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1624[] bgm1624) {
        this.bgm1624 = bgm1624;
    }

    public com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1624 getBgm1624(int i) {
        return this.bgm1624[i];
    }

    public void setBgm1624(int i, com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1624 _value) {
        this.bgm1624[i] = _value;
    }


    /**
     * Gets the bgm1625 value for this ConsultaInfoCuentaResponse.
     * 
     * @return bgm1625
     */
    public com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1625 getBgm1625() {
        return bgm1625;
    }


    /**
     * Sets the bgm1625 value for this ConsultaInfoCuentaResponse.
     * 
     * @param bgm1625
     */
    public void setBgm1625(com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1625 bgm1625) {
        this.bgm1625 = bgm1625;
    }


    /**
     * Gets the bgm1626 value for this ConsultaInfoCuentaResponse.
     * 
     * @return bgm1626
     */
    public com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1626[] getBgm1626() {
        return bgm1626;
    }


    /**
     * Sets the bgm1626 value for this ConsultaInfoCuentaResponse.
     * 
     * @param bgm1626
     */
    public void setBgm1626(com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1626[] bgm1626) {
        this.bgm1626 = bgm1626;
    }

    public com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1626 getBgm1626(int i) {
        return this.bgm1626[i];
    }

    public void setBgm1626(int i, com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1626 _value) {
        this.bgm1626[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaInfoCuentaResponse)) return false;
        ConsultaInfoCuentaResponse other = (ConsultaInfoCuentaResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.bgm1621==null && other.getBgm1621()==null) || 
             (this.bgm1621!=null &&
              this.bgm1621.equals(other.getBgm1621()))) &&
            ((this.bgm1622==null && other.getBgm1622()==null) || 
             (this.bgm1622!=null &&
              this.bgm1622.equals(other.getBgm1622()))) &&
            ((this.bgm1623==null && other.getBgm1623()==null) || 
             (this.bgm1623!=null &&
              this.bgm1623.equals(other.getBgm1623()))) &&
            ((this.bgm1624==null && other.getBgm1624()==null) || 
             (this.bgm1624!=null &&
              java.util.Arrays.equals(this.bgm1624, other.getBgm1624()))) &&
            ((this.bgm1625==null && other.getBgm1625()==null) || 
             (this.bgm1625!=null &&
              this.bgm1625.equals(other.getBgm1625()))) &&
            ((this.bgm1626==null && other.getBgm1626()==null) || 
             (this.bgm1626!=null &&
              java.util.Arrays.equals(this.bgm1626, other.getBgm1626())));
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
        if (getBgm1621() != null) {
            _hashCode += getBgm1621().hashCode();
        }
        if (getBgm1622() != null) {
            _hashCode += getBgm1622().hashCode();
        }
        if (getBgm1623() != null) {
            _hashCode += getBgm1623().hashCode();
        }
        if (getBgm1624() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBgm1624());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBgm1624(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getBgm1625() != null) {
            _hashCode += getBgm1625().hashCode();
        }
        if (getBgm1626() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBgm1626());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBgm1626(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultaInfoCuentaResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "ConsultaInfoCuentaResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bgm1621");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "Bgm1621"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "BGM1621"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bgm1622");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "Bgm1622"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "BGM1622"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bgm1623");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "Bgm1623"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "BGM1623"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bgm1624");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "Bgm1624"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "BGM1624"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bgm1625");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "Bgm1625"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "BGM1625"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bgm1626");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "Bgm1626"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaInfoResponse", "BGM1626"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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

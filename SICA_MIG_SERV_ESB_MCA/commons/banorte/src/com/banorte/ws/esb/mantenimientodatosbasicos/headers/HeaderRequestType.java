/**
 * HeaderRequestType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.mantenimientodatosbasicos.headers;


/**
 * -Elemento Base que
 * 				define los elementos genericos del Header Request
 * 				--ATRIBUTOS--
 * 				-locale: Informacion de localizacion del peticionario
 * 				-CodIdioma:
 * 				Formato de codificacion de caracteres
 */
public class HeaderRequestType  implements java.io.Serializable {
    private com.banorte.ws.esb.mantenimientodatosbasicos.headers.DespliegueType despliegue;

    private com.banorte.ws.esb.mantenimientodatosbasicos.headers.AccesoType acceso;

    private com.banorte.ws.esb.mantenimientodatosbasicos.headers.ConsumidorType consumidor;

    private com.banorte.ws.esb.mantenimientodatosbasicos.headers.RequestType datosPeticion;

    private com.banorte.ws.esb.mantenimientodatosbasicos.headers.PropiedadType[] datosVariables;

    private java.lang.String locale;  // attribute

    private java.lang.String codIdioma;  // attribute

    public HeaderRequestType() {
    }

    public HeaderRequestType(
           com.banorte.ws.esb.mantenimientodatosbasicos.headers.DespliegueType despliegue,
           com.banorte.ws.esb.mantenimientodatosbasicos.headers.AccesoType acceso,
           com.banorte.ws.esb.mantenimientodatosbasicos.headers.ConsumidorType consumidor,
           com.banorte.ws.esb.mantenimientodatosbasicos.headers.RequestType datosPeticion,
           com.banorte.ws.esb.mantenimientodatosbasicos.headers.PropiedadType[] datosVariables,
           java.lang.String locale,
           java.lang.String codIdioma) {
           this.despliegue = despliegue;
           this.acceso = acceso;
           this.consumidor = consumidor;
           this.datosPeticion = datosPeticion;
           this.datosVariables = datosVariables;
           this.locale = locale;
           this.codIdioma = codIdioma;
    }


    /**
     * Gets the despliegue value for this HeaderRequestType.
     * 
     * @return despliegue
     */
    public com.banorte.ws.esb.mantenimientodatosbasicos.headers.DespliegueType getDespliegue() {
        return despliegue;
    }


    /**
     * Sets the despliegue value for this HeaderRequestType.
     * 
     * @param despliegue
     */
    public void setDespliegue(com.banorte.ws.esb.mantenimientodatosbasicos.headers.DespliegueType despliegue) {
        this.despliegue = despliegue;
    }


    /**
     * Gets the acceso value for this HeaderRequestType.
     * 
     * @return acceso
     */
    public com.banorte.ws.esb.mantenimientodatosbasicos.headers.AccesoType getAcceso() {
        return acceso;
    }


    /**
     * Sets the acceso value for this HeaderRequestType.
     * 
     * @param acceso
     */
    public void setAcceso(com.banorte.ws.esb.mantenimientodatosbasicos.headers.AccesoType acceso) {
        this.acceso = acceso;
    }


    /**
     * Gets the consumidor value for this HeaderRequestType.
     * 
     * @return consumidor
     */
    public com.banorte.ws.esb.mantenimientodatosbasicos.headers.ConsumidorType getConsumidor() {
        return consumidor;
    }


    /**
     * Sets the consumidor value for this HeaderRequestType.
     * 
     * @param consumidor
     */
    public void setConsumidor(com.banorte.ws.esb.mantenimientodatosbasicos.headers.ConsumidorType consumidor) {
        this.consumidor = consumidor;
    }


    /**
     * Gets the datosPeticion value for this HeaderRequestType.
     * 
     * @return datosPeticion
     */
    public com.banorte.ws.esb.mantenimientodatosbasicos.headers.RequestType getDatosPeticion() {
        return datosPeticion;
    }


    /**
     * Sets the datosPeticion value for this HeaderRequestType.
     * 
     * @param datosPeticion
     */
    public void setDatosPeticion(com.banorte.ws.esb.mantenimientodatosbasicos.headers.RequestType datosPeticion) {
        this.datosPeticion = datosPeticion;
    }


    /**
     * Gets the datosVariables value for this HeaderRequestType.
     * 
     * @return datosVariables
     */
    public com.banorte.ws.esb.mantenimientodatosbasicos.headers.PropiedadType[] getDatosVariables() {
        return datosVariables;
    }


    /**
     * Sets the datosVariables value for this HeaderRequestType.
     * 
     * @param datosVariables
     */
    public void setDatosVariables(com.banorte.ws.esb.mantenimientodatosbasicos.headers.PropiedadType[] datosVariables) {
        this.datosVariables = datosVariables;
    }


    /**
     * Gets the locale value for this HeaderRequestType.
     * 
     * @return locale
     */
    public java.lang.String getLocale() {
        return locale;
    }


    /**
     * Sets the locale value for this HeaderRequestType.
     * 
     * @param locale
     */
    public void setLocale(java.lang.String locale) {
        this.locale = locale;
    }


    /**
     * Gets the codIdioma value for this HeaderRequestType.
     * 
     * @return codIdioma
     */
    public java.lang.String getCodIdioma() {
        return codIdioma;
    }


    /**
     * Sets the codIdioma value for this HeaderRequestType.
     * 
     * @param codIdioma
     */
    public void setCodIdioma(java.lang.String codIdioma) {
        this.codIdioma = codIdioma;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof HeaderRequestType)) return false;
        HeaderRequestType other = (HeaderRequestType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.despliegue==null && other.getDespliegue()==null) || 
             (this.despliegue!=null &&
              this.despliegue.equals(other.getDespliegue()))) &&
            ((this.acceso==null && other.getAcceso()==null) || 
             (this.acceso!=null &&
              this.acceso.equals(other.getAcceso()))) &&
            ((this.consumidor==null && other.getConsumidor()==null) || 
             (this.consumidor!=null &&
              this.consumidor.equals(other.getConsumidor()))) &&
            ((this.datosPeticion==null && other.getDatosPeticion()==null) || 
             (this.datosPeticion!=null &&
              this.datosPeticion.equals(other.getDatosPeticion()))) &&
            ((this.datosVariables==null && other.getDatosVariables()==null) || 
             (this.datosVariables!=null &&
              java.util.Arrays.equals(this.datosVariables, other.getDatosVariables()))) &&
            ((this.locale==null && other.getLocale()==null) || 
             (this.locale!=null &&
              this.locale.equals(other.getLocale()))) &&
            ((this.codIdioma==null && other.getCodIdioma()==null) || 
             (this.codIdioma!=null &&
              this.codIdioma.equals(other.getCodIdioma())));
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
        if (getDespliegue() != null) {
            _hashCode += getDespliegue().hashCode();
        }
        if (getAcceso() != null) {
            _hashCode += getAcceso().hashCode();
        }
        if (getConsumidor() != null) {
            _hashCode += getConsumidor().hashCode();
        }
        if (getDatosPeticion() != null) {
            _hashCode += getDatosPeticion().hashCode();
        }
        if (getDatosVariables() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDatosVariables());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDatosVariables(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLocale() != null) {
            _hashCode += getLocale().hashCode();
        }
        if (getCodIdioma() != null) {
            _hashCode += getCodIdioma().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(HeaderRequestType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", "HeaderRequestType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("locale");
        attrField.setXmlName(new javax.xml.namespace.QName("", "locale"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", ">HeaderRequestType>locale"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("codIdioma");
        attrField.setXmlName(new javax.xml.namespace.QName("", "codIdioma"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", ">HeaderRequestType>codIdioma"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("despliegue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Despliegue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", "DespliegueType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acceso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Acceso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", "AccesoType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("consumidor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Consumidor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", "ConsumidorType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datosPeticion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DatosPeticion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", "RequestType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datosVariables");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DatosVariables"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/general/Headers", "PropiedadType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "Propiedad"));
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

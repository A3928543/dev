/*
 * $Id: SapGenPolXs.java,v 1.2 2009/12/22 18:05:17 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="SAP_A_GENPOL_XS"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.SapGenPolXs"
 * dynamic-update="true"
 * 
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.2 $ $Date: 2009/12/22 18:05:17 $
 */
public class SapGenPolXs implements Serializable {

    public SapGenPolXs() {
        super();
    }

    /**
     * Regresa el valor de idRegistro.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_REGISTRO"
     * unsaved-value="0"
     * @return int.
     */
    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    /**
     * Regresa el valor de fechaDoc.
     *
     * @hibernate.property column="FECHADOC"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public Date getFechaDoc() {
        return fechaDoc;
    }

    public void setFechaDoc(Date fechaDoc) {
        this.fechaDoc = fechaDoc;
    }

    /**
     * Regresa el valor de producto.
     *
     * @hibernate.property column="PRODUCTO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    /**
     * Regresa el valor de asignacion.
     *
     * @hibernate.property column="ASIGNACION"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getAsignacion() {
        return asignacion;
    }

    public void setAsignacion(String asignacion) {
        this.asignacion = asignacion;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof SapGenPolXs)) {
            return false;
        }
        SapGenPolXs castOther = (SapGenPolXs) other;
        return new EqualsBuilder().append(this.getIdRegistro(),
                castOther.getIdRegistro()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdRegistro()).toHashCode();
    }
    
    private int idRegistro;

    private Date fechaDoc;

    private String producto;

    private String asignacion;       

    private static final long serialVersionUID = 657935221251381370L;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banorte.ods.sica.batch.transfint.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Class ScParametro.
 */
@Entity
@Table(name = "SC_PARAMETRO")
public class ScParametro implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id parametro. */
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PARAMETRO")
    private String idParametro;
    
    /** The caducidad. */
    @Column(name = "CADUCIDAD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date caducidad;
    
    /** The tipo valor. */
    @Column(name = "TIPO_VALOR")
    private Character tipoValor;
    
    /** The valor. */
    @Column(name = "VALOR")
    private String valor;

    /**
     * Instantiates a new sc parametro.
     */
    public ScParametro() {
    }

    /**
     * Instantiates a new sc parametro.
     *
     * @param idParametro the id parametro
     */
    public ScParametro(String idParametro) {
        this.idParametro = idParametro;
    }

    /**
     * Gets the id parametro.
     *
     * @return the id parametro
     */
    public String getIdParametro() {
        return idParametro;
    }

    /**
     * Sets the id parametro.
     *
     * @param idParametro the new id parametro
     */
    public void setIdParametro(String idParametro) {
        this.idParametro = idParametro;
    }

    /**
     * Gets the caducidad.
     *
     * @return the caducidad
     */
    public Date getCaducidad() {
        return caducidad;
    }

    /**
     * Sets the caducidad.
     *
     * @param caducidad the new caducidad
     */
    public void setCaducidad(Date caducidad) {
        this.caducidad = caducidad;
    }

    /**
     * Gets the tipo valor.
     *
     * @return the tipo valor
     */
    public Character getTipoValor() {
        return tipoValor;
    }

    /**
     * Sets the tipo valor.
     *
     * @param tipoValor the new tipo valor
     */
    public void setTipoValor(Character tipoValor) {
        this.tipoValor = tipoValor;
    }

    /**
     * Gets the valor.
     *
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * Sets the valor.
     *
     * @param valor the new valor
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idParametro != null ? idParametro.hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ScParametro)) {
            return false;
        }
        ScParametro other = (ScParametro) object;
        if ((this.idParametro == null && other.idParametro != null) || (this.idParametro != null && !this.idParametro.equals(other.idParametro))) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "com.banorte.ods.sica.batch.transfint.domain.ScParametro[ idParametro=" + idParametro + " ]";
    }
    
}

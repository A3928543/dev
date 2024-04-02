package com.ixe.ods.sica.batch.domain;

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
 *
 * @author IHernandez
 */
@Entity
@Table(name = "SC_PARAMETRO")
public class ScParametro implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PARAMETRO")
    private String idParametro;
    
    @Column(name = "CADUCIDAD")
    @Temporal(TemporalType.DATE)
    private Date caducidad;
    
    @Column(name = "TIPO_VALOR")
    private Character tipoValor;
    
    @Column(name = "VALOR")
    private String valor;

    public ScParametro() {
    }

    public ScParametro(String idParametro) {
        this.idParametro = idParametro;
    }

    public String getIdParametro() {
        return idParametro;
    }

    public void setIdParametro(String idParametro) {
        this.idParametro = idParametro;
    }

    public Date getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(Date caducidad) {
        this.caducidad = caducidad;
    }

    public Character getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(Character tipoValor) {
        this.tipoValor = tipoValor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idParametro != null ? idParametro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ScParametro)) {
            return false;
        }
        ScParametro other = (ScParametro) object;
        if ((this.idParametro == null && 
        		other.idParametro != null) || 
        		(this.idParametro != null && 
        		!this.idParametro.equals(other.idParametro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ixe.ods.sica.batch.domain.ScParametro[" + 
        			" idParametro=" + idParametro + " ]";
    }
    
}

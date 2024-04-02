package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "SC_SISTEMA")
public class ScSistema implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID_SISTEMA")
    private Short idSistema;
    
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @Basic(optional = false)
    @Column(name = "USUARIO_CREACION")
    private BigInteger usuarioCreacion;
    
    @Basic(optional = false)
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    
    @Column(name = "USUARIO_ULT_MODIF")
    private BigInteger usuarioUltModif;
    
    @Column(name = "FECHA_ULT_MODIF")
    @Temporal(TemporalType.DATE)
    private Date fechaUltModif;

    public ScSistema() {
    }

    public ScSistema(Short idSistema) {
        this.idSistema = idSistema;
    }

    public ScSistema(Short idSistema, String descripcion, 
    		BigInteger usuarioCreacion, Date fechaCreacion) {
        this.idSistema = idSistema;
        this.descripcion = descripcion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
    }

    public Short getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(Short idSistema) {
        this.idSistema = idSistema;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigInteger getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(BigInteger usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigInteger getUsuarioUltModif() {
        return usuarioUltModif;
    }

    public void setUsuarioUltModif(BigInteger usuarioUltModif) {
        this.usuarioUltModif = usuarioUltModif;
    }

    public Date getFechaUltModif() {
        return fechaUltModif;
    }

    public void setFechaUltModif(Date fechaUltModif) {
        this.fechaUltModif = fechaUltModif;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSistema != null ? idSistema.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ScSistema)) {
            return false;
        }
        ScSistema other = (ScSistema) object;
        if ((this.idSistema == null && other.idSistema != null) || 
        		(this.idSistema != null && !this.idSistema.equals(other.idSistema))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ixe.ods.sica.batch.domain.ScSistema[ idSistema=" + idSistema + " ]";
    }
    
}

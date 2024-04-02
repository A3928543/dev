package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author IHernandez
 */
@Entity
@Table(name = "BUP_PERSONA_CUENTA_ROL")
public class BupPersonaCuentaRol implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected BupPersonaCuentaRolPK bupPersonaCuentaRolPK;
    
    @Column(name = "PARTICIPACION")
    private String participacion;
    
    @Column(name = "TIPO_FIRMA")
    private String tipoFirma;
    
    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "FECHA_STATUS")
    @Temporal(TemporalType.DATE)
    private Date fechaStatus;
    
    @Column(name = "USUARIO_ULT_MOD")
    private String usuarioUltMod;
    
    @Column(name = "FECHA_ULT_MOD")
    @Temporal(TemporalType.DATE)
    private Date fechaUltMod;
    
    @Column(name = "NO_CUENTA", insertable = false, updatable = false)
    @Basic(optional = false)
    private String noCuenta;
    
    @JoinColumn(name = "ID_PERSONA", referencedColumnName = "ID_PERSONA", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private BupPersona bupPersona;
    
    @Column(name = "ID_ROL", insertable = false, updatable = false)
    @Basic(optional = false)
    private String idRol;

    public BupPersonaCuentaRol() {
    }

    public BupPersonaCuentaRol(BupPersonaCuentaRolPK bupPersonaCuentaRolPK) {
        this.bupPersonaCuentaRolPK = bupPersonaCuentaRolPK;
    }

    public BupPersonaCuentaRol(BigInteger idPersona, String noCuenta, String idRol) {
        this.bupPersonaCuentaRolPK = new BupPersonaCuentaRolPK(idPersona, noCuenta, idRol);
    }

    public BupPersonaCuentaRolPK getBupPersonaCuentaRolPK() {
        return bupPersonaCuentaRolPK;
    }

    public void setBupPersonaCuentaRolPK(BupPersonaCuentaRolPK bupPersonaCuentaRolPK) {
        this.bupPersonaCuentaRolPK = bupPersonaCuentaRolPK;
    }

    public String getParticipacion() {
        return participacion;
    }

    public void setParticipacion(String participacion) {
        this.participacion = participacion;
    }

    public String getTipoFirma() {
        return tipoFirma;
    }

    public void setTipoFirma(String tipoFirma) {
        this.tipoFirma = tipoFirma;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getFechaStatus() {
        return fechaStatus;
    }

    public void setFechaStatus(Date fechaStatus) {
        this.fechaStatus = fechaStatus;
    }

    public String getUsuarioUltMod() {
        return usuarioUltMod;
    }

    public void setUsuarioUltMod(String usuarioUltMod) {
        this.usuarioUltMod = usuarioUltMod;
    }

    public Date getFechaUltMod() {
        return fechaUltMod;
    }

    public void setFechaUltMod(Date fechaUltMod) {
        this.fechaUltMod = fechaUltMod;
    }

    public String getNoCuenta() {
        return noCuenta;
    }

    public void setNoCuenta(String noCuenta) {
        this.noCuenta = noCuenta;
    }

    public BupPersona getBupPersona() {
        return bupPersona;
    }

    public void setBupPersona(BupPersona bupPersona) {
        this.bupPersona = bupPersona;
    }

    public String getIdRol() {
        return idRol;
    }

    public void setIdRol(String idRol) {
        this.idRol = idRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bupPersonaCuentaRolPK != null ? bupPersonaCuentaRolPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof BupPersonaCuentaRol)) {
            return false;
        }
        BupPersonaCuentaRol other = (BupPersonaCuentaRol) object;
        if ((this.bupPersonaCuentaRolPK == null && other.bupPersonaCuentaRolPK != null) ||
        		(this.bupPersonaCuentaRolPK != null && !this.bupPersonaCuentaRolPK.
        		equals(other.bupPersonaCuentaRolPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ixe.ods.sica.batch.domain.BupPersonaCuentaRol[ bupPersonaCuentaRolPK=" + 
        					bupPersonaCuentaRolPK + " ]";
    }
    
}

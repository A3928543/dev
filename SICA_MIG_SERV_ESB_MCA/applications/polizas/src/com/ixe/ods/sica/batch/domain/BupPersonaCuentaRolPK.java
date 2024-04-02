package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author IHernandez
 */
@Embeddable
public class BupPersonaCuentaRolPK implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
    @Column(name = "ID_PERSONA")
    private BigInteger idPersona;
	
    @Basic(optional = false)
    @Column(name = "NO_CUENTA")
    private String noCuenta;
    
    @Basic(optional = false)
    @Column(name = "ID_ROL")
    private String idRol;

    public BupPersonaCuentaRolPK() {
    }

    public BupPersonaCuentaRolPK(BigInteger idPersona, String noCuenta, String idRol) {
        this.idPersona = idPersona;
        this.noCuenta = noCuenta;
        this.idRol = idRol;
    }

    public BigInteger getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(BigInteger idPersona) {
        this.idPersona = idPersona;
    }

    public String getNoCuenta() {
        return noCuenta;
    }

    public void setNoCuenta(String noCuenta) {
        this.noCuenta = noCuenta;
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
        hash += (idPersona != null ? idPersona.hashCode() : 0);
        hash += (noCuenta != null ? noCuenta.hashCode() : 0);
        hash += (idRol != null ? idRol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BupPersonaCuentaRolPK)) {
            return false;
        }
        BupPersonaCuentaRolPK other = (BupPersonaCuentaRolPK) object;
        if ((this.idPersona == null && other.idPersona != null) || 
        		(this.idPersona != null && !this.idPersona.equals(other.idPersona))) {
            return false;
        }
        if ((this.noCuenta == null && other.noCuenta != null) || 
        		(this.noCuenta != null && !this.noCuenta.equals(other.noCuenta))) {
            return false;
        }
        if ((this.idRol == null && other.idRol != null) || 
        		(this.idRol != null && !this.idRol.equals(other.idRol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ixe.ods.sica.batch.domain.BupPersonaCuentaRolPK[ idPersona=" + idPersona + 
        		", noCuenta=" + noCuenta + ", idRol=" + idRol + " ]";
    }
    
}

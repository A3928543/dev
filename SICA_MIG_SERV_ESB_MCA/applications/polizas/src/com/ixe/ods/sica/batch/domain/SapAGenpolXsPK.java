package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SapAGenpolXsPK implements Serializable {
	
    private static final long serialVersionUID = 1L;

	@Basic(optional = false)
    @Column(name = "REFERENCIA")
    private String referencia;
    
    @Basic(optional = false)
    @Column(name = "ID_REGISTRO")
    private BigDecimal idRegistro;

    public SapAGenpolXsPK() {
    }

    public SapAGenpolXsPK(String referencia, BigDecimal idRegistro) {
        this.referencia = referencia;
        this.idRegistro = idRegistro;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public BigDecimal getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(BigDecimal idRegistro) {
        this.idRegistro = idRegistro;
    }

    public int hashCode() {
        int hash = 0;
        hash += (referencia != null ? referencia.hashCode() : 0);
        hash += (idRegistro != null ? idRegistro.hashCode() : 0);
        
        return hash;
    }

    public boolean equals(Object object) {
        if (!(object instanceof SapAGenpolXsPK)) {
            return false;
        }
        SapAGenpolXsPK other = (SapAGenpolXsPK) object;
        if ((this.referencia == null && other.referencia != null) || 
        		(this.referencia != null && !this.referencia.equals(other.referencia))) {
            return false;
        }
        if ((this.idRegistro == null && other.idRegistro != null) || 
        		(this.idRegistro != null && !this.idRegistro.equals(other.idRegistro))) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "com.ixe.ods.sica.batch.domain.SapAGenpolXsPK[ referencia=" + 
        						referencia + ", idRegistro=" + idRegistro + " ]";
    }
    
}

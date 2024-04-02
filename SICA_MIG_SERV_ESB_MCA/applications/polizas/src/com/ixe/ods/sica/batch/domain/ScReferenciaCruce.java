package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.DATE;

/**
 *
 * @author IHernandez
 */
@Entity
@Table(name = "SC_REFERENCIA_CRUCE")
@SequenceGenerator(name = "ScReferenciaCruceSeq", sequenceName = "SC_REFERENCIA_CRUCE_SEQ",
		allocationSize = 1)
public class ScReferenciaCruce implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID_REFERENCIA_CRUCE")
    @GeneratedValue(strategy = SEQUENCE, generator = "ScReferenciaCruceSeq")
    private Long idReferenciaCruce;
    
    @Basic(optional = false)
    @Column(name = "FECHA_CONT")
    @Temporal(DATE)
    private Date fechaCont;
    
    @Basic(optional = false)
    @Column(name = "REFERENCIA_CRUCE")
    private String referenciaCruce;
    
    @OneToMany(cascade = ALL, mappedBy = "scReferenciaCruce", fetch = LAZY)
    private Collection<ScReferenciaCruceDetalle> scReferenciaCruceDetalleCollection;

    public ScReferenciaCruce() {
    }

    public ScReferenciaCruce(Long idReferenciaCruce) {
        this.idReferenciaCruce = idReferenciaCruce;
    }

    public ScReferenciaCruce(Long idReferenciaCruce, Date fechaCont, 
    		String referenciaCruce) {
        this.idReferenciaCruce = idReferenciaCruce;
        this.fechaCont = fechaCont;
        this.referenciaCruce = referenciaCruce;
    }

    public Long getIdReferenciaCruce() {
        return idReferenciaCruce;
    }

    public void setIdReferenciaCruce(Long idReferenciaCruce) {
        this.idReferenciaCruce = idReferenciaCruce;
    }

    public Date getFechaCont() {
        return fechaCont;
    }

    public void setFechaCont(Date fechaCont) {
        this.fechaCont = fechaCont;
    }

    public String getReferenciaCruce() {
        return referenciaCruce;
    }

    public void setReferenciaCruce(String referenciaCruce) {
        this.referenciaCruce = referenciaCruce;
    }
    
    public Collection<ScReferenciaCruceDetalle> getScReferenciaCruceDetalleCollection() {
        return scReferenciaCruceDetalleCollection;
    }

    public void setScReferenciaCruceDetalleCollection(Collection<ScReferenciaCruceDetalle> det) {
        this.scReferenciaCruceDetalleCollection = det;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReferenciaCruce != null ? idReferenciaCruce.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ScReferenciaCruce)) {
            return false;
        }
        ScReferenciaCruce other = (ScReferenciaCruce) object;
        if ((this.idReferenciaCruce == null && 
        		other.idReferenciaCruce != null) || 
        		(this.idReferenciaCruce != null && !
        		this.idReferenciaCruce.equals(other.idReferenciaCruce))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ixe.ods.sica.batch.domain.ScReferenciaCruce[ idReferenciaCruce=" + 
        					idReferenciaCruce + " ]";
    }
    
}

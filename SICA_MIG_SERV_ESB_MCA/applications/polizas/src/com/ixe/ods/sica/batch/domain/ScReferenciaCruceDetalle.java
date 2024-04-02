package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 *
 * @author IHernandez
 */
@Entity
@Table(name = "SC_REFERENCIA_CRUCE_DETALLE")
@SequenceGenerator(name = "ScRefCruceDetalleSeq", sequenceName = "SC_REF_CRUCE_DETALLE_SEQ",
							allocationSize = 1)
public class ScReferenciaCruceDetalle implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    public static final Short STATUS_ACTIVO = 1;
    
    public static final Short STATUS_INACTIVO = 0;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID_REF_CRUCE_DETALLE")
    @GeneratedValue(strategy = SEQUENCE, generator = "ScRefCruceDetalleSeq")
    private Long idRefCruceDetalle;
    
    @Basic(optional = false)
    @Column(name = "FOLIO_FISCAL")
    private String folioFiscal;
    
    @Basic(optional = false)
    @Column(name = "REPROCESO")
    private String reproceso;
    
    @Basic(optional = false)
    @Column(name = "FECHA_ALTA")
    @Temporal(TIMESTAMP)
    private Date fechaAlta;
    
    @Basic(optional = false)
    @Column(name = "FECHA_ULT_MOD")
    @Temporal(TIMESTAMP)
    private Date fechaUltMod;
    
    @JoinColumn(name = "ID_REFERENCIA_CRUCE", referencedColumnName = "ID_REFERENCIA_CRUCE")
    @ManyToOne(optional = false, fetch = EAGER)
    private ScReferenciaCruce scReferenciaCruce;
    
    @Transient
    private String folioFiscalAnt;
    
    @Basic(optional = false)
    @Column(name = "ID_DETALLE_ANT")
    private Long idDetalleAnt;
    
    @Basic(optional = false)
    @Column(name = "STATUS")
    private Short status;

    public ScReferenciaCruceDetalle() {
    }

    public ScReferenciaCruceDetalle(Long idRefCruceDetalle) {
        this.idRefCruceDetalle = idRefCruceDetalle;
    }

    public ScReferenciaCruceDetalle(Long idRefCruceDetalle, String folioFiscal, 
    		String reproceso, Date fechaAlta, Date fechaUltMod) {
        this.idRefCruceDetalle = idRefCruceDetalle;
        this.folioFiscal = folioFiscal;
        this.reproceso = reproceso;
        this.fechaAlta = fechaAlta;
        this.fechaUltMod = fechaUltMod;
    }

    public Long getIdRefCruceDetalle() {
        return idRefCruceDetalle;
    }

    public void setIdRefCruceDetalle(Long idRefCruceDetalle) {
        this.idRefCruceDetalle = idRefCruceDetalle;
    }

    public String getFolioFiscal() {
        return folioFiscal;
    }

    public void setFolioFiscal(String folioFiscal) {
        this.folioFiscal = folioFiscal;
    }

    public String getReproceso() {
        return reproceso;
    }

    public void setReproceso(String reproceso) {
        this.reproceso = reproceso;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaUltMod() {
        return fechaUltMod;
    }

    public void setFechaUltMod(Date fechaUltMod) {
        this.fechaUltMod = fechaUltMod;
    }

    public ScReferenciaCruce getScReferenciaCruce() {
        return scReferenciaCruce;
    }

    public void setScReferenciaCruce(ScReferenciaCruce scRefCruce) {
        this.scReferenciaCruce = scRefCruce;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRefCruceDetalle != null ? idRefCruceDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ScReferenciaCruceDetalle)) {
            return false;
        }
        ScReferenciaCruceDetalle other = (ScReferenciaCruceDetalle) object;
        if ((this.idRefCruceDetalle == null && 
        		other.idRefCruceDetalle != null) || 
        		(this.idRefCruceDetalle != null && 
        		!this.idRefCruceDetalle.equals(other.idRefCruceDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ixe.ods.sica.batch.domain.ScReferenciaCruceDetalle[ idRefCruceDetalle=" + 
        					idRefCruceDetalle + " ]";
    }

	public String getFolioFiscalAnt() {
		return folioFiscalAnt;
	}

	public void setFolioFiscalAnt(String folioFiscalAnt) {
		this.folioFiscalAnt = folioFiscalAnt;
	}

	public Long getIdDetalleAnt() {
		return idDetalleAnt;
	}

	public void setIdDetalleAnt(Long idDetalleAnt) {
		this.idDetalleAnt = idDetalleAnt;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}
    
}

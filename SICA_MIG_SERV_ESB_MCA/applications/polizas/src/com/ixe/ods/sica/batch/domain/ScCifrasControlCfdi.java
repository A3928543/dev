package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * The Class ScCifrasControlCfdi.
 *
 * @author i.hernandez.chavez
 */
@Entity
@Table(name = "SC_CIFRAS_CONTROL_CFDI")
@SequenceGenerator(name = "ScCifrasControlCfdiSeq", sequenceName = "SC_CIFRAS_CONTROL_CFDI_SEQ",
allocationSize = 1)
public class ScCifrasControlCfdi implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L; 
    
    /** The id cifra control. */
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CIFRAS_CONTROL")
    @GeneratedValue(strategy = SEQUENCE, generator = "ScCifrasControlCfdiSeq")
    private Long idCifrasControl;
    
    /** The fecha recepcion. */
    @Basic(optional = false)
    @Column(name = "FECHA_RECEPCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRecepcion;
    
    /** The fecha recepcion str. */
    @Basic(optional = false)
    @Column(name = "FECHA_RECEPCION_STR")
    private String fechaRecepcionStr;
    
    /** The archivo cifras control. */
    @Column(name = "ARCHIVO_CIFRAS_CONTROL")
    private String archivoCifrasControl;
    
    /** The archivos cfdis faltantes. */
    @Column(name = "ARCHIVO_CFDIS_FALTANTES")
    private String archivosCfdisFaltantes;
    
    /** The cantidad recibidos sap. */
    @Basic(optional = false)
    @Column(name = "CANTIDAD_RECIBIDOS_SAP")
    private Integer cantidadRecibidosSap;
    
    /** The cantidad recibidos indices. */
    @Column(name = "CANTIDAD_RECIBIDOS_INDICES")
    private Integer cantidadRecibidosIndices;
    
    /** The estatus. */
    @Basic(optional = false)
    @Column(name = "ESTATUS")
    private short estatus;
    
    
    /** The reproceso. */
    @Basic(optional = false)
    @Column(name = "REPROCESO")
    private Integer reproceso;
    
    /** The fecha creacion. */
    @Basic(optional = false)
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    /** The fecha ult mod. */
    @Column(name = "FECHA_ULT_MOD")
    private Date fechaUltMod;

    /**
     * Instantiates a new sc cifras control cfdi.
     */
    public ScCifrasControlCfdi() {
    }

    /**
     * Instantiates a new sc cifras control cfdi.
     *
     * @param idCifrasControl the id cifra control
     */
    public ScCifrasControlCfdi(Long idCifraControl) {
        this.idCifrasControl = idCifraControl;
    }

    /**
     * Instantiates a new sc cifras control cfdi.
     *
     * @param idCifrasControl the id cifra control
     * @param fechaRecepcion the fecha recepcion
     * @param cantidadRecibidosSap the cantidad recibidos sap
     */
    public ScCifrasControlCfdi(Long idCifraControl, Date fechaRecepcion,
    		Integer cantidadRecibidosSap) {
        this.idCifrasControl = idCifraControl;
        this.fechaRecepcion = fechaRecepcion;
        this.cantidadRecibidosSap = cantidadRecibidosSap;
    }

    /**
     * Gets the id cifra control.
     *
     * @return the id cifra control
     */
    public Long getIdCifrasControl() {
        return idCifrasControl;
    }

    /**
     * Sets the id cifra control.
     *
     * @param idCifrasControl the new id cifra control
     */
    public void setIdCifrasControl(Long idCifraControl) {
        this.idCifrasControl = idCifraControl;
    }

    /**
     * Gets the fecha recepcion str.
     *
     * @return the fecha recepcion str
     */
    public String getFechaRecepcionStr() {
		return fechaRecepcionStr;
	}

	/**
	 * Sets the fecha recepcion str.
	 *
	 * @param fechaRecepcionStr the new fecha recepcion str
	 */
	public void setFechaRecepcionStr(String fechaRecepcionStr) {
		this.fechaRecepcionStr = fechaRecepcionStr;
	}

	/**
     * Gets the fecha recepcion.
     *
     * @return the fecha recepcion
     */
    public Date getFechaRecepcion() {
        return fechaRecepcion;
    }

    /**
     * Sets the fecha recepcion.
     *
     * @param fechaRecepcion the new fecha recepcion
     */
    public void setFechaRecepcion(Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    /**
     * Gets the archivo cifras control.
     *
     * @return the archivos cifras control
     */
    public String getArchivoCifrasControl() {
        return archivoCifrasControl;
    }
    
    /**
     * Sets the archivo cifras control.
     *
     * @param archivosCifrasControl the new archivos cifras control
     */
    public void setArchivoCifrasControl(String archivoCifrasControl) {
        this.archivoCifrasControl = archivoCifrasControl;
    }

    /**
     * Gets the archivos cfdis faltantes.
     *
     * @return the archivos cfdis faltantes
     */
    public String getArchivosCfdisFaltantes() {
		return archivosCfdisFaltantes;
	}

	/**
	 * Sets the archivos cfdis faltantes.
	 *
	 * @param archivosCfdisFaltantes the new archivos cfdis faltantes
	 */
	public void setArchivosCfdisFaltantes(String archivosCfdisFaltantes) {
		this.archivosCfdisFaltantes = archivosCfdisFaltantes;
	}

	/**
     * Gets the cantidad recibidos sap.
     *
     * @return the cantidad recibidos sap
     */
    public Integer getCantidadRecibidosSap() {
        return cantidadRecibidosSap;
    }

    /**
     * Sets the cantidad recibidos sap.
     *
     * @param cantidadRecibidosSap the new cantidad recibidos sap
     */
    public void setCantidadRecibidosSap(Integer cantidadRecibidosSap) {
        this.cantidadRecibidosSap = cantidadRecibidosSap;
    }

    /**
     * Gets the cantidad recibidos indices.
     *
     * @return the cantidad recibidos indices
     */
    public Integer getCantidadRecibidosIndices() {
        return cantidadRecibidosIndices;
    }

    /**
     * Sets the cantidad recibidos indices.
     *
     * @param cantidadRecibidosIndices the new cantidad recibidos indices
     */
    public void setCantidadRecibidosIndices(Integer cantidadRecibidosIndices) {
        this.cantidadRecibidosIndices = cantidadRecibidosIndices;
    }
    
    /**
     * Gets the estatus.
     *
     * @return the estatus
     */
    public short getEstatus() {
		return estatus;
	}

	/**
	 * Sets the estatus.
	 *
	 * @param estatus the new estatus
	 */
	public void setEstatus(short estatus) {
		this.estatus = estatus;
	}
	
	/**
	 * Gets the reproceso.
	 *
	 * @return the reproceso
	 */
	public Integer getReproceso() {
		return reproceso;
	}

	/**
	 * Sets the reproceso.
	 *
	 * @param reproceso the new reproceso
	 */
	public void setReproceso(Integer reproceso) {
		this.reproceso = reproceso;
	}    

	/**
	 * Gets the fecha creacion.
	 *
	 * @return the fecha creacion
	 */
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * Sets the fecha creacion.
	 *
	 * @param fechaCreacion the new fecha creacion
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * Gets the fecha ult mod.
	 *
	 * @return the fecha ult mod
	 */
	public Date getFechaUltMod() {
		return fechaUltMod;
	}

	/**
	 * Sets the fecha ult mod.
	 *
	 * @param fechaUltMod the new fecha ult mod
	 */
	public void setFechaUltMod(Date fechaUltMod) {
		this.fechaUltMod = fechaUltMod;
	}
	
	/**
	 * Checks if is reproceso.
	 *
	 * @return true, if is reproceso
	 */
	@Transient
	public boolean isReproceso() {
		return reproceso > 0;
	}
    
    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCifrasControl != null ? idCifrasControl.hashCode() : 0);
        return hash;
    }
    
    /**
     * Equals.
     *
     * @param object the object
     * @return true, if successful
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ScCifrasControlCfdi)) {
            return false;
        }
        ScCifrasControlCfdi other = (ScCifrasControlCfdi) object;
        if ((this.idCifrasControl == null && other.idCifrasControl != null) || 
        		(this.idCifrasControl != null && !this.idCifrasControl.equals(other.idCifrasControl))) {
            return false;
        }
        return true;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
	public String toString() {
		return "ScCifrasControlCfdi [idCifrasControl=" + idCifrasControl
				+ ", fechaRecepcion=" + fechaRecepcion + ", fechaRecepcionStr="
				+ fechaRecepcionStr + ", archivoCifrasControl="
				+ archivoCifrasControl + ", archivosCfdisFaltantes="
				+ archivosCfdisFaltantes + ", cantidadRecibidosSap="
				+ cantidadRecibidosSap + ", cantidadRecibidosIndices="
				+ cantidadRecibidosIndices + ", estatus=" + estatus
				+ ", reproceso=" + reproceso + ", fechaCreacion="
				+ fechaCreacion + ", fechaUltMod=" + fechaUltMod + "]";
	}
}

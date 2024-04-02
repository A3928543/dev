package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 *
 * @author IHernandez
 */
@Entity
@Table(name = "SC_LOTE_PROCESADO")
@SequenceGenerator(name = "ScLoteProcesadoSeq", 
				sequenceName = "SC_LOTE_PROCESADO_SEQ", 
				allocationSize = 1)
public class ScLoteProcesado implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ID_LOTE_PROCESADO", nullable = false)
    @GeneratedValue(strategy = SEQUENCE, generator = "ScLoteProcesadoSeq")
    private Long idLoteProcesado;
    
	@Column(name = "ID_CARGA", nullable = false)
	private Long idCarga;
    
    @Column(name = "TOT_CARGOS_MXN", nullable = false)
    private BigDecimal totCargosMxn;
    
    @Column(name = "TOT_ABONOS_MXN", nullable = false)
    private BigDecimal totAbonosMxn;
    
    @Column(name = "TOT_CARGOS_DIV", nullable = false)
    private BigDecimal totCargosDiv;
    
    @Column(name = "TOT_ABONOS_DIV", nullable = false)
    private BigDecimal totAbonosDiv;
    
    @Column(name = "ARCHIVO", nullable = false)
    private String archivo;
    
    @Temporal(TIMESTAMP)
    @Column(name = "FECHA_PROCESO", nullable = false)
    private Date fechaProceso;

    public ScLoteProcesado() {
    }

    public ScLoteProcesado(Long idLoteProcesado) {
        this.idLoteProcesado = idLoteProcesado;
    }

    public ScLoteProcesado(Long idLoteProcesado, BigDecimal totCargosMxn, 
    		               BigDecimal totAbonosMxn, BigDecimal totCargosDiv, 
    		               BigDecimal totAbonosDiv, String archivo, 
    		               Date fechaProceso) {
        this.idLoteProcesado = idLoteProcesado;
        this.totCargosMxn = totCargosMxn;
        this.totAbonosMxn = totAbonosMxn;
        this.totCargosDiv = totCargosDiv;
        this.totAbonosDiv = totAbonosDiv;
        this.archivo = archivo;
        this.fechaProceso = fechaProceso;
    }

    public Long getIdLoteProcesado() {
        return idLoteProcesado;
    }

    public void setIdLoteProcesado(Long idLoteProcesado) {
        this.idLoteProcesado = idLoteProcesado;
    }

    public Long getIdCarga() {
		return idCarga;
	}

	public void setIdCarga(Long idCarga) {
		this.idCarga = idCarga;
	}

	public BigDecimal getTotCargosMxn() {
        return totCargosMxn;
    }

    public void setTotCargosMxn(BigDecimal totCargosMxn) {
        this.totCargosMxn = totCargosMxn;
    }

    public BigDecimal getTotAbonosMxn() {
        return totAbonosMxn;
    }

    public void setTotAbonosMxn(BigDecimal totAbonosMxn) {
        this.totAbonosMxn = totAbonosMxn;
    }

    public BigDecimal getTotCargosDiv() {
        return totCargosDiv;
    }

    public void setTotCargosDiv(BigDecimal totCargosDiv) {
        this.totCargosDiv = totCargosDiv;
    }

    public BigDecimal getTotAbonosDiv() {
        return totAbonosDiv;
    }

    public void setTotAbonosDiv(BigDecimal totAbonosDiv) {
        this.totAbonosDiv = totAbonosDiv;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Date getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(Date fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLoteProcesado != null ? idLoteProcesado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ScLoteProcesado)) {
            return false;
        }
        ScLoteProcesado other = (ScLoteProcesado) object;
        if ((this.idLoteProcesado == null && 
        		other.idLoteProcesado != null) || 
        		(this.idLoteProcesado != null && 
        		!this.idLoteProcesado.equals(other.idLoteProcesado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ixe.ods.sica.batch.domain.ScLoteProcesado[ " + 
        			"idLoteProcesado=" + idLoteProcesado + " ]";
    }
    
}

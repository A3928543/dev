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
@Table(name = "BUP_PERSONA")
public class BupPersona implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PERSONA")
    private BigInteger idPersona;
    
    @Column(name = "RFC")
    private String rfc;
    
    @Column(name = "NOMBRE_CORTO")
    private String nombreCorto;
    
    @Column(name = "NOMBRE_COMERCIAL")
    private String nombreComercial;
    
    @Column(name = "ACTIVIDAD_GIRO")
    private String actividadGiro;
    
    @Column(name = "MANEJO_IMPUESTOS")
    private String manejoImpuestos;
    
    @Column(name = "NIP")
    private String nip;
    
    @Column(name = "FECHA_ALTA")
    @Temporal(TemporalType.DATE)
    private Date fechaAlta;
    
    @Column(name = "USUARIO_ULT_MOD")
    private String usuarioUltMod;
    
    @Column(name = "FECHA_ULT_MOD")
    @Temporal(TemporalType.DATE)
    private Date fechaUltMod;
    
    @Column(name = "ALTO_RIESGO")
    private Character altoRiesgo;
    
    @Column(name = "FECHA_REGISTRO_ALTO_RIESGO")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistroAltoRiesgo;
    
    @Basic(optional = false)
    @Column(name = "B_ILYTICS")
    private BigInteger bIlytics;
    
    @Column(name = "SINCRONIZACION")
    @Temporal(TemporalType.DATE)
    private Date sincronizacion;
    
    @Column(name = "VERIFICA_FE")
    private Short verificaFe;
    
    @Basic(optional = false)
    @Column(name = "TIPO_PERSONA")
    private String tipoPersona;

    public BupPersona() {
    }

    public BupPersona(BigInteger idPersona) {
        this.idPersona = idPersona;
    }

    public BupPersona(BigInteger idPersona, BigInteger bIlytics) {
        this.idPersona = idPersona;
        this.bIlytics = bIlytics;
    }

    public BigInteger getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(BigInteger idPersona) {
        this.idPersona = idPersona;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getActividadGiro() {
        return actividadGiro;
    }

    public void setActividadGiro(String actividadGiro) {
        this.actividadGiro = actividadGiro;
    }

    public String getManejoImpuestos() {
        return manejoImpuestos;
    }

    public void setManejoImpuestos(String manejoImpuestos) {
        this.manejoImpuestos = manejoImpuestos;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
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

    public Character getAltoRiesgo() {
        return altoRiesgo;
    }

    public void setAltoRiesgo(Character altoRiesgo) {
        this.altoRiesgo = altoRiesgo;
    }

    public Date getFechaRegistroAltoRiesgo() {
        return fechaRegistroAltoRiesgo;
    }

    public void setFechaRegistroAltoRiesgo(Date fechaRegistroAltoRiesgo) {
        this.fechaRegistroAltoRiesgo = fechaRegistroAltoRiesgo;
    }

    public BigInteger getBIlytics() {
        return bIlytics;
    }

    public void setBIlytics(BigInteger bIlytics) {
        this.bIlytics = bIlytics;
    }

    public Date getSincronizacion() {
        return sincronizacion;
    }

    public void setSincronizacion(Date sincronizacion) {
        this.sincronizacion = sincronizacion;
    }

    public Short getVerificaFe() {
        return verificaFe;
    }

    public void setVerificaFe(Short verificaFe) {
        this.verificaFe = verificaFe;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPersona != null ? idPersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BupPersona)) {
            return false;
        }
        BupPersona other = (BupPersona) object;
        if ((this.idPersona == null && other.idPersona != null) || 
        		(this.idPersona != null && !this.idPersona.equals(other.idPersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ixe.ods.sica.batch.domain.BupPersona[ idPersona=" + idPersona + " ]";
    }
    
}

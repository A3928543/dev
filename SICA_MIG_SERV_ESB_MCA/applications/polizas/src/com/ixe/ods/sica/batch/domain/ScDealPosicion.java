package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author IHernandez
 */
@Entity
@Table(name = "SC_DEAL_POSICION")
public class ScDealPosicion implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID_DEAL_POSICION")
    private BigInteger idDealPosicion;
    
    @Basic(optional = false)
    @Column(name = "ID_DIVISA")
    private String idDivisa;
    
    @Basic(optional = false)
    @Column(name = "ID_MESA_CAMBIO")
    private BigInteger idMesaCambio;
    
    @Basic(optional = false)
    @Column(name = "ID_USUARIO")
    private BigInteger idUsuario;
    
    @Basic(optional = false)
    @Column(name = "MONTO")
    private BigDecimal monto;
    
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    
    @Basic(optional = false)
    @Column(name = "RECIBIMOS")
    private Character recibimos;
    
    @Basic(optional = false)
    @Column(name = "TIPO_CAMBIO")
    private BigDecimal tipoCambio;
    
    @Basic(optional = false)
    @Column(name = "TIPO_DEAL")
    private Character tipoDeal;
    
    @Column(name = "VERSION")
    private BigInteger version;

    public ScDealPosicion() {
    }

    public ScDealPosicion(BigInteger idDealPosicion) {
        this.idDealPosicion = idDealPosicion;
    }

    public ScDealPosicion(BigInteger idDealPosicion, String idDivisa, BigInteger idMesaCambio,
    		BigInteger idUsuario, BigDecimal monto, Character recibimos, BigDecimal tipoCambio,
    		Character tipoDeal) {
        this.idDealPosicion = idDealPosicion;
        this.idDivisa = idDivisa;
        this.idMesaCambio = idMesaCambio;
        this.idUsuario = idUsuario;
        this.monto = monto;
        this.recibimos = recibimos;
        this.tipoCambio = tipoCambio;
        this.tipoDeal = tipoDeal;
    }

    public BigInteger getIdDealPosicion() {
        return idDealPosicion;
    }

    public void setIdDealPosicion(BigInteger idDealPosicion) {
        this.idDealPosicion = idDealPosicion;
    }

    public String getIdDivisa() {
        return idDivisa;
    }

    public void setIdDivisa(String idDivisa) {
        this.idDivisa = idDivisa;
    }

    public BigInteger getIdMesaCambio() {
        return idMesaCambio;
    }

    public void setIdMesaCambio(BigInteger idMesaCambio) {
        this.idMesaCambio = idMesaCambio;
    }

    public BigInteger getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Character getRecibimos() {
        return recibimos;
    }

    public void setRecibimos(Character recibimos) {
        this.recibimos = recibimos;
    }

    public BigDecimal getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(BigDecimal tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public Character getTipoDeal() {
        return tipoDeal;
    }

    public void setTipoDeal(Character tipoDeal) {
        this.tipoDeal = tipoDeal;
    }

    public BigInteger getVersion() {
        return version;
    }

    public void setVersion(BigInteger version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDealPosicion != null ? idDealPosicion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ScDealPosicion)) {
            return false;
        }
        ScDealPosicion other = (ScDealPosicion) object;
        if ((this.idDealPosicion == null && other.idDealPosicion != null) ||
        		(this.idDealPosicion != null && 
        		 !this.idDealPosicion.equals(other.idDealPosicion))) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "com.ixe.ods.sica.batch.domain.ScDealPosicion[ idDealPosicion=" + 
        				idDealPosicion + " ]";
    }
    
}

package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

/**
 *
 * @author IHernandez
 */
@Entity
@Table(name = "SC_DEAL_DETALLE")
public class ScDealDetalle implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID_DEAL_POSICION")
    private BigInteger idDealPosicion;
    
    @Column(name = "CLAVE_FORMA_LIQUIDACION")
    private String claveFormaLiquidacion;
    
    @Column(name = "DENOMINACION")
    private BigDecimal denominacion;
    
    @Column(name = "COSTO_FORMA_LIQ")
    private BigDecimal costoFormaLiq;
    
    @Basic(optional = false)
    @Column(name = "EVENTOS_DETALLE_DEAL")
    private String eventosDetalleDeal;
    
    @Basic(optional = false)
    @Column(name = "FOLIO_DETALLE")
    private short folioDetalle;
    
    @Column(name = "ID_FACTOR_DIVISA")
    private BigInteger idFactorDivisa;
    
    @Column(name = "ID_PRECIO_REFERENCIA")
    private BigInteger idPrecioReferencia;
    
    @Basic(optional = false)
    @Column(name = "ID_SPREAD")
    private BigInteger idSpread;
    
    @Column(name = "ID_LIQUIDACION_DETALLE")
    private BigInteger idLiquidacionDetalle;
    
    @Column(name = "INSTRUCCIONES_BENEFICIARIO")
    private String instruccionesBeneficiario;
    
    @Column(name = "INSTRUCCIONES_INTERMEDIARIO")
    private String instruccionesIntermediario;
    
    @Column(name = "INSTRUCCIONES_PAGADOR")
    private String instruccionesPagador;
    
    @Column(name = "MNEMONICO")
    private String mnemonico;
    
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    
    @Basic(optional = false)
    @Column(name = "STATUS_DETALLE_DEAL")
    private String statusDetalleDeal;
    
    @Basic(optional = false)
    @Column(name = "TIPO_CAMBIO_MESA")
    private BigDecimal tipoCambioMesa;
    
    @Basic(optional = false)
    @Column(name = "COMISION_OFICIAL_USD")
    private BigDecimal comisionOficialUsd;
    
    @Basic(optional = false)
    @Column(name = "COMISION_COBRADA_USD")
    private BigDecimal comisionCobradaUsd;
    
    @Basic(optional = false)
    @Column(name = "COMISION_COBRADA_MXN")
    private BigDecimal comisionCobradaMxn;
    
    @Column(name = "REEMPLAZADO_POR_A")
    private BigInteger reemplazadoPorA;
    
    @Column(name = "SUSTITUYE_A")
    private BigInteger sustituyeA;
    
    @Column(name = "REEMPLAZADO_POR_B")
    private BigInteger reemplazadoPorB;
    
    @Column(name = "REVERSADO")
    private BigInteger reversado;
    
    @Column(name = "DATOS_CONFIRMACION")
    private String datosConfirmacion;
    
    @Column(name = "PRE_REF_MID_SPOT")
    private BigDecimal preRefMidSpot;
    
    @Column(name = "PRE_REF_SPOT")
    private BigDecimal preRefSpot;
    
    @Column(name = "FACTOR_DIVISA")
    private BigDecimal factorDivisa;
    
    @Column(name = "FED")
    private String fed;
    
    @Column(name = "MSG_MT")
    private String msgMt;
    
    @JoinColumn(name = "ID_DEAL", referencedColumnName = "ID_DEAL")
    @ManyToOne(optional = false, fetch = LAZY)
    private ScDeal scDeal;
    
    @JoinColumn(name = "ID_DEAL_POSICION", referencedColumnName = "ID_DEAL_POSICION", 
    		insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = EAGER)
    private ScDealPosicion scDealPosicion;
    
    @Basic(optional = false)
    @Column(name = "ID_PLANTILLA")
    private BigDecimal idPlantilla;

    public ScDealDetalle() {
    }

    public ScDealDetalle(BigInteger idDealPosicion) {
        this.idDealPosicion = idDealPosicion;
    }

    public ScDealDetalle(BigInteger idDealPosicion, String eventosDetalleDeal, 
    		short folioDetalle, BigInteger idSpread, String statusDetalleDeal, 
    		BigDecimal tipoCambioMesa, BigDecimal comisionOficialUsd, 
    		BigDecimal comisionCobradaUsd, BigDecimal comisionCobradaMxn) {
        this.idDealPosicion = idDealPosicion;
        this.eventosDetalleDeal = eventosDetalleDeal;
        this.folioDetalle = folioDetalle;
        this.idSpread = idSpread;
        this.statusDetalleDeal = statusDetalleDeal;
        this.tipoCambioMesa = tipoCambioMesa;
        this.comisionOficialUsd = comisionOficialUsd;
        this.comisionCobradaUsd = comisionCobradaUsd;
        this.comisionCobradaMxn = comisionCobradaMxn;
    }

    public BigInteger getIdDealPosicion() {
        return idDealPosicion;
    }

    public void setIdDealPosicion(BigInteger idDealPosicion) {
        this.idDealPosicion = idDealPosicion;
    }

    public String getClaveFormaLiquidacion() {
        return claveFormaLiquidacion;
    }

    public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
        this.claveFormaLiquidacion = claveFormaLiquidacion;
    }

    public BigDecimal getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(BigDecimal denominacion) {
        this.denominacion = denominacion;
    }

    public BigDecimal getCostoFormaLiq() {
        return costoFormaLiq;
    }

    public void setCostoFormaLiq(BigDecimal costoFormaLiq) {
        this.costoFormaLiq = costoFormaLiq;
    }

    public String getEventosDetalleDeal() {
        return eventosDetalleDeal;
    }

    public void setEventosDetalleDeal(String eventosDetalleDeal) {
        this.eventosDetalleDeal = eventosDetalleDeal;
    }

    public short getFolioDetalle() {
        return folioDetalle;
    }

    public void setFolioDetalle(short folioDetalle) {
        this.folioDetalle = folioDetalle;
    }

    public BigInteger getIdFactorDivisa() {
        return idFactorDivisa;
    }

    public void setIdFactorDivisa(BigInteger idFactorDivisa) {
        this.idFactorDivisa = idFactorDivisa;
    }

    public BigInteger getIdPrecioReferencia() {
        return idPrecioReferencia;
    }

    public void setIdPrecioReferencia(BigInteger idPrecioReferencia) {
        this.idPrecioReferencia = idPrecioReferencia;
    }

    public BigInteger getIdSpread() {
        return idSpread;
    }

    public void setIdSpread(BigInteger idSpread) {
        this.idSpread = idSpread;
    }

    public BigInteger getIdLiquidacionDetalle() {
        return idLiquidacionDetalle;
    }

    public void setIdLiquidacionDetalle(BigInteger idLiquidacionDetalle) {
        this.idLiquidacionDetalle = idLiquidacionDetalle;
    }

    public String getInstruccionesBeneficiario() {
        return instruccionesBeneficiario;
    }

    public void setInstruccionesBeneficiario(String instruccionesBeneficiario) {
        this.instruccionesBeneficiario = instruccionesBeneficiario;
    }

    public String getInstruccionesIntermediario() {
        return instruccionesIntermediario;
    }

    public void setInstruccionesIntermediario(String instruccionesIntermediario) {
        this.instruccionesIntermediario = instruccionesIntermediario;
    }

    public String getInstruccionesPagador() {
        return instruccionesPagador;
    }

    public void setInstruccionesPagador(String instruccionesPagador) {
        this.instruccionesPagador = instruccionesPagador;
    }

    public String getMnemonico() {
        return mnemonico;
    }

    public void setMnemonico(String mnemonico) {
        this.mnemonico = mnemonico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getStatusDetalleDeal() {
        return statusDetalleDeal;
    }

    public void setStatusDetalleDeal(String statusDetalleDeal) {
        this.statusDetalleDeal = statusDetalleDeal;
    }

    public BigDecimal getTipoCambioMesa() {
        return tipoCambioMesa;
    }

    public void setTipoCambioMesa(BigDecimal tipoCambioMesa) {
        this.tipoCambioMesa = tipoCambioMesa;
    }

    public BigDecimal getComisionOficialUsd() {
        return comisionOficialUsd;
    }

    public void setComisionOficialUsd(BigDecimal comisionOficialUsd) {
        this.comisionOficialUsd = comisionOficialUsd;
    }

    public BigDecimal getComisionCobradaUsd() {
        return comisionCobradaUsd;
    }

    public void setComisionCobradaUsd(BigDecimal comisionCobradaUsd) {
        this.comisionCobradaUsd = comisionCobradaUsd;
    }

    public BigDecimal getComisionCobradaMxn() {
        return comisionCobradaMxn;
    }

    public void setComisionCobradaMxn(BigDecimal comisionCobradaMxn) {
        this.comisionCobradaMxn = comisionCobradaMxn;
    }

    public BigInteger getReemplazadoPorA() {
        return reemplazadoPorA;
    }

    public void setReemplazadoPorA(BigInteger reemplazadoPorA) {
        this.reemplazadoPorA = reemplazadoPorA;
    }

    public BigInteger getSustituyeA() {
        return sustituyeA;
    }

    public void setSustituyeA(BigInteger sustituyeA) {
        this.sustituyeA = sustituyeA;
    }

    public BigInteger getReemplazadoPorB() {
        return reemplazadoPorB;
    }

    public void setReemplazadoPorB(BigInteger reemplazadoPorB) {
        this.reemplazadoPorB = reemplazadoPorB;
    }

    public BigInteger getReversado() {
        return reversado;
    }

    public void setReversado(BigInteger reversado) {
        this.reversado = reversado;
    }

    public String getDatosConfirmacion() {
        return datosConfirmacion;
    }

    public void setDatosConfirmacion(String datosConfirmacion) {
        this.datosConfirmacion = datosConfirmacion;
    }

    public BigDecimal getPreRefMidSpot() {
        return preRefMidSpot;
    }

    public void setPreRefMidSpot(BigDecimal preRefMidSpot) {
        this.preRefMidSpot = preRefMidSpot;
    }

    public BigDecimal getPreRefSpot() {
        return preRefSpot;
    }

    public void setPreRefSpot(BigDecimal preRefSpot) {
        this.preRefSpot = preRefSpot;
    }

    public BigDecimal getFactorDivisa() {
        return factorDivisa;
    }

    public void setFactorDivisa(BigDecimal factorDivisa) {
        this.factorDivisa = factorDivisa;
    }

    public String getFed() {
        return fed;
    }

    public void setFed(String fed) {
        this.fed = fed;
    }

    public String getMsgMt() {
        return msgMt;
    }

    public void setMsgMt(String msgMt) {
        this.msgMt = msgMt;
    }

    public ScDeal getScDeal() {
        return scDeal;
    }

    public void setScDeal(ScDeal scDeal) {
        this.scDeal = scDeal;
    }

    public ScDealPosicion getScDealPosicion() {
        return scDealPosicion;
    }

    public void setScDealPosicion(ScDealPosicion scDealPosicion) {
        this.scDealPosicion = scDealPosicion;
    }

    public BigDecimal getIdPlantilla() {
        return idPlantilla;
    }

    public void setIdPlantilla(BigDecimal idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDealPosicion != null ? idDealPosicion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ScDealDetalle)) {
            return false;
        }
        ScDealDetalle other = (ScDealDetalle) object;
        if ((this.idDealPosicion == null && 
        		other.idDealPosicion != null) || 
        		(this.idDealPosicion != null && 
        		!this.idDealPosicion.equals(other.idDealPosicion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ixe.ods.sica.batch.domain.ScDealDetalle[ idDealPosicion=" + 
        					idDealPosicion + " ]";
    }
    
}

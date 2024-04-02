
package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "TES_DETALLE_LIQUIDACION")
public class TesDetalleLiquidacion implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID_DETALLE_LIQUIDACION")
    private BigInteger idDetalleLiquidacion;
    
    @Column(name = "ID_PERSONA_BENEFICIARIO")
    private BigInteger idPersonaBeneficiario;
    
    @Basic(optional = false)
    @Column(name = "ID_MATRIZ")
    private BigInteger idMatriz;
    
    @Column(name = "NO_DOCUMENTO_CLIENTE")
    private String noDocumentoCliente;
    
    @Basic(optional = false)
    @Column(name = "MONTO_MONEDA_NACIONAL")
    private BigDecimal montoMonedaNacional;
    
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    
    @Column(name = "FECHA_CREACION")
    @Temporal(DATE)
    private Date fechaCreacion;
    
    @Column(name = "FECHA_ULTIMA_MODIFICACION")
    @Temporal(DATE)
    private Date fechaUltimaModificacion;
    
    @Column(name = "NO_CUENTA_CLIENTE")
    private String noCuentaCliente;
    
    @Column(name = "ID_BANCO_CLIENTE")
    private BigInteger idBancoCliente;
    
    @Column(name = "FECHA_LIQUIDADO")
    @Temporal(DATE)
    private Date fechaLiquidado;
    
    @Column(name = "USA_MENSAJERIA")
    private Short usaMensajeria;
    
    @Column(name = "MONTO_DIVISA")
    private BigDecimal montoDivisa;
    
    @Column(name = "DIAS_VENCIMIENTO")
    private Short diasVencimiento;
    
    @Column(name = "TIPO_CAMBIO")
    private BigDecimal tipoCambio;
    
    @Column(name = "MARCADO_CANCELACION")
    private Short marcadoCancelacion;
    
    @Column(name = "INSTRUCCIONES_BANCO_BENEF")
    private String instruccionesBancoBenef;
    
    @Column(name = "INSTRUCCIONES_BENEFICIARIO")
    private String instruccionesBeneficiario;
    
    @Column(name = "FOLIO_FACTURA_COMPROBANTE")
    private Long folioFacturaComprobante;
    
    @Column(name = "ARCHIVO_BATCH")
    private String archivoBatch;
    
    @Column(name = "INSTRUCCIONES_BANCO_INTERM")
    private String instruccionesBancoInterm;
    
    @Column(name = "COMISION_CONCERTADA")
    private BigDecimal comisionConcertada;
    
    @Column(name = "PORCENTAJE_IVA")
    private BigDecimal porcentajeIva;
    
    @Column(name = "ID_PERSONA_MODIFICA")
    private BigInteger idPersonaModifica;
    
    @Column(name = "STATUS_ANTERIOR")
    private String statusAnterior;
    
    @Column(name = "TIPO_CLAVE_BANCO_CLIENTE")
    private String tipoClaveBancoCliente;
    
    @Column(name = "TIPO_CLAVE_BANCO_INTERMEDIARIO")
    private String tipoClaveBancoIntermediario;
    
    @Column(name = "NOMBRE_BENEFICIARIO")
    private String nombreBeneficiario;
    
    @Column(name = "CLAVE_RASTREO_SPEI")
    private String claveRastreoSpei;
    
    @Column(name = "CONTRAPARTE_LIQUIDADA")
    private Short contraparteLiquidada;
    
    @Column(name = "CUENTA_BANCO_INTERM")
    private String cuentaBancoInterm;
    
    @Column(name = "NOMBRE_BANCO_INTERNACIONAL")
    private String nombreBancoInternacional;
    
    @Column(name = "VERSION")
    private BigInteger version;
    
    @Column(name = "FOLIO_DETALLE")
    private Short folioDetalle;
    
    @Column(name = "ID_DETALLE_EXT")
    private BigInteger idDetalleExt;
    
    @Column(name = "MEDIO_DISTRIBUCION")
    private Short medioDistribucion;
    
    @Column(name = "ESTADO_FACTURACION")
    private Short estadoFacturacion;
    
    @Column(name = "SUSTITUYE_A")
    private BigInteger sustituyeA;
    
    @Column(name = "FECHA_ENVIO_REAL")
    @Temporal(DATE)
    private Date fechaEnvioReal;
    
    @Column(name = "REFERENCIACBCT")
    private String referenciacbct;
    
    @Column(name = "PROC_PHOENIX")
    private Short procPhoenix;
    
    @Column(name = "NUMFOLIOINTERNO")
    private BigInteger numfoliointerno;
    
    @Column(name = "TRANID")
    private String tranid;
    
    @Column(name = "NUMTASK")
    private String numtask;
    
    @Column(name = "NUMMOVCARGOABONO")
    private BigInteger nummovcargoabono;
    
    @Column(name = "METODOPAGOCFD")
    private String metodopagocfd;
    
    @Column(name = "CUENTAPAGOCFD")
    private String cuentapagocfd;
    
    @Column(name = "ESTATUS_MEDIADOR")
    private String estatusMediador;
    
    @Column(name = "MSG_MT")
    private String msgMt;
    
    @Column(name = "FED")
    private String fed;
    
    @Column(name = "ENVIADO_MEDIADOR")
    private Short enviadoMediador;
    
    @Column(name = "FOLIO_MEDIADOR")
    private BigInteger folioMediador;

    public TesDetalleLiquidacion() {
    }

    public TesDetalleLiquidacion(BigInteger idDetalleLiquidacion) {
        this.idDetalleLiquidacion = idDetalleLiquidacion;
    }

    public TesDetalleLiquidacion(BigInteger idDetalleLiquidacion, BigInteger idMatriz,
    		BigDecimal montoMonedaNacional, String status) {
        this.idDetalleLiquidacion = idDetalleLiquidacion;
        this.idMatriz = idMatriz;
        this.montoMonedaNacional = montoMonedaNacional;
        this.status = status;
    }

    public BigInteger getIdDetalleLiquidacion() {
        return idDetalleLiquidacion;
    }

    public void setIdDetalleLiquidacion(BigInteger idDetalleLiquidacion) {
        this.idDetalleLiquidacion = idDetalleLiquidacion;
    }

    public BigInteger getIdPersonaBeneficiario() {
        return idPersonaBeneficiario;
    }

    public void setIdPersonaBeneficiario(BigInteger idPersonaBeneficiario) {
        this.idPersonaBeneficiario = idPersonaBeneficiario;
    }

    public BigInteger getIdMatriz() {
        return idMatriz;
    }

    public void setIdMatriz(BigInteger idMatriz) {
        this.idMatriz = idMatriz;
    }

    public String getNoDocumentoCliente() {
        return noDocumentoCliente;
    }

    public void setNoDocumentoCliente(String noDocumentoCliente) {
        this.noDocumentoCliente = noDocumentoCliente;
    }

    public BigDecimal getMontoMonedaNacional() {
        return montoMonedaNacional;
    }

    public void setMontoMonedaNacional(BigDecimal montoMonedaNacional) {
        this.montoMonedaNacional = montoMonedaNacional;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    public void setFechaUltimaModificacion(Date fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

    public String getNoCuentaCliente() {
        return noCuentaCliente;
    }

    public void setNoCuentaCliente(String noCuentaCliente) {
        this.noCuentaCliente = noCuentaCliente;
    }

    public BigInteger getIdBancoCliente() {
        return idBancoCliente;
    }

    public void setIdBancoCliente(BigInteger idBancoCliente) {
        this.idBancoCliente = idBancoCliente;
    }

    public Date getFechaLiquidado() {
        return fechaLiquidado;
    }

    public void setFechaLiquidado(Date fechaLiquidado) {
        this.fechaLiquidado = fechaLiquidado;
    }

    public Short getUsaMensajeria() {
        return usaMensajeria;
    }

    public void setUsaMensajeria(Short usaMensajeria) {
        this.usaMensajeria = usaMensajeria;
    }

    public BigDecimal getMontoDivisa() {
        return montoDivisa;
    }

    public void setMontoDivisa(BigDecimal montoDivisa) {
        this.montoDivisa = montoDivisa;
    }

    public Short getDiasVencimiento() {
        return diasVencimiento;
    }

    public void setDiasVencimiento(Short diasVencimiento) {
        this.diasVencimiento = diasVencimiento;
    }

    public BigDecimal getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(BigDecimal tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public Short getMarcadoCancelacion() {
        return marcadoCancelacion;
    }

    public void setMarcadoCancelacion(Short marcadoCancelacion) {
        this.marcadoCancelacion = marcadoCancelacion;
    }

    public String getInstruccionesBancoBenef() {
        return instruccionesBancoBenef;
    }

    public void setInstruccionesBancoBenef(String instruccionesBancoBenef) {
        this.instruccionesBancoBenef = instruccionesBancoBenef;
    }

    public String getInstruccionesBeneficiario() {
        return instruccionesBeneficiario;
    }

    public void setInstruccionesBeneficiario(String instruccionesBeneficiario) {
        this.instruccionesBeneficiario = instruccionesBeneficiario;
    }

    public Long getFolioFacturaComprobante() {
        return folioFacturaComprobante;
    }

    public void setFolioFacturaComprobante(Long folioFacturaComprobante) {
        this.folioFacturaComprobante = folioFacturaComprobante;
    }

    public String getArchivoBatch() {
        return archivoBatch;
    }

    public void setArchivoBatch(String archivoBatch) {
        this.archivoBatch = archivoBatch;
    }

    public String getInstruccionesBancoInterm() {
        return instruccionesBancoInterm;
    }

    public void setInstruccionesBancoInterm(String instruccionesBancoInterm) {
        this.instruccionesBancoInterm = instruccionesBancoInterm;
    }

    public BigDecimal getComisionConcertada() {
        return comisionConcertada;
    }

    public void setComisionConcertada(BigDecimal comisionConcertada) {
        this.comisionConcertada = comisionConcertada;
    }

    public BigDecimal getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(BigDecimal porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public BigInteger getIdPersonaModifica() {
        return idPersonaModifica;
    }

    public void setIdPersonaModifica(BigInteger idPersonaModifica) {
        this.idPersonaModifica = idPersonaModifica;
    }

    public String getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(String statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public String getTipoClaveBancoCliente() {
        return tipoClaveBancoCliente;
    }

    public void setTipoClaveBancoCliente(String tipoClaveBancoCliente) {
        this.tipoClaveBancoCliente = tipoClaveBancoCliente;
    }

    public String getTipoClaveBancoIntermediario() {
        return tipoClaveBancoIntermediario;
    }

    public void setTipoClaveBancoIntermediario(String tipoClaveBancoIntermediario) {
        this.tipoClaveBancoIntermediario = tipoClaveBancoIntermediario;
    }

    public String getNombreBeneficiario() {
        return nombreBeneficiario;
    }

    public void setNombreBeneficiario(String nombreBeneficiario) {
        this.nombreBeneficiario = nombreBeneficiario;
    }

    public String getClaveRastreoSpei() {
        return claveRastreoSpei;
    }

    public void setClaveRastreoSpei(String claveRastreoSpei) {
        this.claveRastreoSpei = claveRastreoSpei;
    }

    public Short getContraparteLiquidada() {
        return contraparteLiquidada;
    }

    public void setContraparteLiquidada(Short contraparteLiquidada) {
        this.contraparteLiquidada = contraparteLiquidada;
    }

    public String getCuentaBancoInterm() {
        return cuentaBancoInterm;
    }

    public void setCuentaBancoInterm(String cuentaBancoInterm) {
        this.cuentaBancoInterm = cuentaBancoInterm;
    }

    public String getNombreBancoInternacional() {
        return nombreBancoInternacional;
    }

    public void setNombreBancoInternacional(String nombreBancoInternacional) {
        this.nombreBancoInternacional = nombreBancoInternacional;
    }

    public BigInteger getVersion() {
        return version;
    }

    public void setVersion(BigInteger version) {
        this.version = version;
    }

    public Short getFolioDetalle() {
        return folioDetalle;
    }

    public void setFolioDetalle(Short folioDetalle) {
        this.folioDetalle = folioDetalle;
    }

    public BigInteger getIdDetalleExt() {
        return idDetalleExt;
    }

    public void setIdDetalleExt(BigInteger idDetalleExt) {
        this.idDetalleExt = idDetalleExt;
    }

    public Short getMedioDistribucion() {
        return medioDistribucion;
    }

    public void setMedioDistribucion(Short medioDistribucion) {
        this.medioDistribucion = medioDistribucion;
    }

    public Short getEstadoFacturacion() {
        return estadoFacturacion;
    }

    public void setEstadoFacturacion(Short estadoFacturacion) {
        this.estadoFacturacion = estadoFacturacion;
    }

    public BigInteger getSustituyeA() {
        return sustituyeA;
    }

    public void setSustituyeA(BigInteger sustituyeA) {
        this.sustituyeA = sustituyeA;
    }

    public Date getFechaEnvioReal() {
        return fechaEnvioReal;
    }

    public void setFechaEnvioReal(Date fechaEnvioReal) {
        this.fechaEnvioReal = fechaEnvioReal;
    }

    public String getReferenciacbct() {
        return referenciacbct;
    }

    public void setReferenciacbct(String referenciacbct) {
        this.referenciacbct = referenciacbct;
    }

    public Short getProcPhoenix() {
        return procPhoenix;
    }

    public void setProcPhoenix(Short procPhoenix) {
        this.procPhoenix = procPhoenix;
    }

    public BigInteger getNumfoliointerno() {
        return numfoliointerno;
    }

    public void setNumfoliointerno(BigInteger numfoliointerno) {
        this.numfoliointerno = numfoliointerno;
    }

    public String getTranid() {
        return tranid;
    }

    public void setTranid(String tranid) {
        this.tranid = tranid;
    }

    public String getNumtask() {
        return numtask;
    }

    public void setNumtask(String numtask) {
        this.numtask = numtask;
    }

    public BigInteger getNummovcargoabono() {
        return nummovcargoabono;
    }

    public void setNummovcargoabono(BigInteger nummovcargoabono) {
        this.nummovcargoabono = nummovcargoabono;
    }

    public String getMetodopagocfd() {
        return metodopagocfd;
    }

    public void setMetodopagocfd(String metodopagocfd) {
        this.metodopagocfd = metodopagocfd;
    }

    public String getCuentapagocfd() {
        return cuentapagocfd;
    }

    public void setCuentapagocfd(String cuentapagocfd) {
        this.cuentapagocfd = cuentapagocfd;
    }

    public String getEstatusMediador() {
        return estatusMediador;
    }

    public void setEstatusMediador(String estatusMediador) {
        this.estatusMediador = estatusMediador;
    }

    public String getMsgMt() {
        return msgMt;
    }

    public void setMsgMt(String msgMt) {
        this.msgMt = msgMt;
    }

    public String getFed() {
        return fed;
    }

    public void setFed(String fed) {
        this.fed = fed;
    }

    public Short getEnviadoMediador() {
        return enviadoMediador;
    }

    public void setEnviadoMediador(Short enviadoMediador) {
        this.enviadoMediador = enviadoMediador;
    }

    public BigInteger getFolioMediador() {
        return folioMediador;
    }

    public void setFolioMediador(BigInteger folioMediador) {
        this.folioMediador = folioMediador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleLiquidacion != null ? 
        		idDetalleLiquidacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TesDetalleLiquidacion)) {
            return false;
        }
        TesDetalleLiquidacion other = (TesDetalleLiquidacion) object;
        if ((this.idDetalleLiquidacion == null && other.idDetalleLiquidacion != null) || 
        		(this.idDetalleLiquidacion != null && 
        		!this.idDetalleLiquidacion.equals(other.idDetalleLiquidacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ixe.ods.sica.batch.domain.TesDetalleLiquidacion[ idDetalleLiquidacion=" +
        						idDetalleLiquidacion + " ]";
    }
    
}

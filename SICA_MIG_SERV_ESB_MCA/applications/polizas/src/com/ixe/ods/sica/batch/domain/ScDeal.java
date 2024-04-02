package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import static javax.persistence.FetchType.LAZY;

/**
 *
 * @author IHernandez
 */
@Entity
@Table(name = "SC_DEAL")
public class ScDeal implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID_DEAL")
    private BigInteger idDeal;
    
    @Basic(optional = false)
    @Column(name = "COMPRA")
    private Character compra;
    
    @Basic(optional = false)
    @Column(name = "ENVIAR_AL_CLIENTE")
    private Character enviarAlCliente;
    
    @Basic(optional = false)
    @Column(name = "EVENTOS_DEAL")
    private String eventosDeal;
    
    @Basic(optional = false)
    @Column(name = "FACTURA")
    private Character factura;
    
    @Basic(optional = false)
    @Column(name = "FECHA_CAPTURA")
    @Temporal(TemporalType.DATE)
    private Date fechaCaptura;
    
    @Basic(optional = false)
    @Column(name = "FECHA_LIQUIDACION")
    @Temporal(TemporalType.DATE)
    private Date fechaLiquidacion;
    
    @Column(name = "ID_BROKER")
    private BigInteger idBroker;
    
    @Basic(optional = false)
    @Column(name = "ID_CANAL")
    private String idCanal;
    
    @Column(name = "ID_DIRECCION_MENSAJERIA")
    private BigInteger idDireccionMensajeria;
    
    @Column(name = "ID_LIQUIDACION")
    private BigInteger idLiquidacion;
    
    @Column(name = "ID_PROMOTOR")
    private BigInteger idPromotor;
    
    @Basic(optional = false)
    @Column(name = "ID_USUARIO")
    private BigInteger idUsuario;
    
    @Basic(optional = false)
    @Column(name = "ID_MESA_CAMBIO")
    private BigInteger idMesaCambio;
    
    @Basic(optional = false)
    @Column(name = "MENSAJERIA")
    private Character mensajeria;
    
    @Column(name = "NO_CUENTA")
    private String noCuenta;
    
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    
    @Basic(optional = false)
    @Column(name = "PAGO_ANTICIPADO")
    private Character pagoAnticipado;
    
    @Basic(optional = false)
    @Column(name = "TOMA_EN_FIRME")
    private Character tomaEnFirme;
    
    @Basic(optional = false)
    @Column(name = "SIMPLE")
    private Character simple;
    
    @Basic(optional = false)
    @Column(name = "STATUS_DEAL")
    private String statusDeal;
    
    @Basic(optional = false)
    @Column(name = "TIPO_VALOR")
    private String tipoValor;
    
    @Column(name = "NOMBRE_FACTURA")
    private String nombreFactura;
    
    @Column(name = "RFC_FACTURA")
    private String rfcFactura;
    
    @Column(name = "ID_DIR_FACTURA")
    private BigInteger idDirFactura;
    
    @Column(name = "ID_FOLIO_SWAP")
    private BigInteger idFolioSwap;
    
    @Column(name = "FECHA_LIM_CAPT_CONT")
    @Temporal(TemporalType.DATE)
    private Date fechaLimCaptCont;
    
    @Column(name = "VERSION")
    private BigInteger version;
    
    @Column(name = "ACUDIR_CON")
    private String acudirCon;
    
    @Column(name = "LIQUIDACION_ESPECIAL")
    private String liquidacionEspecial;
    
    @Column(name = "REVERSO_DE")
    private BigInteger reversoDe;
    
    @Column(name = "REVERSA_A")
    private BigInteger reversaA;
    
    @Basic(optional = false)
    @Column(name = "LIQUIDACION_ANTICIPADA")
    private Character liquidacionAnticipada;
    
    @Column(name = "ORIGINAL_DE")
    private BigInteger originalDe;
    
    @Column(name = "CONTRARIO_DE")
    private BigInteger contrarioDe;
    
    @Column(name = "CORRECCION")
    private BigInteger correccion;
    
    @Column(name = "REVERSADO")
    private BigInteger reversado;
    
    @Column(name = "TC_COB_USD_DIV")
    private BigDecimal tcCobUsdDiv;
    
    @Column(name = "TC_COB_MXN_USD")
    private BigDecimal tcCobMxnUsd;
    
    @Column(name = "MONTO_EQUIV_LC")
    private BigDecimal montoEquivLc;
    
    @Basic(optional = false)
    @Column(name = "FACTOR_RIESGO")
    private BigDecimal factorRiesgo;
    
    @Basic(optional = false)
    @Column(name = "CAMBIO_RFC")
    private Character cambioRfc;
    
    @Basic(optional = false)
    @Column(name = "CAMBIO_EMAIL")
    private Character cambioEmail;
    
    @Column(name = "ID_DIR_FACT_MENS")
    private BigInteger idDirFactMens;
    
    @Column(name = "EMAIL_FACTURA")
    private String emailFactura;
    
    @Column(name = "EMAIL_FACTURA_OTRO")
    private String emailFacturaOtro;
    
    @Column(name = "NO_CUENTA_IXE")
    private String noCuentaIxe;
    
    @Column(name = "CLAVE_REFERENCIA")
    private String claveReferencia;
    
    @Column(name = "ID_GRUPO_TRABAJO")
    private String idGrupoTrabajo;
    
    @Column(name = "NETEO")
    private Character neteo;
    
    @Column(name = "TIPO_EXCEPCION")
    private String tipoExcepcion;
    
    @Column(name = "TIPO_ZONA")
    private String tipoZona;
    
    @Column(name = "ES_CLIENTE")
    private Character esCliente;
    
    @Column(name = "ESPECIAL")
    private Character especial;
    
    @Column(name = "ESTATUS_ESPECIAL")
    private String estatusEspecial;
    
    @Column(name = "METODO_PAGO")
    private String metodoPago;
    
    @Column(name = "CUENTA_PAGO")
    private String cuentaPago;
    
    @Column(name = "EMAILS_COMPROBANTES")
    private String emailsComprobantes;
    
    @Column(name = "CR")
    private Integer cr;
    
    @JoinColumn(name = "ID_SISTEMA", referencedColumnName = "ID_SISTEMA")
    @ManyToOne(fetch = LAZY)
    private ScSistema idSistema;

    public ScDeal() {
    }

    public ScDeal(BigInteger idDeal) {
        this.idDeal = idDeal;
    }

    public ScDeal(BigInteger idDeal, Character compra, Character enviarAlCliente, 
    			  String eventosDeal, Character factura, Date fechaCaptura, Date fechaLiquidacion,
    			  String idCanal, BigInteger idUsuario, BigInteger idMesaCambio, 
    			  Character mensajeria, Character pagoAnticipado, Character tomaEnFirme, 
    			  Character simple, String statusDeal, String tipoValor, 
    			  Character liquidacionAnticipada, BigDecimal factorRiesgo, Character cambioRfc, 
    			  Character cambioEmail) {
        this.idDeal = idDeal;
        this.compra = compra;
        this.enviarAlCliente = enviarAlCliente;
        this.eventosDeal = eventosDeal;
        this.factura = factura;
        this.fechaCaptura = fechaCaptura;
        this.fechaLiquidacion = fechaLiquidacion;
        this.idCanal = idCanal;
        this.idUsuario = idUsuario;
        this.idMesaCambio = idMesaCambio;
        this.mensajeria = mensajeria;
        this.pagoAnticipado = pagoAnticipado;
        this.tomaEnFirme = tomaEnFirme;
        this.simple = simple;
        this.statusDeal = statusDeal;
        this.tipoValor = tipoValor;
        this.liquidacionAnticipada = liquidacionAnticipada;
        this.factorRiesgo = factorRiesgo;
        this.cambioRfc = cambioRfc;
        this.cambioEmail = cambioEmail;
    }

    public BigInteger getIdDeal() {
        return idDeal;
    }

    public void setIdDeal(BigInteger idDeal) {
        this.idDeal = idDeal;
    }

    public Character getCompra() {
        return compra;
    }

    public void setCompra(Character compra) {
        this.compra = compra;
    }

    public Character getEnviarAlCliente() {
        return enviarAlCliente;
    }

    public void setEnviarAlCliente(Character enviarAlCliente) {
        this.enviarAlCliente = enviarAlCliente;
    }

    public String getEventosDeal() {
        return eventosDeal;
    }

    public void setEventosDeal(String eventosDeal) {
        this.eventosDeal = eventosDeal;
    }

    public Character getFactura() {
        return factura;
    }

    public void setFactura(Character factura) {
        this.factura = factura;
    }

    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public Date getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    public void setFechaLiquidacion(Date fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    public BigInteger getIdBroker() {
        return idBroker;
    }

    public void setIdBroker(BigInteger idBroker) {
        this.idBroker = idBroker;
    }

    public String getIdCanal() {
        return idCanal;
    }

    public void setIdCanal(String idCanal) {
        this.idCanal = idCanal;
    }

    public BigInteger getIdDireccionMensajeria() {
        return idDireccionMensajeria;
    }

    public void setIdDireccionMensajeria(BigInteger idDireccionMensajeria) {
        this.idDireccionMensajeria = idDireccionMensajeria;
    }

    public BigInteger getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(BigInteger idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    public BigInteger getIdPromotor() {
        return idPromotor;
    }

    public void setIdPromotor(BigInteger idPromotor) {
        this.idPromotor = idPromotor;
    }

    public BigInteger getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigInteger getIdMesaCambio() {
        return idMesaCambio;
    }

    public void setIdMesaCambio(BigInteger idMesaCambio) {
        this.idMesaCambio = idMesaCambio;
    }

    public Character getMensajeria() {
        return mensajeria;
    }

    public void setMensajeria(Character mensajeria) {
        this.mensajeria = mensajeria;
    }

    public String getNoCuenta() {
        return noCuenta;
    }

    public void setNoCuenta(String noCuenta) {
        this.noCuenta = noCuenta;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Character getPagoAnticipado() {
        return pagoAnticipado;
    }

    public void setPagoAnticipado(Character pagoAnticipado) {
        this.pagoAnticipado = pagoAnticipado;
    }

    public Character getTomaEnFirme() {
        return tomaEnFirme;
    }

    public void setTomaEnFirme(Character tomaEnFirme) {
        this.tomaEnFirme = tomaEnFirme;
    }

    public Character getSimple() {
        return simple;
    }

    public void setSimple(Character simple) {
        this.simple = simple;
    }

    public String getStatusDeal() {
        return statusDeal;
    }

    public void setStatusDeal(String statusDeal) {
        this.statusDeal = statusDeal;
    }

    public String getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(String tipoValor) {
        this.tipoValor = tipoValor;
    }

    public String getNombreFactura() {
        return nombreFactura;
    }

    public void setNombreFactura(String nombreFactura) {
        this.nombreFactura = nombreFactura;
    }

    public String getRfcFactura() {
        return rfcFactura;
    }

    public void setRfcFactura(String rfcFactura) {
        this.rfcFactura = rfcFactura;
    }

    public BigInteger getIdDirFactura() {
        return idDirFactura;
    }

    public void setIdDirFactura(BigInteger idDirFactura) {
        this.idDirFactura = idDirFactura;
    }

    public BigInteger getIdFolioSwap() {
        return idFolioSwap;
    }

    public void setIdFolioSwap(BigInteger idFolioSwap) {
        this.idFolioSwap = idFolioSwap;
    }

    public Date getFechaLimCaptCont() {
        return fechaLimCaptCont;
    }

    public void setFechaLimCaptCont(Date fechaLimCaptCont) {
        this.fechaLimCaptCont = fechaLimCaptCont;
    }

    public BigInteger getVersion() {
        return version;
    }

    public void setVersion(BigInteger version) {
        this.version = version;
    }

    public String getAcudirCon() {
        return acudirCon;
    }

    public void setAcudirCon(String acudirCon) {
        this.acudirCon = acudirCon;
    }

    public String getLiquidacionEspecial() {
        return liquidacionEspecial;
    }

    public void setLiquidacionEspecial(String liquidacionEspecial) {
        this.liquidacionEspecial = liquidacionEspecial;
    }

    public BigInteger getReversoDe() {
        return reversoDe;
    }

    public void setReversoDe(BigInteger reversoDe) {
        this.reversoDe = reversoDe;
    }

    public BigInteger getReversaA() {
        return reversaA;
    }

    public void setReversaA(BigInteger reversaA) {
        this.reversaA = reversaA;
    }

    public Character getLiquidacionAnticipada() {
        return liquidacionAnticipada;
    }

    public void setLiquidacionAnticipada(Character liquidacionAnticipada) {
        this.liquidacionAnticipada = liquidacionAnticipada;
    }

    public BigInteger getOriginalDe() {
        return originalDe;
    }

    public void setOriginalDe(BigInteger originalDe) {
        this.originalDe = originalDe;
    }

    public BigInteger getContrarioDe() {
        return contrarioDe;
    }

    public void setContrarioDe(BigInteger contrarioDe) {
        this.contrarioDe = contrarioDe;
    }

    public BigInteger getCorreccion() {
        return correccion;
    }

    public void setCorreccion(BigInteger correccion) {
        this.correccion = correccion;
    }

    public BigInteger getReversado() {
        return reversado;
    }

    public void setReversado(BigInteger reversado) {
        this.reversado = reversado;
    }

    public BigDecimal getTcCobUsdDiv() {
        return tcCobUsdDiv;
    }

    public void setTcCobUsdDiv(BigDecimal tcCobUsdDiv) {
        this.tcCobUsdDiv = tcCobUsdDiv;
    }

    public BigDecimal getTcCobMxnUsd() {
        return tcCobMxnUsd;
    }

    public void setTcCobMxnUsd(BigDecimal tcCobMxnUsd) {
        this.tcCobMxnUsd = tcCobMxnUsd;
    }

    public BigDecimal getMontoEquivLc() {
        return montoEquivLc;
    }

    public void setMontoEquivLc(BigDecimal montoEquivLc) {
        this.montoEquivLc = montoEquivLc;
    }

    public BigDecimal getFactorRiesgo() {
        return factorRiesgo;
    }

    public void setFactorRiesgo(BigDecimal factorRiesgo) {
        this.factorRiesgo = factorRiesgo;
    }

    public Character getCambioRfc() {
        return cambioRfc;
    }

    public void setCambioRfc(Character cambioRfc) {
        this.cambioRfc = cambioRfc;
    }

    public Character getCambioEmail() {
        return cambioEmail;
    }

    public void setCambioEmail(Character cambioEmail) {
        this.cambioEmail = cambioEmail;
    }

    public BigInteger getIdDirFactMens() {
        return idDirFactMens;
    }

    public void setIdDirFactMens(BigInteger idDirFactMens) {
        this.idDirFactMens = idDirFactMens;
    }

    public String getEmailFactura() {
        return emailFactura;
    }

    public void setEmailFactura(String emailFactura) {
        this.emailFactura = emailFactura;
    }

    public String getEmailFacturaOtro() {
        return emailFacturaOtro;
    }

    public void setEmailFacturaOtro(String emailFacturaOtro) {
        this.emailFacturaOtro = emailFacturaOtro;
    }

    public String getNoCuentaIxe() {
        return noCuentaIxe;
    }

    public void setNoCuentaIxe(String noCuentaIxe) {
        this.noCuentaIxe = noCuentaIxe;
    }

    public String getClaveReferencia() {
        return claveReferencia;
    }

    public void setClaveReferencia(String claveReferencia) {
        this.claveReferencia = claveReferencia;
    }

    public String getIdGrupoTrabajo() {
        return idGrupoTrabajo;
    }

    public void setIdGrupoTrabajo(String idGrupoTrabajo) {
        this.idGrupoTrabajo = idGrupoTrabajo;
    }

    public Character getNeteo() {
        return neteo;
    }

    public void setNeteo(Character neteo) {
        this.neteo = neteo;
    }

    public String getTipoExcepcion() {
        return tipoExcepcion;
    }

    public void setTipoExcepcion(String tipoExcepcion) {
        this.tipoExcepcion = tipoExcepcion;
    }

    public String getTipoZona() {
        return tipoZona;
    }

    public void setTipoZona(String tipoZona) {
        this.tipoZona = tipoZona;
    }

    public Character getEsCliente() {
        return esCliente;
    }

    public void setEsCliente(Character esCliente) {
        this.esCliente = esCliente;
    }

    public Character getEspecial() {
        return especial;
    }

    public void setEspecial(Character especial) {
        this.especial = especial;
    }

    public String getEstatusEspecial() {
        return estatusEspecial;
    }

    public void setEstatusEspecial(String estatusEspecial) {
        this.estatusEspecial = estatusEspecial;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getCuentaPago() {
        return cuentaPago;
    }

    public void setCuentaPago(String cuentaPago) {
        this.cuentaPago = cuentaPago;
    }

    public String getEmailsComprobantes() {
        return emailsComprobantes;
    }

    public void setEmailsComprobantes(String emailsComprobantes) {
        this.emailsComprobantes = emailsComprobantes;
    }

    public Integer getCr() {
        return cr;
    }

    public void setCr(Integer cr) {
        this.cr = cr;
    }

    public ScSistema getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(ScSistema idSistema) {
        this.idSistema = idSistema;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDeal != null ? idDeal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ScDeal)) {
            return false;
        }
        ScDeal other = (ScDeal) object;
        if ((this.idDeal == null && other.idDeal != null) || 
        		(this.idDeal != null && !this.idDeal.equals(other.idDeal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ixe.ods.sica.batch.domain.ScDeal[ idDeal=" + idDeal + " ]";
    }
    
}

package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
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
 * The Class TesFacturacionSap.
 *
 * @author IHernandez
 */
@Entity
@Table(name = "TES_FACTURACION_SAP")
public class TesFacturacionSap implements Serializable {
	
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The Constant STATUS_PEND. */
    public static final String STATUS_PEND = "PEND";
	
	/** The Constant STATUS_FACT. */
	public static final String STATUS_FACT = "FACT";
	
	/** The Constant STATUS_CANC. */
	public static final String STATUS_CANC = "CANC";
	
	/** The Constant STATUS_SCAN. */
	public static final String STATUS_SCAN = "SCAN";
   
    /** The id movimiento. */
    @Id
    @Basic(optional = false)
    @Column(name = "ID_MOVIMIENTO")
    private BigInteger idMovimiento;
    
    /** The id liquidacion. */
    @Basic(optional = false)
    @Column(name = "ID_LIQUIDACION")
    private BigInteger idLiquidacion;
    
    /** The id deal. */
    @Basic(optional = false)
    @Column(name = "ID_DEAL")
    private BigInteger idDeal;
    
    /** The id detalle sica. */
    @Basic(optional = false)
    @Column(name = "ID_DETALLE_SICA")
    private BigInteger idDetalleSica;
    
    /** The id persona liquida. */
    @Basic(optional = false)
    @Column(name = "ID_PERSONA_LIQUIDA")
    private BigInteger idPersonaLiquida;
    
    /** The fecha envio site. */
    @Basic(optional = false)
    @Column(name = "FECHA_ENVIO_SITE")
    @Temporal(TemporalType.DATE)
    private Date fechaEnvioSite;
    
    /** The fecha proceso sap. */
    @Column(name = "FECHA_PROCESO_SAP")
    @Temporal(TemporalType.DATE)
    private Date fechaProcesoSap;
    
    /** The fecha lectura site. */
    @Column(name = "FECHA_LECTURA_SITE")
    @Temporal(TemporalType.DATE)
    private Date fechaLecturaSite;
    
    /** The ejercicio. */
    @Basic(optional = false)
    @Column(name = "EJERCICIO")
    private BigInteger ejercicio;
    
    /** The folio factura site. */
    @Basic(optional = false)
    @Column(name = "FOLIO_FACTURA_SITE")
    private BigInteger folioFacturaSite;
    
    /** The folio fiscal factura. */
    @Column(name = "FOLIO_FISCAL_FACTURA")
    private String folioFiscalFactura;
    
    /** The url xml factura. */
    @Column(name = "URL_XML_FACTURA")
    private String urlXmlFactura;
    
    /** The url pdf factura. */
    @Column(name = "URL_PDF_FACTURA")
    private String urlPdfFactura;
    
    /** The descripcion error. */
    @Column(name = "DESCRIPCION_ERROR")
    private String descripcionError;
    
    /** The nombre archivo. */
    @Basic(optional = false)
    @Column(name = "NOMBRE_ARCHIVO")
    private String nombreArchivo;
    
    /** The estatus factura. */
    @Basic(optional = false)
    @Column(name = "ESTATUS_FACTURA")
    private String estatusFactura;
    
    /** The es refacturacion. */
    @Basic(optional = false)
    @Column(name = "ES_REFACTURACION")
    private short esRefacturacion;
    
    /** The recibido arch indices. */
    @Basic(optional = true)
    @Column(name = "RECIBIDO_ARCH_INDICES")
    private short recibidoArchIndices;

    /** The tes detalle liquidacion. */
    @JoinColumn(name = "ID_DETALLE_LIQUIDACION", 
    		referencedColumnName = "ID_DETALLE_LIQUIDACION")
    @ManyToOne(optional = false, fetch = LAZY)
    private TesDetalleLiquidacion tesDetalleLiquidacion;

    /**
     * Instantiates a new tes facturacion sap.
     */
    public TesFacturacionSap() {
    }

    /**
     * Instantiates a new tes facturacion sap.
     *
     * @param idMovimiento the id movimiento
     */
    public TesFacturacionSap(BigInteger idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    /**
     * Instantiates a new tes facturacion sap.
     *
     * @param idMovimiento the id movimiento
     * @param idLiquidacion the id liquidacion
     * @param idDeal the id deal
     * @param idDetalleSica the id detalle sica
     * @param idPersonaLiquida the id persona liquida
     * @param fechaEnvioSite the fecha envio site
     * @param ejercicio the ejercicio
     * @param folioFacturaSite the folio factura site
     * @param nombreArchivo the nombre archivo
     * @param estatusFactura the estatus factura
     * @param esRefacturacion the es refacturacion
     */
    public TesFacturacionSap(BigInteger idMovimiento, BigInteger idLiquidacion, 
    		BigInteger idDeal, BigInteger idDetalleSica, BigInteger idPersonaLiquida,
    		Date fechaEnvioSite, BigInteger ejercicio, BigInteger folioFacturaSite, 
    		String nombreArchivo, String estatusFactura, short esRefacturacion) {
        this.idMovimiento = idMovimiento;
        this.idLiquidacion = idLiquidacion;
        this.idDeal = idDeal;
        this.idDetalleSica = idDetalleSica;
        this.idPersonaLiquida = idPersonaLiquida;
        this.fechaEnvioSite = fechaEnvioSite;
        this.ejercicio = ejercicio;
        this.folioFacturaSite = folioFacturaSite;
        this.nombreArchivo = nombreArchivo;
        this.estatusFactura = estatusFactura;
        this.esRefacturacion = esRefacturacion;
    }

    /**
     * Gets the id movimiento.
     *
     * @return the id movimiento
     */
    public BigInteger getIdMovimiento() {
        return idMovimiento;
    }

    /**
     * Sets the id movimiento.
     *
     * @param idMovimiento the new id movimiento
     */
    public void setIdMovimiento(BigInteger idMovimiento) {
        this.idMovimiento = idMovimiento;
    }
    
    /**
     * Gets the tes detalle liquidacion.
     *
     * @return the tes detalle liquidacion
     */
    public TesDetalleLiquidacion getTesDetalleLiquidacion() {
        return tesDetalleLiquidacion;
    }

    /**
     * Sets the tes detalle liquidacion.
     *
     * @param tesDetalleLiquidacion the new tes detalle liquidacion
     */
    public void setTesDetalleLiquidacion(TesDetalleLiquidacion tesDetalleLiquidacion) {
        this.tesDetalleLiquidacion = tesDetalleLiquidacion;
    }

    /**
     * Gets the id liquidacion.
     *
     * @return the id liquidacion
     */
    public BigInteger getIdLiquidacion() {
        return idLiquidacion;
    }

    /**
     * Sets the id liquidacion.
     *
     * @param idLiquidacion the new id liquidacion
     */
    public void setIdLiquidacion(BigInteger idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    /**
     * Gets the id deal.
     *
     * @return the id deal
     */
    public BigInteger getIdDeal() {
        return idDeal;
    }

    /**
     * Sets the id deal.
     *
     * @param idDeal the new id deal
     */
    public void setIdDeal(BigInteger idDeal) {
        this.idDeal = idDeal;
    }

    /**
     * Gets the id detalle sica.
     *
     * @return the id detalle sica
     */
    public BigInteger getIdDetalleSica() {
        return idDetalleSica;
    }

    /**
     * Sets the id detalle sica.
     *
     * @param idDetalleSica the new id detalle sica
     */
    public void setIdDetalleSica(BigInteger idDetalleSica) {
        this.idDetalleSica = idDetalleSica;
    }

    /**
     * Gets the id persona liquida.
     *
     * @return the id persona liquida
     */
    public BigInteger getIdPersonaLiquida() {
        return idPersonaLiquida;
    }

    /**
     * Sets the id persona liquida.
     *
     * @param idPersonaLiquida the new id persona liquida
     */
    public void setIdPersonaLiquida(BigInteger idPersonaLiquida) {
        this.idPersonaLiquida = idPersonaLiquida;
    }

    /**
     * Gets the fecha envio site.
     *
     * @return the fecha envio site
     */
    public Date getFechaEnvioSite() {
        return fechaEnvioSite;
    }

    /**
     * Sets the fecha envio site.
     *
     * @param fechaEnvioSite the new fecha envio site
     */
    public void setFechaEnvioSite(Date fechaEnvioSite) {
        this.fechaEnvioSite = fechaEnvioSite;
    }

    /**
     * Gets the fecha proceso sap.
     *
     * @return the fecha proceso sap
     */
    public Date getFechaProcesoSap() {
        return fechaProcesoSap;
    }

    /**
     * Sets the fecha proceso sap.
     *
     * @param fechaProcesoSap the new fecha proceso sap
     */
    public void setFechaProcesoSap(Date fechaProcesoSap) {
        this.fechaProcesoSap = fechaProcesoSap;
    }

    /**
     * Gets the fecha lectura site.
     *
     * @return the fecha lectura site
     */
    public Date getFechaLecturaSite() {
        return fechaLecturaSite;
    }

    /**
     * Sets the fecha lectura site.
     *
     * @param fechaLecturaSite the new fecha lectura site
     */
    public void setFechaLecturaSite(Date fechaLecturaSite) {
        this.fechaLecturaSite = fechaLecturaSite;
    }

    /**
     * Gets the ejercicio.
     *
     * @return the ejercicio
     */
    public BigInteger getEjercicio() {
        return ejercicio;
    }

    /**
     * Sets the ejercicio.
     *
     * @param ejercicio the new ejercicio
     */
    public void setEjercicio(BigInteger ejercicio) {
        this.ejercicio = ejercicio;
    }

    /**
     * Gets the folio factura site.
     *
     * @return the folio factura site
     */
    public BigInteger getFolioFacturaSite() {
        return folioFacturaSite;
    }

    /**
     * Sets the folio factura site.
     *
     * @param folioFacturaSite the new folio factura site
     */
    public void setFolioFacturaSite(BigInteger folioFacturaSite) {
        this.folioFacturaSite = folioFacturaSite;
    }

    /**
     * Gets the folio fiscal factura.
     *
     * @return the folio fiscal factura
     */
    public String getFolioFiscalFactura() {
        return folioFiscalFactura;
    }

    /**
     * Sets the folio fiscal factura.
     *
     * @param folioFiscalFactura the new folio fiscal factura
     */
    public void setFolioFiscalFactura(String folioFiscalFactura) {
        this.folioFiscalFactura = folioFiscalFactura;
    }

    /**
     * Gets the url xml factura.
     *
     * @return the url xml factura
     */
    public String getUrlXmlFactura() {
        return urlXmlFactura;
    }

    /**
     * Sets the url xml factura.
     *
     * @param urlXmlFactura the new url xml factura
     */
    public void setUrlXmlFactura(String urlXmlFactura) {
        this.urlXmlFactura = urlXmlFactura;
    }

    /**
     * Gets the url pdf factura.
     *
     * @return the url pdf factura
     */
    public String getUrlPdfFactura() {
        return urlPdfFactura;
    }

    /**
     * Sets the url pdf factura.
     *
     * @param urlPdfFactura the new url pdf factura
     */
    public void setUrlPdfFactura(String urlPdfFactura) {
        this.urlPdfFactura = urlPdfFactura;
    }

    /**
     * Gets the descripcion error.
     *
     * @return the descripcion error
     */
    public String getDescripcionError() {
        return descripcionError;
    }

    /**
     * Sets the descripcion error.
     *
     * @param descripcionError the new descripcion error
     */
    public void setDescripcionError(String descripcionError) {
        this.descripcionError = descripcionError;
    }

    /**
     * Gets the nombre archivo.
     *
     * @return the nombre archivo
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * Sets the nombre archivo.
     *
     * @param nombreArchivo the new nombre archivo
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    /**
     * Gets the estatus factura.
     *
     * @return the estatus factura
     */
    public String getEstatusFactura() {
        return estatusFactura;
    }

    /**
     * Sets the estatus factura.
     *
     * @param estatusFactura the new estatus factura
     */
    public void setEstatusFactura(String estatusFactura) {
        this.estatusFactura = estatusFactura;
    }

    /**
     * Gets the es refacturacion.
     *
     * @return the es refacturacion
     */
    public short getEsRefacturacion() {
        return esRefacturacion;
    }

    /**
     * Sets the es refacturacion.
     *
     * @param esRefacturacion the new es refacturacion
     */
    public void setEsRefacturacion(short esRefacturacion) {
        this.esRefacturacion = esRefacturacion;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMovimiento != null ? idMovimiento.hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TesFacturacionSap)) {
            return false;
        }
        TesFacturacionSap other = (TesFacturacionSap) object;
        if ((this.idMovimiento == null && other.idMovimiento != null) || 
        		(this.idMovimiento != null && !this.idMovimiento.equals(other.idMovimiento))) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "com.ixe.ods.sica.batch.domain.TesFacturacionSap[ idMovimiento=" + 
        							idMovimiento + " ]";
    }

	/**
	 * Gets the recibido arch indices.
	 *
	 * @return the recibido arch indices
	 */
	public short getRecibidoArchIndices() {
		return recibidoArchIndices;
	}

	/**
	 * Sets the recibido arch indices.
	 *
	 * @param recibidoArchIndices the new recibido arch indices
	 */
	public void setRecibidoArchIndices(short recibidoArchIndices) {
		this.recibidoArchIndices = recibidoArchIndices;
	}
    
}

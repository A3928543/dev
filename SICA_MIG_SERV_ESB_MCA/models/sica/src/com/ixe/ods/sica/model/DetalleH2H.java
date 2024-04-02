package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Clase persistente para la tabla SC_DETALLES_H2H
 *
 * @hibernate.class table="SC_DETALLES_H2H"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.DetalleH2H"
 * dynamic-update="true"
 */
public class DetalleH2H implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private Long idDealPosicion = null; 
	private Long idDeal = null; 
	private Date fechaEnvio = null;
	private Integer version = null; 
	private String tipoOperacion = null;
	private Date fechaCaptura = null;
	private Date fechaEfectiva = null;
	private Date fechaLiquidacion = null;
	private BigDecimal importe = null;
	private BigDecimal montoDolarizado = null; 
	private BigDecimal factorDivisaUsd = null;
	private BigDecimal tipoCambio = null;
	private String idDivisa = null;
	private String plazo = null;
	private String producto = null;
	private String claveContraparte = null;
	private String nombreCliente = null; // Solo 50 caracteres 
	private String rfc = null;
	private String tipoCliente = null;
	private String statusH2H = null;
	private String enviada = null;
	private String folioBanxico = null;
	private Integer codigoError = null; 
	private String descripcionError = null;
	private Long idPersonaPromotor = null;
	private Integer cierreVespertino = null; 
	private Date fechaCancelacion = null;
	private String cancelarDetalle = null; 
	
	public DetalleH2H()
	{
		super();
	}

	/**
     * Regresa el identificador del detalle de deal
     * 
     * @return <code>Long</code>
     *
     *@hibernate.id generator-class="assigned" 
     *column="ID_DEAL_POSICION"
     *unsaved-value="null"
     */
	public Long getIdDealPosicion() {
		return idDealPosicion;
	}

	public void setIdDealPosicion(Long idDealPosicion) {
		this.idDealPosicion = idDealPosicion;
	}

	/**
     * Regresa el identificador del deal
     * 
     * @return <code>Long</code>
     *
     *@hibernate.property column="ID_DEAL"
     *not-null="true"
     */
	public Long getIdDeal() {
		return idDeal;
	}

	public void setIdDeal(Long idDeal) {
		this.idDeal = idDeal;
	}

	/**
     * Regresa la fecha de envio al H2H de Banorte
     * 
     * @return <code>Date</code>
     *
     *@hibernate.property column="FECHA_ENVIO"
     *not-null="true"
     */
	public Date getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	/**
     * Regresa el numero de version de la operacion registrada en el H2H de Banorte
     * 
     * @return <code>Integer</code>
     *
     *@hibernate.property column="VERSION"
     */
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
     * Regresa S: (si la operacion es una Venta) o B: (Si la operacion es una compra)
     * 
     * @return <code>String</code>
     *
     *@hibernate.property column="TIPO_OPERACION"
     *not-null="true"
     */
	public String getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	/**
     * Regresa la fecha de captura del deal
     * 
     * @return <code>Date</code>
     *
     *@hibernate.property column="FECHA_CAPTURA"
     *not-null="true"
     */
	public Date getFechaCaptura() {
		return fechaCaptura;
	}

	public void setFechaCaptura(Date fechaCaptura) {
		this.fechaCaptura = fechaCaptura;
	}

	/**
     * Regresa la fecha de captura del deal
     * 
     * @return <code>Date</code>
     *
     *@hibernate.property column="FECHA_EFECTIVA"
     *not-null="true"
     */
	public Date getFechaEfectiva() {
		return fechaEfectiva;
	}

	public void setFechaEfectiva(Date fechaEfectiva) {
		this.fechaEfectiva = fechaEfectiva;
	}

	/**
     * Regresa la fecha de liquidacion del deal
     * 
     * @return <code>Date</code>
     *
     *@hibernate.property column="FECHA_VENCIMIENTO"
     *not-null="true"
     */
	public Date getFechaLiquidacion() {
		return fechaLiquidacion;
	}

	public void setFechaLiquidacion(Date fechaLiquidacion) {
		this.fechaLiquidacion = fechaLiquidacion;
	}

	/**
     * Regresa el monto del detalle de deal en la divisa pactada
     * 
     * @return <code>BigDecimal</code>
     *
     *@hibernate.property column="IMPORTE"
     *not-null="true"
     */
	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	/**
     * Regresa el monto dolarizado del importe del detalle de deal
     * 
     * @return <code>BigDecimal</code>
     *
     *@hibernate.property column="MONTO_DOLARIZADO"
     *not-null="true"
     */
	public BigDecimal getMontoDolarizado() {
		return montoDolarizado;
	}

	public void setMontoDolarizado(BigDecimal montoDolarizado) {
		this.montoDolarizado = montoDolarizado;
	}

	/**
     * Regresa el factor de divisa USD
     * 
     * @return <code>BigDecimal</code>
     *
     *@hibernate.property column="FACTOR_DIV_USD"
     *not-null="true"
     */
	public BigDecimal getFactorDivisaUsd() {
		return factorDivisaUsd;
	}

	public void setFactorDivisaUsd(BigDecimal factorDivisaUsd) {
		this.factorDivisaUsd = factorDivisaUsd;
	}

	/**
     * Regresa el tipo de cambio de la divisa del detalle de deal
     * 
     * @return <code>BigDecimal</code>
     *
     *@hibernate.property column="TIPO_CAMBIO"
     *not-null="true"
     */
	public BigDecimal getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(BigDecimal tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	/**
     * Regresa el id de la divisa del detalle de deal
     * 
     * @return <code>String</code>
     *
     *@hibernate.property column="ID_DIVISA"
     *not-null="true"
     */
	public String getIdDivisa() {
		return idDivisa;
	}

	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}

	/**
     * Regresa el plazo de liquidacion del detalle de deal:FX_V_HOY, FX_V_24HRS, FX_V_48HRS, FX_FORWARD
     * 
     * @return <code>String</code>
     *
     *@hibernate.property column="PLAZO"
     *not-null="true"
     */
	public String getPlazo() {
		return plazo;
	}

	public void setPlazo(String plazo) {
		this.plazo = plazo;
	}

	/**
     * Regresa el producto del detalle de deal:FX_V_HOY, FX_V_24HRS, FX_V_48HRS, FX_FORWARD
     * 
     * @return <code>String</code>
     *
     *@hibernate.property column="PRODUCTO"
     *not-null="true"
     */
	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	/**
     * Regresa la clave de contraparte Banxico del cliente
     * 
     * @return <code>String</code>
     *
     *@hibernate.property column="NUM_CTE"
     *not-null="false"
     */
	public String getClaveContraparte() {
		return claveContraparte;
	}

	public void setClaveContraparte(String claveContraparte) {
		this.claveContraparte = claveContraparte;
	}

	/**
     * Regresa el nombre del cliente. El campo solo acepta 50 caracteres
     * 
     * @return <code>String</code>
     *
     *@hibernate.property column="NOMBRE_CLIENTE"
     *not-null="false"
     */
	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	/**
     * Regresa el RFC del cliente
     * 
     * @return <code>String</code>
     *
     *@hibernate.property column="RFC"
     *not-null="false"
     */
	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	/**
     * Regresa el tipo de cliente
     * 
     * @return <code>String</code>
     *
     *@hibernate.property column="TIPO_CLIENTE"
     *not-null="false"
     */
	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	/**
     * Regresa el estatus del detalle de deal que se enviara al H2H: A(Autorizada), C(Cambio) y B(Baja)
     * 
     * @return <code>String</code>
     *
     *@hibernate.property column="STATUSH2H"
     *not-null="true"
     */
	public String getStatusH2H() {
		return statusH2H;
	}

	public void setStatusH2H(String statusH2H) {
		this.statusH2H = statusH2H;
	}

	/**
     * Regresa S(si el detalle de deal ya ha sido enviado al H2H), de lo contrario N 
     * 
     * @return <code>String</code>
     *
     *@hibernate.property column="ENVIADA"
     *not-null="true"
     */
	public String getEnviada() {
		return enviada;
	}

	public void setEnviada(String enviada) {
		this.enviada = enviada;
	}

	/**
     * Regresa el folio que Banxico le asigno al detalle de deal
     * 
     * @return <code>String</code>
     *
     *@hibernate.property column="FOLIO_BANXICO"
     *not-null="false"
     */
	public String getFolioBanxico() {
		return folioBanxico;
	}

	public void setFolioBanxico(String folioBanxico) {
		this.folioBanxico = folioBanxico;
	}

	/**
     * Regresa el codigo de error que envio Banxico
     * 
     * @return <code>Integer</code>
     *
     *@hibernate.property column="COD_ERROR"
     *not-null="false"
     */
	public Integer getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(Integer codigoError) {
		this.codigoError = codigoError;
	}

	/**
     * Regresa la descripcion del error
     * @return <code>String</code>
     *
     *@hibernate.property column="DESC_ERROR"
     *not-null="false"
     */
	public String getDescripcionError() {
		return descripcionError;
	}

	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}

	/**
     * Regresa el id persona del promotor
     * 
     * @return <code>Long</code>
     *
     *@hibernate.property column="ID_PROMOTOR"
     *not-null="true"
     */
	public Long getIdPersonaPromotor() {
		return idPersonaPromotor;
	}

	public void setIdPersonaPromotor(Long idPersonaPromotor) {
		this.idPersonaPromotor = idPersonaPromotor;
	}

	/**
     * Regresa 1 si el detalle de liquidacion fue enviado al H2H en el cierre vespertino, de lo contrario 0 o null
     * 
     * @return <code>Integer</code>
     *
     *@hibernate.property column="CIERRE_VESPERTINO"
     *not-null="false"
     */
	public Integer getCierreVespertino() {
		return cierreVespertino;
	}

	public void setCierreVespertino(Integer cierreVespertino) {
		this.cierreVespertino = cierreVespertino;
	}

	/**
     * Regresa el la fecha de cancelacion del detalle de deal en el H2H
     * 
     * @return <code>Date</code>
     *
     *@hibernate.property column="FECHA_CANC"
     *not-null="false"
     */
	public Date getFechaCancelacion() {
		return fechaCancelacion;
	}

	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}

	/**
     * Regresa 1 si el detalle ha sido enviado al H2H para cancelarse, de lo contrario null
     * 
     * @return <code>Integer</code>
     *
     *@hibernate.property column="CANCELAR_DETALLE"
     *not-null="false"
     */
	public String getCancelarDetalle() {
		return cancelarDetalle;
	}

	public void setCancelarDetalle(String cancelarDetalle) {
		this.cancelarDetalle = cancelarDetalle;
	}
}

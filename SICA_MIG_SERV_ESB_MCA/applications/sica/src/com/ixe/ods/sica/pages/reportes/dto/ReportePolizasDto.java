/**
 * 
 */
package com.ixe.ods.sica.pages.reportes.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author J. Isaac Godinez
 *
 */
public class ReportePolizasDto implements Serializable {
	
	public static final Integer INT_CERO=new Integer(0);
	public static final Double DOUB_CERO=new Double(0);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 23234123123L;
	private String nombreCliente="";
	private String idPoliza="";//idPoliza", row.get("ID_POLIZA"));
	private Date fechaValor=null;//, new Date(((Timestamp)row.get("FECHA_VALOR")).getTime()));
	private String cuentaContable="";//", row.get("CUENTA_CONTABLE"));
	private String entidad="";//, row.get("ENTIDAD"));
	private String cargoAbono="";//, row.get("CARGO_ABONO"));
	private String idDivisa="";//, row.get("ID_DIVISA"));
	private Double importe=DOUB_CERO;//, new Double(row.get("IMPORTE").toString()));
	private Integer idDeal=INT_CERO;//, new Integer(row.get("ID_DEAL").toString()));
	private String referencia="";//, row.get("REFERENCIA"));
	private Date fechaCreacion=null;//", new Date(((Timestamp)row.get("FECHA_CREACION")).getTime()));
	private String tipoDeal="";//, row.get("TIPO_DEAL"));
	private String sucursalOrigen="";//, row.get("SUCURSAL_ORIGEN"));	
	private String cliente="";//, row.get("CLIENTE") != null ? row.get("CLIENTE") : "");
	private String fechaProcesamiento="";//, row.get("FECHA_PROCESAMIENTO"));
	private Integer statusProceso=INT_CERO;//, (new Integer(row.get("STATUS_PROCESO").toString())));
	private Integer folioDetalle=INT_CERO;//, new Integer(row.get("FOLIO_DETALLE").toString()));
	private String noContratoSica="";//, row.get("NO_CONTRATO_SICA"));
	private Double tipoDeCambio=DOUB_CERO;//, new Double(row.get("TIPO_CAMBIO").toString()));						
	private String claveReferencia="";//,row.get("CLAVE_REFERENCIA") == null ?"":row.get("CLAVE_REFERENCIA"));
	
	public ReportePolizasDto(){		
	}

	
	
	
	public ReportePolizasDto(String nombreCliente, String idPoliza,
			Date fechaValor, String cuentaContable, String entidad,
			String cargoAbono, String idDivisa, Double importe, Integer idDeal,
			String referencia, Date fechaCreacion, String tipoDeal,
			String sucursalOrigen, String cliente, String fechaProcesamiento,
			Integer statusProceso, Integer folioDetalle, String noContratoSica,
			Double tipoDeCambio, String claveReferencia) {
		super();
		this.nombreCliente = nombreCliente;
		this.idPoliza = idPoliza;
		this.fechaValor = fechaValor;
		this.cuentaContable = cuentaContable;
		this.entidad = entidad;
		this.cargoAbono = cargoAbono;
		this.idDivisa = idDivisa;
		this.importe = importe;
		this.idDeal = idDeal;
		this.referencia = referencia;
		this.fechaCreacion = fechaCreacion;
		this.tipoDeal = tipoDeal;
		this.sucursalOrigen = sucursalOrigen;
		this.cliente = cliente;
		this.fechaProcesamiento = fechaProcesamiento;
		this.statusProceso = statusProceso;
		this.folioDetalle = folioDetalle;
		this.noContratoSica = noContratoSica;
		this.tipoDeCambio = tipoDeCambio;
		this.claveReferencia = claveReferencia;
	}




	/**
	 * @return the nombreCliente
	 */
	public String getNombreCliente() {
		return nombreCliente;
	}

	/**
	 * @param nombreCliente the nombreCliente to set
	 */
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	/**
	 * @return the idPoliza
	 */
	public String getIdPoliza() {
		return idPoliza;
	}

	/**
	 * @param idPoliza the idPoliza to set
	 */
	public void setIdPoliza(String idPoliza) {
		this.idPoliza = idPoliza;
	}

	/**
	 * @return the fechaValor
	 */
	public Date getFechaValor() {
		return fechaValor;
	}

	/**
	 * @param fechaValor the fechaValor to set
	 */
	public void setFechaValor(Date fechaValor) {
		this.fechaValor = fechaValor;
	}

	/**
	 * @return the cuentaContable
	 */
	public String getCuentaContable() {
		return cuentaContable;
	}

	/**
	 * @param cuentaContable the cuentaContable to set
	 */
	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	/**
	 * @return the entidad
	 */
	public String getEntidad() {
		return entidad;
	}

	/**
	 * @param entidad the entidad to set
	 */
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	/**
	 * @return the cargoAbono
	 */
	public String getCargoAbono() {
		return cargoAbono;
	}

	/**
	 * @param cargoAbono the cargoAbono to set
	 */
	public void setCargoAbono(String cargoAbono) {
		this.cargoAbono = cargoAbono;
	}

	/**
	 * @return the idDivisa
	 */
	public String getIdDivisa() {
		return idDivisa;
	}

	/**
	 * @param idDivisa the idDivisa to set
	 */
	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}

	/**
	 * @return the importe
	 */
	public Double getImporte() {
		return importe;
	}

	/**
	 * @param importe the importe to set
	 */
	public void setImporte(Double importe) {
		this.importe = importe;
	}

	/**
	 * @return the idDeal
	 */
	public Integer getIdDeal() {
		return idDeal;
	}

	/**
	 * @param idDeal the idDeal to set
	 */
	public void setIdDeal(Integer idDeal) {
		this.idDeal = idDeal;
	}

	/**
	 * @return the referencia
	 */
	public String getReferencia() {
		return referencia;
	}

	/**
	 * @param referencia the referencia to set
	 */
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	/**
	 * @return the fechaCreacion
	 */
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion the fechaCreacion to set
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @return the tipoDeal
	 */
	public String getTipoDeal() {
		return tipoDeal;
	}

	/**
	 * @param tipoDeal the tipoDeal to set
	 */
	public void setTipoDeal(String tipoDeal) {
		this.tipoDeal = tipoDeal;
	}

	/**
	 * @return the sucursalOrigen
	 */
	public String getSucursalOrigen() {
		return sucursalOrigen;
	}

	/**
	 * @param sucursalOrigen the sucursalOrigen to set
	 */
	public void setSucursalOrigen(String sucursalOrigen) {
		this.sucursalOrigen = sucursalOrigen;
	}

	/**
	 * @return the cliente
	 */
	public String getCliente() {
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	/**
	 * @return the fechaProcesamiento
	 */
	public String getFechaProcesamiento() {
		return fechaProcesamiento;
	}

	/**
	 * @param fechaProcesamiento the fechaProcesamiento to set
	 */
	public void setFechaProcesamiento(String fechaProcesamiento) {
		this.fechaProcesamiento = fechaProcesamiento;
	}

	/**
	 * @return the statusProceso
	 */
	public Integer getStatusProceso() {
		return statusProceso;
	}

	/**
	 * @param statusProceso the statusProceso to set
	 */
	public void setStatusProceso(Integer statusProceso) {
		this.statusProceso = statusProceso;
	}

	/**
	 * @return the folioDetalle
	 */
	public Integer getFolioDetalle() {
		return folioDetalle;
	}

	/**
	 * @param folioDetalle the folioDetalle to set
	 */
	public void setFolioDetalle(Integer folioDetalle) {
		this.folioDetalle = folioDetalle;
	}

	/**
	 * @return the noContratoSica
	 */
	public String getNoContratoSica() {
		return noContratoSica;
	}

	/**
	 * @param noContratoSica the noContratoSica to set
	 */
	public void setNoContratoSica(String noContratoSica) {
		this.noContratoSica = noContratoSica;
	}

	/**
	 * @return the tipoDeCambio
	 */
	public Double getTipoDeCambio() {
		return tipoDeCambio;
	}

	/**
	 * @param tipoDeCambio the tipoDeCambio to set
	 */
	public void setTipoDeCambio(Double tipoDeCambio) {
		this.tipoDeCambio = tipoDeCambio;
	}

	/**
	 * @return the claveReferencia
	 */
	public String getClaveReferencia() {
		return claveReferencia;
	}

	/**
	 * @param claveReferencia the claveReferencia to set
	 */
	public void setClaveReferencia(String claveReferencia) {
		this.claveReferencia = claveReferencia;
	}
	
}
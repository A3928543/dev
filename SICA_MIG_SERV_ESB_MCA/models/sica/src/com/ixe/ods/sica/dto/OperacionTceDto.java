package com.ixe.ods.sica.dto;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.ixe.ods.sica.model.SistemaTce;

/**
 * Contiene los datos necesarios para llevar a cabo una operacion de Tipo de cambio especial
 * 
 * @author Cesar Jeronimo Gomez
 */
public class OperacionTceDto {
	
	/**
     * Constante para el tipo de operaci&oacute;n de compra.
     */
    public static final String OPERACION_COMPRA = "Compra";

    /**
     * Constante para el tipo de operaci&oacute;n de venta.
     */
    public static final String OPERACION_VENTA = "Venta";
    
    /** Identificador del deal generado o a consultar */
    private String dealNumber;
	
	/** Fecha de operacion */
	private Date fechaOperacion;
	
	/** Sistema que captura autorizacion TCE */
	private SistemaTce sistema;
	
	/** Contrato corto del cliente (contraparte) con el que opera */
	private String contratoCorto;
	
	/** Nombre del cliente que corresponde al contrato corto */
	private String nombreClienteContratoCorto;
	
	/** Clave del promotor que captura */
	private String usuarioPromotor;
	
	/** Nombre del promotor que captura */
	private String nombreUsuarioPromotor;
	
	/** Tipo de operacion C/V */
	private String tipoOperacion;
	
	/** Fecha valor o Tipo valor (CASH, TOM, ...) */
	private String fechaValor;
	
	/** Fecha de liquidacion de la operacion */
	private Date fechaLiquidacion;
	
	/** Arbitraje: indica si se captura el segundo detalle de la operacion, la parte contraria del detalle principal */
	private boolean arbitraje;
	
	/** Observaciones de la operacion */
	private String observaciones;
	
	/** Detalle de la operacion (no arbitraje) */
	private DetalleOperacionTceDto detalleOperacion;
	
	/** Detalle de la operacion de arbitraje */
	private DetalleOperacionTceDto detalleOperacionArbitraje;
	
	/** Instante en el que inicia la captura de un deal */
	private Date dealInputIniTime;
	
	/**
	 * Obtiene {@link #dealNumber}
	 * 
	 * @return
	 */
	public String getDealNumber() {
		if(dealNumber == null) {
			dealNumber = "";
		}
		return dealNumber;
	}

	/**
	 * Establece el valor de {@link #dealNumber}
	 * @param dealNumber
	 */
	public void setDealNumber(String dealNumber) {
		this.dealNumber = dealNumber;
	}

	public String getTipoOperacion() {
		if(tipoOperacion == null) {
			tipoOperacion = "";
		}
		return tipoOperacion;
	}
	
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public boolean isArbitraje() {
		return arbitraje;
	}
	
	public void setArbitraje(boolean arbitraje) {
		this.arbitraje = arbitraje;
	}
	
	public String getObservaciones() {
		if(observaciones == null) {
			observaciones = "";
		}
		return StringUtils.trim(observaciones);
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public String getFechaValor() {
		if(fechaValor == null) {
			fechaValor = "";
		}
		return fechaValor;
	}
	
	public void setFechaValor(String fechaValor) {
		this.fechaValor = fechaValor;
	}

	public Date getFechaLiquidacion() {
		return fechaLiquidacion;
	}
	
	public void setFechaLiquidacion(Date fechaLiquidacion) {
		this.fechaLiquidacion = fechaLiquidacion;
	}
	
	public Date getFechaOperacion() {
		return fechaOperacion;
	}
	
	public void setFechaOperacion(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}

	public SistemaTce getSistema() {
		return sistema;
	}
	
	public void setSistema(SistemaTce sistema) {
		this.sistema = sistema;
	}

	public String getContratoCorto() {
		if(contratoCorto == null) {
			contratoCorto = "";
		}
		return StringUtils.trim(contratoCorto);
	}
	
	public void setContratoCorto(String contratoCorto) {
		this.contratoCorto = contratoCorto;
	}

	public String getNombreClienteContratoCorto() {
		if(nombreClienteContratoCorto == null) {
			nombreClienteContratoCorto = "";
		}
		return nombreClienteContratoCorto;
	}
	
	public void setNombreClienteContratoCorto(String nombreClienteContratoCorto) {
		this.nombreClienteContratoCorto = nombreClienteContratoCorto;
	}

	public String getUsuarioPromotor() {
		if(usuarioPromotor == null) {
			usuarioPromotor = "";
		}
		return StringUtils.trim(usuarioPromotor);
	}
	
	public void setUsuarioPromotor(String usuarioPromotor) {
		this.usuarioPromotor = usuarioPromotor;
	}

	public String getNombreUsuarioPromotor() {
		if(nombreUsuarioPromotor == null) {
			nombreUsuarioPromotor = "";
		}
		return nombreUsuarioPromotor;
	}
	
	public void setNombreUsuarioPromotor(String nombreUsuarioPromotor) {
		this.nombreUsuarioPromotor = nombreUsuarioPromotor;
	}

	public DetalleOperacionTceDto getDetalleOperacion() {
		if(detalleOperacion == null) {
			detalleOperacion = new DetalleOperacionTceDto();
		}
		return detalleOperacion;
	}

	public void setDetalleOperacion(DetalleOperacionTceDto detalleOperacion) {
		this.detalleOperacion = detalleOperacion;
	}

	public DetalleOperacionTceDto getDetalleOperacionArbitraje() {
		if(detalleOperacionArbitraje == null) {
			detalleOperacionArbitraje = new DetalleOperacionTceDto();
		}
		return detalleOperacionArbitraje;
	}

	public void setDetalleOperacionArbitraje(DetalleOperacionTceDto detalleOperacionArbitraje) {
		this.detalleOperacionArbitraje = detalleOperacionArbitraje;
	}

	public Date getDealInputIniTime() {
		return dealInputIniTime;
	}

	public void setDealInputIniTime(Date dealInputIniTime) {
		this.dealInputIniTime = dealInputIniTime;
	}

	public boolean isCompra() {
		return OPERACION_COMPRA.equals(tipoOperacion);
	}
	
	public boolean isVenta() {
		return OPERACION_VENTA.equals(tipoOperacion);
	}

	public String toString() {
		return "OperacionTceDto [" 
			+ "dealNumber=" + dealNumber
			+ ", fechaOperacion=" + fechaOperacion
			+ ", sistema=" + (sistema == null ? null : sistema.getDescription()) + ", contratoCorto=" + contratoCorto
			+ ", nombreClienteContratoCorto=" + nombreClienteContratoCorto
			+ ", usuarioPromotor=" + usuarioPromotor
			+ ", nombreUsuarioPromotor=" + nombreUsuarioPromotor
			+ ", tipoOperacion=" + tipoOperacion + ", fechaValor="
			+ fechaValor + ", fechaLiquidacion=" + fechaLiquidacion
			+ ", arbitraje=" + arbitraje + ", observaciones="
			+ observaciones + ", detalleOperacion=" + detalleOperacion
			+ ", detalleOperacionArbitraje=" + detalleOperacionArbitraje
			+ "]";
	}

}

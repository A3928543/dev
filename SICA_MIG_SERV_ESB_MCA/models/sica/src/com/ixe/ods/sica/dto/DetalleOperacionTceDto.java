package com.ixe.ods.sica.dto;

import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.model.Divisa;

/**
 * Contiene los datos de un detalle de una operacion
 * 
 * @author Cesar Jeronimo Gomez
 */
public class DetalleOperacionTceDto {
	
	/** Valor por default del estatus de confirmacion de desviacion tc, cuando aun no se detecta desviacion */
	public static final int DESV_TC_NOT_DETECTED = 0;
	
	/** 
	 * Valor del estatus de confirmacion de desviacion tc, cuando se detecta desviacion y se requiere 
	 * preguntar confirmacion al usuario de mesa de cambios 
	 */
	public static final int DESV_TC_DETECTED = 1;
	
	/**
	 * Valor del estatus de confirmacion de desviacion tc, cuando se se ha confirmado el uso del tc con desviacion
	 */
	public static final int DESV_TC_CONFIRMED = 2;
	
	/** Divisa */
	private Divisa divisa;
	
	/** Producto o forma liquidacion */
	private String producto;
	
	/** Monto de la operacion */
	private double monto;
	
	/** Tipo de cambio cliente */
	private double tcCliente;
	
	/** Tipo de cambio mesa */
	private double tcMesa;
	
	/** Contraimporte MXN equivalente al monto */
	private double contraimporte;
	
	/** Utilidad generada por el detalle de la operacion */
	private double utilidad;
	
	/** Indica si la desviacion de tc mesa ha sido confirmada por el usuario de mesa de cambios o en espera de confirmacion */
	private int desvTcmConfirmStatus;
	
	/** Indica si la desviacion de tc cliente ha sido confirmada por el usuario de mesa de cambios o en espera de confirmacion */
	private int desvTccConfirmStatus;
	
	/**
	 * Default constructor
	 */
	public DetalleOperacionTceDto() {
		super();
		setDesvTccConfirmStatus(DetalleOperacionTceDto.DESV_TC_NOT_DETECTED);
    	setDesvTcmConfirmStatus(DetalleOperacionTceDto.DESV_TC_NOT_DETECTED);
	}

	/**
	 * Full constructor
	 * 
	 * @param divisa
	 * @param producto
	 * @param monto
	 * @param tcCliente
	 * @param tcMesa
	 * @param contraimporte
	 * @param utilidad
	 */
	public DetalleOperacionTceDto(Divisa divisa, String producto, double monto,
			double tcCliente, double tcMesa, double contraimporte,
			double utilidad) {
		super();
		this.divisa = divisa;
		this.producto = producto;
		this.monto = monto;
		this.tcCliente = tcCliente;
		this.tcMesa = tcMesa;
		this.contraimporte = contraimporte;
		this.utilidad = utilidad;
		setDesvTccConfirmStatus(DetalleOperacionTceDto.DESV_TC_NOT_DETECTED);
    	setDesvTcmConfirmStatus(DetalleOperacionTceDto.DESV_TC_NOT_DETECTED);
	}

	public Divisa getDivisa(){
		return divisa;
	}
	
	public void setDivisa(Divisa divisa) {
		this.divisa = divisa;
	}

	public String getProducto() {
		if(producto == null) {
			producto = "";
		}
		return producto;
	}
	
	public void setProducto(String producto) {
		this.producto = producto;
	}

	public double getMonto() {
		return Redondeador.redondear2Dec(monto);
	}
	
	public void setMonto(double monto) {
		this.monto = monto;
	}

	public double getTcCliente() {
		return Redondeador.redondear6Dec(tcCliente);
	}
	
	public void setTcCliente(double tcCliente) {
		this.tcCliente = tcCliente;
	}

	public double getTcMesa() {
		return Redondeador.redondear6Dec(tcMesa);
	}
	
	public void setTcMesa(double tcMesa) {
		this.tcMesa = tcMesa;
	}

	public double getContraimporte() {
		return Redondeador.redondear2Dec(contraimporte);
	}
	
	public void setContraimporte(double contraimporte) {
		this.contraimporte = contraimporte;
	}

	public double getUtilidad() {
		return Redondeador.redondear2Dec(utilidad);
	}
	
	public void setUtilidad(double utilidad) {
		this.utilidad = utilidad;
	}

	public int getDesvTcmConfirmStatus() {
		return desvTcmConfirmStatus;
	}

	public void setDesvTcmConfirmStatus(int desvTcmConfirmStatus) {
		this.desvTcmConfirmStatus = desvTcmConfirmStatus;
	}

	public int getDesvTccConfirmStatus() {
		return desvTccConfirmStatus;
	}

	public void setDesvTccConfirmStatus(int desvTccConfirmStatus) {
		this.desvTccConfirmStatus = desvTccConfirmStatus;
	}

	/**
	 * Obtiene el id divisa de este detalle 
	 * 
	 * @return
	 */
	public String safeGetIdDivisa() {
		if(divisa == null) {
			return "";
		} else {
			return divisa.getIdDivisa();
		}
	}

	public String toString() {
		return "DetalleOperacionTce [divisa=" + (divisa == null ? null : divisa.getIdDivisa()) + ", producto="
			+ producto + ", monto=" + monto + ", tcCliente=" + tcCliente
			+ ", tcMesa=" + tcMesa + ", contraimporte=" + contraimporte
			+ ", utilidad=" + utilidad + "]";
	}

}

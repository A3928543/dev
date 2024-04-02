package com.ixe.ods.sica.vo;

/**
 * Contiene la informacion de un deal para el modulo de informacion de
 * negociacion
 * 
 * @author Cesar Jeronimo Gomez
 */
public class InfoNegociacionDealVO {

	/** Identificador del deal */
	private int idDeal;

	/** Fecha de captura del deal */
	private String fechaCaptura;

	/** Tipo de operacion compra/venta */
	private String tipoOperacion;

	/** Fecha valor de la operacion */
	private String fechaValor;

	/** Monto de la operacion que ha afectado posicion */
	private String monto;

	/** Divisa de la operacion que ha afectado posicion */
	private String divisa;

	/** Tipo de cambio cliente que ha afectado la posicion */
	private String tcCliente;

	/** Diferencial tcCliente contra tcMesa */
	private String spread;

	/**
	 * Full constructor
	 * 
	 * @param idDeal
	 * @param fechaCaptura
	 * @param tipoOperacion
	 * @param fechaValor
	 * @param monto
	 * @param divisa
	 * @param tcCliente
	 * @param spread
	 */
	public InfoNegociacionDealVO(int idDeal, String fechaCaptura,
			String tipoOperacion, String fechaValor, String monto,
			String divisa, String tcCliente, String spread) {
		super();
		this.idDeal = idDeal;
		this.fechaCaptura = fechaCaptura;
		this.tipoOperacion = tipoOperacion;
		this.fechaValor = fechaValor;
		this.monto = monto;
		this.divisa = divisa;
		this.tcCliente = tcCliente;
		this.spread = spread;
	}

	public int getIdDeal() {
		return idDeal;
	}

	public String getFechaCaptura() {
		if (fechaCaptura == null)
			fechaCaptura = "";
		return fechaCaptura;
	}

	public String getTipoOperacion() {
		if (tipoOperacion == null)
			tipoOperacion = "";
		return tipoOperacion;
	}

	public String getFechaValor() {
		if (fechaValor == null)
			fechaValor = "";
		return fechaValor;
	}

	public String getMonto() {
		return monto;
	}

	public String getDivisa() {
		if (divisa == null)
			divisa = "";
		return divisa;
	}

	public String getTcCliente() {
		return tcCliente;
	}

	public String getSpread() {
		return spread;
	}

	public String toString() {
		return "InfoNegociacionDealVO [idDeal=" + idDeal + ", fechaCaptura="
				+ fechaCaptura + ", tipoOperacion=" + tipoOperacion
				+ ", fechaValor=" + fechaValor + ", monto=" + monto
				+ ", divisa=" + divisa + ", tcCliente=" + tcCliente
				+ ", spread=" + spread + "]";
	}

}

package com.ixe.ods.sica.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class OperacionClienteBanorteCrDto 
{
	private String contratoSica = null;
	private String cliente = null;
	private String promotor = null;
	private String sic = null;
	private Integer sucursalOrigen = null;
	private String cuentaCheques = null;
	private String idCanal = null;
	private BigDecimal tipoCambioMesa = null;
	private BigDecimal tipoCambioPosicion = null;
	private BigDecimal utilidadAcumulada = null;
	private BigDecimal monto = null;
	private Integer idDeal = null;
	private boolean isRecibimos = false;
	private boolean primerCargoCuenta = false;
	
	
	private String idDivisa = null; 
	private String mnemonico = null;
	private int folioDetalle = 0;
	
	private final DecimalFormat formateador = new DecimalFormat("###,###,##0.00");
	
	public OperacionClienteBanorteCrDto(){}
	
	public OperacionClienteBanorteCrDto(
			Integer idDeal,
			String contratoSica,
			Integer sucursalOrigen,
			String idDivisa,
			String mnemonico,
			String promotor,
			String cliente,
			String idCanal, 
			String sic, 
			String cuentaCheques,
			BigDecimal monto,
			BigDecimal tipoCambioMesa,
			BigDecimal tipoCambioPosicion, 
			boolean isRecibimos,
			int folioDetalle 			 
			) {
		this.contratoSica = contratoSica;
		this.cliente = cliente;
		this.promotor = promotor;
		this.sic = sic;
		this.sucursalOrigen = sucursalOrigen;
		this.cuentaCheques = cuentaCheques;
		this.idCanal = idCanal;
		this.tipoCambioMesa = tipoCambioMesa;
		this.tipoCambioPosicion = tipoCambioPosicion;
		this.monto = monto;
		this.idDeal = idDeal;
		this.isRecibimos = isRecibimos;
		this.idDivisa = idDivisa;
		this.mnemonico = mnemonico;
		this.folioDetalle = folioDetalle;
	}
	
	/**
	 * Obtiene la utilidad con formato de moneda. Si la utilidad acumulada es null se calcula la utilidad del
	 * detalle para darle formato. En caso contrario, se da formato a la utilidad acumulada que fue seteada y 
	 * calculada por otro proceso.
	 * @return String
	 */
	public String getUtilidadConFormato()
	{
		String utilidadConFormato = "";
		BigDecimal utilidad = null;
		
		utilidad = utilidadAcumulada == null ? getUtilidadDelDetalle() : utilidadAcumulada;
		
		if(utilidad != null)
		{
			formateador.setMinimumFractionDigits(2);
			utilidadConFormato = formateador.format(utilidad);
		}
		
		return utilidadConFormato;
	}
	
	public BigDecimal getUtilidadDelDetalle() 
	{	
		BigDecimal utilidad = null;
		
		if(tipoCambioMesa != null && tipoCambioPosicion != null && monto != null)
		{
			if(isRecibimos)
				utilidad = tipoCambioMesa.subtract(tipoCambioPosicion).multiply(monto);
			else
				utilidad = tipoCambioPosicion.subtract(tipoCambioMesa).multiply(monto);
		}
			
		return utilidad;
	}
	
	public boolean isRecibimos() {
		return isRecibimos;
	}
	
	public String getContratoSica() {
		return contratoSica == null ? "" : contratoSica;
	}
	public void setContratoSica(String contratoSica) {
		this.contratoSica = contratoSica;
	}
	public String getCliente() {
		return cliente == null ? "" : cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getPromotor() {
		return promotor == null ? "" : promotor;
	}
	public void setPromotor(String promotor) {
		this.promotor = promotor;
	}
	public String getSic() {
		return sic;
	}
	public void setSic(String sic) {
		this.sic = sic;
	}
	public Integer getSucursalOrigen() {
		return sucursalOrigen;
	}
	public void setSucursalOrigen(Integer sucursalOrigen) {
		this.sucursalOrigen = sucursalOrigen;
	}
	public String getCuentaCheques() {
		return cuentaCheques == null ? "" : cuentaCheques;
	}
	public void setCuentaCheques(String cuentaCheques) {
		this.cuentaCheques = cuentaCheques;
	}
	public String getIdCanal() {
		return idCanal == null ? "" : idCanal;
	}
	public void setIdCanal(String idCanal) {
		this.idCanal = idCanal;
	}
	
	public BigDecimal getTipoCambioMesa() {
		return tipoCambioMesa;
	}
	public void setTipoCambioMesa(BigDecimal tipoCambioMesa) {
		this.tipoCambioMesa = tipoCambioMesa;
	}
	public BigDecimal getTipoCambioPosicion() {
		return tipoCambioPosicion;
	}
	public void setTipoCambioPosicion(BigDecimal tipoCambioPosicion) {
		this.tipoCambioPosicion = tipoCambioPosicion;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public BigDecimal getUtilidadAcumulada() {
		return utilidadAcumulada;
	}

	public void setUtilidadAcumulada(BigDecimal utilidadAcumulada) {
		this.utilidadAcumulada = utilidadAcumulada;
	}

	public Integer getIdDeal() {
		return idDeal;
	}

	public void setIdDeal(Integer idDeal) {
		this.idDeal = idDeal;
	}



	public String getIdDivisa() {
		return idDivisa;
	}



	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}



	public String getMnemonico() {
		return mnemonico;
	}



	public void setMnemonico(String mnemonico) {
		this.mnemonico = mnemonico;
	}



	public int getFolioDetalle() {
		return folioDetalle;
	}



	public void setFolioDetalle(int folioDetalle) {
		this.folioDetalle = folioDetalle;
	}



	public void setRecibimos(boolean isRecibimos) {
		this.isRecibimos = isRecibimos;
	}

	public boolean isPrimerCargoCuenta() {
		return primerCargoCuenta;
	}

	public void setPrimerCargoCuenta(boolean primerCargoCuenta) {
		this.primerCargoCuenta = primerCargoCuenta;
	}
}

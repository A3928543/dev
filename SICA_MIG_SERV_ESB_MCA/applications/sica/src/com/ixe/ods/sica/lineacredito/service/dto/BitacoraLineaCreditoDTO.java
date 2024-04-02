package com.ixe.ods.sica.lineacredito.service.dto;

import java.math.BigDecimal;

import com.ixe.ods.seguridad.model.IUsuario;

public class BitacoraLineaCreditoDTO {
	
	String tipoOperacion;
	IUsuario usuario;
	String observaciones;
	BigDecimal importe;
	
	public BitacoraLineaCreditoDTO(String tipoOperacion, IUsuario iUsuario,
			BigDecimal importe, String observaciones) {
		super();
		this.tipoOperacion = tipoOperacion;
		this.usuario = iUsuario;
		this.observaciones = observaciones;
		this.importe=importe;
	}
	public String getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
 	public IUsuario getUsuario() {
		return usuario;
	}
	public void setUsuario(IUsuario usuario) {
		this.usuario = usuario;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public BigDecimal getImporte() {
		return importe;
	}
	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}
	
	

}

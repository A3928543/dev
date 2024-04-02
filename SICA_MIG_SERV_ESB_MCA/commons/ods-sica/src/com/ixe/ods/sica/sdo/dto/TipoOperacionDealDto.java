package com.ixe.ods.sica.sdo.dto;

import com.ixe.ods.sica.model.DealDetalle;

/**
 * Clase que permite identificar de que flujo viene el Deal, SPLIT, GOMA, Modificacion monto del Deal.
 * @author HMASG771
 *
 */
public class TipoOperacionDealDto {
	
	private boolean split=false;
	
	private boolean goma=false;
	
	private boolean cambioProducto = false;
	
	private boolean reverso = false;
	
	private boolean lapiz = false;
	
	private DealDetalle dealDetalleOriginal;
	
	
	

	public boolean isCambioProducto() {
		return cambioProducto;
	}

	public void setCambioProducto(boolean cambioProducto) {
		this.cambioProducto = cambioProducto;
	}

	public DealDetalle getDealDetalleOriginal() {
		if(isLapiz()){
			return dealDetalleOriginal;
		}else{
			return null;
		}
	}

	public void setDealDetalleOriginal(DealDetalle dealDetalleOriginal) {
		this.dealDetalleOriginal = dealDetalleOriginal;
	}

	public boolean isLapiz() {
		return lapiz;
	}

	public void setLapiz(boolean lapiz) {
		this.lapiz = lapiz;
	}

	public boolean isReverso() {
		return reverso;
	}

	public void setReverso(boolean reverso) {
		this.reverso = reverso;
	}

	public boolean isSplit() {
		return split;
	}

	public void setSplit(boolean split) {
		this.split = split;
	}

	public boolean isGoma() {
		return goma;
	}

	public void setGoma(boolean goma) {
		this.goma = goma;
	}
	
	
	
	
	
	

}

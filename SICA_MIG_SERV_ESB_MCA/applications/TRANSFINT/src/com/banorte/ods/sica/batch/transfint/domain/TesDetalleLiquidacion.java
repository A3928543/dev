/*
 * TesDetalleLiquidacion.java
 * FacturacionSiteSap
 * Copyright (c) 2013 Banorte. All rights reserved.
 */

package com.banorte.ods.sica.batch.transfint.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Jesus Cortes
 *
 */
@Entity
@Table(name = "TES_DETALLE_LIQUIDACION")
public class TesDetalleLiquidacion implements Serializable {

	private static final long serialVersionUID = 8055193845643325809L;
	
	public TesDetalleLiquidacion() {
		super();
	}
	
	/**
	 * Instantiates a new tes detalle liquidacion
	 *
	 * @param idDetalleLiquidacion the id del detalle de liquidacion
	 * @param idLiquidacion the id de la liquidacion
	 */
	public TesDetalleLiquidacion(Long idDetalleLiquidacion, Long idLiquidacion) {
		this.idDetalleLiquidacion = idDetalleLiquidacion;
		this.idLiquidacion = idLiquidacion;
	}
	
	@Id
	@Column(name = "ID_DETALLE_LIQUIDACION", nullable = false)
	private Long idDetalleLiquidacion;
	

	public Long getIdDetalleLiquidacion() {
		return idDetalleLiquidacion;
	}


	public void setIdDetalleLiquidacion(Long idDetalleLiquidacion) {
		this.idDetalleLiquidacion = idDetalleLiquidacion;
	}

	@Column(name = "ID_LIQUIDACION", nullable = false)
	private Long idLiquidacion;

	public Long getIdLiquidacion() {
		return idLiquidacion;
	}


	public void setIdLiquidacion(Long idLiquidacion) {
		this.idLiquidacion = idLiquidacion;
	}
	
}

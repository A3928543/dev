/*
 * TesLiquidacion.java
 * TRANSFINT
 * Copyright (c) 2020 Banorte. All rights reserved.
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
@Table(name = "TES_LIQUIDACION")

public class TesLiquidacion implements Serializable {
	
	private static final long serialVersionUID = 8055193845643325809L;
	
	public TesLiquidacion() {
		super();
	}
	
	
	@Id
	@Column(name = "ID_LIQUIDACION", nullable = false)
	private Long idLiquidacion;
	

	public Long getIdLiquidacion() {
		return idLiquidacion;
	}


	public void setIdLiquidacion(Long idLiquidacion) {
		this.idLiquidacion = idLiquidacion;
	}

	@Column(name = "NUMERO_ORDEN", nullable = false)
	private Long numeroOrden;

	public Long getNumeroOrden() {
		return numeroOrden;
	}


	public void setNumeroOrden(Long numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

}

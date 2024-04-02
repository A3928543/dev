package com.ixe.ods.sica.batch.dao;

import java.math.BigInteger;

import com.ixe.ods.sica.batch.domain.ScDealDetalle;

public interface ScDealDetalleDao {
	
	ScDealDetalle findScDealDetalleById(BigInteger id);
	
	ScDealDetalle findScDealDetalle(BigInteger idDeal, BigInteger idDetalleLiq);
	
}

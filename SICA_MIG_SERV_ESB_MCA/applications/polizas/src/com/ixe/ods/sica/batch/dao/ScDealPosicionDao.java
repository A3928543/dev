package com.ixe.ods.sica.batch.dao;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.ixe.ods.sica.batch.domain.ScDealPosicion;

public interface ScDealPosicionDao {
	
	ScDealPosicion findScDealPosicionById(BigInteger id);
	
	BigDecimal findTipoCambioScDealPosicionById(BigInteger id);
}

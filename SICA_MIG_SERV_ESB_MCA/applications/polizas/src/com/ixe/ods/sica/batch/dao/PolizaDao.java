package com.ixe.ods.sica.batch.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.ixe.ods.sica.batch.domain.ScPoliza;

public interface PolizaDao {
	
	ScPoliza findScPolizaById(BigDecimal id);
	
	List<ScPoliza> findScPolizasBySapReferencia(String ref);
	
	Object[] findPolizaInfoBySapReferencia(String ref);
	
	Object[] findPolizaInfoBySapAGenPolXS(BigInteger idDeal, String sapRef, 
			String cargoAbono, String cuentaContable, String idDivisa);
	
}

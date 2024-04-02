package com.ixe.ods.sica.batch.dao;

import java.math.BigInteger;

import com.ixe.ods.sica.batch.domain.BupPersona;
import com.ixe.ods.sica.batch.domain.BupPersonaCuentaRol;

public interface BupPersonaCuentaRolDao {
	
	static final String ID_ROL_TITULAR = "TIT";
	
	BupPersonaCuentaRol findBupPersonaCuentaRolById(BigInteger idPersona, 
			String noCuenta, String idRol);
	
	BupPersona findPersonaByCuentaRol(String noCuenta, String idRol);
	
	String findTipoPersonaTitularByCuenta(String noCuenta);
	
	Object[] findInfoPersonaTitularByCuenta(String noCuenta);
}

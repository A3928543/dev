package com.ixe.ods.sica.batch.dao;

import java.math.BigInteger;

import com.ixe.ods.sica.batch.domain.ScCliente;

public interface ScClienteDao {
	
	ScCliente findScClienteById(Integer id);
	
	String findSicByIdPersona(BigInteger idPersona);
	
	ScCliente findScClienteByIdPersona(BigInteger idPersona);
}

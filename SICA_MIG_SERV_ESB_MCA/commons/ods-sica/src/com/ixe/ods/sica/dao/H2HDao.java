package com.ixe.ods.sica.dao;

import java.util.List;

import com.ixe.ods.sica.model.RegulatorioDatosPM;

public interface H2HDao 
{
	List findDetalleH2HByIdDealPosicion(int idDealPosicion);
	List findDealsEnAltaProcesandoceCapturadosHoy();
	List findDetallesH2HDeal(int idDeal);
	String findClaveBanxico(String contratoSica, Integer idPersonaCliente);
	RegulatorioDatosPM findDatosRegulatoriosPM(String contratoSica, Integer idPersonaCliente);
	void save(Object object);
	void update(Object object);
}

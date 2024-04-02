package com.banorte.contrapartes.dao;

import java.util.List;

import com.banorte.contrapartes.dto.OperacionDto;

public interface IConsultaDao 
{
	public int consultarEstadoSistema();
	public List<OperacionDto> consultaDeals();
}

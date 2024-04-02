package com.banorte.contrapartes.services;

import java.util.HashMap;
import java.util.List;

import com.banorte.contrapartes.dto.OperacionDto;

public interface IOperacionesDeal 
{
	public List<OperacionDto> consultaDeals();
	public void enviarEmail(HashMap<String , Object> datosReporte);
	public int consultarEstadoSistema();
}

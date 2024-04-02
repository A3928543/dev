package com.ixe.ods.sica.services;

import java.util.Map;

import com.banorte.www.ws.exception.SicaAltamiraException;
import com.ixe.ods.sica.dto.DatosBasicosClienteAltamiraDto;

public interface MantenerDatosBasicosService {
	public DatosBasicosClienteAltamiraDto getDatosBasicosClienteAltamira(Map parameters) throws SicaAltamiraException;
}

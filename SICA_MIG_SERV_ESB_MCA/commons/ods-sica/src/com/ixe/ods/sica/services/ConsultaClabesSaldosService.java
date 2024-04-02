package com.ixe.ods.sica.services;

import java.util.Map;

import com.banorte.www.ws.exception.SicaAltamiraException;
import com.ixe.ods.sica.dto.InfoCuentaAltamiraDto;

public interface ConsultaClabesSaldosService {
	public InfoCuentaAltamiraDto consultaInformacionCuenta(Map param) throws SicaAltamiraException;
}

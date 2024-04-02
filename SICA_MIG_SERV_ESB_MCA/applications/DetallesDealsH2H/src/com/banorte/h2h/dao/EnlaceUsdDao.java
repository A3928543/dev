package com.banorte.h2h.dao;

import java.util.List;

import com.banorte.h2h.dto.DetalleH2H;

public interface EnlaceUsdDao 
{
	String FECHA_ACTUAL = "FECHAACTUAL";
	String IDS = "IDS";
	String CONSULTA_FOLIOS_BANXICO = "SELECT FOLIO_REF, FOLIO_BANXICO FROM DL_OPERACION WHERE FECHA_CAP = FECHAACTUAL AND FOLIO_REF IN (IDS)";
	
	List<DetalleH2H> consultarFoliosBanxicoDetallesSica(List<DetalleH2H> detallesProcesar);
}

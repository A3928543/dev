package com.ixe.ods.sica.batch.dao;

import com.ixe.ods.sica.batch.domain.ScLoteProcesado;

public interface ScLoteProcesadoDao {
	
	void saveScLoteProcesado(ScLoteProcesado lote);
	
	ScLoteProcesado findScLoteProcesadoById(Long id);
	
	Long findMaxLoteProcesado();
	
}

package com.banorte.ods.sica.batch.transfint.dao;

import com.banorte.ods.sica.batch.transfint.domain.TesLiquidacion;

public interface TesLiquidacionDao {
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the tes_detalle_liquidacion
	 */
	TesLiquidacion findById(Long id);

	void save(TesLiquidacion liquidacion);

	void update(TesLiquidacion liquidacion);

}

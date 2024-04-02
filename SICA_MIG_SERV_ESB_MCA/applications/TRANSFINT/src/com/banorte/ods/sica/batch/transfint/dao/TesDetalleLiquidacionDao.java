package com.banorte.ods.sica.batch.transfint.dao;

import com.banorte.ods.sica.batch.transfint.domain.TesDetalleLiquidacion;

public interface TesDetalleLiquidacionDao {

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the tes_detalle_liquidacion
	 */
	TesDetalleLiquidacion findById(Long id);

	void save(TesDetalleLiquidacion detalleLiquidacion);

	void update(TesDetalleLiquidacion detalleLiquidacion);
	
}

package com.ixe.ods.sica.batch.dao;

import java.util.List;

import com.ixe.ods.sica.batch.domain.ScReferenciaCruceDetalle;

public interface ScReferenciaCruceDetalleDao {
	
	ScReferenciaCruceDetalle findScReferenciaCruceDetalleById(Long id);
	
	void saveScReferenciaCruceDetalle(ScReferenciaCruceDetalle scRefDet);
	
	List<ScReferenciaCruceDetalle> findFoliosByIdRefCruce(Long idRefCruce, 
			List<String> folios);
	
	ScReferenciaCruceDetalle findScReferenciaCruceDetalleByIdRefCruceAndFolio(
			Long idRefCruce, String folio);
	
	void update(ScReferenciaCruceDetalle detalle);
}

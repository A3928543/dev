package com.ixe.ods.sica.batch.dao;

import java.util.Date;

import com.ixe.ods.sica.batch.domain.ScReferenciaCruce;

public interface ScReferenciaCruceDao {
	
	void saveScReferenciaCruce(ScReferenciaCruce referencia);
	
	ScReferenciaCruce findScReferenciaCruceById(Long id);
	
	ScReferenciaCruce findScReferenciaCruceByReferencia(Date fechaCont, String refCruce);
	
	ScReferenciaCruce findScReferenciaCruceWithRefCruce(Date fechaCont, String refCruce);
}

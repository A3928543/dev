package com.ixe.ods.sica.batch.dao;

import java.util.List;

import com.ixe.ods.sica.batch.domain.SapABitacoraXs;


public interface SapBitacoraXsDao {
	
	SapABitacoraXs findSapBitacoraXsById(Long id);
	
	List<SapABitacoraXs> findSapABitacoraXs(Long idCarga);
	
	List<SapABitacoraXs> findSapABitacoraXs(Long ini, Long fin);
	
}

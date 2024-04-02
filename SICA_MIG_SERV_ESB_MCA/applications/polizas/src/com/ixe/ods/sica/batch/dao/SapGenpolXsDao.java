package com.ixe.ods.sica.batch.dao;

import java.math.BigDecimal;
import java.util.List;

import com.ixe.ods.sica.batch.domain.SapAGenpolXs;
import com.ixe.ods.sica.batch.domain.SapAGenpolXsPK;


public interface SapGenpolXsDao {
	
	public List<SapAGenpolXs> findSapAGenpolXsByLote(Long idLote);
	
	public List<SapAGenpolXsPK> findSapAGenpolXsIdsByLote(Long idLote);
	
	public SapAGenpolXs findSapAGenpolXsById(String referencia, BigDecimal idRegistro);
	
	public SapAGenpolXs findSapAGenpolXsById(SapAGenpolXsPK pk);
}

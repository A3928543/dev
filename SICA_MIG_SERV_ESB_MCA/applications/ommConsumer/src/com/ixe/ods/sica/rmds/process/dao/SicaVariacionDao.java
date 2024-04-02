package com.ixe.ods.sica.rmds.process.dao;

import java.util.Map;

import com.ixe.ods.sica.rmds.feed.dto.CurrencyPriceDto;

public interface SicaVariacionDao {
	
	public boolean storeVariacion(Map<String, CurrencyPriceDto> sicaVariacionRow);

}

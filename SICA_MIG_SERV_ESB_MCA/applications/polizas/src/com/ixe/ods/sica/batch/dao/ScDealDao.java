package com.ixe.ods.sica.batch.dao;

import java.math.BigInteger;

import com.ixe.ods.sica.batch.domain.ScDeal;

public interface ScDealDao {
	
	ScDeal findScDealById(BigInteger id);
	
	Object[] findSpecificInfoScDealbyId(BigInteger id);
}

package com.ixe.ods.sica.rmds.process.service;

import com.ixe.ods.sica.rmds.process.stack.CurrencyPriceStack;
/**
 * 
 * @author Efren Trinidad, Na-at Technologies
 *
 */
public interface SicaVariacionService {
	
	/**
	 * 
	 * @param currencyPriceStack
	 * @param firstInvocation
	 */
	public void senseVariation(CurrencyPriceStack currencyPriceStack, boolean firstInvocation);

}

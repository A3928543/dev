package com.ixe.ods.sica.rmds.feed.session;

import java.util.Date;

import com.ixe.ods.sica.rmds.feed.exception.SicaOmmConsumerException;
import com.ixe.ods.sica.rmds.process.stack.CurrencyPriceStack;

public interface RmdsSession {

	/**
	 * Registra a un usuario con RMDS y crea el contexto para subsecuentes 
	 * solicitudes de RMD (Reuters Domain Model).
	 * 
	 * @param usr
	 */
	public boolean login(String sessionName, String usr, long loginTimeOut)
			throws SicaOmmConsumerException;

	public boolean loadDictionary() throws SicaOmmConsumerException;

	/**
	 * 
	 * @param serviceName
	 * @param rics
	 */
	public CurrencyPriceStack sendRequestMarketData(String serviceName,
			String[] rics, String[] interestView, long marketFeedTimeOut, Date marketFeedEndTime)
			throws SicaOmmConsumerException;

	public void shutdown();

}
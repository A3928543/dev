package com.ixe.ods.sica.rmds.process.service.impl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ixe.ods.sica.rmds.feed.dto.CurrencyPriceDto;
import com.ixe.ods.sica.rmds.feed.util.Helper;

import static com.ixe.ods.sica.rmds.feed.util.Constantes.*;
import com.ixe.ods.sica.rmds.process.dao.SicaVariacionDao;
import com.ixe.ods.sica.rmds.process.service.OmmConsumerNotificationService;
import com.ixe.ods.sica.rmds.process.service.SicaVariacionService;
import com.ixe.ods.sica.rmds.process.stack.CurrencyPriceStack;

/**
 * Implementaci&oacute;n que alimenta los TC al SICA utilizando como
 * medio una tabla en Base de Datos. Es encargada que reaccinar cada
 * vez que haya una variaci&oacute;n que procesar y de enviar el registro
 * completo de todas las divisas consideradas.
 * 
 * @author Efren Trinidad, Na-at Technologies
 *
 */
@Service
public class SicaVariacionServiceImpl implements SicaVariacionService {
	
	@Autowired
	@Qualifier("JdbcSicaVariacionDao")
	private SicaVariacionDao variacionDao;
	
	@Autowired
	OmmConsumerNotificationService notificacionService;
	
	/**
	 * Registro en memoria que almacena el &uacute;ltimo registro en
	 * SICA_VARIACION. Es actualizado con las variaciones le&iacute;das
	 * de la pila.
	 */
	private static Map<String, CurrencyPriceDto> sicaVariacionCache  
		= new LinkedHashMap<String, CurrencyPriceDto>();
	
	/**
	 * La instancia de utilidad para grabar mensajes en el log.
	 */
	private Logger logger = Logger.getLogger(SicaVariacionServiceImpl.class.getName());
	
	/**
	 * Escucha en la lista <code>currencyPriceStack</code> las variaciones insertadas.
	 * Una vez detectada alguna(s) variaci&oacute;n vacia la pila e invoca su procesamiento.
	 * 
	 * @param currencyPriceStack
	 * @param firstInvocation
	 */
	public void senseVariation(CurrencyPriceStack currencyPriceStack, boolean firstInvocation){
		
		logger.debug("Se escuchan las variaciones insertadas en la pila.");
		
		long waitTimeout = 5000;
		Collection<CurrencyPriceDto> currencyPrices = null;
		
		try{
			while(currencyPriceStack.isAlive()){
				
				if(currencyPriceStack.isEmpty() || firstInvocation){
					logger.info("Pila vacia, esperando variacion...");
					synchronized (currencyPriceStack) {
						currencyPriceStack.wait(waitTimeout);
					}
				}
					
				currencyPrices = currencyPriceStack.popClear();
				
				//Solo se procesa la lista si no esta vacia, puede
				//estar vacia si el wait() termino por timeout.
				if( ! currencyPrices.isEmpty()){
					processCurrencyPrices(currencyPrices);
				}
					
				if(firstInvocation){
					notificacionService.sendInitNotification(sicaVariacionCache);
				}
					
				firstInvocation = false;
					
			}
			
		} catch (InterruptedException ie) {
			logger.error(ie.getMessage(), ie);
			return;
		}
		
		logger.info("Se termina el procesamiento de variaciones");
		
		if( currencyPriceStack.getErrMsg() != null){
			notificacionService.sendErrNotification(currencyPriceStack.getErrMsg());
		}
		
		notificacionService.sendEndNotification(sicaVariacionCache);
		return;
		
	}
	
	/**
	 * Verifica los nuevos valores recibidos y, en caso de haber variaci&oacute;n 
	 * actualiza y/o completa el cache para posteriormente invocar su almacenamiento 
	 * al SICA. 
	 * 
	 * @param currencyPrices
	 */
	private void processCurrencyPrices(Collection<CurrencyPriceDto> currencyPrices ){
		
		boolean generarRegistro = false;
		
		logger.info("Procesando variaciones en pila " 
				+ currentVariationsToString(currencyPrices));
		
		for(CurrencyPriceDto currencyPrice : currencyPrices){
			if(Helper.isVariacion(sicaVariacionCache.get(currencyPrice.getRic()), currencyPrice)){
				generarRegistro = true;
				sicaVariacionCache.put(currencyPrice.getRic(), currencyPrice);
			}
		}
		
		if( ! generarRegistro ){
			logger.info("Precio(s) sin variacion en cache");
			return;
		}
		
		if(sicaVariacionCache.size() < OMM_DIV_NUM){
			logger.info("Cache de SICA_VARIACION aun no completado");
			return;
		}
		
		//Se calcula el RIC MXNEUR=RR que no se lee desde RMDS
		sicaVariacionCache.put(EUR_MXN_RIC, getMxnEurPrice(sicaVariacionCache.get(USD_RIC), 
				sicaVariacionCache.get(EUR_RIC)));
		
		if(logger.isDebugEnabled()){
			printCache();
		}
		
		variacionDao.storeVariacion(sicaVariacionCache);
		
	}
	
	/**
	 * Calcula el precio del EUR vs MXN multiplicando el factor Divisa del EUR
	 * por MXN vs USD para cada par de precios (BID y ASK).
	 * 
	 * @param mxn Precios USD vs MXN.
	 * @param eur Precios EUR vs USD
	 * @return
	 */
	private CurrencyPriceDto getMxnEurPrice(CurrencyPriceDto mxn, CurrencyPriceDto eur){
		CurrencyPriceDto mxnEurPrice = new CurrencyPriceDto();
		
		mxnEurPrice.setRic(EUR_MXN_RIC);
		mxnEurPrice.setBid(Helper.redondear6Dec(mxn.getBid() * eur.getBid()));
		mxnEurPrice.setAsk(Helper.redondear6Dec(mxn.getAsk() * eur.getAsk()));
		
		return mxnEurPrice;
		
	}
	
	/**
	 * Imprime en el log el valor de el cache de SICA_VARIACION
	 */
	private void printCache(){
		
		logger.debug("Se imprime el valor del cache SICA_VARIACION");
		
		for(CurrencyPriceDto currencyPrice : sicaVariacionCache.values()){
			logger.debug(currencyPrice.toString());
		}
	}
	
	/**
	 * Devuelve una cadena representativa de las variaciones depositadas en la 
	 * pila.
	 * 
	 * @param currencyPrices
	 * @return
	 */
	private String currentVariationsToString(Collection<CurrencyPriceDto> currencyPrices){
		
		StringBuilder sb = new StringBuilder();
		
		for(CurrencyPriceDto currencyPrice : currencyPrices){
			sb.append(currencyPrice.toString());
		}
		
		return sb.toString();
	}
}

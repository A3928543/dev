package com.ixe.ods.sica.rmds.process.stack;

import java.util.LinkedList;

import com.ixe.ods.sica.rmds.feed.dto.CurrencyPriceDto;

/**
 * Define el mecanismo de &quot;Intervalizaci&oacute;n&quot; que permite
 * adaptarse al tiempo de procesamiento de la generaci&oacute;n de pizarrones
 * en el SICA. Las variaciones son depositadas en esta lista antes de ser
 * procesadas.
 * 
 * @author Efren Trinidad, Na-at Technologies
 *
 */
public interface CurrencyPriceStack {
	
	 /**
	 * Actualiza el precio de la divisa que recibe como par&acute;metro en la lista
	 * de precios en memoria.
	 * 
	 * @param currencyPrice
	 * @param msgType
	 * @return true si la variariaci&oacute;n se inserto y se procesar&aacute;, false
	 * de otra manera.
	 */
	public boolean push(CurrencyPriceDto currencyPrice, byte msgType);
	
	/**
	 * Hace un clon de la lista que contiene los precios en memoria. No elimina
	 * los elementos en la lista original ya que sirven para identificar si en los 
	 * precios recibidos posteriormente del RMDS existe una variaci&oacute;n.
	 * @return Una copia de la lista en memoria de precios que es actualizada 
	 * por los precios recibidos del RMDS.
	 */
	public LinkedList<CurrencyPriceDto> popClear();
	
	/**
	 * Permite saber si la lista contiene variaciones que procesar.
	 * 
	 * @return true si hay variaciones que procesar, false de otra manera.
	 */
	public boolean isEmpty();
	
	/**
	 * Termina la pila para no depositar m&aacute; variaciones en ella.
	 * 
	 * @param err Exception en caso de terminar de manera anormal.
	 */
	public void shutDown( Throwable err );
	
	/**
	 * Permite saber si se seguiran depositando variaciones en esta pila o 
	 * el porceso ha terminado.
	 * 
	 * @return true si se seguiran depositando variaciones, false de otra manera.
	 */
	public boolean isAlive();
	
	/**
	 * Retorna el mensaje de error en caso de que el proceso
	 * termine de manera anormal.
	 * 
	 * @return Mensaje de error.
	 */
	public Throwable getErrMsg();
	
}

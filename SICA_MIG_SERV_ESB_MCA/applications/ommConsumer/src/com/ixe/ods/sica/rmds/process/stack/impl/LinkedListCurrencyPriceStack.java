package com.ixe.ods.sica.rmds.process.stack.impl;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.ixe.ods.sica.rmds.feed.dto.CurrencyPriceDto;
import com.ixe.ods.sica.rmds.feed.util.Helper;
import com.ixe.ods.sica.rmds.process.stack.CurrencyPriceStack;
import com.reuters.rfa.omm.OMMMsg;

/**
 * Implementaci&oacute;n LinkedList de la pila que almacena las
 * variaciones de precios en Divisas.
 * 
 * @author Efren Trinidad, Na-at Technologies
 *
 */
public class LinkedListCurrencyPriceStack extends LinkedList<CurrencyPriceDto> 
	implements CurrencyPriceStack
	{
	
	/**
	 * Permite saber si un proceso debe &quot;escuchar&quot; variaciones
	 * en esta pila (alive = true) o terminar (alive = false);
	 */
	private boolean alive = true;
	
	/**
	 * En caso de alg&uacute;n error, almacena el mensaje y el detalle
	 * que se debe informar al usuario.
	 */
	private Throwable errMsg;

	/**
	 * La instancia de utilidad para grabar mensajes en el log de proceso
	 */
	public static Logger logger = Logger.getLogger(LinkedListCurrencyPriceStack.class.getName());
	
	/**
	 * @see com.ixe.ods.sica.rmds.process.stack.CurrencyPriceStack#push(com.ixe.ods.sica.rmds.feed.dto.CurrencyPriceDto, byte)
	 */
	public synchronized boolean push(CurrencyPriceDto currencyPrice, byte msgType){
		
		logger.debug("Push price " + currencyPrice.toString());
		
		//Si ya se tiene un precio registrado para la divisa
		//se sustituye por el nuevo
		if(indexOf(currencyPrice) != -1){
			//Se valida si hay variacion respecto al valor ya almacenado,
			//se ha identificado casos en los que llega el mismo valor.
			//Posiblemente esto ya no sea necesario si habilita la OMMViewFeature.
			if( Helper.isVariacion( get(indexOf(currencyPrice)) , currencyPrice )){
				remove(indexOf(currencyPrice));
			}else{
				//No hay variacion, no se procesan datos.
				return false;
			}
		}
		
		addFirst(currencyPrice);
		
		//Se notifica que se ha registrado un nuevo precio solo si ya 
		//se han recibido los precios de todas las divisas, es decir, si
		//ya se empiezan a recibir mensajes de UPDATE 
		if( msgType == OMMMsg.MsgType.UPDATE_RESP){
			notify();
		}
		
		return true;
	}
	
	/**
	 * Implementaci&oacute;n LinkedList
	 * 
	 * @see com.ixe.ods.sica.rmds.process.stack.CurrencyPriceStack#popClear()
	 */
	public synchronized LinkedList<CurrencyPriceDto> popClear(){
		
		logger.debug("PopClear...");
		
		LinkedList<CurrencyPriceDto> clone = (LinkedList<CurrencyPriceDto>)clone();
		clear();
		return clone;
		
	}
	
	/**
	 * Terminar la inserci&oacute;n de variaciones en esta
	 * pila.
	 */
	public void shutDown( Throwable err ){
		this.alive = false;
		this.errMsg = err;
	}
	
	/**
	 * Indica si en esta pila a&uacute;n se estar&acute;n depositando
	 * variaciones. 
	 */
	public boolean isAlive() {
		return alive;
	}
	
	/**
	 * Establece el valor de <code>alive</code>
	 * @param alive
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	/**
	 * Retorna el mensaje de error en caso de que el proceso
	 * termine de manera anormal.
	 * 
	 * @return
	 */
	public Throwable getErrMsg() {
		return errMsg;
	}
	
	/**
	 * Estable el mensaje de error en caso de que el proceso
	 * termine de manera anormal.
	 * 
	 * @param errMsg
	 */
	public void setErrMsg(Throwable errMsg) {
		this.errMsg = errMsg;
	}

	/**
	 * El identificador de esta versi&oacute;n de la clase 
	 */
	private static final long serialVersionUID = 5392383367261148728L;

}

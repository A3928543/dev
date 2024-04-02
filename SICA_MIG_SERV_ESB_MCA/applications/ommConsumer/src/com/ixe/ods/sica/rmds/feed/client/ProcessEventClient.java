package com.ixe.ods.sica.rmds.feed.client;

import java.util.Date;

import org.apache.log4j.Logger;

import com.ixe.ods.sica.rmds.feed.exception.SicaOmmConsumerException;
import com.ixe.ods.sica.rmds.process.stack.CurrencyPriceStack;
import com.reuters.rfa.common.Client;
import com.reuters.rfa.common.DeactivatedException;
import com.reuters.rfa.common.DispatchQueueInGroupException;
import com.reuters.rfa.common.EventQueue;
import com.reuters.rfa.dictionary.FieldDictionary;
import com.reuters.rfa.rdm.RDMMsgTypes;

/**
 * @author Efren Trinidad, Na-at Technologies
 *
 */
public abstract class ProcessEventClient extends Thread
	implements Client{
	
	/**
	 * Queue en la cual se depositar&aacute;n los mensajes para 
	 * los cuales fue suscrito este cliente.
	 */
	protected EventQueue eventQueue;
	
	/**
	 * Tipo de eventos que escuchar&aacute; este cliente.
	 */
	protected short msgModelType;
	
	/**
	 * Bandera que indica si ya no se deden esperar m&aacute;s mensajes
	 * en el queue, es decir, el proceso de este cliente ha terminado.
	 */
	protected boolean completed;

	/**
	 * Bandera que indica se ha hecho login exitoso con el RMDS.
	 */
	protected boolean connected;

	/**
	 * La cantidad de segundos que este cliente  estar&aacute; escuchando
	 * los eventos en el Queue.
	 */
	protected long timeOut;
	
	/**
	 * La hora del d&iacute;a actual en que se debe detener el proceso
	 * de alimentaci&oacute;n de precios. Este parametro es ignorado
	 * si <code>timeOut</code> != 0.
	 */
	protected Date marketFeedEndTime;
	
	/**
	 * La instancia del diccionario con el cual
	 * se procesar&aacute;n los datos en el mensaje
	 */
	protected FieldDictionary dictionary;
	
	/**
	 * La pila donde se depositar&aacute;n los precios de las diferentes
	 * divisas.
	 */
	protected CurrencyPriceStack currencyPriceStack;
	
	/**
	 * En caso de alg&uacute;n error, almacena el mensaje y el detalle
	 * que se debe informar al usuario.
	 */
	private Throwable errMsg;
	
	/**
	 * La instancia de utilidad para grabar mensajes en el log.
	 */
	protected Logger logger = Logger.getLogger((this.getClass().getName()));
	
	/**
	 * Escucha en la Queue <code>eventQueue</code> los eventos para los que 
	 * haya sido suscrito este cliente por <code>timeOut</code> segundos si <code>timeOut</code>
	 * != 0 o en su defecto hasta la hora del d&iacute;a denotada por <code>marketFeedEndTime</code>. 
	 * Solo esperar&aacute; alg&uacute;n lapso de tiempo si su procesamiento
	 * no ha sido completado (this.completed == false);
	 */
	public void run(){
		
		long fin = 0;
		
		try{
			if( timeOut != 0){
				fin = System.currentTimeMillis() + (timeOut * 1000);
				logger.info("Eschando por eventos de " + RDMMsgTypes.toString( this.msgModelType) +
						" por " + timeOut + " segundos." );
				
			}else{
				fin = marketFeedEndTime.getTime();
				logger.info("Eschando por eventos de " + RDMMsgTypes.toString( this.msgModelType) +
						" hasta "+ marketFeedEndTime.toString() );
			}
		
			if( fin < System.currentTimeMillis()){
				throw new SicaOmmConsumerException("La hora de termino es menor a la hora actual.");
			}
		
			while( System.currentTimeMillis() < fin && ! this.completed ){
				try {
					//Wait for 1000 milliseconds if no event exist.
					eventQueue.dispatch(1000);
	            }
	            catch (DeactivatedException de) {
	            	logger.error(de.getMessage(), de);
	            	throw new SicaOmmConsumerException(de.getMessage(), de);
	            }
	            catch (DispatchQueueInGroupException dqige) {
	            	logger.error(dqige, dqige);
	            	throw new SicaOmmConsumerException(dqige.getMessage(), dqige);
	            }
			}
		
			synchronized(this){
				notify();
			}
		
			//Se termina la pila donde se depositan las variaciones
			//en caso de haber sido iniciada.
			stopProcess(null);
			
			logger.info("Se termina la ejecucion del hilo que escucha los eventos de tipo : "
					+ RDMMsgTypes.toString( this.msgModelType) );
		
		}catch(SicaOmmConsumerException soce){
			this.completed = true;
			logger.error(soce.getMessage(),soce);
			stopProcess(soce);
		}catch(Exception err){
			this.completed = true;
			logger.error(err.getMessage(),err);
			stopProcess(err);
		}
	}
	
	/**
	 * Debido a que el proceso que consume las variaciones de la pila
	 * espera a que se haga un evento push(), se debe marcar la pila como
	 * "muerta" y de esta manera indicarle que no se depositaran m&aacute;s 
	 * variaciones y que debe detenerse.
	 *
	 * @param soce Exception con el mensaje de error en caso de no terminar
	 * de manera normal.
	 */
	private void stopProcess( Throwable soce ){
		if(currencyPriceStack!= null){
			logger.info("Se notifica al hilo que escucha la pila que el proceso ha terminado.");
			currencyPriceStack.shutDown(soce);
		}
		errMsg = soce;
	}
	
	public void shutdown(){
		if(eventQueue!=null){
			eventQueue.deactivate();
		}
	}
	
	public EventQueue getEventQueue() {
		return eventQueue;
	}

	public Throwable getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(Throwable errMsg) {
		this.errMsg = errMsg;
	}

	public void setEventQueue(EventQueue eventQueue) {
		this.eventQueue = eventQueue;
	}
	
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public CurrencyPriceStack getCurrencyPriceStack() {
		return currencyPriceStack;
	}

	public void setCurrencyPriceStack(CurrencyPriceStack currencyPriceStack) {
		this.currencyPriceStack = currencyPriceStack;
	}
	
}

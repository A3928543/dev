package com.ixe.ods.sica.rmds.feed.exception;

/**
 * @author Efren Trinidad, Na-at Technologies
 *
 */
public class SicaOmmConsumerException extends Exception {
	
	public SicaOmmConsumerException(String message){
		super(message);
	}
	
	public SicaOmmConsumerException(String message, Throwable cause){
		super(message,cause);
	}
	
	/**
	 * Identificador de la versi&oacute;n de la clase.
	 */
	private static final long serialVersionUID = -6940135402596274749L;

}

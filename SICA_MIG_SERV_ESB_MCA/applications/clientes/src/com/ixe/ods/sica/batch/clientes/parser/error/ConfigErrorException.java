package com.ixe.ods.sica.batch.clientes.parser.error;

/**
 * The Class ConfigErrorException.
 */
public class ConfigErrorException extends RuntimeException {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5174119209948923317L;

	/**
	 * Instantiates a new config error exception.
	 */
	public ConfigErrorException() {
	}

	/**
	 * Instantiates a new config error exception.
	 *
	 * @param message the message
	 */
	public ConfigErrorException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new config error exception.
	 *
	 * @param cause the cause
	 */
	public ConfigErrorException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new config error exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public ConfigErrorException(String message, Throwable cause) {
		super(message, cause);
	}

}

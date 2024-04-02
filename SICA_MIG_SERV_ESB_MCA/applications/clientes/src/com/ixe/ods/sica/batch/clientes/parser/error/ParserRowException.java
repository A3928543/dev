package com.ixe.ods.sica.batch.clientes.parser.error;

/**
 * The Class ParserRowException.
 */
public class ParserRowException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1343254842471160947L;
	
	/** The detalle. */
	private String detalle;

	public ParserRowException(String message, String detalle) {
		super(message);
		this.detalle = detalle;
	}

	/**
	 * Instantiates a new parser row exception.
	 */
	public ParserRowException() {
	}

	/**
	 * Instantiates a new parser row exception.
	 *
	 * @param message the message
	 */
	public ParserRowException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new parser row exception.
	 *
	 * @param cause the cause
	 */
	public ParserRowException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new parser row exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public ParserRowException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getDetalle() {
		return detalle;
	}

}

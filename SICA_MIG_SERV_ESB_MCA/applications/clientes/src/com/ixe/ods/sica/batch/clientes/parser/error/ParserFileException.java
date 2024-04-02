package com.ixe.ods.sica.batch.clientes.parser.error;

/**
 * The Class ParserFileException.
 */
public class ParserFileException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8466942922194998967L;

	/**
	 * Instantiates a new parser file exception.
	 */
	public ParserFileException() {
	}

	/**
	 * Instantiates a new parser file exception.
	 *
	 * @param message the message
	 */
	public ParserFileException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new parser file exception.
	 *
	 * @param cause the cause
	 */
	public ParserFileException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new parser file exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public ParserFileException(String message, Throwable cause) {
		super(message, cause);
	}

}

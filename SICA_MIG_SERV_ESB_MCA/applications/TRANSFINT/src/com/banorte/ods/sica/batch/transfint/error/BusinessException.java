package com.banorte.ods.sica.batch.transfint.error;

/**
 * The Class BusinessException.
 */
public class BusinessException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new no row exception.
	 */
	public BusinessException() {
		
	}

	/**
	 * Instantiates a new no row exception.
	 *
	 * @param message the message
	 */
	public BusinessException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new no row exception.
	 *
	 * @param cause the cause
	 */
	public BusinessException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new no row exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

}

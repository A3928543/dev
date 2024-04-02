package com.banorte.ods.sica.batch.transfint.error;

/**
 * The Class SystemErrorException.
 */
public class SystemErrorException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new system error exception.
	 */
	public SystemErrorException() {
	}

	/**
	 * Instantiates a new system error exception.
	 *
	 * @param message the message
	 */
	public SystemErrorException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new system error exception.
	 *
	 * @param cause the cause
	 */
	public SystemErrorException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new system error exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public SystemErrorException(String message, Throwable cause) {
		super(message, cause);
	}

}

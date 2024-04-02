package com.ixe.ods.sica.batch.error;

/**
 * The Class FileReportException.
 */
public class FileReportException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new file report exception.
	 */
	public FileReportException() {
	}

	/**
	 * Instantiates a new file report exception.
	 *
	 * @param message the message
	 */
	public FileReportException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new file report exception.
	 *
	 * @param cause the cause
	 */
	public FileReportException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new file report exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public FileReportException(String message, Throwable cause) {
		super(message, cause);
	}

}

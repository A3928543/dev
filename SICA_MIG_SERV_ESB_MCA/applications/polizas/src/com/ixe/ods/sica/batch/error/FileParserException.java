package com.ixe.ods.sica.batch.error;

public class FileParserException extends Exception {

	private static final long serialVersionUID = 1L;

	public FileParserException() {
	}

	public FileParserException(String message) {
		super(message);
	}

	public FileParserException(Throwable cause) {
		super(cause);
	}

	public FileParserException(String message, Throwable cause) {
		super(message, cause);
	}

}

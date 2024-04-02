package com.ixe.ods.sica.batch.error;

public class RowParserException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String detail; 

	public RowParserException() {
	}

	public RowParserException(String message, String detail) {
		super(message);
		this.detail = detail;
	}

	public RowParserException(Throwable cause) {
		super(cause);
	}

	public RowParserException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RowParserException(String message) {
		super(message);
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}

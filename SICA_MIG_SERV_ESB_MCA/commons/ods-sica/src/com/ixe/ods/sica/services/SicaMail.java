package com.ixe.ods.sica.services;

public interface SicaMail {
	
	public static final String SICA_MAIL_SERVICE= "sicaMailService";
	
	public boolean sendEmail(String[] emails);

	public void setParametrosEmail(String[] parametrosEmail);
	
}

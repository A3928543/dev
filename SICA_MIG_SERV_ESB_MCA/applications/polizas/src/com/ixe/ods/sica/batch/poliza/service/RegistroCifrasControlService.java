package com.ixe.ods.sica.batch.poliza.service;

/**
 * The Interface RegistroCifrasControlService.
 */
public interface RegistroCifrasControlService {
	
	/**
	 * Registra cifras control.
	 *
	 */
	void registraCifrasControl();
	

	/**
	 * Send error email.
	 *
	 * @param mensaje the mensaje
	 */
	void sendErrorEmail(String mensaje);
}

package com.ixe.ods.sica.batch.mail;

import java.util.Map;

/**
 * The Interface MailSender.
 */
public interface MailSender {
	
	/**
	 * Send.
	 *
	 * @param model the model
	 */
	void send(Map<String, Object> model);
	
	/**
	 * Enviar correo.
	 *
	 * @param param the param
	 * @return true, if successful
	 */
	boolean enviarCorreo(Map<String, Object> param);

}

package com.banorte.ods.sica.batch.transfint.mail;

import java.util.Map;

public interface MailSender {
	
	/**
	 * Send.
	 *
	 * @param model the model
	 */
	void send(Map<String, Object> model);
	
	/**
	 * Send.
	 *
	 * @param model the model
	 */
	void send(Map<String, Object> model,String correosCopia);
	
	/**
	 * Enviar correo.
	 *
	 * @param param the param
	 * @return true, if successful
	 */
	boolean enviarCorreo(Map<String, Object> param);

}

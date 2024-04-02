package com.ixe.ods.sica.batch.clientes.mail;

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
	public void send(Map<String, Object> model);

}

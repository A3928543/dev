package com.banorte.contrapartes.services.mail;

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

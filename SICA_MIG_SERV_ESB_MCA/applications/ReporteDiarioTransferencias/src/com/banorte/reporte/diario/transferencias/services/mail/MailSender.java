package com.banorte.reporte.diario.transferencias.services.mail;

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
	
	public String getTo();
	public void setTo(String to);
	
	public String getTemplate();
	public void setTemplate(String template);
}

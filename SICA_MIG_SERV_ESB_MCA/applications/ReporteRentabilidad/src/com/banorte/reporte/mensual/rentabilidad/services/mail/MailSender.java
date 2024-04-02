package com.banorte.reporte.mensual.rentabilidad.services.mail;

import java.util.Map;

public interface MailSender 
{
	public void send(Map<String, Object> model);
	
	public String getTo();
	public void setTo(String to);
	
	public String getTemplate();
	public void setTemplate(String template);

}

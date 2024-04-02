package com.ixe.ods.sica.services.impl;

import java.text.MessageFormat;

import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.ixe.ods.sica.services.SicaMail;

public class SicaSimpleMailImpl implements SicaMail {
	
	private MailMessage mailMessage;
	
	private MailSender mailSender;
	
	public boolean sendEmail(String[] To) {
		mailMessage.setTo(To);
		mailSender.send((SimpleMailMessage)mailMessage);
		return false;
	}
	
	public void setParametrosEmail(String[] elementos){
		SimpleMailMessage simpleMailMessage = ((SimpleMailMessage)mailMessage);
		String textMail = simpleMailMessage.getText();
		simpleMailMessage.setText(MessageFormat.format(textMail, elementos));
		String subject =simpleMailMessage.getSubject();
		simpleMailMessage.setSubject(MessageFormat.format(subject,elementos));
	}

	public MailMessage getMailMessage() {
		return mailMessage;
	}

	public void setMailMessage(MailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}

	public MailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}


}

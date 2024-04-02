package com.banorte.h2h.services.impl;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;

import com.banorte.h2h.services.MailH2HSender;

public class MailH2HSenderImpl implements MailH2HSender 
{
	/** The velocity engine. */
	private VelocityEngine velocityEngine;
	
	/** The java mail sender. */
	private JavaMailSender javaMailSender;
	
	/** The template. */
	private String template;
	
	/** The from. */
	private String from;
	
	/** The to. */
	private String to;
	
	/** The subject. */
	private String subject;

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}

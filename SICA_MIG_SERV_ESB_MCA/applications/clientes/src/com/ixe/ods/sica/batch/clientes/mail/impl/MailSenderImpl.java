package com.ixe.ods.sica.batch.clientes.mail.impl;

import java.io.File;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.ixe.ods.sica.batch.clientes.mail.MailSender;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.springframework.ui.velocity.VelocityEngineUtils.mergeTemplateIntoString;

/**
 * The Class MailSenderImpl.
 */
public class MailSenderImpl implements MailSender {
	
	/** The LOG. */
	private Logger LOG = Logger.getLogger(MailSenderImpl.class);
	
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

	/**
	 * Instantiates a new mail sender impl.
	 */
	public MailSenderImpl() {
	}

	/**
	 * Send.
	 *
	 * @param model the model
	 */
	@Override
	public void send(Map<String, Object> model) {
		final String nameAttachment = (String) model.get("nameAttachment");
		final File attachment = (File) model.get("attachment");
		if (LOG.isInfoEnabled()) {
			LOG.info("getTemplate(): " + getTemplate());
			LOG.info("getFrom(): " + getFrom());
			LOG.info("getTo(): " + getTo());
			LOG.info("getSubject(): " + getSubject());
			LOG.info("model: " + model);
		}
		try {
			final String text = 
					mergeTemplateIntoString(getVelocityEngine(), getTemplate(), model);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Texto correo: " + text);
			}
			JavaMailSender sender = getJavaMailSender();
			sender.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) 
				throws MessagingException {
					MimeMessageHelper helper = 
							new MimeMessageHelper(mimeMessage, true, "ISO-8859-1");
					helper.setFrom(getFrom());
					helper.setTo(getTo());
					//helper.setCc(correoDto.getCc());
					helper.setSubject(getSubject());
					helper.setText(text, true);
					if (isNotEmpty(nameAttachment) && 
							attachment != null) {
						helper.addAttachment(nameAttachment, attachment);
					}
				}
			});
		}
		catch (MailException ex) {
			LOG.error("MailException ", ex);
		}
		catch (VelocityException ex) {
			LOG.error("VelocityException ", ex);
		}
	}

	/**
	 * Gets the from.
	 *
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Sets the from.
	 *
	 * @param from the new from
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Gets the to.
	 *
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Sets the to.
	 *
	 * @param to the new to
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject.
	 *
	 * @param subject the new subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Gets the velocity engine.
	 *
	 * @return the velocity engine
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	/**
	 * Sets the velocity engine.
	 *
	 * @param velocityEngine the new velocity engine
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	/**
	 * Gets the template.
	 *
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Sets the template.
	 *
	 * @param template the new template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Gets the java mail sender.
	 *
	 * @return the java mail sender
	 */
	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	/**
	 * Sets the java mail sender.
	 *
	 * @param javaMailSender the new java mail sender
	 */
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

}

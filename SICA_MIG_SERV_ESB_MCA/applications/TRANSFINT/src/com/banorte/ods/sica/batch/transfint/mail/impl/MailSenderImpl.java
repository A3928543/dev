package com.banorte.ods.sica.batch.transfint.mail.impl;

import java.io.File;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.banorte.ods.sica.batch.transfint.mail.MailSender;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.split;
import static org.springframework.ui.velocity.VelocityEngineUtils.mergeTemplateIntoString;


public class MailSenderImpl implements MailSender {

	/** The LOG. */
	private Logger LOG = LoggerFactory.getLogger(MailSenderImpl.class);
	
	/** The seperator mail. */
	private static String SEPERATOR_MAIL = ",";
	
	/** The env. */
	private Map<String, Object> env;
	
	/** The velocity engine. */
	private VelocityEngine velocityEngine;
	
	/** The mail sender. */
	private JavaMailSender mailSender;

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
	public void send(final Map<String, Object> model) {
		final String nameAttachment = (String) model.get("nameAttachment");
		final File attachment = (File) model.get("attachment");
		final String nameAttachment0 = (String) model.get("nameAttachment0");
		final File attachment0 = (File) model.get("attachment0");
		LOG.debug("Model: {}", model);
		LOG.debug("Enviroment: {}", env);
		try {
			String template = (String) model.get("template");
			if (isEmpty(template)) {
				template = (String) env.get("template");
			}
			final String text = 
					mergeTemplateIntoString(this.velocityEngine, template, model);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Texto correo: {}", text);
			}
			this.mailSender.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) 
				throws MessagingException {
					MimeMessageHelper helper = 
							new MimeMessageHelper(mimeMessage, 
									              true, 
									              (String) env.get("encoding"));
					helper.setFrom((String) env.get("from"));
					String to[] = split((String) env.get("to"), SEPERATOR_MAIL);
					if (to != null && to.length > 0) {
						helper.setTo(to);
					}
					String cc[] = split((String) env.get("cc"), SEPERATOR_MAIL);
					if (cc != null && cc.length > 0) {
						for(String mailcc : cc) {
							LOG.debug("mailcc: {}", mailcc);
						}
						helper.setCc(cc);
					}
					if (isNotEmpty((String) model.get("subject"))) {
						helper.setSubject((String) model.get("subject"));
					}
					else {
						helper.setSubject((String) env.get("subject"));
					}
					helper.setText(text, true);
					if (isNotEmpty(nameAttachment) && 
							attachment != null) {
						helper.addAttachment(nameAttachment, attachment);
					}
					if (isNotEmpty(nameAttachment0) && 
							attachment0 != null) {
						helper.addAttachment(nameAttachment0, attachment0);
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
	 * Enviar correo.
	 *
	 * @param param the param
	 * @return true, if successful
	 */
	@Override
	public boolean enviarCorreo(Map<String, Object> param) {
		boolean enviado = false;
		LOG.debug("param: {}", param);
		if (param != null && !param.isEmpty()) {
			String body = null;
	    	String encoding = "UTF-8";
	    	try {
	    		MimeMessage message = this.mailSender.createMimeMessage();
	    		if (this.env.containsKey("encoding")) {
	    			encoding = (String) env.get("encoding");
	    		}
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, encoding);
				if (this.velocityEngine != null && param.containsKey("template")) {
					String template = (String) param.get("template");
		    		body = mergeTemplateIntoString(this.velocityEngine, template, param);
		    	}
		    	else if (param.containsKey("text")) {
		    		body = (String) param.get("text");
		    	}
				boolean esHtml = false;
				if (param.containsKey("esHtml")) {
					esHtml = (Boolean) (param.get("esHtml"));
				}
				helper.setText(body, esHtml);
				helper.setFrom((String) param.get("from"));
				String to[] = null;
				if (param.get("to") instanceof String) {
					to = split((String) param.get("to"), ",");
				}
				else if (param.get("to") instanceof String[]) {
					to = (String[]) param.get("to");
				}
				if (to != null && to.length > 0) {
					helper.setTo(to);
				}
				String cc[] = null;
				if (param.get("cc") instanceof String) {
					cc = split((String) param.get("cc"), ",");
				}
				else if (param.get("cc") instanceof String[]) {
					cc = (String[]) param.get("cc");
				}
				if (cc != null && cc.length > 0) {
					helper.setCc(cc);
				}
				if (isNotEmpty((String) param.get("subject"))) {
					helper.setSubject((String) param.get("subject"));
				}
				String nameAttachment = (String) param.get("nameAttachment");
				File attachment = (File) param.get("attachment");
				if (StringUtils.isNotEmpty(nameAttachment) && 
						attachment != null) {
					helper.addAttachment(nameAttachment, attachment);
				}
		    	this.mailSender.send(message);
		    	enviado = true;
		    	LOG.debug("El correo fue enviado correctamente.");
	    	}
	    	catch (MailException ex) {
	    		LOG.error("MailException en enviarCorreo() " , ex);
	    	}
	    	catch(VelocityException ex) {
	    		LOG.error("VelocityException en enviarCorreo() " , ex);
	    	}
	    	catch (MessagingException ex) {
	    		LOG.error("MessagingException en enviarCorreo() " , ex);
	    	}
		}
		
		return enviado;
	}
	
	/**
	 * Send.
	 *
	 * @param model the model
	 */
	@Override
	public void send(final Map<String, Object> model,final String correosCopia) {
		final String nameAttachment = (String) model.get("nameAttachment");
		final File attachment = (File) model.get("attachment");
		final String nameAttachment0 = (String) model.get("nameAttachment0");
		final File attachment0 = (File) model.get("attachment0");
		LOG.debug("Model: {}", model);
		LOG.debug("Enviroment: {}", env);
		try {
			String template = (String) model.get("template");
			if (isEmpty(template)) {
				template = (String) env.get("template");
			}
			final String text = 
					mergeTemplateIntoString(this.velocityEngine, template, model);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Texto correo: {}", text);
			}
			this.mailSender.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) 
				throws MessagingException {
					MimeMessageHelper helper = 
							new MimeMessageHelper(mimeMessage, 
									              true, 
									              (String) env.get("encoding"));
					helper.setFrom((String) env.get("from"));
					//String to[] = split((String) env.get("to"), SEPERATOR_MAIL);
					LOG.debug("Se enviará el archivo copia del TRANSFINT a los siguientes correos:{" + correosCopia + "}");
					String to[] = split(correosCopia, SEPERATOR_MAIL);
					
					if (to != null && to.length > 0) {
						helper.setTo(to);
					}
					String cc[] = split((String) env.get("cc"), SEPERATOR_MAIL);
					if (cc != null && cc.length > 0) {
						for(String mailcc : cc) {
							LOG.debug("mailcc: {}", mailcc);
						}
						helper.setCc(cc);
					}
					if (isNotEmpty((String) model.get("subject"))) {
						helper.setSubject((String) model.get("subject"));
					}
					else {
						helper.setSubject((String) env.get("subject"));
					}
					helper.setText(text, true);
					if (isNotEmpty(nameAttachment) && 
							attachment != null) {
						helper.addAttachment(nameAttachment, attachment);
					}
					if (isNotEmpty(nameAttachment0) && 
							attachment0 != null) {
						helper.addAttachment(nameAttachment0, attachment0);
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
	 * Gets the env.
	 *
	 * @return the env
	 */
	public Map<String, Object> getEnv() {
		return env;
	}

	/**
	 * Sets the env.
	 *
	 * @param env the env
	 */
	public void setEnv(Map<String, Object> env) {
		this.env = env;
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
	 * Gets the mail sender.
	 *
	 * @return the mail sender
	 */
	public JavaMailSender getMailSender() {
		return mailSender;
	}

	/**
	 * Sets the mail sender.
	 *
	 * @param mailSender the new mail sender
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

}

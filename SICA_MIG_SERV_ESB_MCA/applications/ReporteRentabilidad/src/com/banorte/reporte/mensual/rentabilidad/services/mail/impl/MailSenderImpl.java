package com.banorte.reporte.mensual.rentabilidad.services.mail.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;



import com.banorte.reporte.mensual.rentabilidad.services.mail.MailSender;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

public class MailSenderImpl implements MailSender 
{

	
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
	public void send(Map<String, Object> model) 
	{
		final String nameAttachment = (String) model.get("nameAttachment");
		final ByteArrayOutputStream attachment = (ByteArrayOutputStream) model.get("attachment");
		
			LOG.warn("getTemplate(): " + getTemplate());
			LOG.warn("getFrom(): " + getFrom());
			LOG.warn("getTo(): " + getTo());
			LOG.warn("getSubject(): " + getSubject());
			//LOG.warn("model: " + model);
			String[] emails = getTo().split(",");
			if(emails != null && emails.length > 0)
			{
				for(String e: emails)
					LOG.warn(e);
			}
		
		try 
		{
			
			final String text = VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(), getTemplate(), "ISO-8859-1", model);
			//if (LOG.isDebugEnabled()) {
				//LOG.warn("Texto correo: " + text);
			//}
			JavaMailSender sender = getJavaMailSender();
			sender.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) 
				throws MessagingException {
					MimeMessageHelper helper = 
							new MimeMessageHelper(mimeMessage, true, "ISO-8859-1");
					helper.setFrom(getFrom());
					helper.setTo(getTo().split(","));
					//helper.setCc(correoDto.getCc());
					helper.setSubject(getSubject());
					helper.setText(text, true);
					if (isNotEmpty(nameAttachment) && attachment != null) 
					{
						InputStreamSource reporteExcel = new InputStreamSource(){
						    public InputStream getInputStream() throws IOException {
						      return new ByteArrayInputStream(attachment.toByteArray());
						    }
						  }
						;
						helper.addAttachment(nameAttachment, reporteExcel);
					}
					
					/*InputStreamSource headerjpg = new InputStreamSource(){
					    public InputStream getInputStream() throws IOException {
					    	File f = new File("img/header.jpg"); 
					    	byte jpg[] = new byte[(int)f.length()];
							FileInputStream fis = new FileInputStream(f);
							fis.read(jpg);
					      return new ByteArrayInputStream(jpg);
					    }
					  };*/
					  FileSystemResource headerBanorte = new FileSystemResource("config/header.jpg"); 
					  helper.addInline("headerBanorte", headerBanorte);
					
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

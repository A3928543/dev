/*
 * $Id: GeneralMailSenderImpl.java,v 1.4.84.1 2017/10/20 15:57:29 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.ixe.ods.sica.services.GeneralMailSender;

/**
 * Bean gen&eacute;rico para enviar mails.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.4.84.1 $ $Date: 2017/10/20 15:57:29 $
 */
public class GeneralMailSenderImpl implements GeneralMailSender {

    /**
     * @see GeneralMailSender#enviarMail(String, String[], String[], String, String).
     */
    public void enviarMail(String de, String[] para, String[] copia, String titulo,
                           String mensaje) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(de);
        msg.setTo(para);
        if (copia != null && copia.length > 0) {
            msg.setCc(copia);
        }
        msg.setSubject(titulo);
        msg.setText(mensaje);
        mailSender.send(msg);
    }

    /**
     * @see GeneralMailSender#enviarMail(String, String[], String[], String, String, String,
     *          String, byte[]).
     */
    public void enviarMail(String de, String[] para, String[] copia, String titulo,
                           String mensaje, String fileName, final String contentType,
                           final byte[] bytes) {
        try {
            MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(de);
            helper.setTo(para);
            helper.setSubject(titulo);
            helper.setText(mensaje);
            helper.addAttachment(fileName, new DataSource() {
                public InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream(bytes);
                }
                public OutputStream getOutputStream() throws IOException {
                    throw new IOException("Read-only data");
                }
                public String getContentType() {
                    return contentType;
                }
                public String getName() {
                    return "principal";
                }
            });
            ((JavaMailSender) mailSender).send(message);
        }
        catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
    
    /**
     * Enviar mail.
     *
     * @param param the param
     */
    public void enviarMail(Map param) {
    	String html = null;
    	String text = null;
    	String encoding = "UTF-8";
    	try {
    		logger.debug("param: " + param);
    		JavaMailSender jms = (JavaMailSender) this.mailSender;
    		MimeMessage message = jms.createMimeMessage();
    		if (param.containsKey("encoding")) {
    			encoding = (String) param.get("encoding");
    		}
            MimeMessageHelper helper = new MimeMessageHelper(message, true, encoding);
			if (velocityEngine != null && param.containsKey("template")) {
				String template = (String) param.get("template");
	    		html = VelocityEngineUtils.
	    				mergeTemplateIntoString(velocityEngine, template, param);
	    	}
	    	else if (param.containsKey(text)) {
	    		text = (String) param.get("text");
	    	}
			if (StringUtils.isNotEmpty(html)) {
				helper.setText(html, true);
			}
			else if (StringUtils.isNotEmpty(text)) {
				helper.setText(text, false);
			}
			helper.setFrom((String) param.get("from"));
			String to[] = null;
			if (param.get("to") instanceof String) {
				to = StringUtils.split((String) param.get("to"), ",");
			}
			else if (param.get("to") instanceof String[]) {
				to = (String[]) param.get("to");
			}
			if (to != null && to.length > 0) {
				helper.setTo(to);
			}
			String cc[] = null;
			if (param.get("cc") instanceof String) {
				cc = StringUtils.split((String) param.get("cc"), ",");
			}
			else if (param.get("cc") instanceof String[]) {
				cc = (String[]) param.get("cc");
			}
			if (cc != null && cc.length > 0) {
				helper.setCc(cc);
			}
			if (StringUtils.isNotEmpty((String) param.get("subject"))) {
				helper.setSubject((String) param.get("subject"));
			}
			if (StringUtils.isNotEmpty(html)) {
				
			}
			String nameAttachment = (String) param.get("nameAttachment");
			File attachment = (File) param.get("attachment");
			if (StringUtils.isNotEmpty(nameAttachment) && 
					attachment != null) {
				helper.addAttachment(nameAttachment, attachment);
			}
	    	jms.send(message);
	    	logger.debug("El corre fue enviado correctamente.");
    	}
    	catch (MailException ex) {
    		logger.error("MailException en enviarMail() " , ex);
    	}
    	catch(VelocityException ex) {
    		logger.error("VelocityException en enviarMail() " , ex);
    	}
    	catch (MessagingException ex) {
    		logger.error("MessagingException en enviarMail() " , ex);
    	}
    }

    /**
     * Regresa el valor de mailSender.
     *
     * @return MailSender.
     */
    public MailSender getMailSender() {
        return mailSender;
    }

    /**
     * Establece el valor de mailSender.
     *
     * @param mailSender El valor a asignar.
     */
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
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
     * El objeto para logging.
     */
    private final transient Log logger = LogFactory.getLog(getClass());

    /**
     * El objeto de spring que permite el env&iacute;o de mensajes de correo electr&oacute;nico.
     */
    private MailSender mailSender;
    
    /** The velocity engine. */
    private VelocityEngine velocityEngine;

}

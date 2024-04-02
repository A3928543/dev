package com.banorte.reporte.UIF.transferencias.services.mail.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.banorte.reporte.UIF.transferencias.services.mail.GeneralMailSender;;

public class GeneralMailSenderImpl implements GeneralMailSender{
	
    /**
     * @see GeneralMailSender#enviarMail(String, String[], String[], String, String, String,
     *          String, byte[]).
     */
    public void enviarMail(String titulo,
                           String mensaje, String fileName, final String contentType,
                           final byte[] bytes) {
        try {
            MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
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
     * El objeto para logging.
     */
    private final transient Log logger = LogFactory.getLog(getClass());

    /**
     * El objeto de spring que permite el env&iacute;o de mensajes de correo electr&oacute;nico.
     */
    private MailSender mailSender;

    public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	private String from;
    
    private String subject;
    
    private String to;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
}

package com.banorte.ods.sica.batch.transfint.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.banorte.ods.sica.batch.transfint.dao.ScParametroDao;
import com.banorte.ods.sica.batch.transfint.mail.MailSender;
import com.banorte.ods.sica.batch.transfint.util.Archivo;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static com.banorte.ods.sica.batch.transfint.util.Constantes.DEFAULT_MSG;
import static com.banorte.ods.sica.batch.transfint.util.Constantes.LOCALE_ES;
import static com.banorte.ods.sica.batch.transfint.util.Utilerias.limpiaRutaArchivo;

/**
 * The Class BaseService.
 */
public class BaseService {
	
	/** The sc parametro dao. */
	@Autowired
	private ScParametroDao scParametroDao;
	
	/** The messages. */
	@Autowired
	private ReloadableResourceBundleMessageSource messages;
	
	/** The dir errores. */
	@Value("${dir.errores}")
	private String dirErrores;
	
	/** The template error. */
	@Value("${mail.error.template}")
	private String templateError;
	
	/** The subject error. */
	@Value("${mail.error.subject}")
	private String subjectError;
	
	/** The text error. */
	@Value("${mail.error.text}")
	private String textError;
	
	/** The mail sender. */
	@Autowired
	private MailSender mailSender;

	/**
	 * Instantiates a new base service.
	 */
	public BaseService() {
	}
	
	/**
	 * Gets the message.
	 *
	 * @param key the key
	 * @param param the param
	 * @return the message
	 */
	String getMessage(String key, Object... param) {
		String mensaje = null;
		if (messages != null) {
			mensaje = messages.getMessage(key, param, DEFAULT_MSG, LOCALE_ES);
		}
		
		return mensaje;
	}
	
	/**
	 * Send error email.
	 *
	 * @param mensaje the mensaje
	 */
	public void sendErrorEmail(String mensaje) {
		Map<String, Object> model = new HashMap<String, Object>();
		if (isNotEmpty(mensaje)) {
			String nombre = "Errores" + new Date().getTime();
			Archivo archivo = new Archivo(this.dirErrores, nombre, true);
			archivo.agregar(mensaje);
			model.put("nameAttachment", nombre);
			model.put("attachment", archivo.getContent());
		}
		model.put("template", this.templateError);
		model.put("subject", this.subjectError);
		model.put("text", this.textError);
		mailSender.send(model);
		String tmpDir = limpiaRutaArchivo(this.dirErrores);
		removeDirectory(new File(tmpDir));
	}
	
	/**
	 * Send error email.
	 *
	 * @param mensaje the mensaje
	 */
	public void sendEmail(String mensaje) {
		Map<String, Object> model = new HashMap<String, Object>();
		if (isNotEmpty(mensaje)) {
			String nombre = "Errores" + new Date().getTime();
			Archivo archivo = new Archivo(this.dirErrores, nombre, true);
			archivo.agregar(mensaje);
			model.put("nameAttachment", nombre);
			model.put("attachment", archivo.getContent());
		}
		model.put("template", this.templateError);
		model.put("subject", this.subjectError);
		model.put("text", this.textError);
		mailSender.send(model);
		String tmpDir = limpiaRutaArchivo(this.dirErrores);
		removeDirectory(new File(tmpDir));
	}
	
	public void enviaCorreo(Map<String, Object> model) {
		this.mailSender.send(model);
	}
	
	/**
	 * Removes the directory.
	 *
	 * @param dir the dir
	 */
	private void removeDirectory(File dir) {
		if (dir != null) {
			if (dir.isDirectory()) {
				File[] files = dir.listFiles();
				if (files != null && files.length > 0) {
					for (File file : files) {
						removeDirectory(file);
					}
				}
				dir.delete();
			} 
			else {
				dir.delete();
			}
		}
	}

	/**
	 * Gets the mail sender.
	 *
	 * @return the mail sender
	 */
	public MailSender getMailSender() {
		return mailSender;
	}

	/**
	 * Gets the sc parametro dao.
	 *
	 * @return the sc parametro dao
	 */
	public ScParametroDao getScParametroDao() {
		return scParametroDao;
	}
}

package com.ixe.ods.sica.batch.poliza.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.ixe.ods.sica.batch.dao.ScParametroDao;
import com.ixe.ods.sica.batch.domain.ScParametro;
import com.ixe.ods.sica.batch.mail.MailSender;
import com.ixe.ods.sica.batch.util.Archivo;

import static com.ixe.ods.sica.batch.util.BatchConstants.DEFAULT_MSG;
import static com.ixe.ods.sica.batch.util.BatchConstants.LOCALE_ES;
import static com.ixe.ods.sica.batch.util.Utilerias.stringToDate;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

/**
 * The Class BaseService.
 */
public class BaseService {
	
	/** The log. */
	private Logger LOG = LoggerFactory.getLogger(BaseService.class);
	
	/** The sc parametro dao. */
	@Autowired
	private ScParametroDao scParametroDao;
	
	/** The messages. */
	@Autowired
	private ReloadableResourceBundleMessageSource messages;
	
	@Value("${dir.errores}")
	private String dirErrores;
	
	@Value("${mail.error.template}")
	private String templateError;
	
	@Value("${mail.error.subject}")
	private String subjectError;
	
	@Value("${mail.error.text}")
	private String textError;
	
	/** The formato fecha sistema. */
	@Value("${date.pattern.system}")
	private String formatoFechaSistema;
	
	/** The mail sender. */
	@Autowired
	private MailSender mailSender;
	
	/** The fecha sistema. */
	private Date fechaSistema;
	
	/** The id param fecha sistema. */
	@Value("${param.date.system}")
	private String idParamFechaSistema;

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
		removeDirectory(new File(this.dirErrores));
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
	 * Inits the fecha sistema.
	 */
	private void initFechaSistema() {
		if (this.fechaSistema == null) {
			ScParametro param = scParametroDao.findScParametroById(this.idParamFechaSistema);
			if (param != null && isNotEmpty(param.getValor())) {
				this.fechaSistema = stringToDate(param.getValor(), this.formatoFechaSistema);
			}
			if (this.fechaSistema == null) {
				this.fechaSistema = new Date();
			}
		}
		LOG.debug("this.fechaSistema: {}", this.fechaSistema);
	}

	/**
	 * Gets the fecha sistema.
	 *
	 * @return the fecha sistema
	 */
	public Date getFechaSistema() {		
		initFechaSistema();
		
		return this.fechaSistema;
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

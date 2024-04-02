package com.banorte.ods.sica.batch.transfint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * The Class Contexto.
 */
public class Contexto {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(Contexto.class);
	
	/** The Constant LOCATION_FILE. */
	private static final String LOCATION_FILE = "archivos/config/applicationContext.xml";
	
	/** The contexto. */
	private ApplicationContext contexto;

	/**
	 * Instantiates a new contexto.
	 */
	public Contexto() {
		this.contexto = new FileSystemXmlApplicationContext(LOCATION_FILE);
		LOG.debug("El contexto fue cargado correctamenete: {}", this.contexto);
	}

	/**
	 * Gets the contexto.
	 *
	 * @return the contexto
	 */
	public ApplicationContext getContexto() {
		return contexto;
	}
}

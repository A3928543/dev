/*
 * $Id: ISicaKondorService.java,v 1.1 2010/04/13 21:57:00 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.kondor.service;

import java.io.Writer;

import javax.xml.bind.JAXBException;

/**
 * The Interface ISicaKondorService.
 *
 * @author Israel Hernandez
 * @version $Revision: 1.1 $ $Date: 2010/04/13 21:57:00 $
 */
public interface ISicaKondorService {

	/** The Constant NO_MOVS_MESG. */
	public static final String NO_MOVS_MESG = "No se definieron operaciones.";

	/** The Constant OPERACION_EXITOSA. */
	public static final String OPERACION_EXITOSA = "Operacion exitosa.";

	/** The Constant FORMATO_FECHA_INVALIDO. */
	public static final String FORMATO_FECHA_INVALIDO = "El formato de la fecha es invalido.";

	/** The Constant FORMATO_NUMERICO_INVALIDO. */
	public static final String FORMATO_NUMERICO_INVALIDO = "El formato numerico es invalido.";

	/**
	 * Request.
	 *
	 * @param xml the xml
	 * @param response the response
	 *
	 * @throws JAXBException the JAXB exception
	 */
	public void request(String xml, Writer response) throws JAXBException;
}

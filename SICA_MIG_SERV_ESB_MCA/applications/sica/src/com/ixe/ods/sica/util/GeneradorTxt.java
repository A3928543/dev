/*
 * $Id: GeneradorTxt.java,v 1.1 2008/05/22 22:19:15 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Controlador que utiliza los atributos ContentType del HttPServletResponse para generar
 * una archivo de texto con el contenido especificado.
 *
 * @author Gustavo Gonzalez.
 * @version  $Revision: 1.1 $ $Date: 2008/05/22 22:19:15 $
 */
public class GeneradorTxt {

	/**
	 * Constructor vacio.
	 */
	public GeneradorTxt() {
		super();
	}
	
	/**
	 * Escribe por medio de un ByteArrayOutputStream el contenido del String
	 * definido como parametro, usando los atributos ContentType del objeto
	 * HttpServletResponse.
	 * 
	 * @param response El response de la pagina.
	 * @param nombreArchivo El nombre del archivo.
	 * @param contenido El contenido del archivo.
	 */
	public static void generarEscribir(HttpServletResponse response, String nombreArchivo,
			String contenido) {
		ByteArrayOutputStream txtReport = new ByteArrayOutputStream();
		try {
			txtReport.write(contenido.getBytes());
			response.setContentType("text/plain");
			response.setContentLength(txtReport.toByteArray().length);
			response.setHeader("Content-disposition", "attachment; filename=\"" +
					nombreArchivo + ".txt\"");
			byte[] bytes2 = txtReport.toByteArray();
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(bytes2, 0, bytes2.length);
			ouputStream.flush();
			ouputStream.close();
		}
		catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.warn("No se pudo crear el archivo de texto.", e);
			}
		}
		finally {
			if (txtReport != null) {
				try {
					txtReport.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
     * El objeto para logging.
     */
    private static final transient Log logger = LogFactory.getLog(GeneradorTxt.class);
    
}

package com.ixe.ods.sica.batch.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.io.FileUtils.writeStringToFile;

/**
 * The Class Archivo.
 */
public class Archivo {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(Archivo.class);
	
	/** The content. */
	private File content;
	
	/** The separador linea. */
	private String separadorLinea;
	
	/** The ruta. */
	private String ruta;
	
	/**
	 * Instantiates a new archivo.
	 *
	 * @param ruta the ruta
	 * @param nombre the nombre
	 * @param eliminar the eliminar
	 */
	public Archivo(String ruta, String nombre, boolean eliminar) {
		this.ruta = ruta;
		String pathname = ruta + nombre;
		separadorLinea = System.getProperty("line.separator");
		LOG.debug("Archivo: {}", pathname);
		content = new File(pathname);
		if (content.exists() && eliminar) {
			content.delete();
		}
	}
	
	/**
	 * Agregar.
	 *
	 * @param linea the linea
	 */
	public void agregar(String linea) {
		try {
			writeStringToFile(content, linea, true);
		}
		catch (IOException ex) {
			LOG.error("IOException en agregar()", ex);
		}
	}
	
	/**
	 * Agregar linea con salto.
	 *
	 * @param linea the linea
	 * @return true, if successful
	 */
	public boolean agregarLineaConSalto(String linea) {
		boolean agregar = false;
		try {
			writeStringToFile(content, linea + separadorLinea, true);
			agregar = true;
		}
		catch (IOException ex) {
			LOG.error("IOException en agregar()", ex);
		}
		
		return agregar;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public File getContent() {
		return content;
	}

	/**
	 * Gets the dir.
	 *
	 * @return the dir
	 */
	public File getDir() {
		return new File(ruta);
	}
	
}

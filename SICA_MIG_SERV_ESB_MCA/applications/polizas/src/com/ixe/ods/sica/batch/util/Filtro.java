package com.ixe.ods.sica.batch.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.lastIndexOf;
import static org.apache.commons.lang.StringUtils.startsWithIgnoreCase;
import static org.apache.commons.lang.StringUtils.upperCase;

public class Filtro implements FileFilter {
	
	private static final Logger LOG = LoggerFactory.getLogger(Filtro.class);
	
	private String prefijo;
	
	private String extension;
	
	private short diasHistorico;
	
	private Calendar fechaDepuracion;

	public Filtro() {
	}
	
	/**
	 * @param prefijo
	 * @param extension
	 * @param diasHistorico
	 */
	public Filtro(String prefijo, String extension, short diasHistorico) {
		this.prefijo = prefijo;
		this.extension = extension;
		this.diasHistorico = diasHistorico;
		initFechaDepuracion();
	}

	/**
	 * @param prefijo
	 * @param extension
	 */
	public Filtro(String prefijo, String extension) {
		this.prefijo = prefijo;
		this.extension = extension;
	}

	/**
	 * @param prefijo
	 * @param diasHistorico
	 */
	public Filtro(String prefijo, short diasHistorico) {
		this.prefijo = prefijo;
		this.diasHistorico = diasHistorico;
		initFechaDepuracion();
	}
	
	private void initFechaDepuracion() {
		if (diasHistorico > 0) {
			fechaDepuracion = Calendar.getInstance();
			fechaDepuracion.add(Calendar.DAY_OF_MONTH, -diasHistorico);
			LOG.debug("fechaDepuracion: {}", fechaDepuracion.getTime());
		}
	}
	
	private boolean isPrefijoValido(String archivo) {
		boolean result = true;
		if (isNotEmpty(prefijo)) {
			result = startsWithIgnoreCase(archivo, prefijo);
		}
		
		return result;
	}
	
	private boolean  isExtensionValida(String archivo) {
		boolean result = true;
		if (isNotEmpty(extension)) {
			int lon = archivo.length() - extension.length();
			int indice = lastIndexOf(upperCase(archivo), extension);
			if (lon != indice) {
				result = false;
			}
		}
		return result;
	}
	
	private boolean isFechaDepuracionValida(File file) {
		boolean result = true;
		if (fechaDepuracion != null) {
			Calendar fechaArchivo = Calendar.getInstance();
			fechaArchivo.setTimeInMillis(file.lastModified());
			if (!fechaArchivo.before(fechaDepuracion)) {
				result = false;
			}
		}
		
		return result;
	}

	public boolean accept(File pathname) {
		boolean result = false;
		if (pathname.isFile() && isPrefijoValido(pathname.getName()) && 
				isExtensionValida(pathname.getName()) && isFechaDepuracionValida(pathname)) {
			result = true;
		}
		
		return result;
	}

}

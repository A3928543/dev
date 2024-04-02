package com.ixe.ods.sica.batch.clientes.util;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import static org.apache.commons.lang.StringUtils.endsWithIgnoreCase;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isNumeric;
import static org.apache.commons.lang.StringUtils.replace;
import static org.apache.commons.lang.StringUtils.startsWithIgnoreCase;
import static org.apache.commons.lang.StringUtils.substring;

/**
 * The Class FiltroArchivo.
 */
public class FiltroArchivo implements FilenameFilter {
	
	/** The LOG. */
	private static Logger LOG = Logger.getLogger(FiltroArchivo.class);
	
	/** The tipo archivo. */
	private String tipoArchivo;
	
	/** The nombre archivo valido. */
	private String nombreArchivoValido;
	
	/** The depuracion. */
	private boolean depuracion;
	
	/** The prefijo arch errores. */
	private String prefijoArchErrores;
	
	/** The fecha limite inferior. */
	private Calendar fechaLimiteInferior;
	
	/**
	 * Instantiates a new filtro archivo.
	 *
	 * @param tipoArchivo the tipo archivo
	 * @param nombreArchivoValido the nombre archivo valido
	 * @param depuracion the depuracion
	 * @param prefijoArchErrores the prefijo arch errores
	 * @param diasHistorico the dias historico
	 */
	public FiltroArchivo(String tipoArchivo, String nombreArchivoValido,
			boolean depuracion, String prefijoArchErrores, int diasHistorico) {
		this.tipoArchivo = tipoArchivo;
		this.nombreArchivoValido = nombreArchivoValido;
		this.depuracion = depuracion;
		this.prefijoArchErrores = prefijoArchErrores;
		this.fechaLimiteInferior = calcularFechaLimite(diasHistorico);
	}

	/**
	 * Instantiates a new filtro archivo.
	 */
	public FiltroArchivo() {
	}
	
	/**
	 * Instantiates a new filtro archivo.
	 *
	 * @param tipoArchivo the tipo archivo
	 * @param nombreValido the nombre valido
	 */
	public FiltroArchivo(String tipoArchivo, String nombreValido) {
		this.tipoArchivo = tipoArchivo;
		this.nombreArchivoValido = nombreValido;
	}

	/**
	 * Accept.
	 *
	 * @param dir the dir
	 * @param name the name
	 * @return true, if successful
	 */
	@Override
	public boolean accept(File dir, String name) {
		boolean archValido = false;
		if (isDepuracion()) {
			if (startsWithIgnoreCase(name, prefijoArchErrores) && 
					endsWithIgnoreCase(name, tipoArchivo)) {
				archValido = true;
			}
			else if (startsWithIgnoreCase(name, nombreArchivoValido) && 
						endsWithIgnoreCase(name, tipoArchivo)) {
				int index = nombreArchivoValido.length();
				String strValor = substring(name, index);
				LOG.debug("strValor: " + strValor);
				strValor = replace(strValor, tipoArchivo, "");
				LOG.debug("strValor: " + strValor);
				if (isNumeric(strValor)) {
					if ((stringToDate(strValor)).before(fechaLimiteInferior)) {
						archValido = true;
					}
				}
			}
		}
		else {
			if (startsWithIgnoreCase(name, nombreArchivoValido) && 
					endsWithIgnoreCase(name, tipoArchivo)) {
				archValido = true;
			}
		}
		
		if (archValido) {
			LOG.debug("name: " + name);
		}
		
		return archValido;
	}

	/**
	 * Checks if is depuracion.
	 *
	 * @return true, if is depuracion
	 */
	public boolean isDepuracion() {
		return depuracion;
	}
	
	/**
	 * Calcular fecha limite.
	 *
	 * @param diasHistorico the dias historico
	 * @return the calendar
	 */
	private Calendar calcularFechaLimite(int diasHistorico) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -diasHistorico);
				
		return cal;
	}
	
	/**
	 * String to date.
	 *
	 * @param value the value
	 * @return the calendar
	 */
	private Calendar stringToDate(String value) {
		Calendar cal = Calendar.getInstance();
		if (isNotEmpty(value)) {
			try {
				DateFormat df = new SimpleDateFormat("ddMMyyyy");
				cal.setTime(df.parse(value));
			}
			catch (ParseException ex) {
				LOG.error("ParseException ", ex);
			}
		}
		
		return cal;
	}
}

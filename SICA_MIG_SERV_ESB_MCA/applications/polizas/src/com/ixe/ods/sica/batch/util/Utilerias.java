package com.ixe.ods.sica.batch.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixe.ods.sica.batch.error.BusinessException;

import static org.apache.commons.lang.StringUtils.isEmpty;


/**
 * The Class Utilerias.
 */
public class Utilerias {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(Utilerias.class);
	
	/** The Constant DEFAULT_PATTERN_DATE. */
	private static final String DEFAULT_PATTERN_DATE = "yyyyMMdd";

	/**
	 * Instantiates a new utilerias.
	 */
	private Utilerias() {
	}
	
	/**
	 * Date to string.
	 *
	 * @param fecha the fecha
	 * @param pattern the pattern
	 * @return the string
	 */
	public static String dateToString(Date fecha, String pattern) {
		if (fecha == null) {
			fecha = new Date();
		}
		if (isEmpty(pattern)) {
			pattern = DEFAULT_PATTERN_DATE;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(fecha);
	}
	
	/**
	 * String to date.
	 *
	 * @param value the value
	 * @param pattern the pattern
	 * @return the date
	 */
	public static Date stringToDate(String value, String pattern) {
		Date fecha = null;
		DateFormat df = null;
		LOG.debug("stringToDate({}, {})", value, pattern);
		try {
			if (isEmpty(pattern)) {
				pattern = DEFAULT_PATTERN_DATE;
			}
			df = new SimpleDateFormat(pattern);
			fecha = df.parse(value);
		}
		catch (ParseException ex) {
			LOG.error(ex.getMessage());
		}
		
		return fecha;
	}

	/**
	 * Throwable to string.
	 *
	 * @param ex the ex
	 * @return the string
	 */
	public static String throwableToString(Throwable ex) {
		String result = "";
		if (ex != null) {
			if (ex instanceof BusinessException) {
				result = ex.getMessage();
			}
			else {
			Writer writer = new StringWriter();
		    PrintWriter printWriter = new PrintWriter(writer);
		    ex.printStackTrace(printWriter);
		    result = writer.toString();
		}
		}
		
		return result;
	}
	
	/**
	 * Resetea hora fecha.
	 *
	 * @param fecha the fecha
	 * @param param the param
	 * @return the date
	 */
	public static Date reseteaHoraFecha(Date fecha, Integer...param) {
		Date result = null;
		if (fecha != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(fecha);
			int hora = 0;
			int minuto = 0;
			int segundo = 0;
			if (param != null) {
				if (param.length >= 1) {
					hora = param[0];
				}
				if (param.length >= 2) {
					minuto = param[1];
				}
				if (param.length >= 3) {
					segundo = param[2];
				}
			}
			cal.set(Calendar.HOUR_OF_DAY, hora);
			cal.set(Calendar.MINUTE, minuto);
			cal.set(Calendar.SECOND, segundo);
			result = cal.getTime();
		}
		LOG.debug("Fecha reseteada: {}", result);
		
		return result;
	}

}

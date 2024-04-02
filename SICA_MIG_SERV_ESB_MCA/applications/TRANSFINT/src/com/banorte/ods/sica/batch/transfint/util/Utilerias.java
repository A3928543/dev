package com.banorte.ods.sica.batch.transfint.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Utilerias.
 */
public class Utilerias {
	
	public static final Logger LOG = LoggerFactory.getLogger(Utilerias.class);

	/**
	 * Instantiates a new utilerias.
	 */
	private Utilerias() {
	}
	
	
	/**
	 * Resetea hora fecha.
	 *
	 * @param fecha the fecha
	 * @param horas the horas
	 * @param minutos the minutos
	 * @param segundos the segundos
	 * @return the date
	 */
	public final static Date reseteaHoraFecha(Date fecha, int horas, int minutos, int segundos) {
		Date result = fecha;
		if (result != null) {
			result = DateUtils.setHours(result, horas);
			result = DateUtils.setMinutes(result, minutos);
			result = DateUtils.setSeconds(result, segundos);
			LOG.debug("Fecha reseteada: {}", result);
		}
		
		return fecha;
	}
	
	/**
	 * Formatea fecha.
	 *
	 * @param fecha the fecha
	 * @param formato the formato
	 * @return the string
	 */
	public final static String formateaFecha(Date fecha, String formato) {
		String result = "";
		if (fecha != null) {
			if (StringUtils.isNotEmpty(formato)) {
				DateFormat df = new SimpleDateFormat(formato);
				result = df.format(fecha);
				LOG.debug("Fecha formateada: {}", result);
			}
		}
		
		return result;
	}
	
	/**
	 * Clean filename.
	 *
	 * @param filename the filename
	 * @param withExtension the with extension
	 * @return the string
	 */
	public static String limpiaRutaArchivo(String archivo) {
        if (archivo == null) return null;
        String cleanFilename = "";
        for (int i = 0; i < archivo.length(); ++i) {
        	cleanFilename += cleanChar(archivo.charAt(i));
        }
        LOG.debug("cleanFilename(" + archivo + "): " + cleanFilename);
        
        return cleanFilename;
    }

    /**
     * Clean char.
     *
     * @param aChar the a char
     * @return the char
     */
    private static char cleanChar(char aChar) {
        // 0 - 9
        for (int i = 48; i < 58; ++i) {
            if (aChar == i) return (char) i;
        }
        // 'A' - 'Z'
        for (int i = 65; i < 91; ++i) {
            if (aChar == i) return (char) i;
        }
        // 'a' - 'z'
        for (int i = 97; i < 123; ++i) {
            if (aChar == i) return (char) i;
        }
        // other valid characters
        switch (aChar) {
            case '/':
                return '/';
            case '.':
                return '.';
            case '-':
                return '-';
            case '_':
                return '_';
            case ' ':
                return ' ';
            case '\\':
            	return '\\';
            case ':':
            	return ':';
        }
        
        return '%';
    }
   
    /**
     * Checks if is fin de semana.
     *
     * @param fecha the fecha
     * @return true, if is fin de semana
     */
    public static boolean isFinDeSemana(Date fecha) {
    	boolean isFinDeSemana = false;
    	
    	if (fecha != null) {
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(fecha);
    		int dia = cal.get(Calendar.DAY_OF_WEEK);
    		if (dia == Calendar.SATURDAY || dia == Calendar.SUNDAY) {
    			isFinDeSemana = true;
    		}
    	}
    	
    	return isFinDeSemana;
    }

}

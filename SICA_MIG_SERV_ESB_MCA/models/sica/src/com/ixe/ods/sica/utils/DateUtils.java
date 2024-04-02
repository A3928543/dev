/*
 * $Id: DateUtils.java,v 1.11.34.3.52.1 2017/07/29 03:17:55 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ixe.ods.sica.Num;

/**
 * Utiler&iacute;a para operaciones con fechas.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.11.34.3.52.1 $ $Date: 2017/07/29 03:17:55 $
 */
public class DateUtils {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(DateUtils.class); 
	
	/** The Constant FORMATO_SIMPLE_FECHA. */
	public static final String FORMATO_FECHA_YYMMDD = "yyMMdd";
	
	/** The Constant FORMATO_FECHA_CON_GUION. */
	public static final String FORMATO_FECHA_CON_GUION = "dd-MM-yyyy";
	
	/** The Constant FORMATO_FECHA_CON_DIAGONAL. */
	public static final String FORMATO_FECHA_CON_DIAGONAL = "dd/MM/yyyy";

    /**
     * Constructor por default.
     */
    private DateUtils() {
        super();
    }

    /**
     * Regresa la fecha actual del sistema a las 00:00:00.0 horas
     *
     * @return Date.
     */
    public static Date inicioDia() {
        return inicioDia(new Date());
    }

    /**
     * Regresa la fecha especificada, cambiando la hora a las 00:00:00.0.
     *
     * @param fecha La fecha a evaluar.
     * @return Date.
     */
    public static Date inicioDia(Date fecha) {
        Calendar gc = new GregorianCalendar();
        gc.setTime(fecha);
        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);
        return gc.getTime();
    }

    /**
     * Regresa la fecha actual del sistema a las 23:59:59.999 horas
     *
     * @return Date.
     */
    public static Date finDia() {
        return finDia(new Date());
    }

    /**
     * Regresa la fecha especificada, cambiando la hora a las 23:59:59.999.
     *
     * @param fecha La fecha a evaluar.
     * @return Date.
     */
    public static Date finDia(Date fecha) {
        Calendar gc = new GregorianCalendar();
        gc.setTime(fecha);
        gc.set(Calendar.HOUR_OF_DAY, Num.I_23);
        gc.set(Calendar.MINUTE, Num.I_59);
        gc.set(Calendar.SECOND, Num.I_59);
        gc.set(Calendar.MILLISECOND, 999);
        return gc.getTime();
    }
    
    /**
     * Regresa la fecha de d&iacute;as anteriores
     *
     * @param fecha      Fecha del Sistema
     * @param diasAtras  D&iacute;as anteriores a calcular
     * @return Date.
     */
    public static Date diasPrevios(Date fecha, int diasAtras) {
        Calendar gc = new GregorianCalendar();
        gc.setTime(fecha);
        gc.add(Calendar.DAY_OF_MONTH, diasAtras);  
        return gc.getTime();
    }
    
    /**
     * Regresa el primer d&iacute;a del mes de la fecha solicitada.
     *
     * @param fecha		Fecha a evaluar.
     * @return	Date.
     */
    public static Date primerDiaDeMes(Date fecha) {
    	Calendar gc = new GregorianCalendar();
    	gc.setTime(fecha);
    	gc.set(Calendar.DAY_OF_MONTH, Num.I_1);
    	return gc.getTime();
    }
    
    /**
     * Regresa el &uacute;ltimo d&iacute;a del mes de la fecha solicitada.
     *
     * @param fecha		Fecha a evaluar.
     * @return	Date.
     */
    public static Date ultimoDiaDelMes(Date fecha) {
    	Calendar gc = new GregorianCalendar();
    	gc.setTime(fecha);
    	gc.set(Calendar.DAY_OF_MONTH, Num.I_1);
    	gc.add(Calendar.MONTH, Num.I_1);
    	gc.add(Calendar.DAY_OF_MONTH, -Num.I_1);
    	return gc.getTime();
    }
    
    /**
     * Calcula la diferencia en días entre 2 fechas
     * 
     * @param dateIni Fecha Inicial
     * @param dateEnd Fecha Final
     * @return Dias de diferencia
     */
    public static long diferenceInDays(Date dateIni, Date dateEnd) {
		double difference = (double)(dateEnd.getTime() - dateIni.getTime()) / (1000*60*60*24); 
	  	return Math.round(difference);
	}

   /**
     * Regresa cualquier mes del anio que se desea consultar.
     *
     * @param fecha Fecha a evaluar.
     * @param numeroMes El n&uacute;mero del mes que se desea, tomando en cuenta que Enero es 0 y
     * Diciembre es 11.
     * @return Date.
     */
    public static Date mesDelAnioEnCurso(Date fecha, int numeroMes) {
    	Calendar gc = new GregorianCalendar();
    	gc.setTime(fecha);
		gc.set(Calendar.MONTH, numeroMes);
		return gc.getTime();
    }
    
    /**
     * Date to string.
     *
     * @param fecha the fecha
     * @param formato the formato
     * @return the string
     */
    public static String dateToString(Date fecha, String formato) {
    	String result = null;
    	if (fecha != null && StringUtils.isNotEmpty(formato)) {
    		DateFormat format = new SimpleDateFormat(formato);
    		result = format.format(fecha);
    	}
    	
    	return result;
    }
    
    /**
     * String to date.
     *
     * @param strDate the str date
     * @param pattern the pattern
     * @return the date
     */
    public static Date stringToDate(String strDate, String pattern) {
    	Date date = null;
    	try {
	    	if (StringUtils.isNotEmpty(strDate) && 
	    			StringUtils.isNotEmpty(pattern)) {
	    		DateFormat df = new SimpleDateFormat(pattern);
	    		date = df.parse(strDate);
	    	}
    	}
    	catch (ParseException ex) {
    		log.error("ParseException en stringToDate() ", ex);
    	}
    	
    	return date;
    }
    
    /**
     * Diferencia anios.
     *
     * @param ini the ini
     * @param fin the fin
     * @return the int
     */
    public static int diferenciaAnios(Date ini, Date fin) {
	    Calendar a = getCalendar(ini);
	    Calendar b = getCalendar(fin);
	    int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
	    if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) || 
	        (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && 
	        		a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
	        diff--;
	    }
	    
	    return diff;
	}

	/**
	 * Gets the calendar.
	 *
	 * @param fecha the fecha
	 * @return the calendar
	 */
	private static Calendar getCalendar(Date fecha) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(fecha);
	    
	    return cal;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		log.debug("Fecha: " + stringToDate("990601", "yyMMdd"));
	}
    
}

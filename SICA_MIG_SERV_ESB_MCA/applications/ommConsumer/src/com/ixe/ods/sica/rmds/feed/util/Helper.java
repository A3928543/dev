package com.ixe.ods.sica.rmds.feed.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import com.ixe.ods.sica.rmds.feed.dto.CurrencyPriceDto;

/**
 * Implementa funcionalidades de prop&oacute;sito general
 * 
 * @author Efren Trinidad, Na-at Technologies
 *
 */
public class Helper {
	
	/**
	 * La instancia de utilidad para grabar mensajes en el log.
	 */
	public static Logger logger = Logger.getLogger(Helper.class.getName());
	
	/**
     * Regresa la cantidad redondeada a 6 cifras decimales.
     *
     * @param cantidad La cantidad a redondear.
     * @return double.
     */
    public static double redondear6Dec(double cantidad) {
        double res = cantidad > 0 ? cantidad + 0.0000005 : cantidad - 0.0000005;
        return ((long) (res * 1000000.0)) / 1000000.0;
    }
    
    
    /**
     * Regresa un objeto <code>java.util.Date</code> que contiene la decha del
     * d&iacute;a de hoy y la hora que recibe como par&aacute;metro.
     * 
     * @param stringDate Cadena que indica la hora del d&iacute;a de hoy que 
     * debe contener el objeto <code>java.util.Date</code> retornado en formato 
     * 24 Hrs. Solo las horas y minutos, por ejemplo: "14:30".
     * @return Date del d&iacute;a de hoy a la hora especificada, <code>null</code>
     *  si algo sale mal.
     * @throws ParseException 
     */
    public static Date hourStringToDate(String stringDate) throws ParseException{
		
    	try{
			String[] hm = stringDate.split(":");
			
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, new Integer(hm[0]));
			calendar.set(Calendar.MINUTE, new Integer(hm[1]));
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND, 0);
			
			return calendar.getTime();
			
    	}catch(Exception er){
    		logger.error("No se puede convertir la cadena " + stringDate + 
    				" debe seguir el patron HH:MM " + er);
    		throw new ParseException(er.getMessage(), 0);
    	}
    }
    
    /**
	 * Compara el par de precios BID y ASK para
	 * saber si hubo una variaci&oacute;n del precio para determinada divisa.
	 * 
	 * @param anterior Precio recibido anteriormente y almacenado
	 * @param nuevo &Uacute;ltimo precio recibido.
	 * @return true si hay alguna variaci&oacute;n, false de otra manera.
	 */
	public static boolean isVariacion(CurrencyPriceDto anterior, CurrencyPriceDto nuevo){
		
		if(anterior == null){
			logger.warn("El valor anterior para verificar variacion es null");
			return true;
		}
		
		if(nuevo == null){
			logger.warn("El valor nuevo para verificar variacion es null");
			return false;
		}
		
		if( anterior.getBid().equals(nuevo.getBid() ) &&
				anterior.getAsk().equals(nuevo.getAsk()) ){
			return false;
		}
		
		return true;
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
     * Regresa la fecha especificada, cambiando la hora a las 23:59:59.999.
     *
     * @param fecha La fecha a evaluar.
     * @return Date.
     */
    public static Date finDia(Date fecha) {
        Calendar gc = new GregorianCalendar();
        gc.setTime(fecha);
        gc.set(Calendar.HOUR_OF_DAY, 23);
        gc.set(Calendar.MINUTE, 59);
        gc.set(Calendar.SECOND, 59);
        gc.set(Calendar.MILLISECOND, 999);
        return gc.getTime();
    }

}

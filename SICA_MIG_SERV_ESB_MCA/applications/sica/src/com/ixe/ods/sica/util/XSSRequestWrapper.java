package com.ixe.ods.sica.util;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author HCIUJ950
 * F-51213- Task Force, Remediación de seguridad detectado por pruebas de caja gris
 * Clase encargada de reemplazar por vacío los siguientes parametros PropertySelection, RadioGroup, reportesPropertySelection, service, sp, superior
 */
public class XSSRequestWrapper extends HttpServletRequestWrapper{
	
	/**
	 * Arreglo de patterns para evaluar cross site scripting 
	 */
	private static Pattern[] patterns = new Pattern[]{
	    Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
	    Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	    Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
	    Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	    Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	    Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	    Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
	    Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	    Pattern.compile("on(.*?)=", Pattern.CASE_INSENSITIVE)
	};
	
	
	/**
	 * @param servletRequest
	 * Constructor simple
	 */
	public XSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    /**
     * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
     * Método heredado, valida si alguno de los parametros recibidos contienen cadenas que contengan scripts
     */
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);

        if (values == null && validNameParameter(parameter)) {
            return values;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
    		encodedValues[i] = encode(values[i]);
        }

        return encodedValues;
    }

    /** 
     * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
     * Método heredado, valida si el parametros recibido contiene cadenas que contengan scripts
     */
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if(validNameParameter(parameter)){
    		value=encode(value);
    	}
        return value;
    }
    
    /**
     * @param parameter
     * @return boolean
     * Valida el parametro recibido, si contiene las siguientes palabras: PropertySelection, RadioGroup, reportesPropertySelection, service, sp, superior
     */
    private boolean validNameParameter(String parameter){
    	if(parameter.indexOf("PropertySelection") != -1 ||
        		parameter.indexOf("RadioGroup") != -1 ||
        		parameter.indexOf("reportesPropertySelection") != -1||
        		parameter.indexOf("service") != -1||
        		parameter.indexOf("sp") != -1 ||
        		parameter.indexOf("superior") != -1){
    		return true;
    	}else{
    		return false;
    	}
    }
    
	/**
	 * @param input
	 * @return
	 * Encargado de aplicar el pattern al input recibido, si la expresion regular aplica remplaza la cadena por vacío  
	 */
	private static String encode(String input){
		if(input!=null){
	    	for(int i=0;i<patterns.length;i++ ){
	    		input = patterns[i].matcher(input).replaceAll("");
	    	}
		}
	    return input;
	}
}

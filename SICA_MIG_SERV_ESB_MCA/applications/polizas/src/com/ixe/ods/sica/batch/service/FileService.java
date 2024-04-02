package com.ixe.ods.sica.batch.service;

import static org.apache.commons.lang.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang.StringUtils.leftPad;
import static org.apache.commons.lang.StringUtils.rightPad;
import static org.apache.commons.lang.StringUtils.substring;
import static org.apache.commons.lang.StringUtils.trim;
import static org.apache.commons.lang.reflect.MethodUtils.invokeMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class FileService.
 */
public abstract class FileService {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

	/**
	 * Instantiates a new file service.
	 */
	public FileService() {
	}
	
	/**
	 * Right.
	 *
	 * @param value the value
	 * @param filler the filler
	 * @param lon the lon
	 * @return the string
	 */
	public String right(String value, String filler, Integer lon) {
		String rightpad = rightPad(value, lon, filler);
		rightpad = tuncString(rightpad, lon);
		
		return rightpad;
	}
	
	/**
	 * Left.
	 *
	 * @param value the value
	 * @param filler the filler
	 * @param lon the lon
	 * @return the string
	 */
	public String left(String value, String filler, Integer lon) {
		String leftpad = leftPad(value, lon, filler);
		leftpad = tuncString(leftpad, lon);
		
		return leftpad;
	}
	
	/**
	 * Tunc string.
	 *
	 * @param value the value
	 * @param lon the lon
	 * @return the string
	 */
	private String tuncString(String value, Integer lon) {
		if (value.length() > lon) {
			value = substring(value, 0, lon);
		}
		
		return value;
	}
	
	/**
	 * Crea renglon.
	 *
	 * @param part the part
	 * @return the string
	 */
	protected String creaRenglon(Map<String, Map<String, Object>> part) {
		String result = "";
		for (String field : part.keySet()) {
			String temp = "";
			Map<String, Object> env = part.get(field);
			String from = (String) env.get("from");
			if ("FILE".equals(from)) {
				temp = (String) env.get("value");
			}
			else if ("CODE".equals(from)) {
				Object params = env.get("params");
				LOG.debug("params: {}", params);
				if (params == null) {//Llamada sin parametros.
					temp = invokeCode(field, true);
				}
				else if (params instanceof Object[]) {//Llmada con n parametros.
					temp = invokeCode(field, true, (Object[]) params);
				}
				else {//Llamada con un solo parametro
					temp = invokeCode(field, true, params);
				}
			}
			Integer lon = (Integer) env.get("long");
			String type = (String) env.get("type");
			temp = defaultIfEmpty(temp, "");
			temp = trim(temp);
			Map<String, String> envFiller = getEnvFiller(type);
			String filler = envFiller.get("filler");
			String method = envFiller.get("method");
			temp = invokeCode(method, false, temp, filler, lon);
			if (temp.length() > lon) {
				temp = substring(temp, 0, lon);
			}
			result = result + temp;
		}
			
		return result;
	}
	
	/**
	 * Gets the env filler.
	 *
	 * @param type the type
	 * @return the env filler
	 */
	protected abstract Map<String, String> getEnvFiller(String type);
	
	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	protected abstract Map<String, Map<String, Object>> getBody();
	
	/**
	 * Crea body.
	 *
	 * @return the string
	 */
	protected String creaBody() {
		Map<String, Map<String, Object>> env = getBody();
		
		return creaRenglon(env);
	}
	
	/**
	 * Invoke code.
	 *
	 * @param field the field
	 * @param agrega the agrega
	 * @param param the param
	 * @return the string
	 */
	protected String invokeCode(String field, boolean agrega, Object...param) {
		String result = "";
		String metodo = field;
		try {
			if (agrega) {
				metodo = "get" + field;
			}
			Object ret = invokeMethod(this, metodo, param);
			if (ret != null) {
				result = ret.toString();
			}
		}
		catch (InvocationTargetException ex) {
			LOG.error("InvocationTargetException en invokeCode() ", ex);
		}
		catch (IllegalAccessException ex) {
			LOG.error("IllegalAccessException en invokeCode() ", ex);
		}
		catch (NoSuchMethodException ex) {
			LOG.error("Falta implementar el metodo a ejecutar.", ex);
		}
		
		return result;
	}

}

package com.ixe.ods.sica.batch.cfdi.parser.util;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class FileIndicesParserConfig {
	
	/** The Constant LOCALE_ES. */
	public static final Locale LOCALE_ES = new Locale("es");
	
	public static final String DEFAULT_MSG = "Informaci\u00F3n incorrecta en el campo.";
	
	private Short longRow;
	
	private LinkedHashMap<String, Map<String, Object>> env;
	
	/** The props. */
	private ReloadableResourceBundleMessageSource props;

	public FileIndicesParserConfig() {
	}
	
	public String getMessage(String key, Object... param) {
		String mensaje = null;
		if (props != null) {
			mensaje = props.getMessage(key, param, DEFAULT_MSG, LOCALE_ES);
		}
		
		return mensaje;
	}

	public Short getLongRow() {
		return longRow;
	}

	public void setLongRow(Short longRow) {
		this.longRow = longRow;
	}

	public LinkedHashMap<String, Map<String, Object>> getEnv() {
		return env;
	}

	public void setEnv(LinkedHashMap<String, Map<String, Object>> env) {
		this.env = env;
	}

	public ReloadableResourceBundleMessageSource getProps() {
		return props;
	}

	public void setProps(ReloadableResourceBundleMessageSource props) {
		this.props = props;
	}

}

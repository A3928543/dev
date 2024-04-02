package com.ixe.ods.sica.batch.cfdi.parser;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixe.ods.sica.batch.cfdi.parser.util.FileIndicesParserConfig;
import com.ixe.ods.sica.batch.error.FileParserException;
import com.ixe.ods.sica.batch.error.RowParserException;
import com.ixe.ods.sica.batch.parser.FileParser;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isNumeric;
import static org.apache.commons.lang.StringUtils.split;
import static org.apache.commons.lang.StringUtils.substring;
import static org.apache.commons.lang.StringUtils.trim;
import static org.apache.commons.lang.StringUtils.upperCase;
import static org.apache.commons.lang.reflect.MethodUtils.invokeMethod;

import static com.ixe.ods.sica.batch.util.Utilerias.stringToDate;

public class FileIndicesParser extends FileParser {
	
	private static final Logger LOG = LoggerFactory.getLogger(FileIndicesParser.class);
	
	private static final String KEY_OPTIONAL_ERROR         = "msg.error.obligatorio";
	
	private static final String KEY_NOT_NUMERIC_ERROR      = "msg.error.not.numeric";
	
	private static final String KET_NOT_DATE_ERROR         = "msg.error.not.date";
	
	private static final String KEY_NOT_FORMAT_SPLIT_ERROR = "msg.error.split.format";
	
	private static final String KEY_NOT_LONG_ROW_ERROR     = "msg.error.lon.row";
	
	private FileIndicesParserConfig parserConfig;

	public FileIndicesParser() {
	}

	public FileIndicesParser(File archivo, FileIndicesParserConfig config) 
			throws FileParserException {
		super(archivo);
		this.parserConfig = config;
	}

	protected boolean validateRow(String row) throws RowParserException {
		Short lonRow = this.parserConfig.getLongRow();
		LOG.debug("row[{}]: '{}'", this.numRow, row);
		LOG.debug("row.length(): {}", row.length());
		if (lonRow != row.length()) {
			String msg = this.parserConfig.getMessage(KEY_NOT_LONG_ROW_ERROR, lonRow, row.length());
			throw new RowParserException(msg, row);
		}
		
		return true;
	}
	
	public Map<String, Object> nextRow() throws RowParserException {
		String renglon = this.getRows().get(this.numRow);
		Map<String, Object> result = null;
		String msg = null;
		int start = 0;
		int end = 0;
		try {
			if (validateRow(renglon)) {
				result = new HashMap<String, Object>();
				result.put("row", renglon);
				for (String field : this.parserConfig.getEnv().keySet()) {
					Map<String, Object> envField = 
							this.parserConfig.getEnv().get(field);
					Short lon = (Short) envField.get("lon");
					end = end + lon;
					String value = substring(renglon, start, end);
					value = trim(value);
					Boolean isOptional = (Boolean) envField.get("isOptional");
					LOG.info("Valor[{}] = {}", field, value);
					if (!isOptional && isEmpty(value)) {
						msg = this.parserConfig.getMessage(KEY_OPTIONAL_ERROR, 
								upperCase(field));
						throw new RowParserException(msg, renglon);
					}
					else if (isOptional && isEmpty(value)) {
						result.put(field, value);
					}
					else {
						try {
							String type = (String) envField.get("type");
							Map<String, Object> map = 
									invokeCode(type, value, field, envField);
							result.putAll(map);
						}
						catch (RowParserException ex) {
							ex.setDetail(renglon);
							throw ex;
						}
					}
					start = start + lon;
				}
			}
			this.numRow++;
		}
		catch (RowParserException ex) {//Solo para incrementar el contador.
			this.numRow++;
			throw ex;
		}
		
		return result;
	}
	
	public Map<String, String> getAlphanumericType(String value, String field, 
			Map<String, Object> env) throws RowParserException {
		Map<String, String> result = new HashMap<String, String>();
		result.put(field, value);
		
		return result;
	}
	
	
	public Map<String, Date> getDateType(String value, String field, Map<String, Object> env)
			throws RowParserException {
		Map<String, Date> result = null;
		String pattern = (String) env.get("pattern");
		Date fecha = stringToDate(value, pattern);
		if (fecha != null) {
			result = new HashMap<String, Date>();
			result.put(field, fecha);
		}
		else {
			String msg = this.parserConfig.getMessage(KET_NOT_DATE_ERROR, 
					upperCase(field));
			throw new RowParserException(msg);
		}
		
		return result;
	}
	
	public Map<String, BigInteger> getBigIntegerType(String value, String field, 
			Map<String, Object> env) throws RowParserException {
		Map<String, BigInteger> result = null;
		String msg = null;
		try {
			if (isNumeric(value)) {
				result = new HashMap<String, BigInteger>();
				result.put(field, new BigInteger(value));
			}
			else {
				msg = this.parserConfig.getMessage(KEY_NOT_NUMERIC_ERROR, 
						upperCase(field));
				throw new RowParserException(msg);
			}
		}
		catch (NumberFormatException ex) {
			LOG.debug("NumberFormatException en getLongType() ", ex);
			msg = this.parserConfig.getMessage(KEY_NOT_NUMERIC_ERROR, 
					upperCase(field));
			throw new RowParserException(msg);
		}
		
		return result;
	}
	
	public Map<String, Object> getSplitType(String value, String field, Map<String, Object> env) 
			throws RowParserException {
		Map<String, Object> result = null;
		String msg = null;
		String separetor = (String) env.get("separetor");
		@SuppressWarnings("unchecked")
		List<String> subfields = (List<String>) env.get("subfields");
		String[] tokens = split(value, separetor);
		if(subfields.size() != tokens.length) {
			msg = this.parserConfig.
					getMessage(KEY_NOT_FORMAT_SPLIT_ERROR, upperCase(field), separetor);
			throw new RowParserException(msg);
		}
		else {
			result = new HashMap<String, Object>();
			int indice = 0;
			for (String subfield : subfields) {
				String subValue = tokens[indice];
				Map<String, BigInteger> mapLong = 
						getBigIntegerType(subValue, subfield, env);
				result.putAll(mapLong);
				indice++;
			}
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> invokeCode(String field, Object...param) 
			throws RowParserException {
		Map<String, Object> result = null;
		String metodo = "get" + field;
		try {
			Object ret = invokeMethod(this, metodo, param);
			if (ret != null) {
				result = (Map<String, Object>) ret;
			}
		}
		catch (InvocationTargetException ex) {
			if (ex.getCause() instanceof RowParserException) {
				throw (RowParserException) ex.getCause();
			}
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

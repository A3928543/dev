package com.ixe.ods.sica.batch.clientes.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.ixe.ods.sica.batch.clientes.parser.error.ParserFileException;
import com.ixe.ods.sica.batch.clientes.parser.error.ParserRowException;
import com.ixe.ods.sica.batch.clientes.parser.util.ParserConfig;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.replace;
import static org.apache.commons.lang.StringUtils.trim;

/**
 * The Class ParserFile.
 */
public class ParserFile {
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(ParserFile.class);
	
	/** The ENCODING. */
    public static final String ENCODING = "ISO-8859-1";
    
    /** The rows. */
    private StringTokenizer rows;
    
    /** The total rows. */
    private int totalRows;
    
    /** The count. */
    private Integer count;
    
    /** The parser config. */
    private ParserConfig parserConfig;
    
    /** The separador linea. */
    private String separadorLinea;
    
    /** The nombre archivo. */
    private String nombreArchivo;
    
	/**
	 * Instantiates a new parser file.
	 */
	public ParserFile() {
	}
	
	
	/**
	 * Instantiates a new parser file.
	 *
	 * @param archivo the archivo
	 * @param parserConfig the parser config
	 * @throws ParserFileException the parser file exception
	 */
	public ParserFile(File archivo, ParserConfig parserConfig) 
			throws ParserFileException{
		this.parserConfig = parserConfig;
		this.nombreArchivo = archivo.getName();
		creaRows(archivo);
	}
    
    
    /**
     * Crea rows.
     *
     * @param archivo the archivo
     * @throws ParserFileException the parser file exception
     */
    private synchronized void creaRows(File archivo)
	throws ParserFileException {
        FileInputStream inputStream = null;
        byte[] content = null;
        String strContent = null;
        
        try {
        	count = 0;
        	Charset charset = Charset.defaultCharset();
        	LOG.debug("Tipo de codificacion en la maquina virtual: " + charset);
            inputStream = new FileInputStream(archivo);
            LOG.debug("Numero de bytes disponibles: " +
            inputStream.available());
            content = new byte[inputStream.available()];
            LOG.debug("Numero de bytes leidos: " + inputStream.read(content));
            LOG.debug("Charset.isSupported(ENCODING): " + Charset.isSupported(ENCODING));
            if (Charset.isSupported(ENCODING)) {
            	charset = Charset.forName(ENCODING);
            	strContent = new String(content, charset);
            }
            else {
            	strContent = new String(content);
            }
            separadorLinea = System.getProperty("line.separator");
            rows = new StringTokenizer(strContent, separadorLinea);
            if (rows.hasMoreTokens()) {
                totalRows = rows.countTokens();
                LOG.debug("Se van a procesar " + totalRows + " registro(s).");
            }
        } 
        catch (IOException ex) {
            throw new ParserFileException(ex);
        } 
        finally {
        	closeStream(inputStream);
        	content = null;
            strContent = null;
        }
    }

    /**
     * Checks for more rows.
     *
     * @return true, if successful
     */
    public boolean hasMoreRows() {
    	boolean hasMore = false;
    	if (rows != null) {
    		hasMore = rows.hasMoreTokens();
    	}
    	
        return hasMore;
    }

    /**
     * Close stream.
     *
     * @param stream the stream
     */
    private void closeStream(Object stream) {
        try {
        	if (stream != null) {
                if (stream instanceof BufferedReader) {
                    ((BufferedReader) stream).close();
                } 
                else if (stream instanceof InputStreamReader) {
                    ((InputStreamReader) stream).close();
                } 
                else if (stream instanceof FileInputStream) {
                    ((FileInputStream) stream).close();
                }
        	}
        } 
        catch (IOException ex) {
            LOG.error(ex.getMessage());
        }
    }

    /**
     * Validate row.
     *
     * @param row the row
     * @return true, if successful
     * @throws ParserRowException the parser row exception
     */
    private boolean validateRow(String row) 
    		throws ParserRowException {
    	boolean result = false;
    	int longReg = parserConfig.getLongReg();
    	LOG.debug("longReg: " + longReg);
    	LOG.debug("row.length(): " + row.length());
    	result = longReg == row.length();
    	if (!result) {
    		String mensaje = 
    				parserConfig.getMensaje("msg.error.long.renglon");
    		mensaje = 
    				parserConfig.getMensaje("msg.error.detalle", 
    						nombreArchivo, getCount(), row, mensaje);
    		throw new ParserRowException(mensaje);
    	}
    	
    	return result;
    }

    /**
     * Next row.
     *
     * @return the map
     * @throws ParserRowException the parser row exception
     */
    public Map<String, String> nextRow() 
    		throws ParserRowException {
    	Map<String, String> result = null;
    	String mensaje = null;
    	String valorDef = "";
    	String espacio = " ";
		String row = getRows().nextToken();
		row = replace(row, separadorLinea, "");
		setCount(getCount() + 1);
		LOG.info("Valor de row " + getCount() + ": [" + row +  "]  ");
    	if (validateRow(row)) {
    		result = new HashMap<String, String>();
    		int min = 0;
    		int max = parserConfig.getLongNumClien();
    		String valor = row.substring(min, max);
    		valor = trim(valor);
    		if (!esNumerico(valor)) {
    			mensaje = 
    					parserConfig.getMensaje("msg.error.id.cliente.numerico", 
    							valorDef);
    			mensaje = 
    					parserConfig.getMensaje("msg.error.detalle", 
    							nombreArchivo, getCount(), row, mensaje);
    			throw new ParserRowException(mensaje);
    		}
    		result.put("NUMCLIEN", valor);
    		min = max;
    		max = max + parserConfig.getLongIndBanco();
    		valor = row.substring(min, max);
    		valor = trim(valor);
    		boolean contieneValor = 
    				parserConfig.getValoresBanco().containsKey(valor);
    		if (!contieneValor) {
    			mensaje = 
    					parserConfig.getMensaje("msg.error.valor.rango.banco", 
    							                 valorDef);
    			mensaje = mensaje + espacio + 
    					parserConfig.getValoresBanco().keySet();
    			mensaje = 
    					parserConfig.getMensaje("msg.error.detalle", 
    							nombreArchivo, getCount(), row, mensaje);
    			throw new ParserRowException(mensaje);
    		}
    		result.put("INDBANCO", valor);
    		min = max;
    		max = max + parserConfig.getLongNumClRef();
    		valor = row.substring(min, max);
    		valor = trim(valor);
    		valor = valor.trim();
    		if (!esNumerico(valor)) {
    			mensaje = 
    					parserConfig.getMensaje("msg.error.id.cliente.numerico", 
    							                 valorDef);
    			mensaje = 
    					parserConfig.getMensaje("msg.error.detalle", 
    							nombreArchivo, getCount(), row, mensaje);
    			throw new ParserRowException(mensaje);
    		}
    		result.put("NUMCLREF", valor);
    		min = max;
    		max = max + parserConfig.getLongIndBnCor();
    		valor = row.substring(min, max);
    		valor = trim(valor);
    		contieneValor = 
    				parserConfig.getValoresBanco().containsKey(valor);
    		if (!contieneValor) {
    			mensaje = 
    					parserConfig.getMensaje("msg.error.valor.rango.banco", 
    							                 valorDef);
    			mensaje = mensaje + espacio + parserConfig.getValoresBanco().keySet();
    			mensaje = 
    					parserConfig.getMensaje("msg.error.detalle", 
    							nombreArchivo, getCount(), row, mensaje);
    			throw new ParserRowException(mensaje);
    		}
    		result.put("INDBNCOR", valor);
    		min = max;
    		max = max + parserConfig.getLongSucursal();
    		valor = row.substring(min, max);
    		valor = trim(valor);
    		result.put("SUCURSALCLIENTE", valor);
    		min = max;
    		max = max + parserConfig.getLongRfc();
    		valor = row.substring(min, max);
    		valor = trim(valor);
    		result.put("RFC", valor);
    		min = max;
    		max = max + parserConfig.getLongFeProces();
    		valor = row.substring(min, max);
    		//valor = trim(valor);
    		if (isNotEmpty(valor)) {
	    		if (trim(valor).length() != 10 || stringToDate(valor) == null ) {
	    			mensaje = 
	    					parserConfig.getMensaje("msg.error.formato.fecha.proceso", 
	    							                valorDef);
	    			mensaje = 
	    					parserConfig.getMensaje("msg.error.detalle", 
	    							nombreArchivo, getCount(), row, mensaje);
	    			throw new ParserRowException(mensaje);
	    		}
    		}
    		result.put("FEPROCES", valor);
    		result.put("DETALLE", row);
    		result.put("ARCHIVO", nombreArchivo);
    		LOG.info(result);
    	}
    	
    	return result;
    }
    
    /**
     * Es numerico.
     *
     * @param valor the valor
     * @return true, if successful
     */
    private boolean esNumerico(String valor) {
    	boolean result = true;
    	
    	try {
    		new Integer(valor);
    	}
    	catch (NumberFormatException ex) {
    		LOG.debug("NumberFormatException ", ex);
    		result = false;
    	}
    	catch (NullPointerException ex) {
    		LOG.debug("NullPointerException ", ex);
    		result = false;
    	}
    	
    	return result;
    }

    
    /**
     * Parses the row.
     *
     * @param line the line
     * @return the map
     * @throws ParserRowException the parser row exception
     */
    public Map<String, Object> parseRow(String line)
    throws ParserRowException {
    	Map<String, Object> row = new HashMap<String, Object>();
    	
    	return row;
    }

    /**
     * Gets the total rows.
     *
     * @return the totalRows
     */
    public Integer getTotalRows() {
        return totalRows;
    }

	/**
	 * Gets the rows.
	 *
	 * @return the rows
	 */
	public StringTokenizer getRows() {
		return rows;
	}

	/**
	 * Sets the rows.
	 *
	 * @param rows the rows to set
	 */
	public void setRows(StringTokenizer rows) {
		this.rows = rows;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * Sets the total rows.
	 *
	 * @param totalRows the totalRows to set
	 */
	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}
		
    /**
     * String to date.
     *
     * @param fecha the fecha
     * @return the date
     */
    public Date stringToDate(String fecha) {
		Date dFecha = null;
		try {
			String pattern = parserConfig.getFormatoFecha();
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			dFecha = format.parse(fecha);
			LOG.debug("dFecha" + dFecha);
		}
		catch (ParseException ex) {
			LOG.error("ParseException ", ex);
		}
		
		return dFecha;
	}

	/**
	 * Gets the parser config.
	 *
	 * @return the parser config
	 */
	public ParserConfig getParserConfig() {
		return parserConfig;
	}

	/**
	 * Sets the parser config.
	 *
	 * @param parserConfig the new parser config
	 */
	public void setParserConfig(ParserConfig parserConfig) {
		this.parserConfig = parserConfig;
	}
}

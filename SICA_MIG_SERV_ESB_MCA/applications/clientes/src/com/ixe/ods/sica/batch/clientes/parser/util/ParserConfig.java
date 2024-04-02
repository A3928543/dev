package com.ixe.ods.sica.batch.clientes.parser.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.ixe.ods.sica.batch.clientes.parser.error.ConfigErrorException;

import static org.apache.commons.lang.StringUtils.defaultString;

/**
 * The Class ParserConfig.
 */
public class ParserConfig {
	
	/** The LOG. */
	private static Logger LOG = Logger.getLogger(ParserConfig.class);
	
	/** The Constant LOCALE_ES. */
	public static final Locale LOCALE_ES = new Locale("es");
	
	/** The props. */
	private ReloadableResourceBundleMessageSource props;
	
	/** The DEFAUL t_ msg. */
	private String DEFAULT_MSG = "No se encontr\u00F3 mensaje.";
	
	/** The long reg. */
	private Integer longReg;
    
    /** The long num clien. */
    private Integer longNumClien;
    
    /** The long ind banco. */
    private Integer longIndBanco;
    
    /** The long num cl ref. */
    private Integer longNumClRef;
    
    /** The long ind bn cor. */
    private Integer longIndBnCor;
    		
    /** The long sucursal. */
    private Integer longSucursal;
    
    /** The long rfc. */
    private Integer longRfc;
    
    /** The long fe proces. */
    private Integer longFeProces;
    
    /** The formato fecha. */
    private String formatoFecha;
    
    /** The valores banco. */
    private Map<String, String> valoresBanco;
	
	/**
	 * Instantiates a new parser config.
	 *
	 * @param archivo the archivo
	 */
	public ParserConfig(String archivo) {
		props = new ReloadableResourceBundleMessageSource();
		props.setBasename(archivo);
		initConfig();
	}
	
	/**
	 * Inits the config.
	 */
	private void initConfig() {
		String msg = "Configuraci\u00F3n incorrecta.";
		try {
			String defaultValor = "yyyy-MM-dd";
	    	formatoFecha = 
	    			defaultString(getMensaje("formato.fecha"), defaultValor);
			defaultValor = "45";
	    	String valor = 
	    			defaultString(getMensaje("long.renglon"), defaultValor);
	    	longReg = new Integer(valor);
	    	defaultValor = "8";
	    	valor = 
	    			defaultString(getMensaje("long.numclien"), defaultValor); 
	    	longNumClien = new Integer(valor);
	    	defaultValor = "1";
	    	valor = 
	    			defaultString(getMensaje("long.indbanco"), defaultValor); 
	    	longIndBanco = new Integer(valor);
	    	defaultValor = "8";
	    	valor = 
	    			defaultString(getMensaje("long.numclref"), defaultValor); 
	    	longNumClRef = new Integer(valor);
	    	defaultValor = "1";
	    	valor = 
	    			defaultString(getMensaje("long.indbncor"), defaultValor); 
	    	longIndBnCor = new Integer(valor);
	    	defaultValor = "4";
	    	valor = 
	    			defaultString(getMensaje("long.sucursal"), defaultValor); 
	    	longSucursal = new Integer(valor);
	    	defaultValor = "13";
	    	valor = 
	    			defaultString(getMensaje("long.rfc"), defaultValor); 
	    	longRfc = new Integer(valor);
	    	defaultValor = "10";
	    	valor = 
	    			defaultString(getMensaje("long.feproces"), defaultValor); 
	    	longFeProces = new Integer(valor);
	    	initValoresBanco();
		}
		catch (NumberFormatException ex) {
			msg = getMensaje("msg.error.config", msg);
			throw new ConfigErrorException(msg);
		}
		catch (NullPointerException ex) {
			msg = getMensaje("msg.error.config", msg);
			throw new ConfigErrorException(msg);
		}
    }
	
	
	/**
	 * Inits the valores banco.
	 */
	private void initValoresBanco() {
		String listaValores = getMensaje("valores.banco");
		String valores[] = listaValores.split("\\|");
		valoresBanco = new HashMap<String, String>();
		for (int i = 0; i < valores.length; i++) {
			valoresBanco.put(valores[i], valores[i]);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug(valoresBanco);
		}
	}
	
	/**
	 * Gets the mensaje.
	 *
	 * @param llave the llave
	 * @param args the args
	 * @return the mensaje
	 */
	public String getMensaje(String llave, Object... args) {
		String mensaje = null;
		if (props != null) {
			mensaje = props.getMessage(llave, args, DEFAULT_MSG, LOCALE_ES);
		}
		LOG.info(llave + " = " + mensaje);
		
		return mensaje;
	}
    
	/**
	 * Gets the long reg.
	 *
	 * @return the long reg
	 */
	public Integer getLongReg() {
		return longReg;
	}

	/**
	 * Sets the long reg.
	 *
	 * @param longReg the new long reg
	 */
	public void setLongReg(Integer longReg) {
		this.longReg = longReg;
	}

	/**
	 * Gets the long num clien.
	 *
	 * @return the long num clien
	 */
	public Integer getLongNumClien() {
		return longNumClien;
	}

	/**
	 * Sets the long num clien.
	 *
	 * @param longNumClien the new long num clien
	 */
	public void setLongNumClien(Integer longNumClien) {
		this.longNumClien = longNumClien;
	}

	/**
	 * Gets the long ind banco.
	 *
	 * @return the long ind banco
	 */
	public Integer getLongIndBanco() {
		return longIndBanco;
	}

	/**
	 * Sets the long ind banco.
	 *
	 * @param longIndBanco the new long ind banco
	 */
	public void setLongIndBanco(Integer longIndBanco) {
		this.longIndBanco = longIndBanco;
	}

	/**
	 * Gets the long num cl ref.
	 *
	 * @return the long num cl ref
	 */
	public Integer getLongNumClRef() {
		return longNumClRef;
	}

	/**
	 * Sets the long num cl ref.
	 *
	 * @param longNumClRef the new long num cl ref
	 */
	public void setLongNumClRef(Integer longNumClRef) {
		this.longNumClRef = longNumClRef;
	}

	/**
	 * Gets the long ind bn cor.
	 *
	 * @return the long ind bn cor
	 */
	public Integer getLongIndBnCor() {
		return longIndBnCor;
	}

	/**
	 * Sets the long ind bn cor.
	 *
	 * @param longIndBnCor the new long ind bn cor
	 */
	public void setLongIndBnCor(Integer longIndBnCor) {
		this.longIndBnCor = longIndBnCor;
	}

	/**
	 * Gets the long sucursal.
	 *
	 * @return the long sucursal
	 */
	public Integer getLongSucursal() {
		return longSucursal;
	}

	/**
	 * Sets the long sucursal.
	 *
	 * @param longSucursal the new long sucursal
	 */
	public void setLongSucursal(Integer longSucursal) {
		this.longSucursal = longSucursal;
	}

	/**
	 * Gets the long rfc.
	 *
	 * @return the long rfc
	 */
	public Integer getLongRfc() {
		return longRfc;
	}

	/**
	 * Sets the long rfc.
	 *
	 * @param longRfc the new long rfc
	 */
	public void setLongRfc(Integer longRfc) {
		this.longRfc = longRfc;
	}

	/**
	 * Gets the long fe proces.
	 *
	 * @return the long fe proces
	 */
	public Integer getLongFeProces() {
		return longFeProces;
	}

	/**
	 * Sets the long fe proces.
	 *
	 * @param longFeProces the new long fe proces
	 */
	public void setLongFeProces(Integer longFeProces) {
		this.longFeProces = longFeProces;
	}

	/**
	 * Gets the formato fecha.
	 *
	 * @return the formato fecha
	 */
	public String getFormatoFecha() {
		return formatoFecha;
	}

	/**
	 * Sets the formato fecha.
	 *
	 * @param formatoFecha the new formato fecha
	 */
	public void setFormatoFecha(String formatoFecha) {
		this.formatoFecha = formatoFecha;
	}

	/**
	 * Gets the valores banco.
	 *
	 * @return the valores banco
	 */
	public Map<String, String> getValoresBanco() {
		return valoresBanco;
	}

	/**
	 * Sets the valores banco.
	 *
	 * @param valoresBanco the valores banco
	 */
	public void setValoresBanco(Map<String, String> valoresBanco) {
		this.valoresBanco = valoresBanco;
	}
}

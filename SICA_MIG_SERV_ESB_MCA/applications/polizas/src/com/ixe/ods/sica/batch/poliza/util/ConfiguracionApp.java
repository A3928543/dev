package com.ixe.ods.sica.batch.poliza.util;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;

import static org.apache.commons.lang.StringUtils.defaultString;

import static com.ixe.ods.sica.batch.util.Utilerias.dateToString;

/**
 * The Class ConfiguracionApp.
 */
@Configuration
public class ConfiguracionApp {
	
	/** The header. */
	private LinkedHashMap<String, Map<String, Object>> header;
	
	/** The body. */
	private LinkedHashMap<String, Map<String, Object>> body;
	
	/** The trailer. */
	private LinkedHashMap<String, Map<String, Object>> trailer;
	
	/** The nombre archivo. */
	private LinkedHashMap<String, Map<String, Object>> nombreArchivo;
	
	/** The personalidades. */
	private Map<String, Map<String, String>> personalidades;

	/** The fillers. */
	private Map<String, Map<String, String>> fillers;
	
	/** The metales. */
	private Map<String, String> metales;
	
	/** The excepciones. */
	private Map<String, String> excepciones;
	
	/** The dir salida. */
	private String dirSalida;
	
	/** The dir depuracion. */
	private String dirDepuracion;

	/** The parametros. */
	private Map<String, String> parametros;
	
	/** The dias historico. */
	private Short diasHistorico;
	
	/** The pre arch depurar. */
	private String preArchDepurar;

	/** The nombre archivo bandera. */
	private LinkedHashMap<String, Map<String, Object>> nombreArchivoBandera;
	
	/** The dir archivo bandera. */
	private String dirArchivoBandera;
	
	/** The header archivo bandera. */
	private LinkedHashMap<String, Map<String, Object>> headerArchivoBandera;

	/**
	 * Instantiates a new configuracion app.
	 */
	public ConfiguracionApp() {
	}
	
	/**
	 * Gets the cve metal.
	 *
	 * @param key the key
	 * @return the cve metal
	 */
	public String getCveMetal(String key) {
		return defaultString(metales.get(key));
	}

	/**
	 * Gets the date as string.
	 *
	 * @param fecha the fecha
	 * @param pattern the pattern
	 * @return the date as string
	 */
	public String getDateAsString(Date fecha, String pattern) {
		return dateToString(fecha, pattern);
	}
	
	/**
	 * Gets the sector.
	 *
	 * @param tipoPersona the tipo persona
	 * @return the sector
	 */
	public String getSector(String tipoPersona) {
		Map<String, String> env = personalidades.get(tipoPersona);
		
		return env.get("sector");		
	}
	
	/**
	 * Gets the subsector.
	 *
	 * @param tipoPersona the tipo persona
	 * @return the subsector
	 */
	public String getSubsector(String tipoPersona) {
		Map<String, String> env = personalidades.get(tipoPersona);
		
		return env.get("subsector");
	} 
	
	/**
	 * Gets the env header field.
	 *
	 * @param field the field
	 * @return the env header field
	 */
	public Map<String, Object> getEnvHeaderField(String field) {
		return header.get(field);
	}
	
	/**
	 * Gets the env filler.
	 *
	 * @param type the type
	 * @return the env filler
	 */
	public Map<String, String> getEnvFiller(String type) {
		return fillers.get(type);
	}
	
	/**
	 * Gets the header.
	 *
	 * @return the header
	 */
	public LinkedHashMap<String, Map<String, Object>> getHeader() {
		return header;
	}

	/**
	 * Sets the header.
	 *
	 * @param header the header
	 */
	public void setHeader(LinkedHashMap<String, Map<String, Object>> header) {
		this.header = header;
	}
	
	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public LinkedHashMap<String, Map<String, Object>> getBody() {
		return body;
	}

	/**
	 * Sets the body.
	 *
	 * @param body the body
	 */
	public void setBody(LinkedHashMap<String, Map<String, Object>> body) {
		this.body = body;
	}
	
	/**
	 * Gets the trailer.
	 *
	 * @return the trailer
	 */
	public LinkedHashMap<String, Map<String, Object>> getTrailer() {
		return trailer;
	}

	/**
	 * Sets the trailer.
	 *
	 * @param trailer the trailer
	 */
	public void setTrailer(LinkedHashMap<String, Map<String, Object>> trailer) {
		this.trailer = trailer;
	}

	/**
	 * Gets the fillers.
	 *
	 * @return the fillers
	 */
	public Map<String, Map<String, String>> getFillers() {
		return fillers;
	}

	/**
	 * Sets the fillers.
	 *
	 * @param fillers the fillers
	 */
	public void setFillers(Map<String, Map<String, String>> fillers) {
		this.fillers = fillers;
	}

	/**
	 * Gets the personalidades.
	 *
	 * @return the personalidades
	 */
	public Map<String, Map<String, String>> getPersonalidades() {
		return personalidades;
	}

	/**
	 * Sets the personalidades.
	 *
	 * @param personalidades the personalidades
	 */
	public void setPersonalidades(Map<String, Map<String, String>> personalidades) {
		this.personalidades = personalidades;
	}

	/**
	 * Gets the dir salida.
	 *
	 * @return the dir salida
	 */
	public String getDirSalida() {
		return dirSalida;
	}

	/**
	 * Sets the dir salida.
	 *
	 * @param dirSalida the new dir salida
	 */
	public void setDirSalida(String dirSalida) {
		this.dirSalida = dirSalida;
	}
	
	/**
	 * Gets the dir depuracion.
	 *
	 * @return the dir depuracion
	 */
	public String getDirDepuracion() {
		return dirDepuracion;
	}

	/**
	 * Sets the dir depuracion.
	 *
	 * @param dirDepuracion the new dir depuracion
	 */
	public void setDirDepuracion(String dirDepuracion) {
		this.dirDepuracion = dirDepuracion;
	}

	/**
	 * Gets the metales.
	 *
	 * @return the metales
	 */
	public Map<String, String> getMetales() {
		return metales;
	}

	/**
	 * Sets the metales.
	 *
	 * @param matales the matales
	 */
	public void setMetales(Map<String, String> matales) {
		this.metales = matales;
	}

	/**
	 * Gets the nombre archivo.
	 *
	 * @return the nombre archivo
	 */
	public LinkedHashMap<String, Map<String, Object>> getNombreArchivo() {
		return nombreArchivo;
	}

	/**
	 * Sets the nombre archivo.
	 *
	 * @param nombreArchivo the nombre archivo
	 */
	public void setNombreArchivo(
			LinkedHashMap<String, Map<String, Object>> nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	
	/**
	 * Gets the excepciones.
	 *
	 * @return the excepciones
	 */
	public Map<String, String> getExcepciones() {
		return excepciones;
	}

	/**
	 * Sets the excepciones.
	 *
	 * @param excepciones the excepciones
	 */
	public void setExcepciones(Map<String, String> excepciones) {
		this.excepciones = excepciones;
	}
	
	/**
	 * Gets the parametros.
	 *
	 * @return the parametros
	 */
	public Map<String, String> getParametros() {
		return parametros;
	}

	/**
	 * Sets the parametros.
	 *
	 * @param parametros the parametros
	 */
	public void setParametros(Map<String, String> parametros) {
		this.parametros = parametros;
	}

	/**
	 * Gets the dias historico.
	 *
	 * @return the dias historico
	 */
	public Short getDiasHistorico() {
		return diasHistorico;
	}

	/**
	 * Sets the dias historico.
	 *
	 * @param diasHistorico the new dias historico
	 */
	public void setDiasHistorico(Short diasHistorico) {
		this.diasHistorico = diasHistorico;
	}

	/**
	 * Gets the pre arch depurar.
	 *
	 * @return the pre arch depurar
	 */
	public String getPreArchDepurar() {
		return preArchDepurar;
	}

	/**
	 * Sets the pre arch depurar.
	 *
	 * @param preArchDepurar the new pre arch depurar
	 */
	public void setPreArchDepurar(String preArchDepurar) {
		this.preArchDepurar = preArchDepurar;
	}

	/**
	 * Gets the nombre archivo bandera.
	 *
	 * @return the nombre archivo bandera
	 */
	public LinkedHashMap<String, Map<String, Object>> getNombreArchivoBandera() {
		return nombreArchivoBandera;
	}

	/**
	 * Sets the nombre archivo bandera.
	 *
	 * @param nombreArchivoBandera the nombre archivo bandera
	 */
	public void setNombreArchivoBandera(
			LinkedHashMap<String, Map<String, Object>> nombreArchivoBandera) {
		this.nombreArchivoBandera = nombreArchivoBandera;
	}

	/**
	 * Gets the dir archivo bandera.
	 *
	 * @return the dir archivo bandera
	 */
	public String getDirArchivoBandera() {
		return dirArchivoBandera;
	}

	/**
	 * Sets the dir archivo bandera.
	 *
	 * @param dirArchivoBandera the new dir archivo bandera
	 */
	public void setDirArchivoBandera(String dirArchivoBandera) {
		this.dirArchivoBandera = dirArchivoBandera;
	}

	/**
	 * Gets the header archivo bandera.
	 *
	 * @return the header archivo bandera
	 */
	public LinkedHashMap<String, Map<String, Object>> getHeaderArchivoBandera() {
		return headerArchivoBandera;
	}

	/**
	 * Sets the header archivo bandera.
	 *
	 * @param headerArchivoBandera the header archivo bandera
	 */
	public void setHeaderArchivoBandera(
			LinkedHashMap<String, Map<String, Object>> headerArchivoBandera) {
		this.headerArchivoBandera = headerArchivoBandera;
	}
}

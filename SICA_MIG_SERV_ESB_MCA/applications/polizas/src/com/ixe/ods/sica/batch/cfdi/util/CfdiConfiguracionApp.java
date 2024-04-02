package com.ixe.ods.sica.batch.cfdi.util;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ixe.ods.sica.batch.cfdi.parser.util.FileIndicesParserConfig;
import com.ixe.ods.sica.batch.util.ConfiguracionApp;

/**
 * The Class CfdiConfiguracionApp.
 */
public class CfdiConfiguracionApp extends ConfiguracionApp {
	
	/** The pattern. */
	private String pattern;
	
	/** The ext file. */
	private String extFile;
	
	/** The prefijo file. */
	private String prefijoFile;
	
	/** The password file. */
	private String passwordFile;
	
	/** The parser config. */
	private FileIndicesParserConfig parserConfig;
	
	/** The nom arch salida. */
	private LinkedHashMap<String, Map<String, Object>> nomArchSalida;
	
	/** The nom arch errores. */
	private LinkedHashMap<String, Map<String, Object>> nomArchErrores;
	
	/** The env reproceso. */
	private Map<String, String> envReproceso;
	
	/** The fin archivo. */
	private String finArchivo;
	
	/** The re emplazar. */
	private String reEmplazar;
	
	/** The cadena buscar. */
	private String cadenaBuscar;
	
	/** The nombre arch errores. */
	private String nombreArchErrores;
	
	/** The status factura. */
	private Map<String, String> statusFactura;
	
	/** The dias historico. */
	private Short diasHistorico;
	
	/** The ext file in. */
	private String extFileIn;
	
	/** The zip out. */
	private boolean zipOut;
	
	/** The dir enviados. */
	private String dirEnviados;
	
	/** The config rep cifras. */
	private Map<String, String> configRepCifras;
	
	/** The config rep cfdi det. */
	private Map<String, String> configRepCfdiDet;
	
	/** The dir rep cfdi porenviar. */
	private String dirRepCfdiPorenviar;
	
	/** The dir rep cfdi enviados. */
	private String dirRepCfdiEnviados;
	
	/** The nombre rep cifras error. */
	private String nombreRepCifrasError;
	
	/**
	 * Instantiates a new cfdi configuracion app.
	 */
	public CfdiConfiguracionApp() {
	}
	
	/**
	 * Gets the ind reproceso.
	 *
	 * @param key the key
	 * @return the ind reproceso
	 */
	public String getIndReproceso(String key) {
		return envReproceso.get(key);
	}
	
	/**
	 * Gets the des status factura.
	 *
	 * @param status the status
	 * @return the des status factura
	 */
	public String getDesStatusFactura(String status) {
		return statusFactura.get(status);
	}

	/**
	 * Gets the pattern.
	 *
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * Sets the pattern.
	 *
	 * @param pattern the new pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * Gets the ext file.
	 *
	 * @return the ext file
	 */
	public String getExtFile() {
		return extFile;
	}

	/**
	 * Sets the ext file.
	 *
	 * @param extFile the new ext file
	 */
	public void setExtFile(String extFile) {
		this.extFile = extFile;
	}

	/**
	 * Gets the prefijo file.
	 *
	 * @return the prefijo file
	 */
	public String getPrefijoFile() {
		return prefijoFile;
	}

	/**
	 * Sets the prefijo file.
	 *
	 * @param prefijoFile the new prefijo file
	 */
	public void setPrefijoFile(String prefijoFile) {
		this.prefijoFile = prefijoFile;
	}

	/**
	 * Gets the password file.
	 *
	 * @return the password file
	 */
	public String getPasswordFile() {
		return passwordFile;
	}

	/**
	 * Sets the password file.
	 *
	 * @param passwordFile the new password file
	 */
	public void setPasswordFile(String passwordFile) {
		this.passwordFile = passwordFile;
	}

	/**
	 * Gets the parser config.
	 *
	 * @return the parser config
	 */
	public FileIndicesParserConfig getParserConfig() {
		return parserConfig;
	}

	/**
	 * Sets the parser config.
	 *
	 * @param parserConfig the new parser config
	 */
	public void setParserConfig(FileIndicesParserConfig parserConfig) {
		this.parserConfig = parserConfig;
	}

	/**
	 * Gets the nom arch salida.
	 *
	 * @return the nom arch salida
	 */
	public LinkedHashMap<String, Map<String, Object>> getNomArchSalida() {
		return nomArchSalida;
	}

	/**
	 * Sets the nom arch salida.
	 *
	 * @param nomArchSalida the nom arch salida
	 */
	public void setNomArchSalida(
			LinkedHashMap<String, Map<String, Object>> nomArchSalida) {
		this.nomArchSalida = nomArchSalida;
	}

	/**
	 * Gets the nom arch errores.
	 *
	 * @return the nom arch errores
	 */
	public LinkedHashMap<String, Map<String, Object>> getNomArchErrores() {
		return nomArchErrores;
	}

	/**
	 * Sets the nom arch errores.
	 *
	 * @param nomArchErrores the nom arch errores
	 */
	public void setNomArchErrores(
			LinkedHashMap<String, Map<String, Object>> nomArchErrores) {
		this.nomArchErrores = nomArchErrores;
	}

	/**
	 * Gets the env reproceso.
	 *
	 * @return the env reproceso
	 */
	public Map<String, String> getEnvReproceso() {
		return envReproceso;
	}

	/**
	 * Sets the env reproceso.
	 *
	 * @param envReproceso the env reproceso
	 */
	public void setEnvReproceso(Map<String, String> envReproceso) {
		this.envReproceso = envReproceso;
	}

	/**
	 * Gets the fin archivo.
	 *
	 * @return the fin archivo
	 */
	public String getFinArchivo() {
		return finArchivo;
	}

	/**
	 * Sets the fin archivo.
	 *
	 * @param finArchivo the new fin archivo
	 */
	public void setFinArchivo(String finArchivo) {
		this.finArchivo = finArchivo;
	}

	/**
	 * Gets the re emplazar.
	 *
	 * @return the re emplazar
	 */
	public String getReEmplazar() {
		return reEmplazar;
	}

	/**
	 * Sets the re emplazar.
	 *
	 * @param reEmplazar the new re emplazar
	 */
	public void setReEmplazar(String reEmplazar) {
		this.reEmplazar = reEmplazar;
	}

	/**
	 * Gets the cadena buscar.
	 *
	 * @return the cadena buscar
	 */
	public String getCadenaBuscar() {
		return cadenaBuscar;
	}

	/**
	 * Sets the cadena buscar.
	 *
	 * @param cadenaBuscar the new cadena buscar
	 */
	public void setCadenaBuscar(String cadenaBuscar) {
		this.cadenaBuscar = cadenaBuscar;
	}

	/**
	 * Gets the nombre arch errores.
	 *
	 * @return the nombre arch errores
	 */
	public String getNombreArchErrores() {
		return nombreArchErrores;
	}

	/**
	 * Sets the nombre arch errores.
	 *
	 * @param nombreArchErrores the new nombre arch errores
	 */
	public void setNombreArchErrores(String nombreArchErrores) {
		this.nombreArchErrores = nombreArchErrores;
	}

	/**
	 * Gets the status factura.
	 *
	 * @return the status factura
	 */
	public Map<String, String> getStatusFactura() {
		return statusFactura;
	}

	/**
	 * Sets the status factura.
	 *
	 * @param statusFactura the status factura
	 */
	public void setStatusFactura(Map<String, String> statusFactura) {
		this.statusFactura = statusFactura;
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
	 * Gets the ext file in.
	 *
	 * @return the ext file in
	 */
	public String getExtFileIn() {
		return extFileIn;
	}

	/**
	 * Sets the ext file in.
	 *
	 * @param extFileIn the new ext file in
	 */
	public void setExtFileIn(String extFileIn) {
		this.extFileIn = extFileIn;
	}

	/**
	 * Checks if is zip out.
	 *
	 * @return true, if is zip out
	 */
	public boolean isZipOut() {
		return zipOut;
	}

	/**
	 * Sets the zip out.
	 *
	 * @param zipOut the new zip out
	 */
	public void setZipOut(boolean zipOut) {
		this.zipOut = zipOut;
	}
	
	/**
	 * Gets the dir enviados.
	 *
	 * @return the dir enviados
	 */
	public String getDirEnviados() {
		return dirEnviados;
	}

	/**
	 * Sets the dir enviados.
	 *
	 * @param dirEnviados the new dir enviados
	 */
	public void setDirEnviados(String dirEnviados) {
		this.dirEnviados = dirEnviados;
	}

	/**
	 * Gets the config rep cifras.
	 *
	 * @return the config rep cifras
	 */
	public Map<String, String> getConfigRepCifras() {
		return configRepCifras;
	}

	/**
	 * Sets the config rep cifras.
	 *
	 * @param configRepCifras the config rep cifras
	 */
	public void setConfigRepCifras(Map<String, String> configRepCifras) {
		this.configRepCifras = configRepCifras;
	}

	/**
	 * Gets the config rep cfdi det.
	 *
	 * @return the config rep cfdi det
	 */
	public Map<String, String> getConfigRepCfdiDet() {
		return configRepCfdiDet;
	}

	/**
	 * Sets the config rep cfdi det.
	 *
	 * @param configRepCfdiDet the config rep cfdi det
	 */
	public void setConfigRepCfdiDet(Map<String, String> configRepCfdiDet) {
		this.configRepCfdiDet = configRepCfdiDet;
	}

	
	/**
	 * Gets the dir rep cfdi porenviar.
	 *
	 * @return the dir rep cfdi porenviar
	 */
	public String getDirRepCfdiPorenviar() {
		return dirRepCfdiPorenviar;
	}

	/**
	 * Sets the dir rep cfdi porenviar.
	 *
	 * @param dirReportesCfdi the new dir rep cfdi porenviar
	 */
	public void setDirRepCfdiPorenviar(String dirRepCfdiEnviados) {
		this.dirRepCfdiPorenviar = dirRepCfdiEnviados;
	}

	/**
	 * Gets the nombre rep cifras error.
	 *
	 * @return the nombre rep cifras error
	 */
	public String getNombreRepCifrasError() {
		return nombreRepCifrasError;
	}

	/**
	 * Sets the nombre rep cifras error.
	 *
	 * @param nombreRepCifrasError the new nombre rep cifras error
	 */
	public void setNombreRepCifrasError(String nombreRepCifrasError) {
		this.nombreRepCifrasError = nombreRepCifrasError;
	}

	/**
	 * Gets the dir rep cfdi enviados.
	 *
	 * @return the dir rep cfdi enviados
	 */
	public String getDirRepCfdiEnviados() {
		return dirRepCfdiEnviados;
	}

	/**
	 * Sets the dir rep cfdi enviados.
	 *
	 * @param dirRepCfdiEnviados the new dir rep cfdi enviados
	 */
	public void setDirRepCfdiEnviados(String dirRepCfdiEnviados) {
		this.dirRepCfdiEnviados = dirRepCfdiEnviados;
	}
	
}

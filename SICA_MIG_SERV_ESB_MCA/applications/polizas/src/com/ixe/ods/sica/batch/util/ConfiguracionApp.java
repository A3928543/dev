package com.ixe.ods.sica.batch.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfiguracionApp {
	
	private String dirEntrada;
	
	private String dirSalida;
	
	private String dirProcesados;

	private Map<String, Map<String, String>> fillers;
	
	private LinkedHashMap<String, Map<String, Object>> body;

	public ConfiguracionApp() {
	}
	
	public Map<String, String> getEnvFiller(String type) {
		return fillers.get(type);
	}
	
	public String getDirEntrada() {
		return dirEntrada;
	}

	public void setDirEntrada(String dirEntrada) {
		this.dirEntrada = dirEntrada;
	}
	
	public String getDirSalida() {
		return dirSalida;
	}

	public void setDirSalida(String dirSalida) {
		this.dirSalida = dirSalida;
	}
	
	public String getDirProcesados() {
		return dirProcesados;
	}

	public void setDirProcesados(String dirProcesados) {
		this.dirProcesados = dirProcesados;
	}
	
	public Map<String, Map<String, String>> getFillers() {
		return fillers;
	}

	public void setFillers(Map<String, Map<String, String>> fillers) {
		this.fillers = fillers;
	}
	
	public LinkedHashMap<String, Map<String, Object>> getBody() {
		return body;
	}

	public void setBody(LinkedHashMap<String, Map<String, Object>> body) {
		this.body = body;
	}

}

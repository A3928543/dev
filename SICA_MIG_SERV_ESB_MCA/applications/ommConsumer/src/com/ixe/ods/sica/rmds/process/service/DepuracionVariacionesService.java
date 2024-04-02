package com.ixe.ods.sica.rmds.process.service;

public interface DepuracionVariacionesService {

	/**
	 * Invoca la depuraci&oacute;n de 3 elementos del modelo de dominio: Precios
	 * Referencia, Factores Divisa y Variaciones de precios.
	 * 
	 * La operaci&oacute;  debe se at&oacute;mica.
	 * 
	 * @return true si la depuraci&oacute;n fue exitosa, false de otra manera.
	 */
	public boolean depurarVariacionPrecios();

}
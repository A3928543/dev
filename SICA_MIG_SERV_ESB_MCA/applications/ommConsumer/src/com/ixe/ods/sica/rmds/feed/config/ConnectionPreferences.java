package com.ixe.ods.sica.rmds.feed.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.ixe.ods.sica.rmds.feed.exception.SicaOmmConsumerException;

import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

/**
 * Clase de utilidad que proporciona m&eacute;todos para la carga y almacenamiento
 * de propiedades de conexi&oacute; al RMDS.
 * 
 * @author Efren Trinidad, Na-at Technologies
 */
public class ConnectionPreferences {
	
	/**
	 * La instancia de utilidad para grabar mensajes en el log.
	 */
	private static Logger logger = Logger.getLogger(ConnectionPreferences.class.getName());
	
	/**
	 * Carga de un archivo de propiedades en formato XML los par&aacute;metros
	 * para la conexi&oacute;n al RMDS, as&iacute; como de la sesi&oacute;n ligada a ella.
	 * 
	 * Los par&aacute;metros cargados son almancenados en en Nodo de Usuario en las
	 * Java Preferences del Sistema.
	 * 
	 * @param propertiesFileName El nombre del archivo XML que contine las propiedades.
	 * @return true, si se cargaron correctamente las propiedades, false de otra manera.
	 * @see java.util.prefs.Preferences
	 */
	public static boolean load( String propertiesFileName ) throws SicaOmmConsumerException{
		try{
			
			logger.info("Cargando configuracion de conexion a Preferencias de archivo: " + propertiesFileName);
			FileInputStream fis = new FileInputStream(new File(propertiesFileName));
			Preferences.importPreferences(fis);
			return true;
			
		}catch(FileNotFoundException fnfe){
			throw new SicaOmmConsumerException("No se encontro el archivo <" + propertiesFileName + "> " 
					+ fnfe.getMessage(), fnfe);
		}catch(InvalidPreferencesFormatException ipe){
			throw new SicaOmmConsumerException("Archivo Preferences invalido <" + propertiesFileName + "> " 
					+ ipe.getMessage() , ipe);
		}catch (IOException ioe) {
			throw new SicaOmmConsumerException("Error cargando archivo de propiedades <" + propertiesFileName + "> " 
					+ ioe.getMessage() , ioe);
		}
	}
}

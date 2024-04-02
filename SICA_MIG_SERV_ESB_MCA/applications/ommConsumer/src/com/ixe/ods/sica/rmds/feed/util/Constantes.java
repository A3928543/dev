package com.ixe.ods.sica.rmds.feed.util;

public class Constantes {
	
	/**
	 * Field ID para el valor BID de determinada divisa.
	 */
	public static final short BID_FID = 22;
	
	/**
	 * Field ID para el valor ASK de determinada divisa.
	 */
	public static final short ASK_FID = 25;
	
	/**
	 * Nombre del RIC para la divisa USD
	 */
	public static final String USD_RIC = "MXN=D2";
	
	/**
	 * Nombre del RIC para la divisa Euro
	 */
	public static final String EUR_RIC = "EUR=";
	
	/**
	 * Nombre del RIC para la divisa Euro vs MXN
	 */
	public static final String EUR_MXN_RIC = "MXNEUR=RR";
	
	/**
	 * Nombre del RIC para la divisa Jen
	 */
	public static final String JPY_RIC = "JPY=";
	
	/**
	 * Nombre del RIC para la divisa Libra Esterlina
	 */
	public static final String GBP_RIC = "GBP=";
	
	/**
	 * Nombre del RIC para la divisa Franco Suizo
	 */
	public static final String CHF_RIC = "CHF=";
	
	/**
	 * Nombre del RIC para la divisa Canadiense
	 */
	public static final String CAD_RIC = "CAD=";
	
	/**
	 * ID del parametro de Nombre del archivo XML donde se 
	 * configuran las propiedades de conexi&oacute;n al RMDS para 
	 * el OMMConsumer. Almacendo en la tabla SC_PARAMETRO.
	 */
	public static final String OMM_CONNECTION_PROPERTIES_FILE_NAME = "OMM_CONNECTION_PROPERTIES_FILE_NAME";
	
	/**
	 * ID del par&aacute;metro de <code>Nombre de usuario</code> utilizado para hacer login
	 * en el RMDS configurado en el DACS con los permisos
	 * necesarios. Almacendo en la tabla SC_PARAMETRO.
	 */
	public static final String OMM_DACS_USERNAME = "OMM_DACS_USERNAME";
	
	/**
	 * ID del par&aacute;metro <code>sessionName</code> para la conexi&oacute;n al RMDS.
	 * Este parametro est&aacute; relacionado con la configuraci&oacute;n almacenada en
	 * el archivo OMM_CONNECTION_PROPERTIES_FILE_NAME, el nombre de la sesi&oacute;n y el
	 * namespace deben coincidir. Almacendo en la tabla SC_PARAMETRO.
	 */
	public static final String OMM_SESSION_NAME = "OMM_SESSION_NAME";
	
	/**
	 * ID del par&aacute;metro <code>loginTimeout</code>, representa el tiempo en segundos que 
	 * el programa debe esperar la respuesta a la solicitu de login enviada.
	 * Almacendo en la tabla SC_PARAMETRO.
	 */
	public static final String OMM_LOGIN_TIMEOUT = "OMM_LOGIN_TIMEOUT";
	
	/**
	 * ID del par&aacute;metro <code>marketFeedTimeOut</code>, representa el tiempo en segundos que 
	 * el programa debe escuchar los eventos de actualizaci&oacute;n del precio de divisas.
	 * Almacendo en la tabla SC_PARAMETRO.
	 */
	public static final String OMM_MARKETFEED_TIMEOUT = "OMM_MARKETFEED_TIMEOUT";
	
	/**
	 * ID del par&aacute;metro <code>serviceName</code> en el RMDS al cual se
	 * conecta este programa. Almacendo en la tabla SC_PARAMETRO.
	 */
	public static final String OMM_SERVICE_NAME = "OMM_SERVICE_NAME";
	
	/**
	 * ID del par&aacute;metro de la lista de RIC's a los cuales se suscribir&aacute; el
	 * programa. Cadena de caract&eacute;res separados por coma (,).
	 */
	public static final String OMM_RIC_LIST = "OMM_RIC_LIST";
	
	/**
	 * ID del par&aacute;metro de la lista de FID's que se definiran como InterestView en la
	 * suscripci&oacute;n a cada RIC. Cadena de caract&eacute;res separados por coma (,).
	 */
	public static final String OMM_INTERESTVIEW_LIST = "OMM_INTERESTVIEW_LIST";
	
	/**
	 * ID del par&aacute;metro que indica la hora en que detenerse el proceso
	 * de alimentaci&oacute;n de precios desde el RMDS, debe tener el formato
	 * HH:MM.
	 */
	public static final String OMM_MARKETFEED_ENDTIME = "OMM_MARKETFEED_ENDTIME";
	
	/**
	 * Cantidad de divisas que deben ser leidas del RMDS.
	 */
	public static final int OMM_DIV_NUM = 6;
	
	
	
	
	

}

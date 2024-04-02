package test.ods.sica.pizarron;

/**
 * Contiene constantes con código SQL para ejecutar sobre SICA_VARIACION
 * 
 * @author Cesar Jeronimo Gomez
 *
 */
public class VariacionSql {
	
	/**
	 * Sentencia SQL para inserción 
	 */
	public static final String INSERT_VARIACION = 
		"INSERT INTO SICA_ADMIN.SICA_VARIACION ( "
			+ "ID_VARIACION, "
			+ "FECHA, "
			+ "VARIACION_MID, "
			+ "VARIACION_VTA_SPOT, "
			+ "VARIACION_VTA_SPOT_ASK, "
			+ "MXN_EUR_VARIACION_VTA_SPOT, "
			+ "MXN_EUR_VARIACION_VTA_SPOT_ASK, "
			+ "CAD_VARIACION_VTA_SPOT, "
			+ "CAD_VARIACION_VTA_SPOT_ASK, "
			+ "CHF_VARIACION_VTA_SPOT, "
			+ "CHF_VARIACION_VTA_SPOT_ASK, "
			+ "EUR_VARIACION_VTA_SPOT, "
			+ "EUR_VARIACION_VTA_SPOT_ASK, "
			+ "GBP_VARIACION_VTA_SPOT, "
			+ "GBP_VARIACION_VTA_SPOT_ASK, "
			+ "JPY_VARIACION_VTA_SPOT, "
			+ "JPY_VARIACION_VTA_SPOT_ASK "
		+ ") VALUES (SICA_ADMIN.SICA_VARIACION_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

}

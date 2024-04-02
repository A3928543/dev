package com.ixe.ods.sica.lineacredito.service;


/**
 * Constantes para identificar los tipos de operaci&oacute;n en la Bit&aacute;cora de 
 * operaciones - SC_LINEA_CAMBIO_LOG campo TIPO_OPER,  de las L&iacute;neas de Cr&eacute;dito
 */
public interface TipoOperacion {
		
	public static final String AUMENTO = "A";
	public static final String DISMINUCION = "D";
	public static final String USO_LC = "U";
	public static final String VENCIMIENTO = "V";
	public static final String LIBERACION_LINEA_CREDITO = "L";
	public static final String SUSPENSION = "S";
	public static final String ALTA = "T";
	public static final String APROBACION = "P";
	public static final String ACTIVACION = "C";
	public static final String EXCESO = "E";
	public static final String AVISO = "O";
	public static final String CANCELACION = "N";
	
	public static final String CAMBIO_FECHA_VENCIMIENTO = "F";
	public static final String CAMBIO_INSTANCIA_FACULTADA = "I";
	public static final String CAMBIO_TIPO_AUTORIZACION = "Z";
	public static final String CAMBIO_FORMALIZACION = "M";
	
}

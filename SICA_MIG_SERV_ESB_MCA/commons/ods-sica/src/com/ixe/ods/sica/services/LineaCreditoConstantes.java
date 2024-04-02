package com.ixe.ods.sica.services;

public interface LineaCreditoConstantes {

	 /**
     * Constante Estado de la Linea de Credito - Solicitud.
     */
    public static final String STATUS_SOLICITUD = "SO";

    /**
     * Constante Estado de la Linea de Credito -  Aprobada.
     */
    public static final String STATUS_APROBADA = "AP";

    /**
     * Constante Estado de la Linea de Credito -  Activada.
     */
    public static final String STATUS_ACTIVADA = "OK";

    /**
     * Constante Estado de la Linea de Credito -  Vencida.
     */
    public static final String STATUS_VENCIDA = "VE";

    /**
     * Constante Estado de la Linea de Credito -  Suspendida.
     */
    public static final String STATUS_SUSPENDIDA = "SU";

    /**
     * Constante Estado de la Linea de Credito -  Cancelada
     */
    public static final String STATUS_CANCELADA = "CA";
	
	public static final String CADENA_FIN = " en la L\u00ednea de Cr\u00e9dito";
	public static final String CAMBIO_INICIAL = "Cambio en: ";
	public static final String IMPORTE_FV = "Importe Fecha Valor, " + CADENA_FIN;
	public static final String IMPORTE_PAYTF = "Importe PAyTF, " + CADENA_FIN;
	public static final String FECHA_VENCIMIENTO = "Fecha de Vencimiento, " + CADENA_FIN;
	public static final String INSTANCIA_FACULTADA = "Instancia Facultada, " + CADENA_FIN;
	public static final String TIPO_AUTORIZACION = "Tipo de Autorizaci\u00f3n, " + CADENA_FIN;
	public static final String FORMALIZACION = "Formalizaci\u00f3n, " + CADENA_FIN;
	public static final String EXCESO_PAYTF = "Exceso PAyTF";
	public static final String EXCESO_FV = "Exceso FV";

	public static final String USO = "U ";
	
	public static final String LIBERACION = "L ";

	public static final String MONTO_PAYTF = "PAyTF";

	public static final String MONTO_FV = "FV";
	
	
}

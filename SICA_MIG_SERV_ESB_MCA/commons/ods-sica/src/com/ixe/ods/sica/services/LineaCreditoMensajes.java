package com.ixe.ods.sica.services;

public interface LineaCreditoMensajes {
	
	public static final String LINEA_CREDITO_NO_ACTIVA = "El Cliente no cuenta con una L\u00ednea de Cr\u00e9dito Activa por lo que solo se podrán "
														+ "capturar operaciones CASH y adicional no se podrá solicitar Pago Anticipado y/o Toma en Firme";
	public static final String OPERACION_NO_CASH = "No se pueden capturar detalles {TOM, SPOT, 72HR Y 96HR} ya que el Cliente no cuenta con una L\u00ednea de Cr\u00e9dito Activa.";
	
	public static final String ERROR_DEAL_CAPTURA_RAPIDA_ = "No se pueden capturar detalles {TOM, SPOT, 72HR Y 96HR} ya que no se ha seleccionado un Cliente.";
	
	public static final String REBASA_SALDOFV = "Se excede el monto FV de la L\u00ednea de Cr\u00e9dito, favor de validar o solicitar un exceso.";
	
	public static final String REBASA_SALDOPAYTF = "Se excede el monto para Pago Anticipado y/o Toma en Firme de la Línea de Crédito, favor de validar o solicitar un exceso";
	
	public static final String REBASA_LIMITE_EXCEPCIONES = 	"El n\u00famero de excepciones en el mes ha llegado al l\u00edmite. No se " +
    "acepta toma en firme ni pago anticipado para este deal.";
	
	public static final String MONTO_DETALLE_CERO = "El monto del Detalle del Deal no puede ser CERO.";
	
	public static final String LIBERACION = "Liberaci\u00f3n de l\u00ednea de cr\u00e9dito.";
	
	public static final String USO = "Uso de l\u00ednea de cr\u00e9dito.";
	
	public static final String LINEA_CREDITO_NO_ACTIVA_REVERSO = "L\u00ednea de Cr\u00e9dito no se encuentra activa.";
	
	public static final String VENCIMIENTO_LINEA = "Vencimiento de la L\u00ednea de Cr\u00e9dito";
	
}

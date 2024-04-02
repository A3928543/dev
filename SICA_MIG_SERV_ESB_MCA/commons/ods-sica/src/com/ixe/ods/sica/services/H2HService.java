package com.ixe.ods.sica.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ixe.ods.sica.model.DealDetalle;

public interface H2HService 
{
	// Divisas
	String USD = "USD";
	String CAD = "CAD";
	String EUR = "EUR";
	String GBP = "GBP";
	String BRL = "BRL";
	String JPY = "JPY";
	String AUD = "AUD";
	String NZD = "NZD";
	String CHF = "CHF";
	String OTD = "OTD";
    
	// Plazo - Producto
	String FX_V_HOY   = "FX_V_HOY";
	String FX_V_24HRS = "FX_V_24HRS";
	String FX_V_48HRS = "FX_V_48HRS";
	String FX_FORWARD = "FX_FORWARD";
	
	String NO_ENVIADA = "N";
	String STATUS_NUEVA_H2H = "A"; // Autorizada
	String STATUS_BAJA_H2H = "B"; // Autorizada
	
	String NO_CANCELADO = "N";
	String CANCELADO = "S";
	
	String COMPRA = "B";
	String VENTA = "S";
	
	Integer PROCESADO_INTRADIA = new Integer(0);
	Integer PROCESADO_CIERRE_VESPERTINO = new Integer(1);
	
	Integer VERSION_1 = new Integer(1);
	Integer VERSION_CANCELADA = new Integer(2);
	
	BigDecimal MILLON_DOLARES_USD = new BigDecimal("1000000.0");
	
	ArrayList PROCESAR_TODOS_LOS_DETALLES = null;
	
	int MENOR_MILLON_DOLARES = -1;
	
	boolean NO_ES_CIERRE_VESPERTINO = false;
	boolean ES_CIERRE_VESPERTINO = true;
	
	boolean ES_REVERSO = true;
	boolean NO_ES_REVERSO = false;
	
	/**
	 * Servicio que verificara si los deals que esten en estatus ALTA o PROCESANDOSE(con semaforos por autorizar)
	 * deben registrase y ser enviados al H2H
	 */
	void validarDealsH2HAlCierreVespertino();
	
	/**
	 * Servicio que verifica si los detalles de deal contenidos en el parametro 'listaDeals' deben enviarse al H2H 
	 * @param listaDeals          Lista de deals que se van a procesar para enviarse al H2H.
	 * @param detallesAprocesar   Este parametro permite procesar algunos detalles del unico deal contenido en 'listaDeals'. 
	 *                            Si se van a procesar todos los detalles de cada deal en 'listaDeals', 
	 *                            el parametro 'detallesAprocesar' debe ser null.
	 * @param esCierreVespertino  Indica si el sistema ya va a realizar el cierre vespertino
	 */
	void registrarDetallesH2H(List listaDeals, List detallesAprocesar, boolean esCierreVespertino);
	
	/**
	 * Servicio para solicitar la cancelacion de un detalle de deal
	 * @param detalle
	 */
	void solicitarCancelacionDetalleH2H(DealDetalle detalle, boolean esReverso);
}

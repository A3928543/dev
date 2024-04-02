package com.banorte.h2h.dao;

import java.util.List;

import com.banorte.h2h.dto.DetalleH2H;
import java.sql.Types;

public interface SicaDao 
{
	int[] BATCH_TYPES_DETALLE_REPORTADO = new int[]{Types.NUMERIC};
	int[] BATCH_TYPES_DETALLE_CANCELADO = new int[]{Types.NUMERIC};
	int[] BATCH_TYPES_FOLIOS_BANXICO = new int[]{Types.VARCHAR, Types.NUMERIC, Types.NUMERIC};
	
	int OPCION_DETALLE_REPORTADO = 1;
	int OPCION_FOLIO_BANXICO = 2;
	int OPCION_CANCELAR_DETALLE = 3;
			
	String PARAMETRO_INDICE_H2H = "INDICE_H2H";
	String DETALLE_ENVIADO_H2H = "S";
	String DETALLE_NO_ENVIADO_H2H = "N";
	
	Integer VERSION_CANCELAR = new Integer(2);
	String STATUSH2H_CANCELAR = "B";
	
	String CONSULTA_NUEVOS_DETALLES_H2H = "SELECT * FROM SC_DETALLES_H2H " +
										  "WHERE enviada = 'N' AND " +
										  "      statush2h = 'A' AND " +
										  "      folio_banxico IS NULL AND " +
										  "      fecha_canc IS NULL AND " +
										  "      TRUNC(fecha_captura) = TRUNC(SYSDATE) AND " +
										  "      TRUNC(fecha_envio) = TRUNC(SYSDATE)";
	
	String CONSULTA_DETALLES_SIN_FOLIO_BANXICO = "SELECT * FROM SC_DETALLES_H2H " +
												 "WHERE enviada = 'S' AND " +
												 "      statush2h = 'A' AND " +
												 "      folio_banxico IS NULL AND " +
	//											 "      fecha_canc IS NULL AND " +
												 "      TRUNC(fecha_captura) = TRUNC(SYSDATE) AND " +
												 "      TRUNC(fecha_envio) = TRUNC(SYSDATE)";
	
	String CONSULTA_DETALLES_POR_CANCELAR =  "SELECT * FROM SC_DETALLES_H2H " +
											 "WHERE enviada = 'S' AND " +
											 "      statush2h = 'A' AND " +
											 "      (folio_banxico IS NOT NULL AND folio_banxico > 0) AND " +
											 "      fecha_canc IS NULL AND " +
											 "      CANCELAR_DETALLE = 'S' AND " +
											 "      VERSION = 1 AND " +
											 "      TRUNC(fecha_captura) = TRUNC(SYSDATE) AND " +
											 "      TRUNC(fecha_envio) = TRUNC(SYSDATE)";
	
	
	String CONSULTA_PARAMETRO = "select VALOR from SC_PARAMETRO where ID_PARAMETRO = ?";
	
	String ACTUALIZA_DETALLE_H2H_SICA = "UPDATE SC_DETALLES_H2H SET ENVIADA = 'S' WHERE ID_DEAL_POSICION = ?";
	
	String ACTUALIZA_FOLIO_BANXICO_DETALLE_SICA = "UPDATE SC_DETALLES_H2H SET folio_banxico = ?, COD_ERROR = ? WHERE ID_DEAL_POSICION = ?";
	
	String ACTUALIZA_DETALLE_CANCELADO_SICA = "UPDATE SC_DETALLES_H2H SET STATUSH2H = 'B', ENVIADA = 'S', VERSION = 2, FECHA_CANC = SYSDATE WHERE ID_DEAL_POSICION = ?";
	
	List<DetalleH2H> consultarNuevosDetallesDealEnviarH2H();
	
	List<DetalleH2H> consultarFoliosBanxicoDetallesDealEnviadosH2H();
	
	List<DetalleH2H> consultarDetallesPorCancelar();
	
	void actualizarDetallesSica(List<DetalleH2H> detallesH2H, int opcion);
	
	String consultaParametro(String parametro);
}

package com.banorte.contrapartes.services;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import com.banorte.contrapartes.dto.OperacionDto;

public interface IReporteOperaciones 
{
	public ByteArrayOutputStream crearReporteExcel(List<OperacionDto> operaciones);
		
	final int _0_CLIENTE = 0;
	final int _1_CONTRATO_SICA = 1;
	final int _2_TIPO_PERSONA = 2;
	final int _3_RFC = 3;
	final int _4_NUMERO_DEAL = 4;
	final int _5_FOLIO_DETALLE = 5;
	final int _6_ENTREGAR_RECIBIR = 6;
	final int _7_ID_DIVISA = 7;
	final int _8_MONTO_DIVISA_ORIGINAL = 8;
	final int _9_MONTO_USD_DETALLE = 9;
	final int _10_STATUS_DETALLE_DEAL = 10;
	final int _11_PROMOTOR = 11;
	final int _12_ID_CANAL = 12;
	final int _13_FECHA_CREACION = 13;
	final int _14_FECHA_VALOR = 14;
	final int _15_STATUS_DEAL = 15;
	final int _16_DEAL_CORRECCION = 16;
	final int _17_NUM_DEAL_ORIGINAL = 17;
	
	final int w_0_CLIENTE = 9000;
	final int w_1_CONTRATO_SICA = 4200;
	final int w_2_TIPO_PERSONA = 4200;
	final int w_3_RFC = 4200;
	final int w_4_NUMERO_DEAL = 4500;
	final int w_5_FOLIO_DETALLE = 4700;
	final int w_6_ENTREGAR_RECIBIR = 6800;
	final int w_7_ID_DIVISA = 3800;
	final int w_8_MONTO_DIVISA_ORIGINAL = 7000;
	final int w_9_MONTO_USD_DETALLE = 7100;
	final int w_10_STATUS_DETALLE_DEAL = 4200; 
	final int w_11_PROMOTOR = 9000;
	final int w_12_ID_CANAL = 3800;
	final int w_13_FECHA_CREACION = 4500;
	final int w_14_FECHA_VALOR = 4200;
	final int w_15_STATUS_DEAL = 4200;
	final int w_16_DEAL_CORRECCION = 5700;
	final int w_17_NUM_DEAL_ORIGINAL = 6800;
	
	final int widthColumns[] = new int[]{w_0_CLIENTE, w_1_CONTRATO_SICA, w_2_TIPO_PERSONA, w_3_RFC, w_4_NUMERO_DEAL,
										 w_5_FOLIO_DETALLE,w_6_ENTREGAR_RECIBIR,w_7_ID_DIVISA,
										 w_8_MONTO_DIVISA_ORIGINAL,w_9_MONTO_USD_DETALLE,w_10_STATUS_DETALLE_DEAL,
										 w_11_PROMOTOR,w_12_ID_CANAL, w_13_FECHA_CREACION,w_14_FECHA_VALOR,
										 w_15_STATUS_DEAL,w_16_DEAL_CORRECCION,w_17_NUM_DEAL_ORIGINAL};
	
	final int columnas[] = new int[]{_0_CLIENTE,_1_CONTRATO_SICA,_2_TIPO_PERSONA,_3_RFC,
			                         _4_NUMERO_DEAL,_5_FOLIO_DETALLE,_6_ENTREGAR_RECIBIR,
			                         _7_ID_DIVISA,_8_MONTO_DIVISA_ORIGINAL,_9_MONTO_USD_DETALLE,
			                         _10_STATUS_DETALLE_DEAL,_11_PROMOTOR,_12_ID_CANAL,_13_FECHA_CREACION,
			                         _14_FECHA_VALOR,_15_STATUS_DEAL,_16_DEAL_CORRECCION, _17_NUM_DEAL_ORIGINAL};
	
	final List<String> encabezados = Arrays.asList("CLIENTE", "CONTRATO SICA", "TIPO PERSONA", "RFC", "NUMERO DE DEAL",
												   "FOLIO DE DETALLE", "ENTREGAMOS O RECIBIMOS", "DIVISA",
												   "MONTO EN DIVISA ORIGINAL", "MONTO EN USD DEL DETALLE",
												   "ESTATUS DETALLE", "PROMOTOR", "CANAL", 
												   "FECHA CREACION", "FECHA VALOR", "ESTATUS DEAL",
			                                        "¿DEAL DE CORRECION?", "NUMERO DE DEAL ORIGINAL"   
			                                        );
}

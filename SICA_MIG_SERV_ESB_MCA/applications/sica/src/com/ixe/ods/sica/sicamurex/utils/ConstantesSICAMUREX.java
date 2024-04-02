package com.ixe.ods.sica.sicamurex.utils;

import java.math.BigDecimal;

public interface ConstantesSICAMUREX {

	public static final String CASH = "CASH";
	public static final String COMPRA_STR = "COMPRA";
	public static final String BUY_STR = "buy";
	public static final String TOM = "TOM";
	public static final String VENTA_STR = "VENTA";
	public static final String SELL_STR = "sell";
	public static final String SPOT = "SPOT";
	public static final String VFUT = "VFUT";
	public static final String HR72 = "72HR";
	public static final String MXN = "MXN";
	public static final String USD = "USD";
	public static final int NDECIMALES_TC = 6;
	public static final int NDECIMALES_MONTO = 2;
	public static final int TIPO_REDONDEO_MONTO = BigDecimal.ROUND_HALF_UP;
	public static final int TIPO_REDONDEO_TC    = BigDecimal.ROUND_DOWN;
	public static final int TIPO_REDONDEO_TC_DI    = BigDecimal.ROUND_HALF_UP;
	public static final String ID_CERO = "0";
	public static final String TODAS_LAS_DIVISAS = "TODAS";
	public static final String COMPRA = "CO";
	public static final String VENTA = "VE";
	
	public static final String SICA_MUREX_SERVICE = "sicaMurexService";
	public static final String FTP_SERVICE = "ftpService";
	
	/**
	 *  Variable estatica que define el estado en que se encuentra la pantalla de cortes.
	 *  Se ha generado la Vista Previa del Corte.
	 */
	public static final String VISTA_PREVIA = "VISTA_PREVIA";
	
	
	
	/**
	 *  Variable estatica que define el estado en que se encuentra la pantalla de cortes
	 *  Ocurrio un error al realizar la transferencia del Corte de SICA a MUREX.
	 */
	public static final String ERROR_EN_TRANSFERENCIA = "ET";
		
	/**
	 *  Variable estatica que define el estado en que se encuentra la pantalla de cortes
	 *  Ocurrio un error al crear los DEALS Interbancarios.
	 */
	public static final String ERROR_DEALS_INTERBANCARIOS = "EI";
	
	/**
	 *  Variable estatica que define el estado en que se encuentra la pantalla de cortes
	 *  El corte ha sido transferido de SICA a MUREX.
	 */
	public static final String ENVIADO_MUREX = "EM";
	
	/**
	 *  Variable estatica que define el estado en que se encuentra la pantalla de cortes
	 *  Se ha enviado el Corte a MUREX y se han creado los DEALS Interbancarios.
	 */
	public static final String CORTE_PROCESADO = "OK";
	
	/**
	 * Variable estatica que define el estado en que se encuentra la pantalla de cortes
	 * Se ha generado la vista previa de los DEALS Interbancarios.
	 */
	public static final String VISTA_PREVIA_DEALS_INTERBANCARIOS = "VPDI";
	
	/**
	 * Variable estatica que define el estado en que se encuentra la pantalla de cortes
	 * Inicio del flujo de envio de corte
	 */
	public static final String INICIO = "";
	
	/**
	 * Estado incorrecto en SICA para procesar la pantalla de cortes
	 */
	public static final String ESTADO_SICA_INCORRECTO = "SI";	
	public static final String DEAL_ESTADO_ALTA = "AL";
	public static final String FORMA_LIQUIDACION_EFECTIVO = "EFECTI";
	public static final int OPERACION = 0;
	public static final int DIVISA = 1;
	public static final int FECHA_VALOR = 2;
	public static final String SICA_SITE_SERVICE = "sicaSiteCache";
	public static final String RECIBIMOS = "9";
	public static final String ENTREGAMOS = "10";
	public static final String CORTE_HEADER = "CORTE";
	public static final String FECHA_HEADER = "FECHA / HORA (CORTE)";
	public static final String DIVISA_HEADER = "DIVISA";
	public static final String FV_HEADER = "FECHA VALOR";
	public static final String TIPO_OPERACION_HEADER = "OPERACION";
	public static final String PRICE_HEADER = "PRICE";
	public static final String MONTO_DIVISA = "MONTO DIVISA";
	public static final String F_COST_HEADER = "FUNDING COST";
	public static final String TC_HEADER = "T.C.";
	public static final String TC_MESA_HEADER = "T.C. MESA";
	public static final String TC_CLIENTE_HEADER = "T.C. CLIENTE";
	public static final String DEAL_HEADER = "NO. DEAL";
	public static final String SALTO_LINEA = "\n";
	public static final String SEPARADOR_CSV = ",";
	public static final String ESTADO_CORTE_HEADER = "ESTADO";
	public static final String SICA_LAYOUT_SERVICE = "layoutServices";

}
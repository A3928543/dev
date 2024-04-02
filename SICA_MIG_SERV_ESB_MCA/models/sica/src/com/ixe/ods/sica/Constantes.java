/*
 * $Id: Constantes.java,v 1.24.2.1.8.1.6.1.2.2.44.2 2016/08/03 01:07:35 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */
package com.ixe.ods.sica;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Clase que contiene las Contantes del SICA.
 *
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.24.2.1.8.1.6.1.2.2.44.2 $ $Date: 2016/08/03 01:07:35 $
 */
public class Constantes {

    /**
     * Constante para la clave de canal de Ixe Directo.
     */
    public static final String CANAL_IXE_DIRECTO = "DIR";

    /**
     * Constante para el Canal Estrategia
     */
    public static final String CANAL_MESA_ESTRA = "EST";

    /**
     * Constante Par&aacute;metro Canal Mesa Operaci&oacute;n.
     */
    public static final String CANAL_MESA_OPERACION = "MOP";

    /**
     * Constante para el Canal Trader MXN
     */
    public static final String CANAL_MESA_TRADER_MXN = "TMX";

    /**
     * Constante para el Canal Trader USD
     */
    public static final String CANAL_MESA_TRADER_USD = "TDL";

    /**
     * Constante Fecha Valor CASH.
     */
    public static final String CASH = "CASH";

    /**
     * Constante Clave Tipo Liquidaci&oacute;n Cheque.
     */
    public static final String CHEQUE = "CHEQUE";    

    /**
     * Constante para la cadena 'Compra'.
     */
    public static final String COMPRA = "Compra";

    /**
     * Constante para formatear montos de tipos de cambio con el siguiente formato: #,##0.00000000.
     */
    public static DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.000000");

    /**
     * Formato para fechas.
     */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    /**
	 * Constate para "difTomCash"
	 */
	public static final String DIF_TOM_CASH = "difTomCash";

    /**
     * Constate para "difSpotTom"
     */
    public static final String DIF_SPOT_TOM = "difSpotTom";

    /**
     * Constate para "dif72HrSpot"
     */
    public static final String DIF_72_SPOT = "dif72HrSpot";

    /**
     * Constate para "difVFut72Hr"
     */
    public static final String DIF_VFUT_72 = "difVFut72Hr";

    /**
     * Constante Producto Documento Extranjero.
     */
    public static final String DOCUMENTO = "DOCEXT";

    /**
     * Constante Producto Efectivo.
     */
    public static final String EFECTIVO = "EFECTI";

    /**
     * Constante para el Mnem&oacute;nico EMXNBALNETEO.
     */
    public static final String EMXNBALNETEO = "EMXNBALNETEO";

    /**
     * Constante Status Expediente para la Validaci&oacute;n de Documentaci&oacute;n de un Cliente.
     */
    public static final String EXP_DOC_STATUS = "NO ENTREG";

    /**
     * Constante Fecha Valor 72 Horas.
     */
    public static final String HR72 = "72HR";

    /**
     * Constante ID Mesa TRADER USD.
     */
    public static final int ID_MESA_TRADER_USD = 22;

    /**
     * ID PAIS de M&eacute;xico en la BUP.
     */
    public static final String ID_PAIS_MEXICO_BUP = "48";

    /**
     * Constante Clave Tipo Liquidaci&oacute;n L&iacute;nea Bancaria.
     */
    public static final String LINEA_BANCARIA = "LINBAN";

    /**
     * Constante Fecha Valor +48.
     */
    public static final String MAS_48 = "+48";

    /**
     * Constante Producto Metales Amonedados.
     */
    public static final String METALES_AMONEDADOS = "METALS";

    /**
     * Constante Producto Mexd&oacute;lar.
     */
    public static final String MEXDOLAR = "MEXDOL";

    /**
     * Constante para el status de mnem&oacute;nicos activos.
     */
    public static final String MNEMONICO_ACTIVO = "ACTI";

    /**
     * Constante para formatear montos monetarios con el siguiente patr&oacute;n: #,##0.00.
     */
    public static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.00");
    
    /**
     * Constante para formatear el TCC al modificar el producto en el detalle del deal, con el siguiente patr&oacute;n #0.000000}.
     */
    public static final DecimalFormat TCC_FORMAT = new DecimalFormat("#0.000000");

    /**
     * Constante para la cadena 'N'.
     */
    public static final String NO = "N";

    /**
     * Constante Persona F&iacute;sica.
     */
    public static final String PERSONA_FISICA = "PF";
    
    /**
     * Constante Persona Moral.
     */
    public static final String PERSONA_MORAL = "PM";

    /**
     * Constante para el Mnem&oacute;nico RMXNBALNETEO.
     */
    public static final String RMXNBALNETEO = "RMXNBALNETEO";

    /**
     * SC_ALTA Aviso de Alta de deal.
     */
    public static final String SC_ALTA = "SC_ALTA";

    /**
     * SC_CAN Aviso de cancelaci&oacute;n de deal.
     */
    public static final String SC_CAN = "SC_CAN";

    /**
     * SC_CR_ST Status err&oacute;neo al Cierre del d&iacute;a SICA.
     */
    public static final String SC_CR_ST = "SC_CR_ST";

    /**
     * SC_TIMEO Aviso de cancelaci&oacute;n de deal.
     */
    public static final String SC_TIMEO = "SC_TIMEO";

    /**
     * Constante para la cadena 'S'.
     */
    public static final String SI = "S";

    /**
     * Constante para el men&uacute; de Tesorer&iacute;a de Consulta de Deals.
     */
    public static final String SICA_CONS_DEAL_T = "SICA_CONS_DEAL_T";

    /**
     * Constante para el t&eacute;rmino SPEI.
     */
    public static final String SPEI = "SPEI";

    /**
     * Constante Fecha Valor SPOT.
     */
    public static final  String SPOT = "SPOT";    

    /**
     * Constante para la cadena 'TODOS'.
     */
    public static final String TODOS = "TODOS";

    /**
     * Constante Fecha Valor TOM.
     */
    public static final String TOM = "TOM";

    /**
     * Constante Persona Actividad Empresarial.
     */
    public static final String TP_ACTIVIDAD_EMPRESARIAL = "AE";

    /**
     * Constante Producto Transferencia Extranjera.
     */
    public static final String TRANSFERENCIA = "TRAEXT";

    /**
     * Constante Producto Transferencia Nacional.
     */
    public static final String TRANSFERENCIA_NACIONAL = "TRANAL";

    /**
     * Constante Producto Traveler Checks.
     */
    public static final String TRAVELER_CHECKS = "CHVIAJ";
    
    /**
     * Constante para la cadena 'MXN'.
     */
    public static final String MXN = "MXN";

    /**
     * Constante para Validaci&oacute;n de Deals con operaciones mayores a tres mil d&oacute;lares
     * americanos.
     */
    public static final String LIM_USD_DOC_CL = "LIM_USD_DOC_CL";
    
    /**
     * Constante para Validaci&oacute;n de Deals con operaciones mayores a tres CIENTOS d&oacute;lares
     * americanos.
     */
    public static final String LIM_USD_DOC_US = "LIM_USD_DOC_US";

    /**
     * Constante para la cadena 'Venta'.
     */
    public static final String VENTA = "Venta";

    /**
     * Constante Fecha Valor Valor Futuro.
     */
    public static final String VFUT = "VFUT";
    
    /**
     * Constante para la zona fronteriza.
     */
    public static final String ZONA_FRONTERIZA = "ZF";
    
    /**
     * Constante para la zona tur&iacute;stica.
     */
    public static final String ZONA_TURISTICA = "ZT";
    
    /**
     * Constante para la zona normal.
     */
    public static final String ZONA_NORMAL = "ZN";
    
    /**
     * Constante para la cadena 'modifProd'.
     */
    public static final String MODIF_PROD = "modifProd";
    
    /**
     * Constante para la cadena 'modifMto'.
     */
    public static final String MODIF_MTO = "modifMto";
    
    /**
     * Constante para formatear montos de tipos de cambio con el siguiente formato: #,##0.0000.
     */
    public static DecimalFormat CURRENCY4_FORMAT = new DecimalFormat("#,##0.0000");

	
    
    public static final int COD_IBAN_LONG_ERR = 1;
    
    public static final int COD_IBAN_ISO_ERR = 2;
    
    public static final int COD_IBAN_OK = 0;
    
    public static final int COD_IBAN_LONG_CLAVE_ERR = 3;
    
    public static final int COD_IBAN_LONG_ERR_ISO_ERR = 4;//SE AGREGO UNA CONSTANTE CON EL CODIGO DE ERROR CUANDO LA CLAVE Y LONGITUD VIENEN MAL JDCH 18/06/2012

	public static final String OPERACION_COMPRA = "C";

	public static final String OPERACION_VENTA = "S";

	public static final boolean PAGO_ANTICIPADO = false;

	public static final boolean TOMA_EN_FIRME = true;

	

}

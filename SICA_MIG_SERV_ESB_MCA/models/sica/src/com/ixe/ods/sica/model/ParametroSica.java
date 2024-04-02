/*
 * $Id: ParametroSica.java,v 1.31.2.1.6.1.2.2.2.1.4.1.4.1.2.1.4.1.4.1.2.3.12.1.2.1.6.2.2.2.2.1 2016/07/13 21:42:36 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_PARAMETRO.
 *
 * @hibernate.class table="SC_PARAMETRO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.ParametroSica"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.31.2.1.6.1.2.2.2.1.4.1.4.1.2.1.4.1.4.1.2.3.12.1.2.1.6.2.2.2.2.1 $ $Date: 2016/07/13 21:42:36 $
 */
public class ParametroSica implements Serializable {

    /**
     * Constructor por default. No hace nada.
     */
    public ParametroSica() {
        super();
    }
    
    /**
     * Constructor. Inicializa parametros del objeto.
     * 
     * @param idParametro Nombre del par&aacute;metro.
     * @param caducidad Fecha de caducidad.
     * @param tipoValor Tipo de valor del par&aacute;metro.
     * @param valor	Valor correspondiente al par&aacute;metro.
     */
    public ParametroSica(String idParametro, Date caducidad, String tipoValor, String valor) {
        this();
        this.idParametro = idParametro;
        this.caducidad = caducidad;
        this.tipoValor = tipoValor;
        this.valor = valor;
    }

    /**
     * Regresa el valor de idParametro.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_PARAMETRO"
     * unsaved-value="null"
     * @return String.
     */
    public String getIdParametro() {
        return idParametro;
    }

    /**
     * Fija el valor de idParametro.
     *
     * @param idParametro El valor a asignar.
     */
    public void setIdParametro(String idParametro) {
        this.idParametro = idParametro;
    }

    /**
     * Regresa el valor de caducidad.
     *
     * @hibernate.property column="CADUCIDAD"
     * not-null="false"
     * unique="false"
     * @return Date.
     */
    public Date getCaducidad() {
        return caducidad;
    }

    /**
     * Fija el valor de caducidad.
     *
     * @param caducidad El valor a asignar.
     */
    public void setCaducidad(Date caducidad) {
        this.caducidad = caducidad;
    }

    /**
     * Regresa el valor de tipoValor.
     *
     * @hibernate.property column="TIPO_VALOR"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoValor() {
        return tipoValor;
    }

    /**
     * Fija el valor de tipoValor.
     *
     * @param tipoValor El valor a asignar.
     */
    public void setTipoValor(String tipoValor) {
        this.tipoValor = tipoValor;
    }

    /**
     * Regresa el valor de valor.
     *
     * @hibernate.property column="VALOR"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getValor() {
        return valor;
    }

    /**
     * Fija el valor de valor.
     *
     * @param valor El valor a asignar.
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof ParametroSica)) {
            return false;
        }
        ParametroSica castOther = (ParametroSica) other;
        return new EqualsBuilder().append(this.getIdParametro(),
                castOther.getIdParametro()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdParametro()).toHashCode();
    }

    /**
     * El nombre del par&aacute;metro.
     */
    private String idParametro;

    /**
     * La caducidad del par&aacute;metro.
     */
    private Date caducidad;

    /**
     * I)nteger, N)umeric, C)har
     */
    private String tipoValor;

    /**
     * El valor del par&aacute;metro.
     */
    private String valor;

    /**
     * Constante para la lista de contratos que corresponden a liquidaciones especiales (deals
     * interbancarios).
     */
    public static final String CONTRATOS_LIQ_ESPECIALES = "CONTRATOS_LIQ_ESPECIALES";

    /**
     * Constante que representa si el Spread est&acute; definido para iniciar el SICA.
     */
    public static final String CONF_SPREADS = "CONF_SPREADS";

    /**
    * Constante que representa si el precio de referencia est&acute; definido para iniciar el SICA.
    */
    public static final String CONF_PRECIO_REF = "CONF_PRECIO_REFERENCIA";

    /**
     * Constante que representa si los Factores Divisa est&aacute;n definidos para iniciar el SICA.
     */
    public static final String CONF_FACTORES_DIV = "CONF_FACTORES_DIVISA";

    /**
     * Constante que representa el n&uacute;mero de d&iacute;as permitidos de adeudo para las
     * l&iacute;neas de cambio que entran en caso de excepci&oacute;n.
     */
    public static final String DIAS_EXCEPCION_LC = "DIAS_EXCEPCION_LC";
    
    /**
     * Constante que representa si los m&aacute;ximos de excedentes.
     */
    public static final String MAX_EXCEDENTES = "MAX_EXCEDENTES";

    /**
     * Constante para el n&uacute;mero m&aacute;ximo de excepciones por documentaci&oacute;n
     * permitidas para cada contrato sica.
     */
    public static final String MAX_EXCEDENTES_DOC = "MAX_EXCEDENTES_DOC";

    /**
     * Constante que representa si el monto m&aacute;ximo del deal.
     */
    public static final String MONTO_MAXIMO_DEAL = "MONTO_MAXIMO_DEAL";

    /**
     * Constante que representa si el monto m&aacute;ximo recurrente.
     */
    public static final String MONTO_MAXIMO_RECURRENTE = "MONTO_MAXIMO_RECURRENTE";

    /**
     * Constante que representa si el monto m&aacute;ximo recurrente de la operaci&oacute;n.
     */
    public static final String MONTO_MAXIMO_RECURRENTE_OP = "MONTO_MAXIMO_RECURRENTE_OP";

    /**
     * Constante que representa el valor del Capital Global de la Mesa.
     */
    public static final String CAPITAL_DEL_GRUPO = "CAPITAL_DEL_GRUPO";
    
    /**
     * Constante que representa si valor de 
     */
    public static final String HORIZONTE_TIEMPO_RIESGO = "HORIZONTE_TIEMPO_RIESGO";

    /**
     * Constante que representa la frecuencia con la que se checan que la Mesa est&eacute; dentro de
     * sus L&iacute;mites.
     */
    public static final String TIME_OUT_RIESGO = "TIME_OUT_RIESGO";

    /**
     * Constante que representa el porcentaje total con el que opera la Mesa.
     */
    public static final String PORCENTAJE_CAPITAL_GRUPO = "PORCENTAJE_CAPITAL_GRUPO";

    /**
     * Constante que representa el Nivel de Confianza.
     */
    public static final String NIVEL_CONFIANZA_RIESGO = "NIVEL_CONFIANZA_RIESGO";

    /**
     * Constante que representa el porcentaje a partir del cu&aacute;l se genera una alarma de
     * Riesgo.
     */
    public static final String PORC_ALARMA_LIMITE_REGULATORIO = "PORC_ALARMA_LIMITE_REGULATORIO";

    /**
     * Constante que representa el porcentaje a partir del cu&aacute;l se genera una aviso de
     * Riesgo.
     */
    public static final String PORC_AVISO_LIMITE_REGULATORIO = "PORC_AVISO_LIMITE_REGULATORIO";
    
    /**
     * Constante que representa si el valor del Slack del Precio de Referencia.
     */
    public static final String SLACK_PRECIO_REFERENCIA = "SLACK_PRECIO_REFERENCIA";

    /**
     * Constante que representa el ID del Par&aacute;metro LINEA_CREDITO_LLENA.
     */
    public static final String LINEA_CREDITO_LLENA = "LINEA_CREDITO_LLENA";

    /**
     * Constante que representa el ID del Par&aacute;metro DIAS_VENCIMIENTO_LINEA.
     */
    public static final String DIAS_VENCIMIENTO_LINEA = "DIAS_VENCIMIENTO_LINEA";
    
    /**
     * Constante que representa la leyenda del Par&aacute;metro DIAS_VENCIMIENTO_LINEA.
     */
    public static final String LEYENDA_IMPRESION_DEAL = "LEYENDA_IMPRESION_DEAL";
    
    /**
     * Constante que representa el administtrador des sistema SICA.
     */
    public static final String ADMIN_ID_USUARIO = "ADMIN_ID_USUARIO";
    
    /**
     * Constante que representa el director de cambios.
     */
    public static final String DIRECCION_CAMBIOS = "DIRECCION_CAMBIOS";
    
    /**
     * Constante que representa el Time-out de un Deal sin atender.
     */
    public static final String DEAL_TIMEOUT = "DEAL_TIMEOUT";

    /**
     * Constante que representa el Tipo de Cambio Vespertino
     */
    public static final String TIPO_CAMBIO_VESPERTINO = "TIPO_CAMBIO_VESPERTINO";
    
    /**
     * Constante que representa el valor del Porcentaje de Desviaci&oacute;n M&aacute;ximo.
     */
    public static final String DESV_PORC_MAX = "DESV_PORC_MAX";
    
    /**
     * Constante que representa el valor del Porcentaje de Desviaci&oacute;n M&iacute;nimo.
     */
    public static final String DESV_MONTO_LIM = "DESV_MONTO_LIM";

    /**
     * Constante que representa el valor del Limite del Porcentaje de Desviaci&oacute;n.
     */
    public static final String DESV_PORC_LIM = "DESV_PORC_LIM";

    /**
     * Constante que representa el valor del Factor de Desviaci&oacute;n 1.
     */
    public static final String DESV_FACT_1 = "DESV_FACT_1";

    /**
     * Constante que representa el valor del Factor de Desviaci&oacute;n 2.
     */
    public static final String DESV_FACT_2 = "DESV_FACT_2";
    
    /**
     * Constante que representa el n&uacute;mero de cuenta ABA de BOFA.
     */
    public static final String ABA_BANK_OF_AMERICA = "ABA_BANK_OF_AMERICA";
    
    /**
     * Constante que representa el n&uacute;mero de cuenta ABA de BOFA.
     */
    public static final String FECHA_SISTEMA = "FECHA_SISTEMA";
    
    /**
     * Constante que representa la fecha a partir del cual ya no se validara documentacion por el MOC.
     */
    public static final String FECHA_VALIDA_DOCUMENTACION = "FECHA_VALIDA_DOCUMENTACION";
    
    /**
     * Constante que representa la fecha a partir del cual ya no se validara documentacion por el MOC.
     */
    public static final String BANDERA_VALIDA_POSICION_CIERRE = "BANDERA_VALIDA_POSICION_CIERRE";
    /**
     * Constante para identificar el time out en segundos del pizarr\u00f3n:
     */
    public static final String TIMEOUT_PIZ = "TIMEOUT_PIZ";
    
    /**
     * Constante para identificar el time out en segundos del pizarr\u00f3n:
     */
    public static final String FECHA_LIM_CAPT_CONT = "FECHA_LIM_CAPT_CONT";
    
    /**
     * Constante para identificar el Spread que se define en el Horario Vespertino
     */
    public static final String SPREAD_VESPERTINO = "SPREAD_VESPERTINO";
    
    /**
     * Constante para identificar el id de la Mesa Operaci&oacute;n.
     */
    public static final String MESA_OPERACION = "MESA_OPERACION";
        
    /**
     * Constante para identificar el id Persona del Cliente Ixe Forward.
     */
    public static final String ID_PERSONA_IXE_FORWARD = "ID_PERSONA_IXE_FORWARD";
    
    /**
     * Constante para identificar el id de Parametros Automatico.
     */
    public static final String FACTOR_AUTOMATICO = "FACTOR_AUTOMATICO";

    /**
     * Constante para identificar el id del Par&aacute;metro de refresh de la pizarra.
     */
    public static final String SEGS_REFR_PIZARRA = "SEGS_REFR_PIZARRA";

    /**
     * El prefijo para los par&acute;metros de los sectores econ&oacute;micos que pueden operar a
     * 72Hrs.
     */
    public static final String PREFIJO_SECTOR_72 = "SECTOR_72_";
    
    /**
     * El prefijo para los par&acute;metros de los sectores econ&oacute;micos que pueden operar a
     * 72Hrs.
     */
    public static final String CONTRATO_SICA_IXE_FORWARD = "CONTRATO_SICA_IXE_FORWARD";

    /**
     * Par&aacute;metro que contiene el n&uacute;mero de contrato determinado para el folder KONDOR
     * MD344.
     */
    public static final String CONTRATO_FOLDER_KONDOR_MD344 = "CONTRATO_FOLDER_KONDOR_MD344";
    
    /**
     * Fecha parametrizada para la generaci&oacute;n de hist&oacute;ricos.
     */
    public static final String MESES_PARA_HISTORICOS = "MESES_PARA_HISTORICOS";
    
    /**
     * El prefijo para los par&acute;metros de los sectores econ&oacute;micos que pueden operar a
     * 72Hrs.
     */
    public static final String CONTRATO_SICA_IXE_COBERTURAS = "CONTRATO_SICA_IXE_COBERTURAS";

    /**
     * Par&aacute;metro para el porcentaje de desviaci&oacute;n al tipo de cambio en Deals
     * Interbancarios (l&iacute;mite m&aacute;ximo).
     */
    public static final String PORC_DESV_INTERB_LIM = "PORC_DESV_INTERB_LIM";

    /**
     * Par&aacute;metro para el porcentaje de desviaci&oacute;n al tipo de cambio en Deals
     * Interbancarios (advertencia).
     */
    public static final String PORC_DESV_INTERB_WARN = "PORC_DESV_INTERB_WARN";
    
    /**
     * Par&aacute;metro para el porcentaje de desviaci&oacute;n al tipo de cambio en 
     * traspasos entre portafolios (l&iacute;mite m&aacute;ximo).
     */
    public static final String PORC_DESV_TRAS_PORT_LIM = "PORC_DESV_TRAS_PORT_LIM";

    /**
     * Par&aacute;metro para el porcentaje de desviaci&oacute;n al tipo de cambio en Deals
     * Interbancarios (advertencia).
     */
    public static final String PORC_DESV_TRAS_PORT_WARN = "PORC_DESV_TRAS_PORT_WARN";
     
    /**
     * Constante para el Id del par&aacute;metro del Mnem&oacute;nico para las utilidades de Ixe
     * Directo.
     */
    public static final String MNEM_CTA_CHQ_IXE_1 = "MNEM_CTA_CHQ_IXE_1";
    
    /**
     * Constante para el Id del par&aacute;metro del Mnem&oacute;nico para las utilidades de Ixe
     * Directo.
     */
    public static final String MNEM_CTA_CHQ_IXE_2 = "MNEM_CTA_CHQ_IXE_2";
    
    /**
     * Constante para el Id del par&aacute;metro del Mnem&oacute;nico para las utilidades de Ixe
     * Directo.
     */
    public static final String MNEM_CTA_CHQ_IXE_3 = "MNEM_CTA_CHQ_IXE_3";
    
    /**
     * Constante para el Id del par&aacute;metro del Mnem&oacute;nico para las utilidades de Ixe
     * Directo.
     */
    public static final String MNEM_CTA_CHQ_IXE_4 = "MNEM_CTA_CHQ_IXE_4";
    
    /**
     * Constante para el Id del par&aacute;metro del Mnem&oacute;nico para las utilidades de Ixe
     * Directo.
     */
    public static final String MNEM_CTA_CHQ_IXE_5 = "MNEM_CTA_CHQ_IXE_5";
    
    /**
     * Constante para el Id del par&aacute;metro del Mnem&oacute;nico para las utilidades de Ixe
     * Directo.
     */
    public static final String MNEM_CTA_CHQ_IXE_6 = "MNEM_CTA_CHQ_IXE_6";

    /**
     * Constante para el Id del Par&aacute;metro del Monto de Incumplimiento de Pago para la
     * Suspensi&oacute;n de una L&iacute;nea de Cr&eacute;dito.
     */
    public static final String MONTO_INCUMP_PAGO_SUSPENSION_LC = "MONTO_INCUMP_PAGO_SUSPENSION_LC";

    /**
     * Constante para la lista de correos a las que se copiar&acute; el correo de reversos.
     */
    public static final String CC_EMAIL_REVERSO = "CC_EMAIL_REVERSO";

    /**
     * Constante para la lista de correos a las que se copiar&acute; el correo de reversos.
     */
    public static final String CC_EMAIL_REVERSO_INCL_SITE = "CC_EMAIL_REVERSO_INCL_SITE";
    
    /**
     * Constante para la lista de correos a las que se copiar&acute; el correo de solicitud de reversos.
     */
    public static final String CC_EMAIL_SOL_REVERSO = "CC_EMAIL_SOL_REVERSO";

    /**
     * Constante para el horario l&iacute;mite para la afectaci&oacute;n de la posici&oacute;n de
     * los deals de sucursales.
     */
    public static final String HORA_LIM_SUC = "HORA_LIM_SUC";

    /**
     * Constante para el porcentaje de aviso de posicici&oacute; excedida en sucursales (75%).
     */
    public static final String PORC_AVISO_SUCURSALES = "PORC_AVISO_SUCURSALES";

    /**
     * Constante para el porcentaje de aviso de posicici&oacute; excedida en sucursales (90%). 
     */
    public static final String PORC_ALERTA_SUCURSALES = "PORC_ALERTA_SUCURSALES";

    /**
     * Constante para el horario m&aacute;ximo en que puede consultarse por rango de fechas el
     * reporte de utilidades.
     */
    public static final String REP_UT_HR_MAX = "REP_UT_HR_MAX";

    /**
     * Constante para el horario m&iacute;nimo en que puede consultarse por rango de fechas el 
     * reporte de utilidades.
     */
    public static final String REP_UT_HR_MIN = "REP_UT_HR_MIN";

    /**
     * Constante para el grupo de mails, separados por coma para los cuales se notificar&aacute;
     * acerca del cierre vespertino.
     */
    public static final String EMAIL_CIERRE_VESPERTINO = "EMAIL_CIERRE_VESPERTINO";

    /**
     * Constante para el grupo de correos del &aacute;rea de contabilidad.
     */
    public static final String EMAIL_CONTABILIDAD = "EMAIL_CONTABILIDAD";

    /**
     * Constante para el grupo de mails para los cuales se les env&iacute;a el reporte de cambios
     * durante el cierre vespertino.
     */
    public static final String EMAIL_SITE_VESPERTINO = "EMAIL_SITE_VESPERTINO";

    /**
     * Constantes para el grupo de mails para los cuales se les enviar&aacute; un correo en caso
     * de existir alg&uacute;n problema en el sistema.
     */
    public static final String EMAIL_ADMINISTRADORES = "EMAIL_ADMINISTRADORES";
    
    /**
     * Constantes para el grupo de mails para los cuales se les enviar&aacute; un correo en caso
     * de existir alg&uacute;n problema en el sistema.
     */
    public static final String EMAIL_ARMS = "EMAIL_ARMS";

    /**
     * Constante para el grupo de mails para los cuales se informa el ingreso de una
     * operaci&oacute;n de kondor.
     */
    public static final String EMAIL_SWAPS_KONDOR = "EMAIL_SWAPS_KONDOR";
    
    /**
     * Constante para el modo de generacion de la contabilidad.
     */
    public static final String MODO_GEN_CONTA = "MODO_GENERACION_CONTABILIDAD";
    
    /**
     * Constant para el valor de la vigencia del token para el teller.
     */
    public static final String VIGENCIA_TOKEN_TELLER = "VIGENCIA_TOKEN_TELLER";

    /**
     * Constante para el l&iacute;mite que puede adeudar un cliente para no suspender su
     * l&iacute;nea de cambios.
     */
    public static final String LIM_ADEUDO_LIN_CAM = "LIM_ADEUDO_LIN_CAM";
    
    /**
     * Constante para el l&iacute;mite de horario para capturar neteos con cierto monto por encima 
     * o por debajo de la regla establecida.
     */
    public static final String LIM_HORARIO_NETEOS_POR_IMPORTE = "LIM_HORARIO_NETEOS_POR_IMPORTE";
    
    /**
     * La constane para el par&aacute;metro del porcentaje de modificaci&oacute;n del monto
     * de un detalle de deal.
     */
    public static final String PORCENTAJE_MOD_MONTO = "PORCENTAJE_MOD_MONTO";
    
    /**
     * La constante para el par&aacute;metro de los productos en restricci&oacute;n de compra/venta.
     */
    public static final String PRODUCTOS_EN_RESTRICCION = "PRODUCTOS_EN_RESTRICCION";
    
    /**
     * La constante para el par&aacute;metro de los productos que identifican a un cliente.
     */
    public static final String PRODUCTOS_CLIENTE = "PRODUCTOS_CLIENTE";
    
    /**
     * La constante para el par&aacute;metro que identifica el monto m&iacute;nimo de
     * operaci&oacute;n a buscar en el reporte Trimestral por Cliente.
     */
    public static final String MTO_REP_TRIM_CLI = "MTO_REP_TRIM_CLI";
    
    /**
     * La constante para el par&aacute;metro que identifica el monto m&iacute;nimo de
     * operaci&oacute;n a buscar en el reporte Trimestral por Usuario.
     */
    public static final String MTO_REP_TRIM_USR = "MTO_REP_TRIM_USR";
    
    /**
     * Par&aacute;metro para definir el máximo de días permitidos para consultar un rango de fechas
     */
    public static final String MAX_DIAS_RANGO_FECHAS = "MAX_DIAS_RANGO_FECHAS";
    
    /**
     * Par&aacute;metro que define el origen de la variaci&oacute;n para la generaci&oacute;n del pizarr&oacute;n
     * Actualmente se esperan dos origenes
     * <li>Archivo Excel de REUTERS</li>
     * <li>Reuters Market Data System</li>
     */
    public static final String FDD_ORIGEN_VARIACION = "FDD_ORIGEN_VARIACION";
    
    // -- Parametros para operaciones de Tipo de cambio especial
    
    /**
     * Monto maximo para promotores en operaciones de Tipo de cambio especial
     */
    public static final String TCE_MONTO_MAXIMO_PROMOTORES = "TCE_MONTO_MAXIMO_PROMOTORES";
    
    /**
     *  Porcentaje de variacion maximo respecto al TC pizarron, al cual queda sujeto el TCM en operaciones de Tipo de cambio especial
     */
    public static final String TCE_PORCENT_VAR_TCM_VS_TCR = "TCE_PORCENT_VAR_TCM_VS_TCR";
    
    // -- TERMINA - Parametros para operaciones de Tipo de cambio especial

    /**
     * Parámetro para Servicios que participan en Creación de Plantillas Cuenta IXE
     * BUP: Accede a servicios de la BUP
     * ALTAMIRA: Accede a Servicios Altamira
     * BUP_ALTAMIRA: Accede a Servicios Altamira IXE o BUP dependiendo de si una cuenta está o no migrada.
     */
    public static final String MODO_BUP_ALTAMIRA = "MODO_BUP_ALTAMIRA";
    
    /** Url del servicio de Consulta de Informacion de cuenta en el ESB */
    public static final String URL_ESB_CONSULTA_INFO_CTA = "URL_ESB_CONSULTA_INFO_CTA";
    
    /** Url del servicio de Consulta Id Ixe-Altamira en el ESB */
    public static final String URL_ESB_ID_IXE_ALTAMIRA = "URL_ESB_ID_IXE_ALTAMIRA";
    
    /** Url del servicio de Consulta de Datos Basicos de Cliente en el ESB */
    public static final String URL_ESB_DATOS_BASICOS_CLIENTE = "URL_ESB_DATOS_BASICOS_CLIENTE";
    
    /** El porcentaje de variacion permitido para el Precio de Referencia en  la Administracion Manual del Pizarron. */
    public static final String PORC_VAR_PR	= "PORC_VAR_PR";
    
    /** Direccion IP de repositorio FTP de Murex */
    public static final String FTP_MUREX_IP = "FTP_MUREX_IP";
    
    /** Ruta del repositorio FTP de Murex */
    public static final String FTP_MUREX_RUTA = "FTP_MUREX_RUTA";	

    /** Direccion IP de repositorio FTP de ARMS */
    public static final String FTP_ARMS_IP = "FTP_ARMS_IP";
    
    /** Ruta del repositorio FTP de ARMS */
    public static final String FTP_ARMS_RUTA = "FTP_ARMS_RUTA";	
    
    /** Usuario para FTP de ARMS */
    public static final String FTP_ARMS_USUARIO = "FTP_ARMS_USUARIO";
    
    /** Contraseña FTP de ARMS */
    public static final String FTP_ARMS_CONTRASENA = "FTP_ARMS_CONTRASENA";
    
    /** Ruta donde se guardan los Layout de clientes */
    public static final String ARMS_RUTA_LAYOUT_CLIENTES = "ARMS_RUTA_LAYOUT_CLIENTES";
    
    /** Ruta donde se guardan los Layout de contratos */
    public static final String ARMS_RUTA_LAYOUT_CONTRATOS = "ARMS_RUTA_LAYOUT_CONTRATOS";

    /** Ruta donde se guardan los Layout de transacciones */
    public static final String ARMS_RUTA_LAYOUT_TRANSACCIONES = "ARMS_RUTA_LAYOUT_TRANSACCIONES";

    /** CR generico  */
    public static final String CR_GENERICO = "CR_GENERICO";
    
    /**
     * Parametro para consultar el directorio de backup de reportes regulatorios
     */
    public static final String RUTA_REPORTES_REGULATORIOS = "RUTA_REPORTES_REGULATORIOS";
    
    /**
     * Parámetro para consultar si la captura del apoderado legal es o no obligatoria
     */
    public static final String CAPTURA_OBLIGATORIA_APODERADO = "CAPTURA_OBLIGATORIA_APODERADO";
    
    /**
     * Parametro para consultar la lista de emails a quienes se les va a notificar cuando se capture o cancele un 
     * detalle de deal que tenga monto dolarizado mayor que o igual a 1 millon de dolares americanos y la contraparte (cliente)
     * no tiene la información regulatoria requerida por BANXICO.
     */
    public static final String EMAILS_REPORTES_REGULATORIOS = "EMAILS_REPORTES_REGULATORIOS";
    
    /**
     * Lista de emails a quienes les va a llegar la notificación de inicio y fin del proceso que generará 
     * los reportes de regulatorios de backup del día en curso 
     */
    public static final String EMAILS_BACKUP_REGULATORIOS = "EMAILS_BACKUP_REGULATORIOS";
    
    /**
     * Parametro que almacena el numero de excesos permitidos para las lineas de credito
     */
    public static final String LC_CONSTANTE_EXCESOS = "LC_CONSTANTE_EXCESOS";
    
    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -4704940454825303910L;

	public static final String BROKER_DEAL_REINICIO_POSICION = "BROKER_REINICIO_POSICION";

	public static final String FED = "LC_CONSTANTE_FEP";
}
/*
 * $Id: SICA.sql,v 1.11 2008/02/22 18:25:12 ccovian Exp $
 * Autor: LegoSoft.
 * Fecha: 09/29/2005
 *
 * Scripts para la creacion de la Base de Datos del sistema
 * de cambios SICA.
 *
 */

/*
   Tablas de catalogos
 **/
/* Canales */
@SC_CANAL;
/* Brokers */
@SC_BROKER;
/* Divisas */
@SC_DIVISA;
/* Estados del sistema */
@SC_ESTADO;
/* Mesas de cambio (portafolios) */
@SC_MESA_CAMBIO;
/* Paramteros generales del SICA */
@SC_PARAMETRO;
/* Catalogo de Banxico */
@SC_CAT_BANXICO;

/* Tablas de definiciones del Pizarron */
@SC_FACTOR_DIVISA;
@SC_FACTOR_DIVISA_ACTUAL;
@SC_H_FACTOR_DIVISA;
@SC_H_PRECIO_REFERENCIA;
@SC_H_SPREAD;
@SC_H_TIPO_CAMBIO;
@SC_H_VAR;
@SC_PRECIO_REFERENCIA;
@SC_PRECIO_REFERENCIA_ACTUAL;
@SC_SPREAD;
@SC_SPREAD_ACTUAL;

/* Tablas para la definicion de Deals */
@SC_DEAL_POSICION;
@SC_H_DEAL_POSICION;
@SC_DEAL;
@SC_H_DEAL;
@SC_DEAL_DETALLE;
@SC_H_DEAL_DETALLE;
@SC_DEAL_LOG;
@SC_H_DEAL_LOG;
@SC_DEAL_STATUS_LOG;
@SC_H_DEAL_STATUS_LOG;
@SC_DEAL_DETALLE_STATUS_LOG;
@SC_H_DEAL_DETALLE_STATUS_LOG;
@SC_MAXIMO_DEAL;

/* Traspasos entre productos */
@SC_TIPO_TRASPASO_PRODUCTO;
@SC_TRASPASO_PRODUCTO;
@SC_H_TRASPASO_PRODUCTO;

/* Tablas de la herencia para la definicion de las Plantillas */
@SC_PLANTILLA;
@SC_PLANTILLA_NACIONAL;
@SC_PLANTILLA_TRAN_NACIONAL;
@SC_PLANTILLA_INTL;
@SC_PLANTILLA_CUENTA_IXE;
@SC_PLANTILLA_PANTALLA;

/* Tablas para la definicion de la Posicion */
@SC_POSICION;
@SC_H_POSICION;
@SC_POSICION_DETALLE;
@SC_H_POSICION_DETALLE;
@SC_POSICION_LOG;
@SC_H_POSICION_LOG;

/* Tablas de limites de Riesgo y Lineas de Credito y Operacion */
@SC_LIMITE;
@SC_LINEA_CREDITO;
@SC_LINEA_CREDITO_LOG;
@SC_LINEA_OPERACION;
@SC_LINEA_OPERACION_LOG;
@SC_TIPO_LIMITE;
@SC_TIPO_LINEA_CREDITO;

/* Tabla para las bitacoras de envio de Banxico */
@SC_BITACORA_ENVIADAS;
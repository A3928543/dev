/*
 * $Id: SICA_SEQUENCES.sql,v 1.14 2009/08/03 22:38:56 ccovian Exp $
 * Autor: Jean C. Favila, Javier Cordova.
 * Fecha: 09/27/2005
 *
 * Scripts para la creacion de secuencias la Base de Datos del sistema
 * de cambios SICA.
 *
 */

/* ---------------SC_ACTIVIDAD_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_ACTIVIDAD_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_ACTIVIDAD_SEQ;

CREATE PUBLIC SYNONYM SC_ACTIVIDAD_SEQ FOR SICA_ADMIN.SC_ACTIVIDAD_SEQ;

/* ---------------SC_BITACORA_ENVIADAS_SEQ------------------- */
DROP SEQUENCE derivadesa1.SC_BITACORA_ENVIADAS_SEQ;

CREATE SEQUENCE derivadesa1.SC_BITACORA_ENVIADAS_SEQ;

CREATE PUBLIC SYNONYM SC_BITACORA_ENVIADAS_SEQ FOR derivadesa1.SC_BITACORA_ENVIADAS_SEQ;

/* ---------------SC_DEAL_DETALLE_STATUS_LOG_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_DEAL_DETALLE_STATUS_LOG_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_DEAL_DETALLE_STATUS_LOG_SEQ;

CREATE PUBLIC SYNONYM SC_DEAL_DETALLE_STATUS_LOG_SEQ FOR SICA_ADMIN.SC_DEAL_DETALLE_STATUS_LOG_SEQ;

/* ---------------SC_DEAL_LOG_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_DEAL_LOG_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_DEAL_LOG_SEQ;

CREATE PUBLIC SYNONYM SC_DEAL_LOG_SEQ FOR SICA_ADMIN.SC_DEAL_LOG_SEQ;

/* ---------------SC_DEAL_POSICION_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_DEAL_POSICION_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_DEAL_POSICION_SEQ;

CREATE PUBLIC SYNONYM SC_DEAL_POSICION_SEQ FOR SICA_ADMIN.SC_DEAL_POSICION_SEQ;

/* ---------------SC_DEAL_STATUS_LOG_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_DEAL_STATUS_LOG_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_DEAL_STATUS_LOG_SEQ;

CREATE PUBLIC SYNONYM SC_DEAL_STATUS_LOG_SEQ FOR SICA_ADMIN.SC_DEAL_STATUS_LOG_SEQ;

/* ---------------SC_DEAL_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_DEAL_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_DEAL_SEQ;

CREATE PUBLIC SYNONYM SC_DEAL_SEQ FOR SICA_ADMIN.SC_DEAL_SEQ;

/* ---------------SC_FACTOR_DIVISA_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_FACTOR_DIVISA_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_FACTOR_DIVISA_SEQ;

CREATE PUBLIC SYNONYM SC_FACTOR_DIVISA_SEQ FOR SICA_ADMIN.SC_FACTOR_DIVISA_SEQ;

/* ---------------SC_FOLIO_SWAP_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_FOLIO_SWAP_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_FOLIO_SWAP_SEQ;

CREATE PUBLIC SYNONYM SC_FOLIO_SWAP_SEQ FOR SICA_ADMIN.SC_FOLIO_SWAP_SEQ;

/* ---------------SC_H_TIPO_CAMBIO_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_H_TIPO_CAMBIO_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_H_TIPO_CAMBIO_SEQ;

CREATE PUBLIC SYNONYM SC_H_TIPO_CAMBIO_SEQ FOR SICA_ADMIN.SC_H_TIPO_CAMBIO_SEQ;

/* ---------------SC_H_VAR_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_H_VAR_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_H_VAR_SEQ;

CREATE PUBLIC SYNONYM SC_H_VAR_SEQ FOR SICA_ADMIN.SC_H_VAR_SEQ;

/* ---------------SC_LIMITE_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_LIMITE_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_LIMITE_SEQ;

CREATE PUBLIC SYNONYM SC_LIMITE_SEQ FOR SICA_ADMIN.SC_LIMITE_SEQ;

/* ---------------SC_LINEA_CREDITO_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_LINEA_CREDITO_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_LINEA_CREDITO_SEQ;

CREATE PUBLIC SYNONYM SC_LINEA_CREDITO_SEQ FOR SICA_ADMIN.SC_LINEA_CREDITO_SEQ;

/* ---------------SC_LINEA_CREDITO_LOG_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_LINEA_CREDITO_LOG_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_LINEA_CREDITO_LOG_SEQ;

CREATE PUBLIC SYNONYM SC_LINEA_CREDITO_LOG_SEQ FOR SICA_ADMIN.SC_LINEA_CREDITO_LOG_SEQ;

/* ---------------SC_LINEA_OPERACION_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_LINEA_OPERACION_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_LINEA_OPERACION_SEQ;

CREATE PUBLIC SYNONYM SC_LINEA_OPERACION_SEQ FOR SICA_ADMIN.SC_LINEA_OPERACION_SEQ;

/* ---------------SC_LINEA_OPERACION_LOG_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_LINEA_OPERACION_LOG_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_LINEA_OPERACION_LOG_SEQ;

CREATE PUBLIC SYNONYM SC_LINEA_OPERACION_LOG_SEQ FOR SICA_ADMIN.SC_LINEA_OPERACION_LOG_SEQ;

/* ---------------SC_MESA_CAMBIO_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_MESA_CAMBIO_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_MESA_CAMBIO_SEQ;

CREATE PUBLIC SYNONYM SC_MESA_CAMBIO_SEQ FOR SICA_ADMIN.SC_MESA_CAMBIO_SEQ;

/* ---------------SC_PLANTILLA_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_PLANTILLA_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_PLANTILLA_SEQ;

CREATE PUBLIC SYNONYM SC_PLANTILLA_SEQ FOR SICA_ADMIN.SC_PLANTILLA_SEQ;

/* ---------------SC_POSICION_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_POSICION_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_POSICION_SEQ;

CREATE PUBLIC SYNONYM SC_POSICION_SEQ FOR SICA_ADMIN.SC_POSICION_SEQ;

/* ---------------SC_POSICION_DETALLE_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_POSICION_DETALLE_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_POSICION_DETALLE_SEQ;

CREATE PUBLIC SYNONYM SC_POSICION_DETALLE_SEQ FOR SICA_ADMIN.SC_POSICION_DETALLE_SEQ;

/* ---------------SC_POSICION_LOG_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_POSICION_LOG_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_POSICION_LOG_SEQ;

CREATE PUBLIC SYNONYM SC_POSICION_LOG_SEQ FOR SICA_ADMIN.SC_POSICION_LOG_SEQ;

/* ---------------SC_PRECIO_REFERENCIA_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_PRECIO_REFERENCIA_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_PRECIO_REFERENCIA_SEQ;

CREATE PUBLIC SYNONYM SC_PRECIO_REFERENCIA_SEQ FOR SICA_ADMIN.SC_PRECIO_REFERENCIA_SEQ;

/* ---------------SC_RENGLON_PIZARRON_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_RENGLON_PIZARRON_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_RENGLON_PIZARRON_SEQ;

CREATE PUBLIC SYNONYM SC_RENGLON_PIZARRON_SEQ FOR SICA_ADMIN.SC_RENGLON_PIZARRON_SEQ;

/* ---------------SC_SPREAD_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_SPREAD_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_SPREAD_SEQ;

CREATE PUBLIC SYNONYM SC_SPREAD_SEQ FOR SICA_ADMIN.SC_SPREAD_SEQ;

/* --------------SC_TIPO_LIMITE_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_TIPO_LIMITE_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_TIPO_LIMITE_SEQ;

CREATE PUBLIC SYNONYM SC_TIPO_LIMITE_SEQ FOR SICA_ADMIN.SC_TIPO_LIMITE_SEQ;

/* --------------SC_TRASPASO_MESA_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_TRASPASO_MESA_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_TRASPASO_MESA_SEQ;

CREATE PUBLIC SYNONYM SC_TRASPASO_MESA_SEQ FOR SICA_ADMIN.SC_TRASPASO_MESA_SEQ;

/* ---------------SC_RECO_UTILIDAD_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_RECO_UTILIDAD_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_RECO_UTILIDAD_SEQ;

CREATE PUBLIC SYNONYM SC_RECO_UTILIDAD_SEQ FOR SICA_ADMIN.SC_RECO_UTILIDAD_SEQ;

/* ---------------SC_MOVIMIENTO_CONTABLE_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_MOVIMIENTO_CONTABLE_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_MOVIMIENTO_CONTABLE_SEQ;

CREATE PUBLIC SYNONYM SC_MOVIMIENTO_CONTABLE_SEQ FOR SICA_ADMIN.SC_MOVIMIENTO_CONTABLE_SEQ;

/* ---------------SC_MOVIMIENTO_CONT_DETALLE_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_MOVIMIENTO_CONT_DETALLE_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_MOVIMIENTO_CONT_DETALLE_SEQ;

CREATE PUBLIC SYNONYM SC_MOVIMIENTO_CONT_DETALLE_SEQ FOR SICA_ADMIN.SC_MOVIMIENTO_CONT_DETALLE_SEQ;

/* ---------------SC_GUIA_CONTABLE_LIQ_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_GUIA_CONTABLE_LIQ_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_GUIA_CONTABLE_LIQ_SEQ;

CREATE PUBLIC SYNONYM SC_GUIA_CONTABLE_LIQ_SEQ FOR SICA_ADMIN.SC_GUIA_CONTABLE_LIQ_SEQ;

/* ---------------SC_GUIA_CONT_ASIENTO_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_GUIA_CONT_ASIENTO_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_GUIA_CONT_ASIENTO_SEQ;

CREATE PUBLIC SYNONYM SC_GUIA_CONT_ASIENTO_SEQ FOR SICA_ADMIN.SC_GUIA_CONT_ASIENTO_SEQ;

/* ---------------SC_POLIZA_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_POLIZA_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_POLIZA_SEQ;

CREATE PUBLIC SYNONYM SC_POLIZA_SEQ FOR SICA_ADMIN.SC_POLIZA_SEQ;

/* ---------------SC_REVERSO_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_REVERSO_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_REVERSO_SEQ;

CREATE PUBLIC SYNONYM SC_REVERSO_SEQ FOR SICA_ADMIN.SC_REVERSO_SEQ;

/* ---------------SC_MONTO_MAXIMO_SUC_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_MONTO_MAXIMO_SUC_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_MONTO_MAXIMO_SUC_SEQ;

CREATE PUBLIC SYNONYM SC_MONTO_MAXIMO_SUC_SEQ FOR SICA_ADMIN.SC_MONTO_MAXIMO_SUC_SEQ;

/* ---------------SC_REVERSO_DETALLE_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_REVERSO_DETALLE_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_REVERSO_DETALLE_SEQ;

CREATE PUBLIC SYNONYM SC_REVERSO_DETALLE_SEQ FOR SICA_ADMIN.SC_REVERSO_DETALLE_SEQ;

/* ---------------SC_TIPO_PIZARRON_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_TIPO_PIZARRON_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_TIPO_PIZARRON_SEQ;

CREATE PUBLIC SYNONYM SC_TIPO_PIZARRON_SEQ FOR SICA_ADMIN.SC_TIPO_PIZARRON_SEQ;

/* ---------------SC_TC_MIN_MAX_TELLER------------------- */
DROP SEQUENCE SICA_ADMIN.SC_TC_MIN_MAX_TELLER_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_TC_MIN_MAX_TELLER_SEQ;

CREATE PUBLIC SYNONYM SC_TC_MIN_MAX_TELLER_SEQ FOR SICA_ADMIN.SC_TC_MIN_MAX_TELLER_SEQ;

/* ---------------SC_LINEA_CAMBIO_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_LINEA_CAMBIO_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_LINEA_CAMBIO_SEQ;

CREATE PUBLIC SYNONYM SC_LINEA_CAMBIO_SEQ FOR SICA_ADMIN.SC_LINEA_CAMBIO_SEQ;

/* ---------------SC_LINEA_CAMBIO_LOG_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_LINEA_CAMBIO_LOG_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_LINEA_CAMBIO_LOG_SEQ;

CREATE PUBLIC SYNONYM SC_LINEA_CAMBIO_LOG_SEQ FOR SICA_ADMIN.SC_LINEA_CAMBIO_LOG_SEQ;

CREATE PUBLIC SYNONYM SC_FOLIO_SWAP_SEQ FOR TASBANCO.SC_FOLIO_SWAP_SEQ;
CREATE PUBLIC SYNONYM SC_BITACORA_ENVIADAS FOR TASBANCO.SC_BITACORA_ENVIADAS;
CREATE PUBLIC SYNONYM SC_BITACORA_ENVIADAS_SEQ FOR TASBANCO.SC_BITACORA_ENVIADAS_SEQ;

/* ---------------SC_AUT_MEDIO_CONTACTO_SEQ------------------- */
DROP SEQUENCE SICA_ADMIN.SC_AUT_MEDIO_CONTACTO_SEQ;

CREATE SEQUENCE SICA_ADMIN.SC_AUT_MEDIO_CONTACTO_SEQ;

CREATE PUBLIC SYNONYM SC_AUT_MEDIO_CONTACTO_SEQ FOR SICA_ADMIN.SC_AUT_MEDIO_CONTACTO_SEQ;
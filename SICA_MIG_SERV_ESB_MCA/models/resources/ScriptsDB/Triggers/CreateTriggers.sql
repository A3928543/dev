/*
 * $Id: CreateTriggers.sql,v 1.11 2008/02/22 18:25:46 ccovian Exp $
 * Autor: LegoSoft.
 * Fecha: 09/29/2005
 *
 * Scripts para la creacion de los triggers que requiere el
 * SICA para su operacion.
 *
 */


/*
 * Definicion de los triggers que son requeridos para la actualizacion
 * de la posicion. Es decir, cada ves que se inserta un registro en
 * SC_POSICION_LOG se genera la acutlizacion correspondiente en 
 * SC_POSICION_DETALLE y SC_POSICION
 **/
@TRIGGER_ActualizaPosicion;

/*
 * Definicion del trigger para cuando se actualiza el precio de referencia
 * en SICA_VARIACION genera la actualizacion en SC_RECIO_REFERENCIA
 * SC_PRECIO_REFERENCIA_ACTUAL y la notificacion al pizzaron
 **/
@TRIGGER_ActualizaprecioRef;

@TRIGGER_ActFactorDivisaActual;

@TRIGGER_ActPrecioReferenciaActual;

/*
 * Definicion del trigger cuando se inserta un traspaso entre productos de la
 * misma mesa que se utiliza para los servicios del SITE.
 **/
@TRIGGER_TraspasoProducto;


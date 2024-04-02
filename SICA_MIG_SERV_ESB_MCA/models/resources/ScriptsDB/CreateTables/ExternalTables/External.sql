/*
 * $Id: External.sql,v 1.11 2008/02/22 18:25:43 ccovian Exp $
 * Autor: LegoSoft.
 * Fecha: 09/29/2005
 *
 * Scripts para la creacion de tablas que utiliza el SICA y
 * que NO son propias del sistema.
 * Puede ser que dichas tablas NO requieran de ser creadas
 * debido a que se encuentran ya en los scripts de creacion
 * de otros sistemas.
 *
 */


/*
 * Definicion de canales
 **/
@BUP_CANAL;
 
/*
 * Contratos del SICA. Esta tabla SI es necesaria y es donde
 * se registran los contratos del SICA siendo un sub-clase de
 * BUB_CUENTA_CONTRATO.
 * Se requiere migrar dicha informacion del sistema anterior.
 **/
@BUP_CONTRATO_SICA;
 
 /* 
  * Variacion que se tiene del en los precios de referencia
  * proveniente del sistema de informacion de Reuters.
**/
@SICA_VARIACION;

/*
 * Tablas de seguridad y autorizaciones
 *
**/
@SEGU_INTENTO_FALLIDO;

/*
 * Vista que ya puede haber sido creada en Ixe.
**/
@SEGU_USUARIO_VERSION;
/

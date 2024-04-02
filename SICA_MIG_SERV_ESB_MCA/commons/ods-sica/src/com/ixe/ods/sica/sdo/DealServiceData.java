/*
 * $Id: DealServiceData.java,v 1.3 2008/02/22 18:25:15 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo;

import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import java.util.Date;
import java.util.List;

/**
 * Interfaz de servicios de consulta y actualizaci&oacute;n para la clase persistente Deal.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.3 $ $Date: 2008/02/22 18:25:15 $
 */
public interface DealServiceData {

    /**
     * Regresa una lista de los deals que no tienen contrato sica, que se encuentran cancelados.
     *
     * @param fechaInicio El valor inferior del rango para la fecha de captura del deal.
     * @param fechaFin    El valor superior del rango para la fecha de captura del deal.
     * @param idCanal     La clave de canal (Puede ser null para consultar todos los canales).
     * @param idPromotor  El n&uacute;mero de persona del promotor (puede ser null).
     * @param cancelados  True para cancelados, False para sin cancelar.
     * @return List.
     * @throws com.ixe.ods.sica.SicaException Si no se encuentran deals con los criterios
     *  especificados.
     */
    List findDealsCapturaRapida(final Date fechaInicio, final Date fechaFin,
                                final String idCanal, final Integer idPromotor,
                                final boolean cancelados) throws SicaException;

    /**
     * Regresa la direcci&oacute;n de mensajer&iacute;a ligada al deal especificado,
     * inicializ&aacute;ndola en caso de ser necesario.
     *
     * @param deal El deal a inspeccionar.
     * @return Direccion.
     */
    Direccion findDireccionMensajeriaDeal(Deal deal);
}

/*
 * $Id: DesviacionInterbancaria.java,v 1.2 2010/04/13 17:55:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import java.util.Map;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Divisa;

/**
 * Interfaz de un bean que permite realizar validaciones de desviaci&oacute;n al tipo de cambio
 * para operaciones interbancarias.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2010/04/13 17:55:36 $
 */
public interface DesviacionInterbancaria {

    /**
     * Levanta una excepci&oacute;n si el tipo de cambio se encuentra desviado m&aacute;s de un 5%
     * con respecto al precio de referencia de la divisa. Regresa true si el porcentaje de
     * desviaci&oacute;n es de 1%.
     *
     * @param recibimos True para compra, False para venta.
     * @param div La divisa de la operaci&oacute;n.
     * @param fechaValor CASH, TOM, SPOT, 72HR o VFUT.
     * @param claveFormaLiquidacion La clave del instrumento o producto.
     * @param idDivisaReferencia La clave de divisa de la mesa de cambios en la que se opera.
     * @param tipoCambio El tipo de cambio a validar.
     * @return boolean.
     * @throws com.ixe.ods.sica.SicaException Si el porcentaje de desviaci&oacute;n del t.c. es
     * mayor al 5%.
     */
    Map validarDesviacionTc(boolean recibimos, Divisa div, String fechaValor,
                            String claveFormaLiquidacion, String idDivisaReferencia,
                            double tipoCambio) throws SicaException;
}

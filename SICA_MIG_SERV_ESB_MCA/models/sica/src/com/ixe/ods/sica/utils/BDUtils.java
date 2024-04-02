/*
 * $Id: BDUtils.java,v 1.3 2008/12/26 23:17:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.utils;

import java.math.BigDecimal;

/**
 * Utiler&iacute;a para generar BigDecimals.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.3 $ $Date: 2008/12/26 23:17:36 $
 */
public class BDUtils {

    /**
     * Genera un BigDecimal con escala 2, Rounding mode Half Even.
     *
     * @param valor El valor a generar.
     * @return BigDecimal.
     */
    public static BigDecimal generar2(double valor) {
        return generar(valor, 2);
    }

    /**
     * Genera un BigDecimal con escala 2, Rounding mode Half Even.
     *
     * @param valor El valor a generar.
     * @return BigDecimal.
     */
    public static BigDecimal generar6(double valor) {
        return generar(valor, 6);
    }

     /**
      * Genera un BigDecimal con un valor especificado, escala y con Rounding mode Half Even.
      *
      * @param valor El valor a generar.
      * @param escala El valor de la escala.
      * @return BigDecimal.
      */
    private static BigDecimal generar(double valor, int escala) {
        BigDecimal resultado = new BigDecimal("" + valor);
        return resultado.setScale(escala, BigDecimal.ROUND_HALF_EVEN);
    }
}

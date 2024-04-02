/*
 * $Id: Redondeador.java,v 1.11 2008/02/22 18:25:31 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica;

/**
 * Clase que provee la funcionalidad de hacer redondeos a 2, 4 y 6 cifras decimales.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:31 $
 */
public class Redondeador {

    /**
     * Regresa la cantidad redondeada a 2 cifras decimales.
     *
     * @param cantidad La cantidad a redondear.
     * @return double.
     */
    public static double redondear2Dec(double cantidad) {
        double res = cantidad > 0 ? cantidad + 0.005 : cantidad - 0.005;
        return ((long) (res * 100.0)) / 100.0;
    }

    /**
     * Regresa la cantidad redondeada a 4 cifras decimales.
     *
     * @param cantidad La cantidad a redondear.
     * @return double.
     */
    public static double redondear4Dec(double cantidad) {
        double res = cantidad > 0 ? cantidad + 0.00005 : cantidad - 0.00005;
        return ((long) (res * 10000.0)) / 10000.0;
    }

    /**
     * Regresa la cantidad redondeada a 6 cifras decimales.
     *
     * @param cantidad La cantidad a redondear.
     * @return double.
     */
    public static double redondear6Dec(double cantidad) {
        double res = cantidad > 0 ? cantidad + 0.0000005 : cantidad - 0.0000005;
        return ((long) (res * 1000000.0)) / 1000000.0;
    }
}

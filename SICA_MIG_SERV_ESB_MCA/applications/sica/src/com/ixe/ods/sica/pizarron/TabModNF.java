/*
 * $Id: TabModNF.java,v 1.14.22.2 2012/03/13 02:44:06 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pizarron;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Table model para el applet de pizarras, para las divisas no frecuentes y metales amonedados.
 *
 * @author Jean C. Favila.
 * @version  $Revision: 1.14.22.2 $ $Date: 2012/03/13 02:44:06 $
 */
public class TabModNF extends com.ixe.ods.sica.pizarron.AbstractTabMod {

    /**
     * Constructor por default. Inicializa todas las variables de instancia.
     *
     * @param rows La lista de renglones de la pizarra.
     */
    public TabModNF(List rows) {
        super(rows, null);
    }

    /**
     * Regresa el t&iacute;tulo adecuado para la columna c.
     *
     * @param c El n&uacute;mero de la columna.
     * @return String.
     */
    public String getColumnName(int c) {
        switch (c) {
            case 0:
                return "Divisa";
            case 1:
                return "Descripci\u00f3n";
            case 2:
                return "Compra";
            default :
                return "Venta";
        }
    }

    /**
     * Regresa 4.
     *
     * @return int.
     */
    public int getColumnCount() {
        return 4;
    }

    /**
     * Regresa el valor para el renglon row y la columna col.
     * Se realiza la actualizacion para incrementar la presicion a 6 decimales
     *
     * @param row El n&uacute;mero del rengl&oacute;n.
     * @param col El n&uacute;mero de la columna.
     * @return Object.
     */
    public Object getValueAt(int row, int col) {
        Map r = (Map) getRows().get(row);
        
        // cambio para incrementar presicion en no frecuentes
        DecimalFormat df = new DecimalFormat("0.00000000");
        switch (col) {
            case 0:
                return r.get("id");
            case 1:
                return r.get("descripcion");
            case 2:
                return df.format(r.get("compra"));
            case 3:
                return df.format(r.get("venta"));
            default:
                return df.format(r.get(Consts.ID_FACTOR_DIVISA_KEY));
        }
    }

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 1745133912481838089L;
}

/*
 * $Id: TabMod.java,v 1.17.22.1 2011/04/26 02:52:10 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pizarron;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Table model para el applet de pizarras, para las divisas frecuentes.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.17.22.1 $ $Date: 2011/04/26 02:52:10 $
 */
public class TabMod extends com.ixe.ods.sica.pizarron.AbstractTabMod {

    /**
     * Constructor por default. Inicializa todas las variables de instancia.
     *
     * @param idDiv El identificador de la divisa.
     * @param rows La lista de renglones de la pizarra.
     * @param restrs La lista de restricciones.
     * @param soloCash Para el caso de sucursales.
     * @param vFut Si muestra o no las columnas para valor Futuro.
     */
    public TabMod(String idDiv, List rows, List restrs, boolean soloCash, boolean vFut) {
        super(rows, restrs);
        _idDiv = idDiv;
        _soloCash = soloCash;
        _vFut = vFut;
    }

    /**
     * Regresa el t&iacute;tulo adecuado para la columna c.
     *
     * @param c El n&uacute;mero de la columna.
     * @return String.
     */
    public String getColumnName(int c) {
        switch (c) {
            case Consts.NUM_0:
                return _idDiv;
            case Consts.NUM_1:
                if (_soloCash) {
                    return "M\u00ednimo Compra";
                }
                else {
                return "C (HOY)";
                }
            case Consts.NUM_2:
                if (_soloCash) {
                    return "M\u00e1ximo Compra";
                }
                else {                    
                return "V (HOY)";
                }
            case Consts.NUM_3:
                if (_soloCash) {
                    return "M\u00ednimo Venta";
                }
                else {
                return "C (TOM)";
                }
            case Consts.NUM_4:
                if (_soloCash) {
                    return "M\u00e1ximo Venta";
                }
                else {
                return "V (TOM)";
                }
            case Consts.NUM_5:
                return "C (SPOT)";
            case Consts.NUM_6:
                return "V (SPOT)";
            case Consts.NUM_7:
                return "C (72HR)";
            case Consts.NUM_8:
                return "V (72HR)";
            case Consts.NUM_9:
                return "C (96HR)";
            case Consts.NUM_10:
                return "V (96HR)";
            default:
                return "";
        }
    }

    /**
     * Regresa 7.
     *
     * @return int.
     */
    public int getColumnCount() {
        if (_soloCash) {
            return Consts.NUM_5;
        }
        else {
            return _vFut ? Consts.NUM_11 : Consts.NUM_9;
        }
    }

    /**
     * Regresa el identificador del spread en el rengl&oacute;n row.
     *
     * @param row El rengl&oacute;n a inspeccionar.
     * @return int.
     */
    public int getIdSpr(int row) {
        return ((Integer) ((Map) getRows().get(row)).get("idSpread")).intValue();
    }

    /**
     * Regresa el valor para el renglon row y la columna col.
     *
     * @param row El n&uacute;mero del rengl&oacute;n.
     * @param col El n&uacute;mero de la columna.
     * @return Object.
     */
    public Object getValueAt(int row, int col) {
        Map r = (Map) getRows().get(row);
        String cfl = (String) r.get("claveFormaLiquidacion");
        DecimalFormat df = new DecimalFormat("0.00000000");
        if (_soloCash) {
            if (col == Consts.NUM_1) {
                return df.format(r.get(Consts.C_CASH_KEY));
            }
            else if (col == Consts.NUM_2) {
                return df.format(r.get("cMaxCash"));
                    }
            else if (col == Consts.NUM_3) {
                return df.format(r.get("vMinCash"));
                }
            else if (col == Consts.NUM_4) {
                return df.format(r.get(Consts.V_CASH_KEY));
            }
        }
        if (col == Consts.NUM_0) {
            return r.get(CAMPOS[col]);
        }
        else if (col < Consts.NUM_7) {
                return df.format(r.get(CAMPOS[col]));
        }
        else if (col < Consts.NUM_9) {
            if (_vFut || (TRAEXT.equals(cfl) && (USD.equals(_idDiv) || EUR.equals(_idDiv)))) {
                return df.format(r.get(CAMPOS[col]));
            }
        }
        else if (col < Consts.NUM_11) {
            if (TRAEXT.equals(cfl) && (USD.equals(_idDiv) || EUR.equals(_idDiv))) {
                return df.format(r.get(CAMPOS[col]));
            }
        }
        return "";
    }

    /**
     * El identificador de la divisa.
     */
    private String _idDiv;

    /**
     * Variable que define si el pizarr&oacute;n muestra solamente los tipos de cambio para CASH.
     */
    private boolean _soloCash;

    /**
     * Variable que define si hay valor futuro.
     */
    private boolean _vFut;

    /**
     * Constante que identifica la clave de forma de liquidaci&oacute;n de transferencia extranjera.
     */
    private static final String TRAEXT = "TRAEXT";

    /**
     * Constante que identifica la clave de divisa para D&oacute;lar Americano.
     */
    private static final String USD = "USD";

    /**
     * Constante que identifica la clave de divisa para Euro.
     */
    private static final String EUR = "EUR";

    /**
     * Arreglo con los nombres de las llaves para los mapas de los renglones del pizarr&oacute;n.
     */
    private static final String[] CAMPOS = { "nombreFormaLiquidacion", "cCash", "vCash", "cTom",
            "vTom", "cSpot", "vSpot", "c72Hr", "v72Hr", "cVFut", "vVFut", "cMinCash", "vMaxCash"};

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -9015609037088023153L;
}

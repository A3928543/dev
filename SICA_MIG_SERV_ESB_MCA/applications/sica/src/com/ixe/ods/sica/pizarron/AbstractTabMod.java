/*
 * $Id: AbstractTabMod.java,v 1.13.42.1 2011/04/26 02:51:32 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.pizarron;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

/**
 * Superclase de los table model para el pizarr&oacute;n del SICA.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.13.42.1 $ $Date: 2011/04/26 02:51:32 $
 */
public abstract class AbstractTabMod extends DefaultTableModel {

    /**
     * Constructor por default. Inicializa todas las variables de instancia.
     *
     * @param rows La lista de renglones de la pizarra.
     * @param restrs La lista de restricciones.
     */
    public AbstractTabMod(List rows, List restrs) {
        super();
        this.rows = rows;
        this.restrs = restrs;
    }

    /**
     * Regresa el tama&ntilde;o de la lista rows.
     *
     * @return int.
     */
    public int getRowCount() {
        return rows != null ? rows.size() : 0;
    }

    /**
     * Regresa false, para evitar ediciones.
     *
     * @param row El n&uacute;mero del rengl&oacute;n.
     * @param col El n&uacute;mero de la columna.
     * @return boolean.
     */
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
     * Regresa la clave forma liquidaci&oacute;n del rengl&oacute;n row.
     *
     * @param row El n&uacute;mero de rengl&oacute;n.
     * @return String.
     */
    public String getIdProd(int row) {
        return (String) ((Map) rows.get(row)).get("claveFormaLiquidacion");
    }

    /**
     * Regresa el identificador del factorDivisa en el rengl&oacute;n row.
     *
     * @deprecated Se debe utilizar el valor directo del Factor Divisa.
     * @param row El rengl&oacute;n a inspeccionar.
     * @return int.
     */
    public int getIdFD(int row) {
        return ((Integer) ((Map) getRows().get(row)).get(Consts.ID_FACTOR_DIVISA_KEY)).intValue();
    }
    
    /**
     * Regresa el valor del Factor Divisa en el rengl&oacute;n row.
     *
     * @param row El rengl&oacute;n a inspeccionar.
     * @return double.
     */
    public double getFactorDivisa(int row) {
        return ((Double) ((Map) getRows().get(row)).get(Consts.FACTOR_DIVISA_KEY)).doubleValue();
    }

    /**
     * Regresa el identificador del precio de referencia del pizarr&oacute;n.
     *
     * @deprecated Se debe utilizar el valor directo del precio referencia.
     * @param row El rengl&oacute;n a inspeccionar.
     * @return int.
     */
    public int getIdPr(int row) {
        return ((Integer) ((Map) getRows().get(row)).get(Consts.ID_PR_KEY)).intValue();
    }
    
    /**
     * Regresa el precio referencia MID SPOT del pizarr&oaute;n.
     * @param row
     * @return
     */
    public double getPrMidSpot(int row) {
        return ((Double) ((Map) getRows().get(row)).get(Consts.PR_MID_SPOT_KEY)).doubleValue();
    }
    
    /**
     * Regresa el precio referencia SPOT del pizarr&oaute;n.
     * 
     * @param row El rengl&oacute;n a inspeccionar.
     * @return
     */
    public double getPrSpot(int row) {
        return ((Double) ((Map) getRows().get(row)).get(Consts.PR_SPOT_KEY)).doubleValue();
    }

    /**
     * Regresa el valor de rows.
     *
     * @return List.
     */
    protected List getRows() {
        return rows;
    }

    /**
     * Regresa el valor de restrs.
     *
     * @return List.
     */
    protected List getRestrs() {
        return restrs;
    }

    /**
     * La lista de renglones de la pizarra.
     */
    private List rows = new ArrayList();

    /**
     * La lista de restricciones.
     */
    private List restrs;
}

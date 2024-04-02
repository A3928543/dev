/*
 * $Id: FormasPagoLiqSelectionModel.java,v 1.10 2008/02/22 18:25:35 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import com.ixe.treasury.dom.common.FormaPagoLiq;
import org.apache.tapestry.form.IPropertySelectionModel;

import java.util.List;

/**
 * Implementa la interfaz IPropertySelectionModel para una lista de objetos de la clase
 * <code>com.ixe.ods.sica.components.FormasPagoLiqSelectionModel</code>. El valor a tomar es la clave del
 * mnem&oacute;nico, y la etiqueta es la descripci&oacute;n, seguida del mnem&oacute;nico.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:35 $
 * @see IPropertySelectionModel
 */
public class FormasPagoLiqSelectionModel implements IPropertySelectionModel {

    /**
     * Constructor por default. Inicializa la lista de formas pago y liquidaci&oacute;n.
     *
     * @param formasPagoLiquidacion La lista de formas pago y liquidaci&oacute;n.
     */
    public FormasPagoLiqSelectionModel(List formasPagoLiquidacion) {
        super();
        _formasPagoLiquidacion = formasPagoLiquidacion;
    }

    /**
     * Regresa el tama&ntilde;o de formasPagoLiquidacion.
     *
     * @return int.
     */
    public int getOptionCount() {
        return _formasPagoLiquidacion.size();
    }

    /**
     * Regresa la formaPagoLiq que se encuentra en el &iacute;ndice i.
     *
     * @param i El &iacute;ndice a localizar.
     * @return FormaPagoLiq.
     */
    public Object getOption(int i) {
        return _formasPagoLiquidacion.get(i);
    }

    /**
     * Regresa la etiqueta para el combo box, concatenando la descripci&oacute;n m&aacute;s el mnem&oacute;nico de la
     * formaPagoLiq que se encuentra en el &iacute;ndice i.
     *
     * @param i El &iacute;ndice a localizar.
     * @return String.
     */
    public String getLabel(int i) {
        FormaPagoLiq fpl = ((FormaPagoLiq) getOption(i));
        StringBuffer sb = new StringBuffer(fpl.getDescripcion() != null ? fpl.getDescripcion() : "");
        sb.append(".- ").append(fpl.getMnemonico());
        return sb.toString();
    }

    /**
     * Regresa el mnem&oacute;nico de la formaPagoLiq que se encuentra en el &iacute;ndice i.
     *
     * @param i El &iacute;ndice a localizar.
     * @return String.
     */
    public String getValue(int i) {
        return ((FormaPagoLiq) getOption(i)).getMnemonico();
    }

    /**
     * Regresa el mnem&oacute;nico que llega como par&aacute;metro.
     *
     * @param s El mnem&oacute;nico.
     * @return String
     */
    public Object translateValue(String s) {
        return s;
    }

    /**
     * La lista de objetos FormasPagoLiq.
     */
    private List _formasPagoLiquidacion;
}

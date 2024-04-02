/*
 * $Id: StatusSelectionModel.java,v 1.13.44.1 2012/08/25 23:45:57 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import com.ixe.ods.sica.model.Deal;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * Clase para crear un PropertySelection con los distintos status del deal normal e interbancario.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.13.44.1 $ $Date: 2012/08/25 23:45:57 $
 */
public class StatusSelectionModel implements IPropertySelectionModel {

    /**
     * Constructor por default.
     *
     * @param incluirCC Si se desea incluir el status CC o no.
     */
    public StatusSelectionModel(boolean incluirCC) {
        super();
        _claves = new String[]{"", Deal.STATUS_DEAL_PROCESO_CAPTURA, Deal.STATUS_DEAL_CAPTURADO,
                Deal.STATUS_DEAL_PARCIAL_PAGADO_LIQ, Deal.STATUS_DEAL_TOTALMENTE_PAGADO,
                Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO, Deal.STATUS_DEAL_CANCELADO,
                "PR", "RE", Deal.STATUS_ESPECIAL_TC_AUTORIZADO};
        _descripciones = new String[]{"TODOS", "Alta", "Proces\u00e1ndose", "Liq. Parcial",
                "Totalmente Pag.", "Totalmente Liq.", "Cancelado", "Proc. Reverso", "Reversado", "TC Especial"};
    }

    /**
     * Regresa la longitud del arreglo de claves.
     *
     * @return int.
     */
    public int getOptionCount() {
        return _claves.length;
    }

    /**
     * Regresa la clave en el &iacute;ndice i.
     *
     * @param i El &iacute;ndice a revisar.
     * @return Object.
     */
    public Object getOption(int i) {
        return _claves[i];
    }

    /**
     * Regresa la descripci&oacute;n en el &iacute;ndice i.
     *
     * @param i El &iacute;ndice a revisar.
     * @return String.
     */
    public String getLabel(int i) {
        return _descripciones[i];
    }

    /**
     * Regresa la clave en el &iacute;ndice i.
     *
     * @param i El &iacute;ndice a revisar.
     * @return Object.
     */
    public String getValue(int i) {
        return _claves[i];
    }

    /**
     * Regresa el mismo par&aacute;metro.
     *
     * @param string El valor.
     * @return Object.
     */
    public Object translateValue(String string) {
        return string;
    }

    /**
     * El arreglo de claves para los status del deal.
     */
    private String[] _claves;

    /**
     * El arreglo de descripciones para los status del deal.
     */
    private String[] _descripciones;
}

/*
 * $Id: TipoIvaSelectionModel.java,v 1.9 2008/02/22 18:25:35 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.components;

import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.ixe.ods.bup.model.TipoIva;

/**
 * Implementa la interfaz IPropertySelectionModel para una lista de objetos de la clase
 * <code>com.ixe.ods.sica.components.TipoIvaSelectionModel</code>. El valor a tomar es la clave del
 * tipo de Iva, y la etiqueta es la descripci&oacute;n, seguida del tipo de Iva.
 *
 * @author Edgar I. Leija
 * @version  $Revision: 1.9 $ $Date: 2008/02/22 18:25:35 $
 * @see IPropertySelectionModel
 */
public class TipoIvaSelectionModel implements IPropertySelectionModel{
	
	/**
     * Constructor por default. Inicializa la lista de tipos de IVA.
     *
     * @param tipoIva La lista de tipos de IVA.
     */
    public TipoIvaSelectionModel(List tipoIva) {
        super();
        _tipoIva = tipoIva;
    }
    
    /**
     * Regresa el tama&ntilde;o de tipos de IVA.
     *
     * @return int.
     */
    public int getOptionCount() {
        return _tipoIva.size();
    }

    /**
     * Regresa el tipoIva que se encuentra en el &iacute;ndice i.
     *
     * @param i El &iacute;ndice a localizar.
     * @return TipoIva.
     */
    public Object getOption(int i) {
        return _tipoIva.get(i);
    }

    /**
     * Regresa la etiqueta para el combo box, concatenando la descripci&oacute;n m&aacute;s el tipo Iva
     * que se encuentra en el &iacute;ndice i.
     *
     * @param i El &iacute;ndice a localizar.
     * @return String.
     */
    public String getLabel(int i) {
        TipoIva ti = ((TipoIva) getOption(i));
        StringBuffer sb = new StringBuffer(String.valueOf(ti.getPorcIva()));
        sb.append("%.- ").append(ti.getDescripcion() != null ? ti.getDescripcion() : "");
        return sb.toString();
    }

    /**
     * Regresa el tipoIva.
     *
     * @param i El &iacute;ndice a localizar.
     * @return String.
     */
    public String getValue(int i) {
        return ((TipoIva) getOption(i)).getClaveTipoIva();
    }

    /**
     * Regresa la clave tipo IVA que llega como par&aacute;metro.
     *
     * @param s La clave tipo IVA.
     * @return String
     */
    public Object translateValue(String s) {
        return s;
    }
	
	/**
     * La lista de objetos tipoIva.
     */
    private List _tipoIva;

}

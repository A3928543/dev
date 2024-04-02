/*
 * $Id: BancosSelectionModel.java,v 1.11 2008/02/22 18:25:35 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import com.ixe.treasury.dom.common.Banco;
import java.util.List;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * Modelo para PropertySelection de Bancos. El valor asignado es el id del banco, mientras que el texto desplegado
 * en el comboBox corresponde al nombre corto del banco.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:35 $
 */
public class BancosSelectionModel implements IPropertySelectionModel {

    /**
     * Constructor por default. Inicializa la lista de bancos a desplegar en el combo box.
     *
     * @param bancos La lista de bancos.
     */
    public BancosSelectionModel(List bancos) {
        super();
        _bancos = bancos;
    }

    /**
     * Regresa el tamaño de la lista de bancos.
     *
     * @return int.
     */
    public int getOptionCount() {
        return _bancos.size();
    }

    /**
     * Regresa el banco en el &iacute;ndice i.
     *
     * @param i El &iacute;ndice a buscar.
     * @return Object.
     */
    public Object getOption(int i) {
        return _bancos.get(i);
    }

    /**
     * Regresa el nombre corto del banco que se encuentra en el &iacute;ndice i.
     *
     * @param i El &iacute;ndice a buscar.
     * @return String.
     */
    public String getLabel(int i) {
    	String label = "";
    	if ("-1".equals(((Banco) getOption(i)).getClaveBanxico())) {
    		label = ((Banco) getOption(i)).getNombreCorto();
    	}
    	else {
    		label = ((Banco) getOption(i)).getNombreCorto() + " " +  ((Banco) getOption(i)).getClaveBanxico();
    	}
    	return label; 
    }

    /**
     * Regresa el id del banco que se encuentra en el &iacute;ndice i.
     *
     * @param i El &iacute;ndice a buscar.
     * @return String.
     */
    public String getValue(int i) {
        return String.valueOf(((Banco) getOption(i)).getIdBanco());
    }

    /**
     * Transforma el valor de string a Integer.
     *
     * @param string El valor a trasformar.
     * @return Object.
     */
    public Object translateValue(String string) {
        return Integer.valueOf(string);
    }

    /**
     * La lista de bancos.
     *
     * @see Banco
     */
    private List _bancos;
}

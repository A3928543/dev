/*
 * $Id: TabMetalesAmonedados.java,v 1.1.22.1 2011/04/26 02:52:10 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pizarron;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Table model para la captura de metales amonedados.
 *
 * @author Israel Rebollar.
 * @version $Revision: 1.1.22.1 $ $Date: 2011/04/26 02:52:10 $
 */
public class TabMetalesAmonedados extends com.ixe.ods.sica.pizarron.AbstractTabMod {

    /**
     * Constructor por default. Inicializa todas las variables de instancia.
     *
     * @param rows La lista de renglones de la pizarra.
     */
    public TabMetalesAmonedados(List rows) {
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
        		return "C (HOY)";
        	case 3:
        		return "V (HOY)";
        	case Consts.NUM_4:
        		return "C (TOM)";
        	case Consts.NUM_5:
        		return "V (TOM)";
        	case Consts.NUM_6:
        		return "C (SPOT)";
        	default :
        		return "V (SPOT)";
        }
    }
    
    /**
     * Regresa 8.
     *
     * @return int.
     */
    public int getColumnCount() {
    	return Consts.NUM_8;
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
        DecimalFormat df = new DecimalFormat("0.00");
        switch (col) {
	        case 0:
	            return r.get("id");
	        case 1:
	            return r.get("descripcion");
	        case 2:
	            return df.format(r.get(Consts.C_CASH_KEY));
	        case Consts.NUM_3:
	            return df.format(r.get(Consts.V_CASH_KEY));
	        case Consts.NUM_4:
	        	return df.format(r.get(Consts.C_TOM_KEY));
	        case Consts.NUM_5:
	        	return df.format(r.get(Consts.V_TOM_KEY));
	        case Consts.NUM_6:
	        	return df.format(r.get(Consts.C_SPOT_KEY));
	        case Consts.NUM_7:
	        	return df.format(r.get(Consts.V_SPOT_KEY));
	        default:
	            return df.format(r.get(Consts.ID_FACTOR_DIVISA_KEY));
	    }
    }

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 11234154164577L;
}

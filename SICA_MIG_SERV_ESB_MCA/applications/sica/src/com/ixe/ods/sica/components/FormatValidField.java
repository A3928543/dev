/*
 * $Id: FormatValidField.java,v 1.10 2008/02/22 18:25:35 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import org.apache.tapestry.valid.ValidField;

import java.text.DecimalFormat;

/**
 * Sublase de ValidField que permite especificar un formato para cantidades num&eacute;ricas.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:35 $
 * @see FormatValidField
 */
public abstract class FormatValidField extends ValidField {

    /**
     * Regresa un DecimalFormat con el formato especificado por el usuario.
     *
     * @return DecimalFormat.
     */
    public DecimalFormat getDecimalFormat() {
        return new DecimalFormat(getFormat());
    }

    /**
     * Regresa el valor formateado.
     *
     * @return String.
     */
    protected String readValue() {
        String result = super.readValue();
        return getFormat() == null ? result : getDecimalFormat().format(new Double(result));
    }

    /**
     * El formato que se aplicar&aacute;.
     *
     * @return String.
     */
    public abstract String getFormat();
}

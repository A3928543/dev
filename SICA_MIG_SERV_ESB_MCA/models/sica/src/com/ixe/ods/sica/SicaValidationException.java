/*
 * $Id: SicaValidationException.java,v 1.3 2008/02/22 18:25:31 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica;

/**
 * Subclase de SicaException recomendada para reportar errores de validaci&oacute;n.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.3 $ $Date: 2008/02/22 18:25:31 $
 */
public class SicaValidationException extends SicaException {

    /**
     * Constructor por default. Inicializa el mensaje de la excepci&oacute;n.
     *
     * @param message El mensaje de la excepci&oacute;n.
     */
    public SicaValidationException(String message) {
        super(message);
    }

    /**
     * Constructor que recibe un mensaje y una causa.
     *
     * @param message El mensaje de la excepci&oacute;n.
     * @param cause La causa de la excepci&oacute;n.
     */
    public SicaValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * El UID Para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 2249826505710909889L;
}

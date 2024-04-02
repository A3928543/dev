/*
 * $Id: SicaFechaValorException.java,v 1.3 2008/02/22 18:25:31 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica;

/**
 * Clase de excepciones para el SICA.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.3 $ $Date: 2008/02/22 18:25:31 $
 */
public class SicaFechaValorException extends SicaException {

    /**
     * Constructor por default. Inicializa el mensaje de la excepci&oacute;n.
     *
     * @param message El mensaje de la excepci&oacute;n.
     */
    public SicaFechaValorException(String message) {
        super(message);
    }

    /**
     * Constructor que recibe un mensaje y una causa.
     *
     * @param message El mensaje de la excepci&oacute;n.
     * @param cause La causa de la excepci&oacute;n.
     */
    public SicaFechaValorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 4840103480122755885L;
}

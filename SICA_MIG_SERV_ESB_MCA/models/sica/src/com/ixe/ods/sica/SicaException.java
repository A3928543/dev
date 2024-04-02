/*
 * $Id: SicaException.java,v 1.15 2010/04/13 20:25:21 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import org.springframework.remoting.RemoteAccessException;

/**
 * Clase de excepciones para el SICA.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.15 $ $Date: 2010/04/13 20:25:21 $
 */
public class SicaException extends RuntimeException {

    /**
     * Constructor por default. Inicializa el mensaje de la excepci&oacute;n.
     *
     * @param message El mensaje de la excepci&oacute;n.
     */
    public SicaException(String message) {
        super(message);
    }

    /**
     * Constructor que recibe un mensaje y una causa.
     *
     * @param message El mensaje de la excepci&oacute;n.
     * @param cause La causa de la excepci&oacute;n.
     */
    public SicaException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Transforma en una RemoteAccessException en SicaException.
     *
     * @param e La excepci&oacute;n a transformar.
     * @return SicaException.
     */
    public static SicaException transformar(RemoteAccessException e) {
        Throwable t = e;
        while ((t = t.getCause()) != null) {
            if (t instanceof SicaException) {
                return (SicaException) t;
            }
        }
        return new SicaException("Ocurri\u00f3 un error. Por favor intente de nuevo la "
                + "operaci\u00f3n, si persiste el problema comun\u00edquese al \u00e1rea de "
                + "sistemas.");
    }

    /**
     * El UID para Serializaci&oacute;n.
     */
    private static final long serialVersionUID = -3497580141652910261L;
}

/*
 * $Id: SicaExceptionHelper.java,v 1.2 2010/04/13 20:25:45 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica;

import org.springframework.remoting.RemoteAccessException;

/**
 * Helper para SicaExceptions.
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.2 $ $Date: 2010/04/13 20:25:45 $
 */
public class SicaExceptionHelper {

    /**
     * Constructor por default.
     */
    private SicaExceptionHelper() {
        super();
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
}

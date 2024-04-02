/*
 * $Id: Excepcion.java,v 1.13 2008/02/22 18:25:10 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages;

import com.ixe.ods.sica.model.Estado;
import org.apache.tapestry.util.exception.ExceptionDescription;

/**
 * P&aacute;gina de excepciones no atrapadas por las p&aacute;ginas del sistema.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.13 $ $Date: 2008/02/22 18:25:10 $
 */
public class Excepcion extends org.apache.tapestry.pages.Exception {

	/**
	 * Regresa el estado actual del sistema.
	 *
	 * @return Estado
	 */
    public Estado getEstadoActual() {
        return null;
    }

    /**
     * Define si existe un objeto no esta sincronizado con el estado de la p&aacute;gina.
     *
     * @return boolean
     */
    public boolean isStaleObjectStateException() {
        for (int i = 0; i < getExceptions().length; i++) {
            if (getExceptions()[i].getExceptionClassName().
                    indexOf("StaleObjectStateException") >= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Regresa el mensaje en caso de disparse una excepci&oacute;n de tipo
     * <code>StaleObjectStateException</code>.
     *
     * @return String
     */
    public String getMensaje() {
        return isStaleObjectStateException() ?
                "El deal ha sido modificado por otro usuario, por lo que no se pudieron actualizar "
                        + "los cambios realizados. Por favor consulte de nuevo el deal e intente "
                        + "de nuevo la operaci\u00f3n."
                : "No se pudo realizar la operaci&oacute;n. Favor de intentar de nuevo.";
    }

    /**
     * Regresa la cadena del stacktrace comentada en HTML para que no sea visible al usuario
     * directamente.
     *
     * @return String.
     */
    public String getStackTrace() {
        StringBuffer sb = new StringBuffer("<!--\n");
        try {
            ExceptionDescription[] descs = getExceptions();
            for (int i = 0; i < descs.length; i++) {
                ExceptionDescription desc = descs[i];
                sb.append("\n").append(desc.getExceptionClassName()).append(" ").
                        append(desc.getMessage()).append("\n");
                String[] sts = desc.getStackTrace();
                for (int j = 0; j < sts.length; j++) {
                    sb.append(sts[j]).append("\n");
                }
            }
            sb.append("-->");
            return sb.toString();
        }
        catch (Exception e) {
            return sb.append("-->").toString();
        }
    }
}

/*
* $Id: Mensaje.java,v 1.12 2008/06/05 01:15:14 ccovian Exp $
* Ixe, Sep 13, 2003
* Copyright (C) 2005 - 2008 Grupo Financiero Ixe
*/

package com.ixe.ods.sica.pages;

import com.ixe.ods.sica.model.Estado;

/**
 * P&aacute;gina que permite desplegar un mensaje al usuario. Principalmente es utilizada por la 
 * p&aacute;gina de CapturaDeal para indicar al usuario que su deal ha sido actualizado
 * exitosamente.
 *
 * @author  Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/06/05 01:15:14 $
 */
public abstract class Mensaje extends SicaPage {

    /**
     * Regresa un arreglo con todos los estados posibles del sistema.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[] {
            Estado.ESTADO_INICIO_DIA, Estado.ESTADO_OPERACION_NORMAL,
            Estado.ESTADO_OPERACION_RESTRINGIDA, Estado.ESTADO_OPERACION_VESPERTINA,
            Estado.ESTADO_CIERRE_DIA, Estado.ESTADO_OPERACION_NOCTURNA,
            Estado.ESTADO_BLOQUEADO };
    }

    /**
     * Establece el valor de mensaje.
     *
     * @param mensaje El valor a asignar.
     */
    public abstract void setMensaje(String mensaje);

    /**
     * Obtiene el valor del mensaje.
     * 
     * @return String
     */
    public abstract String getMensaje();

    /**
     * Establece el valor del tipo de mensaje (TIPO_ERROR | TIPO_ADVERTENCIA | TIPO_AVISO).
     *
     * @param tipo El valor a asignar.
     */
    public abstract void setTipo(int tipo);

    /**
     * Constante para mensajes de error.
     */
    public static final int TIPO_ERROR = -1;

    /**
     * Constante para mensajes de advertencia.
     */
    public static final int TIPO_ADVERTENCIA = 0;

    /**
     * Constante para mensajes de avisos.
     */
    public static final int TIPO_AVISO = 1;
}

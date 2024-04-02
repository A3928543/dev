/*
 * $Id: Empty.java,v 1.11 2008/02/22 18:25:10 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages;

import com.ixe.ods.sica.model.Estado;

/**
 * P&aacute;gina inicial del sistema SICA.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:10 $
 */
public class Empty extends SicaPage {

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
            Estado.ESTADO_BLOQUEADO
        };
    }
}

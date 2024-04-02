/*
 * $Id: ConsPosicion.java,v 1.4 2008/02/22 18:25:43 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.posicion;

import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * P&aacute;gina que lanza el monitor de la posici&oacute;n en Flex.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.4 $ $Date: 2008/02/22 18:25:43 $
 */
public class ConsPosicion extends SicaPage {

    /**
     * Constructor por default.
     */
    public ConsPosicion() {
        super();
    }

    /**
     * Regresa el estado de inicio del d&iacute;a, operacion normal, restringida y vespertina. 
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[] { Estado.ESTADO_INICIO_DIA, Estado.ESTADO_OPERACION_NORMAL,
                Estado.ESTADO_OPERACION_RESTRINGIDA, Estado.ESTADO_OPERACION_VESPERTINA,
                Estado.ESTADO_FIN_LIQUIDACIONES, Estado.ESTADO_GENERACION_CONTABLE,
                Estado.ESTADO_OPERACION_NOCTURNA
        };
    }
}

package com.ixe.ods.sica.pages.autorizatce;

import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * P&aacute;gina que lanza la pantalla de Autorizaciones de Tipo de cambio Especial.
 *
 * @author Cesar Jeronimo Gomez
 */
public class AutorizaTceLauncher extends SicaPage {

    /**
     * Constructor por default.
     */
    public AutorizaTceLauncher() {
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

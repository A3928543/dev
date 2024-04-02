/*
 * $Id: RiesgoTask.java,v 1.10 2008/02/22 18:25:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.mesa;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.sdo.CierreSicaServiceData;

import java.util.TimerTask;

/**
 * Clase que extiende de <code>TimerTask</code> la cual es utilizada para
 * verificar con cierta frecuencia los montos limites de la mesa y generar
 * seg&uacute;n sea el caso, la alerta correspondiente.
 * 
 * @author Edgar Leija
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:32 $
 */
public class RiesgoTask extends TimerTask {

    /**
     * Constructor por default.No hace nada.
     */
    public RiesgoTask() {
        super();
    }

    /**
     * Inicializa el objeto pasandole una referencia a cierreServiceData.
     *
     * @param cierreServiceData El serviceData.
     */
    public RiesgoTask(CierreSicaServiceData cierreServiceData) {
        this.cierreServiceData = cierreServiceData;
    }

    /**
     * Llamado por el Timer quien checa los l&iacute;mites.
     */
    public void run() {
        try {
            if (cierreServiceData.isEstadoOperacionNormal()) {
                cierreServiceData.checarLimites(false);
            }
        }
        catch (SicaException e) {
            e.printStackTrace();
        }
    }

    /**
     * Referencia a bean CierreSicaServiceData.
     *
     * @see CierreSicaServiceData
     */
    private CierreSicaServiceData cierreServiceData;
}

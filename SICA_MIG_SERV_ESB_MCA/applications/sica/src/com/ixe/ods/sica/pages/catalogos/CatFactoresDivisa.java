/*
 * $Id: CatFactoresDivisa.java,v 1.10 2008/02/22 18:25:39 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.catalogos;

import com.ixe.ods.sica.model.FactorDivisaActual;
import org.apache.tapestry.IRequestCycle;

/**
 * P&aacute;gina que despliega los factores divisa actuales.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:39 $
 */
public abstract class CatFactoresDivisa extends AbstractCatConsulta {

    /**
     * Elimina el factor divisa seleccionado de la base de datos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void eliminar(IRequestCycle cycle) {
        FactorDivisaActual fd = getSicaServiceData().findFactorDivisaActualByID(new Integer(cycle.getServiceParameters()[0].toString()));
        if (fd != null) {
            getSicaServiceData().delete(fd);
        }
    }
}

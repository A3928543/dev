/*
 * $Id: LogExpresionContable.java,v 1.1 2008/11/12 05:53:13 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

/**
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.1 $ $Date: 2008/11/12 05:53:13 $
 */
public class LogExpresionContable implements Serializable {

    public List getLogContabilidad() {
        return logContabilidad;
    }

    public void limpiarLogContabilidad() {
        logContabilidad = new ArrayList();
    }

    public void agregarALogContabilidad(String mensaje) {
        Map map = new HashMap();
        map.put("fechaHora", new Date());
        map.put("mensaje", mensaje);
        logContabilidad.add(map);
    }

    private List logContabilidad = new ArrayList();
}

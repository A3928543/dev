/*
 * $Id: CapturaSwaps.java,v 1.11 2008/06/05 01:15:16 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.interbancario;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.springframework.web.context.ServletContextHolder;

/**
 * P&aacute;gina que lanza la captura de swaps en Flex, prepara los par&aacute;metros necesarios
 * para mandarlos a esta p&aacute;gina de captura.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/06/05 01:15:16 $
 */
public abstract class CapturaSwaps extends SicaPage {

    /**
     * Asigna la propiedad <code>derivados</code> de acuerdo a la facultad del elemento del
     * men&uacute;.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        if (FacultySystem.SICA_CAP_SW_CONT.equals(cycle.getServiceParameters()[0])) {
            setDerivados(0);
        }
        else {
            setDerivados(1);
        }
    }

    /**
     * Regresa un mapa con las siguientes llaves: derivados, idUsuario, idCanal, idMesaCambio y
     * valFut.
     *
     * @return Map.
     */
    public Map getMapa() {
    	PizarronServiceData pizarronServiceData = (PizarronServiceData) ServletContextHolder.
                getApplicationContext().getBean("pizarronServiceData");
    	String valFut = pizarronServiceData.isValorFuturoHabilitado() ? "true" : "false";
    	Map m = new HashMap();
        m.put("derivados", new Integer(getDerivados()));
        m.put("idUsuario", new Integer(((Visit) getVisit()).getUser().getIdUsuario()));
        m.put("idCanal", ((Visit) getVisit()).getIdCanal());
        m.put("idMesaCambio", new Integer(((Visit) getVisit()).getIdMesaCambio()));
        m.put("valFut", valFut);
        return m;
    }

    /**
     * Regresa por default el estado de operacion normal y restringida.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[]{Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA};
    }
    
    /**
     * Regresa el valor de derivados.
     *
     * @return int.
     */
    public abstract int getDerivados();

    /**
     * Establece el valor de derivados.
     *
     * @param derivados El valor a asignar.
     */
    public abstract void setDerivados(int derivados);
}

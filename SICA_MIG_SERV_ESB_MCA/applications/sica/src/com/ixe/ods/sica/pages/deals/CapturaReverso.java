/*
 * $Id: CapturaReverso.java,v 1.4 2008/06/05 01:15:13 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import javax.servlet.http.HttpSession;

import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.SicaServiceData;

/**
 * Subclase de <code>SicaPage</code> que define los parametros necesarios para mostrar
 * la pantalla de captura de Reversos en Flex.
 * 
 * @author Gustavo Gonzalez (ggonzalez)
 * @version $Revision: 1.4 $ $Date: 2008/06/05 01:15:13 $
 */
public abstract class CapturaReverso extends SicaPage {
	
	/**
     * Llama a <code>super.activate()</code>. Define los parametros necesarios
     * para mostrar la interfaz de la captura de Reversos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        limpiarTodo();
        HttpSession ses = getRequestCycle().getRequestContext().getSession();
        SicaServiceData sd = getSicaServiceData();
        ses.setAttribute("idUsuario", new Integer(((Visit) getVisit()).getUser().getIdUsuario()));
        ses.setAttribute("idPromotor", ((Visit) getVisit()).getUser().getIdPersona());
        ses.setAttribute("ixeDirecto", Boolean.valueOf(((Visit) getVisit()).isIxeDirecto()));
        ses.setAttribute("guardia", Boolean.valueOf(((Visit) getVisit()).isGuardia()));
        ses.setAttribute("facultad", cycle.getServiceParameters()[0]);
        ses.setAttribute("desvMontoLim", sd.findParametro("DESV_MONTO_LIM").getValor());
        ses.setAttribute("desvPorcLim", sd.findParametro("DESV_PORC_LIM").getValor());
        ses.setAttribute("desvPorcMax", sd.findParametro("DESV_PORC_MAX").getValor());
        ses.setAttribute("desvFact1", sd.findParametro("DESV_FACT_1").getValor());
        ses.setAttribute("desvFact2", sd.findParametro("DESV_FACT_2").getValor());
    }

	/**
     * Regresa por default el estado de operacion normal y restringida.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[]{Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA};
    }    
}

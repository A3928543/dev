/*
 * $Id: ConsultaSwaps.java,v 1.13 2010/05/20 20:40:41 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.interbancario;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.Swap;
import com.ixe.ods.sica.pages.Empty;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.SwapsServiceData;

/**
 * P&aacute;gina que realiza la consulta de swaps.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.13 $ $Date: 2010/05/20 20:40:41 $
 */
public abstract class ConsultaSwaps extends SicaPage {

    /**
     * Asigna lo valores necesarios al activarse la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        setIdSwap(0);
        setIdDeal(0);
        setFecha(new Date());
        setStatus("");
        if (getSwaps() == null) {
            setSwaps(new ArrayList());
        }
    }

    /**
     * Regresa el mapa para asignar el focus al campo de texto idSwapTextField.
     *
     * @return Map.
     */
    public Map getFirstTextFieldMap() {
        Map map = new HashMap();
        
        map.put("textField", "document.Form0.idSwapTextField");
        return map;
    }

    /**
     * Ejecuta la b&uacute;squeda del swap con los criterios definidos por el usuario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void buscarSwaps(IRequestCycle cycle) {
        IValidationDelegate delegate = getDelegate();

        if (delegate.getHasErrors()) {
            return;
        }
        if (getFecha() == null) {
            delegate.record("Debe proporcionar un valor para Fecha Operaci\u00f3n.", null);
            return;
        }
        List swaps = getSwapsServiceData().findSwaps(getIdSwap(), getIdDeal(), getFecha(),
                getStatus());
        if (swaps.isEmpty()) {
            delegate.record("La b\u00fasqueda no arroj\u00f3 resultados.", null);
        }
        setSwaps(swaps);
    }

    /**
     * Activa la p&aacute;gina 'DatosSwap' y le asigna el swap seleccionado para que el usuario
     * pueda ver su
     * informaci&oacute;n.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void seleccionarSwap(IRequestCycle cycle) {
        DatosSwap nextPage = (DatosSwap) cycle.getPage("DatosSwap");
        Swap swap = getSwapsServiceData().findSwap(((Integer) cycle.getServiceParameters()[0]).
                intValue());
        for (Iterator it = swap.getDeals().iterator(); it.hasNext();) {
            Deal deal = (Deal) it.next();
            deal.setSeleccionado(false);
        }
        nextPage.setSwap(swap);
        nextPage.activate(cycle);
    }

    /**
     * Activa la p&aacute;gina 'Empty'.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cancelar(IRequestCycle cycle) {
        ((Empty) cycle.getPage("Empty")).activate(cycle);
    }

    /**
     * Regresa la referencia a swapsServiceData.
     *
     * @return SwapsServiceData.
     */
    private SwapsServiceData getSwapsServiceData() {
        return (SwapsServiceData) getApplicationContext().
                getBean("swapsServiceData");
    }

    /**
     * Regresa el arreglo con los estados de operaci&oacute;n normal y operaci&oacute;n restringida.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[]{Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
                Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_OPERACION_NOCTURNA};
    }

    /**
     * Regresa el valor de fecha.
     *
     * @return El valor a asignar.
     */
    public abstract Date getFecha();

    /**
     * Establece el valor de fecha.
     *
     * @param fecha El valor a asignar.
     */
    public abstract void setFecha(Date fecha);

    /**
     * Regresa el valor de idDeal.
     *
     * @return int.
     */
    public abstract int getIdDeal();

    /**
     * Establece el valor de idDeal.
     *
     * @param idDeal El valor a asignar.
     */
    public abstract void setIdDeal(int idDeal);

    /**
     * Regresa el valor de idSwap.
     *
     * @return int.
     */
    public abstract int getIdSwap();

    /**
     * Establece el valor de idSwap.
     *
     * @param idSwap El valor a asignar.
     */
    public abstract void setIdSwap(int idSwap);

    /**
     * Regresa el valor de status.
     *
     * @return String.
     */
    public abstract String getStatus();

    /**
     * Establece el valor de status.
     *
     * @param status El valor a asignar.
     */
    public abstract void setStatus(String status);

    /**
     * Regresa el valor de swaps.
     *
     * @return List de Swap.
     */
    public abstract List getSwaps();

    /**
     * Establece el valor de swaps.
     *
     * @param swaps El valor a asignar.
     */
    public abstract void setSwaps(List swaps);
}

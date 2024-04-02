/*
 * $Id: ConsUtilidadesGlobales.java,v 1.3 2008/02/22 18:25:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.mesa;

import com.ixe.ods.sica.model.RecoUtilidad;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.SicaException;

import java.util.Iterator;
import java.util.List;
import java.util.Date;
import org.apache.tapestry.IRequestCycle;

/**
 * P&aacute;gina que despliega registros de la tabla SC_RECO_UTILIDAD, de acuerdo al rango de fechas
 * especificado por el usuario.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.3 $ $Date: 2008/02/22 18:25:32 $
 */
public abstract class ConsUtilidadesGlobales extends SicaPage {

    /**
     * Llama al servicio <code>findUtilidadesGlobales()</code> del sica service data
     *
     * @param cycle El IRequestCycle.
     */
    public void buscar(IRequestCycle cycle) {
        try {
            if (getDelegate().getHasErrors()) {
                return;
            }
            if (getFechaInicio() == null || getFechaFin() == null) {
                throw new SicaException("Por favor ingrese la fecha de inicio y fin.");
            }
            List registros = getSicaServiceData().findUtilidadesGlobales(getFechaInicio(),
                    getFechaFin());
            if (registros.isEmpty()) {
                throw new SicaException("No se encontraron registros en el rango de fechas " +
                        "especificado.");
            }
            double tmp = 0;
            double tmpPerdida = 0;
            for (Iterator iterator = registros.iterator(); iterator.hasNext();) {
            	RecoUtilidad reco = (RecoUtilidad) iterator.next();
            	if(reco.isRecibimos()){
            		tmpPerdida = tmpPerdida + (reco.getMonto() * reco.getTipoCambioOtraDivRef());
            	}
            	else{
            		tmp = tmp + (reco.getMonto() * reco.getTipoCambioOtraDivRef());
            	}
            }
            setImporteGral(tmp);
            setImporteGralPerdida(tmpPerdida);
            setRegistros(registros);
        }
        catch (SicaException e) {
            debug(e);
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * Regresa el valor de fechaInicio.
     *
     * @return Date.
     */
    public abstract Date getFechaInicio();

    /**
     * Regresa el valor de fechaFin.
     *
     * @return Date.
     */
    public abstract Date getFechaFin();

    /**
     * Establece el valor de registros.
     *
     * @param registros El valor a asignar.
     */
    public abstract void setRegistros(List registros);
    
    /**
     * Fija el valor de importeGral.
     *
     * @param importeGral El valor a asignar.
     */
    public abstract void setImporteGral(double importeGral);
    
    /**
     * Fija el valor de importeGralPerdida.
     *
     * @param importeGralPerdida El valor a asignar.
     */
    public abstract void setImporteGralPerdida(double importeGralPerdida);
}

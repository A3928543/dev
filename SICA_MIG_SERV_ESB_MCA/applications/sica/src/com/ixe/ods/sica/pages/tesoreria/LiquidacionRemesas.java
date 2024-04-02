/*
 * $Id: LiquidacionRemesas.java,v 1.4.84.1 2016/08/03 01:09:10 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.tesoreria;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Actividad;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * P&aacute;gina que permite al usuario de tesorer&iacute;a confirmar la validez de una
 * operaci&oacute;n de remesas, con el fin de liberar el uso de la l&iacute;nea de cr&eacute;dito
 * relacionada con dicha operaci&oacute;n.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.4.84.1 $ $Date: 2016/08/03 01:09:10 $
 */
public abstract class LiquidacionRemesas extends SicaPage {

    /**
     * Llena la lista de reversos pendientes.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findRemesasPendientes(java.util.Date).
     */
    public void buscar(IRequestCycle cycle) {
        setRemesasPendientes(getSicaServiceData().findRemesasPendientes(getFecha()));
    }

    /**
     * Termina la actividad de remesas pendientes y libera el uso de la l&iacute;nea de cambios del
     * registro seleccionado por el usuario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see com.ixe.ods.sica.sdo.LineaCambioServiceData#liberarRemesa(String,
     *          com.ixe.ods.sica.model.Actividad, com.ixe.ods.seguridad.model.IUsuario).
     */
    public void confirmarValidez(IRequestCycle cycle) {
        Visit visit = (Visit) getVisit();
        int idActividad = ((Integer) cycle.getServiceParameters()[0]).intValue();
        Actividad actividad = null;
        for (Iterator it = getRemesasPendientes().iterator(); it.hasNext();) {
             Actividad a = (Actividad) it.next();
            if (a.getIdActividad() == idActividad) {
                actividad = a;
                break;
            }
        }
        if (actividad != null) {
            DealDetalle det = actividad.getDealDetalle();
           
            getDelegate().record("La remesa del deal " + det.getDeal().getIdDeal() +
                    " por un monto de " + getMoneyFormat().format(det.getMonto()) +  " " +
                    det.getDivisa().getDescripcion() + " ha sido validada.", null);
            setNivel(1);
        }
        buscar(cycle);
    }

    /**
     * Establece el valor de la propiedad nivel.
     *
     * @param nivel El valor a asignar.
     */
    public abstract void setNivel(int nivel);

    /**
     * Regresa el valor de fecha.
     *
     * @return Date.
     */
    public abstract Date getFecha();

    /**
     * Regresa el valor de remesasPendientes.
     *
     * @return List de Actividad.
     */
    public abstract List getRemesasPendientes();

    /**
     * Establece el valor de la propiedad remesasPendientes.
     *
     * @param remesasPendientes El valor a asignar.
     */
    public abstract void setRemesasPendientes(List remesasPendientes);
}

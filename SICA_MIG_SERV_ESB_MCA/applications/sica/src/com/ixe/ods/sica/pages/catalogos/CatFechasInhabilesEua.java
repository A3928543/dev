/*
 * $Id: CatFechasInhabilesEua.java,v 1.2 2009/11/17 22:35:39 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.catalogos;

import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.FechaInhabilEua;
import com.ixe.ods.sica.pages.SicaPage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

/**
 * P&aacute;gina del Cat&aacute;logo de Fechas Inh&aacute;biles en Estados Unidos.
 * 
 * @author Israel Rebollar
 * @version $Revision: 1.2 $ $Date: 2009/11/17 22:35:39 $
 */
public abstract class CatFechasInhabilesEua extends SicaPage {

    /**
     * Inicializa la lista de fechas inh&aacute;biles.
     * 
     * @param cycle El IRequestCycle.
     */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		//setDiasInhabiles(getSicaServiceData().findAllFechasInhabilesEua());
		cargarFechasInhabilesEua();
	}
	
	private void cargarFechasInhabilesEua(){
		Map yearsMap = new HashMap();
		FechaInhabilEua fecha;
		List fechasInhabilesList;
        int year;
        for (Iterator it = getSicaServiceData().findAllFechasInhabilesEua().iterator();
             it.hasNext();) {
            fecha = (FechaInhabilEua) it.next();
            Calendar cal = Calendar.getInstance();
            cal.setTime(fecha.getIdFechaInhabilEua().getFecha());
            year = cal.get(Calendar.YEAR);
            if (!yearsMap.containsKey("" + year)) {
                fechasInhabilesList = new ArrayList();
                fechasInhabilesList.add(fecha);
            }
            else {
				fechasInhabilesList = (List) yearsMap.get("" + year);
				fechasInhabilesList.add(fecha);
				yearsMap.remove("" + year);
			}
			yearsMap.put("" + year, fechasInhabilesList);
			setYear("" + year);
		}
		setYearsMap(yearsMap);
	}
	
	/**
	 * Agrega una nueva fecha inh&aacute;bil al calendario de EUA.
	 *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void agregarNuevaFecha(IRequestCycle cycle) {
        FechaInhabilEua nuevaFecha = new FechaInhabilEua();
        try {
            nuevaFecha.getIdFechaInhabilEua().setFecha(getNuevaFecha());
            nuevaFecha.setStatus("AC");
            nuevaFecha.setFechaCaptura(getFechaOperacion());
            nuevaFecha.setFechaUltMod(getFechaOperacion());
            nuevaFecha.setClaveUsuarioCaptura(((Visit) getVisit()).getUser().getClave());
            nuevaFecha.setClaveUsuarioModificacion(((Visit) getVisit()).getUser().getClave());
            getSicaServiceData().store(nuevaFecha);
            cargarFechasInhabilesEua();
            Calendar cal = Calendar.getInstance();
            cal.setTime(nuevaFecha.getIdFechaInhabilEua().getFecha());
            setYear("" + cal.get(Calendar.YEAR));

        }
        catch (Exception e) {
            warn(e.getMessage(), e);
            getDelegate().record("Surgi\u00f3 un error al insertar la operaci\u00f3n: \n\tLa " +
                    "fecha ya existe.", null);
        }
    }

    /**
     * Actualiza una fecha Inh&aacute;bil del calendario de EUA.
     *
     * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void cambiaEstatus(IRequestCycle cycle) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        FechaInhabilEua fechaObj;
        List yearList = (List) getYearsMap().get(getYear());
        try {
            for (Iterator iterator = yearList.iterator(); iterator.hasNext();) {
                fechaObj = (FechaInhabilEua) iterator.next();
                int dateComp = fechaObj.getIdFechaInhabilEua().getFecha().compareTo(
                        formatter.parse((String) cycle.getServiceParameters()[0]));
                if (dateComp == 0) {
                    fechaObj.setStatus(cycle.getServiceParameters()[1].toString().equals("AC") ?
                            "IN" : "AC");
                    fechaObj.setClaveUsuarioModificacion(((Visit) getVisit()).getUser().getClave());
                    fechaObj.setFechaUltMod(getFechaOperacion());
                    getSicaServiceData().update(fechaObj);
                    cargarFechasInhabilesEua();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaObj.getIdFechaInhabilEua().getFecha());
                    setYear("" + cal.get(Calendar.YEAR));
                }
            }
        }
        catch (ParseException e) {
            warn(e.getMessage(), e);
            getDelegate().record("Surgi\u00f3 un error al actualizar el estatus de la fecha.",
                    null);
        }
    }

    /**
     * Solo de referencia. No hace nada.
	 * 
     * @param cycle El ciclo de la p&aacute;gina.
	 */
    public void changeYear(IRequestCycle cycle) {
    }

    /**
     * @return IPropertySelectionModel
     */
    public IPropertySelectionModel getYearsModel() {
        List yearsList;
        String[] yearsKeys = new String[getYearsMap().keySet().size()];
        yearsKeys = (String[]) getYearsMap().keySet().toArray(yearsKeys);
        yearsList = Arrays.asList(yearsKeys);
        Collections.sort(yearsList);
        return new StringPropertySelectionModel((String[]) yearsList.toArray());
    }

    /**
     * Regresa el valor de nuevaFecha.
     *
     * @return Date La fecha elegida.
     */
    public abstract Date getNuevaFecha();

    /**
     * Regresa el valor de yearsMap.
     *
     * @return Map.
     */
	public abstract Map getYearsMap();

    /**
     * Establece el valor de yearsMap.
     *
     * @param yearsMap El valor a asignar.
     */
	public abstract void setYearsMap(Map yearsMap);

    /**
     * Establece el valor de date.
     *
     * @param date El valor a asignar.
     */
	public abstract void setNuevaFecha(Date date);

    /**
     * Regresa el valor de year.
     *
     * @return String.
     */
	public abstract String getYear();

    /**
     * Establece el valor de year.
     *
     * @param year El valor a asignar.
     */
	public abstract void setYear(String year);
}
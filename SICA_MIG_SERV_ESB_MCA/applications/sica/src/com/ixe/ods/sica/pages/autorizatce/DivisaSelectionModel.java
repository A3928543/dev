package com.ixe.ods.sica.pages.autorizatce;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.ixe.ods.sica.model.Divisa;

/**
 * Implementacion de la interfaz {@link IPropertySelectionModel} para presentar
 * las opciones de divisas
 * 
 * @author Cesar Jeronimo Gomez
 */
public class DivisaSelectionModel implements IPropertySelectionModel {
	
	/** Lista de divisas */
	private List divisas;

	public DivisaSelectionModel(List divisas) {
		super();
		this.divisas = new ArrayList();
		
		// Agrega primero una opcion "vacia" para mostrar un mensaje -- Seleccionar --
		if(divisas != null && !divisas.isEmpty()) {
			Divisa firstOption = new Divisa();
			this.divisas.add(firstOption);
			this.divisas.addAll(divisas);
		}
	}

	public String getLabel(int index) {
		if(index == 0) {
			return "-- Seleccione --";
		}
		return getDivisa(index).getIdDivisa() + " - " + getDivisa(index).getDescripcion();
	}

	public Object getOption(int index) {
		if(index == 0) {
			return null;
		}
		return getDivisa(index);
	}

	public int getOptionCount() {
		return divisas.size();
	}

	public String getValue(int index) {
		if(index == 0) {
			return "";
		}
		return getDivisa(index).getIdDivisa();
	}

	public Object translateValue(String value) {
		if(value == null || "".equals(value)) {
			return null;
		}
		for(Iterator it = divisas.iterator(); it.hasNext();) {
			Divisa currentDivisa = (Divisa) it.next();
			if(value.equals(currentDivisa.getIdDivisa())) {
				return currentDivisa;
			}
		}
		return null; // should not pass
	}
	
	private Divisa getDivisa(int index) {
		Divisa divisa = (Divisa) divisas.get(index);
		return divisa;
	}

}

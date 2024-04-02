package com.ixe.ods.sica.pages.autorizatce;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * Model para los dropdown basados en una lista de mapas, devuelve como value
 * la llave del mapa
 * 
 * @author Cesar Jeronimo Gomez
 */
public class MapSelectionModel implements IPropertySelectionModel {
	
	/** Identifica la propiedad label de un option para un combobox */
	public static final String LABEL = "LABEL";
	
	/** Identifica la propiedad value de un option para un combobox */
	public static final String VALUE = "VALUE";
	
	/** Llaves para un mapa que contiene la opcion de un combobox */
	public static final String[] VALUE_LABEL_MAP_KEYS = new String[] {VALUE, LABEL}; 
	
	/**
	 * Lista de opciones, cada una debe ser un mapa con dos pares (llave, valor), una de las llaves debe ser {@link #VALUE} y la otra
	 * {@link #LABEL}
	 */
	private List optionsMap;

	/**
	 * Full constructor
	 * 
	 * @param optionsMap {@link #optionsMap}
	 */
	public MapSelectionModel(List optionsMap) {
		super();
		this.optionsMap = optionsMap;
	}

	public String getLabel(int index) {
		return (String) getMapOption(index).get(LABEL);
	}

	public Object getOption(int index) {
		return getMapOption(index).get(VALUE);
	}

	public int getOptionCount() {
		return optionsMap.size();
	}

	public String getValue(int index) {
		return (String) getMapOption(index).get(VALUE);
	}

	public Object translateValue(String value) {
		for(Iterator it = optionsMap.iterator(); it.hasNext();) {
			Map currMap = (Map) it.next();
			if(currMap.get(VALUE).equals(value)) {
				return currMap.get(VALUE);
			}
		}
		return null; // should not pass
	}
	
	public Map getMapOption(int index) {
		return (Map)optionsMap.get(index);
	}

}

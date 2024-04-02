/*
 * $Id: GuiaContableDto.java,v 1.12 2008/02/22 18:25:40 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 LegoSoft S.C.
 */
package com.ixe.ods.sica.sdo.dto;

import com.ixe.ods.sica.model.MovimientoContable;
import java.io.Serializable;
import java.util.List;

/**
 * DTO para las Guias Contables.
 * 
 * @author Edgar Leija
 */
public class GuiaContableDto implements Serializable {

    /**
	 * Constructor por default.
	 */
	public GuiaContableDto(){
		super();
	}
	
	/**
	 * Constructor por default.
     *
     * @param mov El movimiento contable.
     * @param guiasContables La lista de guias contables.
     * @param faseContable La fase contable.
     */
	public GuiaContableDto(MovimientoContable mov, List guiasContables, String faseContable) {
		this();
		setMovimientoContable(mov);
		setGuiasContablesAsientos(guiasContables);
		setFaseContable(faseContable);
		if (mov.getDivisa().isMetalAmonedado()) {
			setIdDivisa(mov.getDivisa().getGrupo());
		}
		else {
			setIdDivisa(mov.getDivisa().getIdDivisa());
		}
	}
	
	/**
	 * Constructor por default.
     *
     * @param mov El movimiento contable.
     * @param guiasContables La lista de guias contables.
     * @param guiasContablesNeteos La lista de guias contables de neteos.
     * @param faseContable La fase contable.
     */
    public GuiaContableDto(MovimientoContable mov, List guiasContables, List guiasContablesNeteos,
                           String faseContable) {
        this();
        setMovimientoContable(mov);
        setGuiasContablesAsientos(guiasContables);
        setGuiasContablesAsientosNeteos(guiasContablesNeteos);
        setFaseContable(faseContable);
        if (mov.getDivisa().isMetalAmonedado()) {
            setIdDivisa(mov.getDivisa().getGrupo());
        }
        else {
			setIdDivisa(mov.getDivisa().getIdDivisa());
		}
	}
		
	/**
	 * Constructor por default.
     *
     * @param mov El movimiento contable.
     * @param guiasContables La lista de guias contables.
     * @param faseContable La fase contable.
     * @param descripciones La lista de descripciones.
	 */
	public GuiaContableDto(MovimientoContable mov, List guiasContables,
                           String faseContable, List descripciones) {
		setMovimientoContable(mov);
		setGuiasContablesAsientos(guiasContables);
		setFaseContable(faseContable);
		setDescripciones(descripciones);
	}
	
	/**
	 * Obtiene el valor de movimientoContable
	 * 
	 * @return MovimientoContable.
	 */
	public MovimientoContable getMovimientoContable() {
		return movimientoContable;
	}
	
	/**
	 * Asigna el valor para movimientoContable.
	 * 
	 * @param movimientoContable El valor para movimientoContable.
	 */
	public void setMovimientoContable(MovimientoContable movimientoContable) {
		this.movimientoContable = movimientoContable;
	}
	
	/**
	 * Obtiene el valor de guiasContablesAsientos.
	 * 
	 * @return List
	 */
	public List getGuiasContablesAsientos() {
		return guiasContablesAsientos;
	}
	
	/**
	 * Asigna el valor para guiasContablesAsientos.
	 * 
	 * @param guiasContablesAsientos El valor a asignar.
	 */
	public void setGuiasContablesAsientos(List guiasContablesAsientos) {
		this.guiasContablesAsientos = guiasContablesAsientos;
	}
	
	/**
	 * Obtiene el valor de guiasContablesAsientosNeteos.
	 * 
	 * @return List.
	 */
	public List getGuiasContablesAsientosNeteos() {
		return guiasContablesAsientosNeteos;
	}
	
	/**
	 * Asigna el valor de guiasContablesAsientosNeteos.
	 * 
	 * @param guiasContablesAsientosNeteos El valor para guiasContablesAsientosNeteos.
	 */
	public void setGuiasContablesAsientosNeteos(List guiasContablesAsientosNeteos) {
		this.guiasContablesAsientosNeteos = guiasContablesAsientosNeteos;
	}
	
	/**
	 * Obtiene el valor de descripciones.
	 * 
	 * @return List
	 */
	public List getDescripciones() {
		return descripciones;
	}
	
	/**
	 * Asigna el valor para descripciones.
	 * 
	 * @param descripciones El valor para descripciones.
	 */	
	public void setDescripciones(List descripciones) {
		this.descripciones = descripciones;
	}
	
	/**
	 * Obtiene el valor de faseContable.
	 * 
	 * @return String.
	 */
	public String getFaseContable() {
		return faseContable;
	}
	
	/**
	 * Asigna el valor de faseContable.
	 * 
	 * @param faseContable El valor para faseContable.
	 */
	public void setFaseContable(String faseContable) {
		this.faseContable = faseContable;
	}
	
	/**
	 * Obtiene el valor de idDivisa.
	 * 
	 * @return String
	 */
	public String getIdDivisa() {
		return idDivisa;
	}
	
	/**
	 * Asigna el valor para idDivisa.
	 * 
	 * @param idDivisa el valor para idDivisa
	 */
	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}
		
	/**
	 * El movimiento contable para la gu&iacute;a.
	 */
	private MovimientoContable movimientoContable;
	
	/**
	 * Lista de las guias contables de asientos.
	 */
	private List guiasContablesAsientos;
	
	/**
	 * Lista de guias contables de asientos para neteos.
	 */
	private List guiasContablesAsientosNeteos;
	
	/**
	 * Lista de descripciones.
	 */
	private List descripciones;
	
	/**
	 * La fase contable de la gu&iacute;a.
	 */
	private String faseContable;
	
	/**
	 * El id de la divisa. 
	 */
	private String idDivisa;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 1829977768783686219L;
}

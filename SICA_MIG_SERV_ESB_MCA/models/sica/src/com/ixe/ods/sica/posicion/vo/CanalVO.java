/*
 * $Id: CanalVO.java,v 1.11 2008/02/22 18:25:26 ccovian Exp $
 * Ixe, Jul 6, 2005
 * Copyright (C) 2001-2004 Grupo Financiero Ixe, S.A.
 */
package com.ixe.ods.sica.posicion.vo;

import java.io.Serializable;

/**
 * Value Object para Canal para la comunicaci&oacute;n del SICA con las applicaciones en Flex (Monitor de la Posici&oacute;n).
 * 
 * @author David Solis (dsolis)
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:26 $
 */
public class CanalVO implements Serializable {

	/**
	 * Constructor default
	 */
    public CanalVO() {
    }

    /**
     * Constructor que inicializa los valores para el VO. 
     * 
     * @param idCanal El id del canal.
     * @param nombre El nombre del canal.
     * @param idTipoPizarron El id del Tipo de Pizarron
     */
    public CanalVO(String idCanal, String nombre, Integer idTipoPizarron) {
    	this._idTipoPizarron = idTipoPizarron;
        this._idCanal = idCanal;
        this._nombre = nombre;
    }

    /**
     * Contructor
     * 
     * @param idCanal
     * @param nombre
     * @param idSucursal
     * @param idTipoPizarron El id del Tipo de Pizarron
     */
    public CanalVO(String idCanal, String nombre, Integer idSucursal, Integer idTipoPizarron) {
        this._idCanal = idCanal;
        this._nombre = nombre;
        this._idSucursal = idSucursal;
        this._idTipoPizarron = idTipoPizarron; 
    }

    /**
     * Regresa el identificador del canal
     * 
     * @return String
     */
    public String getIdCanal() {
        return _idCanal;
    }

    /**
     * Asigna el identificador de canal recibido
     * 
     * @param idCanal
     */
    public void setIdCanal(String idCanal) {
        this._idCanal = idCanal;
    }

    /**
     * Regresa el nombre del canal
     * 
     * @return String
     */
    public String getNombre() {
        return _nombre;
    }

    /**
     * Asigna el nombre del canal recibido
     * 
     * @param nombre
     */
    public void setNombre(String nombre) {
        this._nombre = nombre;
    }

    /**
     * Regresa el identificador de la sucursal
     * 
     * @return Integer
     */
    public Integer getIdSucursal() {
    	return _idSucursal;
    }

    /**
     * Asigna el identificador de la sucursal
     * 
     * @param idSucursal
     */
    public void setIdSucursal(Integer idSucursal) {
    	this._idSucursal = idSucursal;
    }
    
    /**
     * Regresa el identificador del Tipo de Pizarron.
     * 
     * @return Integer.
     */
    public Integer getIdTipoPizarron() {
		return _idTipoPizarron;
	}

    /**
     * Asigna el  valor para el identificador del Tipo de Pizarron.
     * 
     * @param tipoPizarron El valor para el Tipo de Pizarron.
     */
	public void setIdTipoPizarron(Integer tipoPizarron) {
		_idTipoPizarron = tipoPizarron;
	}
    
    /**
     * Identificador del canal
     */
    private String _idCanal;
    /**
     * Nombre del canal
     */
    private String _nombre;

    /**
     * Identificador de la sucursal
     */
    private Integer _idSucursal;
    
    /**
     * El indentificador del tipo de pizarron.
     */
    private Integer _idTipoPizarron;
}

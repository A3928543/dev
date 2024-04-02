/*
 * $Id: MesaVO.java,v 1.11 2008/02/22 18:25:25 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */
package com.ixe.ods.sica.posicion.vo;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Value Object para Mesa para la comunicaci&oacute;n del SICA con las applicaciones en Flex (Monitor de la Posici&oacute;n).
 * 
 * @author David Solis (dsolis)
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:25 $
 */
public class MesaVO implements Serializable {

	/**
	 * Constructor por Default. Inicializa los valores para el VO.
	 */
    public MesaVO() {
        _canales = new ArrayList();
        _sucursales = new ArrayList();
        _productos = new ArrayList();
    }

    /**
     * Constructor que inicializa los valores para el VO.
     * 
     * @param idMesaCambio El id de la mesa de cambio.
     * @param nombre El nombre de la mesa.
     */
    public MesaVO(Integer idMesaCambio, String nombre, String divisaReferencia) {
        this();
        this._idMesaCambio = idMesaCambio;
        this._nombre = nombre;
        this._divisaReferencia = divisaReferencia;
    }

    /**
     * Regresa el identificador de la mesa de cambio
     * 
     * @return Integer
     */
    public Integer getIdMesaCambio() {
        return _idMesaCambio;
    }

    /**
     * Asigna el identificador de mesa recibido
     *  
     * @param idMesaCambio
     */
    public void setIdMesaCambio(Integer idMesaCambio) {
        this._idMesaCambio = idMesaCambio;
    }

    /**
     * Regresa el nombre de la mesa
     * 
     * @return String
     */
    public String getNombre() {
        return _nombre;
    }

    /**
     * Asgina el nombre de la mesa recibida
     * 
     * @param nombre
     */
    public void setNombre(String nombre) {
        this._nombre = nombre;
    }

    /**
     * Regresa los canales asociados a la mesa
     * 
     * @return List
     */
    public List getCanales() {
        return _canales;
    }

    /**
     * Asigna una lista de canales a la mesa
     * 
     * @param canales
     */
    public void setCanales(List canales) {
        this._canales = canales;
    }

    /**
     * Regresa las sucursales asociadas a la mesa
     * 
     * @return List
     */
    public List getSucursales() {
        return _sucursales;
    }

    /**
     * Asigna una lista de sucursales a la mesa
     * 
     * @param sucursales
     */
    public void setSucursales(List sucursales) {
        this._sucursales = sucursales;
    }

    /**
     * Regresa los productos asociados a la mesa
     * 
     * @return List
     */
    public List getProductos() {
        return _productos;
    }

    /**
     * Asigna una lista de productos a la mesa
     * 
     * @param productos
     */
    public void setProductos(List productos) {
        this._productos = productos;
    }

    /**
     * Regresa la divisa de referencia
     * 
     * @return List
     */
    public String getIdDivisa() {
        return _divisaReferencia;
    }

    /**
     * Identificador de la mesa de cambio
     */
    private Integer _idMesaCambio;
    /**
     * Nombre de la mesa de cambio
     */
    private String _nombre;
    /**
     * Divisa de Referencia
     */
    private String _divisaReferencia;
    /**
     * Canales asociados a la mesa de cambio
     */
    private List _canales;
    /**
     * Sucursales asociadas a la mesa de cambio
     */
    private List _sucursales;
    /**
     * Productos asociados a la mesa de cambio
     */
    private List _productos;
}

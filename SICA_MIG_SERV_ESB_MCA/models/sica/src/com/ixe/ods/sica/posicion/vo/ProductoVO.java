/*
 * $Id: ProductoVO.java,v 1.11 2008/02/22 18:25:26 ccovian Exp $
 * Ixe, Jul 6, 2005
 * Copyright (C) 2001-2004 Grupo Financiero Ixe, S.A.
 */
package com.ixe.ods.sica.posicion.vo;

import java.io.Serializable;

/**
 * Value Object para Producto para la comunicaci&oacute;n del SICA con las applicaciones en Flex (Monitor de la Posici&oacute;n).
 * 
 * @author David Solis (dsolis)
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:26 $
 */
public class ProductoVO implements Serializable {

	/**
	 * Constructor por Default.
	 */
    public ProductoVO() {
    }

    /**
     * Constructor que inicializa los valores para VO.
     * 
     * @param idProducto El id del producto.
     */
    public ProductoVO(String idProducto) {
        this._idProducto = idProducto;
    }

    /**
     * Constructor que inicializa los valores para VO.
     * 
     * @param idProducto El id del producto.
     * @param descripcion La descripci&oacute;n del producto.
     */
    public ProductoVO(String idProducto, String descripcion) {
        this._idProducto = idProducto;
        this.descripcion = descripcion;
    }

    /**
     * Regresa el identificador del producto
     * 
     * @return String
     */
    public String getIdProducto() {
        return _idProducto;
    }

    /**
     * Asigna un identificador al producto
     * 
     * @param idProducto
     */
    public void setIdProducto(String idProducto) {
        this._idProducto = idProducto;
    }

    /**
     * Regresa la descripcion del producto
     * 
     * @return String
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Asigna la descripcion del producto recibida
     * 
     * @param descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Identificador del producto
     */
    private String _idProducto;
    /**
     * Descripcion del producto
     */
    private String descripcion;
}

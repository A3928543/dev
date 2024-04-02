/*
 * $Id: HistoricoTraspasoProducto.java,v 1.12 2008/02/22 18:25:22 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

/**
 * <p>Clase persistente para la tabla SC_H_TRASPASO_PRODUCTO, donde se almacena el
 * hist&oacute;rico de Traspaso Producto.
 *
 * @hibernate.class table="SC_H_TRASPASO_PRODUCTO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoTraspasoProducto"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:22 $
 */

public class HistoricoTraspasoProducto extends TraspasoProducto{

    /**
     * Constructor vac&iacute;o, no hace nada.
     */
    public HistoricoTraspasoProducto() {
        super();
    }

    /**
     * Regresa el valor de idDealPosicion.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_DEAL_POSICION"
     * unsaved-value="null"
     * @return int.
     */
    public int getIdDealPosicion() {
        return idDealPosicion;
    }
}

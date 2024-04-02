/*
 * $Id: CatMontosMaximosSucursal.java,v 1.1 2008/10/27 23:20:06 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.catalogos;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * P&aacute;gina que permite al Operador de la Mesa de Cambios editar los montos m&aacute;ximos que
 * se pueden operar en las sucursales durante los horarios vespertino y nocturno.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1 $ $Date: 2008/10/27 23:20:06 $
 */
public abstract class CatMontosMaximosSucursal extends SicaPage implements IExternalPage {

    /**
     * Asigna el canal de acuerdo al primer par&aacute;metro recibido.
     *
     * @param parametros El arreglo de par&aacute;metros del external link.
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activateExternalPage(Object[] parametros, IRequestCycle cycle) {
        setCanal(getSicaServiceData().findCanal((String) parametros[0]));
        String[] cves = getFormasPagoLiqService().getClavesFormasLiquidacion(getTicket(), true);
        String[] productos = new String[cves.length + 1];
        productos[0] = "";
        for (int i = 0; i < cves.length; i++) {
            productos[i + 1] = cves[i];

        }
        setProducto("");
        setProductos(productos);
        setMontosMaximos(new ArrayList());
    }

    public void cambiarProducto(IRequestCycle cycle) {
        if (getProducto().trim().length() == 0) {
            setMontosMaximos(new ArrayList());
            getDelegate().record("Debe seleccionar un producto.", null);
            return;
        }
        setMontosMaximos(getSicaServiceData().obtenerMontosMaximosByCanalProducto(getCanal(),
                getProducto()));
    }

    /**
     * Guarda en la base de datos los cambios realizados por el usuario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void actualizar(IRequestCycle cycle) {
        if (getDelegate().getHasErrors()) {
            return;
        }
        getSicaServiceData().update(getMontosMaximos());
    }

    public IPropertySelectionModel getProductosModel() {
        return new StringPropertySelectionModel(getProductos());
    }

    /**
     * Regresa el valor de canal.
     *
     * @return Canal.
     */
    public abstract Canal getCanal();

    /**
     * Establece el valor de canal.
     *
     * @param canal El valor a asignar.
     */
    public abstract void setCanal(Canal canal);

    /**
     * Regresa el valor de montosMaximos.
     *
     * @return List.
     */
    public abstract List getMontosMaximos();

    /**
     * Establece el valor de montosMaximos.
     *
     * @param montosMaximos El valor a asignar.
     */
    public abstract void setMontosMaximos(List montosMaximos);

    /**
     * Regresa el valor de producto.
     *
     * @return String.
     */
    public abstract String getProducto();

    /**
     * Establece el valor de producto.
     *
     * @param producto El valor a asignar.
     */
    public abstract void setProducto(String producto);

    /**
     * Regresa el valor de productos.
     *
     * @return String[].
     */
    public abstract String[] getProductos();

    /**
     * Establece el valor de productos.
     *
     * @param productos El valor a asignar.
     */
    public abstract void setProductos(String[] productos);
}

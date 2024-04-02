/*
 * $Id: CanalMesa.java,v 1.12 2008/02/22 18:25:21 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * Componente para representar las relaciones a canal y a mesa de cambios.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:21 $
 */
public class CanalMesa implements Serializable {

    /**
     * Constructor vac&iacute;o. No hace nada.
     */
    public CanalMesa() {
        super();
    }

    /**
     * Constructor por default. Inicializa todas las variables de instancia.
     *
     * @param canal El canal a asignar.
     * @param mesaCambio La mesa de cambio a asignar.
     */
    public CanalMesa(Canal canal, MesaCambio mesaCambio) {
        this();
        _canal = canal;
        _mesaCambio = mesaCambio;
    }

    /**
     * Regresa el valor de canal.
     *
     * @hibernate.many-to-one column="ID_CANAL"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Canal"
     * outer-join="auto"
     * unique="false"
     * @return Canal.
     */
    public Canal getCanal() {
        return _canal;
    }

    /**
     * Fija el valor de canal.
     *
     * @param canal El valor a asignar.
     */
    public void setCanal(Canal canal) {
        _canal = canal;
    }

    /**
     * Regresa el valor de mesaCambio.
     *
     * @hibernate.many-to-one column="ID_MESA_CAMBIO"
     * cascade="none"
     * class="com.ixe.ods.sica.model.MesaCambio"
     * outer-join="auto"
     * unique="false"
     * @return MesaCambio.
     */
    public MesaCambio getMesaCambio() {
        return _mesaCambio;
    }

    /**
     * Fija el valor de mesaCambio.
     *
     * @param mesaCambio El valor a asignar.
     */
    public void setMesaCambio(MesaCambio mesaCambio) {
        _mesaCambio = mesaCambio;
    }

    /**
     * Relaci&oacute;n muchos-a-uno con Canal.
     */
    private Canal _canal;

    /**
     * Relaci&oacute;n muchos-a-uno con MesaCambio.
     */
    private MesaCambio _mesaCambio;
}

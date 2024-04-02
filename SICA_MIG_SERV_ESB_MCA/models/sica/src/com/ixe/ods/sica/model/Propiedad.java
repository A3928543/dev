/*
 * $Id: Propiedad.java,v 1.12 2008/02/22 18:25:24 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * Clase persistente para la tabla ODS_PROPIEDADES.
 *
 * @hibernate.class table="ODS_PROPIEDADES"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Propiedad"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:24 $
 */
public class Propiedad implements Serializable {

    /**
     * Constructor por default.
     */
    public Propiedad() {
        super();
    }

    /**
     * Regresa el valor de atributo.
     *
     * @hibernate.id column="ATRIBUTO"
     * unsaved-value="0"
     * generator-class="assigned"
     * @return String.
     */
    public String getAtributo() {
        return atributo;
    }

    /**
     * Fija el valor de atributo.
     *
     * @param atributo El valor a asignar.
     */
    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    /**
     * Regresa valor.
     *
     * @hibernate.property column="VALOR"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getValor() {
        return valor;
    }

    /**
     * Fija valor.
     *
     * @param valor El valor a asignar.
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * El identificador del registro.
     */
    private String atributo;

    /**
     * El valor de la propiedad.
     */
    private String valor;
}

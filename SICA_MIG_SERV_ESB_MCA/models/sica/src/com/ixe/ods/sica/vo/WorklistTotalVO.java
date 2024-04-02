/*
 * WorklistTotalVO.java,v 1.1 2007/06/21 17:15:54 jfavila Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.vo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Value Object que contiene la informaci&oacute;n de total de actividades pendientes clasificadas
 * por tipo de actividad.
 *
 * @author Jean C. Favila
 * @version : 1.1 $ : 2007/03/06 18:50:41 $
 */
public class WorklistTotalVO implements Serializable {

    /**
     * Constructor vac&iacute;o.
     */
    public WorklistTotalVO() {
        super();
    }

    /**
     * Constructor para inicializar todas las variables de instancia.
     *
     * @param nombreActividad El tipo de actividad.
     * @param total El n&uacute;mero total de actividades para ese tipo.
     */
    public WorklistTotalVO(String nombreActividad, Integer total) {
        this();
        this.nombreActividad = nombreActividad;
        this.total = total;
    }

    /**
     * Regresa el valor de nombreActividad.
     *
     * @return String.
     */
    public String getNombreActividad() {
        return nombreActividad;
    }

    /**
     * Establece el valor de nombreActividad.
     *
     * @param nombreActividad El valor a asignar.
     */
    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    /**
     * Regresa el valor de total.
     *
     * @return Integer.
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * Establece el valor de total.
     *
     * @param total El valor a asignar.
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * Regresa una descripci&oacute;n del objeto, construida usando ToStringBuilder.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("nombreActividad", nombreActividad).
                append("total", total).toString();
    }

    /**
     * El tipo de actividad.
     */
    private String nombreActividad;

    /**
     * El n&uacute;mero total de actividades para ese tipo.
     */
    private Integer total;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -3359750499210390410L;
}

/*
 * $Id: CambStatus.java,v 1.12 2008/02/22 18:25:20 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Componente que define las propiedades comunes para la informaci&oacute;n
 * sobre cambio de status en encabezados y detalles de deals, etc.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:20 $
 */
public class CambStatus implements Serializable {

    /**
     * Constructor vac&iacute;o. No hace nada.
     */
    public CambStatus() {
        super();
    }

    /**
     * Constructor por default. Inicializa todas las variables de instancia.
     *
     * @param fechaCambio La fecha de cambio del status.
     * @param statusAnterior El valor del status anterior.
     * @param statusNuevo El valor del nuevo status.
     */
    public CambStatus(Date fechaCambio, String statusAnterior, String statusNuevo) {
        this();
        this.fechaCambio = fechaCambio;
        this.statusAnterior = statusAnterior;
        this.statusNuevo = statusNuevo;
    }

    /**
     * Regresa el valor de fechaCambio.
     *
     * @hibernate.property column="FECHA_CAMBIO"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaCambio() {
        return fechaCambio;
    }

    /**
     * Fija el valor de fechaCambio.
     *
     * @param fechaCambio El valor a asignar.
     */
    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    /**
     * Regresa el valor de statusAnterior.
     *
     * @hibernate.property column="STATUS_ANTERIOR"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getStatusAnterior() {
        return statusAnterior;
    }

    /**
     * Fija el valor de statusAnterior.
     *
     * @param statusAnterior El valor a asignar.
     */
    public void setStatusAnterior(String statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    /**
     * Regresa el valor de statusNuevo.
     *
     * @hibernate.property column="STATUS_NUEVO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getStatusNuevo() {
        return statusNuevo;
    }

    /**
     * Fija el valor de statusNuevo.
     *
     * @param statusNuevo El valor a asignar.
     */
    public void setStatusNuevo(String statusNuevo) {
        this.statusNuevo = statusNuevo;
    }

    /**
     * La fecha en que ocurri&oacute; el cambio de status.
     */
    private Date fechaCambio = new Date();

    /**
     * El status previo.
     */
    private String statusAnterior;

    /**
     * El nuevo status.
     */
    private String statusNuevo;
}

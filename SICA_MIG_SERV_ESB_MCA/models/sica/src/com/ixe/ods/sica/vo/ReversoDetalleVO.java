/*
 * $Id: ReversoDetalleVO.java,v 1.2 2008/02/22 18:25:24 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.vo;

import com.ixe.ods.sica.model.ReversoDetalle;
import java.io.Serializable;

/**
 * Value Object para el model de Hibernate com.ixe.ods.sica.model.ReversoDetalle.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2008/02/22 18:25:24 $
 */
public class ReversoDetalleVO implements Serializable {

    /**
     * Constructor por default, no hace nada.
     */
    public ReversoDetalleVO() {
        super();
    }

    public ReversoDetalleVO(ReversoDetalle revDet) {
        this();
        idReversoDetalle = revDet.getIdReversoDetalle();
        montoNuevo = revDet.getMontoNuevo();
        tipoCambioNuevo = revDet.getTipoCambioNuevo();
    }

    /**
     * Regresa el valor de idReversoDetalle.
     *
     * @return int.
     */
    public int getIdReversoDetalle() {
        return idReversoDetalle;
    }

    /**
     * Establece el valor de idReversoDetalle.
     *
     * @param idReversoDetalle El valor a asignar.
     */
    public void setIdReversoDetalle(int idReversoDetalle) {
        this.idReversoDetalle = idReversoDetalle;
    }

    /**
     * Regresa el valor de montoNuevo.
     *
     * @return Double.
     */
    public Double getMontoNuevo() {
        return montoNuevo;
    }

    /**
     * Establece el valor de montoNuevo.
     *
     * @param montoNuevo El valor a asignar.
     */
    public void setMontoNuevo(Double montoNuevo) {
        this.montoNuevo = montoNuevo;
    }

    /**
     * Regresa el valor de tipoCambioNuevo.
     *
     * @return Double.
     */
    public Double getTipoCambioNuevo() {
        return tipoCambioNuevo;
    }

    /**
     * Establece el valor de tipoCambioNuevo.
     *
     * @param tipoCambioNuevo El valor a asignar.
     */
    public void setTipoCambioNuevo(Double tipoCambioNuevo) {
        this.tipoCambioNuevo = tipoCambioNuevo;
    }

    /**
     * El identificador del registro.
     */
    private int idReversoDetalle;

    /**
     * El monto que debi&oacute; capturar el usuario inicialmente (opcional).
     */
    private Double montoNuevo;

    /**
     * El tipo de cambio al que debi&oacute; pactarse el detalle original (opcional).
     */
    private Double tipoCambioNuevo;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 351085410650968399L;
}

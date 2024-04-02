/*
 * $Id: HistoricoPrecioReferencia.java,v 1.12.42.1 2011/04/26 00:46:47 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @hibernate.class table="SC_H_PRECIO_REFERENCIA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoPrecioReferencia"
 * dynamic-update="true"
 *
 * @author Edgar Leija.
 * @version  $Revision: 1.12.42.1 $ $Date: 2011/04/26 00:46:47 $
 */
public class HistoricoPrecioReferencia implements Serializable {

    /**
     * Constructor vac&iacute;o, no hace nada.
     */
    public HistoricoPrecioReferencia() {
        super();
    }
    
    /**
     * Constructor que recibe un precio de referencia e inicializa todas sus variables.
     * 
     * @param rp El precio de referencia que inicializa el hist&oacute;rico.
     */
    public HistoricoPrecioReferencia(PrecioReferenciaActual pr) {
        super();
        setIdPrecioReferencia(pr.getIdPrecioReferencia());
        setPrecioSpot(pr.getPreRef().getPrecioSpot());
        setPrecioCompra(pr.getPreRef().getPrecioCompra());
        setPrecioVenta(pr.getPreRef().getPrecioVenta());
        setMidSpot(pr.getPreRef().getMidSpot());
        setMetodoActualizacion(pr.getPreRef().getMetodoActualizacion());
        setUltimaModificacion(pr.getPreRef().getUltimaModificacion());
    }

    /**
     * Regresa el valor de idPrecioReferencia.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_PRECIO_REFERENCIA"
     * unsaved-value="null"
     * @return int.
     */
    public int getIdPrecioReferencia() {
        return idPrecioReferencia;
    }

    /**
     * Fija el valor de idPrecioReferencia.
     *
     * @param idPrecioReferencia El valor a asignar.
     */
    public void setIdPrecioReferencia(int idPrecioReferencia) {
        this.idPrecioReferencia = idPrecioReferencia;
    }
        
    /**
     * Regresa el valor de precioSpot.
     *
     * @hibernate.property column="PRECIO_SPOT"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getPrecioSpot() {
        return precioSpot;
    }

    /**
     * Fija el valor de precioSpot.
     *
     * @param precioSpot El valor a asignar.
     */
    public void setPrecioSpot(Double precioSpot) {
        this.precioSpot = precioSpot;
    }

    /**
     * Regresa el valor de precioCompra.
     *
     * @hibernate.property column="PRECIO_COMPRA"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getPrecioCompra() {
        return precioCompra;
    }

    /**
     * Fija el valor de precioCompra.
     *
     * @param precioCompra El valor a asignar.
     */
    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    /**
     * Regresa el valor de precioVenta.
     *
     * @hibernate.property column="PRECIO_VENTA"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getPrecioVenta() {
        return precioVenta;
    }

    /**
     * Fija el valor de precioVenta.
     *
     * @param precioVenta El valor a asignar.
     */
    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    /**
     * Regresa el valor de midSpot.
     *
     * @hibernate.property column="MID_SPOT"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getMidSpot() {
        return midSpot;
    }

    /**
     * Fija el valor de midSpot.
     *
     * @param midSpot El valor a asignar.
     */
    public void setMidSpot(Double midSpot) {
        this.midSpot = midSpot;
    }

    /**
     * Regresa el valor de metodoActualizacion.
     *
     * @hibernate.property column="METODO_ACTUALIZACION"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getMetodoActualizacion() {
        return metodoActualizacion;
    }

    /**
     * Fija el valor de metodoActualizacion.
     *
     * @param metodoActualizacion El valor a asignar.
     */
    public void setMetodoActualizacion(String metodoActualizacion) {
        this.metodoActualizacion = metodoActualizacion;
    }

    /**
     * Regresa el valor de ultimaModificacion.
     *
     * @hibernate.property column="ULTIMA_MODIFICACION"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getUltimaModificacion() {
        return ultimaModificacion;
    }

    /**
     * Fija el valor de ultimaModificacion.
     *
     * @param ultimaModificacion El valor a asignar.
     */
    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    /**
     * El precio de venta spot.
     */
    private Double precioSpot;

    /**
     * El precio de compra spot.
     */
    private Double precioCompra;

    /**
     * El precio de venta spot.
     */
    private Double precioVenta;
    
    /**
     * El precio mid spot.
     */
    private Double midSpot;

    /**
     * A)utom&aacute;tico, M)anual, P)romedio.
     */
    private String metodoActualizacion;

    /**
     * La fecha de la &uacute;ltima actualizaci&oacute;n del registro.
     */
    private Date ultimaModificacion = new Date();

    /**
     * El identificador del registro.
     */
    private int idPrecioReferencia;

}

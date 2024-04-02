/*
 * $Id: TraspasoProducto.java,v 1.12 2008/02/22 18:25:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import java.util.Date;

/**
 * Subclase persistente de DealPosicion, para la tabla SC_TRASPASO_PRODUCTO.
 *
 * @hibernate.joined-subclass table="SC_TRASPASO_PRODUCTO"
 * proxy="com.ixe.ods.sica.model.TraspasoProducto"
 *
 * @hibernate.joined-subclass-key column="ID_DEAL_POSICION"
 * 
 * @hibernate.query name="findTraspasosByFechaOper"
 * query="FROM TraspasoProducto AS tp WHERE tp.fechaOper BETWEEN ? AND ? ORDER BY tp.idDealPosicion"
 *
 * @author Javier Cordova
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:23 $
 */
public class TraspasoProducto extends DealPosicion {

    /**
     * Constructor por default, no hace nada.
     */
    public TraspasoProducto() {
        super();
    	setTipoDeal(TIPO_DEAL_TRASPASO);
    }

    /**
     * Regresa el valor de TipoTraspasoProducto.
     *
     * @hibernate.many-to-one column="MNEMONICO_TRASPASO"
     * cascade="none"
     * class="com.ixe.ods.sica.model.TipoTraspasoProducto"
     * outer-join="auto"
     * unique="false"
     * @return TipoTraspasoProducto.
     */
    public TipoTraspasoProducto getTipoTraspasoProducto() {
        return tipoTraspasoProducto;
    }

    /**
     * Fija el valor de tipoTraspasoProducto.
     *
     * @param tipoTraspasoProducto El valor a asignar.
     */
    public void setTipoTraspasoProducto(TipoTraspasoProducto tipoTraspasoProducto) {
        this.tipoTraspasoProducto = tipoTraspasoProducto;
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
        return canal;
    }

    /**
     * Fija el valor de canal.
     *
     * @param canal El valor a asignar.
     */
    public void setCanal(Canal canal) {
        this.canal = canal;
    }
    
    /**
     * Regresa el valor de fechaOper.
     *
     * @hibernate.property column="FECHA_OPER"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaOper() {
        return fechaOper;
    }

    /**
     * Fija el valor de fechaOper.
     *
     * @param fechaOper El valor a asignar.
     */
    public void setFechaOper(Date fechaOper) {
        this.fechaOper = fechaOper;
    }
    
    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof TraspasoProducto)) {
            return false;
        }
        TraspasoProducto castOther = (TraspasoProducto) other;
        return new EqualsBuilder().append(this.getIdDealPosicion(), castOther.getIdDealPosicion()).isEquals();
    }
    
    //Variables de Instancia
    /**
     * Relacion muchos a uno con TipoTraspasoProducto.
     */
    private TipoTraspasoProducto tipoTraspasoProducto;
    
    /**
     * Relacion muchos a uno con Canal.
     */
    private Canal canal;
    
    /**
     * La fecha de operacion del Traspaso.
     */
    private Date fechaOper;
    
}

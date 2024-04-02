/*
 * $Id: RenglonPizarron.java,v 1.8.40.1 2011/04/26 00:54:54 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ixe.ods.sica.Redondeador;

/**
 * Clase persistente para la tabla SC_RENGLON_PIZARRON. Contiene todos los datos necesarios de un
 * rengl&oacute;n del pizarr&oacute;n para un canal.
 *
 * @hibernate.class table="SC_RENGLON_PIZARRON"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.RenglonPizarron"
 * dynamic-update="true"
 *
 * @hibernate.query name="findRenglonesPizarronTipoPizarron"
 * query="FROM RenglonPizarron AS rp WHERE rp.idTipoPizarron = ?"
 *
 * @hibernate.query name="findRenglonPizarronByTipoPizarronDivisaProducto"
 * query="FROM RenglonPizarron AS rp WHERE rp.idTipoPizarron = ? AND rp.idDivisa = ? AND
 * rp.claveFormaLiquidacion = ?"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.8.40.1 $ $Date: 2011/04/26 00:54:54 $
 */
public class RenglonPizarron implements Serializable {

    /**
     * Constructor vac&iacute;o.
     */
    public RenglonPizarron() {
        super();
    }

    /**
     * Regresa el valor de idRenglonPizarron.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_RENGLON_PIZARRON"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_RENGLON_PIZARRON_SEQ"
     * @return int.
     */
    public int getIdRenglonPizarron() {
        return idRenglonPizarron;
    }

    /**
     * Establece el valor de idRenglonPizarron.
     *
     * @param idRenglonPizarron El valor a asignar.
     */
    public void setIdRenglonPizarron(int idRenglonPizarron) {
        this.idRenglonPizarron = idRenglonPizarron;
    }

    /**
     * Regresa el valor de fecha.
     *
     * @hibernate.property column="FECHA"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece el valor de fecha.
     *
     * @param fecha El valor a asignar.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Regresa el valor de idDivisa.
     *
     * @hibernate.property column="ID_DIVISA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getIdDivisa() {
        return idDivisa;
    }

    /**
     * Establece el valor de idDivisa.
     *
     * @param idDivisa El valor a asignar.
     */
    public void setIdDivisa(String idDivisa) {
        this.idDivisa = idDivisa;
    }

    /**
     * Regresa el valor de idPrecioReferencia.
     *
     * @deprecated Se debe utilizar el valor directo del Precio Referencia
     * @hibernate.property column="ID_PRECIO_REFERENCIA"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getIdPrecioReferencia() {
        return idPrecioReferencia;
    }

    /**
     * Establece el valor de idPrecioReferencia.
     *
     * @deprecated Se debe utilizar el valor directo del Precio Referencia
     * @param idPrecioReferencia El valor a asignar.
     */
    public void setIdPrecioReferencia(int idPrecioReferencia) {
        this.idPrecioReferencia = idPrecioReferencia;
    }
    
    /**
     * Regresa el valor de precioReferenciaMidSpot.
     * 
     * @hibernate.property column="PRE_REF_MID_SPOT"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getPrecioReferenciaMidSpot() {
        return precioReferenciaMidSpot;
    }

    /**
     * Fija el valor de precioReferenciaMidSpot.
     *
     * @param precioReferenciaMidSpot El valor a asignar.
     */
    public void setPrecioReferenciaMidSpot(Double precioReferenciaMidSpot) {
        this.precioReferenciaMidSpot = precioReferenciaMidSpot;
    }
    
    /**
     * Regresa el valor de precioReferenciaSpot.
     * 
     * @hibernate.property column="PRE_REF_SPOT"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getPrecioReferenciaSpot() {
        return precioReferenciaSpot;
    }

    /**
     * Fija el valor de precioReferenciaSpot.
     *
     * @param precioReferenciaSpot El valor a asignar.
     */
    public void setPrecioReferenciaSpot(Double precioReferenciaSpot) {
        this.precioReferenciaSpot = precioReferenciaSpot;
    }

    /**
     * Regresa el valor de idSpread.
     *
     * @hibernate.property column="ID_SPREAD"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getIdSpread() {
        return idSpread;
    }

    /**
     * Establece el valor de idSpread.
     *
     * @param idSpread El valor a asignar.
     */
    public void setIdSpread(int idSpread) {
        this.idSpread = idSpread;
    }

    /**
     * Regresa el valor de idFactorDivisa.
     *
     * @deprecated Se debe utilizar el valor directo del Factor Divisa.
     * @hibernate.property column="ID_FACTOR_DIVISA"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getIdFactorDivisa() {
        return idFactorDivisa;
    }

    /**
     * Establece el valor de idFactorDivisa.
     *
     * @deprecated Se debe utilizar el valor directo del Factor Divisa.
     * @param idFactorDivisa El valor a asignar.
     */
    public void setIdFactorDivisa(int idFactorDivisa) {
        this.idFactorDivisa = idFactorDivisa;
    }

    /**
     * Regresa el valor de factorDivisa.
     *
     * @hibernate.property column="FACTOR_DIVISA"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getFactorDivisa() {
        return factorDivisa;
    }

    /**
     * Establece el valor de factorDivisa.
     *
     * @param factorDivisa El valor a asignar.
     */
    public void setFactorDivisa(Double factorDivisa) {
        this.factorDivisa = factorDivisa;
    }

    /**
     * Regresa el valor de claveFormaLiquidacion.
     *
     * @hibernate.property column="CLAVE_FORMA_LIQUIDACION"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getClaveFormaLiquidacion() {
        return claveFormaLiquidacion;
    }

    /**
     * Establece el valor de claveFormaLiquidacion.
     *
     * @param claveFormaLiquidacion El valor a asignar.
     */
    public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
        this.claveFormaLiquidacion = claveFormaLiquidacion;
    }

    /**
     * Regresa el valor de nombreFormaLiquidacion.
     *
     * @hibernate.property column="NOMBRE_FORMA_LIQUIDACION"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getNombreFormaLiquidacion() {
        return nombreFormaLiquidacion;
    }

    /**
     * Establece el valor de nombreFormaLiquidacion.
     *
     * @param nombreFormaLiquidacion El valor a asignar.
     */
    public void setNombreFormaLiquidacion(String nombreFormaLiquidacion) {
        this.nombreFormaLiquidacion = nombreFormaLiquidacion;
    }

    /**
     * Regresa el valor de compraCash.
     *
     * @hibernate.property column="COMPRA_CASH"
     * not-null="true"
     * unique="false"
     * @return Double.
     */
    public Double getCompraCash() {
        return compraCash != null
                ? new Double(Redondeador.redondear6Dec(compraCash.doubleValue())) : null;
    }

    /**
     * Establece el valor de compraCash.
     *
     * @param compraCash El valor a asignar.
     */
    public void setCompraCash(Double compraCash) {
        this.compraCash = compraCash;
    }

    /**
     * Regresa el valor de ventaCash.
     *
     * @hibernate.property column="VENTA_CASH"
     * not-null="true"
     * unique="false"
     * @return Double.
     */
    public Double getVentaCash() {
        return ventaCash != null
                ? new Double(Redondeador.redondear6Dec(ventaCash.doubleValue())) : null;
    }

    /**
     * Establece el valor de ventaCash.
     *
     * @param ventaCash El valor a asignar.
     */
    public void setVentaCash(Double ventaCash) {
        this.ventaCash = ventaCash;
    }

    /**
     * Regresa el valor de compraTom.
     *
     * @hibernate.property column="COMPRA_TOM"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getCompraTom() {
        return compraTom != null
                ? new Double(Redondeador.redondear6Dec(compraTom.doubleValue())) : null;
    }

    /**
     * Establece el valor de compraTom.
     *
     * @param compraTom El valor a asignar.
     */
    public void setCompraTom(Double compraTom) {
        this.compraTom = compraTom;
    }

    /**
     * Regresa el valor de ventaTom.
     *
     * @hibernate.property column="VENTA_TOM"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getVentaTom() {
        return ventaTom != null
                ? new Double(Redondeador.redondear6Dec(ventaTom.doubleValue())) : null;
    }

    /**
     * Establece el valor de ventaTom.
     *
     * @param ventaTom El valor a asignar.
     */
    public void setVentaTom(Double ventaTom) {
        this.ventaTom = ventaTom;
    }

    /**
     * Regresa el valor de compraSpot.
     *
     * @hibernate.property column="COMPRA_SPOT"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getCompraSpot() {
        return compraSpot != null
                ? new Double(Redondeador.redondear6Dec(compraSpot.doubleValue())) : null;
    }

    /**
     * Establece el valor de compraSpot.
     *
     * @param compraSpot El valor a asignar.
     */
    public void setCompraSpot(Double compraSpot) {
        this.compraSpot = compraSpot;
    }

    /**
     * Regresa el valor de ventaSpot.
     *
     * @hibernate.property column="VENTA_SPOT"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getVentaSpot() {
        return ventaSpot != null
                ? new Double(Redondeador.redondear6Dec(ventaSpot.doubleValue())) : null;
    }

    /**
     * Establece el valor de ventaSpot.
     *
     * @param ventaSpot El valor a asignar.
     */
    public void setVentaSpot(Double ventaSpot) {
        this.ventaSpot = ventaSpot;
    }

    /**
     * Regresa el valor de compra72Hr.
     *
     * @hibernate.property column="COMPRA_72HR"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getCompra72Hr() {
        return compra72Hr != null
                ? new Double(Redondeador.redondear6Dec(compra72Hr.doubleValue())) : null;
    }

    /**
     * Establece el valor de compra72Hr.
     *
     * @param compra72Hr El valor a asignar.
     */
    public void setCompra72Hr(Double compra72Hr) {
        this.compra72Hr = compra72Hr;
    }

    /**
     * Regresa el valor de venta72Hr.
     *
     * @hibernate.property column="VENTA_72HR"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getVenta72Hr() {
        return venta72Hr != null
                ? new Double(Redondeador.redondear6Dec(venta72Hr.doubleValue())) : null;
    }

    /**
     * Establece el valor de venta72Hr.
     *
     * @param venta72Hr El valor a asignar.
     */
    public void setVenta72Hr(Double venta72Hr) {
        this.venta72Hr = venta72Hr;
    }

    /**
     * Regresa el valor de compraVFut.
     *
     * @hibernate.property column="COMPRA_VFUT"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getCompraVFut() {
        return compraVFut != null
                ? new Double(Redondeador.redondear6Dec(compraVFut.doubleValue())) : null;
    }

    /**
     * Establece el valor de compraVFut.
     *
     * @param compraVFut El valor a asignar.
     */
    public void setCompraVFut(Double compraVFut) {
        this.compraVFut = compraVFut;
    }

    /**
     * Regresa el valor de ventaVFut.
     *
     * @hibernate.property column="VENTA_VFUT"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getVentaVFut() {
        return ventaVFut != null
                ? new Double(Redondeador.redondear6Dec(ventaVFut.doubleValue())) : null;
    }

    /**
     * Establece el valor de ventaVFut.
     *
     * @param ventaVFut El valor a asignar.
     */
    public void setVentaVFut(Double ventaVFut) {
        this.ventaVFut = ventaVFut;
    }

    /**
     * Obtiene el tipo de Pizarron para el spread.
     *
     * @hibernate.property column="ID_TIPO_PIZARRON"
     * unique="false"
     * @return Integer
     */
    public Integer getIdTipoPizarron() {
    	return idTipoPizarron;
    }

   /**
    * Asigna el valor para tipoPizarron.
    *
    * @param idTipoPizarron El valor para tipoPizarron.
    */
	public void setIdTipoPizarron(Integer idTipoPizarron) {
		this.idTipoPizarron = idTipoPizarron;
	}

    /**
     * Regresa el valor de maximoCompraCash.
     *
     * @return Double.
     */
    public Double getMaximoCompraCash() {
        return maximoCompraCash;
    }

    /**
     * Establece el valor de maximoCompraCash.
     *
     * @param maximoCompraCash El valor a asignar.
     */
    public void setMaximoCompraCash(Double maximoCompraCash) {
        this.maximoCompraCash = maximoCompraCash;
    }

    /**
     * Regresa el valor de minimoVentaCash.
     *
     * @return Double.
     */
    public Double getMinimoVentaCash() {
        return minimoVentaCash;
    }

    /**
     * Establece el valor de minimoVentaCash.
     *
     * @param minimoVentaCash El valor a asignar.
     */
    public void setMinimoVentaCash(Double minimoVentaCash) {
        this.minimoVentaCash = minimoVentaCash;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof RenglonPizarron)) {
            return false;
        }
        RenglonPizarron castOther = (RenglonPizarron) other;
        return new org.apache.commons.lang.builder.EqualsBuilder().
                append(this.getIdRenglonPizarron(), castOther.getIdRenglonPizarron()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder().
                append(getIdRenglonPizarron()).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("idRenglonPizarron", idRenglonPizarron).
                append("fecha", fecha).append("idDivisa", idDivisa).
                append("precioReferenciaMidSpot", precioReferenciaMidSpot).
                append("precioReferenciaSpot", precioReferenciaSpot).
                append("idSpread", idSpread).append("factorDivisa", factorDivisa).toString();
    }

    /**
     * La llave primaria del registro.
     */
    private int idRenglonPizarron;

    /**
     * La fecha de creaci&oacute;n del registro.
     */
    private Date fecha = new Date();

    /**
     * El identificador de la divisa a la que pertenece este rengl&oacute;n.
     */
    private String idDivisa;

    /**
     * El identificador del precio de referencia actual.
     * 
     * @deprecated Se debe utilizar el valor directo del Precio Referencia.
     */
    private int idPrecioReferencia;
    
    /**
     * El precio referencia Mid Spot utilizado para generar el 
     * rengl&oacute;n
     */
    private Double precioReferenciaMidSpot;
    
    /**
     * El precio referencia Spot utilizado para generar el 
     * rengl&oacute;n
     */
    private Double precioReferenciaSpot;

    /**
     * El identificador del spread actual utilizado para este rengl&oacute;n.
     */
    private int idSpread;

    /**
     * El identificador del factor de divisa utilizado para este rengl&oacute;n.
     * @deprecated Se debe utilizar el valor directo del Factor Divisa.
     */
    private int idFactorDivisa;

    /**
     * El valor del factor de divisa de este rengl&oacute;n.
     */
    private Double factorDivisa;

    /**
     * La clave del producto.
     */
    private String claveFormaLiquidacion;

    /**
     * La descripci&oacute;n del producto.
     */
    private String nombreFormaLiquidacion;

    /**
     * El valor de la compra cash.
     */
    private Double compraCash;

    /**
     * El valor de la venta cash.
     */
    private Double ventaCash;

    /**
     * El valor de la compra compra Tom.
     */
    private Double compraTom;

    /**
     * El valor de la venta Tom.
     */
    private Double ventaTom;

    /**
     * El valor de la compra Spot.
     */
    private Double compraSpot;

    /**
     * El valor de la venta Spot.
     */
    private Double ventaSpot;

    /**
     * El valor de la compra a 72Hr.
     */
    private Double compra72Hr;

    /**
     * El valor de la venta a 72Hr.
     */
    private Double venta72Hr;

    /**
     * El valor de la compra a Valor Futuro.
     */
    private Double compraVFut;

    /**
     * El valor de la venta a Valor Futuro.
     */
    private Double ventaVFut;

    /**
     * El id del tipo de pizarron para el spread.
     */
    private Integer idTipoPizarron;

    /**
     * El valor del m&aacute;ximo de cash en Cash para sucursales exclusivamente.
     */
    private Double maximoCompraCash = new Double(0);

    /**
     * El valor del m&iacute;nimo de venta en Cash para sucursales exclusivamente.
     */
    private Double minimoVentaCash = new Double(0);

    /**
     * El UID de serializaci&oacute;n de esta clase.
     */
    private static final long serialVersionUID = 4298492822797307053L;
}

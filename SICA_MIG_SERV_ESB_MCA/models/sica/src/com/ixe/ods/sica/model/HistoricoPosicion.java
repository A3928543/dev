/*
 * $Id: HistoricoPosicion.java,v 1.13 2008/04/16 22:53:37 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * <p>Clase persistente para la tabla SC_H_POSICION, donde se almacena el
 * hist&oacute;rico de los cambios del objeto Posici&oacute;n. </p>.
 *
 * @hibernate.class table="SC_H_POSICION"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoPosicion"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.13 $ $Date: 2008/04/16 22:53:37 $
 */

public class HistoricoPosicion implements Serializable {

    /**
    * Constructor por default. No hace nada.
    */
    public HistoricoPosicion() {
        super();
    }

    /**
    * Inicializa el objeto recibiendo como par&aacute;metro posici&oacute;n
    *
    * @param posicion La posici&oacute;n que inicializa el hist&oacute;rico.
    */
    public HistoricoPosicion(Posicion posicion) {
        setCpaVta(posicion.getCpaVta());
        setCpaVtaIn(posicion.getCpaVtaIn());
        setCpaVtaMn(posicion.getCpaVtaMn());
        //setDetalles(posicion.getDetalles());
        setDivisa(posicion.getDivisa());
        setMesaCambio(posicion.getMesaCambio());
        //72 hr y vfut dentro de aqui.
        setPosIni(posicion.getPosIni());
        setUtilidadGlobal(posicion.getUtilidadGlobal());
        setUtilidadMesa(posicion.getUtilidadMesa());
        setUltimaModificacion(new Date());
    }

    /**
     * Calcula el monto de la posicion final para una divisa.
     * 
     * @return double
     */
    public double getPosicionFinal() {
    	double posicionFinal = 0;
    	posicionFinal += getPosIni().getPosicionInicial();
    	posicionFinal += getCpaVta().getCash();
    	posicionFinal += getCpaVta().getTom();
    	posicionFinal += getCpaVta().getSpot();
    	posicionFinal += getCpaVta().get72Hr();
    	posicionFinal += getCpaVta().getVFut();
    	return posicionFinal;
    }
    
    /**
     * Calcula el monto de la posicion final de la mesa para una divisa en Pesos.
     *
     * @return double
     */
    public double getPosicionFinalMnMesa() {
    	double posicionFinal = 0;
    	posicionFinal += getPosIni().getPosicionInicialMn();
    	posicionFinal += getCpaVtaMn().getCompraMnMesaCash() - getCpaVtaMn().getVentaMnMesaCash();
    	posicionFinal += getCpaVtaMn().getCompraMnMesaTom() - getCpaVtaMn().getCompraMnMesaTom();
    	posicionFinal += getCpaVtaMn().getCompraMnMesaSpot() - getCpaVtaMn().getCompraMnMesaSpot();
    	posicionFinal += getCpaVtaMn().getCompraMnMesa72Hr().doubleValue() - 
    		getCpaVtaMn().getCompraMnMesa72Hr().doubleValue();
    	posicionFinal += getCpaVtaMn().getCompraMnMesaVFut().doubleValue() -
    		getCpaVtaMn().getCompraMnMesaVFut().doubleValue();
    	return posicionFinal;
    }
    
    /**
     * Calcula el monto del tipo de cambio promedio para la Posicion 
     * de la siguiente manera:<br>
     * 
     * <li>Si la posicion final es Larga (positiva) y la posicion inicial en Divisa es mayor que
     * cero, el tipo de cambio promedio es igual a: <code>(totalComprasMn + posicionInicialMn) /
     * (totalComprasDiv + posicionInicialDivisa)</code>.<br>
     * Si la posicion incial en divisa es menor que cero, el tipo de cambio promedio es igual al
     * tipo de cambio para compras.
     * </li>
     * 
     * <li>Si la posicion final es Corta (negativa) y la posicion inicial en Divisa es mayor que
     * cero, el tipo de cambio promedio es igual al tipo de cambio para Ventas. Si la posicion
     * inicial en divisa el menor que cero, el tipo de cambio promedio es igual a:
     * <code>(totalVentasMn - posicionInicialMn) / (totalventasDiv - posicionInicialDivisa)</code>.
     * </li>
     *
     * @return double.
     */
    public double getTipoCambioPromedio() {
        double tipoCambioPromedio;
        double tipoCambioCompra = getCpaVta().getTotalCompras() != 0 ?
                getCpaVtaMn().getTotalComprasMnMesa() / getCpaVta().getTotalCompras() : 0;
        double tipoCambioventa = getCpaVta().getTotalVentas() != 0 ?
                getCpaVtaMn().getTotalVentasMnMesa() / getCpaVta().getTotalVentas() : 0;
        if (getPosicionFinal() > 0) {
            if (getPosIni().getPosicionInicial() > 0) {
                double mn = getCpaVtaMn().getTotalComprasMnMesa() +
                        getPosIni().getPosicionInicialMn();
                double div = getCpaVta().getTotalCompras() + getPosIni().getPosicionInicial();
                tipoCambioPromedio = mn / div;
            }
            else {
                tipoCambioPromedio = tipoCambioCompra;
            }
        }
        else {
            if (getPosIni().getPosicionInicial() > 0) {
                tipoCambioPromedio = tipoCambioventa;
            }
            else {
                double mn = getCpaVtaMn().getTotalVentasMnMesa() -
                        getPosIni().getPosicionInicialMn();
                double div = getCpaVta().getTotalVentas() - getPosIni().getPosicionInicial();
                tipoCambioPromedio = mn / div;
            }
        }
        return tipoCambioPromedio;
    }

    /**
     * Regresa el Tipo de Cambio Promedio Ponderdado con respecto al Cliente.
     *
     * @return double.
     */
    public double getTipoCambioPromedioPonderadoCliente() {
        double compras = getCpaVta().getTotalCompras();
        double ventas = getCpaVta().getTotalVentas();
        double comprasMn = getCpaVtaMn().getTotalComprasMnCliente();
        double ventasMn = getCpaVtaMn().getTotalVentasMnCliente();
        double posicionInicialMn  = getPosIni().getPosicionInicialMn();
        double tipoCambioCompra = compras == 0 ? 0 : comprasMn / compras;
        double tipoCambioVenta = ventas == 0 ? 0 : ventasMn / ventas;
        double posicionInicial  = getPosIni().getPosicionInicial();
        return getPosicionFinal() > 0 ? (posicionInicial > 0 ?
                (comprasMn + posicionInicialMn) / (compras + posicionInicial) : tipoCambioCompra) :
                (posicionInicial > 0 ? tipoCambioVenta :
                        (ventasMn - posicionInicialMn) / (ventas - posicionInicial));
    }

    /**
     * Regresa el Tipo de Cambio Promedio Ponderdado con respecto a la Mesa.
     *
     * @return double.
     */
    public double getTipoCambioPromedioPonderadoMesa() {
        double compras = getCpaVta().getTotalCompras();
        double ventas = getCpaVta().getTotalVentas();
        double comprasMn = getCpaVtaMn().getTotalComprasMnMesa();
        double ventasMn = getCpaVtaMn().getTotalVentasMnMesa();
        double posicionInicialMn  = getPosIni().getPosicionInicialMn();
        double tipoCambioCompra = compras == 0 ? 0 : comprasMn / compras;
        double tipoCambioVenta = ventas == 0 ? 0 : ventasMn / ventas;
        double posicionInicial  = getPosIni().getPosicionInicial();
        return getPosicionFinal() > 0 ? (posicionInicial > 0 ?
                (comprasMn + posicionInicialMn) / (compras + posicionInicial) : tipoCambioCompra) :
                (posicionInicial > 0 ? tipoCambioVenta :
                        (ventasMn - posicionInicialMn) / (ventas - posicionInicial));
    }

    /**
     * Regresa el Tipo de Cambio Promedio Ponderdado con respecto al Pizarr&oacute;n.
     *
     * @return double.
     */
    public double getTipoCambioPromedioPonderadoPizarron() {
        double compras = getCpaVta().getTotalCompras();
        double ventas = getCpaVta().getTotalVentas();
        double comprasMn = getCpaVtaMn().getTotalComprasMnPizarron();
        double ventasMn = getCpaVtaMn().getTotalVentasMnPizarron();
        double posicionInicialMn  = getPosIni().getPosicionInicialMn();
        double tipoCambioCompra = compras == 0 ? 0 : comprasMn / compras;
        double tipoCambioVenta = ventas == 0 ? 0 : ventasMn / ventas;
        double posicionInicial  = getPosIni().getPosicionInicial();
        return getPosicionFinal() > 0 ? (posicionInicial > 0 ?
                (comprasMn + posicionInicialMn) / (compras + posicionInicial) : tipoCambioCompra) :
                (posicionInicial > 0 ? tipoCambioVenta :
                        (ventasMn - posicionInicialMn) / (ventas - posicionInicial));
    }


    
    /**
     * Regresa el valor de idPosicion.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_POSICION"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_POSICION_SEQ"
     * @return int.
     */
    public int getIdPosicion() {
        return idPosicion;
    }

    /**
     * Fija el valor de idPosicion.
     *
     * @param idPosicion El valor a asignar.
     */
    public void setIdPosicion(int idPosicion) {
        this.idPosicion = idPosicion;
    }

    /**
     * Regresa el valor de utilidadGlobal.
     *
     * @hibernate.property column="UTILIDAD_GLOBAL"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getUtilidadGlobal() {
        return utilidadGlobal;
    }

    /**
     * Fija el valor de utilidadGlobal.
     *
     * @param utilidadGlobal El valor a asignar.
     */
    public void setUtilidadGlobal(double utilidadGlobal) {
        this.utilidadGlobal = utilidadGlobal;
    }

    /**
     * Regresa el valor de utilidadMesa.
     *
     * @hibernate.property column="UTILIDAD_MESA"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getUtilidadMesa() {
        return utilidadMesa;
    }

    /**
     * Fija el valor de utilidadMesa.
     *
     * @param utilidadMesa El valor a asignar.
     */
    public void setUtilidadMesa(double utilidadMesa) {
        this.utilidadMesa = utilidadMesa;
    }

    /**
     * Regresa el valor de cpaVta.
     *
     * @hibernate.component class="com.ixe.ods.sica.model.CpaVta"
     * @return CpaVta.
     */
    public CpaVta getCpaVta() {
        return _cpaVta;
    }

    /**
     * Fija el valor de cpaVta.
     *
     * @param cpaVta El valor a asignar.
     */
    public void setCpaVta(CpaVta cpaVta) {
        _cpaVta = cpaVta;
    }

    /**
     * Regresa el valor de cpaVtaIn.
     *
     * @hibernate.component class="com.ixe.ods.sica.model.CpaVtaIn"
     * @return CpaVta.
     */
    public CpaVtaIn getCpaVtaIn() {
        return _cpaVtaIn;
    }

    /**
     * Fija el valor de cpaVtaIn.
     *
     * @param cpaVtaIn El valor a asignar.
     */
    public void setCpaVtaIn(CpaVtaIn cpaVtaIn) {
        _cpaVtaIn = cpaVtaIn;
    }

    /**
     * Regresa el valor de cpaVtaMn.
     *
     * @hibernate.component class="com.ixe.ods.sica.model.CpaVtaMn"
     * @return CpaVtaMn.
     */
    public CpaVtaMn getCpaVtaMn() {
        return _cpaVtaMn;
    }

    /**
     * Fija el valor de cpaVtaMn.
     *
     * @param cpaVtaMn El valor a asignar.
     */
    public void setCpaVtaMn(CpaVtaMn cpaVtaMn) {
        this._cpaVtaMn = cpaVtaMn;
    }

    /**
     * Regresa el valor de posIni.
     *
     * @hibernate.component class="com.ixe.ods.sica.model.PosIni"
     * @return PosIni.
     */
    public PosIni getPosIni() {
        return _posIni;
    }

    /**
     * Fija el valor de posIni.
     *
     * @param posIni El valor a asignar.
     */
    public void setPosIni(PosIni posIni) {
        _posIni = posIni;
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
     * Establece el valor de ultimaModificacion.
     *
     * @param ultimaModificacion El valor a asignar.
     */
    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    /**
     * Regresa el valor de divisa.
     *
     * @hibernate.many-to-one column="ID_DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return Divisa.
     */
    public Divisa getDivisa() {
        return _divisa;
    }

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        _divisa = divisa;
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
     * Regresa el valor de detalles.
     *
     * @hibernate.set inverse="false"
     * lazy="true"
     * cascade="none"
     * @hibernate.collection-key column="ID_POSICION"
     * @hibernate.collection-one-to-many class="com.ixe.ods.sica.model.HistoricoPosicionDetalle"
     * @return Set.
     */
    public Set getDetalles() {
        return _detalles;
    }

    /**
     * Fija el valor de detalles.
     *
     * @param detalles El valor a asignar.
     */
    public void setDetalles(Set detalles) {
        _detalles = detalles;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof HistoricoPosicion)) {
            return false;
        }
        HistoricoPosicion castOther = (HistoricoPosicion) other;
        return new EqualsBuilder().append(this.getIdPosicion(),
                castOther.getIdPosicion()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdPosicion()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private int idPosicion;

    /**
     * Utilidad de la utilidadGlobal.
     */
    private double utilidadGlobal;

    /**
     * Utilidad de la utilidadMesa.
     */
    private double utilidadMesa;

    /**
     * La fecha en que se escribi&oacute; este registro hist&oacute;rico.
     */
    private Date ultimaModificacion;

    /**
     * Objeto que almacena las propiedades de compra venta.
     */
    private CpaVta _cpaVta = new CpaVta();

    /**
     * Objeto que almacena las propiedades de compra venta para interbancario.
     */
    private CpaVtaIn _cpaVtaIn = new CpaVtaIn();

    /**
     * El componente CpaVtaMn, con los totales de la posici&oacute;n por
     * fecha valor.
     */
    private CpaVtaMn _cpaVtaMn = new CpaVtaMn();

    /**
     * El componente PosIni, con los valores de la posici&oacute;n inicial.
     */
    private PosIni _posIni = new PosIni();

    /**
     * Relaci&oacute;n muchos-a-uno con Divisa.
     */
    private Divisa _divisa;

    /**
     * Relaci&oacute;n muchos-a-uno con MesaCambio.
     */
    private MesaCambio _mesaCambio;

    /**
     * Relaci&oacute;n uno-a-muchos con PosicionDetalle.
     */
    private Set _detalles = new HashSet();

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -6862797928154160712L;
}

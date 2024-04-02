/*
 * $Id: Posicion.java,v 1.19 2009/09/08 18:15:55 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_POSICION. Almacena el resumen total de la
 * posici&oacute;n.
 *
 * @hibernate.class table="SC_POSICION"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Posicion"
 * dynamic-update="true"
 *
 * @hibernate.query name="findPosicionByIdMesaCambioAndIdDivisa"
 * query="FROM Posicion AS p INNER JOIN FETCH p.divisa div WHERE div.tipo = 'F' AND
 * p.mesaCambio.idMesaCambio = ? AND
 * p.divisa.idDivisa = ?"
 *
 * @hibernate.query name="findPosicionByIdMesaCambio"
 * query="FROM Posicion AS p INNER JOIN FETCH p.divisa div WHERE div.tipo = 'F' AND
 * p.mesaCambio.idMesaCambio = ?"
 *
 * @hibernate.query name="findPosicionBanxico"
 * query="FROM Posicion AS p WHERE p.divisa.idDivisa = 'USD'"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.19 $ $Date: 2009/09/08 18:15:55 $
 */
public class Posicion implements Serializable {

    /**
     * Constructor vac&iacute;o. No hace nada.
     */
    public Posicion() {
        super();
    }

    /**
     * Inicializa Posicion con Divisa y Mesa de Cambio.
     *
     * @param divisa La divisa.
     * @param mesaCambio La mesa de cambio.
     */
    public Posicion(Divisa divisa, MesaCambio mesaCambio) {
        this();
        setDivisa(divisa);
        setMesaCambio(mesaCambio);
    }

    /**
     * Constructor por default. Inicializa todas las variables de instancia
     * excepto idPosicion.
     *
     * @param cpaVta El componente CpaVta.
     * @param cpaVtaMn El componente CpaVtaMn.
     * @param posIni El componente PosIni.
     * @param divisa La divisa.
     * @param mesaCambio La mesa de cambio.
     */
    public Posicion(CpaVta cpaVta, CpaVtaMn cpaVtaMn, PosIni posIni, Divisa divisa,
                    MesaCambio mesaCambio) {
        _cpaVta = cpaVta;
        _cpaVtaMn = cpaVtaMn;
        _posIni = posIni;
        _divisa = divisa;
        _mesaCambio = mesaCambio;
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
        double tcppc =  getPosicionFinal() > 0 ? (posicionInicial > 0 ?
                (comprasMn + posicionInicialMn) / (compras + posicionInicial) : tipoCambioCompra) :
                (posicionInicial > 0 ? tipoCambioVenta :
                        (ventasMn - posicionInicialMn) / (ventas - posicionInicial));
        return !Double.isNaN(tcppc) && !Double.isInfinite(tcppc) ? tcppc : 0;
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
        double tcppm = getPosicionFinal() > 0 ? (posicionInicial > 0 ?
                (comprasMn + posicionInicialMn) / (compras + posicionInicial) : tipoCambioCompra) :
                (posicionInicial > 0 ? tipoCambioVenta :
                        (ventasMn - posicionInicialMn) / (ventas - posicionInicial));
        return !Double.isNaN(tcppm) && !Double.isInfinite(tcppm) ? tcppm : 0;        
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
        double tcppp = getPosicionFinal() > 0 ? (posicionInicial > 0 ?
                (comprasMn + posicionInicialMn) / (compras + posicionInicial) : tipoCambioCompra) :
                (posicionInicial > 0 ? tipoCambioVenta :
                        (ventasMn - posicionInicialMn) / (ventas - posicionInicial));
        return !Double.isNaN(tcppp) && !Double.isInfinite(tcppp) ? tcppp : 0;        
    }

    /**
     * Regresa el valor del Stop Loss mediante su factor de Conversi&oacuteln y sus precio de
     * referencia.
     *
     * @param factorDeConversion El valor del factor de conversi&oacute;n.
     * @param precioReferencia El precio de referencia de la divisa.
     * @return double.
     */
    public double getStopLoss(double factorDeConversion, double precioReferencia) {
    	double tipoCambioMercado = factorDeConversion * precioReferencia;
        return (tipoCambioMercado - getTipoCambioPromedioPonderadoCliente()) * getPosicionFinal();
    }

    /**
     * Regresa el valor del Stop Loss total de Mesa mediante su factor de Conversi&oacuteln y sus
     * precio de referencia
     *
     * @param factorDeConversion El valor del factor de conversi&oacute;n.
     * @param precioReferencia El precio de referencia de la divisa.
     * @return double.
     */
    public double getStopLossMesa(double factorDeConversion, double precioReferencia) {
    	double tipoCambioMercado = factorDeConversion * precioReferencia;
        return getStopLoss(factorDeConversion, precioReferencia) / tipoCambioMercado;
    }

    /**
     * Regresa el valor de la Posici&oacute;n Global que cambia dependiendo si la Divisa es Pesos o
     * no.
     *
     * @param idDivisa El id de la divisa.
     * @param posicionGlobal El monto de la posici&oacute;n Global.
     * @param precioRefSpot El valor del precio de referencia para la fecha valor Spot.
     * @return double.
     */
    public static double findDivisaReferenciaParaMesa(String idDivisa, double posicionGlobal,
                                                      double precioRefSpot) {
        if ((Divisa.PESO).equals(idDivisa)) {
            return posicionGlobal /= precioRefSpot;
        }
        return posicionGlobal;
    }

    /**
     * Cambia el valor de la Posici&oacute;n a Dolares.
     *
     * @param posicionGlobal El monto de la posici&oacute;n Global.
     * @param precioReferencia El precio de referencia de la divisa.
     * @param factorDeConversion El valor del factor de conversi&oacute;n.
     *
     * @return double.
     */
    public double dolarizarPosicion(double posicionGlobal, double precioReferencia,
                                    double factorDeConversion) {
        return posicionGlobal /= precioReferencia * factorDeConversion;
    }

    /**
     * Calcula el monto de la posicion final para una divisa.
     *
     * @return double.
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
     * Calcula el monto de la posicion final de la mesa
     * para una divisa en Pesos.
     *
     * @return double
     */
    public double getPosicionFinalMnMesa() {
    	double posicionFinal = 0;
    	posicionFinal += getPosIni().getPosicionInicialMn();
    	posicionFinal += getCpaVtaMn().getCompraMnMesaCash() - getCpaVtaMn().getVentaMnMesaCash();
    	posicionFinal += getCpaVtaMn().getCompraMnMesaTom() - getCpaVtaMn().getVentaMnMesaTom();
    	posicionFinal += getCpaVtaMn().getCompraMnMesaSpot() - getCpaVtaMn().getVentaMnMesaSpot();
    	posicionFinal += getCpaVtaMn().getCompraMnMesa72Hr().doubleValue() -
    		getCpaVtaMn().getVentaMnMesa72Hr().doubleValue();
    	posicionFinal += getCpaVtaMn().getCompraMnMesaVFut().doubleValue() -
    		getCpaVtaMn().getVentaMnMesaVFut().doubleValue();
    	return posicionFinal;
    }

    /**
     * Calcula el monto de la posicion final de la mesa
     * para una divisa en Pesos.
     *
     * @return double
     */
    public double getPosicionFinalMnClientePond() {
        return getPosicionFinal() * getTipoCambioPromedioPonderadoCliente();
    }

    public double getPosicionFinalMnMesaPond() {
        return getPosicionFinal() * getTipoCambioPromedioPonderadoMesa();
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
    	double tipoCambioPromedio = 0;
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
     * @hibernate.set inverse="true"
     * lazy="true"
     * cascade="none"
     * @hibernate.collection-key column="ID_POSICION"
     * @hibernate.collection-one-to-many class="com.ixe.ods.sica.model.PosicionDetalle"
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
        if (!(other instanceof Posicion)) {
            return false;
        }
        Posicion castOther = (Posicion) other;
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
    private static final long serialVersionUID = 4126069532426912115L;
}

/*
 * $Id: TotalPosicionDto.java,v 1.1.40.1 2011/04/25 23:48:03 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.dto;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.ixe.ods.sica.model.Posicion;
import com.ixe.ods.sica.model.PrecioReferenciaActual;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.1.40.1 $ $Date: 2011/04/25 23:48:03 $
 */
public class TotalPosicionDto implements Serializable {

    /**
     * Constructor por default.
     */
    public TotalPosicionDto() {
        super();
    }

    /**
     * Constructor para inicializar las variables de instancia de posicion y gactoresDivisaActuales.
     *
     * @param pos El registro de la posici&oacute;n a asignar.
     * @param factoresDivisaActuales La lista de factores divisa vigentes.
     * @param pr El precio de referencia vigente.
     */
    public TotalPosicionDto(Posicion pos, List factoresDivisaActuales, PrecioReferenciaActual pr) {
        this();
        for (Iterator itFd = factoresDivisaActuales.iterator(); itFd.hasNext();) {
            FactorDivisaActual factorDivisa = (FactorDivisaActual) itFd.next();
            if (factorDivisa.getFacDiv().getFromDivisa().getIdDivisa().equals(
                    pos.getDivisa().getIdDivisa())) {
                setFactorDivisa(factorDivisa.getFacDiv().getFactor());
            }
        }
        posicion = pos;
        setDivisa(pos.getDivisa());
        setPosicionInicial(pos.getPosIni().getPosicionInicial());
        setCash(pos.getCpaVta().getCash());
        setTom(pos.getCpaVta().getTom());
        setSpot(pos.getCpaVta().getSpot());
        setHr72(pos.getCpaVta().get72Hr());
        setVFut(pos.getCpaVta().getVFut());
        setPosicionFinal(pos.getPosicionFinal());
        if (Divisa.PESO.equals(posicion.getMesaCambio().getDivisaReferencia().getIdDivisa())) {
            setUtilidadMn(pos.getUtilidadGlobal());
        }
        else {
            setUtilidadMn(pos.getUtilidadGlobal() * pr.getPreRef().getMidSpot().doubleValue());
        }
    }

    /**
     * Acumula los valores del dto proporcionado en las variables de este objeto, convirtiendo todos
     * estos a USD.
     *
     * @param dto El dto a sumar.
     */
    public void sumarDolarizado(TotalPosicionDto dto) {
        setPosicionInicial(posicionInicial + (dto.getPosicionInicial() * dto.getFactorDivisa()));
        setCash(cash + (dto.getCash() * dto.getFactorDivisa()));
        setTom(tom + (dto.getTom() * dto.getFactorDivisa()));
        setSpot(spot + (dto.getSpot() * dto.getFactorDivisa()));
        setHr72(hr72 + (dto.getHr72() * dto.getFactorDivisa()));
        setVFut(vFut + (dto.getVFut() * dto.getFactorDivisa()));
        setPosicionFinal(posicionFinal + (dto.getPosicionFinal() * dto.getFactorDivisa()));
        setUtilidadMn(utilidadMn + dto.getUtilidadMn());
    }

    /**
     * Acumula los valores del dto proporcionado en las variables de este objeto.
     *
     * @param dto El dto a sumar.
     */
    public void sumar(TotalPosicionDto dto) {
        if (getDivisa() == null) {
            setDivisa(dto.getDivisa());
        }
        if (getFactorDivisa() == 0.0) {
            setFactorDivisa(dto.getFactorDivisa());
        }
        setPosicionInicial(posicionInicial + dto.getPosicionInicial());
        setCash(cash + dto.getCash());
        setTom(tom + dto.getTom());
        setSpot(spot + dto.getSpot());
        setHr72(hr72 + dto.getHr72());
        setVFut(vFut + dto.getVFut());
        setPosicionFinal(posicionFinal + dto.getPosicionFinal());
        setUtilidadMn(utilidadMn + dto.getUtilidadMn());
    }

    /**
     * Regresa el valor de divisa.
     *
     * @return Divisa.
     */
    public Divisa getDivisa() {
        return divisa;
    }

    /**
     * Establece el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        this.divisa = divisa;
    }

    /**
     * Regresa el valor de factorDivisa.
     *
     * @return double.
     */
    public double getFactorDivisa() {
        return factorDivisa;
    }

    /**
     * Establece el valor de factorDivisa.
     *
     * @param factorDivisa El valor a asignar.
     */
    public void setFactorDivisa(double factorDivisa) {
        this.factorDivisa = factorDivisa;
    }

    /**
     * Regresa el valor de posicionInicial.
     *
     * @return double.
     */
    public double getPosicionInicial() {
        return posicionInicial;
    }

    /**
     * Establece el valor de posicionInicial.
     *
     * @param posicionInicial El valor a asignar.
     */
    public void setPosicionInicial(double posicionInicial) {
        this.posicionInicial = posicionInicial;
    }

    /**
     * Regresa el valor de cash.
     *
     * @return double.
     */
    public double getCash() {
        return cash;
    }

    /**
     * Establece el valor de cash.
     *
     * @param cash El valor a asignar.
     */
    public void setCash(double cash) {
        this.cash = cash;
    }

    /**
     * Regresa el valor de tom.
     *
     * @return double.
     */
    public double getTom() {
        return tom;
    }

    /**
     * Establece el valor de tom.
     *
     * @param tom El valor a asignar.
     */
    public void setTom(double tom) {
        this.tom = tom;
    }

    /**
     * Regresa el valor de spot.
     *
     * @return double.
     */
    public double getSpot() {
        return spot;
    }

    /**
     * Establece el valor de spot.
     *
     * @param spot El valor a asignar.
     */
    public void setSpot(double spot) {
        this.spot = spot;
    }

    /**
     * Regresa el valor de hr72.
     *
     * @return double.
     */
    public double getHr72() {
        return hr72;
    }

    /**
     * Establece el valor de hr72.
     *
     * @param hr72 El valor a asignar.
     */
    public void setHr72(double hr72) {
        this.hr72 = hr72;
    }

    /**
     * Regresa el valor de vFut.
     *
     * @return double.
     */
    public double getVFut() {
        return vFut;
    }

    /**
     * Establece el valor de vFut.
     *
     * @param vFut El valor a asignar.
     */
    public void setVFut(double vFut) {
        this.vFut = vFut;
    }

    /**
     * Regresa el valor de posicionFinal.
     *
     * @return double.
     */
    public double getPosicionFinal() {
        return posicionFinal;
    }

    /**
     * Establece el valor de posicionFinal.
     *
     * @param posicionFinal El valor a asignar.
     */
    public void setPosicionFinal(double posicionFinal) {
        this.posicionFinal = posicionFinal;
    }

    /**
     * Regresa el valor de utilidadMn.
     *
     * @return double.
     */
    public double getUtilidadMn() {
        return utilidadMn;
    }

    /**
     * Establece el valor de utilidadMn.
     *
     * @param utilidadMn El valor a asignar.
     */
    public void setUtilidadMn(double utilidadMn) {
        this.utilidadMn = utilidadMn;
    }

    /**
     * Regresa el valor de posicion.
     *
     * @return double.
     */
    public Posicion getPosicion() {
        return posicion;
    }

    /**
     * El objeto divisa.
     */
    private Divisa divisa;

    /**
     * El factor de conversi&oacute;n contra dolar.
     */
    private double factorDivisa;

    /**
     * El valor de la posici&oacute;n inicial.
     */
    private double posicionInicial;

    /**
     * El nivel de compras / ventas Cash.
     */
    private double cash;

    /**
     * El nivel de compras / ventas Tom.
     */
    private double tom;

    /**
     * El nivel de compras / ventas Spot.
     */
    private double spot;

    /**
     * El nivel de compras / ventas 72Hr.
     */
    private double hr72;

    /**
     * El nivel de compras / ventas 96Hr.
     */
    private double vFut;

    /**
     * El valor de la posici&oacute;n final.
     */
    private double posicionFinal;

    /**
     * El valor de la utilidad global en la divisa de referencia de la mesa.
     */
    private double utilidadMn;

    /**
     * La referencia al registro de posici&oacute;n.
     */
    private Posicion posicion;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 1229196300888806638L;
}

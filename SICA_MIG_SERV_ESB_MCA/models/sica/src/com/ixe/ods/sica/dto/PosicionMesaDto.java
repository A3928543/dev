/*
 * $Id: PosicionMesaDto.java,v 1.1 2008/10/27 23:20:10 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.Posicion;

/**
 * Data Transfer Object utilizado para mostrar en el Monitor de Riesgos la informaci&oacute;n de
 * cada mesa de cambios. Esta informaci&oacute;n incluye el resumen de la Posici&oacute;n y el
 * nivel de los l&iacute;mites de riesgo definidos.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1 $ $Date: 2008/10/27 23:20:10 $
 */
public class PosicionMesaDto implements Serializable {

    /**
     * Constructor por default.
     */
    public PosicionMesaDto() {
        super();
        divisaSeleccionada =  getDivisaTodas();
    }

    /**
     * Crea un objeto Divisa con idDivisa y descripci&oacute;n '*todas*'.
     *
     * @return Divisa.
     */
    private Divisa getDivisaTodas() {
        Divisa divisa =  new Divisa();
        divisa.setIdDivisa(TODAS_STR);
        divisa.setDescripcion(TODAS_STR);
        return divisa;
    }

    /**
     * Regresa el valor de mesaCambio.
     *
     * @return MesaCambio.
     */
    public MesaCambio getMesaCambio() {
        return mesaCambio;
    }

    /**
     * Establece el valor de mesaCambio.
     *
     * @param mesaCambio El valor a asignar.
     */
    public void setMesaCambio(MesaCambio mesaCambio) {
        this.mesaCambio = mesaCambio;
    }

    /**
     * Regresa el valor de posiciones.
     *
     * @return List.
     */
    public List getPosiciones() {
        return posiciones;
    }

    /**
     * Establece el valor de posiciones.
     *
     * @param posiciones El valor a asignar.
     */
    public void setPosiciones(List posiciones) {
        this.posiciones = posiciones;
    }

    /**
     * Regresa el valor de divisaSeleccionada.
     *
     * @return Divisa.
     */
    public Divisa getDivisaSeleccionada() {
        return divisaSeleccionada;
    }

    /**
     * Establece el valor de divisaSeleccionada.
     *
     * @param divisaSeleccionada El valor a asignar.
     */
    public void setDivisaSeleccionada(Divisa divisaSeleccionada) {
        this.divisaSeleccionada = divisaSeleccionada;
    }

    /**
     * La lista de objetos Divisa que est&aacute;n activos en la mesa de cambios.
     *
     * @return divisasDisponibles.
     */
    public List getDivisasDisponibles() {
        List divisasDisponibles = new ArrayList();
        divisasDisponibles.add(getDivisaTodas());
        for (Iterator it = posiciones.iterator(); it.hasNext();) {
            TotalPosicionDto posicionDto = (TotalPosicionDto) it.next();
            if (!posicionDto.getDivisa().getIdDivisa().equals("Total:")) {
                divisasDisponibles.add(posicionDto.getDivisa());
            }
        }
        return divisasDisponibles;
    }

    /**
     * Regresa una lista con los registros de posicion.
     *
     * @return List de TotalPosicionDto.
     */
    public List getRegistrosPosicion() {
        List pos = new ArrayList();
        for (Iterator it = posiciones.iterator(); it.hasNext();) {
            TotalPosicionDto dto = (TotalPosicionDto) it.next();
            if (dto.getPosicion() != null) {
                pos.add(dto.getPosicion());
            }
        }
        return pos;
    }

    /**
     * Regresa una lista con los registros de posicion que corresponden a la divisa especificada.
     *
     * @param idDivisa La divisa a encontrar.
     * @return List de Posicion.
     */
    public Posicion getRegistroPosicionParaDivisa(String idDivisa) {
        for (Iterator it = getRegistrosPosicion().iterator(); it.hasNext();) {
            Posicion pos = (Posicion) it.next();
            if (pos.getDivisa().getIdDivisa().equals(idDivisa)) {
                return pos;
            }
        }
        return null;
    }

    /**
     * Regresa el valor de limites.
     *
     * @return List de Map.
     */
    public List getLimites() {
        return limites;
    }

    /**
     * Establece el valor de limites.
     *
     * @param limites El valor a asignar.
     */
    public void setLimites(List limites) {
        this.limites = limites;
    }

    /**
     * La mesa de cambio a la que corresponde el resto de la informaci&oacute;n.
     */
    private MesaCambio mesaCambio;

    /**
     * La lista de objetos TotalPosicionDto que contienen la informaci&oacute;n de los registros de
     * la posici&oacute;n de la mesa de cambios.
     */
    private List posiciones = new ArrayList();

    /**
     * La divisa para la cual se mostrar&aacute;n los l&iacute;mites.
     */
    private Divisa divisaSeleccionada;

    /**
     * La lista de l&iacute;mites de Riesgo para la mesa de cambios.
     */
    private List limites = new ArrayList();

    /**
     * Constante para la cadena '*todas*'.
     */
    public static final String TODAS_STR = "*todas*";

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -4990602013836552967L;
}

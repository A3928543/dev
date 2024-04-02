/*
 * $Id: IPlantillaTranNacional.java,v 1.14 2010/03/03 21:16:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

/**
 * Interfaz Proxy para la clase persistente PlantillaTranNacional.
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.14 $ $Date: 2010/03/03 21:16:04 $
 */
public interface IPlantillaTranNacional extends IPlantilla {

    /**
     * Regresa true si el mnem&oacute;nico contiene la palabra 'SPEI'.
     *
     * @return boolean.
     */
    boolean isSpei();

    /**
     * Regresa true si el mnem&oacute;nico es 'EMXNSPEIBCVO'.
     *
     * @return boolean.
     */
    boolean isBancoVostroSpei();

    /**
     * Regresa true si el mnem&oacute;nico es 'EMXNSPEIBCTC'.
     *
     * @return boolean.
     */
    boolean isBancoBancoSpei();

    /**
     * Regresa true si el mnem&oacute;nico es 'EMXNSPEIIXEB'.
     *
     * @return boolean.
     */
    boolean isBancoTerceroSpei();

    /**
     * Regresa el valor de Clabe.
     *
     * @return String.
     */
    String getClabe();

    /**
     * Fija el valor de clabe.
     *
     * @param clabe El valor a asignar.
     */
    void setClabe(String clabe);

    /**
     * Regresa el valor de nombreBanco.
     *
     * @return String.
     */
    String getNombreBanco();

    /**
     * Fija el valor de nombreBanco.
     *
     * @param nombreBanco El valor a asignar.
     */
    void setNombreBanco(String nombreBanco);

    /**
     * Regresa el valor de idBanco.
     *
     * @return Long.
     */
    Long getIdBanco();

    /**
     * Fija el valor de idBanco.
     *
     * @param idBanco El valor a asignar.
     */
    void setIdBanco(Long idBanco);

    /**
     * Regresa el valor de divisa.
     *
     * @return Divisa.
     */
    Divisa getDivisa();

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    void setDivisa(Divisa divisa);
    
}
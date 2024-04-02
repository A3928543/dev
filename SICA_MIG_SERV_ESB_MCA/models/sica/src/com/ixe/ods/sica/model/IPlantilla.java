/*
 * $Id: IPlantilla.java,v 1.12 2008/02/22 18:25:19 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.model;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.Persona;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Interface Proxy para las Plantillas.
 * 
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:19 $
 */
public interface IPlantilla extends Serializable {

    /**
     * Regresa la persona ligada a los rol 'TIT' del contratoSica.
     *
     * @return Persona.
     */
    Persona getCliente();

    /**
     * Indica si la Plantilla puede Procesarse.
     * @return boolean.
     */
    boolean puedeProcesarse();

    /**
     * Regresa el Tipo de Plantilla.
     * @return int.
     */
    int getTipo();

    /**
     * Regresa el valor de idPlantilla.
     *
     * @return int.
     */
    int getIdPlantilla();

    /**
     * Fija el valor de idPlantilla.
     *
     * @param idPlantilla El valor a asignar.
     */
    void setIdPlantilla(int idPlantilla);

    /**
    * Regresa el valor de clavePlantilla.
    *
    * @return String.
    */
    String getClavePlantilla();

    /**
     * Fija el valor de clavePlantilla.
     *
     * @param clavePlantilla El valor a asignar.
     */
    void setClavePlantilla(String clavePlantilla);

    /**
     * Regresa el valor de internacional.
     *
     * @return String.
     */
    String getInternacional();

    /**
     * Fija el valor de internacional.
     *
     * @param internacional El valor a asignar.
     */
    void setInternacional(String internacional);

    /**
     * Regresa el valor de mnemonico.
     *
     * @return String.
     */
    String getMnemonico();

    /**
     * Fija el valor de mnemonico.
     *
     * @param mnemonico El valor a asignar.
     */
    void setMnemonico(String mnemonico);

    /**
     * Regresa el valor de statusPlantilla.
     *
     * @return String.
     */
    String getStatusPlantilla();

    /**
     * Fija el valor de statusPlantilla.
     *
     * @param statusPlantilla El valor a asignar.
     */
    void setStatusPlantilla(String statusPlantilla);

    /**
     * Regresa el valor de ultimaModificacion.
     *
     * @return Date.
     */
    Date getUltimaModificacion();

    /**
     * Fija el valor de ultimaModificacion.
     *
     * @param ultimaModificacion El valor a asignar.
     */
    void setUltimaModificacion(Date ultimaModificacion);

    /**
     * Regresa el valor de contratoSica.
     *
     * @return ContratoSica.
     */
    ContratoSica getContratoSica();

    /**
     * Fija el valor de contratoSica.
     *
     * @param contratoSica El valor a asignar.
     */
    void setContratoSica(ContratoSica contratoSica);

    /**
     * Regresa La Informacion Adicional.
     * 
     * @return List.
     */
    List getInfoAdicional();
    
    /**
     * Regresa el valor de beneficiario.
     * 
     * @return Persona.
     */
    Persona getBeneficiario();
    
    /**
     * Fija el valor de beneficiario.
     * 
     * @param beneficiario El valor a asignar.
     */
    void setBeneficiario(Persona beneficiario);
}
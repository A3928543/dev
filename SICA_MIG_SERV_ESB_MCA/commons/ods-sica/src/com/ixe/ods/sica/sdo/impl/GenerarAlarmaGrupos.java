/*
 * $Id: GenerarAlarmaGrupos.java,v 1.12 2008/02/22 18:25:48 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.sdo.impl;

import com.ixe.ods.sica.sdo.SicaServiceData;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edgar Leija
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:48 $
 */
public class GenerarAlarmaGrupos {

	/**
	 * Constructor por default.
	 */
    public GenerarAlarmaGrupos() {
        super();
    }

    /**
     * Asigna el valor para SicaServiceData y carga los grupos para las alarmas.
     * 
     * @param sicaServiceData El serviceData del SICA. 
     */
    public void setSicaServiceData(SicaServiceData sicaServiceData) {
        _sicaServiceData = sicaServiceData;
        cargarGrupos();
    }
    
    /**
     * Carga los grupos.
     */
    public void cargarGrupos() {
    }
    
    /**
     * Verifica el deal.
     * 
     * @param idDeal El id del deal.
     */
    public void checarDeal(int idDeal) {
    }

    /**
     * Obtiene el valor de gruposAlarma.
     * 
     * @return List
     */
    public List getGruposAlarma() {
        return gruposAlarma;
    }

    /**
     * Obtiene el de grupos.
     * 
     * @return List
     */
    public List getGrupos() {
        return grupos;
    }

    /**
     * Asigna el valor para grupos.
     * 
     * @param grupos El valor para grupos.
     */
    public void setGrupos(List grupos) {
        this.grupos = grupos;
    }

    /**
     * Clase de servicios del SICA
     */
    private SicaServiceData _sicaServiceData;
    
    /**
     * Los gurpos.
     */
    private List grupos = new ArrayList();
    
    /**
     * Los grupos para alarmas.
     */
    private List gruposAlarma = new ArrayList();
}

/*
 * $Id: WorklistVO.java,v 1.11 2008/02/22 18:25:24 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.vo;

import java.util.List;
import java.io.Serializable;

/**
 * Value Object que representa un WorkList para la aplicaci&oacute;n de Autorizaciones en Flex; 
 * este objeto almacena la lista de deals pendientes (WorkItemVO) por a autorizaciones. 
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:24 $
 */
public class WorklistVO implements Serializable {

	/**
	 * Constructor por Default.
	 *
	 */
    public WorklistVO() {
        super();
    }

    /**
     * Constructor para inicializar todas las variables de instancia.
     * 
     * @param tipo El tipo de autorizaci&oacute;n.
     * @param workitems La lista de deals (WorkItems) pendientes por autorizaci&oacute;n.
     */
    public WorklistVO(String tipo, List workitems) {
        this.tipo = tipo;
        this.workitems = workitems;
    }

    /**
     * Obtiene el valor de tipo.
     * 
     * @return String
     */
    public String getTipo() {
        return tipo;
    }
    
    /**
     * Asigna el valor para tipo.
     * 
     * @param tipo El valor para tipo.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    /**
     * Obtiene el valor de workItems.
     *  
     * @return List
     */
    public List getWorkitems() {
        return workitems;
    }
    
    /**
     * Asigna el valor para workItems.
     * 
     * @param workitems El valor para workItems.
     */
    public void setWorkitems(List workitems) {
        this.workitems = workitems;
    }
    
    /**
     * El tipo autorizaci&oaute;n del deal.
     */
    private String tipo;
    
    /**
     * La lista de deals pendientes por autorizar.
     */
    private List workitems;
}

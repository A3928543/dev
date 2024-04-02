/*
 * $Id: UtilidadGlobalVO.java,v 1.11 2008/02/22 18:25:26 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */
package com.ixe.ods.sica.posicion.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/**
 * Value Object para UtilidadGlobal para la comunicaci&oacute;n del SICA con las applicaciones en Flex (Monitor de la Posici&oacute;n).
 * 
 * @author rchavez
 */
public class UtilidadGlobalVO implements Serializable {
	
	/**
	 * Constructor por Default. 
	 *
	 */
    public UtilidadGlobalVO() {
        divisas = new ArrayList();
    }

    /**
     * Devuelve la utilidad global por valuaci&oacute;n.
     * 
     * @return Ka utilidad global por valuaci&oacute;n.
     */
    public BigDecimal getUtilidadGlobalMn() {
        return utilidadGlobalMn;
    }

    /**
     * Asigna el valor para utilidadGlobalMn.
     *
     * @param utilidadGlobalMn El valor para utilidadGlobalMn
     */
    public void setUtilidadGlobalMn(BigDecimal utilidadGlobalMn) {
        this.utilidadGlobalMn = utilidadGlobalMn;
    }

    /**
     * Devuelve el valor de la lista de divisas.
     *
     * @return List
     */
    public List getDivisas() {
        return divisas;
    }

    /**
     * Asigna el valor para divisas.
     *
     * @param divisas El valor para divisas.
     */
    public void setDivisas(List divisas) {
        this.divisas = divisas;
    }

    /**
     * Utilidad global en Moneda Nacional
     */
    private BigDecimal utilidadGlobalMn;
    
    /**
     * La lista de la utilidad por valuaci&oacute;n de cada una de las divisas.
     */
    private List divisas;
}

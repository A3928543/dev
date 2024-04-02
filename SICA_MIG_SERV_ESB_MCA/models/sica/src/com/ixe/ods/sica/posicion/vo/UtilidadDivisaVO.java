/*
 * $Id: UtilidadDivisaVO.java,v 1.11 2008/02/22 18:25:25 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */
package com.ixe.ods.sica.posicion.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Value Object para UtilidadDivisa para la comunicaci&oacute;n del SICA con las applicaciones en Flex (Monitor de la Posici&oacute;n).
 * 
 * @author Jean C. Favila
 */
public class UtilidadDivisaVO implements Serializable {
	
	/**
	 * Constructor por Default.
	 *
	 */
    public UtilidadDivisaVO() {
    }

    /**
	 * Constructor que inicializa los valores para VO.
	 * 
	 * @param idDivisa El id de la divisa.
	 * @param utilidad El valor de la utilidad.
	 */
	public UtilidadDivisaVO(String idDivisa, BigDecimal utilidad) {
		_idDivisa = idDivisa;
		_utilidad = new BigDecimal(utilidad + "");
	}

    /**
     * Devuelve el identificador de la divisa.
     * @return el identificador de la divisa.
     */
    public String getIdDivisa() {
        return _idDivisa;
    }

    /**
     * Devuelve la utilidad por valuaci&oacute;n.
     * @return la utilidad por valuaci&oacute;n.
     */
    public BigDecimal getUtilidad() {
        return _utilidad;
    }
    
    /**
     * El id de la divisa.
     */
    private String _idDivisa;
    
    /**
     * El monto de la utilidad.
     */
	private BigDecimal _utilidad;
}
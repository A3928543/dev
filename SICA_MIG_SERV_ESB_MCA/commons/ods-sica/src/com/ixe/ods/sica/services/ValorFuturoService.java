/*
 * $Id: ValorFuturoService.java,v 1.15 2009/11/17 16:44:11 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;

import java.util.Date;

/**
 * Componente para determinar si una fecha es o no festiva en Estados Unidos para considerar la
 * Fecha Valor Futuro.
 *
 * @author Gustavo Gonz&aacute;lez, Jean C. Favila
 * @version $Revision: 1.15 $ $Date: 2009/11/17 16:44:11 $
 */
public interface ValorFuturoService {

    /**
     * Regresa true si la fecha especificada es festiva en Estados Unidos.
     *
     * @param fecha Una fecha h&aacute;bil en M&eacute;xico.
     * @return boolean.
     */
    boolean esFestivaEnEua(Date fecha);
    
    /**
     * Valida que si la fecha valor es a 72Hrs, el sector econ&oacute;mico del cliente del deal debe
     * poder operar en la &uacute;ltima fecha valor disponible para la fecha actual del sistema.
     *
     * @param fechaValorDeal La fecha de valor del deal, se pide por separado porque puede ser que
     *      el deal no la tenga asignada.
     * @param deal El deal a revisar.
     * @param valorFuturo Si se puede o no operar a 96Hrs.
     * @throws com.ixe.ods.sica.SicaException Si el deal es a 72HRs y el sector econ&oacute;mico no
     *      puede operarlas.
     */
    void validarSectorEconomicoPorFechaValor(String fechaValorDeal, Deal deal, boolean valorFuturo)
            throws SicaException;

    /**
     * Limpia el cach&eacute; de fechas inh&aacute;biles en Estados Unidos.
     */
    void limpiarCache();
}


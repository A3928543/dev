/*
 * $Id: ValorFuturoServiceImpl.java,v 1.17 2010/04/13 17:54:47 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.ixe.ods.bup.model.SectorEconomico;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.FechaInhabilEua;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.ValorFuturoService;
import com.ixe.ods.sica.utils.DateUtils;

/**
 * Implementaci&oacute;n de la interfaz ValorFuturoService.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.17 $ $Date: 2010/04/13 17:54:47 $
 * @see com.ixe.ods.sica.services.ValorFuturoService
 */
public class ValorFuturoServiceImpl implements ValorFuturoService {

    /**
     * Constructor por default.
     */
    public ValorFuturoServiceImpl() {
        super();
    }

    /**
     * Refresca la lista de fechasInhabilesEua.
     */
    public void limpiarCache() {
        fechasInhabilesEua = null;
        getFechasInhabilesEua();
    }

    /**
     * Regresa la lista de fechas inh&aacute;biles en EUA, llen&aacute;ndola si es necesario.
     *
     * @return List de 
     */
    private synchronized List getFechasInhabilesEua() {
        if (fechasInhabilesEua == null) {
            fechasInhabilesEua = sicaServiceData.findFechasInhabilesEua();
        }
        return fechasInhabilesEua;
    }

    /**
     * @param fecha Una fecha h&aacute;bil en M&eacute;xico.
     * @return boolean.
     * @see com.ixe.ods.sica.services.ValorFuturoService#esFestivaEnEua(java.util.Date).
     */
    public boolean esFestivaEnEua(Date fecha) {
        for (Iterator it = getFechasInhabilesEua().iterator(); it.hasNext();) {
            FechaInhabilEua fechaInhabilEua = (FechaInhabilEua) it.next();
            if (DateUtils.inicioDia(fecha).equals(
                    DateUtils.inicioDia(fechaInhabilEua.getIdFechaInhabilEua().getFecha()))) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Regresa true si el sector econ&oacute;mico del cliente del deal se encuentra entre los que
     * pueden operar a 72HR.
     *
     * @param fechaValorDeal La fecha de valor del deal, se pide por separado porque puede ser que
     *  el deal no la tenga asignada
     * @param deal El deal a revisar.
     * @param valorFuturo Si se puede o no operar a 96Hrs.
     * @return boolean.
     */
    private boolean isDealValidoParaUltimaFechaValor(String fechaValorDeal, Deal deal,
                                                     boolean valorFuturo) {
        if (deal.getContratoSica() == null) {
            return true;
        }
        String fechaValorRestringida = valorFuturo ? Constantes.VFUT : Constantes.HR72;
        if (!fechaValorRestringida.equals(fechaValorDeal)) {
            return true;
        }
        List sectoresUltimaFechaValor = sicaServiceData.findAllSectoresUltimaFechaValor();
        SectorEconomico sectorCliente = deal.getCliente().getSectorEconomico();
        for (Iterator it = sectoresUltimaFechaValor.iterator(); it.hasNext();) {
            SectorEconomico sector = (SectorEconomico) it.next();
            if (sectorCliente.getIdSector().equals(sector.getIdSector())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param fechaValorDeal La fecha de valor del deal, se pide por separado porque puede ser que
     *      el deal no la tenga asignada.
     * @param deal El deal a revisar.
     * @param valorFuturo Si se puede o no operar a 96Hrs.
     * @throws com.ixe.ods.sica.SicaException Si el deal es a 72HRs y el sector econ&oacute;mico no
     *      puede operarlas.
     * @see com.ixe.ods.sica.services.ValorFuturoService#validarSectorEconomicoPorFechaValor(String,
            com.ixe.ods.sica.model.Deal, boolean).
     */
    public void validarSectorEconomicoPorFechaValor(String fechaValorDeal, Deal deal,
                                                    boolean valorFuturo) throws SicaException {
        if (deal.getCliente() != null && fechaValorDeal != null
                && ! isDealValidoParaUltimaFechaValor(fechaValorDeal, deal, valorFuturo)) {
            throw new SicaException("El cliente pertenece al sector econ\u00f3mico " +
                    deal.getCliente().getSectorEconomico().getDescripcion() +
                    ". Este sector no puede operar la fecha valor " +  fechaValorDeal +
                    " en este momento.");
        }
    }

    /**
     * Establece el valor de sicaServiceData.
     *
     * @param sicaServiceData El valor a asignar.
     */
    public void setSicaServiceData(SicaServiceData sicaServiceData) {
        this.sicaServiceData = sicaServiceData;
    }

    /**
     * El cach&eacute; de registros de la tabla SC_FECHA_INHABIL_EUA.
     */
    private List fechasInhabilesEua;

    /**
     * La referencia al service data, para realizar operaciones a la base de datos del SICA.
     */
    private SicaServiceData sicaServiceData;
}